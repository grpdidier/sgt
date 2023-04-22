package com.pe.lima.sg.entity.seguridad;
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
 * TblPerfil generated by hbm2java
 */
@Entity
@Table(name = "tbl_perfil", schema = "seg")
public class TblPerfil implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private int codigoPerfil;
	private String nombre;
	private String descripcion;
	private String estadoPerfil;
	private String estado;
	private Integer usuarioCreacion;
	private Integer usuarioModificacion;
	private Date fechaCreacion;
	private Date fechaModificacion;
	private String ipCreacion;
	private String ipModificacion;
	private Set<TblPerfilOpcion> tblPerfilOpcions = new HashSet<TblPerfilOpcion>(0);	
	private Set<TblUsuario> tblUsuarios = new HashSet<TblUsuario>(0);

	public TblPerfil() {
	}

	public TblPerfil(int codigoPerfil) {
		this.codigoPerfil = codigoPerfil;
	}

	public TblPerfil(int codigoPerfil, String nombre, String descripcion, String estadoPerfil, String estado, Integer usuarioCreacion,
			Integer usuarioModificacion, Date fechaCreacion, Date fechaModificacion, String ipCreacion,
			String ipModificacion, Set<TblPerfilOpcion> tblPerfilOpcions, Set<TblUsuario> tblUsuarios) {
		this.codigoPerfil = codigoPerfil;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.estadoPerfil = estadoPerfil;
		this.estado = estado;
		this.usuarioCreacion = usuarioCreacion;
		this.usuarioModificacion = usuarioModificacion;
		this.fechaCreacion = fechaCreacion;
		this.fechaModificacion = fechaModificacion;
		this.ipCreacion = ipCreacion;
		this.ipModificacion = ipModificacion;
		this.tblPerfilOpcions = tblPerfilOpcions;
		this.tblUsuarios = tblUsuarios;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "codigo_perfil", unique = true, nullable = false)
	public int getCodigoPerfil() {
		return this.codigoPerfil;
	}

	public void setCodigoPerfil(int codigoPerfil) {
		this.codigoPerfil = codigoPerfil;
	}

	@Column(name = "nombre", length = 64)
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

	@Column(name = "ip_modificacion")
	public String getIpModificacion() {
		return this.ipModificacion;
	}

	public void setIpModificacion(String ipModificacion) {
		this.ipModificacion = ipModificacion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblPerfil")
	@JsonManagedReference(value="tablaManage-TablaBack")
	public Set<TblPerfilOpcion> getTblPerfilOpcions() {
		return this.tblPerfilOpcions;
	}

	public void setTblPerfilOpcions(Set<TblPerfilOpcion> tblPerfilOpcions) {
		this.tblPerfilOpcions = tblPerfilOpcions;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblPerfil")
	@JsonManagedReference(value="tablaManage-TablaBack")
	public Set<TblUsuario> getTblUsuarios() {
		return this.tblUsuarios;
	}

	public void setTblUsuarios(Set<TblUsuario> tblUsuarios) {
		this.tblUsuarios = tblUsuarios;
	}

	@Column(name = "estado_perfil", length = 1)
	public String getEstadoPerfil() {
		return estadoPerfil;
	}

	public void setEstadoPerfil(String estadoPerfil) {
		this.estadoPerfil = estadoPerfil;
	}

}
