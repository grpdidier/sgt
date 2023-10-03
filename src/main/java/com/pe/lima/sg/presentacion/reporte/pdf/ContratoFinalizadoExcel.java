package com.pe.lima.sg.presentacion.reporte.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
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

import com.pe.lima.sg.bean.reporte.LocalBean;
import com.pe.lima.sg.bean.reporte.LocalSubTotalBean;
import com.pe.lima.sg.bean.reporte.LocalTotalBean;
import com.pe.lima.sg.presentacion.Filtro;
@Component
public class ContratoFinalizadoExcel {
	
	public void reporteContratoFinalizado(List<LocalTotalBean> listaDataReporte, Filtro criterio, ServletContext context,HttpServletRequest request, HttpServletResponse response, String nameFile) throws IOException{
		//String[] columns = {"LOCAL", "DETALLE", "ESTADO", "ALQUILER $", "SERVICIOS S/"};
		String[] columns = {"Nro","INMUEBLE", "LOCAL", "RUC", "ARRENDATARIO", "ESTADO","INICIO CONTRATO","FIN CONTRATO", "ALQUILER $", "SERVICIOS S/", "GARANTIA $", "FECHA FINALIZACION"};
		
		String filePath = context.getRealPath("/resources/reports");
		File file = new File(filePath);
		boolean exists = new File(filePath).exists();
		if(!exists){
			new File(filePath).mkdirs();
		}
		
		
		Workbook workbook = new XSSFWorkbook();
		int rowNum = 3;
		
		CreationHelper createHelper = workbook.getCreationHelper();
		Sheet sheet = workbook.createSheet("Locales");
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
		CellRangeAddress titulo = new CellRangeAddress(0,0,0,9);
		
		sheet.addMergedRegion(titulo);
		Row rowTitulo = sheet.createRow(0);
		Cell cellTitulo = rowTitulo.createCell(0);
		cellTitulo.setCellValue("RELACIÓN DE CONTRATOS FINALIZADOS - "+ criterio.getFechaInicio() + "  AL "+ criterio.getFechaFin());
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
	    
	    for(LocalTotalBean localTotalBean: listaDataReporte){
			Collections.sort(localTotalBean.getListaLocalSubTotal());
			
			for(LocalSubTotalBean localSubTotalBean : localTotalBean.getListaLocalSubTotal()){
				//Locales
				Integer contador = 1;
				for(LocalBean localBean:localSubTotalBean.getListaLocal() ){
					Row row = sheet.createRow(rowNum++);
					row.createCell(0).setCellValue(contador++);
					row.createCell(1).setCellValue(localBean.getNombreInmueble());
			    	row.createCell(2).setCellValue(localBean.getNumeroTienda());
			    	row.createCell(3).setCellValue(localBean.getRucCliente());
			    	row.createCell(4).setCellValue(localBean.getNombreCliente());
			    	row.createCell(5).setCellValue(localBean.getEstado());
			    	Cell cell = row.createCell(6);
			    	cell.setCellValue(localBean.getFechaInicioContrato());
			    	CellUtil.setCellStyleProperty(cell, CellUtil.ALIGNMENT, HorizontalAlignment.CENTER); 
			    	cell = row.createCell(7);
			    	cell.setCellValue(localBean.getFechaFinContrato());
			    	CellUtil.setCellStyleProperty(cell, CellUtil.ALIGNMENT, HorizontalAlignment.CENTER); 
			    	row.createCell(8).setCellValue(localBean.getMontoAlquiler().doubleValue());
			    	row.createCell(9).setCellValue(localBean.getMontoServicio().doubleValue());
			    	row.createCell(10).setCellValue(localBean.getMontoGarantia().doubleValue());
			    	cell = row.createCell(11);
			    	cell.setCellValue(localBean.getFechaFinalizacion());
			    	CellUtil.setCellStyleProperty(cell, CellUtil.ALIGNMENT, HorizontalAlignment.CENTER); 
			    	
				}
				Row row = sheet.createRow(rowNum++);
		    	row.createCell(0).setCellValue("");
		    	row.createCell(1).setCellValue("");
		    	row.createCell(2).setCellValue("");
		    	row.createCell(3).setCellValue("");
		    	row.createCell(4).setCellValue("");
		    	row.createCell(5).setCellValue("");
		    	row.createCell(6).setCellValue("");
		    	row.createCell(7).setCellValue("TOTAL");
		    	row.createCell(8).setCellValue(localSubTotalBean.getSubTotalAlquiler().doubleValue());
		    	row.createCell(9).setCellValue(localSubTotalBean.getSubTotalServicio().doubleValue());
		    	row.createCell(10).setCellValue(localSubTotalBean.getSubTotalGarantia().doubleValue());
		    	row.createCell(11).setCellValue("");
			}
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
	
}
