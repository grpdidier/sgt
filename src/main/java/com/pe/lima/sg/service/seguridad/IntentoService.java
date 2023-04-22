package com.pe.lima.sg.service.seguridad;

import java.util.List;


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

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class IntentoService  {
	@Autowired
	private IIntentoDAO intentoDao;
	
	
	 /**
	  * getAllIntentos : Lista todos los intentos
	  * */
	public List<TblIntento> getAllIntentos() {
		log.debug("[getAllIntentos] Inicio");
		List<TblIntento> listaIntento = null;
		try{
			listaIntento = intentoDao.findAll();
		}catch(Exception e){
			listaIntento = null;
		}
		log.debug("[getAllIntentos] Fin");
		return listaIntento;
	}

	 /**
	  * getIntentoById : Obtiene un intento
	  * */
	public TblIntento getIntentoById(Integer id) {
		log.debug("[getIntentoById] Inicio");
		TblIntento intento = null;
		try{
			intento = intentoDao.findOne(id);
		}catch(Exception e){
			e.printStackTrace();
			intento = null;
		}
		log.debug("[getIntentoById] Fin");
		return intento;
	}

	/**
	  * addIntento : Registra un intento
	  * */
	public boolean addIntento(TblIntento intento) {
		log.debug("[addIntento] Inicio");
		boolean resultado = false;
		try{
			intentoDao.save(intento);
       	 resultado = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		log.debug("[addIntento] Fin");
		return resultado;
	}

	/**
	  * addIntento : Actualiza un intento
	  * */
	public boolean updateIntento(TblIntento intento) {
		log.debug("[updateIntento] Inicio");
		boolean resultado = false;
		try{
			intentoDao.save(intento);
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
			resultado = false;
		}
		log.debug("[updateIntento] Fin:"+resultado);
		return resultado;
	}

	/**
	  * deleteIntento : Elimina un intento
	  * */
	public boolean deleteIntento(TblIntento intento) {
		log.debug("[deleteIntento] Inicio");
		boolean resultado = false;
		try{
			intentoDao.delete(intento);
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
			resultado = false;
		}
		log.debug("[deleteIntento] Fin:"+resultado);
		return resultado;
				
	}

}
