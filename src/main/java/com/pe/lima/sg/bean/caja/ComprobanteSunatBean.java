package com.pe.lima.sg.bean.caja;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.pe.lima.sg.entity.caja.TblDetalleComprobante;
import com.pe.lima.sg.entity.caja.TblDetalleFormaPago;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComprobanteSunatBean {
	private String tipoOperacion;
	private String domicilioFiscal;
	private String serie;
	private String numero;
	private String fechaEmision;	
	private String horaEmision;	
	private String fechaVencimiento;	
	private String clienteTipoDocumento;
	private String clienteNumero;
	private String clienteDireccion;
	private String clienteNombre;	
	private BigDecimal totalGravados;	
	private BigDecimal totalIgv;
	private BigDecimal total;	
	private String formaPago;	
	
	private Integer codigoComprobante;
	private BigDecimal detracionMonto;
	private BigDecimal detracionPorcentaje;	
	private BigDecimal detracionTotal;	
	private String estado;	
	private Date fechaCreacion;	
	private Date fechaModificacion;	
	
	private String ipCreacion;	
	private String ipModificacion;	
	private String moneda;
	
	
		
	private Integer usuarioCreacion;	
	private Integer usuarioModificacion;
	private Set<TblDetalleComprobante> tblDetalleComprobantes = new HashSet<TblDetalleComprobante>(0);
	private Set<TblDetalleFormaPago> tblDetalleFormaPagos = new HashSet<TblDetalleFormaPago>(0);
	private String tipoPago;
	private Integer codigoContrato;
	private String estadoOperacion;
	private String numeroTienda;
	private String numeroTicket;
	private String nombreCsv;
	private String nombreCdr;
	private String nombreXml;
	private String nombrePdf;
}
