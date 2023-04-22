package com.pe.lima.sg.bean.caja;

import java.io.Serializable;

public class BitacoraBean  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer anio;
	private String 	mes;
	private String 	tipoCobro;
	private String 	alquiler;
	private String 	servicio;
	private String	luz;
	private	String	arbitrio;
	private Integer codigoContrato;
	
	public BitacoraBean() {
		super();
	}


	public BitacoraBean(Integer anio, String mes, String alquiler, String servicio, String luz, String arbitrio) {
		super();
		this.anio = anio;
		this.mes = mes;
		this.alquiler = alquiler;
		this.servicio = servicio;
		this.luz = luz;
		this.arbitrio = arbitrio;
	}


	public Integer getAnio() {
		return anio;
	}


	public void setAnio(Integer anio) {
		this.anio = anio;
	}


	public String getMes() {
		return mes;
	}


	public void setMes(String mes) {
		this.mes = mes;
	}


	public String getAlquiler() {
		return alquiler;
	}


	public void setAlquiler(String alquiler) {
		this.alquiler = alquiler;
	}


	public String getServicio() {
		return servicio;
	}


	public void setServicio(String servicio) {
		this.servicio = servicio;
	}


	public String getLuz() {
		return luz;
	}


	public void setLuz(String luz) {
		this.luz = luz;
	}


	public String getArbitrio() {
		return arbitrio;
	}


	public void setArbitrio(String arbitrio) {
		this.arbitrio = arbitrio;
	}


	public String getTipoCobro() {
		return tipoCobro;
	}


	public void setTipoCobro(String tipoCobro) {
		this.tipoCobro = tipoCobro;
	}


	public Integer getCodigoContrato() {
		return codigoContrato;
	}


	public void setCodigoContrato(Integer codigoContrato) {
		this.codigoContrato = codigoContrato;
	}
	
	
	

	
}
