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
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.pe.lima.sg.bean.reporte.MorosoBean;
import com.pe.lima.sg.bean.reporte.MorosoSubTotalBean;
import com.pe.lima.sg.bean.reporte.MorosoTotalBean;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.UtilSGT;

@Component
public class MorosoPdf {
	
	/*Reporte de Morosos*/
	public void reporteMoroso(List<MorosoTotalBean> listaDataReporte, Filtro criterio,ServletContext context,HttpServletRequest request, HttpServletResponse response, String nameFile) throws IOException{
		Document document 	= new Document(PageSize.A4, 15, 15, 45, 30);
		PdfPTable table 	= null;
		String monedaSubTotal = getSubTotalMoneda(criterio);
		String monedaTotal = getSTotalMoneda(criterio);
		Integer contador = 0;
		try{
			String filePath = context.getRealPath("/resources/reports");
			File file = new File(filePath);
			boolean exists = new File(filePath).exists();
			if(!exists){
				new File(filePath).mkdirs();
			}
			PdfWriter pdf = PdfWriter.getInstance(document, new FileOutputStream(file+ "/" + nameFile+".pdf"));
			
			/*Cabecera*/
			HeaderFooter header =new HeaderFooter(new Phrase(UtilSGT.getFecha("dd/MM/YYYY")),false);
            header.setAlignment(HeaderFooter.ALIGN_RIGHT);
            header.setBorder(Rectangle.NO_BORDER);
            document.setHeader(header);
            
			document.open();
			Font mainFont = FontFactory.getFont("Arial",18, Color.BLACK);
			table =this.setTituloLogo(criterio,mainFont);
			document.add(table);
			
			for(MorosoTotalBean morosoTotalBean: listaDataReporte){
				Collections.sort(morosoTotalBean.getListaMorosoSubTotal());
				
				for(MorosoSubTotalBean morosoSubTotalBean : morosoTotalBean.getListaMorosoSubTotal()){
					//Morosos
					contador = contador +1;
					table = this.setDataDeuda(morosoSubTotalBean.getListaMoroso(), contador); 
					document.add(table);
					table = this.setSubTotales(morosoSubTotalBean.getSubTotal(), morosoSubTotalBean.getSubSaldo(),monedaSubTotal);
					document.add(table);
				}
				
				//Total General
				table = this.setTotales(morosoTotalBean.getTotal(), morosoTotalBean.getSaldo(), monedaTotal);
				document.add(table);
			}
			document.close();
			pdf.close();

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*Obtener la moneda*/
	
	private String getSubTotalMoneda(Filtro filtro){
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
	
	}
	
	/*Nombre del usuario*/
	public PdfPTable setTituloLogo ( Filtro criterio,Font mainFont) throws DocumentException{
		PdfPTable table = new PdfPTable(2);
		try{
			Image img = Image.getInstance(new ClassPathResource("src/main/resources/static/images/iconos/logo.png").getPath());
			table.setWidthPercentage(100);
			table.setSpacingBefore(1f);
			table.setSpacingAfter(0);
			float[] columnWidths = {10f, 4f};
			table.setWidths(columnWidths);
			
			Paragraph paragraph = this.setTitlePdf("REPORTE DEUDORES "+criterio.getNombreCobro() + " - " + criterio.getNombre() + " \n"+ this.mesAnio(criterio), mainFont);
			PdfPCell headerCell = new PdfPCell(paragraph); //this.setHeaderPdf(Nombre, tableHeader);

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
	

	private String mesAnio(Filtro criterio){
		String resultado = null;
		try{
			resultado = criterio.getNombreMes() + " " + criterio.getAnio().toString();
		}catch(Exception e){
			resultado = UtilSGT.getMesPersonalizado(UtilSGT.getMes(criterio.getFechaFin())) + " AÑO";
		}
		return resultado;
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
	public PdfPTable setSubTotales (BigDecimal subTotal, BigDecimal Saldo, String Nombre) throws DocumentException{
		PdfPTable table = new PdfPTable(3);
		PdfPCell cellData 	= null;
		Font tableBody = FontFactory.getFont("Arial",9,Color.BLACK);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(0);
		float[] columnWidths = {11f, 1.5f, 1.5f};
		table.setWidths(columnWidths);
		cellData = this.setDataPdf(Nombre , tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(subTotal.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(Saldo.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
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
		float[] columnWidths = {11f, 1.5f, 1.5f};
		table.setWidths(columnWidths);
		cellData = this.setDataPdf(nombre , tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(total.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(saldo.toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);

		return table;
	}
	
	public PdfPTable setDataDeuda(List<MorosoBean> listaMoroso, Integer primeraVez) throws DocumentException{
		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(0);
		PdfPCell cellData 	= null;
		Integer count 		= 1;

		Font tableHeader = FontFactory.getFont("Arial",10,Color.BLACK);
		Font tableBody = FontFactory.getFont("Arial",9,Color.BLACK);

		float[] columnWidths = {1.5f, 3f, 6.5f, 1.5f, 1.5f};
		table.setWidths(columnWidths);

		if (primeraVez.compareTo(1) == 0){
			/*Titulos de la cabecera*/
			PdfPCell headerCell = this.setHeaderPdf("LOCAL", tableHeader);
			table.addCell(headerCell);
	
			headerCell = this.setHeaderPdf("VENCIMIENTO", tableHeader);
			table.addCell(headerCell);
	
			headerCell = this.setHeaderPdf("NOMBRE COMPLETO", tableHeader);
			table.addCell(headerCell);
	
			headerCell = this.setHeaderPdf("TOTAL", tableHeader);
			table.addCell(headerCell);
	
			headerCell = this.setHeaderPdf("SALDO", tableHeader);
			table.addCell(headerCell);

		}
		Collections.sort(listaMoroso);
		/*Datos de la tabla*/
		for(MorosoBean morosoBean:listaMoroso ){
			cellData = this.setDataPdf(morosoBean.getNumeroTienda(), tableBody, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
			table.addCell(cellData);
			count++;

			cellData = this.setDataPdf(morosoBean.getVencimiento(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(morosoBean.getNombreCliente(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(morosoBean.getTotal().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(morosoBean.getSaldo().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
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
