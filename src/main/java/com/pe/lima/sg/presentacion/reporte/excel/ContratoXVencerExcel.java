package com.pe.lima.sg.presentacion.reporte.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
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

import com.pe.lima.sg.bean.reporte.CriterioReporteBean;
import com.pe.lima.sg.bean.reporte.ContratoXVencerBean;

@Component
public class ContratoXVencerExcel {
	
	public void reporteContratoXVencer(List<ContratoXVencerBean> listaContrato, CriterioReporteBean criterio,ServletContext context,HttpServletRequest request, HttpServletResponse response, String nameFile) throws IOException{
		String[] columns = {"Tienda", "Nombre del Cliente", "Fecha Fin", "Alquiler $", "Servicio S/"};
		
		String filePath = context.getRealPath("/resources/reports");
		File file = new File(filePath);
		boolean exists = new File(filePath).exists();
		if(!exists){
			new File(filePath).mkdirs();
		}
		
		
		Workbook workbook = new XSSFWorkbook();
		int rowNum = 3;
		
		CreationHelper createHelper = workbook.getCreationHelper();
		Sheet sheet = workbook.createSheet("Contrato");
		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.BLUE.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		//Titulo
		CellRangeAddress titulo = new CellRangeAddress(0,0,0,6);
		
		sheet.addMergedRegion(titulo);
		Row rowTitulo = sheet.createRow(0);
		Cell cellTitulo = rowTitulo.createCell(0);
		cellTitulo.setCellValue("REPORTE DE CONTRATOS POR VENCER");
		cellTitulo.setCellStyle(headerCellStyle);
		CellUtil.setCellStyleProperty(cellTitulo, CellUtil.ALIGNMENT, HorizontalAlignment.CENTER); 
		//Criterio
		Row rowCriterio = sheet.createRow(1);
		Cell cellCriterio = rowCriterio.createCell(0);
		cellCriterio.setCellValue("Fecha: ");
		cellCriterio.setCellStyle(headerCellStyle);
		cellCriterio = rowCriterio.createCell(1);
		cellCriterio.setCellValue(criterio.getFechaFin());
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
	    
	    for(ContratoXVencerBean contrato: listaContrato){
	    	Row row = sheet.createRow(rowNum++);
	    	row.createCell(0).setCellValue(contrato.getNumeroTienda());
	    	row.createCell(1).setCellValue(contrato.getNombreCliente());
	    	Cell dateOfBirthCell = row.createCell(2);
	    	dateOfBirthCell.setCellValue(contrato.getFechaVencimiento());
            dateOfBirthCell.setCellStyle(dateCellStyle);
	    	row.createCell(3).setCellValue(contrato.getMontoAlquiler().doubleValue());
	    	row.createCell(4).setCellValue(contrato.getMontoServicio().doubleValue());
	    	
	    }
	    // Resize all columns to fit the content size
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
        FileOutputStream fileOut = new FileOutputStream(file+ "/" + nameFile+".xls");
        workbook.write(fileOut);
        fileOut.flush();
        fileOut.close();

        // Closing the workbook
        workbook.close();
	}

}
