package com.pe.lima.sg.presentacion.caja;

import static com.pe.lima.sg.dao.mantenimiento.IngresoSpecifications.conEdificio;
import static com.pe.lima.sg.dao.mantenimiento.IngresoSpecifications.conEstado;
import static com.pe.lima.sg.dao.mantenimiento.IngresoSpecifications.conFechaIngreso;
import static com.pe.lima.sg.dao.mantenimiento.IngresoSpecifications.conCodigoInterno;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

import com.pe.lima.sg.bean.caja.IngresoBean;
import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.dao.mantenimiento.IEdificioDAO;
import com.pe.lima.sg.dao.mantenimiento.IIngresoDAO;
//import com.pe.lima.sg.entity.cliente.TblContrato;
import com.pe.lima.sg.entity.mantenimiento.TblEdificio;
import com.pe.lima.sg.entity.mantenimiento.TblIngreso;
import com.pe.lima.sg.presentacion.BaseOperacionPresentacion;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.PageWrapper;
import com.pe.lima.sg.presentacion.util.PageableSG;
import com.pe.lima.sg.presentacion.util.UtilSGT;


/**
 * Clase Bean que se encarga de la administracion de la caja chica
 *
 * 			
 */
@Controller
//@PreAuthorize("hasAuthority('CRUD')")
public class IngresoAction extends BaseOperacionPresentacion<TblIngreso> {
	
	private static final Logger logger = LogManager.getLogger(IngresoAction.class);
	
	@Autowired
	private IIngresoDAO ingresoDao;
	
	@Autowired
	private IEdificioDAO edificioDao;

	private String urlPaginado = "/ingresos/paginado/"; 
	
	@SuppressWarnings("rawtypes")
	@Override
	public BaseOperacionDAO getDao() {
		return ingresoDao;
	}
	
	/**
	 * Se encarga de listar todos los registros de caja chica
	 * 
	 * @param model
	 * @return
	 */
	

