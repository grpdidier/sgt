package com.pe.lima.sg.presentacion.reporte.factura.emitida.lib;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.pe.lima.sg.bean.caja.ComprobanteSunatBean;
import com.pe.lima.sg.bean.caja.MasivoSunatBean;


/**
 * Excel "Facturas Electronicas Emitidas del Periodo" (listado de CPE).
 * Columnas: Nro. CPE | Receptor | Importe Total | Fecha de Emision | Fecha de Rechazo | Comprobante Anulado
 */
@Component
public class MasivoFacturaCpeExcel {

	private static final String[] COLUMNAS = {
		"Nro. CPE", "Receptor", "Importe Total", "Fecha de Emisión", "Fecha de Rechazo", "Comprobante Anulado"
	};

	private static final int COL_NRO_CPE = 0;
	private static final int COL_RECEPTOR = 1;
	private static final int COL_IMPORTE = 2;
	private static final int COL_FECHA_EMISION = 3;
	private static final int COL_FECHA_RECHAZO = 4;
	private static final int COL_ANULADO = 5;

	private static final String FORMATO_DOLARES = "\"$\"#,##0.00";
	private static final String FORMATO_SOLES = "\"S/ \"#,##0.00";

	private static final String[] FORMATOS_FECHA = { "yyyy-MM-dd", "dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd" };

	public void reporteFacturaElectronicaEmitidos(List<ComprobanteSunatBean> listaFacturaEmitidos, MasivoSunatBean criterio,
			ServletContext context, HttpServletRequest request, HttpServletResponse response, String nameFile) throws IOException {

		String filePath = context.getRealPath("/resources/reports");
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}

