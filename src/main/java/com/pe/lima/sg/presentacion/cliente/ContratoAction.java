package com.pe.lima.sg.presentacion.cliente;

import static com.pe.lima.sg.dao.caja.CxCBitacoraSpecifications.conAnio;
import static com.pe.lima.sg.dao.caja.CxCBitacoraSpecifications.conCodigoContrato;
import static com.pe.lima.sg.dao.caja.CxCBitacoraSpecifications.conMes;
import static com.pe.lima.sg.dao.caja.CxCBitacoraSpecifications.conTipoCobro;
import static com.pe.lima.sg.dao.caja.CxCBitacoraSpecifications.conTipoOperacion;
/*
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conEdificio;
//import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conEstado;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conMaterno;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conNombre;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conPaterno;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conRazonSocial;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conRuc;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conTienda;
import static com.pe.lima.sg.dao.mantenimiento.PersonaSpecifications.conDNI;
import static com.pe.lima.sg.dao.mantenimiento.PersonaSpecifications.conEstado;
import static com.pe.lima.sg.dao.mantenimiento.PersonaSpecifications.conMaterno;
import static com.pe.lima.sg.dao.mantenimiento.PersonaSpecifications.conNombre;
import static com.pe.lima.sg.dao.mantenimiento.PersonaSpecifications.conPaterno;
import static com.pe.lima.sg.dao.mantenimiento.PersonaSpecifications.conRazonSocial;
import static com.pe.lima.sg.dao.mantenimiento.PersonaSpecifications.conRuc;*/
import static com.pe.lima.sg.dao.mantenimiento.TiendaSpecifications.conCodigoEdificio;
import static com.pe.lima.sg.dao.mantenimiento.TiendaSpecifications.conEstadoTienda;
//import static com.pe.lima.sg.dao.mantenimiento.TiendaSpecifications.conEstado;
import static com.pe.lima.sg.dao.mantenimiento.TiendaSpecifications.conNumero;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import com.pe.lima.sg.bean.caja.BitacoraBean;
//import com.pe.lima.sg.bean.caja.GastoBean;
import com.pe.lima.sg.bean.cliente.PeriodoBean;
import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.dao.caja.ICxCBitacoraDAO;
import com.pe.lima.sg.dao.caja.ICxCDocumentoDAO;
import com.pe.lima.sg.dao.caja.ISunatCabeceraDAO;
import com.pe.lima.sg.dao.caja.ISunatDetalleDAO;
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
import com.pe.lima.sg.dao.mantenimiento.ISerieDAO;
import com.pe.lima.sg.dao.mantenimiento.ISuministroDAO;
import com.pe.lima.sg.dao.mantenimiento.ITiendaDAO;
import com.pe.lima.sg.dao.mantenimiento.ITipoServicioDAO;
import com.pe.lima.sg.entity.caja.TblCxcBitacora;
import com.pe.lima.sg.entity.caja.TblCxcDocumento;
import com.pe.lima.sg.entity.caja.TblSunatCabecera;
import com.pe.lima.sg.entity.caja.TblSunatDetalle;
import com.pe.lima.sg.entity.cliente.TblArbitrio;
import com.pe.lima.sg.entity.cliente.TblContrato;
import com.pe.lima.sg.entity.cliente.TblContratoCliente;
import com.pe.lima.sg.entity.cliente.TblContratoPrimerCobro;
import com.pe.lima.sg.entity.cliente.TblContratoServicio;
import com.pe.lima.sg.entity.cliente.TblLuzxtienda;
import com.pe.lima.sg.entity.cliente.TblObservacion;
import com.pe.lima.sg.entity.mantenimiento.TblEdificio;
//import com.pe.lima.sg.entity.mantenimiento.TblGasto;
import com.pe.lima.sg.entity.mantenimiento.TblParametro;
import com.pe.lima.sg.entity.mantenimiento.TblPersona;
import com.pe.lima.sg.entity.mantenimiento.TblSerie;
import com.pe.lima.sg.entity.mantenimiento.TblSuministro;
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

import lombok.extern.slf4j.Slf4j;

/**
 * Clase Bean que se encarga de la administracion de los contratos
 *
 * 			
 */
@Slf4j
@Controller
public class ContratoAction extends BaseOperacionPresentacion<TblContrato> {
	
	@Autowired
	private IContratoDAO contratoDao;

	@Autowired
	private ITiendaDAO tiendaDao;

	@Autowired
	private ISuministroDAO suministroDao;

	@Autowired
	private IPersonaDAO personaDao;

	@Autowired
	private IArbitrioDAO arbitrioDao;
	
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
	private IObservacionDAO observacionDao;

	@Autowired
	private ListaUtilAction listaUtil;

	@Autowired
	private ITipoServicioDAO tipoServicioDao;
	
	@Autowired
	private IParametroDAO parametroDao;
	
	
	@Autowired
	private ICxCBitacoraDAO cxcBitacoraDao;

	@Autowired
	private ICxCDocumentoDAO documentoDao;

	@Autowired
	private ISunatCabeceraDAO sunatCabeceraDao;
	
	@Autowired
	private ISunatDetalleDAO sunatDetalleDao;
	
	@Autowired
	private ISerieDAO serieDao;
	
	
	
	private String urlPaginado = "/contratos/paginado/"; 
	private String urlPaginadoTienda = "/contratos/tienda/paginado/"; 
	private String urlPaginadoCliente = "/contratos/cliente/paginado/";
	
	@SuppressWarnings("rawtypes")
	@Override
	public BaseOperacionDAO getDao() {
		return contratoDao;
	}

