package com.pe.lima.sg.presentacion.migracion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.pe.lima.sg.dao.mantenimiento.IPersonaDAO;
import com.pe.lima.sg.dao.migracion.IMantenimientoDAO;
import com.pe.lima.sg.entity.mantenimiento.TblPersona;

@Controller
public class MantenimientoAction {

	@Autowired
	private IMantenimientoDAO mantenimientoDAO;
	@Autowired
	private IPersonaDAO personaDAO;
	
	@GetMapping("/migracion/persona")
	public String migrarPersona(){
		String resultado = null;
		Integer total = 0;
		//mantenimientoDAO.abrirConexion();
		List<TblPersona> personas = mantenimientoDAO.listarPersona();
		if(personas!=null){
			for(TblPersona persona: personas){
				personaDAO.save(persona);
				total++;
			}
		}
		resultado = "Total registros insertados " + total;
		return resultado;
	}
}
