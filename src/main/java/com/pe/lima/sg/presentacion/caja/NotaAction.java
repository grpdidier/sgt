package com.pe.lima.sg.presentacion.caja;

import static com.pe.lima.sg.dao.caja.NotaOseSpecifications.conEstado;
import static com.pe.lima.sg.dao.caja.NotaOseSpecifications.conNumero;
import static com.pe.lima.sg.dao.caja.NotaOseSpecifications.conSerie;
import static com.pe.lima.sg.dao.caja.NotaOseSpecifications.conTipoPago;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.pe.lima.sg.bean.caja.FacturaBean;
import com.pe.lima.sg.bean.caja.NotaBean;
import com.pe.lima.sg.bean.caja.NotaSunatBean;
import com.pe.lima.sg.dao.caja.IComprobanteOseDAO;
import com.pe.lima.sg.dao.caja.IDetalleComprobanteOseDAO;
import com.pe.lima.sg.dao.caja.IDetalleFormaPagoOseDAO;
import com.pe.lima.sg.dao.caja.INotaOseDAO;
import com.pe.lima.sg.entity.caja.TblComprobanteSunat;
import com.pe.lima.sg.entity.caja.TblDetalleComprobante;
import com.pe.lima.sg.entity.caja.TblDetalleFormaPago;
import com.pe.lima.sg.entity.caja.TblNotaSunat;
import com.pe.lima.sg.facturador.dao.ISerieSFS12DAO;
import com.pe.lima.sg.facturador.entity.TblSerie;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.PageWrapper;
import com.pe.lima.sg.presentacion.util.PageableSG;
import com.pe.lima.sg.presentacion.util.UtilFacturacion;
import com.pe.lima.sg.presentacion.util.UtilSGT;
import com.pe.lima.sg.presentacion.util.UtilUBLNota;
import com.pe.lima.sg.rs.ose.FacturaOseDao;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class NotaAction {
	
	@Autowired
	private ISerieSFS12DAO serieDao;
	
	
	@Autowired
	private FacturaOseDao facturaOseDao;
	
	@Autowired
	private IComprobanteOseDAO comprobanteOseDao;
	
	@Autowired
	private INotaOseDAO notaOseDao;
	
	@Autowired
	private IDetalleComprobanteOseDAO detalleComprobanteOseDao;
	
	@Autowired
	private IDetalleFormaPagoOseDAO formaPagoOseDao;
	
	@Autowired
	private UtilFacturacion utilFacturacion;
		
	@Autowired
	private IApiOseCSV apiOseCSV;
	
	@Autowired
	private ServletContext context;
	
	private String urlPaginado = "/notas/paginado/"; 
	
	
	@RequestMapping(value = "/notas", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path,HttpServletRequest request) {
		Filtro filtro = new Filtro();
		try{
			log.debug("[traerRegistros] Inicio");
			path = "caja/nota/not_listado";
			model.addAttribute("filtro", filtro);
			model.addAttribute("registros", new ArrayList<TblComprobanteSunat>());
			model.addAttribute("page", null);
			request.getSession().setAttribute("CriterioFiltroNota",filtro);
			log.debug("[traerRegistros] Fin");
		}catch(Exception e){
			log.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}

		return path;

	}
	@RequestMapping(value = "/notas/q", method = RequestMethod.POST)
	public String traerRegistros(Model model, Filtro filtro, String path ,  PageableSG pageable,HttpServletRequest request) {
		path = "caja/nota/not_listado";
		try{
			log.debug("[traerRegistros] Inicio");
			if (criteriosValido(filtro,model)) {
				listarNotaAlquilerServicio(model, filtro,pageable,this.urlPaginado, request);
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
	@RequestMapping(value = "/notas/nuevo", method = RequestMethod.GET)
	public String crearNota(Model model, HttpServletRequest request) {
		TblNotaSunat notaSunat = new TblNotaSunat();
		TblComprobanteSunat facturaSunat = new TblComprobanteSunat();
		TblDetalleComprobante detalleFacturaSunat = new TblDetalleComprobante();
		TblDetalleFormaPago formaPago = new TblDetalleFormaPago();
		NotaBean notaBean = new NotaBean();
		try{
			log.debug("[crearNota] Inicio");
			obtenerDatosNota(notaSunat,request);
			notaBean.setNota(notaSunat);
			notaBean.setFactura(facturaSunat);
			notaBean.setFacturaDetalle(detalleFacturaSunat);
			notaBean.setFormaPago(formaPago);
			model.addAttribute("entidad", notaBean);
			
			log.debug("[crearNota] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "caja/nota/not_nuevo";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/notas/ver/{id}", method = RequestMethod.GET)
	public String verNota(@PathVariable Integer id,Model model, HttpServletRequest request) {
		NotaSunatBean entidad	= null;
		List<NotaSunatBean> lista= null;	
		NotaBean notaBean = new NotaBean();
		TblNotaSunat nota = new TblNotaSunat();
		TblComprobanteSunat factura = new TblComprobanteSunat();
		TblDetalleComprobante facturaDetalle = new TblDetalleComprobante();
		List<TblDetalleFormaPago> listaFormaPago;
		try{
			log.debug("[verNota] Inicio");
			lista = (List<NotaSunatBean>)request.getSession().getAttribute("ListadoNota");
			entidad = lista.get(id);
			nota = notaOseDao.findOne(entidad.getCodigoNota());
			notaBean.setNota(nota);
			factura = comprobanteOseDao.findOne(entidad.getCodigoComprobante());
			notaBean.setFactura(factura);
			facturaDetalle = detalleComprobanteOseDao.getDetallexComprobante(entidad.getCodigoComprobante());
			notaBean.setFacturaDetalle(facturaDetalle);
			listaFormaPago = formaPagoOseDao.listarAllxComprobante(entidad.getCodigoComprobante());
			notaBean.setListaFormaPago(listaFormaPago);
			model.addAttribute("entidad", notaBean);
			model.addAttribute("registrospago", listaFormaPago);
			log.debug("[verNota] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "caja/nota/not_ver";
	}
	@RequestMapping(value = "/notas/nuevo/guardar", method = RequestMethod.POST)
	public String guardarNota(Model model, NotaBean notaBean, HttpServletRequest request,  PageableSG pageable) {
		String path = "caja/nota/not_listado";
		NotaBean entidad = null;
		CredencialBean credencial = null;
		try{
			log.debug("[guardarNota] Inicio" );
			entidad = (NotaBean)request.getSession().getAttribute("DatosNotaBean");
			asignarDatosNotaSessionDeNotaLocal(entidad, notaBean, request);
			if (validarNotaOk(model, notaBean, request, entidad)){
				log.debug("[guardarNota] Pre Guardar..." );
				entidad.getNota().setAuditoriaCreacion(request);
				/*Obtener numero de la serie*/
				obtenerNumeroSerie(entidad.getNota());
				
				TblNotaSunat nota = notaOseDao.save(entidad.getNota());
				
				
				/*Guardar numero de comprobante*/
				actualizarComprobanteConNota(nota.getCodigoNota(), nota.getCodigoComprobante(), nota.getSerie() , nota.getNumero(),request);
				/*Incrementar el numero de la serie*/
				incrementarNumeroSerie();
				/*Generamos el archivo CSV para la nota*/
				credencial = utilFacturacion.obtenerCredenciales(request);
				obtenerNombreArchivos(credencial,entidad);
				generarArchivoCSV(entidad, credencial);
				/*Llamamos a los apis*/
				llamadasApiOse(credencial,model,nota,request);
				/*Mostramos el comprobante registrado*/
				Filtro datosFiltro = obtenerDatosNota(nota);
				listarNotaAlquilerServicio(model, datosFiltro, pageable, this.urlPaginado, request);
				model.addAttribute("filtro", datosFiltro);
				log.debug("[guardarNota] Guardado..." );
				model.addAttribute("respuesta", "Guardado exitoso");
				
			}else{
				path = "caja/nota/not_nuevo";
				model.addAttribute("entidad", entidad);
				model.addAttribute("registrospago", entidad.getListaFormaPago());
				request.getSession().setAttribute("DatosNotaBean",entidad);
			}
			
			
			log.debug("[guardarNota] Fin" );
		}catch(Exception e){
			e.printStackTrace();
			model.addAttribute("respuesta", "Error al intentar guardar:"+e.getMessage());
		}
		return path;
		
	}
	@RequestMapping(value = "/notas/consulta", method = RequestMethod.POST)
	public String consultarFacturas(Model model, NotaBean notaBean, HttpServletRequest request) {
		String path = "caja/nota/not_consulta";
		Filtro filtro = new Filtro();
		try{
			log.debug("[consultarFacturas] Inicio");
			request.getSession().setAttribute("DatosNotaBean",notaBean);
			filtro.setCodigoEdificacion(Constantes.CODIGO_LA_REYNA);
			filtro.setTipo(Constantes.TIPO_COBRO_ALQUILER);
			model.addAttribute("filtro", filtro);
			model.addAttribute("registros", null);
			model.addAttribute("page", null);
			log.debug("[consultarFacturas] Fin");
		}catch(Exception e){
			log.debug("[consultarFacturas] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			
		}
		log.debug("[consultarFacturas] Fin");
		return path;
	}
	@RequestMapping(value = "/notas/consulta/q", method = RequestMethod.POST)
	public String traerRegistrosConsulta(Model model, Filtro filtro, String path ,  PageableSG pageable,HttpServletRequest request) {
		path = "caja/nota/not_consulta";
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
	@RequestMapping(value = "/notas/consulta/seleccionar/{id}", method = RequestMethod.GET)
	public String seleccionaraRegistroConsulta(@PathVariable Integer id, HttpServletRequest request, Model model, PageableSG pageable) {
		NotaBean entidad			= null;
		FacturaBean factura			= null;
		String path 				= null;
		List<FacturaBean> lista 		= null;		
		try{
			log.debug("[seleccionaraRegistroConsulta] Inicio");
			path = "caja/nota/not_nuevo";
			lista = (List<FacturaBean>)request.getSession().getAttribute("ListadoConsultaFacturaAlquilerServicio");
			factura = lista.get(id);
			entidad = new NotaBean();
			entidad.setNota(new TblNotaSunat());
			this.obtenerDatosNota(entidad.getNota(), request);
			entidad.setFactura(comprobanteOseDao.findOne(factura.getFactura().getCodigoComprobante()));
			entidad.setFacturaDetalle(detalleComprobanteOseDao.findOne(factura.getFacturaDetalle().getCodigoDetalleComprobante()));
			entidad.setListaFormaPago(formaPagoOseDao.listarAllxComprobante(factura.getFactura().getCodigoComprobante()));
			if (entidad.getListaFormaPago().isEmpty()) {
				entidad.setFormaPago(new TblDetalleFormaPago());
				entidad.getFormaPago().setMoneda(entidad.getFactura().getMoneda());
				entidad.getFormaPago().setMonto(entidad.getFactura().getTotal());
			}
			request.getSession().setAttribute("DatosNotaBean",entidad);
			model.addAttribute("entidad", entidad);
			log.debug("[seleccionaraRegistroConsulta] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;			
		}
		return path;
	}
	private void listarNotaAlquilerServicio(Model model, Filtro entidad,  PageableSG pageable, String url, HttpServletRequest request){
		List<TblNotaSunat> entidades = new ArrayList<TblNotaSunat>();
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "codigoNota"));
		try{
			Specification<TblNotaSunat> filtro = Specifications.where(conNumero(entidad.getNumero()))
					.and(conSerie(entidad.getSerie()))
					.and(conTipoPago(entidad.getTipo())).and(conEstado("1"));
			pageable.setSort(sort);
			Page<TblNotaSunat> entidadPage = notaOseDao.findAll(filtro, pageable);
			PageWrapper<TblNotaSunat> page = new PageWrapper<TblNotaSunat>(entidadPage, url, pageable);
			List<NotaSunatBean> lista = this.procesarListaNota(page.getContent(), request);
			model.addAttribute("registros", lista);
			model.addAttribute("page", page);
			
			log.debug("[listarNotaAlquilerServicio] entidades:"+entidades);
			request.getSession().setAttribute("CriterioFiltroNota", entidad);
			request.getSession().setAttribute("ListadoNota", lista);
			request.getSession().setAttribute("PageNota", page);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidades = null;
		}
	}
	private List<NotaSunatBean> procesarListaNota(List<TblNotaSunat> content,HttpServletRequest request) {
		List<NotaSunatBean> lista = null;
		NotaSunatBean bean = null;
		if (content != null && !content.isEmpty()) {
			lista = new ArrayList<>();
			for (TblNotaSunat nota: content) {
				bean = new NotaSunatBean();
				bean.setSerie(nota.getSerie());
				bean.setNumero(nota.getNumero());
				bean.setFechaEmision(nota.getFechaEmision());
				bean.setMoneda(nota.getMoneda());
				bean.setEstadoOperacion(nota.getEstadoOperacion());
				bean.setCodigoComprobante(nota.getCodigoComprobante());
				bean.setCodigoNota(nota.getCodigoNota());
				bean.setTipoPago(nota.getTipoPago());
				bean.setSerieFactura(nota.getSerieFactura() + "-" + nota.getNumeroFactura());
				bean.setFechaEmision(nota.getFechaEmision());
				bean.setNumeroTienda(nota.getNumeroTienda());
				bean.setNumeroTicket(nota.getNumeroTicket());
				bean.setNombreCsv(nota.getNombreCsv());
				bean.setNombreCdr(nota.getNombreCdr());
				bean.setNombreXml(nota.getNombreXml());
				bean.setNombrePdf(nota.getNombrePdf());
				lista.add(bean);
			}	
		}
		
		return lista;
	}
	private boolean criteriosValido(Filtro filtro, Model model) {
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private void asignarDatosNotaSessionDeNotaLocal(NotaBean entidad, NotaBean notaBean, HttpServletRequest request) {
		entidad.getNota().setCodigoComprobante(entidad.getFactura().getCodigoComprobante());
		entidad.getNota().setMoneda(entidad.getFactura().getMoneda());
		entidad.getNota().setNumeroTienda(entidad.getFactura().getNumeroTienda());
		entidad.getNota().setSerieFactura(entidad.getFactura().getSerie());
		entidad.getNota().setNumeroFactura(entidad.getFactura().getNumero());
		entidad.getNota().setTipoPago(entidad.getFactura().getTipoPago());
		entidad.getNota().setSustento(notaBean.getNota().getSustento());
		HashMap<String, String> mapMotivo = (HashMap<String, String>)request.getSession().getAttribute("SessionMapNotaCreditoMotivo");
		entidad.setTipoNotaCredito(mapMotivo.get(entidad.getNota().getTipoNota()));
	}
	private void llamadasApiOse(CredencialBean credencial,Model model,TblNotaSunat nota, HttpServletRequest request) {
		try {
			String token = apiOseCSV.obtenerToken(credencial);
			credencial.setAccessToken(token);
			Integer status = apiOseCSV.obtenerTicket(credencial);
			if (status.compareTo(200)==0) {
				nota = actualizarTicketEnComprobanteSunat(credencial,nota,request);
				status = apiOseCSV.obtenerCDRDocumento(credencial);
				if (status.compareTo(200)==0) {
					nota = actualizarCdrEnNotaSunat(credencial,nota,request);
					status = apiOseCSV.obtenerXMLDocumento(credencial);
					if (status.compareTo(200)==0) {
						nota = actualizarXmlEnNotaSunat(credencial,nota,request);
						status = apiOseCSV.obtenerPDFDocumento(credencial);
						if (status.compareTo(200)==0) {
							nota = actualizarPdfEnNotaSunat(credencial,nota,request);
						}else {
							actualizarStatusPdfEnNotaSunat(credencial, nota, request);
							log.info("[llamadasApiOse] Con error en PDF:"+status);
							model.addAttribute("respuesta", "Con error en PDF:"+status);
						}
					}else {
						actualizarStatusXmlEnNotaSunat(credencial, nota, request);
						log.info("[llamadasApiOse] Con error en XML:"+status);
						model.addAttribute("respuesta", "Con error en XML:"+status);
					}
				}else {
					actualizarStatusCdrEnNotaSunat(credencial, nota, request);
					log.info("[llamadasApiOse] Con error en CDR:"+status);
					model.addAttribute("respuesta", "Con error en CDR:"+status);
				}
			}else {
				actualizarStatusTicketEnComprobanteSunat(credencial, nota, request);
				log.info("[llamadasApiOse] Con error en Ticket:"+status);
				model.addAttribute("respuesta", "Con error en Ticket:"+status);
			}
		}catch(Exception e) {
			model.addAttribute("respuesta", "Error obtener el token:"+e.getMessage());
		}
		
	}
	
	private TblNotaSunat actualizarTicketEnComprobanteSunat(CredencialBean credencial, TblNotaSunat nota, HttpServletRequest request) {
		nota.setNumeroTicket(credencial.getTicket());
		return actualizarStatusTicketEnComprobanteSunat(credencial, nota, request);
	}
	private TblNotaSunat actualizarStatusTicketEnComprobanteSunat(CredencialBean credencial, TblNotaSunat nota, HttpServletRequest request) {
		nota.setNombreCsv(credencial.getCsvFileName());
		nota.setEstadoOperacion("TICKET: "+credencial.getStatus());
		nota.setAuditoriaModificacion(request);
		TblNotaSunat notaUpdate = notaOseDao.save(nota);
		return notaUpdate;
	}
	private TblNotaSunat actualizarCdrEnNotaSunat(CredencialBean credencial, TblNotaSunat nota, HttpServletRequest request) {
		nota.setNombreCdr(credencial.getCdrFileName());
		return actualizarStatusCdrEnNotaSunat(credencial, nota, request);
	}
	private TblNotaSunat actualizarStatusCdrEnNotaSunat(CredencialBean credencial, TblNotaSunat nota, HttpServletRequest request) {
		nota.setEstadoOperacion("CDR: "+credencial.getStatus());
		nota.setAuditoriaModificacion(request);
		TblNotaSunat notaUpdate = notaOseDao.save(nota);
		return notaUpdate;
	}
	private TblNotaSunat actualizarXmlEnNotaSunat(CredencialBean credencial, TblNotaSunat nota, HttpServletRequest request) {
		nota.setNombreXml(credencial.getXmlFileName());
		return actualizarStatusXmlEnNotaSunat(credencial, nota, request);
	}
	private TblNotaSunat actualizarStatusXmlEnNotaSunat(CredencialBean credencial, TblNotaSunat nota, HttpServletRequest request) {
		nota.setEstadoOperacion("XML: "+credencial.getStatus());
		nota.setAuditoriaModificacion(request);
		TblNotaSunat notaUpdate = notaOseDao.save(nota);
		return notaUpdate;
	}
	private TblNotaSunat actualizarPdfEnNotaSunat(CredencialBean credencial, TblNotaSunat nota, HttpServletRequest request) {
		nota.setNombrePdf(credencial.getPdfFileName());
		return actualizarStatusPdfEnNotaSunat(credencial, nota, request);
	}
	private TblNotaSunat actualizarStatusPdfEnNotaSunat(CredencialBean credencial, TblNotaSunat nota, HttpServletRequest request) {
		nota.setEstadoOperacion("PDF: "+credencial.getStatus());
		nota.setAuditoriaModificacion(request);
		TblNotaSunat notaUpdate = notaOseDao.save(nota);
		return notaUpdate;
	}
	
	private void asignarDatosNotaBeanDeNotaSunatBean(NotaBean nota, NotaSunatBean entidad) {
		nota.setNota(new TblNotaSunat());
		nota.getNota().setSerie(entidad.getSerie());
		nota.getNota().setNumero(entidad.getNumero());
	}
	private void obtenerNombreArchivos(CredencialBean crendencial, NotaBean entidad) {
		crendencial.setCsvFileName(UtilSGT.getNombreNotaCVS(entidad));
		crendencial.setCdrFileName(UtilSGT.getNombreNotaCDR(entidad));
		crendencial.setXmlFileName(UtilSGT.getNombreNotaXML(entidad));
		crendencial.setPdfFileName(UtilSGT.getNombreNotaPDF(entidad));
	}
	
	private void generarArchivoCSV(NotaBean entidad, CredencialBean crendencial) {
		List<TagUbl> listaHeader = null;
		List<TagUbl> listaDetail = null;
		listaHeader = UtilUBLNota.nodoUblHeader(entidad);
		listaDetail = UtilUBLNota.nodoUblDetail(entidad);
		generarArchivo(listaHeader, listaDetail, entidad , crendencial);
	}
	
	private boolean generarArchivo(List<TagUbl> listaHeader,List<TagUbl> listaDetail, NotaBean entidad, CredencialBean crendencial){
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
	private Filtro obtenerDatosNota(TblNotaSunat nota) {
		Filtro filtro = new Filtro();
		filtro.setTipo(nota.getTipoPago());
		filtro.setSerie(nota.getSerie());
		filtro.setNumero(nota.getNumero());
		return filtro;
	}
	private void actualizarComprobanteConNota(Integer codigoNota, Integer codigoComprobante, String serieNota, String numeroNota, HttpServletRequest request) {
		TblComprobanteSunat factura = comprobanteOseDao.findOne(codigoComprobante);
		factura.setCodigoNota(codigoNota);
		factura.setSerieNota(serieNota);
		factura.setNumeroNota(numeroNota);
		factura.setAuditoriaModificacion(request);
		comprobanteOseDao.save(factura);
	}
	private boolean validarNotaOk(Model model, NotaBean notaBean, HttpServletRequest request,NotaBean entidad) {
		boolean resultado = true;
		if (entidad.getNota().getTipoComprobante() == null || entidad.getNota().getTipoComprobante().equals("-1")) {
			model.addAttribute("respuesta", "Debe seleccionar el tipo de comprobante");
			resultado = false;
		}
		if (notaBean.getNota().getTipoNota() == null || notaBean.getNota().getTipoNota().equals("-1")) {
			model.addAttribute("respuesta", "Debe seleccionar el tipo de nota");
			resultado = false;
		}
		if (notaBean.getNota().getSustento() == null || notaBean.getNota().getSustento().trim().equals("")) {
			model.addAttribute("respuesta", "Debe ingresar el sustento");
			resultado = false;
		}
		if (notaBean.getFactura() == null || notaBean.getFactura().getNumero().trim().equals("")) {
			model.addAttribute("respuesta", "Debe seleccionar una factura");
			resultado = false;
		}
		
		return resultado;
	}
	
	

	private void obtenerDatosNota(TblNotaSunat notaSunat, HttpServletRequest request) {
		//Obtener Serie - Numero
		obtenerNumeroSerie(notaSunat);
		notaSunat.setTipoComprobante(Constantes.TIPO_OPERACION_NOTA_CREDITO_CODIGO);
		notaSunat.setFechaEmision(UtilSGT.getFecha("yyyy-MM-dd"));
		notaSunat.setHoraEmision(UtilSGT.getHora());
		notaSunat.setTipoComprobante(Constantes.SUNAT_CODIGO_COMPROBANTE_NOTA_CREDITO);
		notaSunat.setTipoNota(Constantes.SUNAT_TIPO_NOTA_01_ANULACION_CODIGO);
	}
	private void obtenerNumeroSerie(TblNotaSunat notaSunat) {
		TblSerie serie = null;
		serie = serieDao.buscarOneByTipoComprobante(Constantes.SUNAT_CODIGO_COMPROBANTE_NOTA_CREDITO);
		notaSunat.setSerie(serie.getPrefijoSerie());
		notaSunat.setNumero(serie.getNumeroComprobante());
		
	}
	private void incrementarNumeroSerie() {
		TblSerie serie = null;
		serie = serieDao.buscarOneByTipoComprobante(Constantes.SUNAT_CODIGO_COMPROBANTE_NOTA_CREDITO);
		Integer numero = serie.getSecuencialSerie();
		numero++;
		String numeroFormateado = String.format("%08d", numero);
		serie.setNumeroComprobante(numeroFormateado);
		serie.setSecuencialSerie(numero);
		serieDao.save(serie);
		
	}
	
	
	
	
	private void listarConsultaAlquilerServicio(Model model, Filtro filtro, HttpServletRequest request) {
		List<FacturaBean> lista 		= null;		
		lista = facturaOseDao.getConsultaFacturaOse(filtro);
		
		model.addAttribute("registros", lista);
		request.getSession().setAttribute("CriterioConsultaFacturaAlquilerServicio", filtro);
		request.getSession().setAttribute("ListadoConsultaFacturaAlquilerServicio", lista);
		
	}
	private boolean criteriosConsultaValido(Filtro filtro, Model model) {
		boolean exitoso = true;
		try{
			
			
			if (filtro.getTipo().equals("-1")){
				model.addAttribute("respuesta", "Debe seleccionar el tipo (Alquiler/Servicio)");
				return false;
			}
		}catch(Exception e){
			exitoso = false;
		}
		return exitoso;
	}

	@RequestMapping(value = "/notas/regresar/nuevo", method = RequestMethod.GET)
	public String regresarNuevo(Model model, String path, HttpServletRequest request) {
		NotaBean entidad			= null;
		try{
			log.debug("[regresarNuevo] Inicio");
			path = "caja/nota/not_nuevo";
			entidad = (NotaBean)request.getSession().getAttribute("DatosNotaBean");
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
	@RequestMapping(value = "/notas/regresar", method = RequestMethod.GET)
	public String regresar(Model model, String path, HttpServletRequest request) {
		try{
			log.debug("[regresar] Inicio");
			path = "caja/nota/not_listado";
			
			model.addAttribute("filtro", request.getSession().getAttribute("CriterioFiltroNota"));
			model.addAttribute("registros", request.getSession().getAttribute("ListadoNota"));
			model.addAttribute("page", request.getSession().getAttribute("PageNota"));
			
			log.debug("[regresar] Fin");
		}catch(Exception e){
			log.debug("[regresar] Error:"+e.getMessage());
			e.printStackTrace();
		}
		
		return path;
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
	@RequestMapping(value = "/notas/ticket/{id}", method = RequestMethod.GET)
	public String obtenerTicketDocumento(@PathVariable Integer id, HttpServletRequest request, Model model) {
		NotaSunatBean entidad	= null;
		List<NotaSunatBean> lista= null;		
		CredencialBean credencial 		= null;
		NotaBean nota				= new NotaBean();
		try{
			log.debug("[obtenerTicketDocumento] Inicio");
			lista = (List<NotaSunatBean>)request.getSession().getAttribute("ListadoNota");
			entidad = lista.get(id);
			asignarDatosNotaBeanDeNotaSunatBean(nota,entidad);
			credencial = utilFacturacion.obtenerCredenciales(request);
			obtenerNombreArchivos(credencial,nota);
			String token = apiOseCSV.obtenerToken(credencial);
			credencial.setAccessToken(token);
			Integer status = apiOseCSV.obtenerTicket(credencial);
			log.debug("[obtenerTicketDocumento] status: "+status);
			if (status.compareTo(200)==0) {
				entidad.setNumeroTicket(credencial.getTicket());
				TblNotaSunat comprobante = notaOseDao.findOne(entidad.getCodigoNota());
				comprobante = actualizarTicketEnComprobanteSunat(credencial,comprobante,request);
			}else {
				TblNotaSunat comprobante = notaOseDao.findOne(entidad.getCodigoNota());
				actualizarStatusTicketEnComprobanteSunat(credencial,comprobante,request);
				log.info("[llamadasApiOse] Con error en Ticket:"+status);
				model.addAttribute("respuesta", "Con error en Ticket:"+status);
			}
			model.addAttribute("registros", lista);
			model.addAttribute("page", request.getSession().getAttribute("PageNota"));
			model.addAttribute("filtro", request.getSession().getAttribute("CriterioFiltroNota"));
			log.debug("[obtenerTicketDocumento] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;			
		}
		return "caja/nota/not_listado";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/notas/cdr/{id}", method = RequestMethod.GET)
	public String obtenerCdrDocumento(@PathVariable Integer id, HttpServletRequest request, Model model, PageableSG pageable) {
		NotaSunatBean entidad	= null;
		List<NotaSunatBean> lista= null;		
		CredencialBean credencial 		= null;
		NotaBean nota				= new NotaBean();
		try{
			log.debug("[obtenerCdrDocumento] Inicio");
			lista = (List<NotaSunatBean>)request.getSession().getAttribute("ListadoNota");
			entidad = lista.get(id);
			asignarDatosNotaBeanDeNotaSunatBean(nota,entidad);
			credencial = utilFacturacion.obtenerCredenciales(request);
			obtenerNombreArchivos(credencial,nota);
			String token = apiOseCSV.obtenerToken(credencial);
			credencial.setAccessToken(token);
			credencial.setTicket(entidad.getNumeroTicket());
			Integer status = apiOseCSV.obtenerCDRDocumento(credencial);
			log.debug("[obtenerCdrDocumento] status: " +status);
			if (status.compareTo(200)==0) {
				TblNotaSunat comprobante = notaOseDao.findOne(entidad.getCodigoNota());
				comprobante = actualizarCdrEnNotaSunat(credencial,comprobante,request);
			}else {
				TblNotaSunat comprobante = notaOseDao.findOne(entidad.getCodigoNota());
				actualizarStatusCdrEnNotaSunat(credencial,comprobante,request);
				log.info("[obtenerCdrDocumento] Con error en CDR:"+status);
				model.addAttribute("respuesta", "Con error en CDR:"+status);
			}
			Filtro datosFiltro = (Filtro)request.getSession().getAttribute("CriterioFiltroNota");
			listarNotaAlquilerServicio(model, datosFiltro, pageable, this.urlPaginado, request);
			model.addAttribute("filtro", datosFiltro);
			model.addAttribute("page", request.getSession().getAttribute("PageNota"));
			model.addAttribute("filtro", request.getSession().getAttribute("CriterioFiltroNota"));
			log.debug("[obtenerCdrDocumento] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;			
		}
		return "caja/nota/not_listado";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/notas/cdr/descargar/{id}", method = RequestMethod.GET)
	public void obtenerCdrDocumentoDescargar(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
		NotaSunatBean entidad	= null;
		List<NotaSunatBean> lista= null;		
		CredencialBean credencial 		= null;
		NotaBean nota				= new NotaBean();
		try{
			log.debug("[obtenerCdrDocumentoDescargar] Inicio");
			lista = (List<NotaSunatBean>)request.getSession().getAttribute("ListadoNota");
			entidad = lista.get(id);
			asignarDatosNotaBeanDeNotaSunatBean(nota,entidad);
			credencial = utilFacturacion.obtenerCredenciales(request);
			obtenerNombreArchivos(credencial,nota);
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
	@RequestMapping(value = "/notas/xml/{id}", method = RequestMethod.GET)
	public String obtenerXmlDocumento(@PathVariable Integer id, HttpServletRequest request, Model model, PageableSG pageable) {
		NotaSunatBean entidad	= null;
		List<NotaSunatBean> lista= null;		
		CredencialBean credencial 		= null;
		NotaBean nota				= new NotaBean();
		try{
			log.debug("[obtenerXmlDocumento] Inicio");
			lista = (List<NotaSunatBean>)request.getSession().getAttribute("ListadoNota");
			entidad = lista.get(id);
			asignarDatosNotaBeanDeNotaSunatBean(nota,entidad);
			credencial = utilFacturacion.obtenerCredenciales(request);
			obtenerNombreArchivos(credencial,nota);
			String token = apiOseCSV.obtenerToken(credencial);
			credencial.setAccessToken(token);
			credencial.setTicket(entidad.getNumeroTicket());
			Integer status = apiOseCSV.obtenerXMLDocumento(credencial);
			log.debug("[obtenerXmlDocumento] status: "+status);
			if (status.compareTo(200)==0) {
				TblNotaSunat comprobante = notaOseDao.findOne(entidad.getCodigoNota());
				comprobante = actualizarXmlEnNotaSunat(credencial,comprobante,request);
			}else {
				TblNotaSunat comprobante = notaOseDao.findOne(entidad.getCodigoNota());
				actualizarStatusXmlEnNotaSunat(credencial,comprobante,request);
				log.info("[obtenerXmlDocumento] Con error en XML:"+status);
				model.addAttribute("respuesta", "Con error en XML:"+status);
			}
			Filtro datosFiltro = (Filtro)request.getSession().getAttribute("CriterioFiltroNota");
			listarNotaAlquilerServicio(model, datosFiltro, pageable, this.urlPaginado, request);
			model.addAttribute("filtro", datosFiltro);
			model.addAttribute("page", request.getSession().getAttribute("PageNota"));
			model.addAttribute("filtro", request.getSession().getAttribute("CriterioFiltroNota"));
			log.debug("[obtenerXmlDocumento] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;			
		}
		return "caja/factura/fac_listado";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/notas/xml/descargar/{id}", method = RequestMethod.GET)
	public void obtenerXmlDocumentoDescargar(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
		NotaSunatBean entidad	= null;
		List<NotaSunatBean> lista= null;		
		CredencialBean credencial 		= null;
		NotaBean nota				= new NotaBean();
		try{
			log.debug("[obtenerXmlDocumentoDescargar] Inicio");
			lista = (List<NotaSunatBean>)request.getSession().getAttribute("ListadoNota");
			entidad = lista.get(id);
			asignarDatosNotaBeanDeNotaSunatBean(nota,entidad);
			credencial = utilFacturacion.obtenerCredenciales(request);
			obtenerNombreArchivos(credencial,nota);
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
	@RequestMapping(value = "/notas/pdf/{id}", method = RequestMethod.GET)
	public String obtenerPdfDocumento(@PathVariable Integer id, HttpServletRequest request, Model model, PageableSG pageable) {
		NotaSunatBean entidad	= null;
		List<NotaSunatBean> lista= null;		
		CredencialBean credencial 		= null;
		NotaBean nota				= new NotaBean();
		try{
			log.debug("[obtenerPdfDocumento] Inicio");
			lista = (List<NotaSunatBean>)request.getSession().getAttribute("ListadoNota");
			entidad = lista.get(id);
			asignarDatosNotaBeanDeNotaSunatBean(nota,entidad);
			credencial = utilFacturacion.obtenerCredenciales(request);
			obtenerNombreArchivos(credencial,nota);
			String token = apiOseCSV.obtenerToken(credencial);
			credencial.setAccessToken(token);
			credencial.setTicket(entidad.getNumeroTicket());
			Integer status = apiOseCSV.obtenerPDFDocumento(credencial);
			log.debug("[obtenerPdfDocumento] status: "+status);
			if (status.compareTo(200)==0) {
				TblNotaSunat comprobante = notaOseDao.findOne(entidad.getCodigoNota());
				comprobante = actualizarPdfEnNotaSunat(credencial,comprobante,request);
			}else {
				TblNotaSunat comprobante = notaOseDao.findOne(entidad.getCodigoNota());
				actualizarStatusPdfEnNotaSunat(credencial,comprobante,request);
				log.info("[obtenerPdfDocumento] Con error en PDF:"+status);
				model.addAttribute("respuesta", "Con error en PDF:"+status);
			}
			Filtro datosFiltro = (Filtro)request.getSession().getAttribute("CriterioFiltroNota");
			listarNotaAlquilerServicio(model, datosFiltro, pageable, this.urlPaginado, request);
			model.addAttribute("filtro", datosFiltro);
			model.addAttribute("page", request.getSession().getAttribute("PageNota"));
			model.addAttribute("filtro", request.getSession().getAttribute("CriterioFiltroNota"));
			log.debug("[obtenerPdfDocumento] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			entidad 	= null;			
		}
		return "caja/nota/not_listado";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/notas/pdf/descargar/{id}", method = RequestMethod.GET)
	public void obtenerPdfDocumentoDescargar(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
		NotaSunatBean entidad	= null;
		List<NotaSunatBean> lista= null;		
		CredencialBean credencial 		= null;
		NotaBean nota				= new NotaBean();
		try{
			log.debug("[obtenerPdfDocumentoDescargar] Inicio");
			lista = (List<NotaSunatBean>)request.getSession().getAttribute("ListadoNota");
			entidad = lista.get(id);
			asignarDatosNotaBeanDeNotaSunatBean(nota,entidad);
			credencial = utilFacturacion.obtenerCredenciales(request);
			obtenerNombreArchivos(credencial,nota);
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
	@RequestMapping(value = "/notas/paginado/{page}/{size}/{operacion}", method = RequestMethod.GET)
	public String paginarEntidad(@PathVariable Integer page, @PathVariable Integer size, @PathVariable String operacion, Model model,  PageableSG pageable, HttpServletRequest request) {
		Filtro filtro = null;
		String path = null;

		try{
			//log.debug("[traerRegistros] Inicio");
			path = "caja/nota/not_listado";
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
			
			filtro = (Filtro)request.getSession().getAttribute("CriterioFiltroNota");
			model.addAttribute("filtro", filtro);
			this.listarNotaAlquilerServicio(model, filtro, pageable, urlPaginado, request);
			
		}catch(Exception e){
			//log.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		return path;
	}	
}
