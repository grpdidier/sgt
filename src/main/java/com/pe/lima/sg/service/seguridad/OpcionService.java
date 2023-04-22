package com.pe.lima.sg.service.seguridad;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
@Service
public class OpcionService  {
	private static final Logger logger = LogManager.getLogger(OpcionService.class);
	@Autowired
	private IOpcionDAO opcionDao;
	
	
	 /**
	  * getAllOpcionSistemas : Lista todos las opcion
	  * */
	public List<TblOpcion> getAllOpcion() {
		logger.debug("[getAllOpcion] Inicio");
		List<TblOpcion> listaOpcionSistema = null;
		try{
			listaOpcionSistema = opcionDao.findAll();
		}catch(Exception e){
			listaOpcionSistema = null;
		}
		logger.debug("[getAllOpcion] Fin");
		return listaOpcionSistema;
	}

	 /**
	  * getOpcionSistemaById : Obtiene una opcion
	  * */
	public TblOpcion getOpcionById(Integer id) {
		logger.debug("[getOpcionById] Inicio");
		TblOpcion opcion = null;
		try{
			opcion = opcionDao.findOne(id);
		}catch(Exception e){
			e.printStackTrace();
			opcion = null;
		}
		logger.debug("[getOpcionById] Fin");
		return opcion;
	}

	/**
	  * addopcion : Registra una opcion
	  * */
	public boolean addOpcion(TblOpcion opcion) {
		logger.debug("[addOpcion] Inicio");
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
		logger.debug("[addOpcion] Fin:"+resultado);
		return resultado;
	}

	/**
	  * addopcion : Actualiza una opcion
	  * */
	public boolean updateOpcion(TblOpcion opcion) {
		logger.debug("[updateOpcion] Inicio");
		boolean resultado = false;
		try{
			opcionDao.save(opcion);
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
			resultado = false;
		}
		logger.debug("[updateOpcion] Fin:"+resultado);
		return resultado;
	}

	/**
	  * deleteOpcionSistema : Elimina una opcion
	  * */
	public boolean deleteOpcion(TblOpcion opcion) {
		logger.debug("[deleteOpcion] Inicio");
		boolean resultado = false;
		try{
			opcionDao.delete(opcion);
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
			resultado = false;
		}
		logger.debug("[deleteOpcion] Fin:"+resultado);
		return resultado;
				
	}

}
