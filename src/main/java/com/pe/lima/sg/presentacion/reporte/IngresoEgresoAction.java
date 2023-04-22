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
import com.pe.lima.sg.presentacion.reporte.excel.IngresoEgresoExcel;
import com.pe.lima.sg.presentacion.reporte.pdf.IngresoEgresoPdf;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.UtilSGT;
import com.pe.lima.sg.rs.reporte.IngresoEgresoDao;

@Controller
public class IngresoEgresoAction {
	private static final Logger logger = LogManager.getLogger(IngresoEgresoAction.class);
	@Autowired
	private IngresoEgresoExcel fileExcelBase;
	@Autowired
	private IngresoEgresoPdf pdfIngresoEgreso;
	@Autowired
	private ServletContext context;
	@Autowired
	private IngresoEgresoDao ingresoEgresoDao;

	@RequestMapping(value = "/ingresoegresos", method = RequestMethod.GET)
	public String mostrarFormulario(Model model, String path) {
		Filtro filtro = null;
		try{
			logger.debug("[mostrarFormulario] Inicio");
			path = "reporte/ingreso/ing_listado";
			filtro = new Filtro();
			filtro.setFechaInicio(UtilSGT.getFecha("dd/MM/yyyy"));
			filtro.setFechaFin(UtilSGT.getFecha("dd/MM/yyyy"));
			filtro.setTipoPago(Constantes.TIPO_PAGO_COD_EFECTIVO);
			filtro.setCodigoEdificacion(1);
			model.addAttribute("filtro", filtro);
			//ingresoEgresoDao = new IngresoEgresoDao();
			//ingresoEgresoDao.getReporteIngresoEgreso();
			logger.debug("[mostrarFormulario] Fin");
		}catch(Exception e){
			logger.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		
		return path;
	}
	@RequestMapping(value = "/ingresoegreso/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, Filtro filtro, String path, HttpServletRequest request) {
		path = "reporte/ingreso/ing_listado";
		List<IngresoEgresoBean> listaIngresoEgreso 	= null;
		RespuestaReporteBean respuestaReporteBean	= null;
		try{
			logger.debug("[traerRegistrosFiltrados] Inicio");
			if (this.validarCriterio(filtro,model)){
				//Realiza la busqueda para efectivo o bancarizado				
				this.buscarIngresoEgresoEfectivoBancarizado(model, filtro, path, request);
				
			}else{
				model.addAttribute("filtro", filtro);
				model.addAttribute("registros", respuestaReporteBean);
				//Datos del reporte a imprimir en el PDF
				request.getSession().setAttribute("reporteIngresoEgreso", listaIngresoEgreso);
				
			}
			
			
		}catch(Exception e){
			logger.debug("[traerRegistrosFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			listaIngresoEgreso		= null;
			respuestaReporteBean	= null;
		}
		logger.debug("[traerRegistrosFiltrados] Fin");
		return path;
	}
	
	/*Reporte Efectivo/Bancarizado*/
	private void buscarIngresoEgresoEfectivoBancarizado(Model model, Filtro filtro, String path, HttpServletRequest request){
		List<IngresoEgresoBean> listaIngresoEgreso 	= null;
		List<RespuestaReporteBean> listaRespuesta	= null;
		RespuestaReporteBean respuestaReporteBean	= null;
		
		listaIngresoEgreso = this.listarIngresoEgreso(filtro);
		if (listaIngresoEgreso != null && listaIngresoEgreso.size()>0){
			listaRespuesta = new ArrayList<RespuestaReporteBean>();
			respuestaReporteBean = new RespuestaReporteBean();
			if (filtro.getTipoPago().equals(Constantes.TIPO_PAGO_COD_EFECTIVO)){
				respuestaReporteBean.setTipoPago(Constantes.TIPO_PAGO_DES_EFECTIVO);
			}else{
				respuestaReporteBean.setTipoPago(Constantes.TIPO_PAGO_DES_BANCARIZADO);
			}
			respuestaReporteBean.setDescripcion(Constantes.RESPUESTA_BUSQUEDA_EXITOSA);
			respuestaReporteBean.setTotalRegistro(listaIngresoEgreso.size());
			listaRespuesta.add(respuestaReporteBean);
			model.addAttribute("registros", listaRespuesta);
			request.getSession().setAttribute("reporteIngresoEgreso", listaIngresoEgreso);
			request.getSession().setAttribute("criterioIngresoEgreso", filtro);
			logger.debug("[traerRegistrosFiltrados] listaRespuesta:"+listaRespuesta.size());
		}else{
			model.addAttribute("registros", respuestaReporteBean);
			request.getSession().setAttribute("reporteIngresoEgreso", listaIngresoEgreso);
		}
		//El tipo de pago indicará el tipo de reporte a generar
		request.getSession().setAttribute("sessionRepIngEgrTipoPago", filtro.getTipoPago());
		model.addAttribute("filtro", filtro);
	}
	
	
	
	public boolean validarCriterio(Filtro filtro,Model model){
		boolean resultado = true;
		try{
			//Edificación
			if (filtro.getCodigoEdificacion().compareTo(-1)==0){
				resultado = false;
				model.addAttribute("respuesta", "Debe seleccionar la edificación");
			}
			//Tipo de Pago
			if (filtro.getTipoPago().equals("-1")){
				resultado = false;
				model.addAttribute("respuesta", "Debe seleccionar el tipo de pago");
			}
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
	
	private List<IngresoEgresoBean> listarIngresoEgreso(Filtro filtro){
		List<IngresoEgresoBean> entidades = new ArrayList<IngresoEgresoBean>();
		try{
			if (filtro.getFechaInicio()== null || filtro.getFechaInicio().equals("")){
				filtro.setFechaInicio(UtilSGT.getDateStringFormat(UtilSGT.addDays(new Date(), -1)));
			}
			if (filtro.getFechaFin()==null || filtro.getFechaFin().equals("")){
				filtro.setFechaFin(UtilSGT.getDateStringFormat(UtilSGT.addDays(new Date(), 1)));
			}
			logger.debug("[listarIngresoEgreso] Fec Inicio:"+filtro.getFechaInicio());
			logger.debug("[listarIngresoEgreso] Fec Fin:"+filtro.getFechaFin());
			entidades = ingresoEgresoDao.getReporteIngresoEgreso(filtro);
			logger.debug("[listarIngresoEgreso] entidades:"+entidades.size());
		}catch(Exception e){
			e.printStackTrace();
		}
		return entidades;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/ingresoegreso/excel", method = RequestMethod.GET)
	public void mostrarXls(Model model, String path, HttpServletRequest request, HttpServletResponse response) {
		List<IngresoEgresoBean> entidades 	= null;
		Filtro criterio		= null;
		path = "reporte/ingreso/ing_listado";
		try{
			logger.debug("[mostrarXls] Inicio");
			entidades = (List<IngresoEgresoBean>)request.getSession().getAttribute("reporteIngresoEgreso");
			if (entidades != null && !entidades.isEmpty()){
				criterio = (Filtro)request.getSession().getAttribute("criterioIngresoEgreso");
				String filename = "IngresosEgresos"+ UtilSGT.getFecha("yyyy-MM-dd kk:mm:ss").replace(':', '-');
				fileExcelBase.reporteIngresoEgreso(entidades, criterio, context, request, response, filename);
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
	@RequestMapping(value = "/ingresoegreso/pdf", method = RequestMethod.GET)
	public void mostrarPdf(Model model, String path, HttpServletRequest request, HttpServletResponse response) {
		List<IngresoEgresoBean> entidades 				= null;
		Filtro criterio									= null;
		List<ReporteIngresoEgresoBean> listaDataReporte = null;
		String tipoPago									= null;
		
		try{
			path = "reporte/ingreso/ing_listado";
			logger.debug("[mostrarPdf] Inicio");
			entidades = (List<IngresoEgresoBean>)request.getSession().getAttribute("reporteIngresoEgreso");
			tipoPago = (String)request.getSession().getAttribute("sessionRepIngEgrTipoPago");
			if (entidades != null && !entidades.isEmpty()){
				//Preparando la data para ser mostrada (ingresos y gastos)
				listaDataReporte = this.preparaDataIngresoGasto(entidades);
				criterio = (Filtro)request.getSession().getAttribute("criterioIngresoEgreso");
				obtenerNombreEdificio(request, criterio);
				String filename = "IngresosEgresosPdf";
				//pdfIngresoEgreso.reporteIngresoEgreso(entidades, criterio, context, request, response, filename);
				if (tipoPago.equals(Constantes.TIPO_PAGO_COD_EFECTIVO)){
					filename = "IngresosEgresosEfectivo"+UtilSGT.getFecha("yyyy-MM-dd kk:mm:ss").replace(':', '-');
					pdfIngresoEgreso.reporteIngresoEgreso_v2Efectivo(listaDataReporte, criterio, context, request, response, filename);
				}
				if (tipoPago.equals(Constantes.TIPO_PAGO_COD_BANCARIZADO)){
					filename = "IngresosEgresosBancarizado"+UtilSGT.getFecha("yyyy-MM-dd kk:mm:ss").replace(':', '-');
					pdfIngresoEgreso.reporteIngresoEgreso_v2Bancarizado(listaDataReporte, criterio, context, request, response, filename);
				}
				String fullPath = request.getServletContext().getRealPath("/resources/reports/"+filename+".pdf");
				this.fileDownload(fullPath, response, filename, ".pdf");
			}
			
			logger.debug("[mostrarPdf] Fin");
		}catch(Exception e){
			logger.debug("[mostrarPdf] Error:"+e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	private void obtenerNombreEdificio(HttpServletRequest request, Filtro filtro){
		@SuppressWarnings("unchecked")
		Map<String, Object> resultados = (Map<String, Object>)request.getSession().getAttribute("SessionMapEdificacion");
		
		 for (Entry<String,Object> pair : resultados.entrySet()){
		        if (filtro.getCodigoEdificacion().compareTo((Integer)pair.getValue())==0){
		        	System.out.println(pair.getKey()+" "+pair.getValue());
		        	filtro.setNombre(pair.getKey());
		        }
		    }
	}
	
	//Dato de los Contrato  Cobros
		public void setDataContratoReporte(IngresoEgresoBean data, ReporteIngresoEgresoBean reporteBean){
			reporteBean.setNombreUsuario(data.getNombre());
			/* Contrato (todos) - se conserva solo para usarlo en los totales*/
			reporteBean.getListaContrato().add(data);
			reporteBean.setContratoSubTotalDolares(reporteBean.getContratoSubTotalDolares().add(data.getMonto_dolares()));
			reporteBean.setContratoSubTotalSoles(reporteBean.getContratoSubTotalSoles().add(data.getMonto_soles()));
			
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
	public void setIngresoGastoReporte(IngresoEgresoBean data, ReporteIngresoEgresoBean reporteBean){
		if (data.getNumero().equals(Constantes.DESC_TIPO_CONCEPTO_GASTO)){
			this.setDataGastoReporte(data, reporteBean);
		}else if (data.getNumero().equals(Constantes.DESC_TIPO_CONCEPTO_INGRESO)){
			this.setDataIngresoReporte(data, reporteBean);
		}else{
			this.setDataContratoReporte(data, reporteBean);
		}
	}
	
	public List<ReporteIngresoEgresoBean> preparaDataIngresoGasto(List<IngresoEgresoBean> entidades){
		List<ReporteIngresoEgresoBean> resultado = null;
		HashMap<String, ReporteIngresoEgresoBean> reporteMap = new HashMap<String, ReporteIngresoEgresoBean>();
		String keyAnterior = null;
		ReporteIngresoEgresoBean reporteBean = new ReporteIngresoEgresoBean();
		
		for(IngresoEgresoBean data:entidades){
			//La primera vez
			if (keyAnterior == null){
				keyAnterior = data.getNombre(); //nombre del usuario
				this.setIngresoGastoReporte(data, reporteBean);
				reporteMap.put(keyAnterior, reporteBean);
			}else{
				// Las demas veces
				if (keyAnterior.equals(data.getNombre())){
					this.setIngresoGastoReporte(data, reporteBean);
				}else{
					reporteMap.put(keyAnterior, reporteBean);
					//buscamos el elemento
					reporteBean =reporteMap.get(data.getNombre());
					if (reporteBean == null){
						reporteBean = new ReporteIngresoEgresoBean();
						keyAnterior = data.getNombre();
						this.setIngresoGastoReporte(data, reporteBean);
						reporteMap.put(keyAnterior, reporteBean);
					}else{
						keyAnterior = data.getNombre();
						this.setIngresoGastoReporte(data, reporteBean);
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
