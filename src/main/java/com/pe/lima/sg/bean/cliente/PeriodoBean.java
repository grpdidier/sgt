package com.pe.lima.sg.bean.cliente;

public class PeriodoBean {
	private int anio;
	private int mes;
	
	
	public PeriodoBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public PeriodoBean(int anio, int mes) {
		super();
		this.anio = anio;
		this.mes = mes;
	}
	public int getAnio() {
		return anio;
	}
	public void setAnio(int anio) {
		this.anio = anio;
	}
	public int getMes() {
		return mes;
	}
	public void setMes(int mes) {
		this.mes = mes;
	}
	
}
