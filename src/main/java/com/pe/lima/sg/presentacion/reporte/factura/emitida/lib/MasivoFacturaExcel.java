package com.pe.lima.sg.presentacion.reporte.factura.emitida.lib;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.pe.lima.sg.bean.caja.ComprobanteSunatBean;
import com.pe.lima.sg.bean.caja.MasivoSunatBean;

@Component
public class MasivoFacturaExcel {

	private static final String[] COLUMNAS = {
		"FECHA", "TIPO DE DOC.", "SERIE/NUMERO", "NUMERO RUC", "R. SOCIAL", "MONEDA", "TC SUNAT",
		"BASE IMPONIBLE DOLARES/SOLES", "VENTAS NO GRAVADAS DOLARES", "IGV", "TOTAL DOLARES/SOLES", "CONCEPTO"
	};

	private static final int COL_FECHA = 0;
	private static final int COL_TIPO_DOC = 1;
	private static final int COL_SERIE_NUMERO = 2;
	private static final int COL_RUC = 3;
	private static final int COL_RAZON_SOCIAL = 4;
	private static final int COL_MONEDA = 5;
	private static final int COL_TC = 6;
	private static final int COL_BASE = 7;
	private static final int COL_NO_GRAVADAS = 8;
	private static final int COL_IGV = 9;
	private static final int COL_TOTAL = 10;
	private static final int COL_CONCEPTO = 11;

	private static final String[] FORMATOS_FECHA = { "yyyy-MM-dd", "dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd" };

	public void reporteMasivoFacturaEmitidos(List<ComprobanteSunatBean> listaFacturaEmitidos, MasivoSunatBean criterio,
			ServletContext context, HttpServletRequest request, HttpServletResponse response, String nameFile) throws IOException {

		String filePath = context.getRealPath("/resources/reports");
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}

		XSSFWorkbook workbook = new XSSFWorkbook();
		try {
			String periodo = (criterio != null && criterio.getPeriodo() != null) ? criterio.getPeriodo() : "";
			String nombreHoja = periodo.isEmpty() ? "Ventas" : "Ventas " + periodo;
			Sheet sheet = workbook.createSheet(nombreHoja.length() > 31 ? nombreHoja.substring(0, 31) : nombreHoja);

			Map<String, CellStyle> cache = new HashMap<String, CellStyle>();

			// ---------- Cabecera (fila 1, color verde agua, texto en varias lineas) ----------
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 11);

			XSSFCellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setFont(headerFont);
			headerStyle.setFillForegroundColor(new XSSFColor(new Color(148, 201, 193)));
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			headerStyle.setWrapText(true);
			headerStyle.setBorderTop(BorderStyle.THIN);
			headerStyle.setBorderBottom(BorderStyle.THIN);
			headerStyle.setBorderLeft(BorderStyle.THIN);
			headerStyle.setBorderRight(BorderStyle.THIN);

			Row headerRow = sheet.createRow(0);
			headerRow.setHeightInPoints(48);
			for (int i = 0; i < COLUMNAS.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(COLUMNAS[i]);
				cell.setCellStyle(headerStyle);
			}

			// ---------- Detalle ----------
			int rowNum = 1;
			String monedaAnterior = null;
			// Totales por moneda: [base, igv, total]
			Map<String, BigDecimal[]> totales = new LinkedHashMap<String, BigDecimal[]>();