	/**
	 * Se encarga de listar todos los contratos
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/contratos", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path,  PageableSG pageable,HttpServletRequest request) {
		TblContrato filtro = null;
		try{
			log.debug("[traerRegistros] Inicio");
			path = "cliente/contrato/con_listado";
			filtro = new TblContrato();
			filtro.setTblPersona(new TblPersona());
			filtro.setTblTienda(new TblTienda());
			filtro.getTblTienda().setTblEdificio(new TblEdificio());
			model.addAttribute("filtro", filtro);
			request.getSession().setAttribute("sessionFiltroCriterio", filtro);
			//listaUtil.cargarDatosModel(model, Constantes.MAP_LISTA_EDIFICIO);
			
			//this.listarContratos(model, filtro, pageable, this.urlPaginado, request);
			request.getSession().setAttribute("sessionListaContrato", null);
			request.getSession().setAttribute("PageContrato", null);
			request.getSession().setAttribute("PageableSGContrato", pageable);
			log.debug("[traerRegistros] Fin");
		}catch(Exception e){
			log.debug("[traerRegistros] Error:"+e.getMessage());
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
	@RequestMapping(value = "/contratos/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, TblContrato filtro, String path ,  PageableSG pageable, HttpServletRequest request) {
		//Map<String, Object> campos = null;
		path = "cliente/contrato/con_listado";
		String strSeleccion = "";
		try{
			log.debug("[traerRegistrosFiltrados] Inicio");
			this.listarContratos(model, filtro, pageable, this.urlPaginado, request);
			model.addAttribute("filtro", filtro);
			model.addAttribute("strSeleccion", strSeleccion);
			request.getSession().setAttribute("sessionFiltroCriterio", filtro);
			log.debug("[traerRegistrosFiltrados] Fin");
		}catch(Exception e){
			log.debug("[traerRegistrosFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			//campos		= null;
		}
		log.debug("[traerRegistrosFiltrados] Fin");
		return path;
	}

	/*** Listado de Contratos ***/
	private void listarContratos(Model model, TblContrato tblContrato,  PageableSG pageable, String url, HttpServletRequest request){
		//List<TblContrato> entidades = new ArrayList<TblContrato>();
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "numero"));
		try{
			Specification<TblContrato> filtro = Specifications.where(com.pe.lima.sg.dao.cliente.ContratoSpecifications.conNombre(tblContrato.getTblPersona().getNombre()))
					.and(com.pe.lima.sg.dao.cliente.ContratoSpecifications.conPaterno(tblContrato.getTblPersona().getPaterno()))
					.and(com.pe.lima.sg.dao.cliente.ContratoSpecifications.conMaterno(tblContrato.getTblPersona().getMaterno()))
					.and(com.pe.lima.sg.dao.cliente.ContratoSpecifications.conEdificio(tblContrato.getTblTienda().getTblEdificio().getCodigoEdificio()))
					.and(com.pe.lima.sg.dao.cliente.ContratoSpecifications.conTienda(tblContrato.getTblTienda().getNumero()))
					.and(com.pe.lima.sg.dao.cliente.ContratoSpecifications.conRuc(tblContrato.getTblPersona().getNumeroRuc()))
					.and(com.pe.lima.sg.dao.cliente.ContratoSpecifications.conRazonSocial(tblContrato.getTblPersona().getRazonSocial()))
					.and(com.pe.lima.sg.dao.cliente.ContratoSpecifications.conEstado(Constantes.ESTADO_REGISTRO_ACTIVO));
			/*entidades = contratoDao.findAll(filtro);
			log.debug("[listarContratos] entidades:"+entidades);
			model.addAttribute("registros", entidades);*/
			pageable.setSort(sort);
			Page<TblContrato> entidadPage = contratoDao.findAll(filtro, pageable);
			PageWrapper<TblContrato> page = new PageWrapper<TblContrato>(entidadPage, url, pageable);
			model.addAttribute("registros", page.getContent());
			model.addAttribute("page", page);
			
			request.getSession().setAttribute("sessionFiltroCriterio", tblContrato);
			request.getSession().setAttribute("sessionListaContrato", page.getContent());
			request.getSession().setAttribute("PageContrato", page);
			request.getSession().setAttribute("PageableSGContrato", pageable);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sort = null;
		}
	}

	/**
	 * Se encarga de direccionar a la pantalla de edicion del Contrato
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "contrato/editar/{id}", method = RequestMethod.GET)
	public String editarContrato(@PathVariable Integer id, Model model, TblContrato filtro, HttpServletRequest request) {
		TblContrato contrato 					= null;
		BeanRequest beanRequest					= null;
		List<TblContratoServicio> listaServicio	= null;
		List<TblContratoCliente> listaCliente	= null;
		List<TblContratoPrimerCobro> listaCobro	= null;
		List<TblArbitrio> listaArbitrio			= null;
		List<TblLuzxtienda> listaLuzxtienda		= null;
		List<TblObservacion> listaObservacion	= null;
		String path 							= "";
		try{
			contrato = contratoDao.findOne(id);
			listaServicio = contratoServicioDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			listaCliente = contratoClienteDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			listaCobro = contratoPrimerCobroDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			listaArbitrio = arbitrioDao.listarAllActivosxContrato(contrato.getCodigoContrato());
			listaLuzxtienda = luzxTiendaDao.listarLuzTiendaxContrato(contrato.getCodigoContrato());
			listaObservacion = observacionDao.listarObservacionxContrato(contrato.getCodigoContrato());
			beanRequest = new BeanRequest();
			beanRequest.setContrato(contrato);
			beanRequest.setContratoServicio(new TblContratoServicio());
			beanRequest.setListaServicio(listaServicio);
			beanRequest.setListaCliente(listaCliente);
			beanRequest.setContratoPrimerCobro(new TblContratoPrimerCobro());
			beanRequest.setListaPrimerCobro(listaCobro);
			beanRequest.setArbitrio(new TblArbitrio());
			beanRequest.setListaArbitrio(listaArbitrio);
			beanRequest.setListaLuzxTienda(listaLuzxtienda);
			beanRequest.setListaObservacion(listaObservacion);
			beanRequest.setObservacion(new TblObservacion());
			this.cargarListasRequestBeanContrato(model, beanRequest);
			
			this.cargarListaOperacionContrato(model);
			
			request.getSession().setAttribute("beanRequest", beanRequest);
			//if (contrato.getEstadoContrato().equals(Constantes.ESTADO_CONTRATO_PENDIENTE)){
				path = "cliente/contrato/con_edicion";
			/*}else{
				path = "cliente/contrato/con_edicion_min";
			}*/


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

	public void inicializaDataDefaultContrato(TblContrato contrato){
		List<TblParametro> listaParametro;
		listaParametro = parametroDao.buscarOneByNombre(Constantes.PAR_FIN_CONTRATO);
		contrato.setFechaContrato(UtilSGT.addDays(new Date(), 0));
		contrato.setFechaInicio(UtilSGT.addDays(new Date(), 0));
		UtilSGT.obtenerFechaFinContrato(contrato, listaParametro);
		//this.obtenerFechaFinContrato(contrato);
		
		contrato.setTblTienda(new TblTienda());
		contrato.getTblTienda().setNumero("");
		contrato.getTblTienda().setTblEdificio(new TblEdificio());
		contrato.getTblTienda().setTblSuministro(new TblSuministro());
		contrato.getTblTienda().getTblSuministro().setNumero("");
		contrato.setTblPersona(new TblPersona());
		/*listaParametro = parametroDao.buscarOneByNombre(Constantes.PAR_PERIODO_ADELANTO);
		if(listaParametro!=null && listaParametro.size()>0){
			contrato.setPeriodoAdelanto(new Integer(listaParametro.get(0).getDato()));
		}*/
		//Alquiler
		contrato.setTipoMonedaAlquiler(Constantes.MONEDA_DOLAR);
		contrato.setTipoDocumentoAlquiler(Constantes.TIPO_DOC_FACTURA);
		contrato.setTipoMonedaGarantia(Constantes.MONEDA_DOLAR);
		contrato.setTipoDocumentoGarantia(Constantes.TIPO_DOC_INTERNO);
		contrato.setTipoCobro(Constantes.TIPO_COBRO_FIN);
		contrato.setTipoGarantia(Constantes.TIPO_GARANTIA_SIN);
		contrato.setMontoGarantia(new BigDecimal("0"));
	}
	
	public void inicializaDataDefaultServicio(TblContratoServicio  contratoServicio){
		
		contratoServicio.getTblTipoServicio().setCodigoTipoServicio(Constantes.CODIGO_SERVICIO_MANTENIMIENTO);
		contratoServicio.setTipoMonedaServicio(Constantes.MONEDA_SOL);
		contratoServicio.setTipoDocumentoServicio(Constantes.TIPO_DOC_INTERNO);
	}
	/**
	 * Se encarga de direccionar a la pantalla de creacion del Contrato
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "contrato/nuevo", method = RequestMethod.GET)
	public String crearContrato(Model model ,TblContrato filtro, HttpServletRequest request) {
		TblContrato contrato 					= null;
		TblContratoServicio  contratoServicio 	= null;
		TblContratoPrimerCobro primerCobro		= null;
		TblArbitrio arbitrio					= null;
		TblLuzxtienda luzxTienda				= null;
		TblObservacion observacion				= null;
		//Variables para tener todo en session
		BeanRequest beanRequest					= null;
		List<TblContratoServicio> listaServicio	= new ArrayList<TblContratoServicio>();
		
		try{
			log.debug("[crearContrato] Inicio");
			beanRequest = new BeanRequest();
			//Inicialización del contrato
			contrato = new TblContrato();
			this.inicializaDataDefaultContrato(contrato);
			
			model.addAttribute("contrato", contrato);
			this.cargarListaOperacionContrato(model);
			//Inicializacion de los Servicios - Listado
			contratoServicio = new TblContratoServicio();
			contratoServicio.setTblTipoServicio(new TblTipoServicio());
			this.inicializaDataDefaultServicio(contratoServicio);
			model.addAttribute("contratoServicio", contratoServicio);
			model.addAttribute("listaServicio", listaServicio);
			//Inicializacion de los primeros cobros - Listado
			primerCobro = new TblContratoPrimerCobro();
			primerCobro.setTblTipoServicio(new TblTipoServicio());
			model.addAttribute("contratoPrimerCobro", primerCobro);
			model.addAttribute("listaPrimerosCobros", null);
			//Inicializacion de los arbitrios
			/*arbitrio = new TblArbitrio();
			model.addAttribute("arbitrio", arbitrio);
			model.addAttribute("listaArbitrios", null);
			//Inicializacion de la luz
			luzxTienda =new TblLuzxtienda();
			model.addAttribute("luzxTienda", luzxTienda);
			model.addAttribute("listaLuzxTienda", null);*/
			//Inicializacion de la observacion
			observacion = new TblObservacion();
			model.addAttribute("observacion", observacion);
			model.addAttribute("listaObservacion",null);
			
			beanRequest = new BeanRequest();
			beanRequest.setContrato(contrato);
			beanRequest.setContratoServicio(contratoServicio);
			beanRequest.setContratoPrimerCobro(primerCobro);
			beanRequest.setArbitrio(arbitrio);
			beanRequest.setLuzxTienda(luzxTienda);
			beanRequest.setObservacion(observacion);
			request.getSession().setAttribute("beanRequest", beanRequest);
			model.addAttribute("filtro", filtro);
			model.addAttribute("contratoServicio", contratoServicio);
			log.debug("[crearContrato] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "cliente/contrato/con_nuevo";
	}

	@Override
	public void preGuardar(TblContrato entidad, HttpServletRequest request) {
		BeanRequest beanRequest	= null;
		List<TblContratoServicio> listaCS		= null;
		List<TblContratoPrimerCobro> listaPC	= null;
		//List<TblArbitrio> listaA				= null;
		//List<TblLuzxtienda> listaL				= null;
		List<TblObservacion> listaObservacion	= null;
		String strNumeroContrato				= null;
		Integer intTotalRegistros				= null;
		try{
			log.debug("[preGuardar] Inicio" );
			entidad.setFechaCreacion(new Date(System.currentTimeMillis()));
			entidad.setIpCreacion(request.getRemoteAddr());
			entidad.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			//Datos del Contrato
			entidad.setEstadoContrato(Constantes.ESTADO_CONTRATO_PENDIENTE);
			intTotalRegistros = contratoDao.countContrato();
			intTotalRegistros = intTotalRegistros==null?1:intTotalRegistros+1;
			strNumeroContrato = Constantes.PREFIJO_CONTRATO + UtilSGT.completarCeros(intTotalRegistros.toString(), 6);
			entidad.setNumero(strNumeroContrato);
			//Para lista de Servicios
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			listaCS = beanRequest.getListaServicio();
			if (listaCS!=null && listaCS.size()>0){
				for(TblContratoServicio cs:listaCS){
					cs.setFechaCreacion(new Date(System.currentTimeMillis()));
					cs.setIpCreacion(request.getRemoteAddr());
					cs.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
					cs.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
				}
			}
			//Para lista de primeros cobros
			listaPC = beanRequest.getListaPrimerCobro();
			if (listaPC!=null && listaPC.size()>0){
				for(TblContratoPrimerCobro pc:listaPC){
					pc.setFechaCreacion(new Date(System.currentTimeMillis()));
					pc.setIpCreacion(request.getRemoteAddr());
					pc.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
					pc.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
				}
			}
			//Para lista de arbitrios
			/*listaA = beanRequest.getListaArbitrio();
			if (listaA!=null && listaA.size()>0){
				for(TblArbitrio a:listaA){
					a.setFechaCreacion(new Date(System.currentTimeMillis()));
					a.setIpCreacion(request.getRemoteAddr());
					a.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
					a.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
				}
			}*/
			//Para lista de luzxtienda 
			/*listaL = beanRequest.getListaLuzxTienda();
			if (listaL!=null && listaL.size()>0){
				for(TblLuzxtienda a:listaL){
					a.setFechaCreacion(new Date(System.currentTimeMillis()));
					a.setIpCreacion(request.getRemoteAddr());
					a.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
					a.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
				}
			}*/
			//Para lista de observaciones
			listaObservacion = beanRequest.getListaObservacion();
			if (listaObservacion !=null && listaObservacion.size()>0){
				for(TblObservacion observacion: listaObservacion){
					observacion.setFechaCreacion(new Date(System.currentTimeMillis()));
					observacion.setIpCreacion(request.getRemoteAddr());
					observacion.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
					observacion.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
				}
			}
			//ContratoCliente
			beanRequest.setContratoCliente(new TblContratoCliente());
			beanRequest.getContratoCliente().setFechaCreacion(new Date(System.currentTimeMillis()));
			beanRequest.getContratoCliente().setIpCreacion(request.getRemoteAddr());
			beanRequest.getContratoCliente().setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			beanRequest.getContratoCliente().setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			beanRequest.getContratoCliente().setFechaInicio(entidad.getFechaInicio());
			beanRequest.getContratoCliente().setFechaFin(entidad.getFechaFin());
			log.debug("[preGuardar] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			beanRequest 		= null;
			listaObservacion	= null;
			listaCS				= null;
			listaPC				= null;
			strNumeroContrato	= null;
			intTotalRegistros	= null;
		}

	}

	@Override
	public TblContrato getNuevaEntidad() {
		
		return null;
	}
	/*
	 * Genera los primeros cobros del contrato
	 */
	public void generarPrimerosCobros(TblContrato entidad, HttpServletRequest request){
		TblContratoPrimerCobro primerCobro 	= new TblContratoPrimerCobro();
		//Date datFechaFinContrato			= null;
		//Date datFecha						= null;
		//Double dblFactor					= null;
		BeanRequest beanRequest				= null;
		List<TblContratoPrimerCobro> lista	= new ArrayList<TblContratoPrimerCobro>();
		Date fechaFinMes					= null;
		try{
			fechaFinMes = UtilSGT.getDatetoString(UtilSGT.getLastDate(UtilSGT.getDateStringFormat(entidad.getFechaInicio())));
			//Alquiler
			if (entidad.getPrimerCobroAlquiler() != null && entidad.getPrimerCobroAlquiler().doubleValue() > 0){
				log.debug("[generarPrimerosCobros] fechaFinMes:"+fechaFinMes);
				//Alquiler
				primerCobro.setTblTipoServicio(new TblTipoServicio());
				primerCobro.getTblTipoServicio().setCodigoTipoServicio(Constantes.TIPO_SERVICIO_ALQUILER);
				/*datFecha = entidad.getFechaInicio();
				dblFactor = UtilSGT.obtenerFactorPrimerCobro(datFecha);
				primerCobro.setMontoCobro(new BigDecimal(dblFactor*entidad.getMontoAlquiler().doubleValue()));*/
				primerCobro.setMontoCobro(entidad.getPrimerCobroAlquiler());
				primerCobro.setTipoMoneda(entidad.getTipoMonedaAlquiler());
				primerCobro.setObservacion(Constantes.DESC_PRIMEROS_COBROS_ALQUILER);
				primerCobro.setFechaCobro(fechaFinMes);
				primerCobro.setTipoDocumento(entidad.getTipoDocumentoAlquiler());
				lista.add(primerCobro);
			}
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
			
			//Garantia
			if (entidad.getMontoGarantia()!=null && entidad.getMontoGarantia().doubleValue()>0){
				primerCobro 	= new TblContratoPrimerCobro();
				primerCobro.setTblTipoServicio(new TblTipoServicio());
				primerCobro.getTblTipoServicio().setCodigoTipoServicio(Constantes.TIPO_SERVICIO_GARANTIA);
				primerCobro.setMontoCobro(entidad.getMontoGarantia());
				primerCobro.setTipoMoneda(entidad.getTipoMonedaGarantia());
				primerCobro.setObservacion(Constantes.DESC_PRIMEROS_COBROS_GARANTIA);
				primerCobro.setFechaCobro(fechaFinMes);
				primerCobro.setTipoDocumento(entidad.getTipoDocumentoGarantia());
				lista.add(primerCobro);
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

	/**
	 * Se encarga de guardar la informacion 
	 * 
	 * @param 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/contrato/nuevo/guardar", method = RequestMethod.POST)
	public String guardarEntidad(Model model, TblContrato entidad, HttpServletRequest request, String path) {
		path 											= "cliente/contrato/con_listado";
		BeanRequest beanRequest							= null;
		TblContrato contrato							= null;
		TblContratoCliente cliente						= null;
		List<TblContratoServicio> listaServicio 		= null;
		List<TblContratoPrimerCobro> listaCobro			= null;
		List<TblObservacion> listaObservacion			= null;
		//List<TblArbitrio> listaArbitrio					= null;
		//List<TblLuzxtienda> listaLuzxTienda				= null;
		//List<TblContrato> entidades 					= new ArrayList<TblContrato>();
		TblContrato filtro								= null;
		TblTienda tienda								= null;
		TblSuministro suministro						= null;
		try{
			log.debug("[guardarEntidad] Inicio" );
			if (this.validarNegocio(model, entidad, request)){
				log.debug("[guardarEntidad] Pre Guardar..." );
				//Generar Primeros Cobros
				this.generarPrimerosCobros(entidad, request);
				this.preGuardar(entidad, request);
				boolean exitoso = super.guardar(entidad, model);
				log.debug("[guardarEntidad] Guardado..." );
				if (exitoso){
					beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
					contrato = contratoDao.findByNumeroContrato(entidad.getNumero());
					//Actualizando la tienda a Ocupado
					tienda = tiendaDao.findOne(contrato.getTblTienda().getCodigoTienda());
					suministro = suministroDao.findOne(tienda.getTblSuministro().getCodigoSuministro());
					tienda.setTblSuministro(suministro);
					//tiendaDao.updateEstado(entidad.getTblTienda().getCodigoTienda(), Constantes.ESTADO_TIENDA_OCUPADO);
					preEditarTienda(tienda, request);
					tienda.setEstadoTienda(Constantes.ESTADO_TIENDA_OCUPADO);
					tiendaDao.save(tienda);
					//registrando el cliente
					cliente = beanRequest.getContratoCliente();
					cliente.setTblContrato(contrato);
					cliente.setCodigoPersona(contrato.getTblPersona().getCodigoPersona());
					contratoClienteDao.save(cliente);
					
					//registrando los servicios
					listaServicio = beanRequest.getListaServicio();
					if (listaServicio!=null && listaServicio.size()>0){
						log.debug("[guardarEntidad] listaServicio:"+listaServicio.size());
						for(TblContratoServicio servicio: listaServicio){
							servicio.setTblContrato(contrato);
							servicio = contratoServicioDao.save(servicio);
						}
					}
					//registrando los primeros cobros
					listaCobro = beanRequest.getListaPrimerCobro();
					if (listaCobro!=null && listaCobro.size()>0){
						log.debug("[guardarEntidad] listaCobro:"+listaCobro.size());
						for(TblContratoPrimerCobro cobro: listaCobro){
							cobro.setTblContrato(contrato);
							contratoPrimerCobroDao.save(cobro);
						}
						//Generamos documentos
						entidad.setCodigoContrato(contrato.getCodigoContrato());
						this.generarDocumentoPrimerosCobros(model,entidad, request);

						
					}
					//Generar los alquileres
					this.generarDocumentoAlquiler(model, entidad, request);
					//Generar los servicios
					if (listaServicio!=null && listaServicio.size()>0){
						for(TblContratoServicio servicio: listaServicio){
							this.generarDocumentoServicio(model, entidad, servicio, request);
						}
					}
					//registrando las observaciones
					listaObservacion = beanRequest.getListaObservacion();
					if (listaObservacion!=null && listaObservacion.size()>0){
						log.debug("[guardarEntidad] listaObservacion:"+listaObservacion.size());
						for(TblObservacion observacion: listaObservacion){
							observacion.setCodigoContrato(contrato.getCodigoContrato());
							observacionDao.save(observacion);
						}
					}
					//registrando los arbitrios
					/*listaArbitrio = beanRequest.getListaArbitrio();
					if (listaArbitrio!=null && listaArbitrio.size()>0){
						for(TblArbitrio arbitrio: listaArbitrio){
							arbitrio.setCodigoContrato(contrato.getCodigoContrato());
							arbitrioDao.save(arbitrio);
						}
					}
					//registrando la luz
					listaLuzxTienda = beanRequest.getListaLuzxTienda();
					if (listaLuzxTienda!=null && listaLuzxTienda.size()>0){
						for(TblLuzxtienda luzxTienda: listaLuzxTienda){
							luzxTienda.setCodigoContrato(contrato.getCodigoContrato());
							luzxTiendaDao.save(luzxTienda);
						}
					}*/
					
					//Listar el contrato
					//entidades.add(contrato);
					filtro = new TblContrato();
					filtro.setTblPersona(new TblPersona());
					filtro.setTblTienda(new TblTienda());
					filtro.getTblTienda().setTblEdificio(new TblEdificio());
					model.addAttribute("filtro", filtro);
					//listaUtil.cargarDatosModel(model, Constantes.MAP_LISTA_EDIFICIO);
					this.listarContratos(model, contrato, new PageableSG(), this.urlPaginado, request);
					//model.addAttribute("registros", entidades);
					

				}else{
					path = "cliente/contrato/con_nuevo";

					beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
					beanRequest.setContrato(entidad);	
					this.cargarListasRequestBeanContrato(model, beanRequest);
					this.cargarListaOperacionContrato(model);

					request.getSession().setAttribute("beanRequest", beanRequest);

				}
			}else{
				path = "cliente/contrato/con_nuevo";
				beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
				beanRequest.setContrato(entidad);		
				this.cargarListasRequestBeanContrato(model, beanRequest);
				this.cargarListaOperacionContrato(model);

				request.getSession().setAttribute("beanRequest", beanRequest);
			}

			log.debug("[guardarEntidad] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			beanRequest		= null;
			contrato		= null;
			cliente			= null;
			listaServicio 	= null;
			listaCobro		= null;
			//listaArbitrio	= null;
			//entidades 		= null;
			filtro			= null;
		}
		return path;

	}
	/*
	 * Se asigna los datos del campo de auditoria
	 */
	public void preGuardarSunatDetalle(TblSunatDetalle entidad, HttpServletRequest request) {
		try{
			log.debug("[preGuardarSunatDetalle] Inicio" );
			entidad.setFechaCreacion(new Date(System.currentTimeMillis()));
			entidad.setIpCreacion(request.getRemoteAddr());
			entidad.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			log.debug("[preGuardarSunatDetalle] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	/*
	 * Se asigna los datos del campo de auditoria
	 */
	public void preGuardarSunatCabecera(TblSunatCabecera entidad, HttpServletRequest request) {
		try{
			log.debug("[preGuardarSunatCabecera] Inicio" );
			entidad.setFechaCreacion(new Date(System.currentTimeMillis()));
			entidad.setIpCreacion(request.getRemoteAddr());
			entidad.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			log.debug("[preGuardarSunatCabecera] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	 * Se asigna los datos del campo de auditoria
	 */
	public void preGuardarDocumento(TblCxcDocumento entidad, HttpServletRequest request) {
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
	/*
	 * Preguardar: Campos de auditoria
	 */
	public void preGuardarBitacora(TblCxcBitacora entidad, HttpServletRequest request) {
		try{
			log.debug("[preGuardarBitacora] Inicio" );
			entidad.setFechaCreacion(new Date(System.currentTimeMillis()));
			entidad.setIpCreacion(request.getRemoteAddr());
			entidad.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			log.debug("[preGuardarBitacora] Fin" );
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
	/*
	 * Registro del alquiler
	 */
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
			cabecera.setOperacionGravada(UtilSGT.obtenerTotalMontoGravada(contrato.getMontoAlquiler(), Constantes.SUNAT_IGV));
			//Total valor de venta - Operaciones inafectas
			cabecera.setOperacionInafecta(new BigDecimal("0"));
			//Total valor de venta - Operaciones exoneradas
			cabecera.setOperacionExonerada(new BigDecimal("0"));
			//Sumatoria IGV
			cabecera.setMontoIgv(UtilSGT.obtenerTotalImpuestoGravada(contrato.getMontoAlquiler(), Constantes.SUNAT_IGV));
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
	
	/*private boolean generarDocumentoPrimerCobroSunat(TblCxcDocumento documento, TblContrato entidad, HttpServletRequest request){
		boolean resultado = true;
		TblSunatCabecera cabecera				= null;
		TblSunatDetalle detalle					= null;
		String strMensaje						= "";
		//Generamos informacion para la SUNAT
		if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_FACTURA)){
			//registro de la cabecera del comprobante
			cabecera = this.registrarCabeceraSunat(entidad, documento, request);
			if (cabecera != null){
				detalle = this.registrarDetalleSunat(cabecera, documento, Constantes.PAR_SUNAT_ALQUILER,request);
				if (detalle !=null){
					//Generar Archivos planos
					if (UtilSGT.generarArchivoCabecera(cabecera)){
						if (UtilSGT.generarArchivoDetalle(detalle)){
							resultado = true;
						}else{
							//model.addAttribute("respuesta", "No se pudo generar el archivo plano Detalle SUNAT del Alquiler - Factura para el contrato ["+contrato.getNumero()+"]");
							strMensaje = strMensaje + "No se pudo generar el archivo plano Detalle SUNAT del Alquiler - Factura para el contrato ["+entidad.getNumero()+"]\n";
							resultado = false;
							return resultado;
						}
					}else{
						//model.addAttribute("respuesta", "No se pudo generar el archivo plano Cabecera SUNAT del Alquiler - Factura para el contrato ["+contrato.getNumero()+"]");
						strMensaje = strMensaje +"No se pudo generar el archivo plano Cabecera SUNAT del Alquiler - Factura para el contrato ["+entidad.getNumero()+"]\n";
						resultado = false;
						return resultado;
					}
				}else{
					//model.addAttribute("respuesta", "No se pudo registrar el Detalle SUNAT del Alquiler - Factura para el contrato ["+contrato.getNumero()+"]");
					strMensaje = strMensaje +"No se pudo registrar el Detalle SUNAT del Alquiler - Factura para el contrato ["+entidad.getNumero()+"]\n";
					resultado = false;
					return resultado;
				}
			}else{
				//model.addAttribute("respuesta", "No se pudo registrar la Cabecera SUNAT del Alquiler - Factura para el contrato ["+contrato.getNumero()+"]");
				strMensaje = strMensaje +"No se pudo registrar la Cabecera SUNAT del Alquiler - Factura para el contrato ["+entidad.getNumero()+"] \n";
				resultado = false;
				return resultado;
			}
			
		}else if (documento.getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_BOLETA)){
			//registro de la cabecera del comprobante
			cabecera = this.registrarCabeceraSunat(entidad, documento, request);
			if (cabecera != null){
				detalle = this.registrarDetalleSunat(cabecera, documento, Constantes.PAR_SUNAT_ALQUILER, request);
				if (detalle !=null){
					//Generar Archivos planos
					if (UtilSGT.generarArchivoCabecera(cabecera)){
						if (UtilSGT.generarArchivoDetalle(detalle)){
							resultado = true;
						}else{
							//model.addAttribute("respuesta", "No se pudo generar el archivo plano Detalle SUNAT del Alquiler - Boleta para el contrato ["+contrato.getNumero()+"]");
							strMensaje = strMensaje +"No se pudo generar el archivo plano Detalle SUNAT del Alquiler - Boleta para el contrato ["+entidad.getNumero()+"]\n";
							resultado = false;
							return resultado;
						}
					}else{
						//model.addAttribute("respuesta", "No se pudo generar el archivo plano Cabecera SUNAT del Alquiler - Boleta para el contrato ["+contrato.getNumero()+"]");
						strMensaje = strMensaje +"No se pudo generar el archivo plano Cabecera SUNAT del Alquiler - Boleta para el contrato ["+entidad.getNumero()+"]\n";
						resultado = false;
						return resultado;
					}
				}else{
					//model.addAttribute("respuesta", "No se pudo registrar la Detalle SUNAT del Alquiler - Boleta para el contrato ["+contrato.getNumero()+"]");
					strMensaje = strMensaje +"No se pudo registrar la Detalle SUNAT del Alquiler - Boleta para el contrato ["+entidad.getNumero()+"]\n";
					resultado = false;
					return resultado;
				}
			}else{
				//model.addAttribute("respuesta", "No se pudo registrar la Cabecera SUNAT del Alquiler - Boleta para el contrato ["+contrato.getNumero()+"]");
				strMensaje = strMensaje +"No se pudo registrar la Cabecera SUNAT del Alquiler - Boleta para el contrato ["+entidad.getNumero()+"]\n";
				resultado = false;
				return resultado;
			}
		}else{
			//No se genera comprobante para el tipo Interno
			resultado = true;
		}
		return resultado;
	}*/
	
	
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
			lista = contratoPrimerCobroDao.listarAllActivosXContrato(entidad.getCodigoContrato());
			if (lista !=null && lista.size()>0){
				//cxcBitacoraAction = new CxcBitacoraAction();
				bitacoraBean = new BitacoraBean();
				bitacoraBean.setAnio(UtilSGT.getAnioDate(entidad.getFechaInicio()));
				bitacoraBean.setMes(UtilSGT.getMesDate(entidad.getFechaInicio()).toString());
				
				//Registramos la Bitacora del CxC del alquiler
				bitacoraBean.setCodigoContrato(entidad.getCodigoContrato());
				boolean exitoso =  this.registrarBitacora(model, bitacoraBean, Constantes.TIPO_COBRO_PRIMER_COBRO, Constantes.SERIE_TIPO_OPERACION_PRIMEROS_COBROS, request);
				if (exitoso) {
					criterio = Specifications.where(conAnio(bitacoraBean.getAnio()))
							.and(conMes(new Integer(bitacoraBean.getMes())))
							.and(conTipoCobro(Constantes.TIPO_COBRO_PRIMER_COBRO))
							.and(conCodigoContrato(entidad.getCodigoContrato()))
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


	public void preEditarTienda(TblTienda entidad, HttpServletRequest request) {
		try{
			log.debug("[preEditarTienda] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			log.debug("[preEditarTienda] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
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
			if (entidad.getFechaContrato() == null){
				exitoso = false;
				model.addAttribute("resultadoContrato", "Debe seleccionar la Fecha de Contrato antes de Registrar");
				return exitoso;
			}
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
	@RequestMapping(value = "/contrato/locales", method = RequestMethod.POST)
	public String listarLocales(Model model, TblContrato contrato, String path, HttpServletRequest request, PageableSG pageable) {
		path 						= "cliente/contrato/con_tie_listado";
		TblEdificio edificio 		= null;
		Filtro filtro 				= null;
		Map<String, Object> campos 	= null;
		BeanRequest beanRequest		= null;
		try{
			log.debug("[listarLocales] Inicio");
			if (contrato.getTblTienda().getTblEdificio().getCodigoEdificio() <= 0){
				beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
				contrato = beanRequest.getContrato();
				this.cargarListasRequestBeanContrato(model, beanRequest);
				
				this.cargarListaOperacionContrato(model);

				request.getSession().setAttribute("beanRequest", beanRequest);
				model.addAttribute("resultadoContrato", "Debe seleccionar el Inmueble antes de listar los Locales");
				return "cliente/contrato/con_nuevo"; 
			}
			log.debug("[listarLocales] Codigo :"+contrato.getTblTienda().getTblEdificio().getCodigoEdificio());
			model.addAttribute("contrato", contrato);
			edificio = edificioDao.findOne(contrato.getTblTienda().getTblEdificio().getCodigoEdificio());
			model.addAttribute("edificio", edificio);

			filtro = new Filtro();
			filtro.setCodigo(contrato.getTblTienda().getTblEdificio().getCodigoEdificio());
			filtro.setStrOperacion(Constantes.OPERACION_NUEVO);
			//this.listarTiendas(model, filtro, pageable, this.urlPaginadoTienda);
			model.addAttribute("registros", new ArrayList<TblTienda>() );
			model.addAttribute("page", null);
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			model.addAttribute("filtro", filtro);
			//Se mantiene en Session el contrato hasta retornar
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			beanRequest.setContrato(contrato);
			request.getSession().setAttribute("beanRequest", beanRequest);
			request.getSession().setAttribute("edificioRequest", edificio);
			request.getSession().setAttribute("sessionFiltroTiendaCriterio", filtro);
			
		}catch(Exception e){
			log.debug("[listarLocales] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			edificio	= null;
			filtro		= null;
			campos		= null;
		}
		log.debug("[listarLocales] Fin");
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
	private void listarTiendas(Model model, Filtro filtro,  PageableSG pageable, String url){
		//List<TblTienda> entidades = new ArrayList<TblTienda>();
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "numero"));
		try{
			/*entidades = tiendaDao.listarTiendaDesocupada(filtro.getCodigo(), filtro.getNumero());
			log.debug("[listarTiendaes] entidades:"+entidades);
			model.addAttribute("registros", entidades);*/

			Specification<TblTienda> criterio = Specifications.where(conNumero(filtro.getNumero()))
					.and(conCodigoEdificio(filtro.getCodigo()))
					.and(conEstadoTienda("DSC"))
					.and(com.pe.lima.sg.dao.mantenimiento.TiendaSpecifications.conEstado(Constantes.ESTADO_REGISTRO_ACTIVO));
			pageable.setSort(sort);
			Page<TblTienda> entidadPage = tiendaDao.findAll(criterio, pageable);
			PageWrapper<TblTienda> page = new PageWrapper<TblTienda>(entidadPage, url, pageable);
			model.addAttribute("registros", page.getContent());
			model.addAttribute("page", page);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//entidades = null;
			sort = null;
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
	@RequestMapping(value = "/contrato/locales/q", method = RequestMethod.POST)
	public String listarLocalesFiltrados(Model model, Filtro filtro, String path,  PageableSG pageable, HttpServletRequest request) {
		Map<String, Object> campos = null;
		TblEdificio edificio = null;
		path = "cliente/contrato/con_tie_listado";
		try{
			log.debug("[listarLocalesFiltrados] Inicio");
			log.debug("[listarLocalesFiltrados] Operacion:"+filtro.getStrOperacion());
			edificio = edificioDao.findOne(filtro.getCodigo());
			filtro.setCodigo(edificio.getCodigoEdificio());
			model.addAttribute("edificio", edificio);
			this.listarTiendas(model, filtro, pageable, this.urlPaginadoTienda);
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
			model.addAttribute("filtro", filtro);
			request.getSession().setAttribute("sessionFiltroTiendaCriterio", filtro);
		}catch(Exception e){
			log.debug("[listarLocalesFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			campos		= null;
			edificio	= null;
		}
		log.debug("[listarLocalesFiltrados] Fin");
		return path;
	}

	/**
	 * Se encarga de direccionar a la pantalla de contrato
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/contrato/locales/seleccionar", method = RequestMethod.POST)
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
			//Busqueda de la luz del local
			beanRequest.setListaLuzxTienda(luzxTiendaDao.listarLuzTiendaxTienda(entidad.getCodigoTienda()));
			model.addAttribute("listaLuzxTienda", beanRequest.getListaLuzxTienda());
			request.getSession().setAttribute("beanRequest", beanRequest);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return "cliente/contrato/con_nuevo";
	}
	/**
	 * Se encarga de direccionar a la pantalla de contrato
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/contrato/locales/seleccionar/{id}", method = RequestMethod.GET)
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
			this.cargarListasRequestBeanContrato(model, beanRequest);
			
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return "cliente/contrato/con_nuevo";
	}
	/**
	 * Se encarga de Listar los clientes
	 * seleccionado
	 * 
	 * @param model
	 * @param contratoBean
	 * @return
	 */
	@RequestMapping(value = "/contrato/clientes", method = RequestMethod.POST)
	public String listarClientes(Model model, TblContrato contrato, String path, HttpServletRequest request,  PageableSG pageable) {
		path 						= "cliente/contrato/con_per_listado";
		Filtro filtro 				= null;
		BeanRequest beanRequest		= null;
		try{
			log.debug("[listarClientes] Inicio");
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			
			filtro = new Filtro();
			if (contrato.getNumero() == null || contrato.getNumero().equals("")){
				filtro.setStrOperacion(Constantes.OPERACION_NUEVO);
				beanRequest.setContrato(contrato);
				model.addAttribute("contrato", contrato);
			}else{
				filtro.setStrOperacion(Constantes.OPERACION_EDITAR);
				model.addAttribute("contrato", beanRequest.getContrato());
			}
			model.addAttribute("filtro", filtro);
			request.getSession().setAttribute("sessionFiltroClienteCriterio", filtro);
			//this.listarPersonas(model, filtro, pageable, this.urlPaginadoCliente);
			model.addAttribute("registros", new ArrayList<TblPersona>());
			model.addAttribute("page", null);
			this.cargarListaOperacionCliente(model);

			//Se mantiene en Session el contrato hasta retornar
			request.getSession().setAttribute("beanRequest", beanRequest);

		}catch(Exception e){
			log.debug("[traerRegistrosFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			filtro		= null;
		}
		log.debug("[listarClientes] Fin");
		return path;
	}

	/*** Listado de Personas ***/
	private void listarPersonas(Model model, Filtro filtro,  PageableSG pageable, String url){
		//List<TblPersona> entidades = new ArrayList<TblPersona>();
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "nombre"));
		try{
			/*entidades = personaDao.listarCriterios(filtro.getNombre(), filtro.getPaterno(), filtro.getMaterno(), filtro.getDni(), filtro.getRuc(), filtro.getRazonSocial());
			log.debug("[listarPersonaes] entidades:"+entidades);
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
	/**
	 * Se encarga de buscar la informacion de los clientes segun el filtro
	 * seleccionado
	 * 
	 * @param model
	 * @param tiendaBean
	 * @return
	 */
	@RequestMapping(value = "/contrato/clientes/q", method = RequestMethod.POST)
	public String listarPersonasFiltrados(Model model, Filtro filtro, String path,  PageableSG pageable, HttpServletRequest request) {
		
		try{
			log.debug("[listarPersonasFiltrados] Inicio");
			log.debug("[listarPersonasFiltrados] path:"+path);
			path = "cliente/contrato/con_per_listado";
			log.debug("[listarPersonasFiltrados] Operacion:"+filtro.getStrOperacion());
			this.listarPersonas(model, filtro, pageable, this.urlPaginadoCliente);
			this.cargarListaOperacionCliente(model);
			model.addAttribute("filtro", filtro);
			request.getSession().setAttribute("sessionFiltroClienteCriterio", filtro);
		}catch(Exception e){
			log.debug("[listarPersonasFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
		}
		log.debug("[listarPersonasFiltrados] Fin");
		return path;
	}
	/**
	 * Se encarga de direccionar a la pantalla de contrato
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	/*@RequestMapping(value = "/contrato/clientes/seleccionar", method = RequestMethod.POST)
	public String asignarCliente(Model model, Filtro filtro, String path, HttpServletRequest request) {
		TblPersona entidad 					= null;
		TblContrato contrato				= null;
		BeanRequest beanRequest				= null;
		try{
			log.debug("[asignarCliente] Inicio");
			log.debug("[asignarCliente] path:"+path);
			entidad = personaDao.findOne(filtro.getCodigo());
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			contrato = beanRequest.getContrato();
			contrato.setTblPersona(entidad);
			model.addAttribute("contrato", contrato);
			request.getSession().setAttribute("beanRequest", beanRequest);
			log.debug("[asignarCliente] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return "cliente/contrato/con_nuevo";
	}*/
	/**
	 * Se encarga de direccionar a la pantalla de contrato
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/contrato/clientes/seleccionar/{id}", method = RequestMethod.GET)
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
			if (contrato.getNumero()==null || contrato.getNumero().equals("")){
				path = "cliente/contrato/con_nuevo";
			}else{
				path = "cliente/contrato/con_edicion";
			}

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
	@RequestMapping(value = "/contrato/regresar", method = RequestMethod.POST)
	public String regresarContrato(Model model, Filtro filtro, String path, HttpServletRequest request) {
		//TblContrato contrato				= null;
		BeanRequest beanRequest				= null;
		try{
			log.debug("[regresarContrato] Inicio");
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			filtro = (Filtro)request.getSession().getAttribute("sessionFiltroClienteCriterio");
			log.debug("[regresarContrato] oPERACION:"+filtro.getStrOperacion());
			if (filtro.getStrOperacion().equals(Constantes.OPERACION_NUEVO)){
				path = "cliente/contrato/con_nuevo";
			}else{
				path = "cliente/contrato/con_edicion";
			}
			//contrato = beanRequest.getContrato();
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);
			log.debug("[regresarContrato] Fin");
		}catch(Exception e){
			log.debug("[regresarContrato] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}

		return path;
	}
	@RequestMapping(value = "/contrato/local/regresar", method = RequestMethod.POST)
	public String regresarContratoDeLocal(Model model, Filtro filtro, String path, HttpServletRequest request) {
		//TblContrato contrato				= null;
		BeanRequest beanRequest				= null;
		try{
			log.debug("[regresarContratoDeLocal] Inicio");
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			path = "cliente/contrato/con_nuevo";
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);
			log.debug("[regresarContratoDeLocal] Fin");
		}catch(Exception e){
			log.debug("[regresarContratoDeLocal] Error:"+e.getMessage());
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

	@RequestMapping(value = "/contrato/servicio", method = RequestMethod.POST)
	public String adicionarServicio(Model model, TblContratoServicio contratoServicio, TblContrato contrato, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;

		try{
			log.debug("[adicionarServicio] Inicio");
			path = "cliente/contrato/con_nuevo";
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			
			if (contratoServicio.getMonto()== null || contratoServicio.getMonto().doubleValue() <=0){
				model.addAttribute("resultadoServicio","Debe ingresar el monto del servicio!");
			}else{
				contratoServicio.setTotalAcumulado(contratoServicio.getMonto());
				contratoServicio.setTotalSaldo(contratoServicio.getMonto());
				contratoServicio.setTotalCobrado(new BigDecimal("0"));
				contratoServicio.setTblTipoServicio(tipoServicioDao.findOne(contratoServicio.getTblTipoServicio().getCodigoTipoServicio()));
				log.debug("[adicionarServicio] Doc:"+contratoServicio.getTipoDocumentoServicio());
				log.debug("[adicionarServicio] Moneda:"+contratoServicio.getTipoMonedaServicio());
				
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

			log.debug("[adicionarServicio] Fin");
		}catch(Exception e){
			log.debug("[adicionarServicio] Error:"+e.getMessage());
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
	 * Se encarga de adicionar los primeros cobros
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/contrato/cobro", method = RequestMethod.POST)
	public String adicionarPrimerosCobros(Model model, TblContratoPrimerCobro primerCobro, TblContrato contrato, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		try{
			log.debug("[adicionarPrimerosCobros] Inicio:"+primerCobro.getUsuarioCreacion());
			primerCobro.setTblTipoServicio(tipoServicioDao.findOne(primerCobro.getUsuarioCreacion()));
			path = "cliente/contrato/con_nuevo";
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			if (beanRequest.getListaPrimerCobro() == null) {
				beanRequest.setListaPrimerCobro(new ArrayList<TblContratoPrimerCobro>());

				if (!validarExistenciaCobro(beanRequest.getListaPrimerCobro(),primerCobro)){
					beanRequest.getListaPrimerCobro().add(primerCobro);
				}else{
					model.addAttribute("resultadoCobro","El Cobro ya existe!");
				}
			}else{
				if (!validarExistenciaCobro(beanRequest.getListaPrimerCobro(),primerCobro)){
					beanRequest.getListaPrimerCobro().add(primerCobro);
				}else{
					model.addAttribute("resultadoCobro","El Cobro ya existe!");
				}
			}
			//model.addAttribute("listaPrimerosCobros", beanRequest.getListaPrimerCobro());
			beanRequest.setContrato(contrato);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);
			log.debug("[adicionarPrimerosCobros] Fin");
		}catch(Exception e){
			log.debug("[adicionarPrimerosCobros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			beanRequest = null;
		}

		return path;
	}
	/**
	 * Se encarga de adicionar los primeros cobros
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/contrato/cobro/edicion", method = RequestMethod.POST)
	public String adicionarPrimerosCobrosEdicion(Model model, TblContratoPrimerCobro primerCobro, TblContrato contrato, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		try{
			log.debug("[adicionarPrimerosCobros] Inicio:"+primerCobro.getUsuarioCreacion());
			primerCobro.setTblTipoServicio(tipoServicioDao.findOne(primerCobro.getUsuarioCreacion()));
			path = "cliente/contrato/con_edicion";
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			if (beanRequest.getListaPrimerCobro() == null) {
				beanRequest.setListaPrimerCobro(new ArrayList<TblContratoPrimerCobro>());

				if (!validarExistenciaCobro(beanRequest.getListaPrimerCobro(),primerCobro)){
					beanRequest.getListaPrimerCobro().add(primerCobro);
				}else{
					model.addAttribute("resultadoCobro","El Cobro ya existe!");
				}
			}else{
				if (!validarExistenciaCobro(beanRequest.getListaPrimerCobro(),primerCobro)){
					beanRequest.getListaPrimerCobro().add(primerCobro);
				}else{
					model.addAttribute("resultadoCobro","El Cobro ya existe!");
				}
			}
			//model.addAttribute("listaPrimerosCobros", beanRequest.getListaPrimerCobro());
			beanRequest.setContrato(contrato);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);
			log.debug("[adicionarPrimerosCobros] Fin");
		}catch(Exception e){
			log.debug("[adicionarPrimerosCobros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			beanRequest = null;
		}

		return path;
	}
	
	private boolean validarExistenciaCobro(List<TblContratoPrimerCobro> lista, TblContratoPrimerCobro bean){
		boolean resultado = false;
		for(TblContratoPrimerCobro elemento:lista){
			if (elemento.getTblTipoServicio().getNombre().equals(bean.getTblTipoServicio().getNombre())){
				resultado = true;
				break;
			}
		}
		return resultado;
	}
	private boolean removerExistenciaCobro(List<TblContratoPrimerCobro> lista, String nombre){
		boolean resultado = false;
		for(int i=0; i<lista.size(); i++){
			TblContratoPrimerCobro elemento = lista.get(i);
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
	@RequestMapping(value = "/contrato/servicio/eliminar/{id}", method = RequestMethod.GET)
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
		return "cliente/contrato/con_nuevo";
	}
	/**
	 * Se encarga de eliminar el cobro
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/contrato/cobro/eliminar/{id}", method = RequestMethod.GET)
	public String eliminarCobroGet(@PathVariable String id, Model model, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		try{

			//Se mantiene en Session el contrato hasta retornar
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			if (removerExistenciaCobro(beanRequest.getListaPrimerCobro(),id)){
				model.addAttribute("resultadoCobro","El primer cobro seleccionado se removio satisfactoriamente!");
			}
			this.cargarListasRequestBeanContrato(model, beanRequest);
			
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			beanRequest = null;
		}
		return "cliente/contrato/con_nuevo";
	}


	/**
	 * Se encarga de guardar la informacion 
	 * 
	 * @param 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/contrato/editar/guardar", method = RequestMethod.POST)
	public String actualizarEntidad(Model model, TblContrato entidad, HttpServletRequest request, String path) {
		path 											= "cliente/contrato/con_listado";
		BeanRequest beanRequest							= null;
		TblContrato contrato							= null;
		TblContratoCliente cliente						= null;
		List<TblContrato> entidades 					= new ArrayList<TblContrato>();
		TblContrato filtro								= null;
		//List<TblObservacion> listaObservacion			= null;
		try{
			log.debug("[actualizarEntidad] Inicio" );
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			//if (this.validarNegocio(model, entidad, request)){
				log.debug("[actualizarEntidad] Pre Guardar..." );
				contrato = contratoDao.findOne(entidad.getCodigoContrato());
				this.preEditar(entidad, request);
				/*contrato.setFechaContrato(entidad.getFechaContrato());
				contrato.setFechaInicio(entidad.getFechaInicio());
				contrato.setFechaFin(entidad.getFechaFin());
				contrato.setMontoAlquiler(entidad.getMontoAlquiler());
				contrato.setTipoMonedaAlquiler(entidad.getTipoMonedaAlquiler());
				contrato.setTipoCobro(entidad.getTipoCobro());
				contrato.setTipoGarantia(entidad.getTipoGarantia());
				contrato.setMontoGarantia(entidad.getMontoGarantia());
				contrato.setTipoMonedaGarantia(entidad.getTipoMonedaGarantia());
				contrato.setTipoDocumentoGarantia(entidad.getTipoDocumentoGarantia());
				contrato.setInformacionAdicional(entidad.getInformacionAdicional());*/
				contrato.setFechaModificacion(entidad.getFechaModificacion());
				contrato.setUsuarioModificacion(entidad.getUsuarioModificacion());
				contrato.setIpModificacion(entidad.getIpModificacion());


				if (contrato.getTblPersona().getCodigoPersona() != entidad.getTblPersona().getCodigoPersona()){
					contrato.setTblPersona(entidad.getTblPersona());

					//ContratoCliente
					cliente = new TblContratoCliente();
					cliente.setTblContrato(contrato);
					cliente.setCodigoPersona(contrato.getTblPersona().getCodigoPersona());
					cliente.setFechaCreacion(new Date(System.currentTimeMillis()));
					cliente.setIpCreacion(request.getRemoteAddr());
					cliente.setUsuarioCreacion(contrato.getUsuarioModificacion());
					cliente.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
					cliente.setFechaInicio(entidad.getFechaInicio());
					cliente.setFechaFin(entidad.getFechaFin());
					contratoClienteDao.save(cliente);
				}
				contrato.setTblAdelantos(entidad.getTblAdelantos());
				contrato.setTblContratoPrimerCobros(entidad.getTblContratoPrimerCobros());
				//contrato.setTblContratoPrimerCobros(beanRequest.getListaPrimerCobro());
				contrato.setTblContratoServicios(entidad.getTblContratoServicios());
				//contrato.setTblCxcDocumentos(entidad.getTblCxcDocumentos());
				contrato.setTblTienda(entidad.getTblTienda());

				super.editar(contrato, model);
				log.debug("[actualizarEntidad] Guardado..." );
				//registrando los primeros cobros
				/*for(TblContratoPrimerCobro cobro: beanRequest.getListaPrimerCobro()){
					cobro.setTblContrato(contrato);
					contratoPrimerCobroDao.save(cobro);
				}*/
				//registro de las observaciones
				if (beanRequest.getListaObservacion()!=null && beanRequest.getListaObservacion().size()>0){
					for(TblObservacion observacion:beanRequest.getListaObservacion()){
						observacion.setCodigoContrato(contrato.getCodigoContrato());
						observacionDao.save(observacion);
					}
				}

				//Listar el contrato
				entidades.add(contrato);
				filtro = new TblContrato();
				filtro.setTblPersona(new TblPersona());
				filtro.setTblTienda(new TblTienda());
				filtro.getTblTienda().setTblEdificio(new TblEdificio());
				model.addAttribute("filtro", filtro);
				//listaUtil.cargarDatosModel(model, Constantes.MAP_LISTA_EDIFICIO);
				//model.addAttribute("registros", entidades);
				this.listarContratos(model, contrato, new PageableSG(), this.urlPaginado,request);


			/*}else{
				path = "cliente/contrato/con_edicion";
				beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
				beanRequest.setContrato(entidad);	
				this.cargarListasRequestBeanContrato(model, beanRequest);
				
				this.cargarListaOperacionContrato(model);

				request.getSession().setAttribute("beanRequest", beanRequest);
			}*/

			log.debug("[actualizarEntidad] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			beanRequest		= null;
			contrato		= null;
			cliente			= null;
			entidades 		= null;
			filtro			= null;
		}
		return path;

	}
	@Override
	public void preEditar(TblContrato entidad, HttpServletRequest request) {
		BeanRequest beanRequest	= null;
		//List<TblContratoPrimerCobro> listaPC	= null;
		List<TblObservacion> listaObs	= null;
		try{
			log.debug("[preEditar] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			//Para lista de primeros cobros
			/*listaPC = beanRequest.getListaPrimerCobro();
			for(TblContratoPrimerCobro pc:listaPC){
				pc.setFechaCreacion(new Date(System.currentTimeMillis()));
				pc.setIpCreacion(request.getRemoteAddr());
				pc.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
				pc.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			}*/
			//Para lista de observaciones
			listaObs = beanRequest.getListaObservacion();
			for(TblObservacion pc:listaObs){
				pc.setFechaCreacion(new Date(System.currentTimeMillis()));
				pc.setIpCreacion(request.getRemoteAddr());
				pc.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
				pc.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			}
			
			log.debug("[preEditar] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			beanRequest = null;
			listaObs	= null;
		}

	}

	/**
	 * Se encarga de la eliminacion logica del Concepto
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "contrato/eliminar/{id}", method = RequestMethod.GET)
	public String eliminarContrato(@PathVariable Integer id, HttpServletRequest request, Model model,  PageableSG pageable) {
		TblContrato contrato		= null;
		String path 				= null;
		TblContrato filtro			= null;
		TblTienda tienda			= null;
		TblSuministro suministro	= null;
		List<TblContrato> lista 	= null;
		PageWrapper<TblContrato> page = null;
		try{
			log.debug("[eliminarContrato] Inicio");
			path = "cliente/contrato/con_listado";
			contrato = contratoDao.findOne(id);
			if (this.validarEstadoContrato(contrato)){
				this.preEditar(contrato, request);
				contrato.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);;
				super.editar(contrato, model);
				//Estado de la tienda
				//Actualizando la tienda a Ocupado
				tienda = tiendaDao.findOne(contrato.getTblTienda().getCodigoTienda());
				suministro = suministroDao.findOne(tienda.getTblSuministro().getCodigoSuministro());
				tienda.setTblSuministro(suministro);
				//tiendaDao.updateEstado(entidad.getTblTienda().getCodigoTienda(), Constantes.ESTADO_TIENDA_OCUPADO);
				preEditarTienda(tienda, request);
				tienda.setEstadoTienda(Constantes.ESTADO_TIENDA_DESOCUPADO);
				tiendaDao.save(tienda);
				model.addAttribute("respuesta", "Eliminación exitosa");
				filtro = new TblContrato();
				filtro.setTblPersona(new TblPersona());
				filtro.setTblTienda(new TblTienda());
				filtro.getTblTienda().setTblEdificio(new TblEdificio());
				model.addAttribute("filtro", filtro);
				this.listarContratos(model, filtro, pageable, this.urlPaginado, request);
				//listaUtil.cargarDatosModel(model, Constantes.MAP_LISTA_EDIFICIO);
			}else{
				model.addAttribute("respuesta", "El estado del contrato no permite ser eliminado, Solo se puede eliminar en estado PENDIENTE");
				path = "cliente/contrato/con_listado";
				
				filtro = (TblContrato)request.getSession().getAttribute("sessionFiltroCriterio");
				model.addAttribute("filtro", filtro);
				lista = (List<TblContrato>)request.getSession().getAttribute("sessionListaContrato");
				model.addAttribute("registros",lista);
				page = (PageWrapper<TblContrato>) request.getSession().getAttribute("PageContrato");
				model.addAttribute("page", page);
				request.getSession().setAttribute("PageableSGContrato", pageable);
				
			}
			

			log.debug("[eliminarContrato] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			contrato 	= null;
		}
		return path;
	}
	
	public boolean validarEstadoContrato(TblContrato contrato){
		boolean resultado = false;
		if (contrato.getEstadoContrato().equals(Constantes.ESTADO_CONTRATO_PENDIENTE)){
			resultado = true;
		}else{
			resultado = false;
		}
		return resultado;
	}
	
	/**
	 * Se encarga de Listar los locales
	 * seleccionado
	 * 
	 * @param model
	 * @param contratoBean
	 * @return
	 */
	@RequestMapping(value = "/contrato/arbitrio/editar", method = RequestMethod.POST)
	public String editarArbitrio(Model model, TblArbitrio arbitrio, TblContrato contrato, String path, HttpServletRequest request) {
		
		BeanRequest beanRequest		= null;
		try{
			log.debug("[editarArbitrio] Inicio");
			log.debug("[editarArbitrio] path:"+path);
			log.debug("[editarArbitrio] codigoArbitrio:"+arbitrio.getCodigoArbitrio());
			path = "cliente/contrato/con_nuevo";
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			beanRequest.setContrato(contrato);
			
			this.cargarListasRequestBeanContrato(model, beanRequest);
			
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);
			
			log.debug("[editarArbitrio] Fin");

		}catch(Exception e){
			log.debug("[editarArbitrio] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			
		}
		log.debug("[editarArbitrio] Fin");
		return path;
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
	 * Carga las listas en la sesion para las operaciones del contrato
	 */
	
	private void cargarListaOperacionCliente(Model model){
		
		listaUtil.cargarDatosModel(model, Constantes.MAP_TIPO_PERSONA);
		listaUtil.cargarDatosModel(model, Constantes.MAP_ESTADO_CIVIL);
		listaUtil.cargarDatosModel(model, Constantes.MAP_SI_NO);
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
		
	}
	
	/**
	 * Se encarga de adicionar los primeros cobros
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/contrato/observacion", method = RequestMethod.POST)
	public String adicionarObservacion(Model model, TblObservacion observacionBean, TblContrato contrato, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		try{
			log.debug("[adicionarObservacion] Inicio:");
			path = "cliente/contrato/con_nuevo";
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
			log.debug("[adicionarObservacion] Fin");
		}catch(Exception e){
			log.debug("[adicionarObservacion] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			beanRequest = null;
		}

		return path;
	}
	/**
	 * Se encarga de adicionar los primeros cobros
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/contrato/observacion/edicion", method = RequestMethod.POST)
	public String adicionarObservacionEdicion(Model model, TblObservacion observacionBean, TblContrato contrato, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		try{
			log.debug("[adicionarObservacionEdicion] Inicio:");
			path = "cliente/contrato/con_edicion";
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			contrato = beanRequest.getContrato();
			
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
			log.debug("[adicionarObservacionEdicion] Fin");
		}catch(Exception e){
			log.debug("[adicionarObservacionEdicion] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			beanRequest = null;
		}

		return path;
	}
	/**
	 * Se encarga de Listar los clientes
	 * seleccionado
	 * 
	 * @param model
	 * @param contratoBean
	 * @return
	 */
	@RequestMapping(value = "/contrato/cliente/historial", method = RequestMethod.POST)
	public String listarClientesHistorial(Model model, TblContrato contrato, String path, HttpServletRequest request) {
		path 						= "cliente/contrato/con_per_historial";
		List<TblPersona> listaHistorial = null;
		Filtro filtro 					= null;
		try{
			log.debug("[listarClientesHistorial] Inicio");
			
			listaHistorial = personaDao.listarPersonasxContrato(contrato.getCodigoContrato());
					
			model.addAttribute("registros", listaHistorial);
			filtro = new Filtro();
			
			filtro.setStrOperacion(Constantes.OPERACION_EDITAR);
			
			model.addAttribute("filtro", filtro);
			request.getSession().setAttribute("sessionFiltroClienteCriterio",filtro);

		}catch(Exception e){
			log.debug("[listarClientesHistorial] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			listaHistorial		= null;
		}
		log.debug("[listarClientesHistorial] Fin");
		return path;
	}
	
	/*public void obtenerFechaFinContrato(TblContrato contrato){
		List<TblParametro> listaParametro;
		
		if (contrato.getFechaInicio() == null){
			contrato.setFechaFin(UtilSGT.addDays(new Date(), 30));
			contrato.setFechaFin(UtilSGT.getLastDays(contrato.getFechaFin()));
		}else{
			listaParametro = parametroDao.buscarOneByNombre(Constantes.PAR_FIN_CONTRATO);
			if(listaParametro!=null && listaParametro.size()>0){
				contrato.setFechaFin(UtilSGT.addDays(contrato.getFechaInicio(),new Integer(listaParametro.get(0).getDato())));
				contrato.setFechaFin(UtilSGT.getLastDays(contrato.getFechaFin()));
			}else{
				contrato.setFechaFin(UtilSGT.addDays(contrato.getFechaInicio(), 30));
				contrato.setFechaFin(UtilSGT.getLastDays(contrato.getFechaFin()));
			}
		}
		
	}*/
	
	@RequestMapping(value = "/contrato/fecha/actualizar", method = RequestMethod.POST)
	public String actualizarFechaContrato(Model model, TblContrato contrato, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		List<TblParametro> listaParametro;
		
		
		try{
			log.debug("[actualizarFechaContrato] Inicio");
			listaParametro = parametroDao.buscarOneByNombre(Constantes.PAR_FIN_CONTRATO);
			UtilSGT.obtenerFechaFinContrato(contrato, listaParametro);
			//this.obtenerFechaFinContrato(contrato);
			path = "cliente/contrato/con_nuevo";
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			beanRequest.setContrato(contrato);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);
			log.debug("[actualizarFechaContrato] Fin");
		}catch(Exception e){
			log.debug("[actualizarFechaContrato] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			beanRequest = null;
		}

		return path;
	}
	

	/*
	 * Paginado
	 */
	@RequestMapping(value = "/contratos/paginado/{page}/{size}/{operacion}", method = RequestMethod.GET)
	public String paginarEntidad(@PathVariable Integer page, @PathVariable Integer size, @PathVariable String operacion, Model model,  PageableSG pageable, HttpServletRequest request) {
		TblContrato filtro = null;
		String path = null;
		try{
			//log.debug("[traerRegistros] Inicio");
			path = "cliente/contrato/con_listado";
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
			filtro = (TblContrato)request.getSession().getAttribute("sessionFiltroCriterio");
			model.addAttribute("filtro", filtro);
			
			this.listarContratos(model, filtro, pageable, this.urlPaginado, request);
			
		}catch(Exception e){
			//log.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		return path;
	}
	

	/*
	 * Paginado de Tienda
	 */
	@RequestMapping(value = "/contratos/tienda/paginado/{page}/{size}/{operacion}", method = RequestMethod.GET)
	public String paginarEntidadTienda(@PathVariable Integer page, @PathVariable Integer size, @PathVariable String operacion, Model model,  PageableSG pageable, HttpServletRequest request) {
		Filtro filtro 				= null;
		Map<String, Object> campos 	= null;
		String path 				= null;
		TblEdificio edificio 		= null;
		try{
			//log.debug("[traerRegistros] Inicio");
			path = "cliente/contrato/con_tie_listado";
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
			filtro = (Filtro)request.getSession().getAttribute("sessionFiltroTiendaCriterio");
			if (filtro == null){
				filtro = new Filtro();
			}
			edificio = (TblEdificio)request.getSession().getAttribute("edificioRequest");
			filtro.setCodigo(edificio.getCodigoEdificio());
			model.addAttribute("filtro", filtro);
			model.addAttribute("edificio", edificio);
			
			this.listarTiendas(model, filtro, pageable, this.urlPaginadoTienda);
			campos = configurarCamposConsulta();
			model.addAttribute("contenido", campos);
		}catch(Exception e){
			//log.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		return path;
	}

	/*
	 * Paginado de Cliente
	 */
	@RequestMapping(value = "/contratos/cliente/paginado/{page}/{size}/{operacion}", method = RequestMethod.GET)
	public String paginarEntidadCliente(@PathVariable Integer page, @PathVariable Integer size, @PathVariable String operacion, Model model,  PageableSG pageable, HttpServletRequest request) {
		Filtro filtro = null;
		Map<String, Object> campos = null;
		String path = null;
		try{
			//log.debug("[traerRegistros] Inicio");
			path = "cliente/contrato/con_per_listado";
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
			//log.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		return path;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/contratos/regresar", method = RequestMethod.GET)
	public String regresar(Model model, String path, HttpServletRequest request) {
		TblContrato filtro = null;
		List<TblContrato> lista = null;
		PageWrapper<TblContrato> page = null;
		try{
			log.debug("[regresar] Inicio");
			path = "cliente/contrato/con_listado";
			
			filtro = (TblContrato)request.getSession().getAttribute("sessionFiltroCriterio");
			model.addAttribute("filtro", filtro);
			lista = (List<TblContrato>)request.getSession().getAttribute("sessionListaContrato");
			model.addAttribute("registros",lista);
			page = (PageWrapper<TblContrato>) request.getSession().getAttribute("PageContrato");
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
