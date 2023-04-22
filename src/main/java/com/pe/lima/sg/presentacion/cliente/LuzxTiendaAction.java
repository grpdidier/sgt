package com.pe.lima.sg.presentacion.cliente;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pe.lima.sg.dao.BaseDAO;
import com.pe.lima.sg.dao.cliente.ILuzDAO;
import com.pe.lima.sg.dao.cliente.ILuzxTiendaDAO;
import com.pe.lima.sg.dao.mantenimiento.IParametroDAO;
import com.pe.lima.sg.dao.mantenimiento.ITiendaDAO;
import com.pe.lima.sg.entity.cliente.TblLuz;
import com.pe.lima.sg.entity.cliente.TblLuzxtienda;
import com.pe.lima.sg.entity.mantenimiento.TblParametro;
import com.pe.lima.sg.entity.mantenimiento.TblSuministro;
import com.pe.lima.sg.entity.mantenimiento.TblTienda;
import com.pe.lima.sg.entity.mantenimiento.TiendaBean;
import com.pe.lima.sg.presentacion.BasePresentacion;
import com.pe.lima.sg.presentacion.BeanRequest;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.UtilSGT;

import lombok.extern.slf4j.Slf4j;

/**
 * Clase Bean que se encarga de la administracion del registro del monto de la luz por luz
 *
 * 			
 */
@Slf4j
@Controller
public class LuzxTiendaAction extends BasePresentacion<TblLuz> {
	
	@Autowired
	private ILuzDAO luzDao;
	
	@Autowired
	private ILuzxTiendaDAO luzxTiendaDao;
	
	//@Autowired
	//private ISuministroDAO suministroDao;
	
	//@Autowired
	//private ListaUtilAction listaUtil;
	
	@Autowired
	private ITiendaDAO tiendaDao;
	

	@Autowired
	private IParametroDAO parametroDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDAO getDao() {
		return null;
	}
	
