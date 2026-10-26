package com.pe.lima.sg.presentacion.reporte.factura.emitida.dao;

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

import com.pe.lima.sg.bean.caja.ComprobanteSunatBean;
import com.pe.lima.sg.bean.caja.MasivoSunatBean;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class MasivoFacturaDao {
	
	@Value("${spring.datasource.url}")
	private String urlTienda;
	
	@Value("${spring.datasource.username}")
	private String nombreUsuario;
	
	@Value("${spring.datasource.password}")
	private String credencialUsuario;
	
	private final String userUrl	= "?user=";
	private final String userPass	= "&password=";
	
	private String url;
	
	/*Obtenemos los datos del reporte de Facturas Emitidas*/
	public List<ComprobanteSunatBean> getReporteFacturaEmitido(MasivoSunatBean masivoSunatBean){
		List<ComprobanteSunatBean> listaFacturaEmitidas	 	= null;
		Connection con 										= null;
		PreparedStatement pstmt 							= null;
		ResultSet rs 										= null;
		try{
			log.debug("[getReporteFacturaEmitido] Inicio");
			url = urlTienda + userUrl + nombreUsuario + userPass + credencialUsuario;
			con = DriverManager.getConnection(url);
			//Obtenemos las facturas emitidas para el inmueble por año y mes
			pstmt = this.sqlConsultaFacturaEmitidaAlquilerServicio(masivoSunatBean, con);
			rs = pstmt.executeQuery();
			listaFacturaEmitidas = getFacturaEmitidaAlquilerServicio(rs);
			
		}catch(Exception e){
			e.printStackTrace();
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
		log.debug("[getReporteFacturaEmitido] Fin");
		return listaFacturaEmitidas;
	}
	
	private BigDecimal getBigDecimal(String name, ResultSet rs) throws SQLException {
		if (rs.getBigDecimal(name) == null) {
			return new BigDecimal("0");
		}else {
			return rs.getBigDecimal(name);
		}
	}
	
	/*Llenamos los datos de las facturas emitidas*/
	private List<ComprobanteSunatBean> getFacturaEmitidaAlquilerServicio(ResultSet rs) throws SQLException{
		List<ComprobanteSunatBean> listaFacturaEmitidas 	= new ArrayList<>();
		ComprobanteSunatBean comprobanteSunatBean			= null;
		if (rs !=null){
			log.debug("[getFacturaEmitidaAlquilerServicio] Inicio");
			while(rs.next()){
				comprobanteSunatBean = new ComprobanteSunatBean();
				comprobanteSunatBean.setFechaEmision(rs.getString("fecha_emision"));
				comprobanteSunatBean.setClienteTipoDocumento(rs.getString("cliente_tipo_documento"));
				comprobanteSunatBean.setSerie(rs.getString("serie"));
				comprobanteSunatBean.setNumero(rs.getString("numero"));
				comprobanteSunatBean.setClienteNumero(rs.getString("cliente_numero"));
				comprobanteSunatBean.setClienteNombre(rs.getString("cliente_nombre"));
				comprobanteSunatBean.setMoneda(rs.getString("moneda"));
				comprobanteSunatBean.setTipoCambio(getBigDecimal("tipo_cambio", rs));
				comprobanteSunatBean.setTotalGravados(getBigDecimal("total_gravados", rs));
				comprobanteSunatBean.setTotalIgv(getBigDecimal("total_igv", rs));
				comprobanteSunatBean.setTotal(getBigDecimal("total", rs));
				comprobanteSunatBean.setConcepto(rs.getString("descripcion_concepto"));
				comprobanteSunatBean.setTipoPago(rs.getString("tipo_pago"));
				listaFacturaEmitidas.add(comprobanteSunatBean);
			}
			log.debug("[getFacturaEmitidaAlquilerServicio] Fin:"+listaFacturaEmitidas.size());
			
		}
		return listaFacturaEmitidas;
	}
	


	
	/*Obtenemos todas las facturas emitidas*/
	private PreparedStatement sqlConsultaFacturaEmitidaAlquilerServicio(MasivoSunatBean masivoSunatBean, Connection con) throws SQLException{
		String query = null;
		PreparedStatement pstmt = null;
		log.debug("[sqlConsultaFacturaEmitidaAlquilerServicio] Inicio");
		query = this.sqlConsultaFacturaEmitidaAlquilerServicio();
		pstmt = con.prepareStatement(query);
		pstmt.setInt(1, masivoSunatBean.getCodigoEdificio());
		pstmt.setString(2,  masivoSunatBean.getAnio()+masivoSunatBean.getMes());
		pstmt.setInt(3, masivoSunatBean.getCodigoEdificio());
		pstmt.setString(4,  masivoSunatBean.getAnio()+masivoSunatBean.getMes());
		log.debug("[sqlConsultaFacturaEmitidaAlquilerServicio] query:"+query);
		log.debug("[sqlConsultaFacturaEmitidaAlquilerServicio] Fin");
		return pstmt;
	}
	private String sqlConsultaFacturaEmitidaAlquilerServicio(){
		String query = null;
		query = " select * from ( " + 
				"  select csu.fecha_emision, " + 
				"      csu.cliente_tipo_documento, " + 
				"      csu.serie, " + 
				"      csu.numero, " + 
				"      csu.cliente_numero, " + 
				"      csu.cliente_nombre, " + 
				"      csu.moneda, " + 
				"      csu.tipo_cambio, " + 
				"      csu.total_gravados, " + 
				"      csu.total_igv, " + 
				"      csu.total, " + 
				"      det.descripcion_concepto, " + 
				"      csu.tipo_pago " + 
				"  from caj.tbl_comprobante_sunat csu, " + 
				"  caj.tbl_detalle_comprobante_sunat det, " + 
				"  mae.tbl_tienda tie " + 
				"  where csu.codigo_comprobante = det.codigo_comprobante " + 
				"  and csu.numero_tienda = tie.numero " + 
				"  and csu.tipo_pago = 'ALQ' " + 
				"  and tie.codigo_edificio = ? " + 
				"  and to_char(to_date(fecha_emision, 'YYYY-MM-DD'), 'YYYYMM') = ? " + 
				"  UNION ALL " + 
				"  select csu.fecha_emision, " + 
				"      csu.cliente_tipo_documento, " + 
				"      csu.serie, " + 
				"      csu.numero, " + 
				"      csu.cliente_numero, " + 
				"      csu.cliente_nombre, " + 
				"      csu.moneda, " + 
				"      csu.tipo_cambio, " + 
				"      csu.total_gravados, " + 
				"      csu.total_igv, " + 
				"      csu.total, " + 
				"      det.descripcion_concepto, " + 
				"      csu.tipo_pago " + 
				"  from caj.tbl_comprobante_sunat csu, " + 
				"  caj.tbl_detalle_comprobante_sunat det, " + 
				"  mae.tbl_tienda tie " + 
				"  where csu.codigo_comprobante = det.codigo_comprobante " + 
				"  and csu.numero_tienda = tie.numero " + 
				"  and csu.tipo_pago = 'SER' " + 
				"  and tie.codigo_edificio = ? " + 
				"  and to_char(to_date(fecha_emision, 'YYYY-MM-DD'), 'YYYYMM') = ? " + 
				") Tabla " + 
				"order by tipo_pago, serie, numero";
		return query;
	}
	
	
}
