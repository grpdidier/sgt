package com.pe.lima.sg.service.seguridad;

import java.util.List;


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

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class PerfilService  {
	@Autowired
	private IPerfilDAO perfilDao;
	
	 /**
	  * getAllPerfil : Lista todos los perfiles
	  * */
	public List<TblPerfil> getAllPerfil() {
		log.debug("[getAllPerfil] Inicio");
		List<TblPerfil> listaPerfil = null;
		try{
			listaPerfil = perfilDao.findAll();
		}catch(Exception e){
			listaPerfil = null;
		}
		log.debug("[getAllPerfil] Fin");
		return listaPerfil;
	}

	 /**
	  * getPerfilById : Obtiene un Perfil
	  * */
	public TblPerfil getPerfilById(Integer id) {
		log.debug("[getPerfilById] Inicio");
		TblPerfil perfil = null;
		try{
			perfil = perfilDao.findOne(id);
		}catch(Exception e){
			e.printStackTrace();
			perfil = null;
		}
		log.debug("[getPerfilById] Fin");
		return perfil;
	}

	/**
	  * addPerfil : Registra un Perfil
	  * */
	public boolean addPerfil(TblPerfil perfil) {
		log.debug("[addPerfil] Inicio");
		List<TblPerfil>  lista = null;
		boolean resultado = false;
		try{
			lista = perfilDao.findByNombreEstado(perfil.getNombre(), perfil.getEstado());
			 if (lista != null && lista.size() > 0) {
				 log.debug("[addPerfil] Existe el elemento:"+lista.size());
				 resultado = false;
	         } else {
	        	 perfilDao.save(perfil);
	        	 resultado = true;
	         }
		}catch(Exception e){
			e.printStackTrace();
			log.debug("[addPerfil] Error:"+e.getMessage());
		}
		log.debug("[addPerfil] Fin");
		return resultado;
	}

	/**
	  * updatePerfil : Actualiza un Perfil
	  * */
	public boolean updatePerfil(TblPerfil perfil) {
		log.debug("[updatePerfil] Inicio");
		boolean resultado = false;
		try{
			perfilDao.save(perfil);
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
			log.debug("[updatePerfil] Error:"+e.getMessage());
			resultado = false;
		}
		log.debug("[updatePerfil] Fin:"+resultado);
		return resultado;
	}

	/**
	  * deletePerfil : Elimina un perfil
	  * */
	public boolean deletePerfil(TblPerfil perfil) {
		log.debug("[updatePerfil] Inicio");
		boolean resultado = false;
		try{
			perfilDao.delete(perfil);
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
			log.debug("[updatePerfil] Error:"+e.getMessage());
			resultado = false;
		}
		log.debug("[updatePerfil] Fin:"+resultado);
		return resultado;
				
	}
	/**
	  * getPerfilByNombreEstado : Lista todos los perfiles por nombre y estado
	  * */
	public List<TblPerfil> getPerfilByNombreEstado(TblPerfil perfil) {
		log.debug("[getUsuarioByNombreEstado] Inicio");
		List<TblPerfil> listaPerfil = null;
		try{
			listaPerfil = perfilDao.findByNombreEstado(perfil.getNombre(), perfil.getEstado());
		}catch(Exception e){
			listaPerfil = null;
		}
		log.debug("[getUsuarioByNombreEstado] Fin");
		return listaPerfil;
	}

}
