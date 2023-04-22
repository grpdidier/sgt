package com.pe.lima.sg.presentacion.cliente;

import static com.pe.lima.sg.dao.cliente.ArbitriosSpecifications.conAnio;
import static com.pe.lima.sg.dao.cliente.ArbitriosSpecifications.conCodigoInmueble;
import static com.pe.lima.sg.dao.cliente.ArbitriosSpecifications.conCodigoTienda;
import static com.pe.lima.sg.dao.cliente.ArbitriosSpecifications.conEstadoTienda;
import static com.pe.lima.sg.dao.cliente.ArbitriosSpecifications.conEstadoArbitrio;
import static com.pe.lima.sg.dao.cliente.ArbitriosSpecifications.conLocal;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pe.lima.sg.bean.cliente.TiendaArbitrioBean;
import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.dao.caja.ICobroArbitrioDAO;
import com.pe.lima.sg.dao.cliente.IArbitrioDAO;
import com.pe.lima.sg.dao.cliente.IContratoDAO;
import com.pe.lima.sg.dao.mantenimiento.IParametroDAO;
import com.pe.lima.sg.dao.mantenimiento.ITiendaDAO;
import com.pe.lima.sg.entity.cliente.TblArbitrio;
import com.pe.lima.sg.entity.cliente.TblContrato;
import com.pe.lima.sg.entity.mantenimiento.TblEdificio;
import com.pe.lima.sg.entity.mantenimiento.TblParametro;
import com.pe.lima.sg.entity.mantenimiento.TblTienda;
import com.pe.lima.sg.presentacion.BaseOperacionPresentacion;
import com.pe.lima.sg.presentacion.BeanRequest;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.ListaUtilAction;
import com.pe.lima.sg.presentacion.util.PageWrapper;
import com.pe.lima.sg.presentacion.util.PageableSG;
import com.pe.lima.sg.presentacion.util.UtilSGT;

/**
 * Clase que se encarga de la administracion de los arbitrios
 *
 * 			
 */
@Controller
public class ArbitrioAction extends BaseOperacionPresentacion<TblArbitrio> {
	
	private static final Logger logger = LogManager.getLogger(ArbitrioAction.class);

	@Autowired
	private IArbitrioDAO arbitrioDao;
	
	@Autowired
	private ICobroArbitrioDAO cobroArbitrioDao;

	@Autowired
	private ITiendaDAO tiendaDao;

	@Autowired
	private IParametroDAO parametroDao;
	
	@Autowired
	private ListaUtilAction listaUtil;

	@Autowired
	private IContratoDAO contratoDao;
	
	private String urlPaginado = "/cliente/arbitrios/paginado/"; 
	
	@Override
	public BaseOperacionDAO<TblArbitrio, Integer> getDao() {
		return arbitrioDao;
	}	


