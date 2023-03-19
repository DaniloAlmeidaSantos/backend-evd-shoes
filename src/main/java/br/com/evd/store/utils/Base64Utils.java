package br.com.evd.store.utils;

public class Base64Utils {

	public static String[] replaceBase64(String base64) {
			
		if (base64 != null) {
			String[] archiveSplitted = base64.split(",");
			
			if (archiveSplitted.length == 2) {
				return archiveSplitted;
			}
		}
		
		return null;
	}

}
