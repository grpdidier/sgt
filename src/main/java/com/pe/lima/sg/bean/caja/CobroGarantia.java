package com.pe.lima.sg.bean.caja;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CobroGarantia  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Date fechaCobroGarantia;
	private BigDecimal montoSolesGarantia;
	private BigDecimal montoDolaresGarantia;
	private BigDecimal tipoCambioGarantia;
	private String tipoMonedaGarantia;
	
	public CobroGarantia() {
		super();
	}

	public Date getFechaCobroGarantia() {
		return fechaCobroGarantia;
	}

	public void setFechaCobroGarantia(Date fechaCobroGarantia) {
		this.fechaCobroGarantia = fechaCobroGarantia;
	}

	public BigDecimal getMontoSolesGarantia() {
		return montoSolesGarantia;
	}

	public void setMontoSolesGarantia(BigDecimal montoSolesGarantia) {
		this.montoSolesGarantia = montoSolesGarantia;
	}

	public BigDecimal getMontoDolaresGarantia() {
		return montoDolaresGarantia;
	}

	public void setMontoDolaresGarantia(BigDecimal montoDolaresGarantia) {
		this.montoDolaresGarantia = montoDolaresGarantia;
	}

	public BigDecimal getTipoCambioGarantia() {
		return tipoCambioGarantia;
	}

	public void setTipoCambioGarantia(BigDecimal tipoCambioGarantia) {
		this.tipoCambioGarantia = tipoCambioGarantia;
	}

	public String getTipoMonedaGarantia() {
		return tipoMonedaGarantia;
	}

	public void setTipoMonedaGarantia(String tipoMonedaGarantia) {
		this.tipoMonedaGarantia = tipoMonedaGarantia;
	}

}
