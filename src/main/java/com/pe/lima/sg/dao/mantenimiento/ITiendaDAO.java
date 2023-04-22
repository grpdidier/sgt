package com.pe.lima.sg.dao.mantenimiento;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseDAO;
import com.pe.lima.sg.entity.mantenimiento.TblTienda;

public interface ITiendaDAO extends BaseDAO<TblTienda, Integer> {
	
	@Query(value = "select * from mae.tbl_tienda where (codigoEdificio =:codigoEdificio ) AND ( UPPER(numero) LIKE '%'||UPPER(:numero)||'%' OR :numero IS NULL ) AND (estado = :estado OR :estado IS NULL ) ORDER BY 1", nativeQuery = true)
	List<TblTienda> findByNombreEstado(
										@Param("numero") String strNumero,
										@Param("codigoEdificio") Integer intCodigoEdificio,
										@Param("estado") Character chrEstado);

	@Query(value = "select * from mae.tbl_tienda where(codigoEdificio =:filtro ) AND ( UPPER(numero) LIKE '%'||UPPER(:filtro)||'%' OR :filtro IS NULL ) AND (estado = :filtro OR :filtro IS NULL ) ORDER BY 1", nativeQuery = true)
	List<TblTienda> listarConFiltros(@Param("filtro") String filtro);
	/*@Query(value = "select * from mae.tbl_tienda where(codigoEdificio =:codigoEdificio ) AND ( UPPER(numero) LIKE '%'||UPPER(:numero)||'%' OR :numero IS NULL ) AND (estado = :estado OR :estado IS NULL ) ORDER BY 1", nativeQuery = true)
	List<TblTienda> listarConFiltros(
										@Param("numero") String strNumero,
										@Param("piso") String strPiso,
										@Param("estado") Character chrEstado);*/
	
	@Query(value = "select * from mae.tbl_tienda where codigo_edificio =:codigoEdificio and ( UPPER(numero) LIKE '%'||UPPER(:numero)||'%' OR :numero IS NULL )  AND estado = '1' ORDER BY numero", nativeQuery = true)
	List<TblTienda> listarCriterios(	@Param("codigoEdificio") Integer intCodigoEdificio,
										@Param("numero") String strNumero);
	
	@Query(value = "select * from mae.tbl_tienda where codigo_edificio =:codigoEdificio and estado = '1'  ORDER BY numero", nativeQuery = true)
	List<TblTienda> listarAllActivos(@Param("codigoEdificio") Integer intCodigoEdificio);
	
	@Query(value = "select count(1) from mae.tbl_tienda where codigo_edificio =:codigoEdificio and TRIM(UPPER(numero)) = TRIM(UPPER(:numero)) AND estado = '1' ", nativeQuery = true)
	Integer countOneByNombre(@Param("codigoEdificio") Integer intCodigoEdificio,
							 @Param("numero") String strNumero);
	
	@Query(value = "select * from mae.tbl_tienda where codigo_edificio =:codigoEdificio and UPPER(numero) = UPPER(:numero) AND estado = '1'  ORDER BY numero", nativeQuery = true)
	List<TblTienda> buscarOneByNumero(@Param("codigoEdificio") Integer intCodigoEdificio,
									  @Param("numero") String strNumero);
	
	@Query(value = "select * from mae.tbl_tienda where estado_tienda ='DSC' and codigo_edificio =:codigoEdificio and ( UPPER(numero) LIKE '%'||UPPER(:numero)||'%' OR :numero IS NULL )  AND estado = '1' ORDER BY numero", nativeQuery = true)
	List<TblTienda> listarTiendaDesocupada(	@Param("codigoEdificio") Integer intCodigoEdificio,
											@Param("numero") String strNumero);
	
	@Query(value = "select * from mae.tbl_tienda where codigo_suministro = :codigoSuministro AND estado = '1' ORDER BY numero", nativeQuery = true)
	List<TblTienda> listarTiendaxSuministro(	@Param("codigoSuministro") Integer intCodigoSuministro);
	
	@Query(value = "select * from mae.tbl_tienda where codigo_edificio =:codigoEdificio  AND estado = '1' ORDER BY numero", nativeQuery = true)
	List<TblTienda> listarxInmueble(	@Param("codigoEdificio") Integer intCodigoEdificio);
	
}
