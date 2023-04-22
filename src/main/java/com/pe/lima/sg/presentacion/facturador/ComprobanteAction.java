package com.pe.lima.sg.presentacion.facturador;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pe.lima.sg.bean.facturador.DocumentoBean;
import com.pe.lima.sg.dao.caja.ICxCDocumentoDAO;
import com.pe.lima.sg.entity.caja.TblCxcDocumento;
import com.pe.lima.sg.facturador.bean.AuditoriaBean;
import com.pe.lima.sg.facturador.bean.ComprobanteBean;
import com.pe.lima.sg.facturador.bean.ErrorBean;
import com.pe.lima.sg.facturador.bean.ValidacionBean;
import com.pe.lima.sg.facturador.entity.TblComprobante;
import com.pe.lima.sg.facturador.entity.TblDetalleComprobante;
import com.pe.lima.sg.facturador.entity.TblEmpresa;
import com.pe.lima.sg.facturador.entity.TblSerie;
import com.pe.lima.sg.facturador.service.IComprobanteSFS12;
import com.pe.lima.sg.facturador.service.OperacionUtil;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.UtilSGT;
import com.pe.lima.sg.rs.facturador.ComprobanteDao;

@Controller
public class ComprobanteAction {
	private static final Logger logger = LogManager.getLogger(ComprobanteAction.class);
	@Autowired
	private ComprobanteDao comprobanteDao;

	@Autowired
	private IComprobanteSFS12 comprobanteService;
	
	/*@Autowired
	private ISerieSFS12DAO serieDao;*/
	
	@Autowired
	private ICxCDocumentoDAO cxcDocumentoDao;

	@RequestMapping(value = "/comprobante", method = RequestMethod.GET)
	public String mostrarFormulario(Model model, String path, HttpServletRequest request) {
		List<DocumentoBean> listaComprobante	= null;
		try{
			logger.debug("[mostrarFormulario] Inicio");
			path = "facturador/fac_listado";
			listaComprobante = this.listarComprobante();
			model.addAttribute("registros", listaComprobante);
			request.getSession().setAttribute("listaComprobanteSUNAT", listaComprobante);
			logger.debug("[mostrarFormulario] Fin");
		}catch(Exception e){
			logger.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}

		return path;
	}

