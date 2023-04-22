package com.pe.lima.sg.presentacion.mantenimiento;

import static com.pe.lima.sg.dao.mantenimiento.TiendaSpecifications.conCodigoEdificio;
import static com.pe.lima.sg.dao.mantenimiento.TiendaSpecifications.conEstado;
import static com.pe.lima.sg.dao.mantenimiento.TiendaSpecifications.conEstadoTienda;
import static com.pe.lima.sg.dao.mantenimiento.TiendaSpecifications.conNumero;

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
import com.pe.lima.sg.dao.mantenimiento.IEdificioDAO;
import com.pe.lima.sg.dao.mantenimiento.ISuministroDAO;
import com.pe.lima.sg.dao.mantenimiento.ITiendaDAO;
import com.pe.lima.sg.entity.mantenimiento.TblEdificio;
import com.pe.lima.sg.entity.mantenimiento.TblSuministro;
import com.pe.lima.sg.entity.mantenimiento.TblTienda;
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
 * Clase Bean que se encarga de la administracion de los tiendas
 *
 * 			
 */
@Slf4j
@Controller
//@PreAuthorize("hasAuthority('CRUD')")
public class TiendaAction extends BasePresentacion<TblTienda> {
	@Autowired
	private ITiendaDAO tiendaDao;
	
	@Autowired
	private IEdificioDAO edificioDao;
	
	@Autowired
	private ISuministroDAO suministroDao;
	
	@Autowired
	private ListaUtilAction listaUtil;
	
	private String urlPaginado = "/tiendas/paginado/"; 
	
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDAO getDao() {
		return tiendaDao;
	}
	
