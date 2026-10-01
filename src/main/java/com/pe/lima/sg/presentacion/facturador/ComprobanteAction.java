package com.pe.lima.sg.presentacion.facturador;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pe.lima.sg.bean.facturador.DocumentoBean;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.UtilSGT;
import com.pe.lima.sg.rs.facturador.ComprobanteDao;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
public class ComprobanteAction {
	@Autowired
	private ComprobanteDao comprobanteDao;

	

	@RequestMapping(value = "/comprobante", method = RequestMethod.GET)
	public String mostrarFormulario(Model model, String path, HttpServletRequest request) {
		List<DocumentoBean> listaComprobante	= null;
		try{
			log.debug("[mostrarFormulario] Inicio");
			path = "facturador/fac_listado";
			Filtro filtro = new Filtro();
			filtro.setAnio(UtilSGT.obtenerAnioActual());
			filtro.setMes(UtilSGT.obtenerMesActual());
			filtro.setCodigoEdificacion(Constantes.CODIGO_LA_REYNA);
			model.addAttribute("filtro", filtro);
			model.addAttribute("registros", listaComprobante);
			request.getSession().setAttribute("listaComprobanteSUNAT", listaComprobante);
			log.debug("[mostrarFormulario] Fin");
		}catch(Exception e){
			log.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}

		return path;
	}
	@RequestMapping(value = "/comprobante/busqueda", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, Filtro filtro, String path,HttpServletRequest request) {
		path = "facturador/fac_listado";
		List<DocumentoBean> listaComprobante	= null;
		try{
			log.debug("[traerRegistrosFiltrados] Inicio");
			if (criteriosFiltrosOk(filtro, model)) { 
				listaComprobante = this.listarComprobante(filtro);
			}
			model.addAttribute("registros", listaComprobante);
			request.getSession().setAttribute("listaComprobanteSUNAT", listaComprobante);
			model.addAttribute("filtro", filtro);
			log.debug("[traerRegistrosFiltrados] Fin");
		}catch(Exception e){
			log.debug("[traerRegistrosFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}
		log.debug("[traerRegistrosFiltrados] Fin");
		return path;
	}

	private boolean criteriosFiltrosOk(Filtro filtro, Model model) {
		boolean resultado = true;
		if (filtro.getAnio() <= 0) {
			model.addAttribute("respuesta", "Debe seleccionar el año");
			return false;
		}
		if (filtro.getMes() <= 0) {
			model.addAttribute("respuesta", "Debe seleccionar el mes");
			return false;
		}
		if (filtro.getCodigoEdificacion() <= 0) {
			model.addAttribute("respuesta", "Debe seleccionar el Inmueble");
			return false;
		}
		if (filtro.getNumero() == null || filtro.getNumero().trim().equals("")) {
			filtro.setNumeroFiltro("%");
		} else {
			filtro.setNumeroFiltro(filtro.getNumero().trim());
		}
		return resultado;
	}
	private List<DocumentoBean> listarComprobante(Filtro filtro){
		List<DocumentoBean> entidades = new ArrayList<>();
		try{
			entidades = comprobanteDao.getDocumentoSinComprobante(filtro);
			log.debug("[listarComprobante] entidades:"+entidades);
		}catch(Exception e){
			e.printStackTrace();
		}
		return entidades;
	}
	
	
	
}
