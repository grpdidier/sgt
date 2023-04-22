package com.pe.lima.sg.presentacion.cliente;

import static com.pe.lima.sg.dao.cliente.LuzSpecifications.conEstado;
import static com.pe.lima.sg.dao.cliente.LuzSpecifications.conFechaFin;
import static com.pe.lima.sg.dao.cliente.LuzSpecifications.conNumero;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pe.lima.sg.dao.BaseDAO;
import com.pe.lima.sg.dao.cliente.ILuzDAO;
import com.pe.lima.sg.dao.mantenimiento.ISuministroDAO;
import com.pe.lima.sg.entity.cliente.TblLuz;
import com.pe.lima.sg.presentacion.BasePresentacion;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.ListaUtilAction;
import com.pe.lima.sg.presentacion.util.UtilSGT;

/**
 * Clase Bean que se encarga de la administracion del registro del monto de la luz por luz
 *
 * 			
 */
@Controller
public class LuzAction extends BasePresentacion<TblLuz> {
	
	private static final Logger logger = LogManager.getLogger(LuzAction.class);
	
	@Autowired
	private ILuzDAO luzDao;
	
	@Autowired
	private ISuministroDAO suministroDao;
	
	@Autowired
	private ListaUtilAction listaUtil;
	
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDAO getDao() {
		return null;
	}
	
