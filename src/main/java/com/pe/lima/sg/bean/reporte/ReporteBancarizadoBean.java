package com.pe.lima.sg.bean.reporte;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReporteBancarizadoBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String inmueble;
	private List<BancarizadoBean> listaBancarizado;
	private BigDecimal efectivoSubTotalSoles;
	private BigDecimal efectivoSubTotalDolares;
	private BigDecimal bancarizadoSubTotalSoles;
	private BigDecimal bancarizadoSubTotalDolares;
	
	public ReporteBancarizadoBean(){
		this.listaBancarizado = new ArrayList<BancarizadoBean>();
		this.efectivoSubTotalDolares = new BigDecimal("0");
		this.efectivoSubTotalSoles = new BigDecimal("0");
		this.bancarizadoSubTotalDolares = new BigDecimal("0");
		this.bancarizadoSubTotalSoles = new BigDecimal("0");
	}

	public String getInmueble() {
		return inmueble;
	}

	public void setInmueble(String inmueble) {
		this.inmueble = inmueble;
	}

	public List<BancarizadoBean> getListaBancarizado() {
		return listaBancarizado;
	}

	public void setListaBancarizado(List<BancarizadoBean> listaBancarizado) {
		this.listaBancarizado = listaBancarizado;
	}

	public BigDecimal getEfectivoSubTotalSoles() {
		return efectivoSubTotalSoles;
	}

	public void setEfectivoSubTotalSoles(BigDecimal efectivoSubTotalSoles) {
		this.efectivoSubTotalSoles = efectivoSubTotalSoles;
	}

	public BigDecimal getEfectivoSubTotalDolares() {
		return efectivoSubTotalDolares;
	}

	public void setEfectivoSubTotalDolares(BigDecimal efectivoSubTotalDolares) {
		this.efectivoSubTotalDolares = efectivoSubTotalDolares;
	}

	public BigDecimal getBancarizadoSubTotalSoles() {
		return bancarizadoSubTotalSoles;
	}

	public void setBancarizadoSubTotalSoles(BigDecimal bancarizadoSubTotalSoles) {
		this.bancarizadoSubTotalSoles = bancarizadoSubTotalSoles;
	}

	public BigDecimal getBancarizadoSubTotalDolares() {
		return bancarizadoSubTotalDolares;
	}

	public void setBancarizadoSubTotalDolares(BigDecimal bancarizadoSubTotalDolares) {
		this.bancarizadoSubTotalDolares = bancarizadoSubTotalDolares;
	}

}
