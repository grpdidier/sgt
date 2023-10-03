package com.pe.lima.sg.presentacion.util;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.pe.lima.sg.api.bean.CredencialBean;
import com.pe.lima.sg.entity.caja.TblMasivoSunat;
import com.pe.lima.sg.entity.mantenimiento.TblParametro;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UtilFacturacion {

	@SuppressWarnings("unchecked")
	/*Obtenemos las credenciales de EFACT y del API Ubigeo*/
	public CredencialBean obtenerCredenciales(HttpServletRequest request) {
		log.info("[obtenerCredenciales] Inicio");
		CredencialBean credencialBean = new CredencialBean();
		TblParametro parametro = null;
		Map<String, TblParametro> mapParametro = (Map<String, TblParametro>)request.getSession().getAttribute("SessionMapParametros");
		parametro = mapParametro.get(Constantes.RUTA_FILE_OSE);
		credencialBean.setPath(parametro.getDato());
		parametro = mapParametro.get(Constantes.URL_EFACT_TOKEN);
		credencialBean.setResourceToken(parametro.getDato());
		parametro = mapParametro.get(Constantes.URL_EFACT_DOCUMENTO);
		credencialBean.setResourceDocumento(parametro.getDato());
		parametro = mapParametro.get(Constantes.URL_EFACT_CDR);
		credencialBean.setResourceCdr(parametro.getDato());
		parametro = mapParametro.get(Constantes.URL_EFACT_XML);
		credencialBean.setResourceXml(parametro.getDato());
		parametro = mapParametro.get(Constantes.URL_EFACT_PDF);
		credencialBean.setResourcePdf(parametro.getDato());
		
		parametro = mapParametro.get(Constantes.EFACT_CLIENT_SECRET);
		credencialBean.setClientSecret(parametro.getDato());
		parametro = mapParametro.get(Constantes.EFACT_GRANT_TYPE);
		credencialBean.setGrantType(parametro.getDato());
		parametro = mapParametro.get(Constantes.EFACT_USER_NAME);
		credencialBean.setUserName(parametro.getDato());
		parametro = mapParametro.get(Constantes.EFACT_PASSWORD);
		credencialBean.setPassword(parametro.getDato());
		//Ubigeo
		parametro = mapParametro.get(Constantes.UBIGEO_URL);
		credencialBean.setUrlUbigeo(parametro.getDato());
		parametro = mapParametro.get(Constantes.UBIGEO_TOKEN);
		credencialBean.setTokenUbigeo(parametro.getDato());
		log.info("[obtenerCredenciales] Fin");
		return credencialBean;
	}
	
	@SuppressWarnings("unchecked")
	/*Obtenemos el Flag para generar multiples masivos de facturas*/
	public String obtenerParametroMultiple(HttpServletRequest request) {
		String flag = null;
		TblParametro parametro = null;
		Map<String, TblParametro> mapParametro = (Map<String, TblParametro>)request.getSession().getAttribute("SessionMapParametros");
		parametro = mapParametro.get(Constantes.MULTIPLE_FACTURA);
		flag = parametro.getDato();
		return flag;
	}
	/*Seteamos el estado y la fecha del proceso*/
	public TblMasivoSunat actualizarEstadoMasivo(TblMasivoSunat tblMasivoSunat, Integer totalFacturasAGenerar) {
		Integer xmlGenerado = tblMasivoSunat.getXmlGenerado();
		Integer cdrGenerado = tblMasivoSunat.getCdrGenerado();
		Integer pdfGenerado = tblMasivoSunat.getPdfGenerado();
		if (xmlGenerado == cdrGenerado && cdrGenerado == pdfGenerado && pdfGenerado == totalFacturasAGenerar) {
			tblMasivoSunat.setEstadoMasivo(Constantes.MASIVO_ESTADO_FINALIZADO);
		}else {
			tblMasivoSunat.setEstadoMasivo(Constantes.MASIVO_ESTADO_EN_PARCIAL);
		}
		tblMasivoSunat.setFechaProceso(new Date(System.currentTimeMillis()));
		return tblMasivoSunat;
	}
}
