package com.pe.lima.sg.presentacion.cliente;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pe.lima.sg.bean.caja.BitacoraBean;
import com.pe.lima.sg.bean.cliente.ComparacionTiendaBean;
import com.pe.lima.sg.bean.cliente.LuzBean;
import com.pe.lima.sg.bean.cliente.TiendaSuministroBean;
import com.pe.lima.sg.dao.BaseDAO;
import com.pe.lima.sg.dao.caja.ICxCBitacoraDAO;
import com.pe.lima.sg.dao.cliente.IContratoDAO;
import com.pe.lima.sg.dao.cliente.ILuzDAO;
import com.pe.lima.sg.dao.cliente.ILuzxTiendaDAO;
import com.pe.lima.sg.dao.mantenimiento.IConceptoDAO;
import com.pe.lima.sg.dao.mantenimiento.IParametroDAO;
import com.pe.lima.sg.dao.mantenimiento.ISuministroDAO;
import com.pe.lima.sg.dao.mantenimiento.ITiendaDAO;
import com.pe.lima.sg.entity.cliente.TblContrato;
import com.pe.lima.sg.entity.cliente.TblLuz;
import com.pe.lima.sg.entity.cliente.TblLuzxtienda;
import com.pe.lima.sg.entity.mantenimiento.TblParametro;
import com.pe.lima.sg.entity.mantenimiento.TblTienda;
import com.pe.lima.sg.presentacion.BasePresentacion;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.reporte.MorosoAction;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.ListaUtilAction;
import com.pe.lima.sg.presentacion.util.UtilSGT;
import com.pe.lima.sg.util.SysOutPrintln;

/**
 * Clase Bean que se encarga de la administracion de los suministros x tienda
 *
 * 			
 */
@Controller
public class SuministroxInmuebleAction extends BasePresentacion<TblLuzxtienda> {
	
	private static final Logger logger = LogManager.getLogger(SuministroxInmuebleAction.class);
	
	@Autowired
	private IConceptoDAO conceptoDao;
	
	@Autowired
	private ILuzDAO luzDao;
	
	@Autowired
	private ICxCBitacoraDAO cxcBitacoraDao;
	
	@Autowired
	private ListaUtilAction listaUtil;
	
	@Autowired
	private IParametroDAO parametroDao;
	
	@Autowired
	private ITiendaDAO tiendaDao;
	
	@Autowired
	private ISuministroDAO suministroDao;

	
	@Autowired
	private ILuzxTiendaDAO luzxTiendaDao;
	
	@Autowired
	private IContratoDAO contratoDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDAO getDao() {
		return conceptoDao;
	}
	
