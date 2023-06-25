package com.pe.lima.sg.presentacion.caja;

import static com.pe.lima.sg.dao.caja.ComprobanteOseSpecifications.conEstado;
import static com.pe.lima.sg.dao.caja.ComprobanteOseSpecifications.conNumero;
import static com.pe.lima.sg.dao.caja.ComprobanteOseSpecifications.conSerie;
import static com.pe.lima.sg.dao.caja.ComprobanteOseSpecifications.conTipoPago;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.pe.lima.sg.bean.caja.ComprobanteSunatBean;
import com.pe.lima.sg.bean.caja.FacturaBean;
import com.pe.lima.sg.dao.caja.IComprobanteOseDAO;
import com.pe.lima.sg.dao.caja.ICxCDocumentoDAO;
import com.pe.lima.sg.dao.caja.IDetalleComprobanteOseDAO;
import com.pe.lima.sg.dao.caja.IDetalleFormaPagoOseDAO;
import com.pe.lima.sg.dao.mantenimiento.ITipoCambioDAO;
import com.pe.lima.sg.entity.caja.TblComprobanteSunat;
import com.pe.lima.sg.entity.caja.TblCxcDocumento;
import com.pe.lima.sg.entity.caja.TblDetalleComprobante;
import com.pe.lima.sg.entity.caja.TblDetalleFormaPago;
import com.pe.lima.sg.entity.mantenimiento.TblParametro;
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

@Slf4j
@Controller
public class FacturaAction {
	
	@Autowired
	private ISerieSFS12DAO serieDao;
	
	@Autowired
	private FacturaOseDao facturaOseDao;
	
	@Autowired
	private IComprobanteOseDAO comprobanteOseDao;
	
	@Autowired
	private IDetalleComprobanteOseDAO detalleComprobanteOseDao;
	
	@Autowired
	private IDetalleFormaPagoOseDAO formaPagoOseDao;
	
	@Autowired
	private ICxCDocumentoDAO cxCDocumentoDao;
	
	@Autowired
	private ITipoCambioDAO tipoCambioDao;
	
	@Autowired
	private IApiOseCSV apiOseCSV;
	
	@Autowired
	private ServletContext context;
	
