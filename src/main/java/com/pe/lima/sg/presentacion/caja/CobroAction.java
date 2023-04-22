package com.pe.lima.sg.presentacion.caja;

import static com.pe.lima.sg.dao.caja.CxCDocumentoSpecifications.conCodigoContrato;
import static com.pe.lima.sg.dao.caja.CxCDocumentoSpecifications.conSaldoPositivo;
import static com.pe.lima.sg.dao.caja.CxCDocumentoSpecifications.conSaldoCero;
import static com.pe.lima.sg.dao.caja.CxCDocumentoSpecifications.conTipoReferencia;
import static com.pe.lima.sg.dao.caja.CxCDocumentoSpecifications.conEstadoDocumento;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conEdificio;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conEstado;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conListaEstadoContrato;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conMaterno;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conNombre;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conPaterno;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conRazonSocial;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conRuc;
import static com.pe.lima.sg.dao.cliente.ContratoSpecifications.conTienda;
import static com.pe.lima.sg.dao.cliente.ArbitriosSpecifications.conCodigoTienda;
import static com.pe.lima.sg.dao.cliente.ArbitriosSpecifications.conSaldoPositivoArbitrio;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.pe.lima.sg.bean.caja.DesembolsoBean;
import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.dao.caja.ICobroArbitrioDAO;
import com.pe.lima.sg.dao.caja.ICobroDAO;
import com.pe.lima.sg.dao.caja.ICxCDocumentoDAO;
import com.pe.lima.sg.dao.caja.IDesembolsoArbitrioDAO;
import com.pe.lima.sg.dao.caja.IDesembolsoDAO;

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
import com.pe.lima.sg.entity.caja.TblCobroArbitrio;
import com.pe.lima.sg.entity.caja.TblCxcDocumento;
import com.pe.lima.sg.entity.caja.TblDesembolso;
import com.pe.lima.sg.entity.caja.TblDesembolsoArbitrio;
import com.pe.lima.sg.entity.cliente.TblAdelanto;
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
import com.pe.lima.sg.entity.mantenimiento.TblTienda;
import com.pe.lima.sg.entity.mantenimiento.TblTipoCambio;
import com.pe.lima.sg.facturador.bean.FiltroPdf;
import com.pe.lima.sg.facturador.bean.ParametroFacturadorBean;
import com.pe.lima.sg.facturador.dao.IComprobanteDAO;
import com.pe.lima.sg.facturador.dao.IDetalleComprobanteDAO;
import com.pe.lima.sg.facturador.dao.ILeyendaDAO;
import com.pe.lima.sg.facturador.dao.ISunatSFS12CabeceraDAO;
import com.pe.lima.sg.facturador.dao.ISunatSFS12DetalleDAO;
import com.pe.lima.sg.facturador.entity.TblComprobante;
import com.pe.lima.sg.facturador.entity.TblDetalleComprobante;
import com.pe.lima.sg.facturador.entity.TblLeyenda;
import com.pe.lima.sg.facturador.entity.TblSunatCabecera;
import com.pe.lima.sg.facturador.entity.TblSunatDetalle;
import com.pe.lima.sg.facturador.pdf.ComprobanteKenorPdf;
import com.pe.lima.sg.presentacion.BaseOperacionPresentacion;
import com.pe.lima.sg.presentacion.BeanRequest;
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
public class CobroAction extends BaseOperacionPresentacion<TblCobro> {

	
	private final String PAGO_EXCESO 		= "PAGO EXCESO";
	private final String PAGO_EXACTO		= "PAGO EXACTO";
	private final String PAGO_FALTATE		= "PAGO FALTANTE";
	private final String KEY_ESTADO_PAGO	= "KEY ESTADO PAGO";
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
	private ICobroArbitrioDAO cobroArbitrioDao;
	
	@Autowired
	private IArbitrioDAO arbitrioDao;
	
	@Autowired
	private ILuzxTiendaDAO luzxTiendaDao;
	
	@Autowired
	private IObservacionDAO observacionDao;	

	@Autowired
	private IDesembolsoArbitrioDAO desembolsoArbitrioDao;
	
	@Autowired
	private IDesembolsoDAO desembolsoDao;

	@Autowired
	private ITipoCambioDAO tipoCambioDao;
	
	@Autowired
	private ListaUtilAction listaUtil;

	@Autowired
	private IAdelantoDAO adelantoDao;
	
	@Autowired
	private ISunatSFS12CabeceraDAO sunatCabeceraDao;
	
	@Autowired
	private ISunatSFS12DetalleDAO sunatDetalleDao;
	
	@Autowired
	private IComprobanteDAO comprobanteDao;
	
	@Autowired
	private ILeyendaDAO leyendaDao;

	@Autowired
	private IDetalleComprobanteDAO detalleComprobanteDao;
	
