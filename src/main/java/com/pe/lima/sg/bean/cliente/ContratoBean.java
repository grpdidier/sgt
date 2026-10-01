package com.pe.lima.sg.bean.cliente;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ContratoBean  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String numero;
	private String estado;
	private String nombre;
	private String fecha_inicio;
	private String fecha_fin;
	private BigDecimal nuevoMontoAlquiler;
	private BigDecimal nuevoMontoServicio;
	private String fechaAlquiler;
	private String fechaServicio;
	
	
	
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



}
