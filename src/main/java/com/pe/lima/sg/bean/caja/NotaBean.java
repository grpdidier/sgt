package com.pe.lima.sg.bean.caja;

import java.util.List;

import com.pe.lima.sg.entity.caja.TblComprobanteSunat;
import com.pe.lima.sg.entity.caja.TblDetalleComprobante;
import com.pe.lima.sg.entity.caja.TblDetalleFormaPago;
import com.pe.lima.sg.entity.caja.TblNotaSunat;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class NotaBean {
	private TblNotaSunat nota;
	private TblComprobanteSunat factura;
	private TblDetalleComprobante facturaDetalle;
	private TblDetalleFormaPago formaPago;
	private List<TblDetalleFormaPago> listaFormaPago;
	//motivo del tipo de nota de credito
	private String tipoNotaCredito;
}
