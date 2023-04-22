package com.pe.lima.sg.service.seguridad;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author: Gregorio Rodriguez P.
 * @Clase: PerfilService
 * @Descripcion: Implementacion de los metodos de la interfaz de PerfilService
 *
 */



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pe.lima.sg.dao.seguridad.IPerfilDAO;
import com.pe.lima.sg.entity.seguridad.TblPerfil;
@Service
public class PerfilService  {
	private static final Logger logger = LogManager.getLogger(PerfilService.class);
	@Autowired
	private IPerfilDAO perfilDao;
	
	 /**
	  * getAllPerfil : Lista todos los perfiles
	  * */
	public List<TblPerfil> getAllPerfil() {
		logger.debug("[getAllPerfil] Inicio");
		List<TblPerfil> listaPerfil = null;
		try{
			listaPerfil = perfilDao.findAll();
		}catch(Exception e){
			listaPerfil = null;
		}
		logger.debug("[getAllPerfil] Fin");
		return listaPerfil;
	}

	 /**
	  * getPerfilById : Obtiene un Perfil
	  * */
	public TblPerfil getPerfilById(Integer id) {
		logger.debug("[getPerfilById] Inicio");
		TblPerfil perfil = null;
		try{
			perfil = perfilDao.findOne(id);
		}catch(Exception e){
			e.printStackTrace();
			perfil = null;
		}
		logger.debug("[getPerfilById] Fin");
		return perfil;
	}

	/**
	  * addPerfil : Registra un Perfil
	  * */
	public boolean addPerfil(TblPerfil perfil) {
		logger.debug("[addPerfil] Inicio");
		List<TblPerfil>  lista = null;
		boolean resultado = false;
		try{
			lista = perfilDao.findByNombreEstado(perfil.getNombre(), perfil.getEstado());
			 if (lista != null && lista.size() > 0) {
				 logger.debug("[addPerfil] Existe el elemento:"+lista.size());
				 resultado = false;
	         } else {
	        	 perfilDao.save(perfil);
	        	 resultado = true;
	         }
		}catch(Exception e){
			e.printStackTrace();
			logger.debug("[addPerfil] Error:"+e.getMessage());
		}
		logger.debug("[addPerfil] Fin");
		return resultado;
	}

	/**
	  * updatePerfil : Actualiza un Perfil
	  * */
	public boolean updatePerfil(TblPerfil perfil) {
		logger.debug("[updatePerfil] Inicio");
		boolean resultado = false;
		try{
			perfilDao.save(perfil);
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
			logger.debug("[updatePerfil] Error:"+e.getMessage());
			resultado = false;
		}
		logger.debug("[updatePerfil] Fin:"+resultado);
		return resultado;
	}

	/**
	  * deletePerfil : Elimina un perfil
	  * */
	public boolean deletePerfil(TblPerfil perfil) {
		logger.debug("[updatePerfil] Inicio");
		boolean resultado = false;
		try{
			perfilDao.delete(perfil);
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
			logger.debug("[updatePerfil] Error:"+e.getMessage());
			resultado = false;
		}
		logger.debug("[updatePerfil] Fin:"+resultado);
		return resultado;
				
	}
	/**
	  * getPerfilByNombreEstado : Lista todos los perfiles por nombre y estado
	  * */
	public List<TblPerfil> getPerfilByNombreEstado(TblPerfil perfil) {
		logger.debug("[getUsuarioByNombreEstado] Inicio");
		List<TblPerfil> listaPerfil = null;
		try{
			listaPerfil = perfilDao.findByNombreEstado(perfil.getNombre(), perfil.getEstado());
		}catch(Exception e){
			listaPerfil = null;
		}
		logger.debug("[getUsuarioByNombreEstado] Fin");
		return listaPerfil;
	}

}