	/**
	 * Se encarga de listar todas las tiendas del suministro de un edificio
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "cliente/suministrosxinmueble", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path, HttpServletRequest request) {
		Filtro filtro 						= null;
		List<TblParametro> listaParametro	= null;
		List<TblTienda> listaTienda			= null;
		Map<String, Object> mapSuministro 	= null;
		Integer intAnioInicio				= null;
		try{
			logger.debug("[traerRegistros] Inicio");
			path = "cliente/suministroxinmueble/sxi_listado";
			filtro = (Filtro)request.getSession().getAttribute("sessionFiltroSuministroLuz");
			if (filtro ==null){
				filtro = new Filtro();
				//Asignacion del inmueble
				filtro.setCodigoEdificacionFiltro(-1);
				listaParametro = parametroDao.buscarOneByNombre(Constantes.PAR_INMUEBLE);
				if(listaParametro!=null && listaParametro.size()>0){
					filtro.setCodigoEdificacionFiltro(new Integer(listaParametro.get(0).getDato()));
					//Asignacion del suministro
					listaTienda = tiendaDao.listarAllActivos(filtro.getCodigoEdificacionFiltro());
					mapSuministro = this.obtenerSuministros(listaTienda);
					
				}
				//Buscando el año de inicio
				listaParametro = parametroDao.buscarOneByNombre(Constantes.PAR_ANIO_INICIO);
				if(listaParametro!=null && listaParametro.size()>0){
					try{
						intAnioInicio = new Integer(listaParametro.get(0).getDato());
					}catch(Exception e1){
						e1.printStackTrace();
						intAnioInicio = Calendar.getInstance().get(Calendar.YEAR);
					}
				}else{
					intAnioInicio = Calendar.getInstance().get(Calendar.YEAR);
				}
				filtro.setAnioInicio(intAnioInicio);
				filtro.setAnioFiltro(Calendar.getInstance().get(Calendar.YEAR));
				filtro.setMesFiltro(getMoth());
			}else{
				//Asignacion del suministro
				listaTienda = tiendaDao.listarAllActivos(filtro.getCodigoEdificacionFiltro());
				mapSuministro = this.obtenerSuministros(listaTienda);
			}
			
			this.cargarListaOperacionLuz(model, filtro);
			request.getSession().setAttribute("SessionMapAnio", UtilSGT.getListaAnio(filtro.getAnioInicio(), Calendar.getInstance().get(Calendar.YEAR) + 1 ));
			request.getSession().setAttribute("SessionMapSuministroxTienda", mapSuministro);
			//model.addAttribute("mapSuministroxTienda", mapSuministro);
			model.addAttribute("filtro", filtro);
			request.getSession().setAttribute("sessionFiltroSuministroLuz", filtro);
			logger.debug("[traerRegistros] Fin");
		}catch(Exception e){
			logger.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		
		return path;
	}
	/*
	 * Obtiene el mes en curso
	 */
	private String getMoth(){
		String strMes = null;
		Integer intMes = null;
		intMes = Calendar.getInstance().get(Calendar.MONDAY)+1;
		if (intMes>11){
			strMes = "01";
		}else{
			strMes = intMes.toString().length()==1? "0"+intMes.toString(): intMes.toString();
		}
		return strMes;
	}
	/*
	 * Obtiene la lista de tiendas y setea el numero de suministro
	 */
	private Map<String, Object> obtenerSuministros(List<TblTienda> listaTienda	){
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		try{
			if (listaTienda !=null){
				for(TblTienda tienda: listaTienda){
					resultados.put( tienda.getTblSuministro().getNumero(), tienda.getTblSuministro().getCodigoSuministro());
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultados;
	}

	@Override
	public TblLuzxtienda getNuevaEntidad() {
		
		return null;
	}
	
	/**
	 * Se encarga los suministros del inmueble seleccionado
	 * seleccionado
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/suministroxinmueble/suministro", method = RequestMethod.POST)
	public String cargarSuministro(Model model, Filtro filtro, String path, HttpServletRequest request) {
		//path = "cliente/suministroxinmueble/sxi_listado";
		List<TblTienda> listaTienda			= null;
		Map<String, Object> mapSuministro 	= null;
		try{
			logger.debug("[cargarSuministro] Inicio");
			//Asignacion del suministro
			listaTienda = tiendaDao.listarAllActivos(filtro.getCodigoEdificacionFiltro());
			mapSuministro = this.obtenerSuministros(listaTienda);
			request.getSession().setAttribute("SessionMapSuministroxTienda", mapSuministro);
			//model.addAttribute("mapSuministroxTienda", mapSuministro);
			this.cargarListaOperacionLuz(model, filtro);
			model.addAttribute("filtro", filtro);
			logger.debug("[cargarSuministro] Fin");
		}catch(Exception e){
			logger.debug("[cargarSuministro] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{

		}
		logger.debug("[cargarSuministro] Fin");
		return path;
	}
	/*
	 * Listados para el modulo
	 */
	public void cargarListaOperacionLuz(Model model, Filtro filtro){
		//Listado de meses
		listaUtil.cargarDatosModel(model, Constantes.MAP_MESES);
		
	}
	//Obtenemos el monto original asignado
	private void obtenerMontoOriginal(Integer intCodigoLuz, Filtro filtro){
		if (intCodigoLuz != null){
			TblLuz tblLuz = luzDao.findByLuz(intCodigoLuz);
			if (tblLuz != null){
				filtro.setMontoAsignado(tblLuz.getMontoContrato());
			}
		}
	}
	/**
	 * Se encarga los montos de cobro de luz por cada local, del suministro seleccionado, año y mes
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cliente/suministroxinmueble/q", method = RequestMethod.POST)
	public String buscarSuministroxLocal(Model model, Filtro filtro, String path, HttpServletRequest request) {
		path = "cliente/suministroxinmueble/sxi_listado";
		List<TblTienda> listaTienda				= null;
		TblTienda tienda						= null;
		Map<Integer, Object> mapLuzxTienda	 	= null;
		List<TblLuzxtienda>  listaLuzxTienda	= null;
		List<TiendaSuministroBean> listaTiendaSuministro	= null;
		TiendaSuministroBean tiendaSuministro	= null;
		String strFecha							= null;
		Date datFecha							= null;
		Integer intCodigoLuz					= null;
		try{
			logger.debug("[buscarSuministroxLocal] Inicio");
			//Listado de Suministro
			request.getSession().setAttribute("SessionMapSuministroxTienda", request.getSession().getAttribute("SessionMapSuministroxTienda"));
			this.cargarListaOperacionLuz(model, filtro);
			if (validarCampos(model, filtro)){
				
				//Listado de LuzxTienda (costo luz)
				strFecha = getFechaMesAnio(filtro.getMesFiltro(), filtro.getAnioFiltro());
				datFecha = UtilSGT.getDatetoString(strFecha);
				listaLuzxTienda = luzxTiendaDao.listarLuzTiendaxSuministro(filtro.getCodigoSuministroFiltro(), datFecha);
				if (listaLuzxTienda!=null && !listaLuzxTienda.isEmpty()){
					//Listado de Tiendas (Locales)
					listaTienda= tiendaDao.listarTiendaxSuministro(filtro.getCodigoSuministroFiltro());
					
					mapLuzxTienda = new HashMap<Integer, Object>();
					for(TblLuzxtienda luzxtienda: listaLuzxTienda){
						mapLuzxTienda.put(luzxtienda.getTblTienda().getCodigoTienda(), luzxtienda);
						intCodigoLuz = luzxtienda.getTblLuz().getCodigoLuz();
					}
					if (intCodigoLuz != null){ 
						filtro.setCountLuzGenerado(cxcBitacoraDao.countCobroLuzGenerado(intCodigoLuz));
					}else{
						filtro.setCountLuzGenerado(0);
					}
					//Obtenemos el monto original asignado
					this.obtenerMontoOriginal(intCodigoLuz, filtro);
					//Inicializamos el monto calculado
					filtro.setMontoCalculado(new BigDecimal(0));
					listaTiendaSuministro = new ArrayList<TiendaSuministroBean>();
					for(int i=0; listaTienda!=null && i < listaTienda.size(); i++){
						tienda = listaTienda.get(i);
						//Datos de tienda
						tiendaSuministro = new TiendaSuministroBean();
						tiendaSuministro.setNumero(tienda.getNumero());
						tiendaSuministro.setNumeroSuministro(tienda.getTblSuministro().getNumero());
						tiendaSuministro.setPiso(tienda.getPiso());
						tiendaSuministro.setArea(tienda.getArea());
						tiendaSuministro.setEstadoTienda(tienda.getEstadoTienda());
						tiendaSuministro.setCodigoTienda(tienda.getCodigoTienda());
						tiendaSuministro.setAnio(filtro.getAnioFiltro());
						tiendaSuministro.setMes(filtro.getMesFiltro());
						tiendaSuministro.setCodigoLuz(intCodigoLuz);
						TblLuzxtienda luzxtienda = (TblLuzxtienda)mapLuzxTienda.get(tiendaSuministro.getCodigoTienda());
						if (luzxtienda !=null){
							tiendaSuministro.setFechaFin(luzxtienda.getFechaFin());
							tiendaSuministro.setMontoGenerado(luzxtienda.getMontoGenerado());
							tiendaSuministro.setMontoContrato(luzxtienda.getMontoContrato());
							tiendaSuministro.setValorCobrado(luzxtienda.getValorCobrado());
							tiendaSuministro.setSaldo(luzxtienda.getSaldo());
							tiendaSuministro.setCodigoLuzxtienda(luzxtienda.getCodigoLuzxtienda());
							//Codigo del contrato
							tiendaSuministro.setCodigoContrato(luzxtienda.getCodigoContrato());
							//Monto total calculado
							filtro.setMontoCalculado(filtro.getMontoCalculado().add(luzxtienda.getMontoContrato()));
						}else{
							tiendaSuministro.setMontoGenerado(new BigDecimal("0"));
							tiendaSuministro.setMontoContrato(new BigDecimal("0"));
							tiendaSuministro.setValorCobrado(new BigDecimal("0"));
							tiendaSuministro.setSaldo(new BigDecimal("0"));
						}
						listaTiendaSuministro.add(tiendaSuministro);
					}
				}else{
					filtro.setCountLuzGenerado(0);
				}
			}
			

			model.addAttribute("registros", listaTiendaSuministro);
			model.addAttribute("filtro", filtro);
			request.getSession().setAttribute("sessionFiltroSuministroLuz", filtro);
			request.getSession().setAttribute("sessionListaEliminarSuministroLuz", listaTiendaSuministro);
			logger.debug("[buscarSuministroxLocal] Fin");
		}catch(Exception e){
			logger.debug("[buscarSuministroxLocal] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			listaTienda				= null;
			tienda					= null;
			mapLuzxTienda	 		= null;
			listaLuzxTienda			= null;
			listaTiendaSuministro	= null;
			tiendaSuministro		= null;
			strFecha				= null;
			datFecha				= null;
		}
		logger.debug("[buscarSuministroxLocal] Fin");
		return path;
	}
	
	@RequestMapping(value = "/cliente/suministroxinmueble/eliminar", method = RequestMethod.POST)
	public String EliminarCobro(Model model, Filtro filtro, String path, HttpServletRequest request) {
		path = "cliente/suministroxinmueble/sxi_listado";
		List<TblTienda> listaTienda				= null;
		TblTienda tienda						= null;
		Map<Integer, Object> mapLuzxTienda	 	= null;
		List<TblLuzxtienda>  listaLuzxTienda	= null;
		List<TiendaSuministroBean> listaTiendaSuministro	= null;
		TiendaSuministroBean tiendaSuministro	= null;
		String strFecha							= null;
		Date datFecha							= null;
		Integer intCodigoLuz					= null;
		Filtro filtroTemporal					= null;
		BitacoraBean entidad					= null;
		try{
			logger.debug("[EliminarCobro] Inicio");
			this.cargarListaOperacionLuz(model, filtroTemporal);
			//Critreios de busqueda
			filtroTemporal = (Filtro)request.getSession().getAttribute("sessionFiltroSuministroLuz");
			//Listado a eliminar
			listaTiendaSuministro = (List<TiendaSuministroBean>)request.getSession().getAttribute("sessionListaEliminarSuministroLuz");
			
			//Eliminamos registros de la tabla luzxtienda
			for(TiendaSuministroBean tiendaSuministroBean : listaTiendaSuministro ){
				TblLuzxtienda luzxTienda = luzxTiendaDao.findLuzTienda(tiendaSuministroBean.getCodigoLuzxtienda());
				if (luzxTienda != null){
					luzxTienda.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
					preEditar(luzxTienda, request);
					luzxTiendaDao.save(luzxTienda);
					intCodigoLuz = luzxTienda.getTblLuz().getCodigoLuz();
					logger.debug("[EliminarCobro] CodigoLuzxTienda:"+tiendaSuministroBean.getCodigoLuzxtienda() +" CodigoLuz:"+intCodigoLuz);
				}else{
					String strFecha2 = getFechaMesAnio(tiendaSuministroBean.getMes(), tiendaSuministroBean.getAnio());
					Date datFecha2 = UtilSGT.getDatetoString(strFecha2);
					TblLuzxtienda luzxTienda2 = luzxTiendaDao.getTiendaxTiendaLuzFecha(tiendaSuministroBean.getCodigoLuz(), tiendaSuministroBean.getCodigoTienda(), datFecha2);
					if (luzxTienda2 != null){
						luzxTienda2.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
						preEditar(luzxTienda2, request);
						luzxTiendaDao.save(luzxTienda2);
						intCodigoLuz = luzxTienda2.getTblLuz().getCodigoLuz();
						logger.debug("[EliminarCobro] CodigoLuzxTienda:"+tiendaSuministroBean.getCodigoLuzxtienda() +" CodigoLuz:"+intCodigoLuz);
					}else{
						logger.debug("[EliminarCobro] No existe registro para eliminar CodigoLuz:"+intCodigoLuz);
					}
					
				}
			}
			//Eliminamos la luz
			TblLuz tblLuz = luzDao.findByLuz(intCodigoLuz);
			if (tblLuz != null){
				tblLuz.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
				preEditarLuz(tblLuz, request);
				luzDao.save(tblLuz);
				logger.debug("[EliminarCobro] Eliminado Luz:"+intCodigoLuz);
			}
			model.addAttribute("respuesta", "Se eliminó los cobros de Luz Exitosamente");

			model.addAttribute("registros", null);
			model.addAttribute("filtro", filtroTemporal);
			request.getSession().setAttribute("sessionFiltroSuministroLuz", filtroTemporal);
			logger.debug("[EliminarCobro] Fin");
		}catch(Exception e){
			logger.debug("[EliminarCobro] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			listaTienda				= null;
			tienda					= null;
			mapLuzxTienda	 		= null;
			listaLuzxTienda			= null;
			listaTiendaSuministro	= null;
			tiendaSuministro		= null;
			strFecha				= null;
			datFecha				= null;
		}
		logger.debug("[EliminarCobro] Fin");
		return path;
	}
	
	
	@RequestMapping(value = "/cliente/suministroxinmueble/generarcobro", method = RequestMethod.POST)
	public String generarCobro(Model model, Filtro filtro, String path, HttpServletRequest request) {
		path = "cliente/suministroxinmueble/sxi_listado";
		List<TblTienda> listaTienda				= null;
		TblTienda tienda						= null;
		Map<Integer, Object> mapLuzxTienda	 	= null;
		List<TblLuzxtienda>  listaLuzxTienda	= null;
		List<TiendaSuministroBean> listaTiendaSuministro	= null;
		TiendaSuministroBean tiendaSuministro	= null;
		String strFecha							= null;
		Date datFecha							= null;
		Integer intCodigoLuz					= null;
		Filtro filtroTemporal					= null;
		BitacoraBean entidad					= null;
		try{
			logger.debug("[generarCobro] Inicio");
			//Listado de Suministro
			filtroTemporal = (Filtro)request.getSession().getAttribute("sessionFiltroSuministroLuz");
			this.cargarListaOperacionLuz(model, filtroTemporal);
		
			
			//Listado de LuzxTienda (costo luz)
			strFecha = getFechaMesAnio(filtroTemporal.getMesFiltro(), filtroTemporal.getAnioFiltro());
			datFecha = UtilSGT.getDatetoString(strFecha);
			listaLuzxTienda = luzxTiendaDao.listarLuzTiendaxSuministro(filtroTemporal.getCodigoSuministroFiltro(), datFecha);
			if (listaLuzxTienda!=null && !listaLuzxTienda.isEmpty()){
				
				
				//Listado de Tiendas (Locales)
				listaTienda= tiendaDao.listarTiendaxSuministro(filtroTemporal.getCodigoSuministroFiltro());
				
				mapLuzxTienda = new HashMap<Integer, Object>();
				for(TblLuzxtienda luzxtienda: listaLuzxTienda){
					mapLuzxTienda.put(luzxtienda.getTblTienda().getCodigoTienda(), luzxtienda);
					intCodigoLuz = luzxtienda.getTblLuz().getCodigoLuz();
					/*TODO:Registro de los cobros*/
					entidad = new BitacoraBean();
					entidad.setLuz(Constantes.TIPO_SI);
					entidad.setCodigoContrato(luzxtienda.getCodigoContrato());
					entidad.setAnio(filtroTemporal.getAnioFiltro());
					entidad.setMes(filtroTemporal.getMesFiltro());
					listaUtil.generarCxCLuz(model, entidad, request);
				}
				model.addAttribute("respuesta", "Se generó los cobros de Luz Exitosamente");
				if (intCodigoLuz != null){ 
					filtroTemporal.setCountLuzGenerado(cxcBitacoraDao.countCobroLuzGenerado(intCodigoLuz));
				}else{
					filtroTemporal.setCountLuzGenerado(0);
				}
				listaTiendaSuministro = new ArrayList<TiendaSuministroBean>();
				for(int i=0; listaTienda!=null && i < listaTienda.size(); i++){
					tienda = listaTienda.get(i);
					//Datos de tienda
					tiendaSuministro = new TiendaSuministroBean();
					tiendaSuministro.setNumero(tienda.getNumero());
					tiendaSuministro.setNumeroSuministro(tienda.getTblSuministro().getNumero());
					tiendaSuministro.setPiso(tienda.getPiso());
					tiendaSuministro.setArea(tienda.getArea());
					tiendaSuministro.setEstadoTienda(tienda.getEstadoTienda());
					tiendaSuministro.setCodigoTienda(tienda.getCodigoTienda());
					tiendaSuministro.setAnio(filtroTemporal.getAnioFiltro());
					tiendaSuministro.setMes(filtroTemporal.getMesFiltro());
					
					TblLuzxtienda luzxtienda = (TblLuzxtienda)mapLuzxTienda.get(tiendaSuministro.getCodigoTienda());
					if (luzxtienda !=null){
						tiendaSuministro.setFechaFin(luzxtienda.getFechaFin());
						tiendaSuministro.setMontoGenerado(luzxtienda.getMontoGenerado());
						tiendaSuministro.setMontoContrato(luzxtienda.getMontoContrato());
						tiendaSuministro.setValorCobrado(luzxtienda.getValorCobrado());
						tiendaSuministro.setSaldo(luzxtienda.getSaldo());
						tiendaSuministro.setCodigoLuzxtienda(luzxtienda.getCodigoLuzxtienda());
						//Codigo del contrato
						tiendaSuministro.setCodigoContrato(luzxtienda.getCodigoContrato());
						
					}else{
						tiendaSuministro.setMontoGenerado(new BigDecimal("0"));
						tiendaSuministro.setMontoContrato(new BigDecimal("0"));
						tiendaSuministro.setValorCobrado(new BigDecimal("0"));
						tiendaSuministro.setSaldo(new BigDecimal("0"));
					}
					listaTiendaSuministro.add(tiendaSuministro);
				}
			}else{
				filtroTemporal.setCountLuzGenerado(0);
			}
	
			

			model.addAttribute("registros", listaTiendaSuministro);
			model.addAttribute("filtro", filtro);
			request.getSession().setAttribute("sessionFiltroSuministroLuz", filtroTemporal);
			logger.debug("[generarCobro] Fin");
		}catch(Exception e){
			logger.debug("[generarCobro] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			listaTienda				= null;
			tienda					= null;
			mapLuzxTienda	 		= null;
			listaLuzxTienda			= null;
			listaTiendaSuministro	= null;
			tiendaSuministro		= null;
			strFecha				= null;
			datFecha				= null;
		}
		logger.debug("[generarCobro] Fin");
		return path;
	}
	/*
	 * Valida los criterios de busqueda
	 */
	private boolean validarCampos(Model model, Filtro filtro){
		boolean resultado = false;
		try{
			if (filtro.getCodigoEdificacionFiltro() <= 0){
				model.addAttribute("respuesta", "Debe seleccionar el Inmueble");
				return resultado;
			}
			if (filtro.getCodigoSuministroFiltro() <= 0){
				model.addAttribute("respuesta", "Debe seleccionar el Suministro");
				return resultado;
			}
			if (filtro.getAnioFiltro() <= 0){
				model.addAttribute("respuesta", "Debe seleccionar el Año");
				return resultado;
			}
			if (filtro.getMesFiltro().equals("-1")){
				model.addAttribute("respuesta", "Debe seleccionar el Mes");
				return resultado;
			}
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
		return resultado;
		
	}
	/*
	 * Obtiene el ultimo dia del mes
	 */
	private String getFechaMesAnio(String strMes, Integer intAnio){
		String strFecha = null;
		Integer intMes 	= null;
		try{
			intMes = new Integer(strMes) - 1;
			strFecha = UtilSGT.getOnlyLastDay(intMes, intAnio);
			strFecha = strFecha+"/"+strMes+"/"+intAnio;
			
		}catch(Exception e){
			e.printStackTrace();
			strFecha = "01/01/2010";
		}
		return strFecha;
	}
	
	/**
	 * Se encarga de direccionar a la pantalla de creacion del 
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "cliente/suministroxinmueble/nuevo", method = RequestMethod.GET)
	public String crearSuministroxLocal(Model model, HttpServletRequest request) {
		LuzBean luzBean = new LuzBean();
		
		Filtro filtro						= null;
		try{
			logger.debug("[crearSuministroxLocal] Inicio");
			filtro = (Filtro)request.getSession().getAttribute("sessionFiltroSuministroLuz");
			
			if (filtro.getCodigoEdificacionFiltro() > 0){
				luzBean.setCodigoInmueble(filtro.getCodigoEdificacionFiltro());
				luzBean.setCodigoSuministro(filtro.getCodigoSuministroFiltro());
				luzBean.setIntAnio(filtro.getAnioFiltro());
				luzBean.setStrMes(filtro.getMesFiltro());
				model.addAttribute("mapAnioFiltro", request.getSession().getAttribute("SessionMapAnio"));
				model.addAttribute("mapSuministro", request.getSession().getAttribute("SessionMapSuministroxTienda"));
			}else{
				this.setDataInicial(luzBean, model);
			}
			
			model.addAttribute("entidad", luzBean);
			//Listado de meses
			listaUtil.cargarDatosModel(model, Constantes.MAP_MESES);
			logger.debug("[crearSuministroxLocal] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "cliente/suministroxinmueble/sxi_nuevo";
	}
	public void setDataInicial(LuzBean luzBean, Model model){
		List<TblParametro> listaParametro	= null;
		List<TblTienda> listaTienda			= null;
		Map<String, Object> mapSuministro 	= null;
		Integer intAnioInicio				= null;
		
		//Inmueble y Suministro
		listaParametro = parametroDao.buscarOneByNombre(Constantes.PAR_INMUEBLE);
		if(listaParametro!=null && listaParametro.size()>0){
			luzBean.setCodigoInmueble(new Integer(listaParametro.get(0).getDato()));
			//Asignacion del suministro
			listaTienda = tiendaDao.listarAllActivos(luzBean.getCodigoInmueble());
			mapSuministro = this.obtenerSuministros(listaTienda);
			model.addAttribute("mapSuministro", mapSuministro);
		}
		//Año y Mes
		listaParametro = parametroDao.buscarOneByNombre(Constantes.PAR_ANIO_INICIO);
		if(listaParametro!=null && listaParametro.size()>0){
			try{
				intAnioInicio = new Integer(listaParametro.get(0).getDato());
			}catch(Exception e1){
				e1.printStackTrace();
				intAnioInicio = Calendar.getInstance().get(Calendar.YEAR);
			}
		}else{
			intAnioInicio = Calendar.getInstance().get(Calendar.YEAR);
		}
		model.addAttribute("mapAnioFiltro", UtilSGT.getListaAnio(intAnioInicio, Calendar.getInstance().get(Calendar.YEAR) + 1 ));
		luzBean.setIntAnio(Calendar.getInstance().get(Calendar.YEAR));
		luzBean.setStrMes(this.getMoth());
	}
	/**
	 * Se encarga los suministros del inmueble seleccionado
	 * seleccionado
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/suministroxinmueble/suministro/c", method = RequestMethod.POST)
	public String cargarSuministroNuevo(Model model, LuzBean luzBean, String path, HttpServletRequest request) {
		//path = "cliente/suministroxinmueble/sxi_listado";
		List<TblTienda> listaTienda			= null;
		Map<String, Object> mapSuministro 	= null;
		try{
			logger.debug("[cargarSuministro] Inicio");
			//Asignacion del suministro
			listaTienda = tiendaDao.listarAllActivos(luzBean.getCodigoInmueble());
			mapSuministro = this.obtenerSuministros(listaTienda);
			model.addAttribute("mapSuministro", mapSuministro);
			listaUtil.cargarDatosModel(model, Constantes.MAP_MESES);
			model.addAttribute("entidad", luzBean);
			logger.debug("[cargarSuministro] Fin");
		}catch(Exception e){
			logger.debug("[cargarSuministro] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{

		}
		logger.debug("[cargarSuministro] Fin");
		return path;
	}
	/*
	 * Valida los criterios de busqueda
	 */
	private boolean validarCamposNuevo(Model model, LuzBean luzBean){
		boolean resultado = false;
		Integer totalLuz	= null;
		Date datFecha		= null;
		String strFecha		= null;
		try{
			if (luzBean.getCodigoInmueble() <= 0){
				model.addAttribute("respuesta", "Debe seleccionar el Inmueble");
				return resultado;
			}
			if (luzBean.getCodigoSuministro() <= 0){
				model.addAttribute("respuesta", "Debe seleccionar el Suministro");
				return resultado;
			}
			if (luzBean.getIntAnio() <= 0){
				model.addAttribute("respuesta", "Debe seleccionar el Año");
				return resultado;
			}
			if (luzBean.getStrMes().equals("-1")){
				model.addAttribute("respuesta", "Debe seleccionar el Mes");
				return resultado;
			}
			strFecha = getFechaMesAnio(luzBean.getStrMes(), luzBean.getIntAnio());
			datFecha = UtilSGT.getDatetoString(strFecha);
			totalLuz = luzDao.countBySuministroFechaVencimiento(luzBean.getCodigoSuministro(), datFecha);
			if (totalLuz>0){
				model.addAttribute("respuesta", "Se encontraron registros para el periodo ["+strFecha+"]");
				return resultado;
			}
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
		return resultado;
		
	}
	private void validarEstadoLocales (ComparacionTiendaBean comparacionBean, List<TblTienda> listaTienda, List<TblLuzxtienda> listaLuzxTiendaAnt){
		Map<Integer, TblLuzxtienda> mapLuzxTienda 			= null;
		TblLuzxtienda tiendaAux								= null;
		try{
			comparacionBean.setIntDesocupadoOcupado(0);
			comparacionBean.setIntIguales(0);
			comparacionBean.setIntOcupadoDesocupado(0);
			//organizacion de los locales
			mapLuzxTienda = new HashMap<Integer, TblLuzxtienda>();
			for(TblLuzxtienda luzxtienda: listaLuzxTiendaAnt){
				mapLuzxTienda.put(luzxtienda.getTblTienda().getCodigoTienda(), luzxtienda);
			}
			//validamos el local y el estado del mismo
			for(TblTienda tienda: listaTienda){
				//verificamos su existencia
				tiendaAux = mapLuzxTienda.get(tienda.getCodigoTienda());
				if (tiendaAux == null){
					comparacionBean.setIntDesocupadoOcupado(comparacionBean.getIntDesocupadoOcupado() + 1);
				}
				if (tiendaAux.getMontoGenerado()!=null && tiendaAux.getMontoGenerado().doubleValue()>0 && tienda.getEstadoTienda().equals(Constantes.ESTADO_TIENDA_OCUPADO)){
					comparacionBean.setIntIguales(comparacionBean.getIntIguales()+1);
				}else{
					//if (tiendaAux.getEstadoTienda().equals(Constantes.ESTADO_TIENDA_DESOCUPADO) && tienda.getEstadoTienda().equals(Constantes.ESTADO_TIENDA_OCUPADO)){
					if (tiendaAux.getMontoGenerado().doubleValue()==0 && tienda.getEstadoTienda().equals(Constantes.ESTADO_TIENDA_OCUPADO)){
						comparacionBean.setIntDesocupadoOcupado(comparacionBean.getIntDesocupadoOcupado() + 1);
					}else
					//if (tiendaAux.getEstadoTienda().equals(Constantes.ESTADO_TIENDA_OCUPADO) && tienda.getEstadoTienda().equals(Constantes.ESTADO_TIENDA_DESOCUPADO)){
					if (tiendaAux.getMontoGenerado().doubleValue() > 0 && tienda.getEstadoTienda().equals(Constantes.ESTADO_TIENDA_DESOCUPADO)){
						comparacionBean.setIntOcupadoDesocupado(comparacionBean.getIntOcupadoDesocupado() + 1);
					}else{
						comparacionBean.setIntIguales(comparacionBean.getIntIguales()+1);
					}
				}
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			mapLuzxTienda 			= null;
			tiendaAux				= null;
			
		}
	}
	/*
	 * Criterio 1: se asigna por proporcionalidad
	 */
	private void asignarCriterioUno(List<TblTienda> listaTienda, List<TblLuzxtienda> listaLuzxTiendaAnt, LuzBean luzBean,List<TblLuzxtienda> listaLuzxTienda, HttpServletRequest request){
		List<ComparacionTiendaBean> listaOperacion  		= null;
		ComparacionTiendaBean comparacionBean				= null;
		BigDecimal montoTotal								= null;
		BigDecimal nuevoMontoTotal							= null;
		Map<Integer, TblTienda> mapluzxtienda				= null;
		TblTienda tienda									= null;
		BigDecimal montoResiduo								= null;
		Integer intTotalTienda								= 0;
		BigDecimal nuevoMontoAdicional						= null;
		TblLuzxtienda luzxtienda							= null;
		Map<Integer, ComparacionTiendaBean> mapOperacion 	= null;
		ComparacionTiendaBean comparacionAux				= null;
		String strFecha										= null;
		Date datFecha										= null;
		
		try{
			if (listaLuzxTiendaAnt!=null){
				strFecha = getFechaMesAnio(luzBean.getStrMes(), luzBean.getIntAnio());
				datFecha = UtilSGT.getDatetoString(strFecha);
				
				//Map de las tiendas actuales para luego buscar el estado
				mapluzxtienda = new HashMap<Integer, TblTienda>();
				for(TblTienda tiendaAux:listaTienda){
					mapluzxtienda.put(tiendaAux.getCodigoTienda(), tiendaAux);
				}
				
				listaOperacion = new ArrayList<ComparacionTiendaBean>();
				montoTotal = new BigDecimal("0");
				//Totalizamos el monto de las tiendas
				for(TblLuzxtienda luzxtiendaAux: listaLuzxTiendaAnt){
					comparacionBean = new ComparacionTiendaBean();
					comparacionBean.setCodigoTienda(luzxtiendaAux.getTblTienda().getCodigoTienda());
					comparacionBean.setMontoTienda(luzxtiendaAux.getMontoGenerado());
					montoTotal = montoTotal.add(comparacionBean.getMontoTienda());
					listaOperacion.add(comparacionBean);
				}
				//Calculamos el %
				nuevoMontoTotal = new BigDecimal("0");
				for(ComparacionTiendaBean comparacion: listaOperacion){
					//comparacion.setPorcentaje(montoTotal.divide(comparacion.getMontoTienda()).setScale(0, RoundingMode.CEILING).doubleValue());
					//comparacion.setPorcentaje(new BigDecimal(comparacion.getMontoTienda().doubleValue() /montoTotal.doubleValue()).setScale(2, RoundingMode.CEILING).doubleValue());
					comparacion.setPorcentaje(UtilSGT.montoDivide(comparacion.getMontoTienda(), montoTotal).doubleValue());
					//comparacion.setNuevoMonto(luzBean.getMontoGenerado().multiply(new BigDecimal(comparacion.getPorcentaje())));
					comparacion.setNuevoMonto(UtilSGT.montoMultiplica(luzBean.getMontoGenerado(), new BigDecimal(comparacion.getPorcentaje())));
					//recuperamos la tienda para validar su estado
					tienda = mapluzxtienda.get(comparacion.getCodigoTienda());
					if(tienda.getEstadoTienda().equals(Constantes.ESTADO_TIENDA_DESOCUPADO)){
						comparacion.setNuevoMonto(new BigDecimal("0"));
					}else{
						intTotalTienda = intTotalTienda + 1;
					}
					
					nuevoMontoTotal = nuevoMontoTotal.add(comparacion.getNuevoMonto());
				}
				//Validamos si existe residuo
				montoResiduo = luzBean.getMontoGenerado().subtract(nuevoMontoTotal);
				if(montoResiduo.doubleValue()>0){
					//nuevoMontoAdicional = montoResiduo.divide(new BigDecimal(intTotalTienda)).setScale(0, RoundingMode.CEILING);
					nuevoMontoAdicional = UtilSGT.montoDivide(montoResiduo, new BigDecimal(intTotalTienda));
					for(ComparacionTiendaBean comparacion: listaOperacion){
						//recuperamos la tienda para validar su estado
						tienda = mapluzxtienda.get(comparacion.getCodigoTienda());
						if(tienda.getEstadoTienda().equals(Constantes.ESTADO_TIENDA_OCUPADO)){
							comparacion.setNuevoMonto(comparacion.getNuevoMonto().add(nuevoMontoAdicional));
						}
						
					}
				}
				mapOperacion = new HashMap<Integer, ComparacionTiendaBean>();
				for(ComparacionTiendaBean comparacion: listaOperacion){
					mapOperacion.put(comparacion.getCodigoTienda(), comparacion);
				}
				//Generamos la lista para la tabla
				for(TblTienda tiendaAux: listaTienda){
					luzxtienda = new TblLuzxtienda();
					luzxtienda.setTblTienda(tiendaAux);
					comparacionAux = mapOperacion.get(tiendaAux.getCodigoTienda());
					if (comparacionAux!=null){
						luzxtienda.setMontoGenerado(comparacionAux.getNuevoMonto());
						luzxtienda.setMontoContrato(comparacionAux.getNuevoMonto());
						luzxtienda.setSaldo(comparacionAux.getNuevoMonto());
					}else{
						luzxtienda.setMontoGenerado(new BigDecimal("0"));
						luzxtienda.setMontoContrato(new BigDecimal("0"));
						luzxtienda.setSaldo(new BigDecimal("0"));
					}
					
					luzxtienda.setFechaFin(datFecha);
					luzxtienda.setFechaCreacion(new Date(System.currentTimeMillis()));
					luzxtienda.setIpCreacion(request.getRemoteAddr());
					luzxtienda.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
					luzxtienda.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
					listaLuzxTienda.add(luzxtienda);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			listaOperacion  		= null;
			comparacionBean			= null;
			montoTotal				= null;
			nuevoMontoTotal			= null;
			mapluzxtienda			= null;
			tienda					= null;
			montoResiduo			= null;
			intTotalTienda			= 0;
			nuevoMontoAdicional		= null;
			luzxtienda				= null;
			mapOperacion 			= null;
			comparacionAux			= null;
			strFecha				= null;
			datFecha				= null;
		}
		
	}
	
	/*
	 * Criterio 2: se asigna por asignación
	 */
	private void asignarCriterioDos(List<TblTienda> listaTienda, List<TblLuzxtienda> listaLuzxTiendaAnt, LuzBean luzBean,List<TblLuzxtienda> listaLuzxTienda, HttpServletRequest request){
		BigDecimal montoTotal								= null;
		Map<Integer, TblLuzxtienda> mapluzxtienda			= null;
		BigDecimal montoResiduo								= null;
		Integer intTotalTienda								= 0;
		BigDecimal nuevoMontoAdicional						= null;
		TblLuzxtienda luzxtienda							= null;
		TblLuzxtienda luxTiendaAux						= null;
		String strFecha										= null;
		Date datFecha										= null;
		
		try{
			if (listaLuzxTiendaAnt!=null){
				strFecha = getFechaMesAnio(luzBean.getStrMes(), luzBean.getIntAnio());
				datFecha = UtilSGT.getDatetoString(strFecha);
				
				//Map de la luzxtienda Anterior
				mapluzxtienda = new HashMap<Integer, TblLuzxtienda>();
				for(TblLuzxtienda tiendaAux:listaLuzxTiendaAnt){
					mapluzxtienda.put(tiendaAux.getTblTienda().getCodigoTienda(), tiendaAux);
				}
				
				montoTotal = new BigDecimal("0");
				//Generamos la lista para la tabla
				for(TblTienda tiendaAux: listaTienda){
					luzxtienda = new TblLuzxtienda();
					luzxtienda.setTblTienda(tiendaAux);
					
					luxTiendaAux = mapluzxtienda.get(tiendaAux.getCodigoTienda());
					if (luxTiendaAux!=null){
						luzxtienda.setMontoGenerado(luxTiendaAux.getMontoGenerado());
						luzxtienda.setMontoContrato(luxTiendaAux.getMontoGenerado());
						luzxtienda.setSaldo(luxTiendaAux.getMontoGenerado());
					}else{
						luzxtienda.setMontoGenerado(new BigDecimal("0"));
						luzxtienda.setMontoContrato(new BigDecimal("0"));
						luzxtienda.setSaldo(new BigDecimal("0"));
					}
					montoTotal = montoTotal.add(luzxtienda.getMontoGenerado());
					if (luzxtienda.getMontoGenerado().doubleValue()<=0){
						intTotalTienda = intTotalTienda + 1;
					}
					
					luzxtienda.setFechaFin(datFecha);
					luzxtienda.setFechaCreacion(new Date(System.currentTimeMillis()));
					luzxtienda.setIpCreacion(request.getRemoteAddr());
					luzxtienda.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
					luzxtienda.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
					listaLuzxTienda.add(luzxtienda);
				}
				
				montoResiduo = luzBean.getMontoGenerado().subtract(montoTotal);
				//nuevoMontoAdicional = montoResiduo.divide(new BigDecimal(intTotalTienda)).setScale(0,  RoundingMode.CEILING);
				nuevoMontoAdicional = UtilSGT.montoDivide(montoResiduo, new BigDecimal(intTotalTienda));
				for(TblLuzxtienda luzxTienda :listaLuzxTienda ){
					if (luzxTienda.getMontoGenerado().doubleValue()<=0){
						luzxTienda.setMontoGenerado(nuevoMontoAdicional);
						luzxTienda.setMontoContrato(nuevoMontoAdicional);
						luzxTienda.setSaldo(nuevoMontoAdicional);
					}
				}
				
				
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			montoTotal				= null;
			mapluzxtienda			= null;
			montoResiduo			= null;
			intTotalTienda			= 0;
			nuevoMontoAdicional		= null;
			luzxtienda				= null;
			strFecha				= null;
			datFecha				= null;
		}
		
	}
	
	/**
	 * Se encarga de generar los montos de la luz de cada local del inmueble asociado al suministro seleccionado
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cliente/suministroxinmueble/nuevo/guardar", method = RequestMethod.POST)
	public String generarSuministroxLocal(Model model, LuzBean luzBean, String path, HttpServletRequest request) {
		path = "cliente/suministroxinmueble/sxi_listado";
		List<TblTienda> listaTienda				= null;
		String strFecha							= null;
		Date datFecha							= null;
		TblLuz luz								= null;
		List<TblLuzxtienda> listaLuzxTienda		= null;
		List<TblLuzxtienda> listaLuzxTiendaAnt	= null;
		TblLuzxtienda luzxtienda				= null;
		BigDecimal montoUnitario				= null;
		ComparacionTiendaBean comparacionBean	= null;
		Map<String, Object> mapSuministro 		= null;
		TblContrato contrato					= null;
		//Para el listado
		Filtro filtro							= null;
		List<TblParametro> listaParametro		= null;
		Integer intAnioInicio					= null;
		//BitacoraBean entidad					= null;
		try{
			logger.debug("[generarSuministroxLocal] Inicio");
			
			if (this.validarCamposNuevo(model, luzBean)){
				//Obtiene lista de Locales
				listaTienda= tiendaDao.listarTiendaxSuministro(luzBean.getCodigoSuministro());
				//Obtiene datos de la Luz
				/*TODO: comentamos y dejamos solo la asignación proporcional */
				strFecha = getFechaMesAnio(luzBean.getStrMes(), luzBean.getIntAnio());
				datFecha = UtilSGT.getDatetoString(strFecha);
				/*luz = luzDao.findBySuministroFechaVencimiento(luzBean.getCodigoSuministro(), UtilSGT.getDatePrevious(datFecha));
				if (luz != null && luz.getCodigoLuz() > 0){
					//Obtiene lista de luzxtienda (luz x locales)
					listaLuzxTiendaAnt = luzxTiendaDao.listarLuzTiendaxLuz(luz.getCodigoLuz());
					//Validamos el tipo de asignacion a aplicar segun el estado de los locales
					comparacionBean = new ComparacionTiendaBean();
					this.validarEstadoLocales(comparacionBean, listaTienda, listaLuzxTiendaAnt);
					if (comparacionBean.getIntIguales().compareTo(listaTienda.size())==0){
						//Las tiendas o locales son las mismas del mes anterior al actual en cantidad y estado, por tanto se aplica criterio 1: proporcionalidad en %
						listaLuzxTienda = new ArrayList<TblLuzxtienda>();
						this.asignarCriterioUno(listaTienda, listaLuzxTiendaAnt, luzBean, listaLuzxTienda, request);
					}else if (comparacionBean.getIntOcupadoDesocupado()>0){
						//Se aplica criterio 1: Proporcionalidad en %
						listaLuzxTienda = new ArrayList<TblLuzxtienda>();
						this.asignarCriterioUno(listaTienda, listaLuzxTiendaAnt, luzBean, listaLuzxTienda, request);
					}else if (comparacionBean.getIntDesocupadoOcupado()>0){
						//Se aplica criterio 2: Se asigna la diferencia a la tienda o local ocupado
						listaLuzxTienda = new ArrayList<TblLuzxtienda>();
						this.asignarCriterioDos(listaTienda, listaLuzxTiendaAnt, luzBean, listaLuzxTienda, request);
					}
					
				}else{*/
					//No se tiene registros previos: se asigna proporcionalmente el valor ingresado
					if (listaTienda !=null){
						montoUnitario = UtilSGT.montoDivideUnDecimal(luzBean.getMontoGenerado(), new BigDecimal(listaTienda.size()));
						//montoUnitario = luzBean.getMontoGenerado().divide(new BigDecimal(listaTienda.size()));
						listaLuzxTienda = new ArrayList<TblLuzxtienda>();
						
						BigDecimal acumulado = new BigDecimal(0);
						Integer totalElemento = 1;
						for(TblTienda tienda: listaTienda){
							luzxtienda = new TblLuzxtienda();
							luzxtienda.setTblTienda(tienda);
							luzxtienda.setFechaFin(UtilSGT.getDatetoString(strFecha));
							luzxtienda.setFechaCreacion(new Date(System.currentTimeMillis()));
							luzxtienda.setIpCreacion(request.getRemoteAddr());
							luzxtienda.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
							luzxtienda.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
							if (totalElemento == listaTienda.size()){
								
								luzxtienda.setMontoGenerado(luzBean.getMontoGenerado().subtract(acumulado));
								luzxtienda.setMontoContrato(luzBean.getMontoGenerado().subtract(acumulado));
								luzxtienda.setSaldo(luzBean.getMontoGenerado().subtract(acumulado));
								logger.debug("[generarSuministroxLocal] asignado:"+luzBean.getMontoGenerado().subtract(acumulado));
								acumulado = acumulado.add(luzBean.getMontoGenerado().subtract(acumulado));
								logger.debug("[generarSuministroxLocal] acumulado:"+acumulado.toString());
							}else{
								
								luzxtienda.setMontoGenerado(montoUnitario);
								luzxtienda.setMontoContrato(montoUnitario);
								luzxtienda.setSaldo(montoUnitario);
								acumulado = acumulado.add(montoUnitario);
								logger.debug("[generarSuministroxLocal] montoUnitario:"+montoUnitario);
								logger.debug("[generarSuministroxLocal] acumulado:"+acumulado.toString());
							}
							
							listaLuzxTienda.add(luzxtienda);
							totalElemento = totalElemento + 1;
						}
					}
				/*}*/
				//Pre-Guardar
				luz = new TblLuz();
				luz.setAnio(luzBean.getIntAnio());
				luz.setFechaFin(datFecha);
				luz.setMontoGenerado(luzBean.getMontoGenerado());
				luz.setMontoContrato(luzBean.getMontoGenerado());
				luz.setSaldo(luzBean.getMontoGenerado());
				luz.setTblSuministro(suministroDao.findOne(luzBean.getCodigoSuministro()));
				luz.setEstadoLuz(Constantes.ESTADO_ACTIVO);
				luz.setFechaCreacion(new Date(System.currentTimeMillis()));
				luz.setIpCreacion(request.getRemoteAddr());
				luz.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
				luz.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
				//Registrando
				luzDao.save(luz);
				luz = luzDao.findBySuministroFechaVencimiento(luzBean.getCodigoSuministro(), datFecha);
				for(TblLuzxtienda luzxtiendaAux: listaLuzxTienda){
					//Buscando contrato asignado a la tienda
					contrato = contratoDao.findByNumeroTienda(luzxtiendaAux.getTblTienda().getCodigoTienda());
					if (contrato != null){
						luzxtiendaAux.setCodigoContrato(contrato.getCodigoContrato());
						luzxtiendaAux.setTblLuz(luz);
						luzxTiendaDao.save(luzxtiendaAux);
						/*Registramos la CxC de luz*/
						/*TODO: NO APLICA EN ESTA OPERACION: 2020.11
						entidad = new BitacoraBean();
						entidad.setLuz(Constantes.TIPO_SI);
						entidad.setCodigoContrato(contrato.getCodigoContrato());
						entidad.setAnio(luzBean.getIntAnio());
						entidad.setMes(luzBean.getStrMes());
						listaUtil.generarCxCLuz(model, entidad, request);
						*/
						
					}else{
						luzxtiendaAux.setTblLuz(luz);
						luzxTiendaDao.save(luzxtiendaAux);
						
					}
					
					
				}
				model.addAttribute("respuesta", "Se genero exitosamente los montos de luz");
				
				//this.traerRegistros(model, path, request);
				//Listado de Registros
				filtro = new Filtro();
				
				filtro.setCodigoEdificacionFiltro(luzBean.getCodigoInmueble());
				listaTienda = tiendaDao.listarAllActivos(filtro.getCodigoEdificacionFiltro());
				mapSuministro = this.obtenerSuministros(listaTienda);
				//Buscando el año de inicio
				listaParametro = parametroDao.buscarOneByNombre(Constantes.PAR_ANIO_INICIO);
				if(listaParametro!=null && listaParametro.size()>0){
					try{
						intAnioInicio = new Integer(listaParametro.get(0).getDato());
					}catch(Exception e1){
						e1.printStackTrace();
						intAnioInicio = Calendar.getInstance().get(Calendar.YEAR);
					}
				}else{
					intAnioInicio = Calendar.getInstance().get(Calendar.YEAR);
				}
				filtro.setAnioInicio(intAnioInicio);
				filtro.setAnioFiltro(luzBean.getIntAnio());
				filtro.setMesFiltro(luzBean.getStrMes());
				filtro.setCodigoSuministroFiltro(luzBean.getCodigoSuministro());
				
				this.cargarListaOperacionLuz(model, filtro);
				request.getSession().setAttribute("SessionMapAnio", UtilSGT.getListaAnio(filtro.getAnioInicio(), Calendar.getInstance().get(Calendar.YEAR) + 1 ));
				request.getSession().setAttribute("SessionMapSuministroxTienda", mapSuministro);
				model.addAttribute("filtro", filtro);
				request.getSession().setAttribute("sessionFiltroSuministroLuz", filtro);
				this.buscarSuministroxLocal(model, filtro, path, request);
			}else{
				//Asignacion del suministro
				listaTienda = tiendaDao.listarAllActivos(luzBean.getCodigoInmueble());
				mapSuministro = this.obtenerSuministros(listaTienda);
				model.addAttribute("mapSuministro", mapSuministro);
				listaUtil.cargarDatosModel(model, Constantes.MAP_MESES);
				model.addAttribute("entidad", luzBean);
				path= "cliente/suministroxinmueble/sxi_nuevo";
				
			}
			logger.debug("[generarSuministroxLocal] Fin");
		}catch(Exception e){
			logger.debug("[generarSuministroxLocal] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			
		}
		logger.debug("[generarSuministroxLocal] Fin");
		return path;
	}
	
	public void mListarRegistrosLuz(){
		
	}
	/**
	 * Se encarga de direccionar a la pantalla de edicion
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "cliente/suministroxinmueble/editar/{id}", method = RequestMethod.GET)
	public String editarSuministroxLocal(@PathVariable Integer id, Model model) {
		TblLuzxtienda entidad 			= null;
		try{
			entidad = luzxTiendaDao.findOne(id);
			entidad.setMontoCalculado(entidad.getTblLuz().getMontoContrato().subtract(luzxTiendaDao.totalMontoContrato(entidad.getTblLuz().getCodigoLuz())));
			entidad.setMontoSinTienda(entidad.getTblLuz().getMontoContrato().subtract(entidad.getMontoContrato()).subtract(entidad.getMontoCalculado()));
			model.addAttribute("entidad", entidad);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return "cliente/suministroxinmueble/sxi_edicion";
	}
	/*
	 * 
	 */
	@RequestMapping(value = "cliente/suministroxinmueble/editar/guardar", method = RequestMethod.POST)
	public String editarEntidad(TblLuzxtienda entidad, Model model, HttpServletRequest request) {
		//Map<String, Object> campos 	= null;
		String path 				= "cliente/suministroxinmueble/sxi_listado";;
		TblLuzxtienda entidadEnBd 	= null;
		TblContrato contrato 		= null;
		try{
			// Se actualizan solo los campos del formulario
			entidadEnBd = luzxTiendaDao.findOne(entidad.getCodigoLuzxtienda());
			entidadEnBd.setMontoGenerado(entidad.getMontoContrato());
			entidadEnBd.setMontoContrato(entidad.getMontoContrato());
			entidadEnBd.setSaldo(entidad.getMontoContrato());
			entidadEnBd.setValorCobrado(new BigDecimal("0"));
			this.preEditar(entidadEnBd, request);
			//Buscando contrato asignado a la tienda
			if (entidadEnBd.getCodigoContrato() <=0 ){
				contrato = contratoDao.findByNumeroTienda(entidadEnBd.getTblTienda().getCodigoTienda());
				if (contrato != null){
					entidadEnBd.setCodigoContrato(contrato.getCodigoContrato());
				}
			}
			luzxTiendaDao.save(entidadEnBd);
			model.addAttribute("respuesta", "Guardado exitoso");
			//this.traerRegistros(model, path, request);
			Filtro filtro = (Filtro)request.getSession().getAttribute("sessionFiltroSuministroLuz");
			this.buscarSuministroxLocal(model, filtro, path, request);
			logger.debug("[guardarEntidad] Guardado..." );
			
		}catch(Exception e){
			e.printStackTrace();
			path = "cliente/suministroxinmueble/sxi_edicion";
			model.addAttribute("entidad", entidad);
		}
		return path;
		
	}
	
	@Override
	public void preEditar(TblLuzxtienda entidad, HttpServletRequest request) {
		try{
			logger.debug("[preEditar] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			logger.debug("[preEditar] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void preEditarLuz(TblLuz entidad, HttpServletRequest request) {
		try{
			logger.debug("[preEditar] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			logger.debug("[preEditar] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@RequestMapping(value = "cliente/suministrosxinmueble/regresar", method = RequestMethod.GET)
	public String regresarListado(Model model, String path, HttpServletRequest request) {
		Filtro filtro 						= null;
		List<TblParametro> listaParametro	= null;
		List<TblTienda> listaTienda			= null;
		Map<String, Object> mapSuministro 	= null;
		Integer intAnioInicio				= null;
		
		TblTienda tienda						= null;
		Map<Integer, Object> mapLuzxTienda	 	= null;
		List<TblLuzxtienda>  listaLuzxTienda	= null;
		List<TiendaSuministroBean> listaTiendaSuministro	= null;
		TiendaSuministroBean tiendaSuministro	= null;
		String strFecha							= null;
		Date datFecha							= null;
		
		try{
			logger.debug("[traerRegistros] Inicio");
			path = "cliente/suministroxinmueble/sxi_listado";
			filtro = (Filtro)request.getSession().getAttribute("sessionFiltroSuministroLuz");
			if (filtro ==null){
				filtro = new Filtro();
				//Asignacion del inmueble
				filtro.setCodigoEdificacionFiltro(-1);
				listaParametro = parametroDao.buscarOneByNombre(Constantes.PAR_INMUEBLE);
				if(listaParametro!=null && listaParametro.size()>0){
					filtro.setCodigoEdificacionFiltro(new Integer(listaParametro.get(0).getDato()));
					//Asignacion del suministro
					listaTienda = tiendaDao.listarAllActivos(filtro.getCodigoEdificacionFiltro());
					mapSuministro = this.obtenerSuministros(listaTienda);
					
				}
				//Buscando el año de inicio
				listaParametro = parametroDao.buscarOneByNombre(Constantes.PAR_ANIO_INICIO);
				if(listaParametro!=null && listaParametro.size()>0){
					try{
						intAnioInicio = new Integer(listaParametro.get(0).getDato());
					}catch(Exception e1){
						e1.printStackTrace();
						intAnioInicio = Calendar.getInstance().get(Calendar.YEAR);
					}
				}else{
					intAnioInicio = Calendar.getInstance().get(Calendar.YEAR);
				}
				filtro.setAnioInicio(intAnioInicio);
				filtro.setAnioFiltro(Calendar.getInstance().get(Calendar.YEAR));
				filtro.setMesFiltro(getMoth());
			}else{
				//Asignacion del suministro
				listaTienda = tiendaDao.listarAllActivos(filtro.getCodigoEdificacionFiltro());
				mapSuministro = this.obtenerSuministros(listaTienda);
			}
			
			
			this.cargarListaOperacionLuz(model, filtro);
			if (validarCampos(model, filtro)){
				
				//Listado de Tiendas (Locales)
				listaTienda= tiendaDao.listarTiendaxSuministro(filtro.getCodigoSuministroFiltro());
				//Listado de LuzxTienda (costo luz)
				strFecha = getFechaMesAnio(filtro.getMesFiltro(), filtro.getAnioFiltro());
				datFecha = UtilSGT.getDatetoString(strFecha);
				listaLuzxTienda = luzxTiendaDao.listarLuzTiendaxSuministro(filtro.getCodigoSuministroFiltro(), datFecha);
				if (listaLuzxTienda!=null){
					mapLuzxTienda = new HashMap<Integer, Object>();
					for(TblLuzxtienda luzxtienda: listaLuzxTienda){
						mapLuzxTienda.put(luzxtienda.getTblTienda().getCodigoTienda(), luzxtienda);
					}
					listaTiendaSuministro = new ArrayList<TiendaSuministroBean>();
					for(int i=0; listaTienda!=null && i < listaTienda.size(); i++){
						tienda = listaTienda.get(i);
						//Datos de tienda
						tiendaSuministro = new TiendaSuministroBean();
						tiendaSuministro.setNumero(tienda.getNumero());
						tiendaSuministro.setNumeroSuministro(tienda.getTblSuministro().getNumero());
						tiendaSuministro.setPiso(tienda.getPiso());
						tiendaSuministro.setArea(tienda.getArea());
						tiendaSuministro.setEstadoTienda(tienda.getEstadoTienda());
						tiendaSuministro.setCodigoTienda(tienda.getCodigoTienda());
						tiendaSuministro.setAnio(filtro.getAnioFiltro());
						tiendaSuministro.setMes(filtro.getMesFiltro());
						TblLuzxtienda luzxtienda = (TblLuzxtienda)mapLuzxTienda.get(tiendaSuministro.getCodigoTienda());
						if (luzxtienda !=null){
							tiendaSuministro.setFechaFin(luzxtienda.getFechaFin());
							tiendaSuministro.setMontoGenerado(luzxtienda.getMontoGenerado());
							tiendaSuministro.setMontoContrato(luzxtienda.getMontoContrato());
							tiendaSuministro.setValorCobrado(luzxtienda.getValorCobrado());
							tiendaSuministro.setSaldo(luzxtienda.getSaldo());
							tiendaSuministro.setCodigoLuzxtienda(luzxtienda.getCodigoLuzxtienda());
							
						}else{
							tiendaSuministro.setMontoGenerado(new BigDecimal("0"));
							tiendaSuministro.setMontoContrato(new BigDecimal("0"));
							tiendaSuministro.setValorCobrado(new BigDecimal("0"));
							tiendaSuministro.setSaldo(new BigDecimal("0"));
						}
						listaTiendaSuministro.add(tiendaSuministro);
					}
				}
			}
			

			model.addAttribute("registros", listaTiendaSuministro);
			model.addAttribute("filtro", filtro);
			
			request.getSession().setAttribute("SessionMapAnio", UtilSGT.getListaAnio(filtro.getAnioInicio(), Calendar.getInstance().get(Calendar.YEAR) + 1 ));
			request.getSession().setAttribute("SessionMapSuministroxTienda", mapSuministro);
			//model.addAttribute("mapSuministroxTienda", mapSuministro);
			model.addAttribute("filtro", filtro);
			request.getSession().setAttribute("sessionFiltroSuministroLuz", filtro);
			logger.debug("[traerRegistros] Fin");
		}catch(Exception e){
			logger.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		
		return path;
	}
}
