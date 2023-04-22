package com.pe.lima.sg.rs.reporte;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pe.lima.sg.bean.reporte.BancarizadoBean;
import com.pe.lima.sg.bean.reporte.ReporteBancarizadoBean;
import com.pe.lima.sg.presentacion.Filtro;
import com.pe.lima.sg.presentacion.util.Constantes;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class BancarizadoDao {
	@Value("${spring.datasource.url}")
	private String urlTienda;

	@Value("${spring.datasource.username}")
	private String nombreUsuario;

	@Value("${spring.datasource.password}")
	private String credencialUsuario;

	private final String userUrl	= "?user=";
	private final String userPass	= "&password=";

	private String url;

	/*Consulta a la bd para obtener los datos de los cobros y los ingresos*/
	private String sqlConsultaCobroIngreso(){
		String query = null;

		query = "SELECT usu.nombre, "+
				"       des.tipo_moneda, "+
				"       sum(des.monto_soles) as monto_soles, "+
				"       sum(des.monto_dolares) as monto_dolares, "+
				"       des.fecha_cobro, "+
				"       des.tipo_pago, "+
				"       tie.codigo_edificio,"+
				"       edi.nombre as inmueble "+
				"FROM   caj.tbl_desembolso des, "+
				"       seg.tbl_usuario usu, "+
				"       cli.tbl_contrato con, "+
				"       mae.tbl_tienda tie, "+
				"       mae.tbl_edificio edi "+
				"WHERE  des.usuario_creacion = usu.codigo_usuario "+
				"       AND des.codigo_contrato = con.codigo_contrato "+
				"       AND con.codigo_tienda = tie.codigo_tienda "+
				"       AND des.estado_operacion NOT IN ( 3 ) "+
				"       and tie.codigo_edificio = edi.codigo_edificio "+
				"       AND des.fecha_cobro BETWEEN ( ? :: DATE ) AND ( ? :: DATE ) "+
				"GROUP BY usu.nombre, "+
				"       des.tipo_moneda, "+
				"       des.fecha_cobro, "+
				"       des.tipo_pago, "+
				"       tie.codigo_edificio, "+
				"       edi.nombre"+
				" UNION "+
				"select usu.nombre, "+
				"       ing.tipo_moneda, "+
				"       sum(ing.monto) as monto_soles, "+
				"       sum(ing.monto) as monto_dolares, "+
				"       ing.fecha_ingreso as fecha_cobro, "+
				"       ing.tipo_pago as tipo_pago, "+
				"       edi.codigo_edificio, "+
				"       edi.nombre as inmueble "+
				"from caj.tbl_ingreso ing,  "+
				"     seg.tbl_usuario usu, "+
				"     mae.tbl_edificio edi "+
				"where ing.usuario_creacion = usu.codigo_usuario    "+
				"    and ing.estado = '1' "+
				"    and ing.codigo_edificio = edi.codigo_edificio "+
				"    and ing.fecha_ingreso BETWEEN ( ? :: DATE ) AND ( ? :: DATE )  "+
				"group by usu.nombre, "+
				"        ing.tipo_moneda,  "+
				"        ing.fecha_ingreso, ing.tipo_pago,"+
				"        edi.codigo_edificio, "+
				"        edi.nombre ";

		return query;
	}

	private String sqlConsultaGastos(){
		String query = null;
		query = "select usu.nombre, "+
				"       gas.tipo_moneda, "+
				"       sum(gas.monto) as monto_soles, "+
				"       sum(gas.monto) as monto_dolares, "+
				"       gas.fecha_gasto  as fecha_cobro, "+
				"       'EFE' as tipo_pago, "+
				"       edi.codigo_edificio, "+
				"       edi.nombre as inmueble "+
				"from caj.tbl_gasto gas, "+
				"     seg.tbl_usuario usu, "+
				"     mae.tbl_edificio edi "+
				"where gas.usuario_creacion = usu.codigo_usuario    "+
				"    and gas.estado = '1' "+
				"    and gas.codigo_edificio = edi.codigo_edificio "+
				"    and gas.fecha_gasto BETWEEN ( ? :: DATE ) AND ( ? :: DATE ) "+
				"group by usu.nombre, "+
				"        gas.tipo_moneda, "+
				"        gas.fecha_gasto, "+
				"        edi.codigo_edificio, "+
				"        edi.nombre ";
		return query;
	}

	/*Consulta de Cobros e Ingresos Bancarizados*/
	private PreparedStatement sqlConsultaIngresoBancarizado(Filtro filtro, Connection con) throws SQLException{
		String query = null;
		PreparedStatement pstmt = null;

		query = this.sqlConsultaCobroIngreso();
		pstmt = con.prepareStatement(query);
		pstmt.setString(1, filtro.getFechaInicio());
		pstmt.setString(2, filtro.getFechaFin());
		pstmt.setString(3, filtro.getFechaInicio());
		pstmt.setString(4, filtro.getFechaFin());

		log.debug(query);
		return pstmt;
	}

	/*Consulta de Gastos*/
	private PreparedStatement sqlConsultaGastoBancarizado(Filtro filtro, Connection con) throws SQLException{
		String query = null;
		PreparedStatement pstmt = null;

		query = this.sqlConsultaGastos();
		pstmt = con.prepareStatement(query);
		pstmt.setString(1, filtro.getFechaInicio());
		pstmt.setString(2, filtro.getFechaFin());		
		log.debug(query);
		return pstmt;
	}

	/*Reporte Bancarizado*/
	public List<ReporteBancarizadoBean> getReporteIngresoEgreso(Filtro filtro){

		BancarizadoBean bancarizadoBean 				= null;
		Map<String, BancarizadoBean> mapReporte			= new HashMap<String, BancarizadoBean>();
		Connection con 									= null;
		PreparedStatement pstmt 						= null;
		ResultSet rs 									= null;
		List<ReporteBancarizadoBean> listaReporte		= new ArrayList<ReporteBancarizadoBean>();
		try{
			url = urlTienda + userUrl + nombreUsuario + userPass + credencialUsuario;
			con = DriverManager.getConnection(url);
			/*Reporte de Cobros e Ingresos*/
			pstmt = this.sqlConsultaIngresoBancarizado(filtro, con);
			rs = pstmt.executeQuery();
			if (rs !=null){
				while(rs.next()){
					addDatosIngreso(rs, mapReporte, rs.getString("nombre"));
				}
			}
			/*Reporte de Gastos*/
			pstmt = this.sqlConsultaGastoBancarizado(filtro, con);
			rs = pstmt.executeQuery();
			if (rs !=null){
				while(rs.next()){
					addDatosGastos(rs, mapReporte, rs.getString("nombre"));
				}
			}
			/*Generamos el reporte como lista*/
			for (Map.Entry<String,BancarizadoBean> entry : mapReporte.entrySet())  {
				bancarizadoBean = entry.getValue();
				addReporteBancarizado(bancarizadoBean, listaReporte);
			}

		}catch(Exception e){
			e.printStackTrace();

		}finally{

			rs = null;
			pstmt = null;
			//query = null;
			try{
				if (con !=null && !con.isClosed()){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
				con = null;
			}
		}
		return listaReporte;
	}
	/*Adicionamos a la lista del reporte*/	
	private void addReporteBancarizado(BancarizadoBean bancarizadoBean, List<ReporteBancarizadoBean> listaReporte){
		boolean encontrado = false;
		if (listaReporte.isEmpty()){
			ReporteBancarizadoBean reporteBancarizadoBean = null;
			reporteBancarizadoBean = new ReporteBancarizadoBean();
			reporteBancarizadoBean.setInmueble(bancarizadoBean.getNombreInmueble());
			reporteBancarizadoBean.getListaBancarizado().add(bancarizadoBean);
			reporteBancarizadoBean.setBancarizadoSubTotalDolares(reporteBancarizadoBean.getBancarizadoSubTotalDolares().add(bancarizadoBean.getBancarizadoDolares()));
			reporteBancarizadoBean.setBancarizadoSubTotalSoles(reporteBancarizadoBean.getBancarizadoSubTotalSoles().add(bancarizadoBean.getBancarizadoSoles()));
			reporteBancarizadoBean.setEfectivoSubTotalDolares(reporteBancarizadoBean.getEfectivoSubTotalDolares().add(bancarizadoBean.getEfectivoDolares()));
			reporteBancarizadoBean.setEfectivoSubTotalSoles(reporteBancarizadoBean.getEfectivoSubTotalSoles().add(bancarizadoBean.getEfectivoSoles()));
			listaReporte.add(reporteBancarizadoBean);
		}else{
			
			for (ReporteBancarizadoBean reporteBancarizadoBean: listaReporte ){
				if (reporteBancarizadoBean.getInmueble().equals(bancarizadoBean.getNombreInmueble())){
					encontrado = true;
					reporteBancarizadoBean.getListaBancarizado().add(bancarizadoBean);
					reporteBancarizadoBean.setBancarizadoSubTotalDolares(reporteBancarizadoBean.getBancarizadoSubTotalDolares().add(bancarizadoBean.getBancarizadoDolares()));
					reporteBancarizadoBean.setBancarizadoSubTotalSoles(reporteBancarizadoBean.getBancarizadoSubTotalSoles().add(bancarizadoBean.getBancarizadoSoles()));
					reporteBancarizadoBean.setEfectivoSubTotalDolares(reporteBancarizadoBean.getEfectivoSubTotalDolares().add(bancarizadoBean.getEfectivoDolares()));
					reporteBancarizadoBean.setEfectivoSubTotalSoles(reporteBancarizadoBean.getEfectivoSubTotalSoles().add(bancarizadoBean.getEfectivoSoles()));
				}
			}
			if (!encontrado){
				ReporteBancarizadoBean reporteBancarizadoBean = null;
				reporteBancarizadoBean = new ReporteBancarizadoBean();
				reporteBancarizadoBean.setInmueble(bancarizadoBean.getNombreInmueble());
				reporteBancarizadoBean.getListaBancarizado().add(bancarizadoBean);
				reporteBancarizadoBean.setBancarizadoSubTotalDolares(reporteBancarizadoBean.getBancarizadoSubTotalDolares().add(bancarizadoBean.getBancarizadoDolares()));
				reporteBancarizadoBean.setBancarizadoSubTotalSoles(reporteBancarizadoBean.getBancarizadoSubTotalSoles().add(bancarizadoBean.getBancarizadoSoles()));
				reporteBancarizadoBean.setEfectivoSubTotalDolares(reporteBancarizadoBean.getEfectivoSubTotalDolares().add(bancarizadoBean.getEfectivoDolares()));
				reporteBancarizadoBean.setEfectivoSubTotalSoles(reporteBancarizadoBean.getEfectivoSubTotalSoles().add(bancarizadoBean.getEfectivoSoles()));
				listaReporte.add(reporteBancarizadoBean);
			}
		}
	}

	/*Adicionamos los datos en el reporte*/
	private void addDatosIngreso(ResultSet rs,Map<String, BancarizadoBean> mapReporte, String key){
		try{

			if (rs.getString("tipo_moneda").equals(Constantes.MONEDA_DOLAR)){
				if (rs.getString("tipo_pago") != null && rs.getString("tipo_pago").equals(Constantes.TIPO_PAGO_COD_BANCARIZADO)){
					addDolarBancarizado(mapReporte, key, rs);
				}else{
					addDolarEfectivo(mapReporte, key, rs);
				}
			}else {
				if (rs.getString("tipo_pago") != null && rs.getString("tipo_pago").equals(Constantes.TIPO_PAGO_COD_BANCARIZADO)){
					addSolesBancarizado(mapReporte, key, rs);
				}else{
					addSolesEfectivo(mapReporte, key, rs);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	/*Adicionamos los dolares: Moneda Dolar, Tipo de Pago Bancarizado*/
	private void addDolarBancarizado(Map<String, BancarizadoBean> mapReporte, String key, ResultSet rs) throws SQLException{
		BancarizadoBean bancarizadoBean 			= null;
		if (!existeElemento(mapReporte, key)){
			bancarizadoBean = new BancarizadoBean();
			bancarizadoBean.setBancarizadoDolares(rs.getBigDecimal("monto_dolares"));
			bancarizadoBean.setNombreUsuario(rs.getString("nombre"));
			bancarizadoBean.setNombreInmueble(rs.getString("inmueble"));
			mapReporte.put(key, bancarizadoBean);
		}else{
			bancarizadoBean = mapReporte.get(key);
			bancarizadoBean.setBancarizadoDolares(bancarizadoBean.getBancarizadoDolares().add(rs.getBigDecimal("monto_dolares")));
		}
	}
	/*Adicionamos los soles: Moneda Sol, Tipo de Pago Bancarizado*/
	private void addSolesBancarizado(Map<String, BancarizadoBean> mapReporte, String key, ResultSet rs) throws SQLException{
		BancarizadoBean bancarizadoBean 			= null;
		if (!existeElemento(mapReporte, key)){
			bancarizadoBean = new BancarizadoBean();
			bancarizadoBean.setBancarizadoSoles(rs.getBigDecimal("monto_soles"));
			bancarizadoBean.setNombreUsuario(rs.getString("nombre"));
			bancarizadoBean.setNombreInmueble(rs.getString("inmueble"));
			mapReporte.put(key, bancarizadoBean);
		}else{
			bancarizadoBean = mapReporte.get(key);
			bancarizadoBean.setBancarizadoSoles(bancarizadoBean.getBancarizadoSoles().add(rs.getBigDecimal("monto_soles")));
		}
	}
	/*Adicionamos los dolares: Moneda Dolar, Tipo de Pago Efectivo*/
	private void addDolarEfectivo(Map<String, BancarizadoBean> mapReporte, String key, ResultSet rs) throws SQLException{
		BancarizadoBean bancarizadoBean 			= null;
		if (!existeElemento(mapReporte, key)){
			bancarizadoBean = new BancarizadoBean();
			bancarizadoBean.setEfectivoDolares(rs.getBigDecimal("monto_dolares"));
			bancarizadoBean.setNombreUsuario(rs.getString("nombre"));
			bancarizadoBean.setNombreInmueble(rs.getString("inmueble"));
			mapReporte.put(key, bancarizadoBean);
		}else{
			bancarizadoBean = mapReporte.get(key);
			bancarizadoBean.setEfectivoDolares(bancarizadoBean.getEfectivoDolares().add(rs.getBigDecimal("monto_dolares")));
		}
	}
	/*Adicionamos los soles: Moneda Sol, Tipo de Pago Efectivo*/
	private void addSolesEfectivo(Map<String, BancarizadoBean> mapReporte, String key, ResultSet rs) throws SQLException{
		BancarizadoBean bancarizadoBean 			= null;
		if (!existeElemento(mapReporte, key)){
			bancarizadoBean = new BancarizadoBean();
			bancarizadoBean.setEfectivoSoles(rs.getBigDecimal("monto_soles"));
			bancarizadoBean.setNombreUsuario(rs.getString("nombre"));
			bancarizadoBean.setNombreInmueble(rs.getString("inmueble"));
			mapReporte.put(key, bancarizadoBean);
		}else{
			bancarizadoBean = mapReporte.get(key);
			bancarizadoBean.setEfectivoSoles(bancarizadoBean.getEfectivoSoles().add(rs.getBigDecimal("monto_soles")));
		}
	}

	/*validamos si existe el elemento en el reporte*/
	private boolean existeElemento(Map<String, BancarizadoBean> mapReporte, String key){
		BancarizadoBean bancarizadoBean = mapReporte.get(key);
		if (bancarizadoBean == null){
			return false;
		}else{
			return true;
		}
	}

	/*Adicionamos los datos de los Gastos en el reporte*/
	private void addDatosGastos(ResultSet rs,Map<String, BancarizadoBean> mapReporte, String key){
		try{

			if (rs.getString("tipo_moneda").equals(Constantes.MONEDA_DOLAR)){
				if (rs.getString("tipo_pago") != null && rs.getString("tipo_pago").equals(Constantes.TIPO_PAGO_COD_BANCARIZADO)){
					addDolarBancarizadoGasto(mapReporte, key, rs);
				}else{
					addDolarEfectivoGasto(mapReporte, key, rs);
				}
			}else {
				if (rs.getString("tipo_pago") != null && rs.getString("tipo_pago").equals(Constantes.TIPO_PAGO_COD_BANCARIZADO)){
					addSolesBancarizadoGasto(mapReporte, key, rs);
				}else{
					addSolesEfectivoGasto(mapReporte, key, rs);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	/*Adicionamos los dolares: Moneda Dolar, Tipo de Pago Bancarizado*/
	private void addDolarBancarizadoGasto(Map<String, BancarizadoBean> mapReporte, String key, ResultSet rs) throws SQLException{
		BancarizadoBean bancarizadoBean 			= null;
		if (!existeElemento(mapReporte, key)){
			bancarizadoBean = new BancarizadoBean();
			bancarizadoBean.setBancarizadoDolares(bancarizadoBean.getBancarizadoDolares().subtract(rs.getBigDecimal("monto_dolares")));
			bancarizadoBean.setNombreUsuario(rs.getString("nombre"));
			bancarizadoBean.setNombreInmueble(rs.getString("inmueble"));
			mapReporte.put(key, bancarizadoBean);
		}else{
			bancarizadoBean = mapReporte.get(key);
			bancarizadoBean.setBancarizadoDolares(bancarizadoBean.getBancarizadoDolares().subtract(rs.getBigDecimal("monto_dolares")));
		}
	}
	/*Adicionamos los soles: Moneda Sol, Tipo de Pago Bancarizado*/
	private void addSolesBancarizadoGasto(Map<String, BancarizadoBean> mapReporte, String key, ResultSet rs) throws SQLException{
		BancarizadoBean bancarizadoBean 			= null;
		if (!existeElemento(mapReporte, key)){
			bancarizadoBean = new BancarizadoBean();
			bancarizadoBean.setBancarizadoSoles(bancarizadoBean.getBancarizadoSoles().subtract(rs.getBigDecimal("monto_soles")));
			bancarizadoBean.setNombreUsuario(rs.getString("nombre"));
			bancarizadoBean.setNombreInmueble(rs.getString("inmueble"));
			mapReporte.put(key, bancarizadoBean);
		}else{
			bancarizadoBean = mapReporte.get(key);
			bancarizadoBean.setBancarizadoSoles(bancarizadoBean.getBancarizadoSoles().subtract(rs.getBigDecimal("monto_soles")));
		}
	}
	/*Adicionamos los dolares: Moneda Dolar, Tipo de Pago Efectivo*/
	private void addDolarEfectivoGasto(Map<String, BancarizadoBean> mapReporte, String key, ResultSet rs) throws SQLException{
		BancarizadoBean bancarizadoBean 			= null;
		if (!existeElemento(mapReporte, key)){
			bancarizadoBean = new BancarizadoBean();
			bancarizadoBean.setEfectivoDolares(bancarizadoBean.getEfectivoDolares().subtract(rs.getBigDecimal("monto_dolares")));
			bancarizadoBean.setNombreUsuario(rs.getString("nombre"));
			bancarizadoBean.setNombreInmueble(rs.getString("inmueble"));
			mapReporte.put(key, bancarizadoBean);
		}else{
			bancarizadoBean = mapReporte.get(key);
			bancarizadoBean.setEfectivoDolares(bancarizadoBean.getEfectivoDolares().subtract(rs.getBigDecimal("monto_dolares")));
		}
	}
	/*Adicionamos los soles: Moneda Sol, Tipo de Pago Efectivo*/
	private void addSolesEfectivoGasto(Map<String, BancarizadoBean> mapReporte, String key, ResultSet rs) throws SQLException{
		BancarizadoBean bancarizadoBean 			= null;
		if (!existeElemento(mapReporte, key)){
			bancarizadoBean = new BancarizadoBean();
			bancarizadoBean.setEfectivoSoles(bancarizadoBean.getEfectivoSoles().subtract(rs.getBigDecimal("monto_soles")));
			bancarizadoBean.setNombreUsuario(rs.getString("nombre"));
			bancarizadoBean.setNombreInmueble(rs.getString("inmueble"));
			mapReporte.put(key, bancarizadoBean);
		}else{
			bancarizadoBean = mapReporte.get(key);
			bancarizadoBean.setEfectivoSoles(bancarizadoBean.getEfectivoSoles().subtract(rs.getBigDecimal("monto_soles")));
		}
	}

}
