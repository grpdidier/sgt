package com.pe.lima.sg.dao.mantenimiento;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseDAO;
import com.pe.lima.sg.entity.mantenimiento.TblPersona;

public interface IPersonaDAO extends BaseDAO<TblPersona, Integer> {
	
	@Query(value = "select * from mae.tbl_persona where ( UPPER(nombre) LIKE '%'||UPPER(:nombre)||'%' OR :nombre IS NULL ) AND (estado = :estado OR :estado IS NULL ) ORDER BY tipo_persona, nombre, paterno, materno", nativeQuery = true)
	List<TblPersona> findByNombreEstado(
										@Param("nombre") String strNombre,
										@Param("estado") Character chrEstado);
	
	@Query(value = "select * from mae.tbl_persona where ( UPPER(nombre) LIKE '%'||UPPER(:filtro)||'%' OR :filtro IS NULL ) AND ( UPPER(paterno) LIKE '%'||UPPER(:filtro)||'%' OR :filtro IS NULL ) AND ( UPPER(materno) LIKE '%'||UPPER(:filtro)||'%' OR :filtro IS NULL ) AND ( UPPER(numeroDni) LIKE '%'||UPPER(:filtro)||'%' OR :filtro IS NULL )  AND ( UPPER(numeroRuc) LIKE '%'||UPPER(:filtro)||'%' OR :filtro IS NULL ) AND ( UPPER(razonSocial) LIKE '%'||UPPER(:filtro)||'%' OR :filtro IS NULL ) AND (estado = :filtro OR :filtro IS NULL )  ORDER BY tipo_persona, nombre, paterno, materno", nativeQuery = true)
	List<TblPersona> listarConFiltros(@Param("filtro") String filtro);

	/*@Query(value = "select * from mae.parametro where ( UPPER(nombre) LIKE '%'||UPPER(:nombre)||'%' OR :nombre IS NULL ) AND ( UPPER(paterno) LIKE '%'||UPPER(:paterno)||'%' OR :paterno IS NULL ) AND ( UPPER(materno) LIKE '%'||UPPER(:materno)||'%' OR :materno IS NULL ) AND ( UPPER(numeroDni) LIKE '%'||UPPER(:numeroDni)||'%' OR :numeroDni IS NULL )  AND ( UPPER(numeroRuc) LIKE '%'||UPPER(:numeroRuc)||'%' OR :numeroRuc IS NULL ) AND ( UPPER(razonSocial) LIKE '%'||UPPER(:razonSocial)||'%' OR :razonSocial IS NULL ) AND (estado = :estado OR :estado IS NULL ) ORDER BY 1", nativeQuery = true)
	List<TblPersona> listarConFiltros(
										@Param("nombre") String strNombre,
										@Param("paterno") String strPaterno,
										@Param("materno") String strMaterno,
										@Param("numeroDni") String strDni,
										@Param("numeroRuc") String strRuc,
										@Param("razonSocial") String strRazonSocial,
										@Param("estado") Character chrEstado);*/
	
	@Query(value = "select * from mae.tbl_persona where ( trim(UPPER(nombre)) LIKE '%'|| trim(UPPER(:nombre)) ||'%' OR :nombre IS NULL ) AND ( trim(UPPER(paterno)) LIKE '%'|| trim(UPPER(:paterno))||'%' OR :paterno IS NULL ) AND ( trim(UPPER(materno)) LIKE '%'|| trim(UPPER(:materno)) ||'%' OR :materno IS NULL ) AND ( trim(UPPER(numero_dni)) LIKE '%'|| trim(UPPER(:numeroDni))||'%' OR :numeroDni IS NULL )  AND ( trim(UPPER(numero_ruc)) LIKE '%'||UPPER(:numeroRuc)||'%' OR :numeroRuc IS NULL ) AND ( trim(UPPER(razon_social)) LIKE '%'|| trim(UPPER(:razonSocial)) ||'%' OR :razonSocial IS NULL ) AND estado = '1'  ORDER BY tipo_persona, nombre, paterno, materno", nativeQuery = true)
	List<TblPersona> listarCriterios(	
										@Param("nombre") String strNombre,
										@Param("paterno") String strPaterno,
										@Param("materno") String strMaterno,
										@Param("numeroDni") String strDni,
										@Param("numeroRuc") String strRuc,
										@Param("razonSocial") String strRazonSocial);
	
	@Query(value = "select * from mae.tbl_persona where estado = '1'  ORDER BY tipo_persona, nombre, paterno, materno", nativeQuery = true)
	List<TblPersona> listarAllActivos();
	
	@Query(value = "select count(1) from mae.tbl_persona where ( TRIM(UPPER(numero_dni)) = TRIM(UPPER(:dni))  AND  TRIM(UPPER(numero_ruc)) = TRIM(UPPER(:ruc)) ) AND estado = '1' ", nativeQuery = true)
	Integer countOneByDniRuc(@Param("dni") String strDni,
							 @Param("ruc") String strRuc);
	
	@Query(value = "select * from mae.tbl_persona where ( TRIM(UPPER(numero_dni)) = TRIM(UPPER(:dni))  OR  TRIM(UPPER(numero_ruc)) = TRIM(UPPER(:ruc)) ) AND estado = '1'  ORDER BY tipo_persona, nombre, paterno, materno", nativeQuery = true)
	List<TblPersona> buscarOneByDniRuc(	@Param("dni") String strDni,
			 							@Param("ruc") String strRuc);

	@Query(value = "select * from mae.tbl_persona p, cli.tbl_contrato_cliente c where p.codigo_persona = c.codigo_persona AND c.codigo_contrato = :codigoContrato AND p.estado = '1'  ORDER BY tipo_persona, nombre, paterno, materno", nativeQuery = true)
	List<TblPersona> listarPersonasxContrato(@Param("codigoContrato") Integer intCodigoContrato);
}
