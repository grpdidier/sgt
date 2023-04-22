package com.pe.lima.sg.presentacion.caja;

import static com.pe.lima.sg.dao.caja.CxCBitacoraSpecifications.conAnio;
import static com.pe.lima.sg.dao.caja.CxCBitacoraSpecifications.conMes;
import static com.pe.lima.sg.dao.caja.CxCBitacoraSpecifications.conTipoCobro;
import static com.pe.lima.sg.dao.caja.CxCBitacoraSpecifications.conTipoOperacion;
import static com.pe.lima.sg.dao.caja.CxCBitacoraSpecifications.conEstado;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

import com.pe.lima.sg.bean.caja.BitacoraBean;
import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.dao.caja.ICxCBitacoraDAO;
import com.pe.lima.sg.dao.caja.ICxCDocumentoDAO;
import com.pe.lima.sg.dao.caja.ISunatCabeceraDAO;
import com.pe.lima.sg.dao.caja.ISunatDetalleDAO;
import com.pe.lima.sg.dao.cliente.IArbitrioDAO;
import com.pe.lima.sg.dao.cliente.IContratoDAO;
import com.pe.lima.sg.dao.cliente.IContratoServicioDAO;
import com.pe.lima.sg.dao.cliente.ILuzDAO;
import com.pe.lima.sg.dao.cliente.ILuzxTiendaDAO;
import com.pe.lima.sg.dao.mantenimiento.IParametroDAO;
import com.pe.lima.sg.dao.mantenimiento.ISerieDAO;
import com.pe.lima.sg.entity.caja.TblCxcBitacora;
import com.pe.lima.sg.entity.caja.TblCxcDocumento;
import com.pe.lima.sg.entity.caja.TblSunatCabecera;
import com.pe.lima.sg.entity.caja.TblSunatDetalle;
import com.pe.lima.sg.entity.cliente.TblArbitrio;
import com.pe.lima.sg.entity.cliente.TblContrato;
import com.pe.lima.sg.entity.cliente.TblContratoPrimerCobro;
import com.pe.lima.sg.entity.cliente.TblContratoServicio;
import com.pe.lima.sg.entity.cliente.TblLuz;
import com.pe.lima.sg.entity.cliente.TblLuzxtienda;
import com.pe.lima.sg.entity.mantenimiento.TblParametro;
import com.pe.lima.sg.entity.mantenimiento.TblSerie;
import com.pe.lima.sg.presentacion.BaseOperacionPresentacion;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.ListaUtilAction;
import com.pe.lima.sg.presentacion.util.UtilSGT;

/**
 * Clase Bean que se encarga de la administracion de los cxcs
 *
 * 			
 */
@Controller
//@PreAuthorize("hasAuthority('CRUD')")
public class CxcBitacoraAction extends BaseOperacionPresentacion<TblCxcBitacora> {

	private static final Logger logger = LogManager.getLogger(CxcBitacoraAction.class);
	
	@Autowired
	private ICxCBitacoraDAO cxcBitacoraDao;

	@Autowired
	private IContratoDAO contratoDao;
	
	@Autowired
	private ILuzxTiendaDAO luzxTiendaDao;
	
	@Autowired
	private IArbitrioDAO arbitrioDao;
	
	@Autowired
	private ILuzDAO luzDao;
	
	@Autowired
	private IContratoServicioDAO servicioDao;
	
	@Autowired
	private ICxCDocumentoDAO documentoDao;
	
	@Autowired
	private ISunatCabeceraDAO sunatCabeceraDao;
	
	@Autowired
	private ISunatDetalleDAO sunatDetalleDao;
	
	@Autowired
	private ISerieDAO serieDao;

	@Autowired
	private ListaUtilAction listaUtil;


	@Autowired
	private IParametroDAO parametroDao;


	@SuppressWarnings("rawtypes")
	@Override
	public BaseOperacionDAO getDao() {
		return cxcBitacoraDao;
	}

