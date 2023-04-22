package com.pe.lima.sg.bean.cliente;

import java.math.BigDecimal;


public class TiendaArbitrioBean   implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	private int codigoTienda;
	private int codigoEdificio;
	private String nombre;
	private String tipo;
	private String piso;
	private String numero;
	private BigDecimal area;
	private String observacion;
	private String estadoTienda;
	private Integer anio;
	
	
	public int getCodigoTienda() {
		return codigoTienda;
	}
	public void setCodigoTienda(int codigoTienda) {
		this.codigoTienda = codigoTienda;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getPiso() {
		return piso;
	}
	public void setPiso(String piso) {
		this.piso = piso;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public BigDecimal getArea() {
		return area;
	}
	public void setArea(BigDecimal area) {
		this.area = area;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public String getEstadoTienda() {
		return estadoTienda;
	}
	public void setEstadoTienda(String estadoTienda) {
		this.estadoTienda = estadoTienda;
	}
	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	public int getCodigoEdificio() {
		return codigoEdificio;
	}
	public void setCodigoEdificio(int codigoEdificio) {
		this.codigoEdificio = codigoEdificio;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
}
