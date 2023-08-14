package com.pe.lima.sg.entity.caja;

import java.io.Serializable;
import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

import com.pe.lima.sg.presentacion.util.Constantes;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the tbl_detalle_comprobante0 database table.
 * 
 */
@Entity
@Table(name="tbl_detalle_comprobante_sunat", schema = "caj")
public class TblDetalleComprobante implements Serializable {
	private static final long serialVersionUID = 1L;
	private String unidadMedida;
	private String moneda;	
	private Integer cantidad;
	private BigDecimal precioUnitario;	
	private String nombreProducto;
	private String afectacionIgv;
	private String descripcionConcepto;
	
	private Integer codigoDetalleComprobante;	
	private BigDecimal descuento;
	private String estado;
	private Date fechaCreacion;
	private Date fechaModificacion;
	private String ipCreacion;
	private String ipModificacion;
	private BigDecimal precioVentaUnitario;	
	private Integer usuarioCreacion;	
	private Integer usuarioModificacion;	
	private BigDecimal valorIgv;	
	private BigDecimal valorReferencial;
	private TblComprobanteSunat tblComprobante;

	public TblDetalleComprobante() {
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="codigo_detalle_comprobante")
	public Integer getCodigoDetalleComprobante() {
		return this.codigoDetalleComprobante;
	}
	public void setCodigoDetalleComprobante(Integer codigoDetalleComprobante) {
		this.codigoDetalleComprobante = codigoDetalleComprobante;
	}

	@Column(name="afectacion_igv", length = 8)
	public String getAfectacionIgv() {
		return this.afectacionIgv;
	}
	public void setAfectacionIgv(String afectacionIgv) {
		this.afectacionIgv = afectacionIgv;
	}

	public Integer getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	@Column(name="descripcion_concepto", length = 512)
	public String getDescripcionConcepto() {
		return this.descripcionConcepto;
	}
	public void setDescripcionConcepto(String descripcionConcepto) {
		this.descripcionConcepto = descripcionConcepto;
	}

	public BigDecimal getDescuento() {
		return this.descuento;
	}
	public void setDescuento(BigDecimal descuento) {
		this.descuento = descuento;
	}	

	public String getMoneda() {
		return this.moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	
	@Column(name="nombre_producto", length = 128)
	public String getNombreProducto() {
		return this.nombreProducto;
	}
	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	@Column(name="precio_unitario", precision = 12)
	public BigDecimal getPrecioUnitario() {
		return this.precioUnitario;
	}
	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	@Column(name="precio_venta_unitario", precision = 12)
	public BigDecimal getPrecioVentaUnitario() {
		return this.precioVentaUnitario;
	}
	public void setPrecioVentaUnitario(BigDecimal precioVentaUnitario) {
		this.precioVentaUnitario = precioVentaUnitario;
	}

	@Column(name="unidad_medida", length = 8)
	public String getUnidadMedida() {
		return this.unidadMedida;
	}
	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}
	
	@Column(name="valor_igv", precision = 12)
	public BigDecimal getValorIgv() {
		return this.valorIgv;
	}
	public void setValorIgv(BigDecimal valorIgv) {
		this.valorIgv = valorIgv;
	}

	@Column(name="valor_referencial", precision = 12)
	public BigDecimal getValorReferencial() {
		return this.valorReferencial;
	}

	public void setValorReferencial(BigDecimal valorReferencial) {
		this.valorReferencial = valorReferencial;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_comprobante")
	public TblComprobanteSunat getTblComprobante() {
		return this.tblComprobante;
	}

	public void setTblComprobante(TblComprobanteSunat tblComprobante) {
		this.tblComprobante = tblComprobante;
	}
	
	@Column(name = "estado", length = 1)
	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Column(name = "usuario_creacion")
	public Integer getUsuarioCreacion() {
		return this.usuarioCreacion;
	}

	public void setUsuarioCreacion(Integer usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}

	@Column(name = "usuario_modificacion")
	public Integer getUsuarioModificacion() {
		return this.usuarioModificacion;
	}

	public void setUsuarioModificacion(Integer usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_creacion", length = 29)
	public Date getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_modificacion", length = 29)
	public Date getFechaModificacion() {
		return this.fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	@Column(name = "ip_creacion", length = 64)
	public String getIpCreacion() {
		return this.ipCreacion;
	}

	public void setIpCreacion(String ipCreacion) {
		this.ipCreacion = ipCreacion;
	}

	@Column(name = "ip_modificacion", length = 64)
	public String getIpModificacion() {
		return this.ipModificacion;
	}

	public void setIpModificacion(String ipModificacion) {
		this.ipModificacion = ipModificacion;
	}

	public void setAuditoriaCreacion(HttpServletRequest request){
		Integer idUsuario 		= null;
		String strIdUsuario 	= null;
		try{
			this.setFechaCreacion(new Date(System.currentTimeMillis()));
			this.setIpCreacion(request.getRemoteAddr());
			this.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			strIdUsuario = (String)request.getSession().getAttribute("id_usuario");
			if (strIdUsuario==null){
				idUsuario = 1;
			}else{
				idUsuario = new Integer(strIdUsuario);
			}
			this.setUsuarioCreacion(idUsuario);
		}catch(Exception e){
			e.printStackTrace();
			this.setUsuarioCreacion(0);
		}finally{
			idUsuario 		= null;
			strIdUsuario	= null;
		}
	}
	
	public void setAuditoriaModificacion(HttpServletRequest request){
		Integer idUsuario 		= null;
		String strIdUsuario 	= null;
		try{
			this.setFechaModificacion(new Date(System.currentTimeMillis()));
			this.setIpModificacion(request.getRemoteAddr());
			strIdUsuario = (String)request.getSession().getAttribute("id_usuario");
			if (strIdUsuario==null){
				idUsuario = 1;
			}else{
				idUsuario = new Integer(strIdUsuario);
			}
			this.setUsuarioModificacion(idUsuario);
		}catch(Exception e){
			e.printStackTrace();
			this.setUsuarioModificacion(0);
		}finally{
			idUsuario 		= null;
			strIdUsuario	= null;
		}
	}
}