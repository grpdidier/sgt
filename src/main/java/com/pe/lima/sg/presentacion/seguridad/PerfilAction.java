package com.pe.lima.sg.presentacion.seguridad;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pe.lima.sg.dao.BaseDAO;
import com.pe.lima.sg.dao.seguridad.IOpcionDAO;
import com.pe.lima.sg.dao.seguridad.IPerfilDAO;
import com.pe.lima.sg.dao.seguridad.IPerfilOpcionDAO;
//import com.pe.lima.sg.dao.seguridad.IPerfilOpcionDAO;
import com.pe.lima.sg.entity.seguridad.TblOpcion;
import com.pe.lima.sg.entity.seguridad.TblPerfil;
import com.pe.lima.sg.entity.seguridad.TblPerfilOpcion;
import com.pe.lima.sg.entity.seguridad.TblPerfilOpcionId;
import com.pe.lima.sg.presentacion.BasePresentacion;
import com.pe.lima.sg.presentacion.Campo;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.ListaUtilAction;
import com.pe.lima.sg.presentacion.util.UtilSGT;

import lombok.extern.slf4j.Slf4j;

/**
 * Clase Bean que se encarga de la administracion de los perfiles
 *
 * 			
 */
@Slf4j
@Controller
//@PreAuthorize("hasAuthority('CRUD')")
public class PerfilAction extends BasePresentacion<TblPerfil> {
	@Autowired
	private IPerfilDAO perfilDao;
	
	@Autowired
	private IOpcionDAO opcionDao;
	
	@Autowired
	private IPerfilOpcionDAO perfilOpcionDao;
	
	//@Autowired
	//private RolDao rol;
	
	/*@Autowired
	private IndexAction index;*/
	
	@Autowired
	private ListaUtilAction listaUtil;
	
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDAO getDao() {
		return perfilDao;
	}
	
