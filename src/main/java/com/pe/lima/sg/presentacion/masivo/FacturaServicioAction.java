package com.pe.lima.sg.presentacion.masivo;

import static com.pe.lima.sg.dao.caja.MasivoSunatSpecifications.conAnio;
import static com.pe.lima.sg.dao.caja.MasivoSunatSpecifications.conCodigoEdificio;
import static com.pe.lima.sg.dao.caja.MasivoSunatSpecifications.conEstado;
import static com.pe.lima.sg.dao.caja.MasivoSunatSpecifications.conMes;
import static com.pe.lima.sg.dao.caja.MasivoSunatSpecifications.conTipoMasivo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.pe.lima.sg.api.Interface.IApiOseCSV;
import com.pe.lima.sg.api.bean.CredencialBean;
import com.pe.lima.sg.bean.Ubl.TagUbl;
import com.pe.lima.sg.bean.caja.ComprobanteReintentoBean;
import com.pe.lima.sg.bean.caja.ComprobanteSunatBean;
import com.pe.lima.sg.bean.caja.FacturaBean;
import com.pe.lima.sg.bean.caja.MasivoSunatBean;
import com.pe.lima.sg.bean.caja.MasivoTiendaSunatBean;
import com.pe.lima.sg.bean.caja.UbigeoBean;
import com.pe.lima.sg.dao.caja.IComprobanteOseDAO;
import com.pe.lima.sg.dao.caja.ICxCDocumentoDAO;
import com.pe.lima.sg.dao.caja.IDetalleComprobanteOseDAO;
import com.pe.lima.sg.dao.caja.IDetalleFormaPagoOseDAO;
import com.pe.lima.sg.dao.caja.IMasivoSunatDAO;
import com.pe.lima.sg.dao.caja.IMasivoTiendaSunatDAO;
import com.pe.lima.sg.dao.cliente.IContratoDAO;
import com.pe.lima.sg.dao.mantenimiento.ITiendaDAO;
import com.pe.lima.sg.dao.mantenimiento.ITipoCambioDAO;
import com.pe.lima.sg.entity.caja.TblComprobanteSunat;
import com.pe.lima.sg.entity.caja.TblCxcDocumento;
import com.pe.lima.sg.entity.caja.TblDetalleComprobante;
import com.pe.lima.sg.entity.caja.TblDetalleFormaPago;
import com.pe.lima.sg.entity.caja.TblMasivoSunat;
import com.pe.lima.sg.entity.caja.TblMasivoTiendaSunat;
import com.pe.lima.sg.entity.cliente.TblContrato;
import com.pe.lima.sg.entity.mantenimiento.TblParametro;
import com.pe.lima.sg.entity.mantenimiento.TblTienda;
import com.pe.lima.sg.entity.mantenimiento.TblTipoCambio;
import com.pe.lima.sg.facturador.dao.ISerieSFS12DAO;
import com.pe.lima.sg.facturador.entity.TblSerie;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.PageWrapper;
import com.pe.lima.sg.presentacion.util.PageableSG;
import com.pe.lima.sg.presentacion.util.UtilSGT;
import com.pe.lima.sg.presentacion.util.UtilUBL;
import com.pe.lima.sg.rs.ose.FacturaOseDao;

import lombok.extern.slf4j.Slf4j;


/**
 * Clase Bean que se encarga de la administracion de la caja chica
 *
 * 			
 */
@Slf4j
@Controller
//@PreAuthorize("hasAuthority('CRUD')")
public class FacturaServicioAction {
	
	
	@Autowired
	private IMasivoSunatDAO masivoSunatDao;
	@Autowired
	private IMasivoTiendaSunatDAO masivoTiendaSunatDao;
	@Autowired
	private ITiendaDAO tiendaDao;
	@Autowired
	private IContratoDAO contratoDao;
	@Autowired
	private ICxCDocumentoDAO cxcDocumentoDao;
	@Autowired
	private IComprobanteOseDAO comprobanteOseDao;
	@Autowired
	private FacturaOseDao facturaOseDao;
	@Autowired
	private ISerieSFS12DAO serieDao;
	@Autowired
	private ITipoCambioDAO tipoCambioDao;
	@Autowired
	private IDetalleComprobanteOseDAO detalleComprobanteOseDao;
	@Autowired
	private ICxCDocumentoDAO cxCDocumentoDao;
	@Autowired
	private IApiOseCSV apiOseCSV;
	@Autowired
	private IDetalleFormaPagoOseDAO formaPagoOseDao;
	@Autowired
	private ServletContext context;
	
	private String urlPaginado = "/masivo/facturas/servicio/paginado/"; 
	
	private final Integer MAXIMO_REPETICIONES = 4;
	/**
	 * Se encarga de listar todos los registros de caja chica
	 * 
	 * @param model
	 * @return
	 */
	

