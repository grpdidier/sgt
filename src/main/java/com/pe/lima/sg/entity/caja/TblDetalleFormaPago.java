package com.pe.lima.sg.entity.caja;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the tbl_detalle_forma_pago database table.
 * 
 */
@Entity
@Table(name="tbl_detalle_forma_pago", schema = "caj")
public class TblDetalleFormaPago implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private Integer codigFormaPago;
	private String estado;
	private String fecha;	
	private Date fechaCreacion;	
	private Date fechaModificacion;	
	private String ipCreacion;	
	private String ipModificacion;
	private String moneda;
	private BigDecimal monto;	
	private Integer usuarioCreacion;	
	private Integer usuarioModificacion;	
	private TblComprobanteSunat tblComprobante;

	public TblDetalleFormaPago() {
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="codig_forma_pago")
	public Integer getCodigFormaPago() {
		return this.codigFormaPago;
	}
	public void setCodigFormaPago(Integer codigFormaPago) {
		this.codigFormaPago = codigFormaPago;
	}

	
	public String getFecha() {
		return this.fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}	

	public String getMoneda() {
		return this.moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public BigDecimal getMonto() {
		return this.monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="codigo_comprobante")
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
}