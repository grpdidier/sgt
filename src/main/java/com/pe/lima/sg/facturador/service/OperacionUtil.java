package com.pe.lima.sg.facturador.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.pe.lima.sg.entity.mantenimiento.TblParametro;
import com.pe.lima.sg.facturador.bean.AuditoriaBean;
import com.pe.lima.sg.facturador.bean.ComprobanteBean;
import com.pe.lima.sg.facturador.entity.TblComprobante;
import com.pe.lima.sg.facturador.entity.TblDetalleComprobante;
import com.pe.lima.sg.facturador.entity.TblTributoGeneral;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.UtilSGT;


public class OperacionUtil {

	/*
	public static void asignarParametros(Filtro entidad, Map<String, TblParametro> mapParametro, HttpServletRequest request){
		TblParametro parametro =null;
		try{
			entidad.setComprobante(new TblComprobante());
			entidad.setDetalleComprobante(new TblDetalleComprobante());
			if (mapParametro!=null){
				//IGV
				parametro = mapParametro.get(Constantes.PARAMETRO_IGV);
				if (parametro!=null){
					entidad.setValorIGV(parametro.getCantidad());
					entidad.setNombreIGV(parametro.getDato());
				}else{
					entidad.setValorIGV(Constantes.SUNAT_IGV);
				}
				//SERVICIO
				parametro = mapParametro.get(Constantes.PARAMETRO_SERVICIO);
				if (parametro!=null){
					entidad.setValorServicio(parametro.getCantidad());
					entidad.setNombreServicio(parametro.getDato());
				}else{
					entidad.setValorServicio(Constantes.SUNAT_SERVICIO);
				}
				//Tipo de Operacion
				parametro = mapParametro.get(Constantes.PARAMETRO_TIPO_OPERACION_SFS_12);
				if (parametro!=null){
					entidad.getComprobante().setTipoOperacion(parametro.getDato());
				}else{
					entidad.getComprobante().setTipoOperacion("");
				}
				//Codigo el domicilio fiscal
				parametro = mapParametro.get(Constantes.PARAMETRO_CODIGO_DOMICILIO_FISCAL);
				if (parametro!=null){
					entidad.getComprobante().setCodigoDomicilio(parametro.getDato());
				}else{
					entidad.getComprobante().setCodigoDomicilio("");
				}
				//Tipo de comprobante
				parametro = mapParametro.get(Constantes.PARAMETRO_TIPO_COMPROBANTE);
				if (parametro!=null){
					entidad.getComprobante().setTipoComprobante(parametro.getDato());
				}else{
					entidad.getComprobante().setTipoComprobante("");
				}
				//Moneda
				parametro = mapParametro.get(Constantes.PARAMETRO_MONEDA);
				if (parametro!=null){
					entidad.getComprobante().setMoneda(parametro.getDato());
				}else{
					entidad.getComprobante().setMoneda("");
				}
				//Serie
				parametro = mapParametro.get(Constantes.PARAMETRO_SERIE);
				if (parametro!=null){
					entidad.getComprobante().setSerie(parametro.getDato());
				}else{
					entidad.getComprobante().setSerie("");
				}
				//Unidad medida
				parametro = mapParametro.get(Constantes.PARAMETRO_UNIDAD_MEDIDA);
				if (parametro!=null){
					entidad.getDetalleComprobante().setUnidadMedida(parametro.getDato());
				}else{
					entidad.getDetalleComprobante().setUnidadMedida("");
				}
				//afectacion igv
				parametro = mapParametro.get(Constantes.PARAMETRO_AFECTACION_IGV);
				if (parametro!=null){
					entidad.getDetalleComprobante().setTipoAfectacion(parametro.getDato());
				}else{
					entidad.getDetalleComprobante().setTipoAfectacion("");
				}
				//Ruta del repositorio Data
				parametro = mapParametro.get(Constantes.PARAMETRO_SUNAT_DATA);
				if (parametro!=null){
					entidad.setSunatData(parametro.getDato());
				}else{
					entidad.setSunatData(Constantes.SUNAT_FACTURADOR_RUTA_PRUEBA);
				}
				//Ruta de la Base de Datos
				parametro = mapParametro.get(Constantes.PARAMETRO_SUNAT_BD);
				if (parametro!=null){
					entidad.setSunatBD(parametro.getDato());
				}else{
					entidad.setSunatBD(Constantes.SUNAT_FACTURADOR_DB_LOCAL);
				}

				//RUC de la empresa
				entidad.setRuc(Constantes.SUNAT_RUC_EMISOR);

				//SERIE DE LA FACTURA
				parametro = mapParametro.get(Constantes.PARAMETRO_SERIE_AUTOMATICA);
				if (parametro!=null){
					entidad.setFlagSerieAutomatica(parametro.getDato());
					
				}else{
					entidad.setFlagSerieAutomatica(Constantes.ESTADO_INACTIVO);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	*/
	public void setDatosComprobanteSFS12(List<ComprobanteBean> listaComprobante){
		
		for(ComprobanteBean entidad: listaComprobante){
			if (entidad.isResultadoValidacion()){
				entidad.getComprobante().setSumTributo(entidad.getComprobante().getTotalIgv().add(entidad.getComprobante().getSumatorioaOtrosTributos()));
				entidad.getComprobante().setTotValorVenta(entidad.getComprobante().getTotalOpGravada().add(entidad.getComprobante().getTotalOpExonerada()).add(entidad.getComprobante().getTotalOpInafecta()));
				entidad.getComprobante().setTotPrecioVenta(entidad.getComprobante().getSumTributo().add(entidad.getComprobante().getTotValorVenta()));
				entidad.getComprobante().setTotDescuento(entidad.getComprobante().getTotalDescuento().add(entidad.getComprobante().getDescuentosGlobales()));
				entidad.getComprobante().setSumOtrosCargos(entidad.getComprobante().getTotalOtrosCargos());
				entidad.getComprobante().setImpTotalVenta(entidad.getComprobante().getTotPrecioVenta().add(entidad.getComprobante().getSumOtrosCargos()));
				entidad.getComprobante().setImpTotalVenta(entidad.getComprobante().getImpTotalVenta().subtract(entidad.getComprobante().getTotDescuento()));
				
				/*Asignamos a todos dos decimales*/
				entidad.getComprobante().setSumTributo(UtilSGT.getRoundDecimal(entidad.getComprobante().getSumTributo(), 2));
				entidad.getComprobante().setTotValorVenta(UtilSGT.getRoundDecimal(entidad.getComprobante().getTotValorVenta(), 2));
				entidad.getComprobante().setTotPrecioVenta(UtilSGT.getRoundDecimal(entidad.getComprobante().getTotPrecioVenta(), 2));
				entidad.getComprobante().setTotDescuento(UtilSGT.getRoundDecimal(entidad.getComprobante().getTotDescuento(), 2));
				entidad.getComprobante().setSumOtrosCargos(UtilSGT.getRoundDecimal(entidad.getComprobante().getSumOtrosCargos(), 2));
				entidad.getComprobante().setImpTotalVenta(UtilSGT.getRoundDecimal(entidad.getComprobante().getImpTotalVenta(), 2));
				entidad.getComprobante().setImpTotalVenta(UtilSGT.getRoundDecimal(entidad.getComprobante().getImpTotalVenta(), 2));
				
				//No se esta aplicando los anticipos
				entidad.getComprobante().setVersionUbl(Constantes.SUNAT_VERSION_UBL);
				entidad.getComprobante().setCustomizacionDoc(Constantes.SUNAT_CUSTOMIZACION);
			}
			
		}
	}

