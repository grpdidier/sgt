package com.pe.lima.sg.presentacion.cliente;

import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conEdificio;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conEstado;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conEstadoContrato;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conMaterno;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conNombre;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conPaterno;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conRazonSocial;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conRuc;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conTienda;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.dao.cliente.IArbitrioDAO;
import com.pe.lima.sg.dao.cliente.IContratoClienteDAO;
import com.pe.lima.sg.dao.cliente.IContratoDAO;
import com.pe.lima.sg.dao.cliente.IContratoPrimerCobroDAO;
import com.pe.lima.sg.dao.cliente.IContratoServicioDAO;
import com.pe.lima.sg.dao.cliente.ILuzxTiendaDAO;
import com.pe.lima.sg.dao.cliente.IObservacionDAO;
import com.pe.lima.sg.dao.mantenimiento.IPersonaDAO;
import com.pe.lima.sg.dao.mantenimiento.ITipoServicioDAO;
import com.pe.lima.sg.entity.cliente.TblArbitrio;
import com.pe.lima.sg.entity.cliente.TblContrato;
import com.pe.lima.sg.entity.cliente.TblContratoCliente;
import com.pe.lima.sg.entity.cliente.TblContratoPrimerCobro;
import com.pe.lima.sg.entity.cliente.TblContratoServicio;
import com.pe.lima.sg.entity.cliente.TblLuzxtienda;
import com.pe.lima.sg.entity.cliente.TblObservacion;
import com.pe.lima.sg.entity.mantenimiento.TblEdificio;
import com.pe.lima.sg.entity.mantenimiento.TblPersona;
import com.pe.lima.sg.entity.mantenimiento.TblTienda;
import com.pe.lima.sg.entity.mantenimiento.TblTipoServicio;
import com.pe.lima.sg.presentacion.BaseOperacionPresentacion;
import com.pe.lima.sg.presentacion.BeanRequest;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.ListaUtilAction;
import com.pe.lima.sg.presentacion.util.UtilSGT;

/**
 * Clase Bean que se encarga de la administracion de los contratos para la aprobacion
 *
 * 			
 */
@Controller
public class AprobacionAction extends BaseOperacionPresentacion<TblContrato> {
	private static final Logger logger = LogManager.getLogger(AprobacionAction.class);
	@Autowired
	private IContratoDAO contratoDao;

	/*@Autowired
	private ITiendaDAO tiendaDao;*/

	/*@Autowired
	private ISuministroDAO suministroDao;*/

	/*@Autowired
	private IPersonaDAO personaDao;*/
	
	@Autowired
	private ITipoServicioDAO tipoServicioDao;
	
	
	@Autowired
	private IPersonaDAO personaDao;

	@Autowired
	private IArbitrioDAO arbitrioDao;
	
	@Autowired
	private ILuzxTiendaDAO luzxTiendaDao;	

	@Autowired
	private IObservacionDAO observacionDao;

	@Autowired
	private IContratoClienteDAO contratoClienteDao;

	@Autowired
	private IContratoServicioDAO contratoServicioDao;

	@Autowired
	private IContratoPrimerCobroDAO contratoPrimerCobroDao;

	@Autowired
	private ListaUtilAction listaUtil;

	/*@Autowired
	private ITipoServicioDAO tipoServicioDao;*/

	@SuppressWarnings("rawtypes")
	@Override
	public BaseOperacionDAO getDao() {
		return contratoDao;
	}

