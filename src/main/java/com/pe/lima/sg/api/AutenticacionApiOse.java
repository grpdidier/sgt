package com.pe.lima.sg.api;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import java.nio.file.Path;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteSource;
import com.pe.lima.sg.presentacion.util.Constantes;


public class AutenticacionApiOse {

	private static String obtenerToken() throws Exception {
		String accessToken = null;
		try { 
			/*Se establece el cliente POST para el servidor de autenticación */				   
			String resource = "https://ose-gw1.efact.pe/api-efact-ose/oauth/token"; 
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(resource);
			/*Se codifica a base 64 las credenciasles de la aplicación cliente*/
			//Para las pruebas las credenciales son -> client:secret
			String credentials = Base64.getEncoder().encodeToString("client:secret".getBytes("UTF-8")); 
			httpPost.setHeader("Authorization", "Basic " + credentials);
			/*Se agregan los datos de autenticación para la plataforma Efact OSE*/
			List<NameValuePair> params = new ArrayList<>(); 
			params.add(new BasicNameValuePair("grant_type", "password")); 
			params.add(new BasicNameValuePair("username", "20386431427")); 
			params.add(new BasicNameValuePair("password", "d617f37eb232ceb7899d6c2e2f3cf557a94152336b56e9da7cc7225304142560")); 
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			/*Se envía la petición y se recibe el json con el token*/
			String json= null; 

			json = httpclient.execute(httpPost, new StringResponseHandler());
			// En caso de enviar datos correcto se obtiene el token de autenticación 
			ObjectMapper mapper = new ObjectMapper(); 
			JsonNode rootNodeToker = mapper.readTree(json); 
			accessToken = rootNodeToker.path("access_token").asText(); 
			System.out.println("TOKEN: " + accessToken); 
		} catch (ClientProtocolException e) {
			// En caso de error de autenticación retornará una excepción 
			throw new Exception(e); 
		} 
		return accessToken;
	}

