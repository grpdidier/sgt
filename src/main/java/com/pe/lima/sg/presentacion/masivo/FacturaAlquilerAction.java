package com.pe.lima.sg.presentacion.masivo;

import static com.pe.lima.sg.dao.caja.MasivoSunatSpecifications.conAnio;
import static com.pe.lima.sg.dao.caja.MasivoSunatSpecifications.conCodigoEdificio;
import static com.pe.lima.sg.dao.caja.MasivoSunatSpecifications.conEstado;
import static com.pe.lima.sg.dao.caja.MasivoSunatSpecifications.conMes;
import static com.pe.lima.sg.dao.caja.MasivoSunatSpecifications.conTipoMasivo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

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

import com.pe.lima.sg.bean.caja.MasivoSunatBean;
import com.pe.lima.sg.bean.caja.MasivoTiendaSunatBean;
import com.pe.lima.sg.dao.caja.ICxCDocumentoDAO;
import com.pe.lima.sg.dao.caja.IMasivoSunatDAO;
import com.pe.lima.sg.dao.caja.IMasivoTiendaSunatDAO;
import com.pe.lima.sg.dao.cliente.IContratoDAO;
import com.pe.lima.sg.dao.mantenimiento.ITiendaDAO;
import com.pe.lima.sg.entity.caja.TblCxcDocumento;
import com.pe.lima.sg.entity.caja.TblMasivoSunat;
import com.pe.lima.sg.entity.caja.TblMasivoTiendaSunat;
import com.pe.lima.sg.entity.cliente.TblContrato;
import com.pe.lima.sg.entity.mantenimiento.TblTienda;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.PageWrapper;
import com.pe.lima.sg.presentacion.util.PageableSG;
import com.pe.lima.sg.presentacion.util.UtilSGT;

import lombok.extern.slf4j.Slf4j;


/**
 * Clase Bean que se encarga de la administracion de la caja chica
 *
 * 			
 */
@Slf4j
@Controller
//@PreAuthorize("hasAuthority('CRUD')")
public class FacturaAlquilerAction {
	
	
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
	private String urlPaginado = "/masivo/facturas/alquiler/paginado/"; 
	
	
	
	/**
	 * Se encarga de listar todos los registros de caja chica
	 * 
	 * @param model
	 * @return
	 */
	