	/**
	 * Se encarga de listar todos los arbitrios x tienda
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cliente/arbitrios", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path,  PageableSG pageable,HttpServletRequest request) {
		Filtro filtro = null;
		List<TblParametro> listaParametro;
		try{
			logger.debug("[traerRegistros] Inicio");
			path = "cliente/arbitrio/arb_listado";
			filtro = new Filtro();
			model.addAttribute("filtro", filtro);
			filtro.setCodigoEdificacionFiltro(-1);
			filtro.setAnioFiltro(Calendar.getInstance().get(Calendar.YEAR));
			listaParametro = parametroDao.buscarOneByNombre(Constantes.PAR_INMUEBLE);
			if(listaParametro!=null && listaParametro.size()>0){
				filtro.setCodigoEdificacionFiltro(new Integer(listaParametro.get(0).getDato()));
			}
			this.cargarListaAnio(model,request);
			this.listarArbitrios(model, filtro, pageable, this.urlPaginado,request);
			request.getSession().setAttribute("sessionFiltroCriterioArbitrio", filtro);
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
	 * Genera la lista de Años
	 */
	@SuppressWarnings("unchecked")
	public void cargarListaAnio(Model model, HttpServletRequest request){
		List<TblParametro> listaParametro		= null;
		Integer intAnioInicio					= null;
		Map<String, Object> resultados			= null;
		try{
			resultados = (Map<String, Object>)request.getSession().getAttribute("SessionmapAnioFiltro");
			if (resultados == null) {
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
				model.addAttribute("mapAnioFiltro", UtilSGT.getListaAnio(intAnioInicio, Calendar.getInstance().get(Calendar.YEAR) + 1 ));
				request.getSession().setAttribute("SessionmapAnioFiltro", UtilSGT.getListaAnio(intAnioInicio, Calendar.getInstance().get(Calendar.YEAR) + 1 ));
			}
			
		}catch(Exception e){
			
		}finally{
			
		}
	}
	/**
	 * Se encarga de buscar la informacion del Concepto segun el filtro
	 * seleccionado
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cliente/arbitrios/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, Filtro filtro, String path,  PageableSG pageable, HttpServletRequest request) {
		path = "cliente/arbitrio/arb_listado";
		try{
			logger.debug("[traerRegistrosFiltrados] Inicio");
			this.listarArbitrios(model, filtro, pageable, this.urlPaginado,request);
			//this.cargarListaAnio(model);
			model.addAttribute("filtro", filtro);
			request.getSession().setAttribute("sessionFiltroCriterioArbitrio", filtro);
			logger.debug("[traerRegistrosFiltrados] Fin");
		}catch(Exception e){
			logger.debug("[traerRegistrosFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{

		}
		logger.debug("[traerRegistrosFiltrados] Fin");
		return path;
	}
	/*** Listado de Arbitrios ***/
	private void listarArbitrios(Model model, Filtro filtro,  PageableSG pageable, String url, HttpServletRequest request){
		//List<TblArbitrio> entidades = new ArrayList<TblArbitrio>();
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "codigoArbitrio"));
		try{
			Specification<TblArbitrio> criterio = Specifications.where(conCodigoInmueble(filtro.getCodigoEdificacionFiltro()))
					.and(conAnio(filtro.getAnioFiltro()))
					.and(conLocal(filtro.getNumeroFiltro()==null?filtro.getNumeroFiltro():filtro.getNumeroFiltro().toUpperCase()))
					.and(conEstadoTienda(Constantes.ESTADO_REGISTRO_ACTIVO))
					.and(conEstadoArbitrio(Constantes.ESTADO_REGISTRO_ACTIVO));
			pageable.setSort(sort);
			Page<TblArbitrio> entidadPage = arbitrioDao.findAll(criterio, pageable);
			PageWrapper<TblArbitrio> page = new PageWrapper<TblArbitrio>(entidadPage, url, pageable);
			model.addAttribute("registros", page.getContent());
			model.addAttribute("page", page);
			
			request.getSession().setAttribute("sessionFiltroCriterioArbitrio", filtro);
			request.getSession().setAttribute("sessionListaArbitrio", page.getContent());
			request.getSession().setAttribute("PageArbitrio", page);
			request.getSession().setAttribute("PageableSGArbitrio", pageable);
			
			/*entidades = arbitrioDao.findAll(criterio);
			logger.debug("[listarArbitrios] entidades:"+entidades);
			model.addAttribute("registros", entidades);*/
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//entidades = null;
		}
	}


	/**
	 * Se encarga de direccionar a la pantalla de edicion del arbitrio
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cliente/arbitrio/editar/{id}", method = RequestMethod.GET)
	public String editarArbitrio(@PathVariable Integer id, Model model) {
		TblArbitrio entidad 			= null;
		TblContrato contrato			= new TblContrato();
		try{
			entidad = arbitrioDao.findOne(id);
			if (entidad.getCodigoContrato()>0){
				contrato = contratoDao.findOne(entidad.getCodigoContrato());
			}
			model.addAttribute("entidad", entidad);
			model.addAttribute("contrato", contrato);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return "cliente/arbitrio/arb_edicion";
	}

	/**
	 * Se encarga de direccionar a la pantalla de creacion del Arbitrio
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "cliente/arbitrio/nuevo", method = RequestMethod.GET)
	public String crearArbitrio(Model model) {
		TblArbitrio arbitrio = null;
		Filtro filtro		 = null;
		try{
			logger.debug("[crearArbitrio] Inicio");
			filtro = new Filtro();
			arbitrio = new TblArbitrio();
			arbitrio.setTblTienda(new TblTienda());
			arbitrio.getTblTienda().setTblEdificio(new TblEdificio());
			filtro.setArbitrio(arbitrio);
			model.addAttribute("entidad", filtro);
			model.addAttribute("registros",null);
			logger.debug("[crearArbitrio] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "cliente/arbitrio/arb_nuevo";
	}
	/*
	 * Lista todos los locales del edificio
	 */
	@RequestMapping(value = "/cliente/arbitrio/local", method = RequestMethod.POST)
	public String listarLocales(Model model, Filtro entidad, HttpServletRequest request) {
		List<TblTienda> listaTienda = null;
		BeanRequest beanRequest		= null;
		try{
			logger.debug("[listarLocales] Inicio");
			beanRequest = new BeanRequest();
			logger.debug("[listarLocales] Edificio:"+entidad.getArbitrio().getTblTienda().getTblEdificio().getCodigoEdificio());
			listaTienda = tiendaDao.listarxInmueble(entidad.getArbitrio().getTblTienda().getTblEdificio().getCodigoEdificio());
			beanRequest.setListaTienda(listaTienda);
			this.cargarLocales(model, listaTienda);
			//this.cargarListaAnio(model);
			request.getSession().setAttribute("beanRequest", beanRequest);
			model.addAttribute("entidad", entidad);
			model.addAttribute("registros",entidad.getListaArbitrio());
			logger.debug("[listarLocales] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			listaTienda = null;
		}
		return "cliente/arbitrio/arb_nuevo";
	}
	/*
	 * Listado de locales del edificio
	 */
	private void cargarLocales(Model model, List<TblTienda> listaTienda){
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		try{
			if (listaTienda!=null && listaTienda.size()>0){
				for(TblTienda tienda:listaTienda){
					resultados.put(tienda.getNumero(), tienda.getCodigoTienda());
				}
				model.addAttribute("mapListaLocales", resultados);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			resultados = null;
		}
	}
	
	@RequestMapping(value = "/cliente/arbitrio/asignar", method = RequestMethod.POST)
	public String asignarArbitrio(Model model, Filtro entidad, HttpServletRequest request) {
		List<TblTienda> listaTienda = null;
		TblArbitrio arbitrio 		= null;
		TblTienda tienda			= null;
		BeanRequest beanRequest		= null;
		try{
			logger.debug("[asignarArbitrio] Inicio");
			entidad.setListaArbitrio( new ArrayList<TblArbitrio>());
			Map<String, String> mapPeriodo = UtilSGT.getMapFechaPeriodo();
			tienda = tiendaDao.findOne(entidad.getArbitrio().getTblTienda().getCodigoTienda());
			if (entidad.getArbitrio().getNumeroMeses()!=null) {
				for(int i =1; i <=entidad.getArbitrio().getNumeroMeses(); i++) {
					arbitrio = new TblArbitrio();
					arbitrio.setTblTienda(tienda);
					arbitrio.setAnio(entidad.getArbitrio().getAnio());
					arbitrio.setMontoGenerado(entidad.getArbitrio().getMontoGenerado());
					arbitrio.setMontoContrato(new BigDecimal("0"));
					arbitrio.setValorCobrado(new BigDecimal("0"));
					arbitrio.setSaldo(entidad.getArbitrio().getMontoGenerado());
					arbitrio.setTrimestre(UtilSGT.getPeriodo(entidad.getArbitrio().getNumeroMeses(), i));
					arbitrio.setFechaFin(UtilSGT.getFechaFinPeriodo(entidad.getArbitrio().getNumeroMeses(),entidad.getArbitrio().getAnio(),i, arbitrio.getTrimestre(),mapPeriodo));
					entidad.getListaArbitrio().add(arbitrio);
				}
			}

			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			beanRequest.setListaArbitrio(entidad.getListaArbitrio());
			listaTienda = beanRequest.getListaTienda();
			this.cargarLocales(model, listaTienda);
			//this.cargarListaAnio(model);
			model.addAttribute("entidad", entidad);
			//model.addAttribute("registros",entidad.getListaArbitrio());
			request.getSession().setAttribute("beanRequest", beanRequest);
			logger.debug("[asignarArbitrio] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			listaTienda = null;
			beanRequest = null;
		}
		return "cliente/arbitrio/arb_nuevo";
	}
	
	/**
	 * Se encarga generar masivamente los arbitrios
	 * 
	 * @param model
	 * @return
	 */
	/*@RequestMapping(value = "/cliente/arbitrio/nuevo", method = RequestMethod.POST)
	public String crearArbitrio(Model model, Filtro filtro, String path,HttpServletRequest request) {
		Double porcentaje = null;
		List<TblParametro> listaParametro;
		try{
			logger.debug("[crearArbitrio] Inicio");
			listaParametro = parametroDao.buscarOneByNombre(Constantes.PAR_ARBITRIO_PORCENTAJE_INCREMENTO);
			if(listaParametro!=null && listaParametro.size()>0){
				try{
					porcentaje = listaParametro.get(0).getValor().doubleValue(); //new Double(listaParametro.get(0).getDato());
				}catch(Exception e1){
					e1.printStackTrace();
					porcentaje = new Double(0);
				}
			}else{
				porcentaje = new Double(0);
			}
			if (!this.generarArbitriosMasivo(request, filtro.getCodigoEdificacionFiltro(), porcentaje, filtro.getAnioFiltro(), model)){
				model.addAttribute("respuesta", "Se encontro arbitros generados para el año procesado. No se puede generar nuevamente.");
			}else{
				model.addAttribute("respuesta", "Se genero exitosamente los arbitrios");
			}
			this.listarArbitrios(model, filtro);
			logger.debug("[crearArbitrio] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "cliente/arbitrio/arb_listado";
	}
*/
	/**
	 * Genera masivamente los arbitrios
	 * criterio: toma como referencia el anterior e incrementa un porcentaje para el nuevo
	 */
	public boolean generarArbitriosMasivo(HttpServletRequest request, Integer intCodigoEdificio, Double dblPorcentaje, Integer intAnio,Model model){
		boolean resultado 					= false;
		List<TblTienda> listaTienda 		= null;
		List<TblArbitrio> listaArbitrioAnt 	= null;
		List<TblArbitrio> listaArbitrioAct 	= null;
		TblArbitrio arbitrioNuevo			= null;
		Double valorCalculado				= null;
		try{
			
			listaTienda = tiendaDao.listarAllActivos(intCodigoEdificio);
			for(TblTienda tienda : listaTienda){

				Specification<TblArbitrio> criterio = Specifications.where((conAnio(intAnio-1))) //Año anterior
						.and(conCodigoTienda(tienda.getCodigoTienda()))
						.and(conEstadoTienda(Constantes.ESTADO_REGISTRO_ACTIVO))
						.and(conEstadoArbitrio(Constantes.ESTADO_REGISTRO_ACTIVO));
				listaArbitrioAnt = arbitrioDao.findAll(criterio);
				if (listaArbitrioAnt == null || listaArbitrioAnt.size()<=0){
					//Se asigna CERO
					//Se consulta si existe registro para el arbitrio del año en curso
					criterio = Specifications.where((conAnio(intAnio))) //Año actual
							.and(conCodigoTienda(tienda.getCodigoTienda()))
							.and(conEstadoTienda(Constantes.ESTADO_REGISTRO_ACTIVO))
							.and(conEstadoArbitrio(Constantes.ESTADO_REGISTRO_ACTIVO));
					listaArbitrioAct = arbitrioDao.findAll(criterio);
					if (listaArbitrioAct == null || listaArbitrioAct.size()<=0){
						//Generamos los arbitrios para el año en curso
						//Trimestre 01
						arbitrioNuevo = asignarCeroArbitrio(request);
						arbitrioNuevo.setTblTienda(tienda);
						arbitrioNuevo.setAnio(intAnio);
						arbitrioNuevo.setFechaInicio(UtilSGT.getDatetoString("01/01/"+intAnio));
						arbitrioNuevo.setFechaFin(UtilSGT.getDatetoString("31/03/"+intAnio));
						arbitrioNuevo.setAnio(intAnio);
						arbitrioNuevo.setTrimestre(Constantes.TRIMESTRE_PRIMERO);
						arbitrioDao.save(arbitrioNuevo);
						//Trimestre 02
						arbitrioNuevo = asignarCeroArbitrio(request);
						arbitrioNuevo.setTblTienda(tienda);
						arbitrioNuevo.setAnio(intAnio);
						arbitrioNuevo.setFechaInicio(UtilSGT.getDatetoString("01/04/"+intAnio));
						arbitrioNuevo.setFechaFin(UtilSGT.getDatetoString("30/06/"+intAnio));
						arbitrioNuevo.setAnio(intAnio);
						arbitrioNuevo.setTrimestre(Constantes.TRIMESTRE_SEGUNDO);
						arbitrioDao.save(arbitrioNuevo);
						//Trimestre 03
						arbitrioNuevo = asignarCeroArbitrio(request);
						arbitrioNuevo.setTblTienda(tienda);
						arbitrioNuevo.setAnio(intAnio);
						arbitrioNuevo.setFechaInicio(UtilSGT.getDatetoString("01/07/"+intAnio));
						arbitrioNuevo.setFechaFin(UtilSGT.getDatetoString("30/09/"+intAnio));
						arbitrioNuevo.setAnio(intAnio);
						arbitrioNuevo.setTrimestre(Constantes.TRIMESTRE_TERCERO);
						arbitrioDao.save(arbitrioNuevo);
						//Trimestre 04
						arbitrioNuevo = asignarCeroArbitrio(request);
						arbitrioNuevo.setTblTienda(tienda);
						arbitrioNuevo.setAnio(intAnio);
						arbitrioNuevo.setFechaInicio(UtilSGT.getDatetoString("01/10/"+intAnio));
						arbitrioNuevo.setFechaFin(UtilSGT.getDatetoString("31/12/"+intAnio));
						arbitrioNuevo.setAnio(intAnio);
						arbitrioNuevo.setTrimestre(Constantes.TRIMESTRE_CUARTO);
						arbitrioDao.save(arbitrioNuevo);
						//Finalizó el proceso
						resultado = true;
					}else{
						//Mensaje de error
						model.addAttribute("respuesta", "[1] Se identifico arbitrios generados para el presente año, se interrumpe el proceso...");
						return false;
					}


				}else{
					//Se calcula el nuevo arbitrio
					//Se consulta si existe registro para el arbitrio del año en curso
					criterio = Specifications.where((conAnio(intAnio))) //Año actual
							.and(conCodigoTienda(tienda.getCodigoTienda()))
							.and(conEstadoTienda(Constantes.ESTADO_REGISTRO_ACTIVO))
							.and(conEstadoArbitrio(Constantes.ESTADO_REGISTRO_ACTIVO));
					listaArbitrioAct = arbitrioDao.findAll(criterio);
					if (listaArbitrioAct == null || listaArbitrioAct.size()<= 0){
						//se registra los nuevos arbitrios
						//Generamos los arbitrios para el año en curso
						for(TblArbitrio arbitrioAux:listaArbitrioAnt){
							valorCalculado = arbitrioAux.getMontoGenerado().doubleValue() +  arbitrioAux.getMontoGenerado().doubleValue() * (dblPorcentaje/100);
							if (arbitrioAux.getTrimestre().equals(Constantes.TRIMESTRE_PRIMERO)){
								//Trimestre 01
								arbitrioNuevo = asignarCeroArbitrio(request);
								arbitrioNuevo.setMontoGenerado(new BigDecimal(valorCalculado));
								arbitrioNuevo.setSaldo(new BigDecimal(valorCalculado));
								arbitrioNuevo.setTrimestre(Constantes.TRIMESTRE_PRIMERO);
								arbitrioNuevo.setTblTienda(tienda);
								arbitrioNuevo.setAnio(intAnio);
								arbitrioNuevo.setFechaInicio(UtilSGT.getDatetoString("01/01/"+intAnio));
								arbitrioNuevo.setFechaFin(UtilSGT.getDatetoString("31/03/"+intAnio));
								arbitrioNuevo.setAnio(intAnio);
								arbitrioDao.save(arbitrioNuevo);
							}else if(arbitrioAux.getTrimestre().equals(Constantes.TRIMESTRE_SEGUNDO)){
								//Trimestre 02
								arbitrioNuevo = asignarCeroArbitrio(request);
								arbitrioNuevo.setMontoGenerado(new BigDecimal(valorCalculado));
								arbitrioNuevo.setSaldo(new BigDecimal(valorCalculado));
								arbitrioNuevo.setTrimestre(Constantes.TRIMESTRE_SEGUNDO);
								arbitrioNuevo.setTblTienda(tienda);
								arbitrioNuevo.setAnio(intAnio);
								arbitrioNuevo.setFechaInicio(UtilSGT.getDatetoString("01/04/"+intAnio));
								arbitrioNuevo.setFechaFin(UtilSGT.getDatetoString("30/06/"+intAnio));
								arbitrioNuevo.setAnio(intAnio);
								arbitrioDao.save(arbitrioNuevo);
							}if(arbitrioAux.getTrimestre().equals(Constantes.TRIMESTRE_TERCERO)){
								//Trimestre 03
								arbitrioNuevo = asignarCeroArbitrio(request);
								arbitrioNuevo.setMontoGenerado(new BigDecimal(valorCalculado));
								arbitrioNuevo.setSaldo(new BigDecimal(valorCalculado));
								arbitrioNuevo.setTrimestre(Constantes.TRIMESTRE_TERCERO);
								arbitrioNuevo.setTblTienda(tienda);
								arbitrioNuevo.setAnio(intAnio);
								arbitrioNuevo.setFechaInicio(UtilSGT.getDatetoString("01/07/"+intAnio));
								arbitrioNuevo.setFechaFin(UtilSGT.getDatetoString("30/09/"+intAnio));
								arbitrioNuevo.setAnio(intAnio);
								arbitrioDao.save(arbitrioNuevo);
							} if(arbitrioAux.getTrimestre().equals(Constantes.TRIMESTRE_CUARTO)){
								//Trimestre 04
								arbitrioNuevo = asignarCeroArbitrio(request);
								arbitrioNuevo.setMontoGenerado(new BigDecimal(valorCalculado));
								arbitrioNuevo.setSaldo(new BigDecimal(valorCalculado));
								arbitrioNuevo.setTrimestre(Constantes.TRIMESTRE_CUARTO);
								arbitrioNuevo.setTblTienda(tienda);
								arbitrioNuevo.setAnio(intAnio);
								arbitrioNuevo.setFechaInicio(UtilSGT.getDatetoString("01/10/"+intAnio));
								arbitrioNuevo.setFechaFin(UtilSGT.getDatetoString("31/12/"+intAnio));
								arbitrioNuevo.setAnio(intAnio);
								arbitrioDao.save(arbitrioNuevo);
							} 
						}
						//Finalizó el proceso
						resultado = true;
					}else{
						//Mensaje de error
						model.addAttribute("respuesta", "[2] Se identifico arbitrios generados para el presente año, se interrumpe el proceso...");
						return false;
					}
				}

			}
			
		}catch(Exception e){
			e.printStackTrace();
			resultado = false;
			model.addAttribute("respuesta", "[3] Se identifico arbitrios generados para el presente año, se interrumpe el proceso...");
		}finally{

		}
		return resultado;

	}
	/*
	 * Genera un bean con valores iniciales
	 */
	private TblArbitrio asignarCeroArbitrio(HttpServletRequest request){
		TblArbitrio arbitrioNuevo			= null;
		arbitrioNuevo = new TblArbitrio();
		arbitrioNuevo.setMontoGenerado(new BigDecimal("0"));
		arbitrioNuevo.setMontoContrato(new BigDecimal("0"));
		arbitrioNuevo.setValorCobrado(new BigDecimal("0"));
		arbitrioNuevo.setSaldo(new BigDecimal("0"));
		arbitrioNuevo.setEstadoArbitrio(Constantes.ESTADO_ACTIVO);
		arbitrioNuevo.setFechaCreacion(new Date(System.currentTimeMillis()));
		arbitrioNuevo.setIpCreacion(request.getRemoteAddr());
		arbitrioNuevo.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
		arbitrioNuevo.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);

		return arbitrioNuevo;
	}


	@Override
	public void preGuardar(TblArbitrio entidad, HttpServletRequest request) {
		
	}
	
	public void preGuardarListado(TblArbitrio arbitrio, HttpServletRequest request) {
		
		try{
			logger.debug("[preGuardarListado] Inicio" );

			arbitrio.setFechaCreacion(new Date(System.currentTimeMillis()));
			arbitrio.setIpCreacion(request.getRemoteAddr());
			arbitrio.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			arbitrio.setCodigoArbitrio(0);
			arbitrio.setValorCobrado(new BigDecimal("0"));
			arbitrio.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			arbitrio.setEstadoArbitrio("S");
			
			logger.debug("[preGuardarListado] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/** Validamos logica del negocio**/
	@Override
	public boolean validarNegocio(Model model,TblArbitrio entidad, HttpServletRequest request){
		boolean exitoso = true;
		try{
			//Validando 
			if (entidad.getMontoGenerado() == null || entidad.getMontoGenerado().doubleValue() < 0){
				exitoso = false;
				model.addAttribute("respuesta", "El valor del monto generado no puede ser vacio o negativo");
			}

		}catch(Exception e){
			exitoso = false;
		}finally{
			
		}
		return exitoso;
	}
	private boolean existeArbitriosEnTiendaxAnio(TblArbitrio arbitrio) {
		
		ArrayList<TblArbitrio> listTiendas = arbitrioDao.listarAllActivosxTiendaxAnio(arbitrio.getTblTienda().getCodigoTienda(), arbitrio.getAnio());
		if (listTiendas != null && !listTiendas.isEmpty()) {
			return true;
		}else {
			return false;
		}
		
		
	}
	public boolean validarNegocioListado(Model model,TblArbitrio arbitrio, HttpServletRequest request, ArrayList<TblArbitrio> registros){
		boolean resultado = true;
		try{
			if (existeArbitriosEnTiendaxAnio(arbitrio)) {
				resultado = false;
				model.addAttribute("respuesta", "Existen registros para la tienda en el año seleccionado.");
				return resultado;
			}
			for(TblArbitrio arb: registros){
				if (arb.getMontoGenerado() == null || arb.getMontoGenerado().doubleValue() < 0){
					resultado = false;
					model.addAttribute("respuesta", "El valor del monto generado no puede ser vacio o negativo");
				}
			}
		}catch(Exception e){
			resultado = false;
		}
		return resultado;
	}
	
	


	@Override
	public void preEditar(TblArbitrio entidad, HttpServletRequest request) {
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
	
	private void actualizarSaldo(TblArbitrio entidadEnBd,TblArbitrio entidad) {
		logger.debug("[actualizarSaldo] Inicio" );
		int count = cobroArbitrioDao.countCobroArbitrio(entidad.getCodigoArbitrio());
		logger.debug("[actualizarSaldo] count:"+count );
		if (count > 0) {
			BigDecimal monto = cobroArbitrioDao.montoCobroArbitrio(entidad.getCodigoArbitrio());
			logger.debug("[actualizarSaldo] monto:"+monto );
			entidadEnBd.setSaldo(entidad.getMontoGenerado().subtract(monto));
			logger.debug("[actualizarSaldo] Saldo Final:"+entidadEnBd.getSaldo() );
		}else {
			entidadEnBd.setSaldo(entidad.getMontoGenerado());
		}
		logger.debug("[actualizarSaldo] Fin" );
	}

	@RequestMapping(value = "/cliente/arbitrio/editar", method = RequestMethod.POST)
	public String editarEntidad(TblArbitrio entidad, Model model,  PageableSG pageable,  HttpServletRequest request) {
		String path 				= "cliente/arbitrio/arb_listado";;
		TblArbitrio entidadEnBd 	= null;
		Filtro filtro				= null;
		try{
			// Se actualizan solo los campos del formulario
			entidadEnBd = arbitrioDao.findOne(entidad.getCodigoArbitrio());
			entidadEnBd.setMontoGenerado(entidad.getMontoGenerado());
			//entidadEnBd.setSaldo(entidad.getSaldo());
			actualizarSaldo(entidadEnBd, entidadEnBd);
			entidadEnBd.setObservacion(entidad.getObservacion());
			this.preEditar(entidadEnBd, request);
			boolean exitoso = super.guardar(entidadEnBd, model);
			logger.debug("[guardarEntidad] Guardado..." );
			if (exitoso){
				filtro = new Filtro();
				model.addAttribute("filtro", filtro);
				filtro.setCodigoEdificacionFiltro(-1);
				filtro.setAnioFiltro(Calendar.getInstance().get(Calendar.YEAR));
				this.listarArbitrios(model, filtro, pageable, this.urlPaginado,request);
				//this.cargarListaAnio(model);
				
			}else{
				path = "cliente/arbitrio/arb_edicion";
				model.addAttribute("entidad", entidad);
			}

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidadEnBd 	= null;
			filtro			= null;
			
		}
		return path;

	}


	@Override
	public TblArbitrio getNuevaEntidad() {
		return new TblArbitrio();
	}
	/**
	 * Se encarga de guardar la informacion del Concepto
	 * 
	 * @param conceptoBean
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "cliente/arbitrio/nuevo/guardar", method = RequestMethod.POST)
	public String guardarEntidad(Model model, Filtro entidad ,  PageableSG pageable , HttpServletRequest request) {
		String path = "cliente/arbitrio/arb_listado";
		boolean exitoso 						= false;
		List<TblTienda> listaTienda 			= null;
		BeanRequest beanRequest					= null;
		TblTienda tienda						= null;
		TblContrato contrato 					= null;
		try{
			logger.debug("[guardarEntidad] Inicio" );
			if (this.validarNegocioListado(model, entidad.getArbitrio(), request,entidad.getListaArbitrio())){
				logger.debug("[guardarEntidad] Pre Guardar..." );
				contrato = contratoDao.findByNumeroTiendaParaArbitrios(entidad.getArbitrio().getTblTienda().getCodigoTienda());
				tienda = tiendaDao.findOne(entidad.getArbitrio().getTblTienda().getCodigoTienda());
				for(TblArbitrio arbitrio:entidad.getListaArbitrio()){
					this.preGuardarListado(arbitrio, request);
					arbitrio.setTblTienda(tienda);
					arbitrio.setAnio(entidad.getArbitrio().getAnio());
					arbitrio.setCodigoContrato(contrato !=null?contrato.getCodigoContrato():0);
					arbitrio.setSaldo(arbitrio.getMontoGenerado());
					exitoso = super.guardar(arbitrio, model);
					if (!exitoso){
						break;
					}
				}
				
				
				logger.debug("[guardarEntidad] Guardado..." );
				if (exitoso){
					this.traerRegistros(model, path, pageable, request);
					
				}else{
					path = "cliente/arbitrio/arb_nuevo";
					beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
					listaTienda = beanRequest.getListaTienda();
					this.cargarLocales(model, listaTienda);
					//this.cargarListaAnio(model);
					model.addAttribute("entidad", entidad);
					
				}
			}else{
				path = "cliente/arbitrio/arb_nuevo";
				beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
				listaTienda = beanRequest.getListaTienda();
				this.cargarLocales(model, listaTienda);
				//this.cargarListaAnio(model);
				setDatosArbitrios(beanRequest, entidad);
				model.addAttribute("entidad", entidad);
				//model.addAttribute("registros",entidad.getListaArbitrio());
			}
			
			logger.debug("[guardarEntidad] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			listaTienda 			= null;
			beanRequest 			= null;
		}
		return path;
		
	}
	
	/**
	 * Se encarga de la eliminacion logica del Arbitrio
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "cliente/arbitrio/eliminar/{id}", method = RequestMethod.GET)
	public String eliminarArbitrio(@PathVariable Integer id, HttpServletRequest request, Model model,  PageableSG pageable) {
		TblArbitrio entidad			= null;
		String path 				= "cliente/arbitrio/arb_listado";
		Filtro entidadFiltro		= null;
		try{
			logger.debug("[eliminarArbitrio] Inicio");
			entidad = arbitrioDao.findOne(id);
			entidad.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			this.preEditar(entidad, request);
			arbitrioDao.save(entidad);
			model.addAttribute("respuesta", "Eliminación exitosa");
			entidadFiltro = (Filtro)request.getSession().getAttribute("sessionFiltroCriterioArbitrio");
			model.addAttribute("filtro", entidadFiltro);
			this.traerRegistrosFiltrados(model, entidadFiltro, path, pageable, request);
			//this.traerRegistros(model, path, pageable, request);
			logger.debug("[eliminarArbitrio] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;
		}
		return path;
	}
	private void setDatosArbitrios(BeanRequest beanRequest,Filtro entidad) {
		
		for(int i=0; i<entidad.getListaArbitrio().size();i++){
			entidad.getListaArbitrio().get(i).setTblTienda(beanRequest.getListaArbitrio().get(i).getTblTienda());
			entidad.getListaArbitrio().get(i).setAnio(beanRequest.getListaArbitrio().get(i).getAnio());
		}
	}
		
	
	public Integer validarContrato(TblArbitrio arbitrio, TblContrato contrato){
		Integer resultado				= null;
		Integer fechaInicioArbitrio		= null;
		Integer fechaFinArbitrio		= null;
		Integer fechaInicioContrato		= null;
		try{
			if (contrato !=null){
				fechaInicioArbitrio = new Integer(UtilSGT.getDateStringFormatYYYMMDD(arbitrio.getFechaInicio()));
				fechaFinArbitrio = new Integer(UtilSGT.getDateStringFormatYYYMMDD(arbitrio.getFechaFin()));
				fechaInicioContrato = new Integer(UtilSGT.getDateStringFormatYYYMMDD(contrato.getFechaInicio()));
				//Menor a la fecha de inicio de contrato
				if(fechaInicioArbitrio < fechaInicioContrato &&
				   fechaFinArbitrio < fechaInicioContrato	
				  ){
					resultado = -1;
				}
				//Mayor a la fecha de inicio de contrato
				if (fechaInicioArbitrio > fechaInicioContrato &&
				    fechaFinArbitrio > fechaInicioContrato	
				   ){
					resultado = 1;
				}
				//El contrato esta entre el inicio del arbitrio y fin del arcbitrio
				if(fechaInicioArbitrio <= fechaInicioContrato &&
				   fechaFinArbitrio >= fechaInicioContrato){
					resultado = 0;
				}
					
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			fechaInicioArbitrio		= null;
			fechaFinArbitrio		= null;
			fechaInicioContrato		= null;
		}
		return resultado;
	}
	/*
	 * Factor de calculo del arbitrio
	 */
	public Double factorCalculoArbitrio(Integer valor, TblArbitrio arbitrio, TblContrato contrato){
		Double resultado 	= null;
		int totalDias		= 0;
		int totalResta		= 0;
		try{
			if(valor == -1){
				resultado = new Double("0");
			}
			if(valor == 1){
				resultado = new Double("1");
			}
			if(valor == 0){
				resultado = new Double("0");
				logger.debug("[factorCalculoArbitrio] Arbitrio: FecIni: "+arbitrio.getFechaInicio()+" FecFin:"+arbitrio.getFechaFin());
				logger.debug("[factorCalculoArbitrio] Contrato: FecIni: "+contrato.getFechaInicio());
				totalDias = UtilSGT.getDiasFechas(UtilSGT.getDateStringFormat(arbitrio.getFechaInicio()), UtilSGT.getDateStringFormat(arbitrio.getFechaFin()));
				totalResta = UtilSGT.getDiasFechas(UtilSGT.getDateStringFormat(contrato.getFechaInicio()), UtilSGT.getDateStringFormat(arbitrio.getFechaFin()));
				logger.debug("[factorCalculoArbitrio] totalDias: "+totalDias+ " totalResta:"+totalResta);
				resultado = new Double(totalResta*(new Double(1))/totalDias);
				logger.debug("[factorCalculoArbitrio] resultado: "+resultado);
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultado;
	}
	/**
	 * Se encarga de direccionar a la pantalla de edicion del arbitrio
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cliente/arbitrio/anio/{cadena}", method = RequestMethod.GET)
	public String anioAnteriorPosterior(@PathVariable String cadena, Model model,  PageableSG pageable, HttpServletRequest request) {
		Filtro filtro 		= null;
		String operacion	= null;
		String anio			= null;
		List<TblParametro> listaParametro;
		try{
			logger.debug("[anioAnteriorPosterior] Inicio" );
			operacion = cadena.substring(0, 1);
			anio = cadena.substring(1);
			logger.debug("[anioAnteriorPosterior] operacion:"+operacion );
			logger.debug("[anioAnteriorPosterior] anio: "+anio );
			filtro = new Filtro();
			model.addAttribute("filtro", filtro);
			filtro.setCodigoEdificacionFiltro(-1);
			listaParametro = parametroDao.buscarOneByNombre(Constantes.PAR_INMUEBLE);
			if(listaParametro!=null && listaParametro.size()>0){
				filtro.setCodigoEdificacionFiltro(new Integer(listaParametro.get(0).getDato()));
			}
			
			if (operacion.equals(Constantes.PAGINADO_ANTERIOR)){
				filtro.setAnioFiltro(new Integer(anio) - 1);
			}else{
				if(operacion.equals(Constantes.PAGINADO_SIGUIENTE)){
					filtro.setAnioFiltro(new Integer(anio) + 1);
					
				}else{
					filtro.setAnioFiltro(new Integer(1900));
				}
			}
			this.listarArbitrios(model, filtro,pageable, this.urlPaginado,request);
			logger.debug("[anioAnteriorPosterior] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		return "cliente/arbitrio/arb_listado";
	}
	
	/**
	 * Se encarga de Mostrar los locales pendientes de los arbitrios
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "cliente/arbitrio/pendiente", method = RequestMethod.GET)
	public String listarPendienteArbitrio(Model model, HttpServletRequest request) {
		Filtro filtro		 = null;
		List<TblParametro> listaParametro;
		List<TblTienda> listaLocales = null;
		List<TblArbitrio> listaArbitrio = null;
		List<TiendaArbitrioBean> lista	= null;
		try{
			logger.debug("[listarPendienteArbitrio] Inicio");
			filtro = new Filtro();
			model.addAttribute("filtro", filtro);
			filtro.setCodigoEdificacionFiltro(-1);
			filtro.setAnioFiltro(Calendar.getInstance().get(Calendar.YEAR));
			listaParametro = parametroDao.buscarOneByNombre(Constantes.PAR_INMUEBLE);
			if(listaParametro!=null && listaParametro.size()>0){
				filtro.setCodigoEdificacionFiltro(new Integer(listaParametro.get(0).getDato()));
			}
			//this.cargarListaAnio(model);
			listaLocales = tiendaDao.listarAllActivos(filtro.getCodigoEdificacionFiltro());
			listaArbitrio = arbitrioDao.listarCriterios(filtro.getAnioFiltro());
			lista = getListaLocalesPendiente(listaLocales, listaArbitrio, filtro.getAnioFiltro());
			model.addAttribute("registros",lista);
			listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_ASIGNACION);
			request.getSession().setAttribute("listaTiendaxArbitrio", lista);
			logger.debug("[listarPendienteArbitrio] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "cliente/arbitrio/arb_pendiente";
	}
	/*
	 * Obtiene la lista de Locales indicando cuales tiene arbitrio y cuales no
	 */
	private List<TiendaArbitrioBean> getListaLocalesPendiente(List<TblTienda> listaLocales, List<TblArbitrio> listaArbitrio, Integer intAnio){
		List<TiendaArbitrioBean> lista	= null;
		TiendaArbitrioBean tiendaBean	= null;
		Map<String , String> mapArbitrio = null;
		Map<String , TiendaArbitrioBean> mapTienda   = null;
		Iterator<?> iteMap							= null;
		try{
			if(listaLocales!=null){
				lista = new ArrayList<TiendaArbitrioBean>();
				mapTienda = new HashMap<String , TiendaArbitrioBean>();
				for(TblTienda tienda: listaLocales){
					tiendaBean = new TiendaArbitrioBean();
					tiendaBean.setAnio(intAnio);
					tiendaBean.setNombre(tienda.getTblEdificio().getNombre());
					tiendaBean.setNumero(tienda.getNumero());
					tiendaBean.setPiso(tienda.getPiso());
					tiendaBean.setTipo(tienda.getTipo());
					tiendaBean.setArea(tienda.getArea());
					tiendaBean.setEstadoTienda(Constantes.ESTADO_PENDIENTE);
					mapTienda.put(tienda.getNumero(), tiendaBean);
				}
			}
			if(listaArbitrio !=null){
				mapArbitrio = new HashMap<String , String>();
				for(TblArbitrio arbitrio: listaArbitrio){
					mapArbitrio.put(arbitrio.getTblTienda().getNumero(), arbitrio.getTblTienda().getNumero());
				}
				iteMap = mapArbitrio.entrySet().iterator();
				while(iteMap.hasNext()){
					@SuppressWarnings("unchecked")
					Map.Entry<String, String> pair = (Map.Entry<String, String>)iteMap.next();
					tiendaBean = mapTienda.get(pair.getKey());
					if (tiendaBean!=null){
						tiendaBean.setEstadoTienda(Constantes.ESTADO_ASIGNADO);
					}
					
				}
				iteMap = mapTienda.entrySet().iterator();
				while(iteMap.hasNext()){
					@SuppressWarnings("unchecked")
					Map.Entry<String, TiendaArbitrioBean> pair = (Map.Entry<String, TiendaArbitrioBean>)iteMap.next();
					tiendaBean = pair.getValue();
					lista.add(tiendaBean);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return lista;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cliente/arbitrio/pendiente/estado", method = RequestMethod.POST)
	public String listarPendientesxEstado(Model model, Filtro entidad, HttpServletRequest request) {
		List<TiendaArbitrioBean> lista	= null;
		List<TiendaArbitrioBean> listaEstado	= null;
		try{
			logger.debug("[listarPendientesxEstado] Inicio");
			lista = (ArrayList<TiendaArbitrioBean>)request.getSession().getAttribute("listaTiendaxArbitrio");
			listaEstado = new ArrayList<TiendaArbitrioBean>();
			if (entidad.getEstado().equals("-1")){
				listaEstado = lista;
			}else{
				for(TiendaArbitrioBean tiendaBean: lista){
					if (tiendaBean.getEstadoTienda().equals(entidad.getEstado())){
						listaEstado.add(tiendaBean);
					}
				}
			}
			//this.cargarListaAnio(model);
			listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_ASIGNACION);
			model.addAttribute("entidad", entidad);
			model.addAttribute("registros",listaEstado);
			logger.debug("[listarPendientesxEstado] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			lista = null;
			listaEstado = null;
		}
		return  "cliente/arbitrio/arb_pendiente";
	}
	@RequestMapping(value = "/cliente/arbitrios/p", method = RequestMethod.POST)
	public String buscarPendientes(Model model, Filtro filtro, HttpServletRequest request) {
		List<TblTienda> listaLocales = null;
		List<TblArbitrio> listaArbitrio = null;
		List<TiendaArbitrioBean> lista	= null;
		List<TiendaArbitrioBean> listaFiltro	= null;
		try{
			logger.debug("[buscarPendientes] Inicio");
			model.addAttribute("filtro", filtro);
			//this.cargarListaAnio(model);
			listaLocales = tiendaDao.listarAllActivos(filtro.getCodigoEdificacionFiltro());
			listaArbitrio = arbitrioDao.listarCriterios(filtro.getAnioFiltro());
			lista = getListaLocalesPendiente(listaLocales, listaArbitrio, filtro.getAnioFiltro());
			
			if (filtro.getEstado().equals("-1")){
				listaFiltro = lista;
			}else{
				listaFiltro = new ArrayList<TiendaArbitrioBean>();
				for(TiendaArbitrioBean tiendaBean: lista){
					if (tiendaBean.getEstadoTienda().equals(filtro.getEstado())){
						listaFiltro.add(tiendaBean);
					}
				}
			}
			model.addAttribute("registros",listaFiltro);
			listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_ASIGNACION);
			request.getSession().setAttribute("listaTiendaxArbitrio", lista);
			logger.debug("[buscarPendientes] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "cliente/arbitrio/arb_pendiente";
	}

	/*
	 * Paginado
	 */
	@RequestMapping(value = "/cliente/arbitrios/paginado/{page}/{size}/{operacion}", method = RequestMethod.GET)
	public String paginarEntidad(@PathVariable Integer page, @PathVariable Integer size, @PathVariable String operacion, Model model,  PageableSG pageable, HttpServletRequest request) {
		Filtro filtro = null;
		String path = null;
		try{
			//LOGGER.debug("[traerRegistros] Inicio");
			path = "cliente/arbitrio/arb_listado";
			if (pageable!=null){
				if (pageable.getLimit() == 0){
					pageable.setLimit(size);
				}
				if (pageable.getOffset()== 0){
					pageable.setOffset(page*size);
				}
				if (pageable.getOperacion() ==null || pageable.getOperacion().equals("")){
					pageable.setOperacion(operacion);
				}
				
			}
			filtro = (Filtro)request.getSession().getAttribute("sessionFiltroCriterioArbitrio");
			model.addAttribute("filtro", filtro);
			
			this.listarArbitrios(model, filtro, pageable, this.urlPaginado,request);
			
		}catch(Exception e){
			//LOGGER.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		return path;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cliente/arbitrios/regresar", method = RequestMethod.GET)
	public String regresar(Model model, String path, HttpServletRequest request) {
		Filtro filtro = null;
		List<TblContrato> lista = null;
		PageWrapper<TblContrato> page = null;
		try{
			logger.debug("[regresar] Inicio");
			path = "cliente/arbitrio/arb_listado";
			
			filtro = (Filtro)request.getSession().getAttribute("sessionFiltroCriterioArbitrio");
			model.addAttribute("filtro", filtro);
			lista = (List<TblContrato>)request.getSession().getAttribute("sessionListaArbitrio");
			model.addAttribute("registros",lista);
			page = (PageWrapper<TblContrato>) request.getSession().getAttribute("PageArbitrio");
			model.addAttribute("page", page);
						
			logger.debug("[regresar] Fin");
		}catch(Exception e){
			logger.debug("[regresar] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		
		return path;
	}
	
	private void calculoAsignacionFechas() throws ParseException {
		List<TblArbitrio> listArbitrio = arbitrioDao.listarAllActivosOrderByCodigo();
		Map<String, List<TblArbitrio>> mapArbitrio = new HashMap<String, List<TblArbitrio>>();
		//Creamos un Map con los arbitrios por Contrato-Año : Listado de Arbitrios
		for(TblArbitrio arb: listArbitrio) {
			String key = new Integer(arb.getCodigoContrato()).toString()+":" +arb.getAnio().toString();
			List<TblArbitrio> subListArbitrio = mapArbitrio.get(key);
			if (subListArbitrio == null) {
				subListArbitrio = new ArrayList<TblArbitrio>();
				subListArbitrio.add(arb);
				mapArbitrio.put(key, subListArbitrio);
			}else {
				subListArbitrio.add(arb);
			}
		}
		Map<String, String> mapPeriodo = UtilSGT.getMapFechaPeriodo();
		//Recorremos el Map para asignar la fecha
		for(String key : mapArbitrio.keySet()) {
			List<TblArbitrio> subListArbitrio = mapArbitrio.get(key);
			for(int i=0; i<subListArbitrio.size(); i++) {
				TblArbitrio arb = subListArbitrio.get(i);
				arb.setFechaFin(UtilSGT.getFechaFinPeriodo(subListArbitrio.size(),arb.getAnio(),i+1,arb.getTrimestre(),mapPeriodo));
				logger.debug("[calculoAsignacionFechas] codigo:"+arb.getCodigoArbitrio()+ " anio:"+ arb.getAnio()+" periodo:"+arb.getTrimestre()+" i:"+i + " Fecha:"+arb.getFechaFin());
				arbitrioDao.save(arb);
			}
		}
	}
	
	
	/**
	 * Se encarga de buscar la informacion del Concepto segun el filtro
	 * seleccionado
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cliente/arbitrios/fecha", method = RequestMethod.POST)
	public String asignarFechasArbitrios(Model model, String path,  PageableSG pageable,HttpServletRequest request) {
		Filtro filtro = null;
		List<TblParametro> listaParametro;
		try{
			logger.debug("[asignarFechasArbitrios] Inicio");
			
			calculoAsignacionFechas();
			
			path = "cliente/arbitrio/arb_listado";
			filtro = new Filtro();
			model.addAttribute("filtro", filtro);
			filtro.setCodigoEdificacionFiltro(-1);
			filtro.setAnioFiltro(Calendar.getInstance().get(Calendar.YEAR));
			listaParametro = parametroDao.buscarOneByNombre(Constantes.PAR_INMUEBLE);
			if(listaParametro!=null && listaParametro.size()>0){
				filtro.setCodigoEdificacionFiltro(new Integer(listaParametro.get(0).getDato()));
			}
			this.cargarListaAnio(model,request);
			this.listarArbitrios(model, filtro, pageable, this.urlPaginado,request);
			request.getSession().setAttribute("sessionFiltroCriterioArbitrio", filtro);
			logger.debug("[asignarFechasArbitrios] Fin");
		}catch(Exception e){
			logger.debug("[asignarFechasArbitrios] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}

		return path;
	}
}
