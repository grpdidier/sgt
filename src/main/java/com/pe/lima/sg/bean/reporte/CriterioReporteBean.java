package com.pe.lima.sg.bean.reporte;

import java.io.Serializable;

public class CriterioReporteBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private String fechaInicio;
	private String fechaFin;
	private String tipoConcepto;
	
	
	public CriterioReporteBean() {
		super();
	}
	public String getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public String getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}
	public String getTipoConcepto() {
		return tipoConcepto;
	}
	public void setTipoConcepto(String tipoConcepto) {
		this.tipoConcepto = tipoConcepto;
	}
	
	
}