	public void setDatosDetalleComprobanteSFS12(List<ComprobanteBean> listaComprobante){
		
		for(ComprobanteBean entidad: listaComprobante){
			if (entidad.isResultadoValidacion()){
				for(TblDetalleComprobante detalle: entidad.getListaDetalle()){
					detalle.setCodigoProductoSunat(Constantes.SUNAT_SIN_CODIGO);
		
					detalle.setTribCodTipoTributo(UtilSGT.getTributo(entidad.getTipoAfectacion(), Constantes.SUNAT_TRIBUTO_TIPO));
					detalle.setTribNombreTributo(UtilSGT.getTributo(entidad.getTipoAfectacion(), Constantes.SUNAT_TRIBUTO_NOMBRE));
					detalle.setTribCodTipoTributoIgv(UtilSGT.getTributo(entidad.getTipoAfectacion(), Constantes.SUNAT_TRIBUTO_CODIGO));
					detalle.setTribMontoIgv(obtenerTotalImpuestoGravada(detalle.getPrecioTotal(), entidad.getValorIGV(), entidad.getValorServicio()));
					detalle.setTribBaseImponibleIgv(UtilSGT.getMontoTributoBaseImponible(entidad.getTipoAfectacion(), detalle));
		
					detalle.setTribAfectacionIgv(entidad.getTipoAfectacion());
					detalle.setTribPorcentajeIgv(entidad.getValorIGV().toString());
		
					detalle.setIscCodTipoTributoIsc(Constantes.SUNAT_SIN_CODIGO);
					detalle.setIscMontoIsc(new BigDecimal("0"));
					detalle.setIscBaseImponibleIsc(new BigDecimal("0"));
					detalle.setIscNombreTributo("");
					detalle.setIscCodTipoTributo("");
					detalle.setIscTipoSistema("");
					detalle.setIscPorcentaje("15");
		
					detalle.setOtroCodTipoTributoOtro(Constantes.SUNAT_SIN_CODIGO);
					detalle.setOtroMontoTributo(new BigDecimal("0"));
					detalle.setOtroBaseImponibleOtro(new BigDecimal("0"));
					detalle.setOtroNombreTributo("");
					detalle.setOtroCodTipoTributo("");
					detalle.setOtroPorcentaje("15");
		
					detalle.setSumTributosItem(detalle.getTribMontoIgv().add(detalle.getIscMontoIsc()).add(detalle.getOtroMontoTributo()));
					if (detalle.getTipoAfectacion().equals(Constantes.SUNAT_INAFECTO_OPERACION_GRATUITA) || detalle.getTipoAfectacion().equals(Constantes.SUNAT_EXONERADO_OPERACION_GRATUITA) ) {
						detalle.setValorVentaItem(detalle.getValorReferencia().multiply(detalle.getCantidad()));
						detalle.setValorUnitario(detalle.getValorReferencia());
						detalle.setPrecioVentaUnitario(detalle.getValorReferencia());
						detalle.setValorReferencialUnitario(new BigDecimal("0"));
					}else{
						detalle.setValorVentaItem(detalle.getTribBaseImponibleIgv());
						detalle.setValorUnitario(detalle.getTribBaseImponibleIgv().divide(detalle.getCantidad(), 4, RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP));
						detalle.setPrecioVentaUnitario(detalle.getTribBaseImponibleIgv().add(detalle.getSumTributosItem()).divide(detalle.getCantidad(), 4, RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP));
						detalle.setValorReferencialUnitario(new BigDecimal("0"));
					}
					/*Asignamos dos decimales*/
					detalle.setValorVentaItem(UtilSGT.getRoundDecimal(detalle.getValorVentaItem(), 2));
					detalle.setValorUnitario(UtilSGT.getRoundDecimal(detalle.getValorUnitario(), 4));
					detalle.setPrecioVentaUnitario(UtilSGT.getRoundDecimal(detalle.getPrecioVentaUnitario(), 4));
					detalle.setValorReferencialUnitario(UtilSGT.getRoundDecimal(detalle.getValorReferencialUnitario(), 2));
					
					//detalle.setValorReferencialUnitario(new BigDecimal("0"));
					
		
				}
			}
			
		}
	}
	public void setDatosTributosComprobanteSFS12(List<ComprobanteBean> listaComprobante){
		
		TblTributoGeneral tributoIgv			= null;
		TblTributoGeneral tributoExo			= null;
		TblTributoGeneral tributoIna			= null;
		TblTributoGeneral tributoGra			= null;
		
		for(ComprobanteBean entidad: listaComprobante){
			if (entidad.isResultadoValidacion()){
				entidad.setListaTributo(new ArrayList<>());
				tributoIgv			= new TblTributoGeneral();
				tributoExo			= new TblTributoGeneral();
				tributoIna			= new TblTributoGeneral();
				tributoGra			= new TblTributoGeneral();
				
				tributoIgv.setIdentificadorTributo(UtilSGT.getTributoGravado(Constantes.SUNAT_TRIBUTO_CODIGO));
				tributoIgv.setNombreTributo(UtilSGT.getTributoGravado(Constantes.SUNAT_TRIBUTO_NOMBRE));
				tributoIgv.setCodigoTipoTributo(UtilSGT.getTributoGravado(Constantes.SUNAT_TRIBUTO_TIPO));
				tributoIgv.setBaseImponible(new BigDecimal("0"));
				tributoIgv.setMontoTributoItem(new BigDecimal("0"));
		
				tributoExo.setIdentificadorTributo(UtilSGT.getTributoExonerado(Constantes.SUNAT_TRIBUTO_CODIGO));
				tributoExo.setNombreTributo(UtilSGT.getTributoExonerado(Constantes.SUNAT_TRIBUTO_NOMBRE));
				tributoExo.setCodigoTipoTributo(UtilSGT.getTributoExonerado(Constantes.SUNAT_TRIBUTO_TIPO));
				tributoExo.setBaseImponible(new BigDecimal("0"));
				tributoExo.setMontoTributoItem(new BigDecimal("0"));
		
				tributoGra.setIdentificadorTributo(UtilSGT.getTributoGratuito(Constantes.SUNAT_TRIBUTO_CODIGO));
				tributoGra.setNombreTributo(UtilSGT.getTributoGratuito(Constantes.SUNAT_TRIBUTO_NOMBRE));
				tributoGra.setCodigoTipoTributo(UtilSGT.getTributoGratuito(Constantes.SUNAT_TRIBUTO_TIPO));
				tributoGra.setBaseImponible(new BigDecimal("0"));
				tributoGra.setMontoTributoItem(new BigDecimal("0"));
		
				tributoIna.setIdentificadorTributo(UtilSGT.getTributoInafecto(Constantes.SUNAT_TRIBUTO_CODIGO));
				tributoIna.setNombreTributo(UtilSGT.getTributoInafecto(Constantes.SUNAT_TRIBUTO_NOMBRE));
				tributoIna.setCodigoTipoTributo(UtilSGT.getTributoInafecto(Constantes.SUNAT_TRIBUTO_TIPO));
				tributoIna.setBaseImponible(new BigDecimal("0"));
				tributoIna.setMontoTributoItem(new BigDecimal("0"));
		
				for(TblDetalleComprobante detalle: entidad.getListaDetalle()){
					UtilSGT.getMontoAfectacion(detalle, tributoIgv, tributoExo, tributoIna, tributoGra);
				}
				
				entidad.getListaTributo().add(tributoIgv);
				entidad.getListaTributo().add(tributoExo);
				entidad.getListaTributo().add(tributoIna);
				entidad.getListaTributo().add(tributoGra);
			}
			
		}

	}

