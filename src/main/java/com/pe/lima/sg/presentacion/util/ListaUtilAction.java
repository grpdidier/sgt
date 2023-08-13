package com.pe.lima.sg.presentacion.util;

import static com.pe.lima.sg.dao.caja.CxCBitacoraSpecifications.conAnio;
import static com.pe.lima.sg.dao.caja.CxCBitacoraSpecifications.conContrato;
import static com.pe.lima.sg.dao.caja.CxCBitacoraSpecifications.conMes;
import static com.pe.lima.sg.dao.caja.CxCBitacoraSpecifications.conTipoCobro;
import static com.pe.lima.sg.dao.caja.CxCBitacoraSpecifications.conTipoOperacion;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pe.lima.sg.bean.caja.BitacoraBean;
import com.pe.lima.sg.dao.caja.ICxCBitacoraDAO;
import com.pe.lima.sg.dao.caja.ICxCDocumentoDAO;
import com.pe.lima.sg.dao.cliente.IContratoDAO;
import com.pe.lima.sg.dao.cliente.ILuzxTiendaDAO;
import com.pe.lima.sg.dao.mantenimiento.IEdificioDAO;
import com.pe.lima.sg.dao.mantenimiento.ISerieDAO;
import com.pe.lima.sg.entity.caja.TblCxcBitacora;
import com.pe.lima.sg.entity.caja.TblCxcDocumento;
import com.pe.lima.sg.entity.cliente.TblContrato;
import com.pe.lima.sg.entity.cliente.TblLuzxtienda;
import com.pe.lima.sg.entity.mantenimiento.TblEdificio;
import com.pe.lima.sg.entity.mantenimiento.TblSerie;
import com.pe.lima.sg.entity.mantenimiento.TblSuministro;

import lombok.extern.slf4j.Slf4j;

/**
 * Clase Bean que se encarga de la consruccion de las listas desplegables para
 * los combos de seleccion que se utilizan en las vistas
 *
 * @author Gregorio Rodriguez P.
 * @version 20/09/2017
 *
 */
@Slf4j
@Service
public class ListaUtilAction {
	@Autowired
	private IEdificioDAO edificioDao;
	
	@Autowired
	private ICxCBitacoraDAO cxcBitacoraDao;
	
	@Autowired
	private ILuzxTiendaDAO luzxTiendaDao;

	@Autowired
	private ICxCDocumentoDAO documentoDao;
	
	@Autowired
	private IContratoDAO contratoDao;
	
	@Autowired
	private ISerieDAO serieDao;
	
	/**
	 * Constructor por defecto.
	 */
	public ListaUtilAction() {
	}

