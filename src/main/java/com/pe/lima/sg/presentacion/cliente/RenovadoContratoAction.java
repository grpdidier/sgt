package com.pe.lima.sg.presentacion.cliente;

import static com.pe.lima.sg.dao.caja.CxCBitacoraSpecifications.conAnio;
import static com.pe.lima.sg.dao.caja.CxCBitacoraSpecifications.conCodigoContrato;
import static com.pe.lima.sg.dao.caja.CxCBitacoraSpecifications.conMes;
import static com.pe.lima.sg.dao.caja.CxCBitacoraSpecifications.conTipoCobro;
import static com.pe.lima.sg.dao.caja.CxCBitacoraSpecifications.conTipoOperacion;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conEdificio;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conEstado;
//import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conEstadoContrato;
//import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conFechaFinContrato;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conMaterno;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conNombre;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conPaterno;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conRazonSocial;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conRuc;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conTienda;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import com.pe.lima.sg.dao.mantenimiento.IParametroDAO;
import com.pe.lima.sg.dao.mantenimiento.IPersonaDAO;
import com.pe.lima.sg.dao.mantenimiento.ITiendaDAO;
import com.pe.lima.sg.dao.mantenimiento.ITipoServicioDAO;
import com.pe.lima.sg.entity.caja.TblCxcBitacora;
import com.pe.lima.sg.entity.caja.TblCxcDocumento;
import com.pe.lima.sg.entity.cliente.TblArbitrio;
import com.pe.lima.sg.entity.cliente.TblContrato;
import com.pe.lima.sg.entity.cliente.TblContratoCliente;
import com.pe.lima.sg.entity.cliente.TblContratoPrimerCobro;
import com.pe.lima.sg.entity.cliente.TblContratoServicio;
import com.pe.lima.sg.entity.cliente.TblLuzxtienda;
import com.pe.lima.sg.entity.cliente.TblObservacion;
import com.pe.lima.sg.entity.mantenimiento.TblEdificio;
import com.pe.lima.sg.entity.mantenimiento.TblParametro;
import com.pe.lima.sg.entity.mantenimiento.TblPersona;
import com.pe.lima.sg.entity.mantenimiento.TblSerie;
import com.pe.lima.sg.entity.mantenimiento.TblTienda;
import com.pe.lima.sg.entity.mantenimiento.TblTipoServicio;
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
public class RenovadoContratoAction extends BaseOperacionPresentacion<TblContrato> {

	private static final Logger logger = LogManager.getLogger(RenovadoContratoAction.class);
	
	@Autowired
	private IContratoDAO contratoDao;

	@Autowired
	private ITiendaDAO tiendaDao;

	//@Autowired
	//private ISuministroDAO suministroDao;

	@Autowired
	private IParametroDAO parametroDao;

	@Autowired
	private ICxCDocumentoDAO documentoDao;

	@Autowired
	private IPersonaDAO personaDao;

	@Autowired
	private IArbitrioDAO arbitrioDao;

	@Autowired
	private ICxCBitacoraDAO cxcBitacoraDao;

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

	@SuppressWarnings("rawtypes")
	@Override
	public BaseOperacionDAO getDao() {
		return contratoDao;
	}

