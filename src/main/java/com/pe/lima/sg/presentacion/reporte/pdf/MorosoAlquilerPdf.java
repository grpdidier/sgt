package com.pe.lima.sg.presentacion.reporte.pdf;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.pe.lima.sg.bean.reporte.CriterioReporteBean;
import com.pe.lima.sg.bean.reporte.MorososAlquilerBean;
import com.pe.lima.sg.bean.reporte.MorososAlquilerPdfBean;

@Component
public class MorosoAlquilerPdf {

	

	/*Sub Totales*/
	public PdfPTable setSubTotales (BigDecimal subTotalDolares) throws DocumentException{
		PdfPTable table = new PdfPTable(3);
		PdfPCell cellData 	= null;
		Font tableBody = FontFactory.getFont("Arial",9,Color.BLACK);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(10);
		float[] columnWidths = {9.5f, 1.5f, 1.5f};
		table.setWidths(columnWidths);
		cellData = this.setDataPdf("Deuda Total  por Tienda - Sub Totales $ ", tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(subTotalDolares.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf("", tableBody, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
		table.addCell(cellData);
		
		return table;
	}
	/*Data de los cobros*/
	public PdfPTable setDataCobro(List<MorososAlquilerBean> listaMoroso) throws DocumentException{
		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(0);
		PdfPCell cellData 	= null;
		Integer count 		= 1;
		
		Font tableHeader = FontFactory.getFont("Arial",10,Color.BLACK);
		Font tableBody = FontFactory.getFont("Arial",9,Color.BLACK);
		
		float[] columnWidths = {1f, 7f, 1.5f, 1.5f, 1.5f};
		table.setWidths(columnWidths);
		
		/*Titulos de la cabecera*/
		PdfPCell headerCell = this.setHeaderPdf("Nro", tableHeader);
		table.addCell(headerCell);
		
		headerCell = this.setHeaderPdf("Nombre y Apellido", tableHeader);
		table.addCell(headerCell);
		
		headerCell = this.setHeaderPdf("Tienda", tableHeader);
		table.addCell(headerCell);
		
		headerCell = this.setHeaderPdf("Alquiler $", tableHeader);
		table.addCell(headerCell);
		
		headerCell = this.setHeaderPdf("Fecha Vencimiento", tableHeader);
		table.addCell(headerCell);
		
		
		/*Datos de la tabla*/
		for(MorososAlquilerBean moroso:listaMoroso ){
			cellData = this.setDataPdf(count.toString(), tableBody, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
			table.addCell(cellData);
			count++;
			
			cellData = this.setDataPdf(moroso.getNombreCliente(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
			cellData = this.setDataPdf(moroso.getNumeroTienda(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
			cellData = this.setDataPdf(moroso.getMontoDolares().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
			cellData = this.setDataPdf(moroso.getFechaVencimiento().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
		}
		return table;
	}
	
	
	public void reporteMorosoAlquiler(List<MorososAlquilerBean> listaMoroso, CriterioReporteBean criterio,ServletContext context,HttpServletRequest request, HttpServletResponse response, String nameFile) throws IOException{
		Document document 	= new Document(PageSize.A4, 15, 15, 45, 30);
		PdfPTable table = null;
		List<MorososAlquilerPdfBean> listDataFinal = null;
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
			Paragraph paragraph = this.setTitlePdf("REPORTE DE TODOS LOS MOROSOS DE ALQUILER - " + criterio.getFechaFin(), mainFont);
			document.add(paragraph);
			
			listDataFinal = this.generateListDataPdf(listaMoroso);
			for(MorososAlquilerPdfBean morosoPdf: listDataFinal){
				
				table = this.setDataCobro(morosoPdf.getListaDatos());
				document.add(table);
				table = this.setSubTotales(morosoPdf.getSubTotalDolares());
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
		paragraph.setAlignment(Element.ALIGN_CENTER);
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
	
	private List<MorososAlquilerPdfBean> generateListDataPdf(List<MorososAlquilerBean> listaMoroso){
		List<MorososAlquilerPdfBean> listDataFinal = new ArrayList<>();
		MorososAlquilerPdfBean morososAlquilerPdfBean = null;
		String clienteAnterior = null;
		for(MorososAlquilerBean moroso: listaMoroso){
			if (clienteAnterior == null){
				clienteAnterior = moroso.getNombreCliente();
				morososAlquilerPdfBean = new MorososAlquilerPdfBean();
				morososAlquilerPdfBean.setNombreCliente(clienteAnterior);
				morososAlquilerPdfBean.setSubTotalDolares(new BigDecimal("0"));
				morososAlquilerPdfBean.setListaDatos(new ArrayList<>());
			}
			if (clienteAnterior.equals(moroso.getNombreCliente())){
				morososAlquilerPdfBean.setSubTotalDolares(morososAlquilerPdfBean.getSubTotalDolares().add(moroso.getMontoDolares()));
				morososAlquilerPdfBean.getListaDatos().add(moroso);
				
			}else{
				listDataFinal.add(morososAlquilerPdfBean);
				clienteAnterior = moroso.getNombreCliente();
				morososAlquilerPdfBean = new MorososAlquilerPdfBean();
				morososAlquilerPdfBean.setNombreCliente(clienteAnterior);
				morososAlquilerPdfBean.setSubTotalDolares(new BigDecimal("0"));
				morososAlquilerPdfBean.setSubTotalSoles(new BigDecimal("0"));
				morososAlquilerPdfBean.setListaDatos(new ArrayList<>());
				morososAlquilerPdfBean.setSubTotalDolares(morososAlquilerPdfBean.getSubTotalDolares().add(moroso.getMontoDolares()));
				morososAlquilerPdfBean.getListaDatos().add(moroso);
			}
			
		}
		
		listDataFinal.add(morososAlquilerPdfBean);
		
		
		return listDataFinal;
		
	}
}