	/*Codigo por reemplazar con el de la Session*/
	public void cargarDatosModel(Model model, String nombreListado){
		try{
			if (nombreListado.equals(Constantes.MAP_ESTADO_USUARIO)){
				model.addAttribute("mapEstadoUsuario", obtenerValoresEstadoUsuario());
			}else if(nombreListado.equals(Constantes.MAP_TIPO_CADUCIDAD)){
				model.addAttribute("mapTipoCaducidad", obtenerValoresTipoCaducidad());
			}else if (nombreListado.equals(Constantes.MAP_TIPO_TIENDA)){
				model.addAttribute("mapTipoTienda", obtenerValoresTipoTienda());
			}else if (nombreListado.equals(Constantes.MAP_TIPO_PISO)){
				model.addAttribute("mapTipoPiso", obtenerValoresTipoPiso());
			}else if (nombreListado.equals(Constantes.MAP_TIPO_CONCEPTO)){
				model.addAttribute("mapTipoConcepto", obtenerValoresTipoConcepto());
			}else if (nombreListado.equals(Constantes.MAP_TIPO_PERSONA)){
				model.addAttribute("mapTipoPersona", obtenerValoresTipoPersona());
			}else if (nombreListado.equals(Constantes.MAP_ESTADO_CIVIL)){
				model.addAttribute("mapEstadoCivil", obtenerValoresEstadoCivil());
			}else if (nombreListado.equals(Constantes.MAP_SI_NO)){
				model.addAttribute("mapSiNo", obtenerValoresSINO());
			}else if (nombreListado.equals(Constantes.MAP_LISTA_EDIFICIO)){
				model.addAttribute("mapListaEdificio", obtenerValoresEdificacio());
			}else if (nombreListado.equals(Constantes.MAP_TIPO_MONEDA)){
				model.addAttribute("mapTipoMoneda", obtenerValoresMoneda());
			}else if (nombreListado.equals(Constantes.MAP_TIPO_COBRO)){
				model.addAttribute("mapTipoCobro", obtenerValoresTipoCobroAlquiler());
			}else if (nombreListado.equals(Constantes.MAP_TIPO_GARANTIA)){
				model.addAttribute("mapTipoGarantia", obtenerValoresTipoGarantia());
			}else if (nombreListado.equals(Constantes.MAP_TIPO_DOCUMENTO)){
				model.addAttribute("mapTipoDocumento", obtenerValoresTipoDocumento());
			}else if (nombreListado.equals(Constantes.MAP_TIPO_RUBRO)){
				model.addAttribute("mapTipoRubro", obtenerValoresTipoRubro());
			}else if (nombreListado.equals(Constantes.MAP_TIPO_PERIODO_ADELANTO)){
				model.addAttribute("mapTipoPeriodoAdelanto", obtenerValoresTipoPeriodoAdelanto());
			}else if (nombreListado.equals(Constantes.MAP_ESTADO_ASIGNACION)){
				model.addAttribute("mapEstadoAsignacion", obtenerValoresAsignacion());
			}else if (nombreListado.equals(Constantes.MAP_MESES)){
				model.addAttribute("mapMeses", obtenerValoresMeses());
			}else if (nombreListado.equals(Constantes.MAP_TIPO_COBRO_CXC)){
				model.addAttribute("mapTipoCobroCxC", obtenerValoresTipoCobro());
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	/**
	 * Listado de edificios
	 * 
	 */
	public Map<String, Object> obtenerValoresEdificacio() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		List<TblEdificio> listaEdificio = null;
		try{
			listaEdificio = edificioDao.listarAllActivos();
			if (listaEdificio!=null){
				for(TblEdificio edificio: listaEdificio){
					log.debug("[obtenerValoresEdificacio] Nombre:"+edificio.getNombre());
					resultados.put(edificio.getNombre(), edificio.getCodigoEdificio());
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			listaEdificio = null;
		}
		
		return resultados;
	}
	/**
	 * Listado de tipos de caducidad
	 * 
	 */
	public Map<String, Object> obtenerValoresTipoCaducidad() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.TIPO_CADUCIDAD_INDEFINIDO, Constantes.TIPO_CADUCIDAD_INDEFINIDO);
		resultados.put(Constantes.TIPO_CADUCIDAD_CADUCA_30, Constantes.TIPO_CADUCIDAD_CADUCA_30);
		return resultados;
	}

	/**
	 * Listado de tipos de caducidad
	 * 
	 */
	public Map<String, Object> obtenerValoresEstadoUsuario() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.DESC_ESTADO_USUARIO_ACTIVO, Constantes.ESTADO_USUARIO_ACTIVO);
		resultados.put(Constantes.DESC_ESTADO_USUARIO_INACTIVO, Constantes.ESTADO_USUARIO_INACTIVO);
		return resultados;
	}
	/**
	 * Listado de estado tienda
	 * 
	 */
	public Map<String, Object> obtenerValoresEstadoTienda() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.DESC_ESTADO_TIENDA_OCUPADO, Constantes.ESTADO_TIENDA_OCUPADO);
		resultados.put(Constantes.DESC_ESTADO_TIENDA_DESOCUPADO, Constantes.ESTADO_TIENDA_DESOCUPADO);
		return resultados;
	}
	/**
	 * Listado de tipos de Concepto
	 * 
	 */
	public Map<String, Object> obtenerValoresTipoConcepto() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.DESC_TIPO_CONCEPTO_INGRESO, Constantes.TIPO_CONCEPTO_INGRESO);
		resultados.put(Constantes.DESC_TIPO_CONCEPTO_GASTO, Constantes.TIPO_CONCEPTO_GASTO);
		return resultados;
	}
	/**
	 * Listado de Asignado - Pendiente
	 * 
	 */
	public Map<String, Object> obtenerValoresAsignacion() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.ESTADO_ASIGNADO, Constantes.ESTADO_ASIGNADO);
		resultados.put(Constantes.ESTADO_PENDIENTE, Constantes.ESTADO_PENDIENTE);
		return resultados;
	}
	/**
	 * Listado de tipos de tienda
	 * 
	 */
	public Map<String, Object> obtenerValoresTipoTienda() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.DESC_TIPO_TIENDA_TIENDA, Constantes.TIPO_TIENDA_TIENDA);
		resultados.put(Constantes.DESC_TIPO_TIENDA_ALMACEN, Constantes.TIPO_TIENDA_ALMACEN);
		resultados.put(Constantes.DESC_TIPO_TIENDA_BODEGA, Constantes.TIPO_TIENDA_BODEGA);
		return resultados;
	}
	/**
	 * Listado de Meses
	 * 
	 */
	public Map<String, Object> obtenerValoresMeses() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.DESC_MES_01, Constantes.MES_01);
		resultados.put(Constantes.DESC_MES_02, Constantes.MES_02);
		resultados.put(Constantes.DESC_MES_03, Constantes.MES_03);
		resultados.put(Constantes.DESC_MES_04, Constantes.MES_04);
		resultados.put(Constantes.DESC_MES_05, Constantes.MES_05);
		resultados.put(Constantes.DESC_MES_06, Constantes.MES_06);
		resultados.put(Constantes.DESC_MES_07, Constantes.MES_07);
		resultados.put(Constantes.DESC_MES_08, Constantes.MES_08);
		resultados.put(Constantes.DESC_MES_09, Constantes.MES_09);
		resultados.put(Constantes.DESC_MES_10, Constantes.MES_10);
		resultados.put(Constantes.DESC_MES_11, Constantes.MES_11);
		resultados.put(Constantes.DESC_MES_12, Constantes.MES_12);
		return resultados;
	}
	
	/**
	 * Listado de tipos de cobro
	 * 
	 */
	public Map<String, Object> obtenerValoresTipoCobro() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.DESC_TIPO_COBRO_ALQUILER, Constantes.TIPO_COBRO_ALQUILER);
		resultados.put(Constantes.DESC_TIPO_COBRO_SERVICIO, Constantes.TIPO_COBRO_SERVICIO);
		resultados.put(Constantes.DESC_TIPO_COBRO_LUZ, Constantes.TIPO_COBRO_LUZ);
		resultados.put(Constantes.DESC_TIPO_COBRO_ARBITRIO, Constantes.TIPO_COBRO_ARBITRIO);
		
		return resultados;
	}
	public static Map<String, Object> obtenerTipoCobro() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.DESC_TIPO_COBRO_ALQUILER, Constantes.TIPO_COBRO_ALQUILER);
		resultados.put(Constantes.DESC_TIPO_COBRO_SERVICIO, Constantes.TIPO_COBRO_SERVICIO);
		resultados.put(Constantes.DESC_TIPO_COBRO_LUZ, Constantes.TIPO_COBRO_LUZ);
		resultados.put(Constantes.DESC_TIPO_COBRO_ARBITRIO, Constantes.TIPO_COBRO_ARBITRIO);
		
		return resultados;
	}
	/**
	 * Listado de tipos de persona
	 * 
	 */
	public Map<String, Object> obtenerValoresTipoPersona() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.DESC_TIPO_PERSONA_NATURAL, Constantes.TIPO_PERSONA_NATURAL);
		resultados.put(Constantes.DESC_TIPO_PERSONA_JURIDICA, Constantes.TIPO_PERSONA_JURIDICA);
		return resultados;
	}
	/**
	 * Listado de estado civil
	 * 
	 */
	public Map<String, Object> obtenerValoresEstadoCivil() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.DESC_ESTADO_CIVIL_SOLTERO, Constantes.ESTADO_CIVIL_SOLTERO);
		resultados.put(Constantes.DESC_ESTADO_CIVIL_CASADO, Constantes.ESTADO_CIVIL_CASADO);
		resultados.put(Constantes.DESC_ESTADO_CIVIL_VIUDO, Constantes.ESTADO_CIVIL_VIUDO);
		resultados.put(Constantes.DESC_ESTADO_CIVIL_DIVORCIADO, Constantes.ESTADO_CIVIL_DIVORCIADO);
		return resultados;
	}
	/**
	 * Listado de estado tipo de moneda
	 * 
	 */
	public Map<String, Object> obtenerValoresMoneda() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.DESC_MONEDA_SOL, Constantes.MONEDA_SOL);
		resultados.put(Constantes.DESC_MONEDA_DOLAR, Constantes.MONEDA_DOLAR);
		return resultados;
	}
	/**
	 * Listado de tipo de cobro
	 * 
	 */
	public Map<String, Object> obtenerValoresTipoCobroAlquiler() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.DESC_TIPO_COBRO_INICIO, Constantes.TIPO_COBRO_INICIO);
		resultados.put(Constantes.DESC_TIPO_COBRO_FIN, Constantes.TIPO_COBRO_FIN);
		resultados.put(Constantes.DESC_TIPO_COBRO_QUINCENA, Constantes.TIPO_COBRO_QUINCENA);
		resultados.put(Constantes.DESC_TIPO_COBRO_FECHA, Constantes.TIPO_COBRO_FECHA);
		return resultados;
	}
	/**
	 * Listado de tipo de garantia
	 * 
	 */
	public Map<String, Object> obtenerValoresTipoGarantia() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.DESC_TIPO_GARANTIA_CON, Constantes.TIPO_GARANTIA_CON);
		resultados.put(Constantes.DESC_TIPO_GARANTIA_SIN, Constantes.TIPO_GARANTIA_SIN);
		resultados.put(Constantes.DESC_TIPO_GARANTIA_LLAVE, Constantes.TIPO_GARANTIA_LLAVE);
		return resultados;
	}
	/**
	 * Listado de tipo de documento
	 * 
	 */
	public Map<String, Object> obtenerValoresTipoDocumento() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.DESC_TIPO_DOC_BOLETA, Constantes.TIPO_DOC_BOLETA);
		resultados.put(Constantes.DESC_TIPO_DOC_FACTURA, Constantes.TIPO_DOC_FACTURA);
		resultados.put(Constantes.DESC_TIPO_DOC_INTERNO, Constantes.TIPO_DOC_INTERNO);
		return resultados;
	}
	/**
	 * Listado de tipo de rubro
	 * 
	 */
	public Map<String, Object> obtenerValoresTipoRubro() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.DESC_TIPO_RUBRO_CONTRATO, Constantes.TIPO_RUBRO_CONTRATO);
		resultados.put(Constantes.DESC_TIPO_RUBRO_SERVICIO, Constantes.TIPO_RUBRO_SERVICIO);
		return resultados;
	}
	
	/**
	 * Listado de tipo de periodo de adelantos
	 * 
	 */
	public Map<String, Object> obtenerValoresTipoPeriodoAdelanto() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.DESC_PERIODO_ADELANTO_0, Constantes.PERIODO_ADELANTO_0);
		resultados.put(Constantes.DESC_PERIODO_ADELANTO_1, Constantes.PERIODO_ADELANTO_1);
		resultados.put(Constantes.DESC_PERIODO_ADELANTO_2, Constantes.PERIODO_ADELANTO_2);
		resultados.put(Constantes.DESC_PERIODO_ADELANTO_3, Constantes.PERIODO_ADELANTO_3);
		resultados.put(Constantes.DESC_PERIODO_ADELANTO_4, Constantes.PERIODO_ADELANTO_4);
		resultados.put(Constantes.DESC_PERIODO_ADELANTO_5, Constantes.PERIODO_ADELANTO_5);
		resultados.put(Constantes.DESC_PERIODO_ADELANTO_6, Constantes.PERIODO_ADELANTO_6);
		resultados.put(Constantes.DESC_PERIODO_ADELANTO_7, Constantes.PERIODO_ADELANTO_7);
		resultados.put(Constantes.DESC_PERIODO_ADELANTO_8, Constantes.PERIODO_ADELANTO_8);
		resultados.put(Constantes.DESC_PERIODO_ADELANTO_9, Constantes.PERIODO_ADELANTO_9);
		resultados.put(Constantes.DESC_PERIODO_ADELANTO_10, Constantes.PERIODO_ADELANTO_10);
		resultados.put(Constantes.DESC_PERIODO_ADELANTO_11, Constantes.PERIODO_ADELANTO_11);
		resultados.put(Constantes.DESC_PERIODO_ADELANTO_12, Constantes.PERIODO_ADELANTO_12);
		return resultados;
	}

	/**
	 * Listado de tipos de persona
	 * 
	 */
	public Map<String, Object> obtenerValoresSINO() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.DESC_TIPO_SI, Constantes.TIPO_SI);
		resultados.put(Constantes.DESC_TIPO_NO, Constantes.TIPO_NO);
		return resultados;
	}
	/**
	 * Listado de Piso
	 * 
	 */
	public Map<String, Object> obtenerValoresTipoPiso() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put("SEMI-SOTANO", "SEMI-SOTANO");
		resultados.put("SOTANO", "SOTANO");
		resultados.put("SOTANO 1", "SOTANO 1");
		resultados.put("SOTANO 2", "SOTANO 2");
		resultados.put("SOTANO 3", "SOTANO 3");
		resultados.put("PISO 1", "1");
		resultados.put("PISO 2", "2");
		resultados.put("PISO 3", "3");
		resultados.put("PISO 4", "4");
		resultados.put("PISO 5", "5");
		resultados.put("PISO 6", "6");
		resultados.put("PISO 7", "7");
		resultados.put("PISO 8", "8");
		resultados.put("PISO 9", "9");
		resultados.put("PISO 10", "10");
		return resultados;
	}
	
	/**
	 * Listado de tipos de caducidad
	 * 
	 */
	public static Map<String, Object> obtenerValoresEstadoUsuario2() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.DESC_ESTADO_USUARIO_ACTIVO, Constantes.ESTADO_USUARIO_ACTIVO);
		resultados.put(Constantes.DESC_ESTADO_USUARIO_INACTIVO, Constantes.ESTADO_USUARIO_INACTIVO);
		return resultados;
	}
	
	/**
	 * Listado de suministros
	 * 
	 */
	public static Map<String, Object> obtenerValoresListaSuministroActivo(List<TblSuministro> listaSuministro) {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		for(TblSuministro suministro: listaSuministro){
			resultados.put(suministro.getNumero(), suministro.getCodigoSuministro());
		}
		return resultados;
	}
	
	public static Map<String, Object> obtenerValoresIngresoSalida() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.DESC_CAJA_CHICA_INGRESO, Constantes.CAJA_CHICA_INGRESO);
		resultados.put(Constantes.DESC_CAJA_CHICA_GASTO, Constantes.CAJA_CHICA_GASTO);
		return resultados;
	}
	public static Map<String, Object> obtenerValoresEstadoCajaChica() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.DESC_CAJA_CHICA_ABIERTO, Constantes.CAJA_CHICA_ABIERTO);
		resultados.put(Constantes.DESC_CAJA_CHICA_CERRADO, Constantes.CAJA_CHICA_CERRADO);
		return resultados;
	}
	/**
	 * Listado de años
	 * 
	 */
	public  static Map<Integer, Object> obtenerAnios() {
		Map<Integer, Object> resultados = new LinkedHashMap<Integer, Object>();
		String strFecha	= null;
		Integer anio = null;
		strFecha = UtilSGT.getFecha("yyyy-MM-dd");
		anio = new Integer(strFecha.substring(0,4));
		for (Integer i=2018; i<=anio; i++){
			resultados.put(i, i);
		}
		return resultados;
	}
	public  static Map<Integer, Object> obtenerAniosFactura() {
		Map<Integer, Object> resultados = new LinkedHashMap<Integer, Object>();
		String strFecha	= null;
		Integer anio = null;
		strFecha = UtilSGT.getFecha("yyyy-MM-dd");
		anio = new Integer(strFecha.substring(0,4));
		for (Integer i=2023; i<=anio; i++){
			resultados.put(i, i);
		}
		return resultados;
	}
	/**
	 * Listado de Meses
	 * 
	 */
	public static Map<String, Object> obtenerValoresMesesSession() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.DESC_MES_01, Constantes.MES_01);
		resultados.put(Constantes.DESC_MES_02, Constantes.MES_02);
		resultados.put(Constantes.DESC_MES_03, Constantes.MES_03);
		resultados.put(Constantes.DESC_MES_04, Constantes.MES_04);
		resultados.put(Constantes.DESC_MES_05, Constantes.MES_05);
		resultados.put(Constantes.DESC_MES_06, Constantes.MES_06);
		resultados.put(Constantes.DESC_MES_07, Constantes.MES_07);
		resultados.put(Constantes.DESC_MES_08, Constantes.MES_08);
		resultados.put(Constantes.DESC_MES_09, Constantes.MES_09);
		resultados.put(Constantes.DESC_MES_10, Constantes.MES_10);
		resultados.put(Constantes.DESC_MES_11, Constantes.MES_11);
		resultados.put(Constantes.DESC_MES_12, Constantes.MES_12);
		return resultados;
	}
	
	/**
	 * Listado de Meses
	 * 
	 */
	public static Map<String, Object> obtenerMesesMoroso() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.DESC_MES_01, Constantes.MES_01_MOROSO);
		resultados.put(Constantes.DESC_MES_02, Constantes.MES_02_MOROSO);
		resultados.put(Constantes.DESC_MES_03, Constantes.MES_03_MOROSO);
		resultados.put(Constantes.DESC_MES_04, Constantes.MES_04_MOROSO);
		resultados.put(Constantes.DESC_MES_05, Constantes.MES_05_MOROSO);
		resultados.put(Constantes.DESC_MES_06, Constantes.MES_06_MOROSO);
		resultados.put(Constantes.DESC_MES_07, Constantes.MES_07_MOROSO);
		resultados.put(Constantes.DESC_MES_08, Constantes.MES_08_MOROSO);
		resultados.put(Constantes.DESC_MES_09, Constantes.MES_09_MOROSO);
		resultados.put(Constantes.DESC_MES_10, Constantes.MES_10_MOROSO);
		resultados.put(Constantes.DESC_MES_11, Constantes.MES_11_MOROSO);
		resultados.put(Constantes.DESC_MES_12, Constantes.MES_12_MOROSO);
		return resultados;
	}
	
	/**
	 * Listado de estado tipo de moneda
	 * 
	 */
	public static Map<String, Object> obtenerValoresTipoMoneda() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.DESC_MONEDA_SOL, Constantes.MONEDA_SOL);
		resultados.put(Constantes.DESC_MONEDA_DOLAR, Constantes.MONEDA_DOLAR);
		return resultados;
	}
	
	public static Map<String, Object> obtenerTipoIngresoEgreso(){
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.DESCRIPCION_INGRESO, Constantes.CODIGO_INGRESO);
		resultados.put(Constantes.DESCRIPCION_EGRESO, Constantes.CODIGO_EGRESO);
		return resultados;
	}
	public static Map<String, Object> obtenerTipoPagoFactura(){
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.TIPO_PAGO_ALQUILER_DESCRIPCION, Constantes.TIPO_PAGO_ALQUILER_CODIGO);
		resultados.put(Constantes.TIPO_PAGO_SERVICIO_DESCRIPCION, Constantes.TIPO_PAGO_SERVICIO_CODIGO);
		return resultados;
	}
	public static Map<String, Object> obtenerTipoOperacionFactura(){
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.TIPO_OPERACION_VENTA_INTERNA_DESCRIPCION, Constantes.TIPO_OPERACION_VENTA_INTERNA_CODIGO);
		resultados.put(Constantes.TIPO_OPERACION_DETRACCIONDESCRIPCION, Constantes.TIPO_OPERACION_DETRACCION_CODIGO);
		return resultados;
	}
	public static Map<String, Object> obtenerTipoComprobanteNota(){
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.TIPO_OPERACION_NOTA_CREDITO_DESCRIPCION, Constantes.TIPO_OPERACION_NOTA_CREDITO_CODIGO);
		resultados.put(Constantes.TIPO_OPERACION_NOTA_DEBITO_DESCRIPCION, Constantes.TIPO_OPERACION_NOTA_DEBITO_CODIGO);
		return resultados;
	}
	public static Map<String, Object> obtenerTipoNotaCredito(){
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put("01: Anulación de Operación", "01");
		resultados.put("02: Anulación por error en RUC", "02");
		resultados.put("03: Corrección por error en la descripción", "03");
		resultados.put("04: Descuento global", "04");
		resultados.put("05: Descuento por ítem", "05");
		resultados.put("06: Devolución total", "06");
		resultados.put("07: Devolución por ítem", "07");
		resultados.put("08: Bonificación", "08");
		resultados.put("09: Disminución en el valor", "09");
		resultados.put("10: Otros conceptos", "10");
		return resultados;
	}
	public static Map<String,String> obtenerTipoNotaCreditoMemoria(){
		Map<String, String> resultados = new HashMap<String, String>();
		resultados.put("01","01: Anulación de Operación");
		resultados.put("02","02: Anulación por error en RUC");
		resultados.put("03","03: Corrección por error en la descripción");
		resultados.put("04","04: Descuento global");
		resultados.put("05","05: Descuento por ítem");
		resultados.put("06","06: Devolución total");
		resultados.put("07","07: Devolución por ítem");
		resultados.put("08","08: Bonificación");
		resultados.put("09","09: Disminución en el valor");
		resultados.put("10","10: Otros conceptos");
		return resultados;
	}

	public  boolean generarCxCLuz(Model model, BitacoraBean entidad, HttpServletRequest request){
		boolean resultado = false;
		Integer intTotalRegistros				= null;
		List<TblLuzxtienda> listaLuzxTienda		= null;
		TblCxcDocumento documento				= null;
		TblCxcBitacora bitacora					= null;
		Specification<TblCxcBitacora> criterio	= null;
		TblSerie serieFactura					= null;
		TblSerie serieBoleta					= null;
		TblSerie serieInterno					= null;
		TblCxcDocumento cxcDocumento			= null;
		Integer totalLuzMasivo					= 0;
		Integer totalLuzIndividual				= 0;
		String strMensaje						= "";
		
		//Obtenemos Prefijo, Secuencia y Numero para generar los siguiente
		serieFactura = serieDao.buscarOneByTipoComprobante(Constantes.TIPO_COMPROBANTE_FACTURA);
		serieBoleta = serieDao.buscarOneByTipoComprobante(Constantes.TIPO_COMPROBANTE_BOLETA);
		serieInterno = serieDao.buscarOneByTipoComprobante(Constantes.TIPO_COMPROBANTE_INTERNO);
		
		//Generacion de las CXC de la Luz
		if (entidad.getLuz().equals(Constantes.TIPO_SI)){
			//Inicializamos variable
			resultado = false;
			bitacora = null;
			intTotalRegistros =cxcBitacoraDao.countTipoCobroxContrato(entidad.getCodigoContrato(), entidad.getAnio(), new Integer(entidad.getMes()), Constantes.TIPO_COBRO_LUZ);
			if (intTotalRegistros >0){
				//model.addAttribute("respuesta", "Se identifico registros generados de Luz para el año ["+entidad.getAnio()+"] y mes ["+entidad.getMes()+"] seleccionados. Se interrumpio el proceso");
				strMensaje = strMensaje + "Se identifico registros generados de Luz para el año ["+entidad.getAnio()+"] y mes ["+entidad.getMes()+"] seleccionados. Se interrumpio el proceso\n";
			}else{
				String fecha = UtilSGT.getLastDay(new Integer(entidad.getMes()), entidad.getAnio());
				listaLuzxTienda = luzxTiendaDao.listarLuzTiendaxSuministroConMontoxContrato(entidad.getCodigoContrato(), UtilSGT.getDatetoString(fecha));
				if (listaLuzxTienda!=null && listaLuzxTienda.size()>0){
					//Registramos la Bitacora del CxC del alquiler
					boolean exitoso = registrarBitacora(model, entidad, Constantes.TIPO_COBRO_LUZ, Constantes.SERIE_TIPO_OPERACION_INDIVIDUAL, request);
					if (exitoso) {
						criterio = Specifications.where(conAnio(entidad.getAnio()))
								.and(conMes(new Integer(entidad.getMes())))
								.and(conContrato(new Integer(entidad.getCodigoContrato())))
								.and(conTipoCobro(Constantes.TIPO_COBRO_LUZ))
								.and((conTipoOperacion(Constantes.SERIE_TIPO_OPERACION_INDIVIDUAL)));
						bitacora = cxcBitacoraDao.findOne(criterio);
						for (TblLuzxtienda luzxtienda: listaLuzxTienda){
							//Validamos si se registro individualmente el cobro de luz
							//cxcDocumento = documentoDao.findByAnioMesCodigoReferencia(Constantes.TIPO_COBRO_LUZ, luz.getCodigoLuz(), entidad.getAnio(), new Integer(entidad.getMes()));
							cxcDocumento = documentoDao.findByAnioMesCodigoReferencia(Constantes.TIPO_COBRO_LUZ, luzxtienda.getCodigoLuzxtienda(), entidad.getAnio(), new Integer(entidad.getMes()));
							if (cxcDocumento == null || cxcDocumento.getCodigoReferencia() == null){
								//Registro del CxC Documento
								documento = this.registrarCxCDocumentoLuz(model, entidad, request, luzxtienda, bitacora, serieFactura, serieBoleta, serieInterno);
								if (documento !=null){
									totalLuzMasivo++;
									resultado = true;
								}else{
									//model.addAttribute("respuesta", "No se pudo registrar la Cuenta por Cobrar de la Luz para el suministro  ["+luz.getTblSuministro().getNumero()+"]");
									strMensaje = strMensaje +"No se pudo registrar la Cuenta por Cobrar de la Luz para el codigo  ["+luzxtienda.getCodigoLuzxtienda()+"]\n";
									resultado = false;
									break;
								}
								//No se genera para la sunat
							}else{
								totalLuzIndividual++;
								resultado = true;
							}
						}
					}
				}
					
			}
			if (resultado == false){
				//model.addAttribute("respuesta", "Error en el proceso de generacion masiva de las Cuentas por Cobrar de la Luz");
				strMensaje = strMensaje +"Error en el proceso de generacion masiva de las Cuentas por Cobrar de la Luz\n";
			}else{
				//Validamos los valores para actualizar la bitacora
				if (bitacora !=null && bitacora.getCodigoCxcBitacora() >0){
					//if (totalLuzMasivo >0 || totalLuzIndividual >0 ){
						String resultadoLuz= "Se registro Masivamente ["+totalLuzMasivo+"] Cuentas por Cobrar Internos de Luz  , y se identico ["+totalLuzIndividual+"] Cuentas por Cobrar de Luz generados Individualmente.";
						bitacora.setResultado(resultadoLuz);
						this.preEditar(bitacora,request);
						cxcBitacoraDao.save(bitacora);
					//}
				}
			}
			
		}
		return resultado;
	}
	public void preEditar(TblCxcBitacora entidad, HttpServletRequest request) {
		try{
			log.debug("[preEditar] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			//entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			log.debug("[preEditar] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
	public void preGuardar(TblCxcBitacora entidad, HttpServletRequest request) {
		try{
			log.debug("[preGuardar] Inicio" );
			entidad.setFechaCreacion(new Date(System.currentTimeMillis()));
			entidad.setIpCreacion(request.getRemoteAddr());
			entidad.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			log.debug("[preGuardar] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	 * Registro de la bitacora
	 */
	public boolean registrarBitacora(Model model, BitacoraBean entidad, String tipoCobro, String tipoOperacion, HttpServletRequest request){
		boolean resultado 			= false;
		TblCxcBitacora bitacora		= null;
		try{
			bitacora = new TblCxcBitacora();
			this.preGuardar(bitacora, request);
			bitacora.setAnio(entidad.getAnio());
			bitacora.setMes(new Integer(entidad.getMes()));
			bitacora.setTipoOperacion(tipoOperacion);
			bitacora.setTipoCobro(tipoCobro);
			bitacora.setCodigoContrato(entidad.getCodigoContrato());
			cxcBitacoraDao.save(bitacora);
			resultado = true;
		}catch(Exception e){
			resultado = false;
			e.printStackTrace();
		}finally{
			bitacora	= null;
		}
		return resultado;
	}
	
	/*
	 * Registro del documento de Luz
	 */
	public TblCxcDocumento registrarCxCDocumentoLuz(Model model, BitacoraBean entidad, HttpServletRequest request, TblLuzxtienda luzxTienda,TblCxcBitacora bitacora,TblSerie serieFactura,TblSerie serieBoleta,TblSerie serieInterno){
		TblCxcDocumento documento	= null;
		//List<TblLuzxtienda> listaLuzxTienda = null;
		//TblLuzxtienda luzxTienda	= null;
		TblContrato contrato		= null;
		try{
			documento = new TblCxcDocumento();
			
			//Buscando el contrato y asociamos para el cobro
			//listaLuzxTienda = luzxTiendaDao.listarLuzTiendaxLuz(luz.getCodigoLuz());
			//if (listaLuzxTienda!=null && listaLuzxTienda.size()>0){
				//luzxTienda = listaLuzxTienda.get(0);
			contrato = contratoDao.findByNumeroTienda(luzxTienda.getTblTienda().getCodigoTienda());
			if (contrato !=null){
				documento.setCodigoContrato(contrato.getCodigoContrato());
			}
				
			//}
			documento.setCodigoReferencia(luzxTienda.getCodigoLuzxtienda());
			documento.setTipoReferencia(Constantes.TIPO_COBRO_LUZ);
			documento.setMontoGenerado(luzxTienda.getMontoGenerado());
			documento.setMontoContrato(luzxTienda.getMontoContrato());
			documento.setMontoCobrado(new BigDecimal("0"));
			documento.setSaldo(luzxTienda.getMontoContrato());
			documento.setTipoMoneda(Constantes.MONEDA_SOL);
			documento.setTipoDocumento(Constantes.TIPO_DOC_INTERNO); //TODO: No se definio el tipo de documento, asi asigna por defecto
			//validamos el tipo de documento
			if (documento.getTipoDocumento().equals(Constantes.TIPO_DOC_FACTURA)){
				documento.setTipoComprobante(Constantes.TIPO_COMPROBANTE_FACTURA);
			}else if (documento.getTipoDocumento().equals(Constantes.TIPO_DOC_BOLETA)){
				documento.setTipoComprobante(Constantes.TIPO_COMPROBANTE_BOLETA);
			}else if (documento.getTipoDocumento().equals(Constantes.TIPO_DOC_INTERNO)){
				documento.setTipoComprobante(Constantes.TIPO_COMPROBANTE_INTERNO);
			}
			//Fechas
			documento.setAnio(entidad.getAnio());
			documento.setMes(new Integer(entidad.getMes()));
			//documento.setFechaFin(UtilSGT.getDatetoString(UtilSGT.getLastDay(new Integer(entidad.getMes()), entidad.getAnio())));
			documento.setFechaFin(luzxTienda.getFechaFin());
			documento.setFechaFinNombre(UtilSGT.getFechaNombre(new Integer(entidad.getMes()), entidad.getAnio()));
			//Bitacora
			documento.setTblCxcBitacora(bitacora);
			//Campos de auditoria
			this.preGuardarDocumento(documento, request);
			//Validamos el tipo de comprobante
			/**AF20181214:No se crea el documento sino luego del cobro, saldo cero*/
			/*if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_FACTURA)){
				this.obtenerSiguienteFactura(serieFactura);
				documento.setSerieComprobante(serieFactura.getPrefijoSerie()+UtilSGT.completarCeros(serieFactura.getSecuencialSerie().toString(), Constantes.SUNAT_LONGITUD_SERIE));
				documento.setNumeroComprobante(UtilSGT.completarCeros(serieFactura.getNumeroComprobante().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			}
			if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_BOLETA)){
				this.obtenerSiguienteFactura(serieBoleta);
				documento.setSerieComprobante(serieBoleta.getPrefijoSerie()+UtilSGT.completarCeros(serieBoleta.getSecuencialSerie().toString(), Constantes.SUNAT_LONGITUD_SERIE));
				documento.setNumeroComprobante(UtilSGT.completarCeros(serieBoleta.getNumeroComprobante().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			}
			if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_INTERNO)){
				this.obtenerSiguienteFactura(serieInterno);
				documento.setSerieComprobante(serieInterno.getPrefijoSerie()+UtilSGT.completarCeros(serieInterno.getSecuencialSerie().toString(), Constantes.SUNAT_LONGITUD_SERIE));
				documento.setNumeroComprobante(UtilSGT.completarCeros(serieInterno.getNumeroComprobante().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			}*/
			
			documento.setNombre(Constantes.DESC_CUOTA_LUZ);
			//Registrar: Se podria validar que no exista el registro "Codigo de Referencia" antes de registrar (luego en generacion individual)
			documentoDao.save(documento);
			//Buscamos el documento registrado
			documento = documentoDao.findByAnioMesCodigoReferencia(documento.getTipoReferencia(), documento.getCodigoReferencia(), documento.getAnio(), documento.getMes());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//listaLuzxTienda = null;
			luzxTienda		= null;
			contrato		= null;
		}
		return documento;
	}
	/*
	 * Se asigna los datos del campo de auditoria
	 */
	public void preGuardarDocumento(TblCxcDocumento entidad, HttpServletRequest request) {
		try{
			log.debug("[preGuardar] Inicio" );
			entidad.setFechaCreacion(new Date(System.currentTimeMillis()));
			entidad.setIpCreacion(request.getRemoteAddr());
			entidad.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			log.debug("[preGuardar] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	 * Genera el siguiente valor de la Serie (FXXX999999999)
	 */
	public void obtenerSiguienteFactura(TblSerie entidad){
		try{
			if (entidad.getNumeroComprobante().compareTo(new Integer("999999999"))>=0){
				entidad.setNumeroComprobante(1);
				if (entidad.getSecuencialSerie().compareTo(new Integer("999"))>=0){
					log.debug("Se excedió el rango de la Serie... ");
					System.exit(0);
				}else{
					entidad.setSecuencialSerie(entidad.getSecuencialSerie()+1);
				}
			}else{
				entidad.setNumeroComprobante(entidad.getNumeroComprobante()+1);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	 * Genera el siguiente valor de la Serie (FXXX999999999)
	 */
	public void obtenerSiguienteBoleta(TblSerie entidad){
		try{
			if (entidad.getNumeroComprobante().compareTo(new Integer("999999999"))>=0){
				entidad.setNumeroComprobante(1);
				if (entidad.getSecuencialSerie().compareTo(new Integer("999"))>=0){
					log.debug("Se excedió el rango de la Serie... ");
					System.exit(0);
				}else{
					entidad.setSecuencialSerie(entidad.getSecuencialSerie()+1);
				}
			}else{
				entidad.setNumeroComprobante(entidad.getNumeroComprobante()+1);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	 * Genera el siguiente valor de la Serie (FXXX999999999)
	 */
	public void obtenerSiguienteInterno(TblSerie entidad){
		try{
			if (entidad.getNumeroComprobante().compareTo(new Integer("999999999"))>=0){
				entidad.setNumeroComprobante(1);
				if (entidad.getSecuencialSerie().compareTo(new Integer("999"))>=0){
					log.debug("Se excedió el rango de la Serie... ");
					System.exit(0);
				}else{
					entidad.setSecuencialSerie(entidad.getSecuencialSerie()+1);
				}
			}else{
				entidad.setNumeroComprobante(entidad.getNumeroComprobante()+1);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	 * Listado de Tipo de Pago
	 * 
	 */
	public static Map<String, Object> obtenerTipoPago() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put("EFECTIVO", "EFE");
		resultados.put("BANCARIZADO", "BAN");
		return resultados;
	}
	/*
	 * Listado de Tipo de Bancarizado
	 * 
	 */
	public static Map<String, Object> obtenerTipoBancarizado() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put("TRANSFERENCIA", "01");
		resultados.put("DEPOSITO", "02");
		resultados.put("CHEQUE", "03");
		resultados.put("IZIPAY (POS)", "05");
		resultados.put("OTROS", "04");
		
		return resultados;
	}
	/*
	 * Listado de Forma de Pago
	 * 
	 */
	public static Map<String, Object> obtenerFormaPago() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put("CONTADO", "CONTADO");
		resultados.put("CREDITO", "CREDITO");
		return resultados;
	}
	/*
	 * Listado de Forma de Pago
	 * 
	 */
	public static Map<String, Object> obtenerTipoDocumento() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put("DNI", "1");
		resultados.put("RUC", "6");
		return resultados;
	}
	public static Map<String, String> obtenerTipoProductoFactura() {
		Map<String, String> resultados = new LinkedHashMap<String, String>();
		resultados.put(Constantes.DESC_TIPO_COBRO_ALQUILER, Constantes.TIPO_COBRO_ALQUILER);
		resultados.put(Constantes.DESC_TIPO_COBRO_SERVICIO, Constantes.TIPO_COBRO_SERVICIO);
		
		return resultados;
	}
}
