package com.pe.lima.sg.presentacion.reporte.factura.emitida;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pe.lima.sg.bean.caja.ComprobanteSunatBean;
import com.pe.lima.sg.bean.caja.MasivoSunatBean;
import com.pe.lima.sg.bean.reporte.RespuestaReporteBean;
import com.pe.lima.sg.presentacion.reporte.factura.emitida.dao.MasivoFacturaDao;
import com.pe.lima.sg.presentacion.reporte.factura.emitida.lib.MasivoFacturaCpeExcel;
import com.pe.lima.sg.presentacion.reporte.factura.emitida.lib.MasivoFacturaCpePdf;
import com.pe.lima.sg.presentacion.reporte.factura.emitida.lib.MasivoFacturaExcel;
import com.pe.lima.sg.presentacion.reporte.factura.emitida.lib.MasivoFacturaPdf;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.UtilSGT;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
public class MasivoFacturaAction {
	
	@Autowired
	private MasivoFacturaPdf masivoFacturaPdf;
	
	@Autowired
	private MasivoFacturaCpePdf masivoFacturaCpePdf;
	
	
	@Autowired
	private MasivoFacturaExcel masivoFacturaExcel;
	
	@Autowired
	private MasivoFacturaCpeExcel masivoFacturaCpeExcel;
	
	@Autowired
	private ServletContext context;
	@Autowired
	private MasivoFacturaDao masivoFacturaDao;

	@RequestMapping(value = "/masivoFactura/emitida", method = RequestMethod.GET)
	public String mostrarFormulario(Model model, String path) {
		MasivoSunatBean masivoSunatBean = null;
		try{
			log.debug("[mostrarFormulario] Inicio");
			path = "reporte/masivo/emitido/emi_listado";
			masivoSunatBean = new MasivoSunatBean();
			masivoSunatBean.setCodigoEdificio(Constantes.CODIGO_INMUEBLE_LA_REYNA);
			//Periodo			
			String strMes = UtilSGT.getMesDateFormateado(new Date()); //01 al 12
			Integer intAnio = UtilSGT.getAnioDate(new Date());
			String nombreMes = UtilSGT.getMesPersonalizado(new Integer(strMes));
			
			masivoSunatBean.setAnio(intAnio);
			masivoSunatBean.setMes(strMes);
			masivoSunatBean.setPeriodo(intAnio+"-"+nombreMes);
			masivoSunatBean.setTipoReporte(Constantes.TIPO_SISTEMA_CONTABLE);
			model.addAttribute("masivoSunatBean", masivoSunatBean);
			log.debug("[mostrarFormulario] Fin");
		}catch(Exception e){
			log.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			masivoSunatBean = null;
		}
		
		return path;
	}
	
