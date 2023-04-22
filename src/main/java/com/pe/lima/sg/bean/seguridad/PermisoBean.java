package com.pe.lima.sg.bean.seguridad;

import java.io.Serializable;

public class PermisoBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private int codigoPermiso;
	private String nombreModulo;
	private String nombreOpcion;
	private String descripcion;
	private String estado;
	private String asignado = "N";
	
	public int getCodigoPermiso() {
		return codigoPermiso;
	}
	public void setCodigoPermiso(int codigoPermiso) {
		this.codigoPermiso = codigoPermiso;
	}
	public String getNombreModulo() {
		return nombreModulo;
	}
	public void setNombreModulo(String nombreModulo) {
		this.nombreModulo = nombreModulo;
	}
	public String getNombreOpcion() {
		return nombreOpcion;
	}
	public void setNombreOpcion(String nombreOpcion) {
		this.nombreOpcion = nombreOpcion;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getAsignado() {
		return asignado;
	}
	public void setAsignado(String asignado) {
		this.asignado = asignado;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
}
