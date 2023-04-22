package com.pe.lima.sg.bean.caja;

import java.math.BigDecimal;

public class MontoBean {

	//para la vista de edicion
	private BigDecimal monto;
	private BigDecimal calculado;
	//para la vista de refinanciacion
	private BigDecimal saldo;
	private BigDecimal nuevoMonto;
	private BigDecimal nuevoSaldo;
	private Integer codigoCxCDocumento;
	
	
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
	public BigDecimal getSaldo() {
		return saldo;
	}
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	public BigDecimal getNuevoMonto() {
		return nuevoMonto;
	}
	public void setNuevoMonto(BigDecimal nuevoMonto) {
		this.nuevoMonto = nuevoMonto;
	}
	public BigDecimal getNuevoSaldo() {
		return nuevoSaldo;
	}
	public void setNuevoSaldo(BigDecimal nuevoSaldo) {
		this.nuevoSaldo = nuevoSaldo;
	}
	public Integer getCodigoCxCDocumento() {
		return codigoCxCDocumento;
	}
	public void setCodigoCxCDocumento(Integer codigoCxCDocumento) {
		this.codigoCxCDocumento = codigoCxCDocumento;
	}
	
}