	/**
	 * Se encarga de listar todos los luces
	 * 
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cliente/lucesxtienda", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path,HttpServletRequest request) {
		TblSuministro suministro 				= new TblSuministro();
		Filtro filtro 							= new Filtro();
		TblLuz luz 								= new TblLuz();
		Map<String, Integer> listaSuministro 	= null;
		Map.Entry<String, Integer> elemento  	= null;
		String strFechaFin 						= null;
		List<TblTienda> listaTienda				= null;
		//List<TblLuzxtienda> listaLuzxTienda = null;
		BeanRequest bean 						= new BeanRequest();
		List<TblParametro> listaParametro		= null;
		Integer intAnioInicio					= null;
		try{
			log.debug("[traerRegistros] Inicio");
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
			model.addAttribute("mapAnioFiltro", UtilSGT.getListaAnio(intAnioInicio, Calendar.getInstance().get(Calendar.YEAR) + 1 ));
			//Path de la pagina
			path = "cliente/luzxtienda/lxt_listado";
			luz.setTblSuministro(new TblSuministro());
			//model.addAttribute("suminitro", suminitro);
			model.addAttribute("filtro", filtro);
			//model.addAttribute("luz", luz);
			filtro.setLuz(luz);
			filtro.setSuministro(suministro);
			filtro.setAnioFiltro(Calendar.getInstance().get(Calendar.YEAR));
			log.debug("[traerRegistros] Mes:"+ Calendar.getInstance().get(Calendar.MONTH) );
			strFechaFin = UtilSGT.getLastDay( Calendar.getInstance().get(Calendar.MONTH) , Calendar.getInstance().get(Calendar.YEAR));
			log.debug("[traerRegistros] strFechaFin:"+strFechaFin);
			filtro.setStrFechaFinFiltro(strFechaFin);
			filtro.setMapListado(obtenerFechaVencimiento(Calendar.getInstance().get(Calendar.YEAR)));
			model.addAttribute("mapFechaVencimiento", filtro.getMapListado());
			listaSuministro = (Map<String, Integer>)request.getSession().getAttribute("SessionMapSuministro");
			if (listaSuministro != null){
				elemento = listaSuministro.entrySet().iterator().next();
				log.debug("[traerRegistros] Key:"+elemento.getKey() + " Value: "+elemento.getValue());
				suministro.setCodigoSuministro(elemento.getValue());
				luz.getTblSuministro().setCodigoSuministro(elemento.getValue());
				luz = luzDao.findBySuministroFechaVencimiento(elemento.getValue(), UtilSGT.getDatetoString(filtro.getStrFechaFinFiltro()));
				listaTienda = tiendaDao.listarTiendaxSuministro(elemento.getValue());
				//bean.setLuz(luz);
				bean.setListaTiendaBean(copyBean(listaTienda));
				if(bean != null && luz !=null){
					for(TiendaBean tienda:bean.getListaTiendaBean()){
						tienda.setTblLuzxtienda(luzxTiendaDao.listarLuzTienda(luz.getCodigoLuz(), tienda.getCodigoTienda()));
					}
				}
				
			}
			model.addAttribute("registros", bean.getListaTiendaBean());
			//model.addAttribute("beanRequest", bean);
			filtro.setBeanRequest(bean);
			log.debug("[traerRegistros] Fin");
		}catch(Exception e){
			log.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			suministro = null;
			filtro = null;
			luz = null;
			listaSuministro = null;
			elemento  = null;
			strFechaFin = null;
			listaTienda	= null;
			bean = null;
		}
		
		return path;
	}
	
	private List<TiendaBean> copyBean(List<TblTienda> lista){
		List<TiendaBean> listaTiendaBean = null;
		TiendaBean tiendaBean = null;
		try{
			if (lista!=null){
				listaTiendaBean = new ArrayList<TiendaBean> ();
				for(TblTienda tienda:lista){
					tiendaBean = new TiendaBean();
					 BeanUtils.copyProperties(tienda, tiendaBean);
					 tiendaBean.setTblLuzxtienda(new TblLuzxtienda());
					 listaTiendaBean.add(tiendaBean);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			log.debug("[copyBean] Error:"+e.getMessage());
		}
		return listaTiendaBean;
	}
	
	
	/**
	 * Se encarga de buscar la informacion del suministro seleccionado
	 * seleccionado
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cliente/lucesxtienda/suministro", method = RequestMethod.POST)
	public String cargarTiendasxSuministro(Model model, Filtro filtro, String path,HttpServletRequest request) {
		path									= "cliente/luzxtienda/lxt_listado";
		List<TblTienda> listaTienda				= null;
		Integer anioInicio						= null;
		String fechaFin							= null;
		try{
			log.debug("[cargarTiendasxSuministro] Inicio");
			//log.debug("[cargarTiendasxSuministro] Codigo Suminitro:"+filtro.getSuministro().getCodigoSuministro());
			if (filtro.getAnioInicio()==null){
				anioInicio = Calendar.getInstance().get(Calendar.YEAR);
			}else{
				anioInicio = filtro.getAnioInicio();
			}
			if (filtro.getStrFechaFinFiltro()==null){
				fechaFin = UtilSGT.getFecha("dd/MM/yyyy");
			}else{
				fechaFin = filtro.getStrFechaFinFiltro();
			}
			model.addAttribute("mapAnioFiltro", UtilSGT.getListaAnio(anioInicio, Calendar.getInstance().get(Calendar.YEAR) + 1 ));
			
			//Busqueda de los datos de la luz
			filtro.setLuz(luzDao.findBySuministroFechaVencimiento(filtro.getSuministro().getCodigoSuministro(), UtilSGT.getDatetoString(fechaFin)));
			//Busqueda de los datos de las tiendas - format to Bean for presentation
			listaTienda = tiendaDao.listarTiendaxSuministro(filtro.getSuministro().getCodigoSuministro());
			filtro.setBeanRequest(new BeanRequest());
			filtro.getBeanRequest().setListaTiendaBean(copyBean(listaTienda));
			if(filtro.getLuz() !=null){
				for(TiendaBean tienda:filtro.getBeanRequest().getListaTiendaBean()){
					tienda.setTblLuzxtienda(luzxTiendaDao.listarLuzTienda(filtro.getLuz().getCodigoLuz(), tienda.getCodigoTienda()));
				}
			}
			filtro.setMapListado(obtenerFechaVencimiento(filtro.getAnioFiltro()));
			model.addAttribute("mapFechaVencimiento", filtro.getMapListado());
			
			model.addAttribute("registros", filtro.getBeanRequest().getListaTiendaBean());
			model.addAttribute("filtro", filtro);
			log.debug("[cargarTiendasxSuministro] Fin");
		}catch(Exception e){
			log.debug("[cargarTiendasxSuministro] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			listaTienda = null;
		}
		log.debug("[cargarTiendasxSuministro] Fin");
		return path;
	}
	
	/**
	 * Se encarga de buscar la informacion del suministro seleccionado
	 * seleccionado
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cliente/lucesxtienda/ultimodia", method = RequestMethod.POST)
	public String cargarListaUltimoDia(Model model, Filtro filtro, String path,HttpServletRequest request) {
		path									= "cliente/luzxtienda/lxt_listado";
		List<TblTienda> listaTienda				= null;
		String strFechaFin 						= null;
		try{
			log.debug("[cargarListaUltimoDia] Inicio");
			log.debug("[cargarListaUltimoDia] Codigo Suminitro:"+filtro.getSuministro().getCodigoSuministro());
			model.addAttribute("mapAnioFiltro", UtilSGT.getListaAnio(filtro.getAnioInicio(), Calendar.getInstance().get(Calendar.YEAR) + 1 ));
			//Calculo de la nueva fecha de vencimiento
			log.debug("[cargarListaUltimoDia] Fecha Anterior:"+filtro.getStrFechaFinFiltro());
			strFechaFin = UtilSGT.getLastDay( UtilSGT.getMes(filtro.getStrFechaFinFiltro()),filtro.getAnioFiltro());
			filtro.setStrFechaFinFiltro(strFechaFin);
			//Busqueda de los datos de la luz
			if (filtro.getStrFechaFinFiltro()!= null){
				filtro.setLuz(luzDao.findBySuministroFechaVencimiento(filtro.getSuministro().getCodigoSuministro(), UtilSGT.getDatetoString(filtro.getStrFechaFinFiltro())));
			}else{
				filtro.setLuz(null);
			}
			
			//Busqueda de los datos de las tiendas - format to Bean for presentation
			listaTienda = tiendaDao.listarTiendaxSuministro(filtro.getSuministro().getCodigoSuministro());
			filtro.setBeanRequest(new BeanRequest());
			filtro.getBeanRequest().setListaTiendaBean(copyBean(listaTienda));
			if(filtro.getLuz() !=null){
				for(TiendaBean tienda:filtro.getBeanRequest().getListaTiendaBean()){
					TblLuzxtienda luzxtienda = luzxTiendaDao.listarLuzTienda(filtro.getLuz().getCodigoLuz(), tienda.getCodigoTienda());
					if (luzxtienda !=null){
						tienda.setTblLuzxtienda(luzxtienda);
					}
					
				}
			}
			filtro.setMapListado(obtenerFechaVencimiento(filtro.getAnioFiltro()));
			model.addAttribute("mapFechaVencimiento", filtro.getMapListado());
			
			model.addAttribute("registros", filtro.getBeanRequest().getListaTiendaBean());
			model.addAttribute("filtro", filtro);
			log.debug("[cargarListaUltimoDia] Fin");
		}catch(Exception e){
			log.debug("[cargarListaUltimoDia] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			listaTienda = null;
		}
		log.debug("[cargarListaUltimoDia] Fin");
		return path;
	}
	
	/**
	 * Se encarga de buscar la informacion del suministro seleccionado
	 * seleccionado
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cliente/lucesxtienda/tienda", method = RequestMethod.POST)
	public String cargarListaTienda(Model model, Filtro filtro, String path,HttpServletRequest request) {
		path									= "cliente/luzxtienda/lxt_listado";
		List<TblTienda> listaTienda				= null;
		try{
			log.debug("[cargarListaTienda] Inicio");
			log.debug("[cargarListaTienda] Codigo Suminitro:"+filtro.getSuministro().getCodigoSuministro());
			model.addAttribute("mapAnioFiltro", UtilSGT.getListaAnio(filtro.getAnioInicio(), Calendar.getInstance().get(Calendar.YEAR) + 1 ));
			//Calculo de la nueva fecha de vencimiento
			log.debug("[cargarListaTienda] Fecha Anterior:"+filtro.getStrFechaFinFiltro());
			//strFechaFin = this.getLastDay( UtilSGT.getMes(filtro.getStrFechaFinFiltro()),filtro.getAnioFiltro());
			//filtro.setStrFechaFinFiltro(strFechaFin);
			//Busqueda de los datos de la luz
			filtro.setLuz(luzDao.findBySuministroFechaVencimiento(filtro.getSuministro().getCodigoSuministro(), UtilSGT.getDatetoString(filtro.getStrFechaFinFiltro())));
			//Busqueda de los datos de las tiendas - format to Bean for presentation
			listaTienda = tiendaDao.listarTiendaxSuministro(filtro.getSuministro().getCodigoSuministro());
			filtro.setBeanRequest(new BeanRequest());
			filtro.getBeanRequest().setListaTiendaBean(copyBean(listaTienda));
			if(filtro.getLuz() !=null){
				for(TiendaBean tienda:filtro.getBeanRequest().getListaTiendaBean()){
					tienda.setTblLuzxtienda(luzxTiendaDao.listarLuzTienda(filtro.getLuz().getCodigoLuz(), tienda.getCodigoTienda()));
				}
			}
			filtro.setMapListado(obtenerFechaVencimiento(filtro.getAnioFiltro()));
			model.addAttribute("mapFechaVencimiento", filtro.getMapListado());
			
			model.addAttribute("registros", filtro.getBeanRequest().getListaTiendaBean());
			model.addAttribute("filtro", filtro);
			log.debug("[cargarListaTienda] Fin");
		}catch(Exception e){
			log.debug("[cargarListaTienda] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			listaTienda = null;
		}
		log.debug("[cargarListaTienda] Fin");
		return path;
	}
	/*** Listado de Luces ***/
	/*private void listarLuces(Model model, TblLuz luz){
		List<TblLuz> entidades = new ArrayList<TblLuz>();
		Date datFechaFin = null;
		Specification<TblLuz> criterio = null;
		try{
			/*if (luz.getFechaFin()==null){
				criterio = Specifications.where(conNumero(TblLuz.getNumeroFiltro()))
						.and(conEstado(Constantes.ESTADO_REGISTRO_ACTIVO));
			}else{
				datFechaFin = UtilSGT.getDatetoString(filtro.getStrFechaFinFiltro());
				criterio = Specifications.where(conNumero(filtro.getNumeroFiltro()))
						.and(conFechaFin(datFechaFin))
						.and(conEstado(Constantes.ESTADO_REGISTRO_ACTIVO));
			}
			*//*
			entidades = luzDao.findAll(criterio);
			log.debug("[listarLuces] entidades:"+entidades);
			model.addAttribute("registros", entidades);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades = null;
		}
	}*/

