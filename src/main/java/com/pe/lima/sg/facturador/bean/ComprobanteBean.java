package com.pe.lima.sg.facturador.bean;

import java.util.List;


import com.pe.lima.sg.facturador.entity.TblComprobante;
import com.pe.lima.sg.facturador.entity.TblDetalleComprobante;
import com.pe.lima.sg.facturador.entity.TblLeyenda;
import com.pe.lima.sg.facturador.entity.TblTributoGeneral;
import com.pe.lima.sg.facturador.entity.TblTributoGeneralNota;

public class ComprobanteBean {
	private TblComprobante comprobante;
	private TblLeyenda leyenda;
	private List<TblDetalleComprobante> listaDetalle;
	/*Datos adicionales*/
	private String  tipoAfectacion;
	private Integer valorServicio;
	private Integer valorIGV;
	private List<TblTributoGeneral> listaTributo		= null;
	private List<TblTributoGeneralNota> listaTributoNota= null;
	private String sunatData;
	private boolean resultadoValidacion;
	private Integer codigoCxC;
	

	public ComprobanteBean() {
		super();
	}
	
	public TblComprobante getComprobante() {
		return comprobante;
	}
	public void setComprobante(TblComprobante comprobante) {
		this.comprobante = comprobante;
	}
	public List<TblDetalleComprobante> getListaDetalle() {
		return listaDetalle;
	}
	public void setListaDetalle(List<TblDetalleComprobante> listaDetalle) {
		this.listaDetalle = listaDetalle;
	}
	public TblLeyenda getLeyenda() {
		return leyenda;
	}
	public void setLeyenda(TblLeyenda leyenda) {
		this.leyenda = leyenda;
	}

	public String getTipoAfectacion() {
		return tipoAfectacion;
	}

	public void setTipoAfectacion(String tipoAfectacion) {
		this.tipoAfectacion = tipoAfectacion;
	}

	public Integer getValorServicio() {
		return valorServicio;
	}

	public void setValorServicio(Integer valorServicio) {
		this.valorServicio = valorServicio;
	}

	public Integer getValorIGV() {
		return valorIGV;
	}

	public void setValorIGV(Integer valorIGV) {
		this.valorIGV = valorIGV;
	}

	public List<TblTributoGeneral> getListaTributo() {
		return listaTributo;
	}

	public void setListaTributo(List<TblTributoGeneral> listaTributo) {
		this.listaTributo = listaTributo;
	}

	public List<TblTributoGeneralNota> getListaTributoNota() {
		return listaTributoNota;
	}

	public void setListaTributoNota(List<TblTributoGeneralNota> listaTributoNota) {
		this.listaTributoNota = listaTributoNota;
	}

	public String getSunatData() {
		return sunatData;
	}

	public void setSunatData(String sunatData) {
		this.sunatData = sunatData;
	}

	public boolean isResultadoValidacion() {
		return resultadoValidacion;
	}

	public void setResultadoValidacion(boolean resultadoValidacion) {
		this.resultadoValidacion = resultadoValidacion;
	}

	public Integer getCodigoCxC() {
		return codigoCxC;
	}

	public void setCodigoCxC(Integer codigoCxC) {
		this.codigoCxC = codigoCxC;
	}

	
	
	
}
