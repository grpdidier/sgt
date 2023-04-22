package com.pe.lima.sg.presentacion.caja;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pe.lima.sg.bean.caja.CobroBean;
import com.pe.lima.sg.bean.caja.DesembolsoBean;
import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.dao.caja.ICobroArbitrioDAO;
import com.pe.lima.sg.dao.caja.ICobroDAO;
import com.pe.lima.sg.dao.caja.ICxCDocumentoDAO;
import com.pe.lima.sg.dao.caja.IDesembolsoArbitrioDAO;
import com.pe.lima.sg.dao.caja.IDesembolsoDAO;
import com.pe.lima.sg.dao.cliente.IAdelantoDAO;
import com.pe.lima.sg.dao.cliente.IArbitrioDAO;
import com.pe.lima.sg.dao.cliente.IContratoDAO;
import com.pe.lima.sg.entity.caja.TblCobro;
import com.pe.lima.sg.entity.caja.TblCobroArbitrio;
import com.pe.lima.sg.entity.caja.TblCxcDocumento;
import com.pe.lima.sg.entity.caja.TblDesembolso;
import com.pe.lima.sg.entity.caja.TblDesembolsoArbitrio;
import com.pe.lima.sg.entity.cliente.TblAdelanto;
import com.pe.lima.sg.entity.cliente.TblArbitrio;
import com.pe.lima.sg.entity.cliente.TblContrato;
import com.pe.lima.sg.presentacion.BaseOperacionPresentacion;
import com.pe.lima.sg.presentacion.BeanRequest;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.ListaUtilAction;
import com.pe.lima.sg.presentacion.util.UtilSGT;

/**
 * Clase Bean que se encarga de la administracion de los contratos
 *
 * 			
 */
@Controller
public class ReversionAction extends BaseOperacionPresentacion<TblCobro> {

	private static final Logger logger = LogManager.getLogger(ReversionAction.class);
	
	@Autowired
	private IContratoDAO contratoDao;

	@Autowired
	private ICxCDocumentoDAO cxcDocumentoDao;
	
	@Autowired
	private IArbitrioDAO arbitrioDao;
	@Autowired
	private ICobroDAO cobroDao;
	
	@Autowired
	private ICobroArbitrioDAO cobroArbitrioDao;

	@Autowired
	private IDesembolsoDAO desembolsoDao;
	
	@Autowired
	private IDesembolsoArbitrioDAO desembolsoArbitrioDao;

	@Autowired
	private ListaUtilAction listaUtil;

	@Autowired
	private IAdelantoDAO adelantoDao;

	@SuppressWarnings("rawtypes")
	@Override
	public BaseOperacionDAO getDao() {
		return contratoDao;
	}

	/**
	 * Se encarga de direccionar a la pantalla de Solicitud de reversion - Listado
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/reversion", method = RequestMethod.GET)
	public String traerRegistro(Model model, HttpServletRequest request) {
		String path 								= "";
		List<DesembolsoBean> listaDesembolsoBean	= null;
		BeanRequest beanRequest						= null;
		try{
			path = "caja/reversion/rev_aprobacion_listado";
			listaDesembolsoBean = getDesembolsos();
			model.addAttribute("registros", listaDesembolsoBean);
			beanRequest = new BeanRequest();
			beanRequest.setListaSolicitudReversion(listaDesembolsoBean);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			request.getSession().setAttribute("beanRequest", beanRequest);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			listaDesembolsoBean		= null;
		}
		return path;
	}
	
	private List<DesembolsoBean> getDesembolsos(){
		List<TblDesembolso> listaDesembolso					= null;
		List<TblDesembolsoArbitrio> listaDesembolsoArbitrio	= null;
		List<DesembolsoBean> listaDesembolsoBean			= null;
		
		listaDesembolso = desembolsoDao.listarAllRevesionxEstado(Constantes.ESTADO_DESEMBOLSO_SOLICITADA_REVERSION);
		listaDesembolsoArbitrio = desembolsoArbitrioDao.listarAllRevesionxEstado(Constantes.ESTADO_DESEMBOLSO_SOLICITADA_REVERSION);
		listaDesembolsoBean = this.mAsignarDesembolso(listaDesembolso);
		listaDesembolsoBean = this.mAsignarDesembolso(listaDesembolsoArbitrio,listaDesembolsoBean);
		
		return listaDesembolsoBean;
	}
	
	/**
	 * Se encarga de direccionar a la pantalla de historial de desembolso
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/reversion/detalle/{id}/{tipo}", method = RequestMethod.GET)
	public String mostrarDetalleSolicitud(@PathVariable Integer id, @PathVariable String tipo, Model model, HttpServletRequest request) {
		String path 								= "";
		//List<TblCobro> listaCobro					= null;
		//List<TblAdelanto> listaAdelanto				= null;
		List<CobroBean> listaCobroBean				= null;
		//TblDesembolso desembolso					= null;
		Filtro filtro								= new Filtro();
		TblContrato contrato						= null;
		try{
			logger.debug("[mostrarDetalleSolicitud] Inicio");
			logger.debug("[mostrarDetalleSolicitud] id:"+ id + " - tipo:"+tipo);
			path = "caja/reversion/rev_aprobacion_detalle";
			
			/*listaCobro = cobroDao.listarAllActivosxDesembolso(id);
			/*desembolso = desembolsoDao.findOne(id);
			listaCobroBean = this.mAsignarCobro(listaCobro, desembolso);
			listaAdelanto = adelantoDao.listarAllActivosxDesembolso(id);
			listaCobroBean = this.mAsignarAdelanto(listaAdelanto, desembolso, listaCobroBean);
			filtro = new Filtro();
			filtro.setCodigoContrato(desembolso.getCodigoContrato());
			filtro.setCodigoDesembolso(desembolso.getCodigoDesembolso());
			filtro.setEstadoOperacion(desembolso.getEstadoOperacion());
			filtro.setDescripcion(desembolso.getMotivoReversion());*/
			if (!tipo.equals(Constantes.TIPO_COBRO_ARBITRIO)) {
				listaCobroBean = getDetaleSolicitud(id, filtro);
				filtro.setTipoCobro(Constantes.TIPO_COBRO_ALQUILER);
			}else {
				listaCobroBean = getDetaleSolicitudArbitrio(id,filtro );
				filtro.setTipoCobro(Constantes.TIPO_COBRO_ARBITRIO);
			}
			
