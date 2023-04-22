package com.pe.lima.sg.service.seguridad;

import java.util.List;


/**
 * 
 * @author: Gregorio Rodriguez P.
 * @Clase: AccesoService
 * @Descripcion: Implementacion de los metodos de la interfaz de AccesoService
 *
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pe.lima.sg.dao.seguridad.IAccesoDAO;
import com.pe.lima.sg.entity.seguridad.TblAcceso;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class AccesoService  {
	@Autowired
	private IAccesoDAO accesoDao;
	
	 /**
	  * getAllAccesos : Lista todos los accesos
	  * */
	public List<TblAcceso> getAllAccesos() {
		log.debug("[getAllAccesos] Inicio");
		List<TblAcceso> listaAcceso = null;
		try{
			listaAcceso = accesoDao.findAll();
		}catch(Exception e){
			listaAcceso = null;
		}
		log.debug("[getAllAccesos] Fin");
		return listaAcceso;
	}

	 /**
	  * getAccesoById : Obtiene un acceso
	  * */
	public TblAcceso getAccesoById(Integer id) {
		log.debug("[getAccesoById] Inicio");
		TblAcceso acceso = null;
		try{
			acceso = accesoDao.findOne(id);
		}catch(Exception e){
			e.printStackTrace();
			acceso = null;
		}
		log.debug("[getAccesoById] Fin");
		return acceso;
	}

	/**
	  * addAcceso : Registra un acceso
	  * */
	public boolean addAcceso(TblAcceso acceso) {
		log.debug("[addAcceso] Inicio");
		boolean resultado = false;
		try{
			accesoDao.save(acceso);
       	 resultado = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		log.debug("[addAcceso] Fin:"+resultado);
		return resultado;
	}

	/**
	  * addAcceso : Actualiza un acceso
	  * */
	public boolean updateAcceso(TblAcceso acceso) {
		log.debug("[updateAcceso] Inicio");
		boolean resultado = false;
		try{
			accesoDao.save(acceso);
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
			resultado = false;
		}
		log.debug("[updateAcceso] Fin:"+resultado);
		return resultado;
	}

	/**
	  * deleteAcceso : Elimina un acceso
	  * */
	public boolean deleteAcceso(TblAcceso acceso) {
		log.debug("[deleteAcceso] Inicio");
		boolean resultado = false;
		try{
			accesoDao.delete(acceso);
			resultado = true;
		}catch(Exception e){
			e.printStackTrace();
			resultado = false;
		}
		log.debug("[deleteAcceso] Fin:"+resultado);
		return resultado;
				
	}

}
