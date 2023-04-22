package com.pe.lima.sg.bean.cliente;

import java.io.Serializable;
import java.math.BigDecimal;

public class LuzBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer codigoInmueble;
	private Integer codigoSuministro;
	private Integer intAnio;
	private String strMes;
	private BigDecimal montoGenerado;
	
	
	public Integer getCodigoInmueble() {
		return codigoInmueble;
	}
	public void setCodigoInmueble(Integer codigoInmueble) {
		this.codigoInmueble = codigoInmueble;
	}
	public Integer getCodigoSuministro() {
		return codigoSuministro;
	}
	public void setCodigoSuministro(Integer codigoSuministro) {
		this.codigoSuministro = codigoSuministro;
	}
	public Integer getIntAnio() {
		return intAnio;
	}
	public void setIntAnio(Integer intAnio) {
		this.intAnio = intAnio;
	}
	public String getStrMes() {
		return strMes;
	}
	public void setStrMes(String strMes) {
		this.strMes = strMes;
	}
	public BigDecimal getMontoGenerado() {
		return montoGenerado;
	}
	public void setMontoGenerado(BigDecimal montoGenerado) {
		this.montoGenerado = montoGenerado;
	}
	
	
}
