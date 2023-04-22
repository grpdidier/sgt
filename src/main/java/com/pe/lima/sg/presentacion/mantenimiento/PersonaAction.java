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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.pe.lima.sg.presentacion.BasePresentacion;
import com.pe.lima.sg.presentacion.Campo;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.ListaUtilAction;
import com.pe.lima.sg.presentacion.util.PageWrapper;
import com.pe.lima.sg.presentacion.util.PageableSG;
import com.pe.lima.sg.presentacion.util.UtilSGT;

/**
 * Clase Bean que se encarga de la administracion de las personas
 *
 * 			
 */
@Controller
//@PreAuthorize("hasAuthority('CRUD')")
public class PersonaAction extends BasePresentacion<TblPersona> {
	private static final Logger logger = LogManager.getLogger(PersonaAction.class);
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
			logger.debug("[traerRegistros] Inicio");
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
			logger.debug("[traerRegistros] Fin");
		}catch(Exception e){
			logger.debug("[traerRegistros] Error:"+e.getMessage());
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
			logger.debug("[traerRegistrosFiltrados] Inicio");
			this.listarPersonas(model, filtro, pageable, this.urlPaginado);
			//campos = configurarCamposConsulta();
			//model.addAttribute("contenido", campos);
			model.addAttribute("filtro", filtro);
			request.getSession().setAttribute("sessionFiltroCriterio", filtro);
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PERSONA);
			listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_CIVIL);
			listaUtil.cargarDatosModel(model, Constantes.MAP_SI_NO);
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
	/*** Listado de Personas ***/
	private void listarPersonas(Model model, Filtro filtro,  PageableSG pageable, String url){
		//List<TblPersona> entidades = new ArrayList<TblPersona>();
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "nombre"));
		try{
			//entidades = personaDao.listarCriterios(filtro.getNombre(), filtro.getPaterno(), filtro.getMaterno(), filtro.getDni(), filtro.getRuc(), filtro.getRazonSocial());
			//logger.debug("[listarPersonaes] entidades:"+entidades);
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
			//logger.debug("[listarPersonaes] entidades:"+entidades);
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
	public String editarPersona(@PathVariable Integer id, Model model) {
		TblPersona entidad 			= null;
		try{
			entidad = personaDao.findOne(id);
			model.addAttribute("entidad", entidad);
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
	public String crearPersona(Model model) {
		try{
			logger.debug("[crearPersona] Inicio");
			model.addAttribute("entidad", new TblPersona());
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PERSONA);
			listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_CIVIL);
			listaUtil.cargarDatosModel(model, Constantes.MAP_SI_NO);
			logger.debug("[crearPersona] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "mantenimiento/persona/per_nuevo";
	}
	
	@Override
	public void preGuardar(TblPersona entidad, HttpServletRequest request) {
		try{
			logger.debug("[preGuardar] Inicio" );
			entidad.setFechaCreacion(new Date(System.currentTimeMillis()));
			entidad.setIpCreacion(request.getRemoteAddr());
			entidad.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			//datos en mayuscula
			entidad.setNombre(entidad.getNombre().toUpperCase());
			entidad.setPaterno(entidad.getPaterno().toUpperCase());
			entidad.setMaterno(entidad.getMaterno().toUpperCase());
			entidad.setRazonSocial(entidad.getRazonSocial().toUpperCase());
			logger.debug("[preGuardar] Fin" );
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
			
		}catch(Exception e){
			exitoso = false;
		}finally{
			total = null;
		}
		return exitoso;
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
			logger.debug("[guardarEntidad] Inicio" );
			if (this.validarNegocio(model, entidad, request)){
				logger.debug("[guardarEntidad] Pre Guardar..." );
				this.preGuardar(entidad, request);
				boolean exitoso = super.guardar(entidad, model);
				logger.debug("[guardarEntidad] Guardado..." );
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
			
			logger.debug("[guardarEntidad] Fin" );
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
			logger.debug("[preEditar] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			//datos en mayuscula
			entidad.setNombre(entidad.getNombre().toUpperCase());
			entidad.setPaterno(entidad.getPaterno().toUpperCase());
			entidad.setMaterno(entidad.getMaterno().toUpperCase());
			entidad.setRazonSocial(entidad.getRazonSocial().toUpperCase());
			logger.debug("[preEditar] Fin" );
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
			this.preEditar(entidadEnBd, request);
			boolean exitoso = super.guardar(entidadEnBd, model);
			logger.debug("[guardarEntidad] Guardado..." );
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
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
		
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
			logger.debug("[eliminarPersona] Inicio");
			entidad = personaDao.findOne(id);
			entidad.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			this.preEditar(entidad, request);
			personaDao.save(entidad);
			model.addAttribute("respuesta", "Eliminación exitosa");
			model.addAttribute("filtro", new Filtro());
			path = "mantenimiento/persona/per_listado";
			this.traerRegistrosFiltrados(model, new Filtro(), path, pageable, request);
			//List<TblPersona> entidades = personaDao.listarAllActivos();
			//logger.debug("[eliminarPersona] entidades:"+entidades);
			//model.addAttribute("registros", entidades);
			
			
			
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PERSONA);
			listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_CIVIL);
			listaUtil.cargarDatosModel(model, Constantes.MAP_SI_NO);
			logger.debug("[eliminarPersona] Fin");
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
			//LOGGER.debug("[traerRegistros] Inicio");
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
			//LOGGER.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		return path;
	}	
}