	/*public static void setDatosTributosNotaSFS12(Filtro entidad, TblTributoGeneralNota tributoIgv, TblTributoGeneralNota tributoExo, TblTributoGeneralNota tributoIna, TblTributoGeneralNota tributoGra){

		tributoIgv.setIdentificadorTributo(UtilSGT.getTributoGravado(Constantes.SUNAT_TRIBUTO_CODIGO));
		tributoIgv.setNombreTributo(UtilSGT.getTributoGravado(Constantes.SUNAT_TRIBUTO_NOMBRE));
		tributoIgv.setCodigoTipoTributo(UtilSGT.getTributoGravado(Constantes.SUNAT_TRIBUTO_TIPO));
		tributoIgv.setBaseImponible(new BigDecimal("0"));
		tributoIgv.setMontoTributoItem(new BigDecimal("0"));

		tributoExo.setIdentificadorTributo(UtilSGT.getTributoExonerado(Constantes.SUNAT_TRIBUTO_CODIGO));
		tributoExo.setNombreTributo(UtilSGT.getTributoExonerado(Constantes.SUNAT_TRIBUTO_NOMBRE));
		tributoExo.setCodigoTipoTributo(UtilSGT.getTributoExonerado(Constantes.SUNAT_TRIBUTO_TIPO));
		tributoExo.setBaseImponible(new BigDecimal("0"));
		tributoExo.setMontoTributoItem(new BigDecimal("0"));

		tributoGra.setIdentificadorTributo(UtilSGT.getTributoGratuito(Constantes.SUNAT_TRIBUTO_CODIGO));
		tributoGra.setNombreTributo(UtilSGT.getTributoGratuito(Constantes.SUNAT_TRIBUTO_NOMBRE));
		tributoGra.setCodigoTipoTributo(UtilSGT.getTributoGratuito(Constantes.SUNAT_TRIBUTO_TIPO));
		tributoGra.setBaseImponible(new BigDecimal("0"));
		tributoGra.setMontoTributoItem(new BigDecimal("0"));

		tributoIna.setIdentificadorTributo(UtilSGT.getTributoInafecto(Constantes.SUNAT_TRIBUTO_CODIGO));
		tributoIna.setNombreTributo(UtilSGT.getTributoInafecto(Constantes.SUNAT_TRIBUTO_NOMBRE));
		tributoIna.setCodigoTipoTributo(UtilSGT.getTributoInafecto(Constantes.SUNAT_TRIBUTO_TIPO));
		tributoIna.setBaseImponible(new BigDecimal("0"));
		tributoIna.setMontoTributoItem(new BigDecimal("0"));

		for(TblDetalleComprobante detalle: entidad.getListaDetalle()){
			UtilSGT.getMontoAfectacionNota(detalle, tributoIgv, tributoExo, tributoIna, tributoGra);
		}
		entidad.getListaTributoNota().add(tributoIgv);
		/**SOLO aplica para las notas el IGV*/
		/*entidad.getListaTributoNota().add(tributoExo);
		entidad.getListaTributoNota().add(tributoIna);
		entidad.getListaTributoNota().add(tributoGra);*/

