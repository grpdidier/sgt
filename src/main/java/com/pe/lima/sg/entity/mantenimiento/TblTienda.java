package com.pe.lima.sg.entity.mantenimiento;
// Generated 18/09/2017 01:36:18 PM by Hibernate Tools 5.2.3.Final

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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
import com.pe.lima.sg.entity.cliente.TblArbitrio;
import com.pe.lima.sg.entity.cliente.TblContrato;
import com.pe.lima.sg.entity.cliente.TblLuzxtienda;

/**
 * TblTienda generated by hbm2java
 */
@Entity
@Table(name = "tbl_tienda", schema = "mae")
public class TblTienda implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private int codigoTienda;
	private TblEdificio tblEdificio;
	private TblSuministro tblSuministro;
	private String tipo;
	private String piso;
	private String numero;
	private BigDecimal area;
	private String observacion;
	private String estadoTienda;
	private String estado;
	private Integer usuarioCreacion;
	private Integer usuarioModificacion;
	private Date fechaCreacion;
	private Date fechaModificacion;
	private String ipCreacion;
	private String ipModificacion;
	private Set<TblContrato> tblContratos = new HashSet<TblContrato>(0);
	private Set<TblArbitrio> tblArbitrios = new HashSet<TblArbitrio>(0);
	private Set<TblLuzxtienda> tblLuzxtiendas = new HashSet<TblLuzxtienda>(0);


	public TblTienda() {
	}

	public TblTienda(int codigoTienda, TblEdificio tblEdificio, TblSuministro tblSuministro) {
		this.codigoTienda = codigoTienda;
		this.tblEdificio = tblEdificio;
		this.tblSuministro = tblSuministro;
	}

	public TblTienda(int codigoTienda, TblEdificio tblEdificio, TblSuministro tblSuministro, String tipo, String piso,
			String numero, BigDecimal area, String observacion, String estadoTienda, String estado,
			Integer usuarioCreacion, Integer usuarioModificacion, Date fechaCreacion, Date fechaModificacion,
			String ipCreacion, String ipModificacion, Set<TblContrato> tblContratos, Set<TblArbitrio> tblArbitrios, Set<TblLuzxtienda> tblLuzxtiendas) {
		this.codigoTienda = codigoTienda;
		this.tblEdificio = tblEdificio;
		this.tblSuministro = tblSuministro;
		this.tipo = tipo;
		this.piso = piso;
		this.numero = numero;
		this.area = area;
		this.observacion = observacion;
		this.estadoTienda = estadoTienda;
		this.estado = estado;
		this.usuarioCreacion = usuarioCreacion;
		this.usuarioModificacion = usuarioModificacion;
		this.fechaCreacion = fechaCreacion;
		this.fechaModificacion = fechaModificacion;
		this.ipCreacion = ipCreacion;
		this.ipModificacion = ipModificacion;
		this.tblContratos = tblContratos;
		this.tblArbitrios = tblArbitrios;
		this.tblLuzxtiendas = tblLuzxtiendas;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "codigo_tienda", unique = true, nullable = false)
	public int getCodigoTienda() {
		return this.codigoTienda;
	}

	public void setCodigoTienda(int codigoTienda) {
		this.codigoTienda = codigoTienda;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "codigo_edificio", nullable = false)
	@JsonBackReference(value="tablaManage-TablaBack")
	public TblEdificio getTblEdificio() {
		return this.tblEdificio;
	}

	public void setTblEdificio(TblEdificio tblEdificio) {
		this.tblEdificio = tblEdificio;
	}

	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "codigo_suministro", nullable = false)
	@JsonBackReference(value="tablaManage-TablaBack")
	public TblSuministro getTblSuministro() {
		return this.tblSuministro;
	}

	public void setTblSuministro(TblSuministro tblSuministro) {
		this.tblSuministro = tblSuministro;
	}

	@Column(name = "tipo", length = 2)
	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Column(name = "piso", length = 32)
	public String getPiso() {
		return this.piso;
	}

	public void setPiso(String piso) {
		this.piso = piso;
	}

	@Column(name = "numero", length = 64)
	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(name = "area", precision = 5)
	public BigDecimal getArea() {
		return this.area;
	}

	public void setArea(BigDecimal area) {
		this.area = area;
	}

	@Column(name = "observacion", length = 256)
	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	@Column(name = "estado_tienda", length = 3)
	public String getEstadoTienda() {
		return this.estadoTienda;
	}

	public void setEstadoTienda(String estadoTienda) {
		this.estadoTienda = estadoTienda;
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
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblTienda")
	@JsonManagedReference(value="tablaManage-TablaBack")
	public Set<TblContrato> getTblContratos() {
		return tblContratos;
	}

	public void setTblContratos(Set<TblContrato> tblContratos) {
		this.tblContratos = tblContratos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblTienda")
	@JsonManagedReference(value="tablaManage-TablaBack")
	public Set<TblArbitrio> getTblArbitrios() {
		return tblArbitrios;
	}

	public void setTblArbitrios(Set<TblArbitrio> tblArbitrios) {
		this.tblArbitrios = tblArbitrios;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblTienda")
	@JsonManagedReference(value="tablaManage-TablaBack")
	public Set<TblLuzxtienda> getTblLuzxtiendas() {
		return tblLuzxtiendas;
	}

	public void setTblLuzxtiendas(Set<TblLuzxtienda> tblLuzxtiendas) {
		this.tblLuzxtiendas = tblLuzxtiendas;
	}

}