package com.pe.lima.sg.service.mantenimiento;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author: Gregorio Rodriguez P.
 * @Clase: SuministroService
 * @Descripcion: Implementacion de los metodos de la interfaz de SuministroService
 *
 */



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pe.lima.sg.dao.mantenimiento.ISuministroDAO;
import com.pe.lima.sg.entity.mantenimiento.TblSuministro;
@Service
public class SuministroService  {
	private static final Logger logger = LogManager.getLogger(SuministroService.class);
	@Autowired
	private ISuministroDAO suministroDao;
	
	 /**
	  * getAllSuministro : Lista todos los suministros
	  * */
	public List<TblSuministro> getAllSuministro() {
		logger.debug("[getAllSuministro] Inicio");
		List<TblSuministro> listaSuministro = null;
		try{
			listaSuministro = suministroDao.findAll();
		}catch(Exception e){
			listaSuministro = null;
		}
		logger.debug("[getAllSuministro] Fin");
		return listaSuministro;
	}

	 /**
	  * getSuministroById : Obtiene un Suministro
	  * */
	public TblSuministro getSuministroById(Integer id) {
		logger.debug("[getSuministroById] Inicio");
		TblSuministro suministro = null;
		try{
			suministro = suministroDao.findOne(id);
		}catch(Exception e){
			e.printStackTrace();
			suministro = null;
		}
		logger.debug("[getSuministroById] Fin");
		return suministro;
	}

	/**
	  * addSuministro : Registra un Suministro
	  * */
	public boolean addSuministro(TblSuministro suministro) {
		logger.debug("[addSuministro] Inicio");
		List<TblSuministro>  lista = null;
		boolean resultado = false;
		try{
			lista = suministroDao.findByNumeroEstado(suministro.getNumero(), suministro.getEstado());
			 if (lista != null && lista.size() > 0) {
				 logger.debug("[addSuministro] Existe el elemento:"+lista.size());
				 resultado = false;
	         } else {
	        	 suministroDao.save(suministro);
	        	 resultado = true;
	         }
		}catch(Exception e){
			e.printStackTrace();
			logger.debug("[addSuministro] Error:"+e.getMessage());
		}
		logger.debug("[addSuministro] Fin");
		return resultado;
	}

	/**
	  * updateSuministro : Actualiza un Suministro
	  * */
	public boolean updateSuministro(TblSuministro suministro) {
		logger.debug("[updateSuministro] Inicio");
		boolean resultado = false;
		try{
			suministroDao.save(suministro);
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
			logger.debug("[updateSuministro] Error:"+e.getMessage());
			resultado = false;
		}
		logger.debug("[updateSuministro] Fin:"+resultado);
		return resultado;
	}

	/**
	  * deleteSuministro : Elimina un suministro
	  * */
	public boolean deleteSuministro(TblSuministro suministro) {
		logger.debug("[updateSuministro] Inicio");
		boolean resultado = false;
		try{
			suministroDao.delete(suministro);
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
			logger.debug("[updateSuministro] Error:"+e.getMessage());
			resultado = false;
		}
		logger.debug("[updateSuministro] Fin:"+resultado);
		return resultado;
				
	}
	
}
