package com.pe.lima.sg.service.seguridad;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author: Gregorio Rodriguez P.
 * @Clase: IntentoService
 * @Descripcion: Implementacion de los metodos de la interfaz de IntentoService
 *
 */



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pe.lima.sg.dao.seguridad.IIntentoDAO;
import com.pe.lima.sg.entity.seguridad.TblIntento;
@Service
public class IntentoService  {
	private static final Logger logger = LogManager.getLogger(IntentoService.class);
	@Autowired
	private IIntentoDAO intentoDao;
	
	
	 /**
	  * getAllIntentos : Lista todos los intentos
	  * */
	public List<TblIntento> getAllIntentos() {
		logger.debug("[getAllIntentos] Inicio");
		List<TblIntento> listaIntento = null;
		try{
			listaIntento = intentoDao.findAll();
		}catch(Exception e){
			listaIntento = null;
		}
		logger.debug("[getAllIntentos] Fin");
		return listaIntento;
	}

	 /**
	  * getIntentoById : Obtiene un intento
	  * */
	public TblIntento getIntentoById(Integer id) {
		logger.debug("[getIntentoById] Inicio");
		TblIntento intento = null;
		try{
			intento = intentoDao.findOne(id);
		}catch(Exception e){
			e.printStackTrace();
			intento = null;
		}
		logger.debug("[getIntentoById] Fin");
		return intento;
	}

	/**
	  * addIntento : Registra un intento
	  * */
	public boolean addIntento(TblIntento intento) {
		logger.debug("[addIntento] Inicio");
		boolean resultado = false;
		try{
			intentoDao.save(intento);
       	 resultado = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		logger.debug("[addIntento] Fin");
		return resultado;
	}

	/**
	  * addIntento : Actualiza un intento
	  * */
	public boolean updateIntento(TblIntento intento) {
		logger.debug("[updateIntento] Inicio");
		boolean resultado = false;
		try{
			intentoDao.save(intento);
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
			resultado = false;
		}
		logger.debug("[updateIntento] Fin:"+resultado);
		return resultado;
	}

	/**
	  * deleteIntento : Elimina un intento
	  * */
	public boolean deleteIntento(TblIntento intento) {
		logger.debug("[deleteIntento] Inicio");
		boolean resultado = false;
		try{
			intentoDao.delete(intento);
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
			resultado = false;
		}
		logger.debug("[deleteIntento] Fin:"+resultado);
		return resultado;
				
	}

}
