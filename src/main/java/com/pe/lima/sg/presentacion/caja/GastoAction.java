package com.pe.lima.sg.presentacion.caja;

import static com.pe.lima.sg.dao.mantenimiento.GastoSpecifications.conEdificio;
import static com.pe.lima.sg.dao.mantenimiento.GastoSpecifications.conEstado;
import static com.pe.lima.sg.dao.mantenimiento.GastoSpecifications.conFechaGasto;
import static com.pe.lima.sg.dao.mantenimiento.GastoSpecifications.conCodigoInterno;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

import com.pe.lima.sg.bean.caja.GastoBean;
import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.dao.mantenimiento.IEdificioDAO;
import com.pe.lima.sg.dao.mantenimiento.IGastoDAO;
//import com.pe.lima.sg.entity.cliente.TblContrato;
import com.pe.lima.sg.entity.mantenimiento.TblEdificio;
import com.pe.lima.sg.entity.mantenimiento.TblGasto;
import com.pe.lima.sg.presentacion.BaseOperacionPresentacion;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.PageWrapper;
import com.pe.lima.sg.presentacion.util.PageableSG;
import com.pe.lima.sg.presentacion.util.UtilSGT;

import lombok.extern.slf4j.Slf4j;


/**
 * Clase Bean que se encarga de la administracion de la caja chica
 *
 * 			
 */
@Slf4j
@Controller
//@PreAuthorize("hasAuthority('CRUD')")
public class GastoAction extends BaseOperacionPresentacion<TblGasto> {
	
	
	@Autowired
	private IGastoDAO gastoDao;
	
	@Autowired
	private IEdificioDAO edificioDao;

	private String urlPaginado = "/gastos/paginado/"; 
	
	@SuppressWarnings("rawtypes")
	@Override
	public BaseOperacionDAO getDao() {
		return gastoDao;
	}
	
	/**
	 * Se encarga de listar todos los registros de caja chica
	 * 
	 * @param model
	 * @return
	 */
	

