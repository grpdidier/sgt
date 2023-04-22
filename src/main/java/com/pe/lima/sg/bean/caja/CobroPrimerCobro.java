package com.pe.lima.sg.bean.caja;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CobroPrimerCobro  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Date fechaCobroPrimerCobro;
	private BigDecimal montoSolesPrimerCobro;
	private BigDecimal montoDolaresPrimerCobro;
	private BigDecimal tipoCambioPrimerCobro;
	private String tipoMonedaPrimerCobro;
	private Integer intCodigoPrimerCobro;
	private String tipoPrimerCobro; //Indica el tipo del primer cobro(Ser, Alq, Gar, etc)
	//para la vista de edicion
	private BigDecimal monto;
	private BigDecimal calculado;
	
	public CobroPrimerCobro() {
		super();
	}

	public Date getFechaCobroPrimerCobro() {
		return fechaCobroPrimerCobro;
	}

	public void setFechaCobroPrimerCobro(Date fechaCobroPrimerCobro) {
		this.fechaCobroPrimerCobro = fechaCobroPrimerCobro;
	}

	public BigDecimal getMontoSolesPrimerCobro() {
		return montoSolesPrimerCobro;
	}

	public void setMontoSolesPrimerCobro(BigDecimal montoSolesPrimerCobro) {
		this.montoSolesPrimerCobro = montoSolesPrimerCobro;
	}

	public BigDecimal getMontoDolaresPrimerCobro() {
		return montoDolaresPrimerCobro;
	}

	public void setMontoDolaresPrimerCobro(BigDecimal montoDolaresPrimerCobro) {
		this.montoDolaresPrimerCobro = montoDolaresPrimerCobro;
	}

	public BigDecimal getTipoCambioPrimerCobro() {
		return tipoCambioPrimerCobro;
	}

	public void setTipoCambioPrimerCobro(BigDecimal tipoCambioPrimerCobro) {
		this.tipoCambioPrimerCobro = tipoCambioPrimerCobro;
	}

	public String getTipoMonedaPrimerCobro() {
		return tipoMonedaPrimerCobro;
	}

	public void setTipoMonedaPrimerCobro(String tipoMonedaPrimerCobro) {
		this.tipoMonedaPrimerCobro = tipoMonedaPrimerCobro;
	}

	public Integer getIntCodigoPrimerCobro() {
		return intCodigoPrimerCobro;
	}

	public void setIntCodigoPrimerCobro(Integer intCodigoPrimerCobro) {
		this.intCodigoPrimerCobro = intCodigoPrimerCobro;
	}


	public String getTipoPrimerCobro() {
		return tipoPrimerCobro;
	}

	public void setTipoPrimerCobro(String tipoPrimerCobro) {
		this.tipoPrimerCobro = tipoPrimerCobro;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public BigDecimal getCalculado() {
		return calculado;
	}

	public void setCalculado(BigDecimal calculado) {
		this.calculado = calculado;
	}

}
