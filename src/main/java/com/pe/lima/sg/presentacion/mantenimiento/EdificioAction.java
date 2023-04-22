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
import com.pe.lima.sg.dao.mantenimiento.IEdificioDAO;
import com.pe.lima.sg.entity.mantenimiento.TblEdificio;
import com.pe.lima.sg.presentacion.BasePresentacion;
import com.pe.lima.sg.presentacion.Campo;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.ListaUtilAction;
import com.pe.lima.sg.presentacion.util.UtilSGT;

/**
 * Clase Bean que se encarga de la administracion de los edificios
 *
 * 			
 */
@Controller
//@PreAuthorize("hasAuthority('CRUD')")
public class EdificioAction extends BasePresentacion<TblEdificio> {
	private static final Logger logger = LogManager.getLogger(EdificioAction.class);
	@Autowired
	private IEdificioDAO edificioDao;

	
	@Autowired
	private ListaUtilAction listaUtil;
	
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDAO getDao() {
		return edificioDao;
	}
	
	/**
	 * Se encarga de listar todos los edificioes
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/edificios", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path) {
		Map<String, Object> campos = null;
		Filtro filtro = null;
		try{
			logger.debug("[traerRegistros] Inicio");
			path = "mantenimiento/edificio/edi_listado";
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			filtro = new Filtro();
			model.addAttribute("filtro", filtro);
			this.listarEdificios(model, filtro);
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
	 * Se encarga de buscar la informacion del Edificio segun el filtro
	 * seleccionado
	 * 
	 * @param model
	 * @param edificioBean
	 * @return
	 */
	@RequestMapping(value = "/edificios/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, Filtro filtro, String path,HttpServletRequest request) {
		Map<String, Object> campos = null;
		path = "mantenimiento/edificio/edi_listado";
		try{
			logger.debug("[traerRegistrosFiltrados] Inicio");
			this.listarEdificios(model, filtro);
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			model.addAttribute("filtro", filtro);
			listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
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
	/*** Listado de Edificios ***/
	private void listarEdificios(Model model, Filtro filtro){
		List<TblEdificio> entidades = new ArrayList<TblEdificio>();
		try{
			entidades = edificioDao.listarCriterios(filtro.getNombre());
			logger.debug("[listarEdificioes] entidades:"+entidades);
			model.addAttribute("registros", entidades);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades = null;
		}
	}
	
	
	/**
	 * Se encarga de direccionar a la pantalla de edicion del Edificio
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "edificio/editar/{id}", method = RequestMethod.GET)
	public String editarEdificio(@PathVariable Integer id, Model model) {
		TblEdificio entidad 			= null;
		try{
			entidad = edificioDao.findOne(id);
			model.addAttribute("entidad", entidad);
			listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return "mantenimiento/edificio/edi_edicion";
	}
	
	
	/**
	 * Se encarga de direccionar a la pantalla de creacion del Edificio
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "edificio/nuevo", method = RequestMethod.GET)
	public String crearEdificio(Model model) {
		try{
			logger.debug("[crearEdificio] Inicio");
			model.addAttribute("entidad", new TblEdificio());
			listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
			logger.debug("[crearEdificio] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "mantenimiento/edificio/edi_nuevo";
	}
	
	@Override
	public void preGuardar(TblEdificio entidad, HttpServletRequest request) {
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
	public boolean validarNegocio(Model model,TblEdificio entidad, HttpServletRequest request){
		boolean exitoso = true;
		Integer total 	= null;
		try{
			//Validando la existencia del edificio
			total = edificioDao.countOneByNombre(entidad.getNombre());
			if (total > 0){
				exitoso = false;
				model.addAttribute("respuesta", "El Edificio existe, debe modificarlo para continuar...");
			}
			
		}catch(Exception e){
			exitoso = false;
		}finally{
			total = null;
		}
		return exitoso;
	}
	/**
	 * Se encarga de guardar la informacion del Edificio
	 * 
	 * @param edificioBean
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "edificio/nuevo/guardar", method = RequestMethod.POST)
	public String guardarEntidad(Model model, TblEdificio entidad, HttpServletRequest request, String path) {
		Map<String, Object> campos 	= null;
		path = "mantenimiento/edificio/edi_listado";
		try{
			logger.debug("[guardarEntidad] Inicio" );
			if (this.validarNegocio(model, entidad, request)){
				logger.debug("[guardarEntidad] Pre Guardar..." );
				this.preGuardar(entidad, request);
				boolean exitoso = super.guardar(entidad, model);
				logger.debug("[guardarEntidad] Guardado..." );
				if (exitoso){
					List<TblEdificio> entidades = edificioDao.buscarOneByNombre(entidad.getNombre());
					model.addAttribute("registros", entidades);
					listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
					campos = configurarCamposConsulta();
					model.addAttribute("contenido", campos);
					model.addAttribute("filtro", new Filtro());
				}else{
					path = "mantenimiento/edificio/edi_nuevo";
					listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
					model.addAttribute("entidad", entidad);
					
				}
			}else{
				path = "mantenimiento/edificio/edi_nuevo";
				listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
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
	public void preEditar(TblEdificio entidad, HttpServletRequest request) {
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
	
	@RequestMapping(value = "edificio/nuevo/editar", method = RequestMethod.POST)
	public String editarEntidad(TblEdificio entidad, Model model, HttpServletRequest request) {
		Map<String, Object> campos 	= null;
		String path 				= "mantenimiento/edificio/edi_listado";;
		TblEdificio entidadEnBd 		= null;
		try{
			// Se actualizan solo los campos del formulario
			entidadEnBd = edificioDao.findOne(entidad.getCodigoEdificio());
			entidadEnBd.setDireccion(entidad.getDireccion());
			entidadEnBd.setDescripcion(entidad.getDescripcion());
			this.preEditar(entidadEnBd, request);
			boolean exitoso = super.guardar(entidadEnBd, model);
			logger.debug("[guardarEntidad] Guardado..." );
			if (exitoso){
				
				List<TblEdificio> entidades = edificioDao.buscarOneByNombre(entidadEnBd.getNombre());
				
				model.addAttribute("registros", entidades);
				listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
				campos = configurarCamposConsulta();
				model.addAttribute("contenido", campos);
				model.addAttribute("filtro", new Filtro());
			}else{
				path = "mantenimiento/edificio/edi_edicion";
				listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
				model.addAttribute("entidad", entidad);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
		
	}
	
	/**
	 * Se encarga de la eliminacion logica del Edificio
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "edificio/eliminar/{id}", method = RequestMethod.GET)
	public String eliminarEdificio(@PathVariable Integer id, HttpServletRequest request, Model model) {
		TblEdificio entidad			= null;
		String path 				= null;
		Map<String, Object> campos 	= null;
		try{
			logger.debug("[eliminarEdificio] Inicio");
			entidad = edificioDao.findOne(id);
			entidad.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			this.preEditar(entidad, request);
			edificioDao.save(entidad);
			model.addAttribute("respuesta", "Eliminación exitosa");
			List<TblEdificio> entidades = edificioDao.listarAllActivos();
			logger.debug("[eliminarEdificio] entidades:"+entidades);
			model.addAttribute("registros", entidades);
			path = "mantenimiento/edificio/edi_listado";
			model.addAttribute("filtro", new Filtro());
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
			logger.debug("[eliminarEdificio] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;
			campos		= null;
		}
		return path;
	}
	

	@Override
	public TblEdificio getNuevaEntidad() {
		return new TblEdificio();
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
		return campos;
	}
	
	
}