	@RequestMapping(value = "/masivo/facturas/servicio", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path, HttpServletRequest request) {
		Filtro filtro = null;
		
		try{
			log.debug("[traerRegistros] Inicio");
			path = "masivo/servicio/fac_listado";
			
			filtro = new Filtro();
			request.getSession().setAttribute("CriterioFiltroMasivoFacturaServicio",filtro);
			
			model.addAttribute("registros",null);
			model.addAttribute("page", null);
			model.addAttribute("filtro", filtro);
			
			request.getSession().setAttribute("ListadoMasivoFactura",null);
			request.getSession().setAttribute("PageMasivoFactura",null);
			
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
	 * Se encarga de buscar la informacion del masivos alquiler segun el filtro
	 * seleccionado
	 * 
	 */
	@RequestMapping(value = "/masivo/facturas/servicio/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, Filtro filtro, String path ,  PageableSG pageable,HttpServletRequest request) {
		path = "masivo/servicio/fac_listado";
		try{
			log.debug("[traerRegistrosFiltrados] Inicio");
			this.listarMasivoFactura(model, filtro, pageable, this.urlPaginado, request);
			model.addAttribute("filtro", filtro);
			log.debug("[traerRegistrosFiltrados] Fin");
		}catch(Exception e){
			log.debug("[traerRegistrosFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			
		}
		log.debug("[traerRegistrosFiltrados] Fin");
		return path;
	}
	/**
	 * Se encarga de direccionar a la pantalla de creacion de Masivos para la factura de alquiler
	 * 
	 */
	@RequestMapping(value = "/masivo/facturas/servicio/nuevo", method = RequestMethod.GET)
	public String nuevoMasivoFactura(Model model,HttpServletRequest request) {
		MasivoSunatBean masivoSunatBean = null;
		try{
			log.debug("[nuevoMasivoFactura] Inicio");
			masivoSunatBean = inicializaDatosParaNuevoRegistro();
			model.addAttribute("entidad", masivoSunatBean);
			request.getSession().setAttribute("MasivoFacturaServicioNuevo", masivoSunatBean);
			request.getSession().setAttribute("SessionMapTiendaExcluida", null);
			log.debug("[nuevoMasivoFactura] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "masivo/servicio/fac_nuevo_empresa";
	}
	/*Mostramos todas las tiendas de la empresa(edificio) seleccionado*/
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "masivo/facturas/servicio/nuevo/tiendas", method = RequestMethod.POST)
	public String nuevoConTiendaMasivoFacturaTienda(Model model, MasivoSunatBean entidad, HttpServletRequest request) {
		Map<Integer, String> mapEdificio = null;
		try{
			log.debug("[nuevoConTiendaMasivoFacturaTienda] Inicio");
			mapEdificio = (Map<Integer, String>) request.getSession().getAttribute("SessionMapEdificacionOperacion");
			Integer keyTienda = entidad.getCodigoEdificio();
			entidad.setNombreEdificio(mapEdificio.get(keyTienda));
			List<TblTienda> listaTiendaActivo = tiendaDao.listarAllActivos(entidad.getCodigoEdificio());
			List<TblContrato> listaContratoActivo = contratoDao.listAllContratoActivosxFecha(new Date());
			List<TblCxcDocumento> listaCxCActivo = cxcDocumentoDao.listarCxCByAnioMes(Constantes.TIPO_PAGO_SERVICIO_CODIGO, entidad.getAnio(), new Integer(entidad.getMes()));
			obtenerListaTiendasAFacturar(listaTiendaActivo,listaContratoActivo,listaCxCActivo,entidad);
			
			model.addAttribute("entidad", entidad);
			model.addAttribute("registros", entidad.getListaTiendaSunat());
			request.getSession().setAttribute("MasivoFacturaServicioNuevo", entidad);
			request.getSession().setAttribute("SessionMapTiendaExcluida", entidad.getMapTiendaExcluidasComboBox());
			log.debug("[nuevoConTiendaMasivoFacturaTienda] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "masivo/servicio/fac_nuevo_tienda";
	}
	

	@RequestMapping(value = "/masivotienda/facturas/servicio/nuevo/agregarExcluida", method = RequestMethod.POST)
	public String agregarNuevoTiendaMasivoFacturaTienda(Model model, MasivoSunatBean entidad, HttpServletRequest request) {
		MasivoSunatBean masivoSunatBean = null;
		try{
			log.debug("[agregarNuevoTiendaMasivoFacturaTienda] Inicio");
			masivoSunatBean = (MasivoSunatBean)request.getSession().getAttribute("MasivoFacturaServicioNuevo");
			if (entidad.getCodigoTienda().compareTo(-1) ==0){
				model.addAttribute("respuesta", "Debe seleccionar una tienda para ser agregada.");
				model.addAttribute("entidad", masivoSunatBean);
				model.addAttribute("registros", masivoSunatBean.getListaTiendaSunat());
			}else {
				Map<Integer,String> mapTiendaExcluidas = masivoSunatBean.getMapTiendaExcluidas();
				String numeroTienda = mapTiendaExcluidas.get(entidad.getCodigoTienda());
				mapTiendaExcluidas.remove(entidad.getCodigoTienda());
				Map<String,Integer> mapTiendaExcluidasCombo = generarNuevoMapTiendaExcluidaCombo(mapTiendaExcluidas);
				masivoSunatBean.setMapTiendaExcluidasComboBox(mapTiendaExcluidasCombo);
				masivoSunatBean = adicionarTiendaEnLista(masivoSunatBean, entidad.getCodigoTienda(), numeroTienda);
				model.addAttribute("entidad", masivoSunatBean);
				model.addAttribute("registros", masivoSunatBean.getListaTiendaSunat());
				request.getSession().setAttribute("MasivoFacturaServicioNuevo", masivoSunatBean);
				request.getSession().setAttribute("SessionMapTiendaExcluida", masivoSunatBean.getMapTiendaExcluidasComboBox());
			}
			
			log.debug("[agregarNuevoTiendaMasivoFacturaTienda] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "masivo/servicio/fac_nuevo_tienda";
	}
	
	@RequestMapping(value = "/masivotienda/facturas/servicio/tienda/eliminar/{id}", method = RequestMethod.GET)
	public String quitarNuevoTiendaMasivoFacturaTienda(@PathVariable Integer id, Model model, HttpServletRequest request) {
		MasivoSunatBean masivoSunatBean = null;
		String path 					= "masivo/servicio/fac_nuevo_tienda";
		try{
			masivoSunatBean = (MasivoSunatBean)request.getSession().getAttribute("MasivoFacturaServicioNuevo");
			MasivoTiendaSunatBean masivoTiendaSunatBean = masivoSunatBean.getListaTiendaSunat().get(id.intValue());
			Integer codigoTienda = masivoTiendaSunatBean.getCodigoTienda();
			String numeroTienda = masivoTiendaSunatBean.getNumeroTienda();
			masivoSunatBean.getListaTiendaSunat().remove(id.intValue());
			masivoSunatBean.getMapTiendaExcluidas().put(codigoTienda,numeroTienda);
			Map<String,Integer> mapTiendaExcluidasCombo = generarNuevoMapTiendaExcluidaCombo(masivoSunatBean.getMapTiendaExcluidas());
			masivoSunatBean.setMapTiendaExcluidasComboBox(mapTiendaExcluidasCombo);
			model.addAttribute("entidad", masivoSunatBean);
			model.addAttribute("registros", masivoSunatBean.getListaTiendaSunat());
			request.getSession().setAttribute("MasivoFacturaServicioNuevo", masivoSunatBean);
			request.getSession().setAttribute("SessionMapTiendaExcluida", masivoSunatBean.getMapTiendaExcluidasComboBox());
		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
	}

	/**
	 * Se encarga de guardar la informacion 
	 * 
	 */
	@RequestMapping(value = "/masivotienda/facturas/servicio/nuevo/tiendas/guardar", method = RequestMethod.POST)
	public String guardarEntidad(Model model, MasivoSunatBean entidad, HttpServletRequest request, String path , PageableSG pageable) {
		path = "masivo/servicio/fac_listado";
		MasivoSunatBean masivoSunatBean = null;
		try{
			log.debug("[guardarEntidad] Inicio" );
			masivoSunatBean = (MasivoSunatBean)request.getSession().getAttribute("MasivoFacturaServicioNuevo");
			if (this.validarNegocio(model, masivoSunatBean, request)){
				log.debug("[guardarEntidad] Pre Guardar..." );
				TblMasivoSunat tblMasivoSunat = setearDatosMasivoSunat(masivoSunatBean,request);
				tblMasivoSunat = masivoSunatDao.save(tblMasivoSunat);
				List<TblMasivoTiendaSunat> listaMasivoTienda = setearDatosMasivoTienda(masivoSunatBean, tblMasivoSunat,request);
				for(TblMasivoTiendaSunat tblMasivoTiendaSunat : listaMasivoTienda) {
					masivoTiendaSunatDao.save(tblMasivoTiendaSunat);
				}
				model.addAttribute("respuesta", "Se registró exitosamente");
				Filtro filtro = (Filtro)request.getSession().getAttribute("CriterioFiltroMasivoFacturaServicio");
				this.traerRegistrosFiltrados(model,filtro, path, pageable, request);
			}else{
				model.addAttribute("entidad", masivoSunatBean);
				model.addAttribute("registros", masivoSunatBean.getListaTiendaSunat());
				
				path = "masivo/servicio/fac_nuevo_tienda";
				model.addAttribute("entidad", entidad);
			}
			
			log.debug("[guardarEntidad] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
		
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/masivo/facturas/servicio/ver/{id}", method = RequestMethod.GET)
	public String verMasivoFacturaTienda(@PathVariable Integer id, Model model, HttpServletRequest request) {
		String path 				= null;
		List<MasivoSunatBean> lista = null;
		try{
			log.debug("[verMasivoFacturaTienda] Inicio");
			path = "masivo/servicio/fac_ver_tienda";
			lista = (List<MasivoSunatBean>)request.getSession().getAttribute("ListadoMasivoFactura");
			MasivoSunatBean masivoSunatBean = lista.get(id.intValue());
			//entidad = masivoSunatDao.findOne(masivoSunatBean.getCodigoMasivo());
			List<TblMasivoTiendaSunat> listaTiendaMasivo = masivoTiendaSunatDao.listarActivosxMasivo(masivoSunatBean.getCodigoMasivo());
			
			obtenerListaTiendaVer(listaTiendaMasivo,masivoSunatBean);
			
			model.addAttribute("entidad", masivoSunatBean);
			model.addAttribute("registros", masivoSunatBean.getListaTiendaSunat());
			request.getSession().setAttribute("MasivoFacturaServicioNuevo", masivoSunatBean);
			request.getSession().setAttribute("SessionMapTiendaExcluida", masivoSunatBean.getMapTiendaExcluidasComboBox());
			
			log.debug("[verMasivoFacturaTienda] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
	}
	private void obtenerListaTiendaVer(List<TblMasivoTiendaSunat> listaTiendaMasivo, MasivoSunatBean masivoSunatBean) {
		
		List<MasivoTiendaSunatBean> listaTienda = new ArrayList<>();
		Map<Integer,String> mapTiendaExcluida = new HashMap<>();
		Map<String,Integer> mapTiendaExcluidaComboBox = new HashMap<>();
		MasivoTiendaSunatBean masivoTiendaSunatBean = new MasivoTiendaSunatBean();
		
		for(TblMasivoTiendaSunat tblMasivoTiendaSunat: listaTiendaMasivo) {
			if (tblMasivoTiendaSunat.getExcluido().equals("N")) {
				masivoTiendaSunatBean = new MasivoTiendaSunatBean();
				masivoTiendaSunatBean.setNombreEdificio(masivoSunatBean.getNombreEdificio());
				masivoTiendaSunatBean.setNumeroTienda(tblMasivoTiendaSunat.getNumeroTienda());
				masivoTiendaSunatBean.setMonto(tblMasivoTiendaSunat.getMonto());
				masivoTiendaSunatBean.setNombrePeriodo(masivoSunatBean.getPeriodo());
				masivoTiendaSunatBean.setCodigoTienda(tblMasivoTiendaSunat.getCodigoTienda());
				listaTienda.add(masivoTiendaSunatBean);
			}else {
				Integer keyTienda = tblMasivoTiendaSunat.getCodigoTienda();
				mapTiendaExcluida.put(keyTienda,tblMasivoTiendaSunat.getNumeroTienda());
			}
		}
		mapTiendaExcluidaComboBox= generarNuevoMapTiendaExcluidaCombo(mapTiendaExcluida);
		
		masivoSunatBean.setMapTiendaExcluidas(mapTiendaExcluida);
		masivoSunatBean.setMapTiendaExcluidasComboBox(mapTiendaExcluidaComboBox);
		masivoSunatBean.setListaTiendaSunat(listaTienda);
	}

	/**
	 * Se encarga de la eliminacion logica del registro
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/masivo/facturas/servicio/eliminar/{id}", method = RequestMethod.GET)
	public String eliminarMasivoFactura(@PathVariable Integer id, HttpServletRequest request, Model model, PageableSG pageable) {
		TblMasivoSunat entidad		= null;
		String path 				= null;
		Filtro filtro				= null;
		List<MasivoSunatBean> lista = null;
		try{
			log.debug("[eliminarMasivoFactura] Inicio");
			path = "masivo/servicio/fac_listado";
			lista = (List<MasivoSunatBean>)request.getSession().getAttribute("ListadoMasivoFactura");
			MasivoSunatBean masivoSunatBean = lista.get(id.intValue());
			entidad = masivoSunatDao.findOne(masivoSunatBean.getCodigoMasivo());
			entidad.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			entidad.setAuditoriaModificacion(request);
			
			masivoSunatDao.save(entidad);
			model.addAttribute("respuesta", "Eliminación exitosa");
			model.addAttribute("filtro", request.getSession().getAttribute("CriterioFiltroMasivoFacturaServicio"));
			filtro = (Filtro)request.getSession().getAttribute("CriterioFiltroMasivoFacturaServicio");
			this.traerRegistrosFiltrados(model, filtro, path, pageable, request);
			log.debug("[eliminarMasivoFactura] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;
			filtro		= null;
		}
		return path;
	}
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/masivo/facturas/servicio/regresarlista", method = RequestMethod.GET)
	public String regresar(Model model, String path, HttpServletRequest request) {
		Filtro filtro = null;
		List<TblMasivoSunat> lista = null;
		PageWrapper<TblMasivoSunat> page = null;
		try{
			log.debug("[regresar] Inicio");
			path = "masivo/servicio/fac_listado";
			
			filtro = (Filtro)request.getSession().getAttribute("CriterioFiltroMasivoFacturaServicio");
			model.addAttribute("filtro", filtro);
			lista = (List<TblMasivoSunat>)request.getSession().getAttribute("ListadoMasivoFactura");
			model.addAttribute("registros",lista);
			page = (PageWrapper<TblMasivoSunat>) request.getSession().getAttribute("PageMasivoFactura");
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
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/masivo/facturas/servicio/procesar/{id}", method = RequestMethod.GET)
	public String procesarMasivoFacturaTienda(@PathVariable Integer id, Model model, HttpServletRequest request, PageableSG pageable) {
		MasivoSunatBean masivoSunatBean = null;
		String path 					= null;
		List<MasivoSunatBean> lista 	= null;
		List<FacturaBean> listaFactura	= null;	
		Filtro filtro 					= new Filtro();
		TblMasivoSunat tblMasivoSunat 	= null;
		TblMasivoTiendaSunat tblMasivoTiendaSunat = null;
		List<ComprobanteReintentoBean> listaComprobanteReintento = new ArrayList<>();
		try{
			log.debug("[procesarMasivoFacturaTienda] Inicio");
			path = "masivo/servicio/fac_listado";
			lista = (List<MasivoSunatBean>)request.getSession().getAttribute("ListadoMasivoFactura");
			masivoSunatBean = lista.get(id.intValue());
			tblMasivoSunat = masivoSunatDao.findOne(masivoSunatBean.getCodigoMasivo());
			if (fechaDelProcesoValido(masivoSunatBean, model) && okTokenParaProceso(request,model)) {
				filtro.setTipo(Constantes.TIPO_COBRO_ALQUILER);
				filtro.setCodigoEdificacion(masivoSunatBean.getCodigoEdificio());
				filtro.setNumero("");
				listaFactura = facturaOseDao.getConsultaAlquilerServicioOse(filtro);
				Integer totalFacturasAGenerar = 0;
				for(FacturaBean entidad: listaFactura) {
					//Graba el comprobante
					tblMasivoTiendaSunat = masivoTiendaSunatDao.obtenerMasivoTiendaxPeriodo(masivoSunatBean.getCodigoMasivo(), entidad.getNumeroTienda());
					log.info("[procesarMasivoFacturaTienda Exito] Tienda:"+tblMasivoTiendaSunat.getNumeroTienda()+" Excluido:"+tblMasivoTiendaSunat.getExcluido());
					if (tblMasivoTiendaSunat.getExcluido().equals("N")) {
						this.obtenerDatosFactura(entidad.getFactura(), request);
						actualizarTipoOperacion(entidad,request);
						totalFacturasAGenerar++;
						boolean resultado = grabarComprobante(entidad,request,model,tblMasivoSunat,tblMasivoTiendaSunat,listaComprobanteReintento);
						if (resultado) {
							log.info("[procesarMasivoFacturaTienda Exito] Tienda:"+tblMasivoTiendaSunat.getNumeroTienda());
						}else {
							log.info("[procesarMasivoFacturaTienda Error] --> Tienda:"+tblMasivoTiendaSunat.getNumeroTienda());
						}
					}else {
						log.info("[procesarMasivoFacturaTienda Exito] Tienda:"+tblMasivoTiendaSunat.getNumeroTienda()+" --> No se procesa ");
					}
				}
				//Reintentos
				
				if (!listaComprobanteReintento.isEmpty() && existeEstado202(listaComprobanteReintento)) {
					Integer totalRepeticion = MAXIMO_REPETICIONES;
					for (int i=0; i<totalRepeticion; i++) {
						List<ComprobanteReintentoBean> listaComprobanteReintentoNuevo = new ArrayList<>();
						for(ComprobanteReintentoBean reintento: listaComprobanteReintento) {
							ComprobanteReintentoBean comprobanteReintento = this.reintentoLlamadasApiOse(reintento.getCredencial(), model, reintento.getTblComprobanteSunat(), request, reintento.getTblMasivoSunat());
							if (comprobanteReintento != null) {
								listaComprobanteReintentoNuevo.add(comprobanteReintento);
								log.info("[procesarMasivoFacturaTienda Error] --> Tienda:"+reintento.getTblComprobanteSunat().getNumeroTienda());
							}else {
								log.info("[procesarMasivoFacturaTienda Exito] Tienda:"+reintento.getTblComprobanteSunat().getNumeroTienda());
							}
						}
						listaComprobanteReintento = listaComprobanteReintentoNuevo;
						if (listaComprobanteReintento.isEmpty() || !existeEstado202(listaComprobanteReintento)) {
							break;
						}
					}
					
				}
				actualizarEstadoMasivo(tblMasivoSunat,totalFacturasAGenerar);
				model.addAttribute("respuesta", "Proceso finalizado.");
				model.addAttribute("filtro", request.getSession().getAttribute("CriterioFiltroMasivoFacturaServicio"));
				filtro = (Filtro)request.getSession().getAttribute("CriterioFiltroMasivoFacturaServicio");
				this.traerRegistrosFiltrados(model, filtro, path, pageable, request);
				
			}else {
				model.addAttribute("registros", request.getSession().getAttribute("ListadoMasivoFactura"));
				model.addAttribute("page", request.getSession().getAttribute("PageMasivoFactura"));
				model.addAttribute("filtro", request.getSession().getAttribute("CriterioFiltroMasivoFacturaServicio"));

			}
			log.debug("[procesarMasivoFacturaTienda] Fin");
		}catch(Exception e){
			log.debug("[procesarMasivoFacturaTienda] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			masivoSunatBean = null;
		}
		
		return path;
	}
	
	/*Validamos si existe alguna operacion con estado 202 para poder realizar una re-proceso*/
	private boolean existeEstado202(List<ComprobanteReintentoBean> listaComprobanteReintento) {
		boolean resultado = false;
		for(ComprobanteReintentoBean c:listaComprobanteReintento) {
			if (c.getTblComprobanteSunat().getEstadoOperacion().contains("202")) {
				resultado = true;
				break;
			}
		}
		return resultado;
	}

	private boolean okTokenParaProceso(HttpServletRequest request, Model model) {
		boolean resultado = false;
		CredencialBean credencial 		= null;
		credencial = obtenerCredenciales(request);
		try {
			String token = apiOseCSV.obtenerToken(credencial); 
			if (token != null && token.length()>0) { //Cualquier otro codigo es error
				resultado = true;
			}else {
				model.addAttribute("respuesta", "No se puede procesar, Error al obtener el Token, problemas con el servidor OSE.");
			}
		}catch(Exception e) {
			model.addAttribute("respuesta", "No se puede procesar, Error al obtener el Token, problemas con el servidor OSE.["+e.getMessage()+"]");
		}
		return resultado;
	}

	private void actualizarEstadoMasivo(TblMasivoSunat tblMasivoSunat, Integer totalFacturasAGenerar) {
		Integer xmlGenerado = tblMasivoSunat.getXmlGenerado();
		Integer cdrGenerado = tblMasivoSunat.getCdrGenerado();
		Integer pdfGenerado = tblMasivoSunat.getPdfGenerado();
		if (xmlGenerado == cdrGenerado && cdrGenerado == pdfGenerado && pdfGenerado == totalFacturasAGenerar) {
			tblMasivoSunat.setEstadoMasivo(Constantes.MASIVO_ESTADO_FINALIZADO);
		}else {
			tblMasivoSunat.setEstadoMasivo(Constantes.MASIVO_ESTADO_EN_PARCIAL);
		}
		masivoSunatDao.save(tblMasivoSunat);
	}

	private boolean grabarComprobante(FacturaBean entidad,HttpServletRequest request,Model model,TblMasivoSunat tblMasivoSunat,TblMasivoTiendaSunat tblMasivoTiendaSunat, List<ComprobanteReintentoBean> listaComprobanteReintento) {
		boolean resultado = false;
		try {
			CredencialBean credencial 		= null;
			log.debug("[grabarComprobante] Pre Guardar..." );
			entidad.getFactura().setAuditoriaCreacion(request);
			/*Obtener numero de la serie*/
			obtenerNumeroSerie(entidad.getFactura());
			TblComprobanteSunat comprobante = comprobanteOseDao.save(entidad.getFactura());
			entidad.getFacturaDetalle().setAuditoriaCreacion(request);
			entidad.getFacturaDetalle().setTblComprobante(comprobante);
			detalleComprobanteOseDao.save(entidad.getFacturaDetalle());
			if (entidad.getFactura().getFormaPago().equals(Constantes.FORMA_PAGO_CREDITO)) {
				log.error("[grabarComprobante] Error en la forma de Pago:"+entidad.getFactura().getFormaPago());
			}
			/*Guardar numero de comprobante*/
			actualizarCxCDocumentoConNumeroComprobante(entidad.getCodigoCxCDocumento(), comprobante.getCodigoComprobante(), comprobante.getSerie() , comprobante.getNumero(),request);
			actualizarMasivoTiendaConNumeroComprobante(tblMasivoTiendaSunat,comprobante.getCodigoComprobante(),request);
			/*Incrementar el numero de la serie*/
			incrementarNumeroSerie();
			/*Generamos el archivo CSV para la factura*/
			credencial = obtenerCredenciales(request);
			obtenerNombreArchivos(credencial,entidad);
			/*Obtenemos el Ubigeo*/
			UbigeoBean ubigeo = apiOseCSV.obtenerUbigeo(entidad.getFactura().getClienteNumero(), "708f63d6550fa89310e64c5a39591034e1ed36721cf30b7dfb2466222a82522c");
			entidad.setUbigeo(ubigeo);
			generarArchivoCSV(entidad, credencial);
			/*Llamamos a los apis*/
			resultado = llamadasApiOse(credencial,model,comprobante,request,tblMasivoSunat);
			ComprobanteReintentoBean comprobanteReintentoBean = new ComprobanteReintentoBean();
			comprobanteReintentoBean.setCredencial(credencial);
			comprobanteReintentoBean.setTblComprobanteSunat(comprobante);
			comprobanteReintentoBean.setTblMasivoSunat(tblMasivoSunat);
			listaComprobanteReintento.add(comprobanteReintentoBean);
			
		}catch(Exception e) {
			e.printStackTrace();
			resultado = false;
		}

		return resultado;
	}
	private void actualizarMasivoTiendaConNumeroComprobante(TblMasivoTiendaSunat tblMasivoTiendaSunat,
			Integer codigoComprobante,HttpServletRequest request) {
		tblMasivoTiendaSunat.setCodigoComprobante(codigoComprobante);
		tblMasivoTiendaSunat.setAuditoriaModificacion(request);
		masivoTiendaSunatDao.save(tblMasivoTiendaSunat);
	}

	private boolean llamadasApiOse(CredencialBean credencial,Model model,TblComprobanteSunat comprobante, HttpServletRequest request,TblMasivoSunat tblMasivoSunat) {
		boolean resultado = false;
		try {
			String token = apiOseCSV.obtenerToken(credencial); 
			credencial.setAccessToken(token);
			Integer status = apiOseCSV.obtenerTicket(credencial);
			
			if (status.compareTo(200)==0) {
				
				okEnvioCsv(tblMasivoSunat);
				comprobante = actualizarTicketEnComprobanteSunat(credencial,comprobante,request);
				status = apiOseCSV.obtenerCDRDocumento(credencial);
				if (status.compareTo(200)==0) {
					okGeneracionCDR(tblMasivoSunat);
					comprobante = actualizarCdrEnComprobanteSunat(credencial,comprobante,request);
					status = apiOseCSV.obtenerXMLDocumento(credencial);
					if (status.compareTo(200)==0) {
						okGeneracionXML(tblMasivoSunat);
						comprobante = actualizarXmlEnComprobanteSunat(credencial,comprobante,request);
						status = apiOseCSV.obtenerPDFDocumento(credencial);
						if (status.compareTo(200)==0) {
							okGeneracionPDF(tblMasivoSunat);
							comprobante = actualizarPdfEnComprobanteSunat(credencial,comprobante,request);
							resultado = true;
						}else {
							errorPDF(tblMasivoSunat);
							actualizarStatusPdfEnComprobanteSunat(credencial, comprobante, request);
							log.info("[llamadasApiOse] Con error en PDF:"+status);
							model.addAttribute("respuesta", "Con error en PDF:"+status);
						}
					}else {
						errorXML(tblMasivoSunat);
						actualizarStatusXmlEnComprobanteSunat(credencial, comprobante, request);
						errorCDR(tblMasivoSunat);
						log.info("[llamadasApiOse] Con error en XML:"+status);
						model.addAttribute("respuesta", "Con error en XML:"+status);
					}
				}else {
					actualizarStatusCdrEnComprobanteSunat(credencial, comprobante, request);
					log.info("[llamadasApiOse] Con error en CDR:"+status);
					model.addAttribute("respuesta", "Con error en CDR:"+status);
				}
			}else {
				
				errorEnvioCsv(tblMasivoSunat);
				actualizarStatusTicketEnComprobanteSunat(credencial, comprobante, request);
				log.info("[llamadasApiOse] Con error en Ticket:"+status);
				model.addAttribute("respuesta", "Con error en Ticket:"+status);
			}
		}catch(Exception e) {
			model.addAttribute("respuesta", "Error obtener el token:"+e.getMessage());
			//registramos el estado del token para luego volver a reintentar
			actualizarStatusTockenEnComprobanteSunat(credencial, comprobante, request);
			errorEnvioCsv(tblMasivoSunat);
			e.printStackTrace();
		}
		masivoSunatDao.save(tblMasivoSunat);
		return resultado;
	}
	
	private ComprobanteReintentoBean reintentoLlamadasApiOse(CredencialBean credencial,Model model,TblComprobanteSunat comprobante, HttpServletRequest request,TblMasivoSunat tblMasivoSunat) {
		ComprobanteReintentoBean comprobanteReintentoBean = null;
		boolean resultado = false;
		try {
			
			if (this.okTokenParaProceso(request, model)) {
				String token = apiOseCSV.obtenerToken(credencial);
				credencial.setAccessToken(token);
				String etapaProceso = obtenerEtapaProceso(comprobante);
				if (etapaProceso.equals("TICKET")) {
					resultado = procesarDesdeTicket(credencial,model,comprobante, request,tblMasivoSunat);
				}
				if (etapaProceso.equals("CDR")) {
					resultado = procesarDesdeCdr(credencial,model,comprobante, request,tblMasivoSunat);
				}
				if (etapaProceso.equals("XML")) {
					resultado = procesarDesdeXml(credencial,model,comprobante, request,tblMasivoSunat);
				}
				if (etapaProceso.equals("PDF")) {
					resultado = procesarDesdePdf(credencial,model,comprobante, request,tblMasivoSunat);
				}
			}
			if (!resultado) {
				comprobanteReintentoBean = new ComprobanteReintentoBean();
				comprobanteReintentoBean.setCredencial(credencial);
				comprobanteReintentoBean.setTblComprobanteSunat(comprobante);
				comprobanteReintentoBean.setTblMasivoSunat(tblMasivoSunat);
			}
			 
		}catch(Exception e) {
			model.addAttribute("respuesta", "Error obtener el token:"+e.getMessage());
			//registramos el estado del token para luego volver a reintentar
			actualizarStatusTockenEnComprobanteSunat(credencial, comprobante, request);
			errorEnvioCsv(tblMasivoSunat);
			e.printStackTrace();
		}
		masivoSunatDao.save(tblMasivoSunat);
		return comprobanteReintentoBean;
	}
	/*Procesamos desde CDR, XML y PDF*/
	private boolean procesarDesdeCdr(CredencialBean credencial, Model model, TblComprobanteSunat comprobante,
			HttpServletRequest request, TblMasivoSunat tblMasivoSunat) {
		boolean resultado = false;
		Integer status = apiOseCSV.obtenerCDRDocumento(credencial);
		if (status.compareTo(200)==0) {
			okGeneracionCDR(tblMasivoSunat);
			comprobante = actualizarCdrEnComprobanteSunat(credencial,comprobante,request);
			resultado = procesarDesdeXml(credencial, model, comprobante, request, tblMasivoSunat);
		}else {
			actualizarStatusCdrEnComprobanteSunat(credencial, comprobante, request);
			log.info("[llamadasApiOse] Con error en CDR:"+status);
			model.addAttribute("respuesta", "Con error en CDR:"+status);
		}
		return resultado;
	}
	/*Procesamos desde el XML y PDF*/
	private boolean procesarDesdeXml(CredencialBean credencial, Model model, TblComprobanteSunat comprobante,
			HttpServletRequest request, TblMasivoSunat tblMasivoSunat) {
		boolean resultado = false;
		Integer status = apiOseCSV.obtenerXMLDocumento(credencial);
		if (status.compareTo(200)==0) {
			okGeneracionXML(tblMasivoSunat);
			comprobante = actualizarXmlEnComprobanteSunat(credencial,comprobante,request);
			resultado = procesarDesdePdf(credencial, model, comprobante, request, tblMasivoSunat);
		}else {
			errorXML(tblMasivoSunat);
			actualizarStatusXmlEnComprobanteSunat(credencial, comprobante, request);
			errorCDR(tblMasivoSunat);
			log.info("[procesarDesdeXml] Con error en XML:"+status);
			model.addAttribute("respuesta", "Con error en XML:"+status);
		}
		return resultado;
	}

	/*Procesa desde el PDF*/
	private boolean procesarDesdePdf(CredencialBean credencial, Model model, TblComprobanteSunat comprobante,
			HttpServletRequest request, TblMasivoSunat tblMasivoSunat) {
		boolean resultado = false;
		Integer status = apiOseCSV.obtenerPDFDocumento(credencial);
		if (status.compareTo(200)==0) {
			okGeneracionPDF(tblMasivoSunat);
			comprobante = actualizarPdfEnComprobanteSunat(credencial,comprobante,request);
			resultado = true;
		}else {
			errorPDF(tblMasivoSunat);
			actualizarStatusPdfEnComprobanteSunat(credencial, comprobante, request);
			log.info("[llamadasApiOse] Con error en PDF:"+status);
			model.addAttribute("respuesta", "Con error en PDF:"+status);
		}
		return resultado;
	}

	/*Se procesa desde el Ticket*/
	private boolean procesarDesdeTicket(CredencialBean credencial, Model model, TblComprobanteSunat comprobante,
			HttpServletRequest request, TblMasivoSunat tblMasivoSunat) {
		boolean resultado = false;
		Integer status = apiOseCSV.obtenerTicket(credencial);
		if (status.compareTo(200)==0) {
			okEnvioCsv(tblMasivoSunat);
			comprobante = actualizarTicketEnComprobanteSunat(credencial,comprobante,request);
			resultado = procesarDesdeCdr(credencial, model, comprobante, request, tblMasivoSunat);
		}else {
			errorEnvioCsv(tblMasivoSunat);
			actualizarStatusTicketEnComprobanteSunat(credencial, comprobante, request);
			log.info("[llamadasApiOse] Con error en Ticket:"+status);
			model.addAttribute("respuesta", "Con error en Ticket:"+status);
		}
		return resultado;
	}

	private String obtenerEtapaProceso(TblComprobanteSunat comprobante) {
		String etapa = "";
		try {
			String cadena = comprobante.getEstadoOperacion().substring(0,comprobante.getEstadoOperacion().indexOf(":"));
			etapa = cadena.trim();
		}catch(Exception e) {
			log.error("[obtenerEtapaProceso] Comprobante ["+comprobante.getCodigoComprobante()+"] Estado:"+comprobante.getEstadoOperacion()+" Error:"+e.getMessage());
		}
		return etapa;
	}

	private void errorPDF(TblMasivoSunat tblMasivoSunat) {
		tblMasivoSunat.setPdfError(tblMasivoSunat.getPdfError()+1);
		tblMasivoSunat.setPdfTotal(tblMasivoSunat.getPdfTotal()+1);
		tblMasivoSunat.setPdfIntento(tblMasivoSunat.getPdfIntento()+1);
		
	}

	private void okGeneracionPDF(TblMasivoSunat tblMasivoSunat) {
		tblMasivoSunat.setPdfGenerado(tblMasivoSunat.getPdfGenerado()+1);
		tblMasivoSunat.setPdfTotal(tblMasivoSunat.getPdfTotal()+1);
		tblMasivoSunat.setPdfIntento(tblMasivoSunat.getPdfIntento()+1);
		
	}

	private void errorXML(TblMasivoSunat tblMasivoSunat) {
		tblMasivoSunat.setXmlError(tblMasivoSunat.getXmlError()+1);
		tblMasivoSunat.setXmlTotal(tblMasivoSunat.getXmlTotal()+1);
		tblMasivoSunat.setXmlIntento(tblMasivoSunat.getXmlIntento()+1);
	}

	private void okGeneracionXML(TblMasivoSunat tblMasivoSunat) {
		tblMasivoSunat.setXmlGenerado(tblMasivoSunat.getXmlGenerado()+1);
		tblMasivoSunat.setXmlTotal(tblMasivoSunat.getXmlTotal()+1);
		tblMasivoSunat.setXmlIntento(tblMasivoSunat.getXmlIntento()+1);
	}

	private void errorCDR(TblMasivoSunat tblMasivoSunat) {
		tblMasivoSunat.setCdrError(tblMasivoSunat.getCdrError()+1);
		tblMasivoSunat.setCdrTotal(tblMasivoSunat.getCdrTotal()+1);
		tblMasivoSunat.setCdrIntento(tblMasivoSunat.getCdrIntento()+1);
		
		
	}

	private void okGeneracionCDR(TblMasivoSunat tblMasivoSunat) {
		tblMasivoSunat.setCdrGenerado(tblMasivoSunat.getCdrGenerado()+1);
		tblMasivoSunat.setCdrTotal(tblMasivoSunat.getCdrTotal()+1);
		tblMasivoSunat.setCdrIntento(tblMasivoSunat.getCdrIntento()+1);
		
	}

	private void errorEnvioCsv(TblMasivoSunat tblMasivoSunat) {
		tblMasivoSunat.setCsvError(tblMasivoSunat.getCsvError()+1);
		tblMasivoSunat.setCsvTotal(tblMasivoSunat.getCsvTotal()+1);
		tblMasivoSunat.setCsvIntento(tblMasivoSunat.getCsvIntento()+1);
	}

	private void okEnvioCsv(TblMasivoSunat tblMasivoSunat) {
		tblMasivoSunat.setCsvEnviado(tblMasivoSunat.getCsvEnviado()+1);
		tblMasivoSunat.setCsvTotal(tblMasivoSunat.getCsvTotal()+1);
		tblMasivoSunat.setCsvIntento(tblMasivoSunat.getCsvIntento()+1);
	}
	
	private TblComprobanteSunat actualizarStatusTockenEnComprobanteSunat(CredencialBean credencial, TblComprobanteSunat comprobante, HttpServletRequest request) {
		comprobante.setEstadoOperacion("TOKEN: 202");
		comprobante.setAuditoriaModificacion(request);
		TblComprobanteSunat comprobanteUpdate = comprobanteOseDao.save(comprobante);
		return comprobanteUpdate;
	}

	private TblComprobanteSunat actualizarTicketEnComprobanteSunat(CredencialBean credencial, TblComprobanteSunat comprobante, HttpServletRequest request) {
		comprobante.setNumeroTicket(credencial.getTicket());
		return actualizarStatusTicketEnComprobanteSunat(credencial, comprobante, request);
	}
	private TblComprobanteSunat actualizarStatusTicketEnComprobanteSunat(CredencialBean credencial, TblComprobanteSunat comprobante, HttpServletRequest request) {
		comprobante.setNombreCsv(credencial.getCsvFileName());
		comprobante.setEstadoOperacion("TICKET: "+credencial.getStatus());
		comprobante.setAuditoriaModificacion(request);
		TblComprobanteSunat comprobanteUpdate = comprobanteOseDao.save(comprobante);
		return comprobanteUpdate;
	}
	private TblComprobanteSunat actualizarCdrEnComprobanteSunat(CredencialBean credencial, TblComprobanteSunat comprobante, HttpServletRequest request) {
		comprobante.setNombreCdr(credencial.getCdrFileName());
		return actualizarStatusCdrEnComprobanteSunat(credencial, comprobante, request);
	}
	private TblComprobanteSunat actualizarStatusCdrEnComprobanteSunat(CredencialBean credencial, TblComprobanteSunat comprobante, HttpServletRequest request) {
		comprobante.setEstadoOperacion("CDR: "+credencial.getStatus());
		comprobante.setAuditoriaModificacion(request);
		TblComprobanteSunat comprobanteUpdate = comprobanteOseDao.save(comprobante);
		return comprobanteUpdate;
	}
	private TblComprobanteSunat actualizarXmlEnComprobanteSunat(CredencialBean credencial, TblComprobanteSunat comprobante, HttpServletRequest request) {
		comprobante.setNombreXml(credencial.getXmlFileName());
		return actualizarStatusXmlEnComprobanteSunat(credencial, comprobante, request);
	}
	private TblComprobanteSunat actualizarStatusXmlEnComprobanteSunat(CredencialBean credencial, TblComprobanteSunat comprobante, HttpServletRequest request) {
		comprobante.setEstadoOperacion("XML: "+credencial.getStatus());
		comprobante.setAuditoriaModificacion(request);
		TblComprobanteSunat comprobanteUpdate = comprobanteOseDao.save(comprobante);
		return comprobanteUpdate;
	}
	private TblComprobanteSunat actualizarPdfEnComprobanteSunat(CredencialBean credencial, TblComprobanteSunat comprobante, HttpServletRequest request) {
		comprobante.setNombrePdf(credencial.getPdfFileName());
		return actualizarStatusPdfEnComprobanteSunat(credencial, comprobante, request);
	}
	private TblComprobanteSunat actualizarStatusPdfEnComprobanteSunat(CredencialBean credencial, TblComprobanteSunat comprobante, HttpServletRequest request) {
		comprobante.setEstadoOperacion("PDF: "+credencial.getStatus());
		comprobante.setAuditoriaModificacion(request);
		TblComprobanteSunat comprobanteUpdate = comprobanteOseDao.save(comprobante);
		return comprobanteUpdate;
	}
	private void generarArchivoCSV(FacturaBean entidad, CredencialBean crendencial) {
		List<TagUbl> listaHeader = null;
		List<TagUbl> listaDetail = null;
		listaHeader = UtilUBL.nodoUblHeader(entidad);
		listaDetail = UtilUBL.nodoUblDetail(entidad);
		generarArchivo(listaHeader, listaDetail, entidad , crendencial);
	}
	
	private boolean generarArchivo(List<TagUbl> listaHeader,List<TagUbl> listaDetail, FacturaBean entidad, CredencialBean crendencial){
		boolean resultado = false;
		String cadena = null;
		BufferedWriter bufferedWriter = null;
		String FILENAME = crendencial.getPath() + crendencial.getCsvFileName();
		log.debug("[generarArchivoCabecera] filename: "+FILENAME);
		try{
			bufferedWriter = new BufferedWriter(new FileWriter(FILENAME, true));
			//Nombres del Header
			cadena = obtenerNodoOValorDeLista(listaHeader,"NODO");
			bufferedWriter.write(cadena);
			bufferedWriter.newLine();
			//Datos del Header
			cadena = obtenerNodoOValorDeLista(listaHeader,"VALOR");
			bufferedWriter.write(cadena);
			bufferedWriter.newLine();
			//Nombres del Detail
			cadena = obtenerNodoOValorDeLista(listaDetail,"NODO");
			bufferedWriter.write(cadena);
			bufferedWriter.newLine();
			//Datos del Detail
			cadena = obtenerNodoOValorDeLista(listaDetail,"VALOR");
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
	private String obtenerNodoOValorDeLista(List<TagUbl> lista, String tipoDato) {
		String cadena = null;
		for(TagUbl tag:lista) {
			if (cadena == null) {
				cadena = obtenerDato(tag,tipoDato);
			}else {
				cadena = cadena + Constantes.SUNAT_COMA + obtenerDato(tag,tipoDato);;
			}
		}
		return cadena;
	}
	private String obtenerDato(TagUbl tag,String tipoDato) {
		if (tipoDato.equals("NODO")) {
			return tag.getNodo();
		}else {
			return tag.getValor();
		}
		
	}
	private void obtenerNombreArchivos(CredencialBean crendencial, FacturaBean entidad) {
		crendencial.setCsvFileName(UtilSGT.getNombreFacturaCVS(entidad));
		crendencial.setCdrFileName(UtilSGT.getNombreFacturaCDR(entidad));
		crendencial.setXmlFileName(UtilSGT.getNombreFacturaXML(entidad));
		crendencial.setPdfFileName(UtilSGT.getNombreFacturaPDF(entidad));
	}
	@SuppressWarnings("unchecked")
	private CredencialBean obtenerCredenciales(HttpServletRequest request) {
		CredencialBean credencialBean = new CredencialBean();
		TblParametro parametro = null;
		Map<String, TblParametro> mapParametro = (Map<String, TblParametro>)request.getSession().getAttribute("SessionMapParametros");
		parametro = mapParametro.get(Constantes.RUTA_FILE_OSE);
		credencialBean.setPath(parametro.getDato());
		parametro = mapParametro.get(Constantes.URL_EFACT_TOKEN);
		credencialBean.setResourceToken(parametro.getDato());
		parametro = mapParametro.get(Constantes.URL_EFACT_DOCUMENTO);
		credencialBean.setResourceDocumento(parametro.getDato());
		parametro = mapParametro.get(Constantes.URL_EFACT_CDR);
		credencialBean.setResourceCdr(parametro.getDato());
		parametro = mapParametro.get(Constantes.URL_EFACT_XML);
		credencialBean.setResourceXml(parametro.getDato());
		parametro = mapParametro.get(Constantes.URL_EFACT_PDF);
		credencialBean.setResourcePdf(parametro.getDato());
		
		parametro = mapParametro.get(Constantes.EFACT_CLIENT_SECRET);
		credencialBean.setClientSecret(parametro.getDato());
		parametro = mapParametro.get(Constantes.EFACT_GRANT_TYPE);
		credencialBean.setGrantType(parametro.getDato());
		parametro = mapParametro.get(Constantes.EFACT_USER_NAME);
		credencialBean.setUserName(parametro.getDato());
		parametro = mapParametro.get(Constantes.EFACT_PASSWORD);
		credencialBean.setPassword(parametro.getDato());
		
		return credencialBean;
	}
	private void incrementarNumeroSerie() {
		TblSerie serie = null;
		serie = serieDao.buscarOneByTipoComprobante(Constantes.SUNAT_CODIGO_COMPROBANTE_FACTURA);
		Integer numero = serie.getSecuencialSerie();
		numero++;
		String numeroFormateado = String.format("%08d", numero);
		serie.setNumeroComprobante(numeroFormateado);
		serie.setSecuencialSerie(numero);
		serieDao.save(serie);
		
	}
	private void actualizarCxCDocumentoConNumeroComprobante(Integer codigoCxCDocumento, Integer codigoComprobante, String serie, String numeroComprobante, HttpServletRequest request) {
		TblCxcDocumento documento = cxCDocumentoDao.findOne(codigoCxCDocumento);
		documento.setCodigoComprobante(codigoComprobante);
		documento.setSerieComprobante(serie);
		documento.setNumeroComprobante(numeroComprobante);
		documento.setAuditoriaModificacion(request);
		cxCDocumentoDao.save(documento);
	}
	@SuppressWarnings("unchecked")
	private void actualizarTipoOperacion(FacturaBean entidad, HttpServletRequest request) {
		TblParametro parametro = null;
		TblTipoCambio tipoCambio = null;
		BigDecimal montoDetraccion = null;
		tipoCambio = tipoCambioDao.obtenerUltimoTipoCambio();
		
		Map<String, TblParametro> mapParametro = (Map<String, TblParametro>)request.getSession().getAttribute("SessionMapParametros");
		
		
		if (entidad.getFactura().getTipoPago().equals(Constantes.TIPO_COBRO_ALQUILER)) {
			parametro = mapParametro.get(Constantes.PARAMETRO_MONTO_DETRACCION_ALQUILER);
			montoDetraccion = parametro.getValor();
			if (montoEsParaDetracccion(tipoCambio,entidad,montoDetraccion)) {
				parametro = mapParametro.get(Constantes.PARAMETRO_PORCENTAJE_DETRACCION_ALQUILER);	
				asignarDatosDetraccion(parametro, entidad);
			}else {
				entidad.getFactura().setTipoOperacion(Constantes.TIPO_OPERACION_VENTA_INTERNA_CODIGO);
			}
		}else {
			parametro = mapParametro.get(Constantes.PARAMETRO_MONTO_DETRACCION_SERVICIO);
			montoDetraccion = parametro.getValor();
			if (entidad.getFactura().getTotal().compareTo(montoDetraccion)>=0) {
				parametro = mapParametro.get(Constantes.PARAMETRO_PORCENTAJE_DETRACCION_SERVICIO);
				asignarDatosDetraccion(parametro, entidad);
			}else {
				entidad.getFactura().setTipoOperacion(Constantes.TIPO_OPERACION_VENTA_INTERNA_CODIGO);
			}
		}
		
	}
	private void asignarDatosDetraccion(TblParametro parametro, FacturaBean entidad) {
		BigDecimal porcentajeDetraccion = null;
		BigDecimal montoCalculadoDetraccion = null;
		TblTipoCambio tipoCambio = tipoCambioDao.obtenerUltimoTipoCambio();
		
		
		porcentajeDetraccion = parametro.getValor();
		montoCalculadoDetraccion = entidad.getFactura().getTotal().multiply(porcentajeDetraccion).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
		entidad.getFactura().setDetracionTotal(entidad.getFactura().getTotal().subtract(montoCalculadoDetraccion));
		//Sobre este monto se realiza la forma de pago
		entidad.getFormaPago().setMonto(entidad.getFactura().getDetracionTotal());
		if (entidad.getFactura().getMoneda().equals("USD")) {
			log.info("[asignarDatosDetraccion] detraccion[ROUND_UP]: "+montoCalculadoDetraccion.multiply(tipoCambio.getValor()).setScale(0, RoundingMode.HALF_UP));
			montoCalculadoDetraccion = montoCalculadoDetraccion.multiply(tipoCambio.getValor()).setScale(0, RoundingMode.HALF_UP);
			entidad.getFactura().setDetracionMonto(montoCalculadoDetraccion);
			//entidad.getFactura().setDetracionTotal((entidad.getFactura().getTotal().multiply(tipoCambio.getValor()).subtract(montoCalculadoDetraccion)).setScale(0, BigDecimal.ROUND_HALF_EVEN));
		}else {
			entidad.getFactura().setDetracionMonto(montoCalculadoDetraccion);
		}
		entidad.setTipoCambio(tipoCambio.getValor().toString() + " : "+UtilSGT.formatFechaSGT(tipoCambio.getFecha()));
		entidad.getFactura().setDetracionPorcentaje(porcentajeDetraccion);
		entidad.getFactura().setTipoOperacion(Constantes.TIPO_OPERACION_DETRACCION_CODIGO);
	}
	private boolean montoEsParaDetracccion(TblTipoCambio tipoCambio, FacturaBean entidad, BigDecimal montoDetraccion) {
		BigDecimal montoSoles = null;
		montoSoles = tipoCambio.getValor().multiply(entidad.getFactura().getTotal()).setScale(2, RoundingMode.HALF_UP);
		if (montoSoles.compareTo(montoDetraccion)>=0) {
			return true;
		}else {
			return false;
		}
	}
	@SuppressWarnings("unchecked")
	private void obtenerDatosFactura(TblComprobanteSunat facturaSunat, HttpServletRequest request) {
		//Obtener Serie - Numero
		TblParametro parametro = null;
		Map<String, TblParametro> mapParametro = (Map<String, TblParametro>)request.getSession().getAttribute("SessionMapParametros");
		parametro = mapParametro.get(Constantes.PARAMETRO_DOMICILIO_FISCAL);
		facturaSunat.setDomicilioFiscal(parametro.getDato());
		obtenerNumeroSerie(facturaSunat);
		facturaSunat.setFechaEmision(UtilSGT.getFecha("yyyy-MM-dd"));
		facturaSunat.setHoraEmision(UtilSGT.getHora());
		facturaSunat.setFechaVencimiento(UtilSGT.getFecha("yyyy-MM-dd"));
	}
	private void obtenerNumeroSerie(TblComprobanteSunat facturaSunat) {
		TblSerie serie = null;
		serie = serieDao.buscarOneByTipoComprobante(Constantes.SUNAT_CODIGO_COMPROBANTE_FACTURA);
		facturaSunat.setSerie(serie.getPrefijoSerie());
		facturaSunat.setNumero(serie.getNumeroComprobante());
		
	}
	private boolean fechaDelProcesoValido(MasivoSunatBean masivoSunatBean, Model model) {
		Integer anioCurso		= UtilSGT.getAnioDate(new Date());
		Integer mesCurso		= UtilSGT.getMesDate(new Date());
		if ( (anioCurso.compareTo(masivoSunatBean.getAnio())==0) && (mesCurso.compareTo(new Integer(masivoSunatBean.getMes()))==0)) {
			return true;
		}else {
			model.addAttribute("respuesta", "No se puede procesar, no corresponde al periodo en curso.");
			return false;
			
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/masivo/facturas/servicio/proceso/ver/{id}", method = RequestMethod.GET)
	public String verProcesoMasivoFacturaTienda(@PathVariable Integer id, Model model, HttpServletRequest request) {
		MasivoSunatBean masivoSunatBean = null;
		String path 					= null;
		List<MasivoSunatBean> lista 	= null;
		try{
			log.debug("[verProcesoMasivoFacturaTienda] Inicio");
			path = "masivo/servicio/fac_proceso";
			lista = (List<MasivoSunatBean>)request.getSession().getAttribute("ListadoMasivoFactura");
			masivoSunatBean = lista.get(id.intValue());
			List<TblComprobanteSunat> listaTblComprobante = comprobanteOseDao.listarComprobantexEdificioxPeriodoAlquiler(masivoSunatBean.getCodigoEdificio(), masivoSunatBean.getPeriodo());
			List<ComprobanteSunatBean> listaComprobanteBean = this.procesarListaComprobante(listaTblComprobante, request);
			masivoSunatBean.setTotalErrorEnData(obtenerErrorEnData(listaComprobanteBean));
			masivoSunatBean.setTotalSinErrorEnData(masivoSunatBean.getCsvTotal()-masivoSunatBean.getTotalErrorEnData());
			masivoSunatBean.setListaComprobanteBean(listaComprobanteBean);
			model.addAttribute("registros",listaComprobanteBean);
			model.addAttribute("entidad",masivoSunatBean);
			request.getSession().setAttribute("MasivoFacturaServicioProcesoComprobante",masivoSunatBean);
			//apiOseCSV.obtenerUbigeo("20386431427", "708f63d6550fa89310e64c5a39591034e1ed36721cf30b7dfb2466222a82522c");
			log.debug("[verProcesoMasivoFacturaTienda] Fin");
		}catch(Exception e){
			log.debug("[verProcesoMasivoFacturaTienda] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			masivoSunatBean = null;
		}
		
		return path;
	}
	/*Contamos los errores de data*/
	private Integer obtenerErrorEnData(List<ComprobanteSunatBean> listaComprobanteBean) {
		Integer totalError = 0;
		if (listaComprobanteBean !=null && !listaComprobanteBean.isEmpty()) {
			for(ComprobanteSunatBean comprobante: listaComprobanteBean) {
				if (comprobante.getEstadoOperacion().equals("ERROR")) {
					totalError++;
				}
			}
		}
		return totalError;
	}

	private List<ComprobanteSunatBean> procesarListaComprobante(List<TblComprobanteSunat> content,HttpServletRequest request) {
		List<ComprobanteSunatBean> lista = null;
		ComprobanteSunatBean bean = null;
		if (content != null && !content.isEmpty()) {
			lista = new ArrayList<>();
			for (TblComprobanteSunat comprobante: content) {
				bean = new ComprobanteSunatBean();
				bean.setSerie(comprobante.getSerie());
				bean.setNumero(comprobante.getNumero());
				bean.setNumeroTienda(comprobante.getNumeroTienda());
				bean.setFechaVencimiento(comprobante.getFechaVencimiento());
				bean.setMoneda(comprobante.getMoneda());
				bean.setTotal(comprobante.getTotal());
				bean.setTotalGravados(comprobante.getTotalGravados());
				bean.setTotalIgv(comprobante.getTotalIgv());
				bean.setEstadoOperacion(renombrarEstadoOperacion(comprobante.getEstadoOperacion()));
				bean.setCodigoComprobante(comprobante.getCodigoComprobante());
				bean.setTipoPago(comprobante.getTipoPago());
				bean.setFechaEmision(comprobante.getFechaEmision());
				bean.setNumeroTienda(comprobante.getNumeroTienda());
				bean.setNumeroTicket(comprobante.getNumeroTicket());
				bean.setNombreCsv(comprobante.getNombreCsv());
				bean.setNombreCdr(comprobante.getNombreCdr());
				bean.setNombreXml(comprobante.getNombreXml());
				bean.setNombrePdf(comprobante.getNombrePdf());
				lista.add(bean);
			}	
		}
		
		return lista;
	}
	@RequestMapping(value = "/masivotienda/facturas/servicio/regresarempresa", method = RequestMethod.GET)
	public String regresarEmpresa(Model model, String path, HttpServletRequest request) {
		MasivoSunatBean masivoSunatBean = null;
		try{
			log.debug("[regresarEmpresa] Inicio");
			path = "masivo/servicio/fac_nuevo_empresa";
			masivoSunatBean = (MasivoSunatBean)request.getSession().getAttribute("MasivoFacturaServicioNuevo");
			model.addAttribute("entidad", masivoSunatBean);
			
			
			log.debug("[regresarEmpresa] Fin");
		}catch(Exception e){
			log.debug("[regresarEmpresa] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			masivoSunatBean = null;
		}
		
		return path;
	}

	/*
	 * Paginado
	 */
	@RequestMapping(value = "/masivo/facturas/servicio/paginado/{page}/{size}/{operacion}", method = RequestMethod.GET)
	public String paginarEntidad(@PathVariable Integer page, @PathVariable Integer size, @PathVariable String operacion, Model model,  PageableSG pageable, HttpServletRequest request) {
		Filtro filtro = null;
		String path = null;
		try{
			//log.debug("[traerRegistros] Inicio");
			path = "masivo/servicio/fac_listado";
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
			filtro = (Filtro)request.getSession().getAttribute("CriterioFiltroMasivoFacturaServicio");
			this.traerRegistrosFiltrados(model, filtro, path, pageable, request);
			//this.traerRegistrosFiltrados(model, filtro, pageable, this.urlPaginado, request);
			model.addAttribute("filtro", filtro);
			
		}catch(Exception e){
			//log.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		return path;
	}
	/*** Listado de Registro de Gasto ***/
	private void listarMasivoFactura(Model model, Filtro entidad,  PageableSG pageable, String url, HttpServletRequest request){

		Sort sort = new Sort(new Sort.Order(Direction.DESC, "codigoMasivo"));
		try{
			Specification<TblMasivoSunat> filtro = Specifications.where(conCodigoEdificio(entidad.getCodigoEdificacion()))
					.and(conAnio(entidad.getAnio()))
					.and(conMes(entidad.getMesFiltro()))
					.and(conTipoMasivo(Constantes.MASIVO_TIPO_SERVICIO))
					.and(conEstado(Constantes.ESTADO_REGISTRO_ACTIVO));
			pageable.setSort(sort);
			Page<TblMasivoSunat> entidadPage = masivoSunatDao.findAll(filtro, pageable);
			PageWrapper<TblMasivoSunat> page = new PageWrapper<TblMasivoSunat>(entidadPage, url, pageable);
			List<MasivoSunatBean> lista = this.procesarListaMasivoServicio(page.getContent(), request);
			model.addAttribute("registros", lista);
			model.addAttribute("page", page);
			

			request.getSession().setAttribute("CriterioFiltroMasivoFacturaServicio", entidad);
			request.getSession().setAttribute("ListadoMasivoFactura", lista);
			request.getSession().setAttribute("PageMasivoFactura", page);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	private List<MasivoSunatBean> procesarListaMasivoServicio(List<TblMasivoSunat> listaMasivoFactura,	HttpServletRequest request) {
		List<MasivoSunatBean> lista = new ArrayList<>();
		MasivoSunatBean masivoSunatBean = null;
		Map<Integer, String> mapEdificio = null;
		if (listaMasivoFactura !=null){
			mapEdificio = (Map<Integer, String>) request.getSession().getAttribute("SessionMapEdificacionOperacion");
			for(TblMasivoSunat tblMasivoSunat : listaMasivoFactura){
				masivoSunatBean = new MasivoSunatBean();
				masivoSunatBean.setAnio(tblMasivoSunat.getAnio());
				masivoSunatBean.setMes(tblMasivoSunat.getMes());
				masivoSunatBean.setCodigoEdificio(tblMasivoSunat.getCodigoEdificio());
				masivoSunatBean.setCodigoMasivo(tblMasivoSunat.getCodigoMasivo());
				masivoSunatBean.setPeriodo(tblMasivoSunat.getPeriodo());
				masivoSunatBean.setNombreEdificio(mapEdificio.get(tblMasivoSunat.getCodigoEdificio()));
				masivoSunatBean.setTotalExcluido(tblMasivoSunat.getTotalExcluido());
				masivoSunatBean.setEstadoMasivo(tblMasivoSunat.getEstadoMasivo());
				masivoSunatBean.setTotalProcesada(tblMasivoSunat.getTotalProcesada());
				masivoSunatBean.setFlagAdicionar("N");
				masivoSunatBean.setFlagEliminar("N");
				masivoSunatBean.setFlagProcesa("N");
				if (masivoSunatBean.getEstadoMasivo().equals(Constantes.MASIVO_ESTADO_REGISTRADO)) {
					masivoSunatBean.setFlagAdicionar("S");
					masivoSunatBean.setFlagEliminar("S");
					masivoSunatBean.setFlagProcesa("S");
				}
				masivoSunatBean.setCsvEnviado(tblMasivoSunat.getCsvEnviado());
				masivoSunatBean.setCsvError(tblMasivoSunat.getCsvError());
				masivoSunatBean.setCsvIntento(tblMasivoSunat.getCsvIntento());
				masivoSunatBean.setCsvTotal(tblMasivoSunat.getCsvTotal());
				masivoSunatBean.setXmlError(tblMasivoSunat.getXmlError());
				masivoSunatBean.setXmlGenerado(tblMasivoSunat.getXmlGenerado());
				masivoSunatBean.setXmlIntento(tblMasivoSunat.getXmlIntento());
				masivoSunatBean.setXmlTotal(tblMasivoSunat.getXmlTotal());
				masivoSunatBean.setCdrError(tblMasivoSunat.getCdrError());
				masivoSunatBean.setCdrGenerado(tblMasivoSunat.getCdrGenerado());
				masivoSunatBean.setCdrIntento(tblMasivoSunat.getCdrIntento());
				masivoSunatBean.setCdrTotal(tblMasivoSunat.getCdrTotal());
				masivoSunatBean.setPdfError(tblMasivoSunat.getPdfError());
				masivoSunatBean.setPdfGenerado(tblMasivoSunat.getPdfGenerado());
				masivoSunatBean.setPdfIntento(tblMasivoSunat.getPdfIntento());
				masivoSunatBean.setPdfTotal(tblMasivoSunat.getPdfTotal());
				lista.add(masivoSunatBean);
			}
		}
		return lista;
	}
	
	
	/*Asignamos Empresa y periodo por defecto*/
	private MasivoSunatBean inicializaDatosParaNuevoRegistro() {
		MasivoSunatBean masivoSunatBean = new MasivoSunatBean();
		//Empresa
		masivoSunatBean.setCodigoEdificio(Constantes.CODIGO_INMUEBLE_LA_REYNA);
		//Periodo
		String strMes = UtilSGT.getMesDateFormateado(new Date()); //01 al 12
		Integer intAnio = UtilSGT.getAnioDate(new Date());
		String nombreMes = UtilSGT.getMesPersonalizado(new Integer(strMes));
		masivoSunatBean.setAnio(intAnio);
		masivoSunatBean.setMes(strMes);
		masivoSunatBean.setPeriodo(intAnio+"-"+nombreMes);
		return masivoSunatBean;
	}
	
	
	
	private MasivoSunatBean adicionarTiendaEnLista(MasivoSunatBean masivoSunatBean, Integer codigoTienda, String numeroTienda) {
		MasivoTiendaSunatBean tiendaSunatBean = new MasivoTiendaSunatBean();
		Integer codigoContrato = obtenerCodigoContratoDeTienda(codigoTienda);
		TblCxcDocumento documento = obtenerDocumentoDeTienda(codigoContrato, masivoSunatBean.getAnio(), new Integer(masivoSunatBean.getMes()));
		tiendaSunatBean.setCodigoContrato(codigoContrato);
		tiendaSunatBean.setCodigoCxcDocumento(documento.getCodigoCxcDoc());
		tiendaSunatBean.setMonto(documento.getMontoGenerado());
		tiendaSunatBean.setExcluido("N");
		tiendaSunatBean.setNombreEdificio(masivoSunatBean.getNombreEdificio());
		tiendaSunatBean.setNombrePeriodo(masivoSunatBean.getPeriodo());
		tiendaSunatBean.setNumeroTienda(numeroTienda);
		tiendaSunatBean.setCodigoTienda(codigoTienda);
		masivoSunatBean.getListaTiendaSunat().add(tiendaSunatBean);
		
		return masivoSunatBean;
	}

	private TblCxcDocumento obtenerDocumentoDeTienda(Integer codigoContrato, Integer intAnio, Integer intMes) {
		TblCxcDocumento documento = null;
		if (codigoContrato.compareTo(0)==0) {
			documento = new TblCxcDocumento();
			documento.setCodigoCxcDoc(0);
			documento.setMontoGenerado(new BigDecimal("0"));
		}else {
			documento = cxcDocumentoDao.findByAnioMesCodigoReferencia(Constantes.TIPO_PAGO_ALQUILER_CODIGO, codigoContrato, intAnio, intMes);
			if (documento ==null) {
				documento = new TblCxcDocumento();
				documento.setCodigoCxcDoc(0);
				documento.setMontoGenerado(new BigDecimal("0"));
			}
		}
		return documento;
	}

	private Integer obtenerCodigoContratoDeTienda(Integer codigoTienda) {
		Integer codigoContrato = null;
		TblContrato contrato = contratoDao.findByNumeroTiendaParaFacturar(codigoTienda);
		if (contrato == null) {
			codigoContrato = 0;
		}else {
			codigoContrato = contrato.getCodigoContrato();
		}
		return codigoContrato;
	}

	private Map<String, Integer> generarNuevoMapTiendaExcluidaCombo(Map<Integer, String> mapTiendaExcluidas) {
		 Map<String, Integer> mapTiendaExcluidasCombo = new LinkedHashMap<>();
		for (Map.Entry<Integer,String> entry : mapTiendaExcluidas.entrySet()) {
			mapTiendaExcluidasCombo.put(entry.getValue(), entry.getKey());
			//System.out.println("Original:"+entry.getValue() + " ==> " + entry.getKey());
	    }
		TreeMap<String, Integer> sorted = new TreeMap<>(mapTiendaExcluidasCombo);
        Set<Entry<String, Integer>> mappings = sorted.entrySet();
        
        //System.out.println("HashMap after sorting by keys in ascending order ");
        mapTiendaExcluidasCombo = new LinkedHashMap<>();
        for(Entry<String, Integer> mapping : mappings){
        	
            //System.out.println(mapping.getKey() + " ==> " + mapping.getValue());
            mapTiendaExcluidasCombo.put(mapping.getKey(),mapping.getValue());
        }
		return mapTiendaExcluidasCombo;
	}

	private void obtenerListaTiendasAFacturar(List<TblTienda> listaTiendaActivo, List<TblContrato> listaContratoActivo,
			List<TblCxcDocumento> listaCxCActivo, MasivoSunatBean entidad) {
		Map<Integer,TblContrato> mapContratoxTienda = obtenerMapContratoxTienda(listaContratoActivo);
		Map<Integer,TblContrato> mapContrato = obtenerMapContrato(listaContratoActivo);
		Map<Integer,TblCxcDocumento> mapDocumento = obtenerMapCxCDocumentoxTienda(listaCxCActivo,mapContrato);
		Map<Integer,String> mapTiendaExcluida = new HashMap<>();
		Map<String,Integer> mapTiendaExcluidaComboBox = new HashMap<>();
		MasivoTiendaSunatBean tiendaSunatBean = null;
		entidad.setListaTiendaSunat(new ArrayList<>());
		for(TblTienda tienda: listaTiendaActivo) {
			Integer keyTienda = tienda.getCodigoTienda();
			TblContrato contrato = mapContratoxTienda.get(keyTienda);
			TblCxcDocumento documento = mapDocumento.get(keyTienda);
			if (contrato !=null && documento != null) {
				tiendaSunatBean = new MasivoTiendaSunatBean();
				tiendaSunatBean.setCodigoContrato(contrato.getCodigoContrato());
				tiendaSunatBean.setCodigoCxcDocumento(documento.getCodigoCxcDoc());
				tiendaSunatBean.setMonto(documento.getMontoGenerado());
				tiendaSunatBean.setExcluido("N");
				tiendaSunatBean.setNombreEdificio(entidad.getNombreEdificio());
				tiendaSunatBean.setNombrePeriodo(entidad.getPeriodo());
				tiendaSunatBean.setNumeroTienda(tienda.getNumero());
				tiendaSunatBean.setCodigoTienda(tienda.getCodigoTienda());
				entidad.getListaTiendaSunat().add(tiendaSunatBean);
			}else {
				mapTiendaExcluidaComboBox.put(tienda.getNumero(),keyTienda);
				mapTiendaExcluida.put(keyTienda,tienda.getNumero());
			}
		}
		mapTiendaExcluidaComboBox= generarNuevoMapTiendaExcluidaCombo(mapTiendaExcluida);
		entidad.setMapTiendaExcluidasComboBox(mapTiendaExcluidaComboBox);
		entidad.setMapTiendaExcluidas(mapTiendaExcluida);
	}

	private Map<Integer, TblContrato> obtenerMapContrato(List<TblContrato> listaContratoActivo) {
		Map<Integer,TblContrato> mapContrato = new HashMap<>();
		if (listaContratoActivo!=null && !listaContratoActivo.isEmpty()) {
			for(TblContrato contrato:listaContratoActivo) {
				mapContrato.put(new Integer(contrato.getCodigoContrato()), contrato);
			}
		}
		return mapContrato;
	}

	private Map<Integer, TblCxcDocumento> obtenerMapCxCDocumentoxTienda(List<TblCxcDocumento> listaCxCActivo,Map<Integer,TblContrato> mapContrato) {
		Map<Integer,TblCxcDocumento> mapDocumento = new HashMap<>();
		if (listaCxCActivo!=null && !listaCxCActivo.isEmpty()) {
			for(TblCxcDocumento documento:listaCxCActivo) {
				TblContrato contrato = mapContrato.get(documento.getCodigoContrato());
				if (contrato != null) {
					mapDocumento.put(new Integer(contrato.getTblTienda().getCodigoTienda()), documento);
				}
			}
		}
		return mapDocumento;
	}

	private Map<Integer, TblContrato> obtenerMapContratoxTienda(List<TblContrato> listaContratoActivo) {
		Map<Integer,TblContrato> mapContrato = new HashMap<>();
		if (listaContratoActivo!=null && !listaContratoActivo.isEmpty()) {
			for(TblContrato contrato:listaContratoActivo) {
				mapContrato.put(new Integer(contrato.getTblTienda().getCodigoTienda()), contrato);
			}
		}
		return mapContrato;
	}

	/** Validamos logica del negocio**/
	public boolean validarNegocio(Model model,MasivoSunatBean entidad, HttpServletRequest request){
		boolean exitoso = true;
		try{
			
			if (entidad.getNombreEdificio() == null && entidad.getNombreEdificio().trim().length()<= 0){
				model.addAttribute("respuesta", "No se encontró el edificio seleccionado");
				return false;
			}
			if (entidad.getListaTiendaSunat() == null || entidad.getListaTiendaSunat().isEmpty()){
				model.addAttribute("respuesta", "Debe agregar tiendas a la lista para generar las facturas");
				return false;
			}
			Integer totalRegistros = masivoSunatDao.existeMasivoxEmpresaxPeriodo(entidad.getPeriodo(), entidad.getCodigoEdificio());
			if (totalRegistros > 0){
				model.addAttribute("respuesta", "Se encontró registros para el periodo y edificio seleccionado. No se puede registrar. Elimine el antiguo para continuar.");
				return false;
			}
			
		}catch(Exception e){
			exitoso = false;
		}
		return exitoso;
	}
	
	private List<TblMasivoTiendaSunat> setearDatosMasivoTienda(MasivoSunatBean entidad, TblMasivoSunat tblMasivoSunat,
			HttpServletRequest request) {
		List<TblMasivoTiendaSunat> listaMasivoTienda = new ArrayList<>();
		TblMasivoTiendaSunat tblMasivoTiendaSunat = null;
		for(MasivoTiendaSunatBean masivoTiendaSunatBean: entidad.getListaTiendaSunat()) {
			tblMasivoTiendaSunat = new TblMasivoTiendaSunat();
			tblMasivoTiendaSunat.setNumeroTienda(masivoTiendaSunatBean.getNumeroTienda());
			tblMasivoTiendaSunat.setCodigoContrato(masivoTiendaSunatBean.getCodigoContrato());
			tblMasivoTiendaSunat.setCodigoCxcDocumento(masivoTiendaSunatBean.getCodigoCxcDocumento());
			tblMasivoTiendaSunat.setExcluido(masivoTiendaSunatBean.getExcluido());
			tblMasivoTiendaSunat.setCodigoTienda(masivoTiendaSunatBean.getCodigoTienda());
			tblMasivoTiendaSunat.setAuditoriaCreacion(request);
			tblMasivoTiendaSunat.setTblMasivoSunat(tblMasivoSunat);
			tblMasivoTiendaSunat.setMonto(masivoTiendaSunatBean.getMonto());
			listaMasivoTienda.add(tblMasivoTiendaSunat);
		}
		if (entidad.getMapTiendaExcluidas().size()>0) {
			for (Integer key : entidad.getMapTiendaExcluidas().keySet()) {
				String numeroTienda = entidad.getMapTiendaExcluidas().get(key);
				tblMasivoTiendaSunat = new TblMasivoTiendaSunat();
				tblMasivoTiendaSunat.setNumeroTienda(numeroTienda);
				tblMasivoTiendaSunat.setCodigoContrato(0);
				tblMasivoTiendaSunat.setCodigoCxcDocumento(0);
				tblMasivoTiendaSunat.setExcluido("S");
				tblMasivoTiendaSunat.setCodigoTienda(key);
				tblMasivoTiendaSunat.setAuditoriaCreacion(request);
				tblMasivoTiendaSunat.setTblMasivoSunat(tblMasivoSunat);
				tblMasivoTiendaSunat.setMonto(new BigDecimal("0"));
				listaMasivoTienda.add(tblMasivoTiendaSunat);
		    }
		}
		return listaMasivoTienda;
	}

	/*Seteamos los datos a registrar en la tabla*/
	private TblMasivoSunat setearDatosMasivoSunat(MasivoSunatBean entidad, HttpServletRequest request) {
		TblMasivoSunat tblMasivoSunat = new TblMasivoSunat();
		tblMasivoSunat.setAnio(entidad.getAnio());
		tblMasivoSunat.setMes(entidad.getMes());
		tblMasivoSunat.setCodigoEdificio(entidad.getCodigoEdificio());
		tblMasivoSunat.setPeriodo(entidad.getPeriodo());
		tblMasivoSunat.setTotalProcesada(entidad.getListaTiendaSunat().size());
		tblMasivoSunat.setTotalExcluido(entidad.getMapTiendaExcluidas().size());
		tblMasivoSunat.setEstadoMasivo(Constantes.MASIVO_ESTADO_REGISTRADO);
		tblMasivoSunat.setCsvError(0);
		tblMasivoSunat.setCsvEnviado(0);
		tblMasivoSunat.setCsvIntento(0);
		tblMasivoSunat.setCsvTotal(0);
		tblMasivoSunat.setXmlError(0);
		tblMasivoSunat.setXmlGenerado(0);
		tblMasivoSunat.setXmlIntento(0);
		tblMasivoSunat.setXmlTotal(0);
		tblMasivoSunat.setCdrError(0);
		tblMasivoSunat.setCdrGenerado(0);
		tblMasivoSunat.setCdrIntento(0);
		tblMasivoSunat.setCdrTotal(0);
		tblMasivoSunat.setPdfError(0);
		tblMasivoSunat.setPdfGenerado(0);
		tblMasivoSunat.setPdfIntento(0);
		tblMasivoSunat.setPdfTotal(0);
		tblMasivoSunat.setTipoMasivo(Constantes.MASIVO_TIPO_SERVICIO);
		tblMasivoSunat.setAuditoriaCreacion(request);
		
		return tblMasivoSunat;
	}
	private String renombrarEstadoOperacion(String estadoOperacion) {
		if (estadoOperacion.equals("PDF: 200")) {
			return "VALIDADO";
		}
		if (estadoOperacion.equals("CDR: 202")) {
			return "CDR PENDIENTE";
		}
		if (estadoOperacion.equals("CDR: 200")) {
			return "CDR RECIBIDO";
		}
		if (estadoOperacion.equals("XML: 202")) {
			return "XML PENDIENTE";
		}
		if (estadoOperacion.equals("XML: 200")) {
			return "XML RECIBIDO";
		}
		if (estadoOperacion.equals("PDF: 202")) {
			return "PDF PENDIENTE";
		}
		
		return "ERROR";
	}
	
	@RequestMapping(value = "/masivo/facturas/servicio/comprobante/ver/{id}", method = RequestMethod.GET)
	public String verFactura(@PathVariable Integer id,Model model, HttpServletRequest request) {
		ComprobanteSunatBean entidad	= null;
		List<ComprobanteSunatBean> lista= null;	
		FacturaBean facturaBean = new FacturaBean();
		TblComprobanteSunat factura = new TblComprobanteSunat();
		TblDetalleComprobante facturaDetalle = new TblDetalleComprobante();
		List<TblDetalleFormaPago> listaFormaPago;
		MasivoSunatBean masivoSunatBean = null;
		try{
			log.debug("[verFactura] Inicio");
			masivoSunatBean = (MasivoSunatBean)request.getSession().getAttribute("MasivoFacturaServicioProcesoComprobante");
			lista = masivoSunatBean.getListaComprobanteBean();
			entidad = lista.get(id);
			factura = comprobanteOseDao.findOne(entidad.getCodigoComprobante());
			facturaBean.setFactura(factura);
			facturaDetalle = detalleComprobanteOseDao.getDetallexComprobante(entidad.getCodigoComprobante());
			facturaBean.setFacturaDetalle(facturaDetalle);
			listaFormaPago = formaPagoOseDao.listarAllxComprobante(entidad.getCodigoComprobante());
			facturaBean.setListaFormaPago(listaFormaPago);
			model.addAttribute("entidad", facturaBean);
			model.addAttribute("registrospago", listaFormaPago);
			log.debug("[verFactura] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "masivo/servicio/fac_ver";
	}
	
	@RequestMapping(value = "/masivotienda/facturas/servicio/regresarverfactura", method = RequestMethod.GET)
	public String regresarVerProceso(Model model, String path, HttpServletRequest request) {
		MasivoSunatBean masivoSunatBean = null;
		try{
			log.debug("[regresarVerProceso] Inicio");
			path = "masivo/servicio/fac_proceso";
			masivoSunatBean = (MasivoSunatBean)request.getSession().getAttribute("MasivoFacturaServicioProcesoComprobante");
			model.addAttribute("entidad", masivoSunatBean);
			model.addAttribute("registros",masivoSunatBean.getListaComprobanteBean());
			log.debug("[regresarVerProceso] Fin");
		}catch(Exception e){
			log.debug("[regresarVerProceso] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			masivoSunatBean = null;
		}
		
		return path;
	}
	@RequestMapping(value = "/masivotienda/facturas/servicio/pdf/descargar/{id}", method = RequestMethod.GET)
	public void obtenerPdfDocumentoDescargar(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
		ComprobanteSunatBean entidad	= null;
		List<ComprobanteSunatBean> lista= null;		
		CredencialBean credencial 		= null;
		FacturaBean factura				= new FacturaBean();
		MasivoSunatBean masivoSunatBean = null;
		try{
			log.debug("[obtenerPdfDocumentoDescargar] Inicio");
			masivoSunatBean = (MasivoSunatBean)request.getSession().getAttribute("MasivoFacturaServicioProcesoComprobante");
			lista = masivoSunatBean.getListaComprobanteBean();
			entidad = lista.get(id);
			asignarDatosFacturaBeanDeComprobanteSunatBean(factura,entidad);
			credencial = obtenerCredenciales(request);
			obtenerNombreArchivos(credencial,factura);
			String path = credencial.getPath() + credencial.getPdfFileName();
			fileDownload(path, response, credencial.getPdfFileName());
			log.debug("[obtenerPdfDocumentoDescargar] Fin");
		}catch(Exception e){
			e.printStackTrace();
			log.debug("[obtenerPdfDocumentoDescargar] Error: "+e.getMessage());
		}finally{
			entidad 	= null;			
		}
	}
	@RequestMapping(value = "/masivotienda/facturas/servicio/xml/descargar/{id}", method = RequestMethod.GET)
	public void obtenerXmlDocumentoDescargar(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
		ComprobanteSunatBean entidad	= null;
		List<ComprobanteSunatBean> lista= null;		
		CredencialBean credencial 		= null;
		FacturaBean factura				= new FacturaBean();
		MasivoSunatBean masivoSunatBean = null;
		try{
			log.debug("[obtenerXmlDocumentoDescargar] Inicio");
			masivoSunatBean = (MasivoSunatBean)request.getSession().getAttribute("MasivoFacturaServicioProcesoComprobante");
			lista = masivoSunatBean.getListaComprobanteBean();
			entidad = lista.get(id);
			asignarDatosFacturaBeanDeComprobanteSunatBean(factura,entidad);
			credencial = obtenerCredenciales(request);
			obtenerNombreArchivos(credencial,factura);
			String path = credencial.getPath() + credencial.getXmlFileName();
			fileDownload(path, response, credencial.getXmlFileName());
			log.debug("[obtenerXmlDocumentoDescargar] Fin");
		}catch(Exception e){
			e.printStackTrace();
			log.debug("[obtenerXmlDocumentoDescargar] Error: "+e.getMessage());
		}finally{
			entidad 	= null;			
		}
	}
	@RequestMapping(value = "/masivotienda/facturas/servicio/cdr/descargar/{id}", method = RequestMethod.GET)
	public void obtenerCdrDocumentoDescargar(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
		ComprobanteSunatBean entidad	= null;
		List<ComprobanteSunatBean> lista= null;		
		CredencialBean credencial 		= null;
		FacturaBean factura				= new FacturaBean();
		MasivoSunatBean masivoSunatBean = null;
		try{
			log.debug("[obtenerCdrDocumentoDescargar] Inicio");
			masivoSunatBean = (MasivoSunatBean)request.getSession().getAttribute("MasivoFacturaServicioProcesoComprobante");
			lista = masivoSunatBean.getListaComprobanteBean();
			entidad = lista.get(id);
			asignarDatosFacturaBeanDeComprobanteSunatBean(factura,entidad);
			credencial = obtenerCredenciales(request);
			obtenerNombreArchivos(credencial,factura);
			String path = credencial.getPath() + credencial.getCdrFileName();
			fileDownload(path, response, credencial.getCdrFileName());
			log.debug("[obtenerCdrDocumentoDescargar] Fin");
		}catch(Exception e){
			e.printStackTrace();
			log.debug("[obtenerCdrDocumentoDescargar] Error: "+e.getMessage());
		}finally{
			entidad 	= null;			
		}
	}
	@RequestMapping(value = "/masivotienda/facturas/servicio/ticket/{id}", method = RequestMethod.GET)
	public String obtenerTicketDocumento(@PathVariable Integer id, HttpServletRequest request, Model model) {
		ComprobanteSunatBean entidad	= null;
		List<ComprobanteSunatBean> lista= null;		
		CredencialBean credencial 		= null;
		FacturaBean factura				= new FacturaBean();
		MasivoSunatBean masivoSunatBean = null;
		try{
			log.debug("[obtenerTicketDocumento] Inicio");
			masivoSunatBean = (MasivoSunatBean)request.getSession().getAttribute("MasivoFacturaServicioProcesoComprobante");
			lista = masivoSunatBean.getListaComprobanteBean();
			entidad = lista.get(id);
			asignarDatosFacturaBeanDeComprobanteSunatBean(factura,entidad);
			credencial = obtenerCredenciales(request);
			obtenerNombreArchivos(credencial,factura);
			String token = apiOseCSV.obtenerToken(credencial);
			credencial.setAccessToken(token);
			Integer status = apiOseCSV.obtenerTicket(credencial);
			log.debug("[obtenerTicketDocumento] status: "+status);
			if (status.compareTo(200)==0) {
				entidad.setNumeroTicket(credencial.getTicket());
				TblComprobanteSunat comprobante = comprobanteOseDao.findOne(entidad.getCodigoComprobante());
				comprobante = actualizarTicketEnComprobanteSunat(credencial,comprobante,request);
			}else {
				TblComprobanteSunat comprobante = comprobanteOseDao.findOne(entidad.getCodigoComprobante());
				actualizarStatusTicketEnComprobanteSunat(credencial,comprobante,request);
				log.info("[llamadasApiOse] Con error en Ticket:"+status);
				model.addAttribute("respuesta", "Con error en Ticket:"+status);
			}
			model.addAttribute("registros", lista);
			model.addAttribute("entidad", masivoSunatBean);
			log.debug("[obtenerTicketDocumento] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;			
		}
		return "masivo/servicio/fac_proceso";
	}
	private void asignarDatosFacturaBeanDeComprobanteSunatBean(FacturaBean factura, ComprobanteSunatBean entidad) {
		factura.setFactura(new TblComprobanteSunat());
		factura.getFactura().setSerie(entidad.getSerie());
		factura.getFactura().setNumero(entidad.getNumero());
	}
	private void fileDownload(String fullPath, HttpServletResponse response, String filename){
		File file = new File(fullPath);
		final int BUFFER_ZISE = 4096;
		if (file.exists()){
			try{
				FileInputStream inputStream = new FileInputStream(file);
				String mimeType = context.getMimeType(fullPath);
				response.setContentType(mimeType);
				response.setHeader("content-disposition", "attachment; filename="+filename);
				OutputStream outputStream = response.getOutputStream();
				byte[] buffer = new byte[BUFFER_ZISE];
				int bytesRead = -1;
				while((bytesRead = inputStream.read(buffer))!=-1){
					outputStream.write(buffer, 0, bytesRead);
				}
				inputStream.close();
				outputStream.close();
				//file.delete();
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}
}
