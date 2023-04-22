package com.pe.lima.sg.presentacion.reporte;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

import com.pe.lima.sg.bean.reporte.IngresoEgresoBean;
import com.pe.lima.sg.bean.reporte.ReporteIngresoEgresoBean;
import com.pe.lima.sg.bean.reporte.RespuestaReporteBean;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.reporte.pdf.IngresoEgresoPdf;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.UtilSGT;
import com.pe.lima.sg.rs.reporte.IngresoEgresoDao;

@Controller
public class RezagoAction {
	
	private static final Logger logger = LogManager.getLogger(RezagoAction.class);
	@Autowired
	private IngresoEgresoPdf pdfIngresoEgreso;
	@Autowired
	private ServletContext context;
	@Autowired
	private IngresoEgresoDao ingresoEgresoDao;

	@RequestMapping(value = "/rezago", method = RequestMethod.GET)
	public String mostrarFormulario(Model model, String path) {
		Filtro filtro = null;
		try{
			logger.debug("[mostrarFormulario] Inicio");
			path = "reporte/rezago/rez_listado";
			filtro = new Filtro();
			filtro.setFechaInicio(UtilSGT.getFecha("dd/MM/yyyy"));
			filtro.setFechaFin(UtilSGT.getFecha("dd/MM/yyyy"));
			model.addAttribute("filtro", filtro);
			logger.debug("[mostrarFormulario] Fin");
		}catch(Exception e){
			logger.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		
		return path;
	}
	@RequestMapping(value = "/rezago/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, Filtro filtro, String path, HttpServletRequest request) {
		path = "reporte/rezago/rez_listado";
		List<IngresoEgresoBean> listaCobro 	= null;
		RespuestaReporteBean respuestaReporteBean	= null;
		try{
			logger.debug("[traerRegistrosFiltrados] Inicio");
			
			if (this.validarCriterio(filtro,model)){
				//Realiza la busqueda		
				this.buscarCobrosFueraFecha(model, filtro, path, request);
			}else{
				model.addAttribute("filtro", filtro);
				model.addAttribute("registros", respuestaReporteBean);
				request.getSession().setAttribute("reporteFueraFecha", listaCobro);
			}
		}catch(Exception e){
			logger.debug("[traerRegistrosFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			listaCobro				= null;
			respuestaReporteBean	= null;
		}
		logger.debug("[traerRegistrosFiltrados] Fin");
		return path;
	}
	
	/*Reporte de Fuera de Fecha*/
	private void buscarCobrosFueraFecha(Model model, Filtro filtro, String path, HttpServletRequest request){
		List<IngresoEgresoBean> listacobro 	= null;
		List<RespuestaReporteBean> listaRespuesta	= null;
		RespuestaReporteBean respuestaReporteBean	= null;
		
		listacobro = this.listarCobrosFueraFecha(filtro);
		if (listacobro != null && listacobro.size()>0){
			listaRespuesta = new ArrayList<RespuestaReporteBean>();
			respuestaReporteBean = new RespuestaReporteBean();
			respuestaReporteBean.setDescripcion(Constantes.RESPUESTA_BUSQUEDA_EXITOSA);
			respuestaReporteBean.setTotalRegistro(listacobro.size());
			listaRespuesta.add(respuestaReporteBean);
			model.addAttribute("registros", listaRespuesta);
			request.getSession().setAttribute("reporteFueraFecha", listacobro);
			request.getSession().setAttribute("criterioFueraFecha", filtro);
			logger.debug("[traerRegistrosFiltrados] listaRespuesta:"+listaRespuesta.size());
		}else{
			model.addAttribute("registros", respuestaReporteBean);
			request.getSession().setAttribute("reporteFueraFecha", listacobro);
		}
		
		model.addAttribute("filtro", filtro);
	}
	
	
	/*Validacion de los criterios de busqueda*/
	public boolean validarCriterio(Filtro filtro,Model model){
		boolean resultado = true;
		try{
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
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultado;
	}
	
	private List<IngresoEgresoBean> listarCobrosFueraFecha(Filtro filtro){
		List<IngresoEgresoBean> entidades = new ArrayList<IngresoEgresoBean>();
		try{
			if (filtro.getFechaInicio()== null || filtro.getFechaInicio().equals("")){
				filtro.setFechaInicio(UtilSGT.getDateStringFormat(UtilSGT.addDays(new Date(), -1)));
			}
			if (filtro.getFechaFin()==null || filtro.getFechaFin().equals("")){
				filtro.setFechaFin(UtilSGT.getDateStringFormat(UtilSGT.addDays(new Date(), 1)));
			}
			logger.debug("[listarCobrosFueraFecha] Fec Inicio:"+filtro.getFechaInicio());
			logger.debug("[listarCobrosFueraFecha] Fec Fin:"+filtro.getFechaFin());
			entidades = ingresoEgresoDao.getReporteFueraFecha(filtro);
			logger.debug("[listarIngresoEgreso] entidades:"+entidades.size());
		}catch(Exception e){
			e.printStackTrace();
		}
		return entidades;
	}
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/rezago/pdf", method = RequestMethod.GET)
	public void mostrarPdf(Model model, String path, HttpServletRequest request, HttpServletResponse response) {
		List<IngresoEgresoBean> entidades 				= null;
		Filtro criterio									= null;
		List<ReporteIngresoEgresoBean> listaDataReporte = null;
		
		try{
			path = "reporte/rezago/rez_listado";
			logger.debug("[mostrarPdf] Inicio");
			entidades = (List<IngresoEgresoBean>)request.getSession().getAttribute("reporteFueraFecha");

			if (entidades != null && !entidades.isEmpty()){
				//Preparando la data para ser mostrada
				listaDataReporte = this.preparaDataFueraFecha(entidades,request);
				criterio = (Filtro)request.getSession().getAttribute("criterioFueraFecha");
				String filename = "ReporteFueraFecha" + UtilSGT.getFecha("yyyy-MM-dd kk:mm:ss").replace(':', '-');
				//pdfIngresoEgreso.reporteIngresoEgreso(entidades, criterio, context, request, response, filename);
				pdfIngresoEgreso.reporteFueraFecha(listaDataReporte, criterio, context, request, response, filename);
				String fullPath = request.getServletContext().getRealPath("/resources/reports/"+filename+".pdf");
				this.fileDownload(fullPath, response, filename, ".pdf");
			}
			
			logger.debug("[mostrarPdf] Fin");
		}catch(Exception e){
			logger.debug("[mostrarPdf] Error:"+e.getMessage());
			e.printStackTrace();
		}
		
	}
	

	/*Obtenemos el nombre del inmueble*/
	private String obtenerNombreEdificio(HttpServletRequest request, Integer codigoInmueble){
		@SuppressWarnings("unchecked")
		Map<String, Object> resultados = (Map<String, Object>)request.getSession().getAttribute("SessionMapEdificacion");
		String nombreInmueble = null;
		 for (Entry<String,Object> pair : resultados.entrySet()){
		        if (codigoInmueble.compareTo((Integer)pair.getValue())==0){
		        	System.out.println(pair.getKey()+" "+pair.getValue());
		        	nombreInmueble = pair.getKey();
		        }
		    }
		return nombreInmueble;
	}
	
	//Dato de los Contrato  Cobros
		public void setDataContratoReporte(IngresoEgresoBean data, ReporteIngresoEgresoBean reporteBean,HttpServletRequest request){
			//reporteBean.setNombreUsuario(data.getNombre());
			reporteBean.setNombreInmueble(obtenerNombreEdificio(request, data.getCodigo_edificio()));
			reporteBean.setCodigoInmueble(data.getCodigo_edificio());
			@SuppressWarnings("unchecked")
			Map<Integer, String> usuarios= (Map<Integer, String>)request.getSession().getAttribute("SessionMapUsuarioAllMap");
			/* Contrato (todos) - se conserva solo para usarlo en los totales*/
			reporteBean.getListaContrato().add(data);
			reporteBean.setContratoSubTotalDolares(reporteBean.getContratoSubTotalDolares().add(data.getMonto_dolares()));
			reporteBean.setContratoSubTotalSoles(reporteBean.getContratoSubTotalSoles().add(data.getMonto_soles()));
			data.setNombre((usuarios.get(data.getUsurio_creacion())));
			
			
			if (data.getTipo_cobro().equals("ALQ")){
				reporteBean.getListaAlquiler().add(data);
				reporteBean.setAlquilerSubTotalDolares(reporteBean.getAlquilerSubTotalDolares().add(data.getMonto_dolares()));
				reporteBean.setAlquilerSubTotalSoles(reporteBean.getAlquilerSubTotalSoles().add(data.getMonto_soles()));
			}
			if (data.getTipo_cobro().equals("SER")){
				reporteBean.getListaServicio().add(data);
				reporteBean.setServicioSubTotalDolares(reporteBean.getServicioSubTotalDolares().add(data.getMonto_dolares()));
				reporteBean.setServicioSubTotalSoles(reporteBean.getServicioSubTotalSoles().add(data.getMonto_soles()));
			}
			if (data.getTipo_cobro().equals("LUZ")){
				reporteBean.getListaLuz().add(data);
				reporteBean.setLuzSubTotalDolares(reporteBean.getLuzSubTotalDolares().add(data.getMonto_dolares()));
				reporteBean.setLuzSubTotalSoles(reporteBean.getLuzSubTotalSoles().add(data.getMonto_soles()));
			}
			if (data.getTipo_cobro().equals("ARB")){
				reporteBean.getListaArbitrios().add(data);
				reporteBean.setArbitriosSubTotalDolares(reporteBean.getArbitriosSubTotalDolares().add(data.getMonto_dolares()));
				reporteBean.setArbitriosSubTotalSoles(reporteBean.getArbitriosSubTotalSoles().add(data.getMonto_soles()));
			}
		}
	//Dato de los gastos
	public void setDataGastoReporte(IngresoEgresoBean data, ReporteIngresoEgresoBean reporteBean){
		reporteBean.setNombreUsuario(data.getNombre());
		reporteBean.getListaGasto().add(data);
		reporteBean.setGastoSubTotalDolares(reporteBean.getGastoSubTotalDolares().add(data.getMonto_dolares()));
		reporteBean.setGastoSubTotalSoles(reporteBean.getGastoSubTotalSoles().add(data.getMonto_soles()));
	}
	//Dato de los ingresos
	public void setDataIngresoReporte(IngresoEgresoBean data, ReporteIngresoEgresoBean reporteBean){
		reporteBean.setNombreUsuario(data.getNombre());
		reporteBean.getListaIngreso().add(data);
		reporteBean.setIngresoSubTotalDolares(reporteBean.getIngresoSubTotalDolares().add(data.getMonto_dolares()));
		reporteBean.setIngresoSubTotalSoles(reporteBean.getIngresoSubTotalSoles().add(data.getMonto_soles()));
	}
	//Asigna los datos a los ingresos o gastos
	public void setIngresoGastoReporte(IngresoEgresoBean data, ReporteIngresoEgresoBean reporteBean, HttpServletRequest request){
		if (data.getNumero().equals(Constantes.DESC_TIPO_CONCEPTO_GASTO)){
			this.setDataGastoReporte(data, reporteBean);
		}else if (data.getNumero().equals(Constantes.DESC_TIPO_CONCEPTO_INGRESO)){
			this.setDataIngresoReporte(data, reporteBean);
		}else{
			this.setDataContratoReporte(data, reporteBean, request);
		}
	}
	
	public List<ReporteIngresoEgresoBean> preparaDataFueraFecha(List<IngresoEgresoBean> entidades,HttpServletRequest request){
		List<ReporteIngresoEgresoBean> resultado = null;
		HashMap<String, ReporteIngresoEgresoBean> reporteMap = new HashMap<String, ReporteIngresoEgresoBean>();
		Integer keyAnterior = null;
		ReporteIngresoEgresoBean reporteBean = new ReporteIngresoEgresoBean();
		
		for(IngresoEgresoBean data:entidades){
			//La primera vez
			if (keyAnterior == null){
				keyAnterior = data.getCodigo_edificio();
				this.setIngresoGastoReporte(data, reporteBean, request);
				reporteMap.put(keyAnterior.toString(), reporteBean);
			}else{
				// Las demas veces
				if (keyAnterior.compareTo(data.getCodigo_edificio())==0){
					this.setIngresoGastoReporte(data, reporteBean,request);
				}else{
					reporteMap.put(keyAnterior.toString(), reporteBean);
					//buscamos el elemento
					reporteBean =reporteMap.get(data.getCodigo_edificio().toString());
					if (reporteBean == null){
						reporteBean = new ReporteIngresoEgresoBean();
						keyAnterior = data.getCodigo_edificio();
						this.setIngresoGastoReporte(data, reporteBean,request);
						reporteMap.put(keyAnterior.toString(), reporteBean);
					}else{
						keyAnterior = data.getCodigo_edificio();
						this.setIngresoGastoReporte(data, reporteBean,request);
					}
				}
				
			}
		}
		
		if (reporteMap.size() > 0){
			resultado = new ArrayList<ReporteIngresoEgresoBean>(reporteMap.values());
		}
		
		return resultado;
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
