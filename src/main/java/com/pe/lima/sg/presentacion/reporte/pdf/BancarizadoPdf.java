package com.pe.lima.sg.presentacion.reporte.pdf;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import com.pe.lima.sg.bean.reporte.BancarizadoBean;
import com.pe.lima.sg.bean.reporte.ReporteBancarizadoBean;
import com.pe.lima.sg.presentacion.Filtro;

@Component
public class BancarizadoPdf {
	
	
	public void reporteBancarizado(List<ReporteBancarizadoBean> listaIngresoEgreso, Filtro criterio,ServletContext context,HttpServletRequest request, HttpServletResponse response, String nameFile) throws IOException{
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
			Font mainFont = FontFactory.getFont("Arial",18, Color.BLACK);
			/*Titulo*/
			table =this.setTituloLogo(criterio,mainFont);
			document.add(table);

			for(ReporteBancarizadoBean ingresoEgresoPdfBean: listaIngresoEgreso){
				
				table = this.setTituloTabla("DATOS DEL USUARIO");
				document.add(table);
				//Datos de Efectivo y Bancarizado por usuario
				if (ingresoEgresoPdfBean.getListaBancarizado() != null && ingresoEgresoPdfBean.getListaBancarizado().size()>0){
					table = this.setDataIngresoEgreso(ingresoEgresoPdfBean.getListaBancarizado());
					document.add(table);
					
				}
				
				//Total General
				table = this.setTotalesIngresosEgresos(ingresoEgresoPdfBean);
				document.add(table);
			}
			document.close();
			pdf.close();

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*Nombre del Reporte*/
	public PdfPTable setTituloLogo ( Filtro criterio,Font mainFont) throws DocumentException{
		PdfPTable table = new PdfPTable(2);
		try{
			Image img = Image.getInstance(new ClassPathResource("src/main/resources/static/images/iconos/logo.png").getPath());
			table.setWidthPercentage(100);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(0);
			float[] columnWidths = {10f, 4f};
			table.setWidths(columnWidths);
			Paragraph paragraph = this.setTitlePdf("RESUMEN DIARIO \n"+criterio.getFechaInicio(), mainFont);
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
	public PdfPTable setGrupoReporte (String Nombre) throws DocumentException{
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
	public PdfPTable setTituloTabla (String Inmueble) throws DocumentException{
		PdfPTable table = new PdfPTable(3);

		table.setWidthPercentage(100);
		table.setSpacingBefore(10f);
		table.setSpacingAfter(0);
		Font tableHeader = FontFactory.getFont("Arial",10,Color.BLACK);
		float[] columnWidths = {6f, 4f, 4f};
		table.setWidths(columnWidths);
		PdfPCell headerCell = this.setHeaderPdf(Inmueble, tableHeader);
		table.addCell(headerCell);
		headerCell = this.setHeaderPdf("EFECTIVO", tableHeader);
		table.addCell(headerCell);
		headerCell = this.setHeaderPdf("BANCARIZADO", tableHeader);
		table.addCell(headerCell);
		return table;
	}
	/*Datos de cobros y gastos por usuario*/
	public PdfPTable setDataIngresoEgreso(List<BancarizadoBean> listaIngresoEgreso) throws DocumentException{
		
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(0);
		PdfPCell cellData 	= null;
		Integer count 		= 1;

		Font tableHeader = FontFactory.getFont("Arial",10,Color.BLACK);
		Font tableBody = FontFactory.getFont("Arial",9,Color.BLACK);

		float[] columnWidths = {1f, 5f, 2f, 2f, 2f, 2f};
		table.setWidths(columnWidths);

		/*Titulos de la cabecera*/
		PdfPCell headerCell = this.setHeaderPdf("Nro", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("USUARIO", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("SOLES S/", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("DOLARES $ ", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("SOLES S/", tableHeader);
		table.addCell(headerCell);

		headerCell = this.setHeaderPdf("DOLARES $", tableHeader);
		table.addCell(headerCell);


		/*Datos de la tabla*/
		for(BancarizadoBean ingreso:listaIngresoEgreso ){
			cellData = this.setDataPdf(count.toString(), tableBody, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
			table.addCell(cellData);
			count++;

			cellData = this.setDataPdf(ingreso.getNombreUsuario(), tableBody, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getEfectivoSoles().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getEfectivoDolares().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getBancarizadoSoles().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

			cellData = this.setDataPdf(ingreso.getBancarizadoDolares().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
			table.addCell(cellData);

		}
		return table;
	}
	
	/*Totales por usuario*/
	public PdfPTable setTotalesIngresosEgresos (ReporteBancarizadoBean ingresoEgresoPdfBean) throws DocumentException{
		PdfPTable table = new PdfPTable(5);
		PdfPCell cellData 	= null;
		Font tableBody = FontFactory.getFont("Arial",9,Color.BLACK);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(10);
		float[] columnWidths = {6f, 2f, 2f, 2f, 2f};
		table.setWidths(columnWidths);
		cellData = this.setDataPdf("TOTALES " , tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(ingresoEgresoPdfBean.getEfectivoSubTotalSoles().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(ingresoEgresoPdfBean.getEfectivoSubTotalDolares().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(ingresoEgresoPdfBean.getBancarizadoSubTotalSoles().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);
		cellData = this.setDataPdf(ingresoEgresoPdfBean.getBancarizadoSubTotalDolares().toString(), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_CENTER);
		table.addCell(cellData);

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