	private List<DocumentoBean> listarComprobante(){
		List<DocumentoBean> entidades = new ArrayList<>();
		try{
			entidades = comprobanteDao.getDocumentoSinComprobante();
			logger.debug("[listarComprobante] entidades:"+entidades);
		}catch(Exception e){
			e.printStackTrace();
		}
		return entidades;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/comprobante/generar", method = RequestMethod.POST)
	public String procesarComprobante(Model model, String path, HttpServletRequest request) {
		path = "facturador/fac_listado";
		List<DocumentoBean> listaDatos			= null;
		List<ComprobanteBean> listaComprobante	= null;
		TblEmpresa empresa						= null;
		List<ValidacionBean> listaValidacion	= null;
		AuditoriaBean auditoriaBean				= new AuditoriaBean();
		List<ErrorBean> listaError				= null;
		TblSerie serieFactura					= null;
		TblSerie serieBoleta					= null;
		try{
			logger.debug("[procesarComprobante] Inicio");
			serieFactura = new TblSerie();//serieDao.buscarOneByTipoComprobante(Constantes.TIPO_COMPROBANTE_FACTURA);
			serieBoleta = new TblSerie();//serieDao.buscarOneByTipoComprobante(Constantes.TIPO_COMPROBANTE_BOLETA);
			listaDatos = (List<DocumentoBean>) request.getSession().getAttribute("listaComprobanteSUNAT");
			auditoriaBean.setCodigoUsuario(UtilSGT.mGetUsuario(request));
			auditoriaBean.setIpCreacion(request.getRemoteAddr());
			listaComprobante = this.obtenerComprobantes(listaDatos, serieFactura, serieBoleta, auditoriaBean);
			listaValidacion = new ArrayList<>();
			empresa = new TblEmpresa();
			empresa.setCodigoEntidad(1);//TODO: CAMBIAR EL CODIGO DE LA EMPRESA
			boolean validacionOK = comprobanteService.validarComprobante(listaComprobante, listaValidacion, empresa);
			//if (validacionOK){
				
				validacionOK = comprobanteService.calcularAsignarDatosComprobante(listaComprobante, auditoriaBean);
				if (validacionOK){
					listaError = new ArrayList<>();
					
					comprobanteService.registrarComprobante(listaComprobante, listaError, empresa, auditoriaBean);
					//Actualizar CxC
					this.actualizarCuentaxCobrar(listaComprobante, request);
				}
				
			//}32480605
				List<DocumentoBean> lista = this.listarComprobante();
				model.addAttribute("registros", lista);
				request.getSession().setAttribute("listaComprobanteSUNAT", lista);
			logger.debug("[procesarComprobante] Fin");
		}catch(Exception e){
			logger.debug("[procesarComprobante] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{

		}
		logger.debug("[procesarComprobante] Fin");
		return path;
	}
	
	public void actualizarCuentaxCobrar(List<ComprobanteBean> listaComprobante, HttpServletRequest request){
		TblCxcDocumento documento = null;
		for(ComprobanteBean comprobante: listaComprobante){
			if (comprobante.isResultadoValidacion()){
				documento = cxcDocumentoDao.findOne(comprobante.getCodigoCxC());
				documento.setSerieComprobante(comprobante.getComprobante().getSerie());
				documento.setNumeroComprobante(comprobante.getComprobante().getNumero());
				documento.setFechaModificacion(new Date());
				documento.setIpModificacion(request.getRemoteAddr());
				documento.setCodigoComprobante(comprobante.getComprobante().getCodigoComprobante());
				cxcDocumentoDao.save(documento);
			}
		}
		
	}

	public List<ComprobanteBean> obtenerComprobantes(List<DocumentoBean> listaDatos, TblSerie serieFactura, TblSerie serieBoleta, AuditoriaBean auditoriaBean	){
		List<ComprobanteBean> listaComprobante	= null;
		ComprobanteBean entidad 				= null;
		List<TblDetalleComprobante> listaDetalle= null;		
		
		
		if (listaDatos !=null){
			listaComprobante = new ArrayList<>();
			//Obtenemos Prefijo, Secuencia y Numero para generar los siguiente
			
			for(DocumentoBean documento: listaDatos){
				entidad = new ComprobanteBean();
				entidad.setComprobante(new TblComprobante());
				listaDetalle = new ArrayList<>();
				entidad.setListaDetalle(listaDetalle);
				//Seteamos el comprobante
				entidad.setComprobante(this.setDatosComprobante(documento, serieFactura, serieBoleta));
				//Seteamos el detalle del comprobante
				listaDetalle.add(this.setDatosDetalleComprobante(documento));
				//Datos adicionales
				entidad.setTipoAfectacion(Constantes.SUNAT_AFECTACION_IGV_GRAVADO_OPE_ONEROSO);
				entidad.setValorIGV(Constantes.SUNAT_IGV);
				entidad.setValorServicio(0);
				entidad.setSunatData("G:\\data0\\gamarra\\DATA\\");
				entidad.setCodigoCxC(documento.getCodigoCxC());
				listaComprobante.add(entidad);
				OperacionUtil.asignarAuditoria(listaComprobante, auditoriaBean);
			}
			
		}
		return listaComprobante;
	}

	/*Se asigna los datos del comprobante*/
	public TblComprobante setDatosComprobante(DocumentoBean documento, TblSerie serieFactura, TblSerie serieBoleta){
		TblComprobante comprobante = new TblComprobante();
		comprobante.setTipoOperacion(Constantes.SUNAT_TIPO_OPERACION_VENTA_INTERNA); 
		comprobante.setFechaEmision(UtilSGT.getFecha("yyyy-MM-dd"));
		comprobante.setHoraEmision(UtilSGT.getHora());
		comprobante.setFechaVencimiento(UtilSGT.getFecha("yyyy-MM-dd"));
		comprobante.setCodigoDomicilio(Constantes.SUNAT_CODIGO_DOMICILIO_FISCAL_DEFAULT);
		if (documento.getTipoPersona().equals(Constantes.PERSONA_NATURAL)){
			comprobante.setTipoDocumento(Constantes.SUNAT_TIPO_DOCUMENTO_DNI);
			comprobante.setTipoComprobante(Constantes.TIPO_COMPROBANTE_BOLETA);
			comprobante.setSerie(Constantes.SUNAT_SERIE_BOLETA);
			//valores temporales, en la grabación se vuelve a asignar
			//UtilSGT.obtenerSiguienteComprobante(serieBoleta);
			//comprobante.setSerie(serieBoleta.getPrefijoSerie());
			//comprobante.setNumero(UtilSGT.completarCeros(serieBoleta.getSecuencialSerie().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			comprobante.setSerie("0000");
			comprobante.setNumero("00000000");
			
		}else{
			comprobante.setTipoDocumento(Constantes.SUNAT_TIPO_DOCUMENTO_RUC);
			comprobante.setTipoComprobante(Constantes.TIPO_COMPROBANTE_FACTURA);
			comprobante.setSerie(Constantes.SUNAT_SERIE_FACTURA);
			//valores temporales, en la grabación se vuelve a asignar
			//UtilSGT.obtenerSiguienteComprobante(serieFactura);
			//comprobante.setSerie(serieFactura.getPrefijoSerie());
			//comprobante.setNumero(UtilSGT.completarCeros(serieFactura.getSecuencialSerie().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
			comprobante.setSerie("0000");
			comprobante.setNumero("00000000");
		}
		
		comprobante.setNumeroDocumento(documento.getNumeroDocumento());
		comprobante.setNombreCliente(documento.getNombreCliente());
		comprobante.setMoneda(Constantes.SUNAT_TIPO_MONEDA_DOLAR);
		comprobante.setVersionUbl(Constantes.SUNAT_VERSION_UBL);
		comprobante.setCustomizacionDoc(Constantes.SUNAT_CUSTOMIZACION);
		comprobante.setCodigoVerificacion(UUID.randomUUID().toString());

		comprobante.setSumTributo(new BigDecimal("0"));
		comprobante.setTotValorVenta(new BigDecimal("0"));
		comprobante.setTotPrecioVenta(new BigDecimal("0"));
		comprobante.setTotDescuento(new BigDecimal("0"));
		comprobante.setSumOtrosCargos(new BigDecimal("0"));
		comprobante.setTotAnticipos(new BigDecimal("0"));
		comprobante.setImpTotalVenta(new BigDecimal("0"));


		return comprobante;
	}
	
	public TblDetalleComprobante setDatosDetalleComprobante(DocumentoBean documento){
		TblDetalleComprobante detalle = new TblDetalleComprobante();
		
		detalle.setUnidadMedida(Constantes.SUNAT_UNIDAD_MEDIDA);
		detalle.setCantidad(new BigDecimal("1"));
		detalle.setCodigoProducto(Constantes.SUNAT_CODIGO_PRODUCTO_DEFAULT);
		detalle.setCodigoProductoSunat("-");
		detalle.setDescripcion(documento.getDescripcion() + " CORRESPONDE AL PERIODO " + UtilSGT.formatFechaSGT(documento.getFechaFin()));
		detalle.setPrecioUnitario(documento.getMontoCobrado());
		detalle.setValorUnitario(documento.getMontoCobrado());
		detalle.setDescuento(new BigDecimal("0"));
		detalle.setValorReferencialUnitario(new BigDecimal("0"));
		detalle.setTipoAfectacion(Constantes.SUNAT_AFECTACION_IGV_GRAVADO_OPE_ONEROSO);
		this.calculoDetalleComprobante(detalle);
		
		return detalle;
	}
	
	/*
	 * Calculo del Detalle 
	 */
	public void calculoDetalleComprobante(TblDetalleComprobante detalle){
		try{
			detalle.setPrecioTotal(detalle.getPrecioUnitario().multiply(detalle.getCantidad()));
			
			if (detalle.getDescuento()!=null && detalle.getDescuento().doubleValue()>0){
				detalle.setPrecioFinal(UtilSGT.getCalculoDescuento(detalle.getPrecioTotal(), detalle.getDescuento(), 4));
				
			}else{
				detalle.setPrecioFinal(detalle.getPrecioTotal());
			}
			//Luego de la operacion se redondea a 2 decimales
			detalle.setPrecioTotal(UtilSGT.getRoundDecimal(detalle.getPrecioTotal(), 2));
			detalle.setPrecioFinal(UtilSGT.getRoundDecimal(detalle.getPrecioFinal(), 2));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
