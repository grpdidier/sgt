package com.pe.lima.sg.presentacion;

import java.util.List;
import java.util.Map;

import com.pe.lima.sg.bean.caja.CobroArbitrioBean;
import com.pe.lima.sg.bean.caja.CobroBean;
import com.pe.lima.sg.bean.caja.CobroGarantia;
import com.pe.lima.sg.bean.caja.CobroGeneralBean;
import com.pe.lima.sg.bean.caja.CobroLuzBean;
import com.pe.lima.sg.bean.caja.CobroPrimerCobro;
import com.pe.lima.sg.bean.caja.CobroServicioBean;
import com.pe.lima.sg.bean.caja.DesembolsoBean;
import com.pe.lima.sg.entity.caja.TblCobro;
import com.pe.lima.sg.entity.caja.TblCxcDocumento;
import com.pe.lima.sg.entity.cliente.TblArbitrio;
import com.pe.lima.sg.entity.cliente.TblContrato;
import com.pe.lima.sg.entity.cliente.TblContratoCliente;
import com.pe.lima.sg.entity.cliente.TblContratoPrimerCobro;
import com.pe.lima.sg.entity.cliente.TblContratoServicio;
import com.pe.lima.sg.entity.cliente.TblLuz;
import com.pe.lima.sg.entity.cliente.TblLuzxtienda;
import com.pe.lima.sg.entity.cliente.TblObservacion;
import com.pe.lima.sg.entity.mantenimiento.TblTienda;
import com.pe.lima.sg.entity.mantenimiento.TiendaBean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class BeanRequest {
	private TblContrato contrato;
	//Se crea para la generación de los primeros cobros en el modulo de aprobacion
	private TblContrato contratoAntiguo;
	private TblContratoServicio contratoServicio;
	private TblContratoPrimerCobro contratoPrimerCobro;
	private TblArbitrio arbitrio;
	private TblLuz luz;
	private List<TblContratoServicio> listaServicio;
	//Se crea para la generación de los primeros cobros en el modulo de aprobacion
	private List<TblContratoServicio> listaServicioEliminar;
	private List<TblContratoServicio> listaServicioAntiguo;
	private List<TblContratoPrimerCobro> listaPrimerCobro;
	private List<TblContratoPrimerCobro> listaPrimerCobroNuevo;
	private List<TblArbitrio> listaArbitrio;
	private List<TblLuz> listaLuz;
	private TblContratoCliente contratoCliente;
	private List<TblContratoCliente> listaCliente;
	private TblLuzxtienda luzxTienda;
	private List<TblLuzxtienda> listaLuzxTienda;
	private List<TblTienda> listaTienda;
	private List<TiendaBean> listaTiendaBean;
	private List<TblContrato> listaContrato;
	private TblObservacion observacion;
	private List<TblObservacion> listaObservacion;
	//Cobro
	/*Historial de cobro*/
	private List<TblCobro> historialCobroAlquiler;
	private List<TblCobro> historialCobroPrimerCobro;
	private List<TblCobro> historialCobroGarantia;
	private List<TblCobro> historialCobroServicio;
	private List<TblCobro> historialCobroArbitrio;
	private List<TblCobro> historialCobroLuz;
	/*Cobros del Local*/
	private CobroBean cobroAlquiler;
	private CobroServicioBean cobroServicio;
	private CobroLuzBean cobroLuz;
	private CobroArbitrioBean cobroArbitrio;
	private CobroPrimerCobro cobroPrimerCobro;
	private CobroGarantia cobroGarantia;
	/*Documentos x Cobrar*/
	private List<TblCxcDocumento> listaCxcAlquiler;
	private List<TblCxcDocumento> listaCxcSinSaldo;
	private List<TblCxcDocumento> listaCxcPrimerCobro;
	private List<TblCxcDocumento> listaCxcGarantia;
	private List<TblCxcDocumento> listaCxcServicio;
	private List<TblCxcDocumento> listaCxcLuz;
	private List<TblArbitrio> listaCxcArbitrio;
	/*Varios*/
	private Map<String, Object> mapServicio;
	private TblContratoServicio servicioTipo;
	/*Reversion*/
	private List<DesembolsoBean> listaSolicitudReversion;
	//Adelanto
	private String flagAdelanto; /*0:NO, 1: Si hay adelantos*/
	/*Combo Primeros cobros*/
	private Map<String, Object> mapPrimerosCobros;
	private CobroGeneralBean cobroGeneralBean;
	
	

}