			if (listaFacturaEmitidos != null) {
				for (ComprobanteSunatBean bean : listaFacturaEmitidos) {
					String moneda = codigoMoneda(bean.getMoneda());
					// Linea separadora cuando cambia la moneda (D -> S)
					boolean separador = monedaAnterior != null && !moneda.equals(monedaAnterior);
					monedaAnterior = moneda;

					Row row = sheet.createRow(rowNum++);

					// Fecha
					Date fecha = parseFecha(bean.getFechaEmision());
					Cell cell = row.createCell(COL_FECHA);
					if (fecha != null) {
						cell.setCellValue(fecha);
						cell.setCellStyle(estilo(workbook, cache, "dd/MM/yyyy", HorizontalAlignment.CENTER, separador, false));
					} else {
						cell.setCellValue(nvl(bean.getFechaEmision()));
						cell.setCellStyle(estilo(workbook, cache, null, HorizontalAlignment.CENTER, separador, false));
					}

					// Tipo de documento
					cell = row.createCell(COL_TIPO_DOC);
					cell.setCellValue(nvl(bean.getClienteTipoDocumento()));
					cell.setCellStyle(estilo(workbook, cache, null, HorizontalAlignment.CENTER, separador, false));

					// Serie-Numero
					cell = row.createCell(COL_SERIE_NUMERO);
					cell.setCellValue(serieNumero(bean));
					cell.setCellStyle(estilo(workbook, cache, null, HorizontalAlignment.CENTER, separador, false));

					// RUC / documento del cliente (numerico, salvo que empiece con 0 para no perder ceros)
					cell = row.createCell(COL_RUC);
					String doc = nvl(bean.getClienteNumero()).trim();
					if (doc.matches("[1-9]\\d{0,14}")) {
						cell.setCellValue(Double.parseDouble(doc));
						cell.setCellStyle(estilo(workbook, cache, "0", HorizontalAlignment.RIGHT, separador, false));
					} else {
						cell.setCellValue(doc);
						cell.setCellStyle(estilo(workbook, cache, null, HorizontalAlignment.RIGHT, separador, false));
					}

					// Razon social
					cell = row.createCell(COL_RAZON_SOCIAL);
					cell.setCellValue(nvl(bean.getClienteNombre()));
					cell.setCellStyle(estilo(workbook, cache, null, HorizontalAlignment.LEFT, separador, false));

					// Moneda (D / S)
					cell = row.createCell(COL_MONEDA);
					cell.setCellValue(moneda);
					cell.setCellStyle(estilo(workbook, cache, null, HorizontalAlignment.CENTER, separador, false));

					// TC SUNAT
					cell = row.createCell(COL_TC);
					cell.setCellValue(valor(bean.getTipoCambio()));
					cell.setCellStyle(estilo(workbook, cache, "0.00#", HorizontalAlignment.RIGHT, separador, false));

					// Base imponible
					cell = row.createCell(COL_BASE);
					cell.setCellValue(valor(bean.getTotalGravados()));
					cell.setCellStyle(estilo(workbook, cache, "0.00", HorizontalAlignment.RIGHT, separador, false));

					// Ventas no gravadas (el bean no la trae: queda en blanco, pero con borde)
					cell = row.createCell(COL_NO_GRAVADAS);
					cell.setCellStyle(estilo(workbook, cache, "0.00", HorizontalAlignment.RIGHT, separador, false));

					// IGV
					cell = row.createCell(COL_IGV);
					cell.setCellValue(valor(bean.getTotalIgv()));
					cell.setCellStyle(estilo(workbook, cache, "0.00", HorizontalAlignment.RIGHT, separador, false));

					// Total
					cell = row.createCell(COL_TOTAL);
					cell.setCellValue(valor(bean.getTotal()));
					cell.setCellStyle(estilo(workbook, cache, "0.00", HorizontalAlignment.RIGHT, separador, false));

					// Concepto
					cell = row.createCell(COL_CONCEPTO);
					cell.setCellValue(nvl(bean.getConcepto()));
					cell.setCellStyle(estilo(workbook, cache, null, HorizontalAlignment.LEFT, separador, false));

					// Acumular totales
					BigDecimal[] acum = totales.get(moneda);
					if (acum == null) {
						acum = new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO };
						totales.put(moneda, acum);
					}
					acum[0] = acum[0].add(nvlBig(bean.getTotalGravados()));
					acum[1] = acum[1].add(nvlBig(bean.getTotalIgv()));
					acum[2] = acum[2].add(nvlBig(bean.getTotal()));
				}
			}

			// ---------- Totales por moneda (una fila en blanco de separacion) ----------
			rowNum++;
			for (Map.Entry<String, BigDecimal[]> entry : totales.entrySet()) {
				Row row = sheet.createRow(rowNum++);
				Cell cell = row.createCell(COL_TC);
				cell.setCellValue("TOTAL " + entry.getKey());
				cell.setCellStyle(estilo(workbook, cache, null, HorizontalAlignment.RIGHT, false, true));

				BigDecimal[] t = entry.getValue();
				int[] columnas = { COL_BASE, COL_IGV, COL_TOTAL };
				for (int i = 0; i < columnas.length; i++) {
					cell = row.createCell(columnas[i]);
					cell.setCellValue(t[i].doubleValue());
					cell.setCellStyle(estilo(workbook, cache, "0.00", HorizontalAlignment.RIGHT, false, true));
				}
			}

			// ---------- Anchos, inmovilizar cabecera y zoom ----------
			sheet.setColumnWidth(COL_FECHA, 12 * 256);
			sheet.setColumnWidth(COL_TIPO_DOC, 9 * 256);
			sheet.setColumnWidth(COL_SERIE_NUMERO, 16 * 256);
			sheet.setColumnWidth(COL_RUC, 15 * 256);
			sheet.setColumnWidth(COL_MONEDA, 10 * 256);
			sheet.setColumnWidth(COL_TC, 11 * 256);
			sheet.setColumnWidth(COL_BASE, 16 * 256);
			sheet.setColumnWidth(COL_NO_GRAVADAS, 16 * 256);
			sheet.setColumnWidth(COL_IGV, 11 * 256);
			sheet.setColumnWidth(COL_TOTAL, 16 * 256);
			sheet.autoSizeColumn(COL_RAZON_SOCIAL);
			sheet.autoSizeColumn(COL_CONCEPTO);
			// Evita que la columna de la razon social/concepto quede mas angosta que su titulo
			if (sheet.getColumnWidth(COL_RAZON_SOCIAL) < 30 * 256) {
				sheet.setColumnWidth(COL_RAZON_SOCIAL, 30 * 256);
			}

			sheet.createFreezePane(0, 1);
			sheet.setZoom(80); // vista al 80%

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
	 * Crea (o reutiliza) un estilo con bordes finos. Evita crear un estilo por celda
	 * (Excel limita a ~64000 estilos).
	 */
	private CellStyle estilo(XSSFWorkbook wb, Map<String, CellStyle> cache, String formato,
			HorizontalAlignment alineacion, boolean bordeSuperiorMedio, boolean negrita) {
		String key = formato + "|" + alineacion + "|" + bordeSuperiorMedio + "|" + negrita;
		CellStyle style = cache.get(key);
		if (style == null) {
			style = wb.createCellStyle();
			style.setAlignment(alineacion);
			style.setBorderTop(bordeSuperiorMedio ? BorderStyle.MEDIUM : BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderRight(BorderStyle.THIN);
			if (formato != null) {
				style.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat(formato));
			}
			if (negrita) {
				Font font = wb.createFont();
				font.setBold(true);
				style.setFont(font);
			}
			cache.put(key, style);
		}
		return style;
	}

	private String serieNumero(ComprobanteSunatBean bean) {
		String serie = nvl(bean.getSerie()).trim();
		String numero = nvl(bean.getNumero()).trim();
		if (serie.isEmpty()) {
			return numero;
		}
		if (numero.isEmpty()) {
			return serie;
		}
		return serie + "-" + numero;
	}

	/** Normaliza la moneda a "D" (dolares) o "S" (soles). */
	private String codigoMoneda(String moneda) {
		if (moneda == null) {
			return "";
		}
		String v = moneda.trim().toUpperCase();
		if (v.startsWith("D") || v.contains("USD") || v.contains("US$")) {
			return "D";
		}
		if (v.startsWith("S") || v.startsWith("PEN") || v.contains("SOL")) {
			return "S";
		}
		return v;
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

	private BigDecimal nvlBig(BigDecimal valor) {
		return valor == null ? BigDecimal.ZERO : valor;
	}

	private double valor(BigDecimal valor) {
		return nvlBig(valor).doubleValue();
	}
}