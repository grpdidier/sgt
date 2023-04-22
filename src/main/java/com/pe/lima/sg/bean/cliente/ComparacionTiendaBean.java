package com.pe.lima.sg.bean.cliente;

import java.io.Serializable;
import java.math.BigDecimal;

public class ComparacionTiendaBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer intIguales;
	private Integer intOcupadoDesocupado;
	private Integer intDesocupadoOcupado;
	
	//Campos para las operaciones de calculo del local o tienda
	private Integer codigoTienda;
	private BigDecimal montoTienda;
	private double  porcentaje;
	private BigDecimal nuevoMonto;
	
	
	
	public Integer getIntIguales() {
		return intIguales;
	}
	public void setIntIguales(Integer intIguales) {
		this.intIguales = intIguales;
	}
	public Integer getIntOcupadoDesocupado() {
		return intOcupadoDesocupado;
	}
	public void setIntOcupadoDesocupado(Integer intOcupadoDesocupado) {
		this.intOcupadoDesocupado = intOcupadoDesocupado;
	}
	public Integer getIntDesocupadoOcupado() {
		return intDesocupadoOcupado;
	}
	public void setIntDesocupadoOcupado(Integer intDesocupadoOcupado) {
		this.intDesocupadoOcupado = intDesocupadoOcupado;
	}
	public Integer getCodigoTienda() {
		return codigoTienda;
	}
	public void setCodigoTienda(Integer codigoTienda) {
		this.codigoTienda = codigoTienda;
	}
	public BigDecimal getMontoTienda() {
		return montoTienda;
	}
	public void setMontoTienda(BigDecimal montoTienda) {
		this.montoTienda = montoTienda;
	}
	public double getPorcentaje() {
		return porcentaje;
	}
	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}
	public BigDecimal getNuevoMonto() {
		return nuevoMonto;
	}
	public void setNuevoMonto(BigDecimal nuevoMonto) {
		this.nuevoMonto = nuevoMonto;
	}
	
	
}
