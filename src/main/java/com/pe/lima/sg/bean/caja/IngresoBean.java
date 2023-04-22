package com.pe.lima.sg.bean.caja;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class IngresoBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private int codigoIngreso;
	private String nombreEntidad;
	private Integer codigoEntidad;
	private Integer codigoTipoIngreso;
	private String nombreTipoIngreso;
	private BigDecimal monto;
	private String tipoMoneda;
	private Date fechaIngreso;
	private String observacion;
	private String estado;
	private Integer usuarioCreacion;
	private Integer usuarioModificacion;
	private Date fechaCreacion;
	private Date fechaModificacion;
	private String ipCreacion;
	private String ipModificacion;
	private String codigoInterno;
	private String nombreEdificacion;
	private String tipoPago;
	
	public IngresoBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public IngresoBean(int codigoIngreso, String nombreEntidad, Integer codigoEntidad, Integer codigoTipoIngreso,
			String nombreTipoIngreso, BigDecimal monto, String tipoMoneda, Date fechaIngreso, String observacion,
			String estado, Integer usuarioCreacion, Integer usuarioModificacion, Date fechaCreacion,
			Date fechaModificacion, String ipCreacion, String ipModificacion, String codigoInterno) {
		super();
		this.codigoIngreso = codigoIngreso;
		this.nombreEntidad = nombreEntidad;
		this.codigoEntidad = codigoEntidad;
		this.codigoTipoIngreso = codigoTipoIngreso;
		this.nombreTipoIngreso = nombreTipoIngreso;
		this.monto = monto;
		this.tipoMoneda = tipoMoneda;
		this.fechaIngreso = fechaIngreso;
		this.observacion = observacion;
		this.estado = estado;
		this.usuarioCreacion = usuarioCreacion;
		this.usuarioModificacion = usuarioModificacion;
		this.fechaCreacion = fechaCreacion;
		this.fechaModificacion = fechaModificacion;
		this.ipCreacion = ipCreacion;
		this.ipModificacion = ipModificacion;
		this.codigoInterno = codigoInterno;
	}
	
}
