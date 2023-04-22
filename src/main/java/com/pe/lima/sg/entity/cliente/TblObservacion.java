package com.pe.lima.sg.entity.cliente;
// Generated 2/11/2017 04:52:26 AM by Hibernate Tools 4.3.5.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TblObservacion generated by hbm2java
 */
@Entity
@Table(name = "tbl_observacion", schema = "cli")
public class TblObservacion implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private int codigoObservacion;
	private String asunto;
	private String breveDescripcion;
	private String descripcion;
	private String estado;
	private Integer usuarioCreacion;
	private Integer usuarioModificacion;
	private Date fechaCreacion;
	private Date fechaModificacion;
	private String ipCreacion;
	private String ipModificacion;
	private int codigoContrato;

	public TblObservacion() {
	}

	public TblObservacion(int codigoObservacion, int codigoContrato) {
		this.codigoObservacion = codigoObservacion;
		this.codigoContrato = codigoContrato;
	}

	public TblObservacion(int codigoObservacion, String asunto, String breveDescripcion, String descripcion,
			String estado, Integer usuarioCreacion, Integer usuarioModificacion, Date fechaCreacion,
			Date fechaModificacion, String ipCreacion, String ipModificacion, int codigoContrato) {
		this.codigoObservacion = codigoObservacion;
		this.asunto = asunto;
		this.breveDescripcion = breveDescripcion;
		this.descripcion = descripcion;
		this.estado = estado;
		this.usuarioCreacion = usuarioCreacion;
		this.usuarioModificacion = usuarioModificacion;
		this.fechaCreacion = fechaCreacion;
		this.fechaModificacion = fechaModificacion;
		this.ipCreacion = ipCreacion;
		this.ipModificacion = ipModificacion;
		this.codigoContrato = codigoContrato;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "codigo_observacion", unique = true, nullable = false)
	public int getCodigoObservacion() {
		return this.codigoObservacion;
	}

	public void setCodigoObservacion(int codigoObservacion) {
		this.codigoObservacion = codigoObservacion;
	}

	@Column(name = "asunto", length = 128)
	public String getAsunto() {
		return this.asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	@Column(name = "breve_descripcion", length = 256)
	public String getBreveDescripcion() {
		return this.breveDescripcion;
	}

	public void setBreveDescripcion(String breveDescripcion) {
		this.breveDescripcion = breveDescripcion;
	}

	@Column(name = "descripcion", length = 1024)
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

	@Column(name = "ip_modificacion", length = 64)
	public String getIpModificacion() {
		return this.ipModificacion;
	}

	public void setIpModificacion(String ipModificacion) {
		this.ipModificacion = ipModificacion;
	}

	@Column(name = "codigo_contrato", nullable = false)
	public int getCodigoContrato() {
		return this.codigoContrato;
	}

	public void setCodigoContrato(int codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

}
