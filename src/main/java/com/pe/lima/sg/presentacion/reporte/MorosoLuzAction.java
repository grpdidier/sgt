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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pe.lima.sg.bean.reporte.CriterioReporteBean;
import com.pe.lima.sg.bean.reporte.MorosoLuzBean;
import com.pe.lima.sg.bean.reporte.RespuestaReporteBean;
import com.pe.lima.sg.presentacion.reporte.excel.MorosoLuzExcel;
import com.pe.lima.sg.presentacion.reporte.pdf.MorosoLuzPdf;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.UtilSGT;
import com.pe.lima.sg.rs.reporte.MorosoLuzDao;

@Controller
public class MorosoLuzAction {
	private static final Logger logger = LogManager.getLogger(MorosoLuzAction.class);
	@Autowired
	private MorosoLuzExcel morosoLuzExcel;
	@Autowired
	private MorosoLuzPdf morosoLuzPdf;
	@Autowired
	private ServletContext context;
	@Autowired
	private MorosoLuzDao morosoLuzDao;

	@RequestMapping(value = "/morosoluz", method = RequestMethod.GET)
	public String mostrarFormulario(Model model, String path) {
		CriterioReporteBean filtro = null;
		try{
			logger.debug("[mostrarFormulario] Inicio");
			path = "reporte/moroso/luz_listado";
			filtro = new CriterioReporteBean();
			filtro.setFechaInicio(UtilSGT.getFecha("dd/MM/yyyy"));
			filtro.setFechaFin(UtilSGT.getFecha("dd/MM/yyyy"));
			model.addAttribute("filtro", filtro);
			//ingresoEgresoDao = new IngresoEgresoDao();
			//ingresoEgresoDao.getreporteMorosoLuz();
			logger.debug("[mostrarFormulario] Fin");
		}catch(Exception e){
			logger.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		
		return path;
	}
	@RequestMapping(value = "/morosoluz/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, CriterioReporteBean filtro, String path, HttpServletRequest request) {
		path = "reporte/moroso/luz_listado";
		List<MorosoLuzBean> listaMorosoLuz 	= null;
		List<RespuestaReporteBean> listaRespuesta	= null;
		RespuestaReporteBean respuestaReporteBean	= null;
		try{
			logger.debug("[traerRegistrosFiltrados] Inicio");
			listaMorosoLuz = this.listarMorosoLuz(filtro);
			if (listaMorosoLuz != null){
				listaRespuesta = new ArrayList<RespuestaReporteBean>();
				respuestaReporteBean = new RespuestaReporteBean();
				respuestaReporteBean.setDescripcion(Constantes.RESPUESTA_BUSQUEDA_EXITOSA);
				respuestaReporteBean.setTotalRegistro(listaMorosoLuz.size());
				listaRespuesta.add(respuestaReporteBean);
				model.addAttribute("registros", listaRespuesta);
				request.getSession().setAttribute("reporteMorosoLuz", listaMorosoLuz);
				request.getSession().setAttribute("criterioIngresoEgreso", filtro);
				logger.debug("[traerRegistrosFiltrados] listaRespuesta:"+listaRespuesta.size());
			}else{
				model.addAttribute("registros", respuestaReporteBean);
				request.getSession().setAttribute("reporteMorosoLuz", listaMorosoLuz);
			}
			model.addAttribute("filtro", filtro);
			
		}catch(Exception e){
			logger.debug("[traerRegistrosFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			listaMorosoLuz		= null;
			listaRespuesta			= null;
			respuestaReporteBean	= null;
		}
		logger.debug("[traerRegistrosFiltrados] Fin");
		return path;
	}
	
	private List<MorosoLuzBean> listarMorosoLuz(CriterioReporteBean filtro){
		List<MorosoLuzBean> entidades = new ArrayList<MorosoLuzBean>();
		try{
			
			if (filtro.getFechaFin()==null || filtro.getFechaFin().equals("")){
				filtro.setFechaFin(UtilSGT.getDateStringFormat(UtilSGT.addDays(new Date(), 1)));
			}
			logger.debug("[listarIngresoEgreso] Fec Fin:"+filtro.getFechaFin());
			entidades = morosoLuzDao.getReporteMorosoLuz(filtro.getFechaFin());
			logger.debug("[listarIngresoEgreso] entidades:"+entidades);
		}catch(Exception e){
			e.printStackTrace();
		}
		return entidades;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/morosoluz/excel", method = RequestMethod.GET)
	public void mostrarXls(Model model, String path, HttpServletRequest request, HttpServletResponse response) {
		List<MorosoLuzBean> entidades 	= null;
		CriterioReporteBean criterio		= null;
		path = "reporte/moroso/luz_listado";
		try{
			logger.debug("[mostrarXls] Inicio");
			entidades = (List<MorosoLuzBean>)request.getSession().getAttribute("reporteMorosoLuz");
			if (entidades != null && !entidades.isEmpty()){
				criterio = (CriterioReporteBean)request.getSession().getAttribute("criterioIngresoEgreso");
				String filename = "MorosoLuz";
				morosoLuzExcel.reporteMorosoLuz(entidades, criterio, context, request, response, filename);
				String fullPath = request.getServletContext().getRealPath("/resources/reports/"+filename+".xls");
				this.fileDownload(fullPath, response, filename, ".xls");
			}
			
			
			logger.debug("[mostrarXls] Fin");
		}catch(Exception e){
			logger.debug("[mostrarXls] Error:"+e.getMessage());
			e.printStackTrace();
		}
		
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/morosoluz/pdf", method = RequestMethod.GET)
	public void mostrarPdf(Model model, String path, HttpServletRequest request, HttpServletResponse response) {
		List<MorosoLuzBean> entidades 	= null;
		CriterioReporteBean criterio		= null;
		path = "reporte/moroso/luz_listado";
		try{
			logger.debug("[mostrarPdf] Inicio");
			entidades = (List<MorosoLuzBean>)request.getSession().getAttribute("reporteMorosoLuz");
			if (entidades != null && !entidades.isEmpty()){
				criterio = (CriterioReporteBean)request.getSession().getAttribute("criterioIngresoEgreso");
				String filename = "IngresosEgresosPdf";
				morosoLuzPdf.reporteMorosoLuz(entidades, criterio, context, request, response, filename);
				String fullPath = request.getServletContext().getRealPath("/resources/reports/"+filename+".pdf");
				this.fileDownload(fullPath, response, filename, ".pdf");
			}
			
			logger.debug("[mostrarPdf] Fin");
		}catch(Exception e){
			logger.debug("[mostrarXls] Error:"+e.getMessage());
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
