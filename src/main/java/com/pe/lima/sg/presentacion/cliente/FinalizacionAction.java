package com.pe.lima.sg.presentacion.cliente;

import static com.pe.lima.sg.dao.caja.CxCDocumentoSpecifications.conCodigoContrato;
import static com.pe.lima.sg.dao.caja.CxCDocumentoSpecifications.conSaldoPositivo;
import static com.pe.lima.sg.dao.caja.CxCDocumentoSpecifications.conTipoReferencia;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conEdificio;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conEstado;
//import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conEstadoContrato;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conMaterno;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conNombre;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conPaterno;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conRazonSocial;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conRuc;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conTienda;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;


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

import com.pe.lima.sg.bean.caja.CobroBean;
import com.pe.lima.sg.bean.caja.CobroLuzBean;
import com.pe.lima.sg.bean.caja.CobroServicioBean;
import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.dao.caja.ICobroDAO;
import com.pe.lima.sg.dao.caja.ICxCDocumentoDAO;
import com.pe.lima.sg.dao.cliente.IContratoClienteDAO;
import com.pe.lima.sg.dao.cliente.IContratoDAO;
import com.pe.lima.sg.dao.cliente.IContratoPrimerCobroDAO;
import com.pe.lima.sg.dao.cliente.IContratoServicioDAO;
import com.pe.lima.sg.dao.cliente.IObservacionDAO;
import com.pe.lima.sg.dao.mantenimiento.ITiendaDAO;
//import com.pe.lima.sg.dao.mantenimiento.ITipoCambioDAO;
import com.pe.lima.sg.entity.caja.TblCobro;
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
import com.pe.lima.sg.entity.mantenimiento.TblTienda;
//import com.pe.lima.sg.entity.mantenimiento.TblTipoCambio;
import com.pe.lima.sg.presentacion.BaseOperacionPresentacion;
import com.pe.lima.sg.presentacion.BeanRequest;
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
public class FinalizacionAction extends BaseOperacionPresentacion<TblContrato> {

	private static final Logger logger = LogManager.getLogger(FinalizacionAction.class);
	@Autowired
	private IContratoDAO contratoDao;


	/*@Autowired
	private IPersonaDAO personaDao;

	@Autowired
	private IArbitrioDAO arbitrioDao;

	@Autowired
	private ILuzxTiendaDAO luzxTiendaDao;	*/

	@Autowired
	private ICxCDocumentoDAO cxcDocumentoDao;

	@Autowired
	private IContratoClienteDAO contratoClienteDao;

	@Autowired
	private IContratoServicioDAO contratoServicioDao;

	@Autowired
	private IContratoPrimerCobroDAO contratoPrimerCobroDao;

	@Autowired
	private ICobroDAO cobroDao;

	@Autowired
	private IObservacionDAO observacionDao;

	//@Autowired
	//private ITipoCambioDAO tipoCambioDao;

	@Autowired
	private ITiendaDAO tiendaDao;

	@Autowired
	private ListaUtilAction listaUtil;


	@SuppressWarnings("rawtypes")
	@Override
	public BaseOperacionDAO getDao() {
		return contratoDao;
	}

	private String urlPaginado = "/finalizacion/paginado/"; 

	/**
	 * Se encarga de listar todos los contratos
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/finalizacion", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path, HttpServletRequest request) {
		TblContrato filtro = null;
		try{
			logger.debug("[traerRegistros] Inicio");
			path = "cliente/finalizacion/fin_listado";
			filtro = new TblContrato();
			filtro.setTblPersona(new TblPersona());
			filtro.setTblTienda(new TblTienda());
			filtro.getTblTienda().setTblEdificio(new TblEdificio());
			model.addAttribute("filtro", filtro);

			request.getSession().setAttribute("origenLLamada", null);
			request.getSession().setAttribute("sessionFiltroCriterioRemoto",null);
			request.getSession().setAttribute("sessionListaContratoRemoto",null);
			request.getSession().setAttribute("PageContratoRemoto",null);
			
			
			//this.listarContratos(model, filtro);
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
	@RequestMapping(value = "/finalizacions/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, TblContrato filtro, String path,  PageableSG pageable, HttpServletRequest request) {
		//Map<String, Object> campos = null;
		path = "cliente/finalizacion/fin_listado";
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
					//.and(conEstadoContrato(Constantes.ESTADO_CONTRATO_VIGENTE)).or(conEstadoContrato(Constantes.ESTADO_CONTRATO_RENOVADO))
					.and(conEstado(Constantes.ESTADO_REGISTRO_ACTIVO));

			/*Specification<TblContrato> filtro = Specifications.where(conNombre(tblContrato.getTblPersona().getNombre()))
					.and(conPaterno(tblContrato.getTblPersona().getPaterno()))
					.and(conMaterno(tblContrato.getTblPersona().getMaterno()))
					.and(conEdificio(tblContrato.getTblTienda().getTblEdificio().getCodigoEdificio()))
					.and(conTienda(tblContrato.getTblTienda().getNumero()))
					.and(conRuc(tblContrato.getTblPersona().getNumeroRuc()))
					.and(conRazonSocial(tblContrato.getTblPersona().getRazonSocial()))
					.and(conEstado(Constantes.ESTADO_REGISTRO_ACTIVO))
					.and(conEstadoContrato(Constantes.ESTADO_CONTRATO_VIGENTE));*/
			//entidades = contratoDao.findAll(filtro);
			pageable.setSort(sort);
			Page<TblContrato> entidadPage = contratoDao.findAll(filtro, pageable);
			PageWrapper<TblContrato> page = new PageWrapper<TblContrato>(entidadPage, url, pageable);
			model.addAttribute("registros", page.getContent());
			model.addAttribute("page", page);
			request.getSession().setAttribute("sessionFiltroCriterioFinalizacion", tblContrato);
			request.getSession().setAttribute("sessionListaContratoFinalizacion", page.getContent());
			request.getSession().setAttribute("PageContratoFinalizacion", page);

