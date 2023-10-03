package com.pe.lima.sg.presentacion.reporte;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pe.lima.sg.bean.reporte.LocalBean;
import com.pe.lima.sg.bean.reporte.LocalSubTotalBean;
import com.pe.lima.sg.bean.reporte.LocalTotalBean;
import com.pe.lima.sg.bean.reporte.RespuestaReporteBean;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.reporte.pdf.ContratoFinalizadoExcel;
import com.pe.lima.sg.presentacion.reporte.pdf.ContratoFinalizadoPdf;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.UtilSGT;
import com.pe.lima.sg.rs.reporte.ContratoFinalizadoDao;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
public class ContratoFinalizadoAction {
	
	@Autowired
	private ContratoFinalizadoPdf contratoFinalizadoPdf;
	@Autowired
	private ContratoFinalizadoExcel contratoFinalizadoExcel;
	@Autowired
	private ServletContext context;
	@Autowired
	private ContratoFinalizadoDao contratoFinalizadoDao;

	@RequestMapping(value = "/contratoxfinalizado", method = RequestMethod.GET)
	public String mostrarFormulario(Model model, String path) {
		Filtro filtro = null;
		try{
			log.debug("[mostrarFormulario] Inicio");
			path = "reporte/finalizado/fin_listado";
			filtro = new Filtro();
			filtro.setFechaInicio(UtilSGT.getFecha("dd/MM/yyyy"));
			filtro.setFechaFin(UtilSGT.getFecha("dd/MM/yyyy"));
			model.addAttribute("filtro", filtro);
			log.debug("[mostrarFormulario] Fin");
		}catch(Exception e){
			log.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		
		return path;
	}
	
	@RequestMapping(value = "/contratoxfinalizado/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, Filtro filtro, String path, HttpServletRequest request) {
		path = "reporte/finalizado/fin_listado";
		RespuestaReporteBean respuestaReporteBean	= null;
		try{
			log.debug("[traerRegistrosFiltrados] Inicio");
			if (this.validarCriterio(filtro,model)){
				//Realiza la busqueda de morosos				
				this.buscarContratoFinalizado(model, filtro, path, request);
			}else{
				model.addAttribute("filtro", filtro);
				model.addAttribute("registros", respuestaReporteBean);
				//Datos del reporte a imprimir en el PDF
				request.getSession().setAttribute("reporteContratoFinalizado", null);
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
	@RequestMapping(value = "/contratoxfinalizado/excel", method = RequestMethod.GET)
	public void mostraExcel(Model model, String path, HttpServletRequest request, HttpServletResponse response) {
		List<LocalBean> entidades 				= null;
		Filtro criterio							= null;
		List<LocalTotalBean> listaDataReporte 	= null;
		try{
			path = "reporte/finalizado/fin_listado";
			log.debug("[mostraExcel] Inicio");
			entidades = (List<LocalBean>)request.getSession().getAttribute("reporteContratoFinalizado");
			if (entidades != null && !entidades.isEmpty()){
				//Preparando la data para ser mostrada (ingresos y gastos)
				listaDataReporte = this.preparaDataLocalxInmueble(entidades);
				criterio = (Filtro)request.getSession().getAttribute("criterioContratoFinalizado");
				
				//obtenerNombreConcepto(criterio);
				String filename = "ContratoFinalizado"+UtilSGT.getFecha("yyyy-MM-dd kk:mm:ss").replace(':', '-');
				contratoFinalizadoExcel.reporteContratoFinalizado(listaDataReporte, criterio, context, request, response, filename);
				
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
	@RequestMapping(value = "/contratoxfinalizado/pdf", method = RequestMethod.GET)
	public void mostrarPdf(Model model, String path, HttpServletRequest request, HttpServletResponse response) {
		List<LocalBean> entidades 				= null;
		Filtro criterio							= null;
		List<LocalTotalBean> listaDataReporte 	= null;
		try{
			path = "reporte/finalizado/fin_listado";
			log.debug("[mostrarPdf] Inicio");
			entidades = (List<LocalBean>)request.getSession().getAttribute("reporteContratoFinalizado");
			if (entidades != null && !entidades.isEmpty()){
				//Preparando la data para ser mostrada (ingresos y gastos)
				listaDataReporte = this.preparaDataLocalxInmueble(entidades);
				criterio = (Filtro)request.getSession().getAttribute("criterioContratoFinalizado");
				
				//obtenerNombreConcepto(criterio);
				String filename = "ContratoFinalizado"+UtilSGT.getFecha("yyyy-MM-dd kk:mm:ss").replace(':', '-');
				contratoFinalizadoPdf.reporteContratoFinalizado(listaDataReporte, criterio, context, request, response, filename);
				
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
	public boolean validarCriterio(Filtro filtro,Model model){
		boolean resultado = true;
		try{
			log.debug("[validarCriterio] Inicio");
			//Validando la fecha de Inicio
			if (filtro.getFechaInicio() == null || filtro.getFechaInicio().length()< 8){
				resultado = false;
				model.addAttribute("respuesta", "Debe ingresar la fecha de Inicio del reporte");
			}
			//Validando la fecha de Fin
			if (filtro.getFechaFin()== null || filtro.getFechaFin().length()< 8){
				resultado = false;
				model.addAttribute("respuesta", "Debe ingresar la fecha Fin del reporte");
			}
			log.debug("[validarCriterio] Fin");
		}catch(Exception e){
			log.debug("[validarCriterio] Error:"+e.getMessage());
			e.printStackTrace();
		}
		
		return resultado;
	}
	/*Reporte Contratos Finalizados*/
	private void buscarContratoFinalizado(Model model, Filtro filtro, String path, HttpServletRequest request){
		List<LocalBean> listaLocal 				= null;
		List<RespuestaReporteBean> listaRespuesta	= null;
		RespuestaReporteBean respuestaReporteBean	= null;
		log.debug("[buscarContratoFinalizado] Inicio");
		listaLocal = this.listarContratoFinalizado(filtro);
		if (listaLocal != null && listaLocal.size()>0){
			listaRespuesta = new ArrayList<RespuestaReporteBean>();
			respuestaReporteBean = new RespuestaReporteBean();
			respuestaReporteBean.setDescripcion(Constantes.RESPUESTA_BUSQUEDA_EXITOSA);
			respuestaReporteBean.setTotalRegistro(listaLocal.size());
			listaRespuesta.add(respuestaReporteBean);
			model.addAttribute("registros", listaRespuesta);
			request.getSession().setAttribute("reporteContratoFinalizado", listaLocal);
			request.getSession().setAttribute("criterioContratoFinalizado", filtro);
			log.debug("[buscarContratoFinalizado] listaRespuesta:"+listaRespuesta.size());
		}else{
			model.addAttribute("registros", respuestaReporteBean);
			request.getSession().setAttribute("reporteContratoFinalizado", listaLocal);
			log.debug("[buscarContratoFinalizado] No se encontro registros");
		}
		model.addAttribute("filtro", filtro);
		log.debug("[buscarContratoFinalizado] Fin");
	}
	
	/*Listado de los contrato finalizados*/
	private List<LocalBean> listarContratoFinalizado(Filtro filtro){
		List<LocalBean> entidades = new ArrayList<LocalBean>();
		try{
			log.debug("[listarContratoFinalizado] Inicio");
			log.debug("[listarContratoFinalizado] Fecha Inicio:"+filtro.getFechaInicio());
			log.debug("[listarContratoFinalizado] Fecha Fin:"+filtro.getFechaFin());
			entidades = contratoFinalizadoDao.getReporteContratoFinalizado(filtro);
			log.debug("[listarContratoFinalizado] entidades:"+entidades.size());
			log.debug("[listarContratoFinalizado] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return entidades;
	}
	

	/*Prepara la data para la impresion en el PDF*/
	private List<LocalTotalBean> preparaDataLocalxInmueble(List<LocalBean> entidades){
		List<LocalTotalBean> listaDataReporte 		= null;
		LocalTotalBean localTotalBean 				= new LocalTotalBean();
		Map<String, LocalSubTotalBean> mapLocal		= new HashMap<String, LocalSubTotalBean>();
		log.debug("[preparaDataLocalxInmueble] Inicio");
		
		
		//Agrupamos por ListaDeuda y SubTotal
		for(LocalBean local: entidades){
			LocalSubTotalBean localSubTotalBean = mapLocal.get(local.getEstado());
			if (localSubTotalBean == null){
				this.setDataEstado(mapLocal, local);
			}else{
				this.addDataEstado(localSubTotalBean, local);
			}
		}
		//Totalizamos
		for (Map.Entry<String, LocalSubTotalBean> entry : mapLocal.entrySet()) {
		   LocalSubTotalBean localSubTotalBean = entry.getValue();
		   localTotalBean.setTotalAlquiler(localTotalBean.getTotalAlquiler().add(localSubTotalBean.getSubTotalAlquiler()));
		   localTotalBean.setTotalServicio(localTotalBean.getTotalServicio().add(localSubTotalBean.getSubTotalServicio()));
		   //Adicion de datos en el reporte - 2023.02.22
		   localTotalBean.setTotalGarantia(localTotalBean.getTotalGarantia().add(localSubTotalBean.getSubTotalGarantia()));
		   localTotalBean.getListaLocalSubTotal().add(localSubTotalBean);
		}
		listaDataReporte = new ArrayList<LocalTotalBean>();
		listaDataReporte.add(localTotalBean);
		log.debug("[preparaDataLocalxInmueble] Fin");
		return listaDataReporte;
	}
	/*Adiciona un elemento*/
	private void setDataEstado(Map<String, LocalSubTotalBean> mapLocal, LocalBean localBean){
		LocalSubTotalBean localSubTotalBean = new LocalSubTotalBean();
		localSubTotalBean.setNumeroTienda(localBean.getNumeroTienda());
		localSubTotalBean.setSubTotalAlquiler(localSubTotalBean.getSubTotalAlquiler().add(localBean.getMontoAlquiler()));
		localSubTotalBean.setSubTotalServicio(localSubTotalBean.getSubTotalServicio().add(localBean.getMontoServicio()));
		//Adicion de datos en el reporte - 2023.02.22
		localSubTotalBean.setSubTotalGarantia(localSubTotalBean.getSubTotalGarantia().add(localBean.getMontoGarantia()));
		localSubTotalBean.getListaLocal().add(localBean);
		localSubTotalBean.setEstado(localBean.getEstado());
		localSubTotalBean.setOrden(localBean.getOrden());
		mapLocal.put(localBean.getEstado(), localSubTotalBean);
	}
	/*Adiciona un elemento*/
	private void addDataEstado(LocalSubTotalBean localSubTotalBean, LocalBean localBean){
		localSubTotalBean.setSubTotalAlquiler(localSubTotalBean.getSubTotalAlquiler().add(localBean.getMontoAlquiler()));
		localSubTotalBean.setSubTotalServicio(localSubTotalBean.getSubTotalServicio().add(localBean.getMontoServicio()));
		//Adicion de datos en el reporte - 2023.02.22
		localSubTotalBean.setSubTotalGarantia(localSubTotalBean.getSubTotalGarantia().add(localBean.getMontoGarantia()));
		localSubTotalBean.getListaLocal().add(localBean);
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
