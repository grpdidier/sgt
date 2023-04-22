package com.pe.lima.sg.presentacion.cliente;

import static com.pe.lima.sg.dao.caja.CxCBitacoraSpecifications.conAnio;
import static com.pe.lima.sg.dao.caja.CxCBitacoraSpecifications.conMes;
import static com.pe.lima.sg.dao.caja.CxCBitacoraSpecifications.conTipoCobro;
import static com.pe.lima.sg.dao.caja.CxCBitacoraSpecifications.conTipoOperacion;
import static com.pe.lima.sg.dao.caja.CxCDocumentoSpecifications.conEstadoDocumento;
import static com.pe.lima.sg.dao.caja.CxCDocumentoSpecifications.conSaldoPositivo;
import static com.pe.lima.sg.dao.caja.CxCDocumentoSpecifications.conTipoReferencia;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conEdificio;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conEstado;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conMaterno;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conNombre;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conPaterno;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conRazonSocial;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conRuc;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conTienda;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
//import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
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

import com.pe.lima.sg.bean.caja.BitacoraBean;
import com.pe.lima.sg.bean.cliente.PeriodoBean;
import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.dao.caja.ICxCBitacoraDAO;
import com.pe.lima.sg.dao.caja.ICxCDocumentoDAO;
import com.pe.lima.sg.dao.cliente.IArbitrioDAO;
import com.pe.lima.sg.dao.cliente.IContratoClienteDAO;
import com.pe.lima.sg.dao.cliente.IContratoDAO;
import com.pe.lima.sg.dao.cliente.IContratoPrimerCobroDAO;
import com.pe.lima.sg.dao.cliente.IContratoServicioDAO;
import com.pe.lima.sg.dao.cliente.ILuzxTiendaDAO;
import com.pe.lima.sg.dao.cliente.IObservacionDAO;
import com.pe.lima.sg.dao.mantenimiento.IEdificioDAO;
import com.pe.lima.sg.dao.mantenimiento.IPersonaDAO;
import com.pe.lima.sg.dao.mantenimiento.ISerieDAO;
import com.pe.lima.sg.dao.mantenimiento.ITiendaDAO;
import com.pe.lima.sg.dao.mantenimiento.ITipoServicioDAO;
import com.pe.lima.sg.entity.caja.TblCxcBitacora;
//import com.pe.lima.sg.entity.caja.TblCxcBitacora;
import com.pe.lima.sg.entity.caja.TblCxcDocumento;
import com.pe.lima.sg.entity.cliente.TblArbitrio;
import com.pe.lima.sg.entity.cliente.TblContrato;
import com.pe.lima.sg.entity.cliente.TblContratoCliente;
import com.pe.lima.sg.entity.cliente.TblContratoPrimerCobro;
import com.pe.lima.sg.entity.cliente.TblContratoServicio;
import com.pe.lima.sg.entity.cliente.TblLuzxtienda;
import com.pe.lima.sg.entity.cliente.TblObservacion;
import com.pe.lima.sg.entity.mantenimiento.TblEdificio;
import com.pe.lima.sg.entity.mantenimiento.TblPersona;
import com.pe.lima.sg.entity.mantenimiento.TblSerie;
import com.pe.lima.sg.entity.mantenimiento.TblTienda;
import com.pe.lima.sg.presentacion.BaseOperacionPresentacion;
import com.pe.lima.sg.presentacion.BeanRequest;
import com.pe.lima.sg.presentacion.Campo;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.ListaUtilAction;
import com.pe.lima.sg.presentacion.util.PageWrapper;
import com.pe.lima.sg.presentacion.util.PageableSG;
import com.pe.lima.sg.presentacion.util.UtilSGT;

/**
 * Clase Bean que se encarga de la administracion de los contratos
 *
 * 			
 */
@Controller
public class CambioContratoAction extends BaseOperacionPresentacion<TblContrato> {

	private static final Logger logger = LogManager.getLogger(CambioContratoAction.class);
	
	@Autowired
	private IContratoDAO contratoDao;

	@Autowired
	private ITiendaDAO tiendaDao;

	//@Autowired
	//private ISuministroDAO suministroDao;
	
	@Autowired
	private ICxCDocumentoDAO documentoDao;

	@Autowired
	private IPersonaDAO personaDao;

	@Autowired
	private IArbitrioDAO arbitrioDao;
	

	@Autowired
	private IObservacionDAO observacionDao;
	
	@Autowired
	private ILuzxTiendaDAO luzxTiendaDao;

	@Autowired
	private IEdificioDAO edificioDao;

	@Autowired
	private IContratoClienteDAO contratoClienteDao;

	@Autowired
	private IContratoServicioDAO contratoServicioDao;

	@Autowired
	private IContratoPrimerCobroDAO contratoPrimerCobroDao;

	@Autowired
	private ListaUtilAction listaUtil;

	@Autowired
	private ITipoServicioDAO tipoServicioDao;
	
	@Autowired
	private ISerieDAO serieDao;
	

	@Autowired
	private ICxCBitacoraDAO cxcBitacoraDao;
	
	@Autowired
	private ICxCDocumentoDAO cxcDocumentoDao;

	@SuppressWarnings("rawtypes")
	@Override
	public BaseOperacionDAO getDao() {
		return contratoDao;
	}
	
	private String urlPaginado = "/cambiocontratos/paginado/"; 
	private String urlPaginadoCliente = "/cambiocontratos/cliente/paginado/";
	/**
	 * Se encarga de listar todos los contratos
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cambiocontratos", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path ,  PageableSG pageable, HttpServletRequest request) {
		TblContrato filtro = null;
		try{
			logger.debug("[traerRegistros] Inicio");
			path = "cliente/cambiocontrato/ccn_listado";
			filtro = new TblContrato();
			filtro.setTblPersona(new TblPersona());
			filtro.setTblTienda(new TblTienda());
			filtro.getTblTienda().setTblEdificio(new TblEdificio());
			model.addAttribute("filtro", filtro);
			listaUtil.cargarDatosModel(model, Constantes.MAP_LISTA_EDIFICIO);
			//this.listarContratos(model, filtro, pageable, this.urlPaginado);
			request.getSession().setAttribute("sessionFiltroCriterioCambio", filtro);
			request.getSession().setAttribute("sessionListaContratoCambio", null);
			request.getSession().setAttribute("PageContratoCambio", null);
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
	 * Se encarga de buscar la informacion del Contrato segun el filtro
	 * seleccionado
	 * 
	 * @param model
	 * @param contratoBean
	 * @return
	 */
	@RequestMapping(value = "/cambiocontratos/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, TblContrato filtro, String path,  PageableSG pageable, HttpServletRequest request) {
		//Map<String, Object> campos = null;
		path = "cliente/cambiocontrato/ccn_listado";
		try{
			logger.debug("[traerRegistrosFiltrados] Inicio");
			this.listarContratos(model, filtro, pageable, this.urlPaginado, request);
			model.addAttribute("filtro", filtro);
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

	/*** Listado de Contratos ***/
	private void listarContratos(Model model, TblContrato tblContrato,PageableSG pageable, String url, HttpServletRequest request){
		//List<TblContrato> entidades = new ArrayList<TblContrato>();
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "numero"));
		try{
			Specification<TblContrato> filtro = Specifications.where(conNombre(tblContrato.getTblPersona().getNombre()))
					.and(conPaterno(tblContrato.getTblPersona().getPaterno()))
					.and(conMaterno(tblContrato.getTblPersona().getMaterno()))
					.and(conEdificio(tblContrato.getTblTienda().getTblEdificio().getCodigoEdificio()))
					.and(conTienda(tblContrato.getTblTienda().getNumero()))
					.and(conRuc(tblContrato.getTblPersona().getNumeroRuc()))
					.and(conRazonSocial(tblContrato.getTblPersona().getRazonSocial()))
					.and(conEstado(Constantes.ESTADO_REGISTRO_ACTIVO));
			//entidades = contratoDao.findAll(filtro);
			//logger.debug("[listarContratos] entidades:"+entidades);
			//model.addAttribute("registros", entidades);
			pageable.setSort(sort);
			Page<TblContrato> entidadPage = contratoDao.findAll(filtro, pageable);
			PageWrapper<TblContrato> page = new PageWrapper<TblContrato>(entidadPage, url, pageable);
			model.addAttribute("registros", page.getContent());
			model.addAttribute("page", page);
			request.getSession().setAttribute("sessionFiltroCriterioCambio", tblContrato);
			request.getSession().setAttribute("sessionListaContratoCambio", page.getContent());
			request.getSession().setAttribute("PageContratoCambio", page);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//entidades = null;
		}
	}
	private void inicializaDataDefaultServicio(TblContratoServicio  contratoServicio){
		
		contratoServicio.getTblTipoServicio().setCodigoTipoServicio(Constantes.CODIGO_SERVICIO_MANTENIMIENTO);
		contratoServicio.setTipoMonedaServicio(Constantes.MONEDA_SOL);
		contratoServicio.setTipoDocumentoServicio(Constantes.TIPO_DOC_INTERNO);
	}
	
