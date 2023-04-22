package com.pe.lima.sg.presentacion.reporte.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.pe.lima.sg.bean.reporte.IngresoEgresoBean;
import com.pe.lima.sg.presentacion.Filtro;
@Component
public class IngresoEgresoXls {
	
	public void reporteIngresoEgresoXls(List<IngresoEgresoBean> listaDataReporte, Filtro criterio, ServletContext context,HttpServletRequest request, HttpServletResponse response, String nameFile) throws IOException{
		String[] columns = {"Usuario","Modulo","Tienda","RUC","Concepto","Operación","Numero","Fecha","Soles S/","Tipo Cambio","Dolares $","Tipo Pago","Detalle Modulo","Fecha Cobro","Fecha Registro","Periodo"};
		
		String filePath = context.getRealPath("/resources/reports");
		File file = new File(filePath);
		boolean exists = new File(filePath).exists();
		if(!exists){
			new File(filePath).mkdirs();
		}
		
		
		Workbook workbook = new XSSFWorkbook();
		int rowNum = 3;
		
		CreationHelper createHelper = workbook.getCreationHelper();
		Sheet sheet = workbook.createSheet("IngresoEgreso");
		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerCellStyle.setBorderBottom(BorderStyle.THIN);
		headerCellStyle.setBorderLeft(BorderStyle.THIN);
		//Titulo
		CellRangeAddress titulo = new CellRangeAddress(0,0,0,4);
		
		sheet.addMergedRegion(titulo);
		Row rowTitulo = sheet.createRow(0);
		Cell cellTitulo = rowTitulo.createCell(0);
		cellTitulo.setCellValue(criterio.getNombre() + " \n"+ " REPORTE DIARIO DEL " + criterio.getFechaInicio() + " AL "+ criterio.getFechaFin());
		cellTitulo.setCellStyle(headerCellStyle);
		CellUtil.setCellStyleProperty(cellTitulo, CellUtil.ALIGNMENT, HorizontalAlignment.CENTER); 
		//Criterio
		/*Row rowCriterio = sheet.createRow(1);
		Cell cellCriterio = rowCriterio.createCell(0);
		cellCriterio.setCellValue("Fecha: ");
		cellCriterio.setCellStyle(headerCellStyle);
		cellCriterio = rowCriterio.createCell(1);
		cellCriterio.setCellValue(criterio.getFechaFin());*/
		//Columnas
		Row headerRow = sheet.createRow(2);
		
		//Create Cells
		 for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
            CellUtil.setCellStyleProperty(cell, CellUtil.ALIGNMENT, HorizontalAlignment.CENTER); 
        }
		// Create Cell Style for formatting Date
	    CellStyle dateCellStyle = workbook.createCellStyle();
	    dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
	    
	    for(IngresoEgresoBean ingresoEgresoBean: listaDataReporte){
			
			
			Row row = sheet.createRow(rowNum++);
	    	row.createCell(0).setCellValue(ingresoEgresoBean.getNombre());
	    	row.createCell(1).setCellValue(ingresoEgresoBean.getModulo());
	    	row.createCell(2).setCellValue(ingresoEgresoBean.getNumero());
	    	row.createCell(3).setCellValue(ingresoEgresoBean.getNumero_ruc());
	    	row.createCell(4).setCellValue(ingresoEgresoBean.getNota());
	    	row.createCell(5).setCellValue(ingresoEgresoBean.getTipo_bancarizado());
	    	row.createCell(6).setCellValue(ingresoEgresoBean.getNumero_operacion());
	    	Cell fechaOperacionCell = row.createCell(7);
	    	fechaOperacionCell.setCellValue(ingresoEgresoBean.getFecha_operacion());
	    	fechaOperacionCell.setCellStyle(dateCellStyle);
	    	row.createCell(8).setCellValue(ingresoEgresoBean.getMonto_soles().doubleValue());
	    	row.createCell(9).setCellValue(ingresoEgresoBean.getTipo_cambio().doubleValue());
	    	row.createCell(10).setCellValue(ingresoEgresoBean.getMonto_dolares().doubleValue());
	    	row.createCell(11).setCellValue(ingresoEgresoBean.getTipo_pago());
	    	row.createCell(12).setCellValue(ingresoEgresoBean.getTipo_cobro());
	    	Cell fechaCobroCell = row.createCell(13);
	    	fechaCobroCell.setCellValue(ingresoEgresoBean.getFecha_cobro());
	    	fechaCobroCell.setCellStyle(dateCellStyle);
	    	Cell fechaCreacion = row.createCell(14);
	    	fechaCreacion.setCellValue(ingresoEgresoBean.getFecha_creacion());
	    	fechaCreacion.setCellStyle(dateCellStyle);
	    	row.createCell(15).setCellValue(getPeriodo(ingresoEgresoBean.getNota()));
		}	    
	    // Resize all columns to fit the content size
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
        FileOutputStream fileOut = new FileOutputStream(file+ "/" + nameFile+".xlsx");
        workbook.write(fileOut);
        fileOut.flush();
        fileOut.close();

        // Closing the workbook
        workbook.close();
	}
	
	private String getPeriodo(String descripcion) {
		String periodo = "";
		if (descripcion != null && descripcion.length()>0) {
			int pos = descripcion.toUpperCase().indexOf("MES DE");
			if (pos> -1) {
				if (descripcion.length() > pos+7 ) {
					periodo = descripcion.substring(pos+7);
					pos = periodo.indexOf(".");
					if (pos > -1) {
						periodo = periodo.substring(0, pos);
						return periodo;
					}else {
						return periodo;
					}
				}else {
					return periodo;
				}
				
				
			}else {
				return periodo;
			}
		}else {
			return periodo;
		}
	}
	
}
