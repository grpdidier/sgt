package com.pe.lima.sg.bean.cliente;

import java.math.BigDecimal;
import java.util.Date;



public class TiendaSuministroBean  implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	private int codigoTienda;
	private int codigoEdificio;
	private int codigoContrato;
	private int codigoLuzxtienda;
	private int codigoLuz;
	private String nombre;
	private String tipo;
	private String piso;
	private String numero;
	private String numeroSuministro;
	private BigDecimal area;
	private String observacion;
	private String estadoTienda;
	private Integer anio;	
	private String mes;
	private BigDecimal montoGenerado;
	private BigDecimal montoContrato;
	private BigDecimal valorCobrado;
	private BigDecimal saldo;
	private Date fechaInicio;
	private Date fechaFin;
	
	public int getCodigoTienda() {
		return codigoTienda;
	}
	public void setCodigoTienda(int codigoTienda) {
		this.codigoTienda = codigoTienda;
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
	public int getCodigoLuzxtienda() {
		return codigoLuzxtienda;
	}
	public void setCodigoLuzxtienda(int codigoLuzxtienda) {
		this.codigoLuzxtienda = codigoLuzxtienda;
	}
	public BigDecimal getMontoGenerado() {
		return montoGenerado;
	}
	public void setMontoGenerado(BigDecimal montoGenerado) {
		this.montoGenerado = montoGenerado;
	}
	public BigDecimal getMontoContrato() {
		return montoContrato;
	}
	public void setMontoContrato(BigDecimal montoContrato) {
		this.montoContrato = montoContrato;
	}
	public BigDecimal getValorCobrado() {
		return valorCobrado;
	}
	public void setValorCobrado(BigDecimal valorCobrado) {
		this.valorCobrado = valorCobrado;
	}
	public BigDecimal getSaldo() {
		return saldo;
	}
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public int getCodigoContrato() {
		return codigoContrato;
	}
	public void setCodigoContrato(int codigoContrato) {
		this.codigoContrato = codigoContrato;
	}
	public int getCodigoLuz() {
		return codigoLuz;
	}
	public void setCodigoLuz(int codigoLuz) {
		this.codigoLuz = codigoLuz;
	}
	public String getNumeroSuministro() {
		return numeroSuministro;
	}
	public void setNumeroSuministro(String numeroSuministro) {
		this.numeroSuministro = numeroSuministro;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
}
