package com.pe.lima.sg.entity.mantenimiento;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.pe.lima.sg.entity.cliente.TblArbitrio;
import com.pe.lima.sg.entity.cliente.TblContrato;
import com.pe.lima.sg.entity.cliente.TblLuzxtienda;


public class TiendaBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private int codigoTienda;
	private TblEdificio tblEdificio;
	private TblSuministro tblSuministro;
	private String tipo;
	private String piso;
	private String numero;
	private BigDecimal area;
	private String observacion;
	private String estadoTienda;
	private String estado;
	private Integer usuarioCreacion;
	private Integer usuarioModificacion;
	private Date fechaCreacion;
	private Date fechaModificacion;
	private String ipCreacion;
	private String ipModificacion;
	private List<TblContrato> tblContratos;
	private List<TblArbitrio> tblArbitrios;
	private List<TblLuzxtienda> tblLuzxtiendas;
	private TblLuzxtienda tblLuzxtienda;
	
	public int getCodigoTienda() {
		return codigoTienda;
	}
	public void setCodigoTienda(int codigoTienda) {
		this.codigoTienda = codigoTienda;
	}
	public TblEdificio getTblEdificio() {
		return tblEdificio;
	}
	public void setTblEdificio(TblEdificio tblEdificio) {
		this.tblEdificio = tblEdificio;
	}
	public TblSuministro getTblSuministro() {
		return tblSuministro;
	}
	public void setTblSuministro(TblSuministro tblSuministro) {
		this.tblSuministro = tblSuministro;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getPiso() {
		return piso;
	}
	public void setPiso(String piso) {
		this.piso = piso;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public BigDecimal getArea() {
		return area;
	}
	public void setArea(BigDecimal area) {
		this.area = area;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public String getEstadoTienda() {
		return estadoTienda;
	}
	public void setEstadoTienda(String estadoTienda) {
		this.estadoTienda = estadoTienda;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Integer getUsuarioCreacion() {
		return usuarioCreacion;
	}
	public void setUsuarioCreacion(Integer usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}
	public Integer getUsuarioModificacion() {
		return usuarioModificacion;
	}
	public void setUsuarioModificacion(Integer usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public Date getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public String getIpCreacion() {
		return ipCreacion;
	}
	public void setIpCreacion(String ipCreacion) {
		this.ipCreacion = ipCreacion;
	}
	public String getIpModificacion() {
		return ipModificacion;
	}
	public void setIpModificacion(String ipModificacion) {
		this.ipModificacion = ipModificacion;
	}
	public List<TblContrato> getTblContratos() {
		return tblContratos;
	}
	public void setTblContratos(List<TblContrato> tblContratos) {
		this.tblContratos = tblContratos;
	}
	public List<TblArbitrio> getTblArbitrios() {
		return tblArbitrios;
	}
	public void setTblArbitrios(List<TblArbitrio> tblArbitrios) {
		this.tblArbitrios = tblArbitrios;
	}
	public List<TblLuzxtienda> getTblLuzxtiendas() {
		return tblLuzxtiendas;
	}
	public void setTblLuzxtiendas(List<TblLuzxtienda> tblLuzxtiendas) {
		this.tblLuzxtiendas = tblLuzxtiendas;
	}
	public TblLuzxtienda getTblLuzxtienda() {
		return tblLuzxtienda;
	}
	public void setTblLuzxtienda(TblLuzxtienda tblLuzxtienda) {
		this.tblLuzxtienda = tblLuzxtienda;
	}

}