	public List<TblCxcDocumento> mListarAlquiler(Integer intCodigoContrato){
		Sort sort = new Sort(new Sort.Order(Direction.ASC, "codigoCxcDoc"));
		Specification<TblCxcDocumento> criterio	= null;
		List<TblCxcDocumento> listaCxcAlquiler;
		//conSaldoPositivo
		criterio = Specifications.where(com.pe.lima.sg.dao.caja.CxCDocumentoSpecifications.conCodigoContrato(intCodigoContrato))
				  .and(conSaldoPositivo(new BigDecimal("0")))
				  //.and(conAnioActual(UtilSGT.getAnioMesString(new Date())))
				  .and(conTipoReferencia(Constantes.TIPO_COBRO_ALQUILER));
		listaCxcAlquiler = cxcDocumentoDao.findAll(criterio,sort);
		return listaCxcAlquiler;
	}
	public List<TblCxcDocumento> mListarServicio(Integer intCodigoContrato){
		Sort sort = new Sort(new Sort.Order(Direction.ASC, "codigoCxcDoc"));
		Specification<TblCxcDocumento> criterio	= null;
		List<TblCxcDocumento> listaCxcServicio;
		//conSaldoPositivo
		criterio = Specifications.where(com.pe.lima.sg.dao.caja.CxCDocumentoSpecifications.conCodigoContrato(intCodigoContrato))
				  .and(conSaldoPositivo(new BigDecimal("0")))
				  .and(conEstadoDocumento(Constantes.ESTADO_REGISTRO_ACTIVO))
				  .and(conTipoReferencia(Constantes.TIPO_COBRO_SERVICIO));
		listaCxcServicio = cxcDocumentoDao.findAll(criterio,sort);
		return listaCxcServicio;
	}
	public List<TblCxcDocumento> mListarLuz(Integer intCodigoContrato){
		Sort sort = new Sort(new Sort.Order(Direction.ASC, "codigoCxcDoc"));
		Specification<TblCxcDocumento> criterio	= null;
		List<TblCxcDocumento> listaCxcLuz;
		//conSaldoPositivo
		criterio = Specifications.where(com.pe.lima.sg.dao.caja.CxCDocumentoSpecifications.conCodigoContrato(intCodigoContrato))
				  .and(conSaldoPositivo(new BigDecimal("0")))
				  //.and(conAnioActual(UtilSGT.getAnioMesString(new Date())))
				  .and(conTipoReferencia(Constantes.TIPO_COBRO_LUZ));
		listaCxcLuz = cxcDocumentoDao.findAll(criterio,sort);
		return listaCxcLuz;
	}
	/**
	 * Se encarga de direccionar a la pantalla de edicion del Contrato
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "cambiocontrato/editar/{id}", method = RequestMethod.GET)
	public String editarContrato(@PathVariable Integer id, Model model, TblContrato filtro, HttpServletRequest request) {
		TblContrato contrato 					= null;
		BeanRequest beanRequest					= null;
		List<TblContratoServicio> listaServicio	= null;
		List<TblContratoCliente> listaCliente	= null;
		List<TblContratoPrimerCobro> listaCobro	= null;
		List<TblArbitrio> listaArbitrio			= null;
		List<TblLuzxtienda> listaLuzxtienda		= null;
		List<TblContrato> listaContratoAnt		= null;
		List<TblObservacion> listaObservacion	= null;
		List<TblCxcDocumento> listaCxcAlquiler	= null;
		List<TblCxcDocumento> listaCxcServicio	= null;
		List<TblCxcDocumento> listaCxcLuz		= null;
		Filtro dataCambio = new Filtro();
		String path 							= "";
		try{
			contrato = contratoDao.findOne(id);
			model.addAttribute("contrato", contrato);
			listaServicio = contratoServicioDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			listaCliente = contratoClienteDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			listaCobro = contratoPrimerCobroDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			listaArbitrio = arbitrioDao.listarAllActivosxContrato(contrato.getCodigoContrato());
			listaLuzxtienda = luzxTiendaDao.listarLuzTiendaxContrato(contrato.getCodigoContrato());
			listaObservacion = observacionDao.listarObservacionxContrato(contrato.getCodigoContrato());
			listaCxcAlquiler = mListarAlquiler(contrato.getCodigoContrato());
			listaCxcServicio = mListarServicio(contrato.getCodigoContrato());
			listaCxcLuz = mListarLuz(contrato.getCodigoContrato());
			//Busqueda de los contratos Antiguos
			listaContratoAnt = getContratosAntiguos(contrato);
			
			beanRequest = new BeanRequest();
			beanRequest.setContrato(contrato);
			beanRequest.setContratoAntiguo(contrato);
			beanRequest.setContratoServicio(new TblContratoServicio());
			this.inicializaDataDefaultServicio(beanRequest.getContratoServicio());
			beanRequest.setListaServicio(listaServicio);
			beanRequest.setListaServicioAntiguo(listaServicio);
			beanRequest.setListaCliente(listaCliente);
			beanRequest.setContratoPrimerCobro(new TblContratoPrimerCobro());
			beanRequest.setListaPrimerCobro(listaCobro);
			beanRequest.setArbitrio(new TblArbitrio());
			beanRequest.setListaArbitrio(listaArbitrio);
			beanRequest.setListaLuzxTienda(listaLuzxtienda);
			beanRequest.setListaContrato(listaContratoAnt);
			beanRequest.setListaObservacion(listaObservacion);
			beanRequest.setObservacion(new TblObservacion());
			beanRequest.setListaCxcAlquiler(listaCxcAlquiler);
			beanRequest.setListaCxcServicio(listaCxcServicio);
			beanRequest.setListaCxcLuz(listaCxcLuz);
			
			this.cargarListasRequestBeanContrato(model, beanRequest);
			
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);
			path = "cliente/cambiocontrato/ccn_edicion";
			dataCambio.setNuevoMontoAlquiler(new BigDecimal("0"));
			model.addAttribute("dataCambio", dataCambio);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			contrato 			= null;
			beanRequest			= null;
			listaServicio		= null;
			listaCliente		= null;
			listaCobro			= null;
			listaArbitrio		= null;
		}
		return path;
	}


	@Override
	public TblContrato getNuevaEntidad() {
		
		return null;
	}


	/** Validamos logica del negocio**/
	@Override
	public boolean validarNegocio(Model model,TblContrato entidad, HttpServletRequest request){
		boolean exitoso = true;
		//Integer total 	= null;
		try{
			//Validando Local
			if (entidad.getTblTienda() == null || entidad.getTblTienda().getCodigoTienda()<=0){
				exitoso = false;
				model.addAttribute("resultadoContrato", "Debe seleccionar el Local antes de Registrar");
				return exitoso;
			}
			//Validando Cliente
			if (entidad.getTblPersona() == null || entidad.getTblPersona().getCodigoPersona()<=0){
				exitoso = false;
				model.addAttribute("resultadoContrato", "Debe seleccionar el Cliente antes de Registrar");
				return exitoso;
			}
			//Datos
			/*if (entidad.getFechaContrato() == null){
				exitoso = false;
				model.addAttribute("resultadoContrato", "Debe seleccionar la Fecha de Contrato antes de Registrar");
				return exitoso;
			}*/
			if (entidad.getFechaInicio() == null){
				exitoso = false;
				model.addAttribute("resultadoContrato", "Debe seleccionar la Fecha de Inicio de Contrato antes de Registrar");
				return exitoso;
			}
			if (entidad.getFechaFin() == null){
				exitoso = false;
				model.addAttribute("resultadoContrato", "Debe seleccionar la Fecha Fin de Contrato antes de Registrar");
				return exitoso;
			}
			if (entidad.getMontoAlquiler() == null || entidad.getMontoAlquiler().compareTo(new BigDecimal(0))<= 0){
				exitoso = false;
				model.addAttribute("resultadoContrato", "Debe ingresar el Monto del Alquiler antes de Registrar");
				return exitoso;
			}
			if (entidad.getTipoMonedaAlquiler() == null || entidad.getTipoMonedaAlquiler().equals("-1")){
				exitoso = false;
				model.addAttribute("resultadoContrato", "Debe seleccionar el Tipo de Moneda para el Alquiler antes de Registrar");
				return exitoso;
			}
			if (entidad.getTipoCobro() == null || entidad.getTipoCobro().equals("-1")){
				exitoso = false;
				model.addAttribute("resultadoContrato", "Debe seleccionar el Tipo de Cobro para el Alquiler antes de Registrar");
				return exitoso;
			}
			if (entidad.getTipoGarantia() == null || entidad.getTipoGarantia().equals("-1")){
				exitoso = false;
				model.addAttribute("resultadoContrato", "Debe seleccionar la Garantia antes de Registrar");
				return exitoso;
			}
			if (entidad.getTipoGarantia().equals(Constantes.DESC_TIPO_GARANTIA_CON) || entidad.getTipoGarantia().equals(Constantes.DESC_TIPO_GARANTIA_LLAVE)){
				if (entidad.getMontoGarantia() == null || entidad.getMontoGarantia().compareTo(new BigDecimal(0))<= 0){
					exitoso = false;
					model.addAttribute("resultadoContrato", "Debe ingresar el monto de la Garantia/Llave antes de Registrar");
					return exitoso;
				}
				if (entidad.getTipoMonedaGarantia() == null || entidad.getTipoMonedaGarantia().equals("-1")){
					exitoso = false;
					model.addAttribute("resultadoContrato", "Debe seleccionar el Tipo de Moneda de la Garantia/Llave antes de Registrar");
					return exitoso;
				}
				if (entidad.getTipoDocumentoGarantia() == null || entidad.getTipoDocumentoGarantia().equals("-1")){
					exitoso = false;
					model.addAttribute("resultadoContrato", "Debe seleccionar el Tipo de Documento de la Garantia/Llave antes de Registrar");
					return exitoso;
				}
			}

		}catch(Exception e){
			exitoso = false;
		}finally{
			//	total = null;
		}
		return exitoso;
	}
	/**
	 * Se encarga de Listar los locales
	 * seleccionado
	 * 
	 * @param model
	 * @param contratoBean
	 * @return
	 */
	@RequestMapping(value = "/cambiocontrato/locales", method = RequestMethod.POST)
	public String listarLocales(Model model, TblContrato contrato, String path, HttpServletRequest request) {
		path 						= "cliente/cambiocontrato/ccn_tie_listado";
		TblEdificio edificio 		= null;
		Filtro filtro 				= null;
		Map<String, Object> campos 	= null;
		BeanRequest beanRequest		= null;
		try{
			logger.debug("[listarLocales] Inicio");
			if (contrato.getTblTienda().getTblEdificio().getCodigoEdificio() <= 0){
				beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
				contrato = beanRequest.getContrato();
				model.addAttribute("contrato", contrato);
				model.addAttribute("contratoServicio", beanRequest.getContratoServicio());
				model.addAttribute("listaServicio", beanRequest.getListaServicio());
				model.addAttribute("contratoPrimerCobro", beanRequest.getContratoPrimerCobro());
				model.addAttribute("listaPrimerosCobros", beanRequest.getListaPrimerCobro());
				model.addAttribute("arbitrio", beanRequest.getArbitrio());
				model.addAttribute("listaArbitrios", beanRequest.getListaArbitrio());
				listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_MONEDA);
				listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_COBRO);
				listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_GARANTIA);
				listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_DOCUMENTO);

				request.getSession().setAttribute("beanRequest", beanRequest);
				model.addAttribute("resultadoContrato", "Debe seleccionar el Inmueble antes de listar los Locales");
				return "cliente/cambiocontrato/ccn_nuevo"; 
			}
			logger.debug("[listarLocales] Codigo :"+contrato.getTblTienda().getTblEdificio().getCodigoEdificio());
			model.addAttribute("contrato", contrato);
			edificio = edificioDao.findOne(contrato.getTblTienda().getTblEdificio().getCodigoEdificio());
			model.addAttribute("edificio", edificio);

			filtro = new Filtro();
			filtro.setCodigo(contrato.getTblTienda().getTblEdificio().getCodigoEdificio());
			filtro.setStrOperacion(Constantes.OPERACION_NUEVO);
			this.listarTiendas(model, filtro);
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			model.addAttribute("filtro", filtro);
			//Se mantiene en Session el contrato hasta retornar
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			beanRequest.setContrato(contrato);
			request.getSession().setAttribute("beanRequest", beanRequest);

		}catch(Exception e){
			logger.debug("[listarLocales] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			edificio	= null;
			filtro		= null;
			campos		= null;
		}
		logger.debug("[listarLocales] Fin");
		return path;
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
		return campos;
	}
	/*** Listado de Tiendas ***/
	private void listarTiendas(Model model, Filtro filtro){
		List<TblTienda> entidades = new ArrayList<TblTienda>();
		try{
			entidades = tiendaDao.listarTiendaDesocupada(filtro.getCodigo(), filtro.getNumero());
			logger.debug("[listarTiendaes] entidades:"+entidades);
			model.addAttribute("registros", entidades);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades = null;
		}
	}
	/**
	 * Se encarga de buscar la informacion del Tienda segun el filtro
	 * seleccionado
	 * 
	 * @param model
	 * @param tiendaBean
	 * @return
	 */
	@RequestMapping(value = "/cambiocontrato/locales/q", method = RequestMethod.POST)
	public String listarLocalesFiltrados(Model model, Filtro filtro, String path, HttpServletRequest request) {
		Map<String, Object> campos = null;
		TblEdificio edificio = null;
		path = "cliente/cambiocontrato/ccn_tie_listado";
		try{
			logger.debug("[listarLocalesFiltrados] Inicio");
			logger.debug("[listarLocalesFiltrados] Operacion:"+filtro.getStrOperacion());
			edificio = edificioDao.findOne(filtro.getCodigo());
			filtro.setCodigo(edificio.getCodigoEdificio());
			model.addAttribute("edificio", edificio);
			this.listarTiendas(model, filtro);
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			model.addAttribute("filtro", filtro);
		}catch(Exception e){
			logger.debug("[listarLocalesFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			campos		= null;
			edificio	= null;
		}
		logger.debug("[listarLocalesFiltrados] Fin");
		return path;
	}

	/**
	 * Se encarga de direccionar a la pantalla de contrato
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cambiocontrato/locales/seleccionar", method = RequestMethod.POST)
	public String asignarLocal(Model model, Filtro filtro, String path, HttpServletRequest request) {
		TblTienda entidad 					= null;
		TblContrato contrato				= null;
		BeanRequest beanRequest				= null;
		try{
			entidad = tiendaDao.findOne(filtro.getCodigo());
			//Se mantiene en Session el contrato hasta retornar
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			contrato = beanRequest.getContrato();
			//entidad.setTblSuministro(suministroDao.findOne(entidad.getTblSuministro().getCodigoSuministro()));
			contrato.setTblTienda(entidad);
			model.addAttribute("contrato", contrato);
			//Busqueda de los arbitrios del Local
			beanRequest.setListaArbitrio(arbitrioDao.listarAllActivosxTienda(entidad.getCodigoTienda()));
			model.addAttribute("listaArbitrios", beanRequest.getListaArbitrio());
			request.getSession().setAttribute("beanRequest", beanRequest);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return "cliente/cambiocontrato/ccn_nuevo";
	}
	/**
	 * Se encarga de direccionar a la pantalla de contrato
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cambiocontrato/locales/seleccionar/{id}", method = RequestMethod.GET)
	public String asignarLocalGet(@PathVariable Integer id, Model model, HttpServletRequest request) {
		TblTienda entidad 					= null;
		TblContrato contrato				= null;
		BeanRequest beanRequest				= null;
		try{
			entidad = tiendaDao.findOne(id);
			//Se mantiene en Session el contrato hasta retornar
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			contrato = beanRequest.getContrato();
			//entidad.setTblSuministro(suministroDao.findOne(entidad.getTblSuministro().getCodigoSuministro()));
			contrato.setTblTienda(entidad);
			model.addAttribute("contrato", contrato);
			//Busqueda de los arbitrios del Local
			beanRequest.setListaArbitrio(arbitrioDao.listarAllActivosxTienda(entidad.getCodigoTienda()));
			//model.addAttribute("listaArbitrios", beanRequest.getListaArbitrio());
			//request.getSession().setAttribute("beanRequest", beanRequest);

			model.addAttribute("contrato", contrato);
			model.addAttribute("contratoServicio", beanRequest.getContratoServicio());
			model.addAttribute("listaServicio", beanRequest.getListaServicio());
			model.addAttribute("contratoPrimerCobro", beanRequest.getContratoPrimerCobro());
			model.addAttribute("listaPrimerosCobros", beanRequest.getListaPrimerCobro());
			model.addAttribute("arbitrio", beanRequest.getArbitrio());
			model.addAttribute("listaArbitrios", beanRequest.getListaArbitrio());
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_MONEDA);
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_COBRO);
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_GARANTIA);
			listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_DOCUMENTO);

			request.getSession().setAttribute("beanRequest", beanRequest);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return "cliente/cambiocontrato/ccn_nuevo";
	}
	/**
	 * Se encarga de Listar los clientes
	 * seleccionado
	 * 
	 * @param model
	 * @param contratoBean
	 * @return
	 */
	@RequestMapping(value = "/cambiocontrato/clientes", method = RequestMethod.POST)
	public String listarClientes(Model model, TblContrato contrato, String path, HttpServletRequest request) {
		path 						= "cliente/cambiocontrato/ccn_per_listado";
		Filtro filtro 				= null;
		BeanRequest beanRequest		= null;
		try{
			logger.debug("[listarClientes] Inicio");
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			
			filtro = new Filtro();
			filtro.setStrOperacion(Constantes.OPERACION_EDITAR);
			model.addAttribute("contrato", beanRequest.getContrato());
			
			model.addAttribute("filtro", filtro);
			//this.listarPersonas(model, filtro);
			this.cargarListaOperacionCliente(model);
			model.addAttribute("registros", new ArrayList<TblPersona>());
			model.addAttribute("page", null);
			//Se mantiene en Session el contrato hasta retornar
			request.getSession().setAttribute("beanRequest", beanRequest);

		}catch(Exception e){
			logger.debug("[listarClientes] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			filtro		= null;
		}
		logger.debug("[listarClientes] Fin");
		return path;
	}
	/*** Listado de Personas ***/
	private void listarPersonas(Model model, Filtro filtro,  PageableSG pageable, String url){
		//List<TblPersona> entidades = new ArrayList<TblPersona>();
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "nombre"));
		try{
			/*entidades = personaDao.listarCriterios(filtro.getNombre(), filtro.getPaterno(), filtro.getMaterno(), filtro.getDni(), filtro.getRuc(), filtro.getRazonSocial());
			logger.debug("[listarPersonaes] entidades:"+entidades);
			model.addAttribute("registros", entidades);*/
			Specification<TblPersona> criterio = Specifications.where(com.pe.lima.sg.dao.mantenimiento.PersonaSpecifications.conNombre(filtro.getNombre()))
					.and(com.pe.lima.sg.dao.mantenimiento.PersonaSpecifications.conPaterno(filtro.getPaterno()))
					.and(com.pe.lima.sg.dao.mantenimiento.PersonaSpecifications.conMaterno(filtro.getMaterno()))
					.and(com.pe.lima.sg.dao.mantenimiento.PersonaSpecifications.conDNI(filtro.getDni()))
					.and(com.pe.lima.sg.dao.mantenimiento.PersonaSpecifications.conRuc(filtro.getRuc()))
					.and(com.pe.lima.sg.dao.mantenimiento.PersonaSpecifications.conRazonSocial(filtro.getRazonSocial()))
					.and(com.pe.lima.sg.dao.mantenimiento.PersonaSpecifications.conEstado(Constantes.ESTADO_REGISTRO_ACTIVO));
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
	/*** Listado de Personas ***/
	/*private void listarPersonas(Model model, Filtro filtro){
		List<TblPersona> entidades = new ArrayList<TblPersona>();
		try{
			entidades = personaDao.listarCriterios(filtro.getNombre(), filtro.getPaterno(), filtro.getMaterno(), filtro.getDni(), filtro.getRuc(), filtro.getRazonSocial());
			logger.debug("[listarPersonaes] entidades:"+entidades);
			model.addAttribute("registros", entidades);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades = null;
		}
	}*/
	/**
	 * Se encarga de buscar la informacion de los clientes segun el filtro
	 * seleccionado
	 * 
	 * @param model
	 * @param tiendaBean
	 * @return
	 */
	@RequestMapping(value = "/cambiocontrato/clientes/q", method = RequestMethod.POST)
	public String listarPersonasFiltrados(Model model, Filtro filtro, String path,  PageableSG pageable, HttpServletRequest request) {
		try{
			logger.debug("[listarPersonasFiltrados] Inicio");
			logger.debug("[listarPersonasFiltrados] Operacion:"+filtro.getStrOperacion());
			path = "cliente/cambiocontrato/ccn_per_listado";
			logger.debug("[listarPersonasFiltrados] Operacion:"+filtro.getStrOperacion());
			this.listarPersonas(model, filtro, pageable, this.urlPaginadoCliente);
			this.cargarListaOperacionCliente(model);
			model.addAttribute("filtro", filtro);
			request.getSession().setAttribute("sessionFiltroClienteCriterio", filtro);
		}catch(Exception e){
			logger.debug("[listarPersonasFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
		}
		logger.debug("[listarPersonasFiltrados] Fin");
		return path;
	}
	/*
	 * Paginado de Cliente
	 */
	@RequestMapping(value = "/cambiocontratos/cliente/paginado/{page}/{size}/{operacion}", method = RequestMethod.GET)
	public String paginarEntidadCliente(@PathVariable Integer page, @PathVariable Integer size, @PathVariable String operacion, Model model,  PageableSG pageable, HttpServletRequest request) {
		Filtro filtro = null;
		Map<String, Object> campos = null;
		String path = null;
		try{
			//LOGGER.debug("[traerRegistros] Inicio");
			path = "cliente/cambiocontrato/ccn_per_listado";
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
			filtro = (Filtro)request.getSession().getAttribute("sessionFiltroClienteCriterio");
			model.addAttribute("filtro", filtro);
			
			this.listarPersonas(model, filtro, pageable, this.urlPaginadoCliente);
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
		}catch(Exception e){
			//LOGGER.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		return path;
	}
	/**
	 * Se encarga de direccionar a la pantalla de contrato
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cambiocontrato/clientes/seleccionar", method = RequestMethod.POST)
	public String asignarCliente(Model model, Filtro filtro, String path, HttpServletRequest request) {
		TblPersona entidad 					= null;
		TblContrato contrato				= null;
		BeanRequest beanRequest				= null;
		try{
			entidad = personaDao.findOne(filtro.getCodigo());
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			//entidad.setTblSuministro(suministroDao.findOne(entidad.getTblSuministro().getCodigoSuministro()));
			contrato = beanRequest.getContrato();
			contrato.setTblPersona(entidad);
			model.addAttribute("contrato", contrato);
			request.getSession().setAttribute("beanRequest", beanRequest);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return "cliente/cambiocontrato/ccn_nuevo";
	}
	/**
	 * Se encarga de direccionar a la pantalla de contrato
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cambiocontrato/clientes/seleccionar/{id}", method = RequestMethod.GET)
	public String asignarClienteGet(@PathVariable Integer id, Model model, HttpServletRequest request) {
		TblPersona entidad 					= null;
		TblContrato contrato				= null;
		BeanRequest beanRequest				= null;
		String path							= null;
		try{
			entidad = personaDao.findOne(id);
			//Se mantiene en Session el contrato hasta retornar
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			contrato = beanRequest.getContrato();
			contrato.setTblPersona(entidad);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);
			
			path = "cliente/cambiocontrato/ccn_edicion";

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return path;
	}
	/**
	 * Se encarga de regresar a la pantalla de contrato (nuevo o edicion)
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cambiocontrato/regresar", method = RequestMethod.POST)
	public String regresarContrato(Model model, Filtro filtro, String path, HttpServletRequest request) {
		//TblContrato contrato				= null;
		BeanRequest beanRequest				= null;
		try{
			logger.debug("[regresarContrato] Inicio");
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			logger.debug("[regresarAprobacion] oPERACION:"+filtro.getStrOperacion());
			
			path = "cliente/cambiocontrato/ccn_edicion";
			//contrato = beanRequest.getContrato();
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);
			logger.debug("[regresarContrato] Fin");
		}catch(Exception e){
			logger.debug("[regresarContrato] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}

		return path;
	}
	/**
	 * Se encarga de adicionar un servicio
	 * 
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "/cambiocontrato/servicio", method = RequestMethod.POST)
	public String adicionarServicio(Model model, TblContratoServicio contratoServicio, TblContrato contrato, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;

		try{
			logger.debug("[adicionarServicio] Inicio");
			path = "cliente/cambiocontrato/ccn_edicion";
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			if (contratoServicio.getMonto()== null || contratoServicio.getMonto().doubleValue() <=0){
				model.addAttribute("resultadoServicio","Debe ingresar el monto del servicio!");
			}else{
				contratoServicio.setTotalAcumulado(contratoServicio.getMonto());
				contratoServicio.setTotalSaldo(contratoServicio.getMonto());
				contratoServicio.setTotalCobrado(new BigDecimal("0"));
				contratoServicio.setTblTipoServicio(tipoServicioDao.findOne(contratoServicio.getTblTipoServicio().getCodigoTipoServicio()));
				logger.debug("[adicionarServicio] Doc:"+contratoServicio.getTipoDocumentoServicio());
				logger.debug("[adicionarServicio] Moneda:"+contratoServicio.getTipoMonedaServicio());
				
				if (beanRequest.getListaServicio() == null) {
					beanRequest.setListaServicio(new ArrayList<TblContratoServicio>());
					if (!validarExistenciaServicio(beanRequest.getListaServicio(),contratoServicio)){
						beanRequest.getListaServicio().add(contratoServicio);
					}else{
						model.addAttribute("resultadoServicio","El Tipo de Servicio ya existe!");
					}

				}else{
					if (!validarExistenciaServicio(beanRequest.getListaServicio(),contratoServicio)){
						beanRequest.getListaServicio().add(contratoServicio);
					}else{
						model.addAttribute("resultadoServicio","El Tipo de Servicio ya existe!");
					}
				}
			}
			
			//model.addAttribute("listaServicio", beanRequest.getListaServicio());
			beanRequest.setContrato(contrato);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);

			logger.debug("[adicionarServicio] Fin");
		}catch(Exception e){
			logger.debug("[adicionarServicio] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			beanRequest = null;
		}

		return path;
	}

	private boolean validarExistenciaServicio(List<TblContratoServicio> lista, TblContratoServicio bean){
		boolean resultado = false;
		for(TblContratoServicio elemento:lista){
			if (elemento.getTblTipoServicio().getNombre().equals(bean.getTblTipoServicio().getNombre())){
				resultado = true;
				break;
			}
		}
		return resultado;
	}
	@RequestMapping(value = "/cambiocontrato/servicio/eliminar/{id}", method = RequestMethod.GET)
	public String eliminarServicioGet(@PathVariable String id, Model model, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		try{

			//Se mantiene en Session el contrato hasta retornar
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			if (removerExistenciaServicio(beanRequest,new Integer(id))){
				model.addAttribute("resultadoServicio","El Servicio se removio satisfactoriamente!");
				
			}
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);
			request.getSession().setAttribute("beanRequest", beanRequest);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			beanRequest = null;
		}
		return "cliente/cambiocontrato/ccn_edicion";
	}
	private boolean removerExistenciaServicio(BeanRequest beanRequest, int index){
		boolean resultado = false;
		List<TblContratoServicio> lista = beanRequest.getListaServicio();
		/*for(int i=0; i<lista.size(); i++){
			TblContratoServicio elemento = lista.get(i);
			if (elemento.getTblTipoServicio().getNombre().equals(nombre)){
				resultado = true;
				lista.remove(i);
				break;
			}
		}*/
		if (beanRequest.getListaServicioEliminar() == null ){
			beanRequest.setListaServicioEliminar(new ArrayList<TblContratoServicio>());
			beanRequest.getListaServicioEliminar().add(lista.get(index));
		}else{
			beanRequest.getListaServicioEliminar().add(lista.get(index));
		}
		lista.remove(index);
		resultado = true;
		return resultado;
	}
	
	private void removerServicio(BeanRequest beanRequest){
		List<TblCxcDocumento> listaDoc;
		if (beanRequest.getListaServicioEliminar() != null){
			for(TblContratoServicio servicio: beanRequest.getListaServicioEliminar()){
				servicio.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
				contratoServicioDao.save(servicio);
				listaDoc = documentoDao.listarCxCxContratoxTipoServicio(servicio.getCodigoServicio(), "SER");
				if (listaDoc != null){
					for (TblCxcDocumento documento: listaDoc){
						documento.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
						documentoDao.save(documento);
					}
				}
			}
		}
	}
	
	/**
	 * Se encarga de la eliminacion del servicio
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/cambiocontrato/eliminar/servicio", method = RequestMethod.POST)
	public String eliminarServicio(Model model, TblContrato entidad, HttpServletRequest request, Integer indice) {
		BeanRequest beanRequest				= null;
		try{
			logger.debug("[eliminarServicio] Inicio");
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			logger.debug("[eliminarServicio] indice:"+indice);
			logger.debug("[eliminarServicio] lista:"+beanRequest.getListaServicio().size());
			beanRequest.getListaServicio().remove(beanRequest.getListaServicio().get(indice));
			beanRequest.setContrato(entidad);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);
			logger.debug("[eliminarServicio] lista:"+beanRequest.getListaServicio().size());
			logger.debug("[eliminarServicio] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
		return  "cliente/cambiocontrato/ccn_edicion";
	}
	/**
	 * Se encarga de guardar la informacion del cambio en el valor del alquiler
	 * 
	 * @param 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/cambiocontrato/editar/guardar/alquiler", method = RequestMethod.POST)
	public String actualizarAlquiler(Model model, TblContrato entidad, Filtro dataAlquiler, HttpServletRequest request, String path ,  PageableSG pageable) {
		path 											= "cliente/cambiocontrato/ccn_listado";
		BeanRequest beanRequest							= null;

		String fechaCorte								= null;
		List<TblCxcDocumento> listaCxcAlquiler			= null;
		List<TblCxcDocumento> listaCxcAlquilerCambio	= null;
		boolean resultado = false;
		try{
			logger.debug("[actualizarAlquiler] Inicio");
			fechaCorte = this.getFechaCorte(dataAlquiler.getFechaAlquiler());
			logger.debug("[actualizarAlquiler] fechaCorte:"+fechaCorte);
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			listaCxcAlquiler = beanRequest.getListaCxcAlquiler();
			listaCxcAlquilerCambio = this.getListaCambio(listaCxcAlquiler, fechaCorte, dataAlquiler.getNuevoMontoAlquiler());
			resultado = this.saveMontoAlquiler(listaCxcAlquilerCambio, request, entidad.getCodigoContrato(), dataAlquiler.getNuevoMontoAlquiler());
			if (resultado) {
				model.addAttribute("resultadoContrato", "Se actualizó el monto del Alquiler y los cobros a partir de ["+dataAlquiler.getFechaAlquiler()+"] inclusive.");
			}else {
				model.addAttribute("resultadoContrato", "Se actualizó el monto del Alquiler pero no se encontró cobros del Alquiler para actualizar.");
			}
			this.editarContrato(entidad.getCodigoContrato(), model, entidad, request);
			logger.debug("[actualizarAlquiler] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
		return  "cliente/cambiocontrato/ccn_edicion";
	}
	/**
	 * Se encarga de guardar la informacion del cambio en el valor del servicio
	 * 
	 * @param 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/cambiocontrato/editar/guardar/servicio", method = RequestMethod.POST)
	public String actualizarServicio(Model model, TblContrato entidad, Filtro dataServicio, HttpServletRequest request, String path ,  PageableSG pageable) {
		path 											= "cliente/cambiocontrato/ccn_listado";
		BeanRequest beanRequest							= null;
		String fechaCorte								= null;
		List<TblCxcDocumento> listaCxcServicio			= null;
		List<TblCxcDocumento> listaCxcServicioCambio	= null;
		boolean resultado = false;
		try{
			logger.debug("[actualizarServicio] Inicio");
			fechaCorte = this.getFechaCorte(dataServicio.getFechaServicio());
			logger.debug("[actualizarServicio] fechaCorte:"+fechaCorte);
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			listaCxcServicio = beanRequest.getListaCxcServicio();
			listaCxcServicioCambio = this.getListaCambio(listaCxcServicio, fechaCorte, dataServicio.getNuevoMontoServicio());
			resultado = this.saveMontoDocumento(listaCxcServicioCambio, request);
			if (resultado) {
				model.addAttribute("resultadoContrato", "Se actualizó los cobros de Servicio a partir de ["+dataServicio.getFechaServicio()+"] inclusive.");
			}else {
				model.addAttribute("resultadoContrato", "No se encontró cobros del Servicio para actualizar.");
			}
			this.editarContrato(entidad.getCodigoContrato(), model, entidad, request);
			logger.debug("[actualizarServicio] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
		return  "cliente/cambiocontrato/ccn_edicion";
	}
	/*Actualizamos los montos del alquiler en CXC*/
	private boolean saveMontoDocumento(List<TblCxcDocumento> listaCxc, HttpServletRequest request) {
		boolean resultado = false;
		logger.debug("[saveMontoDocumento] Inicio");
		for(TblCxcDocumento documento:listaCxc ) {
			documento.setFechaModificacion(new Date(System.currentTimeMillis()));
			documento.setIpModificacion(request.getRemoteAddr());
			documento.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			cxcDocumentoDao.save(documento);
			resultado = true;
		}
		logger.debug("[saveMontoDocumento] Fin:"+resultado);
		return resultado;
	}
	/*Actualizamos los montos del alquiler en CXC*/
	private boolean saveMontoAlquiler(List<TblCxcDocumento> listaCxcAlquiler, HttpServletRequest request, Integer codigoContrato, BigDecimal montoAlquiler) {
		boolean resultado = false;
		TblContrato contrato = null;
		logger.debug("[saveMontoAlquiler] Inicio");
		contrato = contratoDao.findOne(codigoContrato);
		contrato.setMontoAlquiler(montoAlquiler);
		contrato.setFechaModificacion(new Date(System.currentTimeMillis()));
		contrato.setIpModificacion(request.getRemoteAddr());
		contrato.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
		contratoDao.save(contrato);
		logger.debug("[saveMontoAlquiler] Contrato Actualizado");
		resultado = this.saveMontoDocumento(listaCxcAlquiler, request);
		logger.debug("[saveMontoAlquiler] Fin:"+resultado);
		return resultado;
	}
	/*Obtenemos la fecha de corte para la comparación*/
	private String getFechaCorte(String fecha) {
		if (fecha != null && !fecha.isEmpty()) {
			return UtilSGT.getAnio(fecha).toString() + UtilSGT.getMes(fecha).toString();
		}else {
			return null;
		}
	}
	/*Obtenemos la lista para los cambios*/
	private List<TblCxcDocumento> getListaCambio(List<TblCxcDocumento> listaCxc, String fechaCorte, BigDecimal montoAlquiler){
		List<TblCxcDocumento> listaCxcCambio	= new ArrayList<TblCxcDocumento>();
		String fechaAlquiler = null;
		for(TblCxcDocumento documento: listaCxc) {
			fechaAlquiler = documento.getAnio().toString() + documento.getMes().toString();
			if (fechaAlquiler.compareTo(fechaCorte) >=0 ) {
				this.setNuevoMontoAlquiler(documento, montoAlquiler);
				listaCxcCambio.add(documento);
			}
		}
		logger.debug("[getListaAlquilerCambio] total elementos encontrados:"+listaCxcCambio.size());	
		
		return listaCxcCambio;
	}
	/*Actualiza monto y saldo*/
	private void setNuevoMontoAlquiler(TblCxcDocumento alquiler, BigDecimal montoAlquiler) {
		alquiler.setMontoGenerado(montoAlquiler);
		alquiler.setMontoContrato(montoAlquiler);
		alquiler.setSaldo(alquiler.getMontoGenerado().subtract(alquiler.getMontoCobrado()));
	}
	
	/**
	 * Se encarga de guardar la informacion 
	 * 
	 * @param 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/cambiocontrato/editar/guardar", method = RequestMethod.POST)
	public String actualizarEntidad(Model model, TblContrato entidad, HttpServletRequest request, String path ,  PageableSG pageable) {
		path 											= "cliente/cambiocontrato/ccn_listado";
		BeanRequest beanRequest							= null;
		TblContrato contrato							= null;
		TblContratoCliente cliente						= null;
		//List<TblContratoPrimerCobro> listaCobro			= null;
		TblContrato filtro								= null;
		try{
			logger.debug("[actualizarEntidad] Inicio" );
				logger.debug("[actualizarEntidad] Pre Guardar..." );
				beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
				beanRequest.setContrato(entidad);
				if (this.validarNegocio(model, entidad, request)){
					//Controles del Negocio ante los cambios realizados
					contrato = contratoDao.findOne(entidad.getCodigoContrato());
					beanRequest.setContrato(entidad);
					beanRequest.setContratoAntiguo(contrato);
					//Registro de los cambios: Cambio del monto del alquiler
					this.mProcesarCambiosContrato(beanRequest, request);
					
					
					
					//this.generarPrimerosCobros(request);
					this.preEditar(entidad, request);
					
					contrato.setFechaContrato(entidad.getFechaContrato());
					contrato.setFechaInicio(entidad.getFechaInicio());
					contrato.setFechaFin(entidad.getFechaFin());
					contrato.setMontoAlquiler(entidad.getMontoAlquiler());
					contrato.setTipoMonedaAlquiler(entidad.getTipoMonedaAlquiler());
					contrato.setTipoCobro(entidad.getTipoCobro());
					contrato.setTipoGarantia(entidad.getTipoGarantia());
					contrato.setMontoGarantia(entidad.getMontoGarantia());
					contrato.setTipoMonedaGarantia(entidad.getTipoMonedaGarantia());
					contrato.setTipoDocumentoGarantia(entidad.getTipoDocumentoGarantia());
					contrato.setInformacionAdicional(entidad.getInformacionAdicional());
					contrato.setPeriodoAdelanto(entidad.getPeriodoAdelanto());
					contrato.setTipoDocumentoAlquiler(entidad.getTipoDocumentoAlquiler());
					//contrato.setEstadoContrato(Constantes.ESTADO_CONTRATO_VIGENTE);
					contrato.setEstadoContrato(entidad.getEstadoContrato());
					contrato.setFechaModificacion(entidad.getFechaModificacion());
					contrato.setUsuarioModificacion(entidad.getUsuarioModificacion());
					contrato.setIpModificacion(entidad.getIpModificacion());
	
	
					if (contrato.getTblPersona().getCodigoPersona() != entidad.getTblPersona().getCodigoPersona()){
						contrato.setTblPersona(entidad.getTblPersona());
	
						//ContratoCliente
						cliente = new TblContratoCliente();
						cliente.setTblContrato(contrato);
						cliente.setCodigoPersona(entidad.getTblPersona().getCodigoPersona());
						cliente.setFechaCreacion(new Date(System.currentTimeMillis()));
						cliente.setIpCreacion(request.getRemoteAddr());
						cliente.setUsuarioCreacion(contrato.getUsuarioModificacion());
						cliente.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
						cliente.setFechaInicio(entidad.getFechaInicio());
						cliente.setFechaFin(entidad.getFechaFin());
						contratoClienteDao.save(cliente);
					}
					contrato.setTblAdelantos(entidad.getTblAdelantos());
					
					//Se quita primeros cobros - falta definir criterio y se excluye del proceso
					/*if(beanRequest.getListaPrimerCobroNuevo().size()>0){
						logger.debug("[actualizarEntidad] Hubo cambios en la lista de primeros cobros, borrando..." );
						contrato.setTblContratoPrimerCobros(new HashSet<TblContratoPrimerCobro>(0));
					}else{
						contrato.setTblContratoPrimerCobros(entidad.getTblContratoPrimerCobros());
					}*/
					
					contrato.setTblContratoServicios(entidad.getTblContratoServicios());
					//contrato.setTblCxcDocumentos(entidad.getTblCxcDocumentos());
					contrato.setTblTienda(entidad.getTblTienda());
	
					//super.editar(contrato, model);
					contratoDao.save(contrato);
					model.addAttribute("respuesta", "El contrato ["+contrato.getNumero()+"] fue actualizado satisfactoriamente.");
					
					
					
					logger.debug("[actualizarEntidad] Guardado..." );
					//registrando los primeros cobros
					
					/*listaCobro = beanRequest.getListaPrimerCobro();
					if (listaCobro!=null && listaCobro.size()>0){
						logger.debug("[guardarEntidad] listaCobro:"+listaCobro.size());
						for(TblContratoPrimerCobro cobro: listaCobro){
							cobro.setTblContrato(contrato);
							contratoPrimerCobroDao.save(cobro);
						}
					}
					listaCobro = beanRequest.getListaPrimerCobroNuevo();
					if (listaCobro!=null && listaCobro.size()>0){
						logger.debug("[guardarEntidad] listaCobro:"+listaCobro.size());
						for(TblContratoPrimerCobro cobro: listaCobro){
							cobro.setTblContrato(contrato);
							contratoPrimerCobroDao.save(cobro);
						}
					}*/
					//TODO: Modificación temporal: Se permite el registro de los nuevos servicio, se asume que no existian anteriormente servicios
					//registrando los servicios
					/*Inicio - Cambio Temporal*/
					
					List<TblContratoServicio> listaServicio 		= null;
					//List<TblContratoPrimerCobro> listaCobro			= null;
					//Primeros cobros servicios
					/* [2021-10-03: Duplicacion de primeros cobros - se comenta]
					 generarPrimerosCobrosServicios(entidad, request);
					
					//registrando los primeros cobros
					listaCobro = beanRequest.getListaPrimerCobro();
					if (listaCobro!=null && listaCobro.size()>0){
						logger.debug("[guardarEntidad] listaCobro:"+listaCobro.size());
						for(TblContratoPrimerCobro cobro: listaCobro){
							cobro.setTblContrato(contrato);
							cobro.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
							cobro.setFechaCreacion(new Date(System.currentTimeMillis()));
							cobro.setIpCreacion(request.getRemoteAddr());
							cobro.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
							contratoPrimerCobroDao.save(cobro);
						}
						//Generamos documentos
						entidad.setCodigoContrato(contrato.getCodigoContrato());
						this.generarDocumentoPrimerosCobros(model,entidad, request);

						
					}*/
					
					listaServicio = beanRequest.getListaServicio();
					if (listaServicio!=null && listaServicio.size()>0){
						logger.debug("[guardarEntidad] listaServicio:"+listaServicio.size());
						for(TblContratoServicio servicio: listaServicio){
							if (servicio.getCodigoServicio() <=0){
								servicio.setTblContrato(contrato);
								servicio = contratoServicioDao.save(servicio);
								this.generarDocumentoServicio(model, entidad, servicio, request);
							}
						}
					}
					//Eliminamos los servicios
					removerServicio(beanRequest);
					
					//Generar los servicios
					/*if (listaServicio!=null && listaServicio.size()>0){
						for(TblContratoServicio servicio: listaServicio){
							if (servicio.getCodigoServicio() <=0) {
								this.generarDocumentoServicio(model, entidad, servicio, request);
							}
						}
					}*/

					/*Fin cambio temporal*/
					//registro de las observaciones
					if (beanRequest.getListaObservacion()!=null && beanRequest.getListaObservacion().size()>0){
						for(TblObservacion observacion:beanRequest.getListaObservacion()){
							observacion.setCodigoContrato(contrato.getCodigoContrato());
							observacionDao.save(observacion);
						}
					}
	
					//registrando los servicios
					/*if (beanRequest.getListaServicio()!=null && beanRequest.getListaServicio().size()>0){
						logger.debug("[guardarEntidad] listaServicio:"+beanRequest.getListaServicio().size());
						for(TblContratoServicio servicio: beanRequest.getListaServicio()){
							servicio.setTblContrato(contrato);
							contratoServicioDao.save(servicio);
						}
					}
					if (beanRequest.getListaServicioAntiguo()!=null && beanRequest.getListaServicioAntiguo().size()>0){
						logger.debug("[guardarEntidad] listaServicio:"+beanRequest.getListaServicioAntiguo().size());
						for(TblContratoServicio servicio: beanRequest.getListaServicioAntiguo()){
							servicio.setTblContrato(contrato);
							contratoServicioDao.save(servicio);
						}
					}*/

					
					
					filtro = new TblContrato();
					filtro.setTblPersona(new TblPersona());
					filtro.setTblTienda(new TblTienda());
					filtro.getTblTienda().setTblEdificio(new TblEdificio());
					model.addAttribute("filtro", filtro);
					listaUtil.cargarDatosModel(model, Constantes.MAP_LISTA_EDIFICIO);
					this.listarContratos(model, filtro, pageable, this.urlPaginado,request);
				}else{
					path = "cliente/cambiocontrato/ccn_edicion";
					beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
					beanRequest.setContrato(entidad);	
					this.cargarListasRequestBeanContrato(model, beanRequest);
					
					this.cargarListaOperacionContrato(model);

					request.getSession().setAttribute("beanRequest", beanRequest);
				}
				
			logger.debug("[actualizarEntidad] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//beanRequest		= null;
			contrato		= null;
			cliente			= null;
			filtro			= null;
		}
		return path;

	}
	//Modificado solo para los servicio
	private boolean generarDocumentoPrimerosCobros(Model model, TblContrato entidad, HttpServletRequest request){
		TblSerie serieFactura					= null;
		TblSerie serieBoleta					= null;
		TblSerie serieInterno					= null;
		List<TblContratoPrimerCobro> lista		= null;
		BitacoraBean bitacoraBean				= null;
		Specification<TblCxcBitacora> criterio	= null;
		TblCxcBitacora bitacora					= null;
		TblCxcDocumento documento				= null;
		//TblSunatCabecera cabecera				= null;
		//TblSunatDetalle detalle					= null;
		boolean resultado 						= false;
		String strMensaje						= "";
		try{
			//Obtenemos Prefijo, Secuencia y Numero para generar los siguiente
			serieFactura = serieDao.buscarOneByTipoComprobante(Constantes.TIPO_COMPROBANTE_FACTURA);
			serieBoleta = serieDao.buscarOneByTipoComprobante(Constantes.TIPO_COMPROBANTE_BOLETA);
			serieInterno = serieDao.buscarOneByTipoComprobante(Constantes.TIPO_COMPROBANTE_INTERNO);
			
			//beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			
			//Lista de los primeros cobros
			//lista = beanRequest.getListaPrimerCobro();
			lista = contratoPrimerCobroDao.listarServicioXContrato(entidad.getCodigoContrato());
			if (lista !=null && lista.size()>0){
				//cxcBitacoraAction = new CxcBitacoraAction();
				bitacoraBean = new BitacoraBean();
				bitacoraBean.setAnio(UtilSGT.getAnioDate(entidad.getFechaInicio()));
				bitacoraBean.setMes(UtilSGT.getMesDate(entidad.getFechaInicio()).toString());
				
				//Registramos la Bitacora del CxC del alquiler
				bitacoraBean.setCodigoContrato(entidad.getCodigoContrato());
				/*boolean exitoso =  this.registrarBitacora(model, bitacoraBean, Constantes.TIPO_COBRO_PRIMER_COBRO, Constantes.SERIE_TIPO_OPERACION_PRIMEROS_COBROS, request);
				/*if (exitoso) {*/
					criterio = Specifications.where(conAnio(bitacoraBean.getAnio()))
							.and(conMes(new Integer(bitacoraBean.getMes())))
							.and(conTipoCobro(Constantes.TIPO_COBRO_PRIMER_COBRO))
							.and(com.pe.lima.sg.dao.caja.CxCBitacoraSpecifications.conCodigoContrato(entidad.getCodigoContrato()))
							.and((conTipoOperacion(Constantes.SERIE_TIPO_OPERACION_PRIMEROS_COBROS)));
					bitacora = cxcBitacoraDao.findOne(criterio);
					for(TblContratoPrimerCobro primerCobro: lista){
						documento = this.registrarCxCDocumentoPrimerCobro(model, bitacoraBean, request, entidad, primerCobro, bitacora, serieFactura, serieBoleta, serieInterno);
						//Actualizamos el numero del documento del primer cobro
						contratoPrimerCobroDao.save(primerCobro);
						/**AF20181214:No generamos aun el documento sino cuando se cobra el total del saldo*/
						
						if (documento!=null){
							//Se genera el documento de la sunat
							//this.generarDocumentoPrimerCobroSunat(documento, entidad, request);
							
						}else{
							//model.addAttribute("respuesta", "No se pudo registrar la Cuenta por Cobrar del Alquiler para el contrato ["+contrato.getNumero()+"]");
							strMensaje = strMensaje + "No se pudo registrar la Cuenta por Cobrar del Alquiler para el contrato ["+entidad.getNumero()+"]\n";
							model.addAttribute("respuesta", strMensaje);
							resultado = false;
							break;
						}
						
					}
					
				/*}*/
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			serieFactura		= null;
			serieBoleta			= null;
			serieInterno		= null;
			lista				= null;
			bitacoraBean		= null;
			criterio			= null;
			bitacora			= null;
			//documento			= null;
			//cabecera			= null;
			//detalle				= null;
			//strMensaje			= null;
		}
		return resultado;
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
			//validamos para identificar la garantia, se usara para finalizar contrato
			if (primerCobro.getObservacion().equals(Constantes.DESC_PRIMEROS_COBROS_GARANTIA)){
				documento.setTipoReferencia(Constantes.TIPO_COBRO_GARANTIA);
			}else if (primerCobro.getObservacion().equals(Constantes.DESC_PRIMEROS_COBROS_ALQUILER)){
				//documento.setTipoReferencia(Constantes.TIPO_COBRO_PRIMER_COBRO);	
				/*Se asigna el primer cobro de alquiler como cuota de alquiler, se elimino listado de primer cobro */
				documento.setTipoReferencia(Constantes.TIPO_COBRO_ALQUILER);	
				
			}else if (primerCobro.getObservacion().equals(Constantes.DESC_PRIMEROS_COBROS_SERVICIO)){
				documento.setTipoReferencia(Constantes.TIPO_COBRO_SERVICIO);	
			}else{
				documento.setTipoReferencia(Constantes.TIPO_COBRO_ALQUILER);
			}
			
			documento.setMontoGenerado(primerCobro.getMontoCobro());
			documento.setMontoContrato(primerCobro.getMontoCobro());
			documento.setMontoCobrado(new BigDecimal("0"));
			documento.setSaldo(primerCobro.getMontoCobro());
			documento.setTipoMoneda(primerCobro.getTipoMoneda());
			documento.setTipoDocumento(primerCobro.getTipoDocumento());
			documento.setNombre(primerCobro.getObservacion());
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
			documento.setFechaFinNombre(UtilSGT.getFechaNombre(new Integer(entidad.getMes()), entidad.getAnio()));
			//Bitacora
			documento.setTblCxcBitacora(bitacora);
			//Campos de auditoria
			this.preGuardarDocumento(documento, request);
			//Validamos el tipo de comprobante
			/*if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_FACTURA)){
				UtilSGT.obtenerSiguienteFactura(serieFactura);
				documento.setSerieComprobante(serieFactura.getPrefijoSerie()+UtilSGT.completarCeros(serieFactura.getSecuencialSerie().toString(), Constantes.SUNAT_LONGITUD_SERIE));
				documento.setNumeroComprobante(UtilSGT.completarCeros(serieFactura.getNumeroComprobante().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			}
			if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_BOLETA)){
				UtilSGT.obtenerSiguienteFactura(serieBoleta);
				documento.setSerieComprobante(serieBoleta.getPrefijoSerie()+UtilSGT.completarCeros(serieBoleta.getSecuencialSerie().toString(), Constantes.SUNAT_LONGITUD_SERIE));
				documento.setNumeroComprobante(UtilSGT.completarCeros(serieBoleta.getNumeroComprobante().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			}
			if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_INTERNO)){
				UtilSGT.obtenerSiguienteFactura(serieInterno);
				documento.setSerieComprobante(serieInterno.getPrefijoSerie()+UtilSGT.completarCeros(serieInterno.getSecuencialSerie().toString(), Constantes.SUNAT_LONGITUD_SERIE));
				documento.setNumeroComprobante(UtilSGT.completarCeros(serieInterno.getNumeroComprobante().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			}*/
			//Registrar: Se podria validar que no exista el registro "Codigo de Referencia" antes de registrar (luego en generacion individual)
			documentoDao.save(documento);
			//Para la actualizacion del primer cobro
			primerCobro.setNumeroDocumento(documento.getSerieComprobante() + "-"+ documento.getNumeroComprobante());
			//Buscamos el documento registrado
			//documento = documentoDao.findByAnioMesCodigoReferencia(documento.getTipoReferencia(), documento.getCodigoReferencia(), documento.getAnio(), documento.getMes());
		}catch(Exception e){
			e.printStackTrace();
		}
		return documento;
	}
	
	/*
	 * Genera los primeros cobros del contrato
	 */
	public void generarPrimerosCobrosServicios(TblContrato entidad, HttpServletRequest request){
		TblContratoPrimerCobro primerCobro 	= new TblContratoPrimerCobro();
		//Date datFechaFinContrato			= null;
		//Date datFecha						= null;
		//Double dblFactor					= null;
		BeanRequest beanRequest				= null;
		List<TblContratoPrimerCobro> lista	= new ArrayList<TblContratoPrimerCobro>();
		Date fechaFinMes					= null;
		try{
			fechaFinMes = UtilSGT.getDatetoString(UtilSGT.getLastDate(UtilSGT.getDateStringFormat(entidad.getFechaInicio())));
			
			//Servicios
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			if(beanRequest.getListaServicio()!=null && beanRequest.getListaServicio().size()>0){
				for(TblContratoServicio servicio:beanRequest.getListaServicio()){
					primerCobro 	= new TblContratoPrimerCobro();
					primerCobro.setTblTipoServicio(servicio.getTblTipoServicio());
					primerCobro.setTipoMoneda(servicio.getTipoMonedaServicio());
					/*primerCobro.setMontoCobro(new BigDecimal(dblFactor*servicio.getMonto().doubleValue()));*/
					primerCobro.setMontoCobro(servicio.getPrimerCobroServicio());
					primerCobro.setObservacion(Constantes.DESC_PRIMEROS_COBROS_SERVICIO);
					primerCobro.setFechaCobro(fechaFinMes);
					primerCobro.setTipoDocumento(servicio.getTipoDocumentoServicio());
					lista.add(primerCobro);
				}
			}
			
			
			beanRequest.setListaPrimerCobro(lista);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			primerCobro 			= null;
			//datFecha				= null;
			//dblFactor				= null;
			beanRequest				= null;
			lista					= null;
			fechaFinMes				= null;
		}
	}
	/*
	 * Preguardar: Campos de auditoria
	 */
	public void preGuardarBitacora(TblCxcBitacora entidad, HttpServletRequest request) {
		try{
			logger.debug("[preGuardarBitacora] Inicio" );
			entidad.setFechaCreacion(new Date(System.currentTimeMillis()));
			entidad.setIpCreacion(request.getRemoteAddr());
			entidad.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			logger.debug("[preGuardarBitacora] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	 * Guardar datos de la bitacora del CxC
	 */
	public TblCxcBitacora registrarBitacora(Model model, BitacoraBean entidad, String tipoCobro, String tipoOperacion, HttpServletRequest request){
		TblCxcBitacora bitacora		= null;
		try{
			bitacora = new TblCxcBitacora();
			this.preGuardarBitacora(bitacora, request);
			bitacora.setAnio(entidad.getAnio());
			bitacora.setMes(new Integer(entidad.getMes()));
			bitacora.setTipoOperacion(tipoOperacion);
			bitacora.setTipoCobro(tipoCobro);
			bitacora.setCodigoContrato(entidad.getCodigoContrato());
			bitacora = cxcBitacoraDao.save(bitacora);
		}catch(Exception e){
			e.printStackTrace();
			bitacora	= null;
		}
		return bitacora;
	}
	
	private boolean generarDocumentoServicio(Model model, TblContrato entidad, TblContratoServicio servicio, HttpServletRequest request){
		TblSerie serieFactura					= null;
		TblSerie serieBoleta					= null;
		TblSerie serieInterno					= null;
		List<PeriodoBean> lista					= null;
		BitacoraBean bitacoraBean				= null;
		//Specification<TblCxcBitacora> criterio	= null;
		TblCxcBitacora bitacora					= null;
		TblCxcDocumento documento				= null;
		//TblSunatCabecera cabecera				= null;
		//TblSunatDetalle detalle					= null;
		boolean resultado 						= false;
		String strMensaje						= "";
		try{
			//Obtenemos Prefijo, Secuencia y Numero para generar los siguiente
			serieFactura = serieDao.buscarOneByTipoComprobante(Constantes.TIPO_COMPROBANTE_FACTURA);
			serieBoleta = serieDao.buscarOneByTipoComprobante(Constantes.TIPO_COMPROBANTE_BOLETA);
			serieInterno = serieDao.buscarOneByTipoComprobante(Constantes.TIPO_COMPROBANTE_INTERNO);
			
			//Lista de los meses a cobrar
			lista = UtilSGT.mGetListAnioMes(entidad.getFechaInicio(), entidad.getFechaFin());
			if (lista !=null && lista.size()>0){
				//cxcBitacoraAction = new CxcBitacoraAction();
				bitacoraBean = new BitacoraBean();
				bitacoraBean.setAnio(UtilSGT.getAnioDate(entidad.getFechaInicio()));
				bitacoraBean.setMes(UtilSGT.getMesDate(entidad.getFechaInicio()).toString());
				
				//Registramos la Bitacora del CxC del servicio
				bitacoraBean.setCodigoContrato(entidad.getCodigoContrato());
				bitacora =  this.registrarBitacora(model, bitacoraBean, Constantes.TIPO_COBRO_SERVICIO, Constantes.SERIE_TIPO_OPERACION_CONTRATO, request);
				if (bitacora != null) {
					/*criterio = Specifications.where(conAnio(bitacoraBean.getAnio()))
							.and(conMes(new Integer(bitacoraBean.getMes())))
							.and(conTipoCobro(Constantes.TIPO_COBRO_SERVICIO))
							.and(conCodigoContrato(entidad.getCodigoContrato()))
							.and((conTipoOperacion(Constantes.SERIE_TIPO_OPERACION_CONTRATO)));
					bitacora = cxcBitacoraDao.findOne(criterio);*/
					for(PeriodoBean periodo: lista){
						documento = this.registrarCxCDocumentoServicio(model, servicio, request, entidad, periodo, bitacora, serieFactura, serieBoleta, serieInterno);
						//Actualizamos el numero del documento del primer cobro
						//contratoPrimerCobroDao.save(primerCobro);
						/**AF20181214:No generamos aun el documento sino cuando se cobra el total del saldo*/
						
						if (documento!=null){
							//Se genera el documento de la sunat
							//this.generarDocumentoPrimerCobroSunat(documento, entidad, request);
							
						}else{
							//model.addAttribute("respuesta", "No se pudo registrar la Cuenta por Cobrar del Alquiler para el contrato ["+contrato.getNumero()+"]");
							strMensaje = strMensaje + "No se pudo registrar la Cuenta del Alquiler para el contrato ["+entidad.getNumero()+"]\n";
							model.addAttribute("respuesta", strMensaje);
							resultado = false;
							break;
						}
						
					}
					
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			serieFactura		= null;
			serieBoleta			= null;
			serieInterno		= null;
			lista				= null;
			bitacoraBean		= null;
			//criterio			= null;
			bitacora			= null;
			//documento			= null;
			//cabecera			= null;
			//detalle				= null;
			//strMensaje			= null;
		}
		return resultado;
	}
	
	public TblCxcDocumento registrarCxCDocumentoServicio(Model model, TblContratoServicio servicio, HttpServletRequest request, TblContrato contrato, PeriodoBean periodo,TblCxcBitacora bitacora,TblSerie serieFactura,TblSerie serieBoleta,TblSerie serieInterno){
		TblCxcDocumento documento	= null;
		try{
			documento = new TblCxcDocumento();
			documento.setCodigoContrato(contrato.getCodigoContrato());
			documento.setCodigoReferencia(servicio.getCodigoServicio());
			documento.setTipoReferencia(Constantes.TIPO_COBRO_SERVICIO);
			
			documento.setMontoGenerado(servicio.getMonto());
			documento.setMontoContrato(servicio.getMonto());
			documento.setMontoCobrado(new BigDecimal("0"));
			documento.setSaldo(servicio.getMonto());
			documento.setTipoMoneda(servicio.getTipoMonedaServicio());
			documento.setTipoDocumento(servicio.getTipoDocumentoServicio());
			documento.setNombre(Constantes.DESC_CUOTA_SERVICIO);
			//validamos el tipo de documento
			if (documento.getTipoDocumento().equals(Constantes.TIPO_DOC_FACTURA)){
				documento.setTipoComprobante(Constantes.TIPO_COMPROBANTE_FACTURA);
			}else if (documento.getTipoDocumento().equals(Constantes.TIPO_DOC_BOLETA)){
				documento.setTipoComprobante(Constantes.TIPO_COMPROBANTE_BOLETA);
			}else if (documento.getTipoDocumento().equals(Constantes.TIPO_DOC_INTERNO)){
				documento.setTipoComprobante(Constantes.TIPO_COMPROBANTE_INTERNO);
			}
			//Fechas
			documento.setAnio(periodo.getAnio());
			documento.setMes(periodo.getMes());
			documento.setFechaFin(UtilSGT.getDatetoString(UtilSGT.getLastDay(periodo.getMes(), periodo.getAnio())));
			documento.setFechaFinNombre(UtilSGT.getFechaNombre(periodo.getMes(), periodo.getAnio()));
			//Bitacora
			documento.setTblCxcBitacora(bitacora);
			//Campos de auditoria
			this.preGuardarDocumento(documento, request);
			//Validamos el tipo de comprobante
			/**AF20181214:No se crea el documento sino luego del cobro, saldo cero*/
			/*if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_FACTURA)){
				UtilSGT.obtenerSiguienteFactura(serieFactura);
				documento.setSerieComprobante(serieFactura.getPrefijoSerie()+UtilSGT.completarCeros(serieFactura.getSecuencialSerie().toString(), Constantes.SUNAT_LONGITUD_SERIE));
				documento.setNumeroComprobante(UtilSGT.completarCeros(serieFactura.getNumeroComprobante().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			}
			if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_BOLETA)){
				UtilSGT.obtenerSiguienteFactura(serieBoleta);
				documento.setSerieComprobante(serieBoleta.getPrefijoSerie()+UtilSGT.completarCeros(serieBoleta.getSecuencialSerie().toString(), Constantes.SUNAT_LONGITUD_SERIE));
				documento.setNumeroComprobante(UtilSGT.completarCeros(serieBoleta.getNumeroComprobante().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			}
			if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_INTERNO)){
				UtilSGT.obtenerSiguienteFactura(serieInterno);
				documento.setSerieComprobante(serieInterno.getPrefijoSerie()+UtilSGT.completarCeros(serieInterno.getSecuencialSerie().toString(), Constantes.SUNAT_LONGITUD_SERIE));
				documento.setNumeroComprobante(UtilSGT.completarCeros(serieInterno.getNumeroComprobante().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			}*/
			//Registrar: Se podria validar que no exista el registro "Codigo de Referencia" antes de registrar (luego en generacion individual)
			documentoDao.save(documento);
			//Buscamos el documento registrado
			//documento = documentoDao.findByAnioMesCodigoReferencia(documento.getTipoReferencia(), documento.getCodigoReferencia(), documento.getAnio(), documento.getMes());
		}catch(Exception e){
			e.printStackTrace();
		}
		return documento;
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
	
	private void mProcesarCambiosContrato(BeanRequest beanRequest, HttpServletRequest request){
		//variación del monto del alquiler
		if (beanRequest.getContrato().getMontoAlquiler().compareTo(beanRequest.getContratoAntiguo().getMontoAlquiler())!=0){
			this.mActualizarAlquilerCxC(beanRequest, request);
		}
		//Otros escenarios
		
	}
	
	private void mActualizarAlquilerCxC(BeanRequest beanRequest, HttpServletRequest request){
		List<TblCxcDocumento> listaAlquiler = null; 
		listaAlquiler = documentoDao.listarDocumentoxReferenciaxSaldo(beanRequest.getContrato().getCodigoContrato(), Constantes.TIPO_COBRO_ALQUILER);
		if (listaAlquiler != null){
			for (TblCxcDocumento documento: listaAlquiler){
				if (documento.getMontoCobrado().doubleValue() > 0){
					if(beanRequest.getContrato().getMontoAlquiler().subtract(documento.getMontoCobrado()).doubleValue() >0){
						documento.setMontoGenerado(beanRequest.getContrato().getMontoAlquiler());
						documento.setMontoContrato(beanRequest.getContrato().getMontoAlquiler());
						documento.setSaldo(beanRequest.getContrato().getMontoAlquiler().subtract(documento.getMontoCobrado()));
					}else{
						//No se toca el saldo, existe un monto cobrado, es mejor realizar el ajuste por refinanciacion
						
					}
				}else{
					documento.setMontoGenerado(beanRequest.getContrato().getMontoAlquiler());
					documento.setMontoContrato(beanRequest.getContrato().getMontoAlquiler());
					documento.setSaldo(beanRequest.getContrato().getMontoAlquiler());
				}
				this.preEditarDocumento(documento, request);
				documentoDao.save(documento);
			}
		}
		
	}
	//Asigna campos de auditoria para el documento
	private void preEditarDocumento(TblCxcDocumento entidad, HttpServletRequest request) {
		try{
			logger.debug("[preEditarDocumento] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			logger.debug("[preEditarDocumento] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public void preEditar(TblContrato entidad, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		List<TblObservacion> listaObs		= null;
		try{
			logger.debug("[preEditar] Inicio" );
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			//primeros cobros
			if(beanRequest.getListaPrimerCobroNuevo()!=null && beanRequest.getListaPrimerCobroNuevo().size()>0){
				if(beanRequest.getListaPrimerCobro()!=null){
					for(TblContratoPrimerCobro primer: beanRequest.getListaPrimerCobro()){
						primer.setFechaModificacion(new Date(System.currentTimeMillis()));
						primer.setIpModificacion(request.getRemoteAddr());
						primer.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
						primer.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
					
					}
				}
				if(beanRequest.getListaPrimerCobroNuevo()!=null){
					for(TblContratoPrimerCobro primer: beanRequest.getListaPrimerCobroNuevo()){
						primer.setFechaCreacion(new Date(System.currentTimeMillis()));
						primer.setIpCreacion(request.getRemoteAddr());
						primer.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
						primer.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
					
					}
				}
				
			}
			//servicios
			
				if(beanRequest.getListaServicioAntiguo()!=null){
					for(TblContratoServicio objeto: beanRequest.getListaServicioAntiguo()){
						objeto.setFechaModificacion(new Date(System.currentTimeMillis()));
						objeto.setIpModificacion(request.getRemoteAddr());
						objeto.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
						objeto.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
					
					}
				}
				if(beanRequest.getListaServicio()!=null){
					for(TblContratoServicio objeto: beanRequest.getListaServicio()){
						objeto.setFechaCreacion(new Date(System.currentTimeMillis()));
						objeto.setIpCreacion(request.getRemoteAddr());
						objeto.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
						objeto.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
					
					}
				}
				//Para lista de observaciones
				listaObs = beanRequest.getListaObservacion();
				for(TblObservacion pc:listaObs){
					pc.setFechaCreacion(new Date(System.currentTimeMillis()));
					pc.setIpCreacion(request.getRemoteAddr());
					pc.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
					pc.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
				}
			

			logger.debug("[preEditar] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			listaObs 		= null;
			beanRequest		= null;
		}


	}

	
	/*
	 * Carga las listas en la sesion para las operaciones del contrato
	 */
	
	private void cargarListaOperacionContrato(Model model){
		
		listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_MONEDA);
		listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_COBRO);
		listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_GARANTIA);
		listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_DOCUMENTO);
		listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PERIODO_ADELANTO);
	}
	/*
	 * Carga en session las listas y beans necesarios para la operacion del contrato
	 */
	private void cargarListasRequestBeanContrato(Model model, BeanRequest beanRequest){
		model.addAttribute("contrato", beanRequest.getContrato());
		model.addAttribute("contratoServicio", beanRequest.getContratoServicio());
		model.addAttribute("listaServicio", beanRequest.getListaServicio());
		model.addAttribute("contratoPrimerCobro", beanRequest.getContratoPrimerCobro());
		model.addAttribute("listaPrimerosCobros", beanRequest.getListaPrimerCobro());
		model.addAttribute("arbitrio", beanRequest.getArbitrio());
		model.addAttribute("listaArbitrios", beanRequest.getListaArbitrio());
		model.addAttribute("listaLuzxTienda", beanRequest.getListaLuzxTienda());
		model.addAttribute("luzxTienda", beanRequest.getLuzxTienda());
		model.addAttribute("listaContratoAnterior", beanRequest.getListaContrato());
		model.addAttribute("observacion", beanRequest.getObservacion());
		model.addAttribute("listaObservacion", beanRequest.getListaObservacion());
		model.addAttribute("listaCxcAlquiler", beanRequest.getListaCxcAlquiler());
		model.addAttribute("listaCxcServicio", beanRequest.getListaCxcServicio());
		model.addAttribute("listaCxcLuz", beanRequest.getListaCxcLuz());
	}
	
/*Busqueda de contratos antiguos*/
	
	private List<TblContrato> getContratosAntiguos(TblContrato contrato){
		List<TblContrato> listaContrato = null;
		TblContrato contratoAnt			= null;
		boolean encontrado 				= true;
		Integer codigoContrato			= null;
		try{
			if(contrato.getCodigoPadreContrato()!=null && contrato.getCodigoPadreContrato() > 0){
				codigoContrato = contrato.getCodigoPadreContrato();
				do{
					contratoAnt = contratoDao.findOne(codigoContrato);
					if (contratoAnt !=null){
						if (listaContrato==null){
							listaContrato = new ArrayList<TblContrato>();
							listaContrato.add(contratoAnt);
						}else{
							listaContrato.add(contratoAnt);
						}
						//verificacion de mas contratos
						if (contratoAnt.getCodigoPadreContrato()!=null && contratoAnt.getCodigoPadreContrato() > 0){
							encontrado = true;
							codigoContrato = contratoAnt.getCodigoPadreContrato();
						}else{
							encontrado = false;
						}
					}else{
						encontrado = false;
					}
					
					
				}while(encontrado);
			}
		}catch(Exception e){
			
		}
		return listaContrato;
		
	}
	
	/*
	 * Carga las listas en la sesion para las operaciones del contrato
	 */
	
	private void cargarListaOperacionCliente(Model model){
		
		listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PERSONA);
		listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_CIVIL);
		listaUtil.cargarDatosModel(model, Constantes.MAP_SI_NO);
	}
	/**
	 * Se encarga de Listar los clientes 
	 * historicos
	 * 
	 * @param model
	 * @param contratoBean
	 * @return
	 */
	@RequestMapping(value = "/cambiocontrato/cliente/historial", method = RequestMethod.POST)
	public String listarClientesHistorial(Model model, TblContrato contrato, String path, HttpServletRequest request) {
		path 						= "cliente/cambiocontrato/ccn_per_historial";
		List<TblPersona> listaHistorial = null;
		Filtro filtro 					= null;
		try{
			logger.debug("[listarClientesHistorial] Inicio");
			
			listaHistorial = personaDao.listarPersonasxContrato(contrato.getCodigoContrato());
					
			model.addAttribute("registros", listaHistorial);
			filtro = new Filtro();
			
			filtro.setStrOperacion(Constantes.OPERACION_EDITAR);
			
			model.addAttribute("filtro", filtro);

		}catch(Exception e){
			logger.debug("[listarClientesHistorial] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			listaHistorial		= null;
		}
		logger.debug("[listarClientesHistorial] Fin");
		return path;
	}
	/**
	 * Se encarga de adicionar los primeros cobros
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cambiocontrato/observacion_v2/edicion", method = RequestMethod.POST)
	public String adicionarObservacion(Model model, TblObservacion observacionBean, TblContrato contrato, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		try{
			logger.debug("[adicionarObservacion] Inicio:");
			path = "cliente/cambiocontrato/ccn_edicion";
			
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			
			if (observacionBean.getAsunto()== null || observacionBean.getAsunto().equals("")){
				model.addAttribute("resultadoObservacion", "Debe ingresar el asunto de la Observacion");
				beanRequest.setContrato(contrato);
				this.cargarListasRequestBeanContrato(model, beanRequest);
				this.cargarListaOperacionContrato(model);
				return path;
			}
			/*if (observacionBean.getBreveDescripcion()== null || observacionBean.getBreveDescripcion().equals("")){
				model.addAttribute("resultadoObservacion", "Debe ingresar una descripcion breve");
				return path;
			}*/
			if (observacionBean.getDescripcion()== null || observacionBean.getDescripcion().equals("")){
				model.addAttribute("resultadoObservacion", "Debe ingresar una descripcion");
				beanRequest.setContrato(contrato);
				this.cargarListasRequestBeanContrato(model, beanRequest);
				this.cargarListaOperacionContrato(model);
				return path;
			}
			
			if (beanRequest.getListaObservacion() == null) {
				beanRequest.setListaObservacion(new ArrayList<TblObservacion>());
				beanRequest.getListaObservacion().add(observacionBean);
			}else{
				beanRequest.getListaObservacion().add(observacionBean);
			}
			beanRequest.setContrato(contrato);
			beanRequest.setObservacion(new TblObservacion());
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);
			
			
			logger.debug("[adicionarObservacion] Fin");
		}catch(Exception e){
			logger.debug("[adicionarObservacion] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			beanRequest = null;
		}

		return path;
	}
	
	/*
	 * Genera los primeros cobros del contrato
	 */
	/*public void generarPrimerosCobros(HttpServletRequest request){
		TblContratoPrimerCobro primerCobro 	= new TblContratoPrimerCobro();
		//Date datFechaFinContrato			= null;
		Date datFechaInicioContrato			= null;
		Double dblFactor					= null;
		BeanRequest beanRequest				= null;
		List<TblContratoPrimerCobro> lista	= new ArrayList<TblContratoPrimerCobro>();
		Date fechaFinMes					= null;
		try{
			logger.debug("[generarPrimerosCobros] Inicio");
			
			
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			fechaFinMes = UtilSGT.getDatetoString(UtilSGT.getLastDate(UtilSGT.getDateStringFormat(beanRequest.getContrato().getFechaInicio())));
			logger.debug("[generarPrimerosCobros] fechaFinMes:"+fechaFinMes);
			logger.debug("[generarPrimerosCobros] Alquiler 1:"+beanRequest.getContrato().getMontoAlquiler()+" Alquiler 2:"+beanRequest.getContratoAntiguo().getMontoAlquiler());
			if (beanRequest.getContrato().getMontoAlquiler().compareTo(beanRequest.getContratoAntiguo().getMontoAlquiler())!=0 ||
				!beanRequest.getContrato().getFechaInicio().toString().equals(beanRequest.getContratoAntiguo().getFechaInicio().toString())	
				){
				//Alquiler
				logger.debug("[generarPrimerosCobros] Cambio Monto Alquiler");
				primerCobro.setTblTipoServicio(new TblTipoServicio());
				primerCobro.getTblTipoServicio().setCodigoTipoServicio(Constantes.TIPO_SERVICIO_ALQUILER);
				//datFechaFinContrato = beanRequest.getContrato().getFechaFin();
				datFechaInicioContrato = beanRequest.getContrato().getFechaInicio();
				dblFactor = UtilSGT.obtenerFactorPrimerCobro(datFechaInicioContrato);
				primerCobro.setMontoCobro(new BigDecimal(dblFactor*beanRequest.getContrato().getMontoAlquiler().doubleValue()));
				primerCobro.setTipoMoneda(beanRequest.getContrato().getTipoMonedaAlquiler());
				primerCobro.setObservacion(Constantes.DESC_PRIMEROS_COBROS);
				primerCobro.setFechaCobro(fechaFinMes);
				
				lista.add(primerCobro);
			}
			//Servicios
			
			if(!igualesListaServicio(beanRequest.getListaServicio(), beanRequest.getContrato(),beanRequest)||
			   !beanRequest.getContrato().getFechaInicio().toString().equals(beanRequest.getContratoAntiguo().getFechaInicio().toString())
				){
				logger.debug("[generarPrimerosCobros] Cambio Servicio");
				if (beanRequest.getListaServicio() !=null && beanRequest.getListaServicio().size()>0){
					for(TblContratoServicio servicio:beanRequest.getListaServicio()){
						primerCobro 	= new TblContratoPrimerCobro();
						primerCobro.setTblTipoServicio(servicio.getTblTipoServicio());
						primerCobro.setTipoMoneda(servicio.getTipoMonedaServicio());
						primerCobro.setMontoCobro(new BigDecimal(dblFactor*servicio.getMonto().doubleValue()));
						primerCobro.setObservacion(Constantes.DESC_PRIMEROS_COBROS);
						primerCobro.setFechaCobro(fechaFinMes);
						lista.add(primerCobro);
					}
				}
				
			}
			beanRequest.setListaPrimerCobroNuevo(lista);
			logger.debug("[generarPrimerosCobros] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			primerCobro 			= null;
			datFechaInicioContrato	= null;
			dblFactor				= null;
			beanRequest				= null;
			lista					= null;
			fechaFinMes				= null;
		}
	}*/
	/*

	private boolean igualesListaServicio(List<TblContratoServicio> listaServicio, TblContrato contrato, BeanRequest beanRequest){
		boolean encontrado 				= false;
		TblContratoServicio servicio 	= null;
		TblContratoServicio servicioAnt	= null;
		List<TblContratoServicio> listaServicioAntiguo = null;
		try{
			logger.debug("[igualesListaServicio] Inicio");
			listaServicioAntiguo = contratoServicioDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			beanRequest.setListaServicioAntiguo(listaServicioAntiguo);
			if (listaServicio == null && listaServicioAntiguo==null){
				return true;
			}
			
			for(int i=0; listaServicio!=null && i< listaServicio.size(); i++){
				servicio = listaServicio.get(i);
				encontrado = false;
				for(int j=0; listaServicioAntiguo!=null && j < listaServicioAntiguo.size(); j++){
					servicioAnt = listaServicioAntiguo.get(j);
					if (servicio.getCodigoServicio() == servicioAnt.getCodigoServicio() ){
						encontrado = true;
						break;
					}
						
				}
				if (!encontrado){
					return false;
				}
			}
			if (encontrado){
				for(int i=0; listaServicioAntiguo!=null && i< listaServicioAntiguo.size(); i++){
					servicioAnt = listaServicioAntiguo.get(i);
					encontrado = false;
					for(int j=0; listaServicio!=null && j < listaServicio.size(); j++){
						servicio = listaServicio.get(j);
						if (servicio.getCodigoServicio() == servicioAnt.getCodigoServicio() ){
							encontrado = true;
							break;
						}
							
					}
					if (!encontrado){
						return false;
					}
				}
			}
			logger.debug("[igualesListaServicio] encontrado:"+encontrado);
			logger.debug("[igualesListaServicio] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			servicio 	= null;
			servicioAnt	= null;
		}
		return encontrado;
	}
	*/
	/*
	 * Paginado
	 */
	@RequestMapping(value = "/cambiocontratos/paginado/{page}/{size}/{operacion}", method = RequestMethod.GET)
	public String paginarEntidad(@PathVariable Integer page, @PathVariable Integer size, @PathVariable String operacion, Model model,  PageableSG pageable, HttpServletRequest request) {
		TblContrato filtro = null;
		String path = null;
		try{
			//LOGGER.debug("[traerRegistros] Inicio");
			path = "cliente/cambiocontrato/ccn_listado";
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
			filtro = (TblContrato)request.getSession().getAttribute("sessionFiltroCriterioCambio");
			model.addAttribute("filtro", filtro);
			if (filtro.getTblPersona() == null){
				filtro.setTblPersona(new TblPersona());
			}
			this.listarContratos(model, filtro, pageable, this.urlPaginado,request);
			
		}catch(Exception e){
			//LOGGER.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		return path;
	}
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cambiocontratos/regresar", method = RequestMethod.GET)
	public String regresar(Model model, String path, HttpServletRequest request) {
		TblContrato filtro = null;
		List<TblContrato> lista = null;
		PageWrapper<TblContrato> page = null;
		try{
			logger.debug("[regresar] Inicio");
			path = "cliente/cambiocontrato/ccn_listado";
			
			filtro = (TblContrato)request.getSession().getAttribute("sessionFiltroCriterioCambio");
			model.addAttribute("filtro", filtro);
			lista = (List<TblContrato>)request.getSession().getAttribute("sessionListaContratoCambio");
			model.addAttribute("registros",lista);
			page = (PageWrapper<TblContrato>) request.getSession().getAttribute("PageContratoCambio");
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
