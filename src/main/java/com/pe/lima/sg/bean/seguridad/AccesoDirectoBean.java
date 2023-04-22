package com.pe.lima.sg.bean.seguridad;

import java.io.Serializable;

import com.pe.lima.sg.presentacion.util.Constantes;

/* Esta clase permite configurar los accesos directos segun la configuración del menu*/
public class AccesoDirectoBean implements Serializable{


	private static final long serialVersionUID = 1L;
	
	private String mostrarContrato;
	private String mostrarCobro;
	private String mostrarGasto;
	private String mostrarIngreso;
	private String mostrarReporteIE;
	
	public AccesoDirectoBean(){
		mostrarContrato = Constantes.ESTADO_INACTIVO;
		mostrarCobro	= Constantes.ESTADO_INACTIVO;
		mostrarGasto	= Constantes.ESTADO_ACTIVO;
		mostrarIngreso	= Constantes.ESTADO_ACTIVO;
		mostrarReporteIE= Constantes.ESTADO_INACTIVO;
	}
	public String getMostrarContrato() {
		return mostrarContrato;
	}
	public void setMostrarContrato(String mostrarContrato) {
		this.mostrarContrato = mostrarContrato;
	}
	public String getMostrarCobro() {
		return mostrarCobro;
	}
	public void setMostrarCobro(String mostrarCobro) {
		this.mostrarCobro = mostrarCobro;
	}
	public String getMostrarGasto() {
		return mostrarGasto;
	}
	public void setMostrarGasto(String mostrarGasto) {
		this.mostrarGasto = mostrarGasto;
	}
	public String getMostrarIngreso() {
		return mostrarIngreso;
	}
	public void setMostrarIngreso(String mostrarIngreso) {
		this.mostrarIngreso = mostrarIngreso;
	}
	public String getMostrarReporteIE() {
		return mostrarReporteIE;
	}
	public void setMostrarReporteIE(String mostrarReporteIE) {
		this.mostrarReporteIE = mostrarReporteIE;
	}
}
