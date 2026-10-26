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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.pe.lima.sg.bean.caja.ComprobanteSunatBean;
import com.pe.lima.sg.bean.caja.MasivoSunatBean;

/**
 * PDF del reporte de ventas (equivalente a MasivoFacturaExcel).
 * Columnas: FECHA | TIPO DE DOC. | SERIE/NUMERO | NUMERO RUC | R. SOCIAL | MONEDA | TC SUNAT |
 * BASE IMPONIBLE DOLARES/SOLES | VENTAS NO GRAVADAS DOLARES | IGV | TOTAL DOLARES/SOLES | CONCEPTO
 */
@Component
public class MasivoFacturaPdf {

	private static final String[] COLUMNAS = {
		"FECHA", "TIPO DE DOC.", "SERIE/NUMERO", "NUMERO RUC", "R. SOCIAL", "MONEDA", "TC SUNAT",
		"BASE IMPONIBLE DOLARES/SOLES", "VENTAS NO GRAVADAS DOLARES", "IGV", "TOTAL DOLARES/SOLES", "CONCEPTO"
	};

	private static final float[] ANCHOS = { 1.2f, 0.9f, 1.8f, 1.7f, 3.6f, 0.9f, 0.9f, 1.5f, 1.5f, 1.1f, 1.5f, 5.4f };

	private static final String[] FORMATOS_FECHA = { "yyyy-MM-dd", "dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd" };

	private static final Color COLOR_CABECERA = new Color(148, 201, 193);

	private static final float BORDE_FINO  = 0.5f;
	private static final float BORDE_MEDIO = 1.5f;

	/*Reporte de ventas (facturas emitidas)*/
	public void reporteMasivoFacturaEmitidos(List<ComprobanteSunatBean> listaFacturaEmitidos, MasivoSunatBean criterio,
			ServletContext context, HttpServletRequest request, HttpServletResponse response, String nameFile) throws IOException{
		//Horizontal: son 12 columnas
		Document document = new Document(PageSize.A4.rotate(), 15, 15, 30, 25);
		try{
			String filePath = context.getRealPath("/resources/reports");
			File file = new File(filePath);
			if(!file.exists()){
				file.mkdirs();
			}
			PdfWriter pdf = PdfWriter.getInstance(document, new FileOutputStream(file + "/" + nameFile + ".pdf"));
			document.open();

			String periodo = (criterio != null && criterio.getPeriodo() != null) ? criterio.getPeriodo() : "";
			Font mainFont = FontFactory.getFont("Arial", 12, Font.BOLD, Color.BLACK);
			Paragraph titulo = new Paragraph("REPORTE DE VENTAS " + periodo, mainFont);
			titulo.setAlignment(Element.ALIGN_CENTER);
			titulo.setSpacingAfter(8);
			document.add(titulo);

			// Totales por moneda: [base, igv, total]
			Map<String, BigDecimal[]> totales = new LinkedHashMap<String, BigDecimal[]>();

			PdfPTable table = this.setDataVentas(listaFacturaEmitidos, totales);
			document.add(table);

			table = this.setTotales(totales);
			document.add(table);

			document.close();
			pdf.close();

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/*Cabecera + detalle*/
	public PdfPTable setDataVentas(List<ComprobanteSunatBean> lista, Map<String, BigDecimal[]> totales) throws Exception{
		PdfPTable table = new PdfPTable(COLUMNAS.length);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0);
		table.setSpacingAfter(0);
		table.setWidths(ANCHOS);

		Font tableHeader = FontFactory.getFont("Arial", 6, Font.BOLD, Color.BLACK);
		Font tableBody   = FontFactory.getFont("Arial", 6.5f, Color.BLACK);

		/*Titulos de la cabecera*/
		for(String columna : COLUMNAS){
			table.addCell(this.setHeaderPdf(columna, tableHeader));
		}
		table.setHeaderRows(1); //repite la cabecera en cada pagina

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String monedaAnterior = null;

		if(lista != null){
			for(ComprobanteSunatBean bean : lista){
				String moneda = this.codigoMoneda(bean.getMoneda());
				// Linea separadora (borde superior mas grueso) cuando cambia la moneda (D -> S)
				boolean separador = monedaAnterior != null && !moneda.equals(monedaAnterior);
				monedaAnterior = moneda;

				Date fecha = this.parseFecha(bean.getFechaEmision());
				String fechaTxt = fecha != null ? sdf.format(fecha) : this.nvl(bean.getFechaEmision());

				table.addCell(this.setDataPdf(fechaTxt, tableBody, Element.ALIGN_CENTER, separador));
				table.addCell(this.setDataPdf(this.nvl(bean.getClienteTipoDocumento()), tableBody, Element.ALIGN_CENTER, separador));
				table.addCell(this.setDataPdf(this.serieNumero(bean), tableBody, Element.ALIGN_CENTER, separador));
				table.addCell(this.setDataPdf(this.nvl(bean.getClienteNumero()).trim(), tableBody, Element.ALIGN_RIGHT, separador));
				table.addCell(this.setDataPdf(this.nvl(bean.getClienteNombre()).trim(), tableBody, Element.ALIGN_LEFT, separador));
				table.addCell(this.setDataPdf(moneda, tableBody, Element.ALIGN_CENTER, separador));
				table.addCell(this.setDataPdf(this.formatTc(bean.getTipoCambio()), tableBody, Element.ALIGN_RIGHT, separador));
				table.addCell(this.setDataPdf(this.formatMonto(bean.getTotalGravados()), tableBody, Element.ALIGN_RIGHT, separador));
				//Ventas no gravadas: el bean no la trae, queda en blanco (igual que el Excel)
				table.addCell(this.setDataPdf("", tableBody, Element.ALIGN_RIGHT, separador));
				table.addCell(this.setDataPdf(this.formatMonto(bean.getTotalIgv()), tableBody, Element.ALIGN_RIGHT, separador));
				table.addCell(this.setDataPdf(this.formatMonto(bean.getTotal()), tableBody, Element.ALIGN_RIGHT, separador));
				table.addCell(this.setDataPdf(this.nvl(bean.getConcepto()), tableBody, Element.ALIGN_LEFT, separador));

				// Acumular totales
				BigDecimal[] acum = totales.get(moneda);
				if(acum == null){
					acum = new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO };
					totales.put(moneda, acum);
				}
				acum[0] = acum[0].add(this.nvlBig(bean.getTotalGravados()));
				acum[1] = acum[1].add(this.nvlBig(bean.getTotalIgv()));
				acum[2] = acum[2].add(this.nvlBig(bean.getTotal()));
			}
		}
		return table;
	}

