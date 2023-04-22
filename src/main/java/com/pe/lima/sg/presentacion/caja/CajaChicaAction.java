package com.pe.lima.sg.presentacion.caja;

import static com.pe.lima.sg.dao.caja.CajaChicaSpecifications.conAnio;
import static com.pe.lima.sg.dao.caja.CajaChicaSpecifications.conEstado;
import static com.pe.lima.sg.dao.caja.CajaChicaSpecifications.conMes;
import static com.pe.lima.sg.dao.caja.CajaChicaSpecifications.conNombre;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.dao.caja.ICajaChicaDAO;
import com.pe.lima.sg.entity.caja.TblCajaChica;
import com.pe.lima.sg.presentacion.BaseOperacionPresentacion;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.UtilSGT;

/**
 * Clase Bean que se encarga de la administracion de la caja chica
 *
 * 			
 */
@Controller
//@PreAuthorize("hasAuthority('CRUD')")
public class CajaChicaAction extends BaseOperacionPresentacion<TblCajaChica> {
	private static final Logger logger = LogManager.getLogger(CajaChicaAction.class);
	@Autowired
	private ICajaChicaDAO cajaChicaDao;

		
	@SuppressWarnings("rawtypes")
	@Override
	public BaseOperacionDAO getDao() {
		return cajaChicaDao;
	}
	
