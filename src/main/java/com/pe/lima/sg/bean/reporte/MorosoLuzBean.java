package com.pe.lima.sg.bean.reporte;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MorosoLuzBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String 	nombreCliente;
	private String	numeroTienda;
	private Date	fechaVencimiento;
	private BigDecimal montoLuz;
	
	
	public MorosoLuzBean() {
		super();
	}
	
	public MorosoLuzBean(String nombreCliente, String numeroTienda, Date fechaVencimiento,
			BigDecimal montoLuz) {
		super();
		this.nombreCliente = nombreCliente;
		this.numeroTienda = numeroTienda;
		this.fechaVencimiento = fechaVencimiento;
		this.montoLuz = montoLuz;
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

	public BigDecimal getMontoLuz() {
		return montoLuz;
	}

	public void setMontoLuz(BigDecimal montoLuz) {
		this.montoLuz = montoLuz;
	}
	
	

}
