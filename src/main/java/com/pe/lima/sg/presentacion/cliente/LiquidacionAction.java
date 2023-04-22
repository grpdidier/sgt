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

/**
 * Clase Bean que se encarga de la administracion de los liquidacion
 *
 * 			
 */
@Controller
public class LiquidacionAction extends BaseOperacionPresentacion<TblContrato> {

	private static final Logger logger = LogManager.getLogger(LiquidacionAction.class);
	
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
	
	
	
	private String urlPaginado = "/liquidacion/paginado/"; 
	private String urlPaginadoTienda = "/liquidacion/tienda/paginado/"; 
	private String urlPaginadoCliente = "/liquidacion/cliente/paginado/";
	
	@SuppressWarnings("rawtypes")
	@Override
	public BaseOperacionDAO getDao() {
		return contratoDao;
	}

	/**
	 * Se encarga de listar todos las liquidaciones
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/liquidacion", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path,  PageableSG pageable,HttpServletRequest request) {
		TblContrato filtro = null;
		try{
			logger.debug("[traerRegistros] Inicio");
			path = "cliente/liquidacion/liq_listado";
			filtro = new TblContrato();
			filtro.setTblPersona(new TblPersona());
			filtro.setTblTienda(new TblTienda());
			filtro.getTblTienda().setTblEdificio(new TblEdificio());
			model.addAttribute("filtro", filtro);
			request.getSession().setAttribute("sessionFiltroCriterio", filtro);

			request.getSession().setAttribute("sessionListaContrato", null);
			request.getSession().setAttribute("PageContrato", null);
			request.getSession().setAttribute("PageableSGContrato", pageable);
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
	@RequestMapping(value = "/liquidacion/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, TblContrato filtro, String path ,  PageableSG pageable, HttpServletRequest request) {
		//Map<String, Object> campos = null;
		path = "cliente/liquidacion/liq_listado";
		String strSeleccion = "";
		try{
			logger.debug("[traerRegistrosFiltrados] Inicio");
			this.listarContratos(model, filtro, pageable, this.urlPaginado, request);
			model.addAttribute("filtro", filtro);
			model.addAttribute("strSeleccion", strSeleccion);
			request.getSession().setAttribute("sessionFiltroCriterio", filtro);
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
			logger.debug("[listarContratos] entidades:"+entidades);
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

	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/liquidacion/regresar", method = RequestMethod.GET)
	public String regresar(Model model, String path, HttpServletRequest request) {
		TblContrato filtro = null;
		List<TblContrato> lista = null;
		PageWrapper<TblContrato> page = null;
		try{
			logger.debug("[regresar] Inicio");
			path = "cliente/liquidacion/liq_listado";
			
			filtro = (TblContrato)request.getSession().getAttribute("sessionFiltroCriterio");
			model.addAttribute("filtro", filtro);
			lista = (List<TblContrato>)request.getSession().getAttribute("sessionListaContrato");
			model.addAttribute("registros",lista);
			page = (PageWrapper<TblContrato>) request.getSession().getAttribute("PageContrato");
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

	@Override
	public TblContrato getNuevaEntidad() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
