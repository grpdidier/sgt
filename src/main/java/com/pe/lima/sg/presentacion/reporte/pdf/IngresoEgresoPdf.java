package com.pe.lima.sg.presentacion.reporte.pdf;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.pe.lima.sg.bean.reporte.CriterioReporteBean;
import com.pe.lima.sg.bean.reporte.IngresoEgresoBean;
import com.pe.lima.sg.bean.reporte.IngresoEgresoPdfBean;
import com.pe.lima.sg.bean.reporte.ReporteIngresoEgresoBean;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.UtilSGT;

@Component
public class IngresoEgresoPdf {
	

	public void reporteIngresoEgreso_v2Efectivo(List<ReporteIngresoEgresoBean> listaIngresoEgreso, Filtro criterio,ServletContext context,HttpServletRequest request, HttpServletResponse response, String nameFile) throws IOException{
		Document document 	= new Document(PageSize.A4, 15, 15, 45, 30);
		PdfPTable table = null;
		try{
			String filePath = context.getRealPath("/resources/reports");
			File file = new File(filePath);
			boolean exists = new File(filePath).exists();
			if(!exists){
				new File(filePath).mkdirs();
			}
			PdfWriter pdf = PdfWriter.getInstance(document, new FileOutputStream(file+ "/" + nameFile+".pdf"));
			document.open();
			//Image img = Image.getInstance(new ClassPathResource("src/main/resources/static/images/iconos/logo.png").getPath());
			//document.add(img);
			Font mainFont = FontFactory.getFont("Arial",18, Color.BLACK);
			//Paragraph paragraph = this.setTitlePdf("REPORTE DIARIO - "+criterio.getFechaInicio() + " al " + criterio.getFechaFin(), mainFont);
			table =this.setTituloLogo(criterio,mainFont);
			document.add(table);
			listaIngresoEgreso = this.generateListDataPdf_v2(listaIngresoEgreso);
			for(ReporteIngresoEgresoBean ingresoEgresoPdfBean: listaIngresoEgreso){
				//Ordenando las listas
				/*if (ingresoEgresoPdfBean.getListaContrato() != null && ingresoEgresoPdfBean.getListaContrato().size() > 0)
					Collections.sort(ingresoEgresoPdfBean.getListaContrato());
				*/
				if (ingresoEgresoPdfBean.getListaAlquiler() != null && ingresoEgresoPdfBean.getListaAlquiler().size() > 0)
					Collections.sort(ingresoEgresoPdfBean.getListaAlquiler());
				
				if (ingresoEgresoPdfBean.getListaServicio() != null && ingresoEgresoPdfBean.getListaServicio().size() > 0)
					Collections.sort(ingresoEgresoPdfBean.getListaServicio());
				
				if (ingresoEgresoPdfBean.getListaLuz() != null && ingresoEgresoPdfBean.getListaLuz().size() > 0)
					Collections.sort(ingresoEgresoPdfBean.getListaLuz());
				
				if (ingresoEgresoPdfBean.getListaArbitrios() != null && ingresoEgresoPdfBean.getListaArbitrios().size() > 0)
					Collections.sort(ingresoEgresoPdfBean.getListaArbitrios());
				
				if (ingresoEgresoPdfBean.getListaGasto() != null && ingresoEgresoPdfBean.getListaGasto().size() > 0)
					Collections.sort(ingresoEgresoPdfBean.getListaGasto());
				if (ingresoEgresoPdfBean.getListaIngreso() != null && ingresoEgresoPdfBean.getListaIngreso().size() > 0)
					Collections.sort(ingresoEgresoPdfBean.getListaIngreso());

				table = this.setUsuario(ingresoEgresoPdfBean.getNombreUsuario());
				document.add(table);
				//Alquiler
				//table = this.setDataCobro_v2_Contrato(ingresoEgresoPdfBean.getListaContrato());
				if (ingresoEgresoPdfBean.getListaAlquiler() != null && ingresoEgresoPdfBean.getListaAlquiler().size()>0){
					table = this.setDataCobro_v2_Contrato(ingresoEgresoPdfBean.getListaAlquiler());
					document.add(table);
					table = this.setSubTotales(ingresoEgresoPdfBean.getAlquilerSubTotalSoles(), ingresoEgresoPdfBean.getAlquilerSubTotalDolares(),"ALQUILER ");
					document.add(table);
				}
				//Servicio
				if (ingresoEgresoPdfBean.getListaServicio() != null && ingresoEgresoPdfBean.getListaServicio().size()>0){
					table = this.setDataCobro_v2_Contrato(ingresoEgresoPdfBean.getListaServicio());
					document.add(table);
					table = this.setSubTotales(ingresoEgresoPdfBean.getServicioSubTotalSoles(), ingresoEgresoPdfBean.getServicioSubTotalDolares(),"SERVICIOS ");
					document.add(table);
				}
				//Luz
				if (ingresoEgresoPdfBean.getListaLuz() != null && ingresoEgresoPdfBean.getListaLuz().size()>0){
					table = this.setDataCobro_v2_Contrato(ingresoEgresoPdfBean.getListaLuz());
					document.add(table);
					table = this.setSubTotales(ingresoEgresoPdfBean.getLuzSubTotalSoles(), ingresoEgresoPdfBean.getLuzSubTotalDolares(),"LUZ ");
					document.add(table);
				}
				//Arbitrios
				if (ingresoEgresoPdfBean.getListaArbitrios() != null && ingresoEgresoPdfBean.getListaArbitrios().size()>0){
					table = this.setDataCobro_v2_Contrato(ingresoEgresoPdfBean.getListaArbitrios());
					document.add(table);
					table = this.setSubTotales(ingresoEgresoPdfBean.getArbitriosSubTotalSoles(), ingresoEgresoPdfBean.getArbitriosSubTotalDolares(),"ARBITRIOS ");
					document.add(table);
				}
				//Gastos
				if (ingresoEgresoPdfBean.getListaGasto() != null && ingresoEgresoPdfBean.getListaGasto().size()>0){
					table = this.setDataCobro_v2_Gasto(ingresoEgresoPdfBean.getListaGasto());
					document.add(table);
					table = this.setSubTotalesGastos(ingresoEgresoPdfBean.getGastoSubTotalSoles(), ingresoEgresoPdfBean.getGastoSubTotalDolares());
					document.add(table);
				}
				//Ingresos
				if (ingresoEgresoPdfBean.getListaIngreso() != null && ingresoEgresoPdfBean.getListaIngreso().size()>0){
					table = this.setDataCobro_v2_Ingreso(ingresoEgresoPdfBean.getListaIngreso());
					document.add(table);
					table = this.setSubTotalesIngresos(ingresoEgresoPdfBean.getIngresoSubTotalSoles(), ingresoEgresoPdfBean.getIngresoSubTotalDolares());
					document.add(table);
				}
				//Total General
				table = this.setTotalesIngresosEgresos(ingresoEgresoPdfBean.getNombreUsuario(), ingresoEgresoPdfBean.getTotalSoles(), ingresoEgresoPdfBean.getTotalDolares());
				document.add(table);
			}
			document.close();
			pdf.close();

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*Reporte Bancarizado*/
	public void reporteIngresoEgreso_v2Bancarizado(List<ReporteIngresoEgresoBean> listaIngresoEgreso, Filtro criterio,ServletContext context,HttpServletRequest request, HttpServletResponse response, String nameFile) throws IOException{
		Document document 	= new Document(PageSize.A4, 15, 15, 30, 20);
		PdfPTable table = null;
		try{
			String filePath = context.getRealPath("/resources/reports");
			File file = new File(filePath);
			boolean exists = new File(filePath).exists();
			if(!exists){
				new File(filePath).mkdirs();
			}
			PdfWriter pdf = PdfWriter.getInstance(document, new FileOutputStream(file+ "/" + nameFile+".pdf"));
			document.open();
			Font mainFont = FontFactory.getFont("Arial",18, Color.BLACK);
			table =this.setTituloLogoBancarizado(criterio,mainFont);
			document.add(table);
			listaIngresoEgreso = this.generateListDataPdf_v2(listaIngresoEgreso);
			for(ReporteIngresoEgresoBean ingresoEgresoPdfBean: listaIngresoEgreso){
				//Ordenando las listas
				/*if (ingresoEgresoPdfBean.getListaContrato() != null && ingresoEgresoPdfBean.getListaContrato().size() > 0)
					Collections.sort(ingresoEgresoPdfBean.getListaContrato());
				*/
				if (ingresoEgresoPdfBean.getListaAlquiler() != null && ingresoEgresoPdfBean.getListaAlquiler().size() > 0)
					Collections.sort(ingresoEgresoPdfBean.getListaAlquiler());
				
				if (ingresoEgresoPdfBean.getListaServicio() != null && ingresoEgresoPdfBean.getListaServicio().size() > 0)
					Collections.sort(ingresoEgresoPdfBean.getListaServicio());
				
				if (ingresoEgresoPdfBean.getListaLuz() != null && ingresoEgresoPdfBean.getListaLuz().size() > 0)
					Collections.sort(ingresoEgresoPdfBean.getListaLuz());
				
				if (ingresoEgresoPdfBean.getListaArbitrios() != null && ingresoEgresoPdfBean.getListaArbitrios().size() > 0)
					Collections.sort(ingresoEgresoPdfBean.getListaArbitrios());
				
				/*if (ingresoEgresoPdfBean.getListaGasto() != null && ingresoEgresoPdfBean.getListaGasto().size() > 0)
					Collections.sort(ingresoEgresoPdfBean.getListaGasto());*/
				if (ingresoEgresoPdfBean.getListaIngreso() != null && ingresoEgresoPdfBean.getListaIngreso().size() > 0)
					Collections.sort(ingresoEgresoPdfBean.getListaIngreso());
				
				table = this.setUsuario(ingresoEgresoPdfBean.getNombreUsuario());
				document.add(table);
				//Alquiler
				//table = this.setDataCobro_v2_Contrato(ingresoEgresoPdfBean.getListaContrato());
				if (ingresoEgresoPdfBean.getListaAlquiler() != null && ingresoEgresoPdfBean.getListaAlquiler().size()>0){
					table = this.setDataCobro_v2Bancarizado_Contrato(ingresoEgresoPdfBean.getListaAlquiler());
					document.add(table);
					table = this.setSubTotalesBancarizado(ingresoEgresoPdfBean.getAlquilerSubTotalSoles(), ingresoEgresoPdfBean.getAlquilerSubTotalDolares(),"ALQUILER ");
					document.add(table);
				}
				//Servicio
				if (ingresoEgresoPdfBean.getListaServicio() != null && ingresoEgresoPdfBean.getListaServicio().size()>0){
					table = this.setDataCobro_v2Bancarizado_Contrato(ingresoEgresoPdfBean.getListaServicio());
					document.add(table);
					table = this.setSubTotalesBancarizado(ingresoEgresoPdfBean.getServicioSubTotalSoles(), ingresoEgresoPdfBean.getServicioSubTotalDolares(),"SERVICIOS ");
					document.add(table);
				}
				//Luz
				if (ingresoEgresoPdfBean.getListaLuz() != null && ingresoEgresoPdfBean.getListaLuz().size()>0){
					table = this.setDataCobro_v2Bancarizado_Contrato(ingresoEgresoPdfBean.getListaLuz());
					document.add(table);
					table = this.setSubTotalesBancarizado(ingresoEgresoPdfBean.getLuzSubTotalSoles(), ingresoEgresoPdfBean.getLuzSubTotalDolares(),"LUZ ");
					document.add(table);
				}
				//Arbitrios
				if (ingresoEgresoPdfBean.getListaArbitrios() != null && ingresoEgresoPdfBean.getListaArbitrios().size()>0){
					table = this.setDataCobro_v2Bancarizado_Contrato(ingresoEgresoPdfBean.getListaArbitrios());
					document.add(table);
					table = this.setSubTotalesBancarizado(ingresoEgresoPdfBean.getArbitriosSubTotalSoles(), ingresoEgresoPdfBean.getArbitriosSubTotalDolares(),"ARBITRIOS ");
					document.add(table);
				}
				//TODO: COMENTADO HASTA VALIDAR CON JHONNY SI SE  DEBE MOSTRAR Y CON QUE CRITERIO --> 2021.03.18 Solicito ingresos bancarizados
				//Gastos
				/*if (ingresoEgresoPdfBean.getListaGasto() != null && ingresoEgresoPdfBean.getListaGasto().size()>0){
					table = this.setDataCobro_v2_GastoBancarizado(ingresoEgresoPdfBean.getListaGasto());
					document.add(table);
					table = this.setSubTotalesGastosBancarizado(ingresoEgresoPdfBean.getGastoSubTotalSoles(), ingresoEgresoPdfBean.getGastoSubTotalDolares());
					document.add(table);
				}*/
				//Ingresos
				if (ingresoEgresoPdfBean.getListaIngreso() != null && ingresoEgresoPdfBean.getListaIngreso().size()>0){
					table = this.setDataCobro_v2_IngresoBancarizado(ingresoEgresoPdfBean.getListaIngreso());
					document.add(table);
					table = this.setSubTotalesIngresosBancarizado(ingresoEgresoPdfBean.getIngresoSubTotalSoles(), ingresoEgresoPdfBean.getIngresoSubTotalDolares());
					document.add(table);
				}
				//Total General
				table = this.setTotalesIngresosEgresosBancarizado(ingresoEgresoPdfBean.getNombreUsuario(), ingresoEgresoPdfBean.getTotalSoles(), ingresoEgresoPdfBean.getTotalDolares());
				document.add(table);
			}
			document.close();
			pdf.close();

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/*Reporte Fuera de Fecha*/
	public void reporteFueraFecha(List<ReporteIngresoEgresoBean> listaCobro, Filtro criterio,ServletContext context,HttpServletRequest request, HttpServletResponse response, String nameFile) throws IOException{
		Document document 	= new Document(PageSize.A4, 15, 15, 30, 20);
		PdfPTable table = null;
		try{
			String filePath = context.getRealPath("/resources/reports");
			File file = new File(filePath);
			boolean exists = new File(filePath).exists();
			if(!exists){
				new File(filePath).mkdirs();
			}
			PdfWriter pdf = PdfWriter.getInstance(document, new FileOutputStream(file+ "/" + nameFile+".pdf"));
			document.open();
			Font mainFont = FontFactory.getFont("Arial",18, Color.BLACK);
			table =this.setTituloLogoFueraFecha(criterio,mainFont);
			document.add(table);
			listaCobro = this.generateListDataPdf_v2(listaCobro);
			for(ReporteIngresoEgresoBean ingresoEgresoPdfBean: listaCobro){
				//Ordenando las listas
				if (ingresoEgresoPdfBean.getListaAlquiler() != null && ingresoEgresoPdfBean.getListaAlquiler().size() > 0)
					Collections.sort(ingresoEgresoPdfBean.getListaAlquiler());
				
				if (ingresoEgresoPdfBean.getListaServicio() != null && ingresoEgresoPdfBean.getListaServicio().size() > 0)
					Collections.sort(ingresoEgresoPdfBean.getListaServicio());
				
				if (ingresoEgresoPdfBean.getListaLuz() != null && ingresoEgresoPdfBean.getListaLuz().size() > 0)
					Collections.sort(ingresoEgresoPdfBean.getListaLuz());
				
				if (ingresoEgresoPdfBean.getListaArbitrios() != null && ingresoEgresoPdfBean.getListaArbitrios().size() > 0)
					Collections.sort(ingresoEgresoPdfBean.getListaArbitrios());
				
				table = this.setInmueble(ingresoEgresoPdfBean.getNombreInmueble());
				document.add(table);
				//Alquiler
				if (ingresoEgresoPdfBean.getListaAlquiler() != null && ingresoEgresoPdfBean.getListaAlquiler().size()>0){
					table = this.setDataCobroFueraFecha(ingresoEgresoPdfBean.getListaAlquiler());
					document.add(table);
					table = this.setSubTotalesFueraFecha(ingresoEgresoPdfBean.getAlquilerSubTotalSoles(), ingresoEgresoPdfBean.getAlquilerSubTotalDolares(),"ALQUILER ");
					document.add(table);
				}
				//Servicio
				if (ingresoEgresoPdfBean.getListaServicio() != null && ingresoEgresoPdfBean.getListaServicio().size()>0){
					table = this.setDataCobroFueraFecha(ingresoEgresoPdfBean.getListaServicio());
					document.add(table);
					table = this.setSubTotalesFueraFecha(ingresoEgresoPdfBean.getServicioSubTotalSoles(), ingresoEgresoPdfBean.getServicioSubTotalDolares(),"SERVICIOS ");
					document.add(table);
				}
				//Luz
				if (ingresoEgresoPdfBean.getListaLuz() != null && ingresoEgresoPdfBean.getListaLuz().size()>0){
					table = this.setDataCobroFueraFecha(ingresoEgresoPdfBean.getListaLuz());
					document.add(table);
					table = this.setSubTotalesFueraFecha(ingresoEgresoPdfBean.getLuzSubTotalSoles(), ingresoEgresoPdfBean.getLuzSubTotalDolares(),"LUZ ");
					document.add(table);
				}
				//Arbitrios
				if (ingresoEgresoPdfBean.getListaArbitrios() != null && ingresoEgresoPdfBean.getListaArbitrios().size()>0){
					table = this.setDataCobroFueraFecha(ingresoEgresoPdfBean.getListaArbitrios());
					document.add(table);
					table = this.setSubTotalesFueraFecha(ingresoEgresoPdfBean.getArbitriosSubTotalSoles(), ingresoEgresoPdfBean.getArbitriosSubTotalDolares(),"ARBITRIOS ");
					document.add(table);
				}
				
				//Total General
				table = this.setTotalesIngresosEgresosBancarizado(" - " + ingresoEgresoPdfBean.getNombreInmueble(), ingresoEgresoPdfBean.getTotalSoles(), ingresoEgresoPdfBean.getTotalDolares());
				document.add(table);
			}
			document.close();
			pdf.close();

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	/*Nombre del usuario*/
	public PdfPTable setTituloLogo ( Filtro criterio,Font mainFont) throws DocumentException{
		PdfPTable table = new PdfPTable(2);
		try{
			Image img = Image.getInstance(new ClassPathResource("src/main/resources/static/images/iconos/logo.png").getPath());
			table.setWidthPercentage(100);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(0);
			float[] columnWidths = {10f, 4f};
			table.setWidths(columnWidths);
			Paragraph paragraph = this.setTitlePdf(criterio.getNombre()+ "\n" + "REPORTE DIARIO al "+criterio.getFechaInicio() + this.valiaTipoReporte(criterio) , mainFont);
			PdfPCell headerCell = new PdfPCell(paragraph); //this.setHeaderPdf(Nombre, tableHeader);
			//PdfPCell headerCell = null;
			//headerCell = new PdfPCell(new Paragraph(nameHeader, tableHeader));
			headerCell.setBorderColor(Color.WHITE);
			headerCell.setPaddingLeft(10);
			headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			headerCell.setBackgroundColor(Color.WHITE);
			headerCell.setExtraParagraphSpace(5);
			
			table.addCell(headerCell);
			
			headerCell = new PdfPCell(img);
			headerCell.setBorderColor(Color.WHITE);
			headerCell.setPaddingLeft(10);
			headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			headerCell.setVerticalAlignment(Element.ALIGN_CENTER);
			headerCell.setBackgroundColor(Color.WHITE);
			headerCell.setExtraParagraphSpace(5);
		
			table.addCell(headerCell);
		}catch(Exception e){
			e.printStackTrace();
		}
		return table;
	}
	
	public PdfPTable setTituloLogoBancarizado ( Filtro criterio,Font mainFont) throws DocumentException{
		PdfPTable table = new PdfPTable(2);
		try{
			Image img = Image.getInstance(new ClassPathResource("src/main/resources/static/images/iconos/logo.png").getPath());
			table.setWidthPercentage(100);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(0);
			float[] columnWidths = {10f, 4f};
			table.setWidths(columnWidths);
			Paragraph paragraph = this.setTitlePdf(criterio.getNombre()+ "\n" + "REPORTE DIARIO al "+criterio.getFechaInicio() + this.valiaTipoReporteBancarizado(criterio) + "\n BANCARIZADO", mainFont);
			PdfPCell headerCell = new PdfPCell(paragraph); //this.setHeaderPdf(Nombre, tableHeader);
			//PdfPCell headerCell = null;
			//headerCell = new PdfPCell(new Paragraph(nameHeader, tableHeader));
			headerCell.setBorderColor(Color.WHITE);
			headerCell.setPaddingLeft(10);
			headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			headerCell.setBackgroundColor(Color.WHITE);
			headerCell.setExtraParagraphSpace(5);
			
			table.addCell(headerCell);
			
			headerCell = new PdfPCell(img);
			headerCell.setBorderColor(Color.WHITE);
			headerCell.setPaddingLeft(10);
			headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			headerCell.setVerticalAlignment(Element.ALIGN_CENTER);
			headerCell.setBackgroundColor(Color.WHITE);
			headerCell.setExtraParagraphSpace(5);
		
			table.addCell(headerCell);
		}catch(Exception e){
			e.printStackTrace();
		}
		return table;
	}
	
	public PdfPTable setTituloLogoFueraFecha ( Filtro criterio,Font mainFont) throws DocumentException{
		PdfPTable table = new PdfPTable(2);
		try{
			Image img = Image.getInstance(new ClassPathResource("src/main/resources/static/images/iconos/logo.png").getPath());
			table.setWidthPercentage(100);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(0);
			float[] columnWidths = {10f, 4f};
			table.setWidths(columnWidths);
			Paragraph paragraph = this.setTitlePdf("COBROS FUERA DE FECHA \n "+criterio.getFechaInicio() +" al "+criterio.getFechaFin(), mainFont);
			PdfPCell headerCell = new PdfPCell(paragraph); //this.setHeaderPdf(Nombre, tableHeader);
			//PdfPCell headerCell = null;
			//headerCell = new PdfPCell(new Paragraph(nameHeader, tableHeader));
			headerCell.setBorderColor(Color.WHITE);
			headerCell.setPaddingLeft(10);
			headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			headerCell.setBackgroundColor(Color.WHITE);
			headerCell.setExtraParagraphSpace(5);
			
			table.addCell(headerCell);
			
			headerCell = new PdfPCell(img);
			headerCell.setBorderColor(Color.WHITE);
			headerCell.setPaddingLeft(10);
			headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			headerCell.setVerticalAlignment(Element.ALIGN_CENTER);
			headerCell.setBackgroundColor(Color.WHITE);
			headerCell.setExtraParagraphSpace(5);
		
			table.addCell(headerCell);
		}catch(Exception e){
			e.printStackTrace();
		}
		return table;
	}

	private String valiaTipoReporte(Filtro criterio){
		String resultado = null;
		String tipo = criterio.getTipoConcepto();
		if (tipo.equals("E")){
			resultado = " - Egresos EFECTIVO";
			
		}else if (tipo.equals("I")){
			resultado = " - Ingresos EFECTIVO";
		} else{
			resultado = " - Todos EFECTIVO";
		}
		return resultado;
	}
	private String valiaTipoReporteBancarizado(Filtro criterio){
		String resultado = null;
		String tipo = criterio.getTipoConcepto();
		if (tipo.equals("E")){
			resultado = " - Egresos";
			
		}else if (tipo.equals("I")){
			resultado = " - Ingresos";
		} else{
			resultado = " - Todos";
		}
		return resultado;
	}
	/*Nombre del usuario*/
	public PdfPTable setUsuario (String Nombre) throws DocumentException{
		PdfPTable table = new PdfPTable(2);

		table.setWidthPercentage(100);
		table.setSpacingBefore(10f);
		table.setSpacingAfter(0);
		Font tableHeader = FontFactory.getFont("Arial",10,Color.BLACK);
		float[] columnWidths = {7f, 7f};
		table.setWidths(columnWidths);
		PdfPCell headerCell = this.setHeaderPdf(Nombre, tableHeader);
		table.addCell(headerCell);
		headerCell = this.setDataPdfNoBorder("", tableHeader, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
		table.addCell(headerCell);

		return table;
	}
	/*Nombre del Inmueble*/
	public PdfPTable setInmueble (String Nombre) throws DocumentException{
		PdfPTable table = new PdfPTable(2);

		table.setWidthPercentage(100);
		table.setSpacingBefore(10f);
		table.setSpacingAfter(0);
		Font tableHeader = FontFactory.getFont("Arial",10,Color.BLACK);
		float[] columnWidths = {7f, 7f};
		table.setWidths(columnWidths);
		PdfPCell headerCell = this.setHeaderPdf(Nombre, tableHeader);
		table.addCell(headerCell);
		headerCell = this.setDataPdfNoBorder("", tableHeader, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
		table.addCell(headerCell);

		return table;
	}
	/*Sub Totales*/
	public PdfPTable setSubTotales (BigDecimal subTotalSoles, BigDecimal subTotalDolares, String Nombre) throws DocumentException{
		PdfPTable table = new PdfPTable(4);
		PdfPCell cellData 	= null;
		Font tableBody = FontFactory.getFont("Arial",9,Color.BLACK);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(10);
		float[] columnWidths = {9.5f, 1.5f, 1.5f, 1.5f};
		table.setWidths(columnWidths);
		cellData = this.setDataPdf(Nombre + "Sub Totales Soles S/ ", tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(subTotalSoles.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf("Dolares $", tableBody, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(subTotalDolares.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		return table;
	}
	/*Sub Totales*/
	public PdfPTable setSubTotalesBancarizado (BigDecimal subTotalSoles, BigDecimal subTotalDolares, String Nombre) throws DocumentException{
		PdfPTable table = new PdfPTable(4);
		PdfPCell cellData 	= null;
		Font tableBody = FontFactory.getFont("Arial",9,Color.BLACK);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(10);
		float[] columnWidths = {10.2f, 1.2f, 1.4f, 1.2f};
		table.setWidths(columnWidths);
		cellData = this.setDataPdf(Nombre + "Sub Totales Soles S/ ", tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(subTotalSoles.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf("Dolares $", tableBody, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(subTotalDolares.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		return table;
	}
	/*Sub Totales*/
	public PdfPTable setSubTotalesFueraFecha (BigDecimal subTotalSoles, BigDecimal subTotalDolares, String Nombre) throws DocumentException{
		PdfPTable table = new PdfPTable(4);
		PdfPCell cellData 	= null;
		Font tableBody = FontFactory.getFont("Arial",9,Color.BLACK);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(10);
		float[] columnWidths = {9.9f, 1.4f, 1.3f, 1.4f};
		table.setWidths(columnWidths);
		cellData = this.setDataPdf(Nombre + "Sub Totales Soles S/ ", tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(subTotalSoles.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf("Dolares $", tableBody, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(subTotalDolares.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		return table;
	}
	public PdfPTable setSubTotalesGastos (BigDecimal subTotalSoles, BigDecimal subTotalDolares) throws DocumentException{
		PdfPTable table = new PdfPTable(4);
		PdfPCell cellData 	= null;
		Font tableBody = FontFactory.getFont("Arial",9,Color.BLACK);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(10);
		float[] columnWidths = {9.5f, 1.5f, 1.5f, 1.5f};
		table.setWidths(columnWidths);
		cellData = this.setDataPdf("GASTOS Sub Totales Soles S/ ", tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(subTotalSoles.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf("Dolares $", tableBody, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(subTotalDolares.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);

		return table;
	}
	public PdfPTable setSubTotalesGastosBancarizado (BigDecimal subTotalSoles, BigDecimal subTotalDolares) throws DocumentException{
		PdfPTable table = new PdfPTable(4);
		PdfPCell cellData 	= null;
		Font tableBody = FontFactory.getFont("Arial",9,Color.BLACK);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(10);
		//float[] columnWidths = {9.5f, 1.5f, 1.5f, 1.5f};
		float[] columnWidths = {9.8f, 1.4f, 1.4f, 1.4f};
		table.setWidths(columnWidths);
		cellData = this.setDataPdf("GASTOS Sub Totales Soles S/ ", tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(subTotalSoles.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf("Dolares $", tableBody, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(subTotalDolares.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);

		return table;
	}
	public PdfPTable setSubTotalesIngresos (BigDecimal subTotalSoles, BigDecimal subTotalDolares) throws DocumentException{
		PdfPTable table = new PdfPTable(4);
		PdfPCell cellData 	= null;
		Font tableBody = FontFactory.getFont("Arial",9,Color.BLACK);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(10);
		float[] columnWidths = {9.5f, 1.5f, 1.5f, 1.5f};
		table.setWidths(columnWidths);
		cellData = this.setDataPdf("INGRESOS Sub Totales Soles S/ ", tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(subTotalSoles.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf("Dolares $", tableBody, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(subTotalDolares.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);

		return table;
	}
	public PdfPTable setSubTotalesIngresosBancarizado (BigDecimal subTotalSoles, BigDecimal subTotalDolares) throws DocumentException{
		PdfPTable table = new PdfPTable(4);
		PdfPCell cellData 	= null;
		Font tableBody = FontFactory.getFont("Arial",9,Color.BLACK);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(10);
		//float[] columnWidths = {9.5f, 1.5f, 1.5f, 1.5f};
		float[] columnWidths = {9.8f, 1.4f, 1.4f, 1.4f};
		table.setWidths(columnWidths);
		cellData = this.setDataPdf("INGRESOS Sub Totales Soles S/ ", tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(subTotalSoles.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf("Dolares $", tableBody, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(subTotalDolares.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);

		return table;
	}
	public PdfPTable setSubTotales (BigDecimal subTotalSoles, BigDecimal subTotalDolares) throws DocumentException{
		PdfPTable table = new PdfPTable(4);
		PdfPCell cellData 	= null;
		Font tableBody = FontFactory.getFont("Arial",9,Color.BLACK);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(10);
		float[] columnWidths = {9.5f, 1.5f, 1.5f, 1.5f};
		table.setWidths(columnWidths);
		cellData = this.setDataPdf("INGRESOS Sub Totales Soles S/ ", tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(subTotalSoles.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf("Dolares $", tableBody, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(subTotalDolares.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);

		return table;
	}
	/*Totales por usuario*/
	public PdfPTable setTotalesIngresosEgresos (String nombreUsuario, BigDecimal totalSoles, BigDecimal totalDolares) throws DocumentException{
		PdfPTable table = new PdfPTable(4);
		PdfPCell cellData 	= null;
		Font tableBody = FontFactory.getFont("Arial",9,Color.BLACK);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(10);
		float[] columnWidths = {9.5f, 1.5f, 1.5f, 1.5f};
		table.setWidths(columnWidths);
		cellData = this.setDataPdf("( ALQUILER Y SERVICIO + INGRESOS - GASTOS) " + nombreUsuario, tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(totalSoles.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf("Dolares $", tableBody, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(totalDolares.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);

		return table;
	}
	
	public PdfPTable setTotalesIngresosEgresosBancarizado (String nombreUsuario, BigDecimal totalSoles, BigDecimal totalDolares) throws DocumentException{
		PdfPTable table = new PdfPTable(4);
		PdfPCell cellData 	= null;
		Font tableBody = FontFactory.getFont("Arial",9,Color.BLACK);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(10);
		float[] columnWidths = {9.9f, 1.4f, 1.3f, 1.4f};
		table.setWidths(columnWidths);
		//cellData = this.setDataPdf("( ALQUILER Y SERVICIO + INGRESOS - GASTOS) " + nombreUsuario, tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		cellData = this.setDataPdf("( ALQUILER + SERVICIO + LUZ + ARBITRIOS) " + nombreUsuario, tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(totalSoles.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf("Dolares $", tableBody, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(totalDolares.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);

		return table;
	}

	public PdfPTable setDataCobro_v2_Ingreso(List<IngresoEgresoBean> listaIngresoEgreso) throws DocumentException{
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(0);
		PdfPCell cellData 	= null;
		Integer count 		= 1;

		Font tableHeader = FontFactory.getFont("Arial",10,Color.BLACK);
		Font tableBody = FontFactory.getFont("Arial",9,Color.BLACK);

		float[] columnWidths = {1f, 1.5f, 7f, 1.5f, 1.5f, 1.5f};
		table.setWidths(columnWidths);

		/*Titulos de la cabecera*/
		PdfPCell headerCell = this.setHeaderPdf("Nro", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Ingreso", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Concepto", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Soles S/", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Tipo", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Dolares $", tableHeader);
		table.addCell(headerCell);


		/*Datos de la tabla*/
		for(IngresoEgresoBean ingreso:listaIngresoEgreso ){
			cellData = this.setDataPdf(count.toString(), tableBody, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
			table.addCell(cellData);
			count++;

			//cellData = this.setDataPdf(ingreso.getNumero(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			cellData = this.setDataPdf(ingreso.getDescripcion(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getNota(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getMonto_soles().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getTipo_cambio().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getMonto_dolares().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

		}
		return table;
	}
	
	public PdfPTable setDataCobro_v2_IngresoBancarizado(List<IngresoEgresoBean> listaIngresoEgreso) throws DocumentException{
		PdfPTable table = new PdfPTable(9);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(0);
		PdfPCell cellData 	= null;
		Integer count 		= 1;

		Font tableHeader = FontFactory.getFont("Arial",10,Color.BLACK);
		Font tableBody = FontFactory.getFont("Arial",9,Color.BLACK);
		Font tableBancarizado = FontFactory.getFont("Arial",7,Color.BLACK);
		//float[] columnWidths = {1f, 1.5f, 7f, 1.5f, 1.5f, 1.5f};
		//float[] columnWidths = {0.7f, 1.5f, 7.6f, 1.4f, 1.4f, 1.4f};
		float[] columnWidths = {0.7f, 1.2f, 3.3f, 1.8f, 1.4f, 1.4f, 1.4f, 1.4f, 1.4f};
		table.setWidths(columnWidths);

		/*Titulos de la cabecera*/
		/*PdfPCell headerCell = this.setHeaderPdf("Nro", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Ingreso", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Concepto", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Soles S/", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Tipo", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Dolares $", tableHeader);
		table.addCell(headerCell);*/
		
		/*Titulos de la cabecera*/
		PdfPCell headerCell = this.setHeaderPdf("Nro", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Ingreso", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Concepto", tableHeader);
		table.addCell(headerCell);
		
		//Datos propios de la bancarizacion
		headerCell = this.setHeaderPdf("Operación", tableHeader);
		table.addCell(headerCell);
		headerCell = this.setHeaderPdf("Número", tableHeader);
		table.addCell(headerCell);
		headerCell = this.setHeaderPdf("Fecha", tableHeader);
		table.addCell(headerCell);

		
		headerCell = this.setHeaderPdf("Soles S/", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Tipo", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Dolares $", tableHeader);
		table.addCell(headerCell);


		/*Datos de la tabla*/
		for(IngresoEgresoBean ingreso:listaIngresoEgreso ){
			cellData = this.setDataPdf(count.toString(), tableBody, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
			table.addCell(cellData);
			count++;

			cellData = this.setDataPdf(ingreso.getNumero(), tableBancarizado, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getNota(), tableBancarizado, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
			//Datos de Bancarizacion
			cellData = this.setDataPdf(ingreso.getTipo_bancarizado(), tableBancarizado, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
			cellData = this.setDataPdf(ingreso.getNumero_operacion(), tableBancarizado, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
			cellData = this.setDataPdf(UtilSGT.getDatetoString2(ingreso.getFecha_operacion()), tableBancarizado, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
			cellData = this.setDataPdf(ingreso.getMonto_soles().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getTipo_cambio().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getMonto_dolares().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

		}
		return table;
	}
	public PdfPTable setDataCobro_v2_Contrato(List<IngresoEgresoBean> listaIngresoEgreso) throws DocumentException{
		PdfPTable table = new PdfPTable(7);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(0);
		PdfPCell cellData 	= null;
		Integer count 		= 1;

		Font tableHeader = FontFactory.getFont("Arial",9,Color.BLACK);
		Font tableBody = FontFactory.getFont("Arial",8,Color.BLACK);

		float[] columnWidths = {1f, 1.5f, 1.5f, 5.5f, 1.5f, 1.5f, 1.5f};
		table.setWidths(columnWidths);

		/*Titulos de la cabecera*/
		PdfPCell headerCell = this.setHeaderPdf("Nro", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Tienda", tableHeader);
		table.addCell(headerCell);
		
		headerCell = this.setHeaderPdf("RUC", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Concepto", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Soles S/", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Tipo", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Dolares $", tableHeader);
		table.addCell(headerCell);


		/*Datos de la tabla*/
		for(IngresoEgresoBean ingreso:listaIngresoEgreso ){
			cellData = this.setDataPdf(count.toString(), tableBody, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
			table.addCell(cellData);
			count++;

			cellData = this.setDataPdf(ingreso.getNumero(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
			cellData = this.setDataPdf(ingreso.getNumero_ruc(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getNota(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getMonto_soles().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getTipo_cambio().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getMonto_dolares().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

		}
		return table;
	}
	
	public PdfPTable setDataCobro_v2Bancarizado_Contrato(List<IngresoEgresoBean> listaIngresoEgreso) throws DocumentException{
		PdfPTable table = new PdfPTable(10);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(0);
		PdfPCell cellData 	= null;
		Integer count 		= 1;

		Font tableHeader = FontFactory.getFont("Arial",8,Color.BLACK);
		Font tableBody = FontFactory.getFont("Arial",7,Color.BLACK);
		Font tableBancarizado = FontFactory.getFont("Arial",7,Color.BLACK);
		float[] columnWidths = {0.6f, 1.1f, 1.3f, 3.0f, 1.8f, 1.2f, 1.2f, 1.2f, 1.4f, 1.2f};
		table.setWidths(columnWidths);

		/*Titulos de la cabecera*/
		PdfPCell headerCell = this.setHeaderPdf("Nro", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Tienda", tableHeader);
		table.addCell(headerCell);
		
		headerCell = this.setHeaderPdf("RUC", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Concepto", tableHeader);
		table.addCell(headerCell);
		
		//Datos propios de la bancarizacion
		headerCell = this.setHeaderPdf("Operación", tableHeader);
		table.addCell(headerCell);
		headerCell = this.setHeaderPdf("Número", tableHeader);
		table.addCell(headerCell);
		headerCell = this.setHeaderPdf("Fecha", tableHeader);
		table.addCell(headerCell);

		
		headerCell = this.setHeaderPdf("Soles S/", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Tipo", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Dolares $", tableHeader);
		table.addCell(headerCell);
		
		

		/*Datos de la tabla*/
		for(IngresoEgresoBean ingreso:listaIngresoEgreso ){
			cellData = this.setDataPdf(count.toString(), tableBody, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
			table.addCell(cellData);
			count++;

			cellData = this.setDataPdf(ingreso.getNumero(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
			cellData = this.setDataPdf(ingreso.getNumero_ruc(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getNota(), tableBancarizado, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			//Datos de Bancarizacion
			cellData = this.setDataPdf(ingreso.getTipo_bancarizado(), tableBancarizado, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
			cellData = this.setDataPdf(ingreso.getNumero_operacion(), tableBancarizado, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
			cellData = this.setDataPdf(UtilSGT.getDatetoString2(ingreso.getFecha_operacion()), tableBancarizado, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getMonto_soles().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getTipo_cambio().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getMonto_dolares().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
		
		}
		return table;
	}
	
	public PdfPTable setDataCobroFueraFecha(List<IngresoEgresoBean> listaIngresoEgreso) throws DocumentException{
		PdfPTable table = new PdfPTable(10);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(0);
		PdfPCell cellData 	= null;
		Integer count 		= 1;

		Font tableHeader = FontFactory.getFont("Arial",10,Color.BLACK);
		Font tableBody = FontFactory.getFont("Arial",9,Color.BLACK);
		Font tableBancarizado = FontFactory.getFont("Arial",7,Color.BLACK);
		float[] columnWidths = {0.7f, 1.2f, 1.3f, 3.0f, 0.9f, 1.5f, 1.3f, 1.4f, 1.3f, 1.4f};
		table.setWidths(columnWidths);

		/*Titulos de la cabecera*/
		PdfPCell headerCell = this.setHeaderPdf("Nro", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Tienda", tableHeader);
		table.addCell(headerCell);
		
		headerCell = this.setHeaderPdf("RUC", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Concepto", tableHeader);
		table.addCell(headerCell);
		
		headerCell = this.setHeaderPdf("Tipo", tableHeader);
		table.addCell(headerCell);
		
		//Datos propios de la bancarizacion
		headerCell = this.setHeaderPdf("Usuario", tableHeader);
		table.addCell(headerCell);
		headerCell = this.setHeaderPdf("Fecha", tableHeader);
		table.addCell(headerCell);

		
		headerCell = this.setHeaderPdf("Soles S/", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Fecha Cobro", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Dolares $", tableHeader);
		table.addCell(headerCell);
		
		

		/*Datos de la tabla*/
		for(IngresoEgresoBean ingreso:listaIngresoEgreso ){
			cellData = this.setDataPdf(count.toString(), tableBancarizado, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
			table.addCell(cellData);
			count++;

			cellData = this.setDataPdf(ingreso.getNumero(), tableBancarizado, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
			cellData = this.setDataPdf(ingreso.getNumero_ruc(), tableBancarizado, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getNota(), tableBancarizado, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
			cellData = this.setDataPdf(ingreso.getTipo_pago(), tableBancarizado, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
			table.addCell(cellData);

			//Datos de Usuario
			cellData = this.setDataPdf(ingreso.getNombre(), tableBancarizado, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
			cellData = this.setDataPdf(UtilSGT.getDatetoString2(ingreso.getFecha_creacion()), tableBancarizado, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
			cellData = this.setDataPdf(ingreso.getMonto_soles().toString(), tableBancarizado, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(UtilSGT.getDatetoString2(ingreso.getFecha_cobro()), tableBancarizado, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getMonto_dolares().toString(), tableBancarizado, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
		
		}
		return table;
	}
	
	public PdfPTable setDataCobro_v2_Gasto(List<IngresoEgresoBean> listaIngresoEgreso) throws DocumentException{
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(0);
		PdfPCell cellData 	= null;
		Integer count 		= 1;

		Font tableHeader = FontFactory.getFont("Arial",10,Color.BLACK);
		Font tableBody = FontFactory.getFont("Arial",9,Color.BLACK);

		float[] columnWidths = {1f, 1.5f, 7f, 1.5f, 1.5f, 1.5f};
		table.setWidths(columnWidths);

		/*Titulos de la cabecera*/
		PdfPCell headerCell = this.setHeaderPdf("Nro", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Gasto", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Concepto", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Soles S/", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Tipo", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Dolares $", tableHeader);
		table.addCell(headerCell);


		/*Datos de la tabla*/
		for(IngresoEgresoBean ingreso:listaIngresoEgreso ){
			cellData = this.setDataPdf(count.toString(), tableBody, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
			table.addCell(cellData);
			count++;

			//cellData = this.setDataPdf(ingreso.getNumero(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			cellData = this.setDataPdf(ingreso.getDescripcion(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getNota(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getMonto_soles().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getTipo_cambio().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getMonto_dolares().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

		}
		return table;
	}
	
	public PdfPTable setDataCobro_v2_GastoBancarizado(List<IngresoEgresoBean> listaIngresoEgreso) throws DocumentException{
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(0);
		PdfPCell cellData 	= null;
		Integer count 		= 1;

		Font tableHeader = FontFactory.getFont("Arial",10,Color.BLACK);
		Font tableBody = FontFactory.getFont("Arial",9,Color.BLACK);
		Font tableBancarizado = FontFactory.getFont("Arial",7,Color.BLACK);
		//float[] columnWidths = {1f, 1.5f, 7f, 1.5f, 1.5f, 1.5f};
		float[] columnWidths = {0.7f, 1.5f, 7.6f, 1.4f, 1.4f, 1.4f};
		table.setWidths(columnWidths);

		/*Titulos de la cabecera*/
		PdfPCell headerCell = this.setHeaderPdf("Nro", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Gasto", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Concepto", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Soles S/", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Tipo", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Dolares $", tableHeader);
		table.addCell(headerCell);


		/*Datos de la tabla*/
		for(IngresoEgresoBean ingreso:listaIngresoEgreso ){
			cellData = this.setDataPdf(count.toString(), tableBody, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
			table.addCell(cellData);
			count++;

			cellData = this.setDataPdf(ingreso.getNumero(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getNota(), tableBancarizado, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getMonto_soles().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getTipo_cambio().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getMonto_dolares().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

		}
		return table;
	}
	/*Data de los cobros*/
	public PdfPTable setDataCobro(List<IngresoEgresoBean> listaIngresoEgreso) throws DocumentException{
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(0);
		PdfPCell cellData 	= null;
		Integer count 		= 1;

		Font tableHeader = FontFactory.getFont("Arial",10,Color.BLACK);
		Font tableBody = FontFactory.getFont("Arial",9,Color.BLACK);

		float[] columnWidths = {1f, 1.5f, 7f, 1.5f, 1.5f, 1.5f};
		table.setWidths(columnWidths);

		/*Titulos de la cabecera*/
		PdfPCell headerCell = this.setHeaderPdf("Nro", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Tienda", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Concepto", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Soles S/", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Tipo", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("Dolares $", tableHeader);
		table.addCell(headerCell);


		/*Datos de la tabla*/
		for(IngresoEgresoBean ingreso:listaIngresoEgreso ){
			cellData = this.setDataPdf(count.toString(), tableBody, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
			table.addCell(cellData);
			count++;

			cellData = this.setDataPdf(ingreso.getNumero(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getNota(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getMonto_soles().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getTipo_cambio().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getMonto_dolares().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

		}
		return table;
	}

	public void reporteIngresoEgreso(List<IngresoEgresoBean> listaIngresoEgreso, CriterioReporteBean criterio,ServletContext context,HttpServletRequest request, HttpServletResponse response, String nameFile) throws IOException{
		Document document 	= new Document(PageSize.A4, 15, 15, 45, 30);
		PdfPTable table = null;
		List<IngresoEgresoPdfBean> listDataFinal = null;
		try{
			String filePath = context.getRealPath("/resources/reports");
			File file = new File(filePath);
			boolean exists = new File(filePath).exists();
			if(!exists){
				new File(filePath).mkdirs();
			}
			PdfWriter pdf = PdfWriter.getInstance(document, new FileOutputStream(file+ "/" + nameFile+".pdf"));
			document.open();
			Font mainFont = FontFactory.getFont("Arial",10, Color.BLACK);
			Paragraph paragraph = this.setTitlePdf("REPORTE DIARIO - "+criterio.getFechaInicio() + " al " + criterio.getFechaFin(), mainFont);
			/*Image png = Image.getInstance("./target/classes/static/images/iconos/logo.png");
			png.setAbsolutePosition(171, 250);
			png.scalePercent(20);
			document.add(png);*/
			document.add(paragraph);

			listDataFinal = this.generateListDataPdf(listaIngresoEgreso);
			for(IngresoEgresoPdfBean ingresoEgresoPdfBean: listDataFinal){
				table = this.setUsuario(ingresoEgresoPdfBean.getNombreUsuario());
				document.add(table);
				table = this.setDataCobro(ingresoEgresoPdfBean.getListaDatos());
				document.add(table);
				table = this.setSubTotales(ingresoEgresoPdfBean.getSubTotalSoles(), ingresoEgresoPdfBean.getSubTotalDolares());
				document.add(table);
			}
			document.close();
			pdf.close();

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public PdfPCell setDataPdf(String data, Font tableBody, int horizontal, int vertical){
		PdfPCell cellData = new PdfPCell(new Paragraph(data,tableBody));
		cellData.setBorderColor(Color.BLACK);
		cellData.setPaddingLeft(5);
		cellData.setHorizontalAlignment(horizontal);
		cellData.setVerticalAlignment(vertical);
		cellData.setBackgroundColor(Color.WHITE);
		cellData.setExtraParagraphSpace(5f);
		return cellData;
	}
	public PdfPCell setDataPdfNoBorder(String data, Font tableBody, int horizontal, int vertical){
		PdfPCell cellData = new PdfPCell(new Paragraph(data,tableBody));
		cellData.setBorderColor(Color.WHITE);
		cellData.setPaddingLeft(10);
		cellData.setHorizontalAlignment(horizontal);
		cellData.setVerticalAlignment(vertical);
		cellData.setBackgroundColor(Color.WHITE);
		cellData.setExtraParagraphSpace(5f);
		return cellData;
	}

	public Paragraph setTitlePdf(String nameTitle, Font mainFont){
		Paragraph paragraph = new Paragraph(nameTitle, mainFont);
		paragraph.setAlignment(Element.ALIGN_BOTTOM);
		paragraph.setIndentationLeft(50);
		paragraph.setIndentationRight(50);
		paragraph.setSpacingAfter(10);
		return paragraph;
	}

	public PdfPCell setHeaderPdf(String nameHeader, Font tableHeader){
		PdfPCell headerCell = null;
		headerCell = new PdfPCell(new Paragraph(nameHeader, tableHeader));
		headerCell.setBorderColor(Color.BLACK);
		headerCell.setPaddingLeft(5);
		headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		headerCell.setVerticalAlignment(Element.ALIGN_CENTER);
		headerCell.setBackgroundColor(Color.LIGHT_GRAY);
		headerCell.setExtraParagraphSpace(5);
		return headerCell;
	}

	private List<ReporteIngresoEgresoBean> generateListDataPdf_v2(List<ReporteIngresoEgresoBean> listaIngresoEgreso){
		for(ReporteIngresoEgresoBean ingreso: listaIngresoEgreso){
			ingreso.setTotalDolares(ingreso.getIngresoSubTotalDolares().subtract(ingreso.getGastoSubTotalDolares()));
			ingreso.setTotalDolares(ingreso.getTotalDolares().add(ingreso.getContratoSubTotalDolares()));
			ingreso.setTotalSoles(ingreso.getIngresoSubTotalSoles().subtract(ingreso.getGastoSubTotalSoles()));
			ingreso.setTotalSoles(ingreso.getTotalSoles().add(ingreso.getContratoSubTotalSoles()));
		}
		return listaIngresoEgreso;

	}

	private List<IngresoEgresoPdfBean> generateListDataPdf(List<IngresoEgresoBean> listaIngresoEgreso){
		List<IngresoEgresoPdfBean> listDataFinal = new ArrayList<>();
		IngresoEgresoPdfBean ingresoEgresoPdfBean = null;
		String usuarioAnterior = null;
		for(IngresoEgresoBean ingreso: listaIngresoEgreso){
			if (usuarioAnterior == null){
				usuarioAnterior = ingreso.getNombre();
				ingresoEgresoPdfBean = new IngresoEgresoPdfBean();
				ingresoEgresoPdfBean.setNombreUsuario(usuarioAnterior);
				ingresoEgresoPdfBean.setSubTotalDolares(new BigDecimal("0"));
				ingresoEgresoPdfBean.setSubTotalSoles(new BigDecimal("0"));
				ingresoEgresoPdfBean.setListaDatos(new ArrayList<>());
			}
			if (usuarioAnterior.equals(ingreso.getNombre())){
				ingresoEgresoPdfBean.setSubTotalDolares(ingresoEgresoPdfBean.getSubTotalDolares().add(ingreso.getMonto_dolares()));
				ingresoEgresoPdfBean.setSubTotalSoles(ingresoEgresoPdfBean.getSubTotalSoles().add(ingreso.getMonto_soles()));
				ingresoEgresoPdfBean.getListaDatos().add(ingreso);

			}else{
				listDataFinal.add(ingresoEgresoPdfBean);
				usuarioAnterior = ingreso.getNombre();
				ingresoEgresoPdfBean = new IngresoEgresoPdfBean();
				ingresoEgresoPdfBean.setNombreUsuario(usuarioAnterior);
				ingresoEgresoPdfBean.setSubTotalDolares(new BigDecimal("0"));
				ingresoEgresoPdfBean.setSubTotalSoles(new BigDecimal("0"));
				ingresoEgresoPdfBean.setListaDatos(new ArrayList<>());
				ingresoEgresoPdfBean.setSubTotalDolares(ingresoEgresoPdfBean.getSubTotalDolares().add(ingreso.getMonto_dolares()));
				ingresoEgresoPdfBean.setSubTotalSoles(ingresoEgresoPdfBean.getSubTotalSoles().add(ingreso.getMonto_soles()));
				ingresoEgresoPdfBean.getListaDatos().add(ingreso);
			}

		}

		listDataFinal.add(ingresoEgresoPdfBean);


		return listDataFinal;

	}
}
