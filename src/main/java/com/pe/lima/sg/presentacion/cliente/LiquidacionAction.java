package com.pe.lima.sg.presentacion.cliente;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.dao.cliente.IContratoDAO;
import com.pe.lima.sg.entity.cliente.TblContrato;
import com.pe.lima.sg.entity.mantenimiento.TblEdificio;
import com.pe.lima.sg.entity.mantenimiento.TblPersona;
import com.pe.lima.sg.entity.mantenimiento.TblTienda;
import com.pe.lima.sg.presentacion.BaseOperacionPresentacion;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.PageWrapper;
import com.pe.lima.sg.presentacion.util.PageableSG;

import lombok.extern.slf4j.Slf4j;

/**
 * Clase Bean que se encarga de la administracion de los liquidacion
 *
 * 			
 */
@Slf4j
@Controller
public class LiquidacionAction extends BaseOperacionPresentacion<TblContrato> {
	
	@Autowired
	private IContratoDAO contratoDao;

	
	
	private String urlPaginado = "/liquidacion/paginado/"; 
	
	@SuppressWarnings("rawtypes")
	@Override
	public BaseOperacionDAO getDao() {
		return contratoDao;
	}

	/**
	 * Se encarga de listar todos las liquidaciones
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/liquidacion", method = RequestMethod.GET)
	public String traerRegistros(Model model, String path,  PageableSG pageable,HttpServletRequest request) {
		TblContrato filtro = null;
		try{
			log.debug("[traerRegistros] Inicio");
			path = "cliente/liquidacion/liq_listado";
			filtro = new TblContrato();
			filtro.setTblPersona(new TblPersona());
			filtro.setTblTienda(new TblTienda());
			filtro.getTblTienda().setTblEdificio(new TblEdificio());
			model.addAttribute("filtro", filtro);
			request.getSession().setAttribute("sessionFiltroCriterio", filtro);

			request.getSession().setAttribute("sessionListaContrato", null);
			request.getSession().setAttribute("PageContrato", null);
			request.getSession().setAttribute("PageableSGContrato", pageable);
			log.debug("[traerRegistros] Fin");
		}catch(Exception e){
			log.debug("[traerRegistros] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}

		return path;
	}
	/**
	 * Se encarga de buscar la informacion del Contrato segun el filtro
	 * seleccionado
	 * 
	 * @param model
	 * @param contratoBean
	 * @return
	 */
	@RequestMapping(value = "/liquidacion/q", method = RequestMethod.POST)
	public String traerRegistrosFiltrados(Model model, TblContrato filtro, String path ,  PageableSG pageable, HttpServletRequest request) {
		//Map<String, Object> campos = null;
		path = "cliente/liquidacion/liq_listado";
		String strSeleccion = "";
		try{
			log.debug("[traerRegistrosFiltrados] Inicio");
			this.listarContratos(model, filtro, pageable, this.urlPaginado, request);
			model.addAttribute("filtro", filtro);
			model.addAttribute("strSeleccion", strSeleccion);
			request.getSession().setAttribute("sessionFiltroCriterio", filtro);
			log.debug("[traerRegistrosFiltrados] Fin");
		}catch(Exception e){
			log.debug("[traerRegistrosFiltrados] Error: "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("respuesta", "Se produco un Error:"+e.getMessage());
		}finally{
			//campos		= null;
		}
		log.debug("[traerRegistrosFiltrados] Fin");
		return path;
	}

	/*** Listado de Contratos ***/
	private void listarContratos(Model model, TblContrato tblContrato,  PageableSG pageable, String url, HttpServletRequest request){
		//List<TblContrato> entidades = new ArrayList<TblContrato>();
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "numero"));
		try{
			Specification<TblContrato> filtro = Specifications.where(com.pe.lima.sg.dao.cliente.ContratoSpecifications.conNombre(tblContrato.getTblPersona().getNombre()))
					.and(com.pe.lima.sg.dao.cliente.ContratoSpecifications.conPaterno(tblContrato.getTblPersona().getPaterno()))
					.and(com.pe.lima.sg.dao.cliente.ContratoSpecifications.conMaterno(tblContrato.getTblPersona().getMaterno()))
					.and(com.pe.lima.sg.dao.cliente.ContratoSpecifications.conEdificio(tblContrato.getTblTienda().getTblEdificio().getCodigoEdificio()))
					.and(com.pe.lima.sg.dao.cliente.ContratoSpecifications.conTienda(tblContrato.getTblTienda().getNumero()))
					.and(com.pe.lima.sg.dao.cliente.ContratoSpecifications.conRuc(tblContrato.getTblPersona().getNumeroRuc()))
					.and(com.pe.lima.sg.dao.cliente.ContratoSpecifications.conRazonSocial(tblContrato.getTblPersona().getRazonSocial()))
					.and(com.pe.lima.sg.dao.cliente.ContratoSpecifications.conEstado(Constantes.ESTADO_REGISTRO_ACTIVO));
			/*entidades = contratoDao.findAll(filtro);
			log.debug("[listarContratos] entidades:"+entidades);
			model.addAttribute("registros", entidades);*/
			pageable.setSort(sort);
			Page<TblContrato> entidadPage = contratoDao.findAll(filtro, pageable);
			PageWrapper<TblContrato> page = new PageWrapper<TblContrato>(entidadPage, url, pageable);
			model.addAttribute("registros", page.getContent());
			model.addAttribute("page", page);
			
			request.getSession().setAttribute("sessionFiltroCriterio", tblContrato);
			request.getSession().setAttribute("sessionListaContrato", page.getContent());
			request.getSession().setAttribute("PageContrato", page);
			request.getSession().setAttribute("PageableSGContrato", pageable);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sort = null;
		}
	}

	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/liquidacion/regresar", method = RequestMethod.GET)
	public String regresar(Model model, String path, HttpServletRequest request) {
		TblContrato filtro = null;
		List<TblContrato> lista = null;
		PageWrapper<TblContrato> page = null;
		try{
			log.debug("[regresar] Inicio");
			path = "cliente/liquidacion/liq_listado";
			
			filtro = (TblContrato)request.getSession().getAttribute("sessionFiltroCriterio");
			model.addAttribute("filtro", filtro);
			lista = (List<TblContrato>)request.getSession().getAttribute("sessionListaContrato");
			model.addAttribute("registros",lista);
			page = (PageWrapper<TblContrato>) request.getSession().getAttribute("PageContrato");
			model.addAttribute("page", page);
			
			
			log.debug("[regresar] Fin");
		}catch(Exception e){
			log.debug("[regresar] Error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			filtro = null;
		}
		
		return path;
	}

	@Override
	public TblContrato getNuevaEntidad() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