	/**
	 * Se encarga de listar todos los cxcs
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cxcs", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path, HttpServletRequest request) {
		BitacoraBean filtro 				= null;
		List<TblParametro> listaParametro	= null;
		Integer intAnioInicio				= null;
		List<TblCxcBitacora> entidades 		= null;
		try{
			logger.debug("[traerRegistros] Inicio");
			path = "caja/cxc/cxc_listado";
			filtro = new BitacoraBean();
			model.addAttribute("filtro", filtro);
			//Buscando el año de inicio
			listaParametro = parametroDao.buscarOneByNombre(Constantes.PAR_ANIO_INICIO);
			if(listaParametro!=null && listaParametro.size()>0){
				try{
					intAnioInicio = new Integer(listaParametro.get(0).getDato());
				}catch(Exception e1){
					e1.printStackTrace();
					intAnioInicio = Calendar.getInstance().get(Calendar.YEAR);
				}
			}else{
				intAnioInicio = Calendar.getInstance().get(Calendar.YEAR);
			}
			filtro.setAnio(Calendar.getInstance().get(Calendar.YEAR));
			//Buscando el mes en curso
			filtro.setMes(UtilSGT.getMoth());
			//Cargamos las lista del modulo
			this.cargarListaCuentasxCobrar(model, filtro);
			//Listamos los datos
			entidades = listarBitacora(model, filtro);
			model.addAttribute("registros", entidades);
			
			request.getSession().setAttribute("SessionMapAnio", UtilSGT.getListaAnio(intAnioInicio, Calendar.getInstance().get(Calendar.YEAR) + 1 ));
			request.getSession().setAttribute("SessionFiltroCriterio", filtro);
			logger.debug("[traerRegistros] Fin");
		}catch(Exception e){
			logger.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}

		return path;
	}

	/*
	 * Listados para el modulo
	 */
	public void cargarListaCuentasxCobrar(Model model, BitacoraBean filtro){
		//Listado de meses
		listaUtil.cargarDatosModel(model, Constantes.MAP_MESES);
		listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_COBRO_CXC);
		listaUtil.cargarDatosModel(model, Constantes.MAP_SI_NO);
		
	}
	/**
	 * Se encarga de buscar la informacion del Concepto segun el filtro
	 * seleccionado
	 * 
	 * @param model
	 * @param cxcBean
	 * @return
	 */
	@RequestMapping(value = "/cxcs/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, BitacoraBean filtro, String path,HttpServletRequest request) {
		List<TblCxcBitacora> entidades 		= null;
		path = "caja/cxc/cxc_listado";
		try{
			logger.debug("[traerRegistrosFiltrados] Inicio");
			this.cargarListaCuentasxCobrar(model, filtro);
			model.addAttribute("filtro", filtro);
			//Listamos los datos
			entidades = listarBitacora(model, filtro);
			model.addAttribute("registros", entidades);
			request.getSession().setAttribute("SessionFiltroCriterio", filtro);
			logger.debug("[traerRegistrosFiltrados] Fin");
		}catch(Exception e){
			logger.debug("[traerRegistrosFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			entidades		= null;
		}
		logger.debug("[traerRegistrosFiltrados] Fin");
		return path;
	}
	/*** Listado de Bitacora de la CxC ***/
	private List<TblCxcBitacora>  listarBitacora(Model model, BitacoraBean filtro){
		List<TblCxcBitacora> entidades = new ArrayList<TblCxcBitacora>();
		try{
			Specification<TblCxcBitacora> criterio = Specifications.where(conAnio(filtro.getAnio()))
					.and(conMes(new Integer(filtro.getMes())))
					.and(conTipoCobro(filtro.getTipoCobro()))
					.and(conEstado(Constantes.ESTADO_REGISTRO_ACTIVO));
			entidades = cxcBitacoraDao.findAll(criterio);
			logger.debug("[listarBitacora] entidades:"+entidades);
			model.addAttribute("registros", entidades);
		}catch(Exception e){
			e.printStackTrace();
		}
		return entidades;
	}


	/**
	 * Se encarga de direccionar a la pantalla de edicion del Concepto
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	/*@RequestMapping(value = "cxc/editar/{id}", method = RequestMethod.GET)
	public String editarConcepto(@PathVariable Integer id, Model model) {
		TblConcepto entidad 			= null;
		try{
			entidad = cxcDao.findOne(id);
			model.addAttribute("entidad", entidad);
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_CONCEPTO);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return "caja/cxc/cxc_edicion";
	}*/


	/**
	 * Se encarga de direccionar a la pantalla de creacion de una bitacora
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "cxc/nuevo", method = RequestMethod.GET)
	public String crearBitacora(Model model) {
		BitacoraBean entidad = new BitacoraBean();
		try{
			logger.debug("[crearBitacora] Inicio");
			model.addAttribute("entidad", entidad);
			this.cargarListaCuentasxCobrar(model, entidad);
			entidad.setAlquiler(Constantes.TIPO_NO);
			entidad.setArbitrio(Constantes.TIPO_NO);
			entidad.setLuz(Constantes.TIPO_NO);
			entidad.setServicio(Constantes.TIPO_NO);
			entidad.setAnio(Calendar.getInstance().get(Calendar.YEAR));
			entidad.setMes(UtilSGT.getMoth());
			logger.debug("[crearBitacora] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "caja/cxc/cxc_nuevo";
	}

	@Override
	public void preGuardar(TblCxcBitacora entidad, HttpServletRequest request) {
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
	@Override
	public void preEditar(TblCxcBitacora entidad, HttpServletRequest request) {
		try{
			logger.debug("[preEditar] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			//entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			logger.debug("[preEditar] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
	/*
	 * Campos de Auditoria
	 */
	public void preEditarSerie(TblSerie entidad, HttpServletRequest request) {
		try{
			logger.debug("[preGuardar] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			logger.debug("[preGuardar] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	/*
	 * Se asigna los datos del campo de auditoria
	 */
	public void preGuardarDocumento(TblCxcDocumento entidad, HttpServletRequest request) {
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
	/*
	 * Se asigna los datos del campo de auditoria
	 */
	public void preGuardarSunatCabecera(TblSunatCabecera entidad, HttpServletRequest request) {
		try{
			logger.debug("[preGuardarSunatCabecera] Inicio" );
			entidad.setFechaCreacion(new Date(System.currentTimeMillis()));
			entidad.setIpCreacion(request.getRemoteAddr());
			entidad.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			logger.debug("[preGuardarSunatCabecera] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	 * Se asigna los datos del campo de auditoria
	 */
	public void preGuardarSunatDetalle(TblSunatDetalle entidad, HttpServletRequest request) {
		try{
			logger.debug("[preGuardarSunatDetalle] Inicio" );
			entidad.setFechaCreacion(new Date(System.currentTimeMillis()));
			entidad.setIpCreacion(request.getRemoteAddr());
			entidad.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			logger.debug("[preGuardarSunatDetalle] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	 * Valida la pantalla de nuevo
	 */
	public boolean validarNegocio(Model model,BitacoraBean entidad, HttpServletRequest request){
		boolean exitoso = true;
		try{
			if (entidad.getAnio() == -1){
				exitoso = false;
				model.addAttribute("respuesta", "Debe seleccionar el año antes de continuar.");
			}
			if (entidad.getMes().equals("-1")){
				exitoso = false;
				model.addAttribute("respuesta", "Debe seleccionar el mes antes de continuar.");
			}
			if (entidad.getAlquiler().equals("-1") && entidad.getServicio().equals("-1") && entidad.getArbitrio().equals("-1") && entidad.getLuz().equals("-1")){
				exitoso = false;
				model.addAttribute("respuesta", "Debe seleccionar al menos un tipo de cobro antes de continuar");
			}

		}catch(Exception e){
			exitoso = false;
		}
		return exitoso;
	}
	/** Validamos logica del negocio**/
	/*@Override
	public boolean validarNegocio(Model model,TblConcepto entidad, HttpServletRequest request){
		boolean exitoso = true;
		Integer total 	= null;
		try{
			//Validando la existencia del cxc
			total = cxcDao.countOneByNombre(entidad.getNombre());
			if (total > 0){
				exitoso = false;
				model.addAttribute("respuesta", "El Concepto existe, debe modificarlo para continuar...");
			}

		}catch(Exception e){
			exitoso = false;
		}finally{
			total = null;
		}
		return exitoso;
	}*/
	/**
	 * Se encarga de guardar la informacion del Concepto
	 * 
	 * @param cxcBean
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "cxc/nuevo/guardar", method = RequestMethod.POST)
	public String guardarEntidad(Model model, BitacoraBean entidad, HttpServletRequest request, String path) {
		path 								= "caja/cxc/cxc_listado";
		List<TblCxcBitacora> entidades 		= null;
		try{
			logger.debug("[guardarEntidad] Inicio" );
			if (this.validarNegocio(model, entidad, request)){
				logger.debug("[guardarEntidad] Pre Guardar..." );
				
				boolean exitoso = this.procesarTipoCobro(model, entidad, request);
				
				logger.debug("[guardarEntidad] Guardado..." );
				if (exitoso){
					model.addAttribute("filtro", entidad);
					//Listamos los datos
					entidades = listarBitacora(model, entidad);
					model.addAttribute("registros", entidades);
					//Cargamos las lista del modulo
					this.cargarListaCuentasxCobrar(model, entidad);
					
				}else{
					path = "caja/cxc/cxc_nuevo";
					this.cargarListaCuentasxCobrar(model, entidad);
					model.addAttribute("entidad", entidad);
				}
			}else{
				path = "caja/cxc/cxc_nuevo";
				listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_CONCEPTO);
				model.addAttribute("entidad", entidad);
			}

			logger.debug("[guardarEntidad] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
		return path;

	}
	/*
	 * Genera el siguiente valor de la Serie (FXXX999999999)
	 */
	public void obtenerSiguienteFactura(TblSerie entidad){
		try{
			if (entidad.getNumeroComprobante().compareTo(new Integer("999999999"))>=0){
				entidad.setNumeroComprobante(1);
				if (entidad.getSecuencialSerie().compareTo(new Integer("999"))>=0){
					logger.debug("Se excedió el rango de la Serie... ");
					System.exit(0);
				}else{
					entidad.setSecuencialSerie(entidad.getSecuencialSerie()+1);
				}
			}else{
				entidad.setNumeroComprobante(entidad.getNumeroComprobante()+1);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	 * Genera el siguiente valor de la Serie (FXXX999999999)
	 */
	public void obtenerSiguienteBoleta(TblSerie entidad){
		try{
			if (entidad.getNumeroComprobante().compareTo(new Integer("999999999"))>=0){
				entidad.setNumeroComprobante(1);
				if (entidad.getSecuencialSerie().compareTo(new Integer("999"))>=0){
					logger.debug("Se excedió el rango de la Serie... ");
					System.exit(0);
				}else{
					entidad.setSecuencialSerie(entidad.getSecuencialSerie()+1);
				}
			}else{
				entidad.setNumeroComprobante(entidad.getNumeroComprobante()+1);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	 * Genera el siguiente valor de la Serie (FXXX999999999)
	 */
	public void obtenerSiguienteInterno(TblSerie entidad){
		try{
			if (entidad.getNumeroComprobante().compareTo(new Integer("999999999"))>=0){
				entidad.setNumeroComprobante(1);
				if (entidad.getSecuencialSerie().compareTo(new Integer("999"))>=0){
					logger.debug("Se excedió el rango de la Serie... ");
					System.exit(0);
				}else{
					entidad.setSecuencialSerie(entidad.getSecuencialSerie()+1);
				}
			}else{
				entidad.setNumeroComprobante(entidad.getNumeroComprobante()+1);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	 * Registro de la bitacora
	 */
	public boolean registrarBitacora(Model model, BitacoraBean entidad, String tipoCobro, String tipoOperacion, HttpServletRequest request){
		boolean resultado 			= false;
		TblCxcBitacora bitacora		= null;
		try{
			bitacora = new TblCxcBitacora();
			this.preGuardar(bitacora, request);
			bitacora.setAnio(entidad.getAnio());
			bitacora.setMes(new Integer(entidad.getMes()));
			bitacora.setTipoOperacion(tipoOperacion);
			bitacora.setTipoCobro(tipoCobro);
			resultado = super.guardar(bitacora, model);
		}catch(Exception e){
			resultado = false;
			e.printStackTrace();
		}finally{
			bitacora	= null;
		}
		return resultado;
	}
	/*
	 * Registro del documento de Alquiler
	 */
	public TblCxcDocumento registrarCxCDocumentoAlquiler(Model model, BitacoraBean entidad, HttpServletRequest request, TblContrato contrato,TblCxcBitacora bitacora,TblSerie serieFactura,TblSerie serieBoleta,TblSerie serieInterno){
		TblCxcDocumento documento	= null;
		try{
			documento = new TblCxcDocumento();
			documento.setCodigoContrato(contrato.getCodigoContrato());
			documento.setCodigoReferencia(contrato.getCodigoContrato());
			documento.setTipoReferencia(Constantes.TIPO_COBRO_ALQUILER);
			documento.setMontoGenerado(contrato.getMontoAlquiler());
			documento.setMontoContrato(contrato.getMontoAlquiler());
			documento.setMontoCobrado(new BigDecimal("0"));
			documento.setSaldo(contrato.getMontoAlquiler());
			documento.setTipoMoneda(contrato.getTipoMonedaAlquiler());
			documento.setTipoDocumento(contrato.getTipoDocumentoAlquiler());
			//validamos el tipo de documento
			if (documento.getTipoDocumento().equals(Constantes.TIPO_DOC_FACTURA)){
				documento.setTipoComprobante(Constantes.TIPO_COMPROBANTE_FACTURA);
			}else if (documento.getTipoDocumento().equals(Constantes.TIPO_DOC_BOLETA)){
				documento.setTipoComprobante(Constantes.TIPO_COMPROBANTE_BOLETA);
			}else if (documento.getTipoDocumento().equals(Constantes.TIPO_DOC_INTERNO)){
				documento.setTipoComprobante(Constantes.TIPO_COMPROBANTE_INTERNO);
			}
			//Fechas
			documento.setAnio(entidad.getAnio());
			documento.setMes(new Integer(entidad.getMes()));
			documento.setFechaFin(UtilSGT.getDatetoString(UtilSGT.getLastDay(new Integer(entidad.getMes()), entidad.getAnio())));
			//Bitacora
			documento.setTblCxcBitacora(bitacora);
			//Campos de auditoria
			this.preGuardarDocumento(documento, request);
			//Validamos el tipo de comprobante
			if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_FACTURA)){
				this.obtenerSiguienteFactura(serieFactura);
				documento.setSerieComprobante(serieFactura.getPrefijoSerie()+UtilSGT.completarCeros(serieFactura.getSecuencialSerie().toString(), Constantes.SUNAT_LONGITUD_SERIE));
				documento.setNumeroComprobante(UtilSGT.completarCeros(serieFactura.getNumeroComprobante().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			}
			if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_BOLETA)){
				this.obtenerSiguienteFactura(serieBoleta);
				documento.setSerieComprobante(serieBoleta.getPrefijoSerie()+UtilSGT.completarCeros(serieBoleta.getSecuencialSerie().toString(), Constantes.SUNAT_LONGITUD_SERIE));
				documento.setNumeroComprobante(UtilSGT.completarCeros(serieBoleta.getNumeroComprobante().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			}
			if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_INTERNO)){
				this.obtenerSiguienteFactura(serieInterno);
				documento.setSerieComprobante(serieInterno.getPrefijoSerie()+UtilSGT.completarCeros(serieInterno.getSecuencialSerie().toString(), Constantes.SUNAT_LONGITUD_SERIE));
				documento.setNumeroComprobante(UtilSGT.completarCeros(serieInterno.getNumeroComprobante().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			}
			//Registrar: Se podria validar que no exista el registro "Codigo de Referencia" antes de registrar (luego en generacion individual)
			documentoDao.save(documento);
			//Buscamos el documento registrado
			documento = documentoDao.findByAnioMesCodigoReferencia(documento.getTipoReferencia(), documento.getCodigoReferencia(), documento.getAnio(), documento.getMes());
		}catch(Exception e){
			e.printStackTrace();
		}
		return documento;
	}
	/*
	 * Registro del documento de Alquiler
	 */
	public TblCxcDocumento registrarCxCDocumentoPrimerCobro(Model model, BitacoraBean entidad, HttpServletRequest request, TblContrato contrato, TblContratoPrimerCobro primerCobro,TblCxcBitacora bitacora,TblSerie serieFactura,TblSerie serieBoleta,TblSerie serieInterno){
		TblCxcDocumento documento	= null;
		try{
			documento = new TblCxcDocumento();
			documento.setCodigoContrato(contrato.getCodigoContrato());
			documento.setCodigoReferencia(primerCobro.getCodigoPrimero());
			documento.setTipoReferencia(Constantes.TIPO_COBRO_PRIMER_COBRO);
			documento.setMontoGenerado(primerCobro.getMontoCobro());
			documento.setMontoContrato(primerCobro.getMontoCobro());
			documento.setMontoCobrado(new BigDecimal("0"));
			documento.setSaldo(primerCobro.getMontoCobro());
			documento.setTipoMoneda(primerCobro.getTipoMoneda());
			documento.setTipoDocumento(primerCobro.getTipoDocumento());
			//validamos el tipo de documento
			if (documento.getTipoDocumento().equals(Constantes.TIPO_DOC_FACTURA)){
				documento.setTipoComprobante(Constantes.TIPO_COMPROBANTE_FACTURA);
			}else if (documento.getTipoDocumento().equals(Constantes.TIPO_DOC_BOLETA)){
				documento.setTipoComprobante(Constantes.TIPO_COMPROBANTE_BOLETA);
			}else if (documento.getTipoDocumento().equals(Constantes.TIPO_DOC_INTERNO)){
				documento.setTipoComprobante(Constantes.TIPO_COMPROBANTE_INTERNO);
			}
			//Fechas
			documento.setAnio(entidad.getAnio());
			documento.setMes(new Integer(entidad.getMes()));
			documento.setFechaFin(UtilSGT.getDatetoString(UtilSGT.getLastDay(new Integer(entidad.getMes()), entidad.getAnio())));
			//Bitacora
			documento.setTblCxcBitacora(bitacora);
			//Campos de auditoria
			this.preGuardarDocumento(documento, request);
			//Validamos el tipo de comprobante
			if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_FACTURA)){
				this.obtenerSiguienteFactura(serieFactura);
				documento.setSerieComprobante(serieFactura.getPrefijoSerie()+UtilSGT.completarCeros(serieFactura.getSecuencialSerie().toString(), Constantes.SUNAT_LONGITUD_SERIE));
				documento.setNumeroComprobante(UtilSGT.completarCeros(serieFactura.getNumeroComprobante().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			}
			if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_BOLETA)){
				this.obtenerSiguienteFactura(serieBoleta);
				documento.setSerieComprobante(serieBoleta.getPrefijoSerie()+UtilSGT.completarCeros(serieBoleta.getSecuencialSerie().toString(), Constantes.SUNAT_LONGITUD_SERIE));
				documento.setNumeroComprobante(UtilSGT.completarCeros(serieBoleta.getNumeroComprobante().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			}
			if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_INTERNO)){
				this.obtenerSiguienteFactura(serieInterno);
				documento.setSerieComprobante(serieInterno.getPrefijoSerie()+UtilSGT.completarCeros(serieInterno.getSecuencialSerie().toString(), Constantes.SUNAT_LONGITUD_SERIE));
				documento.setNumeroComprobante(UtilSGT.completarCeros(serieInterno.getNumeroComprobante().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			}
			//Registrar: Se podria validar que no exista el registro "Codigo de Referencia" antes de registrar (luego en generacion individual)
			documentoDao.save(documento);
			//Para la actualizacion del primer cobro
			primerCobro.setNumeroDocumento(documento.getSerieComprobante() + "-"+ documento.getNumeroComprobante());
			//Buscamos el documento registrado
			documento = documentoDao.findByAnioMesCodigoReferencia(documento.getTipoReferencia(), documento.getCodigoReferencia(), documento.getAnio(), documento.getMes());
		}catch(Exception e){
			e.printStackTrace();
		}
		return documento;
	}
	/*
	 * Registro del documento de Arbitrio
	 */
	public TblCxcDocumento registrarCxCDocumentoArbitrio(Model model, BitacoraBean entidad, HttpServletRequest request, TblArbitrio arbitrio,TblCxcBitacora bitacora,TblSerie serieFactura,TblSerie serieBoleta,TblSerie serieInterno){
		TblCxcDocumento documento	= null;
		//List<TblLuzxtienda> listaLuzxTienda = null;
		//TblLuzxtienda luzxTienda	= null;
		TblContrato contrato		= null;
		try{
			documento = new TblCxcDocumento();
			
			//Buscando el contrato y asociamos para el cobro
			//listaLuzxTienda = luzxTiendaDao.listarLuzTiendaxLuz(luz.getCodigoLuz());
			//if (listaLuzxTienda!=null && listaLuzxTienda.size()>0){
				//luzxTienda = listaLuzxTienda.get(0);
			contrato = contratoDao.findByCodigoContrato(arbitrio.getCodigoContrato());
			if (contrato !=null){
				documento.setCodigoContrato(contrato.getCodigoContrato());
			}
				
			//}
			documento.setCodigoReferencia(arbitrio.getCodigoArbitrio());
			documento.setTipoReferencia(Constantes.TIPO_COBRO_ARBITRIO);
			documento.setMontoGenerado(arbitrio.getMontoGenerado());
			documento.setMontoContrato(arbitrio.getMontoContrato());
			documento.setMontoCobrado(new BigDecimal("0"));
			documento.setSaldo(arbitrio.getMontoContrato());
			documento.setTipoMoneda(Constantes.MONEDA_SOL);
			documento.setTipoDocumento(Constantes.TIPO_DOC_INTERNO); //TODO: No se definio el tipo de documento, asi asigna por defecto
			//validamos el tipo de documento
			if (documento.getTipoDocumento().equals(Constantes.TIPO_DOC_FACTURA)){
				documento.setTipoComprobante(Constantes.TIPO_COMPROBANTE_FACTURA);
			}else if (documento.getTipoDocumento().equals(Constantes.TIPO_DOC_BOLETA)){
				documento.setTipoComprobante(Constantes.TIPO_COMPROBANTE_BOLETA);
			}else if (documento.getTipoDocumento().equals(Constantes.TIPO_DOC_INTERNO)){
				documento.setTipoComprobante(Constantes.TIPO_COMPROBANTE_INTERNO);
			}
			//Fechas
			documento.setAnio(entidad.getAnio());
			documento.setMes(new Integer(entidad.getMes()));
			//documento.setFechaFin(UtilSGT.getDatetoString(UtilSGT.getLastDay(new Integer(entidad.getMes()), entidad.getAnio())));
			documento.setFechaFin(arbitrio.getFechaFin());
			//Bitacora
			documento.setTblCxcBitacora(bitacora);
			//Campos de auditoria
			this.preGuardarDocumento(documento, request);
			//Validamos el tipo de comprobante
			if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_FACTURA)){
				this.obtenerSiguienteFactura(serieFactura);
				documento.setSerieComprobante(serieFactura.getPrefijoSerie()+UtilSGT.completarCeros(serieFactura.getSecuencialSerie().toString(), Constantes.SUNAT_LONGITUD_SERIE));
				documento.setNumeroComprobante(UtilSGT.completarCeros(serieFactura.getNumeroComprobante().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			}
			if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_BOLETA)){
				this.obtenerSiguienteFactura(serieBoleta);
				documento.setSerieComprobante(serieBoleta.getPrefijoSerie()+UtilSGT.completarCeros(serieBoleta.getSecuencialSerie().toString(), Constantes.SUNAT_LONGITUD_SERIE));
				documento.setNumeroComprobante(UtilSGT.completarCeros(serieBoleta.getNumeroComprobante().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			}
			if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_INTERNO)){
				this.obtenerSiguienteFactura(serieInterno);
				documento.setSerieComprobante(serieInterno.getPrefijoSerie()+UtilSGT.completarCeros(serieInterno.getSecuencialSerie().toString(), Constantes.SUNAT_LONGITUD_SERIE));
				documento.setNumeroComprobante(UtilSGT.completarCeros(serieInterno.getNumeroComprobante().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			}
			//Registrar: Se podria validar que no exista el registro "Codigo de Referencia" antes de registrar (luego en generacion individual)
			documentoDao.save(documento);
			//Buscamos el documento registrado
			documento = documentoDao.findByAnioMesCodigoReferencia(documento.getTipoReferencia(), documento.getCodigoReferencia(), documento.getAnio(), documento.getMes());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//listaLuzxTienda = null;
			arbitrio		= null;
			contrato		= null;
		}
		return documento;
	}
	/*
	 * Registro del documento de Luz
	 */
	public TblCxcDocumento registrarCxCDocumentoLuz(Model model, BitacoraBean entidad, HttpServletRequest request, TblLuzxtienda luzxTienda,TblCxcBitacora bitacora,TblSerie serieFactura,TblSerie serieBoleta,TblSerie serieInterno){
		TblCxcDocumento documento	= null;
		//List<TblLuzxtienda> listaLuzxTienda = null;
		//TblLuzxtienda luzxTienda	= null;
		TblContrato contrato		= null;
		try{
			documento = new TblCxcDocumento();
			
			//Buscando el contrato y asociamos para el cobro
			//listaLuzxTienda = luzxTiendaDao.listarLuzTiendaxLuz(luz.getCodigoLuz());
			//if (listaLuzxTienda!=null && listaLuzxTienda.size()>0){
				//luzxTienda = listaLuzxTienda.get(0);
			contrato = contratoDao.findByNumeroTienda(luzxTienda.getTblTienda().getCodigoTienda());
			if (contrato !=null){
				documento.setCodigoContrato(contrato.getCodigoContrato());
			}
				
			//}
			documento.setCodigoReferencia(luzxTienda.getCodigoLuzxtienda());
			documento.setTipoReferencia(Constantes.TIPO_COBRO_LUZ);
			documento.setMontoGenerado(luzxTienda.getMontoGenerado());
			documento.setMontoContrato(luzxTienda.getMontoContrato());
			documento.setMontoCobrado(new BigDecimal("0"));
			documento.setSaldo(luzxTienda.getMontoContrato());
			documento.setTipoMoneda(Constantes.MONEDA_SOL);
			documento.setTipoDocumento(Constantes.TIPO_DOC_INTERNO); //TODO: No se definio el tipo de documento, asi asigna por defecto
			//validamos el tipo de documento
			if (documento.getTipoDocumento().equals(Constantes.TIPO_DOC_FACTURA)){
				documento.setTipoComprobante(Constantes.TIPO_COMPROBANTE_FACTURA);
			}else if (documento.getTipoDocumento().equals(Constantes.TIPO_DOC_BOLETA)){
				documento.setTipoComprobante(Constantes.TIPO_COMPROBANTE_BOLETA);
			}else if (documento.getTipoDocumento().equals(Constantes.TIPO_DOC_INTERNO)){
				documento.setTipoComprobante(Constantes.TIPO_COMPROBANTE_INTERNO);
			}
			//Fechas
			documento.setAnio(entidad.getAnio());
			documento.setMes(new Integer(entidad.getMes()));
			//documento.setFechaFin(UtilSGT.getDatetoString(UtilSGT.getLastDay(new Integer(entidad.getMes()), entidad.getAnio())));
			documento.setFechaFin(luzxTienda.getFechaFin());
			//Bitacora
			documento.setTblCxcBitacora(bitacora);
			//Campos de auditoria
			this.preGuardarDocumento(documento, request);
			//Validamos el tipo de comprobante
			if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_FACTURA)){
				this.obtenerSiguienteFactura(serieFactura);
				documento.setSerieComprobante(serieFactura.getPrefijoSerie()+UtilSGT.completarCeros(serieFactura.getSecuencialSerie().toString(), Constantes.SUNAT_LONGITUD_SERIE));
				documento.setNumeroComprobante(UtilSGT.completarCeros(serieFactura.getNumeroComprobante().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			}
			if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_BOLETA)){
				this.obtenerSiguienteFactura(serieBoleta);
				documento.setSerieComprobante(serieBoleta.getPrefijoSerie()+UtilSGT.completarCeros(serieBoleta.getSecuencialSerie().toString(), Constantes.SUNAT_LONGITUD_SERIE));
				documento.setNumeroComprobante(UtilSGT.completarCeros(serieBoleta.getNumeroComprobante().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			}
			if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_INTERNO)){
				this.obtenerSiguienteFactura(serieInterno);
				documento.setSerieComprobante(serieInterno.getPrefijoSerie()+UtilSGT.completarCeros(serieInterno.getSecuencialSerie().toString(), Constantes.SUNAT_LONGITUD_SERIE));
				documento.setNumeroComprobante(UtilSGT.completarCeros(serieInterno.getNumeroComprobante().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			}
			//Registrar: Se podria validar que no exista el registro "Codigo de Referencia" antes de registrar (luego en generacion individual)
			documentoDao.save(documento);
			//Buscamos el documento registrado
			documento = documentoDao.findByAnioMesCodigoReferencia(documento.getTipoReferencia(), documento.getCodigoReferencia(), documento.getAnio(), documento.getMes());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//listaLuzxTienda = null;
			luzxTienda		= null;
			contrato		= null;
		}
		return documento;
	}
	
	/*
	 * Registro del documento de Servicio
	 */
	public TblCxcDocumento registrarCxCDocumentoServicio(Model model, BitacoraBean entidad, HttpServletRequest request, TblContratoServicio servicio,TblCxcBitacora bitacora,TblSerie serieFactura,TblSerie serieBoleta,TblSerie serieInterno){
		TblCxcDocumento documento	= null;
		try{
			documento = new TblCxcDocumento();
			documento.setNombre(servicio.getTblTipoServicio().getNombre());
			documento.setCodigoContrato(servicio.getTblContrato().getCodigoContrato());
			documento.setCodigoReferencia(servicio.getCodigoServicio());
			documento.setTipoReferencia(Constantes.TIPO_COBRO_SERVICIO);
			documento.setMontoGenerado(servicio.getMonto());
			documento.setMontoContrato(servicio.getMonto());
			documento.setMontoCobrado(new BigDecimal("0"));
			documento.setSaldo(servicio.getMonto());
			documento.setTipoMoneda(servicio.getTipoMonedaServicio());
			documento.setTipoDocumento(servicio.getTipoDocumentoServicio()); 
			//validamos el tipo de documento
			if (documento.getTipoDocumento().equals(Constantes.TIPO_DOC_FACTURA)){
				documento.setTipoComprobante(Constantes.TIPO_COMPROBANTE_FACTURA);
			}else if (documento.getTipoDocumento().equals(Constantes.TIPO_DOC_BOLETA)){
				documento.setTipoComprobante(Constantes.TIPO_COMPROBANTE_BOLETA);
			}else if (documento.getTipoDocumento().equals(Constantes.TIPO_DOC_INTERNO)){
				documento.setTipoComprobante(Constantes.TIPO_COMPROBANTE_INTERNO);
			}
			//Fechas
			documento.setAnio(entidad.getAnio());
			documento.setMes(new Integer(entidad.getMes()));
			documento.setFechaFin(UtilSGT.getDatetoString(UtilSGT.getLastDay(new Integer(entidad.getMes()), entidad.getAnio())));
			//Bitacora
			documento.setTblCxcBitacora(bitacora);
			//Campos de auditoria
			this.preGuardarDocumento(documento, request);
			//Validamos el tipo de comprobante
			if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_FACTURA)){
				this.obtenerSiguienteFactura(serieFactura);
				documento.setSerieComprobante(serieFactura.getPrefijoSerie()+UtilSGT.completarCeros(serieFactura.getSecuencialSerie().toString(), Constantes.SUNAT_LONGITUD_SERIE));
				documento.setNumeroComprobante(UtilSGT.completarCeros(serieFactura.getNumeroComprobante().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			}
			if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_BOLETA)){
				this.obtenerSiguienteFactura(serieBoleta);
				documento.setSerieComprobante(serieBoleta.getPrefijoSerie()+UtilSGT.completarCeros(serieBoleta.getSecuencialSerie().toString(), Constantes.SUNAT_LONGITUD_SERIE));
				documento.setNumeroComprobante(UtilSGT.completarCeros(serieBoleta.getNumeroComprobante().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			}
			if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_INTERNO)){
				this.obtenerSiguienteFactura(serieInterno);
				documento.setSerieComprobante(serieInterno.getPrefijoSerie()+UtilSGT.completarCeros(serieInterno.getSecuencialSerie().toString(), Constantes.SUNAT_LONGITUD_SERIE));
				documento.setNumeroComprobante(UtilSGT.completarCeros(serieInterno.getNumeroComprobante().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			}
			//Registrar: Se podria validar que no exista el registro "Codigo de Referencia" antes de registrar (luego en generacion individual)
			documentoDao.save(documento);
			//Buscamos el documento registrado
			documento = documentoDao.findByAnioMesCodigoReferencia(documento.getTipoReferencia(), documento.getCodigoReferencia(), documento.getAnio(), documento.getMes());
		}catch(Exception e){
			e.printStackTrace();
		}
		return documento;
	}
	/*
	 * Registro de los datos de la cabecera para la sunat
	 */
	public TblSunatCabecera registrarCabeceraSunat(TblContrato contrato, TblCxcDocumento documento, HttpServletRequest request){
		TblSunatCabecera cabecera = null;
		try{
			cabecera = new TblSunatCabecera();
			//Tipo Operacion
			cabecera.setTipoOperacion(Constantes.SUNAT_TIPO_OPERACION_VENTA_INTERNA);
			//Fecha Emision
			cabecera.setFechaEmision(UtilSGT.getFecha("yyyy-MM-dd"));
			//Domicilio Fiscal
			cabecera.setDomicilioFiscal(Constantes.SUNAT_CODIGO_DOMICILIO_FISCAL);
			//Datos del Cliente
			if(contrato.getTblPersona().getTipoPersona().equals(Constantes.TIPO_PERSONA_NATURAL)){
				cabecera.setTipoDocumentoUsuario(Constantes.SUNAT_TIPO_DOCUMENTO_DOC_NACIONAL_IDENTIDAD);
				cabecera.setNumeroDocumento(contrato.getTblPersona().getNumeroDni());
				cabecera.setRazonSocial(contrato.getTblPersona().getNombre() + " " + contrato.getTblPersona().getPaterno() + " " + contrato.getTblPersona().getMaterno());
			}else if (contrato.getTblPersona().getTipoPersona().equals(Constantes.TIPO_PERSONA_JURIDICA)){
				cabecera.setTipoDocumentoUsuario(Constantes.SUNAT_TIPO_DOCUMENTO_REGISTRO_UNICO_CONTRIBUYENTE);
				cabecera.setNumeroDocumento(contrato.getTblPersona().getNumeroRuc());
				cabecera.setRazonSocial(contrato.getTblPersona().getRazonSocial());
			}
			//Moneda
			if (contrato.getTipoMonedaAlquiler().equals(Constantes.MONEDA_DOLAR)){
				cabecera.setTipoMoneda(Constantes.SUNAT_TIPO_MONEDA_DOLAR);
			}else{
				cabecera.setTipoMoneda(Constantes.SUNAT_TIPO_MONEDA_SOLES);
			}
			//Descuentos Globales
			cabecera.setSumaDescuento(new BigDecimal("0"));
			cabecera.setSumaCargo(new BigDecimal("0"));
			cabecera.setTotalDescuento(new BigDecimal("0"));
			//Total valor de venta - Operaciones gravadas
			cabecera.setOperacionGravada(this.obtenerTotalMontoGravada(contrato.getMontoAlquiler(), Constantes.SUNAT_IGV));
			//Total valor de venta - Operaciones inafectas
			cabecera.setOperacionInafecta(new BigDecimal("0"));
			//Total valor de venta - Operaciones exoneradas
			cabecera.setOperacionExonerada(new BigDecimal("0"));
			//Sumatoria IGV
			cabecera.setMontoIgv(this.obtenerTotalImpuestoGravada(contrato.getMontoAlquiler(), Constantes.SUNAT_IGV));
			//Sumatoria ISC
			cabecera.setMontoIsc(new BigDecimal("0"));
			//Sumatoria otros tributos
			cabecera.setOtrosTributos(new BigDecimal("0"));
			//Importe total de la venta, cesión en uso o del servicio prestado
			cabecera.setImporteTotal(contrato.getMontoAlquiler());
			//Nombre archivo
			cabecera.setNombreArchivo(Constantes.SUNAT_RUC_EMISOR+"-"+documento.getTipoComprobante()+"-"+documento.getSerieComprobante()+"-"+documento.getNumeroComprobante()+"."+Constantes.SUNAT_ARCHIVO_EXTENSION_CABECERA);
			this.preGuardarSunatCabecera(cabecera, request);
			//CxC Documento
			cabecera.setTblCxcDocumento(documento);
			//Registro de la cabecera de la sunat
			sunatCabeceraDao.save(cabecera);
			cabecera = sunatCabeceraDao.findByCodigoDocumento(documento.getCodigoCxcDoc());
		}catch(Exception e){
			e.printStackTrace();
			cabecera = null;
		}
		return cabecera;
	}
	
	/*
	 * Registro de los datos de la cabecera para la sunat
	 */
	public TblSunatCabecera registrarCabeceraSunatServicio(TblContratoServicio servicio, TblCxcDocumento documento, HttpServletRequest request){
		TblSunatCabecera cabecera = null;
		try{
			cabecera = new TblSunatCabecera();
			//Tipo Operacion
			cabecera.setTipoOperacion(Constantes.SUNAT_TIPO_OPERACION_VENTA_INTERNA);
			//Fecha Emision
			cabecera.setFechaEmision(UtilSGT.getFecha("yyyy-MM-dd"));
			//Domicilio Fiscal
			cabecera.setDomicilioFiscal(Constantes.SUNAT_CODIGO_DOMICILIO_FISCAL);
			//Datos del Cliente
			if(servicio.getTblContrato().getTblPersona().getTipoPersona().equals(Constantes.TIPO_PERSONA_NATURAL)){
				cabecera.setTipoDocumentoUsuario(Constantes.SUNAT_TIPO_DOCUMENTO_DOC_NACIONAL_IDENTIDAD);
				cabecera.setNumeroDocumento(servicio.getTblContrato().getTblPersona().getNumeroDni());
				cabecera.setRazonSocial(servicio.getTblContrato().getTblPersona().getNombre() + " " + servicio.getTblContrato().getTblPersona().getPaterno() + " " + servicio.getTblContrato().getTblPersona().getMaterno());
			}else if (servicio.getTblContrato().getTblPersona().getTipoPersona().equals(Constantes.TIPO_PERSONA_JURIDICA)){
				cabecera.setTipoDocumentoUsuario(Constantes.SUNAT_TIPO_DOCUMENTO_REGISTRO_UNICO_CONTRIBUYENTE);
				cabecera.setNumeroDocumento(servicio.getTblContrato().getTblPersona().getNumeroRuc());
				cabecera.setRazonSocial(servicio.getTblContrato().getTblPersona().getRazonSocial());
			}
			//Moneda
			if (servicio.getTipoMonedaServicio().equals(Constantes.MONEDA_DOLAR)){
				cabecera.setTipoMoneda(Constantes.SUNAT_TIPO_MONEDA_DOLAR);
			}else{
				cabecera.setTipoMoneda(Constantes.SUNAT_TIPO_MONEDA_SOLES);
			}
			//Descuentos Globales
			cabecera.setSumaDescuento(new BigDecimal("0"));
			cabecera.setSumaCargo(new BigDecimal("0"));
			cabecera.setTotalDescuento(new BigDecimal("0"));
			//Total valor de venta - Operaciones gravadas
			cabecera.setOperacionGravada(this.obtenerTotalMontoGravada(servicio.getMonto(), Constantes.SUNAT_IGV));
			//Total valor de venta - Operaciones inafectas
			cabecera.setOperacionInafecta(new BigDecimal("0"));
			//Total valor de venta - Operaciones exoneradas
			cabecera.setOperacionExonerada(new BigDecimal("0"));
			//Sumatoria IGV
			cabecera.setMontoIgv(this.obtenerTotalImpuestoGravada(servicio.getMonto(), Constantes.SUNAT_IGV));
			//Sumatoria ISC
			cabecera.setMontoIsc(new BigDecimal("0"));
			//Sumatoria otros tributos
			cabecera.setOtrosTributos(new BigDecimal("0"));
			//Importe total de la venta, cesión en uso o del servicio prestado
			cabecera.setImporteTotal(servicio.getMonto());
			//Nombre archivo
			cabecera.setNombreArchivo(Constantes.SUNAT_RUC_EMISOR+"-"+documento.getTipoComprobante()+"-"+documento.getSerieComprobante()+"-"+documento.getNumeroComprobante()+Constantes.SUNAT_ARCHIVO_EXTENSION_CABECERA);
			this.preGuardarSunatCabecera(cabecera, request);
			//CxC Documento
			cabecera.setTblCxcDocumento(documento);
			//Registro de la cabecera de la sunat
			sunatCabeceraDao.save(cabecera);
			cabecera = sunatCabeceraDao.findByCodigoDocumento(documento.getCodigoCxcDoc());
		}catch(Exception e){
			e.printStackTrace();
			cabecera = null;
		}
		return cabecera;
	}
	
	/*
	 * Registro de los datos del detalle para la sunat
	 */
	public TblSunatDetalle registrarDetalleSunat(TblSunatCabecera cabecera, TblCxcDocumento documento, String tipoDescripcion, HttpServletRequest request){
		TblSunatDetalle detalle 				= null;
		List<TblParametro> listaParametro		= null;
		try{
			//registro del detalle del comprobante
			detalle = new TblSunatDetalle();
			//Código de unidad de medida por ítem
			detalle.setCodigoUnidad(Constantes.SUNAT_UNIDAD_MEDIDA);
			//Cantidad de unidades por ítem
			detalle.setCantidad(Constantes.SUNAT_CANTIDAD_UNITARIA);
			//Código de producto
			detalle.setCodigoProducto("");
			//Codigo producto SUNAT
			detalle.setCodigoProductoSunat("");
			//Descripción detallada del servicio prestado, bien vendido o cedido en uso, indicando las características.
			listaParametro = parametroDao.buscarOneByNombre(tipoDescripcion);
			if(listaParametro!=null && listaParametro.size()>0){
				detalle.setDescripcion(listaParametro.get(0).getDescripcion());
			}else{
				detalle.setDescripcion("");
			}
			//Valor unitario por ítem
			detalle.setValorUnitario(cabecera.getOperacionGravada().toString());
			//Descuentos por item
			detalle.setDescuento("0");
			//Monto de IGV por ítem
			detalle.setMontoIgv(cabecera.getMontoIgv().toString());
			//Afectación al IGV por ítem
			detalle.setAfectacionIgv(Constantes.SUNAT_AFECTACION_IGV_GRAVADO_OPE_ONEROSO);
			//Monto de ISC por ítem
			detalle.setMontoIsc("0");
			//Tipo de sistema ISC
			detalle.setTipoIsc("");
			//Precio de venta unitario por item
			detalle.setPrecioVentaUnitario(cabecera.getImporteTotal().toString());
			//Valor de venta por ítem
			detalle.setValorVentaItem(cabecera.getImporteTotal().toString());
			//Archivo
			detalle.setNombreArchivo(Constantes.SUNAT_RUC_EMISOR+"-"+documento.getTipoComprobante()+"-"+documento.getSerieComprobante()+"-"+documento.getNumeroComprobante()+"."+Constantes.SUNAT_ARCHIVO_EXTENSION_DETALLE);
			//Falta auditoria
			this.preGuardarSunatDetalle(detalle, request);
			detalle.setTblSunatCabecera(cabecera);
			sunatDetalleDao.save(detalle);
		}catch(Exception e){
			e.printStackTrace();
			detalle = null;
		}finally{
			listaParametro = null;
		}
		return detalle;
	}
	/*
	 * Procesa los tipos de cobro seleccionado
	 */
	public boolean procesarTipoCobro(Model model, BitacoraBean entidad, HttpServletRequest request){
		boolean resultado 						= false;
		Integer intTotalRegistros				= null;
		List<TblContrato> listaContrato			= null;
		List<TblLuz> listaLuz					= null;
		List<TblLuzxtienda> listaLuzxTienda		= null;
		List<TblContratoServicio> listaServicio	= null;
		List<TblArbitrio> listaArbitrio			= null;
		TblCxcDocumento documento				= null;
		TblCxcBitacora bitacora					= null;
		Specification<TblCxcBitacora> criterio	= null;
		TblSerie serieFactura					= null;
		TblSerie serieBoleta					= null;
		TblSerie serieInterno					= null;
		TblCxcDocumento cxcDocumento			= null;
		TblSunatCabecera cabecera				= null;
		TblSunatDetalle detalle					= null;
		Integer totalAlquilerMasivoFactura		= 0;
		Integer totalAlquilerMasivoBoleta		= 0;
		Integer totalAlquilerIndividual			= 0;
		Integer totalAlquilerInterno			= 0;
		Integer totalLuzMasivo					= 0;
		Integer totalArbitrioMasivo				= 0;
		Integer totalLuzIndividual				= 0;
		Integer totalArbitrioIndividual			= 0;
		Integer totalServicioMasivoFactura		= 0;
		Integer totalServicioMasivoBoleta		= 0;
		Integer totalServicioIndividual			= 0;
		Integer totalServicioInterno			= 0;
		String strMensaje						= "";
		
		try{
			//Obtenemos Prefijo, Secuencia y Numero para generar los siguiente
			serieFactura = serieDao.buscarOneByTipoComprobante(Constantes.TIPO_COMPROBANTE_FACTURA);
			serieBoleta = serieDao.buscarOneByTipoComprobante(Constantes.TIPO_COMPROBANTE_BOLETA);
			serieInterno = serieDao.buscarOneByTipoComprobante(Constantes.TIPO_COMPROBANTE_INTERNO);
			
			//Alquiler
			if (entidad.getAlquiler().equals(Constantes.TIPO_SI)){
				//Validamos que no se haya generado la carga masiva para ese año y mes
				intTotalRegistros =cxcBitacoraDao.countTipoCobro(entidad.getAnio(), new Integer(entidad.getMes()), Constantes.TIPO_COBRO_ALQUILER);
				if (intTotalRegistros !=null && intTotalRegistros >0){
					model.addAttribute("respuesta", "Se identifico registros generados de Alquiler para el año ["+entidad.getAnio()+"] y mes ["+entidad.getMes()+"] seleccionados. Se interrumpio el proceso");
				}else{
					//Se lista todos los contratos activos
					//listaContrato = contratoDao.listAllContratoActivos();
					listaContrato = contratoDao.listAllContratoActivosxFecha(UtilSGT.getDatetoString(UtilSGT.getFistDay(entidad.getMes(), entidad.getAnio())));
					if (listaContrato != null && listaContrato.size()>0){
						//Registramos la Bitacora del CxC del alquiler
						boolean exitoso = registrarBitacora(model, entidad, Constantes.TIPO_COBRO_ALQUILER, Constantes.SERIE_TIPO_OPERACION_MENSUAL, request);
						
						//Si se regsitro exitosamente se procede con el registro de la cxc
						if (exitoso) {
							criterio = Specifications.where(conAnio(entidad.getAnio()))
									.and(conMes(new Integer(entidad.getMes())))
									.and(conTipoCobro(Constantes.TIPO_COBRO_ALQUILER))
									.and((conTipoOperacion(Constantes.SERIE_TIPO_OPERACION_MENSUAL)));
							bitacora = cxcBitacoraDao.findOne(criterio);
							for (TblContrato contrato: listaContrato){
								//Validamos si se registro individualmente el alquiler
								cxcDocumento = documentoDao.findByAnioMesCodigoReferencia(Constantes.TIPO_COBRO_ALQUILER, contrato.getCodigoContrato(), entidad.getAnio(), new Integer(entidad.getMes()));
								if (cxcDocumento == null || cxcDocumento.getCodigoReferencia() == null){
									//Registro del CxC Documento
									documento = this.registrarCxCDocumentoAlquiler(model, entidad, request, contrato, bitacora, serieFactura, serieBoleta, serieInterno);
									if (documento!=null){
										//Generamos informacion para la SUNAT
										if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_FACTURA)){
											//registro de la cabecera del comprobante
											cabecera = this.registrarCabeceraSunat(contrato, documento, request);
											if (cabecera != null){
												detalle = this.registrarDetalleSunat(cabecera, documento, Constantes.PAR_SUNAT_ALQUILER,request);
												if (detalle !=null){
													//Generar Archivos planos
													if (this.generarArchivoCabecera(cabecera)){
														if (this.generarArchivoDetalle(detalle)){
															totalAlquilerMasivoFactura++;
															resultado = true;
														}else{
															//model.addAttribute("respuesta", "No se pudo generar el archivo plano Detalle SUNAT del Alquiler - Factura para el contrato ["+contrato.getNumero()+"]");
															strMensaje = strMensaje + "No se pudo generar el archivo plano Detalle SUNAT del Alquiler - Factura para el contrato ["+contrato.getNumero()+"]\n";
															resultado = false;
															break;
														}
													}else{
														//model.addAttribute("respuesta", "No se pudo generar el archivo plano Cabecera SUNAT del Alquiler - Factura para el contrato ["+contrato.getNumero()+"]");
														strMensaje = strMensaje +"No se pudo generar el archivo plano Cabecera SUNAT del Alquiler - Factura para el contrato ["+contrato.getNumero()+"]\n";
														resultado = false;
														break;
													}
												}else{
													//model.addAttribute("respuesta", "No se pudo registrar el Detalle SUNAT del Alquiler - Factura para el contrato ["+contrato.getNumero()+"]");
													strMensaje = strMensaje +"No se pudo registrar el Detalle SUNAT del Alquiler - Factura para el contrato ["+contrato.getNumero()+"]\n";
													resultado = false;
													break;
												}
											}else{
												//model.addAttribute("respuesta", "No se pudo registrar la Cabecera SUNAT del Alquiler - Factura para el contrato ["+contrato.getNumero()+"]");
												strMensaje = strMensaje +"No se pudo registrar la Cabecera SUNAT del Alquiler - Factura para el contrato ["+contrato.getNumero()+"] \n";
												resultado = false;
												break;
											}
											
										}else if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_BOLETA)){
											//registro de la cabecera del comprobante
											cabecera = this.registrarCabeceraSunat(contrato, documento, request);
											if (cabecera != null){
												detalle = this.registrarDetalleSunat(cabecera, documento, Constantes.PAR_SUNAT_ALQUILER, request);
												if (detalle !=null){
													//Generar Archivos planos
													if (this.generarArchivoCabecera(cabecera)){
														if (this.generarArchivoDetalle(detalle)){
															totalAlquilerMasivoBoleta++;
															resultado = true;
														}else{
															//model.addAttribute("respuesta", "No se pudo generar el archivo plano Detalle SUNAT del Alquiler - Boleta para el contrato ["+contrato.getNumero()+"]");
															strMensaje = strMensaje +"No se pudo generar el archivo plano Detalle SUNAT del Alquiler - Boleta para el contrato ["+contrato.getNumero()+"]\n";
															resultado = false;
															break;
														}
													}else{
														//model.addAttribute("respuesta", "No se pudo generar el archivo plano Cabecera SUNAT del Alquiler - Boleta para el contrato ["+contrato.getNumero()+"]");
														strMensaje = strMensaje +"No se pudo generar el archivo plano Cabecera SUNAT del Alquiler - Boleta para el contrato ["+contrato.getNumero()+"]\n";
														resultado = false;
														break;
													}
												}else{
													//model.addAttribute("respuesta", "No se pudo registrar la Detalle SUNAT del Alquiler - Boleta para el contrato ["+contrato.getNumero()+"]");
													strMensaje = strMensaje +"No se pudo registrar la Detalle SUNAT del Alquiler - Boleta para el contrato ["+contrato.getNumero()+"]\n";
													resultado = false;
													break;
												}
											}else{
												//model.addAttribute("respuesta", "No se pudo registrar la Cabecera SUNAT del Alquiler - Boleta para el contrato ["+contrato.getNumero()+"]");
												strMensaje = strMensaje +"No se pudo registrar la Cabecera SUNAT del Alquiler - Boleta para el contrato ["+contrato.getNumero()+"]\n";
												resultado = false;
												break;
											}
										}else{
											//No se genera comprobante para el tipo Interno
											totalAlquilerInterno ++;
											resultado = true;
										}
									}else{
										//model.addAttribute("respuesta", "No se pudo registrar la Cuenta por Cobrar del Alquiler para el contrato ["+contrato.getNumero()+"]");
										strMensaje = strMensaje + "No se pudo registrar la Cuenta por Cobrar del Alquiler para el contrato ["+contrato.getNumero()+"]\n";
										resultado = false;
										break;
									}
								}else{
									totalAlquilerIndividual ++;
									resultado = true;
								}
							}//Fin del For
							
						}else{
							//model.addAttribute("respuesta", "No se registro la bitacora del proceso masivo del Alquiler");
							strMensaje = strMensaje + "No se registro la bitacora del proceso masivo del Alquiler\n";
							resultado = false;
						}
					}else{
						//model.addAttribute("respuesta", "No se encontro listado de Contratos de Alquiler activos");
						strMensaje = strMensaje +  "No se encontro listado de Contratos de Alquiler activos\n";
						resultado = false;
					}
				}
				
				if(resultado==false){
					//model.addAttribute("respuesta", "Error en el proceso de generacion masiva de las Cuentas por Cobrar del Alquiler");
					strMensaje = strMensaje + "Error en el proceso de generacion masiva de las Cuentas por Cobrar del Alquiler\n";
				}else{
					//Validamos los valores para actualizar la bitacora
					if (bitacora !=null && bitacora.getCodigoCxcBitacora() >0){
						//if (totalAlquilerMasivoFactura >0 || totalAlquilerMasivoBoleta >0 || totalAlquilerIndividual >0 || totalAlquilerInterno > 0){
							String resultadoAlquiler= "Se registro Masivamente ["+totalAlquilerMasivoFactura+"] Facturas, ["+totalAlquilerMasivoBoleta+"] Boletas, ["+totalAlquilerInterno+"] Internos y se identico ["+totalAlquilerIndividual+"] Contratos generados Individualmente.";
							bitacora.setResultado(resultadoAlquiler);
							this.preEditar(bitacora,request);
							cxcBitacoraDao.save(bitacora);
						//}
					}
				}
				
			}
			

			//Generacion de las CxC de los Arbitrios
			if (entidad.getArbitrio().equals(Constantes.TIPO_SI)){
				//Inicializamos variable
				resultado = false;
				bitacora = null;
				intTotalRegistros =cxcBitacoraDao.countTipoCobro(entidad.getAnio(), new Integer(entidad.getMes()), Constantes.TIPO_COBRO_ARBITRIO);
				if (intTotalRegistros >0){
					//model.addAttribute("respuesta", "Se identifico registros generados de Arbitrios para el año ["+entidad.getAnio()+"] y mes ["+entidad.getMes()+"] seleccionados. Se interrumpio el proceso");
					strMensaje = strMensaje + "Se identifico registros generados de Arbitrios para el año ["+entidad.getAnio()+"] y mes ["+entidad.getMes()+"] seleccionados. Se interrumpio el proceso\n";
				}else{
					String fecha = "01/"+entidad.getMes().toString()+"/"+entidad.getAnio();
					listaArbitrio = arbitrioDao.listarAllActivosxFecha(UtilSGT.getDatetoString(fecha));
					if (listaArbitrio !=null){
						boolean exitoso = registrarBitacora(model, entidad, Constantes.TIPO_COBRO_ARBITRIO, Constantes.SERIE_TIPO_OPERACION_MENSUAL, request);
						if (exitoso) {
							criterio = Specifications.where(conAnio(entidad.getAnio()))
									.and(conMes(new Integer(entidad.getMes())))
									.and(conTipoCobro(Constantes.TIPO_COBRO_ARBITRIO))
									.and((conTipoOperacion(Constantes.SERIE_TIPO_OPERACION_MENSUAL)));
							bitacora = cxcBitacoraDao.findOne(criterio);
							for (TblArbitrio arbitrio: listaArbitrio){
								//cxcDocumento = documentoDao.findByAnioMesCodigoReferencia(Constantes.TIPO_COBRO_ARBITRIO, arbitrio.getCodigoArbitrio(), entidad.getAnio(), new Integer(entidad.getMes()));
								cxcDocumento = documentoDao.findByArbitrioCodigoReferencia(Constantes.TIPO_COBRO_ARBITRIO, arbitrio.getCodigoArbitrio());
								if (cxcDocumento == null || cxcDocumento.getCodigoReferencia() == null){
									//Registro del CxC Documento
									documento = this.registrarCxCDocumentoArbitrio(model, entidad, request, arbitrio, bitacora, serieFactura, serieBoleta, serieInterno);
									if (documento !=null){
										totalArbitrioMasivo++;
										resultado = true;
									}else{
										//model.addAttribute("respuesta", "No se pudo registrar la Cuenta por Cobrar de la Luz para el suministro  ["+luz.getTblSuministro().getNumero()+"]");
										strMensaje = strMensaje +" No se pudo registrar la Cuenta por Cobrar del Arbitrio  ["+ arbitrio.getTblTienda().getNumero()+"]\n";
										resultado = false;
										break;
									}
									//No se genera para la sunat
								}else{
									totalArbitrioIndividual++;
									resultado = true;
								}
							}
							if(resultado==false){
								//model.addAttribute("respuesta", "Error en el proceso de generacion masiva de las Cuentas por Cobrar del Alquiler");
								strMensaje = strMensaje + "Error en el proceso de generacion masiva de las Cuentas por Cobrar del Arbitrio. ";
							}else{
								//Validamos los valores para actualizar la bitacora
								if (bitacora !=null && bitacora.getCodigoCxcBitacora() >0){
									//if (totalAlquilerMasivoFactura >0 || totalAlquilerMasivoBoleta >0 || totalAlquilerIndividual >0 || totalAlquilerInterno > 0){
										String resultadoArbitrio= "Se registro Masivamente ["+totalArbitrioMasivo+"] Cuentas por Cobrar Internos de Arbitrios y se identico ["+totalArbitrioIndividual+"] Arbitrios generados Individualmente.";
										bitacora.setResultado(resultadoArbitrio);
										this.preEditar(bitacora,request);
										cxcBitacoraDao.save(bitacora);
									//}
								}
							}
						}
					}
				}
			}
			
			//Generacion de las CXC de la Luz
			if (entidad.getLuz().equals(Constantes.TIPO_SI)){
				//Inicializamos variable
				resultado = false;
				bitacora = null;
				intTotalRegistros =cxcBitacoraDao.countTipoCobro(entidad.getAnio(), new Integer(entidad.getMes()), Constantes.TIPO_COBRO_LUZ);
				if (intTotalRegistros >0){
					//model.addAttribute("respuesta", "Se identifico registros generados de Luz para el año ["+entidad.getAnio()+"] y mes ["+entidad.getMes()+"] seleccionados. Se interrumpio el proceso");
					strMensaje = strMensaje + "Se identifico registros generados de Luz para el año ["+entidad.getAnio()+"] y mes ["+entidad.getMes()+"] seleccionados. Se interrumpio el proceso\n";
				}else{
					//Se lista todos los registros de luz activos
					listaLuz = luzDao.listarxAnioMes(entidad.getAnio().toString(), entidad.getMes());
					if (listaLuz != null && listaLuz.size()>0){
						
						//Registramos la Bitacora del CxC del alquiler
						boolean exitoso = registrarBitacora(model, entidad, Constantes.TIPO_COBRO_LUZ, Constantes.SERIE_TIPO_OPERACION_MENSUAL, request);
						
						//Si se regsitro exitosamente se procede con el registro de la cxc
						if (exitoso) {
							criterio = Specifications.where(conAnio(entidad.getAnio()))
									.and(conMes(new Integer(entidad.getMes())))
									.and(conTipoCobro(Constantes.TIPO_COBRO_LUZ))
									.and((conTipoOperacion(Constantes.SERIE_TIPO_OPERACION_MENSUAL)));
							bitacora = cxcBitacoraDao.findOne(criterio);
							for (TblLuz luz: listaLuz){
								
								listaLuzxTienda = luzxTiendaDao.listarLuzTiendaxSuministroConMonto(luz.getTblSuministro().getCodigoSuministro(), luz.getFechaFin());
								if (listaLuzxTienda!=null){
									for (TblLuzxtienda luzxtienda: listaLuzxTienda){
										//Validamos si se registro individualmente el cobro de luz
										//cxcDocumento = documentoDao.findByAnioMesCodigoReferencia(Constantes.TIPO_COBRO_LUZ, luz.getCodigoLuz(), entidad.getAnio(), new Integer(entidad.getMes()));
										cxcDocumento = documentoDao.findByAnioMesCodigoReferencia(Constantes.TIPO_COBRO_LUZ, luzxtienda.getCodigoLuzxtienda(), entidad.getAnio(), new Integer(entidad.getMes()));
										if (cxcDocumento == null || cxcDocumento.getCodigoReferencia() == null){
											//Registro del CxC Documento
											documento = this.registrarCxCDocumentoLuz(model, entidad, request, luzxtienda, bitacora, serieFactura, serieBoleta, serieInterno);
											if (documento !=null){
												totalLuzMasivo++;
												resultado = true;
											}else{
												//model.addAttribute("respuesta", "No se pudo registrar la Cuenta por Cobrar de la Luz para el suministro  ["+luz.getTblSuministro().getNumero()+"]");
												strMensaje = strMensaje +"No se pudo registrar la Cuenta por Cobrar de la Luz para el suministro  ["+luz.getTblSuministro().getNumero()+"]\n";
												resultado = false;
												break;
											}
											//No se genera para la sunat
										}else{
											totalLuzIndividual++;
											resultado = true;
										}
									}
								}
								
							}//fin for
							
						}else{
							//model.addAttribute("respuesta", "No se registro la bitacora del proceso masivo del Luz");
							strMensaje = strMensaje +"No se registro la bitacora del proceso masivo del Luz\n";
							resultado = false;
						}

					}else{
						//model.addAttribute("respuesta", "No se encontro listado de Luz activos");
						strMensaje = strMensaje + "No se encontro listado de Luz activos";
						resultado = false;
					}
				}
				if (resultado == false){
					//model.addAttribute("respuesta", "Error en el proceso de generacion masiva de las Cuentas por Cobrar de la Luz");
					strMensaje = strMensaje +"Error en el proceso de generacion masiva de las Cuentas por Cobrar de la Luz\n";
				}else{
					//Validamos los valores para actualizar la bitacora
					if (bitacora !=null && bitacora.getCodigoCxcBitacora() >0){
						//if (totalLuzMasivo >0 || totalLuzIndividual >0 ){
							String resultadoLuz= "Se registro Masivamente ["+totalLuzMasivo+"] Cuentas por Cobrar Internos de Luz  , y se identico ["+totalLuzIndividual+"] Cuentas por Cobrar de Luz generados Individualmente.";
							bitacora.setResultado(resultadoLuz);
							this.preEditar(bitacora,request);
							cxcBitacoraDao.save(bitacora);
						//}
					}
				}
				
			}
			
			//Generacion de las CxC de los servicios
			if (entidad.getServicio().equals(Constantes.TIPO_SI)){
				//Inicializamos variable de control
				resultado = false;
				bitacora = null;
				intTotalRegistros =cxcBitacoraDao.countTipoCobro(entidad.getAnio(), new Integer(entidad.getMes()), Constantes.TIPO_COBRO_SERVICIO);
				if (intTotalRegistros >0){
					//model.addAttribute("respuesta", "Se identifico registros generados de Servicio para el año ["+entidad.getAnio()+"] y mes ["+entidad.getMes()+"] seleccionados. Se interrumpio el proceso");
					strMensaje = strMensaje + "Se identifico registros generados de Servicio para el año ["+entidad.getAnio()+"] y mes ["+entidad.getMes()+"] seleccionados. Se interrumpio el proceso\n";
				}else{
					//Se lista todos los registros de servicios activos
					//listaServicio = servicioDao.listarAllActivos();
					listaServicio = servicioDao.listarAllActivosxFecha(UtilSGT.getDatetoString(UtilSGT.getFistDay(entidad.getMes(), entidad.getAnio())));
					if (listaServicio != null && listaServicio.size()>0){
						//Registramos la Bitacora del CxC del alquiler
						boolean exitoso = registrarBitacora(model, entidad, Constantes.TIPO_COBRO_SERVICIO, Constantes.SERIE_TIPO_OPERACION_MENSUAL, request);
						
						//Si se registro exitosamente se procede con el registro de la cxc
						if (exitoso) {
							criterio = Specifications.where(conAnio(entidad.getAnio()))
									.and(conMes(new Integer(entidad.getMes())))
									.and(conTipoCobro(Constantes.TIPO_COBRO_SERVICIO))
									.and((conTipoOperacion(Constantes.SERIE_TIPO_OPERACION_MENSUAL)));
							bitacora = cxcBitacoraDao.findOne(criterio);
							for (TblContratoServicio servicio: listaServicio){
								//Validamos si se registro individualmente el cobro de servicio
								cxcDocumento = documentoDao.findByAnioMesCodigoReferencia(Constantes.TIPO_COBRO_SERVICIO, servicio.getCodigoServicio(), entidad.getAnio(), new Integer(entidad.getMes()));
								if (cxcDocumento == null || cxcDocumento.getCodigoReferencia() == null){
									//Registro del CxC Documento
									documento = this.registrarCxCDocumentoServicio(model, entidad, request, servicio, bitacora, serieFactura, serieBoleta, serieInterno);
									if (documento!=null){
									//Generamos informacion para la SUNAT
										if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_FACTURA)){
											//registro de la cabecera del comprobante
											cabecera = this.registrarCabeceraSunatServicio(servicio, documento, request);
											if (cabecera != null){
												detalle = this.registrarDetalleSunat(cabecera, documento, Constantes.PAR_SUNAT_SERVICIO, request);
												if (detalle !=null){
													//Generar Archivos planos
													if (this.generarArchivoCabecera(cabecera)){
														if (this.generarArchivoDetalle(detalle)){
															totalServicioMasivoFactura++;
															resultado = true;
														}else{
															//model.addAttribute("respuesta", "No se pudo generar el Detalle SUNAT del Servicio - Factura para el contrato ["+servicio.getTblContrato().getNumero()+"]");
															strMensaje = strMensaje + "No se pudo generar el Detalle SUNAT del Servicio - Factura para el contrato ["+servicio.getTblContrato().getNumero()+"]\n";
															resultado = false;
															break;
														}
													}else{
														//model.addAttribute("respuesta", "No se pudo generar la Cabecera del Archivo SUNAT del Servicio - Factura para el contrato ["+servicio.getTblContrato().getNumero()+"]");
														strMensaje = strMensaje + "No se pudo generar la Cabecera del Archivo SUNAT del Servicio - Factura para el contrato ["+servicio.getTblContrato().getNumero()+"]\n";
														resultado = false;
														break;
													}
												}else{
													//model.addAttribute("respuesta", "No se pudo registrar el Detalle SUNAT del Servicio - Factura para el contrato ["+servicio.getTblContrato().getNumero()+"]");
													strMensaje = strMensaje +  "No se pudo registrar el Detalle SUNAT del Servicio - Factura para el contrato ["+servicio.getTblContrato().getNumero()+"]\n";
													resultado = false;
													break;
												}
											}else{
												//model.addAttribute("respuesta", "No se pudo registrar la Cabecera SUNAT del Servicio - Factura para el contrato ["+servicio.getTblContrato().getNumero()+"]");
												strMensaje = strMensaje + "No se pudo registrar la Cabecera SUNAT del Servicio - Factura para el contrato ["+servicio.getTblContrato().getNumero()+"]\n";
												resultado = false;
												break;
											}
											
										}else if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_BOLETA)){
											//registro de la cabecera del comprobante
											cabecera = this.registrarCabeceraSunatServicio(servicio, documento, request);
											if (cabecera != null){
												detalle = this.registrarDetalleSunat(cabecera, documento, Constantes.PAR_SUNAT_SERVICIO, request);
												if (detalle !=null){
													//Generar Archivos planos
													if (this.generarArchivoCabecera(cabecera)){
														if (this.generarArchivoDetalle(detalle)){
															totalServicioMasivoBoleta++;
															resultado = true;
														}else{
															//model.addAttribute("respuesta", "No se pudo generar el Detalle SUNAT del Servicio - Boleta para el contrato ["+servicio.getTblContrato().getNumero()+"]");
															strMensaje = strMensaje + "No se pudo generar el Detalle SUNAT del Servicio - Boleta para el contrato ["+servicio.getTblContrato().getNumero()+"]\n";
															resultado = false;
															break;
														}
													}else{
														//model.addAttribute("respuesta", "No se pudo generar la Cabecera del Archivo SUNAT del Servicio - Boleta para el contrato ["+servicio.getTblContrato().getNumero()+"]");
														strMensaje = strMensaje +  "No se pudo generar la Cabecera del Archivo SUNAT del Servicio - Boleta para el contrato ["+servicio.getTblContrato().getNumero()+"]\n";
														resultado = false;
														break;
													}
												}else{
													//model.addAttribute("respuesta", "No se pudo registrar el Detalle SUNAT del Servicio - Boleta para el contrato ["+servicio.getTblContrato().getNumero()+"]");
													strMensaje = strMensaje +  "No se pudo registrar el Detalle SUNAT del Servicio - Boleta para el contrato ["+servicio.getTblContrato().getNumero()+"]\n";
													resultado = false;
													break;
												}
											}else{
												//model.addAttribute("respuesta", "No se pudo registrar la Cabecera SUNAT del Servicio - Boleta para el contrato ["+servicio.getTblContrato().getNumero()+"]");
												strMensaje = strMensaje + "No se pudo registrar la Cabecera SUNAT del Servicio - Boleta para el contrato ["+servicio.getTblContrato().getNumero()+"]\n";
												resultado = false;
												break;
											}
										}else{
											totalServicioInterno++;
											resultado = true;
										}
										//No se genera comprobante para el tipo Interno
									}else{
										//model.addAttribute("respuesta", "No se pudo registrar la Cuenta por Cobrar del Servicio para el contrato ["+servicio.getTblContrato().getNumero()+"]");
										strMensaje = strMensaje + "No se pudo registrar la Cuenta por Cobrar del Servicio para el contrato ["+servicio.getTblContrato().getNumero()+"]\n";
										resultado = false;
										break;
									}
								}else{
									totalServicioIndividual++;
									resultado = true;
									break;
								}
							} //Fin del For
						}else{
							//model.addAttribute("respuesta", "No se registro la bitacora del proceso masivo del Servicio");
							strMensaje = strMensaje + "No se registro la bitacora del proceso masivo del Servicio\n";
							resultado = false;
						}

					}else{
						//model.addAttribute("respuesta", "No se encontro listado de Servicios activos");
						strMensaje = strMensaje + "No se encontro listado de Servicios activos\n";
						resultado = false;
					}
				}
				if(resultado == false){
					//model.addAttribute("respuesta", "Error en el proceso de generacion masiva de las Cuentas por Cobrar de los Servicios");
					strMensaje = strMensaje + "Error en el proceso de generacion masiva de las Cuentas por Cobrar de los Servicios\n";
				}else{
					//Validamos los valores para actualizar la bitacora
					if (bitacora !=null && bitacora.getCodigoCxcBitacora() >0){
						//if (totalServicioMasivoFactura >0 || totalServicioMasivoBoleta >0 || totalServicioIndividual >0 || totalServicioInterno > 0){
							String resultadoAlquiler= "Se registro Masivamente ["+totalServicioMasivoFactura+"] Facturas, ["+totalServicioMasivoBoleta+"] Boletas, ["+totalServicioInterno+"] Internos y se identico ["+totalServicioIndividual+"] Servicios generados Individualmente.";
							bitacora.setResultado(resultadoAlquiler);
							this.preEditar(bitacora,request);
							cxcBitacoraDao.save(bitacora);
						//}
					}
				}
				
			}
			
			//Actualizando la Serie
			this.preEditarSerie(serieFactura, request);
			serieDao.save(serieFactura);
			this.preEditarSerie(serieBoleta, request);
			serieDao.save(serieBoleta);
			this.preEditarSerie(serieInterno, request);
			serieDao.save(serieInterno);
			model.addAttribute("respuesta", strMensaje);
		}catch(Exception e){
			e.printStackTrace();
			model.addAttribute("respuesta", e.getMessage());
		}finally{
			intTotalRegistros	= null;
			listaContrato		= null;
			listaLuz			= null;
			listaServicio		= null;
			documento			= null;
			bitacora			= null;
			criterio			= null;
			serieFactura		= null;
			serieBoleta			= null;
			serieInterno		= null;
			cxcDocumento		= null;
			cabecera			= null;
			detalle				= null;
		}
		return resultado;
	}
	/*
	 * Genera un archivo plano Cabecera
	 */
	public boolean generarArchivoCabecera(TblSunatCabecera cabecera){
		boolean resultado = false;
		String cadena = null;
		BufferedWriter bufferedWriter = null;
		//String FILENAME = "G:\\data0\\facturador\\DATA\\"+cabecera.getNombreArchivo();
		String FILENAME = Constantes.SUNAT_FACTURADOR_RUTA_PRUEBA + cabecera.getNombreArchivo();
		try{
			cadena = cabecera.getTipoOperacion() + Constantes.SUNAT_PIPE +
					 cabecera.getFechaEmision() + Constantes.SUNAT_PIPE +
					 cabecera.getDomicilioFiscal() + Constantes.SUNAT_PIPE +
					 cabecera.getTipoDocumentoUsuario() + Constantes.SUNAT_PIPE +
					 cabecera.getNumeroDocumento() + Constantes.SUNAT_PIPE +
					 cabecera.getRazonSocial()	+ Constantes.SUNAT_PIPE +
					 cabecera.getTipoMoneda() + Constantes.SUNAT_PIPE +
					 cabecera.getSumaDescuento() + Constantes.SUNAT_PIPE +
					 cabecera.getSumaCargo() + Constantes.SUNAT_PIPE +
					 cabecera.getTotalDescuento() + Constantes.SUNAT_PIPE +
					 cabecera.getOperacionGravada() + Constantes.SUNAT_PIPE +
					 cabecera.getOperacionInafecta() + Constantes.SUNAT_PIPE +
					 cabecera.getOperacionExonerada() + Constantes.SUNAT_PIPE +
					 cabecera.getMontoIgv() + Constantes.SUNAT_PIPE +
					 cabecera.getMontoIsc() + Constantes.SUNAT_PIPE +
					 cabecera.getOtrosTributos() + Constantes.SUNAT_PIPE +
					 cabecera.getImporteTotal();
			bufferedWriter = new BufferedWriter(new FileWriter(FILENAME, true));
			bufferedWriter.write(cadena); 
			resultado = true;
					
		}catch(Exception e){
			e.printStackTrace();
			resultado = false;
		}finally{
			try{
				if (bufferedWriter !=null){
					bufferedWriter.close();
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
		}
		return resultado;
	}
	/*
	 * Genera un archivo plano Detalle
	 */
	public boolean generarArchivoDetalle(TblSunatDetalle detalle){
		boolean resultado = false;
		String cadena = null;
		BufferedWriter bufferedWriter = null;
		String FILENAME = Constantes.SUNAT_FACTURADOR_RUTA_PRUEBA + detalle.getNombreArchivo();
		try{
			cadena = detalle.getCodigoUnidad() + Constantes.SUNAT_PIPE +
					 detalle.getCantidad() + Constantes.SUNAT_PIPE +
					 detalle.getCodigoProducto() + Constantes.SUNAT_PIPE +
					 detalle.getCodigoProductoSunat() + Constantes.SUNAT_PIPE +
					 detalle.getDescripcion() + Constantes.SUNAT_PIPE +
					 detalle.getValorUnitario()	+ Constantes.SUNAT_PIPE +
					 detalle.getDescuento() + Constantes.SUNAT_PIPE +
					 detalle.getMontoIgv() + Constantes.SUNAT_PIPE +
					 detalle.getAfectacionIgv() + Constantes.SUNAT_PIPE +
					 detalle.getMontoIsc() + Constantes.SUNAT_PIPE +
					 detalle.getTipoIsc() + Constantes.SUNAT_PIPE +
					 detalle.getPrecioVentaUnitario() + Constantes.SUNAT_PIPE +
					 detalle.getValorVentaItem();
			bufferedWriter = new BufferedWriter(new FileWriter(FILENAME, true));
			bufferedWriter.write(cadena); 
			resultado = true;
					
		}catch(Exception e){
			e.printStackTrace();
			resultado = false;
		}finally{
			try{
				if (bufferedWriter !=null){
					bufferedWriter.close();
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return resultado;
	}
	/*
	 * Monto Gravada
	 */
	public BigDecimal obtenerTotalMontoGravada(BigDecimal monto, Integer igv){
		BigDecimal resultado = null;
		try{
			resultado = new BigDecimal(monto.doubleValue()*(100-igv)/100).setScale(2, RoundingMode.CEILING);
			
		}catch(Exception e){
			resultado = new BigDecimal("0");
			e.printStackTrace();
		}
		return resultado;
	}
	/*
	 * Impuesto Gravada
	 */
	public BigDecimal obtenerTotalImpuestoGravada(BigDecimal monto, Integer igv){
		BigDecimal resultado = null;
		try{
			resultado = new BigDecimal(monto.doubleValue()*(igv)/100).setScale(2, RoundingMode.CEILING);
			
		}catch(Exception e){
			resultado = new BigDecimal("0");
			e.printStackTrace();
		}
		return resultado;
	}
	@Override
	public TblCxcBitacora getNuevaEntidad() {
		
		return null;
	}

	/**
	 * Se encarga de eliminar el documento generado
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cxc/eliminar/{id}", method = RequestMethod.GET)
	public String eliminarCobroGet(@PathVariable Integer id, Model model, HttpServletRequest request) {
		Integer intTotal					= null;
		TblCxcBitacora bitacora				= null;
		List<TblCxcDocumento> listaDocumento= null;
		List<TblSunatCabecera> listaCabecera= null;
		List<TblSunatDetalle> listaDetalle	= null;
		boolean resultado					= false;
		String strMensaje					= "";
		String path							= null;
		try{
			path = "caja/cxc/cxc_listado";
			bitacora = cxcBitacoraDao.findOne(id);
			if (bitacora != null){
				//validando que no se haya registrado ningun cobro
				intTotal = documentoDao.countTotalCobro(id, bitacora.getAnio(), bitacora.getMes());
				if (intTotal > 0){
					model.addAttribute("respuesta", "Se encontró ["+intTotal+"] cobros asociados, no se puede realizar la operación.");
				}else{
					//Listamos los documentos
					listaDocumento = documentoDao.listarDocumentoxBitacora(bitacora.getCodigoCxcBitacora());
					if (listaDocumento!=null){
						//Proceso de eliminacion del documento
						for(TblCxcDocumento documento: listaDocumento){
							listaCabecera = sunatCabeceraDao.listarSunatCabeceraxDocumento(documento.getCodigoCxcDoc());
							if (listaCabecera!=null){
								//Proceso de eliminacion de la cabecera
								for(TblSunatCabecera cabecera: listaCabecera){
									listaDetalle = sunatDetalleDao.listarSunatDetallexCabecera(cabecera.getCodigoCabecera());
									if (listaDetalle!=null){
										//Proceso de eliminacion del detalle
										for(TblSunatDetalle detalle: listaDetalle){
											//Eliminacion del detalle
											resultado = this.mEliminarDetalleSunat(detalle, request);
											if (!resultado){
												strMensaje = strMensaje + "[" + detalle.getCodigoDetalle() +"] Detalle Sunat no se pudo eliminar. ";
											}
										}
									}else{
										model.addAttribute("respuesta", "Error: No se encontró el detalle de los documentos de la SUNAT, para eliminar.");
										this.mListarBitacora(model, request);
										return path;
									}
									resultado = this.mEliminarCabeceraSunat(cabecera, request);
									if (!resultado){
										strMensaje = strMensaje + "[" + cabecera.getCodigoCabecera() +"] Cabecera Sunat no se pudo eliminar. ";
									}
								}
								resultado = this.mEliminarDocumento(documento, request);
								if (!resultado){
									strMensaje = strMensaje + "[" + documento.getCodigoCxcDoc() +"] Documento CxC no se pudo eliminar. ";
								}
							}else{
								//elimina el documento
								resultado = this.mEliminarDocumento(documento, request);
								if (!resultado){
									strMensaje = strMensaje + "[" + documento.getCodigoCxcDoc() +"] Documento CxC no se pudo eliminar. ";
								}
							}
						}
						//Eliminando la bitacora
						resultado = this.mEliminarBitacora(bitacora, request);
						if (!resultado){
							strMensaje = strMensaje + "[" + bitacora.getCodigoCxcBitacora() +"] Bitacora no se pudo eliminar. ";
						}
						if (!strMensaje.equals("")){
							model.addAttribute("respuesta", strMensaje);
						}else{
							model.addAttribute("respuesta", "Eliminación Exitosa");
						}
						
					}else{
						model.addAttribute("respuesta", "No se encontró documentos a eliminar. Vuelva a buscar y repita la operación.");
						this.mListarBitacora(model, request);
						return path;
					}
				}
				
			}else{
				model.addAttribute("respuesta", "No se encontró la bitacora a eliminar. Vuelva a buscar y repita la operación.");
			}
			//listamos las bitacoras
			this.mListarBitacora(model, request);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			intTotal		= null;
			bitacora		= null;
			listaDocumento	= null;
			listaCabecera	= null;
			listaDetalle	= null;
			strMensaje		= null;
		}
		return path;
	}
	/*
	 * ELiminacion logica del detalle de Sunat
	 */
	public boolean mEliminarDetalleSunat(TblSunatDetalle detalle, HttpServletRequest request){
		boolean resultado 		= false;
		try{
			//Campos de auditoria
			detalle.setFechaModificacion(new Date(System.currentTimeMillis()));
			detalle.setIpModificacion(request.getRemoteAddr());
			detalle.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			detalle.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			sunatDetalleDao.save(detalle);
			resultado = true;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultado;
	}
	/*
	 * ELiminacion logica de la cabecera de Sunat
	 */
	public boolean mEliminarCabeceraSunat(TblSunatCabecera cabecera, HttpServletRequest request){
		boolean resultado 		= false;
		try{
			//Campos de auditoria
			cabecera.setFechaModificacion(new Date(System.currentTimeMillis()));
			cabecera.setIpModificacion(request.getRemoteAddr());
			cabecera.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			cabecera.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			sunatCabeceraDao.save(cabecera);
			resultado = true;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultado;
	}
	/*
	 * ELiminacion logica de la cabecera de Sunat
	 */
	public boolean mEliminarDocumento(TblCxcDocumento entidad, HttpServletRequest request){
		boolean resultado 		= false;
		try{
			//Campos de auditoria
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			documentoDao.save(entidad);
			resultado = true;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultado;
	}
	/*
	 * ELiminacion logica de la cabecera de Sunat
	 */
	public boolean mEliminarBitacora(TblCxcBitacora entidad, HttpServletRequest request){
		boolean resultado 		= false;
		try{
			//Campos de auditoria
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			cxcBitacoraDao.save(entidad);
			resultado = true;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultado;
	}
	/*
	 * Lista la bitacora
	 */
	public void mListarBitacora(Model model, HttpServletRequest request){
		BitacoraBean filtro = null;
		filtro = (BitacoraBean)request.getSession().getAttribute("SessionFiltroCriterio");
		this.cargarListaCuentasxCobrar(model, filtro);
		model.addAttribute("filtro", filtro);
		//Listamos los datos
		model.addAttribute("registros", listarBitacora(model, filtro));
	}
}
