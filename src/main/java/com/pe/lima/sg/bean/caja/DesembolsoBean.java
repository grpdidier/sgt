package com.pe.lima.sg.bean.caja;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class DesembolsoBean  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Date fechaCobro;
	private BigDecimal montoSoles;
	private BigDecimal montoDolares;
	private BigDecimal tipoCambio;
	private String tipoCobro;
	private String tipoOperacion;
	private Integer codigoDesembolso;
	private Integer codigoContrato;
	private List<CobroBean> listaCobro;
	private Date fechaCreacion;
	private Integer estadoOperacion;
	
	public DesembolsoBean() {
		super();
	}

	public Date getFechaCobro() {
		return fechaCobro;
	}

	public void setFechaCobro(Date fechaCobro) {
		this.fechaCobro = fechaCobro;
	}

	public BigDecimal getMontoSoles() {
		return montoSoles;
	}

	public void setMontoSoles(BigDecimal montoSoles) {
		this.montoSoles = montoSoles;
	}

	public BigDecimal getMontoDolares() {
		return montoDolares;
	}

	public void setMontoDolares(BigDecimal montoDolares) {
		this.montoDolares = montoDolares;
	}

	public BigDecimal getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(BigDecimal tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public String getTipoCobro() {
		return tipoCobro;
	}

	public void setTipoCobro(String tipoCobro) {
		this.tipoCobro = tipoCobro;
	}

	public String getTipoOperacion() {
		return tipoOperacion;
	}

	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	public Integer getCodigoDesembolso() {
		return codigoDesembolso;
	}

	public void setCodigoDesembolso(Integer codigoDesembolso) {
		this.codigoDesembolso = codigoDesembolso;
	}

	public Integer getCodigoContrato() {
		return codigoContrato;
	}

	public void setCodigoContrato(Integer codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	public List<CobroBean> getListaCobro() {
		return listaCobro;
	}

	public void setListaCobro(List<CobroBean> listaCobro) {
		this.listaCobro = listaCobro;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public Integer getEstadoOperacion() {
		return estadoOperacion;
	}

	public void setEstadoOperacion(Integer estadoOperacion) {
		this.estadoOperacion = estadoOperacion;
	}

}
