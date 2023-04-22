package com.pe.lima.sg.presentacion.mantenimiento;

import java.util.ArrayList;
import java.util.Date;
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

import com.pe.lima.sg.dao.BaseDAO;
import com.pe.lima.sg.dao.mantenimiento.ITipoCambioDAO;
import com.pe.lima.sg.entity.mantenimiento.TblTipoCambio;
import com.pe.lima.sg.presentacion.BasePresentacion;
import com.pe.lima.sg.presentacion.Campo;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.UtilSGT;

/**
 * Clase Bean que se encarga de la administracion de los tipocambios
 *
 * 			
 */
@Controller
//@PreAuthorize("hasAuthority('CRUD')")
public class TipoCambioAction extends BasePresentacion<TblTipoCambio> {
	private static final Logger logger = LogManager.getLogger(TipoCambioAction.class);
	@Autowired
	private ITipoCambioDAO tipocambioDao;

	
	
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDAO getDao() {
		return tipocambioDao;
	}
	
	/**
	 * Se encarga de listar todos los tipocambios
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tipocambios", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path) {
		Map<String, Object> campos = null;
		Filtro filtro = null;
		try{
			logger.debug("[traerRegistros] Inicio");
			path = "mantenimiento/tipocambio/tca_listado";
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			filtro = new Filtro();
			model.addAttribute("filtro", filtro);
			this.listarTipoCambios(model, filtro);
			logger.debug("[traerRegistros] Fin");
		}catch(Exception e){
			logger.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			campos		= null;
			filtro = null;
		}
		
		return path;
	}
	/**
	 * Se encarga de buscar la informacion del TipoCambio segun el filtro
	 * seleccionado
	 * 
	 * @param model
	 * @param tipocambioBean
	 * @return
	 */
	@RequestMapping(value = "/tipocambios/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, Filtro filtro, String path,HttpServletRequest request) {
		Map<String, Object> campos = null;
		path = "mantenimiento/tipocambio/tca_listado";
		try{
			logger.debug("[traerRegistrosFiltrados] Inicio");
			this.listarTipoCambios(model, filtro);
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			model.addAttribute("filtro", filtro);
			logger.debug("[traerRegistrosFiltrados] Fin");
		}catch(Exception e){
			logger.debug("[traerRegistrosFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			campos		= null;
		}
		logger.debug("[traerRegistrosFiltrados] Fin");
		return path;
	}
	/*** Listado de TipoCambios ***/
	private void listarTipoCambios(Model model, Filtro filtro){
		List<TblTipoCambio> entidades = new ArrayList<TblTipoCambio>();
		try{
			this.setFechaCriterio(filtro);
			logger.debug("[listarTipoCambioes] Fec Inicio:"+filtro.getFechaInicio());
			logger.debug("[listarTipoCambioes] Fec Fin:"+filtro.getFechaFin());
			entidades = tipocambioDao.listarCriterios(filtro.getFechaInicio(), filtro.getFechaFin());
			logger.debug("[listarTipoCambioes] entidades:"+entidades);
			model.addAttribute("registros", entidades);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades = null;
		}
	}
	
	
	/**
	 * Se encarga de direccionar a la pantalla de edicion del TipoCambio
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "tipocambio/editar/{id}", method = RequestMethod.GET)
	public String editarTipoCambio(@PathVariable Integer id, Model model) {
		TblTipoCambio entidad 			= null;
		try{
			entidad = tipocambioDao.findOne(id);
			model.addAttribute("entidad", entidad);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return "mantenimiento/tipocambio/tca_edicion";
	}
	
	
	/**
	 * Se encarga de direccionar a la pantalla de creacion del TipoCambio
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "tipocambio/nuevo", method = RequestMethod.GET)
	public String crearTipoCambio(Model model) {
		TblTipoCambio tipoCambio = new TblTipoCambio();
		try{
			tipoCambio.setFecha(new Date());
			logger.debug("[crearTipoCambio] Inicio");
			model.addAttribute("entidad", tipoCambio);
			logger.debug("[crearTipoCambio] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "mantenimiento/tipocambio/tca_nuevo";
	}
	
	@Override
	public void preGuardar(TblTipoCambio entidad, HttpServletRequest request) {
		try{
			logger.debug("[preGuardar] Inicio" );
			entidad.setFechaCreacion(new Date(System.currentTimeMillis()));
			entidad.setIpCreacion(request.getRemoteAddr());
			entidad.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			logger.debug("[preGuardar] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	/** Validamos logica del negocio**/
	@Override
	public boolean validarNegocio(Model model,TblTipoCambio entidad, HttpServletRequest request){
		boolean exitoso = true;
		Integer total 	= null;
		try{
			//Validando la existencia del tipocambio
			total = tipocambioDao.countOneByFecha(entidad.getFecha());
			if (total > 0){
				exitoso = false;
				model.addAttribute("respuesta", "El Tipo de Cambio existe, debe modificarlo para continuar...");
			}
			
		}catch(Exception e){
			exitoso = false;
		}finally{
			total = null;
		}
		return exitoso;
	}
	/**
	 * Se encarga de guardar la informacion del TipoCambio
	 * 
	 * @param tipocambioBean
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "tipocambio/nuevo/guardar", method = RequestMethod.POST)
	public String guardarEntidad(Model model, TblTipoCambio entidad, HttpServletRequest request, String path) {
		Map<String, Object> campos 	= null;
		path = "mantenimiento/tipocambio/tca_listado";
		Filtro filtro 				= new Filtro();
		try{
			logger.debug("[guardarEntidad] Inicio" );
			if (this.validarNegocio(model, entidad, request)){
				logger.debug("[guardarEntidad] Pre Guardar..." );
				this.preGuardar(entidad, request);
				boolean exitoso = super.guardar(entidad, model);
				logger.debug("[guardarEntidad] Guardado..." );
				if (exitoso){
					List<TblTipoCambio> entidades = tipocambioDao.buscarOneByFecha(entidad.getFecha());
					model.addAttribute("registros", entidades);
					campos = configurarCamposConsulta();
					model.addAttribute("contenido", campos);
					this.setFechaCriterio(filtro);
					model.addAttribute("filtro", filtro);
				}else{
					path = "mantenimiento/tipocambio/tca_nuevo";
					model.addAttribute("entidad", entidad);
					
				}
			}else{
				path = "mantenimiento/tipocambio/tca_nuevo";
				model.addAttribute("entidad", entidad);
			}
			
			logger.debug("[guardarEntidad] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			campos 			= null;
		}
		return path;
		
	}
	
	@Override
	public void preEditar(TblTipoCambio entidad, HttpServletRequest request) {
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
	
	@RequestMapping(value = "tipocambio/nuevo/editar", method = RequestMethod.POST)
	public String editarEntidad(TblTipoCambio entidad, Model model, HttpServletRequest request) {
		Map<String, Object> campos 	= null;
		String path 				= "mantenimiento/tipocambio/tca_listado";;
		TblTipoCambio entidadEnBd 		= null;
		try{
			// Se actualizan solo los campos del formulario
			entidadEnBd = tipocambioDao.findOne(entidad.getCodigoTipoCambio());
			entidadEnBd.setValor(entidad.getValor());
			entidadEnBd.setObservacion(entidad.getObservacion());
			this.preEditar(entidadEnBd, request);
			boolean exitoso = super.guardar(entidadEnBd, model);
			logger.debug("[guardarEntidad] Guardado..." );
			if (exitoso){
				
				List<TblTipoCambio> entidades = tipocambioDao.buscarOneByFecha(entidadEnBd.getFecha());
				
				model.addAttribute("registros", entidades);
				campos = configurarCamposConsulta();
				model.addAttribute("contenido", campos);
				model.addAttribute("filtro", new Filtro());
			}else{
				path = "mantenimiento/tipocambio/tca_edicion";
				model.addAttribute("entidad", entidad);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
		
	}
	public void setFechaCriterio(Filtro filtro){
		if (filtro.getFechaInicio()== null || filtro.getFechaInicio().equals("")){
			filtro.setFechaInicio(UtilSGT.getDateStringFormat(UtilSGT.addDays(new Date(), -30)));
		}
		if (filtro.getFechaFin()==null || filtro.getFechaFin().equals("")){
			filtro.setFechaFin(UtilSGT.getDateStringFormat(UtilSGT.addDays(new Date(), 1)));
		}
	}
	/**
	 * Se encarga de la eliminacion logica del TipoCambio
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "tipocambio/eliminar/{id}", method = RequestMethod.GET)
	public String eliminarTipoCambio(@PathVariable Integer id, HttpServletRequest request, Model model) {
		TblTipoCambio entidad		= null;
		String path 				= null;
		Map<String, Object> campos 	= null;
		Filtro filtro 				= new Filtro();
		try{
			logger.debug("[eliminarTipoCambio] Inicio");
			entidad = tipocambioDao.findOne(id);
			entidad.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			this.preEditar(entidad, request);
			tipocambioDao.save(entidad);
			model.addAttribute("respuesta", "Eliminación exitosa");
			this.setFechaCriterio(filtro);
			this.listarTipoCambios(model, filtro);
			/*List<TblTipoCambio> entidades = tipocambioDao.listarAllActivos();
			logger.debug("[eliminarTipoCambio] entidades:"+entidades);
			model.addAttribute("registros", entidades);*/
			path = "mantenimiento/tipocambio/tca_listado";
			
			model.addAttribute("filtro", filtro);
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			logger.debug("[eliminarTipoCambio] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;
			campos		= null;
		}
		return path;
	}
	

	@Override
	public TblTipoCambio getNuevaEntidad() {
		return new TblTipoCambio();
	}
	
	/**
	 * Se encarga de configurar los campos del formulario de consulta
	 * @return
	 */
	private Map<String, Object> configurarCamposConsulta() {
		// El Map debe tener la estructura: String del label , Campo 
		Map<String,Object> campos = new LinkedHashMap<>();
		Campo campo = null;
		campo = new Campo("text", "fechaInicio", false);
		campos.put("Fecha Inicio", campo);
		campo = new Campo("text", "fechaFin", false);
		campos.put("Fecha Fin", campo);
		return campos;
	}
	
	
}
