package com.pe.lima.sg.presentacion.util;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import com.pe.lima.sg.bean.caja.CobroArbitrioBean;
import com.pe.lima.sg.bean.caja.CobroBean;
import com.pe.lima.sg.bean.caja.CobroGarantia;
import com.pe.lima.sg.bean.caja.CobroLuzBean;
import com.pe.lima.sg.bean.caja.CobroPrimerCobro;
import com.pe.lima.sg.bean.caja.CobroServicioBean;
import com.pe.lima.sg.bean.caja.FacturaBean;
import com.pe.lima.sg.bean.caja.NotaBean;
import com.pe.lima.sg.bean.cliente.PeriodoBean;
import com.pe.lima.sg.entity.caja.TblCobro;
import com.pe.lima.sg.entity.caja.TblSunatCabecera;
import com.pe.lima.sg.entity.caja.TblSunatDetalle;
import com.pe.lima.sg.entity.cliente.TblContrato;
import com.pe.lima.sg.entity.mantenimiento.TblParametro;
import com.pe.lima.sg.entity.mantenimiento.TblSerie;
import com.pe.lima.sg.entity.seguridad.TblUsuario;
import com.pe.lima.sg.facturador.entity.TblDetalleComprobante;
import com.pe.lima.sg.facturador.entity.TblTributoGeneral;
import com.pe.lima.sg.facturador.entity.TblTributoGeneralNota;