	/**
	 * Se encarga de listar todos los contratos
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/aprobacion", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path) {
		TblContrato filtro = null;
		try{
			logger.debug("[traerRegistros] Inicio");
			path = "cliente/aprobacion/apr_listado";
			filtro = new TblContrato();
			filtro.setTblPersona(new TblPersona());
			filtro.setTblTienda(new TblTienda());
			filtro.getTblTienda().setTblEdificio(new TblEdificio());
			model.addAttribute("filtro", filtro);
			listaUtil.cargarDatosModel(model, Constantes.MAP_LISTA_EDIFICIO);
			this.listarContratos(model, filtro);
			logger.debug("[traerRegistros] Fin");
		}catch(Exception e){
			logger.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}

		return path;
	}
	/**
	 * Se encarga de buscar la informacion del Contrato Pendientes segun el filtro
	 * seleccionado
	 * 
	 * @param model
	 * @param contratoBean
	 * @return
	 */
	@RequestMapping(value = "/aprobacion/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, TblContrato filtro, String path,HttpServletRequest request) {
		//Map<String, Object> campos = null;
		path = "cliente/aprobacion/apr_listado";
		try{
			logger.debug("[traerRegistrosFiltrados] Inicio");
			this.listarContratos(model, filtro);
			model.addAttribute("filtro", filtro);
			//listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_CONCEPTO);
			logger.debug("[traerRegistrosFiltrados] Fin");
		}catch(Exception e){
			logger.debug("[traerRegistrosFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			//campos		= null;
		}
		logger.debug("[traerRegistrosFiltrados] Fin");
		return path;
	}

	/*** Listado de Contratos ***/
	private void listarContratos(Model model, TblContrato tblContrato){
		List<TblContrato> entidades = new ArrayList<TblContrato>();
		try{
			Specification<TblContrato> filtro = Specifications.where(conNombre(tblContrato.getTblPersona().getNombre()))
					.and(conPaterno(tblContrato.getTblPersona().getPaterno()))
					.and(conMaterno(tblContrato.getTblPersona().getMaterno()))
					.and(conEdificio(tblContrato.getTblTienda().getTblEdificio().getCodigoEdificio()))
					.and(conTienda(tblContrato.getTblTienda().getNumero()))
					.and(conRuc(tblContrato.getTblPersona().getNumeroRuc()))
					.and(conRazonSocial(tblContrato.getTblPersona().getRazonSocial()))
					.and(conEstadoContrato(Constantes.ESTADO_CONTRATO_PENDIENTE))  
					.and(conEstado(Constantes.ESTADO_REGISTRO_ACTIVO));
			entidades = contratoDao.findAll(filtro);
			logger.debug("[listarContratos] entidades:"+entidades);
			model.addAttribute("registros", entidades);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades = null;
		}
	}
	
	/*Busqueda de contratos antiguos*/
	
	private List<TblContrato> getContratosAntiguos(TblContrato contrato){
		List<TblContrato> listaContrato = null;
		TblContrato contratoAnt			= null;
		boolean encontrado 				= true;
		Integer codigoContrato			= null;
		try{
			if(contrato.getCodigoPadreContrato()!=null && contrato.getCodigoPadreContrato() > 0){
				codigoContrato = contrato.getCodigoPadreContrato();
				do{
					contratoAnt = contratoDao.findOne(codigoContrato);
					if (contratoAnt !=null){
						if (listaContrato==null){
							listaContrato = new ArrayList<TblContrato>();
							listaContrato.add(contratoAnt);
						}else{
							listaContrato.add(contratoAnt);
						}
						//verificacion de mas contratos
						if (contratoAnt.getCodigoPadreContrato()!=null && contratoAnt.getCodigoPadreContrato() > 0){
							encontrado = true;
						}else{
							encontrado = false;
						}
					}else{
						encontrado = false;
					}
					
					
				}while(encontrado);
			}
		}catch(Exception e){
			
		}
		return listaContrato;
		
	}
	/**
	 * Se encarga de direccionar a la pantalla de edicion del Contrato
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "aprobacion/editar/{id}", method = RequestMethod.GET)
	public String editarContrato(@PathVariable Integer id, Model model, TblContrato filtro, HttpServletRequest request) {
		TblContrato contrato 					= null;
		BeanRequest beanRequest					= null;
		List<TblContratoServicio> listaServicio	= null;
		List<TblContratoCliente> listaCliente	= null;
		List<TblContratoPrimerCobro> listaCobro	= null;
		List<TblArbitrio> listaArbitrio			= null;
		List<TblLuzxtienda> listaLuzxtienda		= null;
		List<TblContrato> listaContratoAnt		= null;
		List<TblObservacion> listaObservacion	= null;
		String path 							= "";
		try{
			contrato = contratoDao.findOne(id);
			model.addAttribute("contrato", contrato);
			listaServicio = contratoServicioDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			listaCliente = contratoClienteDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			listaCobro = contratoPrimerCobroDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			listaArbitrio = arbitrioDao.listarAllActivosxContrato(contrato.getCodigoContrato());
			listaLuzxtienda = luzxTiendaDao.listarLuzTiendaxContrato(contrato.getCodigoContrato());
			listaObservacion = observacionDao.listarObservacionxContrato(contrato.getCodigoContrato());
			//Busqueda de los contratos Antiguos
			listaContratoAnt = getContratosAntiguos(contrato);
			
			beanRequest = new BeanRequest();
			beanRequest.setContrato(contrato);
			beanRequest.setContratoAntiguo(contrato);
			beanRequest.setContratoServicio(new TblContratoServicio());
			beanRequest.setListaServicio(listaServicio);
			beanRequest.setListaServicioAntiguo(listaServicio);
			beanRequest.setListaCliente(listaCliente);
			beanRequest.setContratoPrimerCobro(new TblContratoPrimerCobro());
			beanRequest.setListaPrimerCobro(listaCobro);
			beanRequest.setArbitrio(new TblArbitrio());
			beanRequest.setListaArbitrio(listaArbitrio);
			beanRequest.setListaLuzxTienda(listaLuzxtienda);
			beanRequest.setListaContrato(listaContratoAnt);
			beanRequest.setListaObservacion(listaObservacion);
			beanRequest.setObservacion(new TblObservacion());
			
			this.cargarListasRequestBeanContrato(model, beanRequest);
			
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);
			path = "cliente/aprobacion/apr_edicion";


		}catch(Exception e){
			e.printStackTrace();
		}finally{
			contrato 			= null;
			beanRequest			= null;
			listaServicio		= null;
			listaCliente		= null;
			listaCobro			= null;
			listaArbitrio		= null;
		}
		return path;
	}

	@Override
	public TblContrato getNuevaEntidad() {
		
		return null;
	}


	


	public void preEditarTienda(TblTienda entidad, HttpServletRequest request) {
		try{
			logger.debug("[preEditarTienda] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			logger.debug("[preEditarTienda] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	/**
	 * Se encarga de guardar la informacion 
	 * 
	 * @param 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/aprobacion/editar/guardar", method = RequestMethod.POST)
	public String actualizarEntidad(Model model, TblContrato entidad, HttpServletRequest request, String path) {
		path 											= "cliente/aprobacion/apr_listado";
		BeanRequest beanRequest							= null;
		TblContrato contrato							= null;
		TblContratoCliente cliente						= null;
		//List<TblContratoPrimerCobro> listaCobro			= null;
		TblContrato filtro								= null;
		try{
			logger.debug("[actualizarEntidad] Inicio" );
				logger.debug("[actualizarEntidad] Pre Guardar..." );
				beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
				beanRequest.setContrato(entidad);
				if (this.validarNegocio(model, entidad, request)){
					contrato = contratoDao.findOne(entidad.getCodigoContrato());
					beanRequest.setContrato(entidad);
					beanRequest.setContratoAntiguo(contrato);
					//TODO:(1) Se comenta la generación del primer cobro: se validara el recalculo
					//this.generarPrimerosCobros(request);
					this.preEditar(entidad, request);
					
					contrato.setFechaContrato(entidad.getFechaContrato());
					contrato.setFechaInicio(entidad.getFechaInicio());
					contrato.setFechaFin(entidad.getFechaFin());
					contrato.setMontoAlquiler(entidad.getMontoAlquiler());
					contrato.setTipoMonedaAlquiler(entidad.getTipoMonedaAlquiler());
					contrato.setTipoCobro(entidad.getTipoCobro());
					contrato.setTipoGarantia(entidad.getTipoGarantia());
					contrato.setMontoGarantia(entidad.getMontoGarantia());
					contrato.setTipoMonedaGarantia(entidad.getTipoMonedaGarantia());
					contrato.setTipoDocumentoGarantia(entidad.getTipoDocumentoGarantia());
					contrato.setInformacionAdicional(entidad.getInformacionAdicional());
					contrato.setPeriodoAdelanto(entidad.getPeriodoAdelanto());
					contrato.setTipoDocumentoAlquiler(entidad.getTipoDocumentoAlquiler());
					contrato.setEstadoContrato(Constantes.ESTADO_CONTRATO_VIGENTE);
					contrato.setFechaModificacion(entidad.getFechaModificacion());
					contrato.setUsuarioModificacion(entidad.getUsuarioModificacion());
					contrato.setIpModificacion(entidad.getIpModificacion());
	
	
					if (contrato.getTblPersona().getCodigoPersona() != entidad.getTblPersona().getCodigoPersona()){
						contrato.setTblPersona(entidad.getTblPersona());
	
						//ContratoCliente
						cliente = new TblContratoCliente();
						cliente.setTblContrato(contrato);
						cliente.setCodigoPersona(entidad.getTblPersona().getCodigoPersona());
						cliente.setFechaCreacion(new Date(System.currentTimeMillis()));
						cliente.setIpCreacion(request.getRemoteAddr());
						cliente.setUsuarioCreacion(contrato.getUsuarioModificacion());
						cliente.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
						cliente.setFechaInicio(entidad.getFechaInicio());
						cliente.setFechaFin(entidad.getFechaFin());
						contratoClienteDao.save(cliente);
					}
					contrato.setTblAdelantos(entidad.getTblAdelantos());
					//TODO:(1.1) Se comenta hasta validar el punto (1)
					/*if(beanRequest.getListaPrimerCobroNuevo().size()>0){
						logger.debug("[actualizarEntidad] Hubo cambios en la lista de primeros cobros, borrando..." );
						contrato.setTblContratoPrimerCobros(new HashSet<TblContratoPrimerCobro>(0));
					}else{
						contrato.setTblContratoPrimerCobros(entidad.getTblContratoPrimerCobros());
					}*/
					contrato.setTblContratoPrimerCobros(entidad.getTblContratoPrimerCobros());
					
					contrato.setTblContratoServicios(entidad.getTblContratoServicios());
					//contrato.setTblCxcDocumentos(entidad.getTblCxcDocumentos());
					contrato.setTblTienda(entidad.getTblTienda());
	
					//super.editar(contrato, model);
					contratoDao.save(contrato);
					model.addAttribute("respuesta", "El contrato ["+contrato.getNumero()+"] fue aprobado. Su estado cambio a estado ["+Constantes.DESC_ESTADO_CONTRATO_VIGENTE+"]");
					logger.debug("[actualizarEntidad] Guardado..." );
					
					//registrando los primeros cobros
					//TODO:(1.2.) Se comenta hasta validarlo con Jhonny
					/*
					listaCobro = beanRequest.getListaPrimerCobro();
					if (listaCobro!=null && listaCobro.size()>0){
						logger.debug("[guardarEntidad] listaCobro:"+listaCobro.size());
						for(TblContratoPrimerCobro cobro: listaCobro){
							cobro.setTblContrato(contrato);
							contratoPrimerCobroDao.save(cobro);
						}
					}
					listaCobro = beanRequest.getListaPrimerCobroNuevo();
					if (listaCobro!=null && listaCobro.size()>0){
						logger.debug("[guardarEntidad] listaCobro:"+listaCobro.size());
						for(TblContratoPrimerCobro cobro: listaCobro){
							cobro.setTblContrato(contrato);
							contratoPrimerCobroDao.save(cobro);
						}
					}
					*/
					//registro de las observaciones
					if (beanRequest.getListaObservacion()!=null && beanRequest.getListaObservacion().size()>0){
						for(TblObservacion observacion:beanRequest.getListaObservacion()){
							observacion.setCodigoContrato(contrato.getCodigoContrato());
							observacionDao.save(observacion);
						}
					}
	
					//registrando los servicios
					if (beanRequest.getListaServicio()!=null && beanRequest.getListaServicio().size()>0){
						logger.debug("[guardarEntidad] listaServicio:"+beanRequest.getListaServicio().size());
						for(TblContratoServicio servicio: beanRequest.getListaServicio()){
							servicio.setTblContrato(contrato);
							contratoServicioDao.save(servicio);
						}
					}
					if (beanRequest.getListaServicioAntiguo()!=null && beanRequest.getListaServicioAntiguo().size()>0){
						logger.debug("[guardarEntidad] listaServicio:"+beanRequest.getListaServicioAntiguo().size());
						for(TblContratoServicio servicio: beanRequest.getListaServicioAntiguo()){
							servicio.setTblContrato(contrato);
							contratoServicioDao.save(servicio);
						}
					}
					//Asociacion de Arbitrios
					this.mAsignarArbitrios(contrato, request);
					//Lista los contratos pendientes
					filtro = new TblContrato();
					filtro.setTblPersona(new TblPersona());
					filtro.setTblTienda(new TblTienda());
					filtro.getTblTienda().setTblEdificio(new TblEdificio());
					model.addAttribute("filtro", filtro);
					listaUtil.cargarDatosModel(model, Constantes.MAP_LISTA_EDIFICIO);
					this.listarContratos(model, filtro);
				}else{
					path = "cliente/aprobacion/apr_edicion";
					beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
					beanRequest.setContrato(entidad);	
					this.cargarListasRequestBeanContrato(model, beanRequest);
					
					this.cargarListaOperacionContrato(model);

					request.getSession().setAttribute("beanRequest", beanRequest);
				}
				