	private String urlPaginado = "/facturas/paginado/"; 
	
	
	@RequestMapping(value = "/facturas", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path) {
		Filtro filtro = new Filtro();
		try{
			log.debug("[traerRegistros] Inicio");
			path = "caja/factura/fac_listado";
			model.addAttribute("filtro", filtro);
			model.addAttribute("registros", new ArrayList<TblComprobanteSunat>());
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
	@RequestMapping(value = "/facturas/q", method = RequestMethod.POST)
	public String traerRegistros(Model model, Filtro filtro, String path ,  PageableSG pageable,HttpServletRequest request) {
		path = "caja/factura/fac_listado";
		try{
			log.debug("[traerRegistros] Inicio");
			if (criteriosValido(filtro,model)) {
				listarAlquilerServicio(model, filtro,pageable,this.urlPaginado, request);
			}
			
			model.addAttribute("filtro", filtro);
			log.debug("[traerRegistros] Fin");
		}catch(Exception e){
			log.debug("[traerRegistros] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			
		}
		log.debug("[traerRegistros] Fin");
		return path;
	}
	@RequestMapping(value = "/facturas/nuevo", method = RequestMethod.GET)
	public String crearFactura(Model model, HttpServletRequest request) {
		TblComprobanteSunat facturaSunat = new TblComprobanteSunat();
		TblDetalleComprobante detalleFacturaSunat = new TblDetalleComprobante();
		TblDetalleFormaPago formaPago = new TblDetalleFormaPago();
		FacturaBean facturaBean = new FacturaBean();
		try{
			log.debug("[crearFactura] Inicio");
			obtenerDatosFactura(facturaSunat,request);
			facturaBean.setFactura(facturaSunat);
			facturaBean.setFacturaDetalle(detalleFacturaSunat);
			facturaBean.setFormaPago(formaPago);
			model.addAttribute("entidad", facturaBean);
			request.getSession().setAttribute("DatosFacturaBean",facturaBean);
			log.debug("[crearFactura] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "caja/factura/fac_nuevo";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/facturas/ver/{id}", method = RequestMethod.GET)
	public String verFactura(@PathVariable Integer id,Model model, HttpServletRequest request) {
		ComprobanteSunatBean entidad	= null;
		List<ComprobanteSunatBean> lista= null;	
		FacturaBean facturaBean = new FacturaBean();
		TblComprobanteSunat factura = new TblComprobanteSunat();
		TblDetalleComprobante facturaDetalle = new TblDetalleComprobante();
		List<TblDetalleFormaPago> listaFormaPago;
		try{
			log.debug("[verFactura] Inicio");
			lista = (List<ComprobanteSunatBean>)request.getSession().getAttribute("ListadoComprobante");
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
		return "caja/factura/fac_ver";
	}
	@RequestMapping(value = "/facturas/nuevo/guardar", method = RequestMethod.POST)
	public String guardarFactura(Model model, FacturaBean facturaBean, HttpServletRequest request,  PageableSG pageable) {
		String path = "caja/factura/fac_listado";
		FacturaBean entidad = null;
		CredencialBean credencial = null;
		try{
			log.debug("[guardarFactura] Inicio" );
			entidad = (FacturaBean)request.getSession().getAttribute("DatosFacturaBean");
			if (validarFacturaOk(model, facturaBean, request, entidad)){
				log.debug("[guardarEntidad] Pre Guardar..." );
				preGuardarComprobante(entidad.getFactura(), request);
				/*Obtener numero de la serie*/
				obtenerNumeroSerie(entidad.getFactura());
				TblComprobanteSunat comprobante = comprobanteOseDao.save(entidad.getFactura());
				preGuardarDetalleComprobante(entidad.getFacturaDetalle(), request);
				entidad.getFacturaDetalle().setTblComprobante(comprobante);
				detalleComprobanteOseDao.save(entidad.getFacturaDetalle());
				if (entidad.getFactura().getFormaPago().equals(Constantes.FORMA_PAGO_CREDITO)) {
					for(TblDetalleFormaPago formaPago: entidad.getListaFormaPago()) {
						preGuardarFormaPago(formaPago, request);
						formaPago.setTblComprobante(comprobante);
						formaPagoOseDao.save(formaPago);
					}
				}
				/*Guardar numero de comprobante*/
				actualizarCxCDocumentoConNumeroComprobante(entidad.getCodigoCxCDocumento(), comprobante.getCodigoComprobante(), comprobante.getSerie() , comprobante.getNumero(),request);
				/*Incrementar el numero de la serie*/
				incrementarNumeroSerie();
				/*Generamos el archivo CSV para la factura*/
				credencial = obtenerCredenciales(request);
				obtenerNombreArchivos(credencial,entidad);
				generarArchivoCSV(entidad, credencial);
				/*Llamamos a los apis*/
				llamadasApiOse(credencial,model,comprobante,request);
				/*Mostramos el comprobante registrado*/
				Filtro datosFiltro = obtenerDatosComprobante(comprobante);
				listarAlquilerServicio(model, datosFiltro, pageable, this.urlPaginado, request);
				model.addAttribute("filtro", datosFiltro);
				log.debug("[guardarEntidad] Guardado..." );
				model.addAttribute("respuesta", "Guardado exitoso");
				
			}else{
				path = "caja/factura/fac_nuevo";
				model.addAttribute("entidad", entidad);
				model.addAttribute("registrospago", entidad.getListaFormaPago());
				request.getSession().setAttribute("DatosFacturaBean",entidad);
			}
			
			
			log.debug("[guardarEntidad] Fin" );
		}catch(Exception e){
			e.printStackTrace();
			model.addAttribute("respuesta", "Error al intentar guardar:"+e.getMessage());
		}
		return path;
		
	}
	@RequestMapping(value = "/facturas/consulta", method = RequestMethod.POST)
	public String consultarClientes(Model model, FacturaBean facturaBean, HttpServletRequest request) {
		String path = "caja/factura/fac_consulta";
		Filtro filtro = new Filtro();
		try{
			log.debug("[consultarClientes] Inicio");
			request.getSession().setAttribute("DatosFacturaBean",facturaBean);
			filtro.setCodigoEdificacion(Constantes.CODIGO_LA_REYNA);
			filtro.setTipo(Constantes.TIPO_COBRO_ALQUILER);
			model.addAttribute("filtro", filtro);
			model.addAttribute("registros", null);
			model.addAttribute("page", null);
			log.debug("[consultarClientes] Fin");
		}catch(Exception e){
			log.debug("[consultarClientes] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			
		}
		log.debug("[consultarClientes] Fin");
		return path;
	}
	@RequestMapping(value = "/facturas/consulta/q", method = RequestMethod.POST)
	public String traerRegistrosConsulta(Model model, Filtro filtro, String path ,  PageableSG pageable,HttpServletRequest request) {
		path = "caja/factura/fac_consulta";
		try{
			log.debug("[traerRegistrosConsulta] Inicio");
			if (criteriosConsultaValido(filtro,model)) {
				listarConsultaAlquilerServicio(model, filtro, request);
			}
			
			model.addAttribute("filtro", filtro);
			log.debug("[traerRegistrosConsulta] Fin");
		}catch(Exception e){
			log.debug("[traerRegistrosConsulta] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			
		}
		log.debug("[traerRegistrosConsulta] Fin");
		return path;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/facturas/consulta/seleccionar/{id}", method = RequestMethod.GET)
	public String seleccionaraRegistroConsulta(@PathVariable Integer id, HttpServletRequest request, Model model, PageableSG pageable) {
		FacturaBean entidad			= null;
		String path 				= null;
		List<FacturaBean> lista 		= null;		
		try{
			log.debug("[seleccionaraRegistroConsulta] Inicio");
			path = "caja/factura/fac_nuevo";
			lista = (List<FacturaBean>)request.getSession().getAttribute("ListadoConsultaAlquilerServicio");
			entidad = lista.get(id);
			this.obtenerDatosFactura(entidad.getFactura(), request);
			actualizarTipoOperacion(entidad,request);
			request.getSession().setAttribute("DatosFacturaBean",entidad);
			model.addAttribute("entidad", entidad);
			log.debug("[seleccionaraRegistroConsulta] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;			
		}
		return path;
	}

	@RequestMapping(value = "facturas/formapago/mostrar", method = RequestMethod.POST)
	public String mostrarFormaPago(FacturaBean factura, Model model, String path, HttpServletRequest request) {
		FacturaBean entidad = null;
		try{
			log.debug("[mostrarFormaPago] Inicio");
			path = "caja/factura/fac_nuevo";
			entidad = (FacturaBean)request.getSession().getAttribute("DatosFacturaBean");
			entidad.getFactura().setFormaPago(factura.getFactura().getFormaPago());
			model.addAttribute("entidad", entidad);
			request.getSession().setAttribute("DatosFacturaBean",entidad);
			log.debug("[mostrarFormaPago] Fin");
		}catch(Exception e){
			log.debug("[mostrarFormaPago] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		
		return path;
	}
	@RequestMapping(value = "facturas/formapago/agregar", method = RequestMethod.POST)
	public String agregarFormaPago(FacturaBean factura, Model model, String path, HttpServletRequest request) {
		FacturaBean entidad = null;
		TblDetalleFormaPago detalleFormaPago = null;
		try{
			log.debug("[agregarFormaPago] Inicio");
			path = "caja/factura/fac_nuevo";
			entidad = (FacturaBean)request.getSession().getAttribute("DatosFacturaBean");
			if (validarDatosFormaPagoOk(factura, model,entidad)) {
				if (entidad.getListaFormaPago() == null) {
					entidad.setListaFormaPago(new ArrayList<>());
				}
				detalleFormaPago = new TblDetalleFormaPago();
				detalleFormaPago.setMonto(factura.getMontoCredito());
				detalleFormaPago.setFecha(factura.getFechaFin());
				detalleFormaPago.setMoneda(factura.getFormaPago().getMoneda());
				entidad.getListaFormaPago().add(detalleFormaPago);
			}
			model.addAttribute("entidad", entidad);
			model.addAttribute("registrospago", entidad.getListaFormaPago());
			request.getSession().setAttribute("DatosFacturaBean",entidad);
			log.debug("[agregarFormaPago] Fin");
		}catch(Exception e){
			log.debug("[agregarFormaPago] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		return path;
	}
	
	@RequestMapping(value = "/facturas/formapago/eliminar/{id}", method = RequestMethod.GET)
	public String eliminarFormaPago(@PathVariable Integer id, HttpServletRequest request, Model model, PageableSG pageable) {
		FacturaBean entidad			= null;
		String path 				= null;

		try{
			log.debug("[eliminarFormaPago] Inicio:"+id);
			path = "caja/factura/fac_nuevo";
			entidad = (FacturaBean)request.getSession().getAttribute("DatosFacturaBean");
			TblDetalleFormaPago detalle = entidad.getListaFormaPago().get(id);
			entidad.getListaFormaPago().remove(detalle);
			request.getSession().setAttribute("DatosFacturaBean",entidad);
			model.addAttribute("entidad", entidad);
			model.addAttribute("registrospago", entidad.getListaFormaPago());
			log.debug("[eliminarFormaPago] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;			
		}
		return path;
	}
	@RequestMapping(value = "/facturas/regresar/nuevo", method = RequestMethod.GET)
	public String regresarNuevo(Model model, String path, HttpServletRequest request) {
		FacturaBean entidad			= null;
		try{
			log.debug("[regresarNuevo] Inicio");
			path = "caja/factura/fac_nuevo";
			entidad = (FacturaBean)request.getSession().getAttribute("DatosFacturaBean");
			model.addAttribute("entidad", entidad);

			log.debug("[regresarNuevo] Fin");
		}catch(Exception e){
			log.debug("[regresarNuevo] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			entidad = null;
		}
		
		return path;
	}
	@RequestMapping(value = "/facturas/regresar", method = RequestMethod.GET)
	public String regresar(Model model, String path, HttpServletRequest request) {
		try{
			log.debug("[regresar] Inicio");
			path = "caja/factura/fac_listado";
			
			Filtro filtro = (Filtro)request.getSession().getAttribute("CriterioFiltroComprobante");
			if (filtro == null) {
				filtro = new Filtro();
			}
			
			model.addAttribute("filtro", filtro);
			model.addAttribute("registros", request.getSession().getAttribute("ListadoComprobante"));
			model.addAttribute("page", request.getSession().getAttribute("PageComprobante"));
			
			log.debug("[regresar] Fin");
		}catch(Exception e){
			log.debug("[regresar] Error:"+e.getMessage());
			e.printStackTrace();
		}
		
		return path;
	}
	private void listarAlquilerServicio(Model model, Filtro entidad,  PageableSG pageable, String url, HttpServletRequest request){
		List<TblComprobanteSunat> entidades = new ArrayList<TblComprobanteSunat>();
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "codigoComprobante"));
		try{
			Specification<TblComprobanteSunat> filtro = Specifications.where(conNumero(entidad.getNumero()))
					.and(conSerie(entidad.getSerie()))
					.and(conTipoPago(entidad.getTipo())).and(conEstado("1"));
			pageable.setSort(sort);
			Page<TblComprobanteSunat> entidadPage = comprobanteOseDao.findAll(filtro, pageable);
			PageWrapper<TblComprobanteSunat> page = new PageWrapper<TblComprobanteSunat>(entidadPage, url, pageable);
			List<ComprobanteSunatBean> lista = this.procesarListaComprobante(page.getContent(), request);
			model.addAttribute("registros", lista);
			model.addAttribute("page", page);
			
			log.debug("[listarAlquilerServicio] entidades:"+entidades);
			request.getSession().setAttribute("CriterioFiltroComprobante", entidad);
			request.getSession().setAttribute("ListadoComprobante", lista);
			request.getSession().setAttribute("PageComprobante", page);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades = null;
		}
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
	private boolean criteriosValido(Filtro filtro, Model model) {
		return true;
	}
	
	private void llamadasApiOse(CredencialBean credencial,Model model,TblComprobanteSunat comprobante, HttpServletRequest request) {
		try {
			String token = apiOseCSV.obtenerToken(credencial); 
			credencial.setAccessToken(token);
			Integer status = apiOseCSV.obtenerTicket(credencial);
			if (status.compareTo(200)==0) {
				comprobante = actualizarTicketEnComprobanteSunat(credencial,comprobante,request);
				status = apiOseCSV.obtenerCDRDocumento(credencial);
				if (status.compareTo(200)==0) {
					comprobante = actualizarCdrEnComprobanteSunat(credencial,comprobante,request);
					status = apiOseCSV.obtenerXMLDocumento(credencial);
					if (status.compareTo(200)==0) {
						comprobante = actualizarXmlEnComprobanteSunat(credencial,comprobante,request);
						status = apiOseCSV.obtenerPDFDocumento(credencial);
						if (status.compareTo(200)==0) {
							comprobante = actualizarPdfEnComprobanteSunat(credencial,comprobante,request);
						}else {
							actualizarStatusPdfEnComprobanteSunat(credencial, comprobante, request);
							log.info("[llamadasApiOse] Con error en PDF:"+status);
							model.addAttribute("respuesta", "Con error en PDF:"+status);
						}
					}else {
						actualizarStatusXmlEnComprobanteSunat(credencial, comprobante, request);
						log.info("[llamadasApiOse] Con error en XML:"+status);
						model.addAttribute("respuesta", "Con error en XML:"+status);
					}
				}else {
					actualizarStatusCdrEnComprobanteSunat(credencial, comprobante, request);
					log.info("[llamadasApiOse] Con error en CDR:"+status);
					model.addAttribute("respuesta", "Con error en CDR:"+status);
				}
			}else {
				actualizarStatusTicketEnComprobanteSunat(credencial, comprobante, request);
				log.info("[llamadasApiOse] Con error en Ticket:"+status);
				model.addAttribute("respuesta", "Con error en Ticket:"+status);
			}
		}catch(Exception e) {
			model.addAttribute("respuesta", "Error obtener el token:"+e.getMessage());
		}
		
	}
	
	private TblComprobanteSunat actualizarTicketEnComprobanteSunat(CredencialBean credencial, TblComprobanteSunat comprobante, HttpServletRequest request) {
		comprobante.setNumeroTicket(credencial.getTicket());
		return actualizarStatusTicketEnComprobanteSunat(credencial, comprobante, request);
	}
	private TblComprobanteSunat actualizarStatusTicketEnComprobanteSunat(CredencialBean credencial, TblComprobanteSunat comprobante, HttpServletRequest request) {
		comprobante.setNombreCsv(credencial.getCsvFileName());
		comprobante.setEstadoOperacion("TICKET: "+credencial.getStatus());
		this.preUpdateComprobante(comprobante, request);
		TblComprobanteSunat comprobanteUpdate = comprobanteOseDao.save(comprobante);
		return comprobanteUpdate;
	}
	private TblComprobanteSunat actualizarCdrEnComprobanteSunat(CredencialBean credencial, TblComprobanteSunat comprobante, HttpServletRequest request) {
		comprobante.setNombreCdr(credencial.getCdrFileName());
		return actualizarStatusCdrEnComprobanteSunat(credencial, comprobante, request);
	}
	private TblComprobanteSunat actualizarStatusCdrEnComprobanteSunat(CredencialBean credencial, TblComprobanteSunat comprobante, HttpServletRequest request) {
		comprobante.setEstadoOperacion("CDR: "+credencial.getStatus());
		this.preUpdateComprobante(comprobante, request);
		TblComprobanteSunat comprobanteUpdate = comprobanteOseDao.save(comprobante);
		return comprobanteUpdate;
	}
	private TblComprobanteSunat actualizarXmlEnComprobanteSunat(CredencialBean credencial, TblComprobanteSunat comprobante, HttpServletRequest request) {
		comprobante.setNombreXml(credencial.getXmlFileName());
		return actualizarStatusXmlEnComprobanteSunat(credencial, comprobante, request);
	}
	private TblComprobanteSunat actualizarStatusXmlEnComprobanteSunat(CredencialBean credencial, TblComprobanteSunat comprobante, HttpServletRequest request) {
		comprobante.setEstadoOperacion("XML: "+credencial.getStatus());
		this.preUpdateComprobante(comprobante, request);
		TblComprobanteSunat comprobanteUpdate = comprobanteOseDao.save(comprobante);
		return comprobanteUpdate;
	}
	private TblComprobanteSunat actualizarPdfEnComprobanteSunat(CredencialBean credencial, TblComprobanteSunat comprobante, HttpServletRequest request) {
		comprobante.setNombrePdf(credencial.getPdfFileName());
		return actualizarStatusPdfEnComprobanteSunat(credencial, comprobante, request);
	}
	private TblComprobanteSunat actualizarStatusPdfEnComprobanteSunat(CredencialBean credencial, TblComprobanteSunat comprobante, HttpServletRequest request) {
		comprobante.setEstadoOperacion("PDF: "+credencial.getStatus());
		this.preUpdateComprobante(comprobante, request);
		TblComprobanteSunat comprobanteUpdate = comprobanteOseDao.save(comprobante);
		return comprobanteUpdate;
	}
	
	private void asignarDatosFacturaBeanDeComprobanteSunatBean(FacturaBean factura, ComprobanteSunatBean entidad) {
		factura.setFactura(new TblComprobanteSunat());
		factura.getFactura().setSerie(entidad.getSerie());
		factura.getFactura().setNumero(entidad.getNumero());
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
	private Filtro obtenerDatosComprobante(TblComprobanteSunat comprobante) {
		Filtro filtro = new Filtro();
		filtro.setTipo(comprobante.getTipoPago());
		filtro.setSerie(comprobante.getSerie());
		filtro.setNumero(comprobante.getNumero());
		return filtro;
	}
	private void actualizarCxCDocumentoConNumeroComprobante(Integer codigoCxCDocumento, Integer codigoComprobante, String serie, String numeroComprobante, HttpServletRequest request) {
		TblCxcDocumento documento = cxCDocumentoDao.findOne(codigoCxCDocumento);
		documento.setCodigoComprobante(codigoComprobante);
		documento.setSerieComprobante(serie);
		documento.setNumeroComprobante(numeroComprobante);
		preUpdateCxCDocumento(documento, request);
		cxCDocumentoDao.save(documento);
	}
	private boolean validarFacturaOk(Model model, FacturaBean facturaBean, HttpServletRequest request,FacturaBean entidad) {
		boolean resultado = true;
		
		if (facturaBean.getFacturaDetalle() == null || facturaBean.getFacturaDetalle().getNombreProducto().isEmpty()) {
			model.addAttribute("respuesta", "Debe seleccionar el producto a facturar");
			resultado = false;
		}
		if (facturaBean.getFactura() == null || facturaBean.getFactura().getFormaPago() == null || facturaBean.getFactura().getFormaPago().equals("-1")) {
			model.addAttribute("respuesta", "Debe seleccionar la forma de pago");
			resultado = false;
		}else {
			entidad.getFactura().setFormaPago(facturaBean.getFactura().getFormaPago());
		}
		if (facturaBean.getFactura() != null && facturaBean.getFactura().getFormaPago() != null && 
			facturaBean.getFactura().getFormaPago().equals(Constantes.FORMA_PAGO_CREDITO) && (entidad.getListaFormaPago() == null || entidad.getListaFormaPago().isEmpty())) {
			model.addAttribute("respuesta", "Debe ingresar las cuotas del pago a credito");
			resultado = false;
		}
		if (entidad.getFactura().getFormaPago().equals(Constantes.FORMA_PAGO_CREDITO)) {
			if (entidad.getFactura().getTipoOperacion().equals(Constantes.TIPO_OPERACION_VENTA_INTERNA_CODIGO)) {
				BigDecimal total = entidad.getFactura().getTotal();
				if (montoNoOkFactura(entidad,total)) {
					model.addAttribute("respuesta", "La suma de cuotas no coinciden con el monto pendiente de pago ["+total.toString()+"]");
					resultado = false;
				}
			}else {
				//BigDecimal totalDetraccion = entidad.getFactura().getDetracionMonto();
				BigDecimal totalDetraccion = entidad.getFactura().getDetracionTotal();
				if (montoNoOkFactura(entidad,totalDetraccion)) {
					model.addAttribute("respuesta", "La suma de cuotas no coinciden con el monto pendiente de pago ["+totalDetraccion.toString()+"]");
					resultado = false;
				}
			}
		}
		return resultado;
	}
	private boolean montoNoOkFactura(FacturaBean factura, BigDecimal total) {
		if (factura.getListaFormaPago()== null || factura.getListaFormaPago().isEmpty()) {
			return true;
		}else {
			BigDecimal suma = new BigDecimal("0");
			for(TblDetalleFormaPago det: factura.getListaFormaPago()) {
				suma = suma.add(det.getMonto());
			}
			log.info("[montoNoOkFactura] suma: "+suma + " - total: "+total);
			if (Math.abs(suma.subtract(total).doubleValue())<1) {
				return false; //valores coinciden
			}else {
				return true;
			}
		}
	}
	private void preUpdateCxCDocumento(TblCxcDocumento entidad, HttpServletRequest request) {
		try{
			log.debug("[preUpdateComprobante] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			log.debug("[preUpdateComprobante] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void preUpdateComprobante(TblComprobanteSunat entidad, HttpServletRequest request) {
		try{
			log.debug("[preUpdateComprobante] Inicio" );
			entidad.setFechaModificacion(new Date(System.currentTimeMillis()));
			entidad.setIpModificacion(request.getRemoteAddr());
			entidad.setUsuarioModificacion(UtilSGT.mGetUsuario(request));
			log.debug("[preUpdateComprobante] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void preGuardarComprobante(TblComprobanteSunat entidad, HttpServletRequest request) {
		try{
			log.debug("[preGuardarComprobante] Inicio" );
			entidad.setFechaCreacion(new Date(System.currentTimeMillis()));
			entidad.setIpCreacion(request.getRemoteAddr());
			entidad.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			log.debug("[preGuardarComprobante] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void preGuardarDetalleComprobante(TblDetalleComprobante entidad, HttpServletRequest request) {
		try{
			log.debug("[preGuardarDetalleComprobante] Inicio" );
			entidad.setFechaCreacion(new Date(System.currentTimeMillis()));
			entidad.setIpCreacion(request.getRemoteAddr());
			entidad.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			log.debug("[preGuardarDetalleComprobante] Fin" );
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void preGuardarFormaPago(TblDetalleFormaPago entidad, HttpServletRequest request) {
		try{
			log.debug("[preGuardarFormaPago] Inicio" );
			entidad.setFechaCreacion(new Date(System.currentTimeMillis()));
			entidad.setIpCreacion(request.getRemoteAddr());
			entidad.setUsuarioCreacion(UtilSGT.mGetUsuario(request));
			entidad.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			log.debug("[preGuardarFormaPago] Fin" );
		}catch(Exception e){
			e.printStackTrace();
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
		montoCalculadoDetraccion = entidad.getFactura().getTotal().multiply(porcentajeDetraccion).divide(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_UP);
		entidad.getFactura().setDetracionTotal(entidad.getFactura().getTotal().subtract(montoCalculadoDetraccion));
		//Sobre este monto se realiza la forma de pago
		entidad.getFormaPago().setMonto(entidad.getFactura().getDetracionTotal());
		if (entidad.getFactura().getMoneda().equals("USD")) {
			log.info("[asignarDatosDetraccion] detraccion[ROUND_UP]: "+montoCalculadoDetraccion.multiply(tipoCambio.getValor()).setScale(0, BigDecimal.ROUND_UP));
			montoCalculadoDetraccion = montoCalculadoDetraccion.multiply(tipoCambio.getValor()).setScale(0, BigDecimal.ROUND_UP);
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
		montoSoles = tipoCambio.getValor().multiply(entidad.getFactura().getTotal()).setScale(2, BigDecimal.ROUND_UP);
		if (montoSoles.compareTo(montoDetraccion)>=0) {
			return true;
		}else {
			return false;
		}
	}
	private void listarConsultaAlquilerServicio(Model model, Filtro filtro, HttpServletRequest request) {
		TblTipoCambio tipoCambio = tipoCambioDao.obtenerUltimoTipoCambio();
		filtro.setTipoCambio(tipoCambio.getValor());
		List<FacturaBean> lista 		= null;		
		lista = facturaOseDao.getConsultaAlquilerServicioOse(filtro);
		model.addAttribute("registros", lista);
		request.getSession().setAttribute("CriterioConsultaAlquilerServicio", filtro);
		request.getSession().setAttribute("ListadoConsultaAlquilerServicio", lista);
		
	}
	private boolean criteriosConsultaValido(Filtro filtro, Model model) {
		boolean exitoso = true;
		try{
			
			if (filtro.getCodigoEdificacion() < 0){
				model.addAttribute("respuesta", "Debe seleccionar el inmueble");
				return false;
			}
			if (filtro.getTipo().equals("-1")){
				model.addAttribute("respuesta", "Debe seleccionar el tipo (Alquiler/Servicio)");
				return false;
			}
		}catch(Exception e){
			exitoso = false;
		}
		return exitoso;
	}

	private boolean validarDatosFormaPagoOk(FacturaBean factura, Model model, FacturaBean entidad) {
		boolean resultado = true;
		if (factura.getMontoCredito()== null || factura.getMontoCredito().compareTo(new BigDecimal("0")) <= 0 ) {
			model.addAttribute("respuesta", "Debe ingresar el monto");
			resultado = false;
		}
		entidad.setMontoCredito(factura.getMontoCredito());
		if (factura.getFechaFin() == null || factura.getFechaFin().isEmpty() || UtilSGT.getDatetoString(factura.getFechaFin()).before(new Date())) {
			model.addAttribute("respuesta", "Debe ingresar una fecha posterior");
			resultado = false;
		}
		entidad.setFechaFin(factura.getFechaFin());
		return resultado;
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
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/facturas/ticket/{id}", method = RequestMethod.GET)
	public String obtenerTicketDocumento(@PathVariable Integer id, HttpServletRequest request, Model model) {
		ComprobanteSunatBean entidad	= null;
		List<ComprobanteSunatBean> lista= null;		
		CredencialBean credencial 		= null;
		FacturaBean factura				= new FacturaBean();
		try{
			log.debug("[obtenerTicketDocumento] Inicio");
			lista = (List<ComprobanteSunatBean>)request.getSession().getAttribute("ListadoComprobante");
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
			model.addAttribute("page", request.getSession().getAttribute("PageComprobante"));
			model.addAttribute("filtro", request.getSession().getAttribute("CriterioFiltroComprobante"));
			log.debug("[obtenerTicketDocumento] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;			
		}
		return "caja/factura/fac_listado";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/facturas/cdr/{id}", method = RequestMethod.GET)
	public String obtenerCdrDocumento(@PathVariable Integer id, HttpServletRequest request, Model model, PageableSG pageable) {
		ComprobanteSunatBean entidad	= null;
		List<ComprobanteSunatBean> lista= null;		
		CredencialBean credencial 		= null;
		FacturaBean factura				= new FacturaBean();
		try{
			log.debug("[obtenerCdrDocumento] Inicio");
			lista = (List<ComprobanteSunatBean>)request.getSession().getAttribute("ListadoComprobante");
			entidad = lista.get(id);
			asignarDatosFacturaBeanDeComprobanteSunatBean(factura,entidad);
			credencial = obtenerCredenciales(request);
			obtenerNombreArchivos(credencial,factura);
			String token = apiOseCSV.obtenerToken(credencial);
			credencial.setAccessToken(token);
			credencial.setTicket(entidad.getNumeroTicket());
			Integer status = apiOseCSV.obtenerCDRDocumento(credencial);
			log.debug("[obtenerCdrDocumento] status: " +status);
			if (status.compareTo(200)==0) {
				TblComprobanteSunat comprobante = comprobanteOseDao.findOne(entidad.getCodigoComprobante());
				comprobante = actualizarCdrEnComprobanteSunat(credencial,comprobante,request);
			}else {
				TblComprobanteSunat comprobante = comprobanteOseDao.findOne(entidad.getCodigoComprobante());
				actualizarStatusCdrEnComprobanteSunat(credencial,comprobante,request);
				log.info("[obtenerCdrDocumento] Con error en CDR:"+status);
				model.addAttribute("respuesta", "Con error en CDR:"+status);
			}
			Filtro datosFiltro = (Filtro)request.getSession().getAttribute("CriterioFiltroComprobante");
			listarAlquilerServicio(model, datosFiltro, pageable, this.urlPaginado, request);
			model.addAttribute("filtro", datosFiltro);
			model.addAttribute("page", request.getSession().getAttribute("PageComprobante"));
			model.addAttribute("filtro", request.getSession().getAttribute("CriterioFiltroComprobante"));
			log.debug("[obtenerCdrDocumento] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;			
		}
		return "caja/factura/fac_listado";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/facturas/cdr/descargar/{id}", method = RequestMethod.GET)
	public void obtenerCdrDocumentoDescargar(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
		ComprobanteSunatBean entidad	= null;
		List<ComprobanteSunatBean> lista= null;		
		CredencialBean credencial 		= null;
		FacturaBean factura				= new FacturaBean();
		try{
			log.debug("[obtenerCdrDocumentoDescargar] Inicio");
			lista = (List<ComprobanteSunatBean>)request.getSession().getAttribute("ListadoComprobante");
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
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/facturas/xml/{id}", method = RequestMethod.GET)
	public String obtenerXmlDocumento(@PathVariable Integer id, HttpServletRequest request, Model model, PageableSG pageable) {
		ComprobanteSunatBean entidad	= null;
		List<ComprobanteSunatBean> lista= null;		
		CredencialBean credencial 		= null;
		FacturaBean factura				= new FacturaBean();
		try{
			log.debug("[obtenerXmlDocumento] Inicio");
			lista = (List<ComprobanteSunatBean>)request.getSession().getAttribute("ListadoComprobante");
			entidad = lista.get(id);
			asignarDatosFacturaBeanDeComprobanteSunatBean(factura,entidad);
			credencial = obtenerCredenciales(request);
			obtenerNombreArchivos(credencial,factura);
			String token = apiOseCSV.obtenerToken(credencial);
			credencial.setAccessToken(token);
			credencial.setTicket(entidad.getNumeroTicket());
			Integer status = apiOseCSV.obtenerXMLDocumento(credencial);
			log.debug("[obtenerXmlDocumento] status: "+status);
			if (status.compareTo(200)==0) {
				TblComprobanteSunat comprobante = comprobanteOseDao.findOne(entidad.getCodigoComprobante());
				comprobante = actualizarXmlEnComprobanteSunat(credencial,comprobante,request);
			}else {
				TblComprobanteSunat comprobante = comprobanteOseDao.findOne(entidad.getCodigoComprobante());
				actualizarStatusXmlEnComprobanteSunat(credencial,comprobante,request);
				log.info("[obtenerXmlDocumento] Con error en XML:"+status);
				model.addAttribute("respuesta", "Con error en XML:"+status);
			}
			Filtro datosFiltro = (Filtro)request.getSession().getAttribute("CriterioFiltroComprobante");
			listarAlquilerServicio(model, datosFiltro, pageable, this.urlPaginado, request);
			model.addAttribute("filtro", datosFiltro);
			model.addAttribute("page", request.getSession().getAttribute("PageComprobante"));
			model.addAttribute("filtro", request.getSession().getAttribute("CriterioFiltroComprobante"));
			log.debug("[obtenerXmlDocumento] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;			
		}
		return "caja/factura/fac_listado";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/facturas/xml/descargar/{id}", method = RequestMethod.GET)
	public void obtenerXmlDocumentoDescargar(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
		ComprobanteSunatBean entidad	= null;
		List<ComprobanteSunatBean> lista= null;		
		CredencialBean credencial 		= null;
		FacturaBean factura				= new FacturaBean();
		try{
			log.debug("[obtenerXmlDocumentoDescargar] Inicio");
			lista = (List<ComprobanteSunatBean>)request.getSession().getAttribute("ListadoComprobante");
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
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/facturas/pdf/{id}", method = RequestMethod.GET)
	public String obtenerPdfDocumento(@PathVariable Integer id, HttpServletRequest request, Model model, PageableSG pageable) {
		ComprobanteSunatBean entidad	= null;
		List<ComprobanteSunatBean> lista= null;		
		CredencialBean credencial 		= null;
		FacturaBean factura				= new FacturaBean();
		try{
			log.debug("[obtenerPdfDocumento] Inicio");
			lista = (List<ComprobanteSunatBean>)request.getSession().getAttribute("ListadoComprobante");
			entidad = lista.get(id);
			asignarDatosFacturaBeanDeComprobanteSunatBean(factura,entidad);
			credencial = obtenerCredenciales(request);
			obtenerNombreArchivos(credencial,factura);
			String token = apiOseCSV.obtenerToken(credencial);
			credencial.setAccessToken(token);
			credencial.setTicket(entidad.getNumeroTicket());
			Integer status = apiOseCSV.obtenerPDFDocumento(credencial);
			log.debug("[obtenerPdfDocumento] status: "+status);
			if (status.compareTo(200)==0) {
				TblComprobanteSunat comprobante = comprobanteOseDao.findOne(entidad.getCodigoComprobante());
				comprobante = actualizarPdfEnComprobanteSunat(credencial,comprobante,request);
			}else {
				TblComprobanteSunat comprobante = comprobanteOseDao.findOne(entidad.getCodigoComprobante());
				actualizarStatusPdfEnComprobanteSunat(credencial,comprobante,request);
				log.info("[obtenerPdfDocumento] Con error en PDF:"+status);
				model.addAttribute("respuesta", "Con error en PDF:"+status);
			}
			Filtro datosFiltro = (Filtro)request.getSession().getAttribute("CriterioFiltroComprobante");
			listarAlquilerServicio(model, datosFiltro, pageable, this.urlPaginado, request);
			model.addAttribute("filtro", datosFiltro);
			model.addAttribute("page", request.getSession().getAttribute("PageComprobante"));
			model.addAttribute("filtro", request.getSession().getAttribute("CriterioFiltroComprobante"));
			log.debug("[obtenerPdfDocumento] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;			
		}
		return "caja/factura/fac_listado";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/facturas/pdf/descargar/{id}", method = RequestMethod.GET)
	public void obtenerPdfDocumentoDescargar(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
		ComprobanteSunatBean entidad	= null;
		List<ComprobanteSunatBean> lista= null;		
		CredencialBean credencial 		= null;
		FacturaBean factura				= new FacturaBean();
		try{
			log.debug("[obtenerPdfDocumentoDescargar] Inicio");
			lista = (List<ComprobanteSunatBean>)request.getSession().getAttribute("ListadoComprobante");
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
	/*
	 * Paginado
	 */
	@RequestMapping(value = "/facturas/paginado/{page}/{size}/{operacion}", method = RequestMethod.GET)
	public String paginarEntidad(@PathVariable Integer page, @PathVariable Integer size, @PathVariable String operacion, Model model,  PageableSG pageable, HttpServletRequest request) {
		Filtro filtro = null;
		String path = null;
		Map<String, Object> campos = null;
		try{
			//log.debug("[traerRegistros] Inicio");
			path = "caja/factura/fac_listado";
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
			
			filtro = (Filtro)request.getSession().getAttribute("CriterioFiltroComprobante");
			model.addAttribute("filtro", filtro);
			this.listarAlquilerServicio(model, filtro, pageable, urlPaginado, request);
			
		}catch(Exception e){
			//log.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		return path;
	}	
}
