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
import com.pe.lima.sg.bean.reporte.MorosoLuzBean;
import com.pe.lima.sg.bean.reporte.MorosoLuzPdfBean;

@Component
public class MorosoLuzPdf {

	

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
	public PdfPTable setDataCobro(List<MorosoLuzBean> listaLuz) throws DocumentException{
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
		
		headerCell = this.setHeaderPdf("Monto S/", tableHeader);
		table.addCell(headerCell);
		
		headerCell = this.setHeaderPdf("Fecha Vencimiento", tableHeader);
		table.addCell(headerCell);
		
		
		/*Datos de la tabla*/
		for(MorosoLuzBean moroso:listaLuz ){
			cellData = this.setDataPdf(count.toString(), tableBody, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
			table.addCell(cellData);
			count++;
			
			cellData = this.setDataPdf(moroso.getNombreCliente(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
			cellData = this.setDataPdf(moroso.getNumeroTienda(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
			cellData = this.setDataPdf(moroso.getMontoLuz().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
			cellData = this.setDataPdf(moroso.getFechaVencimiento().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
		}
		return table;
	}
	
	
	public void reporteMorosoLuz(List<MorosoLuzBean> listaLuz, CriterioReporteBean criterio,ServletContext context,HttpServletRequest request, HttpServletResponse response, String nameFile) throws IOException{
		Document document 	= new Document(PageSize.A4, 15, 15, 45, 30);
		PdfPTable table = null;
		List<MorosoLuzPdfBean> listDataFinal = null;
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
			
			listDataFinal = this.generateListDataPdf(listaLuz);
			for(MorosoLuzPdfBean morosoPdf: listDataFinal){
				
				table = this.setDataCobro(morosoPdf.getListaDatos());
				document.add(table);
				table = this.setSubTotales(morosoPdf.getSubTotalLuz());
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
	
	private List<MorosoLuzPdfBean> generateListDataPdf(List<MorosoLuzBean> listaLuz){
		List<MorosoLuzPdfBean> listDataFinal = new ArrayList<>();
		MorosoLuzPdfBean morososAlquilerPdfBean = null;
		String clienteAnterior = null;
		for(MorosoLuzBean moroso: listaLuz){
			if (clienteAnterior == null){
				clienteAnterior = moroso.getNombreCliente();
				morososAlquilerPdfBean = new MorosoLuzPdfBean();
				morososAlquilerPdfBean.setNombreCliente(clienteAnterior);
				morososAlquilerPdfBean.setSubTotalLuz(new BigDecimal("0"));
				morososAlquilerPdfBean.setListaDatos(new ArrayList<>());
			}
			if (clienteAnterior.equals(moroso.getNombreCliente())){
				morososAlquilerPdfBean.setSubTotalLuz(morososAlquilerPdfBean.getSubTotalLuz().add(moroso.getMontoLuz()));
				morososAlquilerPdfBean.getListaDatos().add(moroso);
				
			}else{
				listDataFinal.add(morososAlquilerPdfBean);
				clienteAnterior = moroso.getNombreCliente();
				morososAlquilerPdfBean = new MorosoLuzPdfBean();
				morososAlquilerPdfBean.setNombreCliente(clienteAnterior);
				morososAlquilerPdfBean.setSubTotalLuz(new BigDecimal("0"));
				morososAlquilerPdfBean.setListaDatos(new ArrayList<>());
				morososAlquilerPdfBean.setSubTotalLuz(morososAlquilerPdfBean.getSubTotalLuz().add(moroso.getMontoLuz()));
				morososAlquilerPdfBean.getListaDatos().add(moroso);
			}
			
		}
		
		listDataFinal.add(morososAlquilerPdfBean);
		
		
		return listDataFinal;
		
	}
}
