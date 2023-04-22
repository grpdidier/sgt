package com.pe.lima.sg.bean.reporte;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class MorosoLuzPdfBean  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String nombreCliente;
	private BigDecimal subTotalLuz;
	private List<MorosoLuzBean> listaDatos;
	
	
	
	
	public MorosoLuzPdfBean() {
		super();
	}
	
	public MorosoLuzPdfBean(String nombreCliente, BigDecimal subTotalLuz,
			List<MorosoLuzBean> listaDatos) {
		super();
		this.nombreCliente = nombreCliente;
		this.subTotalLuz = subTotalLuz;
		this.listaDatos = listaDatos;
	}
	
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	

	public BigDecimal getSubTotalLuz() {
		return subTotalLuz;
	}

	public void setSubTotalLuz(BigDecimal subTotalLuz) {
		this.subTotalLuz = subTotalLuz;
	}

	public List<MorosoLuzBean> getListaDatos() {
		return listaDatos;
	}

	public void setListaDatos(List<MorosoLuzBean> listaDatos) {
		this.listaDatos = listaDatos;
	}

	

}
