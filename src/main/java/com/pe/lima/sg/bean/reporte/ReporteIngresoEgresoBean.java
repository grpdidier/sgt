package com.pe.lima.sg.bean.reporte;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReporteIngresoEgresoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String nombreUsuario;
	private List<IngresoEgresoBean> listaContrato;
	private BigDecimal contratoSubTotalSoles;
	private BigDecimal contratoSubTotalDolares;
	private List<IngresoEgresoBean> listaIngreso;
	private BigDecimal ingresoSubTotalSoles;
	private BigDecimal ingresoSubTotalDolares;
	private List<IngresoEgresoBean> listaGasto;
	private BigDecimal gastoSubTotalSoles;
	private BigDecimal gastoSubTotalDolares;
	
	private BigDecimal totalSoles;
	private BigDecimal totalDolares;
	
	
	private List<IngresoEgresoBean> listaAlquiler;
	private BigDecimal alquilerSubTotalSoles;
	private BigDecimal alquilerSubTotalDolares;
	private List<IngresoEgresoBean> listaServicio;
	private BigDecimal servicioSubTotalSoles;
	private BigDecimal servicioSubTotalDolares;
	private List<IngresoEgresoBean> listaLuz;
	private BigDecimal luzSubTotalSoles;
	private BigDecimal luzSubTotalDolares;
	private List<IngresoEgresoBean> listaArbitrios;
	private BigDecimal arbitriosSubTotalSoles;
	private BigDecimal arbitriosSubTotalDolares;
	//Reporte Cobro Fuera de Fecha
	private Integer codigoInmueble;
	private String nombreInmueble;
	
	
	
	public ReporteIngresoEgresoBean() {
		
		listaContrato = new ArrayList<IngresoEgresoBean>();
		contratoSubTotalSoles = new BigDecimal("0");
		contratoSubTotalDolares = new BigDecimal("0");
		
		listaAlquiler = new ArrayList<IngresoEgresoBean>();
		alquilerSubTotalSoles = new BigDecimal("0");
		alquilerSubTotalDolares = new BigDecimal("0");
		listaLuz = new ArrayList<IngresoEgresoBean>();
		luzSubTotalSoles = new BigDecimal("0");
		luzSubTotalDolares = new BigDecimal("0");
		listaServicio= new ArrayList<IngresoEgresoBean>();
		servicioSubTotalSoles = new BigDecimal("0");
		servicioSubTotalDolares = new BigDecimal("0");
		listaArbitrios= new ArrayList<IngresoEgresoBean>();
		arbitriosSubTotalSoles = new BigDecimal("0");
		arbitriosSubTotalDolares = new BigDecimal("0");
		
		listaIngreso = new ArrayList<IngresoEgresoBean>();
		ingresoSubTotalSoles = new BigDecimal("0");
		ingresoSubTotalDolares = new BigDecimal("0");
		listaGasto = new ArrayList<IngresoEgresoBean>();
		gastoSubTotalSoles = new BigDecimal("0");
		gastoSubTotalDolares = new BigDecimal("0");
		totalSoles = new BigDecimal("0");
		totalDolares = new BigDecimal("0");
		
	}
	
	
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	public List<IngresoEgresoBean> getListaIngreso() {
		return listaIngreso;
	}
	public void setListaIngreso(List<IngresoEgresoBean> listaIngreso) {
		this.listaIngreso = listaIngreso;
	}
	public BigDecimal getIngresoSubTotalSoles() {
		return ingresoSubTotalSoles;
	}
	public void setIngresoSubTotalSoles(BigDecimal ingresoSubTotalSoles) {
		this.ingresoSubTotalSoles = ingresoSubTotalSoles;
	}
	public BigDecimal getIngresoSubTotalDolares() {
		return ingresoSubTotalDolares;
	}
	public void setIngresoSubTotalDolares(BigDecimal ingresoSubTotalDolares) {
		this.ingresoSubTotalDolares = ingresoSubTotalDolares;
	}
	public List<IngresoEgresoBean> getListaGasto() {
		return listaGasto;
	}
	public void setListaGasto(List<IngresoEgresoBean> listaGasto) {
		this.listaGasto = listaGasto;
	}
	public BigDecimal getGastoSubTotalSoles() {
		return gastoSubTotalSoles;
	}
	public void setGastoSubTotalSoles(BigDecimal gastoSubTotalSoles) {
		this.gastoSubTotalSoles = gastoSubTotalSoles;
	}
	public BigDecimal getGastoSubTotalDolares() {
		return gastoSubTotalDolares;
	}
	public void setGastoSubTotalDolares(BigDecimal gastoSubTotalDolares) {
		this.gastoSubTotalDolares = gastoSubTotalDolares;
	}
	public BigDecimal getTotalSoles() {
		return totalSoles;
	}
	public void setTotalSoles(BigDecimal totalSoles) {
		this.totalSoles = totalSoles;
	}
	public BigDecimal getTotalDolares() {
		return totalDolares;
	}
	public void setTotalDolares(BigDecimal totalDolares) {
		this.totalDolares = totalDolares;
	}


	public List<IngresoEgresoBean> getListaContrato() {
		return listaContrato;
	}


	public void setListaContrato(List<IngresoEgresoBean> listaContrato) {
		this.listaContrato = listaContrato;
	}


	public BigDecimal getContratoSubTotalSoles() {
		return contratoSubTotalSoles;
	}


	public void setContratoSubTotalSoles(BigDecimal contratoSubTotalSoles) {
		this.contratoSubTotalSoles = contratoSubTotalSoles;
	}


	public BigDecimal getContratoSubTotalDolares() {
		return contratoSubTotalDolares;
	}


	public void setContratoSubTotalDolares(BigDecimal contratoSubTotalDolares) {
		this.contratoSubTotalDolares = contratoSubTotalDolares;
	}


	public List<IngresoEgresoBean> getListaAlquiler() {
		return listaAlquiler;
	}


	public void setListaAlquiler(List<IngresoEgresoBean> listaAlquiler) {
		this.listaAlquiler = listaAlquiler;
	}


	public BigDecimal getAlquilerSubTotalSoles() {
		return alquilerSubTotalSoles;
	}


	public void setAlquilerSubTotalSoles(BigDecimal alquilerSubTotalSoles) {
		this.alquilerSubTotalSoles = alquilerSubTotalSoles;
	}


	public BigDecimal getAlquilerSubTotalDolares() {
		return alquilerSubTotalDolares;
	}


	public void setAlquilerSubTotalDolares(BigDecimal alquilerSubTotalDolares) {
		this.alquilerSubTotalDolares = alquilerSubTotalDolares;
	}


	public List<IngresoEgresoBean> getListaServicio() {
		return listaServicio;
	}


	public void setListaServicio(List<IngresoEgresoBean> listaServicio) {
		this.listaServicio = listaServicio;
	}


	public BigDecimal getServicioSubTotalSoles() {
		return servicioSubTotalSoles;
	}


	public void setServicioSubTotalSoles(BigDecimal servicioSubTotalSoles) {
		this.servicioSubTotalSoles = servicioSubTotalSoles;
	}


	public BigDecimal getServicioSubTotalDolares() {
		return servicioSubTotalDolares;
	}


	public void setServicioSubTotalDolares(BigDecimal servicioSubTotalDolares) {
		this.servicioSubTotalDolares = servicioSubTotalDolares;
	}


	public List<IngresoEgresoBean> getListaLuz() {
		return listaLuz;
	}


	public void setListaLuz(List<IngresoEgresoBean> listaLuz) {
		this.listaLuz = listaLuz;
	}


	public BigDecimal getLuzSubTotalSoles() {
		return luzSubTotalSoles;
	}


	public void setLuzSubTotalSoles(BigDecimal luzSubTotalSoles) {
		this.luzSubTotalSoles = luzSubTotalSoles;
	}


	public BigDecimal getLuzSubTotalDolares() {
		return luzSubTotalDolares;
	}


	public void setLuzSubTotalDolares(BigDecimal luzSubTotalDolares) {
		this.luzSubTotalDolares = luzSubTotalDolares;
	}


	public List<IngresoEgresoBean> getListaArbitrios() {
		return listaArbitrios;
	}


	public void setListaArbitrios(List<IngresoEgresoBean> listaArbitrios) {
		this.listaArbitrios = listaArbitrios;
	}


	public BigDecimal getArbitriosSubTotalSoles() {
		return arbitriosSubTotalSoles;
	}


	public void setArbitriosSubTotalSoles(BigDecimal arbitriosSubTotalSoles) {
		this.arbitriosSubTotalSoles = arbitriosSubTotalSoles;
	}


	public BigDecimal getArbitriosSubTotalDolares() {
		return arbitriosSubTotalDolares;
	}


	public void setArbitriosSubTotalDolares(BigDecimal arbitriosSubTotalDolares) {
		this.arbitriosSubTotalDolares = arbitriosSubTotalDolares;
	}


	public Integer getCodigoInmueble() {
		return codigoInmueble;
	}


	public void setCodigoInmueble(Integer codigoInmueble) {
		this.codigoInmueble = codigoInmueble;
	}


	public String getNombreInmueble() {
		return nombreInmueble;
	}


	public void setNombreInmueble(String nombreInmueble) {
		this.nombreInmueble = nombreInmueble;
	}
	
	
	
	

}