			logger.debug("[listarContratos] Fin");
			//model.addAttribute("registros", entidades);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Se encarga de direccionar a la pantalla de edicion del Contrato
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	/*@RequestMapping(value = "finalizacion/editar/{id}", method = RequestMethod.GET)
	public String editarContrato(@PathVariable Integer id, Model model, TblContrato filtro, HttpServletRequest request) {
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
		Specification<TblCxcDocumento> criterio	= null;
		String path 							= "";
		List<TblTipoCambio> listaTipoCambio		= null;
		CobroBean cobroAlquiler					= null;
		CobroServicioBean cobroServicio			= null;
		CobroLuzBean cobroLuz					= null;
		Map<String, Object> mapServicio	 		= null;
		//TblContratoServicio servicioTipo		= new TblContratoServicio();
		try{
			contrato = contratoDao.findOne(id);
			//servicioTipo.setTblTipoServicio(new TblTipoServicio());
			//CxC Alquiler
			criterio = Specifications.where(conCodigoContrato(contrato.getCodigoContrato()))
					  .and(conTipoReferencia(Constantes.TIPO_COBRO_ALQUILER));
			listaCxcAlquiler = cxcDocumentoDao.findAll(criterio);
			//Tipo de Cambio
			listaTipoCambio = tipoCambioDao.buscarOneByFecha(UtilSGT.getDatetoString(UtilSGT.getFecha("dd/MM/YYYY")));
			cobroAlquiler = new CobroBean();
			if (listaTipoCambio!=null && listaTipoCambio.size()>0){
				cobroAlquiler.setTipoCambio(listaTipoCambio.get(0).getValor());
			}
			//CxC Servicio
			criterio = Specifications.where(conCodigoContrato(contrato.getCodigoContrato()))
					  .and(conTipoReferencia(Constantes.TIPO_COBRO_SERVICIO));
			listaCxcServicio = cxcDocumentoDao.findAll(criterio);
			cobroServicio  = new CobroServicioBean();
			if (listaTipoCambio!=null && listaTipoCambio.size()>0){
				cobroServicio.setTipoCambioServicio(listaTipoCambio.get(0).getValor());
			}
			//CxC Luz
			criterio = Specifications.where(conCodigoContrato(contrato.getCodigoContrato()))
					  .and(conTipoReferencia(Constantes.TIPO_COBRO_LUZ));
			listaCxcLuz = cxcDocumentoDao.findAll(criterio);
			cobroLuz  = new CobroLuzBean();
			if (listaTipoCambio!=null && listaTipoCambio.size()>0){
				cobroLuz.setTipoCambioLuz(listaTipoCambio.get(0).getValor());
			}

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
			//listaArbitrio = arbitrioDao.listarAllActivosxContrato(contrato.getCodigoContrato());
			//listaLuzxtienda = luzxTiendaDao.listarLuzTiendaxContrato(contrato.getCodigoContrato());
			listaObservacion = observacionDao.listarObservacionxContrato(contrato.getCodigoContrato());
			beanRequest = new BeanRequest();
			beanRequest.setListaCxcAlquiler(listaCxcAlquiler);	
			beanRequest.setListaCxcServicio(listaCxcServicio);
			beanRequest.setListaCxcLuz(listaCxcLuz);

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

			beanRequest.setCobroAlquiler(cobroAlquiler);
			//beanRequest.setServicioTipo(servicioTipo);
			beanRequest.setMapServicio(mapServicio);
			beanRequest.setCobroServicio(cobroServicio);
			beanRequest.setCobroLuz(cobroLuz);

			this.cargarListasRequestBeanContrato(model, beanRequest);

			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);
			path = "cliente/finalizacion/fin_edicion";


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
	private void setFechaFinalizacion(TblContrato contrato) {
		if (contrato.getFechaFinalizacion() == null) {
			contrato.setFechaFinalizacion(contrato.getFechaFin());
		}
	}
	
	@RequestMapping(value = "finalizacion/editar/{id}", method = RequestMethod.GET)
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
			contrato = contratoDao.findOne(id);
			this.setFechaFinalizacion(contrato);
			model.addAttribute("contrato", contrato);
			listaServicio = contratoServicioDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			listaCliente = contratoClienteDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			listaCobro = contratoPrimerCobroDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			//listaArbitrio = arbitrioDao.listarAllActivosxContrato(contrato.getCodigoContrato());
			//listaLuzxtienda = luzxTiendaDao.listarLuzTiendaxContrato(contrato.getCodigoContrato());
			listaObservacion = observacionDao.listarObservacionxContrato(contrato.getCodigoContrato());
			//Busqueda de los contratos Antiguos
			listaContratoAnt = new ArrayList<TblContrato>();
			getContratosAntiguos2(contrato,listaContratoAnt);

			beanRequest = new BeanRequest();
			beanRequest.setContrato(contrato);
			beanRequest.setContratoAntiguo(contrato);
			beanRequest.setContratoServicio(new TblContratoServicio());
			//this.inicializaDataDefaultServicio(beanRequest.getContratoServicio());
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
			path = "cliente/finalizacion/fin_edicion";


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
	@RequestMapping(value = "finalizacion/editar/contrato/{id}", method = RequestMethod.GET)
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
			this.setFechaFinalizacion(contrato);
			model.addAttribute("contrato", contrato);
			listaServicio = contratoServicioDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			listaCliente = contratoClienteDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			listaCobro = contratoPrimerCobroDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			//listaArbitrio = arbitrioDao.listarAllActivosxContrato(contrato.getCodigoContrato());
			//listaLuzxtienda = luzxTiendaDao.listarLuzTiendaxContrato(contrato.getCodigoContrato());
			listaObservacion = observacionDao.listarObservacionxContrato(contrato.getCodigoContrato());
			//Busqueda de los contratos Antiguos
			listaContratoAnt = new ArrayList<TblContrato>();
			getContratosAntiguos2(contrato,listaContratoAnt);

			beanRequest = new BeanRequest();
			beanRequest.setContrato(contrato);
			beanRequest.setContratoAntiguo(contrato);
			beanRequest.setContratoServicio(new TblContratoServicio());
			//this.inicializaDataDefaultServicio(beanRequest.getContratoServicio());
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
			path = "cliente/finalizacion/fin_edicion";


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





	public void preEditarTienda(TblTienda entidad, HttpServletRequest request) {
		try{
			logger.debug("[preEditarTienda] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			logger.debug("[preEditarTienda] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}






	/**
	 * Se encarga de guardar la informacion 
	 * 
	 * @param 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/finalizacion/editar/guardar", method = RequestMethod.POST)
	public String actualizarEntidad(Model model, TblContrato entidad, HttpServletRequest request, String path,  PageableSG pageable) {
		path 											= "cliente/finalizacion/fin_edicion";
		BeanRequest beanRequest							= null;
		TblContrato contrato							= null;

		//List<TblContratoServicio> listaServicio	= null;
		List<TblObservacion> listaObservacion	= null;
		/*List<TblContratoCliente> listaCliente	= null;
		List<TblContratoPrimerCobro> listaCobro	= null;
		List<TblArbitrio> listaArbitrio			= null;
		List<TblLuzxtienda> listaLuzxtienda		= null;

		List<TblCxcDocumento> listaCxcAlquiler	= null;
		List<TblCxcDocumento> listaCxcServicio	= null;
		List<TblCxcDocumento> listaCxcLuz		= null;
		CobroBean cobroAlquiler					= null;
		CobroServicioBean cobroServicio			= null;
		CobroLuzBean cobroLuz					= null;*/
		//Map<String, Object> mapServicio	 		= null;
		//TblContratoServicio servicioTipo		= new TblContratoServicio();
		try{
			logger.debug("[actualizarEntidad] Inicio" );
			//servicioTipo.setTblTipoServicio(new TblTipoServicio());
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			logger.debug("[actualizarEntidad] Pre Guardar..." );
			contrato = contratoDao.findOne(entidad.getCodigoContrato());
			//registro de las observaciones
			if (beanRequest.getListaObservacion()!=null && beanRequest.getListaObservacion().size()>0){
				for(TblObservacion observacion:beanRequest.getListaObservacion()){
					observacion.setCodigoContrato(contrato.getCodigoContrato());
					this.preGuardarObservacion(observacion, request);
					observacionDao.save(observacion);
				}
			}
			this.preEditarContrato(entidad, request);
			contrato.setEstadoContrato(Constantes.ESTADO_CONTRATO_FINALIZADO);
			contrato.setInformacionAdicional(entidad.getInformacionAdicional());
			contrato.setFechaFinalizacion(entidad.getFechaFinalizacion());
			contratoDao.save(contrato);
			contrato.getTblTienda().setEstadoTienda(Constantes.ESTADO_TIENDA_DESOCUPADO);
			this.preEditarTienda(contrato.getTblTienda(), request);
			tiendaDao.save(contrato.getTblTienda());
			model.addAttribute("respuesta", "El contrato ["+contrato.getNumero()+"] fue Finalizado");
			beanRequest.setContrato(contrato);
			//Lista los datos del contrato
			/*listaServicio = contratoServicioDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			if (listaServicio != null && listaServicio.size() >0){
				mapServicio = new LinkedHashMap<String, Object>();
				for(TblContratoServicio servicio : listaServicio){
					mapServicio.put(servicio.getTblTipoServicio().getDescripcion(), servicio.getTblTipoServicio().getCodigoTipoServicio());

				}
			}*/
			//listaCliente = contratoClienteDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			//listaCobro = contratoPrimerCobroDao.listarAllActivosXContrato(contrato.getCodigoContrato());
			//listaArbitrio = arbitrioDao.listarAllActivosxContrato(contrato.getCodigoContrato());
			//listaLuzxtienda = luzxTiendaDao.listarLuzTiendaxContrato(contrato.getCodigoContrato());
			listaObservacion = observacionDao.listarObservacionxContrato(contrato.getCodigoContrato());
			beanRequest.setListaObservacion(listaObservacion);

			/*beanRequest = new BeanRequest();
			beanRequest.setListaCxcAlquiler(listaCxcAlquiler);	
			beanRequest.setListaCxcServicio(listaCxcServicio);
			beanRequest.setListaCxcLuz(listaCxcLuz);


			beanRequest.setContratoServicio(new TblContratoServicio());
			beanRequest.setListaServicio(listaServicio);
			beanRequest.setListaCliente(listaCliente);
			beanRequest.setContratoPrimerCobro(new TblContratoPrimerCobro());
			beanRequest.setListaPrimerCobro(listaCobro);
			beanRequest.setArbitrio(new TblArbitrio());
			beanRequest.setListaArbitrio(listaArbitrio);
			beanRequest.setListaLuzxTienda(listaLuzxtienda);*/

			beanRequest.setObservacion(new TblObservacion());

			//beanRequest.setCobroAlquiler(cobroAlquiler);
			//beanRequest.setServicioTipo(servicioTipo);
			//beanRequest.setMapServicio(mapServicio);
			//beanRequest.setCobroServicio(cobroServicio);
			//beanRequest.setCobroLuz(cobroLuz);

			this.cargarListasRequestBeanContrato(model, beanRequest);

			this.cargarListaOperacionContrato(model);
			
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
				
				path = "cliente/finalizacion/fin_listado";

				request.getSession().setAttribute("beanRequest", beanRequest);
	
				TblContrato filtro = null;
				filtro = (TblContrato)request.getSession().getAttribute("sessionFiltroCriterioFinalizacion");
				
				if (filtro.getTblPersona() == null){
					filtro.setTblPersona(new TblPersona());
				}
				this.listarContratos(model, filtro, pageable, this.urlPaginado,request);
				model.addAttribute("filtro", filtro);
				request.getSession().setAttribute("sessionFiltroCriterioFinalizacion", filtro);
			}	
			
