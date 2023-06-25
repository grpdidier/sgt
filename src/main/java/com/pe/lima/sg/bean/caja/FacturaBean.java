package com.pe.lima.sg.bean.caja;

import java.math.BigDecimal;
import java.util.List;

import com.pe.lima.sg.entity.caja.TblComprobanteSunat;
import com.pe.lima.sg.entity.caja.TblDetalleComprobante;
import com.pe.lima.sg.entity.caja.TblDetalleFormaPago;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacturaBean {
	//Para la factura
	private String serie;
	private TblComprobanteSunat factura;
	private TblDetalleComprobante facturaDetalle;
	private TblDetalleFormaPago formaPago;
	private List<TblDetalleFormaPago> listaFormaPago;
	//Para la consulta
	private String nombreInmueble;
	private String numeroTienda;
	//Datos para el credito
	private BigDecimal montoCredito;
	private String fechaFin;
	//Para actualizar el numero del comprobante en CxC
	private Integer codigoContrato;
	private Integer codigoCxCDocumento;
	//Para mostrar el tipo de cambio en la pantalla
	private String tipoCambio;
	
}
