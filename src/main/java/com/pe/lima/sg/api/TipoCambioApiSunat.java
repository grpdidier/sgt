package com.pe.lima.sg.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pe.lima.sg.api.bean.TipoCambioBean;
import com.pe.lima.sg.presentacion.IndexAction;

import lombok.extern.slf4j.Slf4j;

/**
 * Consulta del tipo de cambio SUNAT (Decolecta).
 * GET https://api.decolecta.com/v1/tipo-cambio/sunat
 * Authorization: Bearer {token}
 *
 * Respuesta:
 * {
 *   "buy_price": "3.428",
 *   "sell_price": "3.436",
 *   "base_currency": "USD",
 *   "quote_currency": "PEN",
 *   "date": "2026-10-21"
 * }
 *
 * Configurar en application.properties:
 *   decolecta.api.token=sk_xxxxxxxx
 *   decolecta.api.url.tipo-cambio=https://api.decolecta.com/v1/tipo-cambio/sunat   (opcional)
 */
@Component
@Slf4j
public class TipoCambioApiSunat {

	private static final String URL_POR_DEFECTO = "https://api.decolecta.com/v1/tipo-cambio/sunat";
	private static final int TIMEOUT_CONEXION_MS = 10000;
	private static final int TIMEOUT_LECTURA_MS  = 15000;

	@Value("${decolecta.api.token:}")
	private String token;

	@Value("${decolecta.api.url.tipo-cambio:" + URL_POR_DEFECTO + "}")
	private String url;

	public TipoCambioApiSunat() {
	}

	/** Uso fuera de Spring: new TipoCambioApiSunat("sk_xxx"). */
	public TipoCambioApiSunat(String token) {
		this.token = token;
		this.url = URL_POR_DEFECTO;
	}

	/**
	 * Invoca al API y devuelve el tipo de cambio del dia.
	 * @throws IOException si no hay token, falla la conexion o la respuesta no es 200.
	 */
	public TipoCambioBean obtenerTipoCambio(String token) throws IOException {
		log.info("[obtenerTipoCambio] Inicio:"+token);
		if (token == null || token.trim().isEmpty()) {
			throw new IOException("No se configuro el token del API de tipo de cambio (decolecta.api.token)");
		}
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(TIMEOUT_CONEXION_MS);
			conn.setReadTimeout(TIMEOUT_LECTURA_MS);
			conn.setRequestProperty("Authorization", "Bearer " + token.trim());
			conn.setRequestProperty("Accept", "application/json");

			int status = conn.getResponseCode();
			log.info("[obtenerTipoCambio] status:"+status);
			if (status != HttpURLConnection.HTTP_OK) {
				String detalle = leer(conn.getErrorStream());
				throw new IOException("API tipo de cambio respondio HTTP " + status + (detalle.isEmpty() ? "" : ": " + detalle));
			}

			String json = leer(conn.getInputStream());
			TipoCambioBean tc = new TipoCambioBean();
			tc.setCompra(decimal(json, "buy_price"));
			tc.setVenta(decimal(json, "sell_price"));
			tc.setMonedaBase(texto(json, "base_currency"));
			tc.setMonedaCotizada(texto(json, "quote_currency"));
			tc.setFecha(texto(json, "date"));
			log.info("[obtenerTipoCambio] fecha:"+tc.getFecha());
			if (tc.getVenta() == null) {
				throw new IOException("Respuesta inesperada del API de tipo de cambio: " + json);
			}
			log.info("[obtenerTipoCambio] Fin");
			return tc;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	

	private String leer(InputStream is) throws IOException {
		if (is == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				sb.append(linea);
			}
		}
		return sb.toString();
	}

	/** Valor de un campo string o numerico del JSON plano (sin depender de librerias JSON). */
	private String texto(String json, String campo) {
		Matcher m = Pattern.compile("\"" + Pattern.quote(campo) + "\"\\s*:\\s*(?:\"([^\"]*)\"|([^,}\\s]+))").matcher(json);
		if (m.find()) {
			return m.group(1) != null ? m.group(1) : m.group(2);
		}
		return null;
	}

	private BigDecimal decimal(String json, String campo) {
		String v = texto(json, campo);
		if (v == null || v.trim().isEmpty() || "null".equals(v)) {
			return null;
		}
		try {
			return new BigDecimal(v.trim());
		} catch (NumberFormatException e) {
			return null;
		}
	}

}
