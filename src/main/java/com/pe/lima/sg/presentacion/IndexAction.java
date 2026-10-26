package com.pe.lima.sg.presentacion;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pe.lima.sg.api.TipoCambioApiSunat;
import com.pe.lima.sg.api.bean.TipoCambioBean;
import com.pe.lima.sg.dao.mantenimiento.ITipoCambioDAO;
import com.pe.lima.sg.entity.mantenimiento.TblParametro;
import com.pe.lima.sg.entity.mantenimiento.TblTipoCambio;
import com.pe.lima.sg.entity.seguridad.TblOpcion;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.ListaUtilAction;
import com.pe.lima.sg.presentacion.util.UtilSGT;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class IndexAction {
	@Autowired
	private ListaUtilAction listaUtil;
	
	@Autowired
	private ITipoCambioDAO tipoCambioDao;
	
	@Autowired
	private TipoCambioApiSunat tipoCambioApiSunat;
	//@Autowired
	//private IUsuarioDAO usuario;

	/**
	 * Se encarga de cargar la pagina principal
	 * 
	 * @param httpSession
	 * @param authentication
	 * @return
	 */
	@RequestMapping("/")
	String cargarPagina(HttpSession httpSession, Authentication authentication, HttpServletRequest request) {
		TblTipoCambio tipoCambio		= null;
		TipoCambioBean tipoCambioBean	= null;
		TblParametro parametro 			= null;
		log.debug("[cargarPagina] Inicio" );
		if (authentication != null) {
			User currentUser = (User) authentication.getPrincipal();
			log.debug("[cargarPagina] User:"+currentUser.getUsername() );
			/*Optional<TblUsuario> entidad = usuario.findOneByLogin(currentUser.getUsername());
			if (entidad != null && entidad.get() !=null && entidad.get().getNombre() != null){
				httpSession.setAttribute("usuario", entidad.get().getNombre());
				httpSession.setAttribute("perfil", entidad.get().getTblPerfil().getNombre());
				httpSession.setAttribute("id_usuario", entidad.get().getCodigoUsuario());
					
			}else{*/
				httpSession.setAttribute("usuario", "Pepito Perez");
				httpSession.setAttribute("perfil", "Administrator");
				httpSession.setAttribute("id_usuario", "1");
				
			/*}*/
			this.cargarDatos(httpSession);
			log.debug("[cargarPagina] Tipo Caducidad:"+httpSession.getAttribute("sesMapTiposCaducidad"));
			
			
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String fechaFormateada = LocalDate.now().format(fmt);
			tipoCambio = tipoCambioDao.obtenerUltimoTipoCambioxFecha(fechaFormateada);
			log.debug("[cargarPagina] Fecha:"+fechaFormateada+ " - Fecha TC:"+tipoCambio.getFecha());
			if (UtilSGT.esMismaFecha(fechaFormateada, tipoCambio.getFecha())) {
				httpSession.setAttribute("sesTipoCambioInfo", tipoCambio.getValor() + " ["+fechaFormateada+"]");
			}else {
				try {
					@SuppressWarnings("unchecked")
					Map<String, TblParametro> mapParametro = (Map<String, TblParametro>)request.getSession().getAttribute("SessionMapParametros");
					parametro = mapParametro.get(Constantes.TIPO_CAMBIO_SUNAT_TOKEN);
					if (parametro != null) {
						tipoCambioBean = tipoCambioApiSunat.obtenerTipoCambio(parametro.getDato());
						log.debug("[cargarPagina] tipoCambioBean:"+tipoCambioBean.getFecha() );
						tipoCambio = new TblTipoCambio();
						tipoCambio.setFecha(UtilSGT.aSqlDate(tipoCambioBean.getFecha()));
						tipoCambio.setEstado("1");
						tipoCambio.setValor(tipoCambioBean.getVenta());
						tipoCambio.setFechaCreacion(new Date());
						tipoCambioDao.save(tipoCambio);
						httpSession.setAttribute("sesTipoCambioInfo", tipoCambio.getValor() + " ["+fechaFormateada+"] - API");
					} else {
						httpSession.setAttribute("sesTipoCambioInfo", "Sin Token");
					}
				} catch(Exception e) {
					log.error("[cargarPagina] Error:"+ e.getMessage() );
					httpSession.setAttribute("sesTipoCambioInfo", "API Inactivo");
				}
				
			}
			//this.cargarMenu(httpSession, entidad.get().getTblPerfil().getCodigoPerfil());
			
		}
		log.debug("[cargarPagina] Fin" );
		//return "index";
		return "inicio";
	}


	/**
	 * Se encarga de cargar en sesion los valores de las lista desplegables
	 * 
	 * @param httpSession
	 *            Sesion del usuario
	 */
	private void cargarDatos(HttpSession httpSession) {
		log.debug("[cargarDatos] Inicio" );
		Map<String, Object> map = null;
		map = listaUtil.obtenerValoresTipoCaducidad();
		httpSession.setAttribute("sesMapTiposCaducidad", map);
		map = listaUtil.obtenerValoresEstadoUsuario();
		httpSession.setAttribute("sesMapEstadousuario", map);
		
		log.debug("[cargarDatos] Fin" );
	}
	/**
	 * Obtener arbol de ID de las opciones
	 * 
	 * @param opciones
	 * @return
	 */
	public List<Integer> obtenerArbol(List<TblOpcion> opciones) {
		List<TblOpcion> asignados = new ArrayList<TblOpcion>();
		Item raiz = new Item(0);
		crearArbol(opciones, asignados, raiz);
		return crearMenu(raiz);
	}
	
	public List<Integer> crearMenu(Item item) {
		List<Integer> ids = new ArrayList<Integer>();

		for (Item hijo : item.hijos) {
			if (hijo.path == null) {
				ids.add(hijo.id);
				ids.addAll(crearMenu(hijo));
			} else {
				ids.add(new Integer(hijo.id));
			}
		}
		return ids;
	}
	/**
	 * Crea el arbol de opciones por el modulo
	 */
	public void crearArbol(List<TblOpcion> opciones, List<TblOpcion> asignados, Item seleccionado) {
		for (TblOpcion opcion : opciones) {

			if (opcion.getModulo().longValue() == seleccionado.id
					&& estaAsignado(asignados, opcion.getCodigoOpcion()) == false) {
				Item hijo = new Item(opcion, seleccionado);
				seleccionado.hijos.add(hijo);
				asignados.add(opcion);
				crearArbol(opciones, asignados, hijo);
			}
		}

		if ((opciones.size() > asignados.size()) && seleccionado.padre != null) {
			crearArbol(opciones, asignados, seleccionado.padre);
		}
	}
	
	class Item {
		Integer id;
		String label;
		String path;
		Item padre;
		boolean asignado = false;
		LinkedList<Item> hijos = new LinkedList<IndexAction.Item>();

		public Item(Integer id) {
			
			this.id = id;
		}

		public Item(TblOpcion segTblOpcion, Item padre) {
			
			this.id = segTblOpcion.getCodigoOpcion();
			this.label = segTblOpcion.getNombre();
			this.path = segTblOpcion.getRuta();
			this.padre = padre;
		}
	}
	/**
	 * Verifica..
	 * 
	 * @param asignados
	 * @param id
	 * @return
	 */
	public boolean estaAsignado(List<TblOpcion> asignados, long id) {
		for (TblOpcion opcion : asignados) {
			if (opcion.getCodigoOpcion() == id) {
				return true;
			}
		}
		return false;
	}

}
