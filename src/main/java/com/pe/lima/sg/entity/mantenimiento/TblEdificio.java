package com.pe.lima.sg.entity.mantenimiento;
// Generated 18/09/2017 01:36:18 PM by Hibernate Tools 5.2.3.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * TblEdificio generated by hbm2java
 */
@Entity
@Table(name = "tbl_edificio", schema = "mae")
public class TblEdificio implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private int codigoEdificio;
	private String nombre;
	private String descripcion;
	private String direccion;
	private String estado;
	private Integer usuarioCreacion;
	private Integer usuarioModificacion;
	private Date fechaCreacion;
	private Date fechaModificacion;
	private String ipCreacion;
	private String ipModificacion;
	private Set<TblTienda> tblTiendas = new HashSet<TblTienda>(0);

	public TblEdificio() {
	}

	public TblEdificio(int codigoEdificio) {
		this.codigoEdificio = codigoEdificio;
	}

	public TblEdificio(int codigoEdificio, String nombre, String descripcion, String direccion, String estado,
			Integer usuarioCreacion, Integer usuarioModificacion, Date fechaCreacion, Date fechaModificacion,
			String ipCreacion, String ipModificacion, Set<TblTienda> tblTiendas) {
		this.codigoEdificio = codigoEdificio;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.direccion = direccion;
		this.estado = estado;
		this.usuarioCreacion = usuarioCreacion;
		this.usuarioModificacion = usuarioModificacion;
		this.fechaCreacion = fechaCreacion;
		this.fechaModificacion = fechaModificacion;
		this.ipCreacion = ipCreacion;
		this.ipModificacion = ipModificacion;
		this.tblTiendas = tblTiendas;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "codigo_edificio", unique = true, nullable = false)
	public int getCodigoEdificio() {
		return this.codigoEdificio;
	}

	public void setCodigoEdificio(int codigoEdificio) {
		this.codigoEdificio = codigoEdificio;
	}

	@Column(name = "nombre", length = 128)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "descripcion", length = 256)
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "direccion", length = 256)
	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblEdificio")
	@JsonManagedReference(value="tablaManage-TablaBack")
	public Set<TblTienda> getTblTiendas() {
		return this.tblTiendas;
	}

	public void setTblTiendas(Set<TblTienda> tblTiendas) {
		this.tblTiendas = tblTiendas;
	}

}