	private String urlPaginado = "/renovarcontratos/paginado/"; 
	private String urlPaginadoCliente = "/renovarcontratos/cliente/paginado/";
	/**
	 * Se encarga de listar todos los contratos
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/renovarcontratos", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path,  PageableSG pageable, HttpServletRequest request) {
		TblContrato filtro = null;
		try{
			logger.debug("[traerRegistros] Inicio");
			path = "cliente/renovacion/rnv_listado";
			filtro = new TblContrato();
			filtro.setTblPersona(new TblPersona());
			filtro.setTblTienda(new TblTienda());
			filtro.getTblTienda().setTblEdificio(new TblEdificio());
			filtro.setFechaFin(UtilSGT.addDays(new Date(), 30));
			model.addAttribute("filtro", filtro);
			filtro.setFechaFin(null);
			listaUtil.cargarDatosModel(model, Constantes.MAP_LISTA_EDIFICIO);
			//this.listarContratos(model, filtro);
			request.getSession().setAttribute("sessionFiltroCriterioRenovacion", filtro);
			request.getSession().setAttribute("sessionListaContratoRenovacion", null);
			request.getSession().setAttribute("PageContratoRenovacion", null);
			
			request.getSession().setAttribute("origenLLamada", null);
			request.getSession().setAttribute("sessionFiltroCriterioRemoto",null);
			request.getSession().setAttribute("sessionListaContratoRemoto",null);
			request.getSession().setAttribute("PageContratoRemoto",null);
			
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
	@RequestMapping(value = "/renovarcontratos/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, TblContrato filtro, String path,  PageableSG pageable, HttpServletRequest request) {
		//Map<String, Object> campos = null;
		path = "cliente/renovacion/rnv_listado";
		try{
			logger.debug("[traerRegistrosFiltrados] Inicio");
			this.listarContratos(model, filtro, pageable, this.urlPaginado, request);
			model.addAttribute("filtro", filtro);
			//listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_CONCEPTO);
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
		List<TblContrato> entidades = new ArrayList<TblContrato>();
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "numero"));
		try{
			Specification<TblContrato> filtro = Specifications.where(conNombre(tblContrato.getTblPersona().getNombre()))
					.and(conPaterno(tblContrato.getTblPersona().getPaterno()))
					.and(conMaterno(tblContrato.getTblPersona().getMaterno()))
					.and(conEdificio(tblContrato.getTblTienda().getTblEdificio().getCodigoEdificio()))
					.and(conTienda(tblContrato.getTblTienda().getNumero()))
					.and(conRuc(tblContrato.getTblPersona().getNumeroRuc()))
					.and(conRazonSocial(tblContrato.getTblPersona().getRazonSocial()))
					//.and(conEstadoContrato(Constantes.ESTADO_CONTRATO_VIGENTE)).or(conEstadoContrato(Constantes.ESTADO_CONTRATO_RENOVADO))
					.and(conEstado(Constantes.ESTADO_REGISTRO_ACTIVO));

			/*.and(conPaterno(tblContrato.getTblPersona().getPaterno()))
					.and(conMaterno(tblContrato.getTblPersona().getMaterno()))
					.and(conEdificio(tblContrato.getTblTienda().getTblEdificio().getCodigoEdificio()))
					.and(conTienda(tblContrato.getTblTienda().getNumero()))
					.and(conRuc(tblContrato.getTblPersona().getNumeroRuc()))
					.and(conRazonSocial(tblContrato.getTblPersona().getRazonSocial()))
					.and(conEstadoContrato(Constantes.ESTADO_CONTRATO_VIGENTE)).or(conEstadoContrato(Constantes.ESTADO_CONTRATO_RENOVADO))
					.and(conFechaFinContrato(tblContrato.getFechaFin()))
					.and(conEstado(Constantes.ESTADO_REGISTRO_ACTIVO));*/
			//entidades = contratoDao.findAll(filtro);
			pageable.setSort(sort);
			Page<TblContrato> entidadPage = contratoDao.findAll(filtro, pageable);
			PageWrapper<TblContrato> page = new PageWrapper<TblContrato>(entidadPage, url, pageable);
			model.addAttribute("registros", page.getContent());
			model.addAttribute("page", page);
			request.getSession().setAttribute("sessionFiltroCriterioRenovacion", tblContrato);
			request.getSession().setAttribute("sessionListaContratoRenovacion", page.getContent());
			request.getSession().setAttribute("PageContratoRenovacion", page);
			logger.debug("[listarContratos] entidades:"+entidades);
			//model.addAttribute("registros", entidades);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades = null;
		}
	}

	/*** Listado de Contratos ***/
	/*private void listarContratosXCodigo(Model model, TblContrato tblContrato){
		List<TblContrato> entidades = new ArrayList<TblContrato>();
		try{
			entidades = contratoDao.listAllContratoActivosxCodigoContrato(tblContrato.getCodigoContrato());
			logger.debug("[listarContratosXCodigo] entidades:"+entidades);
			model.addAttribute("registros", entidades);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades = null;
		}
	}*/

	private TblContrato inicializaDataContratoRenovacion(TblContrato contrato){
		List<TblParametro> listaParametro; 
		try{
			listaParametro = parametroDao.buscarOneByNombre(Constantes.PAR_FIN_CONTRATO);
			Date fechaFinAnterior = contrato.getFechaFin();
			contrato.setFechaInicio(contrato.getFechaFin());
			UtilSGT.obtenerFechaFinContrato(contrato, listaParametro);
			contrato.setFechaInicio(UtilSGT.addDays(fechaFinAnterior, Constantes.PAR_ADD_1_DIA));
		}catch(Exception e){
			e.printStackTrace();
		}

		return contrato;
	}
	/**
	 * Se encarga de direccionar a la pantalla de edicion del Contrato
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "renovarcontrato/editar/{id}", method = RequestMethod.GET)
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
		String path 							= "";
		try{
			request.getSession().setAttribute("origenLLamada", "");//Se adiciona por la llamada desde el modulo de contrato, es un flag de control
			contrato = contratoDao.findOne(id);
			this.inicializaDataContratoRenovacion(contrato);
			model.addAttribute("contrato", contrato);
			listaServicio = contratoServicioDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			listaCliente = contratoClienteDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			listaCobro = contratoPrimerCobroDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			listaArbitrio = arbitrioDao.listarAllActivosxContrato(contrato.getCodigoContrato());
			listaLuzxtienda = luzxTiendaDao.listarLuzTiendaxContrato(contrato.getCodigoContrato());
			listaObservacion = observacionDao.listarObservacionxContrato(contrato.getCodigoContrato());
			//Busqueda de los contratos Antiguos
			listaContratoAnt = new ArrayList<TblContrato>();
			getContratosAntiguos2(contrato,listaContratoAnt);

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

			this.cargarListasRequestBeanContrato(model, beanRequest);

			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);
			path = "cliente/renovacion/rnv_edicion";


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
	
	/**
	 * Se encarga de direccionar a la pantalla de edicion del Contrato: Se crea el metodo para ser invocado desde el modulo de contrato
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "renovarcontrato/editar/contrato/{id}", method = RequestMethod.GET)
	public String editarContratoCallMenu(@PathVariable Integer id, Model model, TblContrato filtro, HttpServletRequest request) {
		TblContrato contrato 					= null;
		BeanRequest beanRequest					= null;
		List<TblContratoServicio> listaServicio	= null;
		List<TblContratoCliente> listaCliente	= null;
		List<TblContratoPrimerCobro> listaCobro	= null;
		List<TblArbitrio> listaArbitrio			= null;
		List<TblLuzxtienda> listaLuzxtienda		= null;
		List<TblContrato> listaContratoAnt		= null;
		List<TblObservacion> listaObservacion	= null;
		String path 							= "";
		try{
			request.getSession().setAttribute("origenLLamada", "MODULO_CONTRATO");
			request.getSession().setAttribute("sessionFiltroCriterioRemoto",request.getSession().getAttribute("sessionFiltroCriterio"));
			request.getSession().setAttribute("sessionListaContratoRemoto",request.getSession().getAttribute("sessionListaContrato"));
			request.getSession().setAttribute("PageContratoRemoto",request.getSession().getAttribute("PageContrato"));
			
			contrato = contratoDao.findOne(id);
			this.inicializaDataContratoRenovacion(contrato);
			model.addAttribute("contrato", contrato);
			listaServicio = contratoServicioDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			listaCliente = contratoClienteDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			listaCobro = contratoPrimerCobroDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			listaArbitrio = arbitrioDao.listarAllActivosxContrato(contrato.getCodigoContrato());
			listaLuzxtienda = luzxTiendaDao.listarLuzTiendaxContrato(contrato.getCodigoContrato());
			listaObservacion = observacionDao.listarObservacionxContrato(contrato.getCodigoContrato());
			//Busqueda de los contratos Antiguos
			listaContratoAnt = new ArrayList<TblContrato>();
			getContratosAntiguos2(contrato,listaContratoAnt);

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

			this.cargarListasRequestBeanContrato(model, beanRequest);

			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);
			path = "cliente/renovacion/rnv_edicion";


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

	private void inicializaDataDefaultServicio(TblContratoServicio  contratoServicio){

		contratoServicio.getTblTipoServicio().setCodigoTipoServicio(Constantes.CODIGO_SERVICIO_MANTENIMIENTO);
		contratoServicio.setTipoMonedaServicio(Constantes.MONEDA_SOL);
		contratoServicio.setTipoDocumentoServicio(Constantes.TIPO_DOC_INTERNO);
	}

	@Override
	public TblContrato getNuevaEntidad() {

		return null;
	}


	/** Validamos logica del negocio**/
	@Override
	public boolean validarNegocio(Model model,TblContrato entidad, HttpServletRequest request){
		boolean exitoso = true;
		TblContrato contrato = null;
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
			contrato = contratoDao.findOne(entidad.getCodigoContrato());
			
			if (entidad.getFechaInicio().before(contrato.getFechaFin())){
				exitoso = false;
				model.addAttribute("resultadoContrato", "La fecha de Inicio de la Renovación no puede ser anterior a la fecha fin del contrato anterior");
				return exitoso;
			}
			if (entidad.getFechaFin().before(contrato.getFechaFin())){
				exitoso = false;
				model.addAttribute("resultadoContrato", "La fecha Fin de la Renovación no puede ser anterior a la fecha fin del contrato anterior");
				return exitoso;
			}
			if (entidad.getMontoAlquiler() == null || entidad.getMontoAlquiler().compareTo(new BigDecimal(0))< 0){
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
	@RequestMapping(value = "/renovarcontrato/locales", method = RequestMethod.POST)
	public String listarLocales(Model model, TblContrato contrato, String path, HttpServletRequest request) {
		path 						= "cliente/renovacion/rnv_tie_listado";
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
				return "cliente/renovacion/rnv_nuevo"; 
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
	@RequestMapping(value = "/renovarcontrato/locales/q", method = RequestMethod.POST)
	public String listarLocalesFiltrados(Model model, Filtro filtro, String path, HttpServletRequest request) {
		Map<String, Object> campos = null;
		TblEdificio edificio = null;
		path = "cliente/renovacion/rnv_tie_listado";
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
	@RequestMapping(value = "/renovarcontrato/locales/seleccionar", method = RequestMethod.POST)
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
		return "cliente/renovacion/rnv_nuevo";
	}
	/**
	 * Se encarga de direccionar a la pantalla de contrato
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/renovarcontrato/locales/seleccionar/{id}", method = RequestMethod.GET)
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
		return "cliente/renovacion/rnv_nuevo";
	}
	/**
	 * Se encarga de Listar los clientes
	 * seleccionado
	 * 
	 * @param model
	 * @param contratoBean
	 * @return
	 */
	@RequestMapping(value = "/renovarcontrato/clientes", method = RequestMethod.POST)
	public String listarClientes(Model model, TblContrato contrato, String path, HttpServletRequest request) {
		path 						= "cliente/renovacion/rnv_per_listado";
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
			logger.debug("[traerRegistrosFiltrados] Error: "+e.getMessage());
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
	@RequestMapping(value = "/renovarcontrato/clientes/q", method = RequestMethod.POST)
	public String listarPersonasFiltrados(Model model, Filtro filtro, String path, PageableSG pageable, HttpServletRequest request) {
		path = "cliente/renovacion/rnv_tie_listado";
		try{
			logger.debug("[listarPersonasFiltrados] Inicio");
			logger.debug("[listarPersonasFiltrados] Operacion:"+filtro.getStrOperacion());
			path = "cliente/renovacion/rnv_per_listado";
			logger.debug("[listarPersonasFiltrados] Operacion:"+filtro.getStrOperacion());
			this.listarPersonas(model, filtro, pageable, this.urlPaginadoCliente);
			this.cargarListaOperacionCliente(model);
			model.addAttribute("filtro", filtro);
			request.getSession().setAttribute("sessionFiltroClienteRenovacionCriterio", filtro);
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
	@RequestMapping(value = "/renovarcontratos/cliente/paginado/{page}/{size}/{operacion}", method = RequestMethod.GET)
	public String paginarEntidadCliente(@PathVariable Integer page, @PathVariable Integer size, @PathVariable String operacion, Model model,  PageableSG pageable, HttpServletRequest request) {
		Filtro filtro = null;
		Map<String, Object> campos = null;
		String path = null;
		try{
			//LOGGER.debug("[traerRegistros] Inicio");
			path = "cliente/renovacion/rnv_per_listado";
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
			filtro = (Filtro)request.getSession().getAttribute("sessionFiltroClienteRenovacionCriterio");
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
	@RequestMapping(value = "/renovarcontrato/servicio/eliminar/{id}", method = RequestMethod.GET)
	public String eliminarServicioGet(@PathVariable String id, Model model, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		try{

			//Se mantiene en Session el contrato hasta retornar
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			if (removerExistenciaServicio(beanRequest.getListaServicio(),id)){
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
		return "cliente/renovacion/rnv_edicion";
	}
	private boolean removerExistenciaServicio(List<TblContratoServicio> lista, String nombre){
		boolean resultado = false;
		for(int i=0; i<lista.size(); i++){
			TblContratoServicio elemento = lista.get(i);
			if (elemento.getTblTipoServicio().getNombre().equals(nombre)){
				resultado = true;
				lista.remove(i);
				break;
			}
		}
		return resultado;
	}
	/**
	 * Se encarga de direccionar a la pantalla de contrato
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/renovarcontrato/clientes/seleccionar", method = RequestMethod.POST)
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
		return "cliente/renovacion/rnv_nuevo";
	}
	/**
	 * Se encarga de direccionar a la pantalla de contrato
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/renovarcontrato/clientes/seleccionar/{id}", method = RequestMethod.GET)
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

			path = "cliente/renovacion/rnv_edicion";

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
	@RequestMapping(value = "/renovarcontrato/regresar", method = RequestMethod.POST)
	public String regresarContrato(Model model, Filtro filtro, String path, HttpServletRequest request) {
		//TblContrato contrato				= null;
		BeanRequest beanRequest				= null;
		
		try{
			logger.debug("[regresarContrato] Inicio");
			
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			logger.debug("[regresarAprobacion] oPERACION:"+filtro.getStrOperacion());

			path = "cliente/renovacion/rnv_edicion";
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

	@RequestMapping(value = "/renovarcontrato/servicio", method = RequestMethod.POST)
	public String adicionarServicio(Model model, TblContratoServicio contratoServicio, TblContrato contrato, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;

		try{
			logger.debug("[adicionarServicio] Inicio");
			path = "cliente/renovacion/rnv_edicion";
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
				beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
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
	/*private boolean removerExistenciaServicio(List<TblContratoServicio> lista, String nombre){
		boolean resultado = false;
		for(int i=0; i<lista.size(); i++){
			TblContratoServicio elemento = lista.get(i);
			if (elemento.getTblTipoServicio().getNombre().equals(nombre)){
				resultado = true;
				lista.remove(i);
				break;
			}
		}
		return resultado;
	}*/

	/**
	 * Se encarga de la eliminacion del servicio
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/renovarcontrato/eliminar/servicio", method = RequestMethod.POST)
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
		return  "cliente/renovacion/rnv_edicion";
	}

	public void mAsignarDatosContratoNuevo(TblContrato contratoAntiguo, TblContrato contratoNuevo, HttpServletRequest request){
		Integer intTotalRegistros			= null;
		String strNumeroContrato			= null;

		contratoNuevo.setFechaContrato(contratoAntiguo.getFechaContrato());
		contratoNuevo.setFechaInicio(contratoAntiguo.getFechaInicio());
		contratoNuevo.setFechaFin(contratoAntiguo.getFechaFin());
		contratoNuevo.setMontoAlquiler(contratoAntiguo.getMontoAlquiler());
		contratoNuevo.setTipoMonedaAlquiler(contratoAntiguo.getTipoMonedaAlquiler());
		contratoNuevo.setTipoCobro(contratoAntiguo.getTipoCobro());
		contratoNuevo.setTipoGarantia(contratoAntiguo.getTipoGarantia());
		contratoNuevo.setMontoGarantia(contratoAntiguo.getMontoGarantia());
		contratoNuevo.setTipoMonedaGarantia(contratoAntiguo.getTipoMonedaGarantia());
		contratoNuevo.setTipoDocumentoGarantia(contratoAntiguo.getTipoDocumentoGarantia());
		contratoNuevo.setInformacionAdicional(contratoAntiguo.getInformacionAdicional());
		contratoNuevo.setPeriodoAdelanto(contratoAntiguo.getPeriodoAdelanto());
		contratoNuevo.setTipoDocumentoAlquiler(contratoAntiguo.getTipoDocumentoAlquiler());
		//contratoNuevo.setEstadoContrato(Constantes.ESTADO_CONTRATO_PENDIENTE);
		//contratoNuevo.setEstadoContrato(Constantes.ESTADO_CONTRATO_RENOVADO);
		contratoNuevo.setEstadoContrato(Constantes.ESTADO_CONTRATO_VIGENTE);

		//Numero de contrato
		intTotalRegistros = contratoDao.countContrato();
		intTotalRegistros = intTotalRegistros==null?1:intTotalRegistros+1;
		strNumeroContrato = Constantes.PREFIJO_CONTRATO + UtilSGT.completarCeros(intTotalRegistros.toString(), 6);
		contratoNuevo.setNumero(strNumeroContrato);
		//Campos de auditoria
		contratoNuevo.setFechaCreacion(new Date(System.currentTimeMillis()));
		contratoNuevo.setIpCreacion(request.getRemoteAddr());
		contratoNuevo.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
		contratoNuevo.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
		//datos del cliente
		contratoNuevo.setTblPersona(contratoAntiguo.getTblPersona());
		//datos de la tienda
		contratoNuevo.setTblTienda(contratoAntiguo.getTblTienda());

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
	private boolean generarDocumentoServicio(Model model, TblContrato entidad, TblContratoServicio servicio, HttpServletRequest request){
		TblSerie serieFactura					= null;
		TblSerie serieBoleta					= null;
		TblSerie serieInterno					= null;
		List<PeriodoBean> lista					= null;
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
			/*serieFactura = serieDao.buscarOneByTipoComprobante(Constantes.TIPO_COMPROBANTE_FACTURA);
			serieBoleta = serieDao.buscarOneByTipoComprobante(Constantes.TIPO_COMPROBANTE_BOLETA);
			serieInterno = serieDao.buscarOneByTipoComprobante(Constantes.TIPO_COMPROBANTE_INTERNO);*/

			//Lista de los meses a cobrar
			lista = UtilSGT.mGetListAnioMesRefinanciacion(entidad.getFechaInicio(), entidad.getFechaFin());
			if (lista !=null && lista.size()>0){
				//cxcBitacoraAction = new CxcBitacoraAction();
				bitacoraBean = new BitacoraBean();
				bitacoraBean.setAnio(UtilSGT.getAnioDate(entidad.getFechaInicio()));
				bitacoraBean.setMes(UtilSGT.getMesDate(entidad.getFechaInicio()).toString());

				//Registramos la Bitacora del CxC del servicio
				bitacoraBean.setCodigoContrato(entidad.getCodigoContrato());
				boolean exitoso =  this.registrarBitacora(model, bitacoraBean, Constantes.TIPO_COBRO_SERVICIO, Constantes.SERIE_TIPO_OPERACION_CONTRATO, request);
				if (exitoso) {
					criterio = Specifications.where(conAnio(bitacoraBean.getAnio()))
							.and(conMes(new Integer(bitacoraBean.getMes())))
							.and(conTipoCobro(Constantes.TIPO_COBRO_SERVICIO))
							.and(conCodigoContrato(entidad.getCodigoContrato()))
							.and((conTipoOperacion(Constantes.SERIE_TIPO_OPERACION_CONTRATO)));
					bitacora = cxcBitacoraDao.findOne(criterio);
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
			criterio			= null;
			bitacora			= null;
			//documento			= null;
			//cabecera			= null;
			//detalle				= null;
			//strMensaje			= null;
		}
		return resultado;
	}

	public TblContratoServicio obtenerServicioNuevo(TblContratoServicio servicio, HttpServletRequest request){
		TblContratoServicio servicioNuevo = new TblContratoServicio();
		servicioNuevo.setMonto(servicio.getMonto());
		servicioNuevo.setPrimerCobroServicio(servicio.getPrimerCobroServicio());
		servicioNuevo.setTblContrato(servicio.getTblContrato());
		servicioNuevo.setTblTipoServicio(servicio.getTblTipoServicio());
		servicioNuevo.setTipoDocumentoServicio(servicio.getTipoDocumentoServicio());
		servicioNuevo.setTipoMonedaServicio(servicio.getTipoMonedaServicio());
		servicioNuevo.setFechaCreacion(new Date(System.currentTimeMillis()));
		servicioNuevo.setIpCreacion(request.getRemoteAddr());
		servicioNuevo.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
		servicioNuevo.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
		return servicioNuevo;
	}
	/**
	 * Se encarga de guardar la informacion 
	 * 
	 * @param 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/renovarcontrato/editar/guardar", method = RequestMethod.POST)
	public String actualizarEntidad(Model model, TblContrato entidad, HttpServletRequest request, String path,  PageableSG pageable) {
		path 											= "cliente/renovacion/rnv_listado";
		BeanRequest beanRequest							= null;
		TblContrato contrato							= null;
		TblContrato filtro								= null;
		TblContrato auxContrato							= null;
		List<TblContratoServicio> listaServicio 		= null;
		List<TblObservacion> listaObservacion			= null;
		TblContratoServicio servicioNuevo 				= null;
		try{
			logger.debug("[actualizarEntidad] Inicio" );
			logger.debug("[actualizarEntidad] Pre Guardar..." );
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			beanRequest.setContrato(entidad);
			if (this.validarNegocio(model, entidad, request)){
				//contrato = contratoDao.findOne(entidad.getCodigoContrato());
				contrato = new TblContrato();
				contrato.setCodigoPadreContrato(entidad.getCodigoContrato());
				this.mAsignarDatosContratoNuevo(entidad, contrato, request);
				contratoDao.save(contrato);
				contrato = contratoDao.findByNumeroContrato(contrato.getNumero());
				model.addAttribute("respuesta", "El contrato ["+entidad.getNumero()+"] fue renovado por ["+contrato.getNumero()+"]  - Tienda "+contrato.getTblTienda().getNumero()+" satisfactoriamente.");
				logger.debug("[actualizarEntidad] Guardado..." );


				//registro de las observaciones
				listaObservacion = beanRequest.getListaObservacion();
				if (listaObservacion!=null && listaObservacion.size()>0){
					logger.debug("[guardarEntidad] listaObservacion:"+listaObservacion.size());
					for(TblObservacion observacion: listaObservacion){
						observacion.setCodigoContrato(contrato.getCodigoContrato());
						observacionDao.save(observacion);
					}
				}

				//registrando los servicios
				logger.debug("[guardarEntidad] listaServicio:"+beanRequest.getListaServicio().size());
				listaServicio = beanRequest.getListaServicio();
				if (listaServicio!=null && listaServicio.size()>0){
					logger.debug("[guardarEntidad] listaServicio:"+listaServicio.size());
					for(TblContratoServicio servicio: listaServicio){
						servicioNuevo = this.obtenerServicioNuevo(servicio, request);
						servicioNuevo.setTblContrato(contrato);
						contratoServicioDao.save(servicioNuevo);
					}
				}
				//Finalizamos el contrato antiguo
				auxContrato = contratoDao.findByNumeroContrato(entidad.getNumero());
				auxContrato.setFechaModificacion(new Date(System.currentTimeMillis()));
				auxContrato.setIpModificacion(request.getRemoteAddr());
				auxContrato.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
				//auxContrato.setEstadoContrato(Constantes.ESTADO_CONTRATO_FINALIZADO);
				auxContrato.setEstadoContrato(Constantes.ESTADO_CONTRATO_RENOVADO);

				contratoDao.save(auxContrato);
				
				//2019.09.08 : Registramos los saldos del contrato anterior
				this.registrarCobrosPendientes(contrato, entidad.getCodigoContrato());
				
				//Generar los alquileres
				this.generarDocumentoAlquiler(model, contrato, request);
				//Generar los servicios
				if (listaServicio!=null && listaServicio.size()>0){
					for(TblContratoServicio servicio: listaServicio){
						this.generarDocumentoServicio(model, contrato, servicio, request);
					}
				}
				//Carga la lista de contratos y retorna a la pantalla que lo llamó
				if (request.getSession().getAttribute("origenLLamada") !=null && request.getSession().getAttribute("origenLLamada").equals("MODULO_CONTRATO")){
					
					path = "cliente/contrato/con_listado";
					
					TblContrato filtroContrato = (TblContrato)request.getSession().getAttribute("sessionFiltroCriterioRemoto");
					model.addAttribute("filtro", filtroContrato);
					@SuppressWarnings("unchecked")
					List<TblContrato> listaContrato = (List<TblContrato>)request.getSession().getAttribute("sessionListaContratoRemoto");
					model.addAttribute("registros",listaContrato);
					@SuppressWarnings("unchecked")
					PageWrapper<TblContrato>pageContrato = (PageWrapper<TblContrato>) request.getSession().getAttribute("PageContratoRemoto");
					model.addAttribute("page", pageContrato);
					PageableSG pageableContrato = (PageableSG)request.getSession().getAttribute("PageableSGContrato");
					this.listarContratos(model, filtroContrato, pageableContrato, "/contratos/paginado/", request);
					request.getSession().setAttribute("sessionListaContratoRemoto", request.getSession().getAttribute("sessionListaContratoRenovacion"));
				}else{
					filtro = new TblContrato();
					filtro.setTblPersona(new TblPersona());
					filtro.setTblTienda(new TblTienda());
					filtro.getTblTienda().setTblEdificio(new TblEdificio());
					model.addAttribute("filtro", filtro);
					listaUtil.cargarDatosModel(model, Constantes.MAP_LISTA_EDIFICIO);
					//this.listarContratosXCodigo(model, contrato);
					this.traerRegistrosFiltrados(model, filtro, this.urlPaginado, pageable, request);
				}
			}else{
				path = "cliente/renovacion/rnv_edicion";
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
			filtro			= null;
		}
		return path;

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
	public boolean registrarBitacora(Model model, BitacoraBean entidad, String tipoCobro, String tipoOperacion, HttpServletRequest request){
		boolean resultado 			= false;
		TblCxcBitacora bitacora		= null;
		try{
			bitacora = new TblCxcBitacora();
			this.preGuardarBitacora(bitacora, request);
			bitacora.setAnio(entidad.getAnio());
			bitacora.setMes(new Integer(entidad.getMes()));
			bitacora.setTipoOperacion(tipoOperacion);
			bitacora.setTipoCobro(tipoCobro);
			bitacora.setCodigoContrato(entidad.getCodigoContrato());
			cxcBitacoraDao.save(bitacora);
			resultado = true;
		}catch(Exception e){
			resultado = false;
			e.printStackTrace();
		}finally{
			bitacora	= null;
		}
		return resultado;
	}
	/*
	 * Registro del alquiler
	 */
	public TblCxcDocumento registrarCxCDocumentoAlquiler(Model model, BitacoraBean entidad, HttpServletRequest request, TblContrato contrato, PeriodoBean periodo,TblCxcBitacora bitacora,TblSerie serieFactura,TblSerie serieBoleta,TblSerie serieInterno){
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
			documento.setNombre(Constantes.DESC_CUOTA_ALQUILER);
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
			documento = documentoDao.findByAnioMesCodigoReferencia(documento.getTipoReferencia(), documento.getCodigoReferencia(), documento.getAnio(), documento.getMes());
		}catch(Exception e){
			e.printStackTrace();
		}
		return documento;
	}

	private boolean generarDocumentoAlquiler(Model model, TblContrato entidad, HttpServletRequest request){
		TblSerie serieFactura					= null;
		TblSerie serieBoleta					= null;
		TblSerie serieInterno					= null;
		List<PeriodoBean> lista					= null;
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
			/*serieFactura = serieDao.buscarOneByTipoComprobante(Constantes.TIPO_COMPROBANTE_FACTURA);
			serieBoleta = serieDao.buscarOneByTipoComprobante(Constantes.TIPO_COMPROBANTE_BOLETA);
			serieInterno = serieDao.buscarOneByTipoComprobante(Constantes.TIPO_COMPROBANTE_INTERNO);*/

			//Lista de los meses a cobrar
			lista = UtilSGT.mGetListAnioMesRefinanciacion(entidad.getFechaInicio(), entidad.getFechaFin());
			if (lista !=null && lista.size()>0){
				//cxcBitacoraAction = new CxcBitacoraAction();
				bitacoraBean = new BitacoraBean();
				bitacoraBean.setAnio(UtilSGT.getAnioDate(entidad.getFechaInicio()));
				bitacoraBean.setMes(UtilSGT.getMesDate(entidad.getFechaInicio()).toString());

				//Registramos la Bitacora del CxC del alquiler
				bitacoraBean.setCodigoContrato(entidad.getCodigoContrato());
				boolean exitoso =  this.registrarBitacora(model, bitacoraBean, Constantes.TIPO_COBRO_ALQUILER, Constantes.SERIE_TIPO_OPERACION_CONTRATO, request);
				if (exitoso) {
					criterio = Specifications.where(conAnio(bitacoraBean.getAnio()))
							.and(conMes(new Integer(bitacoraBean.getMes())))
							.and(conTipoCobro(Constantes.TIPO_COBRO_ALQUILER))
							.and(conCodigoContrato(entidad.getCodigoContrato()))
							.and((conTipoOperacion(Constantes.SERIE_TIPO_OPERACION_CONTRATO)));
					bitacora = cxcBitacoraDao.findOne(criterio);
					for(PeriodoBean periodo: lista){
						documento = this.registrarCxCDocumentoAlquiler(model, bitacoraBean, request, entidad, periodo, bitacora, serieFactura, serieBoleta, serieInterno);
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
			criterio			= null;
			bitacora			= null;
			//documento			= null;
			//cabecera			= null;
			//detalle				= null;
			//strMensaje			= null;
		}
		return resultado;
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
			beanRequest 	= null;
			listaObs		= null;
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
		model.addAttribute("observacion", beanRequest.getObservacion());
		model.addAttribute("listaObservacion", beanRequest.getListaObservacion());
		model.addAttribute("listaContratoAnterior", beanRequest.getListaContrato());
	}

	/*Busqueda de contratos antiguos*/
	/*
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

	}*/
	private boolean getContratosAntiguos2(TblContrato contrato, List<TblContrato> listaContrato){
		TblContrato contratoAnt			= null;
		Integer codigoContrato			= null;
		try{
			if(contrato.getCodigoPadreContrato()!=null && contrato.getCodigoPadreContrato() > 0){
				codigoContrato = contrato.getCodigoPadreContrato();
				contratoAnt = contratoDao.findOne(codigoContrato);
				if (contratoAnt !=null){
					listaContrato.add(contratoAnt);
					//verificacion de mas contratos
					if (contratoAnt.getCodigoPadreContrato()!=null && contratoAnt.getCodigoPadreContrato() > 0){
						this.getContratosAntiguos2(contratoAnt, listaContrato);
					}else{
						return true;
					}
				}else{
					return true;
				}

			}else{
				return true;
			}
		}catch(Exception e){

		}
		return true;

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
	@RequestMapping(value = "/renovarcontrato/cliente/historial", method = RequestMethod.POST)
	public String listarClientesHistorial(Model model, TblContrato contrato, String path, HttpServletRequest request) {
		path 						= "cliente/renovacion/rnv_per_historial";
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
	@RequestMapping(value = "/renovarcontrato/observacion/edicion", method = RequestMethod.POST)
	public String adicionarObservacion(Model model, TblObservacion observacionBean, TblContrato contrato, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		try{
			logger.debug("[adicionarObservacion] Inicio:");
			path = "cliente/renovacion/rnv_edicion";
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
				beanRequest.setContrato(contrato);
				this.cargarListasRequestBeanContrato(model, beanRequest);
				this.cargarListaOperacionContrato(model);
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
	public void generarPrimerosCobros(HttpServletRequest request){
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
	}


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
	/*
	 * Paginado
	 */
	@RequestMapping(value = "/renovarcontratos/paginado/{page}/{size}/{operacion}", method = RequestMethod.GET)
	public String paginarEntidad(@PathVariable Integer page, @PathVariable Integer size, @PathVariable String operacion, Model model,  PageableSG pageable, HttpServletRequest request) {
		TblContrato filtro = null;
		String path = null;
		try{
			//LOGGER.debug("[traerRegistros] Inicio");
			path = "cliente/renovacion/rnv_listado";
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
			filtro = (TblContrato)request.getSession().getAttribute("sessionFiltroCriterioRenovacion");
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
	@RequestMapping(value = "/renovarcontratos/regresar", method = RequestMethod.GET)
	public String regresar(Model model, String path, HttpServletRequest request) {
		TblContrato filtro = null;
		List<TblContrato> lista = null;
		PageWrapper<TblContrato> page = null;
		try{
			logger.debug("[regresar] Inicio");
			if (request.getSession().getAttribute("origenLLamada") !=null && request.getSession().getAttribute("origenLLamada").equals("MODULO_CONTRATO")){
				
				path = "cliente/contrato/con_listado";
				
				TblContrato filtroContrato = (TblContrato)request.getSession().getAttribute("sessionFiltroCriterioRemoto");
				model.addAttribute("filtro", filtroContrato);
				List<TblContrato> listaContrato = (List<TblContrato>)request.getSession().getAttribute("sessionListaContratoRemoto");
				model.addAttribute("registros",listaContrato);
				PageWrapper<TblContrato>pageContrato = (PageWrapper<TblContrato>) request.getSession().getAttribute("PageContratoRemoto");
				model.addAttribute("page", pageContrato);
				
			}else{
				path = "cliente/renovacion/rnv_listado";
	
				filtro = (TblContrato)request.getSession().getAttribute("sessionFiltroCriterioRenovacion");
				model.addAttribute("filtro", filtro);
				lista = (List<TblContrato>)request.getSession().getAttribute("sessionListaContratoRenovacion");
				model.addAttribute("registros",lista);
				page = (PageWrapper<TblContrato>) request.getSession().getAttribute("PageContratoRenovacion");
				model.addAttribute("page", page);

			}
			logger.debug("[regresar] Fin");
		}catch(Exception e){
			logger.debug("[regresar] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}

		return path;
	}

	//Obtiene los cobros de alquiler del contrato que tienen saldo > 0
	private List<TblCxcDocumento> obtenerAlquilerContrato(Integer codigoContrato){
		List<TblCxcDocumento> listaAlquiler = null;
		listaAlquiler = documentoDao.listarCxCxContrato(codigoContrato, Constantes.TIPO_COBRO_ALQUILER);
		return listaAlquiler;
	}
	//Obtiene los cobros de servicio del contrato que tienen saldo > 0
	private List<TblCxcDocumento> obtenerServicioContrato(Integer codigoContrato){
		List<TblCxcDocumento> listaServicio = null;
		listaServicio = documentoDao.listarCxCxContrato(codigoContrato, Constantes.TIPO_COBRO_SERVICIO);
		return listaServicio;
	}
	//Obtiene los cobros de luz  del contrato que tienen saldo > 0
	private List<TblCxcDocumento> obtenerLuzContrato(Integer codigoContrato){
		List<TblCxcDocumento> listaLuz = null;
		listaLuz = documentoDao.listarCxCxContrato(codigoContrato, Constantes.TIPO_COBRO_LUZ);
		return listaLuz;
	}
	//Registra las cxc: Alquiler, servicios y luz del nuevo contrato : renovado
	private void registrarCxCContrato(TblCxcDocumento documento, TblContrato contrato){
		TblCxcDocumento nuevoDocumento = new TblCxcDocumento();
		
		nuevoDocumento.setAnio(documento.getAnio());
		nuevoDocumento.setCodigoComprobante(documento.getCodigoComprobante());
		nuevoDocumento.setCodigoContrato(contrato.getCodigoContrato()); //este es el nuevo dato
		nuevoDocumento.setCodigoReferencia(documento.getCodigoReferencia());
		nuevoDocumento.setEstado(documento.getEstado());
		nuevoDocumento.setFechaCreacion(new Date());
		nuevoDocumento.setFechaFin(documento.getFechaFin());
		nuevoDocumento.setFechaFinNombre(documento.getFechaFinNombre());
		nuevoDocumento.setMontoCobrado(documento.getMontoCobrado());
		nuevoDocumento.setMes(documento.getMes());
		nuevoDocumento.setMontoContrato(documento.getMontoContrato());
		nuevoDocumento.setMontoGenerado(documento.getMontoGenerado());
		nuevoDocumento.setNombre(documento.getNombre());
		nuevoDocumento.setNumeroComprobante(documento.getNumeroComprobante());
		nuevoDocumento.setSaldo(documento.getSaldo());
		nuevoDocumento.setSerieComprobante(documento.getSerieComprobante());
		nuevoDocumento.setTblCxcBitacora(documento.getTblCxcBitacora());
		nuevoDocumento.setTipoComprobante(documento.getTipoComprobante());
		nuevoDocumento.setTipoDocumento(documento.getTipoDocumento());
		nuevoDocumento.setTipoMoneda(documento.getTipoMoneda());
		nuevoDocumento.setTipoReferencia(documento.getTipoReferencia());
		nuevoDocumento.setUsuarioCreacion(contrato.getUsuarioCreacion()); //este es el nuevo dato
		
		documentoDao.save(nuevoDocumento);
	}
	//Actualizar las cxc: Alquiler, servicios con el codigo del contrato renovado
	private void actualizarCxCFlagRenovado(TblCxcDocumento documento, TblContrato contrato){
		documento.setUsuarioModificacion(contrato.getUsuarioModificacion());
		documento.setFechaModificacion(new Date());
		documento.setCodigoRenovacion(contrato.getCodigoContrato());
		documentoDao.save(documento);
		
	}

	//Registrar los cobros de Alquiler y Servicio
	private void registrarCobrosPendientes(TblContrato contrato, Integer codigoContrato){
		List<TblCxcDocumento> listaAlquiler = null;
		List<TblCxcDocumento> listaServicio = null;
		List<TblCxcDocumento> listaLuz = null;
		
		listaAlquiler = this.obtenerAlquilerContrato(codigoContrato);
		listaServicio = this.obtenerServicioContrato(codigoContrato);
		listaLuz = this.obtenerLuzContrato(codigoContrato);
		if (!listaAlquiler.isEmpty() && listaAlquiler.size()>0){
			for(TblCxcDocumento doc: listaAlquiler){
				this.registrarCxCContrato(doc, contrato);
				this.actualizarCxCFlagRenovado(doc, contrato);
			}
		}
		if (!listaServicio.isEmpty() && listaServicio.size()>0){
			for(TblCxcDocumento doc: listaServicio){
				this.registrarCxCContrato(doc, contrato);
				this.actualizarCxCFlagRenovado(doc, contrato);
			}
		}
		if (!listaLuz.isEmpty() && listaLuz.size()>0){
			for(TblCxcDocumento doc: listaLuz){
				this.registrarCxCContrato(doc, contrato);
				this.actualizarCxCFlagRenovado(doc, contrato);
			}
		}
	}
}
