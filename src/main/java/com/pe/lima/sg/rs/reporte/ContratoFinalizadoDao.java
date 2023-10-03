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

import com.pe.lima.sg.bean.reporte.LocalBean;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class ContratoFinalizadoDao {
	
	@Value("${spring.datasource.url}")
	private String urlTienda;
	
	@Value("${spring.datasource.username}")
	private String nombreUsuario;
	
	@Value("${spring.datasource.password}")
	private String credencialUsuario;
	
	private final String userUrl	= "?user=";
	private final String userPass	= "&password=";
	
	private String url;
	
	/*Obtenemos los datos del reporte de Local por Inmueble*/
	public List<LocalBean> getReporteContratoFinalizado(Filtro filtro){
		List<LocalBean> listaLocal	 	= null;
		Connection con 					= null;
		PreparedStatement pstmt 		= null;
		ResultSet rs 					= null;
		try{
			log.debug("[getReporteContratoFinalizado] Inicio");
			url = urlTienda + userUrl + nombreUsuario + userPass + credencialUsuario;
			con = DriverManager.getConnection(url);
			//Obtenemos los contratos finalizados
			pstmt = this.sqlConsultaContratoFinalizado(filtro, con);
			rs = pstmt.executeQuery();
			listaLocal = setDatosContratoFinalizado(rs);
			
		}catch(Exception e){
			e.printStackTrace();
			listaLocal = null;
		}finally{
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
		return listaLocal;
	}
	
	private List<LocalBean> setDatosContratoFinalizado(ResultSet rs) throws SQLException{
		List<LocalBean> listaLocal	 	= null;
		LocalBean localBean 			= null;
		if (rs !=null){
			log.debug("[setDatosContratoFinalizado] Inicio");
			listaLocal = new ArrayList<>();
			while(rs.next()){
				localBean = new LocalBean();
				localBean.setNombreInmueble(rs.getString("inmueble"));
				localBean.setNumeroTienda(rs.getString("tienda"));
				localBean.setRucCliente(rs.getString("ruc"));
				localBean.setNombreCliente(rs.getString("nombre"));
				localBean.setEstado(Constantes.ESTADO_FINALIZADO);
				localBean.setMontoAlquiler(rs.getBigDecimal("monto_alquiler"));
				localBean.setMontoServicio(rs.getBigDecimal("servicio"));
				localBean.setFechaFinContrato(rs.getString("fecha_fin"));
				localBean.setFechaInicioContrato(rs.getString("fecha_inicio"));
				localBean.setMontoGarantia(rs.getBigDecimal("monto_garantia"));
				localBean.setFechaFinalizacion(rs.getString("fecha_finalizacion"));
				listaLocal.add(localBean);
			}
			log.debug("[setDatosContratoFinalizado] Fin:"+listaLocal.size());
			
		}
		return listaLocal;
	}


	
	/*Obtenemos los montos de alquiler, servicio y el monto del contrato para los contratos finalizados*/
	private PreparedStatement sqlConsultaContratoFinalizado(Filtro filtro, Connection con) throws SQLException{
		String query = null;
		PreparedStatement pstmt = null;
		log.debug("[sqlConsultaContratoFinalizado] Inicio");
		query = this.queryContratoFinalizado();
		pstmt = con.prepareStatement(query);
		pstmt.setString(1, filtro.getFechaInicio());
		pstmt.setString(2,  filtro.getFechaFin());
		log.debug("[sqlConsultaContratoFinalizado] query:"+query);
		log.debug("[sqlConsultaContratoFinalizado] Fin");
		return pstmt;
	}
	private String queryContratoFinalizado(){
		String query = null;
		query = "SELECT    " + 
				"   con.codigo_contrato," + 
				"   edi.nombre as inmueble,   " + 
				"   tie.numero as tienda,    " + 
				"   case when per.numero_ruc is null or TRIM(per.numero_ruc) = '' THEN ' '   " + 
				"         else  per.numero_ruc   " + 
				"    end as ruc,    " + 
				"    case when per.razon_social is null or TRIM(per.razon_social) = '' THEN   " + 
				"             per.nombre || ' ' || per.paterno || ' '  || per.materno    " + 
				"         else  per.razon_social   " + 
				"    end as nombre,   " + 
				"    'FINALIZADO'  as estado," + 
				"    TO_CHAR(con.fecha_inicio,'dd/mm/yyyy') as fecha_inicio,    " + 
				"    TO_CHAR(con.fecha_fin,'dd/mm/yyyy') as fecha_fin,    " + 
				"    con.monto_alquiler," + 
				"    coalesce(" + 
				"        (select ser.monto" + 
				"        from cli.tbl_contrato_servicio ser" + 
				"        where ser.codigo_contrato = con.codigo_contrato" + 
				"        LIMIT 1), 0" + 
				"    ) as servicio,   " + 
				"    con.monto_garantia," + 
				"    con.fecha_finalizacion   " + 
				" FROM cli.tbl_contrato con,     " + 
				"  mae.tbl_persona per,     " + 
				"  mae.tbl_tienda tie,   " + 
				"  mae.tbl_edificio edi     " + 
				" WHERE  con.codigo_tienda = tie.codigo_tienda     " + 
				"  AND tie.codigo_edificio = edi.codigo_edificio   " + 
				"  AND con.codigo_persona = per.codigo_persona     " + 
				"  AND con.estado = '1'     " + 
				"  AND con.fecha_finalizacion between ( ? :: date) and ( ? :: date) " +
				" order by inmueble, tienda";
		return query;
	}
	
	
}