	/**
	 * Se encarga de listar todos los tiendaes
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tiendas/{id}", method = RequestMethod.GET)
	public String traerRegistros(@PathVariable Integer id, Model model, String path,  PageableSG pageable,HttpServletRequest request) {
		Map<String, Object> campos = null;
		TblEdificio edificio = null;
		Filtro filtro = null;
		try{
			log.debug("[traerRegistros] Inicio");
			edificio = edificioDao.findOne(id);
			filtro = new Filtro();
			filtro.setCodigo(edificio.getCodigoEdificio());
			this.listarTiendas(model, filtro, pageable, this.urlPaginado);
			model.addAttribute("edificio", edificio);
			path = "mantenimiento/tienda/tie_listado";
			campos = configurarCamposConsulta();
			model.addAttribute("mapEstadoTienda", obtenerValoresEstadoTienda());
			model.addAttribute("contenido", campos);
			model.addAttribute("filtro", filtro);
			request.getSession().setAttribute("sessionFiltroCriterio", filtro);
			log.debug("[traerRegistros] Fin");
		}catch(Exception e){
			log.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			campos		= null;
		}
		
		return path;
	}
	/**
	 * Se encarga de buscar la informacion del Tienda segun el filtro
	 * seleccionado
	 * 
	 * @param model
	 * @param tiendaBean
	 * @return
	 */
	@RequestMapping(value = "/tiendas/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, Filtro filtro, String path,  PageableSG pageable, HttpServletRequest request) {
		Map<String, Object> campos = null;
		TblEdificio edificio = null;
		path = "mantenimiento/tienda/tie_listado";
		try{
			log.debug("[traerRegistrosFiltrados] Inicio");
			edificio = edificioDao.findOne(filtro.getCodigo());
			filtro.setCodigo(edificio.getCodigoEdificio());
			model.addAttribute("edificio", edificio);
			this.listarTiendas(model, filtro, pageable, urlPaginado);
			campos = configurarCamposConsulta();
			model.addAttribute("mapEstadoTienda", obtenerValoresEstadoTienda());
			model.addAttribute("contenido", campos);
			model.addAttribute("filtro", filtro);
			request.getSession().setAttribute("sessionFiltroCriterio", filtro);
			log.debug("[traerRegistrosFiltrados] Fin");
		}catch(Exception e){
			log.debug("[traerRegistrosFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			campos		= null;
			edificio	= null;
		}
		log.debug("[traerRegistrosFiltrados] Fin");
		return path;
	}
	/*** Listado de Tiendas ***/
	private void listarTiendas(Model model, Filtro filtro,  PageableSG pageable, String url){
		//List<TblTienda> entidades = new ArrayList<TblTienda>();
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "numero"));
		try{
			//entidades = tiendaDao.listarCriterios(filtro.getCodigo(), filtro.getNumero());
			Specification<TblTienda> criterio = Specifications.where(conNumero(filtro.getNumero().trim()))
					.and(conCodigoEdificio(filtro.getCodigo()))
					.and(conEstadoTienda(filtro.getEstadoTienda()))
					.and(conEstado(Constantes.ESTADO_REGISTRO_ACTIVO));
			pageable.setSort(sort);
			Page<TblTienda> entidadPage = tiendaDao.findAll(criterio, pageable);
			PageWrapper<TblTienda> page = new PageWrapper<TblTienda>(entidadPage, url, pageable);
			model.addAttribute("registros", page.getContent());
			model.addAttribute("page", page);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sort = null;
		}
	}
	
	
	/**
	 * Se encarga de direccionar a la pantalla de edicion del Tienda
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "tienda/editar/{id}", method = RequestMethod.GET)
	public String editarTienda(@PathVariable Integer id, Model model) {
		TblTienda entidad 			= null;
		List<TblSuministro> listaSuministro = null;
		try{
			entidad = tiendaDao.findOne(id);
			model.addAttribute("entidad", entidad);
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_TIENDA);
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PISO);
			listaSuministro = suministroDao.listarAllActivos();
			model.addAttribute("mapListaSuministro", ListaUtilAction.obtenerValoresListaSuministroActivo(listaSuministro));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
			listaSuministro = null;
		}
		return "mantenimiento/tienda/tie_edicion";
	}
	
	
	/**
	 * Se encarga de direccionar a la pantalla de creacion del Tienda
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "tienda/nuevo/{idEdificio}", method = RequestMethod.GET)
	public String crearTienda(@PathVariable Integer idEdificio, Model model) {
		TblEdificio edificio = null;
		TblTienda entidad = new TblTienda();
		List<TblSuministro> listaSuministro = null;
		try{
			log.debug("[crearTienda] Inicio");
			edificio = edificioDao.findOne(idEdificio);
			model.addAttribute("edificio", edificio);
			entidad.setTblEdificio(edificio);
			//entidad.getTblEdificio().setCodigoEdificio(idEdificio);
			model.addAttribute("entidad",entidad);
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_TIENDA);
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PISO);
			listaSuministro = suministroDao.listarAllActivos();
			model.addAttribute("mapListaSuministro", ListaUtilAction.obtenerValoresListaSuministroActivo(listaSuministro));
			log.debug("[crearTienda] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			edificio = null;
		}
		return "mantenimiento/tienda/tie_nuevo";
	}
	
	@Override
	public void preGuardar(TblTienda entidad, HttpServletRequest request) {
		try{
			log.debug("[preGuardar] Inicio" );
			entidad.setFechaCreacion(new Date(System.currentTimeMillis()));
			entidad.setIpCreacion(request.getRemoteAddr());
			entidad.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			entidad.setEstadoTienda(Constantes.ESTADO_TIENDA_DESOCUPADO);
			entidad.setNumero(entidad.getNumero().toUpperCase());
			log.debug("[preGuardar] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	/** Validamos logica del negocio**/
	@Override
	public boolean validarNegocio(Model model,TblTienda entidad, HttpServletRequest request){
		boolean exitoso = true;
		Integer total 	= null;
		try{
			
			if (entidad.getNumero() == null || entidad.getNumero().trim().length()<=0){
				exitoso = false;
				model.addAttribute("respuesta", "Debe ingresar el Código para continuar...");
				return exitoso;
			}
			//Validando la existencia del tienda
			total = tiendaDao.countOneByNombre(entidad.getTblEdificio().getCodigoEdificio(), entidad.getNumero());
			if (total > 0){
				exitoso = false;
				model.addAttribute("respuesta", "El Código existe, debe modificarlo para continuar...");
			}
			entidad.setNumero(entidad.getNumero().trim());
			
		}catch(Exception e){
			exitoso = false;
		}finally{
			total = null;
		}
		return exitoso;
	}
	/**
	 * Se encarga de guardar la informacion del Tienda
	 * 
	 * @param tiendaBean
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "tienda/nuevo/guardar", method = RequestMethod.POST)
	public String guardarEntidad(Model model, TblTienda entidad,  HttpServletRequest request, String path) {
		Map<String, Object> campos 				= null;
		path 									= "mantenimiento/tienda/tie_listado";
		TblEdificio edificio 					= null;
		TblSuministro suministro 				= null;
		Filtro filtro						 	= null;
		List<TblSuministro> listaSuministro 	= null;
		try{
			log.debug("[guardarEntidad] Inicio" );
			if (this.validarNegocio(model, entidad, request)){
				log.debug("[guardarEntidad] Pre Guardar..." );
				this.preGuardar(entidad, request);
				edificio = edificioDao.findOne(entidad.getTblEdificio().getCodigoEdificio());
				entidad.setTblEdificio(edificio);
				suministro = suministroDao.findOne(entidad.getTblSuministro().getCodigoSuministro());
				entidad.setTblSuministro(suministro);
				boolean exitoso = super.guardar(entidad, model);
				log.debug("[guardarEntidad] Guardado..." );
				model.addAttribute("edificio", edificio);
				if (exitoso){
					edificio = edificioDao.findOne(entidad.getTblEdificio().getCodigoEdificio());
					filtro = new Filtro();
					filtro.setCodigo(edificio.getCodigoEdificio());
					model.addAttribute("edificio", edificio);
					filtro.setNumero(entidad.getNumero());
					this.listarTiendas(model, filtro, new PageableSG(), this.urlPaginado);
					//List<TblTienda> entidades = tiendaDao.buscarOneByNumero(entidad.getTblEdificio().getCodigoEdificio(),entidad.getNumero());
					//model.addAttribute("registros", entidades);
					campos = configurarCamposConsulta();
					model.addAttribute("mapEstadoTienda", obtenerValoresEstadoTienda());
					model.addAttribute("contenido", campos);
					model.addAttribute("filtro", filtro);
					
					
				}else{
					path = "mantenimiento/tienda/tie_nuevo";
					model.addAttribute("entidad", entidad);
					
				}
			}else{
				listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_TIENDA);
				listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PISO);
				listaSuministro = suministroDao.listarAllActivos();
				model.addAttribute("mapListaSuministro", ListaUtilAction.obtenerValoresListaSuministroActivo(listaSuministro));
				path = "mantenimiento/tienda/tie_nuevo";
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
	public void preEditar(TblTienda entidad, HttpServletRequest request) {
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
	
	@RequestMapping(value = "tienda/nuevo/editar", method = RequestMethod.POST)
	public String editarEntidad(TblTienda entidad, TblEdificio edificio, Model model, HttpServletRequest request) {
		Map<String, Object> campos 	= null;
		String path 				= "mantenimiento/tienda/tie_listado";;
		TblTienda entidadEnBd 		= null;
		Filtro filtro 				= null;
		TblSuministro suministro 	= null;
		try{
			// Se actualizan solo los campos del formulario
			entidadEnBd = tiendaDao.findOne(entidad.getCodigoTienda());
			entidadEnBd.setTipo(entidad.getTipo());
			entidadEnBd.setPiso(entidad.getPiso());
			entidadEnBd.setArea(entidad.getArea());
			entidadEnBd.setNumero(entidad.getNumero());
			entidadEnBd.setObservacion(entidad.getObservacion());
			//Suministro
			suministro = suministroDao.findOne(entidad.getTblSuministro().getCodigoSuministro());
			entidadEnBd.setTblSuministro(suministro);
			
			this.preEditar(entidadEnBd, request);
			boolean exitoso = super.guardar(entidadEnBd, model);
			model.addAttribute("edificio", edificio);
			log.debug("[guardarEntidad] Guardado..." );
			if (exitoso){
				
				//List<TblTienda> entidades = tiendaDao.buscarOneByNumero(entidadEnBd.getTblEdificio().getCodigoEdificio(),entidadEnBd.getNumero());
				
				edificio = edificioDao.findOne(entidad.getTblEdificio().getCodigoEdificio());
				filtro = new Filtro();
				filtro.setCodigo(edificio.getCodigoEdificio());
				model.addAttribute("edificio", edificio);
				filtro.setNumero(entidadEnBd.getNumero());
				this.listarTiendas(model, filtro, new PageableSG(), this.urlPaginado);
				//model.addAttribute("registros", entidades);
				campos = configurarCamposConsulta();
				model.addAttribute("mapEstadoTienda", obtenerValoresEstadoTienda());
				model.addAttribute("contenido", campos);
				model.addAttribute("filtro", filtro);
			}else{
				path = "mantenimiento/tienda/tie_edicion";
				model.addAttribute("entidad", entidad);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
		
	}
	
	/**
	 * Se encarga de la eliminacion logica del Tienda
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "tienda/eliminar/{id}", method = RequestMethod.GET)
	public String eliminarTienda(@PathVariable Integer id, HttpServletRequest request, Model model, PageableSG pageable) {
		TblTienda entidad			= null;
		String path 				= null;
		Map<String, Object> campos 	= null;
		//List<TblSuministro> listaSuministro = null;
		Filtro filtro 				= null;
		TblEdificio edificio 		= null;
		try{
			log.debug("[eliminarTienda] Inicio");
			entidad = tiendaDao.findOne(id);
			entidad.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			this.preEditar(entidad, request);
			tiendaDao.save(entidad);
			model.addAttribute("respuesta", "Eliminación exitosa");
			
			edificio = edificioDao.findOne(entidad.getTblEdificio().getCodigoEdificio());
			filtro = new Filtro();
			filtro.setCodigo(edificio.getCodigoEdificio());
			model.addAttribute("edificio", edificio);
			this.listarTiendas(model, filtro, pageable, urlPaginado);
			campos = configurarCamposConsulta();
			model.addAttribute("mapEstadoTienda", obtenerValoresEstadoTienda());
			model.addAttribute("contenido", campos);
			model.addAttribute("filtro", filtro);
			request.getSession().setAttribute("sessionFiltroCriterio", filtro);
			path = "mantenimiento/tienda/tie_listado";
			
			/*List<TblTienda> entidades = tiendaDao.listarAllActivos(entidad.getTblEdificio().getCodigoEdificio());
			log.debug("[eliminarTienda] entidades:"+entidades);
			model.addAttribute("registros", entidades);
			path = "mantenimiento/tienda/tie_listado";
			filtro = new Filtro();
			filtro.setCodigo(entidad.getTblEdificio().getCodigoEdificio());
			model.addAttribute("filtro", filtro);
			model.addAttribute("edificio", entidad.getTblEdificio());
			campos = configurarCamposConsulta();
			model.addAttribute("mapEstadoTienda", obtenerValoresEstadoTienda());
			model.addAttribute("contenido", campos);
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_TIENDA);
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PISO);
			listaSuministro = suministroDao.listarAllActivos();
			model.addAttribute("mapListaSuministro", ListaUtilAction.obtenerValoresListaSuministroActivo(listaSuministro));*/
			log.debug("[eliminarTienda] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;
			campos		= null;
			//listaSuministro = null;
		}
		return path;
	}
	

	@Override
	public TblTienda getNuevaEntidad() {
		return new TblTienda();
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
		campo = new Campo("combo_estado_tienda", "estadoTienda", false);
		campos.put("Estado Tienda", campo);
		
		
		return campos;
	}
	/**
	 * Listado de estado tienda
	 * 
	 */
	public Map<String, Object> obtenerValoresEstadoTienda() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		resultados.put(Constantes.DESC_ESTADO_TIENDA_OCUPADO, Constantes.ESTADO_TIENDA_OCUPADO);
		resultados.put(Constantes.DESC_ESTADO_TIENDA_DESOCUPADO, Constantes.ESTADO_TIENDA_DESOCUPADO);
		return resultados;
	}
	/*
	 * Paginado
	 */
	@RequestMapping(value = "/tiendas/paginado/{page}/{size}/{operacion}", method = RequestMethod.GET)
	public String paginarEntidad(@PathVariable Integer page, @PathVariable Integer size, @PathVariable String operacion, Model model,  PageableSG pageable, HttpServletRequest request) {
		Filtro filtro = null;
		String path = null;
		TblEdificio edificio = null;
		Map<String, Object> campos = null;
		try{
			//log.debug("[traerRegistros] Inicio");
			path = "mantenimiento/tienda/tie_listado";
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
			model.addAttribute("mapEstadoTienda", obtenerValoresEstadoTienda());
			model.addAttribute("contenido", campos);
			this.listarTiendas(model, filtro, pageable,this.urlPaginado);
			edificio = edificioDao.findOne(filtro.getCodigo());
			model.addAttribute("edificio", edificio);
			//log.debug("[traerRegistros] Fin");
		}catch(Exception e){
			//log.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
			edificio = null;
		}
		return path;
	}
	
	
}
