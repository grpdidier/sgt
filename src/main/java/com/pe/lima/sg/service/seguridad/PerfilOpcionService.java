package com.pe.lima.sg.service.seguridad;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
@Service
public class PerfilOpcionService  {
	private static final Logger logger = LogManager.getLogger(PerfilOpcionService.class);
	@Autowired
	private IPerfilOpcionDAO perfilOpcionDao;
	
	
	 /**
	  * getAllPerfilOpcions : Lista todos los perfilOpcions
	  * */
	public List<TblPerfilOpcion> getAllPerfilOpcions() {
		logger.debug("[getAllPerfilOpcions] Inicio");
		List<TblPerfilOpcion> listaPerfilOpcion = null;
		try{
			listaPerfilOpcion = perfilOpcionDao.findAll();
		}catch(Exception e){
			listaPerfilOpcion = null;
		}
		logger.debug("[getAllPerfilOpcions] Fin");
		return listaPerfilOpcion;
	}

	 /**
	  * getPerfilOpcionById : Obtiene un perfilOpcion
	  * */
	public TblPerfilOpcion getPerfilOpcionById(TblPerfilOpcionId id) {
		logger.debug("[getPerfilOpcionById] Inicio");
		TblPerfilOpcion perfilOpcion = null;
		try{
			//perfilOpcion = perfilOpcionDao.findOne( id);
		}catch(Exception e){
			e.printStackTrace();
			perfilOpcion = null;
		}
		logger.debug("[getPerfilOpcionById] Fin");
		return perfilOpcion;
	}

	/**
	  * addPerfilOpcion : Registra un perfilOpcion
	  * */
	public boolean addPerfilOpcion(TblPerfilOpcion perfilOpcion) {
		logger.debug("[addPerfilOpcion] Inicio");
		boolean resultado = false;
		try{
			perfilOpcionDao.save(perfilOpcion);
       	 resultado = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		logger.debug("[addPerfilOpcion] Fin:"+resultado);
		return resultado;
	}

	/**
	  * addPerfilOpcion : Actualiza un perfilOpcion
	  * */
	public boolean updatePerfilOpcion(TblPerfilOpcion perfilOpcion) {
		logger.debug("[updatePerfilOpcion] Inicio");
		boolean resultado = false;
		try{
			perfilOpcionDao.save(perfilOpcion);
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
			resultado = false;
		}
		logger.debug("[updatePerfilOpcion] Fin:"+resultado);
		return resultado;
	}

	/**
	  * deletePerfilOpcion : Elimina un perfilOpcion
	  * */
	public boolean deletePerfilOpcion(TblPerfilOpcion perfilOpcion) {
		logger.debug("[deletePerfilOpcion] Inicio");
		boolean resultado = false;
		try{
			perfilOpcionDao.delete(perfilOpcion);
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
			resultado = false;
		}
		logger.debug("[deletePerfilOpcion] Fin:"+resultado);
		return resultado;
				
	}

}
