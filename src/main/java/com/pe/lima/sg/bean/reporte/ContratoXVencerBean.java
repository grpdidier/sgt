package com.pe.lima.sg.bean.reporte;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ContratoXVencerBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String 	nombreCliente;
	private String	numeroTienda;
	private Date	fechaVencimiento;
	private BigDecimal montoAlquiler;
	private BigDecimal montoServicio;
	
	
	public ContratoXVencerBean() {
		super();
	}
	
	public ContratoXVencerBean(String nombreCliente, String numeroTienda, Date fechaVencimiento,
			BigDecimal montoAlquiler, BigDecimal montoServicio) {
		super();
		this.nombreCliente = nombreCliente;
		this.numeroTienda = numeroTienda;
		this.fechaVencimiento = fechaVencimiento;
		this.montoAlquiler = montoAlquiler;
		this.montoServicio = montoServicio;
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
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	public BigDecimal getMontoAlquiler() {
		return montoAlquiler;
	}
	public void setMontoAlquiler(BigDecimal montoAlquiler) {
		this.montoAlquiler = montoAlquiler;
	}
	public BigDecimal getMontoServicio() {
		return montoServicio;
	}
	public void setMontoServicio(BigDecimal montoServicio) {
		this.montoServicio = montoServicio;
	}
	
	

}
