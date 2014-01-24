package br.com.camiloporto.cloudfinance.web;

import java.nio.charset.Charset;

import org.springframework.http.MediaType;

public class MediaTypeApplicationJsonUTF8 extends MediaType {
	
	public static final String APPLICATION_JSON_UTF8_VALUE = "application/json; charset=UTF-8";
	
	public static final MediaType APPLICATION_JSON_UTF8;
	static {
		String type = MediaType.APPLICATION_JSON.getType();
		String subtype = MediaType.APPLICATION_JSON.getSubtype();
		Charset utf8 = Charset.forName("UTF-8");
		APPLICATION_JSON_UTF8 = new MediaTypeApplicationJsonUTF8(type, subtype, utf8);
	}

	public MediaTypeApplicationJsonUTF8(String type, String subtype,
			Charset charSet) {
		super(type, subtype, charSet);
	}

}