	private String urlPaginado = "/cobro/paginado/"; 

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
	@RequestMapping(value = "/cobro", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path,  PageableSG pageable) {
		TblContrato filtro = null;
		try{
			log.debug("[traerRegistros] Inicio");
			path = "caja/cobro/cob_listado";
			filtro = new TblContrato();
			filtro.setTblPersona(new TblPersona());
			filtro.setTblTienda(new TblTienda());
			filtro.getTblTienda().setTblEdificio(new TblEdificio());
			model.addAttribute("filtro", filtro);
			
			model.addAttribute("registros", new ArrayList<TblContrato>());
			model.addAttribute("page", null);
			
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
	@RequestMapping(value = "/cobros/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, TblContrato filtro, String path,  PageableSG pageable,HttpServletRequest request) {
		//Map<String, Object> campos = null;
		path = "caja/cobro/cob_listado";
		try{
			log.debug("[traerRegistrosFiltrados] Inicio");
			this.listarContratos(model, filtro, pageable, this.urlPaginado,request);
			model.addAttribute("filtro", filtro);
			request.getSession().setAttribute("sessionFiltroCriterioCobro", filtro);
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
			//Se comenta para solo listar los vigentes
			//2019.09.22: se ajusta la data 
			//listaEstado.add(Constantes.ESTADO_CONTRATO_PENDIENTE);
			listaEstado.add(Constantes.ESTADO_CONTRATO_VIGENTE);
			//listaEstado.add(Constantes.ESTADO_CONTRATO_RENOVADO);
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
			
			request.getSession().setAttribute("sessionFiltroCriterioCobro", tblContrato);
			request.getSession().setAttribute("sessionListaCobro", page.getContent());
			request.getSession().setAttribute("PageCobro", page);
			request.getSession().setAttribute("PageableSGCobro", pageable);
			
			log.debug("[listarContratos] entidades:"+entidades);
			//model.addAttribute("registros", entidades);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades = null;
		}
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
				  .and(conSaldoPositivo(new BigDecimal("0")))
				  //.and(conAnioActual(UtilSGT.getAnioMesString(new Date())))
				  .and(conTipoReferencia(Constantes.TIPO_COBRO_ALQUILER));
		listaCxcAlquiler = cxcDocumentoDao.findAll(criterio,sort);
		if (listaTipoCambio!=null && listaTipoCambio.size()>0){
			cobroAlquiler.setTipoCambio(listaTipoCambio.get(0).getValor());
			cobroAlquiler.setFechaCobro(UtilSGT.getDatetoString(UtilSGT.getFecha("dd/MM/YYYY")));
			cobroAlquiler.setTipoMoneda(Constantes.MONEDA_DOLAR);
			cobroAlquiler.setMonto(UtilSGT.montoRedondeado(new BigDecimal("0")));
			cobroAlquiler.setCalculado(UtilSGT.montoRedondeado(new BigDecimal("0")));
		}
		return listaCxcAlquiler;
	}
	
	public List<TblCxcDocumento> mListarAlquilerSaldoCero( CobroBean cobroAlquiler, List<TblTipoCambio> listaTipoCambio, Integer intCodigoContrato){
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "numeroComprobante"));
		Specification<TblCxcDocumento> criterio	= null;
		List<TblCxcDocumento> listaCxcAlquiler;
		//conSaldoPositivo
		criterio = Specifications.where(conCodigoContrato(intCodigoContrato))
				  .and(conSaldoCero(new BigDecimal("0")))
				  .and(conTipoReferencia(Constantes.TIPO_COBRO_ALQUILER));
		listaCxcAlquiler = cxcDocumentoDao.findAll(criterio,sort);
		if (listaTipoCambio!=null && listaTipoCambio.size()>0){
			cobroAlquiler.setTipoCambio(listaTipoCambio.get(0).getValor());
			cobroAlquiler.setFechaCobro(UtilSGT.getDatetoString(UtilSGT.getFecha("dd/MM/YYYY")));
			cobroAlquiler.setTipoMoneda(Constantes.MONEDA_DOLAR);
			cobroAlquiler.setMonto(UtilSGT.montoRedondeado(new BigDecimal("0")));
			cobroAlquiler.setCalculado(UtilSGT.montoRedondeado(new BigDecimal("0")));
		}
		return listaCxcAlquiler;
	}
	/*
	 * Listado de Servicio
	 */
	private List<TblCxcDocumento>  mListarServicio(CobroServicioBean cobroServicio, List<TblTipoCambio> listaTipoCambio, Integer intCodigoContrato){
		Sort sort = new Sort(new Sort.Order(Direction.ASC, "codigoCxcDoc"));
		Specification<TblCxcDocumento> criterio	= null;
		List<TblCxcDocumento> listaCxcServicio;
		
		criterio = Specifications.where(conCodigoContrato(intCodigoContrato))
				  .and(conSaldoPositivo(new BigDecimal("0")))
				  .and(conEstadoDocumento(Constantes.ESTADO_REGISTRO_ACTIVO))
				  .and(conTipoReferencia(Constantes.TIPO_COBRO_SERVICIO));
		listaCxcServicio = cxcDocumentoDao.findAll(criterio,sort);
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
		
		if (listaTipoCambio!=null && listaTipoCambio.size()>0){
			cobroLuz.setTipoCambioLuz(listaTipoCambio.get(0).getValor());
			cobroLuz.setFechaCobroLuz(UtilSGT.getDatetoString(UtilSGT.getFecha("dd/MM/YYYY")));
			cobroLuz.setTipoMonedaLuz(Constantes.MONEDA_SOL);
			cobroLuz.setMonto(UtilSGT.montoRedondeado(new BigDecimal("0")));
			cobroLuz.setCalculado(UtilSGT.montoRedondeado(new BigDecimal("0")));
		}
		return listaCxcLuz;
	}
	/*
	 * Listar Arbitrio 
	 */
	public List<TblArbitrio> mListarArbitrio(CobroArbitrioBean cobroArbitrio, List<TblTipoCambio> listaTipoCambio, Integer intCodigoTienda){
		Sort sort = new Sort(new Sort.Order(Direction.ASC, "codigoArbitrio"));
		Specification<TblArbitrio> criterio	= null;
		List<TblArbitrio> listaCxcArbitrio;
		
		criterio = Specifications.where(conCodigoTienda(intCodigoTienda))
								 .and(conSaldoPositivoArbitrio(new BigDecimal("0")));
		listaCxcArbitrio = arbitrioDao.findAll(criterio,sort);
		
		if (listaTipoCambio!=null && listaTipoCambio.size()>0){
			cobroArbitrio.setTipoCambioArbitrio(listaTipoCambio.get(0).getValor());
			cobroArbitrio.setFechaCobroArbitrio(UtilSGT.getDatetoString(UtilSGT.getFecha("dd/MM/YYYY")));
			cobroArbitrio.setTipoMonedaArbitrio(Constantes.MONEDA_SOL);
			cobroArbitrio.setMonto(UtilSGT.montoRedondeado(new BigDecimal("0")));
			cobroArbitrio.setCalculado(UtilSGT.montoRedondeado(new BigDecimal("0")));
		}
		
		return listaCxcArbitrio;
	}
	/*
	 * Listar Primeros Cobros 
	 */
	public List<TblCxcDocumento> mListarPrimerosCobros(CobroPrimerCobro cobroPrimerCobro, List<TblTipoCambio> listaTipoCambio, Integer intCodigoContrato){
		Sort sort = new Sort(new Sort.Order(Direction.ASC, "codigoCxcDoc"));
		Specification<TblCxcDocumento> criterio	= null;
		List<TblCxcDocumento> listaCxcPrimerCobro;
		List<TblCxcDocumento> listaCxcPrimerCobroGarantia;
		
		criterio = Specifications.where(conCodigoContrato(intCodigoContrato))
				  .and(conTipoReferencia(Constantes.TIPO_COBRO_PRIMER_COBRO));
		listaCxcPrimerCobro = cxcDocumentoDao.findAll(criterio,sort);
		//Listamos la garantia - generado en el primer cobro
		criterio = Specifications.where(conCodigoContrato(intCodigoContrato))
				  .and(conTipoReferencia(Constantes.TIPO_COBRO_GARANTIA));
		listaCxcPrimerCobroGarantia = cxcDocumentoDao.findAll(criterio,sort);
		if (listaCxcPrimerCobroGarantia!=null){
			for(TblCxcDocumento garantia: listaCxcPrimerCobroGarantia){
				if (listaCxcPrimerCobro==null){
					listaCxcPrimerCobro = new ArrayList<TblCxcDocumento>();
					listaCxcPrimerCobro.add(garantia);
				}else{
					listaCxcPrimerCobro.add(garantia);
				}
			}
		}
		
		if (listaTipoCambio!=null && listaTipoCambio.size()>0){
			cobroPrimerCobro.setTipoCambioPrimerCobro(listaTipoCambio.get(0).getValor());
			cobroPrimerCobro.setFechaCobroPrimerCobro(UtilSGT.getDatetoString(UtilSGT.getFecha("dd/MM/YYYY")));
			cobroPrimerCobro.setTipoMonedaPrimerCobro(Constantes.MONEDA_DOLAR);
			cobroPrimerCobro.setMonto(UtilSGT.montoRedondeado(new BigDecimal("0")));
			cobroPrimerCobro.setCalculado(UtilSGT.montoRedondeado(new BigDecimal("0")));
		}
		return listaCxcPrimerCobro;
	}
	/*
	 * Listar Primeros Cobros 
	 */
	public List<TblCxcDocumento> mListarGarantia(CobroGarantia cobroGarantia, List<TblTipoCambio> listaTipoCambio, Integer intCodigoContrato){
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "codigoCxcDoc"));
		Specification<TblCxcDocumento> criterio	= null;
		List<TblCxcDocumento> listaCxcPrimerCobroGarantia;
		
		//Listamos la garantia - generado en el primer cobro
		criterio = Specifications.where(conCodigoContrato(intCodigoContrato))
				  .and(conTipoReferencia(Constantes.TIPO_COBRO_GARANTIA));
		listaCxcPrimerCobroGarantia = cxcDocumentoDao.findAll(criterio,sort);
		
		
		if (listaTipoCambio!=null && listaTipoCambio.size()>0){
			cobroGarantia.setTipoCambioGarantia(listaTipoCambio.get(0).getValor());
			cobroGarantia.setFechaCobroGarantia(UtilSGT.getDatetoString(UtilSGT.getFecha("dd/MM/YYYY")));
		}
		return listaCxcPrimerCobroGarantia;
	}
	
	/**
	 * Se encarga de direccionar a la pantalla de edicion del Contrato
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "cobro/editar/{id}", method = RequestMethod.GET)
	public String editarContrato(@PathVariable Integer id, Model model, TblContrato filtro, HttpServletRequest request) {
		String path 							= "";
		try{
			
			path = "caja/cobro/cob_edicion";
			this.mEditarContrato(id, model, request);
			

		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
	}

	public void mSetBeanCobro(CobroGeneralBean cobroGeneralBean, BeanRequest beanRequest){
		cobroGeneralBean.setContrato(beanRequest.getContrato());
		cobroGeneralBean.setCobroAlquiler(beanRequest.getCobroAlquiler());
		cobroGeneralBean.setCobroServicio(beanRequest.getCobroServicio());
		cobroGeneralBean.setCobroPrimerCobro(beanRequest.getCobroPrimerCobro());
		cobroGeneralBean.setCobroLuz(beanRequest.getCobroLuz());
		cobroGeneralBean.setCobroArbitrio(beanRequest.getCobroArbitrio());
	}
	/*Adiciona el elemento al inicio de la lista*/
	public List<TblCxcDocumento> adicionaInicioListado(List<TblCxcDocumento> lista, TblCxcDocumento tblCxcDocumento){
		List<TblCxcDocumento> listaAuxiliar = new ArrayList<>();
		listaAuxiliar.add(tblCxcDocumento);
		if (lista != null){
			for(TblCxcDocumento doc : lista){
				listaAuxiliar.add(doc);
			}
		}
		
		return listaAuxiliar;
	}
	
	private void mEditarContrato(Integer id, Model model, HttpServletRequest request) throws Exception{
		TblContrato contrato 					= null;
		BeanRequest beanRequest					= null;
		List<TblContratoServicio> listaServicio	= null;
		List<TblContratoCliente> listaCliente	= null;
		List<TblContratoPrimerCobro> listaCobro	= null;
		//List<TblArbitrio> listaArbitrio			= null;
		List<TblLuzxtienda> listaLuzxtienda		= null;
		List<TblObservacion> listaObservacion	= null;
		List<TblCxcDocumento> listaCxcAlquiler	= null;
		List<TblCxcDocumento> listaCxcSinSaldo	= null;
		List<TblCxcDocumento> listaCxcAlquilerAux	= null;
		List<TblCxcDocumento> listaCxcServicio	= null;
		List<TblCxcDocumento> listaCxcServicioAux	= null;
		List<TblCxcDocumento> listaCxcLuz		= null;
		List<TblArbitrio> listaCxcArbitrio	= null;
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
			//TODO:219-09-19: Se comenta porque no se esta generando el documento de los comprobantes con saldo CERO: pendiente excluir las que son tiendas vendidas
			//listaCxcSinSaldo = this.mListarAlquilerSaldoCero(cobroAlquiler, listaTipoCambio, contrato.getCodigoContrato());
			listaCxcSinSaldo = null;
			//CxC Servicio
			cobroServicio  = new CobroServicioBean();
			listaCxcServicio= this.mListarServicio(cobroServicio, listaTipoCambio, contrato.getCodigoContrato());
			
			//CxC Luz
			cobroLuz  = new CobroLuzBean();
			listaCxcLuz= this.mListarLuz(cobroLuz, listaTipoCambio, contrato.getCodigoContrato());
			
			//CxC Arbitrio
			cobroArbitrio  = new CobroArbitrioBean();
			listaCxcArbitrio= this.mListarArbitrio(cobroArbitrio, listaTipoCambio, contrato.getTblTienda().getCodigoTienda());
			
			
			//CxC Primeros cobros
			cobroPrimerCobro = new CobroPrimerCobro();
			listaCxcPrimerCobro = this.mListarPrimerosCobros(cobroPrimerCobro, listaTipoCambio, contrato.getCodigoContrato());
			mapPrimerosCobros = this.obtenerValoresPrimerosCobros(listaCxcPrimerCobro);
			
			//Juntamos primeros cobros y alquiler
			
			listaCxcAlquilerAux = listaCxcAlquiler;
			listaCxcServicioAux = listaCxcServicio;
			if (listaCxcPrimerCobro != null){
				for(TblCxcDocumento tblCxcDocumento: listaCxcPrimerCobro){
					if (tblCxcDocumento.getNombre().contains("ALQUILER")){
						listaCxcAlquilerAux = this.adicionaInicioListado(listaCxcAlquilerAux, tblCxcDocumento);
					}
					if (tblCxcDocumento.getNombre().contains("SERVICIO")){
						listaCxcServicioAux = this.adicionaInicioListado(listaCxcServicioAux, tblCxcDocumento);
					}
				}
			}
			listaCxcAlquiler = listaCxcAlquilerAux;
			listaCxcServicio = listaCxcServicioAux;
			//CXC Garantia
			/*cobroGarantia = new CobroGarantia();
			listaCxcGarantia = this.mListarGarantia(cobroGarantia, listaTipoCambio, contrato.getCodigoContrato());**/
			
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
			listaLuzxtienda = luzxTiendaDao.listarLuzTiendaxContrato(contrato.getCodigoContrato());
			listaObservacion = observacionDao.listarObservacionxContrato(contrato.getCodigoContrato());
			beanRequest = new BeanRequest();
			
			beanRequest.setListaCxcAlquiler(listaCxcAlquiler);	
			beanRequest.setListaCxcSinSaldo(listaCxcSinSaldo);
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
			//beanRequest.setListaArbitrio(listaArbitrio);
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
			//control de los mensajes
			obtenerSaldoMesParaMensajeAlquiler(beanRequest, contrato.getCodigoContrato());
			obtenerSaldoMesParaMensajeServicio(beanRequest, contrato.getCodigoContrato());
			obtenerSaldoMesParaMensajeLuz(beanRequest, contrato.getCodigoContrato());
			obtenerSaldoMesParaMensajeArbitrio(beanRequest, contrato.getCodigoContrato());
			
			this.mSetBeanCobro(cobroGeneralBean, beanRequest);
			this.mInicializaFechaCobro(cobroGeneralBean);
			this.mInicializaTipoMoneda(cobroGeneralBean);
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
			//listaArbitrio		= null;
		}
		
	}
	/*Obtenemos el saldo y el mes del saldo para generar el mensaje*/
	private void obtenerSaldoMesParaMensajeAlquiler(BeanRequest beanRequest, Integer intCodigoContrato){
		//TblCxcDocumento alquiler = null;
		Date ultimoMesPago = null;
		
		Optional<TblCxcDocumento> optAlquiler = beanRequest.getListaCxcAlquiler().stream().findFirst();
		
		if (optAlquiler.isPresent()) {
			beanRequest.getCobroAlquiler().setSaldoMesMensaje(optAlquiler.get().getSaldo());
			beanRequest.getCobroAlquiler().setNombreMesMensaje(UtilSGT.formatFechaSGTMensaje(optAlquiler.get().getFechaFin()));
		}else {
			ultimoMesPago = cxcDocumentoDao.ultimoMesCxC(intCodigoContrato, Constantes.TIPO_COBRO_ALQUILER);
			if (ultimoMesPago!= null){
				beanRequest.getCobroAlquiler().setSaldoMesMensaje(new BigDecimal("0"));
				beanRequest.getCobroAlquiler().setNombreMesMensaje(UtilSGT.formatFechaSGTMensaje(ultimoMesPago));
			}else{
				beanRequest.getCobroAlquiler().setSaldoMesMensaje(new BigDecimal("0"));
				beanRequest.getCobroAlquiler().setNombreMesMensaje(UtilSGT.formatFechaSGTMensaje(new Date()));
			}
		}
		
		/*if (beanRequest != null){
			if (beanRequest.getListaCxcAlquiler()!=null && beanRequest.getListaCxcAlquiler().size()>0){
				alquiler = beanRequest.getListaCxcAlquiler().get(0);
				beanRequest.getCobroAlquiler().setSaldoMesMensaje(alquiler.getSaldo());
				beanRequest.getCobroAlquiler().setNombreMesMensaje(UtilSGT.formatFechaSGTMensaje(alquiler.getFechaFin()));
			}else{
				ultimoMesPago = cxcDocumentoDao.ultimoMesCxC(intCodigoContrato, Constantes.TIPO_COBRO_ALQUILER);
				if (ultimoMesPago!= null){
					beanRequest.getCobroAlquiler().setSaldoMesMensaje(new BigDecimal("0"));
					beanRequest.getCobroAlquiler().setNombreMesMensaje(UtilSGT.formatFechaSGTMensaje(ultimoMesPago));
				}else{
					beanRequest.getCobroAlquiler().setSaldoMesMensaje(new BigDecimal("0"));
					beanRequest.getCobroAlquiler().setNombreMesMensaje(UtilSGT.formatFechaSGTMensaje(new Date()));
				}
			}
		}*/
	}
	/*Obtenemos el saldo y el mes del saldo para generar el mensaje*/
	private void obtenerSaldoMesParaMensajeServicio(BeanRequest beanRequest, Integer intCodigoContrato){
		//TblCxcDocumento servicio = null;
		Date ultimoMesPago = null;
		
		Optional<TblCxcDocumento> optServicio = beanRequest.getListaCxcServicio().stream().findFirst();
		
		if (optServicio.isPresent()) {
			beanRequest.getCobroServicio().setSaldoMesMensaje(optServicio.get().getSaldo());
			beanRequest.getCobroServicio().setNombreMesMensaje(UtilSGT.formatFechaSGTMensaje(optServicio.get().getFechaFin()));
		}else {
			ultimoMesPago = cxcDocumentoDao.ultimoMesCxC(intCodigoContrato, Constantes.TIPO_COBRO_SERVICIO);
			if (ultimoMesPago!= null){
				beanRequest.getCobroServicio().setSaldoMesMensaje(new BigDecimal("0"));
				beanRequest.getCobroServicio().setNombreMesMensaje(UtilSGT.formatFechaSGTMensaje(ultimoMesPago));
			}else{
				beanRequest.getCobroServicio().setSaldoMesMensaje(new BigDecimal("0"));
				beanRequest.getCobroServicio().setNombreMesMensaje(UtilSGT.formatFechaSGTMensaje(new Date()));
			}
		}
	}
	/*Obtenemos el saldo y el mes del saldo para generar el mensaje*/
	private void obtenerSaldoMesParaMensajeLuz(BeanRequest beanRequest, Integer intCodigoContrato){
		//TblCxcDocumento luz = null;
		Date ultimoMesPago = null;
		
		Optional<TblCxcDocumento> optLuz = beanRequest.getListaCxcLuz().stream().findFirst();
		
		if (optLuz.isPresent()) {
			beanRequest.getCobroLuz().setSaldoMesMensaje(optLuz.get().getSaldo());
			beanRequest.getCobroLuz().setNombreMesMensaje(UtilSGT.formatFechaSGTMensaje(optLuz.get().getFechaFin()));
		}else {
			ultimoMesPago = cxcDocumentoDao.ultimoMesCxC(intCodigoContrato, Constantes.TIPO_COBRO_LUZ);
			if (ultimoMesPago!= null){
				beanRequest.getCobroLuz().setSaldoMesMensaje(new BigDecimal("0"));
				beanRequest.getCobroLuz().setNombreMesMensaje(UtilSGT.formatFechaSGTMensaje(ultimoMesPago));
			}else{
				beanRequest.getCobroLuz().setSaldoMesMensaje(new BigDecimal("0"));
				beanRequest.getCobroLuz().setNombreMesMensaje(UtilSGT.formatFechaSGTMensaje(new Date()));
			}
		}
	}
	/*Obtenemos el saldo y el mes del saldo para generar el mensaje*/
	private void obtenerSaldoMesParaMensajeArbitrio(BeanRequest beanRequest, Integer intCodigoContrato){
		
		Optional<TblArbitrio> optArbitrio = beanRequest.getListaCxcArbitrio().stream().findFirst();
		
		if (optArbitrio.isPresent()) {
			beanRequest.getCobroArbitrio().setSaldoMesMensaje(optArbitrio.get().getSaldo());
			beanRequest.getCobroArbitrio().setNombreMesMensaje(optArbitrio.get().getTrimestre());
		}else {
			beanRequest.getCobroArbitrio().setSaldoMesMensaje(new BigDecimal("0"));
			beanRequest.getCobroArbitrio().setNombreMesMensaje("PERIODO");
			
		}
	}

	public void mInicializaTipoMoneda(CobroGeneralBean cobroGeneralBean){
		cobroGeneralBean.getCobroAlquiler().setTipoMoneda(Constantes.MONEDA_DOLAR);
		cobroGeneralBean.getCobroAlquiler().setTipoPago(Constantes.TIPO_PAGO_COD_EFECTIVO);
		cobroGeneralBean.getCobroServicio().setTipoPago(Constantes.TIPO_PAGO_COD_EFECTIVO);
		cobroGeneralBean.getCobroLuz().setTipoPago(Constantes.TIPO_PAGO_COD_EFECTIVO);
		cobroGeneralBean.getCobroServicio().setTipoMonedaServicio(Constantes.MONEDA_SOL);
		cobroGeneralBean.getCobroLuz().setTipoMonedaLuz(Constantes.MONEDA_SOL);
		cobroGeneralBean.getCobroArbitrio().setTipoMonedaArbitrio(Constantes.MONEDA_SOL);
		cobroGeneralBean.getCobroArbitrio().setTipoPago(Constantes.TIPO_PAGO_COD_EFECTIVO);
	}
	public void mInicializaFechaCobro(CobroGeneralBean cobroGeneralBean){
		cobroGeneralBean.getCobroAlquiler().setFechaCobro(new Date());
		cobroGeneralBean.getCobroServicio().setFechaCobroServicio(new Date());
		cobroGeneralBean.getCobroLuz().setFechaCobroLuz(new Date());
		cobroGeneralBean.getCobroAlquiler().setFechaOperacion(new Date());
		cobroGeneralBean.getCobroArbitrio().setFechaCobroArbitrio(new Date());
	}

	@Override
	public TblCobro getNuevaEntidad() {
		
		return null;
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
	public void preGuardarObservacion(TblObservacion entidad, HttpServletRequest request) {
		try{
			log.debug("[preGuardarObservacion] Inicio" );
			entidad.setFechaCreacion(new Date(System.currentTimeMillis()));
			entidad.setIpCreacion(request.getRemoteAddr());
			entidad.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			log.debug("[preGuardarObservacion] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void preGuardarAdelanto(TblAdelanto entidad, HttpServletRequest request) {
		try{
			log.debug("[preGuardarAdelanto] Inicio" );
			entidad.setFechaCreacion(new Date(System.currentTimeMillis()));
			entidad.setIpCreacion(request.getRemoteAddr());
			entidad.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			log.debug("[preGuardarAdelanto] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void preGuardarDesembolso(TblDesembolso entidad, HttpServletRequest request) {
		try{
			log.debug("[preGuardarDesembolso] Inicio" );
			entidad.setFechaCreacion(new Date(System.currentTimeMillis()));
			entidad.setIpCreacion(request.getRemoteAddr());
			entidad.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			entidad.setEstadoOperacion(Constantes.ESTADO_DESEMBOLSO_ACTIVO);
			log.debug("[preGuardarDesembolso] Usuario:"+entidad.getUsuarioCreacion() );
			log.debug("[preGuardarDesembolso] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void preGuardarDesembolsoArbitrio(TblDesembolsoArbitrio entidad, HttpServletRequest request) {
		try{
			log.debug("[preGuardarDesembolsoArbitrio] Inicio" );
			entidad.setFechaCreacion(new Date(System.currentTimeMillis()));
			entidad.setIpCreacion(request.getRemoteAddr());
			entidad.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			entidad.setEstadoOperacion(Constantes.ESTADO_DESEMBOLSO_ACTIVO);
			log.debug("[preGuardarDesembolsoArbitrio] Usuario:"+entidad.getUsuarioCreacion() );
			log.debug("[preGuardarDesembolsoArbitrio] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	 * Registra el adelanto
	 */
	public boolean mRegistrarAdelanto(TblCobro cobro, TblContrato tblContrato, HttpServletRequest request, String strTipoRubro){
		boolean resultado = false;
		TblAdelanto adelanto = null;
	
		try{
			if (cobro!=null && (cobro.getMontoDolares().compareTo(new BigDecimal("0"))>0 || cobro.getMontoSoles().compareTo(new BigDecimal("0"))>0)){
				adelanto = new TblAdelanto();
				adelanto.setTipoRubro(strTipoRubro);
				adelanto.setFechaAdelanto(UtilSGT.getDatetoString(UtilSGT.getFecha("dd/MM/YYYY")));
				adelanto.setMontoDolares(cobro.getMontoDolares());
				adelanto.setMontoSoles(cobro.getMontoSoles());
				adelanto.setTipoCambio(cobro.getTipoCambio());
				adelanto.setTipoMoneda(cobro.getTipoMoneda());
				adelanto.setTblContrato(tblContrato);
				adelanto.setCodigoDesembolso(cobro.getTblDesembolso().getCodigoDesembolso());
				this.preGuardarAdelanto(adelanto, request);
				adelantoDao.save(adelanto);
			}
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultado;
	}
	public boolean mRegistrarAdelantoArbitrio(TblCobroArbitrio cobro, TblContrato tblContrato, HttpServletRequest request, String strTipoRubro){
		boolean resultado = false;
		TblAdelanto adelanto = null;
	
		try{
			if (cobro!=null && (cobro.getMontoDolares().compareTo(new BigDecimal("0"))>0 || cobro.getMontoSoles().compareTo(new BigDecimal("0"))>0)){
				adelanto = new TblAdelanto();
				adelanto.setTipoRubro(strTipoRubro);
				adelanto.setFechaAdelanto(UtilSGT.getDatetoString(UtilSGT.getFecha("dd/MM/YYYY")));
				adelanto.setMontoDolares(cobro.getMontoDolares());
				adelanto.setMontoSoles(cobro.getMontoSoles());
				adelanto.setTipoCambio(cobro.getTipoCambio());
				adelanto.setTipoMoneda(cobro.getTipoMoneda());
				adelanto.setTblContrato(tblContrato);
				adelanto.setCodigoDesembolso(cobro.getTblDesembolsoArbitrio().getCodigoDesembolsoArbitrio());
				this.preGuardarAdelanto(adelanto, request);
				adelantoDao.save(adelanto);
			}
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultado;
	}
	
	

	/*
	 * Valida la existencia de adelanto, dado que el monto cobrado supera el saldo de la deuda
	 */
	public boolean mValidarExisteciaAdelanto(List<TblCxcDocumento> listaDeuda, TblCobro cobro){
		boolean resultado 		= false;
		BigDecimal montoDeuda 	= new BigDecimal("0");
		BigDecimal montoCobro 	= new BigDecimal("0");
		String tipoMoneda		= null;
		try{
			//Totalizamos el saldo de la deuda
			if (listaDeuda!=null && listaDeuda.size()>0){
				for(TblCxcDocumento documento: listaDeuda){
					montoDeuda = montoDeuda.add(documento.getSaldo());
					tipoMoneda = documento.getTipoMoneda();
				}
				
			}else{
				return true;
			}
			//Totalizamos el monto cobrado
			if (tipoMoneda.equals(Constantes.MONEDA_DOLAR)){
				montoCobro = cobro.getMontoDolares();
			}else{
				montoCobro = cobro.getMontoSoles();
			}
					
			//Comparamos para identificar
			if ( montoCobro.compareTo(montoDeuda)<=0){
				resultado 		= false;
			}else{
				resultado 		= true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultado;
	}
	public boolean mValidarExisteciaAdelantoArbitrio(List<TblArbitrio> listaDeuda, TblCobroArbitrio cobro){
		boolean resultado 		= false;
		BigDecimal montoDeuda 	= new BigDecimal("0");
		BigDecimal montoCobro 	= new BigDecimal("0");
		String tipoMoneda		= null;
		try{
			//Totalizamos el saldo de la deuda
			if (listaDeuda!=null && listaDeuda.size()>0){
				for(TblArbitrio documento: listaDeuda){
					montoDeuda = montoDeuda.add(documento.getSaldo());
					tipoMoneda = Constantes.MONEDA_SOL;
				}
				
			}else{
				return true;
			}
			//Totalizamos el monto cobrado
			if (tipoMoneda.equals(Constantes.MONEDA_DOLAR)){
				montoCobro = cobro.getMontoDolares();
			}else{
				montoCobro = cobro.getMontoSoles();
			}
					
			//Comparamos para identificar
			if ( montoCobro.compareTo(montoDeuda)<=0){
				resultado 		= false;
			}else{
				resultado 		= true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultado;
	}
	/*
	 * Registra los desembolsos del cliente
	 */
	public void mRegistrarDesembolso(TblCobro cobro, Integer intCodigoContrato, HttpServletRequest request, String strTipoCobro) throws Exception{
		TblDesembolso desembolso = null;
		try{
			if (cobro!=null && (cobro.getMontoDolares().doubleValue()>0 || cobro.getMontoSoles().doubleValue()>0)){
				desembolso = new TblDesembolso();
				desembolso.setCodigoContrato(intCodigoContrato);
				desembolso.setFechaCobro(cobro.getFechaCobro());
				desembolso.setMontoDolares(cobro.getMontoDolares());
				desembolso.setMontoSoles(cobro.getMontoSoles());
				desembolso.setTipoCambio(cobro.getTipoCambio());
				desembolso.setTipoMoneda(cobro.getTipoMoneda());
				//desembolso.setIdentificacion(UtilSGT.getFecha(" yyyy-MM-dd kk:mm:ss").concat(intCodigoContrato.toString()));
				desembolso.setIdentificacion(UUID.randomUUID().toString());
				desembolso.setTipoCobro(strTipoCobro);
				//Bancarizado
				this.validateDatosBancarizado(desembolso, request, strTipoCobro);
				this.preGuardarDesembolso(desembolso, request);
				desembolsoDao.save(desembolso);
				desembolso = desembolsoDao.getDesembolsoxIdentificacion(desembolso.getIdentificacion());
				cobro.setTblDesembolso(desembolso);
				
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	/*
	 * Registra los desembolsos del cliente
	 */
	public void mRegistrarDesembolsoArbitrio(TblCobroArbitrio cobro, Integer intCodigoContrato, HttpServletRequest request, String strTipoCobro) throws Exception{
		TblDesembolsoArbitrio desembolso = null;
		try{
			if (cobro!=null && (cobro.getMontoDolares().doubleValue()>0 || cobro.getMontoSoles().doubleValue()>0)){
				desembolso = new TblDesembolsoArbitrio();
				desembolso.setCodigoContrato(intCodigoContrato);
				desembolso.setFechaCobro(cobro.getFechaCobro());
				desembolso.setMontoDolares(cobro.getMontoDolares());
				desembolso.setMontoSoles(cobro.getMontoSoles());
				desembolso.setTipoCambio(cobro.getTipoCambio());
				desembolso.setTipoMoneda(cobro.getTipoMoneda());
				//desembolso.setIdentificacion(UtilSGT.getFecha(" yyyy-MM-dd kk:mm:ss").concat(intCodigoContrato.toString()));
				desembolso.setIdentificacion(UUID.randomUUID().toString());
				desembolso.setTipoCobro(strTipoCobro);
				//Bancarizado
				this.validateDatosBancarizadoArbitrio(desembolso, request, strTipoCobro);
				this.preGuardarDesembolsoArbitrio(desembolso, request);
				desembolsoArbitrioDao.save(desembolso);
				desembolso = desembolsoArbitrioDao.getDesembolsoxIdentificacion(desembolso.getIdentificacion());
				cobro.setTblDesembolsoArbitrio(desembolso);
				
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	private void validateDatosBancarizado(TblDesembolso desembolso, HttpServletRequest request, String strTipoCobro){
		BeanRequest beanRequest							= null;
		CobroGeneralBean cobroGeneralBean				= null;
		
		beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
		cobroGeneralBean = beanRequest.getCobroGeneralBean();
		//Datos bancarizados del alquiler
		if (strTipoCobro.equals(Constantes.TIPO_COBRO_ALQUILER)){
			this.setDatosBancarizadosAlquiler(desembolso, cobroGeneralBean, strTipoCobro);
		}
		//Datos bancarizados del servicio
		if (strTipoCobro.equals(Constantes.TIPO_COBRO_SERVICIO)){
			this.setDatosBancarizadosServicio(desembolso, cobroGeneralBean, strTipoCobro);
		}
		//Datos bancarizados de la luz
		if (strTipoCobro.equals(Constantes.TIPO_COBRO_LUZ)){
			this.setDatosBancarizadosLuz(desembolso, cobroGeneralBean, strTipoCobro);
		}
		
	}
	private void validateDatosBancarizadoArbitrio(TblDesembolsoArbitrio desembolso, HttpServletRequest request, String strTipoCobro){
		BeanRequest beanRequest							= null;
		CobroGeneralBean cobroGeneralBean				= null;
		
		beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
		cobroGeneralBean = beanRequest.getCobroGeneralBean();
		//Datos bancarizados del alquiler
		if (strTipoCobro.equals(Constantes.TIPO_COBRO_ARBITRIO)){
			this.setDatosBancarizadosArbitrio(desembolso, cobroGeneralBean, strTipoCobro);
		}
		
	}
	/*Datos bancarizados del alquiler*/
	private void setDatosBancarizadosAlquiler(TblDesembolso desembolso, CobroGeneralBean cobroGeneralBean,  String strTipoCobro){
		if (cobroGeneralBean.getCobroAlquiler().getTipoPago().equals(Constantes.TIPO_PAGO_COD_BANCARIZADO)){
			desembolso.setTipoPago(cobroGeneralBean.getCobroAlquiler().getTipoPago());
			desembolso.setTipoBancarizado(cobroGeneralBean.getCobroAlquiler().getTipoBancarizado());
			desembolso.setNumeroOperacion(cobroGeneralBean.getCobroAlquiler().getNumeroOperacion());
			desembolso.setFechaOperacion(cobroGeneralBean.getCobroAlquiler().getFechaOperacion());
		}
		if (cobroGeneralBean.getCobroAlquiler().getTipoPago().equals(Constantes.TIPO_PAGO_COD_EFECTIVO)){
			desembolso.setTipoPago(cobroGeneralBean.getCobroAlquiler().getTipoPago());
		}
	}
	/*Datos bancarizados del servicio*/
	private void setDatosBancarizadosServicio(TblDesembolso desembolso, CobroGeneralBean cobroGeneralBean,  String strTipoCobro){
		if (cobroGeneralBean.getCobroServicio().getTipoPago().equals(Constantes.TIPO_PAGO_COD_BANCARIZADO)){
			desembolso.setTipoPago(cobroGeneralBean.getCobroServicio().getTipoPago());
			desembolso.setTipoBancarizado(cobroGeneralBean.getCobroServicio().getTipoBancarizado());
			desembolso.setNumeroOperacion(cobroGeneralBean.getCobroServicio().getNumeroOperacion());
			desembolso.setFechaOperacion(cobroGeneralBean.getCobroServicio().getFechaOperacion());
		}
		if (cobroGeneralBean.getCobroAlquiler().getTipoPago().equals(Constantes.TIPO_PAGO_COD_EFECTIVO)){
			desembolso.setTipoPago(cobroGeneralBean.getCobroServicio().getTipoPago());
		}
	}
	/*Datos bancarizados del Luz*/
	private void setDatosBancarizadosLuz(TblDesembolso desembolso, CobroGeneralBean cobroGeneralBean,  String strTipoCobro){
		if (cobroGeneralBean.getCobroLuz().getTipoPago().equals(Constantes.TIPO_PAGO_COD_BANCARIZADO)){
			desembolso.setTipoPago(cobroGeneralBean.getCobroLuz().getTipoPago());
			desembolso.setTipoBancarizado(cobroGeneralBean.getCobroLuz().getTipoBancarizado());
			desembolso.setNumeroOperacion(cobroGeneralBean.getCobroLuz().getNumeroOperacion());
			desembolso.setFechaOperacion(cobroGeneralBean.getCobroLuz().getFechaOperacion());
		}
		if (cobroGeneralBean.getCobroLuz().getTipoPago().equals(Constantes.TIPO_PAGO_COD_EFECTIVO)){
			desembolso.setTipoPago(cobroGeneralBean.getCobroLuz().getTipoPago());
		}
	}
	/*Datos bancarizados del Arbitrio*/
	private void setDatosBancarizadosArbitrio(TblDesembolsoArbitrio desembolso, CobroGeneralBean cobroGeneralBean,  String strTipoCobro){
		if (cobroGeneralBean.getCobroArbitrio().getTipoPago().equals(Constantes.TIPO_PAGO_COD_BANCARIZADO)){
			desembolso.setTipoPago(cobroGeneralBean.getCobroArbitrio().getTipoPago());
			desembolso.setTipoBancarizado(cobroGeneralBean.getCobroArbitrio().getTipoBancarizado());
			desembolso.setNumeroOperacion(cobroGeneralBean.getCobroArbitrio().getNumeroOperacion());
			desembolso.setFechaOperacion(cobroGeneralBean.getCobroArbitrio().getFechaOperacion());
		}
		if (cobroGeneralBean.getCobroArbitrio().getTipoPago().equals(Constantes.TIPO_PAGO_COD_EFECTIVO)){
			desembolso.setTipoPago(cobroGeneralBean.getCobroArbitrio().getTipoPago());
		}
	}
	
	
	
	public void mActualizarDesembolsoNota(TblCobro cobro, String nota) throws Exception{
		TblDesembolso desembolso = null;
		try{
			desembolso = desembolsoDao.getDesembolsoByCodigoInterno(cobro.getTblDesembolso().getIdentificacion());
			desembolso.setNota(validaLongitudDato(nota,256));
			desembolsoDao.save(desembolso);
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	
	public void mActualizarDesembolsoNotaArbitrio(TblCobroArbitrio cobro, String nota) throws Exception{
		TblDesembolsoArbitrio desembolso = null;
		try{
			desembolso = desembolsoArbitrioDao.getDesembolsoByCodigoInterno(cobro.getTblDesembolsoArbitrio().getIdentificacion());
			desembolso.setNota(validaLongitudDato(nota,256));
			desembolsoArbitrioDao.save(desembolso);
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	
	private String validaLongitudDato(String dato, int longitud) {
		if (dato != null && dato.length() > longitud) {
			return dato.substring(0, longitud - 4) + "...";
		}else {
			return dato;
		}
	}
	
	/*
	 * Registro de cobro en dolares sin saldo en la deuda
	 */
	public TblCobro mRegistrarCobroContratoDolaresSinSaldo(TblCobro cobroAlquiler, TblCobro cobro, TblCxcDocumento documento, HttpServletRequest request){
		cobroAlquiler.setFechaCobro(cobro.getFechaCobro());
		cobroAlquiler.setMontoDolares(documento.getSaldo());
		cobroAlquiler.setTipoCambio(cobro.getTipoCambio());
		cobroAlquiler.setMontoSoles(UtilSGT.montoMultiplica(documento.getSaldo(), cobro.getTipoCambio()));
		cobroAlquiler.setTblCxcDocumento(documento);
		cobroAlquiler.setTipoCobro(documento.getTipoReferencia());
		cobroAlquiler.setTblDesembolso(cobro.getTblDesembolso());
		cobroAlquiler.setTipoMoneda(cobro.getTipoMoneda());
		this.preGuardarCobro(cobroAlquiler, request);
		cobroDao.save(cobroAlquiler);
		
		return cobroAlquiler;
	}
	/*
	 * Registro de cobro en dolares Con saldo en la deuda
	 */
	public TblCobro mRegistrarCobroContratoDolaresConSaldo(TblCobro cobroAlquiler, TblCobro cobro, TblCxcDocumento documento, HttpServletRequest request){
		cobroAlquiler.setFechaCobro(cobro.getFechaCobro());
		cobroAlquiler.setMontoDolares(cobro.getMontoDolares());
		cobroAlquiler.setTipoCambio(cobro.getTipoCambio());
		cobroAlquiler.setMontoSoles(UtilSGT.montoMultiplica(cobro.getMontoDolares(),cobro.getTipoCambio()));
		cobroAlquiler.setTblCxcDocumento(documento);
		cobroAlquiler.setTipoCobro(documento.getTipoReferencia());
		cobroAlquiler.setTblDesembolso(cobro.getTblDesembolso());
		cobroAlquiler.setTipoMoneda(cobro.getTipoMoneda());
		this.preGuardarCobro(cobroAlquiler, request);
		cobroDao.save(cobroAlquiler);
		
		return cobroAlquiler;
	}
	/*
	 * Registro de cobro en Soles sin saldo en la deuda
	 */
	public TblCobro mRegistrarCobroContratoSolesSinSaldo(TblCobro cobroAlquiler, TblCobro cobro, TblCxcDocumento documento, HttpServletRequest request){
		cobroAlquiler.setFechaCobro(cobro.getFechaCobro());
		cobroAlquiler.setMontoDolares(UtilSGT.montoDivide(documento.getSaldo(), cobro.getTipoCambio()));
		cobroAlquiler.setTipoCambio(cobro.getTipoCambio());
		cobroAlquiler.setMontoSoles(documento.getSaldo());
		cobroAlquiler.setTblCxcDocumento(documento);
		cobroAlquiler.setTipoCobro(documento.getTipoReferencia());
		cobroAlquiler.setTblDesembolso(cobro.getTblDesembolso());
		cobroAlquiler.setTipoMoneda(cobro.getTipoMoneda());
		this.preGuardarCobro(cobroAlquiler, request);
		cobroDao.save(cobroAlquiler);
		
		return cobroAlquiler;
	}
	public TblCobroArbitrio mRegistrarCobroContratoSolesSinSaldoArbitrio(TblCobroArbitrio cobroArbitrio, TblCobroArbitrio cobro, TblArbitrio documento, HttpServletRequest request){
		cobroArbitrio.setFechaCobro(cobro.getFechaCobro());
		cobroArbitrio.setMontoDolares(UtilSGT.montoDivide(documento.getSaldo(), cobro.getTipoCambio()));
		cobroArbitrio.setTipoCambio(cobro.getTipoCambio());
		cobroArbitrio.setMontoSoles(documento.getSaldo());
		cobroArbitrio.setTblDesembolsoArbitrio(cobro.getTblDesembolsoArbitrio());
		cobroArbitrio.setTipoMoneda(cobro.getTipoMoneda());
		cobroArbitrio.setTipoCobro(Constantes.TIPO_COBRO_ARBITRIO);
		cobroArbitrio.setCodigoArbitrio(documento.getCodigoArbitrio());
		this.preGuardarCobroArbitrio(cobroArbitrio, request);
		cobroArbitrioDao.save(cobroArbitrio);
		
		return cobroArbitrio;
	}
	/*
	 * Registro de cobro en Soles Con saldo en la deuda
	 */
	public TblCobro mRegistrarCobroContratoSolesConSaldo(TblCobro cobroAlquiler, TblCobro cobro, TblCxcDocumento documento, HttpServletRequest request){
		cobroAlquiler.setFechaCobro(cobro.getFechaCobro());
		cobroAlquiler.setMontoDolares(UtilSGT.montoDivide(cobro.getMontoSoles(), cobro.getTipoCambio()));
		cobroAlquiler.setTipoCambio(cobro.getTipoCambio());
		cobroAlquiler.setMontoSoles(cobro.getMontoSoles());
		cobroAlquiler.setTblCxcDocumento(documento);
		cobroAlquiler.setTipoCobro(documento.getTipoReferencia());
		cobroAlquiler.setTblDesembolso(cobro.getTblDesembolso());
		cobroAlquiler.setTipoMoneda(cobro.getTipoMoneda());
		this.preGuardarCobro(cobroAlquiler, request);
		cobroDao.save(cobroAlquiler);
		
		return cobroAlquiler;
	}
	public TblCobroArbitrio mRegistrarCobroContratoSolesConSaldoArbitrio(TblCobroArbitrio cobroArbitrio, TblCobroArbitrio cobro, TblArbitrio documento, HttpServletRequest request){
		cobroArbitrio.setFechaCobro(cobro.getFechaCobro());
		cobroArbitrio.setMontoDolares(UtilSGT.montoDivide(cobro.getMontoSoles(), cobro.getTipoCambio()));
		cobroArbitrio.setTipoCambio(cobro.getTipoCambio());
		cobroArbitrio.setMontoSoles(cobro.getMontoSoles());
		
		cobroArbitrio.setTipoCobro(Constantes.TIPO_COBRO_ARBITRIO);
		cobroArbitrio.setTblDesembolsoArbitrio(cobro.getTblDesembolsoArbitrio());
		cobroArbitrio.setTipoMoneda(cobro.getTipoMoneda());
		cobroArbitrio.setCodigoArbitrio(documento.getCodigoArbitrio());
		this.preGuardarCobroArbitrio(cobroArbitrio, request);
		cobroArbitrioDao.save(cobroArbitrio);
		
		return cobroArbitrio;
	}
	/*
	 * Listar Arbitrio 
	 */
	public List<TblArbitrio> mListarDeudaArbitrio(Integer intCodigoTienda){
		Sort sort = new Sort(new Sort.Order(Direction.ASC, "codigoArbitrio"));
		Specification<TblArbitrio> criterio	= null;
		List<TblArbitrio> listaCxcArbitrio;
		criterio = Specifications.where(conCodigoTienda(intCodigoTienda))
				 				 .and(conSaldoPositivoArbitrio(new BigDecimal("0")));
		listaCxcArbitrio = arbitrioDao.findAll(criterio,sort);
		return listaCxcArbitrio;
	}
	/*
	 * Listamos las deudas: Cuentas por cobrar
	 */
	public List<TblCxcDocumento> mListarDeuda(TblContrato contrato, HttpServletRequest request, String strTipoCobro){
		List<TblCxcDocumento> listaDeuda	 		= null;
		//List<TblCxcDocumento> listaDeudaGarantia 	= null;
		Sort sort 									= new Sort(new Sort.Order(Direction.ASC, "codigoCxcDoc"));
		Specification<TblCxcDocumento> criterio		= null;
		
		criterio = Specifications.where(conCodigoContrato(contrato.getCodigoContrato()))
				  .and(conTipoReferencia(strTipoCobro))
				  .and(conSaldoPositivo(new BigDecimal("0")));
		listaDeuda = cxcDocumentoDao.findAll(criterio,sort);
		/*
		if (strTipoCobro.equals(Constantes.TIPO_COBRO_PRIMER_COBRO)){
			criterio = Specifications.where(conCodigoContrato(contrato.getCodigoContrato()))
					  .and(conTipoReferencia(strTipoCobro))
					  .and(conSaldoPositivo(new BigDecimal("0")));
			listaDeuda = cxcDocumentoDao.findAll(criterio,sort);
			//Buscamos la garantia
			criterio = Specifications.where(conCodigoContrato(contrato.getCodigoContrato()))
					  .and(conTipoReferencia(Constantes.TIPO_COBRO_GARANTIA))
					  .and(conSaldoPositivo(new BigDecimal("0")));
			listaDeudaGarantia = cxcDocumentoDao.findAll(criterio,sort);
			if (listaDeudaGarantia!=null){
				for(TblCxcDocumento garantia: listaDeudaGarantia){
					if (listaDeuda == null){
						listaDeuda = new ArrayList<TblCxcDocumento>();
						listaDeuda.add(garantia);
					}else{
						listaDeuda.add(garantia);
					}
				}
			}
			
		}else{
			criterio = Specifications.where(conCodigoContrato(contrato.getCodigoContrato()))
					  .and(conTipoReferencia(strTipoCobro))
					  .and(conSaldoPositivo(new BigDecimal("0")));
			listaDeuda = cxcDocumentoDao.findAll(criterio,sort);
		}*/
		return listaDeuda;
	}
	
	public void setMontoAlquiler(TblCobro cobro, CobroBean cobroBean){
		cobro.setMontoDolares(cobroBean.getMontoDolares());
		cobro.setMontoSoles(cobroBean.getMontoSoles());
		cobro.setFechaCobro(cobroBean.getFechaCobro());
		cobro.setTipoCambio(cobroBean.getTipoCambio());
		cobro.setTipoMoneda(cobroBean.getTipoMoneda());
	}
	public void setMontoServicio(TblCobro cobro, CobroServicioBean cobroBean){
		cobro.setMontoDolares(cobroBean.getMontoDolaresServicio());
		cobro.setMontoSoles(cobroBean.getMontoSolesServicio());
		cobro.setFechaCobro(cobroBean.getFechaCobroServicio());
		cobro.setTipoCambio(cobroBean.getTipoCambioServicio());
		cobro.setTipoMoneda(cobroBean.getTipoMonedaServicio());
	}
	public void setMontoLuz(TblCobro cobro, CobroLuzBean cobroBean){
		cobro.setMontoDolares(cobroBean.getMontoDolaresLuz());
		cobro.setMontoSoles(cobroBean.getMontoSolesLuz());
		cobro.setFechaCobro(cobroBean.getFechaCobroLuz());
		cobro.setTipoCambio(cobroBean.getTipoCambioLuz());
		cobro.setTipoMoneda(cobroBean.getTipoMonedaLuz());
	}
	public void setMontoArbitrio(TblCobroArbitrio cobro, CobroArbitrioBean cobroBean){
		cobro.setMontoDolares(cobroBean.getMontoDolaresArbitrio());
		cobro.setMontoSoles(cobroBean.getMontoSolesArbitrio());
		cobro.setFechaCobro(cobroBean.getFechaCobroArbitrio());
		cobro.setTipoCambio(cobroBean.getTipoCambioArbitrio());
		cobro.setTipoMoneda(cobroBean.getTipoMonedaArbitrio());
	}
	public void setMontoPrimerCobro(TblCobro cobro, CobroPrimerCobro cobroBean){
		cobro.setMontoDolares(cobroBean.getMontoDolaresPrimerCobro());
		cobro.setMontoSoles(cobroBean.getMontoSolesPrimerCobro());
		cobro.setFechaCobro(cobroBean.getFechaCobroPrimerCobro());
		cobro.setTipoCambio(cobroBean.getTipoCambioPrimerCobro());
		cobro.setTipoMoneda(cobroBean.getTipoMonedaPrimerCobro());
	}
	public String setMensajeCompleto(String notaPago, TblCxcDocumento documento, String strTipoCobro){
		if (strTipoCobro.equals(Constantes.TIPO_COBRO_ALQUILER)){
			notaPago = notaPago + " ALQUILER: Cancela mes de "+ documento.getAnio() + "-"+UtilSGT.getMesPersonalizado(documento.getMes()) + ".";
		}else if (strTipoCobro.equals(Constantes.TIPO_COBRO_SERVICIO)){
			notaPago = notaPago + " SERVICIO: Cancela mes de "+ documento.getAnio() + "-"+UtilSGT.getMesPersonalizado(documento.getMes()) + ".";
		}else if (strTipoCobro.equals(Constantes.TIPO_COBRO_LUZ)){
			notaPago = notaPago + " LUZ: Cancela mes de "+ documento.getAnio() + "-"+UtilSGT.getMesPersonalizado(documento.getMes()) + ".";
		}else{
			notaPago = notaPago + " Cancela mes de "+ documento.getAnio() + "-"+UtilSGT.getMesPersonalizado(documento.getMes()) + ".";
		}
		
		return notaPago;
	}
	public String setMensajeCompletoArbitrio(String notaPago, TblArbitrio documento, String strTipoCobro){
		if (strTipoCobro.equals(Constantes.TIPO_COBRO_ARBITRIO)){
			notaPago = notaPago + " ARBITRIO: Cancela el periodo: "+ documento.getTrimestre() + ".";
		}else{
			notaPago = notaPago + " Cancela el periodo "+ documento.getTrimestre() + ".";
		}
		
		return notaPago;
	}
	public String setMensajeParcial(String notaPago, TblCxcDocumento documento, String strTipoCobro){
		if (strTipoCobro.equals(Constantes.TIPO_COBRO_ALQUILER)){
			notaPago = notaPago + " ALQUILER: A cuenta del mes de "+ documento.getAnio() + "-"+ UtilSGT.getMesPersonalizado(documento.getMes())+ ".";
		}else if (strTipoCobro.equals(Constantes.TIPO_COBRO_SERVICIO)){
			notaPago = notaPago + " SERVICIO: A cuenta del mes de "+ documento.getAnio() + "-"+UtilSGT.getMesPersonalizado(documento.getMes())+ ".";
		}else if (strTipoCobro.equals(Constantes.TIPO_COBRO_LUZ)){
			notaPago = notaPago + " LUZ: A cuenta del mes de "+ documento.getAnio() + "-"+UtilSGT.getMesPersonalizado(documento.getMes())+ ".";
		}else{
			notaPago = notaPago + " A cuenta del mes de "+ documento.getAnio() + "-"+UtilSGT.getMesPersonalizado(documento.getMes())+ ".";
		}
		
		return notaPago;
	}
	public String setMensajeParcialArbitrio(String notaPago, TblArbitrio documento, String strTipoCobro){
		if (strTipoCobro.equals(Constantes.TIPO_COBRO_ARBITRIO)){
			notaPago = notaPago + " ARBITRIO: A cuenta del periodo: "+ documento.getTrimestre() + ".";
		}else{
			notaPago = notaPago + " A cuenta del periodo: "+ documento.getTrimestre() + ".";
		}
		
		return notaPago;
	}
	/*Obtenemos el parametro considerado marge de diferencia, considerado para el saldo cero*/
	@SuppressWarnings("unchecked")
	private BigDecimal getSaldoCero(HttpServletRequest request, String strTipoCobro){
		BigDecimal saldo 								= null;
		Map<String, TblParametro> mapParametro			= null;
		TblParametro parametro =null;
		if (strTipoCobro.equals("ALQ")) {
			mapParametro = (Map<String, TblParametro>) request.getSession().getAttribute("SessionMapParametros");
			try{
				parametro = mapParametro.get(Constantes.PARAMETRO_SALDO_CERO);
				if (parametro!=null){
					saldo = parametro.getValor();
				}else{
					saldo = new BigDecimal("1");
				}
			}catch(Exception e){
				saldo = new BigDecimal("1");
			}
		}else {
			saldo = new BigDecimal("0");
		}
			
		
		return saldo;
	}
	
	private boolean validarDiferencia(BigDecimal monto, BigDecimal saldo, BigDecimal margen){
		boolean resultado = false;
		log.debug("[validarDiferencia] monto:"+monto+" saldo:"+saldo+" marge:"+margen);
		log.debug("[validarDiferencia] absoluto:"+monto.subtract(saldo).abs());
		log.debug("[validarDiferencia] comparacion:"+monto.subtract(saldo).abs().compareTo(margen));
		if (monto.subtract(saldo).compareTo(margen)>0) {
			resultado = true; //pago en exceso
		}else if(monto.subtract(saldo).abs().compareTo(margen)<=0) {
			resultado = true; //pago exacto
		}else {
			resultado = false; //falto para pagar
		}

		return resultado;
		
	}
	private String getDiferencia(BigDecimal monto, BigDecimal saldo, BigDecimal margen){
		String resultado = KEY_ESTADO_PAGO;
		log.debug("[getDiferencia] monto:"+monto+" saldo:"+saldo+" marge:"+margen);
		log.debug("[getDiferencia] absoluto:"+monto.subtract(saldo).abs());
		log.debug("[getDiferencia] comparacion:"+monto.subtract(saldo).abs().compareTo(margen));
		if (monto.subtract(saldo).compareTo(margen)>0) {
			resultado = PAGO_EXCESO; //pago en exceso
		}else if(monto.subtract(saldo).abs().compareTo(margen)<=0) {
			resultado = PAGO_EXACTO; //pago exacto
		}else {
			resultado = PAGO_FALTATE; //falto para pagar
		}

		return resultado;
		
	}
	
	private TblCobro calcularNuevoCobroDolares(TblCobro cobro,TblCobro cobroAlquiler,BigDecimal margeSaldo) {
		if (this.validarDiferencia(cobro.getMontoDolares(),cobroAlquiler.getMontoDolares(),margeSaldo)){
			if (cobro.getMontoDolares().subtract(cobroAlquiler.getMontoDolares()).compareTo(margeSaldo)>0) {
				//El resto (pago - deuda) es mayor al margen permitido, mayor a 1 por ej.
				cobro.setMontoDolares(cobro.getMontoDolares().subtract(cobroAlquiler.getMontoDolares()));
				cobro.setMontoSoles(cobro.getMontoSoles().subtract(cobroAlquiler.getMontoSoles()));
			}else {
				//El resto (pago - deuda) es menor o igual al margen permitido.
				setCobroMontosEnCero(cobro);
			}
		}else{
			//El resto (pago - deuda) es menor a cero
			setCobroMontosEnCero(cobro);
		}
		return cobro;
	}
	private TblCobro calcularNuevoCobroSoles(TblCobro cobro,TblCobro cobroAlquiler,BigDecimal margeSaldo) {
		if (this.validarDiferencia(cobro.getMontoSoles(),cobroAlquiler.getMontoSoles(),margeSaldo)){
			if (cobro.getMontoSoles().subtract(cobroAlquiler.getMontoSoles()).compareTo(margeSaldo)>0) {
				//El resto (pago - deuda) es mayor al margen permitido, mayor a 1 por ej.
				cobro.setMontoDolares(cobro.getMontoDolares().subtract(cobroAlquiler.getMontoDolares()));
				cobro.setMontoSoles(cobro.getMontoSoles().subtract(cobroAlquiler.getMontoSoles()));
			}else {
				//El resto (pago - deuda) es menor o igual al margen permitido.
				setCobroMontosEnCero(cobro);
			}
		}else{
			//El resto (pago - deuda) es menor a cero
			setCobroMontosEnCero(cobro);
		}
		return cobro;
	}
	private TblCobroArbitrio calcularNuevoCobroSolesArbitrio(TblCobroArbitrio cobro,TblCobroArbitrio cobroArbitrio,BigDecimal margeSaldo) {
		if (this.validarDiferencia(cobro.getMontoSoles(),cobroArbitrio.getMontoSoles(),margeSaldo)){
			if (cobro.getMontoSoles().subtract(cobroArbitrio.getMontoSoles()).compareTo(margeSaldo)>0) {
				//El resto (pago - deuda) es mayor al margen permitido, mayor a 1 por ej.
				cobro.setMontoDolares(cobro.getMontoDolares().subtract(cobroArbitrio.getMontoDolares()));
				cobro.setMontoSoles(cobro.getMontoSoles().subtract(cobroArbitrio.getMontoSoles()));
			}else {
				//El resto (pago - deuda) es menor o igual al margen permitido.
				setCobroMontosEnCeroArbitrio(cobro);
			}
		}else{
			//El resto (pago - deuda) es menor a cero
			setCobroMontosEnCeroArbitrio(cobro);
		}
		return cobro;
	}
	
	private String registraCobroTotalDolares(TblCobro cobro,TblCxcDocumento documento, HttpServletRequest request,BigDecimal margeSaldo, String strTipoCobro) {
		TblCobro cobroAlquiler = new TblCobro();
		this.mRegistrarCobroContratoDolaresSinSaldo(cobroAlquiler, cobro, documento, request);
		documento.setMontoCobrado(documento.getMontoCobrado().add(documento.getSaldo()));											
		documento.setSaldo(new BigDecimal("0"));
		this.preEditarDocumento(documento, request);
		cxcDocumentoDao.save(documento);
		cobro = calcularNuevoCobroDolares(cobro,cobroAlquiler,margeSaldo);
		return this.setMensajeCompleto("", documento, strTipoCobro);
	}
	
	private String registraCobroTotalSoles(TblCobro cobro,TblCxcDocumento documento, HttpServletRequest request,BigDecimal margeSaldo, String strTipoCobro) {
		TblCobro cobroAlquiler = new TblCobro();
		this.mRegistrarCobroContratoSolesSinSaldo(cobroAlquiler, cobro, documento, request);
		documento.setMontoCobrado(documento.getMontoCobrado().add(documento.getSaldo()));
		documento.setSaldo(new BigDecimal("0"));
		this.preEditarDocumento(documento, request);
		cxcDocumentoDao.save(documento);
		cobro = calcularNuevoCobroSoles(cobro, cobroAlquiler, margeSaldo);
		return this.setMensajeCompleto("", documento, strTipoCobro);
	}
	
	private String registraCobroTotalSolesArbitrio(TblCobroArbitrio cobro,TblArbitrio documento, HttpServletRequest request,BigDecimal margeSaldo, String strTipoCobro) {
		TblCobroArbitrio cobroArbitrio = new TblCobroArbitrio();
		this.mRegistrarCobroContratoSolesSinSaldoArbitrio(cobroArbitrio, cobro, documento, request);
		documento.setValorCobrado(documento.getValorCobrado().add(documento.getSaldo()));
		documento.setSaldo(new BigDecimal("0"));
		this.preEditarArbitrio(documento, request);
		arbitrioDao.save(documento);
		cobro = calcularNuevoCobroSolesArbitrio(cobro, cobroArbitrio, margeSaldo);
		return this.setMensajeCompletoArbitrio("", documento, strTipoCobro);
	}
	
	private String registraCobroParcialDolares(TblCobro cobro,TblCxcDocumento documento, HttpServletRequest request,BigDecimal margeSaldo, String strTipoCobro) {
		TblCobro cobroAlquiler = new TblCobro();
		this.mRegistrarCobroContratoDolaresConSaldo(cobroAlquiler, cobro, documento, request);
		documento.setSaldo(documento.getSaldo().subtract(cobro.getMontoDolares()));
		documento.setMontoCobrado(documento.getMontoCobrado().add(cobro.getMontoDolares()));	
		this.preEditarDocumento(documento, request);
		cxcDocumentoDao.save(documento);
		setCobroMontosEnCero(cobro);
		return this.setMensajeParcial("", documento, strTipoCobro);
	}
	
	private String registraCobroParcialSoles(TblCobro cobro,TblCxcDocumento documento, HttpServletRequest request,BigDecimal margeSaldo, String strTipoCobro) {
		TblCobro cobroAlquiler = new TblCobro();
		this.mRegistrarCobroContratoSolesConSaldo(cobroAlquiler, cobro, documento, request);
		documento.setSaldo(documento.getSaldo().subtract(cobro.getMontoSoles()));
		documento.setMontoCobrado(documento.getMontoCobrado().add(cobro.getMontoSoles()));
		this.preEditarDocumento(documento, request);
		cxcDocumentoDao.save(documento);
		setCobroMontosEnCero(cobro);
		return this.setMensajeParcial("", documento, strTipoCobro);
	}
	private String registraCobroParcialSolesArbitrio(TblCobroArbitrio cobro,TblArbitrio documento, HttpServletRequest request,BigDecimal margeSaldo, String strTipoCobro) {
		TblCobroArbitrio cobroArbitrio = new TblCobroArbitrio();
		this.mRegistrarCobroContratoSolesConSaldoArbitrio(cobroArbitrio, cobro, documento, request);
		documento.setSaldo(documento.getSaldo().subtract(cobro.getMontoSoles()));
		documento.setValorCobrado(documento.getValorCobrado().add(cobro.getMontoSoles()));
		this.preEditarArbitrio(documento, request);
		arbitrioDao.save(documento);
		setCobroMontosEnCeroArbitrio(cobro);
		return this.setMensajeParcialArbitrio("", documento, strTipoCobro);
	}
	
	private void setCobroMontosEnCero(TblCobro cobro) {
		cobro.setMontoDolares(new BigDecimal("0"));
		cobro.setMontoSoles(new BigDecimal("0"));
	}
	
	private void setCobroMontosEnCeroArbitrio(TblCobroArbitrio cobro) {
		cobro.setMontoDolares(new BigDecimal("0"));
		cobro.setMontoSoles(new BigDecimal("0"));
	}
	
	/*
	 * Registra los cobros de alquiler, servicio, luz
	 * False: El monto del cobro es igual o menor a la deuda
	 * True: El monto de cobro es mayor a la deuda, esto genera adelanto
	 */
	public boolean mRegistrarCobroContrato(TblCobro cobro, TblContrato contrato, HttpServletRequest request, String strTipoCobro, String tipoPrimerCobro){
		boolean resultado = false;
		List<TblCxcDocumento> listaDeuda = null;
		List<TblCxcDocumento> listaDeudaAux = null;

		String notaPago = "";
		BigDecimal margeSaldo = null;
		try{
			
			listaDeuda = this.mListarDeuda(contrato, request, strTipoCobro);
			if (strTipoCobro.equals(Constantes.TIPO_COBRO_PRIMER_COBRO)){
				
				if (listaDeuda!=null && listaDeuda.size()>0){
					for(TblCxcDocumento cxcDocumento:listaDeuda ){
						if (cxcDocumento.getNombre().equals(tipoPrimerCobro)){ /*Busca el tipo del primer cobro*/
							listaDeudaAux = new ArrayList<TblCxcDocumento>();
							listaDeudaAux.add(cxcDocumento);
							break;
						}
					}
					listaDeuda = listaDeudaAux;
				}
			}
			
			//Valida la existencia de adelantos
			
			resultado = this.mValidarExisteciaAdelanto(listaDeuda, cobro);
			
			//Grabamos el desembolso del cliente
			this.mRegistrarDesembolso(cobro, contrato.getCodigoContrato(), request, strTipoCobro);
			if (strTipoCobro.equals(Constantes.TIPO_COBRO_GARANTIA)){
				this.mRegistrarAdelanto(cobro, contrato, request, Constantes.TIPO_COBRO_GARANTIA);
			}else{
				//No se registra nada: solo es porque la garantia se registra como adelanto del alquiler y al final del contrato se consume
			}
			
			margeSaldo = this.getSaldoCero(request, strTipoCobro);
			//Realiza la operacion de registro del cobro, descontando los saldos
			if(listaDeuda !=null && listaDeuda.size()>0){
				for(TblCxcDocumento documento: listaDeuda){
					if (cobro !=null && (cobro.getMontoDolares().doubleValue() >0 || cobro.getMontoSoles().doubleValue() > 0)){
						//actualizarDocumento = false;
							//Moneda: DOLARES
							if (documento.getTipoMoneda().equals(Constantes.MONEDA_DOLAR)){
								if (cobro.getMontoDolares().doubleValue()>0 ){
										//Valida si el monto pagado es mayor a la deuda, teniendo en cuenta el margen (+/- 1)
									if (this.validarDiferencia(cobro.getMontoDolares(), documento.getSaldo(), margeSaldo)){
										notaPago = notaPago + registraCobroTotalDolares(cobro, documento, request, margeSaldo,  strTipoCobro);
										
									}else{
										notaPago = notaPago + registraCobroParcialDolares(cobro, documento, request, margeSaldo,  strTipoCobro);
									}
								}
							}else{
								if (cobro.getMontoSoles().doubleValue()>0){
									//Valida si el monto pagado es mayor a la deuda, teniendo en cuenta el margen (+/- 1)
									if (this.validarDiferencia(cobro.getMontoSoles(), documento.getSaldo(), margeSaldo)){
										notaPago = notaPago + registraCobroTotalSoles(cobro, documento, request, margeSaldo, strTipoCobro);
										
									}else{
										notaPago = notaPago + registraCobroParcialSoles(cobro, documento, request, margeSaldo, strTipoCobro);
									}
								}
							}
					}else{
						break;
					}
				}
				//Actualizar mensaje del desembolso
				this.mActualizarDesembolsoNota(cobro, notaPago);
				
			}else{
				//No se hace nada, se retorna indicando que se debe realizar el registro de los adelantos
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
			listaDeuda 		= null;
		}
		return resultado;
	}
	
	public boolean mRegistrarCobroArbitrio(TblCobroArbitrio cobro, TblContrato contrato, HttpServletRequest request, String strTipoCobro, String tipoPrimerCobro){
		boolean resultado 				= false;
		List<TblArbitrio> listaDeuda 	= null;
		//List<TblArbitrio> listaDeudaAux = null;

		String notaPago = "";
		BigDecimal margeSaldo = null;
		try{
			
			listaDeuda = this.mListarDeudaArbitrio(contrato.getTblTienda().getCodigoTienda());
			
			
			//Valida la existencia de adelantos
			
			resultado = this.mValidarExisteciaAdelantoArbitrio(listaDeuda, cobro);
			
			//Grabamos el desembolso del cliente
			this.mRegistrarDesembolsoArbitrio(cobro, contrato.getCodigoContrato(), request, strTipoCobro);
			
			
			margeSaldo = this.getSaldoCero(request, strTipoCobro);
			//Realiza la operacion de registro del cobro, descontando los saldos
			if(listaDeuda !=null && listaDeuda.size()>0){
				for(TblArbitrio documento: listaDeuda){
					if (cobro !=null && (cobro.getMontoDolares().doubleValue() >0 || cobro.getMontoSoles().doubleValue() > 0)){
						//actualizarDocumento = false;
							//Moneda: SOLES
							
								if (cobro.getMontoSoles().doubleValue()>0){
									//Valida si el monto pagado es mayor a la deuda, teniendo en cuenta el margen (+/- 1)
									if (this.validarDiferencia(cobro.getMontoSoles(), documento.getSaldo(), margeSaldo)){
										notaPago = notaPago + registraCobroTotalSolesArbitrio(cobro, documento, request, margeSaldo, strTipoCobro);
										
									}else{
										notaPago = notaPago + registraCobroParcialSolesArbitrio(cobro, documento, request, margeSaldo, strTipoCobro);
									}
								}
							
					}else{
						break;
					}
				}
				//Actualizar mensaje del desembolso
				this.mActualizarDesembolsoNotaArbitrio(cobro, notaPago);
				
			}else{
				//No se hace nada, se retorna indicando que se debe realizar el registro de los adelantos
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
			listaDeuda 		= null;
		}
		return resultado;
	}
	
	
	
	public void preGuardarCobro(TblCobro entidad, HttpServletRequest request) {
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
	public void preGuardarCobroArbitrio(TblCobroArbitrio entidad, HttpServletRequest request) {
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
	public void preEditarDocumento(TblCxcDocumento entidad, HttpServletRequest request) {
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
	public void preEditarArbitrio(TblArbitrio entidad, HttpServletRequest request) {
		try{
			log.debug("[preEditarArbitrio] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			log.debug("[preEditarArbitrio] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public void preEditar(TblCobro entidad, HttpServletRequest request) {
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
		model.addAttribute("listaCxcSinSaldo", beanRequest.getListaCxcSinSaldo());
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

	
	/**
	 * Se encarga de adicionar los primeros cobros
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cobro/observacion/edicion", method = RequestMethod.POST)
	public String adicionarObservacionEdicion(Model model, TblObservacion observacionBean, TblContrato contrato, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		try{
			log.debug("[adicionarObservacionEdicion] Inicio:");
			path = "caja/cobro/cob_edicion";
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
	 * Se encarga de adicionar un cobro de alquiler
	 * 
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "/cobro/alquiler", method = RequestMethod.POST)
	public String adicionarCobroAlquiler(Model model, CobroBean cobroAlquiler, TblContrato contrato, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		TblCobro cobroAlquilerAux			= new TblCobro();
		try{
			log.debug("[adicionarCobroAlquiler] Inicio");
			path = "caja/cobro/cob_edicion";
			
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			if (this.validarCobroAlquiler(model, cobroAlquiler, request)){
				//Calculo del Monto en Soles y Dolares
				UtilSGT.mCalcularMontoCobro(cobroAlquiler);
				//Asigna Fecha, Montos, y tipo de cambio
				cobroAlquilerAux = UtilSGT.mAsignarDatoCobro(cobroAlquiler);
				//Adiciona en el historial
				this.mAsignarHistorialAlquiler(request, cobroAlquilerAux);
				
				model.addAttribute("respuesta", "Se adiciono el Cobro de Alquiler. Esta pendiente el registro.");
			}
			//model.addAttribute("listaServicio", beanRequest.getListaServicio());
			beanRequest.setContrato(contrato);
			beanRequest.setCobroAlquiler(cobroAlquiler);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);

			log.debug("[adicionarCobroAlquiler] Fin");
		}catch(Exception e){
			log.debug("[adicionarCobroAlquiler] Error:"+e.getMessage());
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
			if (entidad.getTipoMoneda() == null){
				exitoso = false;
				model.addAttribute("respuesta", "Debe seleccionar el tipo de moneda.");
			}else if (entidad.getMontoSoles() == null || entidad.getMontoSoles().doubleValue()<=0){
				exitoso = false;
				model.addAttribute("respuesta", "Debe Ingresar el monto");
			}
			
		}catch(Exception e){
			exitoso = false;
		}
		return exitoso;
	}
	
	/*
	 * Valida los campos del cobro de Primer Cobro
	 */
	public boolean validarCobroPrimerCobro(Model model,CobroPrimerCobro entidad, HttpServletRequest request){
		boolean exitoso = true;
		try{
			if (entidad.getTipoMonedaPrimerCobro() == null){
				exitoso = false;
				model.addAttribute("respuesta", "Debe seleccionar el tipo de moneda.");
			}else if (entidad.getMontoSolesPrimerCobro() == null || entidad.getMontoSolesPrimerCobro().doubleValue()<=0){
				exitoso = false;
				model.addAttribute("respuesta", "Debe Ingresar el monto");
			}

		}catch(Exception e){
			exitoso = false;
		}
		return exitoso;
	}
	/*
	 * Valida los campos del cobro de Primer Cobro
	 */
	public boolean validarCobroGarantia(Model model,CobroGarantia entidad, HttpServletRequest request){
		boolean exitoso = true;
		try{
			if (entidad.getTipoMonedaGarantia() == null){
				exitoso = false;
				model.addAttribute("respuesta", "Debe seleccionar el tipo de moneda.");
			}else if (entidad.getMontoSolesGarantia() == null || entidad.getMontoSolesGarantia().doubleValue()<=0){
				exitoso = false;
				model.addAttribute("respuesta", "Debe Ingresar el monto");
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

	@RequestMapping(value = "/cobro/servicio", method = RequestMethod.POST)
	public String adicionarCobroServicio(Model model, CobroServicioBean cobroServicio, TblContrato contrato, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		TblCobro cobroServiciorAux			= new TblCobro();
		try{
			log.debug("[adicionarCobroServicio] Inicio");
			path = "caja/cobro/cob_edicion";
			
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			if (this.validarCobroServicio(model, cobroServicio, request)){
				//Calculo del Monto en Soles y Dolares
				UtilSGT.mCalcularMontoCobro(cobroServicio);
				//Asigna Fecha, Montos, y tipo de cambio
				cobroServiciorAux = UtilSGT.mAsignarDatoCobro(cobroServicio);
				//Adiciona en el historial
				this.mAsignarHistorialServicio(request, cobroServiciorAux);
				
				model.addAttribute("respuestaServicio", "Se adiciono el Cobro de Servicio. Esta pendiente el registro.");
			}
			beanRequest.setContrato(contrato);
			beanRequest.setCobroServicio(cobroServicio);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);

			log.debug("[adicionarCobroServicio] Fin");
		}catch(Exception e){
			log.debug("[adicionarCobroServicio] Error:"+e.getMessage());
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
			if (entidad.getTipoMonedaServicio() == null){
				exitoso = false;
				model.addAttribute("respuesta", "Debe seleccionar el tipo de moneda.");
			}else if (entidad.getMontoSolesServicio() == null || entidad.getMontoSolesServicio().doubleValue()<=0){
				exitoso = false;
				model.addAttribute("respuesta", "Debe Ingresar el monto");
			}else if (entidad.getCodigoTipoServicio() == null || entidad.getCodigoTipoServicio().doubleValue()<=0){
				exitoso = false;
				model.addAttribute("respuesta", "Debe Ingresar el tipo de servicio");
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

	@RequestMapping(value = "/cobro/luz", method = RequestMethod.POST)
	public String adicionarCobroLuz(Model model, CobroLuzBean cobroLuz, TblContrato contrato, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		TblCobro cobroLuzAux			= new TblCobro();
		try{
			log.debug("[adicionarCobroLuz] Inicio");
			path = "caja/cobro/cob_edicion";
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			
			if (this.validarCobroLuz(model, cobroLuz, request)){
				//Calculo del Monto en Soles y Dolares
				UtilSGT.mCalcularMontoCobro(cobroLuz);
				//Asigna Fecha, Montos, y tipo de cambio
				cobroLuzAux = UtilSGT.mAsignarDatoCobro(cobroLuz);
				//Adiciona en el historial
				this.mAsignarHistorialLuz(request, cobroLuzAux);
				
				model.addAttribute("respuestaLuz", "Se adiciono el Cobro de Luz. Esta pendiente el registro.");
			}
			//model.addAttribute("listaServicio", beanRequest.getListaServicio());
			beanRequest.setContrato(contrato);
			beanRequest.setCobroLuz(cobroLuz);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);

			log.debug("[adicionarCobroLuz] Fin");
		}catch(Exception e){
			log.debug("[adicionarCobroLuz] Error:"+e.getMessage());
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
			if (entidad.getTipoMonedaLuz() == null){
				exitoso = false;
				model.addAttribute("respuesta", "Debe seleccionar el tipo de moneda.");
			}else if (entidad.getMontoSolesLuz()== null || entidad.getMontoSolesLuz().doubleValue()<=0){
				exitoso = false;
				model.addAttribute("respuesta", "Debe Ingresar el monto");
			}
			

		}catch(Exception e){
			exitoso = false;
		}
		return exitoso;
	}
	/*
	 * Valida los campos del cobro de Arbitrio
	 */
	public boolean validarCobroArbitrio(Model model,CobroArbitrioBean entidad, HttpServletRequest request){
		boolean exitoso = true;
		try{
			if (entidad.getTipoMonedaArbitrio() == null){
				exitoso = false;
				model.addAttribute("respuesta", "Debe seleccionar el tipo de moneda.");
			}else if (entidad.getMontoSolesArbitrio()== null || entidad.getMontoSolesArbitrio().doubleValue()<=0){
				exitoso = false;
				model.addAttribute("respuesta", "Debe Ingresar el monto");
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
	/*@RequestMapping(value = "/cobro/regresar", method = RequestMethod.POST)
	public String regresarCobro(Model model, Filtro filtro, String path, HttpServletRequest request) {
		//TblContrato contrato				= null;
		BeanRequest beanRequest				= null;
		path = "caja/cobro/cob_edicion";
		try{
			log.debug("[regresarContrato] Inicio");
			beanRequest = (BeanRequest) request.getSession().getAttribute("beanRequest");
			
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
	}*/
	/**
	 * Se encarga de regresar a la pantalla de Cobro
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cobro/adelanto/regresar", method = RequestMethod.POST)
	public String regresarCobrodeAdelanto(Model model, Filtro filtro, String path, HttpServletRequest request) {
		//TblContrato contrato				= null;
		//BeanRequest beanRequest				= null;
		path = "caja/cobro/cob_edicion";
		try{
			log.debug("[regresarCobrodeAdelanto] Inicio");
			this.mEditarContrato(filtro.getCodigoContrato(), model, request);
			log.debug("[regresarCobrodeAdelanto] Fin");
		}catch(Exception e){
			log.debug("[regresarContrato] Error:"+e.getMessage());
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
	@RequestMapping(value = "/cobro/historial/alquiler", method = RequestMethod.POST)
	public String historialCobroAlquiler(Model model, CobroGeneralBean entidad, HttpServletRequest request) {
		String path 							= "";
			try{
			path = "caja/cobro/cob_historial_alquiler";
			this.mListadoDesembolso(model, entidad, request);

		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
	}
	
	@SuppressWarnings("unchecked")
	private void mListadoDesembolso(Model model, CobroGeneralBean entidad, HttpServletRequest request){
		BeanRequest beanRequest						= null;
		List<TblDesembolso> listaCobroAlquiler		= null;
		List<TblDesembolso> listaCobroServicio		= null;
		List<TblDesembolso> listaCobroLuz			= null;
		List<TblDesembolso> listaCobroPrimerCobro	= null;
		List<TblDesembolsoArbitrio> listaCobroArbitrio	= null;
		
		beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
		listaCobroAlquiler = desembolsoDao.listarAllCobroxTipo(beanRequest.getContrato().getCodigoContrato(),"ALQ");
		listaCobroServicio = desembolsoDao.listarAllCobroxTipo(beanRequest.getContrato().getCodigoContrato(),"SER");
		listaCobroLuz = desembolsoDao.listarAllCobroxTipo(beanRequest.getContrato().getCodigoContrato(),"LUZ");
		listaCobroPrimerCobro = desembolsoDao.listarAllCobroxTipo(beanRequest.getContrato().getCodigoContrato(),"PRM");
		listaCobroArbitrio = desembolsoArbitrioDao.listarAllCobroxTipo(beanRequest.getContrato().getCodigoContrato());
		entidad.setListaCobroAlquiler(listaCobroAlquiler);
		entidad.setListaCobroLuz(listaCobroLuz);
		entidad.setListaCobroPrimerCobro(listaCobroPrimerCobro);
		entidad.setListaCobroServicio(listaCobroServicio);
		entidad.setListaCobroArbitrio(listaCobroArbitrio);
		//Seteamos los usuarios
		entidad.setMapUsuario((Map<Integer, String>)request.getSession().getAttribute("SessionMapUsuarioAllMap"));
		
		model.addAttribute("cobro", entidad);
	}
	/**
	 * Se encarga de direccionar a la pantalla de historial de cobro de servicio
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cobro/historial/servicio/{id}", method = RequestMethod.GET)
	public String historialCobroServicio(@PathVariable Integer id, Model model, TblContrato filtro, HttpServletRequest request) {
		String path 							= "";
		List<TblCobro> listaCobro				= null;
		try{
			path = "caja/cobro/cob_historial_servicio";
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
	@RequestMapping(value = "/cobro/historial/luz/{id}", method = RequestMethod.GET)
	public String historialCobroLuz(@PathVariable Integer id, Model model, TblContrato filtro, HttpServletRequest request) {
		String path 							= "";
		List<TblCobro> listaCobro				= null;
		try{
			path = "caja/cobro/cob_historial_luz";
			listaCobro = cobroDao.listarAllActivosxDocumento(id);
			model.addAttribute("registros", listaCobro);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			listaCobro			= null;
		}
		return path;
	}
	
	/*
	 * Asigna en el Bean de Sesion el historial de cobro, temporalmente
	 */
	public void mAsignarHistorialCobroGarantia(HttpServletRequest request,TblCobro cobroAlquilerAux) throws Exception{
		BeanRequest beanRequest				= null;
		try{
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			if (beanRequest.getHistorialCobroGarantia() == null) {
				beanRequest.setHistorialCobroGarantia(new ArrayList<TblCobro>());
				beanRequest.getHistorialCobroGarantia().add(cobroAlquilerAux);

			}else{
				beanRequest.getHistorialCobroGarantia().add(cobroAlquilerAux);
			}
		}catch(Exception e){
			e.printStackTrace();
			beanRequest.setHistorialCobroGarantia(null);
			throw e;
		}
	}
	public void mAsignarHistorialCobroPrimerCobro(HttpServletRequest request,TblCobro cobroAlquilerAux) throws Exception{
		BeanRequest beanRequest				= null;
		try{
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			if (beanRequest.getHistorialCobroPrimerCobro() == null) {
				beanRequest.setHistorialCobroPrimerCobro(new ArrayList<TblCobro>());
				beanRequest.getHistorialCobroPrimerCobro().add(cobroAlquilerAux);

			}else{
				beanRequest.getHistorialCobroPrimerCobro().add(cobroAlquilerAux);
			}
		}catch(Exception e){
			e.printStackTrace();
			beanRequest.setHistorialCobroPrimerCobro(null);
			throw e;
		}
	}
	public void mAsignarHistorialAlquiler(HttpServletRequest request,TblCobro cobroAlquilerAux) throws Exception{
		BeanRequest beanRequest				= null;
		try{
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			if (beanRequest.getHistorialCobroAlquiler() == null) {
				beanRequest.setHistorialCobroAlquiler(new ArrayList<TblCobro>());
				beanRequest.getHistorialCobroAlquiler().add(cobroAlquilerAux);

			}else{
				beanRequest.getHistorialCobroAlquiler().add(cobroAlquilerAux);
			}
		}catch(Exception e){
			e.printStackTrace();
			beanRequest.setHistorialCobroAlquiler(null);
			throw e;
		}
	}
	public void mAsignarHistorialServicio(HttpServletRequest request,TblCobro cobroServiciorAux) throws Exception{
		BeanRequest beanRequest				= null;
		try{
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			if (beanRequest.getHistorialCobroServicio() == null) {
				beanRequest.setHistorialCobroServicio(new ArrayList<TblCobro>());
				beanRequest.getHistorialCobroServicio().add(cobroServiciorAux);

			}else{
				beanRequest.getHistorialCobroServicio().add(cobroServiciorAux);
			}
		}catch(Exception e){
			e.printStackTrace();
			beanRequest.setHistorialCobroServicio(null);
			throw e;
		}
	}
	public void mAsignarHistorialLuz(HttpServletRequest request,TblCobro cobroLuzAux) throws Exception{
		BeanRequest beanRequest				= null;
		try{
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			if (beanRequest.getHistorialCobroLuz() == null) {
				beanRequest.setHistorialCobroLuz(new ArrayList<TblCobro>());
				beanRequest.getHistorialCobroLuz().add(cobroLuzAux);

			}else{
				beanRequest.getHistorialCobroLuz().add(cobroLuzAux);
			}
		}catch(Exception e){
			e.printStackTrace();
			beanRequest.setHistorialCobroLuz(null);
			throw e;
		}
	}
	public void mAsignarHistorialArbitrio(HttpServletRequest request,TblCobro cobroArbitrioAux) throws Exception{
		BeanRequest beanRequest				= null;
		try{
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			if (beanRequest.getHistorialCobroArbitrio() == null) {
				beanRequest.setHistorialCobroArbitrio(new ArrayList<TblCobro>());
				beanRequest.getHistorialCobroArbitrio().add(cobroArbitrioAux);

			}else{
				beanRequest.getHistorialCobroArbitrio().add(cobroArbitrioAux);
			}
		}catch(Exception e){
			e.printStackTrace();
			beanRequest.setHistorialCobroArbitrio(null);
			throw e;
		}
	}
	/**
	 * Se encarga de adicionar un cobro de monto
	 * 
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "/cobro/primerCobro", method = RequestMethod.POST)
	public String adicionarCobroPrimerCobro(Model model, CobroPrimerCobro cobroPrimerCobro, TblContrato contrato, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		TblCobro cobroAlquilerAux			= null;
		try{
			log.debug("[adicionarCobroAlquiler] Inicio");
			path = "caja/cobro/cob_edicion";
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			if (this.validarCobroPrimerCobro(model, cobroPrimerCobro, request)){
				//Calculo del Monto en Soles y Dolares
				UtilSGT.mCalcularMontoCobro(cobroPrimerCobro);
				//Asigna Fecha, Montos, y tipo de cambio
				cobroAlquilerAux = UtilSGT.mAsignarDatoCobro(cobroPrimerCobro);
				//Adiciona en el historial
				this.mAsignarHistorialCobroPrimerCobro(request, cobroAlquilerAux);
				model.addAttribute("respuestaPrimerCobro", "Se adiciono el Cobro. Esta pendiente el registro.");
			}
			//model.addAttribute("listaServicio", beanRequest.getListaServicio());
			beanRequest.setCobroPrimerCobro(cobroPrimerCobro);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);

			log.debug("[adicionarCobroAlquiler] Fin");
		}catch(Exception e){
			log.debug("[adicionarCobroAlquiler] Error:"+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se genero un error inesperado:"+e.getMessage());
		}finally{
			beanRequest = null;
		}

		return path;
	}
	/**
	 * Se encarga de adicionar un cobro de monto
	 * 
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "/cobro/garantia", method = RequestMethod.POST)
	public String adicionarCobroGarantia(Model model, CobroGarantia cobroGarantia, TblContrato contrato, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		TblCobro cobroAlquilerAux			= null;
		try{
			log.debug("[adicionarCobroGarantia] Inicio");
			path = "caja/cobro/cob_edicion";
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			if (this.validarCobroGarantia(model, cobroGarantia, request)){
				//Calculo del Monto en Soles y Dolares
				UtilSGT.mCalcularMontoCobro(cobroGarantia);
				//Asigna Fecha, Montos, y tipo de cambio
				cobroAlquilerAux = UtilSGT.mAsignarDatoCobro(cobroGarantia);
				//Adiciona en el historial
				this.mAsignarHistorialCobroGarantia(request, cobroAlquilerAux);
				model.addAttribute("respuestaGarantia", "Se adiciono el Cobro. Esta pendiente el registro.");
			}
			//model.addAttribute("listaServicio", beanRequest.getListaServicio());
			beanRequest.setCobroGarantia(cobroGarantia);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);

			log.debug("[adicionarCobroGarantia] Fin");
		}catch(Exception e){
			log.debug("[adicionarCobroGarantia] Error:"+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se genero un error inesperado:"+e.getMessage());
		}finally{
			beanRequest = null;
		}

		return path;
	}
	/**
	 * Se encarga de direccionar a la pantalla de historial de desembolso
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cobro/historial/desembolso/{id}", method = RequestMethod.GET)
	public String historialDesembolso(@PathVariable Integer id, Model model, HttpServletRequest request) {
		String path 								= "";
		List<TblDesembolso> listaDesembolso			= null;
		List<DesembolsoBean> listaDesembolsoBean	= null;
		try{
			path = "caja/cobro/cob_historial_desembolso";
			listaDesembolso = desembolsoDao.listarAllActivosxContrato(id);
			listaDesembolsoBean = this.mAsignarDesembolso(listaDesembolso);
			model.addAttribute("registros", listaDesembolsoBean);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			listaDesembolso			= null;
			listaDesembolsoBean		= null;
		}
		return path;
	}
	/*
	 * Copia los datos de la tabla al bean
	 */
	public List<DesembolsoBean> mAsignarDesembolso(List<TblDesembolso> listaDesembolso){
		List<DesembolsoBean> listaDesembolsoBean	= null;
		DesembolsoBean desembolsoBean				= null;
		try{
			if (listaDesembolso!=null && listaDesembolso.size()>0){
				for(TblDesembolso desembolso:listaDesembolso){
					desembolsoBean = new DesembolsoBean();
					desembolsoBean.setCodigoContrato(desembolso.getCodigoContrato());
					desembolsoBean.setCodigoDesembolso(desembolso.getCodigoDesembolso());
					desembolsoBean.setFechaCobro(desembolso.getFechaCobro());
					desembolsoBean.setMontoDolares(desembolso.getMontoDolares());
					desembolsoBean.setMontoSoles(desembolso.getMontoSoles());
					desembolsoBean.setTipoCambio(desembolso.getTipoCambio());
					desembolsoBean.setTipoCobro(desembolso.getTipoCobro());
					desembolsoBean.setFechaCreacion(desembolso.getFechaCreacion());
					desembolsoBean.setEstadoOperacion(desembolso.getEstadoOperacion());
					if (listaDesembolsoBean==null){
						listaDesembolsoBean = new ArrayList<DesembolsoBean>();
						listaDesembolsoBean.add(desembolsoBean);
					}else{
						listaDesembolsoBean.add(desembolsoBean);
					}
				}
			}
		}catch(Exception e){
			
		}
		return listaDesembolsoBean;
	}
	/*
	 * Copia los datos de la tabla al bean
	 */
	public List<CobroBean> mAsignarCobro(List<TblCobro> listaCobro, TblDesembolso desembolso){
		List<CobroBean> listaCobroBean	= null;
		CobroBean cobroBean				= null;
		try{
			if (listaCobro!=null && listaCobro.size()>0){
				for(TblCobro cobro:listaCobro){
					cobroBean = new CobroBean();
					cobroBean.setCodigoContrato(desembolso.getCodigoContrato());
					cobroBean.setCodigoDesembolso(desembolso.getCodigoDesembolso());
					cobroBean.setFechaCobro(cobro.getFechaCobro());
					cobroBean.setMontoDolares(cobro.getMontoDolares());
					cobroBean.setMontoSoles(cobro.getMontoSoles());
					cobroBean.setTipoCambio(cobro.getTipoCambio());
					cobroBean.setTipoCobro(cobro.getTipoCobro());
					cobroBean.setFechaCreacion(cobro.getFechaCreacion());
					cobroBean.setTipoOperacion(Constantes.OPERACION_COBRO);
					cobroBean.setEstado(cobro.getEstado());
					cobroBean.setIntMesCobro(UtilSGT.getMesYYYYMMDD(cobro.getTblCxcDocumento().getFechaFin().toString()));
					if (listaCobroBean==null){
						listaCobroBean = new ArrayList<CobroBean>();
						listaCobroBean.add(cobroBean);
					}else{
						listaCobroBean.add(cobroBean);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return listaCobroBean;
	}
	/*
	 * Copia los datos de la tabla al bean
	 */
	public List<CobroBean> mAsignarAdelanto(List<TblAdelanto> listaAdelanto, TblDesembolso desembolso, List<CobroBean> listaCobroBean){
		CobroBean cobroBean				= null;
		try{
			if (listaAdelanto!=null && listaAdelanto.size()>0){
				for(TblAdelanto adelanto:listaAdelanto){
					cobroBean = new CobroBean();
					cobroBean.setCodigoContrato(desembolso.getCodigoContrato());
					cobroBean.setCodigoDesembolso(desembolso.getCodigoDesembolso());
					cobroBean.setFechaCobro(adelanto.getFechaAdelanto());
					cobroBean.setMontoDolares(adelanto.getMontoDolares());
					cobroBean.setMontoSoles(adelanto.getMontoSoles());
					cobroBean.setTipoCambio(adelanto.getTipoCambio());
					cobroBean.setTipoCobro(adelanto.getTipoRubro());
					cobroBean.setFechaCreacion(adelanto.getFechaCreacion());
					cobroBean.setTipoOperacion(Constantes.OPERACION_ADELANTO);
					cobroBean.setEstado(adelanto.getEstado());
					if (listaCobroBean==null){
						listaCobroBean = new ArrayList<CobroBean>();
						listaCobroBean.add(cobroBean);
					}else{
						listaCobroBean.add(cobroBean);
					}
				}
			}
		}catch(Exception e){
			
		}
		return listaCobroBean;
	}
	/*
	 * Copia los datos de la tabla al bean
	 */
	public List<CobroBean> mAsignarAdelantoCobro(List<TblAdelanto> listaAdelanto, List<CobroBean> listaCobroBean){
		CobroBean cobroBean				= null;
		try{
			if (listaAdelanto!=null && listaAdelanto.size()>0){
				for(TblAdelanto adelanto:listaAdelanto){
					cobroBean = new CobroBean();
					cobroBean.setCodigoContrato(adelanto.getTblContrato().getCodigoContrato());
					cobroBean.setCodigoDesembolso(adelanto.getCodigoDesembolso());
					cobroBean.setFechaCobro(adelanto.getFechaAdelanto());
					cobroBean.setMontoDolares(adelanto.getMontoDolares());
					cobroBean.setMontoSoles(adelanto.getMontoSoles());
					cobroBean.setTipoCambio(adelanto.getTipoCambio());
					cobroBean.setTipoCobro(adelanto.getTipoRubro());
					cobroBean.setFechaCreacion(adelanto.getFechaCreacion());
					cobroBean.setTipoOperacion(Constantes.OPERACION_ADELANTO);
					cobroBean.setEstado(adelanto.getEstado());
					cobroBean.setSeleccionado(false);
					cobroBean.setCodigoAdelanto(adelanto.getCodigoAdelanto());
					//Datos para la reversion
					cobroBean.setMontoDolaresConsumido(adelanto.getMontoDolaresConsumido());
					cobroBean.setMontoSolesConsumido(adelanto.getMontoSolesConsumido());
					if (listaCobroBean==null){
						listaCobroBean = new ArrayList<CobroBean>();
						listaCobroBean.add(cobroBean);
					}else{
						listaCobroBean.add(cobroBean);
					}
				}
			}
		}catch(Exception e){
			
		}
		return listaCobroBean;
	}
	/**
	 * Se encarga de direccionar a la pantalla de historial de desembolso
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cobro/desembolso/detalle/{id}", method = RequestMethod.GET)
	public String historialDesembolsoDetalle(@PathVariable Integer id, Model model, HttpServletRequest request) {
		String path 								= "";
		List<TblCobro> listaCobro					= null;
		List<TblAdelanto> listaAdelanto				= null;
		List<CobroBean> listaCobroBean				= null;
		TblDesembolso desembolso					= null;
		Filtro filtro								= null;
		try{
			path = "caja/cobro/cob_historial_desembolso_detalle";
			listaCobro = cobroDao.listarAllActivosxDesembolso(id);
			desembolso = desembolsoDao.findOne(id);
			listaCobroBean = this.mAsignarCobro(listaCobro, desembolso);
			listaAdelanto = adelantoDao.listarAllActivosxDesembolso(id);
			listaCobroBean = this.mAsignarAdelanto(listaAdelanto, desembolso, listaCobroBean);
			filtro = new Filtro();
			filtro.setCodigoContrato(desembolso.getCodigoContrato());
			filtro.setCodigoDesembolso(desembolso.getCodigoDesembolso());
			filtro.setEstadoOperacion(desembolso.getEstadoOperacion());
			filtro.setDescripcion(desembolso.getMotivoReversion());
			model.addAttribute("registros", listaCobroBean);
			model.addAttribute("filtro", filtro);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			listaCobro			= null;
			listaAdelanto		= null;
			listaCobroBean		= null;
			desembolso			= null;
		}
		return path;
	}
	
	/**
	 * Se encarga de regresar a la pantalla de Cobro
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cobro/desembolso/detalle/regresar", method = RequestMethod.POST)
	public String regresarDesembolsoDetalle(Model model, Filtro filtro,HttpServletRequest request) {
		String path 								= "";
		List<TblDesembolso> listaDesembolso			= null;
		List<DesembolsoBean> listaDesembolsoBean	= null;
		try{
			path = "caja/cobro/cob_historial_desembolso";
			listaDesembolso = desembolsoDao.listarAllActivosxContrato(filtro.getCodigoContrato());
			listaDesembolsoBean = this.mAsignarDesembolso(listaDesembolso);
			model.addAttribute("registros", listaDesembolsoBean);
		}catch(Exception e){
			log.debug("[regresarContrato] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			listaDesembolso 	= null;
			listaDesembolsoBean	= null;
		}
		return path;
	}
	/**
	 * Se encarga de adicionar un cobro de luz
	 * 
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "/cobro/arbitrio", method = RequestMethod.POST)
	public String adicionarCobroArbitrio(Model model, CobroArbitrioBean cobroArbitrio, TblContrato contrato, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		TblCobro cobroArbitrioAux			= new TblCobro();
		try{
			log.debug("[adicionarCobroArbitrio] Inicio");
			path = "caja/cobro/cob_edicion";
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			
			if (this.validarCobroArbitrio(model, cobroArbitrio, request)){
				//Calculo del Monto en Soles y Dolares
				UtilSGT.mCalcularMontoCobro(cobroArbitrio);
				//Asigna Fecha, Montos, y tipo de cambio
				cobroArbitrioAux = UtilSGT.mAsignarDatoCobro(cobroArbitrio);
				//Adiciona en el historial
				this.mAsignarHistorialArbitrio(request, cobroArbitrioAux);
				
				model.addAttribute("respuestaArbitrio", "Se adiciono el Cobro de Arbitrio. Esta pendiente el registro.");
			}
			//model.addAttribute("listaServicio", beanRequest.getListaServicio());
			beanRequest.setContrato(contrato);
			beanRequest.setCobroArbitrio(cobroArbitrio);
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);

			log.debug("[adicionarCobroArbitrio] Fin");
		}catch(Exception e){
			log.debug("[adicionarCobroArbitrio] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			beanRequest = null;
		}

		return path;
	}
	/**
	 * Se encarga de regresar a la pantalla de Cobro
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cobro/desembolso/detalle/reversar/{id}", method = RequestMethod.GET)
	public String registrarSolicitudReversionCobro(@PathVariable Integer id, Model model, HttpServletRequest request) {
		String path 								= "";
		/*List<TblDesembolso> listaDesembolso			= null;
		List<DesembolsoBean> listaDesembolsoBean	= null;
		*/TblDesembolso desembolso					= null;
		BeanRequest beanRequest						= null;
		CobroGeneralBean entidad					= null;
		try{
			path = "caja/cobro/cob_historial_alquiler";
			//Actualiza a pendiente por reversion el desembolos
			desembolso = desembolsoDao.findOne(id);
			desembolso.setEstadoOperacion(Constantes.ESTADO_DESEMBOLSO_SOLICITADA_REVERSION);
			desembolso.setMotivoReversion("Solicitud de Reversión");
			this.preEditarDesembolso(desembolso, request);
			desembolsoDao.save(desembolso);
			model.addAttribute("respuestaDesembolso", "La solicitud de reversión se registró exitosamente.");
			//Lista los desembolsos
//			listaDesembolso = desembolsoDao.listarAllActivosxContrato(filtro.getCodigoContrato());
//			listaDesembolsoBean = this.mAsignarDesembolso(listaDesembolso);
//			model.addAttribute("registros", listaDesembolsoBean);
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			entidad = beanRequest.getCobroGeneralBean();
			this.mListadoDesembolso(model, entidad, request);
		}catch(Exception e){
			log.debug("[regresarContrato] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			/*listaDesembolso 	= null;
			listaDesembolsoBean	= null;*/
		}
		return path;
	}
	/**
	 * Se encarga de regresar a la pantalla de Cobro
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cobro/desembolso/detalle/reversarArbitrio/{id}", method = RequestMethod.GET)
	public String registrarSolicitudReversionCobroArbitrio(@PathVariable Integer id, Model model, HttpServletRequest request) {
		String path 								= "";
		TblDesembolsoArbitrio desembolso			= null;
		BeanRequest beanRequest						= null;
		CobroGeneralBean entidad					= null;
		try{
			log.debug("[registrarSolicitudReversionCobroArbitrio] Inicio" );
			path = "caja/cobro/cob_historial_alquiler";
			//Actualiza a pendiente por reversion el desembolos
			desembolso = desembolsoArbitrioDao.findOne(id);
			desembolso.setEstadoOperacion(Constantes.ESTADO_DESEMBOLSO_SOLICITADA_REVERSION);
			desembolso.setMotivoReversion("Solicitud de Reversión");
			this.preEditarDesembolsoArbitrio(desembolso, request);
			desembolsoArbitrioDao.save(desembolso);
			model.addAttribute("respuestaDesembolso", "La solicitud de reversión se registró exitosamente.");
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			entidad = beanRequest.getCobroGeneralBean();
			this.mListadoDesembolso(model, entidad, request);
			log.debug("[registrarSolicitudReversionCobroArbitrio] Fin" );
		}catch(Exception e){
			log.debug("[registrarSolicitudReversionCobroArbitrio] Error:"+e.getMessage());
			e.printStackTrace();
		}
		return path;
	}
	/*
	 * Actualizacion para los campos de auditoria del Desembolso
	 */
	public void preEditarDesembolso(TblDesembolso entidad, HttpServletRequest request) {
		try{
			log.debug("[preEditarDesembolso] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			log.debug("[preEditarDesembolso] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	 * Actualizacion para los campos de auditoria del Desembolso
	 */
	public void preEditarDesembolsoArbitrio(TblDesembolsoArbitrio entidad, HttpServletRequest request) {
		try{
			log.debug("[preEditarDesembolsoArbitrio] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			log.debug("[preEditarDesembolsoArbitrio] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
	/*
	 * Actualizacion para los campos de auditoria del Adelanto
	 */
	public void preEditarAdelanto(TblAdelanto entidad, HttpServletRequest request) {
		try{
			log.debug("[preEditarAdelanto] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			log.debug("[preEditarAdelanto] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Se encarga de direccionar a la pantalla de historial de desembolso
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cobro/historial/adelanto/{id}", method = RequestMethod.GET)
	public String mostrarListadoAdelanto(@PathVariable Integer id, Model model, HttpServletRequest request) {
		String path 								= "";
		List<TblAdelanto> listaAdelanto				= null;
		List<CobroBean> listaCobroBean				= null;
		Filtro filtro								= null;
		try{
			path = "caja/cobro/cob_adelanto_listado";
			filtro = new Filtro();
			filtro.setCodigoContrato(id);
			listaAdelanto = adelantoDao.listarAllActivosxContrato(id);
			listaCobroBean = this.mAsignarAdelantoCobro(listaAdelanto, listaCobroBean);
			model.addAttribute("registros", listaCobroBean);
			model.addAttribute("filtro",filtro);
			//Se almacena en session para las operaciones de asociacion y desasociacion
			request.getSession().setAttribute("listaAdelantoSession", listaCobroBean);
			request.getSession().setAttribute("filtroAdelantoSession", filtro);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			listaAdelanto		= null;
			listaCobroBean		= null;
			
		}
		return path;
	}
	/**
	 * Se encarga de direccionar a la pantalla de historial de desembolso
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cobro/historial/garantia/{id}", method = RequestMethod.GET)
	public String mostrarListadoAdelantoGarantia(@PathVariable Integer id, Model model, HttpServletRequest request) {
		String path 								= "";
		List<TblAdelanto> listaAdelantoGarantia		= null;
		List<CobroBean> listaCobroBean				= null;
		Filtro filtro								= null;
		try{
			path = "caja/cobro/cob_garantia_listado";
			filtro = new Filtro();
			filtro.setCodigoContrato(id);
			listaAdelantoGarantia = adelantoDao.listarAllActivosxContratoGarantia(id);
			listaCobroBean = this.mAsignarAdelantoCobro(listaAdelantoGarantia, listaCobroBean);
			model.addAttribute("registros", listaCobroBean);
			model.addAttribute("filtro",filtro);
			//Se almacena en session para las operaciones de asociacion y desasociacion
			request.getSession().setAttribute("listaGarantiaSession", listaCobroBean);
			request.getSession().setAttribute("filtroGarantiaSession", filtro);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			listaAdelantoGarantia		= null;
			listaCobroBean				= null;
			
		}
		return path;
	}
	
	/**
	 * Se encarga de asociar el adelanto para el cobro	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cobro/adelanto/asociar/{id}", method = RequestMethod.GET)
	public String asociarCobroAdelanto(@PathVariable Integer id, Model model, HttpServletRequest request) {
		String path 								= "";
		List<CobroBean> listaCobroBean				= null;
		try{
			
			path = "caja/cobro/cob_adelanto_listado";
			listaCobroBean = (List<CobroBean>)request.getSession().getAttribute("listaAdelantoSession");
			for(CobroBean cobro:listaCobroBean){
				if (cobro.getCodigoAdelanto().compareTo(id)==0){
					cobro.setSeleccionado(true);
				}
			}
			request.getSession().setAttribute("listaAdelantoSession", listaCobroBean);
			model.addAttribute("registros", listaCobroBean);
			model.addAttribute("filtro", request.getSession().getAttribute("filtroAdelantoSession"));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			listaCobroBean		= null;
		}
		return path;
	}
	/**
	 * Se encarga de desasociar el adelanto para el cobro	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cobro/adelanto/desasociar/{id}", method = RequestMethod.GET)
	public String desasociarCobroAdelanto(@PathVariable Integer id, Model model, HttpServletRequest request) {
		String path 								= "";
		List<CobroBean> listaCobroBean				= null;
		try{
			
			path = "caja/cobro/cob_adelanto_listado";
			listaCobroBean = (List<CobroBean>)request.getSession().getAttribute("listaAdelantoSession");
			for(CobroBean cobro:listaCobroBean){
				if (cobro.getCodigoAdelanto().compareTo(id)==0){
					cobro.setSeleccionado(false);
				}
			}
			request.getSession().setAttribute("listaAdelantoSession", listaCobroBean);
			model.addAttribute("registros", listaCobroBean);
			model.addAttribute("filtro", request.getSession().getAttribute("filtroAdelantoSession"));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			listaCobroBean		= null;
		}
		return path;
	}
	/**
	 * Se encarga de asociar el adelanto para el cobro	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cobro/garantia/asociar/{id}", method = RequestMethod.GET)
	public String asociarCobroGarantia(@PathVariable Integer id, Model model, HttpServletRequest request) {
		String path 								= "";
		List<CobroBean> listaCobroBean				= null;
		try{
			
			path = "caja/cobro/cob_garantia_listado";
			listaCobroBean = (List<CobroBean>)request.getSession().getAttribute("listaGarantiaSession");
			for(CobroBean cobro:listaCobroBean){
				if (cobro.getCodigoAdelanto().compareTo(id)==0){
					cobro.setSeleccionado(true);
				}
			}
			request.getSession().setAttribute("listaGarantiaSession", listaCobroBean);
			model.addAttribute("registros", listaCobroBean);
			model.addAttribute("filtro", request.getSession().getAttribute("filtroGarantiaSession"));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			listaCobroBean		= null;
		}
		return path;
	}
	/**
	 * Se encarga de desasociar el adelanto para el cobro	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cobro/garantia/desasociar/{id}", method = RequestMethod.GET)
	public String desasociarCobroGarantia(@PathVariable Integer id, Model model, HttpServletRequest request) {
		String path 								= "";
		List<CobroBean> listaCobroBean				= null;
		try{
			
			path = "caja/cobro/cob_garantia_listado";
			listaCobroBean = (List<CobroBean>)request.getSession().getAttribute("listaGarantiaSession");
			for(CobroBean cobro:listaCobroBean){
				if (cobro.getCodigoAdelanto().compareTo(id)==0){
					cobro.setSeleccionado(false);
				}
			}
			request.getSession().setAttribute("listaGarantiaSession", listaCobroBean);
			model.addAttribute("registros", listaCobroBean);
			model.addAttribute("filtro", request.getSession().getAttribute("filtroGarantiaSession"));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			listaCobroBean		= null;
		}
		return path;
	}
	/*
	 * Carga nuevamente los listados que se muestran en la edicion
	 */
	public void mCargarDatosInicialesEdicion(Model model, TblContrato contrato, HttpServletRequest request){
		BeanRequest beanRequest					= null;
		
		beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
		
		List<TblCxcDocumento> listaCxcAlquiler	= null;
		List<TblCxcDocumento> listaCxcAlquilerAux = null;
		List<TblCxcDocumento> listaCxcServicio	= null;
		List<TblCxcDocumento> listaCxcServicioAux = null;
		List<TblCxcDocumento> listaCxcLuz		= null;
		List<TblArbitrio> listaCxcArbitrio		= null;
		List<TblCxcDocumento> listaCxcPrimerCobro = null;
		List<TblCxcDocumento> listaCxcGarantia	= null;
		List<TblAdelanto> listaAdelanto			= null;
		List<TblTipoCambio> listaTipoCambio		= null;
		CobroBean cobroAlquiler					= null;
		CobroPrimerCobro cobroPrimerCobro		= null;
		CobroServicioBean cobroServicio			= null;
		CobroLuzBean cobroLuz					= null;
		CobroArbitrioBean cobroArbitrio			= null;
		CobroGarantia cobroGarantia				= null;
		
		//Tipo de Cambio
		listaTipoCambio = tipoCambioDao.buscarOneByFecha(UtilSGT.getDatetoString(UtilSGT.getFecha("dd/MM/YYYY")));
		
		//servicioTipo.setTblTipoServicio(new TblTipoServicio());
		//CxC Alquiler
		cobroAlquiler = new CobroBean();
		listaCxcAlquiler= this.mListarAlquiler(cobroAlquiler, listaTipoCambio, contrato.getCodigoContrato());
		
		//CxC Servicio
		cobroServicio  = new CobroServicioBean();
		listaCxcServicio = this.mListarServicio( cobroServicio, listaTipoCambio, contrato.getCodigoContrato());
		
		//CxC Luz
		cobroLuz  = new CobroLuzBean();
		listaCxcLuz= this.mListarLuz(cobroLuz, listaTipoCambio, contrato.getCodigoContrato());
		
		//CxC Arbitrio
		cobroArbitrio  = new CobroArbitrioBean();
		listaCxcArbitrio= this.mListarArbitrio(cobroArbitrio, listaTipoCambio, contrato.getCodigoContrato());
		
		
		//CxC Primeros cobros
		cobroPrimerCobro = new CobroPrimerCobro();
		listaCxcPrimerCobro= this.mListarPrimerosCobros(cobroPrimerCobro, listaTipoCambio, contrato.getCodigoContrato());
		//Juntamos primeros cobros y alquiler
		
		listaCxcAlquilerAux = listaCxcAlquiler;
		listaCxcServicioAux = listaCxcServicio;
		if (listaCxcPrimerCobro != null){
			for(TblCxcDocumento tblCxcDocumento: listaCxcPrimerCobro){
				if (tblCxcDocumento.getNombre().contains("ALQUILER")){
					listaCxcAlquilerAux = this.adicionaInicioListado(listaCxcAlquilerAux, tblCxcDocumento);
				}
				if (tblCxcDocumento.getNombre().contains("SERVICIO")){
					listaCxcServicioAux = this.adicionaInicioListado(listaCxcServicioAux, tblCxcDocumento);
				}
			}
		}
		listaCxcAlquiler = listaCxcAlquilerAux;
		listaCxcServicio = listaCxcServicioAux;
		//CxC Garantia
		cobroGarantia = new CobroGarantia();
		listaCxcGarantia = this.mListarGarantia(cobroGarantia, listaTipoCambio, contrato.getCodigoContrato());
		//Validacion de adelantos
		listaAdelanto = adelantoDao.listarAllActivosxContrato(contrato.getCodigoContrato());
		if (listaAdelanto!=null && listaAdelanto.size()>0){
			beanRequest.setFlagAdelanto(Constantes.ESTADO_REGISTRO_ACTIVO);
		}else{
			beanRequest.setFlagAdelanto(Constantes.ESTADO_REGISTRO_INACTIVO);
		}
		
		beanRequest.setListaCxcAlquiler(listaCxcAlquiler);	
		beanRequest.setListaCxcPrimerCobro(listaCxcPrimerCobro);
		beanRequest.setListaCxcServicio(listaCxcServicio);
		beanRequest.setListaCxcLuz(listaCxcLuz);
		beanRequest.setListaCxcArbitrio(listaCxcArbitrio);
		beanRequest.setListaCxcGarantia(listaCxcGarantia);
		
		beanRequest.setContrato(contrato);
		beanRequest.setContratoServicio(new TblContratoServicio());
		beanRequest.setContratoPrimerCobro(new TblContratoPrimerCobro());
		beanRequest.setArbitrio(new TblArbitrio());
		beanRequest.setObservacion(new TblObservacion());
		
		
		beanRequest.setCobroAlquiler(cobroAlquiler);
		beanRequest.setCobroPrimerCobro(cobroPrimerCobro);
		//beanRequest.setServicioTipo(servicioTipo);
		//beanRequest.setMapServicio(mapServicio);
		beanRequest.setCobroServicio(cobroServicio);
		beanRequest.setCobroLuz(cobroLuz);
		beanRequest.setCobroArbitrio(cobroArbitrio);
		
		beanRequest.setHistorialCobroAlquiler(null);
		beanRequest.setHistorialCobroArbitrio(null);
		beanRequest.setHistorialCobroLuz(null);
		beanRequest.setHistorialCobroPrimerCobro(null);
		beanRequest.setHistorialCobroServicio(null);
		beanRequest.setHistorialCobroGarantia(null);
		//control de los mensajes
		obtenerSaldoMesParaMensajeAlquiler(beanRequest, contrato.getCodigoContrato());
		obtenerSaldoMesParaMensajeServicio(beanRequest, contrato.getCodigoContrato());
		obtenerSaldoMesParaMensajeLuz(beanRequest, contrato.getCodigoContrato());
		obtenerSaldoMesParaMensajeArbitrio(beanRequest, contrato.getCodigoContrato());
		
		this.cargarListasRequestBeanContrato(model, beanRequest);
		this.limpiaDatosBanzarizado(beanRequest);
		this.cargarListaOperacionContrato(model);
		
		/*Inicializamos los datos de Bancarización*/
		request.getSession().setAttribute("beanRequest", beanRequest);
	}
	
	private void limpiaDatosBanzarizado(BeanRequest beanRequest){
		CobroGeneralBean  cobroGeneralBean  = beanRequest.getCobroGeneralBean();
		//Alquiler
		cobroGeneralBean.getCobroAlquiler().setMonto(new BigDecimal("0"));
		cobroGeneralBean.getCobroAlquiler().setCalculado(new BigDecimal("0"));
		cobroGeneralBean.getCobroAlquiler().setTipoPago(Constantes.TIPO_PAGO_COD_EFECTIVO);
		cobroGeneralBean.getCobroAlquiler().setTipoBancarizado("-1");
		cobroGeneralBean.getCobroAlquiler().setNumeroOperacion("");
		cobroGeneralBean.getCobroAlquiler().setFechaOperacion(new Date());
		cobroGeneralBean.getCobroAlquiler().setMensajeCobro("");
	
		cobroGeneralBean.getCobroServicio().setMonto(new BigDecimal("0"));
		cobroGeneralBean.getCobroServicio().setCalculado(new BigDecimal("0"));
		cobroGeneralBean.getCobroServicio().setTipoPago(Constantes.TIPO_PAGO_COD_EFECTIVO);
		cobroGeneralBean.getCobroServicio().setTipoBancarizado("-1");
		cobroGeneralBean.getCobroServicio().setNumeroOperacion("");
		cobroGeneralBean.getCobroServicio().setFechaOperacion(new Date());
		cobroGeneralBean.getCobroServicio().setMensajeCobro("");
	
		cobroGeneralBean.getCobroLuz().setMonto(new BigDecimal("0"));
		cobroGeneralBean.getCobroLuz().setCalculado(new BigDecimal("0"));
		cobroGeneralBean.getCobroLuz().setTipoPago(Constantes.TIPO_PAGO_COD_EFECTIVO);
		cobroGeneralBean.getCobroLuz().setTipoBancarizado("-1");
		cobroGeneralBean.getCobroLuz().setNumeroOperacion("");
		cobroGeneralBean.getCobroLuz().setFechaOperacion(new Date());
		cobroGeneralBean.getCobroLuz().setMensajeCobro("");
		
		cobroGeneralBean.getCobroArbitrio().setMonto(new BigDecimal("0"));
		cobroGeneralBean.getCobroArbitrio().setCalculado(new BigDecimal("0"));
		cobroGeneralBean.getCobroArbitrio().setTipoPago(Constantes.TIPO_PAGO_COD_EFECTIVO);
		cobroGeneralBean.getCobroArbitrio().setTipoBancarizado("-1");
		cobroGeneralBean.getCobroArbitrio().setNumeroOperacion("");
		cobroGeneralBean.getCobroArbitrio().setFechaOperacion(new Date());
		cobroGeneralBean.getCobroArbitrio().setMensajeCobro("");
	
	}
	
	/**
	 * Se encarga de registrar los cobros con base a los adelantos
	 * 
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cobro/adelanto/aplicar", method = RequestMethod.POST)
	public String registrarCobroconAdelanto(Model model, Filtro filtro,HttpServletRequest request) {
		String path 								= "";
		List<CobroBean> listaCobroBean				= null;
		TblContrato contrato						= null;
		boolean resultado							= false;
		
		try{
			path = "caja/cobro/cob_adelanto_listado";
			listaCobroBean = (List<CobroBean>)request.getSession().getAttribute("listaAdelantoSession");
			if (this.mValidarSeleccion(listaCobroBean)){
				contrato = contratoDao.findOne(filtro.getCodigoContrato());
				//Realizamos el cobro por adelanto encontrado
				resultado = this.mRealizarCobroxAdelanto(listaCobroBean, contrato, request);
				if (resultado){
					this.mListarAdelantosxContrato(model, filtro, request);
					model.addAttribute("respuesta", "Se realizó los cobros exitosamente.");
					//Cargar nuevamente los listados
					//this.mCargarDatosInicialesEdicion(model, contrato, request);
				}else{
					model.addAttribute("respuesta", "No se pudo realizar los cobros, se generó error en el Proceso. Comunicarse con el Administrador.");
				}
			}else{
				this.mListarAdelantosxContrato(model, filtro, request);
				model.addAttribute("respuesta", "No se encontró adelantos para realizar el cobro.");
			}
			
		
		
		}catch(Exception e){
			log.debug("[regresarContrato] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			listaCobroBean	 	= null;
			contrato			= null;
		}
		return path;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cobro/garantia/aplicar", method = RequestMethod.POST)
	public String registrarCobroGarantiaconAdelanto(Model model, Filtro filtro,HttpServletRequest request) {
		String path 								= "";
		List<CobroBean> listaCobroBean				= null;
		TblContrato contrato						= null;
		boolean resultado							= false;
		
		try{
			path = "caja/cobro/cob_garantia_listado";
			listaCobroBean = (List<CobroBean>)request.getSession().getAttribute("listaGarantiaSession");
			if (this.mValidarSeleccion(listaCobroBean)){
				contrato = contratoDao.findOne(filtro.getCodigoContrato());
				//Realizamos el cobro por adelanto encontrado
				resultado = this.mRealizarCobroxAdelanto(listaCobroBean, contrato, request);
				if (resultado){
					this.mListarAdelantosGarantiaxContrato(model, filtro, request);
					model.addAttribute("respuesta", "Se realizó los cobros exitosamente.");
					//Cargar nuevamente los listados
					//this.mCargarDatosInicialesEdicion(model, contrato, request);
				}else{
					model.addAttribute("respuesta", "No se pudo realizar los cobros, se generó error en el Proceso. Comunicarse con el Administrador.");
				}
			}else{
				this.mListarAdelantosxContrato(model, filtro, request);
				model.addAttribute("respuesta", "No se encontró adelantos para realizar el cobro.");
			}
			
		
		
		}catch(Exception e){
			log.debug("[regresarContrato] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			listaCobroBean	 	= null;
			contrato			= null;
		}
		return path;
	}
	/*
	 * Lista los adelantos por contrato y los setea en session
	 */
	public void mListarAdelantosxContrato(Model model, Filtro filtro, HttpServletRequest request){
		List<CobroBean> listaCobroBean				= null;
		List<TblAdelanto> listaAdelanto				= null;
		
		listaAdelanto = adelantoDao.listarAllActivosxContrato(filtro.getCodigoContrato());
		listaCobroBean = this.mAsignarAdelantoCobro(listaAdelanto, listaCobroBean);
		model.addAttribute("registros", listaCobroBean);
		model.addAttribute("filtro",filtro);
		//Se almacena en session para las operaciones de asociacion y desasociacion
		request.getSession().setAttribute("listaAdelantoSession", listaCobroBean);
		request.getSession().setAttribute("filtroAdelantoSession", filtro);
	}
	public void mListarAdelantosGarantiaxContrato(Model model, Filtro filtro, HttpServletRequest request){
		List<CobroBean> listaCobroBean				= null;
		List<TblAdelanto> listaAdelanto				= null;
		
		listaAdelanto = adelantoDao.listarAllActivosxContratoGarantia(filtro.getCodigoContrato());
		listaCobroBean = this.mAsignarAdelantoCobro(listaAdelanto, listaCobroBean);
		model.addAttribute("registros", listaCobroBean);
		model.addAttribute("filtro",filtro);
		//Se almacena en session para las operaciones de asociacion y desasociacion
		request.getSession().setAttribute("listaGarantiaSession", listaCobroBean);
		request.getSession().setAttribute("filtroGarantiaSession", filtro);
	}
	/*
	 * Valida la existencia de algun adelanto seleccionado para el cobro
	 */
	public boolean mValidarSeleccion(List<CobroBean> listaCobroBean){
		boolean resultado = false;
		try{
			if (listaCobroBean !=null && listaCobroBean.size()>0){
				for(CobroBean cobro : listaCobroBean){
					if (cobro.isSeleccionado()){
						resultado = true;
						break;
					}
				}
			}
		}catch(Exception e){
			
		}
		return resultado;
	}
	
	/*
	 * Obtenemos las deudas del contrato con base al tipo adelanto
	 */
	public List<TblCxcDocumento> mGetDeudaxTipoCobro(String strTipoCobro, TblContrato contrato){
		List<TblCxcDocumento> listaDeuda = null;
		Specification<TblCxcDocumento> criterio	= null;
		Sort sort = new Sort(new Sort.Order(Direction.ASC, "codigoCxcDoc"));
		try{
			criterio = Specifications.where(conCodigoContrato(contrato.getCodigoContrato()))
					  .and(conTipoReferencia(strTipoCobro))
					  .and(conSaldoPositivo(new BigDecimal("0")));
			listaDeuda = cxcDocumentoDao.findAll(criterio, sort);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return listaDeuda;
	}
	
	/*
	 * Registra los cobros con base a los adelantos
	 */
	public boolean mRealizarCobroxAdelanto(List<CobroBean> listaCobroBean, TblContrato contrato, HttpServletRequest request){
		boolean resultado 						= false;
		boolean adelantoSuperior				= false;
		TblCobro cobroAlquiler 					= null;
		List<TblCxcDocumento> listaDeuda 		= null;
		//boolean actualizarDocumento 			= false;
		TblDesembolso desembolso				= null;
		TblAdelanto adelanto					= null;
		
		try{
			
			//Recorremos los adelantos
			if (listaCobroBean !=null && listaCobroBean.size()>0){
				for(CobroBean cobroBean: listaCobroBean){
					listaDeuda = this.mGetDeudaxTipoCobro(cobroBean.getTipoCobro(), contrato);
					adelantoSuperior = this.mValidarAdelantoSuperaDeuda(listaDeuda, cobroBean);
					//Obtenemos el desembolso
					desembolso = desembolsoDao.findOne(cobroBean.getCodigoDesembolso());
					//registramos el cobro de la deuda
					if(listaDeuda !=null && listaDeuda.size()>0){
						for(TblCxcDocumento documento: listaDeuda){
							//Moneda: DOLARES
							if (documento.getTipoMoneda().equals(Constantes.MONEDA_DOLAR)){
								if (cobroBean.getMontoDolares().doubleValue()>0 ){
									//Monto cobrado Mayor o Igual al Saldo
									if (cobroBean.getMontoDolares().doubleValue()>=documento.getSaldo().doubleValue()){
										//Cobro
										cobroAlquiler = this.mRegistrarCobroSinSaldoDolares(documento, cobroBean, desembolso, request);
										//CxC Documento
										documento.setMontoCobrado(documento.getMontoCobrado().add(documento.getSaldo()));											
										documento.setSaldo(new BigDecimal("0"));
										this.preEditarDocumento(documento, request);
										cxcDocumentoDao.save(documento);
										
										cobroBean.setMontoDolares(cobroBean.getMontoDolares().subtract(cobroAlquiler.getMontoDolares()));
										cobroBean.setMontoSoles(cobroBean.getMontoSoles().subtract(cobroAlquiler.getMontoSoles()));
										//Datos para la reversion del adelanto
										if (cobroBean.getMontoDolaresConsumido()==null){
											cobroBean.setMontoDolaresConsumido(cobroAlquiler.getMontoDolares());
										}else{
											cobroBean.setMontoDolaresConsumido(cobroBean.getMontoDolaresConsumido().add(cobroAlquiler.getMontoDolares()));
										}
										if (cobroBean.getMontoSolesConsumido()==null){
											cobroBean.setMontoSolesConsumido(cobroAlquiler.getMontoSoles());
										}else{
											cobroBean.setMontoSolesConsumido(cobroBean.getMontoSolesConsumido().add(cobroAlquiler.getMontoSoles()));
										}
										//actualizarDocumento = false;
										//break;
									}else{
										//Cobro
										cobroAlquiler = this.mRegistrarCobroConSaldoDolares(documento, cobroBean, desembolso, request);
										//CxC Documento
										documento.setMontoCobrado(documento.getMontoCobrado().add(cobroBean.getMontoDolares()));	
										documento.setSaldo(documento.getSaldo().subtract(cobroBean.getMontoDolares()));
										this.preEditarDocumento(documento, request);
										cxcDocumentoDao.save(documento);
										//Datos para la reversion del adelanto
										if (cobroBean.getMontoDolaresConsumido()==null){
											cobroBean.setMontoDolaresConsumido(cobroBean.getMontoDolares());
										}else{
											cobroBean.setMontoDolaresConsumido(cobroBean.getMontoDolaresConsumido().add(cobroBean.getMontoDolares()));
										}
										if (cobroBean.getMontoSolesConsumido()==null){
											cobroBean.setMontoSolesConsumido(cobroAlquiler.getMontoSoles());
										}else{
											cobroBean.setMontoSolesConsumido(cobroBean.getMontoSolesConsumido().add(cobroBean.getMontoSoles()));
										}
										//actualizamos el cobro
										cobroBean.setMontoDolares(new BigDecimal("0"));
										cobroBean.setMontoSoles(new BigDecimal("0"));
										
										break;//Finalizamos el ciclo de lista de deudas porque se agoto el saldo del cobro
									}
								}
								
							}else{
								if (cobroBean.getMontoSoles().doubleValue()>0){
									if (cobroBean.getMontoSoles().doubleValue()>=documento.getSaldo().doubleValue()){
										//Cobro
										cobroAlquiler = this.mRegistrarCobroSinSaldoSoles(documento, cobroBean, desembolso, request);
										//CxC Documento
										documento.setMontoCobrado(documento.getMontoCobrado().add(documento.getSaldo()));
										documento.setSaldo(new BigDecimal("0"));
										this.preEditarDocumento(documento, request);
										cxcDocumentoDao.save(documento);
										//actualizamos el cobro
										cobroBean.setMontoDolares(cobroBean.getMontoDolares().subtract(cobroAlquiler.getMontoDolares()));
										cobroBean.setMontoSoles(cobroBean.getMontoSoles().subtract(cobroAlquiler.getMontoSoles()));
										//Datos para la reversion del adelanto
										if (cobroBean.getMontoDolaresConsumido()==null){
											cobroBean.setMontoDolaresConsumido(cobroAlquiler.getMontoDolares());
										}else{
											cobroBean.setMontoDolaresConsumido(cobroBean.getMontoDolaresConsumido().add(cobroAlquiler.getMontoDolares()));
										}
										if (cobroBean.getMontoSolesConsumido()==null){
											cobroBean.setMontoSolesConsumido(cobroAlquiler.getMontoSoles());
										}else{
											cobroBean.setMontoSolesConsumido(cobroBean.getMontoSolesConsumido().add(cobroAlquiler.getMontoSoles()));
										}
										//actualizarDocumento = false;
										//break;
									}else{
										//Cobro
										cobroAlquiler =  this.mRegistrarCobroConSaldoSoles(documento, cobroBean, desembolso, request);
										//CxC Documento
										documento.setSaldo(documento.getSaldo().subtract(cobroBean.getMontoSoles()));
										documento.setMontoCobrado(documento.getMontoCobrado().add(cobroBean.getMontoSoles()));
										this.preEditarDocumento(documento, request);
										cxcDocumentoDao.save(documento);
										//Datos para la reversion del adelanto
										if (cobroBean.getMontoDolaresConsumido()==null){
											cobroBean.setMontoDolaresConsumido(cobroBean.getMontoDolares());
										}else{
											cobroBean.setMontoDolaresConsumido(cobroBean.getMontoDolaresConsumido().add(cobroBean.getMontoDolares()));
										}
										if (cobroBean.getMontoSolesConsumido()==null){
											cobroBean.setMontoSolesConsumido(cobroAlquiler.getMontoSoles());
										}else{
											cobroBean.setMontoSolesConsumido(cobroBean.getMontoSolesConsumido().add(cobroBean.getMontoSoles()));
										}
										//Cobro agotado
										cobroBean.setMontoDolares(new BigDecimal("0"));
										cobroBean.setMontoSoles(new BigDecimal("0"));
										break;//Finalizamos el ciclo de lista de deudas porque se agoto el saldo del cobro
									}
								}
							}
							
							
						}//Fin del ciclo de deuda
					}//Fin de FIN la validacion del ciclo de deuda
					
					//Actualizamos el adelanto
					adelanto = adelantoDao.findOne(cobroBean.getCodigoAdelanto());
					adelanto.setMontoDolares(cobroBean.getMontoDolares());
					adelanto.setMontoSoles(cobroBean.getMontoSoles());
					//Datos para la reversion
					adelanto.setMontoDolaresConsumido(cobroBean.getMontoDolaresConsumido());
					adelanto.setMontoSolesConsumido(cobroBean.getMontoSolesConsumido());
					if (!adelantoSuperior){
						adelanto.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
					}
					this.preEditarAdelanto(adelanto, request);
					adelantoDao.save(adelanto);
				}//Fin del ciclo de los adelantos cobrados
			}
			
			resultado = true;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultado;
	}
	/*
	 * Registra un cobro, donde el saldo del documento quedara en cero
	 */
	public TblCobro  mRegistrarCobroSinSaldoDolares(TblCxcDocumento documento,CobroBean cobroBean,TblDesembolso desembolso, HttpServletRequest request){
		TblCobro cobroAlquiler = null;
		cobroAlquiler = new TblCobro();
		cobroAlquiler.setFechaCobro(UtilSGT.getDatetoString(UtilSGT.getFecha("dd/MM/YYYY")));
		cobroAlquiler.setMontoDolares(documento.getSaldo());
		cobroAlquiler.setTipoCambio(cobroBean.getTipoCambio());
		cobroAlquiler.setMontoSoles(documento.getSaldo().multiply(cobroBean.getTipoCambio()).setScale(2, RoundingMode.CEILING));
		cobroAlquiler.setTblCxcDocumento(documento);
		cobroAlquiler.setTipoCobro(cobroBean.getTipoCobro());
		cobroAlquiler.setTblDesembolso(desembolso);
		cobroAlquiler.setTipoMoneda(documento.getTipoMoneda());
		this.preGuardarCobro(cobroAlquiler, request);
		cobroDao.save(cobroAlquiler);
		
		return 	cobroAlquiler;	
		
	}
	/*
	 * Registra un cobro, donde el saldo del documento sera diferente de cero
	 */
	public TblCobro  mRegistrarCobroConSaldoDolares(TblCxcDocumento documento,CobroBean cobroBean,TblDesembolso desembolso, HttpServletRequest request){
		TblCobro cobroAlquiler = null;
		cobroAlquiler = new TblCobro();
		cobroAlquiler.setFechaCobro(UtilSGT.getDatetoString(UtilSGT.getFecha("dd/MM/YYYY")));
		cobroAlquiler.setMontoDolares(cobroBean.getMontoDolares());
		cobroAlquiler.setTipoCambio(cobroBean.getTipoCambio());
		cobroAlquiler.setMontoSoles(cobroBean.getMontoDolares().multiply(cobroBean.getTipoCambio()).setScale(2, RoundingMode.CEILING));
		cobroAlquiler.setTblCxcDocumento(documento);
		cobroAlquiler.setTipoCobro(cobroBean.getTipoCobro());
		cobroAlquiler.setTblDesembolso(desembolso);
		cobroAlquiler.setTipoMoneda(documento.getTipoMoneda());
		this.preGuardarCobro(cobroAlquiler, request);
		cobroDao.save(cobroAlquiler);
		
		return 	cobroAlquiler;	
		
	}
	/*
	 * Registra un cobro, donde el saldo del documento quedara en cero y registra el cobro por el saldo del documento
	 */
	public TblCobro  mRegistrarCobroSinSaldoSoles(TblCxcDocumento documento,CobroBean cobroBean,TblDesembolso desembolso, HttpServletRequest request){
		TblCobro cobroAlquiler = null;
		cobroAlquiler = new TblCobro();
		cobroAlquiler.setFechaCobro(UtilSGT.getDatetoString(UtilSGT.getFecha("dd/MM/YYYY")));
		cobroAlquiler.setMontoSoles(documento.getSaldo());
		cobroAlquiler.setTipoCambio(cobroBean.getTipoCambio());
		cobroAlquiler.setMontoDolares(documento.getSaldo().divide(cobroBean.getTipoCambio(), 2, RoundingMode.CEILING));
		cobroAlquiler.setTblCxcDocumento(documento);
		cobroAlquiler.setTipoCobro(cobroBean.getTipoCobro());
		cobroAlquiler.setTblDesembolso(desembolso);
		cobroAlquiler.setTipoMoneda(documento.getTipoMoneda());
		this.preGuardarCobro(cobroAlquiler, request);
		cobroDao.save(cobroAlquiler);
		
		return 	cobroAlquiler;	
		
	}
	/*
	 * Registra un cobro, donde el saldo del documento sera diferente de cero y se registra el residuo del cobro
	 */
	public TblCobro  mRegistrarCobroConSaldoSoles(TblCxcDocumento documento,CobroBean cobroBean,TblDesembolso desembolso, HttpServletRequest request){
		TblCobro cobroAlquiler = null;
		cobroAlquiler = new TblCobro();
		cobroAlquiler.setFechaCobro(UtilSGT.getDatetoString(UtilSGT.getFecha("dd/MM/YYYY")));
		cobroAlquiler.setMontoSoles(cobroBean.getMontoSoles());
		cobroAlquiler.setTipoCambio(cobroBean.getTipoCambio());
		cobroAlquiler.setMontoDolares(cobroBean.getMontoSoles().divide(cobroBean.getTipoCambio(),2, RoundingMode.CEILING));
		cobroAlquiler.setTblCxcDocumento(documento);
		cobroAlquiler.setTipoCobro(cobroBean.getTipoCobro());
		cobroAlquiler.setTblDesembolso(desembolso);
		cobroAlquiler.setTipoMoneda(documento.getTipoMoneda());
		this.preGuardarCobro(cobroAlquiler, request);
		cobroDao.save(cobroAlquiler);
		
		return 	cobroAlquiler;	
		
	}
	/*
	 * Valida si el adelanto supera la deuda existente
	 */
	public boolean mValidarAdelantoSuperaDeuda(List<TblCxcDocumento> listaDeuda, CobroBean cobroBean){
		boolean resultado 			= false;
		BigDecimal montoDeuda 		= new BigDecimal("0");
		BigDecimal montoAdelanto 	= new BigDecimal("0");
		String tipoMoneda			= null;
		try{
			//Totalizamos el saldo de la deuda
			if (listaDeuda!=null && listaDeuda.size()>0){
				for(TblCxcDocumento documento: listaDeuda){
					montoDeuda = montoDeuda.add(documento.getSaldo());
					tipoMoneda = documento.getTipoMoneda();
				}
				
			}else{
				return true;
			}
			
			//Obtenemos el monto del adelanto
			if (tipoMoneda.equals(Constantes.MONEDA_DOLAR)){
				montoAdelanto = montoAdelanto.add(cobroBean.getMontoDolares());
			}else{
				montoAdelanto = montoAdelanto.add(cobroBean.getMontoSoles());
			}
	
			//Comparamos para identificar
			if ( montoAdelanto.compareTo(montoDeuda)<=0){
				resultado 		= false;
			}else{
				resultado 		= true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultado;
	}
	/*@RequestMapping(value = "/cobro/primerCobro/individual", method = RequestMethod.POST)
	public String individualCobroPrimerCobro(Model model, CobroPrimerCobro cobroPrimerCobro, TblContrato entidad, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		TblCobro cobroAlquilerAux			= null;
		boolean adelanto					= false;
		TblContrato contrato				= null;
		try{
			log.debug("[individualCobroPrimerCobro] Inicio");
			path = "caja/cobro/cob_edicion";
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			if (this.validarCobroPrimerCobro(model, cobroPrimerCobro, request)){
				//Calculo del Monto en Soles y Dolares
				UtilSGT.mCalcularMontoCobro(cobroPrimerCobro);
				//Asigna Fecha, Montos, y tipo de cambio
				cobroAlquilerAux = UtilSGT.mAsignarDatoCobro(cobroPrimerCobro);
				//Adiciona en el historial
				this.mAsignarHistorialCobroPrimerCobro(request, cobroAlquilerAux);
				//model.addAttribute("respuestaPrimerCobro", "Se adiciono el Cobro. Esta pendiente el registro.");
				contrato = contratoDao.findOne(entidad.getCodigoContrato());
				//Primeros Cobros
				if (beanRequest.getHistorialCobroPrimerCobro() !=null && beanRequest.getHistorialCobroPrimerCobro().size()>0){
					adelanto = this.mRegistrarCobroContrato(beanRequest.getHistorialCobroPrimerCobro(), contrato, request, Constantes.TIPO_COBRO_PRIMER_COBRO);
					if (adelanto){
						//Registrar Adelanto de Primer Cobro en Alquiler
						this.mRegistrarAdelanto(beanRequest.getHistorialCobroPrimerCobro(), contrato, request, Constantes.TIPO_COBRO_ALQUILER);
					}
					//Se carga nuevamente la informacion
					this.mCargarDatosInicialesEdicion(model, contrato, request);
					
					model.addAttribute("resultadoOperacion", "Se registró exitosamente la operación");
				}else{
					//Se carga nuevamente la informacion
					this.mCargarDatosInicialesEdicion(model, contrato, request);
					
					model.addAttribute("resultadoOperacion", "No se encontró montos a cobrar");
				}
				
				
			}else{
				beanRequest.setCobroPrimerCobro(cobroPrimerCobro);
				this.cargarListasRequestBeanContrato(model, beanRequest);
				this.cargarListaOperacionContrato(model);
			}
			
			
			
			//model.addAttribute("listaServicio", beanRequest.getListaServicio());
//			beanRequest.setCobroPrimerCobro(cobroPrimerCobro);
//			this.cargarListasRequestBeanContrato(model, beanRequest);
//			this.cargarListaOperacionContrato(model);

			request.getSession().setAttribute("beanRequest", beanRequest);

			log.debug("[individualCobroPrimerCobro] Fin");
		}catch(Exception e){
			log.debug("[individualCobroPrimerCobro] Error:"+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se genero un error inesperado:"+e.getMessage());
		}finally{
			beanRequest = null;
		}

		return path;
	}
	*/
	/*
	 * Paginado
	 */
	@RequestMapping(value = "/cobro/paginado/{page}/{size}/{operacion}", method = RequestMethod.GET)
	public String paginarEntidad(@PathVariable Integer page, @PathVariable Integer size, @PathVariable String operacion, Model model,  PageableSG pageable, HttpServletRequest request) {
		TblContrato filtro = null;
		String path = null;
		try{
			//log.debug("[traerRegistros] Inicio");
			path = "caja/cobro/cob_listado";
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
			filtro = (TblContrato)request.getSession().getAttribute("sessionFiltroCriterioCobro");
			model.addAttribute("filtro", filtro);
			
			this.listarContratos(model, filtro, pageable, this.urlPaginado,request);
			
		}catch(Exception e){
			//log.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		return path;
	}
	/**
	 * Listado de Tipo de primeros cobros
	 * 
	 */
	public Map<String, Object> obtenerValoresPrimerosCobros(List<TblCxcDocumento> listaCxcPrimerCobro) {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		if(listaCxcPrimerCobro !=null){
			for(TblCxcDocumento tblCxcDocumento: listaCxcPrimerCobro){
				resultados.put(tblCxcDocumento.getNombre(), tblCxcDocumento.getNombre());
			}
		}
		return resultados;
	}
	
	@RequestMapping(value = "/cobro/edicion/regresar", method = RequestMethod.GET)
	public String vistaPreviaRegresar(Model model, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		
		try{
			log.debug("[vistaPreviaRegresar] Inicio");
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			model.addAttribute("cobroGeneralBean", beanRequest.getCobroGeneralBean());
			this.cargarListasRequestBeanContrato(model, beanRequest);
			this.cargarListaOperacionContrato(model);
			path = "caja/cobro/cob_edicion";
			
			log.debug("[vistaPreviaRegresar] Fin");
		}catch(Exception e){
			log.debug("[vistaPreviaRegresar] Error:"+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se genero un error inesperado:"+e.getMessage());
		}finally{
			beanRequest = null;
		}

		return path;
	}
	
	private String getMonedaAlquiler(HttpServletRequest request) {
		BeanRequest beanRequest = null;
		beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
		return beanRequest.getContrato().getTipoMonedaAlquiler();
	}
	private String getNombreMoneda(String codigoMoneda) {
		if (codigoMoneda.equals(Constantes.MONEDA_SOL)) {
			return Constantes.DESC_MONEDA_SOL;
		}else {
			return Constantes.DESC_MONEDA_DOLAR;
		}
	}
	public boolean validarProductoMoneda(Model model, CobroGeneralBean cobroGeneralBean, String path, HttpServletRequest request){
		boolean resultado = false;
		String msg		= "";
		String moneda 	= null;
		
		if (cobroGeneralBean.getCobroAlquiler().getMonto() != null && cobroGeneralBean.getCobroAlquiler().getMonto().doubleValue() > 0) {
			
			if (cobroGeneralBean.getCobroAlquiler().getTipoMoneda().equals(Constantes.NO_SELECCIONADO) ){
				msg = msg + " Se debe seleccionar el tipo de moneda en el Alquiler";
				
			}
		
			moneda = getMonedaAlquiler(request);
			
			if (!cobroGeneralBean.getCobroAlquiler().getTipoMoneda().equals(moneda) && 
				(cobroGeneralBean.getCobroAlquiler().getTipoCambio() == null || cobroGeneralBean.getCobroAlquiler().getTipoCambio().doubleValue() == 0)
				){
				msg = msg + "Debe ingresar el tipo de cambio en el Alquiler para el calculo en " +getNombreMoneda(moneda);
			}
		}
		if (cobroGeneralBean.getCobroServicio().getMonto() != null && cobroGeneralBean.getCobroServicio().getMonto().doubleValue() > 0)
			if (cobroGeneralBean.getCobroServicio().getTipoMonedaServicio().equals(Constantes.NO_SELECCIONADO)){
				msg = msg + " Se debe seleccionar el tipo de moneda en el Servicio";
			}
		
		if (cobroGeneralBean.getCobroServicio().getTipoMonedaServicio().equals(Constantes.MONEDA_DOLAR) && 
			(cobroGeneralBean.getCobroServicio().getTipoCambioServicio() == null || cobroGeneralBean.getCobroServicio().getTipoCambioServicio().doubleValue() == 0)
			){
			msg = msg + "Debe ingresar el tipo de cambio en el Servicio para el calculo en Soles . ";
		}
		if (cobroGeneralBean.getCobroLuz().getMonto() != null && cobroGeneralBean.getCobroLuz().getMonto().doubleValue() > 0)
			if (cobroGeneralBean.getCobroLuz().getTipoMonedaLuz().equals(Constantes.NO_SELECCIONADO)){
				msg = msg + " Se debe seleccionar el tipo de moneda en la Luz";
			}
		
		if (cobroGeneralBean.getCobroLuz().getTipoMonedaLuz().equals(Constantes.MONEDA_DOLAR) && 
			(cobroGeneralBean.getCobroLuz().getTipoCambioLuz() == null || cobroGeneralBean.getCobroLuz().getTipoCambioLuz().doubleValue() == 0)
			){
			msg = msg + "Debe ingresar el tipo de cambio en la Luz para el calculo en Soles . ";
		}
		if (cobroGeneralBean.getCobroArbitrio().getTipoMonedaArbitrio().equals(Constantes.MONEDA_DOLAR) && 
				(cobroGeneralBean.getCobroArbitrio().getTipoMonedaArbitrio() == null || cobroGeneralBean.getCobroArbitrio().getTipoCambioArbitrio().doubleValue() == 0)
				){
				msg = msg + "Debe ingresar el tipo de cambio en el Arbitrio para el calculo en Soles . ";
			}
		if (msg.length()>0){
			resultado = true;
			model.addAttribute("resultadoOperacion", msg);
		}
		return resultado;
	}
	
	@RequestMapping(value = "/cobro/editar/previo", method = RequestMethod.POST)
	public String vistaPreviaCobro(Model model, CobroGeneralBean cobroGeneralBean, String path, HttpServletRequest request) {
		BeanRequest beanRequest				= null;
		
		try{
			log.debug("[vistaPreviaCobro] Inicio");
			path = "caja/cobro/cob_edicion_previo";
			//Validacion del tipo de cambio por producto y moneda
			if (this.validarProductoMoneda(model, cobroGeneralBean, path, request)){
				beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
				model.addAttribute("cobroGeneralBean", cobroGeneralBean);
				beanRequest.setCobroGeneralBean(cobroGeneralBean);
				this.cargarListasRequestBeanContrato(model, beanRequest);
				this.cargarListaOperacionContrato(model);
				path = "caja/cobro/cob_edicion";
			}else{
				//Validación de datos
				if (!mValidarDatosEdicion(model, cobroGeneralBean, request)){
					this.asignarDatosSesion(request, model, cobroGeneralBean);
					model.addAttribute("resultadoOperacion", "Debe ingresar el monto a cobrar. ");
					path = "caja/cobro/cob_edicion";
				}else{
					//Validación de los datos de bancarizado
					if (!validarBancarizado(cobroGeneralBean, model)){
						this.asignarDatosSesion(request, model, cobroGeneralBean);
						path = "caja/cobro/cob_edicion";
					}else{
						beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
						beanRequest.setCobroGeneralBean(cobroGeneralBean);
						this.cargarListasRequestBeanContrato(model, beanRequest);
						this.cargarListaOperacionContrato(model);
						request.getSession().setAttribute("beanRequest", beanRequest);
					}
				}
			}
			
			
			
			log.debug("[vistaPreviaCobro] Fin");
		}catch(Exception e){
			log.debug("[vistaPreviaCobro] Error:"+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se genero un error inesperado:"+e.getMessage());
		}finally{
			beanRequest = null;
		}

		return path;
	}
	/*Asigna los datos en sesion*/
	private void asignarDatosSesion(HttpServletRequest request, Model model, CobroGeneralBean cobroGeneralBean){
		BeanRequest beanRequest				= null;
		beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
		model.addAttribute("cobroGeneralBean", cobroGeneralBean);
		beanRequest.setCobroGeneralBean(cobroGeneralBean);
		this.cargarListasRequestBeanContrato(model, beanRequest);
		this.cargarListaOperacionContrato(model);
	}
	/*Validar datos de Bancarizado*/
	private boolean validarBancarizado(CobroGeneralBean cobroGeneralBean, Model model){
		boolean resultado = true;
		
		if (cobroGeneralBean.getCobroAlquiler().getMonto() != null && cobroGeneralBean.getCobroAlquiler().getMonto().compareTo(new BigDecimal("0")) > 0){
			resultado = resultado && this.validarBancarizadoAlquiler(cobroGeneralBean, model);
		}
		if (cobroGeneralBean.getCobroServicio().getMonto()!= null && cobroGeneralBean.getCobroServicio().getMonto().compareTo(new BigDecimal("0")) > 0){
			resultado = resultado && this.validarBancarizadoServicio(cobroGeneralBean, model);
		}
		if (cobroGeneralBean.getCobroLuz().getMonto()!= null && cobroGeneralBean.getCobroLuz().getMonto().compareTo(new BigDecimal("0")) > 0){
			resultado = resultado && this.validarBancarizadoLuz(cobroGeneralBean, model);
		}
		if (cobroGeneralBean.getCobroArbitrio().getMonto()!= null && cobroGeneralBean.getCobroArbitrio().getMonto().compareTo(new BigDecimal("0")) > 0){
			resultado = resultado && this.validarBancarizadoArbitrio(cobroGeneralBean, model);
		}
		return resultado;
	}
	
	/*Validamos los datos de la bancarización Alquiler*/
	private boolean validarBancarizadoAlquiler(CobroGeneralBean cobroGeneralBean, Model model){
		//validamos el tipo de pago
		if (!cobroGeneralBean.getCobroAlquiler().getTipoPago().equals(Constantes.TIPO_PAGO_COD_EFECTIVO) && 
			!cobroGeneralBean.getCobroAlquiler().getTipoPago().equals(Constantes.TIPO_PAGO_COD_BANCARIZADO)){
			model.addAttribute("resultadoOperacion", "Debe seleccionar el tipo de pago. ");
			return false;
		}
		//Si es bancarizado
		if (cobroGeneralBean.getCobroAlquiler().getTipoPago().equals(Constantes.TIPO_PAGO_COD_BANCARIZADO)){
			if (cobroGeneralBean.getCobroAlquiler().getTipoBancarizado().equals("-1")){
				model.addAttribute("resultadoOperacion", "Debe seleccionar el tipo de operación. ");
				return false;
			}
			if (cobroGeneralBean.getCobroAlquiler().getNumeroOperacion() == null || cobroGeneralBean.getCobroAlquiler().getNumeroOperacion().trim().length()<=0){
				model.addAttribute("resultadoOperacion", "Debe ingresar el número de la operación. ");
				return false;
			}
			if (cobroGeneralBean.getCobroAlquiler().getFechaOperacion() == null ){
				model.addAttribute("resultadoOperacion", "Debe ingresar la fecha de operación. ");
				return false;
			}
		}
		return true;
	}
	/*Validamos los datos de la bancarización Servicio*/
	private boolean validarBancarizadoServicio(CobroGeneralBean cobroGeneralBean, Model model){
		//validamos el tipo de pago
		if (!cobroGeneralBean.getCobroServicio().getTipoPago().equals(Constantes.TIPO_PAGO_COD_EFECTIVO) && 
			!cobroGeneralBean.getCobroServicio().getTipoPago().equals(Constantes.TIPO_PAGO_COD_BANCARIZADO)){
			model.addAttribute("resultadoOperacion", "Debe seleccionar el tipo de pago del Servicio. ");
			return false;
		}
		//Si es bancarizado
		if (cobroGeneralBean.getCobroServicio().getTipoPago().equals(Constantes.TIPO_PAGO_COD_BANCARIZADO)){
			if (cobroGeneralBean.getCobroServicio().getTipoBancarizado().equals("-1")){
				model.addAttribute("resultadoOperacion", "Debe seleccionar el tipo de operación del Servicio. ");
				return false;
			}
			if (cobroGeneralBean.getCobroServicio().getNumeroOperacion() == null || cobroGeneralBean.getCobroServicio().getNumeroOperacion().trim().length()<=0){
				model.addAttribute("resultadoOperacion", "Debe ingresar el número de la operación del Servicio. ");
				return false;
			}
			if (cobroGeneralBean.getCobroServicio().getFechaOperacion() == null ){
				model.addAttribute("resultadoOperacion", "Debe ingresar la fecha de operación del Servicio. ");
				return false;
			}
		}
		return true;
	}
	/*Validamos los datos de la bancarización Luz*/
	private boolean validarBancarizadoLuz(CobroGeneralBean cobroGeneralBean, Model model){
		//validamos el tipo de pago
		if (!cobroGeneralBean.getCobroLuz().getTipoPago().equals(Constantes.TIPO_PAGO_COD_EFECTIVO) && 
			!cobroGeneralBean.getCobroLuz().getTipoPago().equals(Constantes.TIPO_PAGO_COD_BANCARIZADO)){
			model.addAttribute("resultadoOperacion", "Debe seleccionar el tipo de pago de Luz. ");
			return false;
		}
		//Si es bancarizado
		if (cobroGeneralBean.getCobroLuz().getTipoPago().equals(Constantes.TIPO_PAGO_COD_BANCARIZADO)){
			if (cobroGeneralBean.getCobroLuz().getTipoBancarizado().equals("-1")){
				model.addAttribute("resultadoOperacion", "Debe seleccionar el tipo de operación de Luz. ");
				return false;
			}
			if (cobroGeneralBean.getCobroLuz().getNumeroOperacion() == null || cobroGeneralBean.getCobroLuz().getNumeroOperacion().trim().length()<=0){
				model.addAttribute("resultadoOperacion", "Debe ingresar el número de la operación de Luz. ");
				return false;
			}
			if (cobroGeneralBean.getCobroLuz().getFechaOperacion() == null ){
				model.addAttribute("resultadoOperacion", "Debe ingresar la fecha de operación de Luz. ");
				return false;
			}
		}
		return true;
	}
	/*Validamos los datos de la bancarización Arbitrio*/
	private boolean validarBancarizadoArbitrio(CobroGeneralBean cobroGeneralBean, Model model){
		//validamos el tipo de pago
		if (!cobroGeneralBean.getCobroArbitrio().getTipoPago().equals(Constantes.TIPO_PAGO_COD_EFECTIVO) && 
			!cobroGeneralBean.getCobroArbitrio().getTipoPago().equals(Constantes.TIPO_PAGO_COD_BANCARIZADO)){
			model.addAttribute("resultadoOperacion", "Debe seleccionar el tipo de pago de Arbitrio. ");
			return false;
		}
		//Si es bancarizado
		if (cobroGeneralBean.getCobroArbitrio().getTipoPago().equals(Constantes.TIPO_PAGO_COD_BANCARIZADO)){
			if (cobroGeneralBean.getCobroArbitrio().getTipoBancarizado().equals("-1")){
				model.addAttribute("resultadoOperacion", "Debe seleccionar el tipo de operación de Arbitrio. ");
				return false;
			}
			if (cobroGeneralBean.getCobroArbitrio().getNumeroOperacion() == null || cobroGeneralBean.getCobroArbitrio().getNumeroOperacion().trim().length()<=0){
				model.addAttribute("resultadoOperacion", "Debe ingresar el número de la operación de Arbitrio. ");
				return false;
			}
			if (cobroGeneralBean.getCobroArbitrio().getFechaOperacion() == null ){
				model.addAttribute("resultadoOperacion", "Debe ingresar la fecha de operación de Arbitrio. ");
				return false;
			}
		}
		return true;
	}
	public boolean mValidarDatosEdicion(Model model, CobroGeneralBean cobroGeneralBean, HttpServletRequest request) throws Exception{
		boolean resultado = true;
		
		
		
		if (mValidarMontoNull(cobroGeneralBean)){
			//validación del tipo de cambio
			UtilSGT.mValidarTipoCambio(cobroGeneralBean.getCobroAlquiler());
			UtilSGT.mValidarTipoCambio(cobroGeneralBean.getCobroServicio());
			UtilSGT.mValidarTipoCambio(cobroGeneralBean.getCobroLuz());
			UtilSGT.mValidarTipoCambio(cobroGeneralBean.getCobroArbitrio());
			
			UtilSGT.mCalcularMontoCobro(cobroGeneralBean.getCobroAlquiler());
			UtilSGT.mCalcularMontoCobro(cobroGeneralBean.getCobroServicio());
			UtilSGT.mCalcularMontoCobro(cobroGeneralBean.getCobroLuz());
			UtilSGT.mCalcularMontoCobro(cobroGeneralBean.getCobroArbitrio());
			//UtilSGT.mCalcularMontoCobro(cobroGeneralBean.getCobroPrimerCobro());
			if (!mValidarMonto(cobroGeneralBean)){
				resultado = false;
			}
		}else{
			resultado = false;
		}
		return resultado;
	}
	
	public boolean mValidarMontoNull(CobroGeneralBean cobroGeneralBean){
		boolean resultado = false;
		if (UtilSGT.mValidarMonto(cobroGeneralBean.getCobroAlquiler().getMonto())){
			return true;
		}else if (UtilSGT.mValidarMonto(cobroGeneralBean.getCobroServicio().getMonto())){
			return true;
		/*}else if (UtilSGT.mValidarMonto(cobroGeneralBean.getCobroPrimerCobro().getMonto())){
			return true;*/
		}else if (UtilSGT.mValidarMonto(cobroGeneralBean.getCobroLuz().getMonto())){
			return true;
		}else if (UtilSGT.mValidarMonto(cobroGeneralBean.getCobroArbitrio().getMonto())){
			return true;
		}
		return resultado;
	}
	public boolean mValidarMonto(CobroGeneralBean cobroGeneralBean){
		boolean resultado = true;
		int contador = 0;
		
		if (cobroGeneralBean.getCobroAlquiler().getMonto() == null ){
			cobroGeneralBean.getCobroAlquiler().setMonto(new BigDecimal("0"));
		}
		if (cobroGeneralBean.getCobroServicio().getMonto() == null){
			cobroGeneralBean.getCobroServicio().setMonto(new BigDecimal("0"));
		}
		/*if (cobroGeneralBean.getCobroPrimerCobro().getMonto() == null){
			cobroGeneralBean.getCobroPrimerCobro().setMonto(new BigDecimal("0"));
		}*/
		if (cobroGeneralBean.getCobroLuz().getMonto() == null ){
			cobroGeneralBean.getCobroLuz().setMonto(new BigDecimal("0"));
		}
		if (cobroGeneralBean.getCobroArbitrio().getMonto() == null ){
			cobroGeneralBean.getCobroArbitrio().setMonto(new BigDecimal("0"));
		}
		
		if (cobroGeneralBean.getCobroAlquiler().getMonto() != null && cobroGeneralBean.getCobroAlquiler().getMonto().compareTo(new BigDecimal("0")) > 0){
			contador = contador + 1;
		}
		if (cobroGeneralBean.getCobroServicio().getMonto()!= null && cobroGeneralBean.getCobroServicio().getMonto().compareTo(new BigDecimal("0")) > 0){
			contador = contador + 1;
		}
		/*if (cobroGeneralBean.getCobroPrimerCobro().getMonto()!= null && cobroGeneralBean.getCobroPrimerCobro().getMonto().compareTo(new BigDecimal("0")) > 0){
			contador = contador + 1;
		}*/
		if (cobroGeneralBean.getCobroLuz().getMonto()!= null && cobroGeneralBean.getCobroLuz().getMonto().compareTo(new BigDecimal("0")) > 0){
			contador = contador + 1;
		}
		if (cobroGeneralBean.getCobroArbitrio().getMonto()!= null && cobroGeneralBean.getCobroArbitrio().getMonto().compareTo(new BigDecimal("0")) > 0){
			contador = contador + 1;
		}
		if (contador <= 0){
			resultado = false;
		}
		return resultado;
	}
	
	private BigDecimal getSoles(BigDecimal dolares, BigDecimal tipoCambio) {
		BigDecimal soles = null;
		if (tipoCambio == null || tipoCambio.compareTo(new BigDecimal("0"))==0) {
			soles = new BigDecimal("0");
		}else {
			soles = dolares.multiply(tipoCambio).setScale(2, RoundingMode.CEILING);
		}
		return soles;
	}
	private BigDecimal getDolares(BigDecimal soles, BigDecimal tipoCambio) {
		BigDecimal dolares = null;
		if (tipoCambio == null || tipoCambio.compareTo(new BigDecimal("0"))==0) {
			dolares = new BigDecimal("0");
		}else {
			dolares = soles.divide(tipoCambio,RoundingMode.HALF_UP);
		}
		
		return dolares;
	}
	
	private CobroBean getDesembolsoDolares(BigDecimal monto, CobroBean cobroBean) {
		CobroBean cobroBeanDesembolsos		= null;
		cobroBeanDesembolsos = new CobroBean();
		
		cobroBeanDesembolsos.setMontoDolares(monto);
		cobroBeanDesembolsos.setMontoSoles(this.getSoles(monto, cobroBean.getTipoCambio()));
		cobroBeanDesembolsos.setFechaCobro(cobroBean.getFechaCobro());
		cobroBeanDesembolsos.setTipoCambio(cobroBean.getTipoCambio());
		cobroBeanDesembolsos.setTipoMoneda(cobroBean.getTipoMoneda());
		return cobroBeanDesembolsos;
	}
	private CobroBean getDesembolsoSoles(BigDecimal monto, CobroBean cobroBean) {
		CobroBean cobroBeanDesembolsos		= null;
		cobroBeanDesembolsos = new CobroBean();
		cobroBeanDesembolsos.setMontoSoles(monto);
		cobroBeanDesembolsos.setMontoDolares(this.getDolares(monto, cobroBean.getTipoCambio()));
		cobroBeanDesembolsos.setFechaCobro(cobroBean.getFechaCobro());
		cobroBeanDesembolsos.setTipoCambio(cobroBean.getTipoCambio());
		cobroBeanDesembolsos.setTipoMoneda(cobroBean.getTipoMoneda());
		return cobroBeanDesembolsos;
	}
	private CobroServicioBean getDesembolsoDolaresServicio(BigDecimal monto, CobroServicioBean cobroBean) {
		CobroServicioBean cobroBeanDesembolsos		= null;
		cobroBeanDesembolsos = new CobroServicioBean();
		cobroBeanDesembolsos.setMontoDolaresServicio(monto);
		cobroBeanDesembolsos.setMontoSolesServicio(this.getSoles(monto, cobroBean.getTipoCambioServicio()));
		cobroBeanDesembolsos.setFechaCobroServicio(cobroBean.getFechaCobroServicio());
		cobroBeanDesembolsos.setTipoCambioServicio(cobroBean.getTipoCambioServicio());
		cobroBeanDesembolsos.setTipoMonedaServicio(cobroBean.getTipoMonedaServicio());
		return cobroBeanDesembolsos;
	}
	private CobroServicioBean getDesembolsoSolesServicio(BigDecimal monto, CobroServicioBean cobroBean) {
		CobroServicioBean cobroBeanDesembolsos		= null;
		cobroBeanDesembolsos = new CobroServicioBean();
		cobroBeanDesembolsos.setMontoSolesServicio(monto);
		cobroBeanDesembolsos.setMontoDolaresServicio(this.getDolares(monto, cobroBean.getTipoCambioServicio()));
		cobroBeanDesembolsos.setFechaCobroServicio(cobroBean.getFechaCobroServicio());
		cobroBeanDesembolsos.setTipoCambioServicio(cobroBean.getTipoCambioServicio());
		cobroBeanDesembolsos.setTipoMonedaServicio(cobroBean.getTipoMonedaServicio());
		return cobroBeanDesembolsos;
	}
	private CobroLuzBean getDesembolsoDolaresLuz(BigDecimal monto, CobroLuzBean cobroBean) {
		CobroLuzBean cobroBeanDesembolsos		= null;
		cobroBeanDesembolsos = new CobroLuzBean();
		cobroBeanDesembolsos.setMontoDolaresLuz(monto);
		cobroBeanDesembolsos.setMontoSolesLuz(this.getSoles(monto, cobroBean.getTipoCambioLuz()));
		cobroBeanDesembolsos.setFechaCobroLuz(cobroBean.getFechaCobroLuz());
		cobroBeanDesembolsos.setTipoCambioLuz(cobroBean.getTipoCambioLuz());
		cobroBeanDesembolsos.setTipoMonedaLuz(cobroBean.getTipoMonedaLuz());
		return cobroBeanDesembolsos;
	}
	private CobroLuzBean getDesembolsoSolesLuz(BigDecimal monto, CobroLuzBean cobroBean) {
		CobroLuzBean cobroBeanDesembolsos		= null;
		cobroBeanDesembolsos = new CobroLuzBean();
		cobroBeanDesembolsos.setMontoSolesLuz(monto);
		cobroBeanDesembolsos.setMontoDolaresLuz(this.getDolares(monto, cobroBean.getTipoCambioLuz()));
		cobroBeanDesembolsos.setFechaCobroLuz(cobroBean.getFechaCobroLuz());
		cobroBeanDesembolsos.setTipoCambioLuz(cobroBean.getTipoCambioLuz());
		cobroBeanDesembolsos.setTipoMonedaLuz(cobroBean.getTipoMonedaLuz());
		return cobroBeanDesembolsos;
	}
	private CobroArbitrioBean getDesembolsoSolesArbitrio(BigDecimal monto, CobroArbitrioBean cobroBean) {
		CobroArbitrioBean cobroBeanDesembolsos		= null;
		cobroBeanDesembolsos = new CobroArbitrioBean();
		cobroBeanDesembolsos.setMontoSolesArbitrio(monto);
		cobroBeanDesembolsos.setMontoDolaresArbitrio(this.getDolares(monto, cobroBean.getTipoCambioArbitrio()));
		cobroBeanDesembolsos.setFechaCobroArbitrio(cobroBean.getFechaCobroArbitrio());
		cobroBeanDesembolsos.setTipoCambioArbitrio(cobroBean.getTipoCambioArbitrio());
		cobroBeanDesembolsos.setTipoMonedaArbitrio(cobroBean.getTipoMonedaArbitrio());
		return cobroBeanDesembolsos;
	}	
	
	private void getNewMontoAlquilerDolar(TblCobro cobro,TblCxcDocumento documento,BigDecimal margeSaldo,CobroBean cobroBean) {
		String estadoComparacion = getDiferencia(cobro.getMontoDolares(), documento.getSaldo(), margeSaldo);
		log.info("[getNewMontoAlquilerDolar] estadoComparacion:"+estadoComparacion);
		if ( estadoComparacion.equals(PAGO_EXCESO) ){
			cobro.setMontoDolares(cobro.getMontoDolares().subtract(documento.getSaldo()));
		}else if ( estadoComparacion.equals(PAGO_EXACTO) ) {
			cobro.setMontoDolares(new BigDecimal("0"));
		}else {
			cobro.setMontoDolares(documento.getSaldo().subtract(cobro.getMontoDolares()));
		}

	}
	private void getNewMontoAlquilerSol(TblCobro cobro,TblCxcDocumento documento,BigDecimal margeSaldo,CobroBean cobroBean) {
		String estadoComparacion = getDiferencia(cobro.getMontoSoles(), documento.getSaldo(), margeSaldo);
		log.info("[getNewMontoAlquilerSol] estadoComparacion:"+estadoComparacion);
		if ( estadoComparacion.equals(PAGO_EXCESO) ){
			cobro.setMontoSoles(cobro.getMontoSoles().subtract(documento.getSaldo()));
		}else if ( estadoComparacion.equals(PAGO_EXACTO) ) {
			cobro.setMontoSoles(new BigDecimal("0"));
		}else {
			cobro.setMontoSoles(documento.getSaldo().subtract(cobro.getMontoSoles()));
		}

	}
	/*Obtenemos una lista de desembolsos para registrarlos posteriormente*/
	private List<CobroBean> getListaDesembolsoAlquiler(CobroBean cobroBean, TblContrato contrato, HttpServletRequest request) {
		
		List<CobroBean> listaCobroAlquiler 	= new ArrayList<>();
		List<TblCxcDocumento> listaDeuda 	= null;
		BigDecimal margeSaldo 				= null;
		TblCobro cobro						= new TblCobro();
		CobroBean cobroBeanDesembolsos		= null;

		listaDeuda = this.mListarDeuda(contrato, request, Constantes.TIPO_COBRO_ALQUILER);
		margeSaldo = this.getSaldoCero(request, Constantes.TIPO_COBRO_ALQUILER);
		this.setMontoAlquiler(cobro, cobroBean);
		
		if(listaDeuda !=null && listaDeuda.size()>0){
			for(TblCxcDocumento documento: listaDeuda){
				if (cobro !=null && (cobro.getMontoDolares().doubleValue() >0 || cobro.getMontoSoles().doubleValue() > 0)){
					//actualizarDocumento = false;
						//Moneda: DOLARES
						if (documento.getTipoMoneda().equals(Constantes.MONEDA_DOLAR)){
							if (cobro.getMontoDolares().doubleValue()>0 ){
									//Valida si el monto pagado es mayor a la deuda, teniendo en cuenta el margen (+/- 1)
								if (this.validarDiferencia(cobro.getMontoDolares(), documento.getSaldo(), margeSaldo)){
									cobroBeanDesembolsos = setDesembolsoDolaresAlquiler(cobro, documento, margeSaldo, cobroBean);
									listaCobroAlquiler.add(cobroBeanDesembolsos);
									this.getNewMontoAlquilerDolar(cobro, documento, margeSaldo, cobroBeanDesembolsos);
									//cobro.setMontoDolares(cobro.getMontoDolares().subtract(documento.getSaldo()));
									
								}else{
									cobroBeanDesembolsos = this.getDesembolsoDolares(cobro.getMontoDolares(), cobroBean);
									listaCobroAlquiler.add(cobroBeanDesembolsos);
									cobro.setMontoDolares(new BigDecimal("0"));
									cobro.setMontoSoles(new BigDecimal("0"));
								}
							}
						}else{
							if (cobro.getMontoSoles().doubleValue()>0){
								//Valida si el monto pagado es mayor a la deuda, teniendo en cuenta el margen (+/- 1)
								if (this.validarDiferencia(cobro.getMontoSoles(), documento.getSaldo(), margeSaldo)){
									cobroBeanDesembolsos = setDesembolsoSolesAlquiler(cobro, documento, margeSaldo, cobroBean);
									listaCobroAlquiler.add(cobroBeanDesembolsos);
									this.getNewMontoAlquilerSol(cobro, documento, margeSaldo, cobroBeanDesembolsos);
									//cobro.setMontoSoles(cobro.getMontoSoles().subtract(documento.getSaldo()));
									
								}else{
									cobroBeanDesembolsos = this.getDesembolsoSoles(cobro.getMontoSoles(), cobroBean);
									listaCobroAlquiler.add(cobroBeanDesembolsos);
									cobro.setMontoSoles(new BigDecimal("0"));
									cobro.setMontoDolares(new BigDecimal("0"));
								}
							}
						}
				}else{
					break;
				}
			}
			
		}else{
			listaCobroAlquiler.add(cobroBean);
			
		}
		return listaCobroAlquiler;
	}

	private CobroBean setDesembolsoDolaresAlquiler( TblCobro cobro, TblCxcDocumento documento, BigDecimal margeSaldo, CobroBean cobroBean) {
		String estadoComparacion = getDiferencia(cobro.getMontoDolares(), documento.getSaldo(), margeSaldo);
		log.info("[setDesembolsoDolaresAlquiler] estadoComparacion:"+estadoComparacion);
		if ( estadoComparacion.equals(PAGO_EXCESO) ){
			return this.getDesembolsoDolares(documento.getSaldo(), cobroBean);
		}else if ( estadoComparacion.equals(PAGO_EXACTO) ) {
			return this.getDesembolsoDolares(cobro.getMontoDolares(), cobroBean);
		}else {
			return new CobroBean();
		}
	}
	private CobroBean setDesembolsoSolesAlquiler( TblCobro cobro, TblCxcDocumento documento, BigDecimal margeSaldo,CobroBean cobroBean) {
		String estadoComparacion = getDiferencia(cobro.getMontoSoles(), documento.getSaldo(), margeSaldo);
		log.info("[setDesembolsoSolesAlquiler] estadoComparacion:"+estadoComparacion);
		if ( estadoComparacion.equals(PAGO_EXCESO) ){
			return this.getDesembolsoSoles(documento.getSaldo(), cobroBean);
		}else if ( estadoComparacion.equals(PAGO_EXACTO) ) {
			return this.getDesembolsoSoles(cobro.getMontoSoles(), cobroBean);
		}else {
			return new CobroBean();
		}
	}
	
	private List<CobroServicioBean> getListaDesembolsoServicio(CobroServicioBean cobroBean, TblContrato contrato, HttpServletRequest request) {
		
		List<CobroServicioBean> listaCobroServicio 	= new ArrayList<>();
		List<TblCxcDocumento> listaDeuda 			= null;
		BigDecimal margeSaldo 						= null;
		TblCobro cobro								= new TblCobro();
		CobroServicioBean cobroBeanDesembolsos		= null;
		
		listaDeuda = this.mListarDeuda(contrato, request, Constantes.TIPO_COBRO_SERVICIO);
		margeSaldo = this.getSaldoCero(request, Constantes.TIPO_COBRO_SERVICIO);
		this.setMontoServicio(cobro, cobroBean);
		
		if(listaDeuda !=null && listaDeuda.size()>0){
			for(TblCxcDocumento documento: listaDeuda){
				if (cobro !=null && (cobro.getMontoDolares().doubleValue() >0 || cobro.getMontoSoles().doubleValue() > 0)){
					//actualizarDocumento = false;
						//Moneda: DOLARES
						if (documento.getTipoMoneda().equals(Constantes.MONEDA_DOLAR)){
							if (cobro.getMontoDolares().doubleValue()>0 ){
									//Valida si el monto pagado es mayor a la deuda, teniendo en cuenta el margen (+/- 1)
								if (this.validarDiferencia(cobro.getMontoDolares(), documento.getSaldo(), margeSaldo)){
									cobroBeanDesembolsos = this.getDesembolsoDolaresServicio(documento.getSaldo(), cobroBean);
									listaCobroServicio.add(cobroBeanDesembolsos);
									cobro.setMontoDolares(cobro.getMontoDolares().subtract(documento.getSaldo()));
									
								}else{
									cobroBeanDesembolsos = this.getDesembolsoDolaresServicio(cobro.getMontoDolares(), cobroBean);
									listaCobroServicio.add(cobroBeanDesembolsos);
									cobro.setMontoDolares(new BigDecimal("0"));
									cobro.setMontoSoles(new BigDecimal("0"));
								}
							}
						}else{
							if (cobro.getMontoSoles().doubleValue()>0){
								//Valida si el monto pagado es mayor a la deuda, teniendo en cuenta el margen (+/- 1)
								if (this.validarDiferencia(cobro.getMontoSoles(), documento.getSaldo(), margeSaldo)){
									cobroBeanDesembolsos = this.getDesembolsoSolesServicio(documento.getSaldo(), cobroBean);
									listaCobroServicio.add(cobroBeanDesembolsos);
									cobro.setMontoSoles(cobro.getMontoSoles().subtract(documento.getSaldo()));
									
								}else{
									cobroBeanDesembolsos = this.getDesembolsoSolesServicio(cobro.getMontoSoles(), cobroBean);
									listaCobroServicio.add(cobroBeanDesembolsos);
									cobro.setMontoSoles(new BigDecimal("0"));
									cobro.setMontoDolares(new BigDecimal("0"));
								}
							}
						}
				}else{
					break;
				}
			}
			
		}else{
			listaCobroServicio.add(cobroBean);
			
		}
		return listaCobroServicio;
	}
	
	private List<CobroLuzBean> getListaDesembolsoLuz(CobroLuzBean cobroBean, TblContrato contrato, HttpServletRequest request) {
		
		List<CobroLuzBean> listaCobroLuz		 	= new ArrayList<>();
		List<TblCxcDocumento> listaDeuda 			= null;
		BigDecimal margeSaldo 						= null;
		TblCobro cobro								= new TblCobro();
		CobroLuzBean cobroBeanDesembolsos		= null;
		
		listaDeuda = this.mListarDeuda(contrato, request, Constantes.TIPO_COBRO_LUZ);
		margeSaldo = this.getSaldoCero(request, Constantes.TIPO_COBRO_LUZ);
		this.setMontoLuz(cobro, cobroBean);
		
		if(listaDeuda !=null && listaDeuda.size()>0){
			for(TblCxcDocumento documento: listaDeuda){
				if (cobro !=null && (cobro.getMontoDolares().doubleValue() >0 || cobro.getMontoSoles().doubleValue() > 0)){
					//actualizarDocumento = false;
						//Moneda: DOLARES
						if (documento.getTipoMoneda().equals(Constantes.MONEDA_DOLAR)){
							if (cobro.getMontoDolares().doubleValue()>0 ){
									//Valida si el monto pagado es mayor a la deuda, teniendo en cuenta el margen (+/- 1)
								if (this.validarDiferencia(cobro.getMontoDolares(), documento.getSaldo(), margeSaldo)){
									cobroBeanDesembolsos = this.getDesembolsoDolaresLuz(documento.getSaldo(), cobroBean);
									listaCobroLuz.add(cobroBeanDesembolsos);
									cobro.setMontoDolares(cobro.getMontoDolares().subtract(documento.getSaldo()));
									
								}else{
									cobroBeanDesembolsos = this.getDesembolsoDolaresLuz(cobro.getMontoDolares(), cobroBean);
									listaCobroLuz.add(cobroBeanDesembolsos);
									cobro.setMontoDolares(new BigDecimal("0"));
									cobro.setMontoSoles(new BigDecimal("0"));
								}
							}
						}else{
							if (cobro.getMontoSoles().doubleValue()>0){
								//Valida si el monto pagado es mayor a la deuda, teniendo en cuenta el margen (+/- 1)
								if (this.validarDiferencia(cobro.getMontoSoles(), documento.getSaldo(), margeSaldo)){
									cobroBeanDesembolsos = this.getDesembolsoSolesLuz(documento.getSaldo(), cobroBean);
									listaCobroLuz.add(cobroBeanDesembolsos);
									cobro.setMontoSoles(cobro.getMontoSoles().subtract(documento.getSaldo()));
									
								}else{
									cobroBeanDesembolsos = this.getDesembolsoSolesLuz(cobro.getMontoSoles(), cobroBean);
									listaCobroLuz.add(cobroBeanDesembolsos);
									cobro.setMontoSoles(new BigDecimal("0"));
									cobro.setMontoDolares(new BigDecimal("0"));
								}
							}
						}
				}else{
					break;
				}
			}
			
		}else{
			listaCobroLuz.add(cobroBean);
			
		}
		return listaCobroLuz;
	}
	private List<CobroArbitrioBean> getListaDesembolsoArbitrio(CobroArbitrioBean cobroBean, TblContrato contrato, HttpServletRequest request) {
		
		List<CobroArbitrioBean> listaCobroArbitrio 	= new ArrayList<>();
		List<TblArbitrio> listaDeuda 				= null;
		BigDecimal margeSaldo 						= null;
		TblCobroArbitrio cobro						= new TblCobroArbitrio();
		CobroArbitrioBean cobroBeanDesembolsos		= null;
		
		listaDeuda = this.mListarDeudaArbitrio(contrato.getTblTienda().getCodigoTienda());
		margeSaldo = this.getSaldoCero(request, Constantes.TIPO_COBRO_LUZ);
		this.setMontoArbitrio(cobro, cobroBean);
		
		if(listaDeuda !=null && listaDeuda.size()>0){
			for(TblArbitrio documento: listaDeuda){
				if (cobro !=null && (cobro.getMontoDolares().doubleValue() >0 || cobro.getMontoSoles().doubleValue() > 0)){
					//actualizarDocumento = false;
						//Moneda: SOLES
						
							if (cobro.getMontoSoles().doubleValue()>0){
								//Valida si el monto pagado es mayor a la deuda, teniendo en cuenta el margen (+/- 1)
								if (this.validarDiferencia(cobro.getMontoSoles(), documento.getSaldo(), margeSaldo)){
									cobroBeanDesembolsos = this.getDesembolsoSolesArbitrio(documento.getSaldo(), cobroBean);
									listaCobroArbitrio.add(cobroBeanDesembolsos);
									cobro.setMontoSoles(cobro.getMontoSoles().subtract(documento.getSaldo()));
									
								}else{
									cobroBeanDesembolsos = this.getDesembolsoSolesArbitrio(cobro.getMontoSoles(), cobroBean);
									listaCobroArbitrio.add(cobroBeanDesembolsos);
									cobro.setMontoSoles(new BigDecimal("0"));
									cobro.setMontoDolares(new BigDecimal("0"));
								}
							}
						
				}else{
					break;
				}
			}
			
		}else{
			listaCobroArbitrio.add(cobroBean);
			
		}
		return listaCobroArbitrio;
	}
	/**
	 * Se encarga de guardar la informacion 
	 * 
	 * @param 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/cobro/editar/guardar", method = RequestMethod.GET)
	public String actualizarEntidad(Model model, HttpServletRequest request, String path) {
		path 											= "caja/cobro/cob_edicion";
		BeanRequest beanRequest							= null;
		TblContrato contrato							= null;
		CobroGeneralBean cobroGeneralBean				= null;
		boolean adelanto								= false;
		TblCobro cobro									= null;
		TblCobroArbitrio cobroArbitrio					= null;
		TblContrato entidad								= null;
		
		List<CobroBean> listaCobroAlquiler				= null;
		List<CobroServicioBean> listaCobroServicio		= null;
		List<CobroLuzBean> listaCobroLuz				= null;
		List<CobroArbitrioBean> listaCobroArbitrio		= null;
		try{
			log.debug("[actualizarEntidad] Inicio" );
			//servicioTipo.setTblTipoServicio(new TblTipoServicio());
			beanRequest = (BeanRequest)request.getSession().getAttribute("beanRequest");
			cobroGeneralBean = beanRequest.getCobroGeneralBean();
			entidad = beanRequest.getContrato();
			log.debug("[actualizarEntidad] Pre Guardar..." );
			contrato = contratoDao.findOne(entidad.getCodigoContrato());
			//Alquiler
			if (cobroGeneralBean.getCobroAlquiler().getMonto().doubleValue() > 0){
				//this.setMontoAlquiler(cobro, cobroGeneralBean.getCobroAlquiler());
				listaCobroAlquiler = this.getListaDesembolsoAlquiler(cobroGeneralBean.getCobroAlquiler(), contrato, request);
				for(CobroBean cobroBean:listaCobroAlquiler) {
					cobro = new TblCobro();
					this.setMontoAlquiler(cobro, cobroBean);
					adelanto = this.mRegistrarCobroContrato(cobro, contrato, request, Constantes.TIPO_COBRO_ALQUILER,null);
				}
				if (adelanto){
					//Registrar Adelanto de Alquiler
					this.mRegistrarAdelanto(cobro, contrato, request, Constantes.TIPO_COBRO_ALQUILER);
					String notaPago =" ALQUILER: Adelanto de Alquiler, no se ha definido el periodo al que corresponde. "; 
					//Actualizar mensaje del desembolso
					this.mActualizarDesembolsoNota(cobro, notaPago);
				}
				//Limpiar variables
				cobroGeneralBean.getCobroAlquiler().setMonto(new BigDecimal("0"));
				cobroGeneralBean.getCobroAlquiler().setCalculado(new BigDecimal("0"));
			}
			
			//Servicio
			if (cobroGeneralBean.getCobroServicio().getMonto().doubleValue() > 0){
				//this.setMontoServicio(cobro, cobroGeneralBean.getCobroServicio());
				listaCobroServicio = this.getListaDesembolsoServicio(cobroGeneralBean.getCobroServicio(), contrato, request);
				for(CobroServicioBean cobroServicioBean: listaCobroServicio) {
					cobro = new TblCobro();
					this.setMontoServicio(cobro, cobroServicioBean);
					adelanto = this.mRegistrarCobroContrato(cobro, contrato, request, Constantes.TIPO_COBRO_SERVICIO,null);
				}
				
				if (adelanto){
					//Registrar Adelanto de Servicio
					this.mRegistrarAdelanto(cobro, contrato, request, Constantes.TIPO_COBRO_SERVICIO);
					String notaPago =" SERVICIO: Adelanto de Servicio, no se ha definido el periodo al que corresponde. "; 
					//Actualizar mensaje del desembolso
					this.mActualizarDesembolsoNota(cobro, notaPago);
				}
				//Limpiar variables
				cobroGeneralBean.getCobroServicio().setMonto(new BigDecimal("0"));
				cobroGeneralBean.getCobroServicio().setCalculado(new BigDecimal("0"));
			}
			
			//Luz
			if (cobroGeneralBean.getCobroLuz().getMonto().doubleValue() > 0){
				//this.setMontoLuz(cobro, cobroGeneralBean.getCobroLuz());
				listaCobroLuz = this.getListaDesembolsoLuz(cobroGeneralBean.getCobroLuz(), contrato, request);
				for(CobroLuzBean cobroLuzBean: listaCobroLuz) {
					cobro = new TblCobro();
					this.setMontoLuz(cobro, cobroLuzBean);
					adelanto = this.mRegistrarCobroContrato(cobro, contrato, request, Constantes.TIPO_COBRO_LUZ,null);
				}
				if (adelanto){
					//Registrar Adelanto de Luz
					this.mRegistrarAdelanto(cobro, contrato, request, Constantes.TIPO_COBRO_LUZ);
					String notaPago =" LUZ: Adelanto de Luz, no se ha definido el periodo al que corresponde. "; 
					//Actualizar mensaje del desembolso
					this.mActualizarDesembolsoNota(cobro, notaPago);
				}
				//Limpiar variables
				cobroGeneralBean.getCobroLuz().setMonto(new BigDecimal("0"));
				cobroGeneralBean.getCobroLuz().setCalculado(new BigDecimal("0"));
			}
			//Arbitrios
			if (cobroGeneralBean.getCobroArbitrio().getMonto().doubleValue() > 0){
				listaCobroArbitrio = this.getListaDesembolsoArbitrio(cobroGeneralBean.getCobroArbitrio(), contrato, request);
				for(CobroArbitrioBean cobroArbitrioBean: listaCobroArbitrio) {
					cobroArbitrio = new TblCobroArbitrio();
					this.setMontoArbitrio(cobroArbitrio, cobroArbitrioBean);
					adelanto = this.mRegistrarCobroArbitrio(cobroArbitrio, contrato, request, Constantes.TIPO_COBRO_ARBITRIO,null);
				}
				if (adelanto){
					//Registrar Adelanto de Arbitrio
					this.mRegistrarAdelantoArbitrio(cobroArbitrio, contrato, request, Constantes.TIPO_COBRO_ARBITRIO);
					String notaPago =" ARBITRIO: Adelanto de Arbitrio, no se ha definido el periodo al que corresponde. "; 
					//Actualizar mensaje del desembolso
					this.mActualizarDesembolsoNotaArbitrio(cobroArbitrio, notaPago);
				}
				//Limpiar variables
				cobroGeneralBean.getCobroArbitrio().setMonto(new BigDecimal("0"));
				cobroGeneralBean.getCobroArbitrio().setCalculado(new BigDecimal("0"));
			}
			
			//Arbitrios
			/*if (beanRequest.getHistorialCobroArbitrio() !=null && beanRequest.getHistorialCobroArbitrio().size()>0){
				adelanto = this.mRegistrarCobroContrato(beanRequest.getHistorialCobroArbitrio(), contrato, request, Constantes.TIPO_COBRO_ARBITRIO);
				if (adelanto){
					//Registrar Adelanto de Luz
					this.mRegistrarAdelanto(beanRequest.getHistorialCobroArbitrio(), contrato, request, Constantes.TIPO_COBRO_ARBITRIO);
				}
			}*/
			//Primeros Cobros
			/*if (cobroGeneralBean.getCobroPrimerCobro().getMonto()!= null && cobroGeneralBean.getCobroPrimerCobro().getMonto().doubleValue() > 0){
				this.setMontoPrimerCobro(cobro, cobroGeneralBean.getCobroPrimerCobro());
				adelanto = this.mRegistrarCobroContrato(cobro, contrato, request, Constantes.TIPO_COBRO_PRIMER_COBRO, cobroGeneralBean.getCobroPrimerCobro().getTipoPrimerCobro());
				if (adelanto){
					//Registrar Adelanto de Primer Cobro en Alquiler
					this.mRegistrarAdelanto(cobro, contrato, request, Constantes.TIPO_COBRO_ALQUILER);
				}
			}*/
			//Garantia
			/*if (beanRequest.getHistorialCobroGarantia() !=null && beanRequest.getHistorialCobroGarantia().size()>0){
				//Se registra el adelanto para su uso al finalizar el contrato
				
				adelanto = this.mRegistrarCobroContrato(beanRequest.getHistorialCobroGarantia(), contrato, request, Constantes.TIPO_COBRO_GARANTIA);
				
			}*/
			//Se carga nuevamente la informacion
			//this.mCargarDatosInicialesEdicion(model, contrato, request);
			this.mCargarDatosInicialesEdicion(model, entidad, request);
			this.mEditarContrato(entidad.getCodigoContrato(), model, request);
			
			model.addAttribute("resultadoOperacion", "Se registró exitosamente la operación");
			log.debug("[actualizarEntidad] Fin" );
		}catch(Exception e){
			e.printStackTrace();
			model.addAttribute("resultadoOperacion", "Se generó un error inesperado: "+e.getMessage());
		}finally{
			beanRequest			= null;
			contrato			= null;
			
			
		}
		return path;

	}
	
	/*
	 * Muestra el comprobante como solo lectura
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cobro/editar/pdf/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> pdfComprobante(@PathVariable Integer id, HttpServletRequest request) {
		FiltroPdf filtro								= null;
		List<TblDetalleComprobante> listaDetalle 	= null;
		TblComprobante comprobante					= null;
		HttpHeaders headers 						= new HttpHeaders();
		ByteArrayInputStream bis 					= null;
		TblSunatCabecera sunatCabecera				= null;
		List<TblSunatDetalle> listaDetalleSunat		= null;
		//ComprobantePdf comprobantePdf				= new ComprobantePdf();
		ComprobanteKenorPdf comprobantePdf			= new ComprobanteKenorPdf();
		TblLeyenda leyenda							= null;
		//TblCatalogo domicilio						= null;
		try{
			filtro = new FiltroPdf();
			sunatCabecera = sunatCabeceraDao.findByCodigoDocumento(id);
			if (sunatCabecera!=null && sunatCabecera.getCodigoCabecera()>0){
				listaDetalleSunat = sunatDetalleDao.findByCodigoCabecera(sunatCabecera.getCodigoCabecera());
			}
			comprobante = comprobanteDao.findOne(id);
			listaDetalle = detalleComprobanteDao.listarxComprobante(id);
			leyenda = leyendaDao.getxComprobante(id);
			filtro.setLeyendaSunat(leyenda);
			filtro.setComprobante(comprobante);
			filtro.setListaDetalle(listaDetalle);
			filtro.setSunatCabecera(sunatCabecera);
			filtro.setListaDetalleSunat(listaDetalleSunat);
			filtro.setAppRutaContexto(request.getContextPath());
			filtro.setListaParametro((List<ParametroFacturadorBean>)request.getSession().getAttribute("SessionListParametro"));
			//Datos del Punto de Facturacion
			//domicilio = catalogoDao.getCatalogoxCodigoSunatxTipo(comprobante.getCodigoDomicilio(), Constantes.TIPO_CATALAGO_COD_DOMICILIO_FISCAL);
			//if (domicilio !=null){
			//	filtro.setNombreDomicilioFiscal(domicilio.getNombre());
			//}else{
				filtro.setNombreDomicilioFiscal("-");
			//}
			comprobantePdf.comprobanteReporteEmail(filtro);
			bis = comprobantePdf.comprobanteReporte(filtro);


			//headers.add("Content-Disposition", "inline; filename=Comprobante.pdf");
			headers.add("Content-Disposition", "attachment; filename="+comprobante.getSerie()+"-"+comprobante.getNumero()+".pdf");


		}catch(Exception e){
			e.printStackTrace();
		}finally{
			filtro 				= null;
			listaDetalle 		= null;
			comprobante			= null;

		}
		return ResponseEntity
				.ok()
				.headers(headers)
				.contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cobro/regresar", method = RequestMethod.GET)
	public String regresar(Model model, String path, HttpServletRequest request) {
		TblContrato filtro = null;
		List<TblContrato> lista = null;
		PageWrapper<TblContrato> page = null;
		try{
			log.debug("[regresar] Inicio");
			path = "caja/cobro/cob_listado";
			
			filtro = (TblContrato)request.getSession().getAttribute("sessionFiltroCriterioCobro");
			model.addAttribute("filtro", filtro);
			lista = (List<TblContrato>)request.getSession().getAttribute("sessionListaCobro");
			model.addAttribute("registros",lista);
			page = (PageWrapper<TblContrato>) request.getSession().getAttribute("PageCobro");
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
