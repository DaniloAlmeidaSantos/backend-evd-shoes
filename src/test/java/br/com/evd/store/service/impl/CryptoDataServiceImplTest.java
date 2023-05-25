package br.com.evd.store.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.evd.store.service.CipherService;

@ExtendWith(MockitoExtension.class)
public class CryptoDataServiceImplTest {
	
	@InjectMocks
	private CryptoDataServiceImpl suite;
	
	@Mock
	private CipherService cipherService;
	
	@Mock
	private Cipher cipher;
	
	@Test
	public void shouldBeCryptData() throws IllegalBlockSizeException, BadPaddingException {
		// Arrange
		String data = "data";
		byte[] cipherData = data.getBytes();
		
		when(cipher.doFinal(any())).thenReturn(cipherData);
		when(cipherService.getCipher(anyString())).thenReturn(cipher);
		
		// Act
		List<String> response = suite.encryptData(data);
		
		// Assert
		assertNotNull(response);
		verify(cipher, times(1)).doFinal(any());
		verify(cipherService, times(1)).getCipher(anyString());
	}
	
	@Test
	public void shouldIllegalBlockException_CryptData() throws IllegalBlockSizeException, BadPaddingException {
		// Arrange
		String data = "data";
		
		doThrow(IllegalBlockSizeException.class).when(cipher).doFinal(any());
		when(cipherService.getCipher(anyString())).thenReturn(cipher);
		
		// Act
		List<String> response = suite.encryptData(data);
		
		// Assert
		assertNotNull(response);
		verify(cipher, times(1)).doFinal(any());
		verify(cipherService, times(1)).getCipher(anyString());
	}
	
	@Test
	public void shouldBadPaddingException_CryptData() throws IllegalBlockSizeException, BadPaddingException {
		// Arrange
		String data = "data";
		
		doThrow(BadPaddingException.class).when(cipher).doFinal(any());
		when(cipherService.getCipher(anyString())).thenReturn(cipher);
		
		// Act
		List<String> response = suite.encryptData(data);
		
		// Assert
		assertNotNull(response);
		verify(cipher, times(1)).doFinal(any());
		verify(cipherService, times(1)).getCipher(anyString());
	}
	
	@Test
	public void shouldBeTestDecryptData() throws IllegalBlockSizeException, BadPaddingException {
		// Arrange
		String data = "data";
		byte[] cipherData = data.getBytes();
		
		when(cipher.doFinal(any())).thenReturn(cipherData);
		when(cipherService.getCipher(anyString())).thenReturn(cipher);
		
		// Act
		List<String> response = suite.decryptData(data);
		
		// Assert
		assertNotNull(response);
		verify(cipher, times(1)).doFinal(any());
		verify(cipherService, times(1)).getCipher(anyString());
	}
	
	@Test
	public void shouldIllegalBlockException_DecryptData() throws IllegalBlockSizeException, BadPaddingException {
		// Arrange
		String data = "data";
		
		doThrow(IllegalBlockSizeException.class).when(cipher).doFinal(any());
		when(cipherService.getCipher(anyString())).thenReturn(cipher);
		
		// Act
		List<String> response = suite.decryptData(data);
		
		// Assert
		assertNotNull(response);
		verify(cipher, times(1)).doFinal(any());
		verify(cipherService, times(1)).getCipher(anyString());
	}
	
	@Test
	public void shouldBadPaddingException_DecryptData() throws IllegalBlockSizeException, BadPaddingException {
		// Arrange
		String data = "data";
		
		doThrow(BadPaddingException.class).when(cipher).doFinal(any());
		when(cipherService.getCipher(anyString())).thenReturn(cipher);
		
		// Act
		List<String> response = suite.decryptData(data);
		
		// Assert
		assertNotNull(response);
		verify(cipher, times(1)).doFinal(any());
		verify(cipherService, times(1)).getCipher(anyString());
	}
}
