package com.pe.lima.sg.service.seguridad;

import java.util.List;


/**
 * 
 * @author: Gregorio Rodriguez P.
 * @Clase: PerfilOpcionService
 * @Descripcion: Implementacion de los metodos de la interfaz de PerfilOpcionService
 *
 */



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pe.lima.sg.dao.seguridad.IPerfilOpcionDAO;
import com.pe.lima.sg.entity.seguridad.TblPerfilOpcion;
import com.pe.lima.sg.entity.seguridad.TblPerfilOpcionId;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class PerfilOpcionService  {
	@Autowired
	private IPerfilOpcionDAO perfilOpcionDao;
	
	
	 /**
	  * getAllPerfilOpcions : Lista todos los perfilOpcions
	  * */
	public List<TblPerfilOpcion> getAllPerfilOpcions() {
		log.debug("[getAllPerfilOpcions] Inicio");
		List<TblPerfilOpcion> listaPerfilOpcion = null;
		try{
			listaPerfilOpcion = perfilOpcionDao.findAll();
		}catch(Exception e){
			listaPerfilOpcion = null;
		}
		log.debug("[getAllPerfilOpcions] Fin");
		return listaPerfilOpcion;
	}

	 /**
	  * getPerfilOpcionById : Obtiene un perfilOpcion
	  * */
	public TblPerfilOpcion getPerfilOpcionById(TblPerfilOpcionId id) {
		log.debug("[getPerfilOpcionById] Inicio");
		TblPerfilOpcion perfilOpcion = null;
		try{
			//perfilOpcion = perfilOpcionDao.findOne( id);
		}catch(Exception e){
			e.printStackTrace();
			perfilOpcion = null;
		}
		log.debug("[getPerfilOpcionById] Fin");
		return perfilOpcion;
	}

	/**
	  * addPerfilOpcion : Registra un perfilOpcion
	  * */
	public boolean addPerfilOpcion(TblPerfilOpcion perfilOpcion) {
		log.debug("[addPerfilOpcion] Inicio");
		boolean resultado = false;
		try{
			perfilOpcionDao.save(perfilOpcion);
       	 resultado = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		log.debug("[addPerfilOpcion] Fin:"+resultado);
		return resultado;
	}

	/**
	  * addPerfilOpcion : Actualiza un perfilOpcion
	  * */
	public boolean updatePerfilOpcion(TblPerfilOpcion perfilOpcion) {
		log.debug("[updatePerfilOpcion] Inicio");
		boolean resultado = false;
		try{
			perfilOpcionDao.save(perfilOpcion);
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
			resultado = false;
		}
		log.debug("[updatePerfilOpcion] Fin:"+resultado);
		return resultado;
	}

	/**
	  * deletePerfilOpcion : Elimina un perfilOpcion
	  * */
	public boolean deletePerfilOpcion(TblPerfilOpcion perfilOpcion) {
		log.debug("[deletePerfilOpcion] Inicio");
		boolean resultado = false;
		try{
			perfilOpcionDao.delete(perfilOpcion);
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
			resultado = false;
		}
		log.debug("[deletePerfilOpcion] Fin:"+resultado);
		return resultado;
				
	}

}
