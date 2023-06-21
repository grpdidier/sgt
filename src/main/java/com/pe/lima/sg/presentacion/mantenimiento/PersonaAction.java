package com.pe.lima.sg.presentacion.mantenimiento;

import static com.pe.lima.sg.dao.mantenimiento.PersonaSpecifications.conDNI;
import static com.pe.lima.sg.dao.mantenimiento.PersonaSpecifications.conEstado;
import static com.pe.lima.sg.dao.mantenimiento.PersonaSpecifications.conMaterno;
import static com.pe.lima.sg.dao.mantenimiento.PersonaSpecifications.conNombre;
import static com.pe.lima.sg.dao.mantenimiento.PersonaSpecifications.conPaterno;
import static com.pe.lima.sg.dao.mantenimiento.PersonaSpecifications.conRazonSocial;
import static com.pe.lima.sg.dao.mantenimiento.PersonaSpecifications.conRuc;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.pe.lima.sg.dao.BaseDAO;
import com.pe.lima.sg.dao.mantenimiento.IPersonaDAO;
import com.pe.lima.sg.entity.mantenimiento.TblPersona;
import com.pe.lima.sg.entity.mantenimiento.TblUbigeo;
import com.pe.lima.sg.presentacion.BasePresentacion;
import com.pe.lima.sg.presentacion.Campo;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.ListaUtilAction;
import com.pe.lima.sg.presentacion.util.PageWrapper;
import com.pe.lima.sg.presentacion.util.PageableSG;
import com.pe.lima.sg.presentacion.util.UtilSGT;

import lombok.extern.slf4j.Slf4j;

/**
 * Clase Bean que se encarga de la administracion de las personas
 *
 * 			
 */
@Slf4j
@Controller
//@PreAuthorize("hasAuthority('CRUD')")
public class PersonaAction extends BasePresentacion<TblPersona> {
	@Autowired
	private IPersonaDAO personaDao;

	
	@Autowired
	private ListaUtilAction listaUtil;
	
	private String urlPaginado = "/personas/paginado/"; 
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDAO getDao() {
		return personaDao;
	}
	
