package com.pe.lima.sg.presentacion.reporte.pdf;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
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
import com.pe.lima.sg.bean.reporte.LocalBean;
import com.pe.lima.sg.bean.reporte.LocalSubTotalBean;
import com.pe.lima.sg.bean.reporte.LocalTotalBean;
import com.pe.lima.sg.presentacion.Filtro;

@Component
public class ContratoFinalizadoPdf {
	
	/*Reporte de Morosos*/
	public void reporteContratoFinalizado(List<LocalTotalBean> listaDataReporte, Filtro criterio,ServletContext context,HttpServletRequest request, HttpServletResponse response, String nameFile) throws IOException{
		Document document 	= new Document(PageSize.A4, 15, 15, 45, 30);
		PdfPTable table 	= null;
		//String monedaSubTotal = getSubTotalMoneda(criterio);
		//String monedaTotal = getSTotalMoneda(criterio);
		Integer contador = 0;
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
			table =this.setTituloLogo(criterio,mainFont);
			document.add(table);
			
			for(LocalTotalBean localTotalBean: listaDataReporte){
				Collections.sort(localTotalBean.getListaLocalSubTotal());
				
				for(LocalSubTotalBean localSubTotalBean : localTotalBean.getListaLocalSubTotal()){
					//Locales
					contador = contador +1;
					table = this.setDataDeuda(localSubTotalBean.getListaLocal(), contador); 
					document.add(table);
					table = this.setSubTotales(localSubTotalBean.getSubTotalAlquiler(), localSubTotalBean.getSubTotalServicio(), localSubTotalBean.getSubTotalGarantia(), "TOTAL");
					document.add(table);
				}
				
				//Total General: No se implementa porque no lo solicitó
				//table = this.setTotales(morosoTotalBean.getTotal(), morosoTotalBean.getSaldo(), monedaTotal);
				//document.add(table);
			}
			document.close();
			pdf.close();

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*Obtener la moneda*/
	
	/*private String getSubTotalMoneda(Filtro filtro){
		if(filtro.getTipoCobro().equals(Constantes.TIPO_COBRO_ALQUILER)){
			return " SUB TOTAL DOLARES ($) ";
		}else{
			return " SUB TOTAL SOLES (S/) ";
		}
	
	}
	private String getSTotalMoneda(Filtro filtro){
		if(filtro.getTipoCobro().equals(Constantes.TIPO_COBRO_ALQUILER)){
			return " TOTAL DOLARES ($) ";
		}else{
			return " TOTAL SOLES (S/) ";
		}
	
	}*/
	
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
			Paragraph paragraph = this.setTitlePdf("RELACIÓN DE CONTRATOS FINALIZADOS "+ " \n del "+ criterio.getFechaInicio() + " al " + criterio.getFechaFin(), mainFont);
			PdfPCell headerCell = new PdfPCell(paragraph); 

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
	public PdfPTable setSubTotales (BigDecimal subTotalAlquiler, BigDecimal subTotalServicio, BigDecimal subTotalGarantia, String Nombre) throws DocumentException{
		PdfPTable table = new PdfPTable(4);
		PdfPCell cellData 	= null;
		Font tableBody = FontFactory.getFont("Arial",7,Color.BLACK);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(10);
		//float[] columnWidths = {10f, 2f, 2f};
		float[] columnWidths = {10.7f, 1.1f, 1.1f, 1.1F};
		table.setWidths(columnWidths);
		cellData = this.setDataPdf(Nombre , tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(subTotalAlquiler.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(subTotalServicio.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(subTotalGarantia.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		return table;
	}
	
	/*Totales */
	public PdfPTable setTotales (BigDecimal total, BigDecimal saldo, String nombre) throws DocumentException{
		PdfPTable table = new PdfPTable(3);
		PdfPCell cellData 	= null;
		Font tableBody = FontFactory.getFont("Arial",9,Color.BLACK);
		table.setWidthPercentage(100);
		table.setSpacingBefore(10);
		table.setSpacingAfter(10);
		float[] columnWidths = {10f, 2f, 2f};
		table.setWidths(columnWidths);
		cellData = this.setDataPdf(nombre , tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(total.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(saldo.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);

		return table;
	}
	
	public PdfPTable setDataDeuda(List<LocalBean> listaLocal, Integer primeraVez) throws DocumentException{
		PdfPTable table = new PdfPTable(9);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(0);
		PdfPCell cellData 	= null;
		Integer count 		= 1;

		Font tableHeader = FontFactory.getFont("Arial",6,Color.BLACK);
		Font tableBody = FontFactory.getFont("Arial",7,Color.BLACK);

		//float[] columnWidths = {1.5f, 6.5f, 2f, 2f, 2f};
		float[] columnWidths = {1f, 1.5f, 4f, 1.4f, 1.4f, 1.4f, 1.1f, 1.1f, 1.1f};
		table.setWidths(columnWidths);

		if (primeraVez.compareTo(1) == 0){
			/*Titulos de la cabecera*/
			PdfPCell headerCell = this.setHeaderPdf("LOCAL", tableHeader);
			table.addCell(headerCell);
	
			headerCell = this.setHeaderPdf("RUC", tableHeader);
			table.addCell(headerCell);
			
			headerCell = this.setHeaderPdf("ARRENDATARIO", tableHeader);
			table.addCell(headerCell);
	
			headerCell = this.setHeaderPdf("FECHA FINALIZACION", tableHeader);
			table.addCell(headerCell);
			
			headerCell = this.setHeaderPdf("INICIO CONTRATO", tableHeader);
			table.addCell(headerCell);
			
			headerCell = this.setHeaderPdf("FIN CONTRATO", tableHeader);
			table.addCell(headerCell);
	
			headerCell = this.setHeaderPdf("ALQUILER $", tableHeader);
			table.addCell(headerCell);
	
			headerCell = this.setHeaderPdf("SERVICIO S/", tableHeader);
			table.addCell(headerCell);
			
			headerCell = this.setHeaderPdf("GARANTIA $", tableHeader);
			table.addCell(headerCell);

		}
		Collections.sort(listaLocal);
		/*Datos de la tabla*/
		for(LocalBean localBean:listaLocal ){
			cellData = this.setDataPdf(localBean.getNumeroTienda(), tableBody, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
			table.addCell(cellData);
			count++;
			
			cellData = this.setDataPdf(localBean.getRucCliente(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(localBean.getNombreCliente(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
			cellData = this.setDataPdf(localBean.getFechaFinalizacion(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(localBean.getFechaInicioContrato(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
			cellData = this.setDataPdf(localBean.getFechaFinContrato(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
			cellData = this.setDataPdf(localBean.getMontoAlquiler().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(localBean.getMontoServicio().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(localBean.getMontoGarantia().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

		}
		return table;
	}
	
	
	

	public PdfPCell setDataPdf(String data, Font tableBody, int horizontal, int vertical){
		PdfPCell cellData = new PdfPCell(new Paragraph(data,tableBody));
		cellData.setBorderColor(Color.BLACK);
		cellData.setPaddingLeft(10);
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
		headerCell.setPaddingLeft(10);
		headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		headerCell.setVerticalAlignment(Element.ALIGN_CENTER);
		headerCell.setBackgroundColor(Color.LIGHT_GRAY);
		headerCell.setExtraParagraphSpace(5);
		return headerCell;
	}

}
