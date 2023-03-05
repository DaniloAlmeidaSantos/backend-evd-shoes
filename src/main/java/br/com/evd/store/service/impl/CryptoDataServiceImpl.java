package br.com.evd.store.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.evd.store.enums.CipherEnum;
import br.com.evd.store.service.CipherService;
import br.com.evd.store.service.CryptoDataService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CryptoDataServiceImpl implements CryptoDataService {
	
	@Autowired
	private CipherService cipherService;
	
	@Override
	public List<String> encryptData(Object... objects) {
		Cipher cipher = cipherService.getCipher(CipherEnum.ENCRYPT.toString());
		List<String> dataEncrypteds = new ArrayList<>();

		for (int i = 0; i < objects.length; i++) {
			try {
				byte[] data = String.valueOf(objects[i]).getBytes("UTF-8");
				String cipherResponse = Base64.getEncoder().encodeToString(cipher.doFinal(data));
				dataEncrypteds.add(cipherResponse);
			} catch (IllegalBlockSizeException e) {
				log.error("[ERROR] Error to encrypt data: {} ", e.getMessage());
			} catch (BadPaddingException e) {
				log.error("[ERROR] Error to encrypt data: {} ", e.getMessage());
			} catch (UnsupportedEncodingException e) {
				log.error("[ERROR] Unsupported data to encoding {} ", e.getMessage());
			}
		}

		return dataEncrypteds;
	}

	@Override
	public List<String> decryptData(Object... objects) {
		Cipher cipher = cipherService.getCipher(CipherEnum.DECRYPT.toString());
		List<String> deciphereddata = new ArrayList<>();

		for (int i = 0; i < objects.length; i++) {
			try {
				String encryptedData = String.valueOf(objects[i].toString());
				byte[] encoded = Base64.getDecoder().decode(encryptedData);
				byte[] cipherResponse = cipher.doFinal(encoded);
				String decryptedResponseToString = Base64.getEncoder().encodeToString(cipherResponse);
				deciphereddata.add(decryptedResponseToString);
			} catch (IllegalBlockSizeException e) {
				log.error("[ERROR] Error to encrypt data: {} ", e.getMessage());
			} catch (BadPaddingException e) {
				log.error("[ERROR] Error to encrypt data: {} ", e.getMessage());
			}
		}

		return deciphereddata;
	}
}
