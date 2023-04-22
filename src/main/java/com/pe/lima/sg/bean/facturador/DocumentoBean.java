package com.pe.lima.sg.bean.facturador;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DocumentoBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String numeroTienda;
	private String nombreCliente;
	private String mes;
	private String anio;
	private BigDecimal montoCobrado;
	private String descripcion;
	private Date fechaFin;
	private Date fechaModificacion;
	private String tipoPersona;
	private String numeroDocumento;
	private Integer codigoEdificio;
	private Integer codigoCxC;
	
	public DocumentoBean() {
		super();
	}
	public String getNumeroTienda() {
		return numeroTienda;
	}
	public void setNumeroTienda(String numeroTienda) {
		this.numeroTienda = numeroTienda;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	public BigDecimal getMontoCobrado() {
		return montoCobrado;
	}
	public void setMontoCobrado(BigDecimal montoCobrado) {
		this.montoCobrado = montoCobrado;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public Date getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public String getTipoPersona() {
		return tipoPersona;
	}
	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}
	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public Integer getCodigoEdificio() {
		return codigoEdificio;
	}
	public void setCodigoEdificio(Integer codigoEdificio) {
		this.codigoEdificio = codigoEdificio;
	}
	public Integer getCodigoCxC() {
		return codigoCxC;
	}
	public void setCodigoCxC(Integer codigoCxC) {
		this.codigoCxC = codigoCxC;
	}
	
	
	
	
}