	/*}*/
	/*
	 * Impuesto Gravada
	 */
	public static BigDecimal obtenerTotalImpuestoGravada(BigDecimal monto, Integer igv, Integer servicio){
		BigDecimal resultado = null;
		Integer totalImpuesto = 100;
		try{
			totalImpuesto = totalImpuesto + igv + servicio;
			//resultado = new BigDecimal(monto.doubleValue()*(igv)/totalImpuesto).setScale(2, RoundingMode.HALF_UP);
			resultado = monto.multiply(new BigDecimal(igv));
			resultado = resultado.divide(new BigDecimal(totalImpuesto), 4, RoundingMode.HALF_EVEN);
			resultado = UtilSGT.getRoundDecimal(resultado, 2);

		}catch(Exception e){
			resultado = new BigDecimal("0");
			e.printStackTrace();
		}
		return resultado;
	}
	/*
	 * Impuesto Gravada
	 */
	public static BigDecimal obtenerTotalOtrosTributosGravada(BigDecimal monto, Integer igv, Integer servicio){
		BigDecimal resultado = null;
		Integer totalImpuesto = 100;
		try{
			totalImpuesto = totalImpuesto + igv + servicio;
			//resultado = new BigDecimal(monto.doubleValue()*(servicio)/totalImpuesto).setScale(2, RoundingMode.HALF_UP);
			resultado = monto.multiply(new BigDecimal(servicio));
			resultado = resultado.divide(new BigDecimal(totalImpuesto), 4, RoundingMode.HALF_EVEN);
			resultado = UtilSGT.getRoundDecimal(resultado, 2);

		}catch(Exception e){
			resultado = new BigDecimal("0");
			e.printStackTrace();
		}
		return resultado;
	}
	/*
	 * Calculo del Detalle 
	 */
	/*public static void calculoDetalleComprobante(Filtro entidad){
		TblDetalleComprobante detalle = null;
		try{
			detalle = entidad.getDetalleComprobante();
			detalle.setPrecioTotal(detalle.getPrecioUnitario().multiply(detalle.getCantidad()));
			
			if (detalle.getDescuento()!=null && detalle.getDescuento().doubleValue()>0){
				//detalle.setPrecioFinal(new BigDecimal(detalle.getPrecioTotal().doubleValue() - detalle.getPrecioTotal().doubleValue()*detalle.getDescuento().doubleValue()/100).setScale(2, RoundingMode.HALF_UP));
				detalle.setPrecioFinal(UtilSGT.getCalculoDescuento(detalle.getPrecioTotal(), detalle.getDescuento(), 4));
				
			}else{
				detalle.setPrecioFinal(detalle.getPrecioTotal());
			}
			//Luego de la operacion se redondea a 2 decimales
			detalle.setPrecioTotal(UtilSGT.getRoundDecimal(detalle.getPrecioTotal(), 2));
			detalle.setPrecioFinal(UtilSGT.getRoundDecimal(detalle.getPrecioFinal(), 2));
		}catch(Exception e){

		}
	}*/
	/*
	 * Monto Gravada
	 */
	public static BigDecimal obtenerTotalMontoGravada(BigDecimal monto, Integer igv, Integer servicio){
		BigDecimal resultado = null;
		Integer totalImpuesto = 100; 
		try{
			totalImpuesto = totalImpuesto + igv + servicio;
			//resultado = new BigDecimal(monto.doubleValue()*(100)/totalImpuesto).setScale(2, RoundingMode.HALF_UP);
			resultado = monto.multiply(new BigDecimal("100"));
			resultado = resultado.divide(new BigDecimal(totalImpuesto), 4, RoundingMode.HALF_EVEN);
			resultado = UtilSGT.getRoundDecimal(resultado, 2);			
			//resultado = new BigDecimal(monto.doubleValue()*(100+igv)/100).setScale(2, RoundingMode.HALF_UP);

		}catch(Exception e){
			resultado = new BigDecimal("0");
			e.printStackTrace();
		}
		return resultado;
	}

