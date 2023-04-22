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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pe.lima.sg.bean.reporte.MorosoBean;
import com.pe.lima.sg.bean.reporte.MorosoSubTotalBean;
import com.pe.lima.sg.bean.reporte.MorosoTotalBean;
import com.pe.lima.sg.bean.reporte.RespuestaReporteBean;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.reporte.pdf.MorosoPdf;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.UtilSGT;
import com.pe.lima.sg.rs.reporte.MorosoDao;

@Controller
public class MorosoAction {
	
	private static final Logger logger = LogManager.getLogger(MorosoAction.class);
	
	@Autowired
	private MorosoPdf morosoPdf;
	@Autowired
	private ServletContext context;
	@Autowired
	private MorosoDao morosoDao;

	@RequestMapping(value = "/moroso", method = RequestMethod.GET)
	public String mostrarFormulario(Model model, String path) {
		Filtro filtro = null;
		try{
			logger.debug("[mostrarFormulario] Inicio");
			path = "reporte/moroso_v2/mor_listado";
			filtro = new Filtro();
			filtro.setFechaInicio(UtilSGT.getFecha("dd/MM/yyyy"));
			filtro.setFechaFin(UtilSGT.getFecha("dd/MM/yyyy"));
			filtro.setTipoCobro(Constantes.TIPO_COBRO_ALQUILER);
			filtro.setAnio(new Integer(UtilSGT.getFecha("yyyy")));
			filtro.setCodigoEdificacion(1);
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
	
	@RequestMapping(value = "/moroso/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, Filtro filtro, String path, HttpServletRequest request) {
		path = "reporte/moroso_v2/mor_listado";
		RespuestaReporteBean respuestaReporteBean	= null;
		try{
			logger.debug("[traerRegistrosFiltrados] Inicio");
			if (this.validarCriterio(filtro,model)){
				//Realiza la busqueda de morosos				
				this.buscarMoroso(model, filtro, path, request);
			}else{
				model.addAttribute("filtro", filtro);
				model.addAttribute("registros", respuestaReporteBean);
				//Datos del reporte a imprimir en el PDF
				request.getSession().setAttribute("reporteMoroso", null);
			}
		}catch(Exception e){
			logger.debug("[traerRegistrosFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			respuestaReporteBean	= null;
		}
		logger.debug("[traerRegistrosFiltrados] Fin");
		return path;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/moroso/pdf", method = RequestMethod.GET)
	public void mostrarPdf(Model model, String path, HttpServletRequest request, HttpServletResponse response) {
		List<MorosoBean> entidades 				= null;
		Filtro criterio							= null;
		List<MorosoTotalBean> listaDataReporte 	= null;
		try{
			path = "reporte/moroso_v2/mor_listado";
			logger.debug("[mostrarPdf] Inicio");
			entidades = (List<MorosoBean>)request.getSession().getAttribute("reporteMoroso");
			if (entidades != null && !entidades.isEmpty()){
				//Preparando la data para ser mostrada (ingresos y gastos)
				listaDataReporte = this.preparaDataMoroso(entidades);
				criterio = (Filtro)request.getSession().getAttribute("criterioMoroso");
				obtenerNombreEdificio(request, criterio);
				obtenerNombreConcepto(criterio);
				String filename = "MorosoPdf"+UtilSGT.getFecha("yyyy-MM-dd kk:mm:ss").replace(':', '-');
				morosoPdf.reporteMoroso(listaDataReporte, criterio, context, request, response, filename);
				
				String fullPath = request.getServletContext().getRealPath("/resources/reports/"+filename+".pdf");
				this.fileDownload(fullPath, response, filename, ".pdf");
			}
			
			logger.debug("[mostrarPdf] Fin");
		}catch(Exception e){
			logger.debug("[mostrarPdf] Error:"+e.getMessage());
			e.printStackTrace();
		}
		
	}
	/*Validacion de los filtros de busqueda*/
	public boolean validarCriterio(Filtro filtro,Model model){
		boolean resultado = true;
		try{
			logger.debug("[validarCriterio] Inicio");
			//Edificación
			if (filtro.getCodigoEdificacion().compareTo(-1)==0){
				resultado = false;
				model.addAttribute("respuesta", "Debe seleccionar el Inmueble");
			}
			//Tipo de Pago
			if (filtro.getTipoCobro().equals("-1")){
				resultado = false;
				model.addAttribute("respuesta", "Debe seleccionar el tipo de cobro");
			}
			//Año
			if (filtro.getAnio() == null || filtro.getAnio()< 2000){
				resultado = false;
				model.addAttribute("respuesta", "Debe ingresar un año valido");
			}
			this.obtenerFechaFin(filtro);
			//Validando la fecha
			if (filtro.getFechaFin() == null || filtro.getFechaFin().length()< 8){
				resultado = false;
				model.addAttribute("respuesta", "Debe ingresar la fecha del reporte");
			}
			logger.debug("[validarCriterio] Fin");
		}catch(Exception e){
			logger.debug("[validarCriterio] Error:"+e.getMessage());
			e.printStackTrace();
		}
		
		return resultado;
	}
	/*Generamos la fecha fin segun el mes seleccionado*/
	private void obtenerFechaFin(Filtro filtro){
		logger.debug("[obtenerFechaFin] Inicio");
		logger.debug("[obtenerFechaFin] Mes:"+filtro.getMesFin());
		if (filtro.getMesFin().equals("-1")){
			filtro.setFechaFin(null);
		}else{
			if (filtro.getMesFin().equals(Constantes.MES_12_MOROSO)){
				String strAnio = filtro.getAnio().toString();
				logger.debug("[obtenerFechaFin] strAnio:"+strAnio);
				Integer intAnio = new Integer(strAnio) +1;
				filtro.setFechaFin(filtro.getMesFin() + intAnio.toString());
				logger.debug("[obtenerFechaFin] fechafin:"+filtro.getFechaFin());
				filtro.setNombreMes(Constantes.DESC_MES_12);
			}else{
				String strAnio = filtro.getAnio().toString();
				logger.debug("[obtenerFechaFin] strAnio:"+strAnio);
				filtro.setFechaFin(filtro.getMesFin() + strAnio);
				logger.debug("[obtenerFechaFin] fechafin:"+filtro.getFechaFin());
				if (filtro.getMesFin().equals(Constantes.MES_11_MOROSO)){
					filtro.setNombreMes(Constantes.DESC_MES_11);
				}else if (filtro.getMesFin().equals(Constantes.MES_10_MOROSO)){
					filtro.setNombreMes(Constantes.DESC_MES_10);
				}else if (filtro.getMesFin().equals(Constantes.MES_09_MOROSO)){
					filtro.setNombreMes(Constantes.DESC_MES_09);
				}else if (filtro.getMesFin().equals(Constantes.MES_08_MOROSO)){
					filtro.setNombreMes(Constantes.DESC_MES_08);
				}else if (filtro.getMesFin().equals(Constantes.MES_07_MOROSO)){
					filtro.setNombreMes(Constantes.DESC_MES_07);
				}else if (filtro.getMesFin().equals(Constantes.MES_06_MOROSO)){
					filtro.setNombreMes(Constantes.DESC_MES_06);
				}else if (filtro.getMesFin().equals(Constantes.MES_05_MOROSO)){
					filtro.setNombreMes(Constantes.DESC_MES_05);
				}else if (filtro.getMesFin().equals(Constantes.MES_04_MOROSO)){
					filtro.setNombreMes(Constantes.DESC_MES_04);
				}else if (filtro.getMesFin().equals(Constantes.MES_03_MOROSO)){
					filtro.setNombreMes(Constantes.DESC_MES_03);
				}else if (filtro.getMesFin().equals(Constantes.MES_02_MOROSO)){
					filtro.setNombreMes(Constantes.DESC_MES_02);
				}else {
					filtro.setNombreMes(Constantes.DESC_MES_01);
				}
			}
		}
		logger.debug("[obtenerFechaFin] Nombre Mes:"+filtro.getNombreMes());
		logger.debug("[obtenerFechaFin] Fin");
	}
	/*Reporte Moroso*/
	private void buscarMoroso(Model model, Filtro filtro, String path, HttpServletRequest request){
		List<MorosoBean> listaMoroso 				= null;
		List<RespuestaReporteBean> listaRespuesta	= null;
		RespuestaReporteBean respuestaReporteBean	= null;
		logger.debug("[buscarMoroso] Inicio");
		listaMoroso = this.listarMoroso(filtro);
		if (listaMoroso != null && listaMoroso.size()>0){
			listaRespuesta = new ArrayList<RespuestaReporteBean>();
			respuestaReporteBean = new RespuestaReporteBean();
			respuestaReporteBean.setDescripcion(Constantes.RESPUESTA_BUSQUEDA_EXITOSA);
			respuestaReporteBean.setTotalRegistro(listaMoroso.size());
			listaRespuesta.add(respuestaReporteBean);
			model.addAttribute("registros", listaRespuesta);
			request.getSession().setAttribute("reporteMoroso", listaMoroso);
			request.getSession().setAttribute("criterioMoroso", filtro);
			logger.debug("[traerRegistrosFiltrados] listaRespuesta:"+listaRespuesta.size());
		}else{
			model.addAttribute("registros", respuestaReporteBean);
			request.getSession().setAttribute("reporteMoroso", listaMoroso);
			logger.debug("[buscarMoroso] No se encontro registros");
		}
		model.addAttribute("filtro", filtro);
		logger.debug("[buscarMoroso] Fin");
	}
	
	/*Listado de los morosos*/
	private List<MorosoBean> listarMoroso(Filtro filtro){
		List<MorosoBean> entidades = new ArrayList<MorosoBean>();
		try{
			logger.debug("[listarMoroso] Inicio");
			/*if (filtro.getFechaInicio()== null || filtro.getFechaInicio().equals("")){
				filtro.setFechaInicio(UtilSGT.getDateStringFormat(UtilSGT.addDays(new Date(), -1)));
			}
			if (filtro.getFechaFin()==null || filtro.getFechaFin().equals("")){
				filtro.setFechaFin(UtilSGT.getDateStringFormat(UtilSGT.addDays(new Date(), 1)));
			}*/
			logger.debug("[listarMoroso] Fec Inicio:"+filtro.getFechaInicio());
			logger.debug("[listarMoroso] Fec Fin:"+filtro.getFechaFin());
			entidades = morosoDao.getReporteMoroso(filtro);
			logger.debug("[listarIngresoEgreso] entidades:"+entidades.size());
			logger.debug("[listarMoroso] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		return entidades;
	}
	
	
	//Nombre del concepto
	private void obtenerNombreConcepto(Filtro filtro){
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
	}
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
	private List<MorosoTotalBean> preparaDataMoroso(List<MorosoBean> entidades){
		List<MorosoTotalBean> listaDataReporte 	= null;
		MorosoTotalBean morosoTotalBean = new MorosoTotalBean();
		Map<String, MorosoSubTotalBean> mapMoroso	= new HashMap<String, MorosoSubTotalBean>();
		logger.debug("[preparaDataMoroso] Inicio");
		//Agrupamos por ListaDeuda y SubTotal
		for(MorosoBean moroso: entidades){
			MorosoSubTotalBean morosoSubTotalBean = mapMoroso.get(moroso.getNumeroTienda());
			if (morosoSubTotalBean == null){
				this.setDataMoroso(mapMoroso, moroso);
			}else{
				this.addMoroso(morosoSubTotalBean, moroso);
			}
		}
		//Totalizamos
		for (Map.Entry<String, MorosoSubTotalBean> entry : mapMoroso.entrySet()) {
		    //System.out.println(entry.getKey() + "/" + entry.getValue());
		    MorosoSubTotalBean morosoSubTotalBean = entry.getValue();
		    morosoTotalBean.setTotal(morosoTotalBean.getTotal().add(morosoSubTotalBean.getSubTotal()));
		    morosoTotalBean.setSaldo(morosoTotalBean.getSaldo().add(morosoSubTotalBean.getSubSaldo()));
		    morosoTotalBean.getListaMorosoSubTotal().add(morosoSubTotalBean);
		}
		listaDataReporte = new ArrayList<MorosoTotalBean>();
		listaDataReporte.add(morosoTotalBean);
		logger.debug("[preparaDataMoroso] Fin");
		return listaDataReporte;
	}
	/*Adiciona un elemento*/
	private void setDataMoroso(Map<String, MorosoSubTotalBean> mapMoroso, MorosoBean moroso){
		MorosoSubTotalBean morosoSubTotalBean = new MorosoSubTotalBean();
		morosoSubTotalBean.setNumeroTienda(moroso.getNumeroTienda());
		morosoSubTotalBean.setSubTotal(morosoSubTotalBean.getSubTotal().add(moroso.getTotal()));
		morosoSubTotalBean.setSubSaldo(morosoSubTotalBean.getSubSaldo().add(moroso.getSaldo()));
		morosoSubTotalBean.getListaMoroso().add(moroso);
		mapMoroso.put(moroso.getNumeroTienda(), morosoSubTotalBean);
	}
	/*Adiciona un elemento*/
	private void addMoroso(MorosoSubTotalBean morosoSubTotalBean, MorosoBean moroso){
		morosoSubTotalBean.setSubTotal(morosoSubTotalBean.getSubTotal().add(moroso.getTotal()));
		morosoSubTotalBean.setSubSaldo(morosoSubTotalBean.getSubSaldo().add(moroso.getSaldo()));
		morosoSubTotalBean.getListaMoroso().add(moroso);
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
