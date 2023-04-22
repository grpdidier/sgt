package com.pe.lima.sg.bean.reporte;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class IngresoEgresoPdfBean  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String nombreUsuario;
	private BigDecimal subTotalSoles;
	private BigDecimal subTotalDolares;
	private List<IngresoEgresoBean> listaDatos;
	
	
	public IngresoEgresoPdfBean() {
		super();
		
	}


	public IngresoEgresoPdfBean(String nombreUsuario, BigDecimal subTotalSoles, BigDecimal subTotalDolares,
			List<IngresoEgresoBean> listaDatos) {
		super();
		this.nombreUsuario = nombreUsuario;
		this.subTotalSoles = subTotalSoles;
		this.subTotalDolares = subTotalDolares;
		this.listaDatos = listaDatos;
	}
	
	
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	public BigDecimal getSubTotalSoles() {
		return subTotalSoles;
	}
	public void setSubTotalSoles(BigDecimal subTotalSoles) {
		this.subTotalSoles = subTotalSoles;
	}
	public BigDecimal getSubTotalDolares() {
		return subTotalDolares;
	}
	public void setSubTotalDolares(BigDecimal subTotalDolares) {
		this.subTotalDolares = subTotalDolares;
	}
	public List<IngresoEgresoBean> getListaDatos() {
		return listaDatos;
	}
	public void setListaDatos(List<IngresoEgresoBean> listaDatos) {
		this.listaDatos = listaDatos;
	}
	
	

}