	/*
	 * Calculo de los totales	  
	 */
	public void calculoCabeceraComprobante(List<ComprobanteBean> listaComprobante){
		TblComprobante comprobante = null;
		String tipoAfectacion = null;
		try{
			for(ComprobanteBean entidad: listaComprobante){
				if (entidad.isResultadoValidacion()){
					comprobante = entidad.getComprobante();
					if (comprobante.getTipoOperacion().equals(Constantes.SUNAT_TIPO_OPERACION_EXPORTACION)){
						calculoCabeceraComprobateExtranjero(entidad);
					}else{
						tipoAfectacion = UtilSGT.getTipoAfectacion(entidad.getTipoAfectacion());
						if (tipoAfectacion.equals(Constantes.SUNAT_TIPO_AFECTACION_GRAVADO)){
							calculoCabeceraComprobanteNacional(entidad);
						}
						if (tipoAfectacion.equals(Constantes.SUNAT_TIPO_AFECTACION_EXONERADO)){
							calculoCabeceraComprobanteExonerado(entidad);
						}
						if (tipoAfectacion.equals(Constantes.SUNAT_TIPO_AFECTACION_INAFECTO)){
							calculoCabeceraComprobanteInafecta(entidad);
						}
						if (tipoAfectacion.equals(Constantes.SUNAT_TIPO_AFECTACION_EXPORTACION)){
							calculoCabeceraComprobanteNacional(entidad);
						}
					}
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void calculoCabeceraComprobanteNacional(ComprobanteBean entidad){
		TblComprobante comprobante = null;
		try{
			comprobante = entidad.getComprobante();
			//inicializa
			comprobante.setDescuentosGlobales(new BigDecimal("0"));
			comprobante.setTotalImporte(new BigDecimal("0"));
			comprobante.setValorOpGratuitas(new BigDecimal("0"));
			comprobante.setTotalDescuento(new BigDecimal("0"));
			for(TblDetalleComprobante detalle: entidad.getListaDetalle()){
				//Descuento : Precio Total - Precio Final (monto de descuento)
				comprobante.setTotalDescuento(comprobante.getTotalDescuento().add(detalle.getPrecioTotal().subtract(detalle.getPrecioFinal())));

				//Total Importe: Precio Unitario x Cantidad - Descuento
				//comprobante.setTotalImporte(comprobante.getTotalImporte().add(detalle.getPrecioUnitario().multiply(detalle.getCantidad().setScale(2, RoundingMode.HALF_UP))).setScale(2, RoundingMode.HALF_UP));
				comprobante.setTotalImporte(comprobante.getTotalImporte().add(detalle.getPrecioUnitario().multiply(detalle.getCantidad())));
				//comprobante.setTotalImporte(comprobante.getTotalImporte().subtract(comprobante.getTotalDescuento()));
				//Total de valor de referencia
				//comprobante.setValorOpGratuitas(comprobante.getValorOpGratuitas().add(detalle.getValorReferencia().multiply(detalle.getCantidad().setScale(2, RoundingMode.HALF_UP))).setScale(2, RoundingMode.HALF_UP));
				comprobante.setValorOpGratuitas(comprobante.getValorOpGratuitas().add(detalle.getValorReferencia().multiply(detalle.getCantidad())));
			}
			comprobante.setTotalImporte(UtilSGT.getRoundDecimal(comprobante.getTotalImporte(), 2));
			comprobante.setValorOpGratuitas(UtilSGT.getRoundDecimal(comprobante.getValorOpGratuitas(), 2));
			//Total otros cargos: no se aplica, se deja cero por defaul
			//Total Descuento: Descuento Global + Sumatoria otros Cargos
			//comprobante.setTotalDescuento(comprobante.getDescuentosGlobales().add(comprobante.getTotalOtrosCargos()));
			//Pendiente el calculo del descuento global

			//Se calcula el IGV y el valor total de venta
			//TODO: Validar el tipo de afectacion para obtener el valor del IGV
			//Calculo del monto del igv
			//comprobante.setTotalIgv(this.obtenerTotalImpuestoGravada(comprobante.getTotalImporte(), Constantes.SUNAT_IGV));
			comprobante.setTotalIgv(obtenerTotalImpuestoGravada(comprobante.getTotalImporte(), entidad.getValorIGV(), entidad.getValorServicio()));
			//Calculo del total sin igv
			comprobante.setTotalOpGravada(obtenerTotalMontoGravada(comprobante.getTotalImporte(), entidad.getValorIGV(), entidad.getValorServicio()));
			comprobante.setTotalOpExonerada(new BigDecimal("0"));
			comprobante.setTotalOpInafecta(new BigDecimal("0"));
			//Calculo del total de otros tributos: servicio
			comprobante.setSumatorioaOtrosTributos(obtenerTotalOtrosTributosGravada(comprobante.getTotalImporte(), entidad.getValorIGV(), entidad.getValorServicio()));

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void calculoCabeceraComprobanteExonerado(ComprobanteBean entidad){
		TblComprobante comprobante = null;
		try{
			comprobante = entidad.getComprobante();
			//inicializa
			comprobante.setDescuentosGlobales(new BigDecimal("0"));
			comprobante.setTotalImporte(new BigDecimal("0"));
			comprobante.setValorOpGratuitas(new BigDecimal("0"));
			comprobante.setTotalDescuento(new BigDecimal("0"));
			for(TblDetalleComprobante detalle: entidad.getListaDetalle()){
				//Descuento : Precio Total - Precio Final (monto de descuento)
				comprobante.setTotalDescuento(comprobante.getTotalDescuento().add(detalle.getPrecioTotal().subtract(detalle.getPrecioFinal())));

				//Total Importe: Precio Unitario x Cantidad - Descuento
				//comprobante.setTotalImporte(comprobante.getTotalImporte().add(detalle.getPrecioUnitario().multiply(detalle.getCantidad().setScale(2, RoundingMode.HALF_UP))).setScale(2, RoundingMode.HALF_UP));
				comprobante.setTotalImporte(comprobante.getTotalImporte().add(detalle.getPrecioUnitario().multiply(detalle.getCantidad())));
				
				//Total de valor de referencia
				//comprobante.setValorOpGratuitas(comprobante.getValorOpGratuitas().add(detalle.getValorReferencia().multiply(detalle.getCantidad().setScale(2, RoundingMode.HALF_UP))).setScale(2, RoundingMode.HALF_UP));
				comprobante.setValorOpGratuitas(comprobante.getValorOpGratuitas().add(detalle.getValorReferencia().multiply(detalle.getCantidad())));
			}
			comprobante.setTotalImporte(comprobante.getTotalImporte().subtract(comprobante.getTotalDescuento()));
			
			/*Se redondea a dos decimales*/
			comprobante.setTotalImporte(UtilSGT.getRoundDecimal(comprobante.getTotalImporte(), 2));
			comprobante.setTotalDescuento(UtilSGT.getRoundDecimal(comprobante.getTotalDescuento(), 2));
			comprobante.setValorOpGratuitas(UtilSGT.getRoundDecimal(comprobante.getValorOpGratuitas(), 2));
			
			//Total otros cargos: no se aplica, se deja cero por defaul
			//Total Descuento: Descuento Global + Sumatoria otros Cargos
			//comprobante.setTotalDescuento(comprobante.getDescuentosGlobales().add(comprobante.getTotalOtrosCargos()));

			//Se calcula el IGV y el valor total de venta
			//TODO: Validar el tipo de afectacion para obtener el valor del IGV
			//Calculo del monto del igv
			//comprobante.setTotalIgv(this.obtenerTotalImpuestoGravada(comprobante.getTotalImporte(), Constantes.SUNAT_IGV));
			comprobante.setTotalIgv(new BigDecimal("0")); //Por ser exonerado no se aplica IGV
			//Calculo del total sin igv
			comprobante.setTotalOpGravada(new BigDecimal("0")); //Por ser exonerado no aplica total operacion gravaa
			//Se asigna el valor al campo de Total Operacion Exonerada
			comprobante.setTotalOpExonerada(comprobante.getTotalImporte());
			comprobante.setTotalOpInafecta(new BigDecimal("0"));
			//Calculo del total de otros tributos: servicio
			comprobante.setSumatorioaOtrosTributos(obtenerTotalOtrosTributosGravada(comprobante.getTotalImporte(), entidad.getValorIGV(), entidad.getValorServicio()));

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void calculoCabeceraComprobanteInafecta(ComprobanteBean entidad){
		TblComprobante comprobante = null;
		try{
			comprobante = entidad.getComprobante();
			//inicializa
			comprobante.setDescuentosGlobales(new BigDecimal("0"));
			comprobante.setTotalImporte(new BigDecimal("0"));
			comprobante.setValorOpGratuitas(new BigDecimal("0"));
			comprobante.setTotalDescuento(new BigDecimal("0"));
			for(TblDetalleComprobante detalle: entidad.getListaDetalle()){
				//Descuento : Precio Total - Precio Final (monto de descuento)
				comprobante.setTotalDescuento(comprobante.getTotalDescuento().add(detalle.getPrecioTotal().subtract(detalle.getPrecioFinal())));

				//Total Importe: Precio Unitario x Cantidad - Descuento
				//comprobante.setTotalImporte(comprobante.getTotalImporte().add(detalle.getPrecioUnitario().multiply(detalle.getCantidad().setScale(2, RoundingMode.HALF_UP))).setScale(2, RoundingMode.HALF_UP));
				comprobante.setTotalImporte(comprobante.getTotalImporte().add(detalle.getPrecioUnitario().multiply(detalle.getCantidad())));
				
				//Total de valor de referencia
				//comprobante.setValorOpGratuitas(comprobante.getValorOpGratuitas().add(detalle.getValorReferencia().multiply(detalle.getCantidad().setScale(2, RoundingMode.HALF_UP))).setScale(2, RoundingMode.HALF_UP));
				comprobante.setValorOpGratuitas(comprobante.getValorOpGratuitas().add(detalle.getValorReferencia().multiply(detalle.getCantidad())));
			}
			comprobante.setTotalImporte(comprobante.getTotalImporte().subtract(comprobante.getTotalDescuento()));
			/*Redondeo a dos decimales*/
			comprobante.setTotalDescuento(UtilSGT.getRoundDecimal(comprobante.getTotalDescuento(), 2));
			comprobante.setValorOpGratuitas(UtilSGT.getRoundDecimal(comprobante.getTotalDescuento(), 2));
			comprobante.setValorOpGratuitas(UtilSGT.getRoundDecimal(comprobante.getValorOpGratuitas(), 2));
			comprobante.setTotalImporte(UtilSGT.getRoundDecimal(comprobante.getTotalImporte(), 2));
			
			//Total otros cargos: no se aplica, se deja cero por defaul
			//Total Descuento: Descuento Global + Sumatoria otros Cargos
			//comprobante.setTotalDescuento(comprobante.getDescuentosGlobales().add(comprobante.getTotalOtrosCargos()));

			//Se calcula el IGV y el valor total de venta
			//TODO: Validar el tipo de afectacion para obtener el valor del IGV
			//Calculo del monto del igv
			//comprobante.setTotalIgv(this.obtenerTotalImpuestoGravada(comprobante.getTotalImporte(), Constantes.SUNAT_IGV));
			comprobante.setTotalIgv(new BigDecimal("0")); //Por ser exonerado no se aplica IGV
			//Calculo del total sin igv
			comprobante.setTotalOpGravada(new BigDecimal("0")); //Por ser exonerado no aplica total operacion gravaa
			//Se asigna el valor al campo de Total Operacion Exonerada
			comprobante.setTotalOpExonerada(new BigDecimal("0"));
			//Se asigna el valor al campo de Total Inafecto
			comprobante.setTotalOpInafecta(comprobante.getTotalImporte());
			//Calculo del total de otros tributos: servicio
			comprobante.setSumatorioaOtrosTributos(obtenerTotalOtrosTributosGravada(comprobante.getTotalImporte(), entidad.getValorIGV(), entidad.getValorServicio()));

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void calculoCabeceraComprobateExtranjero(ComprobanteBean entidad){
		TblComprobante comprobante = null;
		try{
			comprobante = entidad.getComprobante();
			//inicializa
			comprobante.setDescuentosGlobales(new BigDecimal("0"));
			comprobante.setTotalImporte(new BigDecimal("0"));
			comprobante.setValorOpGratuitas(new BigDecimal("0"));
			comprobante.setTotalDescuento(new BigDecimal("0"));
			for(TblDetalleComprobante detalle: entidad.getListaDetalle()){
				//Descuento : Precio Total - Precio Final (monto de descuento)
				comprobante.setTotalDescuento(comprobante.getTotalDescuento().add(detalle.getPrecioTotal().subtract(detalle.getPrecioFinal())));

				//Total Importe: Precio Unitario x Cantidad - Descuento
				comprobante.setTotalImporte(comprobante.getTotalImporte().add(detalle.getPrecioUnitario().multiply(detalle.getCantidad().setScale(2, RoundingMode.HALF_UP))).setScale(2, RoundingMode.HALF_UP));
				comprobante.setTotalImporte(comprobante.getTotalImporte().subtract(comprobante.getDescuentosGlobales()));
			}
			//Total otros cargos: no se aplica, se deja cero por defaul
			//Total Descuento: Descuento Global + Sumatoria otros Cargos
			//comprobante.setTotalDescuento(comprobante.getDescuentosGlobales().add(comprobante.getTotalOtrosCargos()));

			//Se calcula el IGV y el valor total de venta
			//TODO: Validar el tipo de afectacion para obtener el valor del IGV
			//Calculo del monto del igv
			//comprobante.setTotalIgv(this.obtenerTotalImpuestoGravada(comprobante.getTotalImporte(), Constantes.SUNAT_IGV));
			comprobante.setTotalIgv(obtenerTotalImpuestoGravada(comprobante.getTotalImporte(), 0, entidad.getValorServicio())); // IGV = 0
			//Calculo del total sin igv
			comprobante.setTotalOpGravada(new BigDecimal("0"));// IGV = 0
			comprobante.setTotalOpInafecta(obtenerTotalMontoGravada(comprobante.getTotalImporte(), 0 , entidad.getValorServicio()));
			//Calculo del total de otros tributos: servicio
			comprobante.setSumatorioaOtrosTributos(obtenerTotalOtrosTributosGravada(comprobante.getTotalImporte(), 0 , entidad.getValorServicio()));// IGV = 0

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	public static TblBandejaFacturador setDatosBandeja(BandejaFacturadorBean bandeja){
		TblBandejaFacturador bandejaFacturador = new TblBandejaFacturador();
		try{
			bandejaFacturador.setNumeroRuc(bandeja.getNumeroRuc());
			bandejaFacturador.setTipoDocumento(bandeja.getTipoDocumento());
			bandejaFacturador.setNumeroDocumento(bandeja.getNumeroDocumento());
			bandejaFacturador.setFechaCargue(bandeja.getFechaCarga());
			bandejaFacturador.setFechaGeneracion(bandeja.getFechaGeneracion());
			bandejaFacturador.setFechaEnvio(bandeja.getFechaEnvio());
			bandejaFacturador.setObservacion(bandeja.getObservacion());
			bandejaFacturador.setNombreArchivo(bandeja.getNombreArchivo());
			bandejaFacturador.setSituacion(bandeja.getSituacion());
			bandejaFacturador.setTipoArchivo(bandeja.getTipoArchivo());
			bandejaFacturador.setFirmaDigital(bandeja.getFirmaDigital());
			bandejaFacturador.setEstado(Constantes.ESTADO_ACTIVO);


		}catch(Exception e){
			e.printStackTrace();
		}
		return bandejaFacturador;
	}
	*/
	/*
	public static TblBandejaFacturador editarDatosBandeja(BandejaFacturadorBean bandeja, TblBandejaFacturador bandejaFacturador){

		try{
			bandejaFacturador.setNumeroRuc(bandeja.getNumeroRuc());
			bandejaFacturador.setTipoDocumento(bandeja.getTipoDocumento());
			bandejaFacturador.setNumeroDocumento(bandeja.getNumeroDocumento());
			bandejaFacturador.setFechaCargue(bandeja.getFechaCarga());
			bandejaFacturador.setFechaGeneracion(bandeja.getFechaGeneracion());
			bandejaFacturador.setFechaEnvio(bandeja.getFechaEnvio());
			bandejaFacturador.setObservacion(bandeja.getObservacion());
			bandejaFacturador.setNombreArchivo(bandeja.getNombreArchivo());
			bandejaFacturador.setSituacion(bandeja.getSituacion());
			bandejaFacturador.setTipoArchivo(bandeja.getTipoArchivo());
			bandejaFacturador.setFirmaDigital(bandeja.getFirmaDigital());


		}catch(Exception e){
			e.printStackTrace();
		}
		return bandejaFacturador;
	}
	*/
	/*
	 * Obtiene la descripcion de la leyenda
	 */
	/*@SuppressWarnings("unchecked")
	public static String getDescripcionLeyenda(String strCodigoLeyenda, HttpServletRequest request){
		String resultado 						= null;
		Map<String, Object> mapTipoLeyenda		= null;
		Map<String, TblParametro> mapParametro	= null;
		TblParametro parametro 					= null; 
		String strDetraccion					= null;
		try{

			mapTipoLeyenda = (Map<String, Object>)request.getSession().getAttribute("SessionMapTipoLeyendaCodigo");
			if (strCodigoLeyenda.equals(Constantes.SUNAT_LEYENDA_DETRACCION_3001)){
				mapParametro = (Map<String, TblParametro>)request.getSession().getAttribute("SessionMapParametroSistema");
				if (mapParametro !=null){
					parametro = mapParametro.get(Constantes.PARAMETRO_DETRACCION);
					if (parametro!=null){
						strDetraccion =parametro.getDato();
					}else{
						strDetraccion = "Por registrar Parametro";
					}
				}else{
					strDetraccion = "Por definir Parametros";
				}
				resultado = strDetraccion;
			}else{
				resultado = (String)mapTipoLeyenda.get(strCodigoLeyenda);
				if (resultado !=null && resultado.length()>6){
					//resultado = resultado.substring(6);
					resultado = "";
				}

			}

		}catch(Exception e){
			resultado = "";
			e.printStackTrace();
		}finally{
			mapTipoLeyenda	= null;
			mapParametro	= null;
			parametro 		= null; 
			strDetraccion	= null;
		}
		return resultado;
	}*/
	@SuppressWarnings("unchecked")
	public static String obtenerParametro(HttpServletRequest request, String strParametro){
		Map<String, TblParametro> mapParametro	= null;
		TblParametro parametro 					= null; 
		String resultado						= "";
		mapParametro = (Map<String, TblParametro>)request.getSession().getAttribute("SessionMapParametroSistema");
		if (mapParametro !=null){
			parametro = mapParametro.get(strParametro);
			if (parametro!=null){
				resultado =parametro.getDato();
			}else{
				resultado = "01";
			}
		}else{
			resultado = "01";
		}
		return resultado;
	}

	public static BigDecimal round(BigDecimal d, int scale, boolean roundUp) {
		int mode = (roundUp) ? BigDecimal.ROUND_UP : BigDecimal.ROUND_DOWN;
		return d.setScale(scale, mode);
	}
	
	public static void asignarAuditoria(List<ComprobanteBean> listaComprobante, AuditoriaBean auditoriaBean){
		for(ComprobanteBean entidad: listaComprobante){
			entidad.getComprobante().setAuditoriaCreacion(auditoriaBean);
			entidad.getComprobante().setCodigoVerificacion(UUID.randomUUID().toString());
			//Detalle de comprobante
			for(TblDetalleComprobante detalle: entidad.getListaDetalle()){
				detalle.setAuditoriaCreacion(auditoriaBean);
			}
			//Leyendas
			if (entidad.getLeyenda() != null){
				entidad.getLeyenda().setAuditoriaCreacion(auditoriaBean);
			}
			//Tributos
			if (entidad.getListaTributo() != null){
				for(TblTributoGeneral tributo: entidad.getListaTributo()){
					tributo.setAuditoriaCreacion(auditoriaBean);
				}
			}
			
			
		}
	}
}
