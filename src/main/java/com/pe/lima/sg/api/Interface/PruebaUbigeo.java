package com.pe.lima.sg.api.Interface;

import com.pe.lima.sg.api.ApiOseCSV;
import com.pe.lima.sg.bean.caja.UbigeoBean;

public class PruebaUbigeo {

	public static void main(String[] args) {	
		try {
		ApiOseCSV api = new ApiOseCSV();
		//UbigeoBean ubi = api.obtenerUbigeo("10773438868", "708f63d6550fa89310e64c5a39591034e1ed36721cf30b7dfb2466222a82522c", "https://www.apisperu.net/api/ruc/");
		UbigeoBean ubi = api.obtenerUbigeoRuc("20513783770", "a2d18060-be03-4083-94ff-74303d647f4d-8244cca7-44b6-4d73-8d50-cee635cf39d5", "https://ruc.com.pe/api/v1/consultas");
		System.out.println(ubi.getUbigeoSunat());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
