package com.pe.lima.sg.bean.caja;

import java.math.BigDecimal;
import java.util.Date;

public class GastoBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private int codigoGasto;
	private String nombreEntidad;
	private Integer codigoEntidad;
	private Integer codigoTipoGasto;
	private String nombreTipoGasto;
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
	private String nombreEdificacion;
	
	
	public GastoBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public GastoBean(int codigoGasto, String nombreEntidad, Integer codigoEntidad, Integer codigoTipoGasto,
			String nombreTipoGasto, BigDecimal monto, String tipoMoneda, Date fechaGasto, String observacion,
			String estado, Integer usuarioCreacion, Integer usuarioModificacion, Date fechaCreacion,
			Date fechaModificacion, String ipCreacion, String ipModificacion, String codigoInterno) {
		super();
		this.codigoGasto = codigoGasto;
		this.nombreEntidad = nombreEntidad;
		this.codigoEntidad = codigoEntidad;
		this.codigoTipoGasto = codigoTipoGasto;
		this.nombreTipoGasto = nombreTipoGasto;
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
		this.codigoInterno = codigoInterno;
	}
	public int getCodigoGasto() {
		return codigoGasto;
	}
	public void setCodigoGasto(int codigoGasto) {
		this.codigoGasto = codigoGasto;
	}
	public String getNombreEntidad() {
		return nombreEntidad;
	}
	public void setNombreEntidad(String nombreEntidad) {
		this.nombreEntidad = nombreEntidad;
	}
	public Integer getCodigoEntidad() {
		return codigoEntidad;
	}
	public void setCodigoEntidad(Integer codigoEntidad) {
		this.codigoEntidad = codigoEntidad;
	}
	public Integer getCodigoTipoGasto() {
		return codigoTipoGasto;
	}
	public void setCodigoTipoGasto(Integer codigoTipoGasto) {
		this.codigoTipoGasto = codigoTipoGasto;
	}
	public String getNombreTipoGasto() {
		return nombreTipoGasto;
	}
	public void setNombreTipoGasto(String nombreTipoGasto) {
		this.nombreTipoGasto = nombreTipoGasto;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public String getTipoMoneda() {
		return tipoMoneda;
	}
	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}
	public Date getFechaGasto() {
		return fechaGasto;
	}
	public void setFechaGasto(Date fechaGasto) {
		this.fechaGasto = fechaGasto;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Integer getUsuarioCreacion() {
		return usuarioCreacion;
	}
	public void setUsuarioCreacion(Integer usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}
	public Integer getUsuarioModificacion() {
		return usuarioModificacion;
	}
	public void setUsuarioModificacion(Integer usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public Date getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public String getIpCreacion() {
		return ipCreacion;
	}
	public void setIpCreacion(String ipCreacion) {
		this.ipCreacion = ipCreacion;
	}
	public String getIpModificacion() {
		return ipModificacion;
	}
	public void setIpModificacion(String ipModificacion) {
		this.ipModificacion = ipModificacion;
	}
	public String getCodigoInterno() {
		return codigoInterno;
	}
	public void setCodigoInterno(String codigoInterno) {
		this.codigoInterno = codigoInterno;
	}
	public String getNombreEdificacion() {
		return nombreEdificacion;
	}
	public void setNombreEdificacion(String nombreEdificacion) {
		this.nombreEdificacion = nombreEdificacion;
	}
}
