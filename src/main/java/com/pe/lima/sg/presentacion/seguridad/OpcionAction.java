package com.pe.lima.sg.presentacion.seguridad;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pe.lima.sg.dao.BaseDAO;
import com.pe.lima.sg.dao.seguridad.IOpcionDAO;
import com.pe.lima.sg.entity.seguridad.TblOpcion;
import com.pe.lima.sg.presentacion.BasePresentacion;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.UtilSGT;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
//@PreAuthorize("hasAuthority('CRUD')")
public class OpcionAction extends BasePresentacion<TblOpcion> {
	@Autowired
	private IOpcionDAO opcionDao;


	@SuppressWarnings("rawtypes")
	@Override
	public BaseDAO getDao() {
		return opcionDao;
	}

	/**
	 * Se encarga de listar todos los opciones
	 * 
	 * @param model
	 * @param path
	 * @return
	 */
	@RequestMapping(value = "/opciones", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path) {
		String strCadena = "";
		List<TblOpcion> listaOpciones 	= null;
		try{
			log.debug("[traerRegistros] Inicio");
			path = "seguridad/opcion/opc_listado";
			listaOpciones = opcionDao.listarAllActivos();
			
			strCadena = this.generarArbol(listaOpciones);
			
			model.addAttribute("cadena", strCadena);
			log.debug("[traerRegistros] Fin");
		}catch(Exception e){
			log.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			listaOpciones		= null;
		}

		return path;
	}
	/**
	 * Generamos el arbol del menu
	 */
	private String generarArbol(List<TblOpcion> listaOpciones){
		String strResultado	= null;
		log.debug("[generarArbol] Inicio ");
		try{
			strResultado = Constantes.MENU_CABECERA_INI;
			strResultado = strResultado + this.getOpcionesRecursivo(1000, listaOpciones);
			strResultado = strResultado + Constantes.MENU_CABECERA_FIN;
		}catch(Exception e){
			
		}
		log.debug("[generarArbol] Fin - strResultado: "+strResultado);
		return strResultado;
	}
	
