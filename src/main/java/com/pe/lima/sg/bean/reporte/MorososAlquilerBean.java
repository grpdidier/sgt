package com.pe.lima.sg.bean.reporte;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MorososAlquilerBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String 	nombreCliente;
	private String	numeroTienda;
	private String tipoCobro;
	private BigDecimal tipoCambio;
	private String 	tipoMoneda;
	private Date	fechaVencimiento;
	private BigDecimal montoSoles;
	private BigDecimal montoDolares;
	
	
	
	public MorososAlquilerBean() {
		super();
	}
	
	public MorososAlquilerBean(String nombreCliente, String numeroTienda, String tipoCobro, BigDecimal tipoCambio,
			String tipoMoneda, Date fechaVencimiento, BigDecimal montoSoles, BigDecimal montoDolares) {
		super();
		this.nombreCliente = nombreCliente;
		this.numeroTienda = numeroTienda;
		this.tipoCobro = tipoCobro;
		this.tipoCambio = tipoCambio;
		this.tipoMoneda = tipoMoneda;
		this.fechaVencimiento = fechaVencimiento;
		this.montoSoles = montoSoles;
		this.montoDolares = montoDolares;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public String getNumeroTienda() {
		return numeroTienda;
	}
	public void setNumeroTienda(String numeroTienda) {
		this.numeroTienda = numeroTienda;
	}
	public String getTipoCobro() {
		return tipoCobro;
	}
	public void setTipoCobro(String tipoCobro) {
		this.tipoCobro = tipoCobro;
	}
	public BigDecimal getTipoCambio() {
		return tipoCambio;
	}
	public void setTipoCambio(BigDecimal tipoCambio) {
		this.tipoCambio = tipoCambio;
	}
	public String getTipoMoneda() {
		return tipoMoneda;
	}
	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}
	
	public BigDecimal getMontoSoles() {
		return montoSoles;
	}
	public void setMontoSoles(BigDecimal montoSoles) {
		this.montoSoles = montoSoles;
	}
	public BigDecimal getMontoDolares() {
		return montoDolares;
	}
	public void setMontoDolares(BigDecimal montoDolares) {
		this.montoDolares = montoDolares;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	
	
	

}