	@RequestMapping(value = "/masivo/facturas/alquiler", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path, HttpServletRequest request) {
		Filtro filtro = null;
		
		try{
			log.debug("[traerRegistros] Inicio");
			path = "masivo/alquiler/fac_listado";
			
			filtro = new Filtro();
			request.getSession().setAttribute("CriterioFiltroMasivoFacturaAlquiler",filtro);
			
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
	@RequestMapping(value = "/masivo/facturas/alquiler/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, Filtro filtro, String path ,  PageableSG pageable,HttpServletRequest request) {
		path = "masivo/alquiler/fac_listado";
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
	/*** Listado de Registro de Gasto ***/
	private void listarMasivoFactura(Model model, Filtro entidad,  PageableSG pageable, String url, HttpServletRequest request){

		Sort sort = new Sort(new Sort.Order(Direction.DESC, "codigoMasivo"));
		try{
			Specification<TblMasivoSunat> filtro = Specifications.where(conCodigoEdificio(entidad.getCodigoEdificacion()))
					.and(conAnio(entidad.getAnio()))
					.and(conMes(entidad.getMesFiltro()))
					.and(conTipoMasivo(Constantes.MASIVO_TIPO_ALQUILER))
					.and(conEstado(Constantes.ESTADO_REGISTRO_ACTIVO));
			pageable.setSort(sort);
			Page<TblMasivoSunat> entidadPage = masivoSunatDao.findAll(filtro, pageable);
			PageWrapper<TblMasivoSunat> page = new PageWrapper<TblMasivoSunat>(entidadPage, url, pageable);
			List<MasivoSunatBean> lista = this.procesarListaMasivoAlquiler(page.getContent(), request);
			model.addAttribute("registros", lista);
			model.addAttribute("page", page);
			

			request.getSession().setAttribute("CriterioFiltroMasivoFacturaAlquiler", entidad);
			request.getSession().setAttribute("ListadoMasivoFactura", lista);
			request.getSession().setAttribute("PageMasivoFactura", page);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private List<MasivoSunatBean> procesarListaMasivoAlquiler(List<TblMasivoSunat> listaMasivoFactura,	HttpServletRequest request) {
		List<MasivoSunatBean> lista = new ArrayList<>();
		MasivoSunatBean masivoSunatBean = null;
		Map<Integer, String> mapEdificio = null;
		if (listaMasivoFactura !=null){
			mapEdificio = (Map<Integer, String>) request.getSession().getAttribute("SessionMapEdificacionOperacion");
			for(TblMasivoSunat tblMasivoSunat : listaMasivoFactura){
				masivoSunatBean = new MasivoSunatBean();
				masivoSunatBean.setAnio(tblMasivoSunat.getAnio());
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
				if (masivoSunatBean.getEstadoMasivo().equals("REGISTRO")) {
					masivoSunatBean.setFlagAdicionar("S");
					masivoSunatBean.setFlagEliminar("S");
					masivoSunatBean.setFlagProcesa("S");
				}
				
			}
		}
		return lista;
	}
	/**
	 * Se encarga de direccionar a la pantalla de adicionar mas elementos
	 * 
	 */
	@RequestMapping(value = "/masivo/facturas/alquiler/adicionar/{id}", method = RequestMethod.GET)
	public String adicionarTiendas(@PathVariable Integer id, Model model) {
		TblMasivoSunat entidad 			= null;
		String path 					= "caja/gasto/gas_edicion";
		try{
			entidad = masivoSunatDao.findOne(id);
			//Pendiente el detalle
			
			model.addAttribute("entidad", entidad);
			path = "masivo/alquiler/fac_add_tienda";
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return path;
	}
	/**
	 * Se encarga de direccionar a la pantalla de creacion de Masivos para la factura de alquiler
	 * 
	 */
	@RequestMapping(value = "/masivo/facturas/alquiler/nuevo", method = RequestMethod.GET)
	public String nuevoMasivoFactura(Model model,HttpServletRequest request) {
		MasivoSunatBean masivoSunatBean = null;
		try{
			log.debug("[nuevoMasivoFactura] Inicio");
			masivoSunatBean = inicializaDatosParaNuevoRegistro();
			model.addAttribute("entidad", masivoSunatBean);
			request.getSession().setAttribute("MasivoFacturaAlquilerNuevo", masivoSunatBean);
			request.getSession().setAttribute("SessionMapTiendaExcluida", null);
			log.debug("[nuevoMasivoFactura] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "masivo/alquiler/fac_nuevo_empresa";
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
	
	
	/*Mostramos todas las tiendas de la empresa(edificio) seleccionado*/
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "masivo/facturas/alquiler/nuevo/tiendas", method = RequestMethod.POST)
	public String nuevoConTiendaMasivoFacturaTienda(Model model, MasivoSunatBean entidad, HttpServletRequest request) {
		Map<Integer, String> mapEdificio = null;
		try{
			log.debug("[nuevoConTiendaMasivoFacturaTienda] Inicio");
			mapEdificio = (Map<Integer, String>) request.getSession().getAttribute("SessionMapEdificacionOperacion");
			Integer keyTienda = entidad.getCodigoEdificio();
			entidad.setNombreEdificio(mapEdificio.get(keyTienda));
			List<TblTienda> listaTiendaActivo = tiendaDao.listarAllActivos(entidad.getCodigoEdificio());
			List<TblContrato> listaContratoActivo = contratoDao.listAllContratoActivosxFecha(new Date());
			List<TblCxcDocumento> listaCxCActivo = cxcDocumentoDao.listarCxCByAnioMes(Constantes.TIPO_PAGO_ALQUILER_CODIGO, entidad.getAnio(), new Integer(entidad.getMes()));
			obtenerListaTiendasAFacturar(listaTiendaActivo,listaContratoActivo,listaCxCActivo,entidad);
			
			model.addAttribute("entidad", entidad);
			model.addAttribute("registros", entidad.getListaTiendaSunat());
			request.getSession().setAttribute("MasivoFacturaAlquilerNuevo", entidad);
			request.getSession().setAttribute("SessionMapTiendaExcluida", entidad.getMapTiendaExcluidasComboBox());
			log.debug("[nuevoConTiendaMasivoFacturaTienda] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "masivo/alquiler/fac_nuevo_tienda";
	}
	

	@RequestMapping(value = "/masivotienda,/facturas/alquiler/nuevo/agregarExcluida", method = RequestMethod.POST)
	public String agregarNuevoTiendaMasivoFacturaTienda(Model model, MasivoSunatBean entidad, HttpServletRequest request) {
		MasivoSunatBean masivoSunatBean = null;
		try{
			log.debug("[agregarNuevoTiendaMasivoFacturaTienda] Inicio");
			masivoSunatBean = (MasivoSunatBean)request.getSession().getAttribute("MasivoFacturaAlquilerNuevo");
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
				request.getSession().setAttribute("MasivoFacturaAlquilerNuevo", masivoSunatBean);
				request.getSession().setAttribute("SessionMapTiendaExcluida", masivoSunatBean.getMapTiendaExcluidasComboBox());
			}
			
			log.debug("[agregarNuevoTiendaMasivoFacturaTienda] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "masivo/alquiler/fac_nuevo_tienda";
	}
	
	@RequestMapping(value = "/masivotienda,/facturas/alquiler/tienda/eliminar/{id}", method = RequestMethod.GET)
	public String quitarNuevoTiendaMasivoFacturaTienda(@PathVariable Integer id, Model model, HttpServletRequest request) {
		MasivoSunatBean masivoSunatBean = null;
		String path 					= "masivo/alquiler/fac_nuevo_tienda";
		try{
			masivoSunatBean = (MasivoSunatBean)request.getSession().getAttribute("MasivoFacturaAlquilerNuevo");
			MasivoTiendaSunatBean masivoTiendaSunatBean = masivoSunatBean.getListaTiendaSunat().get(id.intValue());
			Integer codigoTienda = masivoTiendaSunatBean.getCodigoTienda();
			String numeroTienda = masivoTiendaSunatBean.getNumeroTienda();
			masivoSunatBean.getListaTiendaSunat().remove(id.intValue());
			masivoSunatBean.getMapTiendaExcluidas().put(codigoTienda,numeroTienda);
			Map<String,Integer> mapTiendaExcluidasCombo = generarNuevoMapTiendaExcluidaCombo(masivoSunatBean.getMapTiendaExcluidas());
			masivoSunatBean.setMapTiendaExcluidasComboBox(mapTiendaExcluidasCombo);
			model.addAttribute("entidad", masivoSunatBean);
			model.addAttribute("registros", masivoSunatBean.getListaTiendaSunat());
			request.getSession().setAttribute("MasivoFacturaAlquilerNuevo", masivoSunatBean);
			request.getSession().setAttribute("SessionMapTiendaExcluida", masivoSunatBean.getMapTiendaExcluidasComboBox());
		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
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
				TblContrato contrato = mapContrato.get(documento.getCodigoReferencia());
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
			
			
			
		}catch(Exception e){
			exitoso = false;
		}
		return exitoso;
	}
	/**
	 * Se encarga de guardar la informacion 
	 * 
	 */
	@RequestMapping(value = "/masivotienda,/facturas/alquiler/nuevo/tiendas/guardar", method = RequestMethod.POST)
	public String guardarEntidad(Model model, MasivoSunatBean entidad, HttpServletRequest request, String path , PageableSG pageable) {
		path = "masivo/alquiler/fac_listado";
		try{
			log.debug("[guardarEntidad] Inicio" );

			if (this.validarNegocio(model, entidad, request)){
				log.debug("[guardarEntidad] Pre Guardar..." );
				TblMasivoSunat tblMasivoSunat = setearDatosMasivoSunat(entidad,request);
				tblMasivoSunat = masivoSunatDao.save(tblMasivoSunat);
				List<TblMasivoTiendaSunat> listaMasivoTienda = setearDatosMasivoTienda(entidad, tblMasivoSunat,request);
				for(TblMasivoTiendaSunat tblMasivoTiendaSunat : listaMasivoTienda) {
					masivoTiendaSunatDao.save(tblMasivoTiendaSunat);
				}
				model.addAttribute("respuesta", "Se registró exitosamente");
				Filtro filtro = (Filtro)request.getSession().getAttribute("CriterioFiltroMasivoFacturaAlquiler");
				this.traerRegistrosFiltrados(model,filtro, path, pageable, request);
			}else{
				path = "masivo/alquiler/fac_nuevo_tienda";
				model.addAttribute("entidad", entidad);
			}
			
			log.debug("[guardarEntidad] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
		
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
				listaMasivoTienda.add(tblMasivoTiendaSunat);
		    }
		}
		return null;
	}

	/*Seteamos los datos a registrar en la tabla*/
	private TblMasivoSunat setearDatosMasivoSunat(MasivoSunatBean entidad, HttpServletRequest request) {
		TblMasivoSunat tblMasivoSunat = new TblMasivoSunat();
		tblMasivoSunat.setAnio(entidad.getAnio());
		tblMasivoSunat.setCodigoEdificio(entidad.getCodigoEdificio());
		tblMasivoSunat.setPeriodo(entidad.getPeriodo());
		tblMasivoSunat.setTotalProcesada(entidad.getListaTiendaSunat().size());
		tblMasivoSunat.setTotalExcluido(entidad.getMapTiendaExcluidas().size());
		tblMasivoSunat.setEstadoMasivo(Constantes.MASIVO_ESTADO_REGISTRADO);
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
		tblMasivoSunat.setAuditoriaCreacion(request);
		
		return tblMasivoSunat;
	}

	
	/**
	 * Se encarga de la eliminacion logica del registro
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/masivo/facturas/alquiler/eliminar/{id}", method = RequestMethod.GET)
	public String eliminarMasivoFactura(@PathVariable Integer id, HttpServletRequest request, Model model, PageableSG pageable) {
		TblMasivoSunat entidad		= null;
		String path 				= null;
		Filtro filtro				= null;
		try{
			log.debug("[eliminarMasivoFactura] Inicio");
			path = "masivo/alquiler/fac_listado";
			
			entidad = masivoSunatDao.findOne(id);
			entidad.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO);
			entidad.setAuditoriaModificacion(request);
			
			masivoSunatDao.save(entidad);
			model.addAttribute("respuesta", "Eliminación exitosa");
			model.addAttribute("filtro", request.getSession().getAttribute("CriterioFiltroMasivoFacturaAlquiler"));
			filtro = (Filtro)request.getSession().getAttribute("CriterioFiltroMasivoFacturaAlquiler");
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
	

	/*
	 * Paginado
	 */
	@RequestMapping(value = "/masivo/facturas/alquiler/paginado/{page}/{size}/{operacion}", method = RequestMethod.GET)
	public String paginarEntidad(@PathVariable Integer page, @PathVariable Integer size, @PathVariable String operacion, Model model,  PageableSG pageable, HttpServletRequest request) {
		Filtro filtro = null;
		String path = null;
		try{
			//log.debug("[traerRegistros] Inicio");
			path = "masivo/alquiler/fac_listado";
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
			filtro = (Filtro)request.getSession().getAttribute("CriterioFiltroMasivoFacturaAlquiler");
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
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/masivo/facturas/alquiler/regresarlista", method = RequestMethod.GET)
	public String regresar(Model model, String path, HttpServletRequest request) {
		Filtro filtro = null;
		List<TblMasivoSunat> lista = null;
		PageWrapper<TblMasivoSunat> page = null;
		try{
			log.debug("[regresar] Inicio");
			path = "masivo/alquiler/fac_listado";
			
			filtro = (Filtro)request.getSession().getAttribute("CriterioFiltroMasivoFacturaAlquiler");
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
	@RequestMapping(value = "/masivotienda,/facturas/alquiler/regresarempresa", method = RequestMethod.GET)
	public String regresarEmpresa(Model model, String path, HttpServletRequest request) {
		MasivoSunatBean masivoSunatBean = null;
		try{
			log.debug("[regresarEmpresa] Inicio");
			path = "masivo/alquiler/fac_nuevo_empresa";
			masivoSunatBean = (MasivoSunatBean)request.getSession().getAttribute("MasivoFacturaAlquilerNuevo");
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
}
