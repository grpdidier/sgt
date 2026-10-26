package com.pe.lima.sg.presentacion.reporte.factura.emitida.lib;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.lowagie.text.Chunk;
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
import com.pe.lima.sg.bean.caja.ComprobanteSunatBean;
import com.pe.lima.sg.bean.caja.MasivoSunatBean;

/**
 * PDF "Facturas Electronicas Emitidas del Periodo" (listado de CPE).
 * 
 * Nro. CPE | Receptor | Importe Total | Fecha de Emision | Fecha de Rechazo | Comprobante Anulado
 */
@Component
public class MasivoFacturaCpePdf {

	private static final String[] FORMATOS_FECHA = { "yyyy-MM-dd", "dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd" };

	/*Reporte de Facturas Electronicas Emitidas (CPE)*/
	public void reporteFacturaElectronicaEmitidos(List<ComprobanteSunatBean> listaFacturaEmitidos, MasivoSunatBean criterio,
			ServletContext context, HttpServletRequest request, HttpServletResponse response, String nameFile) throws IOException{
		Document document 	= new Document(PageSize.A4, 15, 15, 45, 30);
		PdfPTable table 	= null;
		try{
			String filePath = context.getRealPath("/resources/reports");
			File file = new File(filePath);
			if(!file.exists()){
				file.mkdirs();
			}
			PdfWriter pdf = PdfWriter.getInstance(document, new FileOutputStream(file + "/" + nameFile + ".pdf"));
			document.open();
			Font mainFont = FontFactory.getFont("Arial", 16, Font.BOLD, Color.BLACK);

			/*Titulo + logo*/
			table = this.setTituloLogo(this.getRango(listaFacturaEmitidos, criterio), mainFont);
			document.add(table);

			/*Detalle*/
			table = this.setDataCpe(listaFacturaEmitidos);
			document.add(table);

			/*Totales por moneda*/
			table = this.setTotales(listaFacturaEmitidos);
			document.add(table);

			document.close();
			pdf.close();

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/*Titulo, rango de fechas y logo*/
	public PdfPTable setTituloLogo (String rango, Font mainFont) throws DocumentException{
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.setSpacingBefore(10f);
		table.setSpacingAfter(5f);
		float[] columnWidths = {10f, 4f};
		table.setWidths(columnWidths);

		Date ahora = new Date();
		Font subFont = FontFactory.getFont("Arial", 9, Color.BLACK);
		Paragraph paragraph = this.setTitlePdf("FACTURAS ELECTRÓNICAS EMITIDAS DEL PERIODO \n" + rango, mainFont);
		paragraph.add(Chunk.NEWLINE); //"Generado" en otra linea
		paragraph.add(new Chunk("Generado: " + new SimpleDateFormat("dd/MM/yyyy").format(ahora)
				+ " " + new SimpleDateFormat("HH:mm:ss").format(ahora), subFont));
		PdfPCell headerCell = new PdfPCell(paragraph);
		headerCell.setBorderColor(Color.WHITE);
		headerCell.setPaddingLeft(10);
		headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		headerCell.setBackgroundColor(Color.WHITE);
		headerCell.setExtraParagraphSpace(5);
		table.addCell(headerCell);

		try{
			Image img = Image.getInstance(new ClassPathResource("src/main/resources/static/images/iconos/logo.png").getPath());
			headerCell = new PdfPCell(img);
		}catch(Exception e){
			headerCell = new PdfPCell(new Paragraph(""));
		}
		headerCell.setBorderColor(Color.WHITE);
		headerCell.setPaddingLeft(10);
		headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		headerCell.setBackgroundColor(Color.WHITE);
		headerCell.setExtraParagraphSpace(5);
		table.addCell(headerCell);

		return table;
	}

	/*Detalle de comprobantes*/
	public PdfPTable setDataCpe(List<ComprobanteSunatBean> lista) throws DocumentException{
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(0);
		PdfPCell cellData 	= null;

		Font tableHeader = FontFactory.getFont("Arial", 6, Font.BOLD, Color.BLACK);
		Font tableBody   = FontFactory.getFont("Arial", 7, Color.BLACK);

		float[] columnWidths = {2.2f, 6f, 1.7f, 1.6f, 1.6f, 1.8f};
		table.setWidths(columnWidths);

		/*Titulos de la cabecera*/
		table.addCell(this.setHeaderPdf("NRO. CPE", tableHeader));
		table.addCell(this.setHeaderPdf("RECEPTOR", tableHeader));
		table.addCell(this.setHeaderPdf("IMPORTE TOTAL", tableHeader));
		table.addCell(this.setHeaderPdf("FECHA DE EMISIÓN", tableHeader));
		table.addCell(this.setHeaderPdf("FECHA DE RECHAZO", tableHeader));
		table.addCell(this.setHeaderPdf("COMPROBANTE ANULADO", tableHeader));
		table.setHeaderRows(1); //repite la cabecera en cada pagina

		/*Datos de la tabla*/
		if(lista != null){
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			for(ComprobanteSunatBean bean : lista){
				cellData = this.setDataPdf(this.nroCpe(bean), tableBody, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE);
				table.addCell(cellData);

				cellData = this.setDataPdf(this.receptor(bean), tableBody, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE);
				table.addCell(cellData);

				cellData = this.setDataPdf(this.formatMonto(bean.getTotal(), bean.getMoneda()), tableBody, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE);
				table.addCell(cellData);

				Date fecha = this.parseFecha(bean.getFechaEmision());
				String fechaTxt = fecha != null ? sdf.format(fecha) : this.nvl(bean.getFechaEmision());
				cellData = this.setDataPdf(fechaTxt, tableBody, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE);
				table.addCell(cellData);

				/*Fecha de rechazo y comprobante anulado: en blanco (igual que el Excel)*/
				cellData = this.setDataPdf("", tableBody, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE);
				table.addCell(cellData);
				cellData = this.setDataPdf("", tableBody, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE);
				table.addCell(cellData);
			}
		}
		return table;
	}

	/*Totales: cantidad de comprobantes y suma por moneda (soles / dolares)*/
	public PdfPTable setTotales (List<ComprobanteSunatBean> lista) throws DocumentException{
		BigDecimal totalSoles = BigDecimal.ZERO;
		BigDecimal totalDolares = BigDecimal.ZERO;
		int cantidad = 0;
		if(lista != null){
			for(ComprobanteSunatBean bean : lista){
				cantidad++;
				if(bean.getTotal() == null) continue;
				if(this.esDolares(bean.getMoneda())){
					totalDolares = totalDolares.add(bean.getTotal());
				}else{
					totalSoles = totalSoles.add(bean.getTotal());
				}
			}
		}
		PdfPTable table = new PdfPTable(2);
		Font tableBody = FontFactory.getFont("Arial", 8, Font.BOLD, Color.BLACK);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(10);
		float[] columnWidths = {8.2f, 5f};
		table.setWidths(columnWidths);

		table.addCell(this.setDataPdf("TOTAL SOLES (S/) - " + cantidad + " comprobantes en total", tableBody, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE));
		table.addCell(this.setDataPdf(this.formatMonto(totalSoles, "S"), tableBody, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE));
		if(totalDolares.signum() != 0){
			table.addCell(this.setDataPdf("TOTAL DOLARES ($)", tableBody, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE));
			table.addCell(this.setDataPdf(this.formatMonto(totalDolares, "D"), tableBody, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE));
		}
		return table;
	}

	/*Rango: minima y maxima fecha de emision; si no hay, el periodo del criterio*/
	private String getRango(List<ComprobanteSunatBean> lista, MasivoSunatBean criterio){
		Date min = null;
		Date max = null;
		if(lista != null){
			for(ComprobanteSunatBean bean : lista){
				Date f = this.parseFecha(bean.getFechaEmision());
				if(f == null) continue;
				if(min == null || f.before(min)) min = f;
				if(max == null || f.after(max))  max = f;
			}
		}
		if(min != null){
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			return sdf.format(min) + " - " + sdf.format(max);
		}
		return (criterio != null && criterio.getPeriodo() != null) ? criterio.getPeriodo() : "";
	}

	/*Serie - Numero (ej. F101 - 00050048)*/
	private String nroCpe(ComprobanteSunatBean bean){
		String serie = this.nvl(bean.getSerie()).trim();
		String numero = this.nvl(bean.getNumero()).trim();
		if(serie.isEmpty()) return numero;
		if(numero.isEmpty()) return serie;
		return serie + " - " + numero;
	}

	/*Numero de documento - Nombre del cliente*/
	private String receptor(ComprobanteSunatBean bean){
		String doc = this.nvl(bean.getClienteNumero()).trim();
		String nombre = this.nvl(bean.getClienteNombre()).trim();
		if(doc.isEmpty()) return nombre;
		if(nombre.isEmpty()) return doc;
		return doc + " - " + nombre;
	}

	private boolean esDolares(String moneda){
		String v = this.nvl(moneda).trim().toUpperCase();
		return v.startsWith("D") || v.contains("USD") || v.contains("US$");
	}

	/*Monto con simbolo de moneda ($ / S/)*/
	private String formatMonto(BigDecimal monto, String moneda){
		BigDecimal valor = monto == null ? BigDecimal.ZERO : monto;
		String simbolo = this.esDolares(moneda) ? "$ " : "S/ ";
		return simbolo + new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.US)).format(valor);
	}

	private Date parseFecha(String texto){
		if(texto == null || texto.trim().isEmpty()){
			return null;
		}
		String v = texto.trim();
		if(v.length() > 10){
			v = v.substring(0, 10);
		}
		for(String formato : FORMATOS_FECHA){
			try{
				SimpleDateFormat sdf = new SimpleDateFormat(formato);
				sdf.setLenient(false);
				return sdf.parse(v);
			}catch(ParseException e){
				// probar el siguiente formato
			}
		}
		return null;
	}

	private String nvl(String valor){
		return valor == null ? "" : valor;
	}

	public PdfPCell setDataPdf(String data, Font tableBody, int horizontal, int vertical){
		PdfPCell cellData = new PdfPCell(new Paragraph(data == null ? "" : data, tableBody));
		cellData.setBorderColor(Color.BLACK);
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
		PdfPCell headerCell = new PdfPCell(new Paragraph(nameHeader, tableHeader));
		headerCell.setBorderColor(Color.BLACK);
		headerCell.setPaddingLeft(10);
		headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		headerCell.setBackgroundColor(Color.LIGHT_GRAY);
		headerCell.setExtraParagraphSpace(5);
		return headerCell;
	}

}