	private String getOpcionesRecursivo(Integer intModulo, List<TblOpcion> listaOpciones){
		String strResultado = "";
		log.debug("[getOpcionesRecursivo] Inicio - Modulo: "+intModulo);
		for(TblOpcion opcion: listaOpciones){
			if (opcion.getModulo().compareTo(intModulo)==0){
				//Se valida si es nodo u hoja
				if (opcion.getRuta()==null || opcion.getRuta().equals("")){
					//Si es nodo
					strResultado = strResultado + "<li id=\"child_node_"+opcion.getCodigoOpcion()+"\"> <a href=\"#\" onclick=\"jsOpcionModuloListado('opcion',"+opcion.getCodigoOpcion()+");\">"+opcion.getNombre()+"</a> ";
					//strResultado = strResultado + "<li>"+ opcion.getNombre();
					String strTemporal = getOpcionesRecursivo(opcion.getCodigoOpcion(),listaOpciones);
					if (strTemporal.length()>0){
						strResultado = strResultado +"	<ul>" + strTemporal + "</ul>";
					}
					//strResultado = strResultado + getOpcionesRecursivo(opcion.getCodigoOpcion(),listaOpciones);
					strResultado = strResultado + "</li>";
					
				}else{
					//Si es hoja
					//strResultado = strResultado + "<li id=\"child_node_"+opcion.getCodigoOpcion()+"\"> <a href=\"#\" onclick=\"jsOpcionMenu('"+opcion.getRuta()+"');\">"+opcion.getNombre()+"</a> </li>";
					strResultado = strResultado + "<li>"+ opcion.getNombre() + " <img src=\"/images/iconos/hoja.png\" alt=\"Hoja\" width=\"20px\"/> </li>";
					
				}
			}
		}
		log.debug("[getOpcionesRecursivo] Fin - resultado: "+strResultado);
		return strResultado;
	}
	
	
	/**
	 * Se encarga de listar todos los opciones
	 * 
	 * @param model
	 * @param path
	 * @return
	 */
	@RequestMapping(value = "/opcion/{id}", method = RequestMethod.GET)
	public String listarOpciones(@PathVariable Integer id, HttpServletRequest request, Model model) {
		List<TblOpcion> listaOpciones 	= null;
		TblOpcion opcion 				= null;
		try{
			log.debug("[listarOpciones] Inicio");
			log.debug("[listarOpciones] id:"+id);
			listaOpciones = opcionDao.listarAllSubModulos(id);
			model.addAttribute("registros", listaOpciones);
			opcion = opcionDao.findOne(id);
			if (opcion == null){
				opcion = this.getOpcionRaiz();
			}
			model.addAttribute("opcion", opcion);

			log.debug("[listarOpciones] Fin");
		}catch(Exception e){
			log.debug("[listarOpciones] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			listaOpciones		= null;
			opcion				= null;
		}

		return "seguridad/opcion/opc_opciones";
	}

	@Override
	public TblOpcion getNuevaEntidad() {
		return new TblOpcion();
	}

	/**
	 * Se encarga de direccionar a la pantalla de creacion del Opciones
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "opcion/nuevo/{id}", method = RequestMethod.GET)
	public String crearOpcion(@PathVariable Integer id, Model model) {
		TblOpcion opcion 				= null;
		TblOpcion opcionNuevo			= new TblOpcion();
		try{
			opcion = opcionDao.findOne(id);
			if (opcion == null){
				opcion = this.getOpcionRaiz();
			}
			model.addAttribute("opcion", opcion);
			opcionNuevo.setModulo(opcion.getCodigoOpcion());
			opcionNuevo.setNivel(opcion.getNivel()+1);
			model.addAttribute("entidad", opcionNuevo);

		}catch(Exception e){

		}finally{

		}

		return "seguridad/opcion/opc_nuevo";
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

	@Override
	public void preGuardar(TblOpcion entidad, HttpServletRequest request) {
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
	public boolean validarNegocio(Model model,TblOpcion entidad, HttpServletRequest request){
		boolean exitoso = true;
		Integer total 	= null;
		try{
			//Validando la existencia del opcion
			total = opcionDao.countOneByLogin(entidad.getNombre());
			if (total > 0){
				exitoso = false;
				model.addAttribute("respuesta", "El Opcion existe, debe modificarlo para continuar...");
			}

		}catch(Exception e){
			exitoso = false;
		}finally{
			total = null;
		}
		return exitoso;
	}
	/**
	 * Se encarga de guardar la informacion del Opcion
	 * 
	 * @param opcionBean
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "opcion/nuevo/guardar", method = RequestMethod.POST)
	public String guardarEntidad(Model model, TblOpcion entidad, HttpServletRequest request, String path) {
		TblOpcion opcion 			= null;
		path = "seguridad/opcion/opc_opciones";
		try{
			log.debug("[guardarEntidad] Inicio" );
			if (this.validarNegocio(model, entidad, request)){
				log.debug("[guardarEntidad] Pre Guardar..." );
				this.preGuardar(entidad, request);
				boolean exitoso = super.guardar(entidad, model);
				log.debug("[guardarEntidad] Guardado..." );
				if (exitoso){
					List<TblOpcion> entidades = opcionDao.listarAllSubModulos(entidad.getModulo());
					model.addAttribute("registros", entidades);
					opcion = opcionDao.findOne(entidad.getModulo());
					if (opcion == null){
						opcion = this.getOpcionRaiz();
					}
					model.addAttribute("opcion", opcion);
				}else{
					path = "seguridad/opcion/opc_nuevo";
					model.addAttribute("entidad", entidad);

				}
			}else{
				path = "seguridad/opcion/opc_nuevo";
				model.addAttribute("entidad", entidad);
			}

			log.debug("[guardarEntidad] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			opcion 			= null;
		}
		return path;

	}
	/**
	 * Se encarga de direccionar a la pantalla de edicion del Opcion
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "opcion/editar/{id}", method = RequestMethod.GET)
	public String editarOpcion(@PathVariable Integer id, Model model) {
		TblOpcion entidad 			= null;
		try{
			entidad = opcionDao.findOne(id);
			model.addAttribute("entidad", entidad);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return "seguridad/opcion/opc_edicion";
	}

	@Override
	public void preEditar(TblOpcion entidad, HttpServletRequest request) {
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

	@RequestMapping(value = "opcion/nuevo/editar", method = RequestMethod.POST)
	public String editarEntidad(TblOpcion entidad, Model model, HttpServletRequest request) {
		String path 				= "seguridad/opcion/opc_opciones";;
		TblOpcion entidadEnBd 		= null;
		TblOpcion opcion 			= null;

		try{
			// Se actualizan solo los campos del formulario
			entidadEnBd = opcionDao.findOne(entidad.getCodigoOpcion());
			entidadEnBd.setNombre(entidad.getNombre());
			entidadEnBd.setRuta(entidad.getRuta());
			this.preEditar(entidadEnBd, request);
			boolean exitoso = super.guardar(entidadEnBd, model);
			log.debug("[guardarEntidad] Guardado..." );
			if (exitoso){

				List<TblOpcion> entidades = opcionDao.listarAllSubModulos(entidadEnBd.getModulo());
				model.addAttribute("registros", entidades);
				opcion = opcionDao.findOne(entidad.getModulo());
				if (opcion == null){
					opcion = this.getOpcionRaiz();
				}
				model.addAttribute("opcion", opcion);
			}else{
				path = "seguridad/opcion/opc_edicion";
				model.addAttribute("entidad", entidad);
			}

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidadEnBd	= null;
			opcion		= null;
		}
		return path;

	}
	/**
	 * Se encarga de la eliminacion logica del Opcion
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "opcion/eliminar/{id}", method = RequestMethod.GET)
	public String eliminarOpcion(@PathVariable Integer id, HttpServletRequest request, Model model) {
		TblOpcion entidad			= null;
		String path 				= null;
		TblOpcion opcion 			= null;
		try{
			log.debug("[eliminarOpcion] Inicio");
			entidad = opcionDao.findOne(id);
			entidad.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			this.preEditar(entidad, request);
			opcionDao.save(entidad);
			model.addAttribute("respuesta", "Eliminación exitosa");
			List<TblOpcion> entidades = opcionDao.listarAllSubModulos(entidad.getModulo());
			log.debug("[eliminarOpcion] entidades:"+entidades);
			model.addAttribute("registros", entidades);
			path = "seguridad/opcion/opc_opciones";
			opcion = opcionDao.findOne(entidad.getModulo());
			if (opcion == null){
				opcion = this.getOpcionRaiz();
			}
			model.addAttribute("opcion", opcion);
			log.debug("[eliminarOpcion] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;
			opcion		= null;
		}
		return path;
	}
}
