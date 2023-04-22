package com.pe.lima.sg.bean.reporte;

import java.io.Serializable;
import java.math.BigDecimal;


public class BancarizadoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private BigDecimal efectivoSoles;
	private BigDecimal efectivoDolares;
	private BigDecimal bancarizadoSoles;
	private BigDecimal bancarizadoDolares;
	private String nombreUsuario;
	private String nombreInmueble;
	
	public BancarizadoBean(){
		this.efectivoSoles 		= new BigDecimal(0);
		this.efectivoDolares 	= new BigDecimal(0);
		this.bancarizadoSoles 	= new BigDecimal(0);
		this.bancarizadoDolares = new BigDecimal(0);
	}
	
	public BigDecimal getEfectivoSoles() {
		return efectivoSoles;
	}
	public void setEfectivoSoles(BigDecimal efectivoSoles) {
		this.efectivoSoles = efectivoSoles;
	}
	public BigDecimal getEfectivoDolares() {
		return efectivoDolares;
	}
	public void setEfectivoDolares(BigDecimal efectivoDolares) {
		this.efectivoDolares = efectivoDolares;
	}
	public BigDecimal getBancarizadoSoles() {
		return bancarizadoSoles;
	}
	public void setBancarizadoSoles(BigDecimal bancarizadoSoles) {
		this.bancarizadoSoles = bancarizadoSoles;
	}
	public BigDecimal getBancarizadoDolares() {
		return bancarizadoDolares;
	}
	public void setBancarizadoDolares(BigDecimal bancarizadoDolares) {
		this.bancarizadoDolares = bancarizadoDolares;
	}
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	public String getNombreInmueble() {
		return nombreInmueble;
	}
	public void setNombreInmueble(String nombreInmueble) {
		this.nombreInmueble = nombreInmueble;
	}
	
	

}
