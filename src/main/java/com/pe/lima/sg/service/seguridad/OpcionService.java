package com.pe.lima.sg.service.seguridad;

import java.util.List;


/**
 * 
 * @author: Gregorio Rodriguez P.
 * @Clase: OpcionSistemaService
 * @Descripcion: Implementacion de los metodos de la interfaz de OpcionService
 *
 */



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pe.lima.sg.dao.seguridad.IOpcionDAO;
import com.pe.lima.sg.entity.seguridad.TblOpcion;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class OpcionService  {
	@Autowired
	private IOpcionDAO opcionDao;
	
	
	 /**
	  * getAllOpcionSistemas : Lista todos las opcion
	  * */
	public List<TblOpcion> getAllOpcion() {
		log.debug("[getAllOpcion] Inicio");
		List<TblOpcion> listaOpcionSistema = null;
		try{
			listaOpcionSistema = opcionDao.findAll();
		}catch(Exception e){
			listaOpcionSistema = null;
		}
		log.debug("[getAllOpcion] Fin");
		return listaOpcionSistema;
	}

	 /**
	  * getOpcionSistemaById : Obtiene una opcion
	  * */
	public TblOpcion getOpcionById(Integer id) {
		log.debug("[getOpcionById] Inicio");
		TblOpcion opcion = null;
		try{
			opcion = opcionDao.findOne(id);
		}catch(Exception e){
			e.printStackTrace();
			opcion = null;
		}
		log.debug("[getOpcionById] Fin");
		return opcion;
	}

	/**
	  * addopcion : Registra una opcion
	  * */
	public boolean addOpcion(TblOpcion opcion) {
		log.debug("[addOpcion] Inicio");
		List<TblOpcion>  lista = null;
		boolean resultado = false;
		try{
			lista = opcionDao.findByNombreEstado(opcion.getNombre(), opcion.getEstado());
			 if (lista != null && lista.size() > 0) {
				 resultado = false;
	         } else {
	        	 opcionDao.save(opcion);
	        	 resultado = true;
	         }
		}catch(Exception e){
			e.printStackTrace();
		}
		log.debug("[addOpcion] Fin:"+resultado);
		return resultado;
	}

	/**
	  * addopcion : Actualiza una opcion
	  * */
	public boolean updateOpcion(TblOpcion opcion) {
		log.debug("[updateOpcion] Inicio");
		boolean resultado = false;
		try{
			opcionDao.save(opcion);
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
			resultado = false;
		}
		log.debug("[updateOpcion] Fin:"+resultado);
		return resultado;
	}

	/**
	  * deleteOpcionSistema : Elimina una opcion
	  * */
	public boolean deleteOpcion(TblOpcion opcion) {
		log.debug("[deleteOpcion] Inicio");
		boolean resultado = false;
		try{
			opcionDao.delete(opcion);
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
			resultado = false;
		}
		log.debug("[deleteOpcion] Fin:"+resultado);
		return resultado;
				
	}

}