	/**
	 * Se encarga de listar todos los registros de caja chica
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/caja/cchicas", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path) {
		Filtro filtro = null;
		try{
			logger.debug("[traerRegistros] Inicio");
			path = "caja/cchica/cch_listado";
			filtro = new Filtro();
			model.addAttribute("filtro", filtro);
			this.listarCajaChica(model, filtro);
			logger.debug("[traerRegistros] Fin");
		}catch(Exception e){
			logger.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		
		return path;
	}
	
	
	/*** Listado de Registro de caja chica ***/
	private void listarCajaChica(Model model, Filtro entidad){
		List<TblCajaChica> entidades = new ArrayList<TblCajaChica>();
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "codigoCajaChica"));
		try{
			Specification<TblCajaChica> filtro = Specifications.where(conNombre(entidad.getNombre()))
					.or(conAnio(entidad.getAnioFiltro()==null?0:entidad.getAnioFiltro()))
					.or(conMes(new Integer(entidad.getMesFiltro()==null?"0":entidad.getMesFiltro())))
					.and(conEstado(Constantes.ESTADO_REGISTRO_ACTIVO));
			entidades = cajaChicaDao.findAll(filtro,sort);
			logger.debug("[listarCajaChica] entidades:"+entidades);
			model.addAttribute("registros", entidades);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades = null;
		}
	}
	
	/**
	 * Se encarga de buscar la informacion de la caja chica segun el filtro
	 * seleccionado
	 * 
	 * @param model
	 * @param cchicaBean
	 * @return
	 */
	@RequestMapping(value = "/caja/cchicas/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, Filtro filtro, String path,HttpServletRequest request) {
		path = "caja/cchica/cch_listado";
		try{
			logger.debug("[traerRegistrosFiltrados] Inicio");
			this.listarCajaChica(model, filtro);
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
	 * Se encarga de direccionar a la pantalla de edicion de caja chica
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/caja/cchica/editar/{id}", method = RequestMethod.GET)
	public String editarCajaChica(@PathVariable Integer id, Model model) {
		TblCajaChica entidad 			= null;
		String path 					= "caja/cchica/cch_edicion";
		try{
			entidad = cajaChicaDao.findOne(id);
			if (entidad.getEstadoCaja().compareTo(Constantes.CAJA_CHICA_ABIERTO)==0){
				path = "caja/cchica/cch_edicion";
			}else{
				path = "caja/cchica/cch_ver";
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
	 * Se encarga de direccionar a la pantalla de creacion de un registro de caja chica
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/caja/cchica/nuevo", method = RequestMethod.GET)
	public String crearCajaChica(Model model) {
		try{
			logger.debug("[crearCajaChica] Inicio");
			model.addAttribute("entidad", new TblCajaChica());
			logger.debug("[crearCajaChica] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "caja/cchica/cch_nuevo";
	}
	
	@Override
	public void preGuardar(TblCajaChica entidad, HttpServletRequest request) {
		try{
			logger.debug("[preGuardar] Inicio" );
			entidad.setFechaCreacion(new Date(System.currentTimeMillis()));
			entidad.setIpCreacion(request.getRemoteAddr());
			entidad.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			entidad.setEstadoCaja(Constantes.CAJA_CHICA_ABIERTO);
			logger.debug("[preGuardar] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/** Validamos logica del negocio**/
	@Override
	public boolean validarNegocio(Model model,TblCajaChica entidad, HttpServletRequest request){
		boolean exitoso = true;
		Integer total 	= null;
		try{
			//Validando la existencia del cchica
			total = cajaChicaDao.countAnioMesNombreCajaChica(entidad.getAnio(), entidad.getMes(), entidad.getNombre());
			if (total > 0){
				exitoso = false;
				model.addAttribute("respuesta", "El registro de caja chica existe, debe modificarlo para continuar...");
			}
			
		}catch(Exception e){
			exitoso = false;
		}finally{
			total = null;
		}
		return exitoso;
	}
	/**
	 * Se encarga de guardar la informacion de la caja chica
	 * 
	 * @param cchicaBean
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/caja/cchica/nuevo/guardar", method = RequestMethod.POST)
	public String guardarEntidad(Model model, TblCajaChica entidad, HttpServletRequest request, String path) {
		path = "caja/cchica/cch_listado";
		List<TblCajaChica> entidades = null;
		try{
			logger.debug("[guardarEntidad] Inicio" );
			entidad.setNombre(entidad.getNombre().toUpperCase());
			if (this.validarNegocio(model, entidad, request)){
				logger.debug("[guardarEntidad] Pre Guardar..." );
				this.preGuardar(entidad, request);
				boolean exitoso = super.guardar(entidad, model);
				logger.debug("[guardarEntidad] Guardado..." );
				if (exitoso){
					entidades = cajaChicaDao.listarAnioMesNombreCajaChica(entidad.getAnio(), entidad.getMes(), entidad.getNombre());
					model.addAttribute("registros", entidades);
					model.addAttribute("filtro", new Filtro());
				}else{
					path = "caja/cchica/cch_nuevo";
					model.addAttribute("entidad", entidad);
					
				}
			}else{
				path = "caja/cchica/cch_nuevo";
				model.addAttribute("entidad", entidad);
			}
			
			logger.debug("[guardarEntidad] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades 	= null;
		}
		return path;
		
	}
	
	@Override
	public void preEditar(TblCajaChica entidad, HttpServletRequest request) {
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
	
	@RequestMapping(value = "/caja/cchica/nuevo/editar", method = RequestMethod.POST)
	public String editarEntidad(TblCajaChica entidad, Model model, HttpServletRequest request) {
		List<TblCajaChica> entidades = null;
		String path 				= "caja/cchica/cch_listado";;
		TblCajaChica entidadEnBd 	= null;
		try{
			// Se actualizan solo los campos del formulario
			entidadEnBd = cajaChicaDao.findOne(entidad.getCodigoCajaChica());
			entidadEnBd.setNombre(entidad.getNombre().toUpperCase());
			entidadEnBd.setAnio(entidad.getAnio());
			entidadEnBd.setMes(entidad.getMes());
			entidadEnBd.setEstadoCaja(entidad.getEstadoCaja());
			this.preEditar(entidadEnBd, request);
			boolean exitoso = super.guardar(entidadEnBd, model);
			logger.debug("[guardarEntidad] Guardado..." );
			if (exitoso){
				
				entidades = cajaChicaDao.listarAnioMesNombreCajaChica(entidadEnBd.getAnio(), entidadEnBd.getMes(), entidadEnBd.getNombre());
				model.addAttribute("registros", entidades);
				model.addAttribute("filtro", new Filtro());
			}else{
				path = "caja/cchica/cch_edicion";
				model.addAttribute("entidad", entidad);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			entidades	= null;
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
	@RequestMapping(value = "/caja/cchica/eliminar/{id}", method = RequestMethod.GET)
	public String eliminarCajaChica(@PathVariable Integer id, HttpServletRequest request, Model model) {
		TblCajaChica entidad		= null;
		String path 				= null;
		Filtro filtro				= null;
		try{
			logger.debug("[eliminarCajaChica] Inicio");
			path = "caja/cchica/cch_listado";
			
			entidad = cajaChicaDao.findOne(id);
			entidad.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			this.preEditar(entidad, request);
			cajaChicaDao.save(entidad);
			model.addAttribute("respuesta", "Eliminación exitosa");
			filtro = new Filtro();
			model.addAttribute("filtro", filtro);
			this.listarCajaChica(model, filtro);
			logger.debug("[eliminarCajaChica] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;
			filtro		= null;
		}
		return path;
	}
	

	@Override
	public TblCajaChica getNuevaEntidad() {
		return new TblCajaChica();
	}
	
	
}
