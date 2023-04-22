package com.pe.lima.sg.presentacion.caja;

import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.dao.caja.ICajaChicaDAO;
import com.pe.lima.sg.dao.caja.ICajaChicaDetalleDAO;
import com.pe.lima.sg.dao.mantenimiento.IConceptoDAO;
import com.pe.lima.sg.dao.mantenimiento.ITipoCambioDAO;
import com.pe.lima.sg.entity.caja.TblCajaChica;
import com.pe.lima.sg.entity.caja.TblDetalleCajaChica;
import com.pe.lima.sg.entity.mantenimiento.TblConcepto;
import com.pe.lima.sg.entity.mantenimiento.TblTipoCambio;
import com.pe.lima.sg.presentacion.BaseOperacionPresentacion;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;

import com.pe.lima.sg.presentacion.util.RestResponse;
import com.pe.lima.sg.presentacion.util.UtilSGT;

/**
 * Clase Bean que se encarga de la administracion de la caja chica
 *
 * 			
 */
@Controller
//@PreAuthorize("hasAuthority('CRUD')")
public class CajaChicaDetalleAction extends BaseOperacionPresentacion<TblDetalleCajaChica> {
	private static final Logger logger = LogManager.getLogger(CajaChicaDetalleAction.class);
	
	@Autowired
	private ICajaChicaDetalleDAO cajaChicaDetalleDao;
	
	@Autowired
	private ICajaChicaDAO cajaChicaDao;
	
	@Autowired
	private IConceptoDAO conceptoDao;
	
	@Autowired
	private ITipoCambioDAO tipoCambioDao;
		
	@SuppressWarnings("rawtypes")
	@Override
	public BaseOperacionDAO getDao() {
		return cajaChicaDetalleDao;
	}
	