	/*Totales por moneda (TOTAL D / TOTAL S)*/
	public PdfPTable setTotales(Map<String, BigDecimal[]> totales) throws Exception{
		PdfPTable table = new PdfPTable(COLUMNAS.length);
		table.setWidthPercentage(100);
		table.setSpacingBefore(10);
		table.setSpacingAfter(10);
		table.setWidths(ANCHOS);

		Font tableBody = FontFactory.getFont("Arial", 7, Font.BOLD, Color.BLACK);
		for(Map.Entry<String, BigDecimal[]> entry : totales.entrySet()){
			BigDecimal[] t = entry.getValue();
			// Etiqueta: ocupa las 7 primeras columnas (hasta TC SUNAT)
			PdfPCell label = this.setDataPdf("TOTAL " + entry.getKey(), tableBody, Element.ALIGN_RIGHT, false);
			label.setColspan(7);
			table.addCell(label);
			table.addCell(this.setDataPdf(this.formatMonto(t[0]), tableBody, Element.ALIGN_RIGHT, false));
			table.addCell(this.setDataPdf("", tableBody, Element.ALIGN_RIGHT, false));
			table.addCell(this.setDataPdf(this.formatMonto(t[1]), tableBody, Element.ALIGN_RIGHT, false));
			table.addCell(this.setDataPdf(this.formatMonto(t[2]), tableBody, Element.ALIGN_RIGHT, false));
			table.addCell(this.setDataPdf("", tableBody, Element.ALIGN_RIGHT, false));
		}
		return table;
	}

	public PdfPCell setHeaderPdf(String nameHeader, Font tableHeader){
		PdfPCell headerCell = new PdfPCell(new Paragraph(nameHeader, tableHeader));
		headerCell.setBorderColor(Color.BLACK);
		headerCell.setBorderWidth(BORDE_FINO);
		headerCell.setPadding(3);
		headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		headerCell.setBackgroundColor(COLOR_CABECERA);
		return headerCell;
	}

	public PdfPCell setDataPdf(String data, Font tableBody, int horizontal, boolean bordeSuperiorMedio){
		PdfPCell cellData = new PdfPCell(new Paragraph(data == null ? "" : data, tableBody));
		cellData.setBorderColor(Color.BLACK);
		cellData.setBorderWidth(BORDE_FINO);
		if(bordeSuperiorMedio){
			cellData.setBorderWidthTop(BORDE_MEDIO);
		}
		cellData.setPadding(3);
		cellData.setHorizontalAlignment(horizontal);
		cellData.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cellData.setBackgroundColor(Color.WHITE);
		return cellData;
	}

	private String serieNumero(ComprobanteSunatBean bean){
		String serie = this.nvl(bean.getSerie()).trim();
		String numero = this.nvl(bean.getNumero()).trim();
		if(serie.isEmpty()) return numero;
		if(numero.isEmpty()) return serie;
		return serie + "-" + numero;
	}

	/*Normaliza la moneda a "D" (dolares) o "S" (soles)*/
	private String codigoMoneda(String moneda){
		if(moneda == null){
			return "";
		}
		String v = moneda.trim().toUpperCase();
		if(v.startsWith("D") || v.contains("USD") || v.contains("US$")){
			return "D";
		}
		if(v.startsWith("S") || v.startsWith("PEN") || v.contains("SOL")){
			return "S";
		}
		return v;
	}

	private String formatMonto(BigDecimal valor){
		return new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.US)).format(this.nvlBig(valor));
	}

	private String formatTc(BigDecimal valor){
		return new DecimalFormat("0.00#", DecimalFormatSymbols.getInstance(Locale.US)).format(this.nvlBig(valor));
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

	private BigDecimal nvlBig(BigDecimal valor){
		return valor == null ? BigDecimal.ZERO : valor;
	}

}
