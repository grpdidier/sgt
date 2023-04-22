package com.pe.lima.sg.presentacion.reporte;

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

import com.pe.lima.sg.bean.reporte.CriterioReporteBean;
import com.pe.lima.sg.bean.reporte.MorososAlquilerBean;
import com.pe.lima.sg.bean.reporte.RespuestaReporteBean;
import com.pe.lima.sg.presentacion.reporte.excel.MorosoAlquilerExcel;
import com.pe.lima.sg.presentacion.reporte.pdf.MorosoAlquilerPdf;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.UtilSGT;
import com.pe.lima.sg.rs.reporte.MorosoAlquilerDao;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
public class MorosoAlquilerAction {
	@Autowired
	private MorosoAlquilerExcel morosoAlquilerExcel;
	@Autowired
	private MorosoAlquilerPdf morosoAlquilerPdf;
	@Autowired
	private ServletContext context;
	@Autowired
	private MorosoAlquilerDao morosoAlquilerDao;

	@RequestMapping(value = "/morosoalquiler", method = RequestMethod.GET)
	public String mostrarFormulario(Model model, String path) {
		CriterioReporteBean filtro = null;
		try{
			log.debug("[mostrarFormulario] Inicio");
			path = "reporte/moroso/alq_listado";
			filtro = new CriterioReporteBean();
			filtro.setFechaInicio(UtilSGT.getFecha("dd/MM/yyyy"));
			filtro.setFechaFin(UtilSGT.getFecha("dd/MM/yyyy"));
			model.addAttribute("filtro", filtro);
			//ingresoEgresoDao = new IngresoEgresoDao();
			//ingresoEgresoDao.getreporteMorosoAlquiler();
			log.debug("[mostrarFormulario] Fin");
		}catch(Exception e){
			log.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		
		return path;
	}
	@RequestMapping(value = "/morosoalquiler/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, CriterioReporteBean filtro, String path, HttpServletRequest request) {
		path = "reporte/moroso/alq_listado";
		List<MorososAlquilerBean> listaMorosoAlquiler 	= null;
		List<RespuestaReporteBean> listaRespuesta	= null;
		RespuestaReporteBean respuestaReporteBean	= null;
		try{
			log.debug("[traerRegistrosFiltrados] Inicio");
			listaMorosoAlquiler = this.listarMorosoAlquiler(filtro);
			if (listaMorosoAlquiler != null){
				listaRespuesta = new ArrayList<RespuestaReporteBean>();
				respuestaReporteBean = new RespuestaReporteBean();
				respuestaReporteBean.setDescripcion(Constantes.RESPUESTA_BUSQUEDA_EXITOSA);
				respuestaReporteBean.setTotalRegistro(listaMorosoAlquiler.size());
				listaRespuesta.add(respuestaReporteBean);
				model.addAttribute("registros", listaRespuesta);
				request.getSession().setAttribute("reporteMorosoAlquiler", listaMorosoAlquiler);
				request.getSession().setAttribute("criterioIngresoEgreso", filtro);
				log.debug("[traerRegistrosFiltrados] listaRespuesta:"+listaRespuesta.size());
			}else{
				model.addAttribute("registros", respuestaReporteBean);
				request.getSession().setAttribute("reporteMorosoAlquiler", listaMorosoAlquiler);
			}
			model.addAttribute("filtro", filtro);
			
		}catch(Exception e){
			log.debug("[traerRegistrosFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			listaMorosoAlquiler		= null;
			listaRespuesta			= null;
			respuestaReporteBean	= null;
		}
		log.debug("[traerRegistrosFiltrados] Fin");
		return path;
	}
	
	private List<MorososAlquilerBean> listarMorosoAlquiler(CriterioReporteBean filtro){
		List<MorososAlquilerBean> entidades = new ArrayList<MorososAlquilerBean>();
		try{
			
			if (filtro.getFechaFin()==null || filtro.getFechaFin().equals("")){
				filtro.setFechaFin(UtilSGT.getDateStringFormat(UtilSGT.addDays(new Date(), 1)));
			}
			log.debug("[listarIngresoEgreso] Fec Fin:"+filtro.getFechaFin());
			entidades = morosoAlquilerDao.getReporteMorosoAlquiler(filtro.getFechaFin());
			log.debug("[listarIngresoEgreso] entidades:"+entidades);
		}catch(Exception e){
			e.printStackTrace();
		}
		return entidades;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/morosoalquiler/excel", method = RequestMethod.GET)
	public void mostrarXls(Model model, String path, HttpServletRequest request, HttpServletResponse response) {
		List<MorososAlquilerBean> entidades 	= null;
		CriterioReporteBean criterio		= null;
		path = "reporte/moroso/alq_listado";
		try{
			log.debug("[mostrarXls] Inicio");
			entidades = (List<MorososAlquilerBean>)request.getSession().getAttribute("reporteMorosoAlquiler");
			if (entidades != null && !entidades.isEmpty()){
				criterio = (CriterioReporteBean)request.getSession().getAttribute("criterioIngresoEgreso");
				String filename = "MorosoAlquiler";
				morosoAlquilerExcel.reporteMorosoAlquiler(entidades, criterio, context, request, response, filename);
				String fullPath = request.getServletContext().getRealPath("/resources/reports/"+filename+".xls");
				this.fileDownload(fullPath, response, filename, ".xls");
			}
			
			
			log.debug("[mostrarXls] Fin");
		}catch(Exception e){
			log.debug("[mostrarXls] Error:"+e.getMessage());
			e.printStackTrace();
		}
		
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/morosoalquiler/pdf", method = RequestMethod.GET)
	public void mostrarPdf(Model model, String path, HttpServletRequest request, HttpServletResponse response) {
		List<MorososAlquilerBean> entidades 	= null;
		CriterioReporteBean criterio		= null;
		path = "reporte/moroso/alq_listado";
		try{
			log.debug("[mostrarPdf] Inicio");
			entidades = (List<MorososAlquilerBean>)request.getSession().getAttribute("reporteMorosoAlquiler");
			if (entidades != null && !entidades.isEmpty()){
				criterio = (CriterioReporteBean)request.getSession().getAttribute("criterioIngresoEgreso");
				String filename = "IngresosEgresosPdf";
				morosoAlquilerPdf.reporteMorosoAlquiler(entidades, criterio, context, request, response, filename);
				String fullPath = request.getServletContext().getRealPath("/resources/reports/"+filename+".pdf");
				this.fileDownload(fullPath, response, filename, ".pdf");
			}
			
			log.debug("[mostrarPdf] Fin");
		}catch(Exception e){
			log.debug("[mostrarXls] Error:"+e.getMessage());
			e.printStackTrace();
		}
		
	}
	
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
