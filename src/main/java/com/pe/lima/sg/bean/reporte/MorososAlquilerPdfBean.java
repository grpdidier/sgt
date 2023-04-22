package com.pe.lima.sg.bean.reporte;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class MorososAlquilerPdfBean  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String nombreCliente;
	private BigDecimal subTotalSoles;
	private BigDecimal subTotalDolares;
	private List<MorososAlquilerBean> listaDatos;
	
	
	public MorososAlquilerPdfBean() {
		super();
		
	}


	public MorososAlquilerPdfBean(String nombreCliente, BigDecimal subTotalSoles, BigDecimal subTotalDolares,
			List<MorososAlquilerBean> listaDatos) {
		super();
		this.nombreCliente = nombreCliente;
		this.subTotalSoles = subTotalSoles;
		this.subTotalDolares = subTotalDolares;
		this.listaDatos = listaDatos;
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
	public List<MorososAlquilerBean> getListaDatos() {
		return listaDatos;
	}
	public void setListaDatos(List<MorososAlquilerBean> listaDatos) {
		this.listaDatos = listaDatos;
	}


	public String getNombreCliente() {
		return nombreCliente;
	}


	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	
	

}
