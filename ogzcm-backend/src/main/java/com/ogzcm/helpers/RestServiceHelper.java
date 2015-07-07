package com.ogzcm.helpers;

import javax.servlet.http.HttpServletResponse;

public class RestServiceHelper {

	/**
	 * For allowing ajax requests, some headers should be set especially allow-origin 
	 * 
	 * @param resp
	 */
	public static void setResponseHeader(HttpServletResponse resp) {
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		resp.setHeader("Access-Control-Allow-Methods", "GET, POST, HEAD, OPTIONS");
	}
	
}