	@RequestMapping(value = "/masivoFactura/emitida/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, MasivoSunatBean masivoSunatBean, String path, HttpServletRequest request) {
		path = "reporte/masivo/emitido/emi_listado";
		RespuestaReporteBean respuestaReporteBean	= null;
		try{
			log.debug("[traerRegistrosFiltrados] Inicio");
			if (this.validarCriterio(masivoSunatBean,model)){
				//Realiza la busqueda de morosos				
				this.buscarMasivoFacturaEmitidos(model, masivoSunatBean, path, request);
			}else{
				model.addAttribute("masivoSunatBean", masivoSunatBean);
				model.addAttribute("registros", respuestaReporteBean);
				//Datos del reporte a imprimir en el PDF
				request.getSession().setAttribute("reporteMasivoFacturaEmitidos", null);
			}
		}catch(Exception e){
			log.debug("[traerRegistrosFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			respuestaReporteBean	= null;
		}
		log.debug("[traerRegistrosFiltrados] Fin");
		return path;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/masivoFactura/emitida/excel", method = RequestMethod.GET)
	public void mostraExcel(Model model, String path, HttpServletRequest request, HttpServletResponse response) {
		List<ComprobanteSunatBean> listaMasivoFactura 	= null;
		MasivoSunatBean masivoSunatBean					= null;
		String filename 								= null;
		try{
			path = "reporte/masivo/emitido/emi_listado";
			log.debug("[mostraExcel] Inicio");
			listaMasivoFactura = (List<ComprobanteSunatBean>) request.getSession().getAttribute("reporteMasivoFacturaEmitidos");
			if (listaMasivoFactura != null && !listaMasivoFactura.isEmpty()){
				masivoSunatBean = (MasivoSunatBean)request.getSession().getAttribute("criterioMasivoFactura");
				if (!masivoSunatBean.getTipoReporte().equals(Constantes.TIPO_SISTEMA_CONTABLE)) {
					filename = "Reporte_Ventas_Interno_"+UtilSGT.getFecha("yyyy-MM-dd kk:mm:ss").replace(':', '-');
					masivoFacturaExcel.reporteMasivoFacturaEmitidos(listaMasivoFactura, masivoSunatBean, context, request, response, filename);
				} else {
					filename = "Reporte_Ventas_Contable_"+UtilSGT.getFecha("yyyy-MM-dd kk:mm:ss").replace(':', '-');
					masivoFacturaCpeExcel.reporteFacturaElectronicaEmitidos(listaMasivoFactura, masivoSunatBean, context, request, response, filename);
					
				}
				String fullPath = request.getServletContext().getRealPath("/resources/reports/"+filename+".xlsx");
				this.fileDownload(fullPath, response, filename, ".xlsx");
			}
			
			log.debug("[mostraExcel] Fin");
		}catch(Exception e){
			log.debug("[mostraExcel] Error:"+e.getMessage());
			e.printStackTrace();
		}
		
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/masivoFactura/emitida/pdf", method = RequestMethod.GET)
	public void mostrarPdf(Model model, String path, HttpServletRequest request, HttpServletResponse response) {
		List<ComprobanteSunatBean> listaMasivoFactura 	= null;
		MasivoSunatBean masivoSunatBean					= null;
		String filename 								= null;
		try{
			path = "reporte/masivo/emitido/emi_listado";
			log.debug("[mostrarPdf] Inicio");
			listaMasivoFactura = (List<ComprobanteSunatBean>) request.getSession().getAttribute("reporteMasivoFacturaEmitidos");
			if (listaMasivoFactura != null && !listaMasivoFactura.isEmpty()){
				masivoSunatBean = (MasivoSunatBean)request.getSession().getAttribute("criterioMasivoFactura");
				if (!masivoSunatBean.getTipoReporte().equals(Constantes.TIPO_SISTEMA_CONTABLE)) {
					filename = "Reporte_Ventas_Interno_"+UtilSGT.getFecha("yyyy-MM-dd kk:mm:ss").replace(':', '-');
					masivoFacturaPdf.reporteMasivoFacturaEmitidos(listaMasivoFactura, masivoSunatBean, context, request, response, filename);
				} else {
					filename = "Reporte_Ventas_Contable_"+UtilSGT.getFecha("yyyy-MM-dd kk:mm:ss").replace(':', '-');
					masivoFacturaCpePdf.reporteFacturaElectronicaEmitidos(listaMasivoFactura, masivoSunatBean, context, request, response, filename);
					
				}
				String fullPath = request.getServletContext().getRealPath("/resources/reports/"+filename+".pdf");
				this.fileDownload(fullPath, response, filename, ".pdf");
			}
			
			
			log.debug("[mostrarPdf] Fin");
		}catch(Exception e){
			log.debug("[mostrarPdf] Error:"+e.getMessage());
			e.printStackTrace();
		}
		
	}
	/*Validacion de los filtros de busqueda*/
	public boolean validarCriterio(MasivoSunatBean masivoSunatBean,Model model){
		boolean resultado = true;
		try{
			log.debug("[validarCriterio] Inicio");
			//Edificación
			if (masivoSunatBean.getCodigoEdificio() == -1){
				resultado = false;
				model.addAttribute("respuesta", "Debe seleccionar el Inmueble");
			}
			
			//Año
			if (masivoSunatBean.getAnio() == null || masivoSunatBean.getAnio()< 2000){
				resultado = false;
				model.addAttribute("respuesta", "Debe ingresar un año valido");
			}
			//Validando la fecha
			if (masivoSunatBean.getMes() == null || masivoSunatBean.getMes().equals("-1")){
				resultado = false;
				model.addAttribute("respuesta", "Debe ingresar un mes valido");
			}
			//Tipo de Reporte
			if (masivoSunatBean.getTipoReporte() == null || masivoSunatBean.getTipoReporte().equals("-1")){
				resultado = false;
				model.addAttribute("respuesta", "Debe seleccionar un tipo de reporte");
			}
			log.debug("[validarCriterio] Fin");
		}catch(Exception e){
			log.debug("[validarCriterio] Error:"+e.getMessage());
			e.printStackTrace();
		}
		
		return resultado;
	}
	/*Reporte Emitidos*/
	private void buscarMasivoFacturaEmitidos(Model model, 
											 MasivoSunatBean masivoSunatBean, 
											 String path, 
											 HttpServletRequest request){
		List<ComprobanteSunatBean> listaMasivoFactura 		= null;
		List<RespuestaReporteBean> listaRespuesta			= null;
		RespuestaReporteBean respuestaReporteBean			= null;
		log.debug("[buscarMasivoFacturaEmitidos] Inicio");
		listaMasivoFactura = this.listarMasivoFacturaEmitidos(masivoSunatBean);
		if (listaMasivoFactura != null && listaMasivoFactura.size()>0){
			listaRespuesta = new ArrayList<RespuestaReporteBean>();
			respuestaReporteBean = new RespuestaReporteBean();
			respuestaReporteBean.setDescripcion(Constantes.RESPUESTA_BUSQUEDA_EXITOSA + " [" + masivoSunatBean.getTipoReporte()+"]");
			respuestaReporteBean.setTotalRegistro(listaMasivoFactura.size());
			listaRespuesta.add(respuestaReporteBean);
			model.addAttribute("registros", listaRespuesta);
			request.getSession().setAttribute("reporteMasivoFacturaEmitidos", listaMasivoFactura);
			request.getSession().setAttribute("criterioMasivoFactura", masivoSunatBean);
			log.debug("[buscarMasivoFacturaEmitidos] listaRespuesta:"+listaRespuesta.size());
		}else{
			model.addAttribute("registros", respuestaReporteBean);
			request.getSession().setAttribute("reporteMasivoFacturaEmitidos", listaMasivoFactura);
			log.debug("[buscarMasivoFacturaEmitidos] No se encontro registros");
		}
		model.addAttribute("masivoSunatBean", masivoSunatBean);
		log.debug("[buscarMasivoFacturaEmitidos] Fin");
	}
	
	/*Listado de los masivoFacturaes emitidos*/
	private List<ComprobanteSunatBean> listarMasivoFacturaEmitidos(MasivoSunatBean masivoSunatBean){
		List<ComprobanteSunatBean> entidades = new ArrayList<ComprobanteSunatBean>();
		try{
			log.debug("[listarMoroso] Inicio");
			log.debug("[listarMoroso] Anio:"+masivoSunatBean.getAnio());
			log.debug("[listarMoroso] Mes:"+masivoSunatBean.getMes());
			entidades = masivoFacturaDao.getReporteFacturaEmitido(masivoSunatBean);
			log.debug("[listarIngresoEgreso] entidades:"+entidades.size());
			log.debug("[listarMoroso] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return entidades;
	}
	
	
	/*Descarga el PDF*/
	private void fileDownload(String fullPath, HttpServletResponse response, String filename, String extension){
		File file = new File(fullPath);
		final int BUFFER_ZISE = 4096;
		if (file.exists()){
			try{
				FileInputStream inputStream = new FileInputStream(file);
				String mimeType = context.getMimeType(fullPath);
				response.setContentType(mimeType);
				response.setHeader("content-disposition", "attachment; filename="+filename+extension);
				OutputStream outputStream = response.getOutputStream();
				byte[] buffer = new byte[BUFFER_ZISE];
				int bytesRead = -1;
				while((bytesRead = inputStream.read(buffer))!=-1){
					outputStream.write(buffer, 0, bytesRead);
				}
				inputStream.close();
				outputStream.close();
				file.delete();
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}
}
