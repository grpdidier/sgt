package com.pe.lima.sg.presentacion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pe.lima.sg.entity.caja.TblComprobanteSunat;
import com.pe.lima.sg.entity.caja.TblDetalleComprobante;
import com.pe.lima.sg.entity.caja.TblDetalleFormaPago;
import com.pe.lima.sg.entity.cliente.TblArbitrio;
import com.pe.lima.sg.entity.cliente.TblLuz;
import com.pe.lima.sg.entity.mantenimiento.TblSuministro;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Filtro {

	private String nombre			= "";
	private String login			= "";
	private String estado   		= "-1";
	private String estadoUsuario	= "";
	private String numero			= "";
	private String numeroFactura	= "";
	private String tipo				= "-1";
	private String dato				= "";
	private String fechaInicio		= null;
	private String fechaFin			= null;
	private String paterno			= "";
	private String materno			= "";
	private String dni				= "";
	private String ruc				= "";
	private String razonSocial		= "";
	private Integer codigo			= null;
	private Integer codigoEdificacion = null; //Inmueble
	private String 	strTienda		= ""; //Local
	private String strOperacion		= "";
	private Integer anio			= null;
	private String descripcion		= null;
	private Integer codigoContrato	= null;
	private Integer codigoDesembolso	= null;
	private Integer estadoOperacion		= null;
	private String tipoConcepto			= null;
	private String tipoPago				= null;;
	//Para que no haya conflicto con los Entities
	private Integer codigoEdificacionFiltro	= null;
	private String 	numeroFiltro			= null;
	private Integer anioFiltro				= null;
	//Luz
	private Integer codigoSuministroFiltro	= null;
	private String 	strFechaFinFiltro		= null;
	private TblLuz  luz						= null;
	private TblSuministro suministro		= null;
	private Map<String, Object> mapListado	= null;
	private BeanRequest beanRequest			= null;		
	private Integer anioInicio				= null;
	private String mesFiltro				= null;
	private Integer countLuzGenerado		= null;
	private BigDecimal montoAsignado		= null;
	private BigDecimal montoCalculado		= null;
	//Arbitrio
	private TblArbitrio arbitrio			= null;
	private ArrayList<TblArbitrio> listaArbitrio = null;
	//Caja Chica
	private String tipoOperacion			= null;
	private Integer codigoConcepto			= null;
	private Date fechaGasto					= null;
	private Date fechaIngreso					= null;
	//Seleccion de un elemento
	private String[] arrSeleccion				= null;
	private String seleccion					= null;
	private String estadoTienda					= null;
	//Para el reporte de moroso
	private String tipoCobro					= null;
	private String nombreCobro					= null;
	private String mesFin						= null;
	private String nombreMes					= null;
	private BigDecimal nuevoMontoAlquiler;
	private String fechaAlquiler;
	private BigDecimal nuevoMontoServicio;
	private String fechaServicio;
	private BigDecimal montoLuz;
	private String fechaLuz;
	//Para la factura
	private String serie;
	private TblComprobanteSunat factura;
	private TblDetalleComprobante facturaDetalle;
	private TblDetalleFormaPago formaPago;
	private List<TblDetalleFormaPago> listaFormaPago;
	private BigDecimal tipoCambio;
	
	public Filtro() {
		
	}



}
