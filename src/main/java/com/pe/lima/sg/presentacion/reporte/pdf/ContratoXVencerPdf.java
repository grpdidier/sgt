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
import com.pe.lima.sg.bean.reporte.ContratoXVencerBean;
import com.pe.lima.sg.bean.reporte.ContratoXVencerPdfBean;

@Component
public class ContratoXVencerPdf {

	

	/*Sub Totales*/
	public PdfPTable setSubTotales (BigDecimal subTotalAlquiler, BigDecimal subTotalServicio) throws DocumentException{
		PdfPTable table = new PdfPTable(3);
		PdfPCell cellData 	= null;
		Font tableBody = FontFactory.getFont("Arial",9,Color.BLACK);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(10);
		float[] columnWidths = {11f, 1.5f, 1.5f};
		table.setWidths(columnWidths);
		cellData = this.setDataPdf("Monto Total ", tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(subTotalAlquiler.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(subTotalServicio.toString(), tableBody, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
		table.addCell(cellData);
		
		return table;
	}
	/*Data de los cobros*/
	public PdfPTable setDataCobro(List<ContratoXVencerBean> listaContrato) throws DocumentException{
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
		
		headerCell = this.setHeaderPdf("Nombre y Apellido", tableHeader);
		table.addCell(headerCell);
		
		headerCell = this.setHeaderPdf("Fecha Fin", tableHeader);
		table.addCell(headerCell);
		
		headerCell = this.setHeaderPdf("Alquiler ($)", tableHeader);
		table.addCell(headerCell);
		
		headerCell = this.setHeaderPdf("Servicio (S/)", tableHeader);
		table.addCell(headerCell);
		
		
		/*Datos de la tabla*/
		for(ContratoXVencerBean contrato:listaContrato ){
			cellData = this.setDataPdf(count.toString(), tableBody, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
			table.addCell(cellData);
			count++;
			
			cellData = this.setDataPdf(contrato.getNumeroTienda(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
			cellData = this.setDataPdf(contrato.getNombreCliente(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
			cellData = this.setDataPdf(contrato.getFechaVencimiento().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
			cellData = this.setDataPdf(contrato.getMontoAlquiler().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
			cellData = this.setDataPdf(contrato.getMontoServicio().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);
			
		}
		return table;
	}
	
	
	public void reporteContratoXVencer(List<ContratoXVencerBean> listaContrato, CriterioReporteBean criterio,ServletContext context,HttpServletRequest request, HttpServletResponse response, String nameFile) throws IOException{
		Document document 	= new Document(PageSize.A4, 15, 15, 45, 30);
		PdfPTable table = null;
		List<ContratoXVencerPdfBean> listDataFinal = null;
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
			Paragraph paragraph = this.setTitlePdf("REPORTE DE CONTRATOS POR VENCER - " + criterio.getFechaFin(), mainFont);
			document.add(paragraph);
			
			listDataFinal = this.generateListDataPdf(listaContrato);
			for(ContratoXVencerPdfBean contratoPdf: listDataFinal){
				
				table = this.setDataCobro(contratoPdf.getListaDatos());
				document.add(table);
				table = this.setSubTotales(contratoPdf.getSubTotalAlquiler(), contratoPdf.getSubTotalServicio());
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
	
	private List<ContratoXVencerPdfBean> generateListDataPdf(List<ContratoXVencerBean> listaContrato){
		List<ContratoXVencerPdfBean> listDataFinal = new ArrayList<>();
		ContratoXVencerPdfBean contratoXVencerPdfBean = null;
		
		contratoXVencerPdfBean = new ContratoXVencerPdfBean();
		contratoXVencerPdfBean.setSubTotalAlquiler(new BigDecimal("0"));
		contratoXVencerPdfBean.setSubTotalServicio(new BigDecimal("0"));
		contratoXVencerPdfBean.setListaDatos(new ArrayList<>());
		
		for(ContratoXVencerBean contrato: listaContrato){
			contratoXVencerPdfBean.setSubTotalAlquiler(contratoXVencerPdfBean.getSubTotalAlquiler().add(contrato.getMontoAlquiler()));
			contratoXVencerPdfBean.setSubTotalServicio(contratoXVencerPdfBean.getSubTotalServicio().add(contrato.getMontoServicio()));
			contratoXVencerPdfBean.getListaDatos().add(contrato);
		}
		
		listDataFinal.add(contratoXVencerPdfBean);
		
		
		return listDataFinal;
		
	}
}
