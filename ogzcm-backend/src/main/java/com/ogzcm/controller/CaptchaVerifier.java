package com.ogzcm.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.ogzcm.dto.Success;

/**
 * @author oguzcam
 *
 * Validate if given response to captcha is OK
 */
public class CaptchaVerifier {
	private static final Logger logger = LoggerFactory.getLogger(BookController.class);
	private static final String secretKey = "6LeDKAkTAAAAAOownt-mWhcAjhqafxHbEqPbgggE";
	private static final String verifyUrl = "https://www.google.com/recaptcha/api/siteverify";
	
	public boolean verifyCaptcha(String response) {
		if(response == null || response.length() < 1){
			return false;
		}
		String charset = java.nio.charset.StandardCharsets.UTF_8.name();
	    try {            
	    	String query = String.format("secret=%s&response=%s", 
	        URLEncoder.encode(secretKey, charset), 
	        URLEncoder.encode(response, charset));

	        // Get response from Google Rest Api 
	    	URLConnection connection = new URL(verifyUrl + "?" + query).openConnection();
	        InputStream stream = connection.getInputStream();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
	        StringBuilder builder = new StringBuilder();

	        // Transfer from inputstream to string 
	        String read = reader.readLine();
	        while(read != null) {
	            builder.append(read);
	            read = reader.readLine();
	        }
	        stream.close();
	        
	        // TODO Convert response to JSON and check if success is true
	        Gson gson = new Gson();
	        Success success = gson.fromJson(builder.toString(), Success.class);
	        if(success.isSuccess()){
	        	return true;
	        }
	    } catch (IOException ex) {
	        logger.error("Got error by method validateCaptcha ", ex);
	    }
	    return false;
	}
}