	/**
	 * Se encarga de listar todos los perfiles
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/perfiles", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path) {
		Map<String, Object> campos = null;
		Filtro filtro = null;
		try{
			log.debug("[traerRegistros] Inicio");
			path = "seguridad/perfil/per_listado";
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			filtro = new Filtro();
			model.addAttribute("filtro", filtro);
			filtro.setEstadoUsuario("-1");
			this.listarPerfiles(model, filtro);
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
	 * Se encarga de buscar la informacion del Perfil segun el filtro
	 * seleccionado
	 * 
	 * @param model
	 * @param perfilBean
	 * @return
	 */
	@RequestMapping(value = "/perfiles/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, Filtro filtro, String path,HttpServletRequest request) {
		Map<String, Object> campos = null;
		path = "seguridad/perfil/per_listado";
		try{
			log.debug("[traerRegistrosFiltrados] Inicio");
			this.listarPerfiles(model, filtro);
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
	/*** Listado de Usuarios ***/
	private void listarPerfiles(Model model, Filtro filtro){
		List<TblPerfil> entidades = new ArrayList<TblPerfil>();
		try{
			entidades = perfilDao.listarCriterios(filtro.getNombre(), filtro.getEstado());
			log.debug("[listarPerfiles] entidades:"+entidades);
			model.addAttribute("registros", entidades);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades = null;
		}
	}
	/**
	 * Se encarga de direccionar a la pantalla de configuracion de Opciones por
	 * Perfil
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	/*@RequestMapping(value = "perfil/opcion/{id}", method = RequestMethod.GET)
	public String irConfigurarOpcion(@PathVariable Integer id, Model model, HttpSession session) {
		TblPerfil entidad = perfilDao.findOne(id);
		// Consulta todos las opciones
		List<TblOpcion> opciones = opcion.findAll();
		// Consulta configuracion existente
		Set<TblPerfilOpcion> entidadesExistentes = entidad.getTblPerfilOpcions();
		if (entidadesExistentes != null && !entidadesExistentes.isEmpty()) {
			// Si existen registros marco los bean como seleccionado
			for (TblPerfilOpcion segTblOpcxper : entidadesExistentes) {
				for (TblOpcion segTblOpcion : opciones) {
					if (segTblOpcxper.getId().getCodigoOpcion()==(segTblOpcion.getCodigoOpcion()) ) {
						segTblOpcion.setSeleccionado(true);
						break;
					}
				}
			}
		}
		// Construye el arbol del menu
		List<TblOpcion> entidadesOrdenadas = new ArrayList<TblOpcion>();
		for (Integer idOpcion : index.obtenerArbol(opciones)) {
			for (TblOpcion segTblOpcion : opciones) {
				if (idOpcion.equals(segTblOpcion.getCodigoOpcion())) {
					entidadesOrdenadas.add(segTblOpcion);
					break;
				}
			}
		}
		
		model.addAttribute("perfil", entidad);
		model.addAttribute("opcionesPerfiles", entidadesOrdenadas);
		session.setAttribute("opcionesPerfiles", entidadesOrdenadas);
		Map<String, Object> campos = configurarCamposOpcion(entidad);
		model.addAttribute("contenido", campos);
		return "seguridad/perfil/per_opcion";
	}*/
	
	/**
	 * Se encarga de direccionar a la pantalla de configuracion de Privilegios por
	 * Perfil
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	/*@RequestMapping(value = "perfil/privilegio/{id}", method = RequestMethod.GET)
	public String irConfigurarPrivilegios(@PathVariable BigDecimal id, Model model) {
		TblPerfil entidad = perfilDao.findOne(id);
		// Consulta todos los roles
		List<SegTblRol> roles = rol.findRoles();
		// Consulta configuracion existente
		Set<SegTblRol> rolesExistentes = entidad.getSegTblRols();
		if (rolesExistentes != null && !rolesExistentes.isEmpty()) {
			// Si existen registros marco los bean como seleccionado
			for (SegTblRol segTblRolExistente : rolesExistentes) {
				for (SegTblRol segTblRol : roles) {
					if (segTblRolExistente.getCodigo().equals(segTblRol.getCodigo())) {
						segTblRol.setSeleccionado(true);
						break;
					}
				}
			}
		}
		
		model.addAttribute("perfil", entidad);
		model.addAttribute("roles", roles);
		Map<String, Object> campos = configurarCamposOpcion(entidad);
		model.addAttribute("contenido", campos);
		return "seguridad/perfil/per_privilegio";
	}*/
	
	/**
	 * Se encarga de direccionar a la pantalla de edicion del Perfil
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "perfil/editar/{id}", method = RequestMethod.GET)
	public String editarPerfil(@PathVariable Integer id, Model model) {
		TblPerfil entidad 			= null;
		try{
			entidad = perfilDao.findOne(id);
			model.addAttribute("entidad", entidad);
			listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return "seguridad/perfil/per_edicion";
	}
	
	
	/**
	 * Se encarga de direccionar a la pantalla de creacion del Perfil
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "perfil/nuevo", method = RequestMethod.GET)
	public String crearPerfil(Model model) {
		try{
			log.debug("[crearPerfil] Inicio");
			model.addAttribute("entidad", new TblPerfil());
			listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
			log.debug("[crearPerfil] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "seguridad/perfil/per_nuevo";
	}
	
	@Override
	public void preGuardar(TblPerfil entidad, HttpServletRequest request) {
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
	public boolean validarNegocio(Model model,TblPerfil entidad, HttpServletRequest request){
		boolean exitoso = true;
		Integer total 	= null;
		try{
			//Validando la existencia del perfil
			total = perfilDao.countOneByLogin(entidad.getNombre());
			if (total > 0){
				exitoso = false;
				model.addAttribute("respuesta", "El Perfil existe, debe modificarlo para continuar...");
			}
			
		}catch(Exception e){
			exitoso = false;
		}finally{
			total = null;
		}
		return exitoso;
	}
	/**
	 * Se encarga de guardar la informacion del Perfil
	 * 
	 * @param perfilBean
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "perfil/nuevo/guardar", method = RequestMethod.POST)
	public String guardarEntidad(Model model, TblPerfil entidad, HttpServletRequest request, String path) {
		Map<String, Object> campos 	= null;
		path = "seguridad/perfil/per_listado";
		try{
			log.debug("[guardarEntidad] Inicio" );
			if (this.validarNegocio(model, entidad, request)){
				log.debug("[guardarEntidad] Pre Guardar..." );
				this.preGuardar(entidad, request);
				boolean exitoso = super.guardar(entidad, model);
				log.debug("[guardarEntidad] Guardado..." );
				if (exitoso){
					List<TblPerfil> entidades = perfilDao.buscarOneByNombre(entidad.getNombre());
					model.addAttribute("registros", entidades);
					listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
					campos = configurarCamposConsulta();
					model.addAttribute("contenido", campos);
					model.addAttribute("filtro", new Filtro());
				}else{
					path = "seguridad/perfil/per_nuevo";
					listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
					model.addAttribute("entidad", entidad);
					
				}
			}else{
				path = "seguridad/perfil/per_nuevo";
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
	public void preEditar(TblPerfil entidad, HttpServletRequest request) {
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
	
	@RequestMapping(value = "perfil/nuevo/editar", method = RequestMethod.POST)
	public String editarEntidad(TblPerfil entidad, Model model, HttpServletRequest request) {
		Map<String, Object> campos 	= null;
		String path 				= "seguridad/perfil/per_listado";;
		TblPerfil entidadEnBd 		= null;
		try{
			// Se actualizan solo los campos del formulario
			entidadEnBd = perfilDao.findOne(entidad.getCodigoPerfil());
			entidadEnBd.setEstadoPerfil(entidad.getEstadoPerfil());
			entidadEnBd.setDescripcion(entidad.getDescripcion());
			this.preEditar(entidadEnBd, request);
			boolean exitoso = super.guardar(entidadEnBd, model);
			log.debug("[guardarEntidad] Guardado..." );
			if (exitoso){
				
				List<TblPerfil> entidades = perfilDao.buscarOneByNombre(entidadEnBd.getNombre());
				
				model.addAttribute("registros", entidades);
				listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
				campos = configurarCamposConsulta();
				model.addAttribute("contenido", campos);
				model.addAttribute("filtro", new Filtro());
			}else{
				path = "seguridad/perfil/per_edicion";
				listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
				model.addAttribute("entidad", entidad);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
		
	}
	
	/**
	 * Se encarga de la eliminacion logica del Perfil
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "perfil/eliminar/{id}", method = RequestMethod.GET)
	public String eliminarPerfil(@PathVariable Integer id, HttpServletRequest request, Model model) {
		TblPerfil entidad			= null;
		String path 				= null;
		Map<String, Object> campos 	= null;
		try{
			log.debug("[eliminarPerfil] Inicio");
			entidad = perfilDao.findOne(id);
			entidad.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			this.preEditar(entidad, request);
			perfilDao.save(entidad);
			model.addAttribute("respuesta", "Eliminación exitosa");
			List<TblPerfil> entidades = perfilDao.listarAllActivos();
			log.debug("[eliminarPerfil] entidades:"+entidades);
			model.addAttribute("registros", entidades);
			path = "seguridad/perfil/per_listado";
			model.addAttribute("filtro", new Filtro());
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
			log.debug("[eliminarPerfil] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;
			campos		= null;
		}
		return path;
	}
	
	/**
	 * Se encarga de asociar una Opcion al Perfil
	 * 
	 * @param idPerfil
	 * @param idOpcion
	 * @param request
	 * @return
	 */
	
	
	
	/**
	 * Se encarga de asociar un Rol al Perfil
	 * 
	 * @param idPerfil
	 * @param idRol
	 * @return
	 */
	/*@RequestMapping("perfil/rol/asociar/{idPerfil}/{idRol}")
	public String asociarRol(@PathVariable("idPerfil") BigDecimal idPerfil,
			@PathVariable("idRol") String idRol, Model model) {
		SegTblPerfil entidad = perfil.findOne(idPerfil);
		Set<SegTblRol> rolExistentes = entidad.getSegTblRols();
		SegTblRol rolNuevo = rol.findOne(idRol);
		rolExistentes.add(rolNuevo);
		perfil.save(entidad);
		Map<String, Object> campos = configurarCamposConsulta();
		model.addAttribute("contenido", campos);
		return "redirect:/perfil/privilegio/" + idPerfil;
	}
	*/
	/**
	 * Se encarga de desasociar un Rol al Perfil
	 * 
	 * @param idPerfil
	 * @param idRol
	 * @return
	 */
	/*@RequestMapping("perfil/rol/desasociar/{idPerfil}/{idRol}")
	public String desasociarRol(@PathVariable("idPerfil") BigDecimal idPerfil,
			@PathVariable("idRol") String idRol, Model model) {
		SegTblPerfil entidad = perfil.findOne(idPerfil);
		Set<SegTblRol> rolExistentes = entidad.getSegTblRols();
		SegTblRol rolEliminar = rol.findOne(idRol);
		rolExistentes.remove(rolEliminar);
		perfil.save(entidad);
		Map<String, Object> campos = configurarCamposConsulta();
		model.addAttribute("contenido", campos);
		return "redirect:/perfil/privilegio/" + idPerfil;
	}*/

	@Override
	public TblPerfil getNuevaEntidad() {
		return new TblPerfil();
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
		campo = new Campo("combo_estado", "estado", false);
		campos.put("Estado Perfil", campo);
		return campos;
	}
	
	/**
	 * Se encarga de configurar los campos del formulario de nuevo
	 * @param perfil 
	 * @return
	 */
	/*private Map<String, Object> configurarCamposOpcion(TblPerfil perfil) {
		// El Map debe tener la estructura: String del label , Campo 
		Map<String,Object> campos = new LinkedHashMap<>();
		Campo campo = null;
		campo = new Campo("label", perfil.getNombre());
		campos.put("Nombre del Perfil", campo);
		campo = new Campo("imagen_estado_registro", "estado");
		campos.put("Estado Registro", campo);
		campo = new Campo("label", perfil.getDescripcion());
		campos.put("Descripción", campo);
		return campos;
	}*/
	/**
	 * Se encarga de direccionar a la pantalla de Opcion
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "perfil/opcion/{id}", method = RequestMethod.GET)
	public String editarOpcion(@PathVariable Integer id, Model model, HttpSession session) {
		TblPerfil entidad 			= null;
		List<TblOpcion> listaOpciones 	= null;
		List<TblOpcion> listaOpcionesNueva 	= null;
		Integer intPos = new Integer(0);
		TblOpcion opc	= null;
		try{
			log.debug("[editarOpcion] Inicio");
			entidad = perfilDao.findOne(id);
			model.addAttribute("entidad", entidad);
			listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
			listaOpciones = opcionDao.listarAllActivos();
			opc = this.getOpcionRaiz();
			listaOpcionesNueva = new ArrayList<TblOpcion>();
			
			// Consulta configuracion existente
			Set<TblPerfilOpcion> entidadesExistentes = entidad.getTblPerfilOpcions();
			if (entidadesExistentes != null && !entidadesExistentes.isEmpty()) {
				// Si existen registros marco los bean como seleccionado
				for (TblPerfilOpcion tblPerfilOpcion : entidadesExistentes) {
					for (TblOpcion tblOpcion : listaOpciones) {
						if (tblPerfilOpcion.getId().getCodigoOpcion() == tblOpcion.getCodigoOpcion()) {
							tblOpcion.setSeleccionado(true);
							break;
						}
					}
				}
			}
			
			
			this.orderOpcion(listaOpciones, intPos, listaOpcionesNueva, opc.getCodigoOpcion());
			log.debug("[editarOpcion] listaOpcionesNueva:"+listaOpcionesNueva.size());
			model.addAttribute("registros", listaOpcionesNueva);
			session.setAttribute("perfilesOpciones", listaOpcionesNueva);
			log.debug("[editarOpcion] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return "seguridad/perfil/per_opcion";
	}
	/**
	 * Genera la lista de opciones ordenada por niveles y modulo
	 * 
	 * @param listado
	 * @param intPos
	 * @param listaOpciones
	 * @param intModulo
	 */
	private void orderOpcion(List<TblOpcion> listado, Integer intPos, List<TblOpcion> listaOpciones, Integer intModulo){
		TblOpcion opc	= null;
		try{
			log.debug("[orderOpcion] Inicio : intPos"+intPos);
			while (intPos < listado.size()){
				opc = listado.get(intPos);
				if (opc.getModulo().compareTo(intModulo)==0){
					listaOpciones.add(opc);
					intPos++;
					log.debug("[orderOpcion] Llamando a : "+ intPos);
					this.orderOpcion(listado, intPos, listaOpciones, opc.getCodigoOpcion());
				}else{
					intPos++;
				}
			}
			log.debug("[orderOpcion] Fin : intPos"+intPos);
		}catch(Exception e){
			
		}finally{
			opc	= null;
		}
	}
	/**
	 * Genera la opcion raiz del sistema
	 * 
	 * @return TblOpcion
	 */
	private TblOpcion getOpcionRaiz(){
		TblOpcion opcion = new TblOpcion();
		opcion.setNombre("Sistema de Gestion de Tiendas");
		opcion.setCodigoOpcion(1000);
		opcion.setNivel(0);
		opcion.setModulo(0);
		return opcion;
	}

	/**
	 * Se encarga de asociar una Opcion al Perfil
	 * 
	 * @param idPerfil
	 * @param idOpcion
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "perfil/opcion/asociar/{idPerfil}/{idOpcion}", method = RequestMethod.GET)
	public String asociarOpcion(@PathVariable("idPerfil") Integer idPerfil,
								@PathVariable("idOpcion") Integer idOpcion, Model model, HttpSession session, HttpServletRequest request) {
		String ip = null;
		TblPerfilOpcion entidad = null;
		List<TblOpcion> entidadesOrdenadas = null;
		TblPerfil entidadPerfil = null;
		try{
			log.debug("[asociarOpcion] Inicio");
			ip = request.getRemoteAddr();
			entidad = new TblPerfilOpcion();
			entidad.setId(new TblPerfilOpcionId());
			entidad.getId().setCodigoPerfil(idPerfil);
			entidad.getId().setCodigoOpcion(idOpcion);	
			entidad.setPrivilegio("");
			entidad.setFechaCreacion(new Date());
			entidad.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			entidad.setIpCreacion(ip);
			TblPerfilOpcion entidadExiste = perfilOpcionDao.findOne(entidad.getId());
			if (entidadExiste != null) {
				perfilOpcionDao.save(entidadExiste);
			} else {
				perfilOpcionDao.save(entidad);	
			}
			
			entidadesOrdenadas = (List<TblOpcion>) request.getSession().getAttribute("perfilesOpciones");
			for (TblOpcion tblOpcion : entidadesOrdenadas) {
				if (entidad.getId().getCodigoOpcion() == tblOpcion.getCodigoOpcion()) {
					tblOpcion.setSeleccionado(true);
					break;
				}
			}
			entidadPerfil = perfilDao.findOne(idPerfil);
			model.addAttribute("entidad", entidadPerfil);
			//model.addAttribute("perfilesOpciones", entidadesOrdenadas);
			model.addAttribute("registros", entidadesOrdenadas);
			session.setAttribute("perfilesOpciones", entidadesOrdenadas);
			log.debug("[asociarOpcion] Fin");
		}catch(Exception e){
			e.printStackTrace();
			
		}finally{
			ip 					= null;
			entidad 			= null;
			entidadesOrdenadas 	= null;
			entidadPerfil 		= null;
		}
		
		
		return "seguridad/perfil/per_opcion";
	}
	/**
	 * Se encarga de desasociar una Opcion al Perfil
	 * 
	 * @param idPerfil
	 * @param idOpcion
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "perfil/opcion/desasociar/{idPerfil}/{idOpcion}", method = RequestMethod.GET)
	public String desasociarOpcion(@PathVariable("idPerfil") Integer idPerfil,
			@PathVariable("idOpcion") Integer idOpcion, Model model, HttpSession session, HttpServletRequest request) {		
		TblPerfilOpcion entidad = null;
		List<TblOpcion> entidadesOrdenadas = null;
		TblPerfil entidadPerfil = null;
		try{
			log.debug("[desasociarOpcion] Inicio");
			entidad = new TblPerfilOpcion();
			entidad.setId(new TblPerfilOpcionId());
			entidad.getId().setCodigoPerfil(idPerfil);
			entidad.getId().setCodigoOpcion(idOpcion);
			
			entidad = perfilOpcionDao.findOne(entidad.getId());
			perfilOpcionDao.delete(entidad);
			
			entidadesOrdenadas = (List<TblOpcion>) request.getSession().getAttribute("perfilesOpciones");
			for (TblOpcion tblOpcion : entidadesOrdenadas) {
				if (entidad.getId().getCodigoOpcion() == tblOpcion.getCodigoOpcion()) {
					tblOpcion.setSeleccionado(false);
					break;
				}
			}
			entidadPerfil = perfilDao.findOne(idPerfil);
			model.addAttribute("entidad", entidadPerfil);
			//model.addAttribute("perfilesOpciones", entidadesOrdenadas);
			model.addAttribute("registros", entidadesOrdenadas);
			session.setAttribute("perfilesOpciones", entidadesOrdenadas);
			log.debug("[desasociarOpcion] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 			= null;
			entidadesOrdenadas 	= null;
			entidadPerfil 		= null;
		}
		
		
		return "seguridad/perfil/per_opcion";
	}
}