	private static class StringResponseHandler implements ResponseHandler<String> { 
		@Override 
		public String handleResponse(HttpResponse response) throws IOException { 
			int status = response.getStatusLine().getStatusCode(); 
			if (status >= 200 && status < 300) { 
				HttpEntity entity = response.getEntity(); 
				return entity != null ? EntityUtils.toString(entity) : null; 
			} else { 
				throw new ClientProtocolException("Unexpected response status: " + status); 
			} 
		}
	}
	private static String obtenerTicket(String accessToken, String nombreDocumento) {
		String ticket=null; 
		//String nombreDocumento="20386431427-01-F101-00000014.csv"; 
		CloseableHttpClient httpclient = HttpClients.createDefault();
		/*Se establece el cliente POST para el servidor de autenticación*/
		HttpPost post = new HttpPost("https://ose-gw1.efact.pe/api-efact-ose/v1/document");
		/*Se agrega un Header de autorización con el token recibido por el servidor de autenticación*/
		post.setHeader("Authorization", "Bearer " + accessToken);
		/*Se adjuntarán los documentos electrónicos en el body de la petición*/
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			String FILENAME = "D:\\03.Gregorio\\06.2023\\01.Reyna\\04.Api\\02.Desarrollo\\ArchivoEnviarSunat\\"+nombreDocumento;
			//byte[] xml = Files.readAllBytes(Paths.get("tmp/"+nombreDocumento));
			byte[] xml = Files.readAllBytes(Paths.get(FILENAME));
			builder.addBinaryBody("file", xml, ContentType.create("text/csv"), nombreDocumento); 
			HttpEntity entity = builder.build(); 
			post.setEntity(entity);
			/*Se envía el documento electrónico a la plataforma Efact OSE*/
			HttpResponse response = httpclient.execute(post);
			/*Se valida el codigo de estado de la peticion*/
			int status = response.getStatusLine().getStatusCode(); 
			System.out.println("STATUS CODE: " + status );
			/*Se valida que sea el status correcto*/
			if (status == 200) { 
				HttpEntity entity2 = response.getEntity();
				//Se obtiene el resultado del response 
				String jsonResponse = entity2 != null ? EntityUtils.toString(entity2) : null;

				// Se transforma el Json y se obtiene el ticket 
				ObjectMapper mapper = new ObjectMapper(); 
				JsonNode rootNodeResponse = mapper.readTree(jsonResponse); 
				ticket = rootNodeResponse.path("description").asText(); 
				System.out.println("TICKET: " + ticket); 
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return ticket;
	}

	private static Integer obtenerCDRTicket(String accessToken, String ticket) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		int status = 0;
		/*Se establece el cliente GET para el servidor de autenticación*/
		HttpGet httpget = new HttpGet("https://ose-gw1.efact.pe:443/api-efact-ose/v1/cdr/" + ticket);
		/*Se agrega un Header de autorización con el token recibido por el servidor de autenticación*/
		httpget.setHeader("Authorization", "Bearer " + accessToken);
		/* Se envía la petición a la plataforma Efact OSE*/
		try {
			HttpResponse response = httpclient.execute(httpget);
			status = response.getStatusLine().getStatusCode(); 
			System.out.println("STATUS CODE: " + status );
			/*Si el código de estado es 200 (OK) entonces la respuesta contiene el CDR en formato XML */
			if (status == 200) { 
				String FILENAME = "D:\\03.Gregorio\\06.2023\\01.Reyna\\04.Api\\02.Desarrollo\\ArchivoEnviarSunat\\CDR-document.xml";
				
				//File myFile = new File("CDR-document.xml"); 
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
		return status;

	}
	private static Integer obtenerXMLDocumento(String accessToken, String ticket) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		int status = 0;
		/*Se establece el cliente GET para el servidor de autenticación*/
		HttpGet httpget = new HttpGet("https://ose-gw1.efact.pe/api-efact-ose/v1/xml/" + ticket);
		/*Se agrega un Header de autorización con el token recibido por el servidor de autenticación*/
		httpget.setHeader("Authorization", "Bearer " + accessToken);
		/* Se envía la petición a la plataforma Efact OSE*/
		try {
			HttpResponse response = httpclient.execute(httpget);
			status = response.getStatusLine().getStatusCode(); 
			System.out.println("STATUS CODE: " + status );
			/*Si el código de estado es 200 (OK) entonces la respuesta contiene el CDR en formato XML */
			if (status == 200) { 
				String FILENAME = "D:\\03.Gregorio\\06.2023\\01.Reyna\\04.Api\\02.Desarrollo\\ArchivoEnviarSunat\\XML-document.xml";
				//File myFile = new File("XML-document.xml"); 
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
		return status;

	}
	private static Integer obtenerPDFDocumento(String accessToken, String ticket) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		int status = 0;
		/*Se establece el cliente GET para el servidor de autenticación*/
		HttpGet httpget = new HttpGet("https://ose-gw1.efact.pe/api-efact-ose/v1/pdf/" + ticket);
		/*Se agrega un Header de autorización con el token recibido por el servidor de autenticación*/
		httpget.setHeader("Authorization", "Bearer " + accessToken);
		/* Se envía la petición a la plataforma Efact OSE*/
		try {
			HttpResponse response = httpclient.execute(httpget);
			status = response.getStatusLine().getStatusCode(); 
			System.out.println("STATUS CODE: " + status );
			/*Si el código de estado es 200 (OK) entonces la respuesta contiene el CDR en formato XML */
			if (status == 200) { 
				String FILENAME = "D:\\03.Gregorio\\06.2023\\01.Reyna\\04.Api\\02.Desarrollo\\ArchivoEnviarSunat\\pdf-document.pdf";
				//File myFile = new File("pdf-document.pdf"); 
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
		return status;

	}
	public static void main(String[] args) {
		try {
			//zipWilly();
			String token = obtenerTokenWilly();
			System.out.println("Token:"+token);
			String ticket = obtenerTicketWilly(token);
			obtenerEstadoWilly(token, ticket);
			/*String token = obtenerToken();
			String ticket = obtenerTicket(token,"20386431427-01-NC01-00000005.csv");
			/*String ticket = "bd419df7-63b6-4b4b-a422-d781f1a3b670";//"1218cef1-db4a-4d84-94c2-fee10e886bc4";
			/*//*obtenerCDRTicket(token,ticket);*//*
			obtenerXMLDocumento(token, ticket);
			obtenerPDFDocumento(token, ticket);*/
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	private static String obtenerEstadoWilly(String accessToken, String ticket) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		JsonNode nameNode = null;
		/*Se establece el cliente GET para el servidor de autenticación*/
		HttpGet httpget = new HttpGet("https://gre-test.nubefact.com/v1/contribuyente/gem/comprobantes/envios/" + ticket);
		/*Se agrega un Header de autorización con el token recibido por el servidor de autenticación*/
		httpget.setHeader("Authorization", "Bearer " + accessToken);
		/* Se envía la petición a la plataforma Efact OSE*/
		try {
			HttpResponse response = httpclient.execute(httpget);
			System.out.println("response: " + response );
			String data = EntityUtils.toString(response.getEntity());
			ObjectMapper mapper = new ObjectMapper();
			nameNode = mapper.readTree(data);
			
			System.out.println("data: " + data );
			int status = response.getStatusLine().getStatusCode(); 
			System.out.println("STATUS CODE: " + status );
			

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		/*Se valida el código de estado de la petición*/
		return nameNode.get("codRespuesta").asText();

	}
	
	private static void zipWilly() {
		Path source = Paths.get("D:\\03.Gregorio\\06.2023\\02.Willy\\02.GuiaRemision\\02.DatosDysalim\\20000000001-09-TTT1-1.xml");
        String zipFileName = "D:\\03.Gregorio\\06.2023\\02.Willy\\02.GuiaRemision\\02.DatosDysalim\\20000000001-09-TTT1-1.zip";
        try {

        	generarZipFile(source, zipFileName);
            
            String encodedString = obtenerBase64File(zipFileName);
            
            
            String checksum = obtenerHashZipFile(zipFileName);
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done");
	
	}
	
	public static String obtenerBase64File(String zipFileName) throws IOException {
		byte[] fileContent = FileUtils.readFileToByteArray(new File(zipFileName));
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        System.out.println("Zip Base 64::"+encodedString);
       return encodedString;
	}
	
	public static String obtenerHashZipFile(String zipFileName) throws IOException {
		File file = new File(zipFileName);
        ByteSource byteSource = com.google.common.io.Files.asByteSource(file);
        HashCode hc = byteSource.hash(Hashing.sha256());
        String checksum = hc.toString();
        System.out.println("Hash de SHA:"+checksum);
        return checksum;
	}
	public static void generarZipFile(Path source, String zipFileName)
	        throws IOException {

	        if (!Files.isRegularFile(source)) {
	            System.err.println("Please provide a file.");
	            return;
	        }

	        try (
	            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFileName));
	            FileInputStream fis = new FileInputStream(source.toFile());
	        ) {

	            ZipEntry zipEntry = new ZipEntry(source.getFileName().toString());
	            
	            zos.putNextEntry(zipEntry);
	            
	            

	            byte[] buffer = new byte[1024];
	            int len;
	            while ((len = fis.read(buffer)) > 0) {
	                zos.write(buffer, 0, len);
	            }
	            zos.closeEntry();
	            
	            
	            
	        }catch(Exception e) {
	        	e.printStackTrace();
	        }

	    }
	
	public static String convertZipFileToBaseEncodeString(String fileZip) {
		File originalFile = new File(fileZip);
		String encodedBase64 = null;
		try {
			FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
			byte[] bytes = new byte[(int) originalFile.length()];
			fileInputStreamReader.read(bytes);
			encodedBase64 = new String(Base64.getEncoder().encode(bytes));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return encodedBase64;

	}

	private static String obtenerTicketWilly(String accessToken) throws ClientProtocolException, IOException {
		String ticket=null; 
		JsonNode nameNode = null;
		ObjectMapper mapper = new ObjectMapper();
		String resource = "https://gre-test.nubefact.com/v1/contribuyente/gem/comprobantes/20602620337-09-TTT1-00000017"; 
		
		Path source = Paths.get("D:\\03.Gregorio\\06.2023\\02.Willy\\02.GuiaRemision\\02.DatosDysalim\\20602620337-09-TTT1-00000017.xml");
		String zipFileName = "D:\\03.Gregorio\\06.2023\\02.Willy\\02.GuiaRemision\\02.DatosDysalim\\20602620337-09-TTT1-00000017.zip";

		generarZipFile(source, zipFileName);


		final HttpPost httpPost = new HttpPost(resource);
		httpPost.setHeader("Authorization", "Bearer " + accessToken);
		String nombreArchivo = "20602620337-09-TTT1-00000017.zip";
		String archivoBase64 = obtenerBase64File(zipFileName);
		String hashArchivo = obtenerHashZipFile(zipFileName);
		final String json = "{\"archivo\":{\"nomArchivo\":\""+nombreArchivo+"\", \"arcGreZip\":\""+archivoBase64+"\",\"hashZip\":\""+hashArchivo+"\"} }";
		final StringEntity entity = new StringEntity(json);
		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");


		CloseableHttpClient httpclient = HttpClients.createDefault();

		String json2= null; 

		json2 = httpclient.execute(httpPost, new StringResponseHandler());
		nameNode = mapper.readTree(json2);
		System.out.println("json2: " + json2); 
		System.out.println("ticket: " + nameNode.get("numTicket")); 
		ticket = nameNode.get("numTicket").asText();
		return ticket;
	}
	private static String obtenerTokenWilly() throws Exception {
		String accessToken = null;
		try { 
			/*Se establece el cliente POST para el servidor de autenticación */				   
			String resource = "https://gre-test.nubefact.com/v1/clientessol/test-85e5b0ae-255c-4891-a595-0b98c65c9854/oauth2/token"; 
			CloseableHttpClient httpclient = HttpClients.createDefault();
			//HttpClient httpclient = httpClientSecury();
			HttpPost httpPost = new HttpPost(resource);

			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			
			/*Se agregan los datos de autenticación para la plataforma Efact OSE*/
			List<NameValuePair> params = new ArrayList<>(); 
			params.add(new BasicNameValuePair("grant_type", "password")); 
			params.add(new BasicNameValuePair("client_id", "test-85e5b0ae-255c-4891-a595-0b98c65c9854")); 
			params.add(new BasicNameValuePair("client_secret", "test-Hty/M6QshYvPgItX2P0+Kw=="));
			params.add(new BasicNameValuePair("username", "20602620337MODDATOS")); 
			params.add(new BasicNameValuePair("password", "MODDATOS")); 
			params.add(new BasicNameValuePair("scope", "https://api-cpe.sunat.gob.pe")); 
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			/*Se envía la petición y se recibe el json con el token*/
			String json= null; 

			json = httpclient.execute(httpPost, new StringResponseHandler());
			System.out.println("json: " + json); 
			// En caso de enviar datos correcto se obtiene el token de autenticación 
			ObjectMapper mapper = new ObjectMapper(); 
			JsonNode rootNodeToker = mapper.readTree(json); 
			accessToken = rootNodeToker.path("access_token").asText();
			System.out.println("TOKEN: " + accessToken); 
			
		} catch (ClientProtocolException e) {
			// En caso de error de autenticación retornará una excepción 
			throw new Exception(e); 
		} 
		return accessToken;
	}
	
	

	
}