	@RequestMapping(value = "/gastos", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path, HttpServletRequest request) {
		Filtro filtro = null;
		
		try{
			log.debug("[traerRegistros] Inicio");
			path = "caja/gasto/gas_listado";
			
			filtro = new Filtro();
			request.getSession().setAttribute("CriterioFiltroGasto",filtro);
			filtro.setFechaGasto(UtilSGT.addMonths(new Date(), -1));
			filtro.setNumero("1");
			model.addAttribute("registros",null);
			model.addAttribute("page", null);
			model.addAttribute("filtro", filtro);
			
			request.getSession().setAttribute("ListadoGasto",null);
			request.getSession().setAttribute("PageGasto",null);
			
			log.debug("[traerRegistros] Fin");
		}catch(Exception e){
			log.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		
		return path;
	}
	
	
	/*** Listado de Registro de Gasto ***/
	private void listarGasto(Model model, Filtro entidad,  PageableSG pageable, String url, HttpServletRequest request){
		List<TblGasto> entidades = new ArrayList<TblGasto>();
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "codigoGasto"));
		try{
			Specification<TblGasto> filtro = Specifications.where(conEdificio(entidad.getCodigoEdificacion()))
					.and(conFechaGasto(entidad.getFechaGasto()))
					.and(conEstado(Constantes.ESTADO_REGISTRO_ACTIVO));
			pageable.setSort(sort);
			Page<TblGasto> entidadPage = gastoDao.findAll(filtro, pageable);
			PageWrapper<TblGasto> page = new PageWrapper<TblGasto>(entidadPage, url, pageable);
			List<GastoBean> lista = this.procesarListaGasto(page.getContent(), request);
			model.addAttribute("registros", lista);
			model.addAttribute("page", page);
			
			log.debug("[listarGasto] entidades:"+entidades);
			request.getSession().setAttribute("CriterioFiltroGasto", entidad);
			request.getSession().setAttribute("ListadoGasto", lista);
			request.getSession().setAttribute("PageGasto", page);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades = null;
		}
	}

	/*** Listado de Registro de Gasto ***/
	private void listarGastoxCodigoInterno(Model model, String codigoInterno,  PageableSG pageable, String url, HttpServletRequest request){
		List<TblGasto> entidades = new ArrayList<TblGasto>();
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "codigoGasto"));
		try{
			Specification<TblGasto> filtro = Specifications.where(conCodigoInterno(codigoInterno));
			pageable.setSort(sort);
			Page<TblGasto> entidadPage = gastoDao.findAll(filtro, pageable);
			PageWrapper<TblGasto> page = new PageWrapper<TblGasto>(entidadPage, url, pageable);
			model.addAttribute("registros", this.procesarListaGasto(page.getContent(), request));
			model.addAttribute("page", page);
			
			log.debug("[listarGastoxCodigoInterno] entidades:"+entidades);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades = null;
		}
	}

	@SuppressWarnings("unchecked")
	private List<GastoBean> procesarListaGasto(List<TblGasto> listaGasto, HttpServletRequest request){
		List<GastoBean> lista = new ArrayList<>();
		GastoBean newGasto = new GastoBean();
		Map<Integer, String> mapTipoGasto = null;
		if (listaGasto !=null){
			mapTipoGasto = (Map<Integer, String>) request.getSession().getAttribute("SessionMapConceptoAllMap");
			
			for(TblGasto gasto : listaGasto){
				newGasto = new GastoBean();
				newGasto.setFechaGasto(gasto.getFechaGasto());
				newGasto.setNombreTipoGasto(mapTipoGasto.get(gasto.getCodigoTipoGasto()));
				newGasto.setObservacion(gasto.getObservacion());
				newGasto.setMonto(gasto.getMonto());
				newGasto.setTipoMoneda(gasto.getTipoMoneda().equals("SO")?"SOLES":"DOLARES");
				newGasto.setCodigoGasto(gasto.getCodigoGasto());
				newGasto.setNombreEdificacion(this.getNombreEdificio(gasto.getTblEdificio()));
				//newGasto.setNombreEdificacion(gasto.getTblEdificio().getNombre());
				lista.add(newGasto);
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
	 * Se encarga de buscar la informacion del gasto segun el filtro
	 * seleccionado
	 * 
	 */
	@RequestMapping(value = "/gastos/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, Filtro filtro, String path ,  PageableSG pageable,HttpServletRequest request) {
		path = "caja/gasto/gas_listado";
		try{
			log.debug("[traerRegistrosFiltrados] Inicio");
			if (filtro.getNumero().isEmpty()){
				filtro.setNumero("1");
			}
			filtro.setFechaGasto(UtilSGT.addMonths(new Date(), new Integer(filtro.getNumero())*(-1)));
			this.listarGasto(model, filtro, pageable, this.urlPaginado, request);
			model.addAttribute("filtro", filtro);
			log.debug("[traerRegistrosFiltrados] Fin");
		}catch(Exception e){
			log.debug("[traerRegistrosFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			
		}
		log.debug("[traerRegistrosFiltrados] Fin");
		return path;
	}
	
	
	/**
	 * Se encarga de direccionar a la pantalla de edicion de gasto
	 * 
	 */
	@RequestMapping(value = "/gasto/editar/{id}", method = RequestMethod.GET)
	public String editarGasto(@PathVariable Integer id, Model model) {
		TblGasto entidad 			= null;
		String path 					= "caja/gasto/gas_edicion";
		try{
			entidad = gastoDao.findOne(id);
			if (entidad.getFechaGasto().before(UtilSGT.getFechaYYYYMMDD()) || entidad.getFechaGasto().after(UtilSGT.getFechaYYYYMMDD()) ){
				path = "caja/gasto/gas_ver";
			}else{
				path = "caja/gasto/gas_edicion";
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
	 * Se encarga de direccionar a la pantalla de creacion de un registro de gasto
	 * 
	 */
	@RequestMapping(value = "/gasto/nuevo", method = RequestMethod.GET)
	public String crearGasto(Model model) {
		TblEdificio edificio = new TblEdificio();
		try{
			log.debug("[crearGasto] Inicio");
			edificio.setCodigoEdificio(Constantes.CODIGO_INMUEBLE_LA_REYNA);
			TblGasto gasto = new TblGasto();
			gasto.setTblEdificio(edificio);
			gasto.setFechaGasto(new Date());
			gasto.setCodigoTipoGasto(Constantes.CODIGO_TIPO_GASTO_DIVERSO);
			gasto.setTipoMoneda(Constantes.MONEDA_SOL);
			model.addAttribute("entidad", gasto);
			log.debug("[crearGasto] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "caja/gasto/gas_nuevo";
	}
	
	@Override
	public void preGuardar(TblGasto entidad, HttpServletRequest request) {
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
	public boolean validarNegocio(Model model,TblGasto entidad, HttpServletRequest request){
		boolean exitoso = true;
		try{
			
			if (entidad.getTblEdificio().getCodigoEdificio() <= 0){
				model.addAttribute("respuesta", "Debe seleccionar el inmueble");
				return false;
			}
			if (entidad.getFechaGasto() == null || entidad.getFechaGasto().toString().isEmpty()){
				model.addAttribute("respuesta", "Debe ingresar la fecha del gasto");
				return false;
			}
			if (entidad.getCodigoTipoGasto() == null || entidad.getCodigoTipoGasto().compareTo(-1) == 0){
				model.addAttribute("respuesta", "Debe ingresar el tipo de gasto");
				return false;
			}
			if (entidad.getMonto() == null || entidad.getMonto().doubleValue() <= 0){
				model.addAttribute("respuesta", "Debe ingresar el monto del gasto");
				return false;
			}
			if (entidad.getTipoMoneda() == null || entidad.getTipoMoneda().isEmpty()){
				model.addAttribute("respuesta", "Debe ingresar el tipo de moneda");
				return false;
			}
			
			
		}catch(Exception e){
			exitoso = false;
		}
		return exitoso;
	}
	/**
	 * Se encarga de guardar la informacion del gasto
	 * 
	 */
	@RequestMapping(value = "/gasto/nuevo/guardar", method = RequestMethod.POST)
	public String guardarEntidad(Model model, TblGasto entidad, HttpServletRequest request, String path , PageableSG pageable) {
		path = "caja/gasto/gas_listado";
		try{
			log.debug("[guardarEntidad] Inicio" );

			if (this.validarNegocio(model, entidad, request)){
				log.debug("[guardarEntidad] Pre Guardar..." );
				entidad.setCodigoInterno(UUID.randomUUID().toString());
				this.preGuardar(entidad, request);
				boolean exitoso = super.guardar(entidad, model);
				log.debug("[guardarEntidad] Guardado..." );
				if (exitoso){
					this.listarGastoxCodigoInterno(model, entidad.getCodigoInterno(), pageable, this.urlPaginado, request);
					model.addAttribute("filtro", request.getSession().getAttribute("CriterioFiltroGasto"));
				}else{
					path = "caja/gasto/gas_nuevo";
					model.addAttribute("entidad", entidad);
					
				}
			}else{
				path = "caja/gasto/gas_nuevo";
				model.addAttribute("entidad", entidad);
			}
			
			log.debug("[guardarEntidad] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
		
	}
	
	@Override
	public void preEditar(TblGasto entidad, HttpServletRequest request) {
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
	
	@RequestMapping(value = "/gasto/nuevo/editar", method = RequestMethod.POST)
	public String editarEntidad(TblGasto entidad, Model model, HttpServletRequest request, PageableSG pageable) {
		String path 				= "caja/gasto/gas_listado";;
		TblGasto entidadEnBd 	= null;
		try{
			if (this.validarNegocio(model, entidad, request)){
				// Se actualizan solo los campos del formulario
				entidadEnBd = gastoDao.findOne(entidad.getCodigoGasto());
				entidadEnBd.setFechaGasto(entidad.getFechaGasto());
				entidadEnBd.setCodigoTipoGasto(entidad.getCodigoTipoGasto());
				entidadEnBd.setMonto(entidad.getMonto());
				entidadEnBd.setTipoMoneda(entidad.getTipoMoneda());
				entidadEnBd.setObservacion(entidad.getObservacion());
				this.preEditar(entidadEnBd, request);
				boolean exitoso = super.guardar(entidadEnBd, model);
				log.debug("[guardarEntidad] Guardado..." );
				if (exitoso){
					
					this.listarGastoxCodigoInterno(model, entidadEnBd.getCodigoInterno(), pageable, this.urlPaginado, request);
					
					model.addAttribute("filtro", request.getSession().getAttribute("CriterioFiltroGasto"));
				}else{
					path = "caja/gasto/gas_edicion";
					model.addAttribute("entidad", entidad);
				}
			}else{
				path = "caja/gasto/gas_edicion";
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
	@RequestMapping(value = "/gasto/eliminar/{id}", method = RequestMethod.GET)
	public String eliminarGasto(@PathVariable Integer id, HttpServletRequest request, Model model, PageableSG pageable) {
		TblGasto entidad		= null;
		String path 				= null;
		Filtro filtro				= null;
		try{
			log.debug("[eliminarGasto] Inicio");
			path = "caja/gasto/gas_listado";
			
			entidad = gastoDao.findOne(id);
			entidad.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			this.preEditar(entidad, request);
			gastoDao.save(entidad);
			model.addAttribute("respuesta", "Eliminación exitosa");
			model.addAttribute("filtro", request.getSession().getAttribute("CriterioFiltroGasto"));
			filtro = (Filtro)request.getSession().getAttribute("CriterioFiltroGasto");
			this.traerRegistrosFiltrados(model, filtro, path, pageable, request);
			log.debug("[eliminarGasto] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;
			filtro		= null;
		}
		return path;
	}
	

	@Override
	public TblGasto getNuevaEntidad() {
		return new TblGasto();
	}
	/*
	 * Paginado
	 */
	@RequestMapping(value = "/gastos/paginado/{page}/{size}/{operacion}", method = RequestMethod.GET)
	public String paginarEntidad(@PathVariable Integer page, @PathVariable Integer size, @PathVariable String operacion, Model model,  PageableSG pageable, HttpServletRequest request) {
		Filtro filtro = null;
		String path = null;
		try{
			//log.debug("[traerRegistros] Inicio");
			path = "caja/gasto/gas_listado";
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
			filtro = (Filtro)request.getSession().getAttribute("CriterioFiltroGasto");
			this.listarGasto(model, filtro, pageable, this.urlPaginado, request);
			model.addAttribute("filtro", filtro);
			
			
			
		}catch(Exception e){
			//log.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		return path;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gastos/regresar", method = RequestMethod.GET)
	public String regresar(Model model, String path, HttpServletRequest request) {
		Filtro filtro = null;
		List<GastoBean> lista = null;
		PageWrapper<TblGasto> page = null;
		try{
			log.debug("[regresar] Inicio");
			path = "caja/gasto/gas_listado";
			
			filtro = (Filtro)request.getSession().getAttribute("CriterioFiltroGasto");
			model.addAttribute("filtro", filtro);
			lista = (List<GastoBean>)request.getSession().getAttribute("ListadoGasto");
			model.addAttribute("registros",lista);
			page = (PageWrapper<TblGasto>) request.getSession().getAttribute("PageGasto");
			model.addAttribute("page", page);
			
			
			log.debug("[regresar] Fin");
		}catch(Exception e){
			log.debug("[regresar] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		
		return path;
	}
}
