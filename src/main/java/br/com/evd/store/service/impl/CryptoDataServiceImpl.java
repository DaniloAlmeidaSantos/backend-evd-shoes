package br.com.evd.store.service.impl;

import static br.com.evd.store.cache.CacheConstants.SERVICE_ON_MEMORY_CACHE;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import br.com.evd.store.enums.CipherEnum;
import br.com.evd.store.service.CryptoDataService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CryptoDataServiceImpl implements CryptoDataService {

	@Override
	public List<byte[]> encryptData(Object... objects) {
		Cipher cipher = this.getCipher(CipherEnum.ENCRYPT.toString());
		List<byte[]> dataEncrypteds = new ArrayList<>();

		for (int i = 0; i < objects.length; i++) {
			try {
				byte[] data = String.valueOf(objects[i]).getBytes();
				cipher.update(data);
				dataEncrypteds.add(cipher.doFinal());
			} catch (IllegalBlockSizeException e) {
				log.error("[ERROR] Error to encrypt data: {} ", e.getMessage());
			} catch (BadPaddingException e) {
				log.error("[ERROR] Error to encrypt data: {} ", e.getMessage());
			}
		}

		return dataEncrypteds;
	}

	@Override
	public List<byte[]> decryptData(Object... objects) {
		Cipher cipher = this.getCipher(CipherEnum.DECRYPT.toString());
		List<byte[]> dataEncrypteds = new ArrayList<>();

		for (int i = 0; i < objects.length; i++) {
			try {
				byte[] data = String.valueOf(objects[i]).getBytes();
				cipher.update(data);
				dataEncrypteds.add(cipher.doFinal());
			} catch (IllegalBlockSizeException e) {
				log.error("[ERROR] Error to encrypt data: {} ", e.getMessage());
			} catch (BadPaddingException e) {
				log.error("[ERROR] Error to encrypt data: {} ", e.getMessage());
			}
		}

		return dataEncrypteds;
	}

	@Cacheable(cacheNames = {SERVICE_ON_MEMORY_CACHE}, key = "{#typeCipher}", unless = "#result == null")
	public Cipher getCipher(String typeCipher) {
		Cipher cipher = null;
		
		try {
			Signature signature = Signature.getInstance("SHA256withRSA");

			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);

			KeyPair keyPair = keyPairGenerator.generateKeyPair();

			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			
			if (CipherEnum.ENCRYPT.toString().equals(typeCipher)) {
				cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());				
			} else {
				cipher.init(Cipher.DECRYPT_MODE, keyPair.getPublic());
			}
			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			log.error("[ERROR] Error to get cipher {} ", e.getMessage());
		} catch (InvalidKeyException e) {
			log.error("[ERROR] Error to initialize cipher: {} ", e.getMessage());
		} catch (Exception e) {
			log.error("[ERROR] Generic error, view specified error: {} ", e.getMessage() + e.getCause());
		}

		return cipher;
	}

}