	@RequestMapping(value = "/ingresos", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path, HttpServletRequest request) {
		Filtro filtro = null;
		
		try{
			logger.debug("[traerRegistros] Inicio");
			path = "caja/ingreso/ing_listado";
			
			filtro = new Filtro();
			request.getSession().setAttribute("CriterioFiltroIngreso",filtro);
			filtro.setFechaIngreso(UtilSGT.addMonths(new Date(), -1));
			filtro.setNumero("1");
			model.addAttribute("registros",null);
			model.addAttribute("page", null);
			model.addAttribute("filtro", filtro);
			
			request.getSession().setAttribute("ListadoIngreso",null);
			request.getSession().setAttribute("PageIngreso",null);
			
			logger.debug("[traerRegistros] Fin");
		}catch(Exception e){
			logger.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		
		return path;
	}
	
	
	/*** Listado de Registro de Ingreso ***/
	private void listarIngreso(Model model, Filtro entidad,  PageableSG pageable, String url, HttpServletRequest request){
		List<TblIngreso> entidades = new ArrayList<TblIngreso>();
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "codigoIngreso"));
		try{
			Specification<TblIngreso> filtro = Specifications.where(conEdificio(entidad.getCodigoEdificacion()))
					.and(conFechaIngreso(entidad.getFechaIngreso()))
					.and(conEstado(Constantes.ESTADO_REGISTRO_ACTIVO));
			pageable.setSort(sort);
			Page<TblIngreso> entidadPage = ingresoDao.findAll(filtro, pageable);
			PageWrapper<TblIngreso> page = new PageWrapper<TblIngreso>(entidadPage, url, pageable);
			List<IngresoBean> lista = this.procesarListaIngreso(page.getContent(), request);
			model.addAttribute("registros", lista);
			model.addAttribute("page", page);
			
			logger.debug("[listarIngreso] entidades:"+entidades);
			request.getSession().setAttribute("CriterioFiltroIngreso", entidad);
			request.getSession().setAttribute("ListadoIngreso", lista);
			request.getSession().setAttribute("PageIngreso", page);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades = null;
		}
	}

	/*** Listado de Registro de Ingreso ***/
	private void listarIngresoxCodigoInterno(Model model, String codigoInterno,  PageableSG pageable, String url, HttpServletRequest request){
		List<TblIngreso> entidades = new ArrayList<TblIngreso>();
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "codigoIngreso"));
		try{
			Specification<TblIngreso> filtro = Specifications.where(conCodigoInterno(codigoInterno));
			pageable.setSort(sort);
			Page<TblIngreso> entidadPage = ingresoDao.findAll(filtro, pageable);
			PageWrapper<TblIngreso> page = new PageWrapper<TblIngreso>(entidadPage, url, pageable);
			model.addAttribute("registros", this.procesarListaIngreso(page.getContent(), request));
			model.addAttribute("page", page);
			
			logger.debug("[listarIngresoxCodigoInterno] entidades:"+entidades);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades = null;
		}
	}

	@SuppressWarnings("unchecked")
	private List<IngresoBean> procesarListaIngreso(List<TblIngreso> listaIngreso, HttpServletRequest request){
		List<IngresoBean> lista = new ArrayList<>();
		IngresoBean newIngreso = new IngresoBean();
		Map<Integer, String> mapTipoIngreso = null;
		if (listaIngreso !=null){
			mapTipoIngreso = (Map<Integer, String>) request.getSession().getAttribute("SessionMapConceptoAllMap");
			
			for(TblIngreso ingreso : listaIngreso){
				newIngreso = new IngresoBean();
				newIngreso.setFechaIngreso(ingreso.getFechaIngreso());
				newIngreso.setNombreTipoIngreso(mapTipoIngreso.get(ingreso.getCodigoTipoIngreso()));
				newIngreso.setObservacion(ingreso.getObservacion());
				newIngreso.setMonto(ingreso.getMonto());
				newIngreso.setTipoMoneda(ingreso.getTipoMoneda().equals("SO")?"SOLES":"DOLARES");
				newIngreso.setCodigoIngreso(ingreso.getCodigoIngreso());
				newIngreso.setNombreEdificacion(this.getNombreEdificio(ingreso.getTblEdificio()));
				newIngreso.setTipoPago(ingreso.getTipoPago());
				//newIngreso.setNombreEdificacion(ingreso.getTblEdificio().getNombre());
				lista.add(newIngreso);
			}
		}
		
		return lista;
	}
	
	public String getNombreEdificio(TblEdificio edificio){
		TblEdificio edificioAux = null;
		String resultado = null;
		if (edificio.getNombre()== null ){
			try{
				if (edificio.getCodigoEdificio() <= 0){
					edificioAux = edificioDao.findOne(1);
				}else{
					edificioAux = edificioDao.findOne(edificio.getCodigoEdificio());
				}
				
				resultado = edificioAux.getNombre();
			}catch(Exception e){
				e.printStackTrace();
				resultado = "-";
			}
		}else{
			resultado = edificio.getNombre();
		}
		return resultado;
	}
	/**
	 * Se encarga de buscar la informacion del ingreso segun el filtro
	 * seleccionado
	 * 
	 */
	@RequestMapping(value = "/ingresos/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, Filtro filtro, String path ,  PageableSG pageable,HttpServletRequest request) {
		path = "caja/ingreso/ing_listado";
		try{
			logger.debug("[traerRegistrosFiltrados] Inicio");
			if (filtro.getNumero().isEmpty()){
				filtro.setNumero("1");
			}
			filtro.setFechaIngreso(UtilSGT.addMonths(new Date(), new Integer(filtro.getNumero())*(-1)));
			this.listarIngreso(model, filtro, pageable, this.urlPaginado, request);
			model.addAttribute("filtro", filtro);
			logger.debug("[traerRegistrosFiltrados] Fin");
		}catch(Exception e){
			logger.debug("[traerRegistrosFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			
		}
		logger.debug("[traerRegistrosFiltrados] Fin");
		return path;
	}
	
	
	/**
	 * Se encarga de direccionar a la pantalla de edicion de ingreso
	 * 
	 */
	@RequestMapping(value = "/ingreso/editar/{id}", method = RequestMethod.GET)
	public String editarIngreso(@PathVariable Integer id, Model model) {
		TblIngreso entidad 			= null;
		String path 					= "caja/ingreso/ing_edicion";
		try{
			entidad = ingresoDao.findOne(id);
			if (entidad.getFechaIngreso().before(UtilSGT.getFechaYYYYMMDD()) || entidad.getFechaIngreso().after(UtilSGT.getFechaYYYYMMDD()) ){
				path = "caja/ingreso/ing_ver";
			}else{
				path = "caja/ingreso/ing_edicion";
			}
			model.addAttribute("entidad", entidad);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return path;
	}
	
	
	/**
	 * Se encarga de direccionar a la pantalla de creacion de un registro de ingreso
	 * 
	 */
	@RequestMapping(value = "/ingreso/nuevo", method = RequestMethod.GET)
	public String crearIngreso(Model model) {
		TblEdificio edificio = new TblEdificio();
		try{
			logger.debug("[crearIngreso] Inicio");
			edificio.setCodigoEdificio(Constantes.CODIGO_INMUEBLE_LA_REYNA);
			TblIngreso ingreso = new TblIngreso();
			ingreso.setTblEdificio(edificio);
			ingreso.setFechaIngreso(new Date());
			ingreso.setCodigoTipoIngreso(Constantes.CODIGO_TIPO_INGRESO_OTROS);
			ingreso.setTipoMoneda(Constantes.MONEDA_SOL);
			ingreso.setTipoPago(Constantes.TIPO_PAGO_COD_EFECTIVO);
			ingreso.setFechaOperacion(new Date());
			model.addAttribute("entidad", ingreso);
			logger.debug("[crearIngreso] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "caja/ingreso/ing_nuevo";
	}
	
	@Override
	public void preGuardar(TblIngreso entidad, HttpServletRequest request) {
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
	public boolean validarNegocio(Model model,TblIngreso entidad, HttpServletRequest request){
		boolean exitoso = true;
		try{
			
			if (entidad.getTblEdificio().getCodigoEdificio() <= 0){
				model.addAttribute("respuesta", "Debe seleccionar el inmueble");
				return false;
			}
			if (entidad.getFechaIngreso() == null || entidad.getFechaIngreso().toString().isEmpty()){
				model.addAttribute("respuesta", "Debe ingresar la fecha del ingreso");
				return false;
			}
			if (entidad.getCodigoTipoIngreso() == null || entidad.getCodigoTipoIngreso().compareTo(-1) == 0){
				model.addAttribute("respuesta", "Debe ingresar el tipo de ingreso");
				return false;
			}
			if (entidad.getMonto() == null || entidad.getMonto().doubleValue() <= 0){
				model.addAttribute("respuesta", "Debe ingresar el monto del ingreso");
				return false;
			}
			if (entidad.getTipoMoneda() == null || entidad.getTipoMoneda().isEmpty()){
				model.addAttribute("respuesta", "Debe ingresar el tipo de moneda");
				return false;
			}
			if (!validarBancarizadoAlquiler(entidad, model)){
				return false;
			}
			
		}catch(Exception e){
			exitoso = false;
		}
		return exitoso;
	}
	
	/*Validamos los datos de la bancarización Alquiler*/
	private boolean validarBancarizadoAlquiler(TblIngreso entidad, Model model){
		//validamos el tipo de pago
		if (!entidad.getTipoPago().equals(Constantes.TIPO_PAGO_COD_EFECTIVO) && 
			!entidad.getTipoPago().equals(Constantes.TIPO_PAGO_COD_BANCARIZADO)){
			model.addAttribute("respuesta", "Debe seleccionar el tipo de pago. ");
			return false;
		}
		//Si es bancarizado
		if (entidad.getTipoPago().equals(Constantes.TIPO_PAGO_COD_BANCARIZADO)){
			if (entidad.getTipoBancarizado().equals("-1")){
				model.addAttribute("respuesta", "Debe seleccionar el tipo de operación. ");
				return false;
			}
			if (entidad.getNumeroOperacion() == null || entidad.getNumeroOperacion().trim().length()<=0){
				model.addAttribute("respuesta", "Debe ingresar el número de la operación. ");
				return false;
			}
			if (entidad.getFechaOperacion() == null ){
				model.addAttribute("respuesta", "Debe ingresar la fecha de operación. ");
				return false;
			}
		}else{
			entidad.setTipoBancarizado(null);
			entidad.setNumeroOperacion(null);
			entidad.setFechaOperacion(null);
		}
		return true;
	}
	
	/**
	 * Se encarga de guardar la informacion del ingreso
	 * 
	 */
	@RequestMapping(value = "/ingreso/nuevo/guardar", method = RequestMethod.POST)
	public String guardarEntidad(Model model, TblIngreso entidad, HttpServletRequest request, String path , PageableSG pageable) {
		path = "caja/ingreso/ing_listado";
		try{
			logger.debug("[guardarEntidad] Inicio" );

			if (this.validarNegocio(model, entidad, request)){
				logger.debug("[guardarEntidad] Pre Guardar..." );
				entidad.setCodigoInterno(UUID.randomUUID().toString());
				this.preGuardar(entidad, request);
				boolean exitoso = super.guardar(entidad, model);
				logger.debug("[guardarEntidad] Guardado..." );
				if (exitoso){
					this.listarIngresoxCodigoInterno(model, entidad.getCodigoInterno(), pageable, this.urlPaginado, request);
					model.addAttribute("filtro", request.getSession().getAttribute("CriterioFiltroIngreso"));
				}else{
					path = "caja/ingreso/ing_nuevo";
					model.addAttribute("entidad", entidad);
					
				}
			}else{
				path = "caja/ingreso/ing_nuevo";
				model.addAttribute("entidad", entidad);
			}
			
			logger.debug("[guardarEntidad] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
		
	}
	
	@Override
	public void preEditar(TblIngreso entidad, HttpServletRequest request) {
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
	
	@RequestMapping(value = "/ingreso/nuevo/editar", method = RequestMethod.POST)
	public String editarEntidad(TblIngreso entidad, Model model, HttpServletRequest request, PageableSG pageable) {
		String path 				= "caja/ingreso/ing_listado";;
		TblIngreso entidadEnBd 	= null;
		try{
			if (this.validarNegocio(model, entidad, request)){
				// Se actualizan solo los campos del formulario
				entidadEnBd = ingresoDao.findOne(entidad.getCodigoIngreso());
				entidadEnBd.setFechaIngreso(entidad.getFechaIngreso());
				entidadEnBd.setCodigoTipoIngreso(entidad.getCodigoTipoIngreso());
				entidadEnBd.setMonto(entidad.getMonto());
				entidadEnBd.setTipoMoneda(entidad.getTipoMoneda());
				entidadEnBd.setObservacion(entidad.getObservacion());
				/*Bancarizado*/
				entidadEnBd.setTipoPago(entidad.getTipoPago());
				entidadEnBd.setTipoBancarizado(entidad.getTipoBancarizado());
				entidadEnBd.setNumeroOperacion(entidad.getNumeroOperacion());
				entidadEnBd.setFechaOperacion(entidad.getFechaOperacion());
				this.preEditar(entidadEnBd, request);
				boolean exitoso = super.guardar(entidadEnBd, model);
				logger.debug("[guardarEntidad] Guardado..." );
				if (exitoso){
					
					this.listarIngresoxCodigoInterno(model, entidadEnBd.getCodigoInterno(), pageable, this.urlPaginado, request);
					
					model.addAttribute("filtro", request.getSession().getAttribute("CriterioFiltroIngreso"));
				}else{
					path = "caja/ingreso/ing_edicion";
					model.addAttribute("entidad", entidad);
				}
			}else{
				path = "caja/ingreso/ing_edicion";
				model.addAttribute("entidad", entidad);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			entidadEnBd	= null;
		}
		return path;
		
	}
	
	/**
	 * Se encarga de la eliminacion logica de la caja chica
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/ingreso/eliminar/{id}", method = RequestMethod.GET)
	public String eliminarIngreso(@PathVariable Integer id, HttpServletRequest request, Model model, PageableSG pageable) {
		TblIngreso entidad		= null;
		String path 				= null;
		Filtro filtro				= null;
		try{
			logger.debug("[eliminarIngreso] Inicio");
			path = "caja/ingreso/ing_listado";
			
			entidad = ingresoDao.findOne(id);
			entidad.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			this.preEditar(entidad, request);
			ingresoDao.save(entidad);
			model.addAttribute("respuesta", "Eliminación exitosa");
			model.addAttribute("filtro", request.getSession().getAttribute("CriterioFiltroIngreso"));
			filtro = (Filtro)request.getSession().getAttribute("CriterioFiltroIngreso");
			this.traerRegistrosFiltrados(model, filtro, path, pageable, request);
			logger.debug("[eliminarIngreso] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;
			filtro		= null;
		}
		return path;
	}
	

	@Override
	public TblIngreso getNuevaEntidad() {
		return new TblIngreso();
	}
	/*
	 * Paginado
	 */
	@RequestMapping(value = "/ingresos/paginado/{page}/{size}/{operacion}", method = RequestMethod.GET)
	public String paginarEntidad(@PathVariable Integer page, @PathVariable Integer size, @PathVariable String operacion, Model model,  PageableSG pageable, HttpServletRequest request) {
		Filtro filtro = null;
		String path = null;
		try{
			//LOGGER.debug("[traerRegistros] Inicio");
			path = "caja/ingreso/ing_listado";
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
			filtro = (Filtro)request.getSession().getAttribute("CriterioFiltroIngreso");
			this.listarIngreso(model, filtro, pageable, this.urlPaginado, request);
			model.addAttribute("filtro", filtro);
			
			
			
		}catch(Exception e){
			//LOGGER.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		return path;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/ingresos/regresar", method = RequestMethod.GET)
	public String regresar(Model model, String path, HttpServletRequest request) {
		Filtro filtro = null;
		List<IngresoBean> lista = null;
		PageWrapper<TblIngreso> page = null;
		try{
			logger.debug("[regresar] Inicio");
			path = "caja/ingreso/ing_listado";
			
			filtro = (Filtro)request.getSession().getAttribute("CriterioFiltroIngreso");
			model.addAttribute("filtro", filtro);
			lista = (List<IngresoBean>)request.getSession().getAttribute("ListadoIngreso");
			model.addAttribute("registros",lista);
			page = (PageWrapper<TblIngreso>) request.getSession().getAttribute("PageIngreso");
			model.addAttribute("page", page);
			
			
			logger.debug("[regresar] Fin");
		}catch(Exception e){
			logger.debug("[regresar] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		
		return path;
	}
}
