package com.pe.lima.sg.presentacion.mantenimiento;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pe.lima.sg.dao.BaseDAO;
import com.pe.lima.sg.dao.mantenimiento.IParametroDAO;
import com.pe.lima.sg.entity.mantenimiento.TblParametro;
import com.pe.lima.sg.presentacion.BasePresentacion;
import com.pe.lima.sg.presentacion.Campo;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.UtilSGT;

import lombok.extern.slf4j.Slf4j;

/**
 * Clase Bean que se encarga de la administracion de los parametros
 *
 * 			
 */
@Slf4j
@Controller
//@PreAuthorize("hasAuthority('CRUD')")
public class ParametroAction extends BasePresentacion<TblParametro> {
	@Autowired
	private IParametroDAO parametroDao;

	
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDAO getDao() {
		return parametroDao;
	}
	
	/**
	 * Se encarga de listar todos los parametros
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/parametros", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path) {
		Map<String, Object> campos = null;
		Filtro filtro = null;
		try{
			log.debug("[traerRegistros] Inicio");
			path = "mantenimiento/parametro/par_listado";
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			filtro = new Filtro();
			model.addAttribute("filtro", filtro);
			this.listarParametros(model, filtro);
			log.debug("[traerRegistros] Fin");
		}catch(Exception e){
			log.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			campos		= null;
			filtro = null;
		}
		
		return path;
	}
	/**
	 * Se encarga de buscar la informacion del Parametro segun el filtro
	 * seleccionado
	 * 
	 * @param model
	 * @param parametroBean
	 * @return
	 */
	@RequestMapping(value = "/parametros/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, Filtro filtro, String path,HttpServletRequest request) {
		Map<String, Object> campos = null;
		path = "mantenimiento/parametro/par_listado";
		try{
			log.debug("[traerRegistrosFiltrados] Inicio");
			this.listarParametros(model, filtro);
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			model.addAttribute("filtro", filtro);
			log.debug("[traerRegistrosFiltrados] Fin");
		}catch(Exception e){
			log.debug("[traerRegistrosFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			campos		= null;
		}
		log.debug("[traerRegistrosFiltrados] Fin");
		return path;
	}
	/*** Listado de Parametros ***/
	private void listarParametros(Model model, Filtro filtro){
		List<TblParametro> entidades = new ArrayList<TblParametro>();
		try{
			entidades = parametroDao.listarCriterios(filtro.getNombre(), filtro.getDato());
			log.debug("[listarParametroes] entidades:"+entidades);
			model.addAttribute("registros", entidades);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades = null;
		}
	}
	
	
	/**
	 * Se encarga de direccionar a la pantalla de edicion del Parametro
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "parametro/editar/{id}", method = RequestMethod.GET)
	public String editarParametro(@PathVariable Integer id, Model model) {
		TblParametro entidad 			= null;
		try{
			entidad = parametroDao.findOne(id);
			model.addAttribute("entidad", entidad);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return "mantenimiento/parametro/par_edicion";
	}
	
	
	/**
	 * Se encarga de direccionar a la pantalla de creacion del Parametro
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "parametro/nuevo", method = RequestMethod.GET)
	public String crearParametro(Model model) {
		try{
			log.debug("[crearParametro] Inicio");
			model.addAttribute("entidad", new TblParametro());
			log.debug("[crearParametro] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "mantenimiento/parametro/par_nuevo";
	}
	
	@Override
	public void preGuardar(TblParametro entidad, HttpServletRequest request) {
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
	/** Validamos logica del negocio**/
	@Override
	public boolean validarNegocio(Model model,TblParametro entidad, HttpServletRequest request){
		boolean exitoso = true;
		Integer total 	= null;
		try{
			//Validando la existencia del parametro
			total = parametroDao.countOneByNombre(entidad.getNombre());
			if (total > 0){
				exitoso = false;
				model.addAttribute("respuesta", "El Parametro existe, debe modificarlo para continuar...");
			}
			
		}catch(Exception e){
			exitoso = false;
		}finally{
			total = null;
		}
		return exitoso;
	}
	/**
	 * Se encarga de guardar la informacion del Parametro
	 * 
	 * @param parametroBean
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "parametro/nuevo/guardar", method = RequestMethod.POST)
	public String guardarEntidad(Model model, TblParametro entidad, HttpServletRequest request, String path) {
		Map<String, Object> campos 	= null;
		path = "mantenimiento/parametro/par_listado";
		try{
			log.debug("[guardarEntidad] Inicio" );
			if (this.validarNegocio(model, entidad, request)){
				log.debug("[guardarEntidad] Pre Guardar..." );
				this.preGuardar(entidad, request);
				boolean exitoso = super.guardar(entidad, model);
				log.debug("[guardarEntidad] Guardado..." );
				if (exitoso){
					List<TblParametro> entidades = parametroDao.buscarOneByNombre(entidad.getNombre());
					model.addAttribute("registros", entidades);
					campos = configurarCamposConsulta();
					model.addAttribute("contenido", campos);
					model.addAttribute("filtro", new Filtro());
				}else{
					path = "mantenimiento/parametro/par_nuevo";
					model.addAttribute("entidad", entidad);
					
				}
			}else{
				path = "mantenimiento/parametro/par_nuevo";
				model.addAttribute("entidad", entidad);
			}
			
			log.debug("[guardarEntidad] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			campos 			= null;
		}
		return path;
		
	}
	
	@Override
	public void preEditar(TblParametro entidad, HttpServletRequest request) {
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
	
	@RequestMapping(value = "parametro/nuevo/editar", method = RequestMethod.POST)
	public String editarEntidad(TblParametro entidad, Model model, HttpServletRequest request) {
		Map<String, Object> campos 	= null;
		String path 				= "mantenimiento/parametro/par_listado";;
		TblParametro entidadEnBd 		= null;
		try{
			// Se actualizan solo los campos del formulario
			entidadEnBd = parametroDao.findOne(entidad.getCodigoParametro());
			entidadEnBd.setDato(entidad.getDato());
			entidadEnBd.setValor(entidad.getValor());
			entidadEnBd.setCantidad(entidad.getCantidad());
			entidadEnBd.setDescripcion(entidad.getDescripcion());
			this.preEditar(entidadEnBd, request);
			boolean exitoso = super.guardar(entidadEnBd, model);
			log.debug("[guardarEntidad] Guardado..." );
			if (exitoso){
				
				List<TblParametro> entidades = parametroDao.buscarOneByNombre(entidadEnBd.getNombre());
				
				model.addAttribute("registros", entidades);
				campos = configurarCamposConsulta();
				model.addAttribute("contenido", campos);
				model.addAttribute("filtro", new Filtro());
			}else{
				path = "mantenimiento/parametro/par_edicion";
				model.addAttribute("entidad", entidad);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
		
	}
	
	/**
	 * Se encarga de la eliminacion logica del Parametro
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "parametro/eliminar/{id}", method = RequestMethod.GET)
	public String eliminarParametro(@PathVariable Integer id, HttpServletRequest request, Model model) {
		TblParametro entidad			= null;
		String path 				= null;
		Map<String, Object> campos 	= null;
		try{
			log.debug("[eliminarParametro] Inicio");
			entidad = parametroDao.findOne(id);
			entidad.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			this.preEditar(entidad, request);
			parametroDao.save(entidad);
			model.addAttribute("respuesta", "Eliminación exitosa");
			List<TblParametro> entidades = parametroDao.listarAllActivos();
			log.debug("[eliminarParametro] entidades:"+entidades);
			model.addAttribute("registros", entidades);
			path = "mantenimiento/parametro/par_listado";
			model.addAttribute("filtro", new Filtro());
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			log.debug("[eliminarParametro] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;
			campos		= null;
		}
		return path;
	}
	

	@Override
	public TblParametro getNuevaEntidad() {
		return new TblParametro();
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
		campo = new Campo("text", "dato", false);
		campos.put("Dato", campo);
		return campos;
	}
	
	
}
