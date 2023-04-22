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
import com.pe.lima.sg.dao.mantenimiento.IConceptoDAO;
import com.pe.lima.sg.entity.mantenimiento.TblConcepto;
import com.pe.lima.sg.presentacion.BasePresentacion;
import com.pe.lima.sg.presentacion.Campo;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.ListaUtilAction;
import com.pe.lima.sg.presentacion.util.UtilSGT;

/**
 * Clase Bean que se encarga de la administracion de los conceptos
 *
 * 			
 */
@Controller
//@PreAuthorize("hasAuthority('CRUD')")
public class ConceptoAction extends BasePresentacion<TblConcepto> {
	private static final Logger logger = LogManager.getLogger(ConceptoAction.class);
	@Autowired
	private IConceptoDAO conceptoDao;

	
	@Autowired
	private ListaUtilAction listaUtil;
	
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDAO getDao() {
		return conceptoDao;
	}
	
	/**
	 * Se encarga de listar todos los conceptos
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/conceptos", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path) {
		Map<String, Object> campos = null;
		Filtro filtro = null;
		try{
			logger.debug("[traerRegistros] Inicio");
			path = "mantenimiento/concepto/con_listado";
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			filtro = new Filtro();
			model.addAttribute("filtro", filtro);
			filtro.setEstadoUsuario("-1");
			this.listarConceptos(model, filtro);
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_CONCEPTO);
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
	 * Se encarga de buscar la informacion del Concepto segun el filtro
	 * seleccionado
	 * 
	 * @param model
	 * @param conceptoBean
	 * @return
	 */
	@RequestMapping(value = "/conceptos/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, Filtro filtro, String path,HttpServletRequest request) {
		Map<String, Object> campos = null;
		path = "mantenimiento/concepto/con_listado";
		try{
			logger.debug("[traerRegistrosFiltrados] Inicio");
			this.listarConceptos(model, filtro);
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			model.addAttribute("filtro", filtro);
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_CONCEPTO);
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
	/*** Listado de Conceptos ***/
	private void listarConceptos(Model model, Filtro filtro){
		List<TblConcepto> entidades = new ArrayList<TblConcepto>();
		try{
			entidades = conceptoDao.listarCriterios(filtro.getNombre(), filtro.getTipo());
			logger.debug("[listarConceptoes] entidades:"+entidades);
			model.addAttribute("registros", entidades);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades = null;
		}
	}
	
	
	/**
	 * Se encarga de direccionar a la pantalla de edicion del Concepto
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "concepto/editar/{id}", method = RequestMethod.GET)
	public String editarConcepto(@PathVariable Integer id, Model model) {
		TblConcepto entidad 			= null;
		try{
			entidad = conceptoDao.findOne(id);
			model.addAttribute("entidad", entidad);
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_CONCEPTO);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return "mantenimiento/concepto/con_edicion";
	}
	
	
	/**
	 * Se encarga de direccionar a la pantalla de creacion del Concepto
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "concepto/nuevo", method = RequestMethod.GET)
	public String crearConcepto(Model model) {
		try{
			logger.debug("[crearConcepto] Inicio");
			model.addAttribute("entidad", new TblConcepto());
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_CONCEPTO);
			logger.debug("[crearConcepto] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "mantenimiento/concepto/con_nuevo";
	}
	
	@Override
	public void preGuardar(TblConcepto entidad, HttpServletRequest request) {
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
	public boolean validarNegocio(Model model,TblConcepto entidad, HttpServletRequest request){
		boolean exitoso = true;
		Integer total 	= null;
		try{
			//Validando la existencia del concepto
			total = conceptoDao.countOneByNombre(entidad.getNombre());
			if (total > 0){
				exitoso = false;
				model.addAttribute("respuesta", "El Concepto existe, debe modificarlo para continuar...");
			}
			
		}catch(Exception e){
			exitoso = false;
		}finally{
			total = null;
		}
		return exitoso;
	}
	/**
	 * Se encarga de guardar la informacion del Concepto
	 * 
	 * @param conceptoBean
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "concepto/nuevo/guardar", method = RequestMethod.POST)
	public String guardarEntidad(Model model, TblConcepto entidad, HttpServletRequest request, String path) {
		Map<String, Object> campos 	= null;
		path = "mantenimiento/concepto/con_listado";
		try{
			logger.debug("[guardarEntidad] Inicio" );
			if (this.validarNegocio(model, entidad, request)){
				logger.debug("[guardarEntidad] Pre Guardar..." );
				this.preGuardar(entidad, request);
				boolean exitoso = super.guardar(entidad, model);
				logger.debug("[guardarEntidad] Guardado..." );
				if (exitoso){
					List<TblConcepto> entidades = conceptoDao.buscarOneByNombre(entidad.getNombre());
					model.addAttribute("registros", entidades);
					listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_CONCEPTO);
					campos = configurarCamposConsulta();
					model.addAttribute("contenido", campos);
					model.addAttribute("filtro", new Filtro());
				}else{
					path = "mantenimiento/concepto/con_nuevo";
					listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_CONCEPTO);
					model.addAttribute("entidad", entidad);
					
				}
			}else{
				path = "mantenimiento/concepto/con_nuevo";
				listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_CONCEPTO);
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
	public void preEditar(TblConcepto entidad, HttpServletRequest request) {
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
	
	@RequestMapping(value = "concepto/nuevo/editar", method = RequestMethod.POST)
	public String editarEntidad(TblConcepto entidad, Model model, HttpServletRequest request) {
		Map<String, Object> campos 	= null;
		String path 				= "mantenimiento/concepto/con_listado";;
		TblConcepto entidadEnBd 		= null;
		try{
			// Se actualizan solo los campos del formulario
			entidadEnBd = conceptoDao.findOne(entidad.getCodigoConcepto());
			entidadEnBd.setTipo(entidad.getTipo());
			entidadEnBd.setDescripcion(entidad.getDescripcion());
			this.preEditar(entidadEnBd, request);
			boolean exitoso = super.guardar(entidadEnBd, model);
			logger.debug("[guardarEntidad] Guardado..." );
			if (exitoso){
				
				List<TblConcepto> entidades = conceptoDao.buscarOneByNombre(entidadEnBd.getNombre());
				
				model.addAttribute("registros", entidades);
				listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_CONCEPTO);
				campos = configurarCamposConsulta();
				model.addAttribute("contenido", campos);
				model.addAttribute("filtro", new Filtro());
			}else{
				path = "mantenimiento/concepto/con_edicion";
				listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_CONCEPTO);
				model.addAttribute("entidad", entidad);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
		
	}
	
	/**
	 * Se encarga de la eliminacion logica del Concepto
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "concepto/eliminar/{id}", method = RequestMethod.GET)
	public String eliminarConcepto(@PathVariable Integer id, HttpServletRequest request, Model model) {
		TblConcepto entidad			= null;
		String path 				= null;
		Map<String, Object> campos 	= null;
		try{
			logger.debug("[eliminarConcepto] Inicio");
			entidad = conceptoDao.findOne(id);
			entidad.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			this.preEditar(entidad, request);
			conceptoDao.save(entidad);
			model.addAttribute("respuesta", "Eliminación exitosa");
			List<TblConcepto> entidades = conceptoDao.listarAllActivos();
			logger.debug("[eliminarConcepto] entidades:"+entidades);
			model.addAttribute("registros", entidades);
			path = "mantenimiento/concepto/con_listado";
			model.addAttribute("filtro", new Filtro());
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_CONCEPTO);
			logger.debug("[eliminarConcepto] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;
			campos		= null;
		}
		return path;
	}
	

	@Override
	public TblConcepto getNuevaEntidad() {
		return new TblConcepto();
	}
	
	/**
	 * Se encarga de configurar los campos del formulario de consulta
	 * @return
	 */
	private Map<String, Object> configurarCamposConsulta() {
		// El Map debe tener la estructura: String del label , Campo 
		Map<String,Object> campos = new LinkedHashMap<>();
		Campo campo = null;
		campo = new Campo("text", "nombre", false);
		campos.put("Nombre", campo);
		campo = new Campo("combo_tipo_concepto", "tipo", false);
		campos.put("Tipo Concepto", campo);
		return campos;
	}
	
	
}
