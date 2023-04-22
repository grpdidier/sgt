package com.pe.lima.sg.rs.reporte;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pe.lima.sg.bean.reporte.ContratoXVencerBean;

@Component
public class ContratoXVencerDao {
	
	@Value("${spring.datasource.url}")
	private String urlTienda;
	
	@Value("${spring.datasource.username}")
	private String nombreUsuario;
	
	@Value("${spring.datasource.password}")
	private String credencialUsuario;
	
	private final String userUrl	= "?user=";
	private final String userPass	= "&password=";
	
	private String url;
	
		
	 /*private static final String URL =  "jdbc:ucanaccess://c:/Migracion/Tienda.mdb;memory=false";
	    
	 public void ejemploBDAccess() throws SQLException{
		 PreparedStatement pstmt = null;
		 ResultSet rs = null;
		 String query = null;
		 try {
	            Connection conn = DriverManager.getConnection(URL,"",""); 
	            //Statement s = connection.createStatement();
	            
	            query = "SELECT cfg_persona.[idpersona], cfg_persona.idpersona, cfg_persona.tipo FROM cfg_persona;";
	            
	            pstmt = conn.prepareStatement(query);
				rs = pstmt.executeQuery();
				
	            // Do something with the connection here!
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	        	query = null;
	        }
	 }*/

	public List<ContratoXVencerBean> getReporteContratoXVencer(String fechaFin){

		ContratoXVencerBean contratoXVencerBean = null;
		List<ContratoXVencerBean> listaContrato 	= null;
		Connection con = null;
		//String url = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = null;
		try{
			url = urlTienda + userUrl + nombreUsuario + userPass + credencialUsuario;
			con = DriverManager.getConnection(url);
			query = "SELECT per.nombre || ' ' || per.paterno || ' '  || per.materno AS nombre_cliente, "+
					"	    per.razon_social, "+
					"       tie.numero, "+
					"       con.monto_alquiler, "+
					"       con.fecha_fin, "+
					"       COALESCE ((select sum(ser.monto) from cli.tbl_contrato_servicio ser where ser.codigo_contrato = con.codigo_contrato),0) as monto "+
					" FROM  "+
					"	 cli.tbl_contrato con, "+
					"    mae.tbl_persona per, "+
					"    mae.tbl_tienda tie "+
					" WHERE  "+
					"    con.codigo_tienda = tie.codigo_tienda "+
					"    AND con.codigo_persona = per.codigo_persona "+
					"    AND con.estado = '1' "+
					"    AND con.estado_contrato in ('PND', 'VGN') " + 
					"    AND con.fecha_fin <= ? :: date "+
					" ORDER BY nombre_cliente, tie.numero ,  con.fecha_fin  ASC "; 

			pstmt = con.prepareStatement(query);
			pstmt.setString(1, fechaFin);
			rs = pstmt.executeQuery();

			if (rs !=null){
				listaContrato = new ArrayList<ContratoXVencerBean>();
				while(rs.next()){
					contratoXVencerBean = new ContratoXVencerBean();
					String nombre = rs.getString("nombre_cliente");
					if (nombre == null || nombre.trim().length() <= 0){
						nombre = rs.getString("razon_social");
					}
					contratoXVencerBean.setNombreCliente(nombre);
					contratoXVencerBean.setNumeroTienda(rs.getString("numero"));
					contratoXVencerBean.setMontoAlquiler(rs.getBigDecimal("monto_alquiler"));
					contratoXVencerBean.setMontoServicio(rs.getBigDecimal("monto"));
					contratoXVencerBean.setFechaVencimiento(rs.getDate("fecha_fin"));
					listaContrato.add(contratoXVencerBean);
				}
				/*Map<String, ContratoXVencerBean> mapContrato = new HashMap<>();
				ContratoXVencerBean auxContrato = null;
				for(ContratoXVencerBean contrato: listaContrato){
					auxContrato = mapContrato.get(contrato.getNumeroTienda());
					if (auxContrato == null){
						mapContrato.put(contrato.getNumeroTienda(), contrato);
					}else{
						auxContrato.setMontoServicio(auxContrato.getMontoServicio().add(contrato.getMontoServicio()));
					}
				}
				Set<Entry<String, ContratoXVencerBean>> set = mapContrato.entrySet();
			    Iterator<Entry<String, ContratoXVencerBean>> iterator = set.iterator();
			    listaContrato = new ArrayList<>();
			    while(iterator.hasNext()) {
			    	Entry<String, ContratoXVencerBean> contratoMap = iterator.next();
			    	listaContrato.add(contratoMap.getValue());
			    }*/
			}

		}catch(Exception e){
			e.printStackTrace();
			listaContrato = null;
		}finally{
			contratoXVencerBean = null;
			rs = null;
			pstmt = null;
			query = null;
			query = null;
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
		return listaContrato;
	}
}