	/**
	 * Se encarga de listar todos los personas
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/personas", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path,  PageableSG pageable,HttpServletRequest request) {
		//Map<String, Object> campos = null;
		Filtro filtro = null;
		try{
			log.debug("[traerRegistros] Inicio");
			path = "mantenimiento/persona/per_listado";
			//campos = configurarCamposConsulta();
			//model.addAttribute("contenido", campos);
			filtro = new Filtro();
			model.addAttribute("filtro", filtro);
			request.getSession().setAttribute("sessionFiltroCriterio", filtro);
			this.listarPersonas(model, filtro, pageable, this.urlPaginado);
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PERSONA);
			listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_CIVIL);
			listaUtil.cargarDatosModel(model, Constantes.MAP_SI_NO);
			log.debug("[traerRegistros] Fin");
		}catch(Exception e){
			log.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			//campos		= null;
			filtro = null;
		}
		
		return path;
	}
	/**
	 * Se encarga de buscar la informacion del Persona segun el filtro
	 * seleccionado
	 * 
	 * @param model
	 * @param personaBean
	 * @return
	 */
	@RequestMapping(value = "/personas/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, Filtro filtro, String path,  PageableSG pageable, HttpServletRequest request) {
		//Map<String, Object> campos = null;
		path = "mantenimiento/persona/per_listado";
		try{
			log.debug("[traerRegistrosFiltrados] Inicio");
			this.listarPersonas(model, filtro, pageable, this.urlPaginado);
			//campos = configurarCamposConsulta();
			//model.addAttribute("contenido", campos);
			model.addAttribute("filtro", filtro);
			request.getSession().setAttribute("sessionFiltroCriterio", filtro);
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PERSONA);
			listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_CIVIL);
			listaUtil.cargarDatosModel(model, Constantes.MAP_SI_NO);
			log.debug("[traerRegistrosFiltrados] Fin");
		}catch(Exception e){
			log.debug("[traerRegistrosFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			//campos		= null;
		}
		log.debug("[traerRegistrosFiltrados] Fin");
		return path;
	}
	/*** Listado de Personas ***/
	private void listarPersonas(Model model, Filtro filtro,  PageableSG pageable, String url){
		//List<TblPersona> entidades = new ArrayList<TblPersona>();
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "nombre"));
		try{
			//entidades = personaDao.listarCriterios(filtro.getNombre(), filtro.getPaterno(), filtro.getMaterno(), filtro.getDni(), filtro.getRuc(), filtro.getRazonSocial());
			//log.debug("[listarPersonaes] entidades:"+entidades);
			//model.addAttribute("registros", entidades);
			Specification<TblPersona> criterio = Specifications.where(conNombre(filtro.getNombre()))
					.and(conPaterno(filtro.getPaterno()))
					.and(conMaterno(filtro.getMaterno()))
					.and(conDNI(filtro.getDni()))
					.and(conRuc(filtro.getRuc()))
					.and(conRazonSocial(filtro.getRazonSocial()))
					.and(conEstado(Constantes.ESTADO_REGISTRO_ACTIVO));
			pageable.setSort(sort);
			Page<TblPersona> entidadPage = personaDao.findAll(criterio, pageable);
			PageWrapper<TblPersona> page = new PageWrapper<TblPersona>(entidadPage, url, pageable);
			model.addAttribute("registros", page.getContent());
			model.addAttribute("page", page);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sort = null;
		}
	}
	
	private void listarPersonaRegistrada(Model model, Filtro filtro,  PageableSG pageable, String url){
		//List<TblPersona> entidades = new ArrayList<TblPersona>();
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "nombre"));
		try{
			//entidades = personaDao.listarCriterios(filtro.getNombre(), filtro.getPaterno(), filtro.getMaterno(), filtro.getDni(), filtro.getRuc(), filtro.getRazonSocial());
			//log.debug("[listarPersonaes] entidades:"+entidades);
			//model.addAttribute("registros", entidades);
			Specification<TblPersona> criterio = Specifications.where(conNombre(filtro.getNombre()))
					.and(conPaterno(filtro.getPaterno()))
					.and(conMaterno(filtro.getMaterno()))
					.and(conDNI(filtro.getDni()))
					.and(conRuc(filtro.getRuc()))
					.and(conRazonSocial(filtro.getRazonSocial()))
					.and(conEstado(Constantes.ESTADO_REGISTRO_ACTIVO));
			pageable.setSort(sort);
			Page<TblPersona> entidadPage = personaDao.findAll(criterio, pageable);
			PageWrapper<TblPersona> page = new PageWrapper<TblPersona>(entidadPage, url, pageable);
			model.addAttribute("registros", page.getContent());
			model.addAttribute("page", page);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sort = null;
		}
	}
	
	/**
	 * Se encarga de direccionar a la pantalla de edicion del Persona
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "persona/editar/{id}", method = RequestMethod.GET)
	public String editarPersona(@PathVariable Integer id, Model model,HttpServletRequest request) {
		TblPersona entidad 			= null;
		try{
			entidad = personaDao.findOne(id);
			model.addAttribute("entidad", entidad);
			asignarMapCargaDepartamento(request, entidad);
			asignarMapCargarDistrito(request, entidad);
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PERSONA);
			listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_CIVIL);
			listaUtil.cargarDatosModel(model, Constantes.MAP_SI_NO);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return "mantenimiento/persona/per_edicion";
	}
	
	
	/**
	 * Se encarga de direccionar a la pantalla de creacion del Persona
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "persona/nuevo", method = RequestMethod.GET)
	public String crearPersona(Model model,HttpServletRequest request) {
		try{
			log.debug("[crearPersona] Inicio");
			model.addAttribute("entidad", new TblPersona());
			
			request.getSession().setAttribute("SessionPersonaMapDistritoInei",null);
			request.getSession().setAttribute("SessionPersonaMapProvinciaInei",null);
			
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PERSONA);
			listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_CIVIL);
			listaUtil.cargarDatosModel(model, Constantes.MAP_SI_NO);
			log.debug("[crearPersona] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "mantenimiento/persona/per_nuevo";
	}
	
	@Override
	public void preGuardar(TblPersona entidad, HttpServletRequest request) {
		try{
			log.debug("[preGuardar] Inicio" );
			entidad.setFechaCreacion(new Date(System.currentTimeMillis()));
			entidad.setIpCreacion(request.getRemoteAddr());
			entidad.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			//datos en mayuscula
			entidad.setNombre(entidad.getNombre().toUpperCase());
			entidad.setPaterno(entidad.getPaterno().toUpperCase());
			entidad.setMaterno(entidad.getMaterno().toUpperCase());
			entidad.setRazonSocial(entidad.getRazonSocial().toUpperCase());
			log.debug("[preGuardar] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	/** Validamos logica del negocio**/
	@Override
	public boolean validarNegocio(Model model,TblPersona entidad, HttpServletRequest request){
		boolean exitoso = true;
		Integer total 	= null;
		try{
			//Validando la existencia del persona
			total = personaDao.countOneByDniRuc(entidad.getNumeroDni(), entidad.getNumeroRuc());
			if (total > 0){
				exitoso = false;
				model.addAttribute("respuesta", "El Persona existe, debe modificarlo para continuar...");
			}
			if (entidad.getTipoCliente().equals("-1")) {
				exitoso = false;
				model.addAttribute("respuesta", "Debe seleccionar si es Cliente");
			}
			if (entidad.getTipoProveedor().equals("-1")) {
				exitoso = false;
				model.addAttribute("respuesta", "Debe seleccionar si es Proveedor");
			}
			
		}catch(Exception e){
			exitoso = false;
		}finally{
			total = null;
		}
		return exitoso;
	}
	
	@RequestMapping(value = "persona/provincia", method = RequestMethod.POST)
	public String cargarDepartamento(Model model, TblPersona entidad, HttpServletRequest request) {
		asignarMapCargaDepartamento(request, entidad);
		listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PERSONA);
		listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_CIVIL);
		listaUtil.cargarDatosModel(model, Constantes.MAP_SI_NO);
		model.addAttribute("entidad", entidad);
		if (entidad.getCodigoPersona()<=0) {
			return "mantenimiento/persona/per_nuevo";
		}else {
			return "mantenimiento/persona/per_edicion";
		}
	}
	@SuppressWarnings("unchecked")
	private void asignarMapCargaDepartamento(HttpServletRequest request, TblPersona entidad) {
		Map<String,List<TblUbigeo>> provinciaMap = (Map<String,List<TblUbigeo>>)request.getSession().getAttribute("SessionMapProvinciaInei");
		if (entidad.getDepartamento() != null) {
			List<TblUbigeo> lista = provinciaMap.get(entidad.getDepartamento());
			request.getSession().setAttribute("SessionPersonaMapProvinciaInei", obtenerDatosUbigeo(lista));
			request.getSession().setAttribute("SessionPersonaMapDistritoInei", null);
		}else {
			request.getSession().setAttribute("SessionPersonaMapProvinciaInei", null);
			request.getSession().setAttribute("SessionPersonaMapDistritoInei", null);
		}
	}

	@RequestMapping(value = "persona/distrito", method = RequestMethod.POST)
	public String cargarDistrito(Model model, TblPersona entidad, HttpServletRequest request) {
		asignarMapCargarDistrito(request,entidad);
		listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PERSONA);
		listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_CIVIL);
		listaUtil.cargarDatosModel(model, Constantes.MAP_SI_NO);
		model.addAttribute("entidad", entidad);
		if (entidad.getCodigoPersona()<=0) {
			return "mantenimiento/persona/per_nuevo";
		}else {
			return "mantenimiento/persona/per_edicion";
		}
	}
	@SuppressWarnings("unchecked")
	private void asignarMapCargarDistrito(HttpServletRequest request, TblPersona entidad) {
		Map<String,List<TblUbigeo>> distritoMap = (Map<String,List<TblUbigeo>>)request.getSession().getAttribute("SessionMapDistritoInei");
		if (entidad.getProvincia() != null) {
			List<TblUbigeo> lista = distritoMap.get(entidad.getProvincia());
			request.getSession().setAttribute("SessionPersonaMapDistritoInei", obtenerDatosUbigeo(lista));
		}else {
			request.getSession().setAttribute("SessionPersonaMapDistritoInei", null);
		}
		
	}

	private Map<String, String> obtenerDatosUbigeo(List<TblUbigeo> lista) {
		Map<String, String> resultados = new LinkedHashMap<String, String>();
		if (lista != null && !lista.isEmpty()) {
			for(TblUbigeo ubigeo: lista) {
				resultados.put(ubigeo.getNombre(), ubigeo.getCodigoInei());
			}
		}
		return resultados;
	}
	/**
	 * Se encarga de guardar la informacion del Persona
	 * 
	 * @param personaBean
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "persona/nuevo/guardar", method = RequestMethod.POST)
	public String guardarEntidad(Model model, TblPersona entidad, HttpServletRequest request, String path) {
		Map<String, Object> campos 	= null;
		path = "mantenimiento/persona/per_listado";
		Filtro filtro = new Filtro();
		try{
			log.debug("[guardarEntidad] Inicio" );
			if (this.validarNegocio(model, entidad, request)){
				log.debug("[guardarEntidad] Pre Guardar..." );
				this.preGuardar(entidad, request);
				boolean exitoso = super.guardar(entidad, model);
				log.debug("[guardarEntidad] Guardado..." );
				if (exitoso){
					//List<TblPersona> entidades = personaDao.buscarOneByDniRuc(entidad.getNumeroDni(), entidad.getNumeroRuc());
					//model.addAttribute("registros", entidades);
					filtro.setNombre(entidad.getNombre());
					filtro.setPaterno(entidad.getPaterno());
					filtro.setMaterno(entidad.getMaterno());
					filtro.setRazonSocial(entidad.getRazonSocial());
					filtro.setDni(entidad.getNumeroDni());
					filtro.setRuc(entidad.getNumeroRuc());
					this.listarPersonaRegistrada(model, filtro, new PageableSG(), this.urlPaginado);
					listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PERSONA);
					listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_CIVIL);
					listaUtil.cargarDatosModel(model, Constantes.MAP_SI_NO);
					campos = configurarCamposConsulta();
					model.addAttribute("contenido", campos);
					model.addAttribute("filtro", new Filtro());
				}else{
					path = "mantenimiento/persona/per_nuevo";
					listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PERSONA);
					listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_CIVIL);
					listaUtil.cargarDatosModel(model, Constantes.MAP_SI_NO);
					model.addAttribute("entidad", entidad);
					
				}
			}else{
				path = "mantenimiento/persona/per_nuevo";
				listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PERSONA);
				listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_CIVIL);
				listaUtil.cargarDatosModel(model, Constantes.MAP_SI_NO);
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
	public void preEditar(TblPersona entidad, HttpServletRequest request) {
		try{
			log.debug("[preEditar] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			//datos en mayuscula
			entidad.setNombre(entidad.getNombre().toUpperCase());
			entidad.setPaterno(entidad.getPaterno().toUpperCase());
			entidad.setMaterno(entidad.getMaterno().toUpperCase());
			entidad.setRazonSocial(entidad.getRazonSocial().toUpperCase());
			log.debug("[preEditar] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "persona/nuevo/editar", method = RequestMethod.POST)
	public String editarEntidad(TblPersona entidad, Model model, HttpServletRequest request) {
		Map<String, Object> campos 	= null;
		String path 				= "mantenimiento/persona/per_listado";;
		TblPersona entidadEnBd 		= null;
		Filtro filtro				= new Filtro();
		try{
			if (validarCamposOK(entidad, model)) {
				// Se actualizan solo los campos del formulario
				entidadEnBd = personaDao.findOne(entidad.getCodigoPersona());
				entidadEnBd.setNombre(entidad.getNombre());
				entidadEnBd.setPaterno(entidad.getPaterno());
				entidadEnBd.setMaterno(entidad.getMaterno());
				entidadEnBd.setNumeroDni(entidad.getNumeroDni());
				entidadEnBd.setNumeroRuc(entidad.getNumeroRuc());
				entidadEnBd.setEstadoCivil(entidad.getEstadoCivil());
				entidadEnBd.setRazonSocial(entidad.getRazonSocial());
				entidadEnBd.setTipoCliente(entidad.getTipoCliente());
				entidadEnBd.setTipoProveedor(entidad.getTipoProveedor());
				entidadEnBd.setTelefono1(entidad.getTelefono1());
				entidadEnBd.setTelefono2(entidad.getTelefono2());
				entidadEnBd.setCelular1(entidad.getCelular1());
				entidadEnBd.setCelular2(entidad.getCelular2());
				entidadEnBd.setCorreoElectronico(entidad.getCorreoElectronico());
				entidadEnBd.setDireccionEmpresa(entidad.getDireccionEmpresa());
				entidadEnBd.setDireccionCasa(entidad.getDireccionCasa());
				entidadEnBd.setObservacion(entidad.getObservacion());
				entidadEnBd.setTipoPersona(entidad.getTipoPersona());
				entidadEnBd.setDepartamento(entidad.getDepartamento());
				entidadEnBd.setProvincia(entidad.getProvincia());
				entidadEnBd.setDistrito(entidad.getDistrito());
				this.preEditar(entidadEnBd, request);
				boolean exitoso = super.guardar(entidadEnBd, model);
				log.debug("[guardarEntidad] Guardado..." );
				if (exitoso){

					//List<TblPersona> entidades = personaDao.buscarOneByDniRuc(entidadEnBd.getNumeroDni(), entidadEnBd.getNumeroRuc());

					//model.addAttribute("registros", entidades);
					filtro.setNombre(entidad.getNombre());
					filtro.setPaterno(entidad.getPaterno());
					filtro.setMaterno(entidad.getMaterno());
					filtro.setRazonSocial(entidad.getRazonSocial());
					filtro.setDni(entidad.getNumeroDni());
					filtro.setRuc(entidad.getNumeroRuc());
					this.listarPersonaRegistrada(model, filtro, new PageableSG(), this.urlPaginado);
					listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PERSONA);
					listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PERSONA);
					listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_CIVIL);
					listaUtil.cargarDatosModel(model, Constantes.MAP_SI_NO);
					campos = configurarCamposConsulta();
					model.addAttribute("contenido", campos);
					model.addAttribute("filtro", new Filtro());
				}else{
					path = "mantenimiento/persona/per_edicion";
					listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PERSONA);
					listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_CIVIL);
					listaUtil.cargarDatosModel(model, Constantes.MAP_SI_NO);
					model.addAttribute("entidad", entidad);
				}
			}else {
				path = "mantenimiento/persona/per_edicion";
				listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PERSONA);
				listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_CIVIL);
				listaUtil.cargarDatosModel(model, Constantes.MAP_SI_NO);
				model.addAttribute("entidad", entidad);
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return path;

	}
	
	private boolean validarCamposOK(TblPersona entidad, Model model) {
		if (entidad.getDistrito().equals("-1")) {
			model.addAttribute("respuesta", "Debe seleccionar el Distrito");
			return false;
			
		}else {
			return true;
		}
		
	}

	/**
	 * Se encarga de la eliminacion logica del Persona
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "persona/eliminar/{id}", method = RequestMethod.GET)
	public String eliminarPersona(@PathVariable Integer id, HttpServletRequest request, Model model,  PageableSG pageable) {
		TblPersona entidad			= null;
		String path 				= null;
		Map<String, Object> campos 	= null;
		try{
			log.debug("[eliminarPersona] Inicio");
			entidad = personaDao.findOne(id);
			entidad.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			this.preEditar(entidad, request);
			personaDao.save(entidad);
			model.addAttribute("respuesta", "Eliminación exitosa");
			model.addAttribute("filtro", new Filtro());
			path = "mantenimiento/persona/per_listado";
			this.traerRegistrosFiltrados(model, new Filtro(), path, pageable, request);
			//List<TblPersona> entidades = personaDao.listarAllActivos();
			//log.debug("[eliminarPersona] entidades:"+entidades);
			//model.addAttribute("registros", entidades);
			
			
			
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PERSONA);
			listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_CIVIL);
			listaUtil.cargarDatosModel(model, Constantes.MAP_SI_NO);
			log.debug("[eliminarPersona] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;
			campos		= null;
		}
		return path;
	}
	

	@Override
	public TblPersona getNuevaEntidad() {
		return new TblPersona();
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
		campo = new Campo("text", "paterno", false);
		campos.put("Paterno", campo);
		campo = new Campo("text", "materno", false);
		campos.put("Materno", campo);
		campo = new Campo("text", "dni", false);
		campos.put("DNI", campo);
		campo = new Campo("text", "ruc", false);
		campos.put("RUC", campo);
		campo = new Campo("text", "razonSocial", false);
		campos.put("Razon Social", campo);
		return campos;
	}
	
	/*
	 * Paginado
	 */
	@RequestMapping(value = "/personas/paginado/{page}/{size}/{operacion}", method = RequestMethod.GET)
	public String paginarEntidad(@PathVariable Integer page, @PathVariable Integer size, @PathVariable String operacion, Model model,  PageableSG pageable, HttpServletRequest request) {
		Filtro filtro = null;
		String path = null;
		Map<String, Object> campos = null;
		try{
			//log.debug("[traerRegistros] Inicio");
			path = "mantenimiento/persona/per_listado";
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
			filtro = (Filtro)request.getSession().getAttribute("sessionFiltroCriterio");
			model.addAttribute("filtro", filtro);
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			this.listarPersonas(model, filtro, pageable, this.urlPaginado);
			
		}catch(Exception e){
			//log.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		return path;
	}	
}
