package com.pe.lima.sg.rs.reporte;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pe.lima.sg.bean.reporte.IngresoEgresoBean;
import com.pe.lima.sg.presentacion.Filtro;
//import java.util.Properties;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.UtilSGT;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class IngresoEgresoDao {
	
	@Value("${spring.datasource.url}")
	private String urlTienda;
	
	@Value("${spring.datasource.username}")
	private String nombreUsuario;
	
	@Value("${spring.datasource.password}")
	private String credencialUsuario;
	
	private final String userUrl	= "?user=";
	private final String userPass	= "&password=";
	
	private String url;
	
	
	
	
	private String sqlIngreso(String tipoPago) throws SQLException{
		String query = null;
		
		query = "SELECT usu.nombre, "+
				"	    tie.numero, "+
				"	    per.numero_ruc, "+
				"       des.tipo_cambio, "+
				"       des.tipo_moneda, "+
				"       des.monto_soles, "+
				"       des.monto_dolares, "+
				"       des.fecha_cobro, "+
				"		des.nota, "+
				"		des.tipo_cobro, "+
				"       des.tipo_pago, des.tipo_bancarizacion,  des.numero_operacion, des.fecha_operacion, '' AS descripcion "+
				" FROM caj.tbl_desembolso des, "+
				"	seg.tbl_usuario usu, "+
				"    cli.tbl_contrato con, "+
				"    mae.tbl_persona per, "+
				"    mae.tbl_tienda tie "+
				" WHERE des.usuario_creacion = usu.codigo_usuario "+
				"    and des.codigo_contrato = con.codigo_contrato "+
				"    and con.codigo_tienda = tie.codigo_tienda "+
				"    and con.codigo_persona = per.codigo_persona "+
				"    and des.estado_operacion not in (3) "+
				"    and des.fecha_cobro between  ( ? :: date) and ( ? :: date) "+
				"    and (des.tipo_pago is null or des.tipo_pago = 'EFE') "+
				//"    and des.fecha_cobro = des.fecha_creacion:: DATE "+
				"    and tie.codigo_edificio = ? "; 


				
		query = query + " UNION " +
				"select usu.nombre, "+
				"	   'INGRESO' as numero, "+
				"	    '' as numero_ruc, "+
				"       0 as tipo_cambio, "+
				"       ing.tipo_moneda, "+
				"       ing.monto as monto_soles, "+
				"       ing.monto as monto_dolares, "+
				"       ing.fecha_ingreso as fecha_cobro, "+
				"       ing.observacion as nota, "+
				"       'INGRESO' as tipo_cobro, "+
				"       ing.tipo_pago, ing.tipo_bancarizacion,  ing.numero_operacion, ing.fecha_operacion, con.nombre 	 AS descripcion "+
				"from caj.tbl_ingreso ing, "+
				"	  seg.tbl_usuario usu,     "+
				"     mae.tbl_concepto con "+
				"where ing.usuario_creacion = usu.codigo_usuario    "+
				"	and ing.estado = '1' "+
				"   and con.codigo_concepto = ing.codigo_tipo_ingreso "+
				"    and (ing.tipo_pago is null or ing.tipo_pago = 'EFE') "+
				"   and ing.fecha_ingreso between  ( ? :: date) and ( ? :: date) "+
				"   and ing.codigo_edificio = ?";
		
		
		return query;
	}
	
	private String sqlIngresoBancarizado(String tipoPago) throws SQLException{
		String query = null;
		
		query = "SELECT usu.nombre, "+
				"	    tie.numero, "+
				"	    per.numero_ruc, "+
				"       des.tipo_cambio, "+
				"       des.tipo_moneda, "+
				"       des.monto_soles, "+
				"       des.monto_dolares, "+
				"       des.fecha_cobro, "+
				"		des.nota, "+
				"		des.tipo_cobro, "+
				"       des.tipo_pago, des.tipo_bancarizacion,  des.numero_operacion, des.fecha_operacion, '' AS descripcion "+
				" FROM caj.tbl_desembolso des, "+
				"	seg.tbl_usuario usu, "+
				"    cli.tbl_contrato con, "+
				"    mae.tbl_persona per, "+
				"    mae.tbl_tienda tie "+
				" WHERE des.usuario_creacion = usu.codigo_usuario "+
				"    and des.codigo_contrato = con.codigo_contrato "+
				"    and con.codigo_tienda = tie.codigo_tienda "+
				"    and con.codigo_persona = per.codigo_persona "+
				"    and des.estado_operacion not in (3) "+
				"    and des.fecha_cobro between  ( ? :: date) and ( ? :: date) " +
				"    and (des.tipo_pago = 'BAN')"+
				//"    and des.fecha_cobro = des.fecha_creacion:: DATE "+
				"    and tie.codigo_edificio = ?"; 
		
		query = query + " UNION " +
				"select usu.nombre, "+
				"	   'INGRESO' as numero, "+
				"	    '' as numero_ruc, "+
				"       0 as tipo_cambio, "+
				"       ing.tipo_moneda, "+
				"       ing.monto as monto_soles, "+
				"       ing.monto as monto_dolares, "+
				"       ing.fecha_ingreso as fecha_cobro, "+
				"       ing.observacion as nota, "+
				"       'INGRESO' as tipo_cobro, "+
				"       ing.tipo_pago, ing.tipo_bancarizacion,  ing.numero_operacion, ing.fecha_operacion, con.nombre 	 AS descripcion "+
				"from caj.tbl_ingreso ing, "+
				"	  seg.tbl_usuario usu,     "+
				"     mae.tbl_concepto con "+
				"where ing.usuario_creacion = usu.codigo_usuario    "+
				"	and ing.estado = '1' "+
				"   and con.codigo_concepto = ing.codigo_tipo_ingreso "+
				"   and ing.tipo_pago = 'BAN' "+
				"   and ing.fecha_ingreso between  ( ? :: date) and ( ? :: date) "+
				"   and ing.codigo_edificio = ?";
				
		return query;
	}
	private String sqlCobroFueraFecha() throws SQLException{
		String query = null;
		
		query = "SELECT usu.nombre, "+
				"	    tie.numero, "+
				"       des.tipo_cambio, "+
				"       des.tipo_moneda, "+
				"       des.monto_soles, "+
				"       des.monto_dolares, "+
				"       des.fecha_cobro, "+
				"		des.nota, "+
				"		des.tipo_cobro, "+
				"       des.tipo_pago, des.tipo_bancarizacion,  des.numero_operacion, des.fecha_operacion, "+
				"		des.usuario_creacion,"+
				"		des.fecha_creacion,"+
				"	    per.numero_ruc, "+
				"		tie.codigo_edificio"+
				" FROM caj.tbl_desembolso des, "+
				"	seg.tbl_usuario usu, "+
				"    cli.tbl_contrato con, "+
				"    mae.tbl_persona per, "+
				"    mae.tbl_tienda tie "+
				" WHERE des.usuario_creacion = usu.codigo_usuario "+
				"    and des.codigo_contrato = con.codigo_contrato "+
				"    and con.codigo_tienda = tie.codigo_tienda "+
				"    and con.codigo_persona = per.codigo_persona "+
				"    and des.estado_operacion not in (3) "+
				"    and des.fecha_cobro between  ( ? :: date) and ( ? :: date) " +
				"    and des.fecha_cobro <> des.fecha_creacion:: DATE ";
				
		return query;
	}

	private String sqlEgreso(){
		String query = null;
		query =	"select usu.nombre, "+
				"	   'GASTO' as numero, "+
				"	    '' as numero_ruc, "+
				"       0 as tipo_cambio, "+
				"       gas.tipo_moneda, "+
				"       gas.monto as monto_soles, "+
				"       gas.monto as monto_dolares, "+
				"       gas.fecha_gasto  as fecha_cobro, "+
				"       gas.observacion as nota, "+
				"       'GASTO' as tipo_cobro, "+
				"       '' as tipo_pago, '' as tipo_bancarizacion,  '' as numero_operacion, null as fecha_operacion, con.nombre  AS descripcion "+
				"from caj.tbl_gasto gas, "+
				"	 seg.tbl_usuario usu,     "+
				"    mae.tbl_concepto con   "+
				"where gas.usuario_creacion = usu.codigo_usuario    "+
				"	and gas.estado = '1' "+
				"   and gas.codigo_tipo_gasto = con.codigo_concepto " +
				"   and gas.fecha_gasto between  ( ? :: date) and ( ? :: date) "+
				"   and gas.codigo_edificio = ? ";
		return query;
	}
	
	private PreparedStatement sqlConsulta(Filtro filtro, Connection con) throws SQLException{
		String query = null;
		PreparedStatement pstmt = null;
		if (filtro.getTipoConcepto().equals("E")){
			query = this.sqlEgreso();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, filtro.getFechaInicio());
			pstmt.setString(2,  filtro.getFechaFin());
			pstmt.setInt(3,  filtro.getCodigoEdificacion());
			
		}else if (filtro.getTipoConcepto().equals("I")){
			query = this.sqlIngreso(filtro.getTipoPago());
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, filtro.getFechaInicio());
			pstmt.setString(2, filtro.getFechaFin());
			pstmt.setInt(3,  filtro.getCodigoEdificacion());
			pstmt.setString(4, filtro.getFechaInicio());
			pstmt.setString(5,  filtro.getFechaFin());
			pstmt.setInt(6,  filtro.getCodigoEdificacion());
		} else{
			query = this.sqlIngreso(filtro.getTipoPago()) + " UNION " + this.sqlEgreso();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, filtro.getFechaInicio());
			pstmt.setString(2,  filtro.getFechaFin());
			pstmt.setInt(3,  filtro.getCodigoEdificacion());
			pstmt.setString(4, filtro.getFechaInicio());
			pstmt.setString(5,  filtro.getFechaFin());
			pstmt.setInt(6,  filtro.getCodigoEdificacion());
			pstmt.setString(7, filtro.getFechaInicio());
			pstmt.setString(8,  filtro.getFechaFin());
			pstmt.setInt(9,  filtro.getCodigoEdificacion());
		}
		log.debug(query);
		return pstmt;
	}
	/*Consulta de Cobros Bancarizados*/
	private PreparedStatement sqlConsultaBancarizado(Filtro filtro, Connection con) throws SQLException{
		String query = null;
		PreparedStatement pstmt = null;

		query = this.sqlIngresoBancarizado(filtro.getTipoPago());
		pstmt = con.prepareStatement(query);
		pstmt.setString(1, filtro.getFechaInicio());
		pstmt.setString(2, filtro.getFechaFin());
		pstmt.setInt(3,  filtro.getCodigoEdificacion());
		pstmt.setString(4, filtro.getFechaInicio());
		pstmt.setString(5, filtro.getFechaFin());
		pstmt.setInt(6,  filtro.getCodigoEdificacion());
		log.debug(query);
		return pstmt;
	}
	/*Consulta de Cobros fuera de fecha*/
	private PreparedStatement sqlConsultaFueraFecha(Filtro filtro, Connection con) throws SQLException{
		String query = null;
		PreparedStatement pstmt = null;

		query = this.sqlCobroFueraFecha();
		pstmt = con.prepareStatement(query);
		pstmt.setString(1, filtro.getFechaInicio());
		pstmt.setString(2, filtro.getFechaFin());

		log.debug(query);
		return pstmt;
	}

	public List<IngresoEgresoBean> getReporteIngresoEgreso(Filtro filtro){

		IngresoEgresoBean ingresoEgresoBean = null;
		List<IngresoEgresoBean> listaIngresoEgreso 	= null;
		Connection con = null;
		//String url = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//String query = null;
		try{
			url = urlTienda + userUrl + nombreUsuario + userPass + credencialUsuario;
			con = DriverManager.getConnection(url);
			if (filtro.getTipoPago().equals(Constantes.TIPO_PAGO_COD_BANCARIZADO)){
				pstmt = this.sqlConsultaBancarizado(filtro, con);
			}else{
				pstmt = this.sqlConsulta(filtro, con);
			}
			
			rs = pstmt.executeQuery();

			if (rs !=null){
				listaIngresoEgreso = new ArrayList<IngresoEgresoBean>();
				while(rs.next()){
					ingresoEgresoBean = new IngresoEgresoBean();
					ingresoEgresoBean.setNombre(rs.getString("nombre"));
					ingresoEgresoBean.setNumero(rs.getString("numero"));
					ingresoEgresoBean.setNumero_ruc(rs.getString("numero_ruc"));
					ingresoEgresoBean.setTipo_cambio(rs.getBigDecimal("tipo_cambio"));
					ingresoEgresoBean.setTipo_moneda(rs.getString("tipo_moneda"));
					ingresoEgresoBean.setMonto_dolares(rs.getBigDecimal("monto_dolares"));
					ingresoEgresoBean.setMonto_soles(rs.getBigDecimal("monto_soles"));
					ingresoEgresoBean.setFecha_cobro(rs.getDate("fecha_cobro"));
					ingresoEgresoBean.setNota(rs.getString("nota"));
					ingresoEgresoBean.setTipo_cobro(rs.getString("tipo_cobro"));
					ingresoEgresoBean.setTipo_pago(rs.getString("tipo_pago"));
					ingresoEgresoBean.setTipo_bancarizado(UtilSGT.getDescripcionTipoBancarizacion(rs.getString("tipo_bancarizacion")));
					ingresoEgresoBean.setNumero_operacion(rs.getString("numero_operacion"));
					ingresoEgresoBean.setFecha_operacion(rs.getDate("fecha_operacion"));
					//Descripcion del ingreso y gasto
					ingresoEgresoBean.setDescripcion(rs.getString("descripcion"));
					/*se valida el tipo de moneda*/
					if (ingresoEgresoBean.getTipo_moneda()!=null && ingresoEgresoBean.getTipo_moneda().equals(Constantes.MONEDA_DOLAR)) {
						ingresoEgresoBean.setMonto_soles(new BigDecimal("0"));
					}else{
						ingresoEgresoBean.setMonto_dolares(new BigDecimal("0"));
					}
					listaIngresoEgreso.add(ingresoEgresoBean);
				}
			}

		}catch(Exception e){
			e.printStackTrace();
			listaIngresoEgreso = null;
		}finally{
			ingresoEgresoBean = null;
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
		return listaIngresoEgreso;
	}
	
	public List<IngresoEgresoBean> getReporteFueraFecha(Filtro filtro){

		IngresoEgresoBean ingresoEgresoBean 		= null;
		List<IngresoEgresoBean> listaIngresoEgreso 	= null;
		Connection con 								= null;
		String url 									= null;
		PreparedStatement pstmt 					= null;
		ResultSet rs 								= null;
		//String query = null;
		try{
			url = urlTienda + userUrl + nombreUsuario + userPass + credencialUsuario;
			con = DriverManager.getConnection(url);
			
			pstmt = this.sqlConsultaFueraFecha(filtro, con);
			
			rs = pstmt.executeQuery();

			if (rs !=null){
				listaIngresoEgreso = new ArrayList<IngresoEgresoBean>();
				while(rs.next()){
					ingresoEgresoBean = new IngresoEgresoBean();
					ingresoEgresoBean.setNombre(rs.getString("nombre"));
					ingresoEgresoBean.setNumero(rs.getString("numero"));
					ingresoEgresoBean.setTipo_cambio(rs.getBigDecimal("tipo_cambio"));
					ingresoEgresoBean.setTipo_moneda(rs.getString("tipo_moneda"));
					ingresoEgresoBean.setMonto_dolares(rs.getBigDecimal("monto_dolares"));
					ingresoEgresoBean.setMonto_soles(rs.getBigDecimal("monto_soles"));
					ingresoEgresoBean.setFecha_cobro(rs.getDate("fecha_cobro"));
					ingresoEgresoBean.setNota(rs.getString("nota"));
					ingresoEgresoBean.setTipo_cobro(rs.getString("tipo_cobro"));
					ingresoEgresoBean.setTipo_pago(rs.getString("tipo_pago"));
					ingresoEgresoBean.setTipo_bancarizado(UtilSGT.getDescripcionTipoBancarizacion(rs.getString("tipo_bancarizacion")));
					ingresoEgresoBean.setNumero_operacion(rs.getString("numero_operacion"));
					ingresoEgresoBean.setFecha_operacion(rs.getDate("fecha_operacion"));
					ingresoEgresoBean.setCodigo_edificio(rs.getInt("codigo_edificio"));
					ingresoEgresoBean.setUsurio_creacion(rs.getInt("usuario_creacion"));
					ingresoEgresoBean.setFecha_creacion(rs.getDate("fecha_creacion"));
					ingresoEgresoBean.setNumero_ruc(rs.getString("numero_ruc"));
					
					/*se valida el tipo de moneda*/
					if (ingresoEgresoBean.getTipo_moneda()!=null && ingresoEgresoBean.getTipo_moneda().equals(Constantes.MONEDA_DOLAR)) {
						ingresoEgresoBean.setMonto_soles(new BigDecimal("0"));
					}else{
						ingresoEgresoBean.setMonto_dolares(new BigDecimal("0"));
					}
					listaIngresoEgreso.add(ingresoEgresoBean);
				}
			}

		}catch(Exception e){
			e.printStackTrace();
			listaIngresoEgreso = null;
		}finally{
			ingresoEgresoBean = null;
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
		return listaIngresoEgreso;
	}
}