			logger.debug("[actualizarEntidad] Fin" );
		}catch(Exception e){
			model.addAttribute("resultadoContrato", e.getMessage());
			e.printStackTrace();
		}finally{
			beanRequest			= null;
			contrato			= null;

			//listaServicio		= null;
			//listaCliente		= null;
			//listaCobro			= null;
			//listaArbitrio		= null;
			//listaLuzxtienda		= null;
			listaObservacion	= null;
			//listaCxcAlquiler	= null;
			//listaCxcServicio	= null;
			//listaCxcLuz			= null;
			//cobroAlquiler		= null;
			//cobroServicio		= null;
			//cobroLuz			= null;
			//mapServicio	 		= null;
			//servicioTipo		= null;
		}
		return path;

	}
	/*
	 * Registra los cobros de alquiler, servicio, luz
	 * True: El monto del cobro es igual o menor a la deuda
	 * False: El monto de cobro es mayor a la deuda, esto genera adelanto
	 */
	public boolean registrarCobroContrato(List<TblCobro> listaCobroContrato, TblContrato contrato, HttpServletRequest request, String tipoCobro){
		boolean resultado = false;
		TblCobro cobroAlquiler = null;
		List<TblCxcDocumento> listaDeuda = null;
		Specification<TblCxcDocumento> criterio	= null;
		boolean actualizarDocumento 	= false;
		try{
			criterio = Specifications.where(conCodigoContrato(contrato.getCodigoContrato()))
					.and(conTipoReferencia(tipoCobro))
					.and(conSaldoPositivo(new BigDecimal("0")));
			listaDeuda = cxcDocumentoDao.findAll(criterio);

			if(listaDeuda !=null && listaDeuda.size()>0){
				for(TblCxcDocumento documento: listaDeuda){
					if (listaCobroContrato !=null && listaCobroContrato.size() >0){
						actualizarDocumento = false;
						for(TblCobro cobro:listaCobroContrato){
							//Moneda: DOLARES
							if (documento.getTipoMoneda().equals(Constantes.MONEDA_DOLAR)){
								if (cobro.getMontoDolares().doubleValue()>0 ){
									if (cobro.getMontoDolares().doubleValue()>=documento.getSaldo().doubleValue()){

										//Cobro
										cobroAlquiler = new TblCobro();
										cobroAlquiler.setFechaCobro(cobro.getFechaCobro());
										cobroAlquiler.setMontoDolares(documento.getSaldo());
										cobroAlquiler.setTipoCambio(cobro.getTipoCambio());
										cobroAlquiler.setMontoSoles(documento.getSaldo().multiply(cobro.getTipoCambio()).setScale(2, RoundingMode.CEILING));
										cobroAlquiler.setTblCxcDocumento(documento);
										this.preGuardarCobro(cobroAlquiler, request);
										cobroDao.save(cobroAlquiler);
										//CxC Documento
										documento.setSaldo(new BigDecimal("0"));
										this.preEditarDocumento(documento, request);
										cxcDocumentoDao.save(documento);
										cobro.setMontoDolares(cobro.getMontoDolares().subtract(cobroAlquiler.getMontoDolares()));
										cobro.setMontoSoles(cobro.getMontoSoles().subtract(cobroAlquiler.getMontoSoles()));
										actualizarDocumento = false;
										break;
									}else{
										//Cobro
										cobroAlquiler = new TblCobro();
										cobroAlquiler.setFechaCobro(cobro.getFechaCobro());
										cobroAlquiler.setMontoDolares(cobro.getMontoDolares());
										cobroAlquiler.setTipoCambio(cobro.getTipoCambio());
										cobroAlquiler.setMontoSoles(cobro.getMontoDolares().multiply(cobro.getTipoCambio()).setScale(2, RoundingMode.CEILING));
										cobroAlquiler.setTblCxcDocumento(documento);
										this.preGuardarCobro(cobroAlquiler, request);
										cobroDao.save(cobroAlquiler);
										//CxC Documento
										documento.setSaldo(documento.getSaldo().subtract(cobro.getMontoDolares()));
										actualizarDocumento = true;
									}
								}

							}else{
								if (cobro.getMontoDolares().doubleValue()>0){
									if (cobro.getMontoSoles().doubleValue()>=documento.getSaldo().doubleValue()){
										//Cobro
										cobroAlquiler = new TblCobro();
										cobroAlquiler.setFechaCobro(cobro.getFechaCobro());
										//cobroAlquiler.setMontoDolares(documento.getSaldo().divide(cobro.getTipoCambio(), 2, RoundingMode.HALF_UP));
										cobroAlquiler.setTipoCambio(cobro.getTipoCambio());
										cobroAlquiler.setMontoSoles(documento.getSaldo());
										cobroAlquiler.setTblCxcDocumento(documento);
										this.preGuardarCobro(cobroAlquiler, request);
										cobroDao.save(cobroAlquiler);
										//CxC Documento
										documento.setSaldo(new BigDecimal("0"));
										this.preEditarDocumento(documento, request);
										cxcDocumentoDao.save(documento);
										cobro.setMontoDolares(cobro.getMontoDolares().subtract(cobroAlquiler.getMontoDolares()));
										cobro.setMontoSoles(cobro.getMontoSoles().subtract(cobroAlquiler.getMontoSoles()));
										actualizarDocumento = false;
										break;
									}else{
										//Cobro
										cobroAlquiler = new TblCobro();
										cobroAlquiler.setFechaCobro(cobro.getFechaCobro());
										//cobroAlquiler.setMontoDolares(cobro.getMontoDolares().divide(cobro.getTipoCambio(), 2, RoundingMode.HALF_UP));
										cobroAlquiler.setTipoCambio(cobro.getTipoCambio());
										cobroAlquiler.setMontoSoles(cobro.getMontoSoles());
										cobroAlquiler.setTblCxcDocumento(documento);
										this.preGuardarCobro(cobroAlquiler, request);
										cobroDao.save(cobroAlquiler);
										//CxC Documento
										documento.setSaldo(documento.getSaldo().subtract(cobro.getMontoSoles()));
										actualizarDocumento = true;
									}
								}
							}
						}
						//Se actualiza el documento porque no hay mas cobros para descontar
						if (actualizarDocumento){
							this.preEditarDocumento(documento, request);
							cxcDocumentoDao.save(documento);
							break;
						}
						//validamos el saldo antes de finalizar
						if (documento.getSaldo().doubleValue()>0){
							break;
						}
					}
				}

			}else{
				if (listaCobroContrato !=null && listaCobroContrato.size() >0){
					//Registrar como adelanto

					resultado = false;
				}else{
					resultado = true;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			cobroAlquiler 	= null;
			listaDeuda 		= null;
			criterio		= null;
		}
		return resultado;
	}



	public void preGuardarCobro(TblCobro entidad, HttpServletRequest request) {
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
	public void preEditarDocumento(TblCxcDocumento entidad, HttpServletRequest request) {
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
	@Override
	public void preEditar(TblContrato entidad, HttpServletRequest request) {
		BeanRequest beanRequest	= null;
		//List<TblContratoPrimerCobro> listaPC	= null;
		List<TblObservacion> listaObs	= null;
		try{
			logger.debug("[preEditar] Inicio" );
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
		model.addAttribute("listaCxcAlquiler", beanRequest.getListaCxcAlquiler());
		model.addAttribute("listaCxcServicio", beanRequest.getListaCxcServicio());
		model.addAttribute("listaCxcLuz", beanRequest.getListaCxcLuz());
		model.addAttribute("cobroAlquiler", beanRequest.getCobroAlquiler());
		model.addAttribute("historialCobroAlquiler", beanRequest.getHistorialCobroAlquiler());
		model.addAttribute("servicioTipo", beanRequest.getServicioTipo());
		model.addAttribute("mapServicioTipo", beanRequest.getMapServicio());
		model.addAttribute("cobroServicio", beanRequest.getCobroServicio());
		model.addAttribute("historialCobroServicio", beanRequest.getHistorialCobroServicio());
		model.addAttribute("cobroLuz", beanRequest.getCobroLuz());
		model.addAttribute("historialLuz", beanRequest.getHistorialCobroLuz());
		model.addAttribute("historialCobroLuz", beanRequest.getHistorialCobroLuz());
		model.addAttribute("listaContratoAnterior", beanRequest.getListaContrato());

	}


	/**
	 * Se encarga de adicionar los primeros cobros
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/finalizacion/observacion/edicion", method = RequestMethod.POST)
	public String adicionarObservacionEdicion(Model model, TblObservacion observacionBean, TblContrato contrato, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		try{
			logger.debug("[adicionarObservacionEdicion] Inicio:");
			path = "cliente/finalizacion/fin_edicion";

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
			/*
			if (observacionBean.getAsunto()== null || observacionBean.getAsunto().equals("")){
				model.addAttribute("resultadoObservacion", "Debe ingresar el asunto de la Observacion");
				return path;
			}
			if (observacionBean.getBreveDescripcion()== null || observacionBean.getBreveDescripcion().equals("")){
				model.addAttribute("resultadoObservacion", "Debe ingresar una descripcion breve");
				return path;
			}
			if (observacionBean.getDescripcion()== null || observacionBean.getDescripcion().equals("")){
				model.addAttribute("resultadoObservacion", "Debe ingresar una descripcion");
				return path;
			}


			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			contrato = beanRequest.getContrato();
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

			request.getSession().setAttribute("beanRequest", beanRequest);*/
			logger.debug("[adicionarObservacionEdicion] Fin");
		}catch(Exception e){
			logger.debug("[adicionarObservacionEdicion] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			beanRequest = null;
		}

		return path;
	}

	/**
	 * Se encarga de adicionar un cobro de alquiler
	 * 
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "/finalizacion/alquiler", method = RequestMethod.POST)
	public String adicionarCobroAlquiler(Model model, CobroBean cobroAlquiler, TblContrato contrato, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		TblCobro cobroAlquilerAux			= new TblCobro();
		try{
			logger.debug("[adicionarCobroAlquiler] Inicio");
			path = "cliente/finalizacion/fin_edicion";
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			if (this.validarCobroAlquiler(model, cobroAlquiler, request)){
				cobroAlquilerAux.setFechaCobro(cobroAlquiler.getFechaCobro());
				cobroAlquilerAux.setMontoSoles(cobroAlquiler.getMontoSoles());
				cobroAlquilerAux.setMontoDolares(cobroAlquiler.getMontoDolares());
				cobroAlquilerAux.setTipoCambio(cobroAlquiler.getTipoCambio());


				if (beanRequest.getHistorialCobroAlquiler() == null) {
					beanRequest.setHistorialCobroAlquiler(new ArrayList<TblCobro>());
					beanRequest.getHistorialCobroAlquiler().add(cobroAlquilerAux);

				}else{
					beanRequest.getHistorialCobroAlquiler().add(cobroAlquilerAux);
				}
				model.addAttribute("respuesta", "Se adiciono el Cobro de Alquiler. Esta pendiente el registro.");
			}
			//model.addAttribute("listaServicio", beanRequest.getListaServicio());
			beanRequest.setContrato(contrato);
			beanRequest.setCobroAlquiler(cobroAlquiler);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);

			logger.debug("[adicionarCobroAlquiler] Fin");
		}catch(Exception e){
			logger.debug("[adicionarCobroAlquiler] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			beanRequest = null;
		}

		return path;
	}

	/*
	 * Valida los campos del cobro de alquiler
	 */
	public boolean validarCobroAlquiler(Model model,CobroBean entidad, HttpServletRequest request){
		boolean exitoso = true;
		try{
			if (entidad.getFechaCobro() == null){
				exitoso = false;
				model.addAttribute("respuesta", "Debe seleccionar la fecha de cobro.");
			}else if (entidad.getMontoSoles() == null || entidad.getMontoSoles().doubleValue()<=0){
				exitoso = false;
				model.addAttribute("respuesta", "Debe Ingresar el monto en soles.");
			}else if (entidad.getMontoDolares() == null || entidad.getMontoDolares().doubleValue()<=0){
				exitoso = false;
				model.addAttribute("respuesta", "Debe Ingresar el monto en dolares.");
			}

		}catch(Exception e){
			exitoso = false;
		}
		return exitoso;
	}

	/**
	 * Se encarga de adicionar un cobro de servicio
	 * 
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "/finalizacion/servicio", method = RequestMethod.POST)
	public String adicionarCobroServicio(Model model, CobroServicioBean cobroServicio, TblContrato contrato, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		TblCobro cobroServiciorAux			= new TblCobro();
		try{
			logger.debug("[adicionarCobroServicio] Inicio");
			path = "cliente/finalizacion/fin_edicion";
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			if (this.validarCobroServicio(model, cobroServicio, request)){
				cobroServiciorAux.setFechaCobro(cobroServicio.getFechaCobroServicio());
				cobroServiciorAux.setMontoSoles(cobroServicio.getMontoSolesServicio());
				cobroServiciorAux.setMontoDolares(cobroServicio.getMontoDolaresServicio());
				cobroServiciorAux.setTipoCambio(cobroServicio.getTipoCambioServicio());


				if (beanRequest.getHistorialCobroServicio() == null) {
					beanRequest.setHistorialCobroServicio(new ArrayList<TblCobro>());
					beanRequest.getHistorialCobroServicio().add(cobroServiciorAux);

				}else{
					beanRequest.getHistorialCobroServicio().add(cobroServiciorAux);
				}
				model.addAttribute("respuestaServicio", "Se adiciono el Cobro de Servicio. Esta pendiente el registro.");
			}
			//model.addAttribute("listaServicio", beanRequest.getListaServicio());
			beanRequest.setContrato(contrato);
			beanRequest.setCobroServicio(cobroServicio);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);

			logger.debug("[adicionarCobroServicio] Fin");
		}catch(Exception e){
			logger.debug("[adicionarCobroServicio] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			beanRequest = null;
		}

		return path;
	}

	/*
	 * Valida los campos del cobro de alquiler
	 */
	public boolean validarCobroServicio(Model model,CobroServicioBean entidad, HttpServletRequest request){
		boolean exitoso = true;
		try{
			if (entidad.getFechaCobroServicio() == null){
				exitoso = false;
				model.addAttribute("respuestaServicio", "Debe seleccionar la fecha de cobro.");
			}else if (entidad.getMontoSolesServicio() == null || entidad.getMontoSolesServicio().doubleValue()<=0){
				exitoso = false;
				model.addAttribute("respuestaServicio", "Debe Ingresar el monto en soles.");
			}else if (entidad.getMontoDolaresServicio() == null || entidad.getMontoDolaresServicio().doubleValue()<=0){
				exitoso = false;
				model.addAttribute("respuestaServicio", "Debe Ingresar el monto en dolares.");
			}

		}catch(Exception e){
			exitoso = false;
		}
		return exitoso;
	}

	/**
	 * Se encarga de adicionar un cobro de luz
	 * 
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "/finalizacion/luz", method = RequestMethod.POST)
	public String adicionarCobroLuz(Model model, CobroLuzBean cobroLuz, TblContrato contrato, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		TblCobro cobroLuzAux			= new TblCobro();
		try{
			logger.debug("[adicionarCobroLuz] Inicio");
			path = "cliente/finalizacion/fin_edicion";
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			if (this.validarCobroLuz(model, cobroLuz, request)){
				cobroLuzAux.setFechaCobro(cobroLuz.getFechaCobroLuz());
				cobroLuzAux.setMontoSoles(cobroLuz.getMontoSolesLuz());
				cobroLuzAux.setMontoDolares(cobroLuz.getMontoDolaresLuz());
				cobroLuzAux.setTipoCambio(cobroLuz.getTipoCambioLuz());


				if (beanRequest.getHistorialCobroLuz() == null) {
					beanRequest.setHistorialCobroLuz(new ArrayList<TblCobro>());
					beanRequest.getHistorialCobroLuz().add(cobroLuzAux);

				}else{
					beanRequest.getHistorialCobroLuz().add(cobroLuzAux);
				}
				model.addAttribute("respuestaLuz", "Se adiciono el Cobro de Luz. Esta pendiente el registro.");
			}
			//model.addAttribute("listaServicio", beanRequest.getListaServicio());
			beanRequest.setContrato(contrato);
			beanRequest.setCobroLuz(cobroLuz);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);

			logger.debug("[adicionarCobroLuz] Fin");
		}catch(Exception e){
			logger.debug("[adicionarCobroLuz] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			beanRequest = null;
		}

		return path;
	}

	/*
	 * Valida los campos del cobro de Luz
	 */
	public boolean validarCobroLuz(Model model,CobroLuzBean entidad, HttpServletRequest request){
		boolean exitoso = true;
		try{
			if (entidad.getFechaCobroLuz() == null){
				exitoso = false;
				model.addAttribute("respuestaLuz", "Debe seleccionar la fecha de cobro.");
			}else if (entidad.getMontoSolesLuz() == null || entidad.getMontoSolesLuz().doubleValue()<=0){
				exitoso = false;
				model.addAttribute("respuestaLuz", "Debe Ingresar el monto en soles.");
			}else if (entidad.getMontoDolaresLuz() == null || entidad.getMontoDolaresLuz().doubleValue()<=0){
				exitoso = false;
				model.addAttribute("respuestaLuz", "Debe Ingresar el monto en dolares.");
			}

		}catch(Exception e){
			exitoso = false;
		}
		return exitoso;
	}
	/**
	 * Se encarga de regresar a la pantalla de Cobro
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/finalizacion/regresar", method = RequestMethod.POST)
	public String regresarCobro(Model model, Filtro filtro, String path, HttpServletRequest request) {
		//TblContrato contrato				= null;
		BeanRequest beanRequest				= null;
		path = "cliente/finalizacion/fin_edicion";
		try{
			logger.debug("[regresarContrato] Inicio");
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");

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
	 * Se encarga de direccionar a la pantalla de historial de cobro de alquiler
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/finalizacion/historial/alquiler/{id}", method = RequestMethod.GET)
	public String historialCobroAlquiler(@PathVariable Integer id, Model model, TblContrato filtro, HttpServletRequest request) {
		String path 							= "";
		List<TblCobro> listaCobro				= null;
		try{
			path = "cliente/finalizacion/fin_historial_alquiler";
			listaCobro = cobroDao.listarAllActivosxDocumento(id);
			model.addAttribute("registros", listaCobro);

		}catch(Exception e){
			e.printStackTrace();
		}finally{

			listaCobro			= null;
		}
		return path;
	}
	/**
	 * Se encarga de direccionar a la pantalla de historial de cobro de servicio
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/finalizacion/historial/servicio/{id}", method = RequestMethod.GET)
	public String historialCobroServicio(@PathVariable Integer id, Model model, TblContrato filtro, HttpServletRequest request) {
		String path 							= "";
		List<TblCobro> listaCobro				= null;
		try{
			path = "cliente/finalizacion/fin_historial_servicio";
			listaCobro = cobroDao.listarAllActivosxDocumento(id);
			model.addAttribute("registros", listaCobro);

		}catch(Exception e){
			e.printStackTrace();
		}finally{

			listaCobro			= null;
		}
		return path;
	}

	/**
	 * Se encarga de direccionar a la pantalla de historial de cobro de luz
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/finalizacion/historial/luz/{id}", method = RequestMethod.GET)
	public String historialCobroLuz(@PathVariable Integer id, Model model, TblContrato filtro, HttpServletRequest request) {
		String path 							= "";
		List<TblCobro> listaCobro				= null;
		try{
			path = "cliente/finalizacion/fin_historial_luz";
			listaCobro = cobroDao.listarAllActivosxDocumento(id);
			model.addAttribute("registros", listaCobro);

		}catch(Exception e){
			e.printStackTrace();
		}finally{

			listaCobro			= null;
		}
		return path;
	}
	public void preGuardarObservacion(TblObservacion entidad, HttpServletRequest request) {
		try{
			logger.debug("[preGuardarObservacion] Inicio" );
			entidad.setFechaCreacion(new Date(System.currentTimeMillis()));
			entidad.setIpCreacion(request.getRemoteAddr());
			entidad.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			logger.debug("[preGuardarObservacion] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void preEditarContrato(TblContrato entidad, HttpServletRequest request) {
		try{
			logger.debug("[preEditarTienda] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			logger.debug("[preEditarTienda] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/finalizacion/regresar/listado", method = RequestMethod.GET)
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
				path = "cliente/finalizacion/fin_listado";
	
				filtro = (TblContrato)request.getSession().getAttribute("sessionFiltroCriterioFinalizacion");
				model.addAttribute("filtro", filtro);
				lista = (List<TblContrato>)request.getSession().getAttribute("sessionListaContratoFinalizacion");
				model.addAttribute("registros",lista);
				page = (PageWrapper<TblContrato>) request.getSession().getAttribute("PageContratoFinalizacion");
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
	/*
	 * Paginado
	 */
	@RequestMapping(value = "/finalizacion/paginado/{page}/{size}/{operacion}", method = RequestMethod.GET)
	public String paginarEntidad(@PathVariable Integer page, @PathVariable Integer size, @PathVariable String operacion, Model model,  PageableSG pageable, HttpServletRequest request) {
		TblContrato filtro = null;
		String path = null;
		try{
			//LOGGER.debug("[traerRegistros] Inicio");
			path = "cliente/finalizacion/fin_listado";
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
			filtro = (TblContrato)request.getSession().getAttribute("sessionFiltroCriterioFinalizacion");
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
}
