package com.pe.lima.sg.presentacion.reporte;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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

import com.pe.lima.sg.bean.reporte.LocalBean;
import com.pe.lima.sg.bean.reporte.LocalSubTotalBean;
import com.pe.lima.sg.bean.reporte.LocalTotalBean;
import com.pe.lima.sg.bean.reporte.RespuestaReporteBean;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.reporte.pdf.LocalExcel;
import com.pe.lima.sg.presentacion.reporte.pdf.LocalPdf;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.UtilSGT;
import com.pe.lima.sg.rs.reporte.LocalDao;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
public class LocalAction {
	
	@Autowired
	private LocalPdf localPdf;
	@Autowired
	private LocalExcel localExcel;
	@Autowired
	private ServletContext context;
	@Autowired
	private LocalDao localDao;

	@RequestMapping(value = "/localxinmueble", method = RequestMethod.GET)
	public String mostrarFormulario(Model model, String path) {
		Filtro filtro = null;
		try{
			log.debug("[mostrarFormulario] Inicio");
			path = "reporte/local/loc_listado";
			filtro = new Filtro();
			filtro.setFechaInicio(UtilSGT.getFecha("dd/MM/yyyy"));
			filtro.setFechaFin(UtilSGT.getFecha("dd/MM/yyyy"));
			filtro.setTipoCobro(Constantes.TIPO_COBRO_ALQUILER);
			filtro.setAnio(new Integer(UtilSGT.getFecha("yyyy")));
			filtro.setMesFin(UtilSGT.getFecha("MM"));
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
	
	@RequestMapping(value = "/localxinmueble/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, Filtro filtro, String path, HttpServletRequest request) {
		path = "reporte/local/loc_listado";
		RespuestaReporteBean respuestaReporteBean	= null;
		try{
			log.debug("[traerRegistrosFiltrados] Inicio");
			if (this.validarCriterio(filtro,model)){
				//Realiza la busqueda de morosos				
				this.buscarLocalxInmueble(model, filtro, path, request);
			}else{
				model.addAttribute("filtro", filtro);
				model.addAttribute("registros", respuestaReporteBean);
				//Datos del reporte a imprimir en el PDF
				request.getSession().setAttribute("reporteLocalxInmueble", null);
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
	@RequestMapping(value = "/localxinmueble/excel", method = RequestMethod.GET)
	public void mostraExcel(Model model, String path, HttpServletRequest request, HttpServletResponse response) {
		List<LocalBean> entidades 				= null;
		Filtro criterio							= null;
		List<LocalTotalBean> listaDataReporte 	= null;
		try{
			path = "reporte/local/loc_listado";
			log.debug("[mostraExcel] Inicio");
			entidades = (List<LocalBean>)request.getSession().getAttribute("reporteLocalxInmueble");
			if (entidades != null && !entidades.isEmpty()){
				//Preparando la data para ser mostrada (ingresos y gastos)
				listaDataReporte = this.preparaDataLocalxInmueble(entidades);
				criterio = (Filtro)request.getSession().getAttribute("criterioLocal");
				obtenerNombreEdificio(request, criterio);
				//obtenerNombreConcepto(criterio);
				String filename = "Local"+UtilSGT.getFecha("yyyy-MM-dd kk:mm:ss").replace(':', '-');
				localExcel.reporteLocalxInmueble(listaDataReporte, criterio, context, request, response, filename);
				
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
	@RequestMapping(value = "/localxinmueble/pdf", method = RequestMethod.GET)
	public void mostrarPdf(Model model, String path, HttpServletRequest request, HttpServletResponse response) {
		List<LocalBean> entidades 				= null;
		Filtro criterio							= null;
		List<LocalTotalBean> listaDataReporte 	= null;
		try{
			path = "reporte/local/loc_listado";
			log.debug("[mostrarPdf] Inicio");
			entidades = (List<LocalBean>)request.getSession().getAttribute("reporteLocalxInmueble");
			if (entidades != null && !entidades.isEmpty()){
				//Preparando la data para ser mostrada (ingresos y gastos)
				listaDataReporte = this.preparaDataLocalxInmueble(entidades);
				criterio = (Filtro)request.getSession().getAttribute("criterioLocal");
				obtenerNombreEdificio(request, criterio);
				//obtenerNombreConcepto(criterio);
				String filename = "Local"+UtilSGT.getFecha("yyyy-MM-dd kk:mm:ss").replace(':', '-');
				localPdf.reporteLocalxInmueble(listaDataReporte, criterio, context, request, response, filename);
				
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
			//Edificación
			if (filtro.getCodigoEdificacion().compareTo(-1)==0){
				resultado = false;
				model.addAttribute("respuesta", "Debe seleccionar el Inmueble");
			}
			
			//Año
			if (filtro.getAnio() == null || filtro.getAnio()< 2000){
				resultado = false;
				model.addAttribute("respuesta", "Debe ingresar un año valido");
			}
			this.obtenerFechaFin(filtro);
			//Validando la fecha
			if (filtro.getMesFin() == null || filtro.getMesFin().equals("-1")){
				resultado = false;
				model.addAttribute("respuesta", "Debe ingresar un mes valido");
			}
			log.debug("[validarCriterio] Fin");
		}catch(Exception e){
			log.debug("[validarCriterio] Error:"+e.getMessage());
			e.printStackTrace();
		}
		
		return resultado;
	}
	/*Generamos la fecha fin segun el mes seleccionado*/
	private void obtenerFechaFin(Filtro filtro){
		log.debug("[obtenerFechaFin] Inicio");
		log.debug("[obtenerFechaFin] Mes:"+filtro.getMesFin());
		if (filtro.getMesFin().equals("-1")){
			filtro.setFechaFin(null);
		}else{
			if (filtro.getMesFin().equals(Constantes.MES_12)){
				filtro.setNombreMes(Constantes.DESC_MES_12);
			}else{
				if (filtro.getMesFin().equals(Constantes.MES_11)){
					filtro.setNombreMes(Constantes.DESC_MES_11);
				}else if (filtro.getMesFin().equals(Constantes.MES_10)){
					filtro.setNombreMes(Constantes.DESC_MES_10);
				}else if (filtro.getMesFin().equals(Constantes.MES_09)){
					filtro.setNombreMes(Constantes.DESC_MES_09);
				}else if (filtro.getMesFin().equals(Constantes.MES_08)){
					filtro.setNombreMes(Constantes.DESC_MES_08);
				}else if (filtro.getMesFin().equals(Constantes.MES_07)){
					filtro.setNombreMes(Constantes.DESC_MES_07);
				}else if (filtro.getMesFin().equals(Constantes.MES_06)){
					filtro.setNombreMes(Constantes.DESC_MES_06);
				}else if (filtro.getMesFin().equals(Constantes.MES_05)){
					filtro.setNombreMes(Constantes.DESC_MES_05);
				}else if (filtro.getMesFin().equals(Constantes.MES_04)){
					filtro.setNombreMes(Constantes.DESC_MES_04);
				}else if (filtro.getMesFin().equals(Constantes.MES_03)){
					filtro.setNombreMes(Constantes.DESC_MES_03);
				}else if (filtro.getMesFin().equals(Constantes.MES_02)){
					filtro.setNombreMes(Constantes.DESC_MES_02);
				}else {
					filtro.setNombreMes(Constantes.DESC_MES_01);
				}
			}
		}
		log.debug("[obtenerFechaFin] Nombre Mes:"+filtro.getNombreMes());
		log.debug("[obtenerFechaFin] Fin");
	}
	/*Reporte Moroso*/
	private void buscarLocalxInmueble(Model model, Filtro filtro, String path, HttpServletRequest request){
		List<LocalBean> listaLocal 				= null;
		List<RespuestaReporteBean> listaRespuesta	= null;
		RespuestaReporteBean respuestaReporteBean	= null;
		log.debug("[buscarLocalxInmueble] Inicio");
		listaLocal = this.listarLocalxInmueble(filtro);
		if (listaLocal != null && listaLocal.size()>0){
			listaRespuesta = new ArrayList<RespuestaReporteBean>();
			respuestaReporteBean = new RespuestaReporteBean();
			respuestaReporteBean.setDescripcion(Constantes.RESPUESTA_BUSQUEDA_EXITOSA);
			respuestaReporteBean.setTotalRegistro(listaLocal.size());
			listaRespuesta.add(respuestaReporteBean);
			model.addAttribute("registros", listaRespuesta);
			request.getSession().setAttribute("reporteLocalxInmueble", listaLocal);
			request.getSession().setAttribute("criterioLocal", filtro);
			log.debug("[buscarLocalxInmueble] listaRespuesta:"+listaRespuesta.size());
		}else{
			model.addAttribute("registros", respuestaReporteBean);
			request.getSession().setAttribute("reporteLocalxInmueble", listaLocal);
			log.debug("[buscarLocalxInmueble] No se encontro registros");
		}
		model.addAttribute("filtro", filtro);
		log.debug("[buscarLocalxInmueble] Fin");
	}
	
	/*Listado de los locales por inmueble*/
	private List<LocalBean> listarLocalxInmueble(Filtro filtro){
		List<LocalBean> entidades = new ArrayList<LocalBean>();
		try{
			log.debug("[listarMoroso] Inicio");
			log.debug("[listarMoroso] Anio:"+filtro.getAnio());
			log.debug("[listarMoroso] Mes:"+filtro.getMesFin());
			entidades = localDao.getReporteLocalxInmueble(filtro);
			log.debug("[listarIngresoEgreso] entidades:"+entidades.size());
			log.debug("[listarMoroso] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return entidades;
	}
	
	
	//Nombre del concepto
	/*private void obtenerNombreConcepto(Filtro filtro){
		if (filtro.getTipoCobro().equals(Constantes.TIPO_COBRO_ALQUILER)){
			filtro.setNombreCobro(Constantes.DESC_TIPO_COBRO_ALQUILER);
		}else{
			if (filtro.getTipoCobro().equals(Constantes.TIPO_COBRO_SERVICIO)){
				filtro.setNombreCobro(Constantes.DESC_TIPO_COBRO_SERVICIO);
			}else{
				if (filtro.getTipoCobro().equals(Constantes.TIPO_COBRO_LUZ)){
					filtro.setNombreCobro(Constantes.DESC_TIPO_COBRO_LUZ);
				}else{
					filtro.setNombreCobro(Constantes.DESC_TIPO_COBRO_ARBITRIO);
				}
			}
		}
	}*/
	/*Obtiene el nombre del inmueble para imprimir en el PDF*/
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