			logger.debug("[actualizarEntidad] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//beanRequest		= null;
			contrato		= null;
			cliente			= null;
			filtro			= null;
		}
		return path;

	}
	/*
	 * Valida y asigna arbitrios
	 */
	public void mAsignarArbitrios(TblContrato contrato, HttpServletRequest request){
		Integer anio 					= null;
		Integer codigoTienda 			= null;
		List<TblArbitrio> listaArbitrio = null;
		try{
			anio = UtilSGT.getAnioDate(contrato.getFechaInicio());
			codigoTienda = contrato.getTblTienda().getCodigoTienda();
			listaArbitrio = arbitrioDao.listarAllActivosxTiendaxAnio(codigoTienda, anio);
			if(listaArbitrio!=null){
				for(TblArbitrio arbitrio: listaArbitrio){
					arbitrio.setMontoContrato(arbitrio.getMontoGenerado());
					arbitrio.setSaldo(arbitrio.getMontoContrato());
					arbitrio.setCodigoContrato(contrato.getCodigoContrato());
					this.preGuardarArbitrio(arbitrio, request);
					arbitrioDao.save(arbitrio);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			anio 			= null;
			codigoTienda 	= null;
			listaArbitrio 	= null;
		}
	}
	public void preGuardarArbitrio(TblArbitrio entidad, HttpServletRequest request) {
		try{
			logger.debug("[preGuardarArbitrio] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			
			logger.debug("[preGuardarArbitrio] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public void preEditar(TblContrato entidad, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		List<TblObservacion> listaObs		= null;
		try{
			logger.debug("[preEditar] Inicio" );
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			//primeros cobros
			if(beanRequest.getListaPrimerCobroNuevo()!=null && beanRequest.getListaPrimerCobroNuevo().size()>0){
				if(beanRequest.getListaPrimerCobro()!=null){
					for(TblContratoPrimerCobro primer: beanRequest.getListaPrimerCobro()){
						primer.setFechaModificacion(new Date(System.currentTimeMillis()));
						primer.setIpModificacion(request.getRemoteAddr());
						primer.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
						primer.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
					
					}
				}
				if(beanRequest.getListaPrimerCobroNuevo()!=null){
					for(TblContratoPrimerCobro primer: beanRequest.getListaPrimerCobroNuevo()){
						primer.setFechaCreacion(new Date(System.currentTimeMillis()));
						primer.setIpCreacion(request.getRemoteAddr());
						primer.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
						primer.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
					
					}
				}
				
			}
			//servicios
			
				if(beanRequest.getListaServicioAntiguo()!=null){
					for(TblContratoServicio objeto: beanRequest.getListaServicioAntiguo()){
						objeto.setFechaModificacion(new Date(System.currentTimeMillis()));
						objeto.setIpModificacion(request.getRemoteAddr());
						objeto.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
						objeto.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
					
					}
				}
				if(beanRequest.getListaServicio()!=null){
					for(TblContratoServicio objeto: beanRequest.getListaServicio()){
						objeto.setFechaCreacion(new Date(System.currentTimeMillis()));
						objeto.setIpCreacion(request.getRemoteAddr());
						objeto.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
						objeto.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
					
					}
				}
				//Para lista de observaciones
				listaObs = beanRequest.getListaObservacion();
				for(TblObservacion pc:listaObs){
					pc.setFechaCreacion(new Date(System.currentTimeMillis()));
					pc.setIpCreacion(request.getRemoteAddr());
					pc.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
					pc.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
				}
			

			logger.debug("[preEditar] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			beanRequest = null;
			listaObs 	= null;
		}
	}
	/*
	 * Carga las listas en la sesion para las operaciones del contrato
	 */
	
	private void cargarListaOperacionCliente(Model model){
		
		listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PERSONA);
		listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_CIVIL);
		listaUtil.cargarDatosModel(model, Constantes.MAP_SI_NO);
	}
	/**
	 * Se encarga de Listar los clientes
	 * seleccionado
	 * 
	 * @param model
	 * @param contratoBean
	 * @return
	 */
	@RequestMapping(value = "/aprobacion/clientes", method = RequestMethod.POST)
	public String listarClientes(Model model, TblContrato contrato, String path, HttpServletRequest request) {
		path 						= "cliente/aprobacion/apr_per_listado";
		Filtro filtro 				= null;
		BeanRequest beanRequest		= null;
		try{
			logger.debug("[listarClientes] Inicio");
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			
			filtro = new Filtro();
			filtro.setStrOperacion(Constantes.OPERACION_EDITAR);
			model.addAttribute("contrato", beanRequest.getContrato());
			
			model.addAttribute("filtro", filtro);
			this.listarPersonas(model, filtro);
			this.cargarListaOperacionCliente(model);

			//Se mantiene en Session el contrato hasta retornar
			request.getSession().setAttribute("beanRequest", beanRequest);

		}catch(Exception e){
			logger.debug("[traerRegistrosFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			filtro		= null;
		}
		logger.debug("[listarClientes] Fin");
		return path;
	}
	
	/*** Listado de Personas ***/
	private void listarPersonas(Model model, Filtro filtro){
		List<TblPersona> entidades = new ArrayList<TblPersona>();
		try{
			entidades = personaDao.listarCriterios(filtro.getNombre(), filtro.getPaterno(), filtro.getMaterno(), filtro.getDni(), filtro.getRuc(), filtro.getRazonSocial());
			logger.debug("[listarPersonaes] entidades:"+entidades);
			model.addAttribute("registros", entidades);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades = null;
		}
	}
	/**
	 * Se encarga de regresar a la pantalla de aprobacion (edicion)
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/aprobacion/regresar", method = RequestMethod.POST)
	public String regresarAprobacion(Model model, Filtro filtro, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		try{
			logger.debug("[regresarAprobacion] Inicio");
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			logger.debug("[regresarAprobacion] oPERACION:"+filtro.getStrOperacion());
			
			path = "cliente/aprobacion/apr_edicion";
			//contrato = beanRequest.getContrato();
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);
			logger.debug("[regresarAprobacion] Fin");
		}catch(Exception e){
			logger.debug("[regresarAprobacion] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}

		return path;
	}
	/*
	 * Carga las listas en la sesion para las operaciones del contrato
	 */
	
	private void cargarListaOperacionContrato(Model model){
		
		listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_MONEDA);
		listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_COBRO);
		listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_GARANTIA);
		listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_DOCUMENTO);
		listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PERIODO_ADELANTO);
	}
	/*
	 * Carga en session las listas y beans necesarios para la operacion del contrato
	 */
	private void cargarListasRequestBeanContrato(Model model, BeanRequest beanRequest){
		model.addAttribute("contrato", beanRequest.getContrato());
		model.addAttribute("contratoServicio", beanRequest.getContratoServicio());
		model.addAttribute("listaServicio", beanRequest.getListaServicio());
		model.addAttribute("contratoPrimerCobro", beanRequest.getContratoPrimerCobro());
		model.addAttribute("listaPrimerosCobros", beanRequest.getListaPrimerCobro());
		model.addAttribute("arbitrio", beanRequest.getArbitrio());
		model.addAttribute("listaArbitrios", beanRequest.getListaArbitrio());
		model.addAttribute("listaLuzxTienda", beanRequest.getListaLuzxTienda());
		model.addAttribute("luzxTienda", beanRequest.getLuzxTienda());
		model.addAttribute("observacion", beanRequest.getObservacion());
		model.addAttribute("listaObservacion", beanRequest.getListaObservacion());
	}
	/**
	 * Se encarga de buscar la informacion de los clientes segun el filtro
	 * seleccionado
	 * 
	 * @param model
	 * @param tiendaBean
	 * @return
	 */
	@RequestMapping(value = "/aprobacion/clientes/q", method = RequestMethod.POST)
	public String listarPersonasFiltrados(Model model, Filtro filtro, String path, HttpServletRequest request) {
		try{
			logger.debug("[listarPersonasFiltrados] Inicio - path:"+path);
			logger.debug("[listarPersonasFiltrados] Operacion:"+filtro.getStrOperacion());
			path = "cliente/aprobacion/apr_per_listado";
			logger.debug("[listarPersonasFiltrados] Operacion:"+filtro.getStrOperacion());
			this.listarPersonas(model, filtro);
			this.cargarListaOperacionCliente(model);
			model.addAttribute("filtro", filtro);
		}catch(Exception e){
			logger.debug("[listarPersonasFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
		}
		logger.debug("[listarPersonasFiltrados] Fin");
		return path;
	}
	/**
	 * Se encarga de direccionar a la pantalla de contrato
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/aprobacion/clientes/seleccionar/{id}", method = RequestMethod.GET)
	public String asignarClienteGet(@PathVariable Integer id, Model model, HttpServletRequest request) {
		TblPersona entidad 					= null;
		TblContrato contrato				= null;
		BeanRequest beanRequest				= null;
		String path							= null;
		try{
			entidad = personaDao.findOne(id);
			//Se mantiene en Session el contrato hasta retornar
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			contrato = beanRequest.getContrato();
			contrato.setTblPersona(entidad);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);
			
			path = "cliente/aprobacion/apr_edicion";
			

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return path;
	}
	/**
	 * Se encarga de Listar los clientes
	 * seleccionado
	 * 
	 * @param model
	 * @param contratoBean
	 * @return
	 */
	@RequestMapping(value = "/aprobacion/cliente/historial", method = RequestMethod.POST)
	public String listarClientesHistorial(Model model, TblContrato contrato, String path, HttpServletRequest request) {
		path 						= "cliente/aprobacion/apr_per_historial";
		List<TblPersona> listaHistorial = null;
		Filtro filtro 					= null;
		try{
			logger.debug("[listarClientesHistorial] Inicio");
			
			listaHistorial = personaDao.listarPersonasxContrato(contrato.getCodigoContrato());
					
			model.addAttribute("registros", listaHistorial);
			filtro = new Filtro();
			
			filtro.setStrOperacion(Constantes.OPERACION_EDITAR);
			
			model.addAttribute("filtro", filtro);

		}catch(Exception e){
			logger.debug("[listarClientesHistorial] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			listaHistorial		= null;
		}
		logger.debug("[listarClientesHistorial] Fin");
		return path;
	}
	/**
	 * Se encarga de adicionar los primeros cobros
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/aprobacion/observacion", method = RequestMethod.POST)
	public String adicionarObservacion(Model model, TblObservacion observacionBean, TblContrato contrato, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		try{
			logger.debug("[adicionarObservacion] Inicio:");
			path = "cliente/aprobacion/apr_edicion";
			if (observacionBean.getAsunto()== null || observacionBean.getAsunto().equals("")){
				model.addAttribute("resultadoObservacion", "Debe ingresar el asunto de la Observacion");
				return path;
			}
			if (observacionBean.getBreveDescripcion()== null || observacionBean.getBreveDescripcion().equals("")){
				model.addAttribute("resultadoObservacion", "Debe ingresar una descripcion breve");
				return path;
			}
			if (observacionBean.getDescripcion()== null || observacionBean.getDescripcion().equals("")){
				model.addAttribute("resultadoObservacion", "Debe ingresar una descripcion");
				return path;
			}
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			if (beanRequest.getListaObservacion() == null) {
				beanRequest.setListaObservacion(new ArrayList<TblObservacion>());
				beanRequest.getListaObservacion().add(observacionBean);
			}else{
				beanRequest.getListaObservacion().add(observacionBean);
			}
			beanRequest.setContrato(contrato);
			beanRequest.setObservacion(new TblObservacion());
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);
			logger.debug("[adicionarObservacion] Fin");
		}catch(Exception e){
			logger.debug("[adicionarObservacion] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			beanRequest = null;
		}

		return path;
	}
	/** Validamos logica del negocio**/
	@Override
	public boolean validarNegocio(Model model,TblContrato entidad, HttpServletRequest request){
		boolean exitoso = true;
		//Integer total 	= null;
		try{
			//Validando Local
			if (entidad.getTblTienda() == null || entidad.getTblTienda().getCodigoTienda()<=0){
				exitoso = false;
				model.addAttribute("resultadoContrato", "Debe seleccionar el Local antes de Registrar");
				return exitoso;
			}
			//Validando Cliente
			if (entidad.getTblPersona() == null || entidad.getTblPersona().getCodigoPersona()<=0){
				exitoso = false;
				model.addAttribute("resultadoContrato", "Debe seleccionar el Cliente antes de Registrar");
				return exitoso;
			}
			//Datos
			if (entidad.getFechaContrato() == null){
				exitoso = false;
				model.addAttribute("resultadoContrato", "Debe seleccionar la Fecha de Contrato antes de Registrar");
				return exitoso;
			}
			if (entidad.getFechaInicio() == null){
				exitoso = false;
				model.addAttribute("resultadoContrato", "Debe seleccionar la Fecha de Inicio de Contrato antes de Registrar");
				return exitoso;
			}
			if (entidad.getFechaFin() == null){
				exitoso = false;
				model.addAttribute("resultadoContrato", "Debe seleccionar la Fecha Fin de Contrato antes de Registrar");
				return exitoso;
			}
			if (entidad.getMontoAlquiler() == null || entidad.getMontoAlquiler().compareTo(new BigDecimal(0))< 0){
				exitoso = false;
				model.addAttribute("resultadoContrato", "Debe ingresar el Monto del Alquiler antes de Registrar");
				return exitoso;
			}
			if (entidad.getTipoMonedaAlquiler() == null || entidad.getTipoMonedaAlquiler().equals("-1")){
				exitoso = false;
				model.addAttribute("resultadoContrato", "Debe seleccionar el Tipo de Moneda para el Alquiler antes de Registrar");
				return exitoso;
			}
			if (entidad.getTipoCobro() == null || entidad.getTipoCobro().equals("-1")){
				exitoso = false;
				model.addAttribute("resultadoContrato", "Debe seleccionar el Tipo de Cobro para el Alquiler antes de Registrar");
				return exitoso;
			}
			if (entidad.getTipoGarantia() == null || entidad.getTipoGarantia().equals("-1")){
				exitoso = false;
				model.addAttribute("resultadoContrato", "Debe seleccionar la Garantia antes de Registrar");
				return exitoso;
			}
			if (entidad.getTipoGarantia().equals(Constantes.DESC_TIPO_GARANTIA_CON) || entidad.getTipoGarantia().equals(Constantes.DESC_TIPO_GARANTIA_LLAVE)){
				if (entidad.getMontoGarantia() == null || entidad.getMontoGarantia().compareTo(new BigDecimal(0))<= 0){
					exitoso = false;
					model.addAttribute("resultadoContrato", "Debe ingresar el monto de la Garantia/Llave antes de Registrar");
					return exitoso;
				}
				if (entidad.getTipoMonedaGarantia() == null || entidad.getTipoMonedaGarantia().equals("-1")){
					exitoso = false;
					model.addAttribute("resultadoContrato", "Debe seleccionar el Tipo de Moneda de la Garantia/Llave antes de Registrar");
					return exitoso;
				}
				if (entidad.getTipoDocumentoGarantia() == null || entidad.getTipoDocumentoGarantia().equals("-1")){
					exitoso = false;
					model.addAttribute("resultadoContrato", "Debe seleccionar el Tipo de Documento de la Garantia/Llave antes de Registrar");
					return exitoso;
				}
			}

		}catch(Exception e){
			exitoso = false;
		}finally{
			//	total = null;
		}
		return exitoso;
	}
	/*
	 * Genera los primeros cobros del contrato
	 */
	public void generarPrimerosCobros(HttpServletRequest request){
		TblContratoPrimerCobro primerCobro 	= new TblContratoPrimerCobro();
		//Date datFechaFinContrato			= null;
		Date datFechaInicioContrato			= null;
		Double dblFactor					= null;
		BeanRequest beanRequest				= null;
		List<TblContratoPrimerCobro> lista	= new ArrayList<TblContratoPrimerCobro>();
		Date fechaFinMes					= null;
		try{
			logger.debug("[generarPrimerosCobros] Inicio");
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			fechaFinMes = UtilSGT.getDatetoString(UtilSGT.getLastDate(UtilSGT.getDateStringFormat(beanRequest.getContrato().getFechaInicio())));
			logger.debug("[generarPrimerosCobros] fechaFinMes:"+fechaFinMes);
			logger.debug("[generarPrimerosCobros] Alquiler 1:"+beanRequest.getContrato().getMontoAlquiler()+" Alquiler 2:"+beanRequest.getContratoAntiguo().getMontoAlquiler());
			if (beanRequest.getContrato().getMontoAlquiler().compareTo(beanRequest.getContratoAntiguo().getMontoAlquiler())!=0 ||
				!beanRequest.getContrato().getFechaInicio().toString().equals(beanRequest.getContratoAntiguo().getFechaInicio().toString())	
				){
				//Alquiler
				logger.debug("[generarPrimerosCobros] Cambio Monto Alquiler");
				primerCobro.setTblTipoServicio(new TblTipoServicio());
				primerCobro.getTblTipoServicio().setCodigoTipoServicio(Constantes.TIPO_SERVICIO_ALQUILER);
				//datFechaFinContrato = beanRequest.getContrato().getFechaFin();
				datFechaInicioContrato = beanRequest.getContrato().getFechaInicio();
				dblFactor = UtilSGT.obtenerFactorPrimerCobro(datFechaInicioContrato);
				primerCobro.setMontoCobro(new BigDecimal(dblFactor*beanRequest.getContrato().getMontoAlquiler().doubleValue()));
				primerCobro.setTipoMoneda(beanRequest.getContrato().getTipoMonedaAlquiler());
				primerCobro.setObservacion(Constantes.DESC_PRIMEROS_COBROS);
				primerCobro.setFechaCobro(fechaFinMes);
				
				lista.add(primerCobro);
			}
			//Servicios
			
			if(!igualesListaServicio(beanRequest.getListaServicio(), beanRequest.getContrato(),beanRequest)||
			   !beanRequest.getContrato().getFechaInicio().toString().equals(beanRequest.getContratoAntiguo().getFechaInicio().toString())
				){
				logger.debug("[generarPrimerosCobros] Cambio Servicio");
				if (beanRequest.getListaServicio() !=null && beanRequest.getListaServicio().size()>0){
					for(TblContratoServicio servicio:beanRequest.getListaServicio()){
						primerCobro 	= new TblContratoPrimerCobro();
						primerCobro.setTblTipoServicio(servicio.getTblTipoServicio());
						primerCobro.setTipoMoneda(servicio.getTipoMonedaServicio());
						primerCobro.setMontoCobro(new BigDecimal(dblFactor*servicio.getMonto().doubleValue()));
						primerCobro.setObservacion(Constantes.DESC_PRIMEROS_COBROS);
						primerCobro.setFechaCobro(fechaFinMes);
						lista.add(primerCobro);
					}
				}
				
			}
			beanRequest.setListaPrimerCobroNuevo(lista);
			logger.debug("[generarPrimerosCobros] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			primerCobro 			= null;
			datFechaInicioContrato	= null;
			dblFactor				= null;
			beanRequest				= null;
			lista					= null;
			fechaFinMes				= null;
		}
	}
	
	private boolean igualesListaServicio(List<TblContratoServicio> listaServicio, TblContrato contrato, BeanRequest beanRequest){
		boolean encontrado 				= false;
		TblContratoServicio servicio 	= null;
		TblContratoServicio servicioAnt	= null;
		List<TblContratoServicio> listaServicioAntiguo = null;
		try{
			logger.debug("[igualesListaServicio] Inicio");
			listaServicioAntiguo = contratoServicioDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			beanRequest.setListaServicioAntiguo(listaServicioAntiguo);
			if (listaServicio == null && listaServicioAntiguo==null){
				return true;
			}
			
			for(int i=0; listaServicio!=null && i< listaServicio.size(); i++){
				servicio = listaServicio.get(i);
				encontrado = false;
				for(int j=0; listaServicioAntiguo!=null && j < listaServicioAntiguo.size(); j++){
					servicioAnt = listaServicioAntiguo.get(j);
					if (servicio.getCodigoServicio() == servicioAnt.getCodigoServicio() ){
						encontrado = true;
						break;
					}
						
				}
				if (!encontrado){
					return false;
				}
			}
			if (encontrado){
				for(int i=0; listaServicioAntiguo!=null && i< listaServicioAntiguo.size(); i++){
					servicioAnt = listaServicioAntiguo.get(i);
					encontrado = false;
					for(int j=0; listaServicio!=null && j < listaServicio.size(); j++){
						servicio = listaServicio.get(j);
						if (servicio.getCodigoServicio() == servicioAnt.getCodigoServicio() ){
							encontrado = true;
							break;
						}
							
					}
					if (!encontrado){
						return false;
					}
				}
			}
			logger.debug("[igualesListaServicio] encontrado:"+encontrado);
			logger.debug("[igualesListaServicio] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			servicio 	= null;
			servicioAnt	= null;
		}
		return encontrado;
	}
	/**
	 * Se encarga de la eliminacion del servicio
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/aprobacion/eliminar/servicio", method = RequestMethod.POST)
	public String eliminarServicio(Model model, TblContrato entidad, HttpServletRequest request, Integer indice) {
		BeanRequest beanRequest				= null;
		try{
			logger.debug("[eliminarServicio] Inicio");
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			logger.debug("[eliminarServicio] indice:"+indice);
			logger.debug("[eliminarServicio] lista:"+beanRequest.getListaServicio().size());
			beanRequest.getListaServicio().remove(beanRequest.getListaServicio().get(indice));
			beanRequest.setContrato(entidad);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);
			logger.debug("[eliminarServicio] lista:"+beanRequest.getListaServicio().size());
			logger.debug("[eliminarServicio] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
		return  "cliente/aprobacion/apr_edicion";
	}
	
	/**
	 * Se encarga de adicionar un servicio
	 * 
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "/aprobacion/servicio", method = RequestMethod.POST)
	public String adicionarServicio(Model model, TblContratoServicio contratoServicio, TblContrato contrato, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;

		try{
			logger.debug("[adicionarServicio] Inicio");
			path = "cliente/aprobacion/apr_edicion";
			contratoServicio.setTotalAcumulado(contratoServicio.getMonto());
			contratoServicio.setTotalSaldo(contratoServicio.getMonto());
			contratoServicio.setTotalCobrado(new BigDecimal("0"));
			contratoServicio.setTblTipoServicio(tipoServicioDao.findOne(contratoServicio.getTblTipoServicio().getCodigoTipoServicio()));
			logger.debug("[adicionarServicio] Doc:"+contratoServicio.getTipoDocumentoServicio());
			logger.debug("[adicionarServicio] Moneda:"+contratoServicio.getTipoMonedaServicio());
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			if (beanRequest.getListaServicio() == null) {
				beanRequest.setListaServicio(new ArrayList<TblContratoServicio>());
				if (!validarExistenciaServicio(beanRequest.getListaServicio(),contratoServicio)){
					beanRequest.getListaServicio().add(contratoServicio);
				}else{
					model.addAttribute("resultadoServicio","El Tipo de Servicio ya existe!");
				}

			}else{
				if (!validarExistenciaServicio(beanRequest.getListaServicio(),contratoServicio)){
					beanRequest.getListaServicio().add(contratoServicio);
				}else{
					model.addAttribute("resultadoServicio","El Tipo de Servicio ya existe!");
				}
			}
			//model.addAttribute("listaServicio", beanRequest.getListaServicio());
			beanRequest.setContrato(contrato);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);

			logger.debug("[adicionarServicio] Fin");
		}catch(Exception e){
			logger.debug("[adicionarServicio] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			beanRequest = null;
		}

		return path;
	}

	private boolean validarExistenciaServicio(List<TblContratoServicio> lista, TblContratoServicio bean){
		boolean resultado = false;
		for(TblContratoServicio elemento:lista){
			if (elemento.getTblTipoServicio().getNombre().equals(bean.getTblTipoServicio().getNombre())){
				resultado = true;
				break;
			}
		}
		return resultado;
	}
	
}
