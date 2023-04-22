package com.pe.lima.sg.rs.reporte;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pe.lima.sg.bean.reporte.MorosoBean;
import com.pe.lima.sg.presentacion.Filtro;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class MorosoDao {
	
	
	@Value("${spring.datasource.url}")
	private String urlTienda;
	
	@Value("${spring.datasource.username}")
	private String nombreUsuario;
	
	@Value("${spring.datasource.password}")
	private String credencialUsuario;
	
	private final String userUrl	= "?user=";
	private final String userPass	= "&password=";
	
	private String url;
	
	/*Obtenemos los datos del reporte de morososo*/
	public List<MorosoBean> getReporteMoroso(Filtro filtro){

		MorosoBean morosoBean 			= null;
		List<MorosoBean> listaMoroso 	= null;
		Connection con 					= null;
		PreparedStatement pstmt 		= null;
		ResultSet rs 					= null;
		try{
			log.debug("[getReporteMoroso] Inicio");
			url = urlTienda + userUrl + nombreUsuario + userPass + credencialUsuario;
			con = DriverManager.getConnection(url);
			//Dependiendo el tipo de reporte se obtiene el pstmt
			pstmt = this.sqlConsulta(filtro, con);
			
			rs = pstmt.executeQuery();

			if (rs !=null){
				listaMoroso = new ArrayList<MorosoBean>();
				while(rs.next()){
					morosoBean = new MorosoBean();
					morosoBean.setNombreInmueble(rs.getString("inmueble"));
					morosoBean.setNumeroTienda(rs.getString("tienda"));
					morosoBean.setNombreCliente(rs.getString("nombre"));
					morosoBean.setVencimiento(rs.getString("vencimiento"));
					morosoBean.setTotal(rs.getBigDecimal("total"));
					morosoBean.setSaldo(rs.getBigDecimal("saldo"));
					morosoBean.setTipoMoneda(rs.getString("tipo_moneda"));
					morosoBean.setTipoReferencia(rs.getString("tipo_referencia"));
					
					listaMoroso.add(morosoBean);
				}
			}

		}catch(Exception e){
			e.printStackTrace();
			listaMoroso = null;
		}finally{
			morosoBean = null;
			rs = null;
			pstmt = null;
			//query = null;
			try{
				if (con !=null && !con.isClosed()){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
				con = null;
			}
		}
		log.debug("[getReporteMoroso] Fin");
		return listaMoroso;
	}
	
	

	private String sqlMoroso(){
		String query = null;
		query =	"SELECT  "+
			    "    	   edi.nombre as inmueble, "+
			    "    	   tie.numero as tienda,  "+
			    "          to_char(cxc.fecha_fin,'yyyy') || '-' || upper(to_char(cxc.fecha_fin,'TMmonth')) as vencimiento, "+
			    "           case when per.razon_social is null or per.razon_social = '' THEN "+
			    "          		     per.nombre || ' ' || per.paterno || ' '  || per.materno  "+
			    "                else  per.razon_social "+
				"	       end as nombre,  "+
			    "           cxc.monto_contrato as total, "+
				"	       cxc.saldo,   "+
				"	       cxc.tipo_moneda, "+
			    "           cxc.tipo_referencia "+
				"	 FROM caj.tbl_cxc_documento cxc,   "+
				"		 cli.tbl_contrato con,   "+
				"	    mae.tbl_persona per,   "+
				"	    mae.tbl_tienda tie, "+
			    "        mae.tbl_edificio edi   "+
				"	 WHERE cxc.codigo_contrato = con.codigo_contrato   "+
				"	    AND con.codigo_tienda = tie.codigo_tienda   "+
			    "        AND tie.codigo_edificio = edi.codigo_edificio "+
				"	    AND con.codigo_persona = per.codigo_persona   "+
				"	    AND con.estado = '1'   "+
				"	    AND con.estado_contrato in ('PND', 'VGN')  "+
				"	    AND cxc.saldo > 0   "+
			    "        AND cxc.tipo_referencia = ?   "+
			    "        AND edi.codigo_edificio = ? "+
				"	    AND cxc.fecha_fin < ? :: date   "+
				"	 ORDER BY edi.nombre asc, tie.numero asc,  cxc.fecha_fin  desc ";
		return query;
	}
	
	private String sqlMorosoArbitrio(){
		String query = null;
		query =	"SELECT " + 
				"       edi.nombre as inmueble, " + 
				"       tie.numero as tienda,   " + 
				"       to_char(cxc.fecha_fin,'yyyy') || '-' || upper(to_char(cxc.fecha_fin,'TMmonth')) as vencimiento, " + 
				"       case when per.razon_social is null or per.razon_social = '' THEN  " + 
				"                 per.nombre || ' ' || per.paterno || ' '  || per.materno   " + 
				"            else  per.razon_social  " + 
				"       end as nombre,   " + 
				"       cxc.monto_generado as total, " + 
				"       cxc.saldo,    " + 
				"       'SO' as tipo_moneda, " + 
				"       'ARB' AS tipo_referencia  " + 
				" FROM cli.tbl_arbitrio cxc,    " + 
				"     cli.tbl_contrato con,    " + 
				"    mae.tbl_persona per,    " + 
				"    mae.tbl_tienda tie, " + 
				"    mae.tbl_edificio edi    " + 
				" WHERE cxc.codigo_contrato = con.codigo_contrato    " + 
				"    AND con.codigo_tienda = tie.codigo_tienda    " + 
				"    AND tie.codigo_edificio = edi.codigo_edificio  " + 
				"    AND con.codigo_persona = per.codigo_persona    " + 
				"    AND con.estado = '1'    " + 
				"    AND con.estado_contrato in ('PND', 'VGN')   " + 
				"    AND cxc.saldo > 0    " + 
				"    AND 'ARB' = ? " + 
				"    AND edi.codigo_edificio = ?  " + 
				"    AND cxc.fecha_fin < ? :: date    " + 
				" ORDER BY edi.nombre asc, tie.numero asc,  cxc.fecha_fin  desc";
		return query;
	}
	/*Preparamos el sql para ser ejecutado*/
	private PreparedStatement sqlConsulta(Filtro filtro, Connection con) throws SQLException{
		String query = null;
		PreparedStatement pstmt = null;
		log.debug("[sqlConsulta] Inicio Tipo:"+filtro.getTipoCobro()+" Edificio:"+filtro.getCodigoEdificacion()+" Fecha:"+filtro.getFechaFin());
		//query = this.sqlMoroso();
		query = this.getSQLMoroso(filtro);
		pstmt = con.prepareStatement(query);
		pstmt.setString(1, filtro.getTipoCobro());
		pstmt.setInt(2,  filtro.getCodigoEdificacion());
		pstmt.setString(3,  filtro.getFechaFin());
		
		log.debug("[sqlConsulta] query:"+query);
		log.debug("[sqlConsulta] Fin");
		return pstmt;
	}
	
	/*Identifica el SQL a ejecutarse*/
	private String getSQLMoroso(Filtro filtro) {
		if (filtro.getTipoCobro().equals("ARB")) {
			return this.sqlMorosoArbitrio();
		}else {
			return this.sqlMoroso();
		}
	}
}
