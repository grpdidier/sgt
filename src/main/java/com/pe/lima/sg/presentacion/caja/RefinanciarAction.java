package com.pe.lima.sg.presentacion.caja;

//import static com.pe.lima.sg.dao.caja.CxCDocumentoSpecifications.conAnioActual;
import static com.pe.lima.sg.dao.caja.CxCDocumentoSpecifications.conCodigoContrato;
import static com.pe.lima.sg.dao.caja.CxCDocumentoSpecifications.conSaldoPositivo;
import static com.pe.lima.sg.dao.caja.CxCDocumentoSpecifications.conTipoReferencia;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conEdificio;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conEstado;
//import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conEstadoContrato;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conListaEstadoContrato;
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

import com.pe.lima.sg.bean.caja.CobroArbitrioBean;
import com.pe.lima.sg.bean.caja.CobroBean;
import com.pe.lima.sg.bean.caja.CobroGarantia;
import com.pe.lima.sg.bean.caja.CobroGeneralBean;
import com.pe.lima.sg.bean.caja.CobroLuzBean;
import com.pe.lima.sg.bean.caja.CobroPrimerCobro;
import com.pe.lima.sg.bean.caja.CobroServicioBean;
import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.dao.caja.ICxCDocumentoDAO;
import com.pe.lima.sg.dao.cliente.IAdelantoDAO;
import com.pe.lima.sg.dao.cliente.IArbitrioDAO;
import com.pe.lima.sg.dao.cliente.IContratoClienteDAO;
import com.pe.lima.sg.dao.cliente.IContratoDAO;
import com.pe.lima.sg.dao.cliente.IContratoPrimerCobroDAO;
import com.pe.lima.sg.dao.cliente.IContratoServicioDAO;
import com.pe.lima.sg.dao.cliente.ILuzxTiendaDAO;
import com.pe.lima.sg.dao.cliente.IObservacionDAO;
import com.pe.lima.sg.dao.mantenimiento.ITipoCambioDAO;
import com.pe.lima.sg.entity.caja.TblCobro;
//import com.pe.lima.sg.entity.caja.TblCxcBitacora;
import com.pe.lima.sg.entity.caja.TblCxcDocumento;
import com.pe.lima.sg.entity.cliente.TblAdelanto;
import com.pe.lima.sg.entity.cliente.TblArbitrio;
import com.pe.lima.sg.entity.cliente.TblContrato;
import com.pe.lima.sg.entity.cliente.TblContratoCliente;
import com.pe.lima.sg.entity.cliente.TblContratoPrimerCobro;
import com.pe.lima.sg.entity.cliente.TblContratoServicio;
import com.pe.lima.sg.entity.cliente.TblLuzxtienda;
import com.pe.lima.sg.entity.cliente.TblObservacion;
import com.pe.lima.sg.entity.mantenimiento.TblEdificio;
import com.pe.lima.sg.entity.mantenimiento.TblPersona;
import com.pe.lima.sg.entity.mantenimiento.TblTienda;
import com.pe.lima.sg.entity.mantenimiento.TblTipoCambio;
import com.pe.lima.sg.presentacion.BaseOperacionPresentacion;
import com.pe.lima.sg.presentacion.BeanRequest;
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
public class RefinanciarAction extends BaseOperacionPresentacion<TblCobro> {

	
	@Autowired
	private IContratoDAO contratoDao;

	@Autowired
	private ICxCDocumentoDAO cxcDocumentoDao;

	@Autowired
	private IContratoClienteDAO contratoClienteDao;

	@Autowired
	private IContratoServicioDAO contratoServicioDao;

	@Autowired
	private IContratoPrimerCobroDAO contratoPrimerCobroDao;
	

	@Autowired
	private IArbitrioDAO arbitrioDao;
	
	@Autowired
	private ILuzxTiendaDAO luzxTiendaDao;
	
	@Autowired
	private IObservacionDAO observacionDao;	

	@Autowired
	private ITipoCambioDAO tipoCambioDao;
	
	@Autowired
	private ListaUtilAction listaUtil;

	@Autowired
	private IAdelantoDAO adelantoDao;
	
