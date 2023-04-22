package com.pe.lima.sg.entity.cliente;
// Generated 11/10/2017 10:08:10 PM by Hibernate Tools 4.3.5.Final

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pe.lima.sg.entity.mantenimiento.TblSuministro;

/**
 * TblLuz generated by hbm2java
 */
@Entity
@Table(name = "tbl_luz", schema = "cli")
public class TblLuz implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private int codigoLuz;
	private TblSuministro tblSuministro;
	private Integer anio;
	private BigDecimal montoGenerado;
	private BigDecimal montoContrato;
	private String tipoMoneda;
	private BigDecimal valorCobrado;
	private BigDecimal saldo;
	private Date fechaInicio;
	private Date fechaFin;
	private String estadoLuz;
	private String estado;
	private Integer usuarioCreacion;
	private Integer usuarioModificacion;
	private Date fechaCreacion;
	private Date fechaModificacion;
	private String ipCreacion;
	private String ipModificacion;
	private Set<TblLuzxtienda> tblLuzxtiendas = new HashSet<TblLuzxtienda>(0);

	public TblLuz() {
	}

	public TblLuz(int codigoLuz, TblSuministro tblSuministro) {
		this.codigoLuz = codigoLuz;
		this.tblSuministro = tblSuministro;
	}

	public TblLuz(int codigoLuz, TblSuministro tblSuministro, Integer anio, BigDecimal montoGenerado,
			BigDecimal montoContrato, String tipoMoneda, BigDecimal valorCobrado, BigDecimal saldo, Date fechaInicio,
			Date fechaFin, String estadoLuz, String estado, Integer usuarioCreacion, Integer usuarioModificacion, Date fechaCreacion,
			Date fechaModificacion, String ipCreacion, String ipModificacion, Set<TblLuzxtienda> tblLuzxtiendas) {
		this.codigoLuz = codigoLuz;
		this.tblSuministro = tblSuministro;
		this.anio = anio;
		this.montoGenerado = montoGenerado;
		this.montoContrato = montoContrato;
		this.tipoMoneda = tipoMoneda;
		this.valorCobrado = valorCobrado;
		this.saldo = saldo;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.estadoLuz = estadoLuz;
		this.estado = estado;
		this.usuarioCreacion = usuarioCreacion;
		this.usuarioModificacion = usuarioModificacion;
		this.fechaCreacion = fechaCreacion;
		this.fechaModificacion = fechaModificacion;
		this.ipCreacion = ipCreacion;
		this.ipModificacion = ipModificacion;
		this.tblLuzxtiendas = tblLuzxtiendas;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "codigo_luz", unique = true, nullable = false)
	public int getCodigoLuz() {
		return this.codigoLuz;
	}

	public void setCodigoLuz(int codigoLuz) {
		this.codigoLuz = codigoLuz;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "codigo_suministro", nullable = false)
	@JsonBackReference(value="tablaManage-TablaBack")
	public TblSuministro getTblSuministro() {
		return this.tblSuministro;
	}

	public void setTblSuministro(TblSuministro tblSuministro) {
		this.tblSuministro = tblSuministro;
	}

	@Column(name = "anio")
	public Integer getAnio() {
		return this.anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	@Column(name = "monto_generado", precision = 12)
	public BigDecimal getMontoGenerado() {
		return this.montoGenerado;
	}

	public void setMontoGenerado(BigDecimal montoGenerado) {
		this.montoGenerado = montoGenerado;
	}

	@Column(name = "monto_contrato", precision = 12)
	public BigDecimal getMontoContrato() {
		return this.montoContrato;
	}

	public void setMontoContrato(BigDecimal montoContrato) {
		this.montoContrato = montoContrato;
	}

	@Column(name = "tipo_moneda", length = 2)
	public String getTipoMoneda() {
		return this.tipoMoneda;
	}

	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}

	@Column(name = "valor_cobrado", precision = 12)
	public BigDecimal getValorCobrado() {
		return this.valorCobrado;
	}

	public void setValorCobrado(BigDecimal valorCobrado) {
		this.valorCobrado = valorCobrado;
	}

	@Column(name = "saldo", precision = 12)
	public BigDecimal getSaldo() {
		return this.saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_inicio", length = 13)
	public Date getFechaInicio() {
		return this.fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_fin", length = 13)
	public Date getFechaFin() {
		return this.fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblLuz")
	@JsonManagedReference(value="tablaManage-TablaBack")
	public Set<TblLuzxtienda> getTblLuzxtiendas() {
		return this.tblLuzxtiendas;
	}

	public void setTblLuzxtiendas(Set<TblLuzxtienda> tblLuzxtiendas) {
		this.tblLuzxtiendas = tblLuzxtiendas;
	}
	
	@Column(name = "estado_luz", length = 1)
	public String getEstadoLuz() {
		return estadoLuz;
	}

	public void setEstadoLuz(String estadoLuz) {
		this.estadoLuz = estadoLuz;
	}

}
