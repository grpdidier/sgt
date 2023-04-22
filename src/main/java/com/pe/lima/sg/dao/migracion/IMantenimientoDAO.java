package com.pe.lima.sg.dao.migracion;

import java.util.List;

import org.springframework.stereotype.Component;

import com.pe.lima.sg.entity.mantenimiento.TblPersona;

@Component
public interface IMantenimientoDAO {
	
	
	public void abrirConexion();
	public List<TblPersona> listarPersona();
}
