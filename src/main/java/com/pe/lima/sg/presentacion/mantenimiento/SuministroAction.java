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
import com.pe.lima.sg.dao.mantenimiento.ISuministroDAO;
import com.pe.lima.sg.entity.mantenimiento.TblSuministro;
import com.pe.lima.sg.presentacion.BasePresentacion;
import com.pe.lima.sg.presentacion.Campo;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.ListaUtilAction;
import com.pe.lima.sg.presentacion.util.UtilSGT;

import lombok.extern.slf4j.Slf4j;

/**
 * Clase Bean que se encarga de la administracion de los suministros
 *
 * 			
 */
@Slf4j
@Controller
//@PreAuthorize("hasAuthority('CRUD')")
public class SuministroAction extends BasePresentacion<TblSuministro> {
	@Autowired
	private ISuministroDAO suministroDao;

	
	@Autowired
	private ListaUtilAction listaUtil;
	
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDAO getDao() {
		return suministroDao;
	}
	
	/**
	 * Se encarga de listar todos los suministroes
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/suministros", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path) {
		Map<String, Object> campos = null;
		Filtro filtro = null;
		try{
			log.debug("[traerRegistros] Inicio");
			path = "mantenimiento/suministro/sum_listado";
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			filtro = new Filtro();
			model.addAttribute("filtro", filtro);
			filtro.setEstadoUsuario("-1");
			this.listarSuministros(model, filtro);
			listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
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
	 * Se encarga de buscar la informacion del Suministro segun el filtro
	 * seleccionado
	 * 
	 * @param model
	 * @param suministroBean
	 * @return
	 */
	@RequestMapping(value = "/suministros/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, Filtro filtro, String path,HttpServletRequest request) {
		Map<String, Object> campos = null;
		path = "mantenimiento/suministro/sum_listado";
		try{
			log.debug("[traerRegistrosFiltrados] Inicio");
			this.listarSuministros(model, filtro);
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			model.addAttribute("filtro", filtro);
			listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
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
	/*** Listado de Suministros ***/
	private void listarSuministros(Model model, Filtro filtro){
		List<TblSuministro> entidades = new ArrayList<TblSuministro>();
		try{
			entidades = suministroDao.listarCriterios(filtro.getNumero(), filtro.getEstado());
			log.debug("[listarSuministroes] entidades:"+entidades);
			model.addAttribute("registros", entidades);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades = null;
		}
	}
	
	
	/**
	 * Se encarga de direccionar a la pantalla de edicion del Suministro
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "suministro/editar/{id}", method = RequestMethod.GET)
	public String editarSuministro(@PathVariable Integer id, Model model) {
		TblSuministro entidad 			= null;
		try{
			entidad = suministroDao.findOne(id);
			model.addAttribute("entidad", entidad);
			listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return "mantenimiento/suministro/sum_edicion";
	}
	
	
	/**
	 * Se encarga de direccionar a la pantalla de creacion del Suministro
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "suministro/nuevo", method = RequestMethod.GET)
	public String crearSuministro(Model model) {
		try{
			log.debug("[crearSuministro] Inicio");
			model.addAttribute("entidad", new TblSuministro());
			listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
			log.debug("[crearSuministro] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "mantenimiento/suministro/sum_nuevo";
	}
	
	@Override
	public void preGuardar(TblSuministro entidad, HttpServletRequest request) {
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
	public boolean validarNegocio(Model model,TblSuministro entidad, HttpServletRequest request){
		boolean exitoso = true;
		Integer total 	= null;
		try{
			//Validando la existencia del suministro
			total = suministroDao.countOneByNumero(entidad.getNumero());
			if (total > 0){
				exitoso = false;
				model.addAttribute("respuesta", "El Suministro existe, debe modificarlo para continuar...");
			}
			
		}catch(Exception e){
			exitoso = false;
		}finally{
			total = null;
		}
		return exitoso;
	}
	/**
	 * Se encarga de guardar la informacion del Suministro
	 * 
	 * @param suministroBean
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "suministro/nuevo/guardar", method = RequestMethod.POST)
	public String guardarEntidad(Model model, TblSuministro entidad, HttpServletRequest request, String path) {
		Map<String, Object> campos 	= null;
		path = "mantenimiento/suministro/sum_listado";
		try{
			log.debug("[guardarEntidad] Inicio" );
			if (this.validarNegocio(model, entidad, request)){
				log.debug("[guardarEntidad] Pre Guardar..." );
				this.preGuardar(entidad, request);
				boolean exitoso = super.guardar(entidad, model);
				log.debug("[guardarEntidad] Guardado..." );
				if (exitoso){
					List<TblSuministro> entidades = suministroDao.buscarOneByNumero(entidad.getNumero());
					model.addAttribute("registros", entidades);
					listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
					campos = configurarCamposConsulta();
					model.addAttribute("contenido", campos);
					model.addAttribute("filtro", new Filtro());
				}else{
					path = "mantenimiento/usuario/usu_nuevo";
					listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
					model.addAttribute("entidad", entidad);
					
				}
			}else{
				path = "mantenimiento/usuario/usu_nuevo";
				listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
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
	public void preEditar(TblSuministro entidad, HttpServletRequest request) {
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
	
	@RequestMapping(value = "suministro/nuevo/editar", method = RequestMethod.POST)
	public String editarEntidad(TblSuministro entidad, Model model, HttpServletRequest request) {
		Map<String, Object> campos 	= null;
		String path 				= "mantenimiento/suministro/sum_listado";;
		TblSuministro entidadEnBd 		= null;
		try{
			// Se actualizan solo los campos del formulario
			entidadEnBd = suministroDao.findOne(entidad.getCodigoSuministro());
			entidadEnBd.setEstadoSuministro(entidad.getEstadoSuministro());
			entidadEnBd.setObservacion(entidad.getObservacion());
			this.preEditar(entidadEnBd, request);
			boolean exitoso = super.guardar(entidadEnBd, model);
			log.debug("[guardarEntidad] Guardado..." );
			if (exitoso){
				
				List<TblSuministro> entidades = suministroDao.buscarOneByNumero(entidadEnBd.getNumero());
				
				model.addAttribute("registros", entidades);
				listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
				campos = configurarCamposConsulta();
				model.addAttribute("contenido", campos);
				model.addAttribute("filtro", new Filtro());
			}else{
				path = "mantenimiento/suministro/sum_edicion";
				listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
				model.addAttribute("entidad", entidad);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
		
	}
	
	/**
	 * Se encarga de la eliminacion logica del Suministro
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "suministro/eliminar/{id}", method = RequestMethod.GET)
	public String eliminarSuministro(@PathVariable Integer id, HttpServletRequest request, Model model) {
		TblSuministro entidad			= null;
		String path 				= null;
		Map<String, Object> campos 	= null;
		try{
			log.debug("[eliminarSuministro] Inicio");
			entidad = suministroDao.findOne(id);
			entidad.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			this.preEditar(entidad, request);
			suministroDao.save(entidad);
			model.addAttribute("respuesta", "Eliminación exitosa");
			List<TblSuministro> entidades = suministroDao.listarAllActivos();
			log.debug("[eliminarSuministro] entidades:"+entidades);
			model.addAttribute("registros", entidades);
			path = "mantenimiento/suministro/sum_listado";
			model.addAttribute("filtro", new Filtro());
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
			log.debug("[eliminarSuministro] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;
			campos		= null;
		}
		return path;
	}
	

	@Override
	public TblSuministro getNuevaEntidad() {
		return new TblSuministro();
	}
	
	/**
	 * Se encarga de configurar los campos del formulario de consulta
	 * @return
	 */
	private Map<String, Object> configurarCamposConsulta() {
		// El Map debe tener la estructura: String del label , Campo 
		Map<String,Object> campos = new LinkedHashMap<>();
		Campo campo = null;
		campo = new Campo("text", "numero", false);
		campos.put("Numero", campo);
		campo = new Campo("combo_estado", "estado", false);
		campos.put("Estado Suministro", campo);
		return campos;
	}
	
	
}
