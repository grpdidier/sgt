package com.pe.lima.sg.bean.caja;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotaSunatBean {
	private int codigoNota;
	private String tipoComprobante;
	private String tipoNota;
	private String serie;
	private String numero;
	private String fechaEmision;
	private String horaEmision;
	private String sustento;
	private String moneda;
	private String estadoOperacion;
	private String numeroTicket;
	private String nombreCsv;
	private String nombreCdr;
	private String nombreXml;
	private String nombrePdf;
	private int codigoComprobante;
	private String estado;	
	private Integer usuarioCreacion;	
	private Integer usuarioModificacion;
	private Date fechaCreacion;	
	private Date fechaModificacion;		
	private String ipCreacion;	
	private String ipModificacion;
	private String numeroTienda;
	private String serieFactura;
	private String numeroFactura;
	private String tipoPago;
}
