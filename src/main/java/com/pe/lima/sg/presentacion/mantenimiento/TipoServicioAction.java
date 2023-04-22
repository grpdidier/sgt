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
import com.pe.lima.sg.dao.mantenimiento.ITipoServicioDAO;
import com.pe.lima.sg.entity.mantenimiento.TblTipoServicio;
import com.pe.lima.sg.presentacion.BasePresentacion;
import com.pe.lima.sg.presentacion.Campo;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.ListaUtilAction;
import com.pe.lima.sg.presentacion.util.UtilSGT;

/**
 * Clase Bean que se encarga de la administracion de los tiposervicios
 *
 * 			
 */
@Controller
//@PreAuthorize("hasAuthority('CRUD')")
public class TipoServicioAction extends BasePresentacion<TblTipoServicio> {
	private static final Logger logger = LogManager.getLogger(TipoServicioAction.class);
	@Autowired
	private ITipoServicioDAO tiposervicioDao;

	@Autowired
	private ListaUtilAction listaUtil;
	
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDAO getDao() {
		return tiposervicioDao;
	}
	
	/**
	 * Se encarga de listar todos los tiposervicios
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tiposervicios", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path) {
		Map<String, Object> campos = null;
		Filtro filtro = null;
		try{
			logger.debug("[traerRegistros] Inicio");
			path = "mantenimiento/tiposervicio/tse_listado";
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			filtro = new Filtro();
			model.addAttribute("filtro", filtro);
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_RUBRO);
			this.listarTipoServicios(model, filtro);
			logger.debug("[traerRegistros] Fin");
		}catch(Exception e){
			logger.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			campos		= null;
		}
		
		return path;
	}
	/**
	 * Se encarga de buscar la informacion del TipoServicio segun el filtro
	 * seleccionado
	 * 
	 * @param model
	 * @param tiposervicioBean
	 * @return
	 */
	@RequestMapping(value = "/tiposervicios/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, Filtro filtro, String path,HttpServletRequest request) {
		Map<String, Object> campos = null;
		path = "mantenimiento/tiposervicio/tse_listado";
		try{
			logger.debug("[traerRegistrosFiltrados] Inicio");
			this.listarTipoServicios(model, filtro);
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_RUBRO);
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
	/*** Listado de TipoServicios ***/
	private void listarTipoServicios(Model model, Filtro filtro){
		List<TblTipoServicio> entidades = new ArrayList<TblTipoServicio>();
		try{
			entidades = tiposervicioDao.listarCriterios(filtro.getNombre());
			logger.debug("[listarTipoServicioes] entidades:"+entidades);
			model.addAttribute("registros", entidades);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades = null;
		}
	}
	
	
	/**
	 * Se encarga de direccionar a la pantalla de edicion del TipoServicio
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "tiposervicio/editar/{id}", method = RequestMethod.GET)
	public String editarTipoServicio(@PathVariable Integer id, Model model) {
		TblTipoServicio entidad 			= null;
		try{
			entidad = tiposervicioDao.findOne(id);
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_RUBRO);
			model.addAttribute("entidad", entidad);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return "mantenimiento/tiposervicio/tse_edicion";
	}
	
	
	/**
	 * Se encarga de direccionar a la pantalla de creacion del TipoServicio
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "tiposervicio/nuevo", method = RequestMethod.GET)
	public String crearTipoServicio(Model model) {
		try{
			logger.debug("[crearTipoServicio] Inicio");
			model.addAttribute("entidad", new TblTipoServicio());
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_RUBRO);
			logger.debug("[crearTipoServicio] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "mantenimiento/tiposervicio/tse_nuevo";
	}
	
	@Override
	public void preGuardar(TblTipoServicio entidad, HttpServletRequest request) {
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
	public boolean validarNegocio(Model model,TblTipoServicio entidad, HttpServletRequest request){
		boolean exitoso = true;
		Integer total 	= null;
		try{
			//Validando la existencia del tiposervicio
			total = tiposervicioDao.countOneByNombre(entidad.getNombre());
			if (total > 0){
				exitoso = false;
				model.addAttribute("respuesta", "El Tipo de Servicio existe, debe modificarlo para continuar...");
			}
			
		}catch(Exception e){
			exitoso = false;
		}finally{
			total = null;
		}
		return exitoso;
	}
	/**
	 * Se encarga de guardar la informacion del TipoServicio
	 * 
	 * @param tiposervicioBean
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "tiposervicio/nuevo/guardar", method = RequestMethod.POST)
	public String guardarEntidad(Model model, TblTipoServicio entidad, HttpServletRequest request, String path) {
		Map<String, Object> campos 	= null;
		path = "mantenimiento/tiposervicio/tse_listado";
		try{
			logger.debug("[guardarEntidad] Inicio" );
			if (this.validarNegocio(model, entidad, request)){
				logger.debug("[guardarEntidad] Pre Guardar..." );
				this.preGuardar(entidad, request);
				boolean exitoso = super.guardar(entidad, model);
				logger.debug("[guardarEntidad] Guardado..." );
				if (exitoso){
					List<TblTipoServicio> entidades = tiposervicioDao.buscarOneByNombre(entidad.getNombre());
					model.addAttribute("registros", entidades);
					campos = configurarCamposConsulta();
					model.addAttribute("contenido", campos);
					model.addAttribute("filtro", new Filtro());
				}else{
					path = "mantenimiento/tiposervicio/tse_nuevo";
					model.addAttribute("entidad", entidad);
					
				}
			}else{
				path = "mantenimiento/tiposervicio/tse_nuevo";
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
	public void preEditar(TblTipoServicio entidad, HttpServletRequest request) {
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
	
	@RequestMapping(value = "tiposervicio/nuevo/editar", method = RequestMethod.POST)
	public String editarEntidad(TblTipoServicio entidad, Model model, HttpServletRequest request) {
		Map<String, Object> campos 	= null;
		String path 				= "mantenimiento/tiposervicio/tse_listado";;
		TblTipoServicio entidadEnBd 		= null;
		try{
			// Se actualizan solo los campos del formulario
			entidadEnBd = tiposervicioDao.findOne(entidad.getCodigoTipoServicio());
			entidadEnBd.setRubro(entidad.getRubro());
			entidadEnBd.setDescripcion(entidad.getDescripcion());
			this.preEditar(entidadEnBd, request);
			boolean exitoso = super.guardar(entidadEnBd, model);
			logger.debug("[guardarEntidad] Guardado..." );
			if (exitoso){
				
				List<TblTipoServicio> entidades = tiposervicioDao.buscarOneByNombre(entidadEnBd.getNombre());
				
				model.addAttribute("registros", entidades);
				campos = configurarCamposConsulta();
				model.addAttribute("contenido", campos);
				model.addAttribute("filtro", new Filtro());
			}else{
				path = "mantenimiento/tiposervicio/tse_edicion";
				model.addAttribute("entidad", entidad);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
		
	}
	
	/**
	 * Se encarga de la eliminacion logica del TipoServicio
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "tiposervicio/eliminar/{id}", method = RequestMethod.GET)
	public String eliminarTipoServicio(@PathVariable Integer id, HttpServletRequest request, Model model) {
		TblTipoServicio entidad			= null;
		String path 				= null;
		Map<String, Object> campos 	= null;
		try{
			logger.debug("[eliminarTipoServicio] Inicio");
			entidad = tiposervicioDao.findOne(id);
			entidad.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			this.preEditar(entidad, request);
			tiposervicioDao.save(entidad);
			model.addAttribute("respuesta", "Eliminación exitosa");
			List<TblTipoServicio> entidades = tiposervicioDao.listarAllActivos();
			logger.debug("[eliminarTipoServicio] entidades:"+entidades);
			model.addAttribute("registros", entidades);
			path = "mantenimiento/tiposervicio/tse_listado";
			model.addAttribute("filtro", new Filtro());
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			logger.debug("[eliminarTipoServicio] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;
			campos		= null;
		}
		return path;
	}
	

	@Override
	public TblTipoServicio getNuevaEntidad() {
		return new TblTipoServicio();
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
