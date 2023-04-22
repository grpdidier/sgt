package com.pe.lima.sg.bean.cliente;

import java.io.Serializable;

public class ContratoBean  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String numero;
	private String estado;
	private String nombre;
	private String fecha_inicio;
	private String fecha_fin;
	
	
	
	public ContratoBean(String numero, String estado, String nombre, String fecha_inicio, String fecha_fin) {
		super();
		this.numero = numero;
		this.estado = estado;
		this.nombre = nombre;
		this.fecha_inicio = fecha_inicio;
		this.fecha_fin = fecha_fin;
	}
	
	
	public ContratoBean() {
		super();
	}


	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getFecha_inicio() {
		return fecha_inicio;
	}
	public void setFecha_inicio(String fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}
	public String getFecha_fin() {
		return fecha_fin;
	}
	public void setFecha_fin(String fecha_fin) {
		this.fecha_fin = fecha_fin;
	}
	

}
