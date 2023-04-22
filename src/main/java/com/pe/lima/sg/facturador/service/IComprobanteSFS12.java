package com.pe.lima.sg.facturador.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.pe.lima.sg.facturador.bean.AuditoriaBean;
import com.pe.lima.sg.facturador.bean.ComprobanteBean;
import com.pe.lima.sg.facturador.bean.ErrorBean;
import com.pe.lima.sg.facturador.bean.ValidacionBean;
import com.pe.lima.sg.facturador.entity.TblEmpresa;

@Component
public interface IComprobanteSFS12 {
	
	public boolean validarComprobante(List<ComprobanteBean> listaComprobante, List<ValidacionBean> listaValidacion, TblEmpresa empresa);

	public boolean calcularAsignarDatosComprobante(List<ComprobanteBean> listaComprobante , AuditoriaBean auditoriaBean);
	
	public boolean registrarComprobante(List<ComprobanteBean> listaComprobante, List<ErrorBean> listaError, TblEmpresa empresa, AuditoriaBean auditoriaBean);
	
	
}
