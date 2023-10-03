package com.pe.lima.sg.rs.ose;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pe.lima.sg.bean.caja.FacturaBean;
import com.pe.lima.sg.entity.caja.TblComprobanteSunat;
import com.pe.lima.sg.entity.caja.TblDetalleComprobante;
import com.pe.lima.sg.entity.caja.TblDetalleFormaPago;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.UtilSGT;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FacturaOseDao {
	
	@Value("${spring.datasource.url}")
	private String urlTienda;
	
	@Value("${spring.datasource.username}")
	private String nombreUsuario;
	
	@Value("${spring.datasource.password}")
	private String credencialUsuario;
	
	private final String userUrl	= "?user=";
	private final String userPass	= "&password=";
	
	private String url;
	
	public List<FacturaBean> getConsultaAlquilerServicioOse(Filtro criterio){

		FacturaBean entidad		= null;
		List<FacturaBean> lista = null;
		Connection con 			= null;
		PreparedStatement pstmt = null;
		ResultSet rs 			= null;
		String query 			= null;
		Integer anioCurso		= UtilSGT.getAnioDate(new Date());
		Integer mesCurso		= UtilSGT.getMesDate(new Date());
		try{
			url = urlTienda + userUrl + nombreUsuario + userPass + credencialUsuario;
			con = DriverManager.getConnection(url);
			query = "SELECT edi.nombre, " + 
					"	   tie.numero, " + 
					"       cxc.tipo_referencia, " + 
					"       cxc.monto_generado, " + 
					"       per.numero_ruc, " + 
					"       per.direccion_casa, " + 
					"       per.direccion_empresa, " + 
					"       per.razon_social, " + 
					"       con.tipo_moneda_alquiler, " +
					"		cxc.codigo_contrato, "+
					"		cxc.codigo_cxc_doc "+
					"FROM caj.tbl_cxc_documento cxc, " + 
					"	 cli.tbl_contrato con, " + 
					"     mae.tbl_persona per, " + 
					"     mae.tbl_tienda tie, " + 
					"     mae.tbl_edificio edi " + 
					"WHERE cxc.codigo_contrato = con.codigo_contrato " + 
					"	and con.codigo_persona = per.codigo_persona " + 
					"    and con.codigo_tienda = tie.codigo_tienda " + 
					"    and tie.codigo_edificio = edi.codigo_edificio " + 
					"    and con.estado_contrato IN ('VGN', 'RNV') " + 
					"    and cxc.anio = ? " + 
					"    and cxc.mes = ? " + 
					"    and cxc.tipo_referencia = ? " + 
					"    and tie.codigo_edificio = ? " + 
					"    and cxc.tipo_documento = 'FAC' "+
					"    and (cxc.codigo_comprobante is null or cxc.codigo_comprobante <= 0)"+
					"    and tie.numero like ? Order by 2 asc"; 

			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, anioCurso);
			pstmt.setInt(2, mesCurso);
			pstmt.setString(3, criterio.getTipo());
			pstmt.setInt(4, criterio.getCodigoEdificacion());
			pstmt.setString(5,  criterio.getNumero().toUpperCase()+"%");
			
			rs = pstmt.executeQuery();

			if (rs !=null){
				lista = new ArrayList<FacturaBean>();
				while(rs.next()){
					entidad = new FacturaBean();
					entidad.setFactura(new TblComprobanteSunat());
					entidad.getFactura().setClienteTipoDocumento(Constantes.SUNAT_TIPO_DOCUMENTO_RUC);
					entidad.getFactura().setClienteNumero(rs.getString("numero_ruc"));
					entidad.getFactura().setClienteDireccion(rs.getString("direccion_empresa"));
					entidad.getFactura().setClienteNombre(rs.getString("razon_social"));
					entidad.getFactura().setMoneda(getMoneda(rs.getString("tipo_referencia")));
					entidad.getFactura().setTipoPago(criterio.getTipo());
					entidad.getFactura().setCodigoContrato(rs.getInt("codigo_contrato"));
					entidad.getFactura().setEstadoOperacion(Constantes.ESTADO_OPERACION_REGISTRADO);
					entidad.getFactura().setNumeroTienda(rs.getString("numero"));
					entidad.setFacturaDetalle(new TblDetalleComprobante());
					entidad.getFacturaDetalle().setUnidadMedida(Constantes.SUNAT_UNIDAD_MEDIDA);
					entidad.getFacturaDetalle().setMoneda(getMoneda(rs.getString("tipo_referencia")));
					entidad.getFacturaDetalle().setCantidad(1);
					entidad.getFacturaDetalle().setPrecioUnitario(getPrecioSinIgv(rs.getBigDecimal("monto_generado")));
					entidad.getFacturaDetalle().setNombreProducto(getNombreProducto(rs.getString("tipo_referencia")));
					entidad.getFacturaDetalle().setAfectacionIgv(Constantes.SUNAT_AFECTACION_IGV_GRAVADO_OPE_ONEROSO + " : GRAVADO OP. ONEROSO");
					entidad.getFacturaDetalle().setDescripcionConcepto(getConcepto((rs.getString("tipo_referencia")),rs.getString("numero") ));
					//entidad.getFactura().setTotalGravados(rs.getBigDecimal("monto_generado"));
					entidad.getFactura().setTotalGravados(entidad.getFacturaDetalle().getPrecioUnitario());
					entidad.getFactura().setTotalIgv(getIgv(rs.getBigDecimal("monto_generado"), entidad.getFacturaDetalle().getPrecioUnitario()));
					entidad.getFactura().setTotal(getTotal(entidad.getFactura().getTotalIgv(), entidad.getFactura().getTotalGravados()));
					entidad.getFacturaDetalle().setValorIgv(entidad.getFactura().getTotalIgv());
					entidad.getFacturaDetalle().setPrecioVentaUnitario(entidad.getFactura().getTotal());
					entidad.setNombreInmueble(rs.getString("nombre"));
					entidad.setNumeroTienda(rs.getString("numero"));
					log.info("[getConsultaAlquilerServicioOse] tienda:"+entidad.getNumeroTienda());
					entidad.setFormaPago(new TblDetalleFormaPago());
					entidad.getFormaPago().setMonto(entidad.getFactura().getTotal());
					entidad.getFormaPago().setMoneda(entidad.getFacturaDetalle().getMoneda());
					entidad.getFactura().setFormaPago(Constantes.FORMA_PAGO_CONTADO);
					entidad.setCodigoContrato(rs.getInt("codigo_contrato"));
					entidad.setCodigoCxCDocumento(rs.getInt("codigo_cxc_doc"));
					lista.add(entidad);
				}
			}

		}catch(Exception e){
			e.printStackTrace();
			lista = null;
		}finally{
			entidad = null;
			rs = null;
			pstmt = null;
			query = null;
			query = null;
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
		return lista;
	}
	private BigDecimal getPrecioSinIgv(BigDecimal monto) {
		BigDecimal igvAumentado = new BigDecimal(Constantes.SUNAT_IGV).add(new BigDecimal("100")).divide(new BigDecimal("100"));
		
		return monto.divide(igvAumentado, 2, RoundingMode.HALF_UP);
	}
	private String getMoneda(String tipo) {
		if (tipo.equals(Constantes.TIPO_PAGO_ALQUILER_CODIGO)) {
			return Constantes.SUNAT_TIPO_MONEDA_DOLAR;
		}else {
			return Constantes.SUNAT_TIPO_MONEDA_SOLES;
		}
	}
	private String getNombreProducto(String tipo) {
		if (tipo.equals(Constantes.TIPO_PAGO_ALQUILER_CODIGO)) {
			return Constantes.TIPO_PAGO_ALQUILER_DESCRIPCION;
		}else {
			return Constantes.TIPO_PAGO_SERVICIO_DESCRIPCION;
		}
	}
	private String getConcepto(String tipo, String tienda) {
		if (tipo.equals(Constantes.TIPO_PAGO_ALQUILER_CODIGO)) {
			return "Alquiler de la Tienda "+ tienda +" correspondiente al periodo " 
					+ UtilSGT.getMesPersonalizado(UtilSGT.getMesDate(new Date())) + " " + UtilSGT.getAnioDate(new Date());
					/*" CORRESPONDIENTE A LA FECHA DE " + UtilSGT.getFistDay(UtilSGT.getMesDateFormateado(new Date()), UtilSGT.getAnioDate(new Date())) +
					" AL " + UtilSGT.getLastDay(UtilSGT.getMesDate(new Date()), UtilSGT.getAnioDate(new Date()));*/
		}else {
			return "Servicio de Mantenimiento de la Tienda "+ tienda +" correspondiente al periodo " 
					+ UtilSGT.getMesPersonalizado(UtilSGT.getMesDate(new Date())) + " " + UtilSGT.getAnioDate(new Date());/* +
					" CORRESPONDIENTE A LA FECHA DE " + UtilSGT.getFistDay(UtilSGT.getMesDateFormateado(new Date()), UtilSGT.getAnioDate(new Date())) +
					" AL " + UtilSGT.getLastDay(UtilSGT.getMesDate(new Date()), UtilSGT.getAnioDate(new Date()));*/
		}
	}
	private BigDecimal getIgv(BigDecimal montoConIgv, BigDecimal montoSinIgv) {
		//return monto.multiply(new BigDecimal(Constantes.SUNAT_IGV)).divide(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		return montoConIgv.subtract(montoSinIgv).setScale(2, BigDecimal.ROUND_UP);
	}
	private BigDecimal getTotal(BigDecimal igv, BigDecimal monto) {
		return monto.add(igv).setScale(2, BigDecimal.ROUND_UP);
	}
	public List<FacturaBean> getConsultaFacturaOse(Filtro criterio){
		log.info("[getConsultaFacturaOse] Inicio");
		FacturaBean entidad		= null;
		List<FacturaBean> lista = null;
		Connection con 			= null;
		PreparedStatement pstmt = null;
		ResultSet rs 			= null;
		String query 			= null;
		//Integer anioCurso		= UtilSGT.getAnioDate(new Date());
		//Integer mesCurso		= UtilSGT.getMesDate(new Date());
		try{
			url = urlTienda + userUrl + nombreUsuario + userPass + credencialUsuario;
			con = DriverManager.getConnection(url);
			query = "SELECT com.numero_tienda, " + 
					"  		 det.nombre_producto, " + 
					"         det.precio_unitario, " + 
					"         det.moneda, " + 
					"         com.serie, " + 
					"         com.numero, " + 
					"         com.fecha_emision, " + 
					"         com.codigo_comprobante, " + 
					"         det.codigo_detalle_comprobante " + 
					"  FROM caj.tbl_comprobante_sunat com, " + 
					"  	   caj.tbl_detalle_comprobante_sunat det " + 
					"  WHERE com.codigo_comprobante = det.codigo_comprobante " + 
					"        and com.numero_nota is null " +
					"        and com.numero_tienda like ? " + 
					"  		 and com.numero like ? " + 
					"        and det.nombre_producto like ? ORDER by 6 desc";
			

			pstmt = con.prepareStatement(query);
			pstmt.setString(1, criterio.getNumero().toUpperCase()+"%");
			pstmt.setString(2, criterio.getNumeroFactura().toUpperCase()+"%");
			pstmt.setString(3, criterio.getTipo()+"%");
			log.info("[getConsultaFacturaOse] Criterios:"+criterio.getNumero() + ":"+criterio.getNumeroFactura()+":"+criterio.getTipo());
			rs = pstmt.executeQuery();

			if (rs !=null){
				lista = new ArrayList<FacturaBean>();
				while(rs.next()){
					entidad = new FacturaBean();
					entidad.setFactura(new TblComprobanteSunat());
					entidad.setFacturaDetalle(new TblDetalleComprobante());
					entidad.getFactura().setNumeroTienda(rs.getString("numero_tienda"));
					entidad.getFacturaDetalle().setNombreProducto(rs.getString("nombre_producto"));
					entidad.getFacturaDetalle().setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
					entidad.getFacturaDetalle().setMoneda(rs.getString("moneda"));
					entidad.getFactura().setSerie(rs.getString("serie"));
					entidad.getFactura().setNumero(rs.getString("numero"));
					entidad.getFactura().setFechaEmision(rs.getString("fecha_emision"));
					entidad.getFactura().setCodigoComprobante(rs.getInt("codigo_comprobante"));
					entidad.getFacturaDetalle().setCodigoDetalleComprobante(rs.getInt("codigo_detalle_comprobante"));
					
					lista.add(entidad);
				}
				log.info("[getConsultaFacturaOse] Resultado:"+lista.size());
			}else {
				log.info("[getConsultaFacturaOse] Resultado vacio []");
			}
			log.info("[getConsultaFacturaOse] Fin");
		}catch(Exception e){
			e.printStackTrace();
			lista = null;
		}finally{
			entidad = null;
			rs = null;
			pstmt = null;
			query = null;
			query = null;
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
		return lista;
	}
}
