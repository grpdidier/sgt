package com.pe.lima.sg.facturador.bean;

public class ErrorBean {
	private String descripcion;
	private String nombreCampo;
	private boolean estado;
	
	
	public ErrorBean() {
		super();
	}
	public ErrorBean(String descripcion, String nombreCampo) {
		super();
		this.descripcion = descripcion;
		this.nombreCampo = nombreCampo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getNombreCampo() {
		return nombreCampo;
	}
	public void setNombreCampo(String nombreCampo) {
		this.nombreCampo = nombreCampo;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	
}