		XSSFWorkbook workbook = new XSSFWorkbook();
		try {
			Sheet sheet = workbook.createSheet("Facturas Emitidas");
			Map<String, CellStyle> cache = new HashMap<String, CellStyle>();

			SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");
			Date ahora = new Date();

			// ---------- Rango de fechas del reporte (minima y maxima fecha de emision) ----------
			Date minFecha = null;
			Date maxFecha = null;
			if (listaFacturaEmitidos != null) {
				for (ComprobanteSunatBean bean : listaFacturaEmitidos) {
					Date f = parseFecha(bean.getFechaEmision());
					if (f == null) {
						continue;
					}
					if (minFecha == null || f.before(minFecha)) {
						minFecha = f;
					}
					if (maxFecha == null || f.after(maxFecha)) {
						maxFecha = f;
					}
				}
			}
			String rango;
			if (minFecha != null) {
				rango = sdfFecha.format(minFecha) + " - " + sdfFecha.format(maxFecha);
			} else {
				rango = (criterio != null && criterio.getPeriodo() != null) ? criterio.getPeriodo() : "";
			}

			// ---------- Fila 1: fecha de generacion | titulo | rango | hora ----------
			Row row1 = sheet.createRow(0);
			row1.setHeightInPoints(24);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, COL_RECEPTOR, COL_IMPORTE));

			Cell c = row1.createCell(COL_NRO_CPE);
			c.setCellValue(sdfFecha.format(ahora));
			c.setCellStyle(estilo(workbook, cache, null, HorizontalAlignment.LEFT, BorderStyle.MEDIUM, false));

			c = row1.createCell(COL_RECEPTOR);
			c.setCellValue("Facturas Electrónicas Emitidas del Periodo");
			c.setCellStyle(estilo(workbook, cache, null, HorizontalAlignment.CENTER, BorderStyle.MEDIUM, false));
			// celda combinada: dar el mismo estilo (borde) a la segunda celda
			c = row1.createCell(COL_IMPORTE);
			c.setCellStyle(estilo(workbook, cache, null, HorizontalAlignment.CENTER, BorderStyle.MEDIUM, false));

			c = row1.createCell(COL_FECHA_EMISION);
			c.setCellValue(rango);
			c.setCellStyle(estilo(workbook, cache, null, HorizontalAlignment.CENTER, BorderStyle.MEDIUM, false));

			c = row1.createCell(COL_FECHA_RECHAZO);
			c.setCellValue(sdfHora.format(ahora));
			c.setCellStyle(estilo(workbook, cache, null, HorizontalAlignment.CENTER, BorderStyle.MEDIUM, false));

			c = row1.createCell(COL_ANULADO);
			c.setCellStyle(estilo(workbook, cache, null, HorizontalAlignment.CENTER, BorderStyle.MEDIUM, false));

			// ---------- Fila 2: cabecera de columnas ----------
			Row headerRow = sheet.createRow(1);
			headerRow.setHeightInPoints(30);
			for (int i = 0; i < COLUMNAS.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(COLUMNAS[i]);
				cell.setCellStyle(estilo(workbook, cache, null, HorizontalAlignment.LEFT, BorderStyle.MEDIUM, true));
			}

			// ---------- Detalle (desde la fila 3) ----------
			int rowNum = 2;
			BigDecimal totalSoles = BigDecimal.ZERO;
			BigDecimal totalDolares = BigDecimal.ZERO;
			int cantidad = 0;
			if (listaFacturaEmitidos != null) {
				for (ComprobanteSunatBean bean : listaFacturaEmitidos) {
					Row row = sheet.createRow(rowNum++);
					row.setHeightInPoints(26);

					// Nro. CPE = serie - numero
					Cell cell = row.createCell(COL_NRO_CPE);
					cell.setCellValue(nroCpe(bean));
					cell.setCellStyle(estilo(workbook, cache, null, HorizontalAlignment.LEFT, BorderStyle.THIN, false));

					// Receptor = numero de documento - nombre del cliente
					cell = row.createCell(COL_RECEPTOR);
					cell.setCellValue(receptor(bean));
					cell.setCellStyle(estilo(workbook, cache, null, HorizontalAlignment.LEFT, BorderStyle.THIN, false));

					// Importe total con simbolo de moneda ($ / S/)
					cell = row.createCell(COL_IMPORTE);
					cell.setCellValue(bean.getTotal() == null ? 0d : bean.getTotal().doubleValue());
					cell.setCellStyle(estilo(workbook, cache, formatoMoneda(bean.getMoneda()), HorizontalAlignment.LEFT, BorderStyle.THIN, false));

					// Fecha de emision
					cell = row.createCell(COL_FECHA_EMISION);
					Date fecha = parseFecha(bean.getFechaEmision());
					if (fecha != null) {
						cell.setCellValue(fecha);
						cell.setCellStyle(estilo(workbook, cache, "dd/MM/yyyy", HorizontalAlignment.RIGHT, BorderStyle.THIN, false));
					} else {
						cell.setCellValue(nvl(bean.getFechaEmision()));
						cell.setCellStyle(estilo(workbook, cache, null, HorizontalAlignment.RIGHT, BorderStyle.THIN, false));
					}

					// Fecha de rechazo y comprobante anulado: en blanco (con borde)
					cell = row.createCell(COL_FECHA_RECHAZO);
					cell.setCellStyle(estilo(workbook, cache, null, HorizontalAlignment.LEFT, BorderStyle.THIN, false));
					cell = row.createCell(COL_ANULADO);
					cell.setCellStyle(estilo(workbook, cache, null, HorizontalAlignment.LEFT, BorderStyle.THIN, false));

					// Acumular totales por moneda
					cantidad++;
					if (bean.getTotal() != null) {
						if (esDolares(bean.getMoneda())) {
							totalDolares = totalDolares.add(bean.getTotal());
						} else {
							totalSoles = totalSoles.add(bean.getTotal());
						}
					}
				}
			}

			// ---------- Totales (igual que el PDF): soles siempre, dolares solo si hay importes ----------
			escribirTotal(workbook, cache, sheet, rowNum++,
					"TOTAL SOLES (S/) - " + cantidad + " comprobantes en total", totalSoles, FORMATO_SOLES);
			if (totalDolares.signum() != 0) {
				escribirTotal(workbook, cache, sheet, rowNum++, "TOTAL DOLARES ($)", totalDolares, FORMATO_DOLARES);
			}

			// ---------- Anchos, cabecera fija y zoom ----------
			sheet.setColumnWidth(COL_NRO_CPE, 16 * 256);
			sheet.setColumnWidth(COL_RECEPTOR, 60 * 256);
			sheet.setColumnWidth(COL_IMPORTE, 16 * 256);
			sheet.setColumnWidth(COL_FECHA_EMISION, 20 * 256);
			sheet.setColumnWidth(COL_FECHA_RECHAZO, 16 * 256);
			sheet.setColumnWidth(COL_ANULADO, 18 * 256);
			sheet.createFreezePane(0, 2);
			sheet.setZoom(80);

			FileOutputStream fileOut = new FileOutputStream(file + "/" + nameFile + ".xlsx");
			try {
				workbook.write(fileOut);
				fileOut.flush();
			} finally {
				fileOut.close();
			}
		} finally {
			workbook.close();
		}
	}

	/**
	 * Crea (o reutiliza) un estilo. Borde inferior segun parametro; los demas bordes finos y grises.
	 */
	private CellStyle estilo(XSSFWorkbook wb, Map<String, CellStyle> cache, String formato,
			HorizontalAlignment alineacion, BorderStyle bordeInferior, boolean ajustarTexto) {
		String key = formato + "|" + alineacion + "|" + bordeInferior + "|" + ajustarTexto;
		CellStyle style = cache.get(key);
		if (style == null) {
			style = wb.createCellStyle();
			style.setAlignment(alineacion);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			style.setWrapText(ajustarTexto);
			short gris = IndexedColors.GREY_25_PERCENT.getIndex();
			style.setBorderTop(BorderStyle.THIN);
			style.setTopBorderColor(gris);
			style.setBorderLeft(BorderStyle.THIN);
			style.setLeftBorderColor(gris);
			style.setBorderRight(BorderStyle.THIN);
			style.setRightBorderColor(gris);
			style.setBorderBottom(bordeInferior);
			if (bordeInferior == BorderStyle.THIN) {
				style.setBottomBorderColor(gris);
			} else {
				style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			}
			if (formato != null) {
				style.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat(formato));
			}
			cache.put(key, style);
		}
		return style;
	}

	/** Serie - Numero (ej. E001 - 61339). */
	private String nroCpe(ComprobanteSunatBean bean) {
		String serie = nvl(bean.getSerie()).trim();
		String numero = nvl(bean.getNumero()).trim();
		if (serie.isEmpty()) {
			return numero;
		}
		if (numero.isEmpty()) {
			return serie;
		}
		return serie + " - " + numero;
	}

	/** Numero de documento - Nombre del cliente. */
	private String receptor(ComprobanteSunatBean bean) {
		String doc = nvl(bean.getClienteNumero()).trim();
		String nombre = nvl(bean.getClienteNombre()).trim();
		if (doc.isEmpty()) {
			return nombre;
		}
		if (nombre.isEmpty()) {
			return doc;
		}
		return doc + " - " + nombre;
	}

	/** Formato numerico segun moneda: dolares ($) o soles (S/). */
	private String formatoMoneda(String moneda) {
		return esDolares(moneda) ? FORMATO_DOLARES : FORMATO_SOLES;
	}

	private boolean esDolares(String moneda) {
		String v = nvl(moneda).trim().toUpperCase();
		return v.startsWith("D") || v.contains("USD") || v.contains("US$");
	}

	/**
	 * Fila de total: etiqueta alineada a la derecha (combinada en Nro. CPE + Receptor)
	 * y el monto en la columna Importe Total, en negrita.
	 */
	private void escribirTotal(XSSFWorkbook wb, Map<String, CellStyle> cache, Sheet sheet, int rowNum,
			String etiqueta, BigDecimal monto, String formato) {
		Row row = sheet.createRow(rowNum);
		row.setHeightInPoints(26);
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, COL_NRO_CPE, COL_RECEPTOR));

		CellStyle estiloEtiqueta = estiloTotal(wb, cache, null, HorizontalAlignment.RIGHT);
		Cell cell = row.createCell(COL_NRO_CPE);
		cell.setCellValue(etiqueta);
		cell.setCellStyle(estiloEtiqueta);
		// celda combinada: mismo estilo (borde) en la segunda celda
		cell = row.createCell(COL_RECEPTOR);
		cell.setCellStyle(estiloEtiqueta);

		cell = row.createCell(COL_IMPORTE);
		cell.setCellValue(monto.doubleValue());
		cell.setCellStyle(estiloTotal(wb, cache, formato, HorizontalAlignment.LEFT));
	}

	/** Estilo en negrita para las filas de totales (se reutiliza por formato/alineacion). */
	private CellStyle estiloTotal(XSSFWorkbook wb, Map<String, CellStyle> cache, String formato,
			HorizontalAlignment alineacion) {
		String key = "TOTAL|" + formato + "|" + alineacion;
		CellStyle style = cache.get(key);
		if (style == null) {
			style = wb.createCellStyle();
			style.cloneStyleFrom(estilo(wb, cache, formato, alineacion, BorderStyle.THIN, false));
			Font font = wb.createFont();
			font.setBold(true);
			style.setFont(font);
			cache.put(key, style);
		}
		return style;
	}

	private Date parseFecha(String texto) {
		if (texto == null || texto.trim().isEmpty()) {
			return null;
		}
		String v = texto.trim();
		if (v.length() > 10) {
			v = v.substring(0, 10);
		}
		for (String formato : FORMATOS_FECHA) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(formato);
				sdf.setLenient(false);
				return sdf.parse(v);
			} catch (ParseException e) {
				// probar el siguiente formato
			}
		}
		return null;
	}

	private String nvl(String valor) {
		return valor == null ? "" : valor;
	}
}