	private String urlPaginado = "/refinanciar/paginado/"; 

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
	@RequestMapping(value = "/refinanciar", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path, PageableSG pageable, HttpServletRequest request) {
		TblContrato filtro = null;
		try{
			log.debug("[traerRegistros] Inicio");
			path = "caja/refinanciar/ref_listado";
			filtro = new TblContrato();
			filtro.setTblPersona(new TblPersona());
			filtro.setTblTienda(new TblTienda());
			filtro.getTblTienda().setTblEdificio(new TblEdificio());
			model.addAttribute("filtro", filtro);
			
			model.addAttribute("registros",null);
			model.addAttribute("page", null);
			request.getSession().setAttribute("sessionFiltroCriterioRefinanciar", filtro);
			request.getSession().setAttribute("sessionListaContratoRefinanciar", null);
			request.getSession().setAttribute("PageContratoRefinanciar", null);
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
	@RequestMapping(value = "/refinanciar/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, TblContrato filtro, String path,  PageableSG pageable,HttpServletRequest request) {
		//Map<String, Object> campos = null;
		path = "caja/refinanciar/ref_listado";
		try{
			log.debug("[traerRegistrosFiltrados] Inicio");
			this.listarContratos(model, filtro, pageable, this.urlPaginado,request);
			model.addAttribute("filtro", filtro);
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
		List<TblContrato> entidades = new ArrayList<TblContrato>();
		List<String> listaEstado = new ArrayList<String>();
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "numero"));
		try{
			listaEstado.add(Constantes.ESTADO_CONTRATO_PENDIENTE);
			listaEstado.add(Constantes.ESTADO_CONTRATO_VIGENTE);
			listaEstado.add(Constantes.ESTADO_CONTRATO_RENOVADO);
			Specification<TblContrato> filtro = Specifications.where(conNombre(tblContrato.getTblPersona().getNombre()))
					.and(conPaterno(tblContrato.getTblPersona().getPaterno()))
					.and(conMaterno(tblContrato.getTblPersona().getMaterno()))
					.and(conEdificio(tblContrato.getTblTienda().getTblEdificio().getCodigoEdificio()))
					.and(conTienda(tblContrato.getTblTienda().getNumero()))
					.and(conRuc(tblContrato.getTblPersona().getNumeroRuc()))
					.and(conRazonSocial(tblContrato.getTblPersona().getRazonSocial()))
					.and(conEstado(Constantes.ESTADO_REGISTRO_ACTIVO))
					.and(conListaEstadoContrato(listaEstado));
					//.and(conEstadoContrato(Constantes.ESTADO_CONTRATO_VIGENTE));
			//entidades = contratoDao.findAll(filtro,sort);
			pageable.setSort(sort);
			Page<TblContrato> entidadPage = contratoDao.findAll(filtro, pageable);
			PageWrapper<TblContrato> page = new PageWrapper<TblContrato>(entidadPage, url, pageable);
			model.addAttribute("registros", page.getContent());
			model.addAttribute("page", page);
			request.getSession().setAttribute("sessionFiltroCriterioRefinanciar", tblContrato);
			request.getSession().setAttribute("sessionListaContratoRefinanciar", page.getContent());
			request.getSession().setAttribute("PageContratoRefinanciar", page);
			log.debug("[listarContratos] entidades:"+entidades);
			//model.addAttribute("registros", entidades);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades = null;
		}
	}

	@Override
	public TblCobro getNuevaEntidad() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Se encarga de direccionar a la pantalla de edicion del Contrato
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/refinanciar/editar/{id}", method = RequestMethod.GET)
	public String editarContrato(@PathVariable Integer id, Model model, TblContrato filtro, HttpServletRequest request) {
		String path 							= "";
		try{
			
			path = "caja/refinanciar/ref_edicion";
			this.mEditarContrato(id, model, request);
			

		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
	}

	
	@RequestMapping(value = "/refinanciar/editar/alquiler/monto/{id}", method = RequestMethod.GET)
	public String editarMontoAlquilerContrato(@PathVariable String id, Model model, TblContrato filtro, HttpServletRequest request) {
		String path 							= "";
		BeanRequest beanRequest					= null;
		CobroGeneralBean cobroGeneralBean		= null;
		try{
			path = "caja/refinanciar/ref_edicion";
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			
			this.asignarMontoAlquiler(beanRequest, new Integer(id));
			cobroGeneralBean = beanRequest.getCobroGeneralBean();
			this.mSetBeanCobro(cobroGeneralBean, beanRequest);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);
			
			request.getSession().setAttribute("beanRequest", beanRequest);
			

		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
	}
	
	@RequestMapping(value = "/refinanciar/editar/alquiler/cancelar", method = RequestMethod.GET)
	public String cancelarMontoAlquilerContrato( Model model, HttpServletRequest request) {
		String path 							= "";
		BeanRequest beanRequest					= null;
		CobroGeneralBean cobroGeneralBean		= null;
		try{
			path = "caja/refinanciar/ref_edicion";
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			beanRequest.setCobroAlquiler(new CobroBean());
			cobroGeneralBean = beanRequest.getCobroGeneralBean();
			this.mSetBeanCobro(cobroGeneralBean, beanRequest);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);
			
			request.getSession().setAttribute("beanRequest", beanRequest);
			

		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
	}
	/*
	 * Preguardar: Campos de auditoria
	 */
	public void preEditarDocumento(TblCxcDocumento entidad, HttpServletRequest request) {
		try{
			log.debug("[alquiler] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			log.debug("[alquiler] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@RequestMapping(value = "/refinanciar/editar/alquiler/grabar", method = RequestMethod.POST)
	public String refinanciarmontoAlquilerContrato(Model model, CobroGeneralBean cobroGeneralBean, String path,  PageableSG pageable,HttpServletRequest request) {
		path = "caja/refinanciar/ref_edicion";
		TblCxcDocumento alquiler 				= null;
		BeanRequest beanRequest					= null;
		List<TblCxcDocumento> listaCxcAlquiler	= null;
		try{
			log.debug("[refinanciarmontoAlquilerContrato] Inicio");
			alquiler = cxcDocumentoDao.findOne(cobroGeneralBean.getCobroAlquiler().getCodigoCxCDocumento());
			alquiler.setMontoContrato(cobroGeneralBean.getCobroAlquiler().getNuevoMonto());
			alquiler.setSaldo(cobroGeneralBean.getCobroAlquiler().getNuevoSaldo());
			this.preEditarDocumento(alquiler, request);
			cxcDocumentoDao.save(alquiler);
			model.addAttribute("respuesta", "Se ha refinanciado los montos del alquiler");
			//listar nuevamente
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			listaCxcAlquiler= this.mListarAlquiler(null, null, beanRequest.getContrato().getCodigoContrato());
			beanRequest.setListaCxcAlquiler(listaCxcAlquiler);	
			
			beanRequest.setCobroAlquiler(new CobroBean());
			cobroGeneralBean = beanRequest.getCobroGeneralBean();
			this.mSetBeanCobro(cobroGeneralBean, beanRequest);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);
			
			request.getSession().setAttribute("beanRequest", beanRequest);
			//limpiar
			log.debug("[refinanciarmontoAlquilerContrato] Fin");
		}catch(Exception e){
			log.debug("[refinanciarmontoAlquilerContrato] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			//campos		= null;
		}
		log.debug("[refinanciarmontoAlquilerContrato] Fin");
		return path;
	}
	
	public void asignarMontoAlquiler(BeanRequest beanRequest, Integer indice){
		List<TblCxcDocumento> listaAlquiler		= null;
		TblCxcDocumento documento				= null;
		CobroBean cobroAlquiler					= null;
		listaAlquiler = beanRequest.getListaCxcAlquiler();
		documento = listaAlquiler.get(indice);
		cobroAlquiler = beanRequest.getCobroAlquiler();
		
		cobroAlquiler.setMonto(documento.getMontoContrato());
		cobroAlquiler.setSaldo(documento.getSaldo());
		cobroAlquiler.setNuevoMonto(documento.getMontoContrato());
		cobroAlquiler.setNuevoSaldo(documento.getSaldo());
		cobroAlquiler.setFechaCobro(documento.getFechaFin());
		cobroAlquiler.setCodigoCxCDocumento(documento.getCodigoCxcDoc());
		
	}
	public void asignarMontoServicio(BeanRequest beanRequest, Integer indice){
		List<TblCxcDocumento> listaServicio		= null;
		TblCxcDocumento documento				= null;
		CobroServicioBean cobroServicio			= null;
		listaServicio = beanRequest.getListaCxcServicio();
		documento = listaServicio.get(indice);
		cobroServicio = beanRequest.getCobroServicio();
		
		cobroServicio.setMonto(documento.getMontoContrato());
		cobroServicio.setSaldo(documento.getSaldo());
		cobroServicio.setNuevoMonto(documento.getMontoContrato());
		cobroServicio.setNuevoSaldo(documento.getSaldo());
		cobroServicio.setFechaCobroServicio(documento.getFechaFin());
		cobroServicio.setCodigoCxCDocumento(documento.getCodigoCxcDoc());
		
	}
	public void asignarMontoLuz(BeanRequest beanRequest, Integer indice){
		List<TblCxcDocumento> listaLuz			= null;
		TblCxcDocumento documento				= null;
		CobroLuzBean cobroLuz					= null;
		listaLuz = beanRequest.getListaCxcLuz();
		documento = listaLuz.get(indice);
		cobroLuz = beanRequest.getCobroLuz();
		
		cobroLuz.setMonto(documento.getMontoContrato());
		cobroLuz.setSaldo(documento.getSaldo());
		cobroLuz.setNuevoMonto(documento.getMontoContrato());
		cobroLuz.setNuevoSaldo(documento.getSaldo());
		cobroLuz.setFechaCobroLuz(documento.getFechaFin());
		cobroLuz.setCodigoCxCDocumento(documento.getCodigoCxcDoc());
		
	}
	
	private void mEditarContrato(Integer id, Model model, HttpServletRequest request) throws Exception{
		TblContrato contrato 					= null;
		BeanRequest beanRequest					= null;
		List<TblContratoServicio> listaServicio	= null;
		List<TblContratoCliente> listaCliente	= null;
		List<TblContratoPrimerCobro> listaCobro	= null;
		List<TblArbitrio> listaArbitrio			= null;
		List<TblLuzxtienda> listaLuzxtienda		= null;
		List<TblObservacion> listaObservacion	= null;
		List<TblCxcDocumento> listaCxcAlquiler	= null;
		List<TblCxcDocumento> listaCxcServicio	= null;
		List<TblCxcDocumento> listaCxcLuz		= null;
		List<TblArbitrio> listaCxcArbitrio		= null;
		List<TblCxcDocumento> listaCxcPrimerCobro = null;
		List<TblCxcDocumento> listaCxcGarantia	= null;
		//Specification<TblCxcDocumento> criterio	= null;
		//String path 							= "";
		List<TblTipoCambio> listaTipoCambio		= null;
		CobroBean cobroAlquiler					= null;
		CobroPrimerCobro cobroPrimerCobro		= null;
		CobroGarantia	cobroGarantia			= null;
		CobroServicioBean cobroServicio			= null;
		CobroLuzBean cobroLuz					= null;
		CobroArbitrioBean cobroArbitrio			= null;
		Map<String, Object> mapServicio	 		= null;
		//TblContratoServicio servicioTipo		= new TblContratoServicio();
		//Sort sort = new Sort(new Sort.Order(Direction.DESC, "codigoCxcDoc"));
		List<TblAdelanto> listaAdelanto			= null;
		Map<String, Object> mapPrimerosCobros	= null;
		CobroGeneralBean cobroGeneralBean 		= new CobroGeneralBean(); /*Contendrá todos los beans para el registro el cobro*/
		try{
			contrato = contratoDao.findOne(id);
			//Tipo de Cambio
			listaTipoCambio = tipoCambioDao.buscarOneByFecha(UtilSGT.getDatetoString(UtilSGT.getFecha("dd/MM/YYYY")));
			
			//servicioTipo.setTblTipoServicio(new TblTipoServicio());
			//CxC Alquiler
			cobroAlquiler = new CobroBean();
			listaCxcAlquiler= this.mListarAlquiler(cobroAlquiler, listaTipoCambio, contrato.getCodigoContrato());
			
			//CxC Servicio
			cobroServicio  = new CobroServicioBean();
			listaCxcServicio= this.mListarServicio(cobroServicio, listaTipoCambio, contrato.getCodigoContrato());
			
			//CxC Luz
			cobroLuz  = new CobroLuzBean();
			listaCxcLuz= this.mListarLuz(cobroLuz, listaTipoCambio, contrato.getCodigoContrato());
			
			//CxC Arbitrio
			/*cobroArbitrio  = new CobroArbitrioBean();
			listaCxcArbitrio= this.mListarArbitrio(cobroArbitrio, listaTipoCambio, contrato.getCodigoContrato());*/
			
			
			
			
			//Lista los datos del contrato
			listaServicio = contratoServicioDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			if (listaServicio != null && listaServicio.size() >0){
				mapServicio = new LinkedHashMap<String, Object>();
				for(TblContratoServicio servicio : listaServicio){
					mapServicio.put(servicio.getTblTipoServicio().getDescripcion(), servicio.getTblTipoServicio().getCodigoTipoServicio());
					
				}
			}
			listaCliente = contratoClienteDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			listaCobro = contratoPrimerCobroDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			listaArbitrio = arbitrioDao.listarAllActivosxContrato(contrato.getCodigoContrato());
			listaLuzxtienda = luzxTiendaDao.listarLuzTiendaxContrato(contrato.getCodigoContrato());
			listaObservacion = observacionDao.listarObservacionxContrato(contrato.getCodigoContrato());
			beanRequest = new BeanRequest();
			
			beanRequest.setListaCxcAlquiler(listaCxcAlquiler);	
			beanRequest.setListaCxcPrimerCobro(listaCxcPrimerCobro);
			beanRequest.setListaCxcGarantia(listaCxcGarantia);
			beanRequest.setMapPrimerosCobros(mapPrimerosCobros);
			
			beanRequest.setListaCxcServicio(listaCxcServicio);
			beanRequest.setListaCxcLuz(listaCxcLuz);
			beanRequest.setListaCxcArbitrio(listaCxcArbitrio);
			
			beanRequest.setContrato(contrato);
			beanRequest.setContratoServicio(new TblContratoServicio());
			beanRequest.setContratoPrimerCobro(new TblContratoPrimerCobro());
			beanRequest.setArbitrio(new TblArbitrio());
			beanRequest.setObservacion(new TblObservacion());
			
			beanRequest.setListaServicio(listaServicio);
			beanRequest.setListaCliente(listaCliente);
			beanRequest.setListaPrimerCobro(listaCobro);
			beanRequest.setListaArbitrio(listaArbitrio);
			beanRequest.setListaLuzxTienda(listaLuzxtienda);
			beanRequest.setListaObservacion(listaObservacion);
			
			beanRequest.setCobroAlquiler(cobroAlquiler);
			beanRequest.setCobroPrimerCobro(cobroPrimerCobro);
			beanRequest.setCobroGarantia(cobroGarantia);
			//beanRequest.setServicioTipo(servicioTipo);
			beanRequest.setMapServicio(mapServicio);
			beanRequest.setCobroServicio(cobroServicio);
			beanRequest.setCobroLuz(cobroLuz);
			beanRequest.setCobroArbitrio(cobroArbitrio);
			//Validacion de adelantos
			listaAdelanto = adelantoDao.listarAllActivosxContrato(id);
			if (listaAdelanto!=null && listaAdelanto.size()>0){
				beanRequest.setFlagAdelanto(Constantes.ESTADO_REGISTRO_ACTIVO);
			}else{
				beanRequest.setFlagAdelanto(Constantes.ESTADO_REGISTRO_INACTIVO);
			}
			
			this.mSetBeanCobro(cobroGeneralBean, beanRequest);
			beanRequest.setCobroGeneralBean(cobroGeneralBean);
			model.addAttribute("cobroGeneralBean", cobroGeneralBean);
			
			this.cargarListasRequestBeanContrato(model, beanRequest);
			
			this.cargarListaOperacionContrato(model);
			
			request.getSession().setAttribute("beanRequest", beanRequest);
			//path = "caja/cobro/cob_edicion";
			

		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			contrato 			= null;
			beanRequest			= null;
			listaServicio		= null;
			listaCliente		= null;
			listaCobro			= null;
			listaArbitrio		= null;
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
		model.addAttribute("cobroGeneralBean", beanRequest.getCobroGeneralBean());
		model.addAttribute("contrato", beanRequest.getContrato());
		model.addAttribute("contrato", beanRequest.getContrato());
		model.addAttribute("flagAdelanto", beanRequest.getFlagAdelanto());
		model.addAttribute("contratoServicio", beanRequest.getContratoServicio());
		model.addAttribute("listaServicio", beanRequest.getListaServicio());
		model.addAttribute("contratoPrimerCobro", beanRequest.getContratoPrimerCobro());
		model.addAttribute("listaPrimerosCobros", beanRequest.getListaPrimerCobro());
		model.addAttribute("mapPrimerosCobros", beanRequest.getMapPrimerosCobros());
		model.addAttribute("arbitrio", beanRequest.getArbitrio());
		model.addAttribute("listaArbitrios", beanRequest.getListaArbitrio());
		model.addAttribute("listaLuzxTienda", beanRequest.getListaLuzxTienda());
		model.addAttribute("luzxTienda", beanRequest.getLuzxTienda());
		model.addAttribute("observacion", beanRequest.getObservacion());
		model.addAttribute("listaObservacion", beanRequest.getListaObservacion());
		model.addAttribute("listaCxcAlquiler", beanRequest.getListaCxcAlquiler());
		model.addAttribute("listaCxcPrimerCobro", beanRequest.getListaCxcPrimerCobro());
		model.addAttribute("listaCxcGarantia", beanRequest.getListaCxcGarantia());
		model.addAttribute("listaCxcServicio", beanRequest.getListaCxcServicio());
		model.addAttribute("listaCxcLuz", beanRequest.getListaCxcLuz());
		model.addAttribute("listaCxcArbitrio", beanRequest.getListaCxcArbitrio());
		model.addAttribute("cobroAlquiler", beanRequest.getCobroAlquiler());
		model.addAttribute("cobroPrimerCobro", beanRequest.getCobroPrimerCobro());
		model.addAttribute("cobroGarantia", beanRequest.getCobroGarantia());
		model.addAttribute("historialCobroAlquiler", beanRequest.getHistorialCobroAlquiler());
		model.addAttribute("historialCobroPrimerCobro", beanRequest.getHistorialCobroPrimerCobro());
		model.addAttribute("historialCobroGarantia", beanRequest.getHistorialCobroGarantia());
		model.addAttribute("servicioTipo", beanRequest.getServicioTipo());
		model.addAttribute("mapServicioTipo", beanRequest.getMapServicio());
		model.addAttribute("cobroServicio", beanRequest.getCobroServicio());
		model.addAttribute("historialCobroServicio", beanRequest.getHistorialCobroServicio());
		model.addAttribute("cobroLuz", beanRequest.getCobroLuz());
		model.addAttribute("cobroArbitrio", beanRequest.getCobroArbitrio());
		model.addAttribute("historialLuz", beanRequest.getHistorialCobroLuz());
		model.addAttribute("historialCobroLuz", beanRequest.getHistorialCobroLuz());
		model.addAttribute("historialCobroArbitrio", beanRequest.getHistorialCobroArbitrio());
	}
	
	public void mSetBeanCobro(CobroGeneralBean cobroGeneralBean, BeanRequest beanRequest){
		cobroGeneralBean.setContrato(beanRequest.getContrato());
		cobroGeneralBean.setCobroAlquiler(beanRequest.getCobroAlquiler());
		cobroGeneralBean.setCobroServicio(beanRequest.getCobroServicio());
		cobroGeneralBean.setCobroPrimerCobro(beanRequest.getCobroPrimerCobro());
		cobroGeneralBean.setCobroLuz(beanRequest.getCobroLuz());
		cobroGeneralBean.setIndiceTab(Constantes.REFINANCIAR_TAB_ALQUILER);
	}
	
	/*
	 * Listado de Alquiler
	 */ 
	public List<TblCxcDocumento> mListarAlquiler( CobroBean cobroAlquiler, List<TblTipoCambio> listaTipoCambio, Integer intCodigoContrato){
		Sort sort = new Sort(new Sort.Order(Direction.ASC, "codigoCxcDoc"));
		Specification<TblCxcDocumento> criterio	= null;
		List<TblCxcDocumento> listaCxcAlquiler;
		//conSaldoPositivo
		criterio = Specifications.where(conCodigoContrato(intCodigoContrato))
				  //.and(conSaldoPositivo(new BigDecimal("0")))
				  //.and(conAnioActual(UtilSGT.getAnioMesString(new Date())))
				  .and(conTipoReferencia(Constantes.TIPO_COBRO_ALQUILER));
		listaCxcAlquiler = cxcDocumentoDao.findAll(criterio,sort);
		listaCxcAlquiler = this.validarListaAlquilerServicioLuz(listaCxcAlquiler);
		if (listaTipoCambio!=null && listaTipoCambio.size()>0){
			cobroAlquiler.setTipoCambio(listaTipoCambio.get(0).getValor());
			cobroAlquiler.setFechaCobro(UtilSGT.getDatetoString(UtilSGT.getFecha("dd/MM/YYYY")));
			cobroAlquiler.setTipoMoneda(Constantes.MONEDA_DOLAR);
			cobroAlquiler.setMonto(UtilSGT.montoRedondeado(new BigDecimal("0")));
			cobroAlquiler.setCalculado(UtilSGT.montoRedondeado(new BigDecimal("0")));
		}
		return listaCxcAlquiler;
	}
	private List<TblCxcDocumento> validarListaAlquilerServicioLuz(List<TblCxcDocumento> listaCxcAlquilerServicioLuz){
		List<TblCxcDocumento>  listaNueva = new ArrayList<TblCxcDocumento>();
		if (listaCxcAlquilerServicioLuz != null){
			for(TblCxcDocumento alquiler: listaCxcAlquilerServicioLuz){
				if (alquiler.getSaldo() != null && alquiler.getSaldo().doubleValue() > 0){
					listaNueva.add(alquiler);
				}else{
					if (alquiler.getSaldo() != null &&  alquiler.getSaldo().doubleValue() == 0 && alquiler.getFechaModificacion()!= null && UtilSGT.getDateStringFormat(alquiler.getFechaModificacion()).equals(UtilSGT.getDateStringFormat(new Date()))){
						listaNueva.add(alquiler);
					}
				}
			}
		}
		return listaNueva;
	}
	/*
	 * Listado de Servicio
	 */
	private List<TblCxcDocumento>  mListarServicio(CobroServicioBean cobroServicio, List<TblTipoCambio> listaTipoCambio, Integer intCodigoContrato){
		Sort sort = new Sort(new Sort.Order(Direction.ASC, "codigoCxcDoc"));
		Specification<TblCxcDocumento> criterio	= null;
		List<TblCxcDocumento> listaCxcServicio;
		
		criterio = Specifications.where(conCodigoContrato(intCodigoContrato))
				  //.and(conSaldoPositivo(new BigDecimal("0")))
				  //.and(conAnioActual(UtilSGT.getAnioMesString(new Date())))
				  .and(conTipoReferencia(Constantes.TIPO_COBRO_SERVICIO));
		listaCxcServicio = cxcDocumentoDao.findAll(criterio,sort);
		listaCxcServicio = this.validarListaAlquilerServicioLuz(listaCxcServicio);
		if (listaTipoCambio!=null && listaTipoCambio.size()>0){
			cobroServicio.setTipoCambioServicio(listaTipoCambio.get(0).getValor());
			cobroServicio.setFechaCobroServicio(UtilSGT.getDatetoString(UtilSGT.getFecha("dd/MM/YYYY")));
			cobroServicio.setTipoMonedaServicio(Constantes.MONEDA_SOL);
			cobroServicio.setMonto(UtilSGT.montoRedondeado(new BigDecimal("0")));
			cobroServicio.setCalculado(UtilSGT.montoRedondeado(new BigDecimal("0")));
		}
		return listaCxcServicio;
	}
	/*
	 * Listado de Luz
	 */
	public List<TblCxcDocumento> mListarLuz(CobroLuzBean cobroLuz, List<TblTipoCambio> listaTipoCambio, Integer intCodigoContrato){
		Sort sort = new Sort(new Sort.Order(Direction.ASC, "codigoCxcDoc"));
		Specification<TblCxcDocumento> criterio	= null;
		List<TblCxcDocumento> listaCxcLuz;
		
		criterio = Specifications.where(conCodigoContrato(intCodigoContrato))
				  .and(conSaldoPositivo(new BigDecimal("0")))
				  //.and(conAnioActual(UtilSGT.getAnioMesString(new Date())))
				  .and(conTipoReferencia(Constantes.TIPO_COBRO_LUZ));
		listaCxcLuz = cxcDocumentoDao.findAll(criterio,sort);
		listaCxcLuz = this.validarListaAlquilerServicioLuz(listaCxcLuz);
		if (listaTipoCambio!=null && listaTipoCambio.size()>0){
			cobroLuz.setTipoCambioLuz(listaTipoCambio.get(0).getValor());
			cobroLuz.setFechaCobroLuz(UtilSGT.getDatetoString(UtilSGT.getFecha("dd/MM/YYYY")));
			cobroLuz.setTipoMonedaLuz(Constantes.MONEDA_SOL);
			cobroLuz.setMonto(UtilSGT.montoRedondeado(new BigDecimal("0")));
			cobroLuz.setCalculado(UtilSGT.montoRedondeado(new BigDecimal("0")));
		}
		return listaCxcLuz;
	}
	
	

	@RequestMapping(value = "/refinanciar/editar/servicio/monto/{id}", method = RequestMethod.GET)
	public String editarMontoServicioContrato(@PathVariable String id, Model model, TblContrato filtro, HttpServletRequest request) {
		String path 							= "";
		BeanRequest beanRequest					= null;
		CobroGeneralBean cobroGeneralBean		= null;
		try{
			path = "caja/refinanciar/ref_edicion";
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			
			this.asignarMontoServicio(beanRequest, new Integer(id));
			cobroGeneralBean = beanRequest.getCobroGeneralBean();
			this.mSetBeanCobro(cobroGeneralBean, beanRequest);
			cobroGeneralBean.setIndiceTab(Constantes.REFINANCIAR_TAB_SERVICIO);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);
			
			request.getSession().setAttribute("beanRequest", beanRequest);
			

		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
	}
	
	@RequestMapping(value = "/refinanciar/editar/servicio/cancelar", method = RequestMethod.GET)
	public String cancelarMontoServicioContrato( Model model, HttpServletRequest request) {
		String path 							= "";
		BeanRequest beanRequest					= null;
		CobroGeneralBean cobroGeneralBean		= null;
		try{
			path = "caja/refinanciar/ref_edicion";
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			beanRequest.setCobroServicio(new CobroServicioBean());
			cobroGeneralBean = beanRequest.getCobroGeneralBean();
			this.mSetBeanCobro(cobroGeneralBean, beanRequest);
			cobroGeneralBean.setIndiceTab(Constantes.REFINANCIAR_TAB_SERVICIO);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);
			
			request.getSession().setAttribute("beanRequest", beanRequest);
			

		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
	}
	@RequestMapping(value = "/refinanciar/editar/servicio/grabar", method = RequestMethod.POST)
	public String refinanciarmontoServicioContrato(Model model, CobroGeneralBean cobroGeneralBean, String path,  PageableSG pageable,HttpServletRequest request) {
		path = "caja/refinanciar/ref_edicion";
		TblCxcDocumento servicio 				= null;
		BeanRequest beanRequest					= null;
		List<TblCxcDocumento> listaCxcServicio	= null;
		try{
			log.debug("[refinanciarmontoServicioContrato] Inicio");
			servicio = cxcDocumentoDao.findOne(cobroGeneralBean.getCobroServicio().getCodigoCxCDocumento());
			servicio.setMontoContrato(cobroGeneralBean.getCobroServicio().getNuevoMonto());
			servicio.setSaldo(cobroGeneralBean.getCobroServicio().getNuevoSaldo());
			this.preEditarDocumento(servicio, request);
			cxcDocumentoDao.save(servicio);
			model.addAttribute("respuesta", "Se ha refinanciado los montos del servicio");
			//listar nuevamente
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			listaCxcServicio= this.mListarServicio(null, null, beanRequest.getContrato().getCodigoContrato());
			beanRequest.setListaCxcServicio(listaCxcServicio);	
			
			beanRequest.setCobroServicio(new CobroServicioBean());
			cobroGeneralBean = beanRequest.getCobroGeneralBean();
			this.mSetBeanCobro(cobroGeneralBean, beanRequest);
			cobroGeneralBean.setIndiceTab(Constantes.REFINANCIAR_TAB_SERVICIO);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);
			
			request.getSession().setAttribute("beanRequest", beanRequest);
			//limpiar
			log.debug("[refinanciarmontoServicioContrato] Fin");
		}catch(Exception e){
			log.debug("[refinanciarmontoServicioContrato] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			//campos		= null;
		}
		log.debug("[refinanciarmontoServicioContrato] Fin");
		return path;
	}
	

	@RequestMapping(value = "/refinanciar/editar/luz/monto/{id}", method = RequestMethod.GET)
	public String editarMontoLuzContrato(@PathVariable String id, Model model, TblContrato filtro, HttpServletRequest request) {
		String path 							= "";
		BeanRequest beanRequest					= null;
		CobroGeneralBean cobroGeneralBean		= null;
		try{
			path = "caja/refinanciar/ref_edicion";
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			
			this.asignarMontoLuz(beanRequest, new Integer(id));
			cobroGeneralBean = beanRequest.getCobroGeneralBean();
			this.mSetBeanCobro(cobroGeneralBean, beanRequest);
			cobroGeneralBean.setIndiceTab(Constantes.REFINANCIAR_TAB_LUZ);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);
			
			request.getSession().setAttribute("beanRequest", beanRequest);
			

		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
	}
	
	@RequestMapping(value = "/refinanciar/editar/Luz/cancelar", method = RequestMethod.GET)
	public String cancelarMontoLuzContrato( Model model, HttpServletRequest request) {
		String path 							= "";
		BeanRequest beanRequest					= null;
		CobroGeneralBean cobroGeneralBean		= null;
		try{
			path = "caja/refinanciar/ref_edicion";
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			beanRequest.setCobroLuz(new CobroLuzBean());
			cobroGeneralBean = beanRequest.getCobroGeneralBean();
			this.mSetBeanCobro(cobroGeneralBean, beanRequest);
			cobroGeneralBean.setIndiceTab(Constantes.REFINANCIAR_TAB_LUZ);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);
			
			request.getSession().setAttribute("beanRequest", beanRequest);
			

		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
	}
	@RequestMapping(value = "/refinanciar/editar/luz/grabar", method = RequestMethod.POST)
	public String refinanciarmontoLuzContrato(Model model, CobroGeneralBean cobroGeneralBean, String path,  PageableSG pageable,HttpServletRequest request) {
		path = "caja/refinanciar/ref_edicion";
		TblCxcDocumento luz 					= null;
		BeanRequest beanRequest					= null;
		List<TblCxcDocumento> listaCxcLuz		= null;
		try{
			log.debug("[refinanciarmontoLuzContrato] Inicio");
			luz = cxcDocumentoDao.findOne(cobroGeneralBean.getCobroLuz().getCodigoCxCDocumento());
			luz.setMontoContrato(cobroGeneralBean.getCobroLuz().getNuevoMonto());
			luz.setSaldo(cobroGeneralBean.getCobroLuz().getNuevoSaldo());
			this.preEditarDocumento(luz, request);
			cxcDocumentoDao.save(luz);
			model.addAttribute("respuesta", "Se ha refinanciado los montos del servicio");
			//listar nuevamente
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			listaCxcLuz= this.mListarLuz(null, null, beanRequest.getContrato().getCodigoContrato());
			beanRequest.setListaCxcLuz(listaCxcLuz);	
			
			beanRequest.setCobroLuz(new CobroLuzBean());
			cobroGeneralBean = beanRequest.getCobroGeneralBean();
			this.mSetBeanCobro(cobroGeneralBean, beanRequest);
			cobroGeneralBean.setIndiceTab(Constantes.REFINANCIAR_TAB_LUZ);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);
			
			request.getSession().setAttribute("beanRequest", beanRequest);
			//limpiar
			log.debug("[refinanciarmontoLuzContrato] Fin");
		}catch(Exception e){
			log.debug("[refinanciarmontoLuzContrato] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			//campos		= null;
		}
		log.debug("[refinanciarmontoLuzContrato] Fin");
		return path;
	}
	/*
	 * Paginado
	 */
	@RequestMapping(value = "/refinanciar/paginado/{page}/{size}/{operacion}", method = RequestMethod.GET)
	public String paginarEntidad(@PathVariable Integer page, @PathVariable Integer size, @PathVariable String operacion, Model model,  PageableSG pageable, HttpServletRequest request) {
		TblContrato filtro = null;
		String path = null;
		try{
			//log.debug("[traerRegistros] Inicio");
			path = "caja/refinanciar/ref_listado";
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
			filtro = (TblContrato)request.getSession().getAttribute("sessionFiltroCriterioRefinanciar");
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
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/refinanciar/regresar", method = RequestMethod.GET)
	public String regresar(Model model, String path, HttpServletRequest request) {
		TblContrato filtro = null;
		List<TblContrato> lista = null;
		PageWrapper<TblContrato> page = null;
		try{
			log.debug("[regresar] Inicio");
			path = "caja/refinanciar/ref_listado";
			
			filtro = (TblContrato)request.getSession().getAttribute("sessionFiltroCriterioRefinanciar");
			model.addAttribute("filtro", filtro);
			lista = (List<TblContrato>)request.getSession().getAttribute("sessionListaContratoRefinanciar");
			model.addAttribute("registros",lista);
			page = (PageWrapper<TblContrato>) request.getSession().getAttribute("PageContratoRefinanciar");
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