	/**
	 * Se encarga de listar todos los luces
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cliente/luces", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path) {
		Filtro filtro = null;
		try{
			logger.debug("[traerRegistros] Inicio");
			path = "cliente/luz/luz_listado";
			filtro = new Filtro();
			model.addAttribute("filtro", filtro);
			filtro.setCodigoEdificacionFiltro(-1);
			filtro.setAnioFiltro(Calendar.getInstance().get(Calendar.YEAR));
			model.addAttribute("mapFechaVencimiento", obtenerFechaVencimiento(filtro.getAnioFiltro()));
			this.listarLuces(model, filtro);
			logger.debug("[traerRegistros] Fin");
		}catch(Exception e){
			logger.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		
		return path;
	}
	/**
	 * Se encarga de buscar la informacion del Luz segun el filtro
	 * seleccionado
	 * 
	 * @param model
	 * @param luzBean
	 * @return
	 */
	@RequestMapping(value = "/cliente/luces/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, Filtro filtro, String path,HttpServletRequest request) {
		path = "cliente/luz/luz_listado";
		try{
			logger.debug("[traerRegistrosFiltrados] Inicio");
			this.listarLuces(model, filtro);
			model.addAttribute("filtro", filtro);
			model.addAttribute("mapFechaVencimiento", obtenerFechaVencimiento(filtro.getAnioFiltro()));
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
	/*** Listado de Luces ***/
	private void listarLuces(Model model, Filtro filtro){
		List<TblLuz> entidades = new ArrayList<TblLuz>();
		Date datFechaFin = null;
		Specification<TblLuz> criterio = null;
		try{
			if (filtro.getStrFechaFinFiltro().equals("-1")){
				criterio = Specifications.where(conNumero(filtro.getNumeroFiltro()))
						.and(conEstado(Constantes.ESTADO_REGISTRO_ACTIVO));
			}else{
				datFechaFin = UtilSGT.getDatetoString(filtro.getStrFechaFinFiltro());
				criterio = Specifications.where(conNumero(filtro.getNumeroFiltro()))
						.and(conFechaFin(datFechaFin))
						.and(conEstado(Constantes.ESTADO_REGISTRO_ACTIVO));
			}
			
			entidades = luzDao.findAll(criterio);
			logger.debug("[listarLuces] entidades:"+entidades);
			model.addAttribute("registros", entidades);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades = null;
		}
	}
	
	
	/**
	 * Se encarga de direccionar a la pantalla de edicion del Luz
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "cliente/luz/editar/{id}", method = RequestMethod.GET)
	public String editarLuz(@PathVariable Integer id, Model model) {
		TblLuz entidad 			= null;
		try{
			entidad = luzDao.findOne(id);
			model.addAttribute("entidad", entidad);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return "cliente/luz/luz_edicion";
	}
	
	
	/**
	 * Se encarga de direccionar a la pantalla de creacion del Luz
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "cliente/luz/nuevo", method = RequestMethod.GET)
	public String crearLuz(Model model) {
		try{
			logger.debug("[crearLuz] Inicio");
			model.addAttribute("entidad", new TblLuz());
			logger.debug("[crearLuz] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "cliente/luz/luz_nuevo";
	}
	
	@Override
	public void preGuardar(TblLuz entidad, HttpServletRequest request) {
		try{
			logger.debug("[preGuardar] Inicio" );
			entidad.setFechaCreacion(new Date(System.currentTimeMillis()));
			entidad.setIpCreacion(request.getRemoteAddr());
			entidad.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			//Obteniendo el año
			entidad.setAnio(UtilSGT.getAnioDate(entidad.getFechaFin()));
			//Estado del cobro
			entidad.setEstadoLuz(Constantes.ESTADO_ACTIVO);
			logger.debug("[preGuardar] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/** Validamos logica del negocio**/
	@Override
	public boolean validarNegocio(Model model,TblLuz entidad, HttpServletRequest request){
		boolean exitoso = true;
		//Integer total 	= null;
		try{
			//Validando la existencia del luz
			
			if(entidad.getTblSuministro().getCodigoSuministro() <= 0){
				exitoso = false;
				model.addAttribute("respuesta", "Debe seleccionar el suministro");
			}else if(entidad.getMontoGenerado()==null){
				exitoso = false;
				model.addAttribute("respuesta", "Debe ingresar el monto de la Luz");
			}else if(entidad.getTipoMoneda().equals("-1")){
				exitoso = false;
				model.addAttribute("respuesta", "Debe seleccionar el tipo de moneda");
			}else if(entidad.getSaldo() == null){
				exitoso = false;
				model.addAttribute("respuesta", "Debe ingresar el saldo");
			}
			
		}catch(Exception e){
			exitoso = false;
		}finally{
			//total = null;
		}
		return exitoso;
	}
	/**
	 * Se encarga de guardar la informacion del Luz
	 * 
	 * @param luzBean
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "luz/nuevo/guardar", method = RequestMethod.POST)
	public String guardarEntidad(Model model, TblLuz entidad, HttpServletRequest request, String path) {
		//Map<String, Object> campos 	= null;
		path = "cliente/luz/luz_listado";
		Filtro filtro = null;
		try{
			logger.debug("[guardarEntidad] Inicio" );
			if (this.validarNegocio(model, entidad, request)){
				logger.debug("[guardarEntidad] Pre Guardar..." );
				entidad.setTblSuministro(suministroDao.findOne(entidad.getTblSuministro().getCodigoSuministro()));
				this.preGuardar(entidad, request);
				boolean exitoso = super.guardar(entidad, model);
				logger.debug("[guardarEntidad] Guardado..." );
				if (exitoso){
					filtro = new Filtro();
					model.addAttribute("filtro", filtro);
					filtro.setCodigoEdificacionFiltro(-1);
					filtro.setAnioFiltro(Calendar.getInstance().get(Calendar.YEAR));
					model.addAttribute("mapFechaVencimiento", obtenerFechaVencimiento(filtro.getAnioFiltro()));
					this.listarLuces(model, filtro);
				}else{
					path = "cliente/luz/luz_nuevo";
					listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
					model.addAttribute("entidad", entidad);
					
				}
			}else{
				path = "cliente/luz/luz_nuevo";
				listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
				model.addAttribute("entidad", entidad);
			}
			
			logger.debug("[guardarEntidad] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//campos 			= null;
		}
		return path;
		
	}
	
	@Override
	public void preEditar(TblLuz entidad, HttpServletRequest request) {
		try{
			logger.debug("[preEditar] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			//Obteniendo el año
			entidad.setAnio(UtilSGT.getAnioDate(entidad.getFechaFin()));
			//Estado del cobro
			entidad.setEstadoLuz(Constantes.ESTADO_ACTIVO);
			logger.debug("[preEditar] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "luz/nuevo/editar", method = RequestMethod.POST)
	public String editarEntidad(TblLuz entidad, Model model, HttpServletRequest request) {
		//Map<String, Object> campos 	= null;
		String path 				= "cliente/luz/luz_listado";;
		TblLuz entidadEnBd 		= null;
		Filtro filtro = null;
		try{
			// Se actualizan solo los campos del formulario
			entidadEnBd = luzDao.findOne(entidad.getCodigoLuz());
			entidadEnBd.setEstadoLuz(entidad.getEstadoLuz());
			entidadEnBd.setAnio(entidad.getAnio());
			entidadEnBd.setFechaFin(entidad.getFechaFin());
			entidadEnBd.setMontoGenerado(entidad.getMontoGenerado());
			entidadEnBd.setSaldo(entidad.getSaldo());
			
			this.preEditar(entidadEnBd, request);
			boolean exitoso = super.guardar(entidadEnBd, model);
			logger.debug("[guardarEntidad] Guardado..." );
			if (exitoso){
				filtro = new Filtro();
				model.addAttribute("filtro", filtro);
				filtro.setCodigoEdificacionFiltro(-1);
				filtro.setAnioFiltro(Calendar.getInstance().get(Calendar.YEAR));
				model.addAttribute("mapFechaVencimiento", obtenerFechaVencimiento(filtro.getAnioFiltro()));
				this.listarLuces(model, filtro);
			}else{
				path = "cliente/luz/luz_edicion";
				listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_USUARIO);
				model.addAttribute("entidad", entidad);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
		
	}
	
	/**
	 * Se encarga de la eliminacion logica del Luz
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "cliente/luz/eliminar/{id}", method = RequestMethod.GET)
	public String eliminarLuz(@PathVariable Integer id, HttpServletRequest request, Model model) {
		TblLuz entidad				= null;
		String path 				= null;
		//Map<String, Object> campos 	= null;
		Filtro filtro				= null;
		try{
			logger.debug("[eliminarLuz] Inicio");
			entidad = luzDao.findOne(id);
			entidad.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			this.preEditar(entidad, request);
			luzDao.save(entidad);
			model.addAttribute("respuesta", "Eliminación exitosa");
			
			filtro = new Filtro();
			model.addAttribute("filtro", filtro);
			filtro.setCodigoEdificacionFiltro(-1);
			filtro.setAnioFiltro(Calendar.getInstance().get(Calendar.YEAR));
			model.addAttribute("mapFechaVencimiento", obtenerFechaVencimiento(filtro.getAnioFiltro()));
			this.listarLuces(model, filtro);
			
			logger.debug("[eliminarLuz] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;
			//campos		= null;
		}
		return path;
	}
	

	@Override
	public TblLuz getNuevaEntidad() {
		return new TblLuz();
	}
	
	public Map<String, Object> obtenerFechaVencimiento(Integer intAnio) {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		String strAnio = null;
		try{
			logger.debug("[obtenerFechaVencimiento] inicio");
			strAnio = intAnio.toString();
			resultados.put("31/01/"+strAnio, "31/01/"+strAnio);
			if(intAnio % 4 == 0){
				resultados.put("29/02/"+strAnio, "29/02/"+strAnio);
			}else{
				resultados.put("28/02/"+strAnio, "28/02/"+strAnio);
			}
			resultados.put("31/03/"+strAnio, "31/03/"+strAnio);
			resultados.put("30/04/"+strAnio, "30/04/"+strAnio);
			resultados.put("31/05/"+strAnio, "31/05/"+strAnio);
			resultados.put("30/06/"+strAnio, "30/06/"+strAnio);
			resultados.put("31/07/"+strAnio, "31/07/"+strAnio);
			resultados.put("31/08/"+strAnio, "31/08/"+strAnio);
			resultados.put("30/09/"+strAnio, "30/09/"+strAnio);
			resultados.put("31/10/"+strAnio, "31/10/"+strAnio);
			resultados.put("30/11/"+strAnio, "31/11/"+strAnio);
			resultados.put("31/12/"+strAnio, "31/12/"+strAnio);
			
			logger.debug("[obtenerFechaVencimiento] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
		
		return resultados;
	}
	/**
	 * Se encarga de direccionar a la pantalla de edicion del arbitrio
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cliente/luz/anio/{cadena}", method = RequestMethod.GET)
	public String anioAnteriorPosterior(@PathVariable String cadena, Model model) {
		Filtro filtro 		= null;
		String operacion	= null;
		String anio			= null;
		try{
			logger.debug("[anioAnteriorPosterior] Inicio" );
			operacion = cadena.substring(0, 1);
			anio = cadena.substring(1);
			logger.debug("[anioAnteriorPosterior] operacion:"+operacion );
			logger.debug("[anioAnteriorPosterior] anio: "+anio );
			filtro = new Filtro();
			model.addAttribute("filtro", filtro);
			filtro.setCodigoEdificacionFiltro(-1);
			
			
			if (operacion.equals(Constantes.PAGINADO_ANTERIOR)){
				filtro.setAnioFiltro(new Integer(anio) - 1);
			}else{
				if(operacion.equals(Constantes.PAGINADO_SIGUIENTE)){
					filtro.setAnioFiltro(new Integer(anio) + 1);
					
				}else{
					filtro.setAnioFiltro(new Integer(1900));
				}
			}
			
			model.addAttribute("mapFechaVencimiento", obtenerFechaVencimiento(filtro.getAnioFiltro()));
			
			this.listarLuces(model, filtro);
			logger.debug("[anioAnteriorPosterior] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		return "cliente/luz/luz_listado";
	}	
	
}
