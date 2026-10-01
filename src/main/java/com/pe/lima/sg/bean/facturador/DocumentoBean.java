package com.pe.lima.sg.bean.facturador;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class DocumentoBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String numeroTienda;
	private String nombreCliente;
	private String mes;
	private String anio;
	private BigDecimal montoGenerado;
	private String descripcion;
	private Date fechaFin;
	private Date fechaFinContrato;
	private Date fechaModificacion;
	private String tipoPersona;
	private String numeroDocumento;
	private Integer codigoEdificio;
	private Integer codigoCxC;
	
	public DocumentoBean() {
		super();
	}
	
	
}