import lombok.extern.slf4j.Slf4j;
@Slf4j
public class UtilSGT {
	/**
	 * Adiciona a una fecha Date un numero de dias
	 */
	public static Date addDays(Date date, int days)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days); //minus number would decrement the days
		return cal.getTime();
	}
	
	public static Date addMonths(Date date, int months)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, months); //minus number would decrement the days
		return cal.getTime();
	}
	
	public static Date getLastDays(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int lastDate = cal.getActualMaximum(Calendar.DATE);
		cal.set(Calendar.DATE, lastDate);
		return cal.getTime();
	}
	/**
	 * Obtiene una fecha en formato String
	 */
	public static String getDateStringFormat(Date date){
		SimpleDateFormat dt1 = null;
		try{
			dt1 = new SimpleDateFormat("yyyy-MM-dd");

		}catch(Exception e){
			e.printStackTrace();
		}

		return  dt1.format(date);
	}
	/**
	 * Obtiene una fecha en formato String
	 */
	public static String getDateStringFormatYYYMMDD(Date date){
		SimpleDateFormat dt1 = null;
		try{
			dt1 = new SimpleDateFormat("yyyyMMdd");

		}catch(Exception e){
			e.printStackTrace();
		}

		return  dt1.format(date);
	}
	/**
	 * 
	 * Obtiene los dias de diferencia
	 */
	public static Integer getDiasFechas(String strFechaInicio, String strFechaFin){
		SimpleDateFormat dateFormat = null;
		int dias=0;
		try{
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			 
			Date fechaInicial=dateFormat.parse(strFechaInicio);
			Date fechaFinal=dateFormat.parse(strFechaFin);
	 
			dias=(int) ((fechaFinal.getTime()-fechaInicial.getTime())/86400000)+1;
		}catch(Exception e){
			dias = 0;
		}
		return dias;
	}
	/**
	 * Obtiene una fecha de tipo Date
	 */
	public static Date getDateFormat(Date date){
		Date date1 = null;
		try{
			SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
			date1 = dt1.parse( dt1.format(date));
		}catch(Exception e){
			e.printStackTrace();
		}

		return date1;
	}
	/**
	 * Obtiene una fecha Date a partir de una cadena
	 *
	 */
	public static Date getDatetoString(String strFecha){
		DateFormat formatter ; 
		Date date = null; 
		try{
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			date = formatter.parse(strFecha);
		}catch(Exception e){
			e.printStackTrace();
		}
		return date;


	}
	
	public static String getDatetoString2(Date dateFecha){
		String fecha = null; 
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			fecha = sdf.format(dateFecha); 
		}catch(Exception e){
			e.printStackTrace();
		}
		return fecha;


	}
	/*Obtenemos el mes para la fecha de morosos*/
	public static String getFechaMorosoCombo(){
		return  "01/" + getFecha("MM") + "/";
	}
	
	/**
	 * Obtiene la informacion de el fecha.
	 *
	 */
	public static String getFecha(String formato) {    	
		SimpleDateFormat FMT    = new SimpleDateFormat( formato );
		/* yyyy       = ano actual
		 * MM         = mes en dos digitos
		 * MMM        = Mes en espanol Ej: ene
		 * WW         = Semana del mes Ej: 02
		 * yyyy-MM-dd = Ano, Mes dia separados por '-' Ej: 2004-10-09
		 * dd         = Dia en 2 digitos Ej: 02
		 * yyyy/MM/dd = Ano, Mes dia separados por '/' Ej: 2004/10/09
		 * yyyyMMdd   = Ano, mes, dia sin separacion
		 * yyyy/MM/dd kk:mm:ss
		 * yyyy-MM-dd kk:mm:ss
		 */        
		String           Fecha  = FMT.format(new Date());        
		return Fecha.toUpperCase();
	}
	
	public static String getFecha(String formato, String strFecha) throws ParseException {    	
		SimpleDateFormat FMT    = new SimpleDateFormat( formato );
		DateFormat formatter ; 
		Date date = null;
		/* yyyy       = ano actual
		 * MM         = mes en dos digitos
		 * MMM        = Mes en espanol Ej: ene
		 * WW         = Semana del mes Ej: 02
		 * yyyy-MM-dd = Ano, Mes dia separados por '-' Ej: 2004-10-09
		 * dd         = Dia en 2 digitos Ej: 02
		 * yyyy/MM/dd = Ano, Mes dia separados por '/' Ej: 2004/10/09
		 * yyyyMMdd   = Ano, mes, dia sin separacion
		 * yyyy/MM/dd kk:mm:ss
		 * yyyy-MM-dd kk:mm:ss
		 */        
		formatter = new SimpleDateFormat("dd/MM/yyyy");
		date = formatter.parse(strFecha);
		String           Fecha  = FMT.format(date);        
		return Fecha.toUpperCase();
	}
	
	public static Date getFechaYYYYMMDD(){
		SimpleDateFormat FMT    = new SimpleDateFormat( "yyyy-MM-dd" );
		String           Fecha  = FMT.format(new Date());   
		DateFormat formatter ; 
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null; 
		try {
			date = formatter.parse(Fecha);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * Completa con ceros la cadena, el numero de ceros se calcula con el tamaño
	 */
	public static String completarCeros(String cadena, Integer tamano){

		int intTotal = 0;
		log.debug("cadena :"+cadena + ", tamano :"+tamano);
		try{
			intTotal = tamano - cadena.length();
			log.debug("intTotal :"+intTotal);
			if (intTotal >0){
				for(int i=0; i< intTotal; i++){
					cadena = Constantes.CADENA_CERO + cadena;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return cadena;
	}
	/**
	 * Obtiene el año de un DATE
	 *
	 */
	public static Integer getAnioDate(Date datFecha){
		Integer anio = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(datFecha);
		anio = cal.get(Calendar.YEAR);
		return anio;
	}
	/**
	 * Obtiene el mes de un DATE
	 *
	 */
	public static Integer getMesDate(Date datFecha){
		Integer mes = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(datFecha);
		mes = cal.get(Calendar.MONTH) + 1;
		return mes;
	}
	public static String getMesDateFormateado(Date datFecha){
		Integer mes = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(datFecha);
		mes = cal.get(Calendar.MONTH) + 1;
		return mes<10?"0"+mes:mes.toString();
	}
	/**
	 * Obtiene el mes de una fecha dd/MM/yyyy
	 * 
	 */
	public static Integer getMes(String strFecha){
		Integer mes = null;
		if (strFecha!=null){
			mes = new Integer (strFecha.substring(3, 5));
		}else{
			mes = Calendar.getInstance().get(Calendar.MONTH) ;
		}
		log.debug("[getMes] mes:"+mes);
		return mes;
	}
	/**
	 * Obtiene el año de una fecha dd/MM/yyyy
	 * 
	 */
	public static Integer getAnio(String strFecha){
		Integer anio = null;
		if (strFecha!=null){
			anio = new Integer (strFecha.substring(6, 10));
		}else{
			anio = Calendar.getInstance().get(Calendar.YEAR) ;
		}
		log.debug("[getAnio] anio:"+anio);
		return anio;
	}
	public static Integer getMesYYYYMMDD(String strFecha){
		Integer mes = null;
		if (strFecha!=null){
			mes = new Integer ((strFecha.replace("-", "")).substring(4, 6)) ;
		}else{
			mes = Calendar.getInstance().get(Calendar.MONTH) ;
		}
		log.debug("[getMes] mes:"+mes);
		return mes;
	}
	/**
	 * Listado de años (año inicio hasta año fin)
	 * 
	 */
	public static Map<String, Object> getListaAnio(Integer intAnioInicio, Integer intAnioFin) {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		try{
			log.debug("[getListaAnio] inicio");
			for(Integer intAnio=intAnioInicio; intAnio <= intAnioFin ; intAnio++){
				resultados.put(intAnio.toString(), intAnio.toString());
			}
			
			log.debug("[getListaAnio] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return resultados;
	}
	/*
	 * Fecha Inicio Primer Trimestre
	 */
	public static Date getPrimerTrimestreInicio(Integer anio){
		String strFecha = "01/01/" + anio;
		return getDatetoString(strFecha);
	}
	/*
	 * Fecha Fin Primer Trimestre
	 */
	public static Date getPrimerTrimestreFin(Integer anio){
		String strFecha = "31/03/" + anio;
		return getDatetoString(strFecha);
	}
	/*
	 * Fecha Fin Segundo Trimestre
	 */
	public static Date getSegundoTrimestreFin(Integer anio){
		String strFecha = "30/06/" + anio;
		return getDatetoString(strFecha);
	}
	/*
	 * Fecha Inicio Tercer Trimestre
	 */
	public static Date getTercerTrimestreInicio(Integer anio){
		String strFecha = "01/07/" + anio;
		return getDatetoString(strFecha);
	}
	/*
	 * Fecha Inicio Segundo Trimestre
	 */
	public static Date getSegundoTrimestreInicio(Integer anio){
		String strFecha = "01/04/" + anio;
		return getDatetoString(strFecha);
	}
	/*
	 * Fecha Fin Tercer Trimestre
	 */
	public static Date getTercerTrimestreFin(Integer anio){
		String strFecha = "30/09/" + anio;
		return getDatetoString(strFecha);
	}
	/*
	 * Fecha Inicio Cuarto Trimestre
	 */
	public static Date getCuartoTrimestreInicio(Integer anio){
		String strFecha = "01/10/" + anio;
		return getDatetoString(strFecha);
	}
	/*
	 * Fecha Fin Cuarto Trimestre
	 */
	public static Date getCuartoTrimestreFin(Integer anio){
		String strFecha = "31/12/" + anio;
		return getDatetoString(strFecha);
	}
	/*
	 * Obtiene la fecha del ultimo dia del mes, la fecha se pasa como parametro
	 */
	public static String getLastDate(String strFecha){
		Integer intMes = null;
		Integer intAnio	= null;
		String resultado = null;
		log.debug("[getLastDate] Inicio - strFecha:"+strFecha);
		if (strFecha!=null && !strFecha.equals("") && strFecha.length()==10){
			
			intMes = new Integer(strFecha.substring(5, 7))-1;
			intAnio = new Integer(strFecha.substring(0, 4));
			if (intMes == -1){
				intMes = 11;
				intAnio= intAnio -1;
			}
			
			log.debug("[getLastDate] intMes:"+intMes+" - intAnio:"+intAnio);
			if (intMes.compareTo(0)==0){
				resultado = "31/01/"+intAnio.toString();
			}else if (intMes.compareTo(1)==0){
				if(intAnio % 4 == 0){
					resultado = "29/02/"+intAnio.toString();
				}else{
					resultado = "28/02/"+intAnio.toString();
				}
			}else if(intMes.compareTo(2)==0){
				resultado = "31/03/"+intAnio.toString();
			}else if  (intMes.compareTo(3)==0){
				resultado = "30/04/"+intAnio.toString();
			}else if (intMes.compareTo(4)==0){
				resultado = "31/05/"+intAnio.toString();
			}else if (intMes.compareTo(5)==0){
				resultado = "30/06/"+intAnio.toString();
			}else if (intMes.compareTo(6)==0){
				resultado = "31/07/"+intAnio.toString();
			}else if (intMes.compareTo(7)==0){
				resultado = "31/08/"+intAnio.toString();
			}else if (intMes.compareTo(8)==0){
				resultado = "30/09/"+intAnio.toString();
			}else if (intMes.compareTo(9)==0){
				resultado = "31/10/"+intAnio.toString();
			}else if (intMes.compareTo(10)==0){
				resultado = "30/11/"+intAnio.toString();
			}else if (intMes.compareTo(11)==0){
				resultado = "31/12/"+intAnio.toString();
			}else {
				resultado = "Seleccionar";
			}
			
		}
		
		log.debug("[getLastDate] Fin - resultado:"+resultado);
		return resultado;
	}
	/*
	 * Obtiene el ultimo día del mes
	 */
	public static String getLastDay(Integer intMes, Integer intAnio){
		String resultado = null;
		if (intMes.compareTo(1)==0){
			resultado = "31/01/"+intAnio.toString();
		}else if (intMes.compareTo(2)==0){
			if(intAnio % 4 == 0){
				resultado = "29/02/"+intAnio.toString();
			}else{
				resultado = "28/02/"+intAnio.toString();
			}
		}else if(intMes.compareTo(3)==0){
			resultado = "31/03/"+intAnio.toString();
		}else if  (intMes.compareTo(4)==0){
			resultado = "30/04/"+intAnio.toString();
		}else if (intMes.compareTo(5)==0){
			resultado = "31/05/"+intAnio.toString();
		}else if (intMes.compareTo(6)==0){
			resultado = "30/06/"+intAnio.toString();
		}else if (intMes.compareTo(7)==0){
			resultado = "31/07/"+intAnio.toString();
		}else if (intMes.compareTo(8)==0){
			resultado = "31/08/"+intAnio.toString();
		}else if (intMes.compareTo(9)==0){
			resultado = "30/09/"+intAnio.toString();
		}else if (intMes.compareTo(10)==0){
			resultado = "31/10/"+intAnio.toString();
		}else if (intMes.compareTo(11)==0){
			resultado = "30/11/"+intAnio.toString();
		}else if (intMes.compareTo(12)==0){
			resultado = "31/12/"+intAnio.toString();
		}else {
			resultado = "31/01/"+intAnio.toString();
		}
		return resultado;
	}
	
	
	public static String getFechaNombre(Integer intMes, Integer intAnio){
		String resultado = null;
		String fechaDiaMes = getLastDay(intMes, intAnio);
		resultado = fechaDiaMes.substring(0,2) + "-"+ getMesPersonalizado(intMes) + "-"+ intAnio;
		return resultado;
	}
	
	public static String getPeriodo(Integer numeroPeriodos, Integer posicion) {
		String resultado = null;
		if (numeroPeriodos == 12) {
			resultado = "MES " +posicion;
		}else if (numeroPeriodos == 2) {
			resultado = "SEMESTRE " +posicion;
		}else if (numeroPeriodos == 3) {
			resultado = "CUATRIMESTRE " +posicion;
		}else if (numeroPeriodos == 4) {
			resultado = "TRIMESTRE " +posicion;
		}else if (numeroPeriodos == 6) {
			resultado = "BIMESTRE " +posicion;
		}else {
			resultado = "PERIODO " +posicion;
		}
		
		return resultado;
	}
	public static Map<String, String> getMapFechaPeriodo(){
		Map<String, String> mapPeriodo = new HashMap<String, String>();
		mapPeriodo.put("MES 1", "31/01/");
		mapPeriodo.put("MES 2", "28/02/");
		mapPeriodo.put("MES 3", "31/03/");
		mapPeriodo.put("MES 4", "30/04/");
		mapPeriodo.put("MES 5", "31/05/");
		mapPeriodo.put("MES 6", "30/06/");
		mapPeriodo.put("MES 7", "31/07/");
		mapPeriodo.put("MES 8", "31/08/");
		mapPeriodo.put("MES 9", "30/09/");
		mapPeriodo.put("MES 10", "31/10/");
		mapPeriodo.put("MES 11", "30/11/");
		mapPeriodo.put("MES 12", "31/12/");
		mapPeriodo.put("BIMESTRE 1", "28/02/");
		mapPeriodo.put("BIMESTRE 2", "30/04/");
		mapPeriodo.put("BIMESTRE 3", "30/06/");
		mapPeriodo.put("BIMESTRE 4", "31/08/");
		mapPeriodo.put("BIMESTRE 5", "31/10/");
		mapPeriodo.put("BIMESTRE 6", "31/12/");
		mapPeriodo.put("TRIMESTRE 1", "31/03/");
		mapPeriodo.put("TRIMESTRE 2", "30/06/");
		mapPeriodo.put("TRIMESTRE 3", "30/09/");
		mapPeriodo.put("TRIMESTRE 4", "31/12/");
		mapPeriodo.put("CUATRIMESTRE 1", "30/04/");
		mapPeriodo.put("CUATRIMESTRE 2", "31/08/");
		mapPeriodo.put("CUATRIMESTRE 3", "31/12/");
		mapPeriodo.put("SEMESTRE 1", "30/06/");
		mapPeriodo.put("SEMESTRE 2", "31/12/");
		return mapPeriodo;
	}
	
	public static String getFechaPeriodoPredefinido(Integer anio, String periodo, Map<String, String> mapPeriodo) throws ParseException {
		
		String fecha = mapPeriodo.get(periodo);
		if (fecha != null) {
			
	        return fecha +anio;
		}else {
			return null;
		}
		
		
		
	}
	
	public static Date getFechaFinPeriodo(Integer numeroMeses, Integer anio, int mes, String periodo, Map<String, String> mapPeriodo) throws ParseException {
		String sDate1 = null;
		Calendar cal = null;
		String fecha = getFechaPeriodoPredefinido(anio, periodo,mapPeriodo);
		if (fecha != null) {
			Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);  
		    cal = Calendar.getInstance();
	        cal.setTime(date1);
		}else {
			int daysInYear = Year.of( anio ).length();
			int incremento = (daysInYear / numeroMeses) * mes;
			sDate1 = "01/01/"+anio;
			Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);  
		    cal = Calendar.getInstance();
	        cal.setTime(date1);
	        cal.add(Calendar.DATE, incremento); //minus number would decrement the days
		}
        return cal.getTime();
	}
	
	public static String getMesPersonalizado(Integer mes){
		String resultado = null;
		if (mes == 1){
			resultado = "ENERO";
		}else if (mes == 2){
			resultado = "FEBRERO";
		}else if (mes == 3){
			resultado = "MARZO";
		}else if (mes == 4){
			resultado = "ABRIL";
		}else if (mes == 5){
			resultado = "MAYO";
		}else if (mes == 6){
			resultado = "JUNIO";
		}else if (mes == 7){
			resultado = "JULIO";
		}else if (mes == 8){
			resultado = "AGOSTO";
		}else if (mes == 9){
			resultado = "SEPTIEMBRE";
		}else if (mes == 10){
			resultado = "OCTUBRE";
		}else if (mes == 11){
			resultado = "NOVIEMBRE";
		}else if (mes == 12){
			resultado = "DICIEMBRE";
		}else {
			resultado = "MES";
		}
		
		return resultado;
	}
	/*
	 * Obtiene el primer día del mes
	 */
	public static String getFistDay(String strMes, Integer intAnio){
		String resultado = null;
		resultado = "01/"+strMes+"/"+intAnio;
		return resultado;
	}
	public static boolean validarFinMes(Date fecha){
		String strFecha = null;
		String strMes	= null;
		String strDia	= null;
		String strAnio	= null;
		String strDiaAux= null;
		boolean resultado = false;
		try{
			strFecha = getDateStringFormat(fecha);
			strAnio = strFecha.substring(0,4);
			strMes = strFecha.substring(5, 7);
			strDia = strFecha.substring(8);
			log.debug("[validarFinMes] Mes:"+strMes+ " Dia:"+strDia);
			strDiaAux = getOnlyLastDay(new Integer(strMes), new Integer(strAnio));
			log.debug("[validarFinMes] Last Day:"+strDiaAux);
			if (strDiaAux.equals(strDia)){
				resultado = true;
			}
		}catch(Exception e){
			
		}
		return resultado;
	}
	
	public static String formatFechaSGT(Date fecha){
		String strFecha = null;
		String strMes	= null;
		String strDia	= null;
		String strAnio	= null;
		String strResultado = null;
		try{
			strFecha = getDateStringFormat(fecha);
			strAnio = strFecha.substring(0,4);
			strMes = strFecha.substring(5, 7);
			strDia = strFecha.substring(8);
			strResultado = strDia + "-" + getMesPersonalizado(new Integer(strMes)) + "-"+ strAnio;
			
		}catch(Exception e){
			strResultado = "";
		}
		return strResultado;
	}
	public static String formatFechaSGTMensaje(Date fecha){
		String strFecha = null;
		String strMes	= null;
		String strAnio	= null;
		String strResultado = null;
		try{
			strFecha = getDateStringFormat(fecha);
			strAnio = strFecha.substring(0,4);
			strMes = strFecha.substring(5, 7);
			strResultado =  getMesPersonalizado(new Integer(strMes)) + "-"+ strAnio;
			
		}catch(Exception e){
			strResultado = "";
		}
		return strResultado;
	}
	/*
	 * Obtiene el ultimo día del mes
	 */
	public static String getOnlyLastDay(Integer intMes, Integer intAnio){
		String resultado = null;
		if (intMes.compareTo(0)==0){
			resultado = "31";
		}else if (intMes.compareTo(1)==0){
			if(intAnio % 4 == 0){
				resultado = "29";
			}else{
				resultado = "28";
			}
		}else if(intMes.compareTo(2)==0){
			resultado = "31";
		}else if  (intMes.compareTo(3)==0){
			resultado = "30";
		}else if (intMes.compareTo(4)==0){
			resultado = "31";
		}else if (intMes.compareTo(5)==0){
			resultado = "30";
		}else if (intMes.compareTo(6)==0){
			resultado = "31";
		}else if (intMes.compareTo(7)==0){
			resultado = "31";
		}else if (intMes.compareTo(8)==0){
			resultado = "30";
		}else if (intMes.compareTo(9)==0){
			resultado = "31";
		}else if (intMes.compareTo(10)==0){
			resultado = "30";
		}else if (intMes.compareTo(11)==0){
			resultado = "31";
		}else {
			resultado = "Seleccionar";
		}
		return resultado;
	}
	public static boolean comparaFechaHoy(Date fecha){
		boolean resultado = false;
		String strFecha = null;
		String strFechaHoy = null;
		strFecha = getDateStringFormat(fecha);
		strFechaHoy = getDateStringFormat(new Date());
		resultado = strFecha.equals(strFechaHoy);
		return resultado;
	}
	public static Date getDatePrevious(Date fecha){
		Date fechaPrevious = null;
		String strFecha = null;
		String strMes	= null;
		String strAnio	= null;
		String strFechaPrevious = null;
		Integer intAnio	= null;
		Integer intMes	= null;
		
		try{
			strFecha = getDateStringFormat(fecha);
			strAnio = strFecha.substring(0,4);
			strMes = strFecha.substring(5, 7);
			intMes = new Integer(strMes)-1;
			intAnio = new Integer(strAnio);
			if (intMes < 0){
				intMes = 12;
				intAnio = intAnio -1;
				
			}
			strFechaPrevious = getLastDay(intMes, intAnio);
			fechaPrevious = getDatetoString(strFechaPrevious);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			strFecha = null;
			strMes	= null;
			strAnio	= null;
			strFechaPrevious = null;
			intAnio	= null;
			intMes	= null;
		}
		return fechaPrevious;
	}
	
	public static Double obtenerFactorPrimerCobro(Date Fecha){
		Double dblFactor		= null;
		String strFecha			= "";
		String strDia			= null;
		String strMes			= null;
		String strAnio			= null;
		String strDiaFin		= null;
		Integer intMes			= null;
		Integer intAnio			= null;
		try{
			log.debug("[obtenerFactorPrimerCobro] Inicio");
			
			strFecha = getDateStringFormat(Fecha);
			strDia = strFecha.substring(8, 10);
			strMes = strFecha.substring(5, 7);
			strAnio = strFecha.substring(0, 4);
			log.debug("[obtenerFactorPrimerCobro] strFecha:"+strFecha+" strDia:"+strDia+" strMes:"+strMes+" strAnio:"+strAnio);
			intMes = new Integer(strMes)-1;
			intAnio = new Integer(strAnio); 
			if (intMes.compareTo(-1)==0){
				intMes = 11;
				intAnio = intAnio -1;
				
			}
			strDiaFin = getOnlyLastDay(intMes, intAnio);
			log.debug("[obtenerFactorPrimerCobro] intMes:"+intMes+" intAnio:"+intAnio);
			log.debug("[obtenerFactorPrimerCobro] strDiaFin:"+strDiaFin);
			dblFactor = (new Double(strDiaFin) - new Double(strDia) + 1 ) / new Double(strDiaFin);
			
			log.debug("[obtenerFactorPrimerCobro] Fin - intTotalDias:"+dblFactor);
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return dblFactor;
	}
	
	/*
	 * Obtiene el mes en curso
	 */
	public static String getMoth(){
		String strMes = null;
		Integer intMes = null;
		intMes = Calendar.getInstance().get(Calendar.MONDAY)+1;
		if (intMes>11){
			strMes = "01";
		}else{
			strMes = intMes.toString().length()==1? "0"+intMes.toString(): intMes.toString();
		}
		return strMes;
	}
	/*
	 * Genera el siguiente valor de la Serie (FXXX999999999)
	 */
	public static void obtenerSiguienteFactura(TblSerie entidad){
		try{
			if (entidad.getNumeroComprobante().compareTo(new Integer("999999999"))>=0){
				entidad.setNumeroComprobante(1);
				if (entidad.getSecuencialSerie().compareTo(new Integer("999"))>=0){
					log.debug("Se excedió el rango de la Serie... ");
					System.exit(0);
				}else{
					entidad.setSecuencialSerie(entidad.getSecuencialSerie()+1);
				}
			}else{
				entidad.setNumeroComprobante(entidad.getNumeroComprobante()+1);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void obtenerSiguienteComprobante(com.pe.lima.sg.facturador.entity.TblSerie serie){
		serie.setSecuencialSerie(serie.getSecuencialSerie()+1);
	}
	/*
	 * Genera el siguiente valor de la Serie (FXXX999999999)
	 */
	public static void obtenerSiguienteBoleta(TblSerie entidad){
		try{
			if (entidad.getNumeroComprobante().compareTo(new Integer("999999999"))>=0){
				entidad.setNumeroComprobante(1);
				if (entidad.getSecuencialSerie().compareTo(new Integer("999"))>=0){
					log.debug("Se excedió el rango de la Serie... ");
					System.exit(0);
				}else{
					entidad.setSecuencialSerie(entidad.getSecuencialSerie()+1);
				}
			}else{
				entidad.setNumeroComprobante(entidad.getNumeroComprobante()+1);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	 * Genera el siguiente valor de la Serie (FXXX999999999)
	 */
	public static void obtenerSiguienteInterno(TblSerie entidad){
		try{
			if (entidad.getNumeroComprobante().compareTo(new Integer("999999999"))>=0){
				entidad.setNumeroComprobante(1);
				if (entidad.getSecuencialSerie().compareTo(new Integer("999"))>=0){
					log.debug("Se excedió el rango de la Serie... ");
					System.exit(0);
				}else{
					entidad.setSecuencialSerie(entidad.getSecuencialSerie()+1);
				}
			}else{
				entidad.setNumeroComprobante(entidad.getNumeroComprobante()+1);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	 * Monto Gravada
	 */
	public static BigDecimal obtenerTotalMontoGravada(BigDecimal monto, Integer igv){
		BigDecimal resultado = null;
		try{
			resultado = new BigDecimal(monto.doubleValue()*(100-igv)/100).setScale(2, RoundingMode.CEILING);
			
		}catch(Exception e){
			resultado = new BigDecimal("0");
			e.printStackTrace();
		}
		return resultado;
	}
	/*
	 * Impuesto Gravada
	 */
	public static BigDecimal obtenerTotalImpuestoGravada(BigDecimal monto, Integer igv){
		BigDecimal resultado = null;
		try{
			resultado = new BigDecimal(monto.doubleValue()*(igv)/100).setScale(2, RoundingMode.CEILING);
			
		}catch(Exception e){
			resultado = new BigDecimal("0");
			e.printStackTrace();
		}
		return resultado;
	}
	/*
	 * Genera un archivo plano Cabecera
	 */
	public static boolean generarArchivoCabecera(TblSunatCabecera cabecera){
		boolean resultado = false;
		String cadena = null;
		BufferedWriter bufferedWriter = null;
		//String FILENAME = "G:\\data0\\facturador\\DATA\\"+cabecera.getNombreArchivo();
		String FILENAME = Constantes.SUNAT_FACTURADOR_RUTA_PRUEBA + cabecera.getNombreArchivo();
		try{
			cadena = cabecera.getTipoOperacion() + Constantes.SUNAT_PIPE +
					 cabecera.getFechaEmision() + Constantes.SUNAT_PIPE +
					 cabecera.getDomicilioFiscal() + Constantes.SUNAT_PIPE +
					 cabecera.getTipoDocumentoUsuario() + Constantes.SUNAT_PIPE +
					 cabecera.getNumeroDocumento() + Constantes.SUNAT_PIPE +
					 cabecera.getRazonSocial()	+ Constantes.SUNAT_PIPE +
					 cabecera.getTipoMoneda() + Constantes.SUNAT_PIPE +
					 cabecera.getSumaDescuento() + Constantes.SUNAT_PIPE +
					 cabecera.getSumaCargo() + Constantes.SUNAT_PIPE +
					 cabecera.getTotalDescuento() + Constantes.SUNAT_PIPE +
					 cabecera.getOperacionGravada() + Constantes.SUNAT_PIPE +
					 cabecera.getOperacionInafecta() + Constantes.SUNAT_PIPE +
					 cabecera.getOperacionExonerada() + Constantes.SUNAT_PIPE +
					 cabecera.getMontoIgv() + Constantes.SUNAT_PIPE +
					 cabecera.getMontoIsc() + Constantes.SUNAT_PIPE +
					 cabecera.getOtrosTributos() + Constantes.SUNAT_PIPE +
					 cabecera.getImporteTotal();
			bufferedWriter = new BufferedWriter(new FileWriter(FILENAME, true));
			bufferedWriter.write(cadena); 
			resultado = true;
					
		}catch(Exception e){
			e.printStackTrace();
			resultado = false;
		}finally{
			try{
				if (bufferedWriter !=null){
					bufferedWriter.close();
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
		}
		return resultado;
	}
	/*
	 * Genera un archivo plano Detalle
	 */
	public static boolean generarArchivoDetalle(TblSunatDetalle detalle){
		boolean resultado = false;
		String cadena = null;
		BufferedWriter bufferedWriter = null;
		String FILENAME = Constantes.SUNAT_FACTURADOR_RUTA_PRUEBA + detalle.getNombreArchivo();
		try{
			cadena = detalle.getCodigoUnidad() + Constantes.SUNAT_PIPE +
					 detalle.getCantidad() + Constantes.SUNAT_PIPE +
					 detalle.getCodigoProducto() + Constantes.SUNAT_PIPE +
					 detalle.getCodigoProductoSunat() + Constantes.SUNAT_PIPE +
					 detalle.getDescripcion() + Constantes.SUNAT_PIPE +
					 detalle.getValorUnitario()	+ Constantes.SUNAT_PIPE +
					 detalle.getDescuento() + Constantes.SUNAT_PIPE +
					 detalle.getMontoIgv() + Constantes.SUNAT_PIPE +
					 detalle.getAfectacionIgv() + Constantes.SUNAT_PIPE +
					 detalle.getMontoIsc() + Constantes.SUNAT_PIPE +
					 detalle.getTipoIsc() + Constantes.SUNAT_PIPE +
					 detalle.getPrecioVentaUnitario() + Constantes.SUNAT_PIPE +
					 detalle.getValorVentaItem();
			bufferedWriter = new BufferedWriter(new FileWriter(FILENAME, true));
			bufferedWriter.write(cadena); 
			resultado = true;
					
		}catch(Exception e){
			e.printStackTrace();
			resultado = false;
		}finally{
			try{
				if (bufferedWriter !=null){
					bufferedWriter.close();
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return resultado;
	}
	
	/************************************************************************************************************************************
	 *  METODOS reutilizables por las clases
	 * ***********************************************************************************************************************************/
	/*
	 * Identifica el tipo de moneda y calcula el monto en soles y dolares
	 */
	public static void mCalcularMontoCobro(CobroGarantia cobroGarantia) throws Exception{
		BigDecimal monto = null;
		try{
			monto = validarNullBigDecimal(cobroGarantia.getMontoSolesGarantia());
			if (cobroGarantia.getTipoMonedaGarantia().equals(Constantes.MONEDA_SOL)){
				cobroGarantia.setMontoSolesGarantia(monto);
				cobroGarantia.setMontoDolaresGarantia(monto.divide(cobroGarantia.getTipoCambioGarantia(),2, BigDecimal.ROUND_HALF_UP));
			}else{
				cobroGarantia.setMontoDolaresGarantia(monto);
				cobroGarantia.setMontoSolesGarantia(monto.multiply(cobroGarantia.getTipoCambioGarantia()));
			}
		}catch(Exception e){
			e.printStackTrace();
			cobroGarantia.setMontoSolesGarantia(new BigDecimal("0"));
			cobroGarantia.setMontoDolaresGarantia(new BigDecimal("0"));
			throw e;
		}finally{
			monto = null;
		}
	}
	public static BigDecimal montoRedondeado(BigDecimal monto){
		return monto.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	public static BigDecimal montoDivide(BigDecimal monto, BigDecimal tipoCambio){
		if (tipoCambio != null && tipoCambio.doubleValue() > 0)
			return monto.divide(tipoCambio,2,BigDecimal.ROUND_HALF_UP);
		else
			return new BigDecimal("0");
	}
	public static BigDecimal montoDivideUnDecimal(BigDecimal monto, BigDecimal totalElemento){
		if (totalElemento != null && totalElemento.doubleValue() > 0)
			return monto.divide(totalElemento,1,BigDecimal.ROUND_HALF_UP);
		else
			return new BigDecimal("0");
	}
	public static BigDecimal montoMultiplica(BigDecimal monto, BigDecimal tipoCambio){
		return monto.multiply(tipoCambio).setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	
	public static void mCalcularMontoCobro(CobroPrimerCobro cobroPrimerCobro) throws Exception{
		BigDecimal monto = null;
		try{
			monto = validarNullBigDecimal(cobroPrimerCobro.getMonto());
			if (cobroPrimerCobro.getTipoMonedaPrimerCobro().equals(Constantes.MONEDA_SOL)){
				cobroPrimerCobro.setMontoSolesPrimerCobro(montoRedondeado(monto));
				cobroPrimerCobro.setMontoDolaresPrimerCobro(montoDivide(monto,cobroPrimerCobro.getTipoCambioPrimerCobro()));
			}else{
				cobroPrimerCobro.setMontoDolaresPrimerCobro(montoRedondeado(monto));
				cobroPrimerCobro.setMontoSolesPrimerCobro(montoMultiplica(monto,cobroPrimerCobro.getTipoCambioPrimerCobro()));
			}
		}catch(Exception e){
			e.printStackTrace();
			cobroPrimerCobro.setMontoSolesPrimerCobro(new BigDecimal("0"));
			cobroPrimerCobro.setMontoDolaresPrimerCobro(new BigDecimal("0"));
			throw e;
		}finally{
			monto = null;
		}
	}
	public static void mValidarTipoCambio(CobroBean cobro) throws Exception{
		BigDecimal monto = null;
		try{
			monto = validarNullBigDecimal(cobro.getTipoCambio());
			cobro.setTipoCambio(monto);
			
		}catch(Exception e){
			e.printStackTrace();
			cobro.setMontoSoles(new BigDecimal("0"));
			cobro.setMontoDolares(new BigDecimal("0"));
			cobro.setTipoCambio(new BigDecimal("0"));
			throw e;
		}finally{
			monto = null;
		}
	}
	public static void mValidarTipoCambio(CobroArbitrioBean cobro) throws Exception{
		BigDecimal monto = null;
		try{
			monto = validarNullBigDecimal(cobro.getTipoCambioArbitrio());
			cobro.setTipoCambioArbitrio(monto);
			
		}catch(Exception e){
			e.printStackTrace();
			cobro.setMontoSolesArbitrio(new BigDecimal("0"));
			cobro.setMontoDolaresArbitrio(new BigDecimal("0"));
			cobro.setTipoCambioArbitrio(new BigDecimal("0"));
			throw e;
		}finally{
			monto = null;
		}
	}
	public static void mCalcularMontoCobro(CobroBean cobro) throws Exception{
		BigDecimal monto = null;
		try{
			monto = validarNullBigDecimal(cobro.getMonto());
			if (cobro.getTipoMoneda().equals(Constantes.MONEDA_SOL)){
				cobro.setMontoSoles(montoRedondeado(monto));
				cobro.setMontoDolares(montoDivide(monto,cobro.getTipoCambio()));
			}else if (cobro.getTipoMoneda().equals(Constantes.MONEDA_DOLAR)){
				cobro.setMontoDolares(montoRedondeado(monto));
				cobro.setMontoSoles(montoMultiplica(monto,cobro.getTipoCambio()));
			}else{
				cobro.setMontoSoles(new BigDecimal("0"));
				cobro.setMontoDolares(new BigDecimal("0"));
			}
		}catch(Exception e){
			e.printStackTrace();
			cobro.setMontoSoles(new BigDecimal("0"));
			cobro.setMontoDolares(new BigDecimal("0"));
			throw e;
		}finally{
			monto = null;
		}
	}
	public static void mValidarTipoCambio(CobroServicioBean cobro) throws Exception{
		BigDecimal monto = null;
		try{
			monto = validarNullBigDecimal(cobro.getTipoCambioServicio());
			cobro.setTipoCambioServicio(monto);
			
		}catch(Exception e){
			e.printStackTrace();
			cobro.setMontoSolesServicio(new BigDecimal("0"));
			cobro.setMontoDolaresServicio(new BigDecimal("0"));
			cobro.setTipoCambioServicio(new BigDecimal("0"));
			throw e;
		}finally{
			monto = null;
		}
	}
	public static void mCalcularMontoCobro(CobroServicioBean cobroServicio) throws Exception{
		BigDecimal monto = null;
		try{
			monto = validarNullBigDecimal(cobroServicio.getMonto());
			if (cobroServicio.getTipoMonedaServicio().equals(Constantes.MONEDA_SOL)){
				cobroServicio.setMontoSolesServicio(montoRedondeado(monto));
				cobroServicio.setMontoDolaresServicio(montoDivide(monto,cobroServicio.getTipoCambioServicio()));
			}else if (cobroServicio.getTipoMonedaServicio().equals(Constantes.MONEDA_DOLAR)){
				cobroServicio.setMontoDolaresServicio(montoRedondeado(monto));
				cobroServicio.setMontoSolesServicio(montoMultiplica(monto,cobroServicio.getTipoCambioServicio()));
			}else{
				cobroServicio.setMontoSolesServicio(new BigDecimal("0"));
				cobroServicio.setMontoDolaresServicio(new BigDecimal("0"));
			}
		}catch(Exception e){
			e.printStackTrace();
			cobroServicio.setMontoSolesServicio(new BigDecimal("0"));
			cobroServicio.setMontoDolaresServicio(new BigDecimal("0"));
			throw e;
		}finally{
			monto = null;
		}
	}
	public static void mValidarTipoCambio(CobroLuzBean cobro) throws Exception{
		BigDecimal monto = null;
		try{
			monto = validarNullBigDecimal(cobro.getTipoCambioLuz());
			cobro.setTipoCambioLuz(monto);
			
		}catch(Exception e){
			e.printStackTrace();
			cobro.setMontoSolesLuz(new BigDecimal("0"));
			cobro.setMontoDolaresLuz(new BigDecimal("0"));
			cobro.setTipoCambioLuz(new BigDecimal("0"));
			throw e;
		}finally{
			monto = null;
		}
	}
	public static void mCalcularMontoCobro(CobroLuzBean cobroLuz) throws Exception{
		BigDecimal monto = null;
		try{
			monto = validarNullBigDecimal(cobroLuz.getMonto());
			if (cobroLuz.getTipoMonedaLuz().equals(Constantes.MONEDA_SOL)){
				cobroLuz.setMontoSolesLuz(montoRedondeado(monto));
				cobroLuz.setMontoDolaresLuz(montoDivide(monto,cobroLuz.getTipoCambioLuz()));
			}else{
				cobroLuz.setMontoDolaresLuz(montoRedondeado(monto));
				cobroLuz.setMontoSolesLuz(montoMultiplica(monto,cobroLuz.getTipoCambioLuz()));
			}
		}catch(Exception e){
			e.printStackTrace();
			cobroLuz.setMontoSolesLuz(new BigDecimal("0"));
			cobroLuz.setMontoDolaresLuz(new BigDecimal("0"));
			throw e;
		}finally{
			monto = null;
		}
	}
	public static void mCalcularMontoCobro(CobroArbitrioBean cobroArbitrio) throws Exception{
		BigDecimal monto = null;
		try{
			monto = validarNullBigDecimal(cobroArbitrio.getMonto());
			
			if (cobroArbitrio.getTipoMonedaArbitrio().equals(Constantes.MONEDA_SOL)){
				cobroArbitrio.setMontoSolesArbitrio(montoRedondeado(monto));
				cobroArbitrio.setMontoDolaresArbitrio(montoDivide(monto,cobroArbitrio.getTipoCambioArbitrio()));
			}else if (cobroArbitrio.getTipoMonedaArbitrio().equals(Constantes.MONEDA_DOLAR)){
				cobroArbitrio.setMontoDolaresArbitrio(montoRedondeado(monto));
				cobroArbitrio.setMontoSolesArbitrio(montoMultiplica(monto,cobroArbitrio.getTipoCambioArbitrio()));
			}else{
				cobroArbitrio.setMontoSolesArbitrio(new BigDecimal("0"));
				cobroArbitrio.setMontoDolaresArbitrio(new BigDecimal("0"));
			}
		}catch(Exception e){
			e.printStackTrace();
			cobroArbitrio.setMontoSolesArbitrio(new BigDecimal("0"));
			cobroArbitrio.setMontoDolaresArbitrio(new BigDecimal("0"));
			throw e;
		}finally{
			monto = null;
		}
	}
	
	public  static BigDecimal validarNullBigDecimal(BigDecimal monto){
		if (monto == null){
			monto = new BigDecimal("0");
		}
		if (monto.compareTo(new BigDecimal("0.001"))<=0){
			monto = new BigDecimal("0");
		}
		return monto;
	}
	
	public  static boolean mValidarMonto(BigDecimal monto){
		if (monto == null){
			return false;
		}else{
			return true;
		}
	}
	/*
	 * Asignar la clase Cobro a otra clase
	 */
	public static TblCobro mAsignarDatoCobro(CobroGarantia cobroGarantia) throws Exception{
		TblCobro cobroAlquilerAux = new TblCobro();
		try{
			cobroAlquilerAux.setFechaCobro(cobroGarantia.getFechaCobroGarantia());
			cobroAlquilerAux.setMontoSoles(cobroGarantia.getMontoSolesGarantia());
			cobroAlquilerAux.setMontoDolares(cobroGarantia.getMontoDolaresGarantia());
			cobroAlquilerAux.setTipoCambio(cobroGarantia.getTipoCambioGarantia());
			cobroAlquilerAux.setTipoMoneda(cobroGarantia.getTipoMonedaGarantia());
			//reset a los montos
			cobroGarantia.setMontoSolesGarantia(new BigDecimal("0"));
			cobroGarantia.setMontoDolaresGarantia(new BigDecimal("0"));
		}catch(Exception e){
			e.printStackTrace();
			cobroAlquilerAux = null;
			throw e;
		}
		return cobroAlquilerAux;
	}
	public static TblCobro mAsignarDatoCobro(CobroPrimerCobro cobroPrimerCobro) throws Exception{
		TblCobro cobroAlquilerAux = new TblCobro();
		try{
			cobroAlquilerAux.setFechaCobro(cobroPrimerCobro.getFechaCobroPrimerCobro());
			cobroAlquilerAux.setMontoSoles(cobroPrimerCobro.getMontoSolesPrimerCobro());
			cobroAlquilerAux.setMontoDolares(cobroPrimerCobro.getMontoDolaresPrimerCobro());
			cobroAlquilerAux.setTipoCambio(cobroPrimerCobro.getTipoCambioPrimerCobro());
			cobroAlquilerAux.setTipoMoneda(cobroPrimerCobro.getTipoMonedaPrimerCobro());
			//Se asigna el PK del primer cobro 
			cobroAlquilerAux.setCodigoCobro(cobroPrimerCobro.getIntCodigoPrimerCobro());
			//reset a los montos
			cobroPrimerCobro.setMontoSolesPrimerCobro(new BigDecimal("0"));
			cobroPrimerCobro.setMontoDolaresPrimerCobro(new BigDecimal("0"));
		}catch(Exception e){
			e.printStackTrace();
			cobroAlquilerAux = null;
			throw e;
		}
		return cobroAlquilerAux;
	}
	public static TblCobro mAsignarDatoCobro(CobroBean cobro) throws Exception{
		TblCobro cobroAlquilerAux = new TblCobro();
		try{
			cobroAlquilerAux.setFechaCobro(cobro.getFechaCobro());
			cobroAlquilerAux.setMontoSoles(cobro.getMontoSoles());
			cobroAlquilerAux.setMontoDolares(cobro.getMontoDolares());
			cobroAlquilerAux.setTipoCambio(cobro.getTipoCambio());
			cobroAlquilerAux.setTipoMoneda(cobro.getTipoMoneda());
			//reset a los montos
			cobro.setMontoSoles(new BigDecimal("0"));
			cobro.setMontoDolares(new BigDecimal("0"));
		}catch(Exception e){
			e.printStackTrace();
			cobroAlquilerAux = null;
			throw e;
		}
		return cobroAlquilerAux;
	}
	public static TblCobro mAsignarDatoCobro(CobroServicioBean cobroServicio) throws Exception{
		TblCobro cobroServiciorAux			= new TblCobro();
		try{
			cobroServiciorAux.setFechaCobro(cobroServicio.getFechaCobroServicio());
			cobroServiciorAux.setMontoSoles(cobroServicio.getMontoSolesServicio());
			cobroServiciorAux.setMontoDolares(cobroServicio.getMontoDolaresServicio());
			cobroServiciorAux.setTipoCambio(cobroServicio.getTipoCambioServicio());
			cobroServiciorAux.setTipoMoneda(cobroServicio.getTipoMonedaServicio());
			//reset a los montos
			cobroServicio.setMontoSolesServicio(new BigDecimal("0"));
			cobroServicio.setMontoDolaresServicio(new BigDecimal("0"));
		}catch(Exception e){
			e.printStackTrace();
			cobroServiciorAux = null;
			throw e;
		}
		return cobroServiciorAux;
	}
	public static TblCobro mAsignarDatoCobro(CobroLuzBean cobroLuz) throws Exception{
		TblCobro cobroServiciorAux			= new TblCobro();
		try{
			cobroServiciorAux.setFechaCobro(cobroLuz.getFechaCobroLuz());
			cobroServiciorAux.setMontoSoles(cobroLuz.getMontoSolesLuz());
			cobroServiciorAux.setMontoDolares(cobroLuz.getMontoDolaresLuz());
			cobroServiciorAux.setTipoCambio(cobroLuz.getTipoCambioLuz());
			cobroServiciorAux.setTipoMoneda(cobroLuz.getTipoMonedaLuz());
			//reset a los montos
			cobroLuz.setMontoSolesLuz(new BigDecimal("0"));
			cobroLuz.setMontoDolaresLuz(new BigDecimal("0"));
		}catch(Exception e){
			e.printStackTrace();
			cobroServiciorAux = null;
			throw e;
		}
		return cobroServiciorAux;
	}
	public static TblCobro mAsignarDatoCobro(CobroArbitrioBean cobroArbitrio) throws Exception{
		TblCobro cobroAlquilerAux = new TblCobro();
		try{
			cobroAlquilerAux.setFechaCobro(cobroArbitrio.getFechaCobroArbitrio());
			cobroAlquilerAux.setMontoSoles(cobroArbitrio.getMontoSolesArbitrio());
			cobroAlquilerAux.setMontoDolares(cobroArbitrio.getMontoDolaresArbitrio());
			cobroAlquilerAux.setTipoCambio(cobroArbitrio.getTipoCambioArbitrio());
			//reset a los montos
			cobroArbitrio.setMontoSolesArbitrio(new BigDecimal("0"));
			cobroArbitrio.setMontoDolaresArbitrio(new BigDecimal("0"));
		}catch(Exception e){
			e.printStackTrace();
			cobroAlquilerAux = null;
			throw e;
		}
		return cobroAlquilerAux;
	}
	/*
	 * Obtiene el codigo del Usuario
	 */
	public static Integer mGetUsuario(HttpServletRequest request){
		Integer idUsuario		= null;
		if (((TblUsuario)request.getSession().getAttribute("UsuarioSession")) != null){
			idUsuario  = ((TblUsuario)request.getSession().getAttribute("UsuarioSession")).getCodigoUsuario();
		}else{
			idUsuario = 1;
		}
		return idUsuario;
	}
	/*
	 * Obtener una lista con el año;mes de un rango de fecha
	 */
	public static List<PeriodoBean> mGetListAnioMes(Date fecInicio, Date fecFin){
		List<PeriodoBean> lista = new ArrayList<>();
		PeriodoBean periodo = null;
		boolean fin = false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecInicio);
		
		int anioInicio = cal.get(Calendar.YEAR);
		int mesInicio = cal.get(Calendar.MONTH);
		cal.setTime(fecFin);
		
		int anioFin = cal.get(Calendar.YEAR);
		int mesFin = cal.get(Calendar.MONTH);
		
		int mes = 0;
		int anio = 0;
		anio = anioInicio;
		mes = mesInicio;
		while (!fin){
			mes = mes + 1;
			
			if (mes == 12){
				mes = 0;
				anio = anio +1;
			}
			if (anio > anioFin || (anio == anioFin && mes>mesFin)){
				fin = true;
			}else{
				periodo = new PeriodoBean(anio, mes+1);
				lista.add(periodo);
			}
			
		}
		
		
		return lista;
	}
	public static List<PeriodoBean> mGetListAnioMesRefinanciacion(Date fecInicio, Date fecFin){
		List<PeriodoBean> lista = new ArrayList<>();
		PeriodoBean periodo = null;
		boolean fin = false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecInicio);
		
		int anioInicio = cal.get(Calendar.YEAR);
		int mesInicio = cal.get(Calendar.MONTH);
		cal.setTime(fecFin);
		
		int anioFin = cal.get(Calendar.YEAR);
		int mesFin = cal.get(Calendar.MONTH);
		
		int mes = 0;
		int anio = 0;
		anio = anioInicio;
		mes = mesInicio;
		while (!fin){
			
			
			if (mes == 12){
				mes = 0;
				anio = anio +1;
			}
			if (anio > anioFin || (anio == anioFin && mes>mesFin)){
				fin = true;
			}else{
				periodo = new PeriodoBean(anio, mes+1);
				lista.add(periodo);
			}
			mes = mes + 1;
		}
		
		
		return lista;
	}
	
	public static String getAnioMesString(Date fecha){
		SimpleDateFormat dt1 = null;
		try{
			dt1 = new SimpleDateFormat("yyyyMM");

		}catch(Exception e){
			e.printStackTrace();
		}

		return  dt1.format(fecha).toString();
	}

	//*************************************************************************************
	// FACTURADOR 1.2
	//*************************************************************************************

	/* Obtiene el codigo internacional : posicion 1, nombre: posicion 2*/
	public static String getDatoTributo(String cadena, Integer posicion){
		String dato = "";
		if (cadena!=null && cadena.length()>0){
			String[] lista = cadena.split("|");
			if (lista != null && lista.length>0){
				dato = lista[posicion-1];
			}
		}
		return dato;		
	}
	/*Obtiene el codigo de la afectacion*/
	public static String getDatoAfectacion(String cadena){
		String dato = "";
		if (cadena!=null && cadena.length()>0){
			dato = cadena.substring(0, 4);
		}
		return dato;		
	}
	/*Obtener la Hora del sistema HH:MM:SS*/
	public static String getHora(){
		String hora = "";
		String minuto;
		String segundo;
		Calendar rightNow = Calendar.getInstance();
		hora = rightNow.get(Calendar.HOUR_OF_DAY) + "";
		if (hora.length()<2){
			hora = "0"+hora;
		}
		minuto = rightNow.get(Calendar.MINUTE) + "";
		if (minuto.length()<2){
			minuto = "0"+minuto;
		}
		segundo = rightNow.get(Calendar.SECOND) + "";
		if (segundo.length()<2){
			segundo = "0"+segundo;
		}
		hora = hora +":"+ minuto+":"+segundo;
		return hora;
	}

	/*Retorna el tipo de afectacion*/
	public static String getTipoAfectacion(String codigoAfectacion){
		String resultado = "";

		switch (codigoAfectacion){
		case "10":
			resultado = Constantes.SUNAT_TIPO_AFECTACION_GRAVADO;
			break;
		case "11":
			resultado = Constantes.SUNAT_TIPO_AFECTACION_GRAVADO;
			break;
		case "12":
			resultado = Constantes.SUNAT_TIPO_AFECTACION_GRAVADO;
			break;
		case "13":
			resultado = Constantes.SUNAT_TIPO_AFECTACION_GRAVADO;
			break;
		case "14":
			resultado = Constantes.SUNAT_TIPO_AFECTACION_GRAVADO;
			break;
		case "15":
			resultado = Constantes.SUNAT_TIPO_AFECTACION_GRAVADO;
			break;
		case "16":
			resultado = Constantes.SUNAT_TIPO_AFECTACION_GRAVADO;
			break;
		case "17":
			resultado = Constantes.SUNAT_TIPO_AFECTACION_GRAVADO;
			break;
		case "20":
			resultado = Constantes.SUNAT_TIPO_AFECTACION_EXONERADO;
			break;
		case "21":
			resultado = Constantes.SUNAT_TIPO_AFECTACION_EXONERADO;
			break;
		case "30":
			resultado = Constantes.SUNAT_TIPO_AFECTACION_INAFECTO;
			break;
		case "31":
			resultado = Constantes.SUNAT_TIPO_AFECTACION_INAFECTO;
			break;
		case "32":
			resultado = Constantes.SUNAT_TIPO_AFECTACION_INAFECTO;
			break;
		case "33":
			resultado = Constantes.SUNAT_TIPO_AFECTACION_INAFECTO;
			break;
		case "34":
			resultado = Constantes.SUNAT_TIPO_AFECTACION_INAFECTO;
			break;
		case "35":
			resultado = Constantes.SUNAT_TIPO_AFECTACION_INAFECTO;
			break;
		case "36":
			resultado = Constantes.SUNAT_TIPO_AFECTACION_INAFECTO;
			break;
		case "37":
			resultado = Constantes.SUNAT_TIPO_AFECTACION_INAFECTO;
			break;
		case "40":
			resultado = Constantes.SUNAT_TIPO_AFECTACION_EXPORTACION;
			break;

		}

		return resultado;
	}
	public static String getTributoGravado(String tipo){
		String resultado = null;
		if (tipo.equals(Constantes.SUNAT_TRIBUTO_CODIGO))
			resultado = "1000";
		if (tipo.equals(Constantes.SUNAT_TRIBUTO_NOMBRE))
			resultado = "IGV";
		if (tipo.equals(Constantes.SUNAT_TRIBUTO_TIPO))
			resultado = "VAT";
		return resultado;
	}
	public static String getTributoExonerado(String tipo){
		String resultado = null;
		if (tipo.equals(Constantes.SUNAT_TRIBUTO_CODIGO))
			resultado = "9997";
		if (tipo.equals(Constantes.SUNAT_TRIBUTO_NOMBRE))
			resultado = "EXO";
		if (tipo.equals(Constantes.SUNAT_TRIBUTO_TIPO))
			resultado = "VAT";
		return resultado;
	}
	public static String getTributoGratuito(String tipo){
		String resultado = null;
		if (tipo.equals(Constantes.SUNAT_TRIBUTO_CODIGO))
			resultado = "9996";
		if (tipo.equals(Constantes.SUNAT_TRIBUTO_NOMBRE))
			resultado = "GRA";
		if (tipo.equals(Constantes.SUNAT_TRIBUTO_TIPO))
			resultado = "FRE";
		return resultado;
	}
	public static String getTributoInafecto(String tipo){
		String resultado = null;
		if (tipo.equals(Constantes.SUNAT_TRIBUTO_CODIGO))
			resultado = "9998";
		if (tipo.equals(Constantes.SUNAT_TRIBUTO_NOMBRE))
			resultado = "INA";
		if (tipo.equals(Constantes.SUNAT_TRIBUTO_TIPO))
			resultado = "FRE";
		return resultado;
	}
	public static String getTributoExportacion(String tipo){
		String resultado = null;
		if (tipo.equals(Constantes.SUNAT_TRIBUTO_CODIGO))
			resultado = "9995";
		if (tipo.equals(Constantes.SUNAT_TRIBUTO_NOMBRE))
			resultado = "EXP";
		if (tipo.equals(Constantes.SUNAT_TRIBUTO_CODIGO))
			resultado = "FRE";
		return resultado;
	}
	
	/*Retorna el tipo de afectacion*/
	public static String getTributo(String codigoAfectacion, String tipo){
		String resultado = "";

		switch (codigoAfectacion){
		case "10":
			resultado = getTributoGravado(tipo);
			break;
		case "11":
			resultado = getTributoGravado(tipo);
			break;
		case "12":
			resultado = getTributoGravado(tipo);
			break;
		case "13":
			resultado = getTributoGravado(tipo);
			break;
		case "14":
			resultado = getTributoGravado(tipo);
			break;
		case "15":
			resultado = getTributoGravado(tipo);
			break;
		case "16":
			resultado = getTributoGravado(tipo);
			break;
		case "17":
			resultado = getTributoGravado(tipo);
			break;
		case "20":
			resultado = getTributoExonerado(tipo);
			break;
		case "21":
			resultado = getTributoGratuito(tipo);
			break;
		case "30":
			resultado = getTributoInafecto(tipo);
			break;
		case "31":
			resultado = getTributoInafecto(tipo);
			break;
		case "32":
			resultado = getTributoInafecto(tipo);
			break;
		case "33":
			resultado = getTributoInafecto(tipo);
			break;
		case "34":
			resultado = getTributoInafecto(tipo);
			break;
		case "35":
			resultado = getTributoInafecto(tipo);
			break;
		case "36":
			resultado = getTributoInafecto(tipo);
			break;
		case "37":
			resultado = getTributoGratuito(tipo);
			break;
		case "40":
			resultado = getTributoExportacion(tipo);
			break;

		}

		return resultado;
	}
	/*Retorna el tipo de afectacion*/
	public static BigDecimal getMontoTributoBaseImponible(String codigoAfectacion, TblDetalleComprobante detalle){
		BigDecimal resultado = null;

		switch (codigoAfectacion){
		case "10":
			resultado = detalle.getPrecioTotal().subtract(detalle.getTribMontoIgv());
			break;
		case "11":
			resultado = detalle.getPrecioTotal().subtract(detalle.getTribMontoIgv());
			break;
		case "12":
			resultado = detalle.getPrecioTotal().subtract(detalle.getTribMontoIgv());
			break;
		case "13":
			resultado = detalle.getPrecioTotal().subtract(detalle.getTribMontoIgv());
			break;
		case "14":
			resultado = detalle.getPrecioTotal().subtract(detalle.getTribMontoIgv());
			break;
		case "15":
			resultado = detalle.getPrecioTotal().subtract(detalle.getTribMontoIgv());
			break;
		case "16":
			resultado = detalle.getPrecioTotal().subtract(detalle.getTribMontoIgv());
			break;
		case "17":
			resultado = detalle.getPrecioTotal().subtract(detalle.getTribMontoIgv());
			break;
		case "20":
			resultado = detalle.getPrecioTotal().subtract(detalle.getTribMontoIgv());
			break;
		case "21":
			resultado = new BigDecimal("0");
			break;
		case "30":
			resultado = detalle.getPrecioTotal().subtract(detalle.getTribMontoIgv());
			break;
		case "31":
			resultado = detalle.getPrecioTotal().subtract(detalle.getTribMontoIgv());
			break;
		case "32":
			resultado = detalle.getPrecioTotal().subtract(detalle.getTribMontoIgv());
			break;
		case "33":
			resultado = detalle.getPrecioTotal().subtract(detalle.getTribMontoIgv());
			break;
		case "34":
			resultado = detalle.getPrecioTotal().subtract(detalle.getTribMontoIgv());
			break;
		case "35":
			resultado = detalle.getPrecioTotal().subtract(detalle.getTribMontoIgv());
			break;
		case "36":
			resultado = detalle.getPrecioTotal().subtract(detalle.getTribMontoIgv());
			break;
		case "37":
			resultado = new BigDecimal("0");
			break;
		case "40":
			resultado = detalle.getPrecioTotal().subtract(detalle.getTribMontoIgv());
			break;

		}

		return resultado;
	}
	/*Retorna el tipo de afectacion*/
	public static void getMontoAfectacion(TblDetalleComprobante detalle, TblTributoGeneral tributoIgv, TblTributoGeneral tributoExo, TblTributoGeneral tributoIna, TblTributoGeneral tributoGra){
		
		switch (detalle.getTipoAfectacion()){
		case "10":
			tributoIgv.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIgv.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "11":
			tributoIgv.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIgv.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "12":
			tributoIgv.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIgv.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "13":
			tributoIgv.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIgv.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "14":
			tributoIgv.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIgv.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "15":
			tributoIgv.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIgv.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "16":
			tributoIgv.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIgv.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "17":
			tributoIgv.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIgv.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "20":
			tributoExo.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoExo.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "21":
			tributoGra.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getValorReferencia().multiply(detalle.getCantidad())));
			tributoGra.setMontoTributoItem(new BigDecimal("0"));
			break;
		case "30":
			tributoIna.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIna.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "31":
			tributoIna.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIna.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "32":
			tributoIna.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIna.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "33":
			tributoIna.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIna.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "34":
			tributoIna.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIna.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "35":
			tributoIna.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIna.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "36":
			tributoIna.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIna.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "37":
			tributoGra.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getValorReferencia().multiply(detalle.getCantidad())));
			tributoGra.setMontoTributoItem(new BigDecimal("0"));
			break;
		case "40":
			
			break;

		}
	}
	
	/*Retorna el tipo de afectacion*/
	public static void getMontoAfectacionNota(TblDetalleComprobante detalle, TblTributoGeneralNota tributoIgv, TblTributoGeneralNota tributoExo, TblTributoGeneralNota tributoIna, TblTributoGeneralNota tributoGra){
		
		switch (detalle.getTipoAfectacion()){
		case "10":
			tributoIgv.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIgv.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "11":
			tributoIgv.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIgv.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "12":
			tributoIgv.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIgv.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "13":
			tributoIgv.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIgv.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "14":
			tributoIgv.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIgv.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "15":
			tributoIgv.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIgv.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "16":
			tributoIgv.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIgv.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "17":
			tributoIgv.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIgv.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "20":
			tributoExo.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoExo.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "21":
			/*tributoGra.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoGra.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));*/
			tributoGra.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getValorReferencia().multiply(detalle.getCantidad())));
			tributoGra.setMontoTributoItem(new BigDecimal("0"));
			break;
		case "30":
			tributoIna.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIna.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "31":
			tributoIna.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIna.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "32":
			tributoIna.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIna.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "33":
			tributoIna.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIna.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "34":
			tributoIna.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIna.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "35":
			tributoIna.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIna.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "36":
			tributoIna.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoIna.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));
			break;
		case "37":
			/*tributoGra.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getTribBaseImponibleIgv()));
			tributoGra.setMontoTributoItem(tributoIgv.getMontoTributoItem().add(detalle.getTribMontoIgv()));*/
			tributoGra.setBaseImponible(tributoIgv.getBaseImponible().add(detalle.getValorReferencia().multiply(detalle.getCantidad())));
			tributoGra.setMontoTributoItem(new BigDecimal("0"));
			break;
		case "40":
			
			break;

		}
	}
	
	public static BigDecimal getRoundDecimal(BigDecimal numero,  int decimal){
		if (numero == null){
			numero = new BigDecimal("0");
		}
		return numero.setScale(decimal, BigDecimal.ROUND_UP);
	}
	public static String getRoundDecimalString(BigDecimal numero,  int decimal){
		if (numero == null){
			numero = new BigDecimal("0");
		}
		return numero.setScale(decimal, BigDecimal.ROUND_UP).toString();
	}
	public static BigDecimal getCalculoDescuento(BigDecimal total, BigDecimal descuento, int decimal){
		BigDecimal calculo = null;
		if (total == null){
			total = new BigDecimal("0");
		}
		if (descuento == null){
			descuento = new BigDecimal("0");
		}
		calculo = total.multiply(descuento);
		calculo = calculo.divide(new BigDecimal("100"), 4, RoundingMode.HALF_EVEN);
		calculo = total.subtract(calculo);
		return calculo.setScale(decimal, BigDecimal.ROUND_UP);
	}
	public static List<String> getNombreArchivos(String nombreArchivo){
		ArrayList<String> lista = new ArrayList<>();
		String prefijo = null;
		if (nombreArchivo !=null && nombreArchivo.length()>0){
			prefijo = nombreArchivo.substring(0, nombreArchivo.indexOf("."));
			lista.add(nombreArchivo);
			lista.add(prefijo+".DET");
			lista.add(prefijo+".TRI");
			lista.add(prefijo+".LEY");
		}
		return lista;
	}

	public static void eliminarArchivo(List<String> lista, String ruta){
		File file = null;
		try{
			for(String nombre:lista){
				file = new File(ruta +nombre);
				if (file.delete()){
					System.out.println("Archivo Eliminado:"+ruta+nombre);
				}else{
	    			System.out.println("Delete operation is failed.");
	    		}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void obtenerFechaFinContrato(TblContrato contrato, List<TblParametro> listaParametro){
		if (contrato.getFechaInicio() == null){
			contrato.setFechaFin(UtilSGT.addDays(new Date(), 30));
			contrato.setFechaFin(UtilSGT.getLastDays(contrato.getFechaFin()));
		}else{
			//listaParametro = parametroDao.buscarOneByNombre(Constantes.PAR_FIN_CONTRATO);
			if(listaParametro!=null && listaParametro.size()>0){
				contrato.setFechaFin(UtilSGT.addDays(contrato.getFechaInicio(),new Integer(listaParametro.get(0).getDato())));
				contrato.setFechaFin(UtilSGT.getLastDays(contrato.getFechaFin()));
			}else{
				contrato.setFechaFin(UtilSGT.addDays(contrato.getFechaInicio(), 30));
				contrato.setFechaFin(UtilSGT.getLastDays(contrato.getFechaFin()));
			}
		}
		
	}
	
	/*Retorna el nombre del usuario*/
	public static String nombreUsuario (Integer codigoUsuario, Map<Integer, String> mapUsuario ){
		return mapUsuario.get(codigoUsuario)==null?"USUARIO ELIMINADO":mapUsuario.get(codigoUsuario);
	}
	
	//Obtiene el nombre del bancarizado
	public static String getDescripcionTipoBancarizacion(String tipo){
		if (tipo == null || tipo.equals("")){
			return "";
		}
		if (tipo.equals(Constantes.TIPO_BANCARIZADO_COD_TRANSFERENCIA)){
			return Constantes.TIPO_BANCARIZADO_DES_TRANSFERENCIA;
		}
		if (tipo.equals(Constantes.TIPO_BANCARIZADO_COD_DEPOSITO)){
			return Constantes.TIPO_BANCARIZADO_DES_DEPOSITO;
		}
		if (tipo.equals(Constantes.TIPO_BANCARIZADO_COD_CHEQUE)){
			return Constantes.TIPO_BANCARIZADO_DES_CHEQUE;
		}
		if (tipo.equals(Constantes.TIPO_BANCARIZADO_COD_OTROS)){
			return Constantes.TIPO_BANCARIZADO_DES_OTROS;
		}
		if (tipo.equals(Constantes.TIPO_BANCARIZADO_COD_POS)){
			return Constantes.TIPO_BANCARIZADO_DES_POS;
		}
		return "";
	}
	
	public static String getDescripcionTipoCobro(String tipo) {
		if (tipo.equals(Constantes.TIPO_COBRO_ALQUILER)){
			return Constantes.DESC_TIPO_COBRO_ALQUILER;
		}
		if (tipo.equals(Constantes.TIPO_COBRO_SERVICIO)){
			return Constantes.DESC_TIPO_COBRO_SERVICIO;
		}
		if (tipo.equals(Constantes.TIPO_COBRO_LUZ)){
			return Constantes.DESC_TIPO_COBRO_LUZ;
		}
		if (tipo.equals(Constantes.TIPO_COBRO_ARBITRIO)){
			return Constantes.DESC_TIPO_COBRO_ARBITRIO;
		}
		if (tipo.equals(Constantes.TIPO_COBRO_GARANTIA)){
			return Constantes.DESC_TIPO_COBRO_GARANTIA;
		}
		return tipo;
	}
	public static String getDescripcionTipoPago(String tipo) {
		if (tipo == null || tipo.isEmpty()) {
			return  Constantes.TIPO_PAGO_DES_EFECTIVO;
		}
		if (tipo.equals(Constantes.TIPO_PAGO_COD_EFECTIVO)){
			return Constantes.TIPO_PAGO_DES_EFECTIVO;
		}
		if (tipo.equals(Constantes.TIPO_PAGO_COD_BANCARIZADO)){
			return Constantes.TIPO_PAGO_DES_BANCARIZADO;
		}
		
		return tipo;
	}
	
	public static String getFormaPagoSunat(String tipo) {
		String resultado = null;
		if (tipo == null || tipo.isEmpty()) {
			resultado =  "";
		}
		if (tipo.equals(Constantes.FORMA_PAGO_CREDITO)){
			resultado =  "Credito";
		}
		if (tipo.equals(Constantes.FORMA_PAGO_CONTADO)){
			resultado =  "Contado";
		}
		return resultado;
	}
	
	public static String getNombreFacturaCVS(FacturaBean entidad) {
		return getNombreArchivoOSE(entidad)+".csv";
	}
	public static String getNombreFacturaCDR(FacturaBean entidad) {
		return getNombreArchivoOSE(entidad)+"-CDR.xml";
	}
	public static String getNombreFacturaPDF(FacturaBean entidad) {
		return getNombreArchivoOSE(entidad)+"-PDF.pdf";
	}
	public static String getNombreFacturaXML(FacturaBean entidad) {
		return getNombreArchivoOSE(entidad)+"-XML.xml";
	}
	
	private static String getNombreArchivoOSE(FacturaBean entidad) {
		return Constantes.SUNAT_RUC_EMISOR+"-01-"+entidad.getFactura().getSerie()+"-"+entidad.getFactura().getNumero();
	}
	private static String getNombreArchivoNotaOSE(NotaBean entidad) {
		return Constantes.SUNAT_RUC_EMISOR+"-07-"+entidad.getNota().getSerie()+"-"+entidad.getNota().getNumero();
	}
	public static String getNombreNotaCVS(NotaBean entidad) {
		return getNombreArchivoNotaOSE(entidad)+".csv";
	}
	public static String getNombreNotaCDR(NotaBean entidad) {
		return getNombreArchivoNotaOSE(entidad)+"-CDR.xml";
	}
	public static String getNombreNotaPDF(NotaBean entidad) {
		return getNombreArchivoNotaOSE(entidad)+"-PDF.pdf";
	}
	public static String getNombreNotaXML(NotaBean entidad) {
		return getNombreArchivoNotaOSE(entidad)+"-XML.xml";
	}
}
