package br.com.evd.store.service.impl;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.evd.store.enums.CipherEnum;
import br.com.evd.store.service.CipherService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CipherServiceImpl implements CipherService {

	@Value("${cipher.key}")
	private String cipherKey;
	
	private Cipher currentCipher = null;
	
	private String currentTypeCipher;
	
	private byte[] currentKey;
	
	@Override
	public Cipher getCipher(String typeCipher) {
		if (this.currentCipher == null) {
			log.info("No Cipher in cache. Will request a new one");
			this.currentCipher = this.retrieveNewCipher(typeCipher);
		} else if (this.currentCipher == null || this.currentTypeCipher != typeCipher) {
			log.info("Cipher not is equal previous type. Will request a new one");
			this.currentCipher = this.retrieveNewCipher(typeCipher);
		} else {
			log.info("Cipher is still valid.");
		}
		
		this.currentTypeCipher = typeCipher;
		
		return this.currentCipher;
	}

	private Cipher retrieveNewCipher(String typeCipher) {		
		try {
			SecretKey aesKey = this.getKey();
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			
			if (CipherEnum.DECRYPT.toString().equals(typeCipher)) {
				cipher.init(Cipher.DECRYPT_MODE, aesKey);					
			} else { 
				cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			}
			
			return cipher;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			log.error("[ERROR] Error to get cipher {} ", e.getMessage());	
		} catch (InvalidKeyException e) {
			log.error("[ERROR] Error to initialize cipher: {} ", e.getMessage());
		} catch (Exception e) {
			log.error("[ERROR] Generic error, view specified error: {} ", e.getMessage() + e.getCause());
		}

		return null;
	}
	
	private SecretKey getKey() {
        MessageDigest sha = null;
        try {
        	currentKey = cipherKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            currentKey = sha.digest(currentKey);
            currentKey = Arrays.copyOf(currentKey, 16);
            return new SecretKeySpec(currentKey, "AES");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