	@Override
	public TblLuz getNuevaEntidad() {
		
		return null;
	}
		

	public Map<String, Object> obtenerFechaVencimiento(Integer intAnio) {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		String strAnio = null;
		try{
			log.debug("[obtenerFechaVencimiento] inicio");
			strAnio = intAnio.toString();
			resultados.put("31/01/"+strAnio, "31/01/"+strAnio);
			if(intAnio % 4 == 0){
				resultados.put("29/02/"+strAnio, "29/02/"+strAnio);
			}else{
				resultados.put("28/02/"+strAnio, "28/02/"+strAnio);
			}
			resultados.put("31/03/"+strAnio, "31/03/"+strAnio);
			resultados.put("30/04/"+strAnio, "30/04/"+strAnio);
			resultados.put("31/05/"+strAnio, "31/05/"+strAnio);
			resultados.put("30/06/"+strAnio, "30/06/"+strAnio);
			resultados.put("31/07/"+strAnio, "31/07/"+strAnio);
			resultados.put("31/08/"+strAnio, "31/08/"+strAnio);
			resultados.put("30/09/"+strAnio, "30/09/"+strAnio);
			resultados.put("31/10/"+strAnio, "31/10/"+strAnio);
			resultados.put("30/11/"+strAnio, "31/11/"+strAnio);
			resultados.put("31/12/"+strAnio, "31/12/"+strAnio);
			
			log.debug("[obtenerFechaVencimiento] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
		
		return resultados;
	}
	
	
	/**
	 * Se encarga de direccionar a la pantalla de creacion del Luz por Tienda
	 * 
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "cliente/luzxtienda/nuevo", method = RequestMethod.GET)
	public String crearLuzxTienda(Model model, HttpServletRequest request) {
		//Variables para tener todo en session
		TblSuministro suministro 				= new TblSuministro();
		TblLuz luzBean							= null;
		TblLuz luzAuxBean						= null;
		Map<String, Integer> listaSuministro 	= null;
		Map.Entry<String, Integer> elemento  	= null;
		String strFechaFin 						= null;
		List<TblTienda> listaTienda				= null;
		//Variables para tener todo en session
		BeanRequest bean 						= new BeanRequest();
		try{
			log.debug("[crearLuzxTienda] Inicio");
			luzBean = new TblLuz();
			luzBean.setTblSuministro(new TblSuministro());
			strFechaFin = UtilSGT.getLastDay( Calendar.getInstance().get(Calendar.MONTH) , Calendar.getInstance().get(Calendar.YEAR));
			log.debug("[crearLuzxTienda] strFechaFin:"+strFechaFin);
			luzBean.setFechaFin(UtilSGT.getDatetoString(strFechaFin));
			listaSuministro = (Map<String, Integer>)request.getSession().getAttribute("SessionMapSuministro");
			if (listaSuministro != null){
				elemento = listaSuministro.entrySet().iterator().next();
				log.debug("[crearLuzxTienda] Key:"+elemento.getKey() + " Value: "+elemento.getValue());
				suministro.setCodigoSuministro(elemento.getValue());
				luzBean.getTblSuministro().setCodigoSuministro(elemento.getValue());
				luzAuxBean = luzDao.findBySuministroFechaVencimiento(elemento.getValue(), luzBean.getFechaFin());
				listaTienda = tiendaDao.listarTiendaxSuministro(elemento.getValue());
				bean.setListaTiendaBean(copyBean(listaTienda));
				if(bean != null && luzAuxBean !=null){
					for(TiendaBean tienda:bean.getListaTiendaBean()){
						tienda.setTblLuzxtienda(luzxTiendaDao.listarLuzTienda(luzAuxBean.getCodigoLuz(), tienda.getCodigoTienda()));
					}
				}
				
			}
			model.addAttribute("entidad",luzBean);
			model.addAttribute("registros", bean.getListaTiendaBean());
			bean.setLuz(luzBean);
			request.getSession().setAttribute("beanRequest", bean);
			log.debug("[crearLuzxTienda] Fin");
		}catch(Exception e){
			log.debug("[crearLuzxTienda] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			suministro = null;
			listaSuministro = null;
			elemento  = null;
			strFechaFin = null;
			listaTienda	= null;
			bean = null;
		}
		return "cliente/luzxtienda/lxt_nuevo";
	}
	@Override
	public void preGuardar(TblLuz luz, HttpServletRequest request) {
		BeanRequest bean 		= null;
		TblLuzxtienda luzxtienda=null;
		try{
			log.debug("[preGuardar] Inicio" );
			luz.setFechaCreacion(new Date(System.currentTimeMillis()));
			luz.setIpCreacion(request.getRemoteAddr());
			luz.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			luz.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			bean = (BeanRequest) request.getSession().getAttribute("beanRequest");
			bean.setListaLuzxTienda(new ArrayList<TblLuzxtienda>());
			for(TiendaBean tienda: bean.getListaTiendaBean()){
				luzxtienda = tienda.getTblLuzxtienda();
				luzxtienda.setTblLuz(luz);
				luzxtienda.setFechaCreacion(new Date(System.currentTimeMillis()));
				luzxtienda.setIpCreacion(request.getRemoteAddr());
				luzxtienda.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
				luzxtienda.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
				bean.getListaLuzxTienda().add(luzxtienda);
			}
			log.debug("[preGuardar] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			bean 		= null;
			luzxtienda	= null;
		}
		
	}
	/** Validamos logica del negocio**/
	@Override
	public boolean validarNegocio(Model model,TblLuz luz, HttpServletRequest request){
		boolean exitoso = true;
		Integer total 	= null;
		BeanRequest bean = null;
		double suma		= 0;
		try{
			//Validando los datos
			if (luz.getTblSuministro().getCodigoSuministro() == -1){
				exitoso = false;
				model.addAttribute("respuesta", "Debe seleccionar un suministro de la lista.");
				return exitoso;
			}
			if (luz.getFechaFin() == null || luz.getFechaFin().toString().equals("")){
				exitoso = false;
				model.addAttribute("respuesta", "Debe seleccionar la fecha fin de suministro");
				return exitoso;
			}
			if (!UtilSGT.validarFinMes(luz.getFechaFin())){
				exitoso = false;
				model.addAttribute("respuesta", "La fecha seleccionada debe ser el ultimo dia del mes, corregir la fecha.");
				return exitoso;
			}
			if (luz.getMontoGenerado() == null || luz.getMontoGenerado().doubleValue()<= 0){
				exitoso = false;
				model.addAttribute("respuesta", "El monto de la luz [Monto Generado] debe ser mayor a CERO");
				return exitoso;
			}
			//Validando la existencia del registro de luz
			total = luzDao.countBySuministroFechaVencimiento(luz.getTblSuministro().getCodigoSuministro(),luz.getFechaFin());
			if (total > 0){
				exitoso = false;
				model.addAttribute("respuesta", "El registro de luz existe para la fecha de vencimiento: "+luz.getFechaFin()+", debe modificar la fecha de vencimiento para continuar...");
				return exitoso;
			}
			bean = (BeanRequest) request.getSession().getAttribute("beanRequest");
			if (bean.getListaTiendaBean() == null || bean.getListaTiendaBean().size()<=0){
				exitoso = false;
				model.addAttribute("respuesta", "No se tiene locales asociadas al suministro seleccionado, debe asociar antes de continuar.");
				return exitoso;
			}
			for(TiendaBean tienda : bean.getListaTiendaBean()){
				suma = suma + tienda.getTblLuzxtienda().getMontoGenerado().doubleValue();
			}
			if (suma == 0 || suma != luz.getMontoGenerado().doubleValue()){
				exitoso = false;
				model.addAttribute("respuesta", "El monto generado del suministro ["+luz.getMontoGenerado().doubleValue()+"] no coincide con la suma de los montos de las tiendas ["+suma+"]");
				return exitoso;
			}
			
		}catch(Exception e){
			exitoso = false;
		}finally{
			total = null;
		}
		return exitoso;
	}
	/**
	 * Se encarga de guardar la informacion de la luz x tienda
	 * 
	 * @param luz
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "cliente/lucesxtienda/nuevo/guardar", method = RequestMethod.POST)
	public String guardarEntidad(Model model, TblLuz luz, HttpServletRequest request, String path) {
		path = "cliente/luzxtienda/lxt_listado";
		TblLuz luzAux				= null;
		BeanRequest bean 			= null;
		try{ 
			log.debug("[guardarEntidad] Inicio" );
			bean = (BeanRequest) request.getSession().getAttribute("beanRequest");
			if (this.validarNegocio(model, luz, request)){
				log.debug("[guardarEntidad] Pre Guardar..." );
				this.preGuardar(luz, request);
				boolean exitoso = super.guardar(luz, model);
				log.debug("[guardarEntidad] Guardado..." );
				if (exitoso){
					luzAux = luzDao.findBySuministroFechaVencimiento(luz.getTblSuministro().getCodigoSuministro(), luz.getFechaFin());
					//Registrando los datos de la luzxtienda
					for(TblLuzxtienda luzxtienda:bean.getListaLuzxTienda()){
						luzxtienda.setTblLuz(luzAux);
						luzxTiendaDao.save(luzxtienda);
					}
					//Listando los suministros
					this.traerRegistros(model, path, request);
					
				}else{
					path = "cliente/luzxtienda/lxt_nuevo";
					model.addAttribute("registros", bean.getListaTiendaBean());
					model.addAttribute("luz", luz);
					
				}
			}else{
				path = "cliente/luzxtienda/lxt_nuevo";
				model.addAttribute("registros", bean.getListaTiendaBean());
				model.addAttribute("luz", luz);
			}
			
			log.debug("[guardarEntidad] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			luzAux 			= null;
			bean			= null;
		}
		return path;
		
	}
	
	/** Validamos los datos basicos antes de realizar el calculo de los montos de las tiendas**/
	public boolean validarDatosPrevioCalculo(Model model,TblLuz luz, HttpServletRequest request){
		boolean exitoso = true;
		Integer total 	= null;
		BeanRequest bean = null;
		try{
			//Validando los datos
			if (luz.getTblSuministro().getCodigoSuministro() == -1){
				exitoso = false;
				model.addAttribute("respuesta", "Debe seleccionar un suministro de la lista.");
				return exitoso;
			}
			if (luz.getFechaFin() == null || luz.getFechaFin().toString().equals("")){
				exitoso = false;
				model.addAttribute("respuesta", "Debe seleccionar la fecha fin de suministro");
				return exitoso;
			}
			if (!UtilSGT.validarFinMes(luz.getFechaFin())){
				exitoso = false;
				model.addAttribute("respuesta", "La fecha seleccionada debe ser el ultimo dia del mes, corregir la fecha.");
				return exitoso;
			}
			if (luz.getMontoGenerado() == null || luz.getMontoGenerado().doubleValue()<= 0){
				exitoso = false;
				model.addAttribute("respuesta", "El monto de la luz [Monto Generado] debe ser mayor a CERO");
				return exitoso;
			}
			//Validando la existencia del registro de luz
			total = luzDao.countBySuministroFechaVencimiento(luz.getTblSuministro().getCodigoSuministro(),luz.getFechaFin());
			if (total > 0){
				exitoso = false;
				model.addAttribute("respuesta", "El registro de luz existe para la fecha de vencimiento: "+luz.getFechaFin()+", debe modificar la fecha de vencimiento para continuar...");
				return exitoso;
			}
			bean = (BeanRequest) request.getSession().getAttribute("beanRequest");
			if (bean.getListaTiendaBean() == null || bean.getListaTiendaBean().size()<=0){
				exitoso = false;
				model.addAttribute("respuesta", "No se tiene locales asociadas al suministro seleccionado, debe asociar antes de continuar.");
				return exitoso;
			}
			
			
		}catch(Exception e){
			exitoso = false;
		}finally{
			total = null;
		}
		return exitoso;
	}
	/*
	 * Calcula los montos para los locales
	 */
	@RequestMapping(value = "cliente/lucesxtienda/calculo", method = RequestMethod.POST)
	public String calcularLuzxTienda(Model model, TblLuz luz, HttpServletRequest request, String path) {
		path = "cliente/luzxtienda/lxt_nuevo";
		TblLuz luzAux							= null;
		BeanRequest bean 						= null;
		List<TblLuzxtienda> listaLuzxTiendaAux 	= null;
		String strComparacion					= null;
		double totalLuzxTienda					= 0;
		TblLuzxtienda luzxtiendaAux				= null;
		Integer totalDesocupados				= null;
		try{
			log.debug("[calcularLuzxTienda] Inicio" );
			bean = (BeanRequest) request.getSession().getAttribute("beanRequest");
			if (this.validarDatosPrevioCalculo(model, luz, request)){
				luzAux = luzDao.findBySuministroFechaVencimiento(luz.getTblSuministro().getCodigoSuministro(), UtilSGT.getDatePrevious(luz.getFechaFin()));
				if (luzAux !=null && luzAux.getCodigoLuz() > 0){
					listaLuzxTiendaAux = luzxTiendaDao.listarLuzTiendaxLuz(luzAux.getCodigoLuz());
					strComparacion = compararListaTienda(listaLuzxTiendaAux, bean.getListaTiendaBean());
					//Validar: Locales desocupados iguales --> se prorratea el monto
					if (strComparacion.equals("00")){
						totalLuzxTienda = getTotalLuzxTienda(listaLuzxTiendaAux);
						for(TiendaBean tiendaBean:bean.getListaTiendaBean()){
							luzxtiendaAux = getBeanLuzxTienda(listaLuzxTiendaAux , tiendaBean.getNumero());
							tiendaBean.getTblLuzxtienda().setMontoGenerado( new BigDecimal((luz.getMontoGenerado().doubleValue() /totalLuzxTienda) * luzxtiendaAux.getMontoGenerado().doubleValue() ));
							tiendaBean.getTblLuzxtienda().setFechaFin(luz.getFechaFin());
						}
					}
					// Locales diferente: si Anterior es Mayor --> se excluye el antiguo y prorratea
					if (strComparacion.equals("10") || strComparacion.equals("20")){
						totalLuzxTienda = getTotalLuzxTiendaOcupados(listaLuzxTiendaAux, bean.getListaTiendaBean());
						for(TiendaBean tiendaBean:bean.getListaTiendaBean()){
							if (tiendaBean.getEstadoTienda().equals(Constantes.ESTADO_TIENDA_DESOCUPADO)){
								tiendaBean.getTblLuzxtienda().setMontoGenerado(new BigDecimal("0"));
								tiendaBean.getTblLuzxtienda().setFechaFin(luz.getFechaFin());
							}else{
								luzxtiendaAux = getBeanLuzxTienda(listaLuzxTiendaAux , tiendaBean.getNumero());
								tiendaBean.getTblLuzxtienda().setMontoGenerado( new BigDecimal((luz.getMontoGenerado().doubleValue() /totalLuzxTienda) * luzxtiendaAux.getMontoGenerado().doubleValue() ));
								tiendaBean.getTblLuzxtienda().setFechaFin(luz.getFechaFin());
							}
						}
					}
					// Locales diferente: si Anterior es Menor --> se asigna los montos del mes pasado y se calcula con la diferencia para asignar
					if (strComparacion.equals("01") || strComparacion.equals("02")){
						totalDesocupados = this.contarDesocupadosLuzxTienda(listaLuzxTiendaAux, bean.getListaTiendaBean());
						for(TiendaBean tiendaBean:bean.getListaTiendaBean()){
							luzxtiendaAux = getBeanLuzxTienda(listaLuzxTiendaAux , tiendaBean.getNumero());
							if ( tiendaBean.getEstadoTienda().equals(Constantes.ESTADO_TIENDA_OCUPADO) &&
								 luzxtiendaAux.getTblTienda().getEstadoTienda().equals(Constantes.ESTADO_TIENDA_DESOCUPADO)
								){
								tiendaBean.getTblLuzxtienda().setMontoGenerado(new BigDecimal(luz.getMontoGenerado().doubleValue()/totalDesocupados));
								tiendaBean.getTblLuzxtienda().setFechaFin(luz.getFechaFin());
							}else{
								tiendaBean.getTblLuzxtienda().setMontoGenerado( luzxtiendaAux.getMontoGenerado());
								tiendaBean.getTblLuzxtienda().setFechaFin(luz.getFechaFin());
							}
						}
					}
					
				}else{
					//No existe registro del mes anterior --> se divide entre la cantidad de locales y se asigna
					for(TiendaBean tiendaBean:bean.getListaTiendaBean()){
						tiendaBean.getTblLuzxtienda().setMontoGenerado( new BigDecimal((luz.getMontoGenerado().doubleValue() /bean.getListaTiendaBean().size()) ));
						tiendaBean.getTblLuzxtienda().setFechaFin(luz.getFechaFin());
					}
				}
				
			}else{
				path = "cliente/luzxtienda/lxt_nuevo";
				model.addAttribute("registros", bean.getListaTiendaBean());
				model.addAttribute("luz", luz);
			}
			
			log.debug("[guardarLuzxTienda] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			luzAux 			= null;
			bean			= null;
		}
		return path;
		
	}
	/*
	 * Compara las listas del mes anterior (TblLuzxtienda) y el actual (TiendaBean)
	 */
	private String compararListaTienda(List<TblLuzxtienda> listaLuzxTienda, List<TiendaBean> listaLuzxTiendaBean){
		String resultado = "";
		boolean encontrado = false;
		try{
			if (listaLuzxTienda!=null && listaLuzxTiendaBean!=null){
				//validando longitud
				if (listaLuzxTienda.size() == listaLuzxTiendaBean.size()){
					for(TblLuzxtienda luzxTienda:listaLuzxTienda){
						encontrado = false;
						for(TiendaBean tiendaBean : listaLuzxTiendaBean){
							if ( luzxTienda.getTblTienda().getNumero().equals(tiendaBean.getNumero()) && 
								 luzxTienda.getTblTienda().getEstadoTienda().equals(tiendaBean.getEstadoTienda()) && 
								 tiendaBean.getEstadoTienda().equals(Constantes.ESTADO_TIENDA_OCUPADO) 
							   ){
								encontrado = true;
								break;
							}
						}
						if(!encontrado){
							resultado = "20";//Mayor la lista del mes pasado(TblTienda)
							break;
						}
					}
					//Validamos con la segunda lista, la lista esta ok
					if (encontrado){
						for(TiendaBean tiendaBean : listaLuzxTiendaBean){
							encontrado = false;
							for(TblLuzxtienda luzxTienda:listaLuzxTienda){
								if ( luzxTienda.getTblTienda().getNumero().equals(tiendaBean.getNumero()) &&
									 luzxTienda.getTblTienda().getEstadoTienda().equals(tiendaBean.getEstadoTienda()) && 
									 tiendaBean.getEstadoTienda().equals(Constantes.ESTADO_TIENDA_OCUPADO) 
								   ){
									encontrado = true;
									break;
								}
							}
							if(!encontrado){
								resultado = "02";//Mayor la lista del mes actual (Tiendabean)
								break;
							}
						}
					}
					if (encontrado){
						resultado = "00";
					}
					
				}else{
					if (listaLuzxTienda.size() > listaLuzxTiendaBean.size()){
						resultado = "10"; //Mayor la lista del mes pasado(TblTienda)
					}else{
						resultado = "01"; //Mayor la lista del mes actual (Tiendabean)
					}
				}
			}else{
				resultado = "00"; //Listas vacias
			}
		}catch(Exception e){
			
		}finally{
			
		}
		return resultado;
	}
	
	/*
	 * Obtiene la suma total de los montos generados
	 */
	private double getTotalLuzxTienda(List<TblLuzxtienda> listaLuzxTienda){
		double suma = 0;
		try{
			for(TblLuzxtienda luzxtienda:listaLuzxTienda){
				suma = suma + luzxtienda.getMontoGenerado().doubleValue();
			}
		}catch(Exception e){
			
		}
		return suma;
		
	}
	/*
	 * Obtiene la suma total de los montos generados solo de los ocupados
	 */
	private double getTotalLuzxTiendaOcupados(List<TblLuzxtienda> listaLuzxTienda, List<TiendaBean> listaTiendaBean){
		double suma = 0;
		TiendaBean tiendaBean = null;
		try{
			for(TblLuzxtienda luzxtienda:listaLuzxTienda){
				tiendaBean = this.getBeanTiendaBean(listaTiendaBean, luzxtienda.getTblTienda().getNumero());
				if (tiendaBean.getEstadoTienda().equals(Constantes.ESTADO_TIENDA_OCUPADO)){
					suma = suma + luzxtienda.getMontoGenerado().doubleValue();
				}
				
			}
		}catch(Exception e){
			
		}
		return suma;
		
	}
	/*
	 * Obtiene el Bean con el numero de TblLuzxTienda
	 */
	private TblLuzxtienda getBeanLuzxTienda(List<TblLuzxtienda> listaLuzxTienda, String numero){
		TblLuzxtienda luzxtienda = null;
		try{
			for(TblLuzxtienda luzxtiendaAux: listaLuzxTienda){
				if(luzxtiendaAux.getTblTienda().getNumero().equals(numero)){
					luzxtienda = luzxtiendaAux;
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return luzxtienda;
	}
	/*
	 * Obtiene el Bean con el numero de TiendaBean
	 */
	private TiendaBean getBeanTiendaBean(List<TiendaBean> listaTiendaBean, String numero){
		TiendaBean tienda = null;
		try{
			for(TiendaBean tiendaBean: listaTiendaBean){
				if(tiendaBean.getNumero().equals(numero)){
					tienda = tiendaBean;
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return tienda;
	}
	
	private Integer contarDesocupadosLuzxTienda(List<TblLuzxtienda> listaLuzxTienda,List<TiendaBean> listaTiendaBean){
		Integer total = 0;
		TiendaBean tienda = null;
		try{
			for(TblLuzxtienda luzxtiendaAux: listaLuzxTienda){
				tienda = this.getBeanTiendaBean(listaTiendaBean, luzxtiendaAux.getTblTienda().getNumero());
				if( luzxtiendaAux.getTblTienda().getEstadoTienda().equals(Constantes.ESTADO_TIENDA_DESOCUPADO) && 
					tienda.getEstadoTienda().equals(Constantes.ESTADO_TIENDA_OCUPADO)
				  ){
					total = total + 1;
					break;
				}
			}
		}catch(Exception e){
			
		}
		return total;
	}
	
	/**
	 * Se encarga de direccionar a la pantalla de edicion del Concepto
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cliente/luzxtienda/editar/{id}", method = RequestMethod.GET)
	public String editarLuzxTienda(@PathVariable Integer id, Model model) {
		TblLuz entidad 			= null;
		List<TblLuzxtienda> listaLuzxTienda = null;
		try{
			entidad = luzDao.findOne(id);
			model.addAttribute("luz", entidad);
			listaLuzxTienda = luzxTiendaDao.listarLuzTiendaxLuz(id);
			model.addAttribute("registros", listaLuzxTienda);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return "cliente/luzxtienda/lxt_edicion";
	}
	
	public boolean validarNegocioEdicion(Model model,TblLuz luz, HttpServletRequest request){
		boolean exitoso = true;
		try{
			//Validando los datos
			if (luz.getTblSuministro().getCodigoSuministro() == -1){
				exitoso = false;
				model.addAttribute("respuesta", "Debe seleccionar un suministro de la lista.");
				return exitoso;
			}
			if (luz.getFechaFin() == null || luz.getFechaFin().toString().equals("")){
				exitoso = false;
				model.addAttribute("respuesta", "Debe seleccionar la fecha fin de suministro");
				return exitoso;
			}
			if (!UtilSGT.validarFinMes(luz.getFechaFin())){
				exitoso = false;
				model.addAttribute("respuesta", "La fecha seleccionada debe ser el ultimo dia del mes, corregir la fecha.");
				return exitoso;
			}
			if (luz.getMontoGenerado() == null || luz.getMontoGenerado().doubleValue()<= 0){
				exitoso = false;
				model.addAttribute("respuesta", "El monto de la luz [Monto Generado] debe ser mayor a CERO");
				return exitoso;
			}
			
		}catch(Exception e){
			exitoso = false;
		}
		return exitoso;
	}
	@Override
	public void preEditar(TblLuz entidad, HttpServletRequest request) {
		try{
			log.debug("[preEditar] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			log.debug("[preEditar] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/cliente/lucesxtienda/nuevo/editar", method = RequestMethod.POST)
	public String editarEntidad(TblLuz entidad, Model model, HttpServletRequest request) {
		String path 				= "cliente/luzxtienda/lxt_edicion";;
		TblLuz entidadEnBd 		= null;
		try{
			if (this.validarNegocioEdicion(model, entidad, request)){
				// Se actualizan solo los campos del formulario
				entidadEnBd = luzDao.findOne(entidad.getCodigoLuz());
				entidadEnBd.setTblSuministro(entidad.getTblSuministro());
				entidadEnBd.setFechaFin(entidad.getFechaFin());
				entidadEnBd.setMontoGenerado(entidad.getMontoGenerado());
				this.preEditar(entidadEnBd, request);
				boolean exitoso = super.guardar(entidadEnBd, model);
				log.debug("[guardarEntidad] Guardado..." );
				if (exitoso){
					List<TblLuzxtienda> entidades = luzxTiendaDao.listarLuzTiendaxLuz(entidad.getCodigoLuz());
					model.addAttribute("registros", entidades);
					model.addAttribute("luz", entidad);
				}else{
					model.addAttribute("respuesta", "Error en el Proceso!");
					List<TblLuzxtienda> entidades = luzxTiendaDao.listarLuzTiendaxLuz(entidad.getCodigoLuz());
							model.addAttribute("registros", entidades);
							model.addAttribute("luz", entidad);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
		
	}
	/**
	 * Se encarga de direccionar a la pantalla de edicion del Concepto
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cliente/luzxtienda/editar/detalle/{id}", method = RequestMethod.GET)
	public String editarLuzxTiendaDetalle(@PathVariable Integer id, Model model) {
		TblLuzxtienda luzxtienda = null;
		try{
			luzxtienda = luzxTiendaDao.findOne(id);
			model.addAttribute("luzxtienda", luzxtienda);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			luzxtienda = null;
		}
		return "cliente/luzxtienda/lxt_edicion_detalle";
	}
	public boolean validarNegocioEdicionDetalle(Model model,TblLuzxtienda luzxtienda, HttpServletRequest request){
		boolean exitoso = true;
		try{
			//Validando los datos
			
			if (luzxtienda.getFechaFin() == null || luzxtienda.getFechaFin().toString().equals("")){
				exitoso = false;
				model.addAttribute("respuesta", "Debe seleccionar la fecha fin");
				return exitoso;
			}
			if (!UtilSGT.validarFinMes(luzxtienda.getFechaFin())){
				exitoso = false;
				model.addAttribute("respuesta", "La fecha seleccionada debe ser el ultimo dia del mes, corregir la fecha.");
				return exitoso;
			}
			if (luzxtienda.getMontoGenerado() == null || luzxtienda.getMontoGenerado().doubleValue()<= 0){
				exitoso = false;
				model.addAttribute("respuesta", "El monto de la luz [Monto Generado] debe ser mayor a CERO");
				return exitoso;
			}
			
		}catch(Exception e){
			exitoso = false;
		}
		return exitoso;
	}
	
	public void preEditarDetalle(TblLuzxtienda entidad, HttpServletRequest request) {
		try{
			log.debug("[preEditar] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			log.debug("[preEditar] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@RequestMapping(value = "/cliente/lucesxtienda/editar/detalle", method = RequestMethod.POST)
	public String editarEntidadDetalle(TblLuzxtienda entidad, Model model, HttpServletRequest request) {
		String path 				= "cliente/luzxtienda/lxt_edicion";;
		TblLuzxtienda entidadEnBd 	= null;
		try{
			if (this.validarNegocioEdicionDetalle(model, entidad, request)){
				// Se actualizan solo los campos del formulario
				entidadEnBd = luzxTiendaDao.findOne(entidad.getCodigoLuzxtienda());
				entidadEnBd.setFechaFin(entidad.getFechaFin());
				entidadEnBd.setMontoGenerado(entidad.getMontoGenerado());
				this.preEditarDetalle(entidadEnBd, request);
				luzxTiendaDao.save(entidadEnBd);
				log.debug("[editarEntidadDetalle] Guardado..." );
				List<TblLuzxtienda> entidades = luzxTiendaDao.listarLuzTiendaxLuz(entidad.getTblLuz().getCodigoLuz());
				model.addAttribute("registros", entidades);
				model.addAttribute("luz", luzDao.findOne(entidad.getTblLuz().getCodigoLuz()));
				model.addAttribute("respuesta", "Guardado exitoso");
			}
			
		}catch(Exception e){
			e.printStackTrace();
			model.addAttribute("respuesta", "Error al intentar guardar");
		}
		return path;
		
	}
}
