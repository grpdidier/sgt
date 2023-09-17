package com.pe.lima.sg.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pe.lima.sg.api.Interface.IApiOseCSV;
import com.pe.lima.sg.api.bean.CredencialBean;
import com.pe.lima.sg.bean.caja.UbigeoBean;
import com.pe.lima.sg.presentacion.util.Constantes;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class ApiOseCSV implements IApiOseCSV {

	@Override
	public String obtenerToken(CredencialBean credencialBean) throws Exception {
		String accessToken = null;
		try { 
			log.info("[obtenerToken] Inicio");
			/*Se establece el cliente POST para el servidor de autenticación */				   
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(credencialBean.getResourceToken());
			log.info("[obtenerToken] Resource: "+credencialBean.getResourceToken());
			/*Se codifica a base 64 las credenciasles de la aplicación cliente*/
			//Para las pruebas las credenciales son -> client:secret
			String credentials = Base64.getEncoder().encodeToString(credencialBean.getClientSecret().getBytes(Constantes.ENCODE_UTF_8)); 
			httpPost.setHeader(Constantes.HEADER_AUTORIZATION, Constantes.HEADER_BASIC + credentials);
			/*Se agregan los datos de autenticación para la plataforma Efact OSE*/
			List<NameValuePair> params = new ArrayList<>(); 
			params.add(new BasicNameValuePair(Constantes.GRANT_TYPE_NAME, credencialBean.getGrantType())); 
			params.add(new BasicNameValuePair(Constantes.USER_NAME, credencialBean.getUserName())); 
			params.add(new BasicNameValuePair(Constantes.PASSWORD, credencialBean.getPassword())); 
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			/*Se envía la petición y se recibe el json con el token*/
			String json= null; 

			json = httpclient.execute(httpPost, new StringResponseHandler());
			// En caso de enviar datos correcto se obtiene el token de autenticación 
			ObjectMapper mapper = new ObjectMapper(); 
			JsonNode rootNodeToker = mapper.readTree(json); 
			accessToken = rootNodeToker.path(Constantes.ACCESS_TOKEN).asText(); 
			log.info("[obtenerToken] TOKEN: " + accessToken); 
		} catch (ClientProtocolException e) {
			// En caso de error de autenticación retornará una excepción
			log.error("[obtenerToken] Error:"+e.getMessage());
			throw new Exception(e);
			
		} 
		log.info("[obtenerToken] Fin");
		return accessToken;
	}
	@Override
	public Integer obtenerTicket(CredencialBean credencialBean) {
		log.info("[obtenerTicket] Inicio");
		Integer status = 0;
		String ticket = null; 
		String nombreDocumento = credencialBean.getCsvFileName();
		log.info("[obtenerTicket] Archivo: "+nombreDocumento);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		/*Se establece el cliente POST para el servidor de autenticación*/
		HttpPost post = new HttpPost(credencialBean.getResourceDocumento());
		log.info("[obtenerTicket] Resource: "+credencialBean.getResourceDocumento());
		/*Se agrega un Header de autorización con el token recibido por el servidor de autenticación*/
		post.setHeader("Authorization", "Bearer " + credencialBean.getAccessToken());
		log.info("[obtenerTicket] Token: "+credencialBean.getAccessToken());
		/*Se adjuntarán los documentos electrónicos en el body de la petición*/
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			String FILENAME = credencialBean.getPath() + nombreDocumento;
			log.info("[obtenerTicket] FILENAME: "+FILENAME);
			byte[] byteFile = Files.readAllBytes(Paths.get(FILENAME));
			builder.addBinaryBody(Constantes.BODY_FILE, byteFile, ContentType.create(Constantes.BODY_FILE_EXTENSION), nombreDocumento); 
			HttpEntity entity = builder.build(); 
			post.setEntity(entity);
			/*Se envía el documento electrónico a la plataforma Efact OSE*/
			HttpResponse response = httpclient.execute(post);
			/*Se valida el codigo de estado de la peticion*/
			status = response.getStatusLine().getStatusCode();
			credencialBean.setStatus(status);
			log.info("[obtenerTicket] STATUS CODE: " + status );
			/*Se valida que sea el status correcto*/
			if (status == 200) { 
				HttpEntity entity2 = response.getEntity();
				//Se obtiene el resultado del response 
				String jsonResponse = entity2 != null ? EntityUtils.toString(entity2) : null;
				
				// Se transforma el Json y se obtiene el ticket 
				ObjectMapper mapper = new ObjectMapper(); 
				JsonNode rootNodeResponse = mapper.readTree(jsonResponse); 
				ticket = rootNodeResponse.path("description").asText(); 
				credencialBean.setTicket(ticket);
				log.info("[obtenerTicket] Ticket: "+ticket);
			}

		} catch (IOException e) {
			log.error("[obtenerTicket] Error: "+e.getMessage());
			e.printStackTrace();
		} 
		log.info("[obtenerTicket] Fin");
		return status;
	}

	@Override
	public Integer obtenerCDRDocumento(CredencialBean credencialBean) {
		log.info("[obtenerCDRDocumento] Inicio");
		CloseableHttpClient httpclient = HttpClients.createDefault();
		int status = 0;
		/*Se establece el cliente GET para el servidor de autenticación*/
		HttpGet httpget = new HttpGet(credencialBean.getResourceCdr() + credencialBean.getTicket());
		log.info("[obtenerCDRDocumento] Resource: "+credencialBean.getResourceCdr());
		log.info("[obtenerCDRDocumento] Ticket: "+credencialBean.getTicket());
		/*Se agrega un Header de autorización con el token recibido por el servidor de autenticación*/
		httpget.setHeader(Constantes.HEADER_AUTORIZATION, Constantes.HEADER_BEARER + credencialBean.getAccessToken());
		/* Se envía la petición a la plataforma Efact OSE*/
		try {
			HttpResponse response = httpclient.execute(httpget);
			status = response.getStatusLine().getStatusCode(); 
			credencialBean.setStatus(status);
			log.info("[obtenerCDRDocumento] STATUS CODE: " + status );
			/*Si el código de estado es 200 (OK) entonces la respuesta contiene el CDR en formato XML */
			if (status == 200) { 
				String FILENAME = credencialBean.getPath() + credencialBean.getCdrFileName();
				log.info("[obtenerCDRDocumento] FILENAME: " + FILENAME );
				File myFile = new File(FILENAME); 
				HttpEntity entity = response.getEntity(); 
				if (entity != null) { 
					try (FileOutputStream outstream = new FileOutputStream(myFile)) { 
						entity.writeTo(outstream); 
					} 
				} 
			} 

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("[obtenerCDRDocumento] ClientProtocolException Error:"+e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("[obtenerCDRDocumento] IOException Error:"+e.getMessage());
		} 
		/*Se valida el código de estado de la petición*/
		log.info("[obtenerCDRDocumento] Fin");
		return status;
	}
	
	@Override
	public Integer obtenerXMLDocumento(CredencialBean credencialBean) {
		log.info("[obtenerXMLDocumento] Inicio");
		CloseableHttpClient httpclient = HttpClients.createDefault();
		int status = 0;
		/*Se establece el cliente GET para el servidor de autenticación*/
		HttpGet httpget = new HttpGet(credencialBean.getResourceXml() + credencialBean.getTicket());
		log.info("[obtenerXMLDocumento] Resource: "+credencialBean.getResourceXml());
		log.info("[obtenerXMLDocumento] Ticket: "+credencialBean.getTicket());
		
		/*Se agrega un Header de autorización con el token recibido por el servidor de autenticación*/
		httpget.setHeader(Constantes.HEADER_AUTORIZATION, Constantes.HEADER_BEARER + credencialBean.getAccessToken());
		/* Se envía la petición a la plataforma Efact OSE*/
		try {
			HttpResponse response = httpclient.execute(httpget);
			status = response.getStatusLine().getStatusCode(); 
			credencialBean.setStatus(status);
			log.info("[obtenerXMLDocumento] STATUS CODE: " + status );
			/*Si el código de estado es 200 (OK) entonces la respuesta contiene el CDR en formato XML */
			if (status == 200) { 
				String FILENAME = credencialBean.getPath() + credencialBean.getXmlFileName();
				log.info("[obtenerXMLDocumento] FILENAME: "+ FILENAME);
				File myFile = new File(FILENAME); 
				HttpEntity entity = response.getEntity(); 
				if (entity != null) { 
					try (FileOutputStream outstream = new FileOutputStream(myFile)) { 
						entity.writeTo(outstream); 
					} 
				} 
			} 

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("[obtenerXMLDocumento] ClientProtocolException Error:"+e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("[obtenerXMLDocumento] IOException Error:"+e.getMessage());
		} 
		/*Se valida el código de estado de la petición*/
		log.info("[obtenerXMLDocumento] Fin");
		return status;
	}
	@Override
	public Integer obtenerPDFDocumento(CredencialBean credencialBean) {
		log.info("[obtenerPDFDocumento] Inicio");
		CloseableHttpClient httpclient = HttpClients.createDefault();
		int status = 0;
		/*Se establece el cliente GET para el servidor de autenticación*/
		HttpGet httpget = new HttpGet(credencialBean.getResourcePdf() + credencialBean.getTicket());
		log.info("[obtenerXMLDocumento] Resource: "+credencialBean.getResourcePdf());
		log.info("[obtenerXMLDocumento] Ticket: "+credencialBean.getTicket());
		
		/*Se agrega un Header de autorización con el token recibido por el servidor de autenticación*/
		httpget.setHeader(Constantes.HEADER_AUTORIZATION, Constantes.HEADER_BEARER + credencialBean.getAccessToken());
		/* Se envía la petición a la plataforma Efact OSE*/
		try {
			HttpResponse response = httpclient.execute(httpget);
			status = response.getStatusLine().getStatusCode(); 
			credencialBean.setStatus(status);
			log.info("[obtenerXMLDocumento] STATUS CODE: " + status );
			/*Si el código de estado es 200 (OK) entonces la respuesta contiene el CDR en formato XML */
			if (status == 200) { 
				String FILENAME = credencialBean.getPath() + credencialBean.getPdfFileName();
				log.info("[obtenerXMLDocumento] FILENAME: " + FILENAME );
				File myFile = new File(FILENAME); 
				HttpEntity entity = response.getEntity(); 
				if (entity != null) { 
					try (FileOutputStream outstream = new FileOutputStream(myFile)) { 
						entity.writeTo(outstream); 
					} 
				} 
			} 

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		/*Se valida el código de estado de la petición*/
		log.info("[obtenerPDFDocumento] Fin");
		return status;
	}
	@Override
	public Integer obtenerTicketParaMasivo(CredencialBean credencialBean) {
		log.info("[obtenerTicketParaMasivo] Inicio");
		Integer status = 0;
		String ticket = null; 
		String nombreDocumento = credencialBean.getCsvFileName();
		log.info("[obtenerTicketParaMasivo] Archivo: "+nombreDocumento);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		/*Se establece el cliente POST para el servidor de autenticación*/
		HttpPost post = new HttpPost(credencialBean.getResourceDocumento());
		log.info("[obtenerTicketParaMasivo] Resource: "+credencialBean.getResourceDocumento());
		/*Se agrega un Header de autorización con el token recibido por el servidor de autenticación*/
		post.setHeader("Authorization", "Bearer " + credencialBean.getAccessToken());
		log.info("[obtenerTicketParaMasivo] Token: "+credencialBean.getAccessToken());
		/*Se adjuntarán los documentos electrónicos en el body de la petición*/
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			String FILENAME = credencialBean.getPath() + nombreDocumento;
			log.info("[obtenerTicketParaMasivo] FILENAME: "+FILENAME);
			byte[] byteFile = Files.readAllBytes(Paths.get(FILENAME));
			builder.addBinaryBody(Constantes.BODY_FILE, byteFile, ContentType.create(Constantes.BODY_FILE_EXTENSION), nombreDocumento); 
			HttpEntity entity = builder.build(); 
			post.setEntity(entity);
			/*Se envía el documento electrónico a la plataforma Efact OSE*/
			HttpResponse response = httpclient.execute(post);
			/*Se valida el codigo de estado de la peticion*/
			status = response.getStatusLine().getStatusCode();
			credencialBean.setStatus(status);
			log.info("[obtenerTicketParaMasivo] STATUS CODE: " + status );
			/*Se valida que sea el status correcto*/
			if (status == 200) { 
				HttpEntity entity2 = response.getEntity();
				//Se obtiene el resultado del response 
				String jsonResponse = entity2 != null ? EntityUtils.toString(entity2) : null;
				
				// Se transforma el Json y se obtiene el ticket 
				ObjectMapper mapper = new ObjectMapper(); 
				JsonNode rootNodeResponse = mapper.readTree(jsonResponse); 
				ticket = rootNodeResponse.path("description").asText(); 
				credencialBean.setTicket(ticket);
				log.info("[obtenerTicketParaMasivo] Ticket: "+ticket);
			}

		} catch (IOException e) {
			log.error("[obtenerTicketParaMasivo] Error: "+e.getMessage());
			e.printStackTrace();
		} 
		log.info("[obtenerTicket] Fin");
		return status;
	}
	@Override
	public UbigeoBean obtenerUbigeo(String ruc, String token) {
		log.info("[obtenerUbigeo] Inicio");
		CloseableHttpClient httpclient = HttpClients.createDefault();
		int status = 0;
		UbigeoBean ubigeo = new UbigeoBean();
		/*Se establece el cliente GET para el servidor de autenticación*/
		HttpGet httpget = new HttpGet("https://www.apisperu.net/api/ruc/" + ruc);
		log.info("[obtenerUbigeo] ruc: "+ruc);
		/*Se agrega un Header de autorización con el token recibido por el servidor de autenticación*/
		httpget.setHeader(Constantes.HEADER_AUTORIZATION, Constantes.HEADER_BEARER + token);
		/* Se envía la petición a la plataforma APIsPERU*/
		try {
			HttpResponse response = httpclient.execute(httpget);
			status = response.getStatusLine().getStatusCode(); 
			if (status == 200) {
				HttpEntity entity2 = response.getEntity();
				//Se obtiene el resultado del response 
				String jsonResponse = entity2 != null ? EntityUtils.toString(entity2) : null;
				
				// Se transforma el Json y se obtiene el ticket 
				ObjectMapper mapper = new ObjectMapper(); 
				JsonNode rootNodeResponse = mapper.readTree(jsonResponse); 
				String ubigeoSunat = rootNodeResponse.get("data").get("ubigeo_sunat").asText();
				String departamento = rootNodeResponse.get("data").get("departamento").asText();
				String provincia = rootNodeResponse.get("data").get("provincia").asText();
				String distrito = rootNodeResponse.get("data").get("distrito").asText();
				String direccion = rootNodeResponse.get("data").get("direccion").asText();
				ubigeo = new UbigeoBean();
				ubigeo.setUbigeoSunat(ubigeoSunat);
				ubigeo.setNombreDepartamento(departamento);
				ubigeo.setNombreProvincia(provincia);
				ubigeo.setNombreDistrito(distrito);
				ubigeo.setDireccionCompleta(direccion);
				log.info("[obtenerUbigeo] Ubigeo: " + ubigeo.getUbigeoSunat() );
			}else {
				log.info("[obtenerUbigeo] status: " + status );
				ubigeo = inicializaUbigeo();
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ubigeo = inicializaUbigeo();
			log.error("[obtenerUbigeo] ClientProtocolException Error:"+e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			log.error("[obtenerUbigeo] IOException Error:"+e.getMessage());
		} 
		/*Se valida el código de estado de la petición*/
		log.info("[obtenerUbigeo] Fin");
		return ubigeo;
	}
	private UbigeoBean inicializaUbigeo() {
		UbigeoBean ubigeo = new UbigeoBean();
		ubigeo.setUbigeoSunat("000000");
		ubigeo.setNombreDepartamento("");
		ubigeo.setNombreProvincia("");
		ubigeo.setNombreDistrito("");
		ubigeo.setDireccionCompleta("");
		return ubigeo;
	}
	
}
