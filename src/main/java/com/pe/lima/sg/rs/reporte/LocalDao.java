package com.pe.lima.sg.rs.reporte;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pe.lima.sg.bean.reporte.LocalBean;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class LocalDao {
	
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
	public List<LocalBean> getReporteLocalxInmueble(Filtro filtro){
		List<LocalBean> listaLocal	 	= null;
		Connection con 					= null;
		PreparedStatement pstmt 		= null;
		ResultSet rs 					= null;
		Map<String, LocalBean> mapLocal	= null;
		try{
			log.debug("[getReporteMoroso] Inicio");
			url = urlTienda + userUrl + nombreUsuario + userPass + credencialUsuario;
			con = DriverManager.getConnection(url);
			//Obtenemos todos los locales (tiendas)
			pstmt = this.sqlConsultaLocales(filtro, con);
			rs = pstmt.executeQuery();
			mapLocal = setLocal(rs);
			//Obtenemos los montos de alquiler, servicio, monto del contrato
			pstmt = this.sqlConsultaMontoAlquilerServicioContrato(filtro, con);
			rs = pstmt.executeQuery();
			mapLocal = setMontoAlquilerServicioContrato(rs,mapLocal);
			//Retornamos la lista
			listaLocal = new ArrayList<LocalBean>();
			for(Map.Entry<String, LocalBean> entry: mapLocal.entrySet()){
				listaLocal.add(entry.getValue());
			}
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
	
	/*Llenamos los locales en un HashMap*/
	private Map<String, LocalBean> setLocal(ResultSet rs ) throws SQLException{
		LocalBean localBean 			= null;
		Map<String, LocalBean> mapLocal	= null;
		if (rs !=null){
			log.debug("[setLocal] Inicio");
			mapLocal = new HashMap<String, LocalBean>();
			while(rs.next()){
				localBean = new LocalBean();
				localBean.setNombreInmueble(rs.getString("inmueble"));
				localBean.setNumeroTienda(rs.getString("tienda"));
				localBean.setNombreCliente("");
				localBean.setEstado(Constantes.ESTADO_LIBRE);
				localBean.setOrden("3");
				localBean.setMontoAlquiler(new BigDecimal(0));
				localBean.setMontoServicio(new BigDecimal(0));
				//Adicion de datos en el reporte - 2023.02.22
				localBean.setRucCliente("");
				localBean.setFechaFinContrato("");
				localBean.setFechaInicioContrato("");
				localBean.setMontoGarantia(new BigDecimal(0));
				mapLocal.put(rs.getString("tienda"), localBean);
			}
			log.debug("[setLocal] Fin:"+mapLocal.size());
			
		}
		return mapLocal;
	}
	private BigDecimal getBigDecimal(String name, ResultSet rs) throws SQLException {
		if (rs.getBigDecimal(name) == null) {
			return new BigDecimal("0");
		}else {
			return rs.getBigDecimal(name);
		}
	}
	
	/*Llenamos los montos de alquiler, servicio detalle y estado*/
	private Map<String, LocalBean> setMontoAlquilerServicioContrato(ResultSet rs, Map<String, LocalBean> mapLocal ) throws SQLException{
		LocalBean localBean 			= null;
		String key						= null;
		if (rs !=null){
			log.debug("[setMontoAlquilerServicioContrato] Inicio");
			while(rs.next()){
				//log.debug("[setMontoAlquilerServicioContrato] tienda:"+rs.getString("tienda"));
				key = rs.getString("tienda");
				localBean = mapLocal.get(key);
				//log.debug("[setMontoAlquilerServicioContrato] localBean:"+localBean);
				if (localBean != null){
					//log.debug("[setMontoAlquilerServicioContrato] nombre:"+rs.getString("nombre"));
					//Detalle - Nombre del cliente
					localBean.setNombreCliente(rs.getString("nombre"));
					//Montos
					//log.debug("[setMontoAlquilerServicioContrato] tipo_referencia:"+rs.getString("tipo_referencia"));
					if (rs.getString("tipo_referencia").equals(Constantes.TIPO_COBRO_ALQUILER)){
						localBean.setMontoAlquiler(getBigDecimal("total",rs));
					} else if (rs.getString("tipo_referencia").equals(Constantes.TIPO_COBRO_SERVICIO)){
						if (localBean.getMontoServicio() != null) {
							localBean.setMontoServicio(localBean.getMontoServicio().add(getBigDecimal("total",rs)));
						}else {
							localBean.setMontoServicio(getBigDecimal("total",rs));
						}
					} else{
						log.debug("[setMontoAlquilerServicioContrato] Oh!!, servicio no identificado:"+rs.getString("tipo_referencia"));
					}
					//Estado
					//log.debug("[setMontoAlquilerServicioContrato] monto_alquiler:"+rs.getString("monto_alquiler"));
					if (rs.getBigDecimal("monto_alquiler").compareTo(new BigDecimal(0)) > 0){
						localBean.setEstado(Constantes.ESTADO_ALQUILADO);
						localBean.setOrden("1");
					}else{
						localBean.setEstado(Constantes.ESTADO_VENDIDO);
						localBean.setOrden("2");
					}
					//Adicion de datos en el reporte - 2023.02.22
					localBean.setRucCliente(rs.getString("ruc"));
					localBean.setFechaInicioContrato(rs.getString("fecha_inicio"));
					localBean.setFechaFinContrato(rs.getString("fecha_fin"));
					localBean.setMontoGarantia(getBigDecimal("monto_garantia",rs));
					
				}else{
					log.debug("[setMontoAlquilerServicioContrato] ¡Houston! we have a problem:, Tienda not Found:"+key);
				}
				
				
			}
			log.debug("[setMontoAlquilerServicioContrato] Fin:"+mapLocal.size());
			
		}
		return mapLocal;
	}
	
	/*Obtenemos todos los locales por inmueble*/
	private PreparedStatement sqlConsultaLocales(Filtro filtro, Connection con) throws SQLException{
		String query = null;
		PreparedStatement pstmt = null;
		log.debug("[sqlConsultaLocales] Inicio");
		query = this.sqlConsultaLocales();
		pstmt = con.prepareStatement(query);
		pstmt.setInt(1, filtro.getCodigoEdificacion());
		
		log.debug("[sqlConsultaLocales] query:"+query);
		log.debug("[sqlConsultaLocales] Fin");
		return pstmt;
	}
	private String sqlConsultaLocales(){
		String query = null;
		query =" SELECT  "+
		       "	   edi.nombre as inmueble, "+
		       "	   tie.numero as tienda "+
			   " FROM    "+
			   "	   mae.tbl_tienda tie, "+
		       "    mae.tbl_edificio edi  "+
		       " WHERE  tie.codigo_edificio = edi.codigo_edificio "+
		       " 	AND tie.estado = '1' "+
		       "    AND edi.codigo_edificio = ? "+
		       " ORDER BY 2 ASC  ";
		return query;
	}
	
	/*Obtenemos los montos de alquiler, servicio y el monto del contrato para definir el estado*/
	private PreparedStatement sqlConsultaMontoAlquilerServicioContrato(Filtro filtro, Connection con) throws SQLException{
		String query = null;
		PreparedStatement pstmt = null;
		log.debug("[sqlConsultaMontoAlquilerServicioContrato] Inicio");
		query = this.sqlConsultaMontoAlquilerServicioContrato();
		pstmt = con.prepareStatement(query);
		pstmt.setInt(1, filtro.getCodigoEdificacion());
		pstmt.setString(2,  filtro.getMesFin()+filtro.getAnio());
		pstmt.setString(3,  filtro.getAnio()+filtro.getMesFin());
		log.debug("[sqlConsultaMontoAlquilerServicioContrato] query:"+query);
		log.debug("[sqlConsultaMontoAlquilerServicioContrato] Fin");
		return pstmt;
	}
	private String sqlConsultaMontoAlquilerServicioContrato(){
		String query = null;
		query = " SELECT  "+
		        "	   edi.nombre as inmueble, "+
		        "	   tie.numero as tienda,  "+
		        "       upper(to_char(cxc.fecha_fin,'TMmonth')) as vencimiento, "+
		        "       case when per.razon_social is null or TRIM(per.razon_social) = '' THEN "+
		        "      		     per.nombre || ' ' || per.paterno || ' '  || per.materno  "+
		        "            else  per.razon_social "+
			    "       end as nombre,  "+
		        "       cxc.monto_contrato as total, "+
		        "       con.monto_alquiler, "+
			    "       cxc.saldo,   "+
			    "       cxc.tipo_moneda, "+
		        "       cxc.tipo_referencia, "+
			    "		TO_CHAR(con.fecha_inicio,'dd/mm/yyyy') as fecha_inicio, " +
			    "		TO_CHAR(con.fecha_fin,'dd/mm/yyyy') as fecha_fin, " +
			    "       case when per.numero_ruc is null or TRIM(per.numero_ruc) = '' THEN ' ' "+
		        "            else  per.numero_ruc "+
			    "       end as ruc,  "+
			    "       con.monto_garantia "+
			    " FROM caj.tbl_cxc_documento cxc,   "+
			    "	 cli.tbl_contrato con,   "+
			    "    mae.tbl_persona per,   "+
			    "    mae.tbl_tienda tie, "+
		        "    mae.tbl_edificio edi   "+
			    " WHERE cxc.codigo_contrato = con.codigo_contrato   "+
			    "    AND con.codigo_tienda = tie.codigo_tienda   "+
		        "    AND tie.codigo_edificio = edi.codigo_edificio "+
			    "    AND con.codigo_persona = per.codigo_persona   "+
			    "    AND con.estado = '1'   "+
			    "    AND con.estado_contrato in ('PND', 'VGN','RNV','FNL')  "+
		        "    AND cxc.tipo_referencia IN ('ALQ', 'SER') "+
		        "    AND edi.codigo_edificio = ? "+
			    "    AND TO_CHAR(cxc.fecha_fin,'MMYYYY') = ?  "+
		        "    and  " + 
		        "    CASE   " + 
		        "     	WHEN  con.estado_contrato = 'FNL' " + 
		        "         	THEN to_char(con.fecha_finalizacion,'YYYYMM')  " + 
		        "       ELSE  to_char(con.fecha_fin,'YYYYMM')  " + 
		        "     END >= ? ";
		return query;
	}
	
	
}
