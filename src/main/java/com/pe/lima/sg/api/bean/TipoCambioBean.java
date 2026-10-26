package com.pe.lima.sg.api.bean;

import java.math.BigDecimal;

public class TipoCambioBean {
	private BigDecimal compra;
	private BigDecimal venta;
	private String monedaBase;
	private String monedaCotizada;
	private String fecha;

	public BigDecimal getCompra() { return compra; }
	public void setCompra(BigDecimal compra) { this.compra = compra; }
	public BigDecimal getVenta() { return venta; }
	public void setVenta(BigDecimal venta) { this.venta = venta; }
	public String getMonedaBase() { return monedaBase; }
	public void setMonedaBase(String monedaBase) { this.monedaBase = monedaBase; }
	public String getMonedaCotizada() { return monedaCotizada; }
	public void setMonedaCotizada(String monedaCotizada) { this.monedaCotizada = monedaCotizada; }
	public String getFecha() { return fecha; }
	public void setFecha(String fecha) { this.fecha = fecha; }

	@Override
	public String toString() {
		return "TipoCambio [compra=" + compra + ", venta=" + venta + ", " + monedaBase + "/" + monedaCotizada + ", fecha=" + fecha + "]";
	}

}
