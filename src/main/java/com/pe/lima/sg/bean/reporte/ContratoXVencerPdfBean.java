package com.pe.lima.sg.bean.reporte;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ContratoXVencerPdfBean  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String nombreCliente;
	private BigDecimal subTotalAlquiler;
	private BigDecimal subTotalServicio;
	private List<ContratoXVencerBean> listaDatos;
	
	
	
	
	public ContratoXVencerPdfBean() {
		super();
	}
	
	public ContratoXVencerPdfBean(String nombreCliente, BigDecimal subTotalAlquiler, BigDecimal subTotalServicio,
			List<ContratoXVencerBean> listaDatos) {
		super();
		this.nombreCliente = nombreCliente;
		this.subTotalAlquiler = subTotalAlquiler;
		this.subTotalServicio = subTotalServicio;
		this.listaDatos = listaDatos;
	}
	
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public BigDecimal getSubTotalAlquiler() {
		return subTotalAlquiler;
	}
	public void setSubTotalAlquiler(BigDecimal subTotalAlquiler) {
		this.subTotalAlquiler = subTotalAlquiler;
	}
	public BigDecimal getSubTotalServicio() {
		return subTotalServicio;
	}
	public void setSubTotalServicio(BigDecimal subTotalServicio) {
		this.subTotalServicio = subTotalServicio;
	}
	public List<ContratoXVencerBean> getListaDatos() {
		return listaDatos;
	}
	public void setListaDatos(List<ContratoXVencerBean> listaDatos) {
		this.listaDatos = listaDatos;
	}
	
	

}
