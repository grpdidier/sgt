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

import com.pe.lima.sg.bean.reporte.IngresoEgresoBean;
import com.pe.lima.sg.bean.reporte.ReporteBancarizadoBean;
import com.pe.lima.sg.bean.reporte.RespuestaReporteBean;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.reporte.pdf.BancarizadoPdf;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.UtilSGT;
import com.pe.lima.sg.rs.reporte.BancarizadoDao;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
public class BancarizadoAction {
	@Autowired
	private BancarizadoPdf bancarizadoPdf;
	@Autowired
	private ServletContext context;
	@Autowired
	private BancarizadoDao bancarizadoDao;

	@RequestMapping(value = "/bancarizado", method = RequestMethod.GET)
	public String mostrarFormulario(Model model, String path) {
		Filtro filtro = null;
		try{
			log.debug("[mostrarFormulario] Inicio");
			path = "reporte/bancarizado/ban_listado";
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
	@RequestMapping(value = "/bancarizado/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, Filtro filtro, String path, HttpServletRequest request) {
		path = "reporte/bancarizado/ban_listado";
		List<IngresoEgresoBean> listaIngresoEgreso 	= null;
		RespuestaReporteBean respuestaReporteBean	= null;
		try{
			log.debug("[traerRegistrosFiltrados] Inicio");
			if (this.validarCriterio(filtro,model)){
				//Realiza la busqueda para efectivo o bancarizado				
				this.buscarIngresoEgresoEfectivoBancarizado(model, filtro, path, request);
				
			}else{
				model.addAttribute("filtro", filtro);
				model.addAttribute("registros", respuestaReporteBean);
				//Datos del reporte a imprimir en el PDF
				request.getSession().setAttribute("reporteIngresoEgresoBancarizado", listaIngresoEgreso);
				
			}
			
			
		}catch(Exception e){
			log.debug("[traerRegistrosFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			listaIngresoEgreso		= null;
			respuestaReporteBean	= null;
		}
		log.debug("[traerRegistrosFiltrados] Fin");
		return path;
	}
	
	/*Reporte Efectivo/Bancarizado*/
	private void buscarIngresoEgresoEfectivoBancarizado(Model model, Filtro filtro, String path, HttpServletRequest request){
		List<ReporteBancarizadoBean> listaIngresoEgreso 	= null;
		List<RespuestaReporteBean> listaRespuesta	= null;
		RespuestaReporteBean respuestaReporteBean	= null;
		
		listaIngresoEgreso = this.listarIngresoEgreso(filtro);
		if (listaIngresoEgreso != null && listaIngresoEgreso.size()>0){
			listaRespuesta = new ArrayList<RespuestaReporteBean>();
			respuestaReporteBean = new RespuestaReporteBean();
			respuestaReporteBean.setDescripcion(Constantes.RESPUESTA_BUSQUEDA_EXITOSA);
			respuestaReporteBean.setTotalRegistro(listaIngresoEgreso.size());
			listaRespuesta.add(respuestaReporteBean);
			model.addAttribute("registros", listaRespuesta);
			request.getSession().setAttribute("reporteIngresoEgresoBancarizado", listaIngresoEgreso);
			request.getSession().setAttribute("criterioIngresoEgresoBancarizado", filtro);
			log.debug("[traerRegistrosFiltrados] listaRespuesta:"+listaRespuesta.size());
		}else{
			model.addAttribute("registros", respuestaReporteBean);
			request.getSession().setAttribute("reporteIngresoEgresoBancarizado", listaIngresoEgreso);
		}
		
		model.addAttribute("filtro", filtro);
	}
	
	
	
	public boolean validarCriterio(Filtro filtro,Model model){
		boolean resultado = true;
		try{
			
			//Validando la fecha
			if (filtro.getFechaInicio() == null || filtro.getFechaInicio().length()< 8){
				resultado = false;
				model.addAttribute("respuesta", "Debe ingresar la fecha del reporte");
			}else{
				filtro.setFechaFin(filtro.getFechaInicio());
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	private List<ReporteBancarizadoBean> listarIngresoEgreso(Filtro filtro){
		List<ReporteBancarizadoBean> entidades = new ArrayList<ReporteBancarizadoBean>();
		try{
			if (filtro.getFechaInicio()== null || filtro.getFechaInicio().equals("")){
				filtro.setFechaInicio(UtilSGT.getDateStringFormat(UtilSGT.addDays(new Date(), -1)));
			}
			if (filtro.getFechaFin()==null || filtro.getFechaFin().equals("")){
				filtro.setFechaFin(UtilSGT.getDateStringFormat(UtilSGT.addDays(new Date(), 1)));
			}
			log.debug("[listarIngresoEgreso] Fec Inicio:"+filtro.getFechaInicio());
			log.debug("[listarIngresoEgreso] Fec Fin:"+filtro.getFechaFin());
			entidades = bancarizadoDao.getReporteIngresoEgreso(filtro);
			log.debug("[listarIngresoEgreso] entidades:"+entidades.size());
		}catch(Exception e){
			e.printStackTrace();
		}
		return entidades;
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bancarizado/pdf", method = RequestMethod.GET)
	public void mostrarPdf(Model model, String path, HttpServletRequest request, HttpServletResponse response) {
		List<ReporteBancarizadoBean> entidades 			= null;
		Filtro criterio									= null;
		
		try{
			path = "reporte/bancarizado/ban_listado";
			log.debug("[mostrarPdf] Inicio");
			entidades = (List<ReporteBancarizadoBean>)request.getSession().getAttribute("reporteIngresoEgresoBancarizado");
			
			if (entidades != null && !entidades.isEmpty()){
				criterio = (Filtro)request.getSession().getAttribute("criterioIngresoEgresoBancarizado");
				String filename = "Bancarizado"+ UtilSGT.getFecha("yyyy-MM-dd kk:mm:ss").replace(':', '-');
				bancarizadoPdf.reporteBancarizado(entidades, criterio, context, request, response, filename);
				
				String fullPath = request.getServletContext().getRealPath("/resources/reports/"+filename+".pdf");
				this.fileDownload(fullPath, response, filename, ".pdf");
			}
			
			log.debug("[mostrarPdf] Fin");
		}catch(Exception e){
			log.debug("[mostrarPdf] Error:"+e.getMessage());
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
