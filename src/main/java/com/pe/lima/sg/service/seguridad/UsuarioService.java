package com.pe.lima.sg.service.seguridad;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author: Gregorio Rodriguez P.
 * @Clase: UsuarioService
 * @Descripcion: Implementacion de los metodos de la interfaz de UsuarioService
 *
 */



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pe.lima.sg.dao.seguridad.IUsuarioDAO;
import com.pe.lima.sg.entity.seguridad.TblUsuario;
import com.pe.lima.sg.presentacion.cliente.ArbitrioAction;
import com.pe.lima.sg.util.SysOutPrintln;
@Service
public class UsuarioService  {
	private static final Logger logger = LogManager.getLogger(UsuarioService.class);
	@Autowired
	private IUsuarioDAO usuarioDao;
	
	 /**
	  * getAllUsuarios : Lista todos los usuarios
	  * */
	public List<TblUsuario> getAllUsuarios() {
		logger.debug("[getAllUsuarios] Inicio");
		List<TblUsuario> listaUsuario = null;
		try{
			listaUsuario = usuarioDao.findAll();
		}catch(Exception e){
			listaUsuario = null;
		}
		logger.debug("[getAllUsuarios] Fin");
		return listaUsuario;
	}

	 /**
	  * getUsuarioById : Obtiene un usuario
	  * */
	public TblUsuario getUsuarioById(Integer id) {
		logger.debug("[getUsuarioById] Inicio");
		TblUsuario usuario = null;
		try{
			usuario = usuarioDao.findOne(id);
		}catch(Exception e){
			usuario = null;
		}
		logger.debug("[getUsuarioById] Fin");
		return usuario;
	}
	

	/**
	  * addUsuario : Registra un usuario
	  * */
	public boolean addUsuario(TblUsuario usuario) {
		logger.debug("[addUsuario] Inicio");
		List<TblUsuario>  lista = null;
		boolean resultado = false;
		try{
			lista = usuarioDao.findByNombreEstado(usuario.getNombre(), usuario.getEstado());
			 if (lista != null && lista.size() > 0) {
				 resultado = false;
	         } else {
	        	 usuarioDao.save(usuario);
	        	 resultado = true;
	         }
		}catch(Exception e){
			e.printStackTrace();
			logger.debug("[addUsuario] Error:"+e.getMessage());
		}
		logger.debug("[addUsuario] Fin");
		return resultado;
	}

	/**
	  * updateUsuario : Actualiza un usuario
	  * */
	public boolean updateUsuario(TblUsuario usuario) {
		logger.debug("[updateUsuario] Inicio");
		boolean resultado = false;
		try{
			usuarioDao.save(usuario);
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
			resultado = false;
			logger.debug("[updateUsuario] Error:"+e.getMessage());
		}
		logger.debug("[updateUsuario] Fin");
		return resultado;
	}

	/**
	  * deleteUsuario : Elimina un usuario
	  * */
	public boolean deleteUsuario(TblUsuario usuario) {
		logger.debug("[deleteUsuario] Inicio");
		boolean resultado = false;
		try{
			usuarioDao.delete(usuario);
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
			resultado = false;
			logger.debug("[deleteUsuario] Error:"+e.getMessage());
		}
		logger.debug("[deleteUsuario] Fin");
		return resultado;
				
	}
	
	/**
	  * getUsuarioByNombreEstado : Lista todos los usuarios por nombre y estado
	  * */
	public List<TblUsuario> getUsuarioByNombreEstado(TblUsuario usuario) {
		logger.debug("[getUsuarioByNombreEstado] Inicio");
		List<TblUsuario> listaUsuario = null;
		try{
			listaUsuario = usuarioDao.findByNombreEstado(usuario.getLogin(), usuario.getEstado());
		}catch(Exception e){
			listaUsuario = null;
		}
		logger.debug("[getUsuarioByNombreEstado] Fin");
		return listaUsuario;
	}
	/**
	  * getUsuarioByFiltros : Lista todos los usuarios por Filtros
	  * */
	/*public List<TblUsuario> getUsuarioByFiltros(TblUsuario usuario) {
		logger.debug("[getUsuarioByFiltros] Inicio");
		List<TblUsuario> listaUsuario = null;
		try{
			listaUsuario = usuarioDao.listarConFiltros(usuario.getLogin(), usuario.getNombre(), usuario.getEstado());
		}catch(Exception e){
			listaUsuario = null;
		}
		logger.debug("[getUsuarioByFiltros] Fin");
		return listaUsuario;
	}	*/
	
	/**
	  * getUsuarioByFiltros : Lista todos los usuarios por Filtros
	  * */
	public List<TblUsuario> getUsuarioByFiltros(String strDato) {
		logger.debug("[getUsuarioByFiltros] Inicio");
		List<TblUsuario> listaUsuario = null;
		try{
			listaUsuario = usuarioDao.listarConFiltros(strDato);
		}catch(Exception e){
			listaUsuario = null;
		}
		logger.debug("[getUsuarioByFiltros] Fin");
		return listaUsuario;
	}	

}
