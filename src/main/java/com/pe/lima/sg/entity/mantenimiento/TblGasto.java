package com.pe.lima.sg.entity.mantenimiento;
// Generated 25/01/2019 12:52:22 PM by Hibernate Tools 5.2.3.Final

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * TblGasto generated by hbm2java
 */
@Entity
@Table(name = "tbl_gasto", schema = "caj")
public class TblGasto implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private int codigoGasto;
	private TblEdificio tblEdificio;
	private Integer codigoTipoGasto;
	private BigDecimal monto;
	private String tipoMoneda;
	private Date fechaGasto;
	private String observacion;
	private String estado;
	private Integer usuarioCreacion;
	private Integer usuarioModificacion;
	private Date fechaCreacion;
	private Date fechaModificacion;
	private String ipCreacion;
	private String ipModificacion;
	private String codigoInterno;
	
	public TblGasto() {
	}

	public TblGasto(int codigoGasto, TblEdificio tblEdificio) {
		this.codigoGasto = codigoGasto;
		this.tblEdificio = tblEdificio;
	}

	public TblGasto(int codigoGasto, TblEdificio tblEdificio, Integer codigoTipoGasto, BigDecimal monto,
			String tipoMoneda, Date fechaGasto, String observacion, String estado, Integer usuarioCreacion,
			Integer usuarioModificacion, Date fechaCreacion, Date fechaModificacion, String ipCreacion,
			String ipModificacion) {
		this.codigoGasto = codigoGasto;
		this.tblEdificio = tblEdificio;
		this.codigoTipoGasto = codigoTipoGasto;
		this.monto = monto;
		this.tipoMoneda = tipoMoneda;
		this.fechaGasto = fechaGasto;
		this.observacion = observacion;
		this.estado = estado;
		this.usuarioCreacion = usuarioCreacion;
		this.usuarioModificacion = usuarioModificacion;
		this.fechaCreacion = fechaCreacion;
		this.fechaModificacion = fechaModificacion;
		this.ipCreacion = ipCreacion;
		this.ipModificacion = ipModificacion;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "codigo_gasto", unique = true, nullable = false)
	public int getCodigoGasto() {
		return this.codigoGasto;
	}

	public void setCodigoGasto(int codigoGasto) {
		this.codigoGasto = codigoGasto;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_edificio", nullable = false)
	@JsonBackReference(value="tablaManage-TablaBack")
	public TblEdificio getTblEdificio() {
		return this.tblEdificio;
	}

	public void setTblEdificio(TblEdificio tblEdificio) {
		this.tblEdificio = tblEdificio;
	}

	@Column(name = "codigo_tipo_gasto")
	public Integer getCodigoTipoGasto() {
		return this.codigoTipoGasto;
	}

	public void setCodigoTipoGasto(Integer codigoTipoGasto) {
		this.codigoTipoGasto = codigoTipoGasto;
	}

	@Column(name = "monto", precision = 12)
	public BigDecimal getMonto() {
		return this.monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	@Column(name = "tipo_moneda", length = 2)
	public String getTipoMoneda() {
		return this.tipoMoneda;
	}

	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_gasto", length = 13)
	public Date getFechaGasto() {
		return this.fechaGasto;
	}

	public void setFechaGasto(Date fechaGasto) {
		this.fechaGasto = fechaGasto;
	}

	@Column(name = "observacion", length = 512)
	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
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

	@Column(name = "codigo_interno", length = 64)
	public String getCodigoInterno() {
		return codigoInterno;
	}

	public void setCodigoInterno(String codigoInterno) {
		this.codigoInterno = codigoInterno;
	}
}