	/**
	 * Se encarga de listar todos los registros de caja chica - Ingreso o Salida
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/caja/ccdetalles/{id}", method = RequestMethod.GET)
	public String traerRegistros(@PathVariable Integer id,Model model, String path) {
		Filtro filtro = null;
		List<TblDetalleCajaChica> entidades;
		try{
			logger.debug("[traerRegistros] Inicio");
			path = "caja/ccdetalle/ccd_listado";
			filtro = new Filtro();
			filtro.setCodigo(id);
			model.addAttribute("filtro", filtro);
			entidades = cajaChicaDetalleDao.listarAllActivosxCajaChica(id);
			model.addAttribute("registros", entidades);
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
	 * Se encarga de buscar la informacion de la caja chica segun el filtro
	 * seleccionado
	 * 
	 * @param model
	 * @param ccdetalleBean
	 * @return
	 */
	@RequestMapping(value = "/caja/ccdetalles/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, Filtro filtro, String path,HttpServletRequest request) {
		path = "caja/ccdetalle/ccd_listado";
		List<TblDetalleCajaChica> entidades;
		//List<TblTipoCambio> listaTipoCambio		= null;
		try{
			logger.debug("[traerRegistrosFiltrados] Inicio");
			//listaTipoCambio = tipoCambioDao.buscarOneByFecha(UtilSGT.getDatetoString(UtilSGT.getFecha("dd/MM/YYYY")));
			
			entidades = cajaChicaDetalleDao.listarAllActivosxCajaChicaxTipoOperacionxConcepto(filtro.getCodigo(), filtro.getTipoOperacion());
			model.addAttribute("filtro", filtro);
			model.addAttribute("registros", entidades);
			logger.debug("[traerRegistrosFiltrados] Fin");
		}catch(Exception e){
			logger.debug("[traerRegistrosFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			entidades = null;
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
	@RequestMapping(value = "/caja/ccdetalle/ver/{id}", method = RequestMethod.GET)
	public String editarCajaChica(@PathVariable Integer id, Model model) {
		TblDetalleCajaChica entidad 			= null;
		try{
			entidad = cajaChicaDetalleDao.findOne(id);
			model.addAttribute("entidad", entidad);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return "caja/ccdetalle/ccd_ver";
	}
	
	
	/**
	 * Se encarga de direccionar a la pantalla de creacion de un registro de caja chica
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/caja/ccdetalle/nuevo/gasto/", method = RequestMethod.POST)
	public String crearCajaChicaGasto(Filtro filtro, Model model, HttpServletRequest request) {
		TblDetalleCajaChica entidad = null;
		try{
			logger.debug("[crearCajaChica] Inicio");
			entidad = new TblDetalleCajaChica();
			entidad.setTblCajaChica(cajaChicaDao.findOne(filtro.getCodigo()));
			request.getSession().setAttribute("SessionMapConcepto", request.getSession().getAttribute("SessionMapConceptoGasto"));
			model.addAttribute("entidad", entidad);
			entidad.setTblConcepto(new TblConcepto());
			entidad.setTipoOperacion(Constantes.CAJA_CHICA_GASTO);
			logger.debug("[crearCajaChica] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return "caja/ccdetalle/ccd_nuevo";
	}
	/**
	 * Se encarga de direccionar a la pantalla de creacion de un registro de caja chica
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/caja/ccdetalle/nuevo/ingreso/", method = RequestMethod.POST)
	public String crearCajaChicaIngreso(Filtro filtro, Model model, HttpServletRequest request) {
		TblDetalleCajaChica entidad = null;
		try{
			logger.debug("[crearCajaChica] Inicio");
			entidad = new TblDetalleCajaChica();
			request.getSession().setAttribute("SessionMapConcepto", request.getSession().getAttribute("SessionMapConceptoIngreso"));
			entidad.setTblCajaChica(cajaChicaDao.findOne(filtro.getCodigo()));
			entidad.setTblConcepto(new TblConcepto());
			entidad.setTipoOperacion(Constantes.CAJA_CHICA_INGRESO);
			model.addAttribute("entidad", entidad);
			logger.debug("[crearCajaChica] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return "caja/ccdetalle/ccd_nuevo";
	}
	@Override
	public void preGuardar(TblDetalleCajaChica entidad, HttpServletRequest request) {
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
	public void preEditarCajaChica(TblCajaChica entidad, HttpServletRequest request) {
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
	/** Validamos logica del negocio**/
	/*@Override
	public boolean validarNegocio(Model model,TblCajaChica entidad, HttpServletRequest request){
		boolean exitoso = true;
		Integer total 	= null;
		try{
			//Validando la existencia del ccdetalle
			total = cajaChicaDetalleDao.countAnioMesNombreCajaChica(entidad.getAnio(), entidad.getMes(), entidad.getNombre());
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
	}*/
	/**
	 * Se encarga de guardar la informacion de la caja chica
	 * 
	 * @param ccdetalleBean
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/caja/ccdetalle/nuevo/guardar", method = RequestMethod.POST)
	public String guardarEntidad(Model model, TblDetalleCajaChica entidad, HttpServletRequest request, String path) {
		path = "caja/ccdetalle/ccd_listado";
		List<TblDetalleCajaChica> entidades = null;
		Filtro filtro = null;
		List<TblTipoCambio> listaTipoCambio		= null;
		TblCajaChica cajaChica = null;
		try{
			logger.debug("[guardarEntidad] Inicio" );
			//Tipo de Cambio
			listaTipoCambio = tipoCambioDao.buscarOneByFecha(UtilSGT.getDatetoString(UtilSGT.getFecha("dd/MM/YYYY")));
			if(listaTipoCambio == null || listaTipoCambio.size()<=0){
				model.addAttribute("respuesta", "No se ha encontrado el Tipo de Cambio, registre antes de continuar.");
				path = "caja/ccdetalle/ccd_nuevo";
				model.addAttribute("entidad", entidad);
			}else{
				entidad.setDescripcion(entidad.getDescripcion().toUpperCase());
				if (listaTipoCambio!=null){
					if(entidad.getTipoMoneda().equals(Constantes.MONEDA_SOL)){
						entidad.setMontoDolares(entidad.getMontoSoles().divide(listaTipoCambio.get(0).getValor(), 2, RoundingMode.HALF_UP));
						entidad.setTipoCambio(listaTipoCambio.get(0).getValor());
					}
					if(entidad.getTipoMoneda().equals(Constantes.MONEDA_DOLAR)){
						entidad.setMontoDolares(entidad.getMontoSoles());
						entidad.setMontoSoles(entidad.getMontoSoles().multiply(listaTipoCambio.get(0).getValor()));
						entidad.setTipoCambio(listaTipoCambio.get(0).getValor());
					}
				}
				//if (this.validarNegocio(model, entidad, request)){
					logger.debug("[guardarEntidad] Pre Guardar..." );
					this.preGuardar(entidad, request);
					cajaChica = cajaChicaDao.findOne(entidad.getTblCajaChica().getCodigoCajaChica());
					entidad.setTblCajaChica(cajaChica);
					boolean exitoso = super.guardar(entidad, model);
					logger.debug("[guardarEntidad] Guardado..." );
					if (exitoso){
						if (entidad.getTipoOperacion().equals(Constantes.CAJA_CHICA_INGRESO)){
							cajaChica.setTotalIngresoSoles(cajaChica.getTotalIngresoSoles().add(entidad.getMontoSoles()));
							cajaChica.setTotalIngresoDolares(cajaChica.getTotalIngresoDolares().add(entidad.getMontoDolares()));
							cajaChica.setSaldoDolares(cajaChica.getSaldoDolares().add(entidad.getMontoDolares()));
							cajaChica.setSaldoSoles(cajaChica.getSaldoSoles().add(entidad.getMontoSoles()));
						}
						if (entidad.getTipoOperacion().equals(Constantes.CAJA_CHICA_GASTO)){
							cajaChica.setTotalSalidaSoles(cajaChica.getTotalSalidaSoles().add(entidad.getMontoSoles()));
							cajaChica.setTotalSalidaDolares(cajaChica.getTotalSalidaDolares().add(entidad.getMontoDolares()));
							cajaChica.setSaldoDolares(cajaChica.getSaldoDolares().subtract(entidad.getMontoDolares()));
							cajaChica.setSaldoSoles(cajaChica.getSaldoSoles().subtract(entidad.getMontoSoles()));
						}
						this.preEditarCajaChica(cajaChica, request);
						cajaChicaDao.save(cajaChica);
						entidades = cajaChicaDetalleDao.listarAllActivosxCajaChica(entidad.getTblCajaChica().getCodigoCajaChica());
						model.addAttribute("registros", entidades);
						filtro = new Filtro();
						filtro.setCodigo(entidad.getTblCajaChica().getCodigoCajaChica());
						model.addAttribute("filtro", filtro);
					}else{
						path = "caja/ccdetalle/ccd_nuevo";
						model.addAttribute("entidad", entidad);
						
					}
			}
			
			/*}else{
				path = "caja/ccdetalle/ccd_nuevo";
				model.addAttribute("entidad", entidad);
			}*/
			
			logger.debug("[guardarEntidad] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades 	= null;
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
	@RequestMapping(value = "/caja/ccdetalle/eliminar/{id}", method = RequestMethod.GET)
	public String eliminarDetalleCajaChica(@PathVariable Integer id, HttpServletRequest request, Model model) {
		TblDetalleCajaChica entidad		= null;
		String path 				= null;
		Filtro filtro				= null;
		List<TblDetalleCajaChica> entidades = null;
		TblCajaChica cajaChica = null;
		try{
			logger.debug("[eliminarCajaChica] Inicio");
			path = "caja/ccdetalle/ccd_listado";
			
			entidad = cajaChicaDetalleDao.findOne(id);
			entidad.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			this.preEditar(entidad, request);
			cajaChicaDetalleDao.save(entidad);
			cajaChica = cajaChicaDao.findOne(entidad.getTblCajaChica().getCodigoCajaChica());
			if (entidad.getTipoOperacion().equals(Constantes.CAJA_CHICA_INGRESO)){
				cajaChica.setTotalIngresoSoles(cajaChica.getTotalIngresoSoles().subtract(entidad.getMontoSoles()));
				cajaChica.setTotalIngresoDolares(cajaChica.getTotalIngresoDolares().subtract(entidad.getMontoDolares()));
				cajaChica.setSaldoDolares(cajaChica.getSaldoDolares().subtract(entidad.getMontoDolares()));
				cajaChica.setSaldoSoles(cajaChica.getSaldoSoles().subtract(entidad.getMontoSoles()));
			}
			if (entidad.getTipoOperacion().equals(Constantes.CAJA_CHICA_GASTO)){
				cajaChica.setTotalSalidaSoles(cajaChica.getTotalSalidaSoles().subtract(entidad.getMontoSoles()));
				cajaChica.setTotalSalidaDolares(cajaChica.getTotalSalidaDolares().subtract(entidad.getMontoDolares()));
				cajaChica.setSaldoDolares(cajaChica.getSaldoDolares().add(entidad.getMontoDolares()));
				cajaChica.setSaldoSoles(cajaChica.getSaldoSoles().add(entidad.getMontoSoles()));
			}
			this.preEditarCajaChica(cajaChica, request);
			cajaChicaDao.save(cajaChica);
			
			model.addAttribute("respuesta", "Eliminación exitosa");
			filtro = new Filtro();
			model.addAttribute("filtro", filtro);
			entidades = cajaChicaDetalleDao.listarAllActivosxCajaChica(entidad.getTblCajaChica().getCodigoCajaChica());
			model.addAttribute("registros", entidades);
			filtro.setCodigo(entidad.getTblCajaChica().getCodigoCajaChica());
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
	public TblDetalleCajaChica getNuevaEntidad() {
		return new TblDetalleCajaChica();
	}
	
	@RequestMapping("/concepto/{id}")
	public RestResponse  obtenerConcepto(@PathVariable String id) {
		 //instantiate the response object
		RestResponse response = new RestResponse();
        List<TblConcepto> listaConcepto = null;
        
        //ConceptoBean conceptoBean = null;
        listaConcepto = conceptoDao.buscarxTipo(id);
        
         
        if (listaConcepto == null) {
            //the URL contains an unknown employee id - we'll return an empty response
            response.setResponseStatus(RestResponse.NOT_FOUND);
            response.setResponse("");
        } else {
            //good response if we get here
            response.setResponseStatus(RestResponse.OK);
            response.setResponse(listaConcepto);
        }
         
        return response;
	}	
}
