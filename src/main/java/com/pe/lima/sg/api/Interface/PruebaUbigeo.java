package com.pe.lima.sg.api.Interface;

import com.pe.lima.sg.api.ApiOseCSV;
import com.pe.lima.sg.bean.caja.UbigeoBean;

public class PruebaUbigeo {

	public static void main(String[] args) {
		try {
		ApiOseCSV api = new ApiOseCSV();
		UbigeoBean ubi = api.obtenerUbigeo("205521038", "708f63d6550fa89310e64c5a39591034e1ed36721cf30b7dfb2466222a82522c", "https://www.apisperu.net/api/ruc/");
		System.out.println(ubi.getUbigeoSunat());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
