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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pe.lima.sg.bean.reporte.IngresoEgresoBean;
import com.pe.lima.sg.bean.reporte.ReporteIngresoEgresoBean;
import com.pe.lima.sg.bean.reporte.RespuestaReporteBean;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.reporte.pdf.IngresoEgresoXls;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.UtilSGT;
import com.pe.lima.sg.rs.reporte.IngresoEgresoXlsDao;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
public class IngresoEgresoXlsAction {
	@Autowired
	private IngresoEgresoXls ingresoEgresoXls;

	@Autowired
	private ServletContext context;
	@Autowired
	private IngresoEgresoXlsDao ingresoEgresoDaoXls;

	@RequestMapping(value = "/ingresoegresosxls", method = RequestMethod.GET)
	public String mostrarFormulario(Model model, String path) {
		Filtro filtro = null;
		try{
			log.debug("[mostrarFormulario] Inicio");
			path = "reporte/ingreso_xls/ing_listado";
			filtro = new Filtro();
			filtro.setFechaInicio(UtilSGT.getDatetoString2(UtilSGT.addDays(new Date(), -30)));
			filtro.setFechaFin(UtilSGT.getFecha("dd/MM/yyyy"));
			filtro.setCodigoEdificacion(1);
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
	@RequestMapping(value = "/ingresoegresosxls/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, Filtro filtro, String path, HttpServletRequest request) {
		path = "reporte/ingreso_xls/ing_listado";
		List<IngresoEgresoBean> listaIngresoEgreso 	= null;
		RespuestaReporteBean respuestaReporteBean	= null;
		try{
			log.debug("[traerRegistrosFiltrados] Inicio");
			if (this.validarCriterio(filtro,model)){
				obtenerNombreEdificio(request, filtro);
				//Realiza la busqueda para efectivo o bancarizado				
				this.buscarIngresoEgresoEfectivoBancarizado(model, filtro, path, request);
				
			}else{
				model.addAttribute("filtro", filtro);
				model.addAttribute("registros", respuestaReporteBean);
				//Datos del reporte a imprimir en el PDF
				request.getSession().setAttribute("reporteIngresoEgresoXls", listaIngresoEgreso);
				
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
	/*Reporte Efectivo/Bancarizado*/
	private void buscarIngresoEgresoEfectivoBancarizado(Model model, Filtro filtro, String path, HttpServletRequest request){
		List<IngresoEgresoBean> listaIngresoEgreso 	= null;
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
			request.getSession().setAttribute("reporteIngresoEgresoXls", listaIngresoEgreso);
			request.getSession().setAttribute("criterioIngresoEgreso", filtro);
			log.debug("[traerRegistrosFiltrados] listaRespuesta:"+listaRespuesta.size());
		}else{
			model.addAttribute("registros", respuestaReporteBean);
			request.getSession().setAttribute("reporteIngresoEgresoXls", listaIngresoEgreso);
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
			
			//Validando la fecha
			if (filtro.getFechaInicio() == null || filtro.getFechaInicio().length()< 8){
				resultado = false;
				model.addAttribute("respuesta", "Debe ingresar la fecha de inicio del reporte");
			}
			if (filtro.getFechaFin() == null || filtro.getFechaFin().length()< 8){
				resultado = false;
				model.addAttribute("respuesta", "Debe ingresar la fecha de fin del reporte");
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
			log.debug("[listarIngresoEgreso] Fec Inicio:"+filtro.getFechaInicio());
			log.debug("[listarIngresoEgreso] Fec Fin:"+filtro.getFechaFin());
			entidades = ingresoEgresoDaoXls.getReporteIngresoEgresoXls(filtro);
			log.debug("[listarIngresoEgreso] entidades:"+entidades.size());
		}catch(Exception e){
			e.printStackTrace();
		}
		return entidades;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/ingresoegresosxls/excel", method = RequestMethod.GET)
	public void mostrarXls(Model model, String path, HttpServletRequest request, HttpServletResponse response) {
		List<IngresoEgresoBean> entidades 	= null;
		Filtro criterio		= null;
		path = "reporte/ingreso_xls/ing_listado";
		try{
			log.debug("[mostrarXls] Inicio");
			entidades = (List<IngresoEgresoBean>)request.getSession().getAttribute("reporteIngresoEgresoXls");
			if (entidades != null && !entidades.isEmpty()){
				criterio = (Filtro)request.getSession().getAttribute("criterioIngresoEgreso");
				String filename = "IngresosEgresos"+ UtilSGT.getFecha("yyyy-MM-dd kk:mm:ss").replace(':', '-');
				ingresoEgresoXls.reporteIngresoEgresoXls(entidades, criterio, context, request, response, filename);
				String fullPath = request.getServletContext().getRealPath("/resources/reports/"+filename+".xlsx");
				this.fileDownload(fullPath, response, filename, ".xlsx");
			}
			
			
			log.debug("[mostrarXls] Fin");
		}catch(Exception e){
			log.debug("[mostrarXls] Error:"+e.getMessage());
			e.printStackTrace();
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
		log.debug("[fileDownload] Inicio");
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
				log.debug("[fileDownload] filename:"+filename);
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.debug("[fileDownload] Fin");
		
	}
	
	
}
