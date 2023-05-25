package br.com.evd.store.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class Base64UtilsTest {
	
	@Test
	public void shouldBeTestArchiveBase64() {
		// Arrange 
		String base64 = "base64;txtPlan,arquivo";
		
		// Act
		String[] fileMultipart =  Base64Utils.replaceBase64(base64);
		
		// Assert
		assertEquals(fileMultipart.length, 2);
		assertEquals(fileMultipart[0], "base64;txtPlan");
	}
	
	@Test
	public void shouldBeTestNullArchive() {
		
		// Act
		String[] fileMultipart =  Base64Utils.replaceBase64(null);
		
		// Assert
		assertNull(fileMultipart);
	}
}