			//Datos del contrato
			contrato = contratoDao.findOne(filtro.getCodigoContrato());
			model.addAttribute("contrato", contrato);
			model.addAttribute("registros", listaCobroBean);
			model.addAttribute("filtro", filtro);
			logger.debug("[mostrarDetalleSolicitud] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			//listaCobro			= null;
			//listaAdelanto		= null;
			listaCobroBean		= null;
			//desembolso			= null;
		}
		return path;
	}
	
	/*Para Alquiler, Servicio y Luz, obtenemos el detalle*/
	private List<CobroBean> getDetaleSolicitud(Integer id, Filtro filtro) {
		List<TblCobro> listaCobro					= null;
		List<TblAdelanto> listaAdelanto				= null;
		List<CobroBean> listaCobroBean				= null;
		TblDesembolso desembolso					= null;
		
		listaCobro = cobroDao.listarAllActivosxDesembolso(id);
		desembolso = desembolsoDao.findOne(id);
		listaCobroBean = this.mAsignarCobro(listaCobro, desembolso);
		listaAdelanto = adelantoDao.listarAllActivosxDesembolso(id);
		listaCobroBean = this.mAsignarAdelanto(listaAdelanto, desembolso, listaCobroBean);
		filtro.setCodigoContrato(desembolso.getCodigoContrato());
		filtro.setCodigoDesembolso(desembolso.getCodigoDesembolso());
		filtro.setEstadoOperacion(desembolso.getEstadoOperacion());
		filtro.setDescripcion(desembolso.getMotivoReversion());
		return listaCobroBean;
	}
	
	/*Para Alquiler, Servicio y Luz, obtenemos el detalle*/
	private List<CobroBean> getDetaleSolicitudArbitrio(Integer id, Filtro filtro) {
		List<TblCobroArbitrio> listaCobro			= null;
		List<TblAdelanto> listaAdelanto				= null;
		List<CobroBean> listaCobroBean				= null;
		TblDesembolsoArbitrio desembolso			= null;
		
		listaCobro = cobroArbitrioDao.listarAllActivosxDesembolso(id);
		desembolso = desembolsoArbitrioDao.getDesembolsoArbitrioxId(id);
		listaCobroBean = this.mAsignarCobroArbitrio(listaCobro, desembolso);
		listaAdelanto = adelantoDao.listarAllActivosxDesembolsoArbitrio(id);
		listaCobroBean = this.mAsignarAdelantoArbitrio(listaAdelanto, desembolso, listaCobroBean);

		filtro.setCodigoContrato(desembolso.getCodigoContrato());
		filtro.setCodigoDesembolso(desembolso.getCodigoDesembolsoArbitrio());
		filtro.setEstadoOperacion(desembolso.getEstadoOperacion());
		filtro.setDescripcion(desembolso.getMotivoReversion());
		return listaCobroBean;
	}
	
	/**
	 * Se encarga de registrar el rechazo de la solicitud de reversion
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/reversion/rechazar", method = RequestMethod.POST)
	public String registrarRechazoReversionCobro(Model model, Filtro filtro,HttpServletRequest request) {
		String path 								= "";
		
		List<DesembolsoBean> listaDesembolsoBean	= null;
		BeanRequest beanRequest						= null;
		try{
			path = "caja/reversion/rev_aprobacion_listado";
			//Actualiza a activo el desembolso del cobro
			if (filtro.getTipoCobro().equals(Constantes.TIPO_COBRO_ARBITRIO)) {
				registrarRechazoReversionArbitrio(filtro, request);
			}else {
				registrarRechazoReversion(filtro, request);
			}
			/*desembolso = desembolsoDao.findOne(filtro.getCodigoDesembolso());
			desembolso.setEstadoOperacion(Constantes.ESTADO_DESEMBOLSO_ACTIVO);
			desembolso.setMotivoReversion(filtro.getDescripcion());
			this.preEditarDesembolso(desembolso, request);
			desembolsoDao.save(desembolso);*/
			model.addAttribute("respuestaReversion", "La solicitud de reversión fue rechazado exitosamente.");
			//Lista los desembolsos
			listaDesembolsoBean = getDesembolsos();
			model.addAttribute("registros", listaDesembolsoBean);
			//Asigna a sesion
			beanRequest = new BeanRequest();
			beanRequest.setListaSolicitudReversion(listaDesembolsoBean);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			request.getSession().setAttribute("beanRequest", beanRequest);
		}catch(Exception e){
			logger.debug("[regresarContrato] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			listaDesembolsoBean	= null;
		}
		return path;
	}
	
	private void registrarRechazoReversion(Filtro filtro,HttpServletRequest request) {
		TblDesembolso desembolso					= null;
		//Actualiza a activo el desembolso del cobro
		desembolso = desembolsoDao.findOne(filtro.getCodigoDesembolso());
		desembolso.setEstadoOperacion(Constantes.ESTADO_DESEMBOLSO_ACTIVO);
		desembolso.setMotivoReversion(filtro.getDescripcion());
		this.preEditarDesembolso(desembolso, request);
		desembolsoDao.save(desembolso);
	}
	private void registrarRechazoReversionArbitrio(Filtro filtro,HttpServletRequest request) {
		TblDesembolsoArbitrio desembolso		=  null;
		//Actualiza a activo el desembolso del cobro
		desembolso = desembolsoArbitrioDao.findOne(filtro.getCodigoDesembolso());
		desembolso.setEstadoOperacion(Constantes.ESTADO_DESEMBOLSO_ACTIVO);
		desembolso.setMotivoReversion(filtro.getDescripcion());
		this.preEditarDesembolso(desembolso, request);
		desembolsoArbitrioDao.save(desembolso);
	}
	private String registrarRevesionCobro(Model model, Filtro filtro,HttpServletRequest request) {
		List<DesembolsoBean> listaDesembolsoBean	= null;
		TblDesembolso desembolso					= null;
		BeanRequest beanRequest						= null;
		boolean resultado							= false;
		String path 								= "";
		try{
			path = "caja/reversion/rev_aprobacion_listado";
			
			//Actualiza a activo el desembolso del cobro
			desembolso = desembolsoDao.findOne(filtro.getCodigoDesembolso());
			//Reversion de Cobro y actualizacion de CxCDocumento
			resultado = this.mReversionCobros(desembolso, request);
			//Reversion de Adelantos
			if (resultado){
				resultado = this.mReversionAdelantos(desembolso, request);
			}else{
				model.addAttribute("respuestaReversion", "Se produjo un error en la actualizacion de adelantos. Comunicar al Administrador.");
				return path;
			}
			//Actualizacion de Desembolso
			if (resultado){
				resultado = this.mActualizarAceptadoDesembolso(desembolso, filtro, request);
			}else{
				model.addAttribute("respuestaReversion", "Se produjo un error en la actualizacion de desembolso. Comunicar al Administrador.");
				return path;
			}
			if (resultado){
				model.addAttribute("respuestaReversion", "La solicitud de reversión fue aceptado exitosamente.");
				//Lista los desembolsos
				listaDesembolsoBean = this.getDesembolsos();
				model.addAttribute("registros", listaDesembolsoBean);
				//Asigna a sesion
				beanRequest = new BeanRequest();
				beanRequest.setListaSolicitudReversion(listaDesembolsoBean);
				this.cargarListasRequestBeanContrato(model, beanRequest);
				request.getSession().setAttribute("beanRequest", beanRequest);	
			}else{
				model.addAttribute("respuesta", "Se produjo un error en la actualizacion de desembolso. Comunicar al Administrador.");
				path = "caja/revesion/rev_aprobacion_detalle";
			}
			
		}catch(Exception e){
			logger.debug("[regresarContrato] Error:"+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuestaReversion", "Se genero un error inesperado:"+e.getMessage());
		}finally{
			listaDesembolsoBean	= null;
		}
		return path;
	}
	private String registrarRevesionCobroArbitrio(Model model, Filtro filtro,HttpServletRequest request) {
		List<DesembolsoBean> listaDesembolsoBean			= null;
		TblDesembolsoArbitrio desembolso					= null;
		BeanRequest beanRequest								= null;
		boolean resultado									= false;
		String path 										= "";
		try{
			path = "caja/reversion/rev_aprobacion_listado";
			
			//Actualiza a activo el desembolso del cobro
			desembolso = desembolsoArbitrioDao.findOne(filtro.getCodigoDesembolso());
			//Reversion de Cobro y actualizacion de CxCDocumento
			resultado = this.mReversionCobrosArbitrios(desembolso, request);
			//Reversion de Adelantos
			if (resultado){
				resultado = this.mReversionAdelantosArbitrios(desembolso, request);
			}else{
				model.addAttribute("respuestaReversion", "Se produjo un error en la actualizacion de adelantos. Comunicar al Administrador.");
				return path;
			}
			//Actualizacion de Desembolso
			if (resultado){
				resultado = this.mActualizarAceptadoDesembolsoArbitrio(desembolso, filtro, request);
			}else{
				model.addAttribute("respuestaReversion", "Se produjo un error en la actualizacion de desembolso. Comunicar al Administrador.");
				return path;
			}
			if (resultado){
				model.addAttribute("respuestaReversion", "La solicitud de reversión fue aceptado exitosamente.");
				//Lista los desembolsos
				//listaDesembolso = desembolsoDao.listarAllRevesionxEstado(Constantes.ESTADO_DESEMBOLSO_SOLICITADA_REVERSION);
				listaDesembolsoBean = this.getDesembolsos();// this.mAsignarDesembolso(listaDesembolso);
				model.addAttribute("registros", listaDesembolsoBean);
				//Asigna a sesion
				beanRequest = new BeanRequest();
				beanRequest.setListaSolicitudReversion(listaDesembolsoBean);
				this.cargarListasRequestBeanContrato(model, beanRequest);
				request.getSession().setAttribute("beanRequest", beanRequest);	
			}else{
				model.addAttribute("respuesta", "Se produjo un error en la actualizacion de desembolso. Comunicar al Administrador.");
				path = "caja/revesion/rev_aprobacion_detalle";
			}
			
		}catch(Exception e){
			logger.debug("[regresarContrato] Error:"+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuestaReversion", "Se genero un error inesperado:"+e.getMessage());
		}finally{
			listaDesembolsoBean	= null;
		}
		return path;
	}
	
	/**
	 * Se encarga de registrar el rechazo de la solicitud de reversion
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/reversion/aceptar", method = RequestMethod.POST)
	public String registrarAceptacionReversionCobro(Model model, Filtro filtro,HttpServletRequest request) {
		String path 								= "";
		if (filtro.getTipoCobro().equals(Constantes.TIPO_COBRO_ARBITRIO)) {
			path = registrarRevesionCobroArbitrio(model, filtro, request);
		}else {
			path = registrarRevesionCobro(model, filtro, request);
		}
		return path;
	}
	
	/**
	 * Se encarga de regresar a la pantalla de Solicitud de Reversion - Listado
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/reversion/regresar", method = RequestMethod.POST)
	public String regresarSolicitudReversion(Model model, Filtro filtro, String path, HttpServletRequest request) {
		//TblContrato contrato				= null;
		BeanRequest beanRequest				= null;
		path = "caja/reversion/rev_aprobacion_listado";
		try{
			logger.debug("[regresarSolicitudReversion] Inicio");
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);
			logger.debug("[regresarSolicitudReversion] Fin");
		}catch(Exception e){
			logger.debug("[regresarSolicitudReversion] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}

		return path;
	}
	
	/*
	 * Carga en session las listas y beans necesarios para la operacion del contrato
	 */
	private void cargarListasRequestBeanContrato(Model model, BeanRequest beanRequest){
		
		model.addAttribute("registros", beanRequest.getListaSolicitudReversion());
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
	 * Actualizacion para los campos de auditoria del Desembolso
	 */
	public void preEditarDesembolso(TblDesembolso entidad, HttpServletRequest request) {
		try{
			logger.debug("[preEditarDesembolso] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			logger.debug("[preEditarDesembolso] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void preEditarDesembolso(TblDesembolsoArbitrio entidad, HttpServletRequest request) {
		try{
			logger.debug("[preEditarDesembolso] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			logger.debug("[preEditarDesembolso] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	 * Campos de auditoria del documento
	 */
	public void preEditarDocumento(TblCxcDocumento entidad, HttpServletRequest request) {
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
	/*
	 * Campos de auditoria del documento
	 */
	public void preEditarDocumentoArbitrio(TblArbitrio entidad, HttpServletRequest request) {
		try{
			logger.debug("[preEditarDocumentoArbitrio] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			logger.debug("[preEditarDocumentoArbitrio] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	 * Reversion de Cobros
	 */
	public boolean mReversionCobros(TblDesembolso desembolso, HttpServletRequest request) throws Exception{
		boolean respuesta 			= false;
		List<TblCobro> listaCobro 	= null;
		TblCxcDocumento documento	= null;
		try{
			listaCobro = cobroDao.listarAllActivosxDesembolso(desembolso.getCodigoDesembolso());
			if (listaCobro != null){
				//Reversion de cobro en documento
				this.mActualizarSaldoDocumento(documento, listaCobro,request);
				//Actualiza el cobro
				this.mActualizarEstadoCobro(listaCobro, request);
				respuesta = true;
			}else{
				//No se tiene ningun cobro por reversar
				respuesta = true;
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}return respuesta;
	}
	/*
	 * Reversion de Cobros
	 */
	public boolean mReversionCobrosArbitrios(TblDesembolsoArbitrio desembolso, HttpServletRequest request) throws Exception{
		boolean respuesta 					= false;
		List<TblCobroArbitrio> listaCobro 	= null;
		TblArbitrio documento				= null;
		try{
			listaCobro = cobroArbitrioDao.listarAllActivosxDesembolso(desembolso.getCodigoDesembolsoArbitrio());
			if (listaCobro != null){
				//Reversion de cobro en documento
				this.mActualizarSaldoDocumentoArbitrio(documento, listaCobro,request);
				//Actualiza el cobro
				this.mActualizarEstadoCobroArbitrio(listaCobro, request);
				respuesta = true;
			}else{
				//No se tiene ningun cobro por reversar
				respuesta = true;
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}return respuesta;
	}
	/*
	 * Reversion de los adelantos
	 */
	public boolean mReversionAdelantos(TblDesembolso desembolso, HttpServletRequest request) throws Exception{
		boolean respuesta 					= false;
		List<TblAdelanto> listaAdelanto 	= null;
		try{
			listaAdelanto = adelantoDao.listarAllActivosxDesembolso(desembolso.getCodigoDesembolso());
			if (listaAdelanto != null){
				//Reversion de adelanto
				this.mActualizarEstadoAdelanto(listaAdelanto, request);
				respuesta = true;
			}else{
				//No se encontro adelantos
				respuesta = true;
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return respuesta;
	}
	/*
	 * Reversion de los adelantos
	 */
	public boolean mReversionAdelantosArbitrios(TblDesembolsoArbitrio desembolso, HttpServletRequest request) throws Exception{
		boolean respuesta 					= false;
		List<TblAdelanto> listaAdelanto 	= null;
		try{
			listaAdelanto = adelantoDao.listarAllActivosxDesembolsoArbitrio(desembolso.getCodigoDesembolsoArbitrio());
			if (listaAdelanto != null){
				//Reversion de adelanto
				this.mActualizarEstadoAdelanto(listaAdelanto, request);
				respuesta = true;
			}else{
				//No se encontro adelantos
				respuesta = true;
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return respuesta;
	}
	
	/*
	 * Actualiza el saldo, monto cobrado y nombre de la CxCDocumento
	 */
	private void mActualizarSaldoDocumento(TblCxcDocumento documento, List<TblCobro> listaCobro, HttpServletRequest request){
		for(TblCobro cobro: listaCobro){
			documento = cxcDocumentoDao.findOne(cobro.getTblCxcDocumento().getCodigoCxcDoc());
			if (documento.getTipoMoneda().equals(Constantes.MONEDA_DOLAR)){
				documento.setSaldo( documento.getSaldo().add(cobro.getMontoDolares()));
				documento.setMontoCobrado(documento.getMontoCobrado().subtract(cobro.getMontoDolares()));
				//documento.setNombre(documento.getNombre().concat(Constantes.DESC_REVERSION_COBRO));
			}else{
				documento.setSaldo( documento.getSaldo().add(cobro.getMontoSoles()));
				documento.setMontoCobrado(documento.getMontoCobrado().subtract(cobro.getMontoSoles()));
				//documento.setNombre(documento.getNombre().concat(Constantes.DESC_REVERSION_COBRO));
			}
			this.preEditarDocumento(documento, request);
			cxcDocumentoDao.save(documento);
		}
	}
	/*
	 * Actualiza el saldo, monto cobrado y nombre de la TblArbitrio
	 */
	private void mActualizarSaldoDocumentoArbitrio(TblArbitrio documento, List<TblCobroArbitrio> listaCobro, HttpServletRequest request){
		for(TblCobroArbitrio cobro: listaCobro){
			documento = arbitrioDao.findOne(cobro.getCodigoArbitrio());
			
			documento.setSaldo( documento.getSaldo().add(cobro.getMontoSoles()));
			documento.setValorCobrado(documento.getValorCobrado().subtract(cobro.getMontoSoles()));
			
			this.preEditarDocumentoArbitrio(documento, request);
			arbitrioDao.save(documento);
		}
	}
	/*
	 * Actualiza a inactivo el cobro 
	 */
	private void mActualizarEstadoCobro(List<TblCobro> listaCobro, HttpServletRequest request){
		for(TblCobro cobro: listaCobro){
			this.preEditarCobro(cobro, request);
			cobro.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			cobroDao.save(cobro);
		}
	}
	/*
	 * Actualiza a inactivo el cobro 
	 */
	private void mActualizarEstadoCobroArbitrio(List<TblCobroArbitrio> listaCobro, HttpServletRequest request){
		for(TblCobroArbitrio cobro: listaCobro){
			this.preEditarCobroArbitrio(cobro, request);
			cobro.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			cobroArbitrioDao.save(cobro);
		}
	}
	/*
	 * Actualiza a inactivo el adelanto
	 */
	private void mActualizarEstadoAdelanto(List<TblAdelanto> listaAdelanto, HttpServletRequest request){
		for(TblAdelanto adelanto: listaAdelanto){
			this.preEditarAdelanto(adelanto, request);
			//adelanto.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			adelanto.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			//reversa los montos
			adelanto.setMontoDolares(adelanto.getMontoDolaresConsumido());
			adelanto.setMontoSolesConsumido(adelanto.getMontoSolesConsumido());
			adelanto.setMontoDolaresConsumido(new BigDecimal("0"));
			adelanto.setMontoSolesConsumido(new BigDecimal("0"));
			adelantoDao.save(adelanto);
		}
	}
	/*
	 * Actualiza a aceptada la reversion del desembolso
	 */
	private boolean mActualizarAceptadoDesembolso(TblDesembolso desembolso, Filtro filtro, HttpServletRequest request){
		boolean resultado 	= false;
		try{
			this.preEditarDesembolso(desembolso, request);
			desembolso.setEstadoOperacion(Constantes.ESTADO_DESEMBOLSO_ACEPTADA_REVERSION);
			//desembolso.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			desembolso.setMotivoReversion(filtro.getDescripcion().concat(" [" + Constantes.DESC_REVERSION_COBRO) + "]");
			desembolsoDao.save(desembolso);
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return resultado;
		
	}
	/*
	 * Actualiza a aceptada la reversion del desembolso
	 */
	private boolean mActualizarAceptadoDesembolsoArbitrio(TblDesembolsoArbitrio desembolso, Filtro filtro, HttpServletRequest request){
		boolean resultado 	= false;
		try{
			this.preEditarDesembolso(desembolso, request);
			desembolso.setEstadoOperacion(Constantes.ESTADO_DESEMBOLSO_ACEPTADA_REVERSION);
			//desembolso.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			desembolso.setMotivoReversion(filtro.getDescripcion().concat(" [" + Constantes.DESC_REVERSION_COBRO) + "]");
			desembolsoArbitrioDao.save(desembolso);
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return resultado;
		
	}
	
	/*
	 * Campos de auditoria del cobro
	 */
	public void preEditarCobro(TblCobro entidad, HttpServletRequest request) {
		try{
			logger.debug("[preEditarCobro] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			
			logger.debug("[preEditarCobro] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	 * Campos de auditoria del cobro
	 */
	public void preEditarCobroArbitrio(TblCobroArbitrio entidad, HttpServletRequest request) {
		try{
			logger.debug("[preEditarCobroArbitrio] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			
			logger.debug("[preEditarCobroArbitrio] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	 * Campos de Auditoria - Modificacion
	 */
	public void preEditarAdelanto(TblAdelanto entidad, HttpServletRequest request) {
		try{
			logger.debug("[preEditarAdelanto] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			
			logger.debug("[preEditarAdelanto] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	 * Copia los datos de la tabla al bean
	 */
	public List<DesembolsoBean> mAsignarDesembolso(List<TblDesembolso> listaDesembolso){
		List<DesembolsoBean> listaDesembolsoBean	= null;
		DesembolsoBean desembolsoBean				= null;
		try{
			if (listaDesembolso!=null && listaDesembolso.size()>0){
				for(TblDesembolso desembolso:listaDesembolso){
					desembolsoBean = new DesembolsoBean();
					desembolsoBean.setCodigoContrato(desembolso.getCodigoContrato());
					desembolsoBean.setCodigoDesembolso(desembolso.getCodigoDesembolso());
					desembolsoBean.setFechaCobro(desembolso.getFechaCobro());
					desembolsoBean.setMontoDolares(desembolso.getMontoDolares());
					desembolsoBean.setMontoSoles(desembolso.getMontoSoles());
					desembolsoBean.setTipoCambio(desembolso.getTipoCambio());
					desembolsoBean.setTipoCobro(desembolso.getTipoCobro());
					desembolsoBean.setFechaCreacion(desembolso.getFechaCreacion());
					desembolsoBean.setEstadoOperacion(desembolso.getEstadoOperacion());
					if (listaDesembolsoBean==null){
						listaDesembolsoBean = new ArrayList<DesembolsoBean>();
						listaDesembolsoBean.add(desembolsoBean);
					}else{
						listaDesembolsoBean.add(desembolsoBean);
					}
				}
			}
		}catch(Exception e){
			
		}
		return listaDesembolsoBean;
	}
	/*
	 * Copia los datos de la tabla al bean
	 */
	public List<DesembolsoBean> mAsignarDesembolso(List<TblDesembolsoArbitrio> listaDesembolso,List<DesembolsoBean> listaDesembolsoBean){
		DesembolsoBean desembolsoBean				= null;
		try{
			if (listaDesembolso!=null && listaDesembolso.size()>0){
				for(TblDesembolsoArbitrio desembolso:listaDesembolso){
					desembolsoBean = new DesembolsoBean();
					desembolsoBean.setCodigoContrato(desembolso.getCodigoContrato());
					desembolsoBean.setCodigoDesembolso(desembolso.getCodigoDesembolsoArbitrio());
					desembolsoBean.setFechaCobro(desembolso.getFechaCobro());
					desembolsoBean.setMontoDolares(desembolso.getMontoDolares());
					desembolsoBean.setMontoSoles(desembolso.getMontoSoles());
					desembolsoBean.setTipoCambio(desembolso.getTipoCambio());
					desembolsoBean.setTipoCobro(desembolso.getTipoCobro());
					desembolsoBean.setFechaCreacion(desembolso.getFechaCreacion());
					desembolsoBean.setEstadoOperacion(desembolso.getEstadoOperacion());
					if (listaDesembolsoBean==null){
						listaDesembolsoBean = new ArrayList<DesembolsoBean>();
						listaDesembolsoBean.add(desembolsoBean);
					}else{
						listaDesembolsoBean.add(desembolsoBean);
					}
				}
			}
		}catch(Exception e){
			
		}
		return listaDesembolsoBean;
	}
	
	/*
	 * Copia los datos de la tabla al bean
	 */
	public List<CobroBean> mAsignarCobro(List<TblCobro> listaCobro, TblDesembolso desembolso){
		List<CobroBean> listaCobroBean	= null;
		CobroBean cobroBean				= null;
		try{
			if (listaCobro!=null && listaCobro.size()>0){
				for(TblCobro cobro:listaCobro){
					cobroBean = new CobroBean();
					cobroBean.setCodigoContrato(desembolso.getCodigoContrato());
					cobroBean.setCodigoDesembolso(desembolso.getCodigoDesembolso());
					cobroBean.setFechaCobro(cobro.getFechaCobro());
					cobroBean.setMontoDolares(cobro.getMontoDolares());
					cobroBean.setMontoSoles(cobro.getMontoSoles());
					cobroBean.setTipoCambio(cobro.getTipoCambio());
					cobroBean.setTipoCobro(cobro.getTipoCobro());
					cobroBean.setFechaCreacion(cobro.getFechaCreacion());
					cobroBean.setTipoOperacion(Constantes.OPERACION_COBRO);
					cobroBean.setNota(desembolso.getNota());
					if (listaCobroBean==null){
						listaCobroBean = new ArrayList<CobroBean>();
						listaCobroBean.add(cobroBean);
					}else{
						listaCobroBean.add(cobroBean);
					}
				}
			}
		}catch(Exception e){
			
		}
		return listaCobroBean;
	}
	/*
	 * Copia los datos de la tabla al bean
	 */
	public List<CobroBean> mAsignarCobroArbitrio(List<TblCobroArbitrio> listaCobro, TblDesembolsoArbitrio desembolso){
		List<CobroBean> listaCobroBean	= null;
		CobroBean cobroBean				= null;
		try{
			if (listaCobro!=null && listaCobro.size()>0){
				for(TblCobroArbitrio cobro:listaCobro){
					cobroBean = new CobroBean();
					cobroBean.setCodigoContrato(desembolso.getCodigoContrato());
					cobroBean.setCodigoDesembolso(desembolso.getCodigoDesembolsoArbitrio());
					cobroBean.setFechaCobro(cobro.getFechaCobro());
					cobroBean.setMontoDolares(cobro.getMontoDolares());
					cobroBean.setMontoSoles(cobro.getMontoSoles());
					cobroBean.setTipoCambio(cobro.getTipoCambio());
					cobroBean.setTipoCobro(cobro.getTipoCobro());
					cobroBean.setFechaCreacion(cobro.getFechaCreacion());
					cobroBean.setTipoOperacion(Constantes.OPERACION_COBRO);
					cobroBean.setNota(desembolso.getNota());
					if (listaCobroBean==null){
						listaCobroBean = new ArrayList<CobroBean>();
						listaCobroBean.add(cobroBean);
					}else{
						listaCobroBean.add(cobroBean);
					}
				}
			}
		}catch(Exception e){
			
		}
		return listaCobroBean;
	}
	/*
	 * Copia los datos de la tabla al bean
	 */
	public List<CobroBean> mAsignarAdelanto(List<TblAdelanto> listaAdelanto, TblDesembolso desembolso, List<CobroBean> listaCobroBean){
		CobroBean cobroBean				= null;
		try{
			if (listaAdelanto!=null && listaAdelanto.size()>0){
				for(TblAdelanto adelanto:listaAdelanto){
					cobroBean = new CobroBean();
					cobroBean.setCodigoContrato(desembolso.getCodigoContrato());
					cobroBean.setCodigoDesembolso(desembolso.getCodigoDesembolso());
					cobroBean.setFechaCobro(adelanto.getFechaAdelanto());
					cobroBean.setMontoDolares(adelanto.getMontoDolares());
					cobroBean.setMontoSoles(adelanto.getMontoSoles());
					cobroBean.setTipoCambio(adelanto.getTipoCambio());
					cobroBean.setTipoCobro(adelanto.getTipoRubro());
					cobroBean.setFechaCreacion(adelanto.getFechaCreacion());
					cobroBean.setTipoOperacion(Constantes.OPERACION_ADELANTO);
					if (listaCobroBean==null){
						listaCobroBean = new ArrayList<CobroBean>();
						listaCobroBean.add(cobroBean);
					}else{
						listaCobroBean.add(cobroBean);
					}
				}
			}
		}catch(Exception e){
			
		}
		return listaCobroBean;
	}
	/*
	 * Copia los datos de la tabla al bean
	 */
	public List<CobroBean> mAsignarAdelantoArbitrio(List<TblAdelanto> listaAdelanto, TblDesembolsoArbitrio desembolso, List<CobroBean> listaCobroBean){
		CobroBean cobroBean				= null;
		try{
			if (listaAdelanto!=null && listaAdelanto.size()>0){
				for(TblAdelanto adelanto:listaAdelanto){
					cobroBean = new CobroBean();
					cobroBean.setCodigoContrato(desembolso.getCodigoContrato());
					cobroBean.setCodigoDesembolso(desembolso.getCodigoDesembolsoArbitrio());
					cobroBean.setFechaCobro(adelanto.getFechaAdelanto());
					cobroBean.setMontoDolares(adelanto.getMontoDolares());
					cobroBean.setMontoSoles(adelanto.getMontoSoles());
					cobroBean.setTipoCambio(adelanto.getTipoCambio());
					cobroBean.setTipoCobro(adelanto.getTipoRubro());
					cobroBean.setFechaCreacion(adelanto.getFechaCreacion());
					cobroBean.setTipoOperacion(Constantes.OPERACION_ADELANTO);
					if (listaCobroBean==null){
						listaCobroBean = new ArrayList<CobroBean>();
						listaCobroBean.add(cobroBean);
					}else{
						listaCobroBean.add(cobroBean);
					}
				}
			}
		}catch(Exception e){
			
		}
		return listaCobroBean;
	}

	@Override
	public TblCobro getNuevaEntidad() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	
	
	
	
	
}
