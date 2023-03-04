package br.com.evd.store.service.impl;

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

import org.springframework.stereotype.Component;
import br.com.evd.store.service.CipherDataService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CipherDataServiceImpl implements CipherDataService {

	@Override
	public Object encryptData(Object... objects) {
		Cipher cipher = this.getCipher();
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
	public Object decryptData(Object... objects) {
		// TODO Auto-generated method stub
		return null;
	}

	private Cipher getCipher() {
		Cipher cipher = null;

		try {
			Signature signature = Signature.getInstance("SHA256withRSA");

			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);

			KeyPair keyPair = keyPairGenerator.generateKeyPair();

			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			log.error("[ERROR] Error to get cipher {} ", e.getMessage());
		} catch (InvalidKeyException e) {
			log.error("[ERRO] Error to initialize cipher: {} ", e.getMessage());
		}

		return cipher;
	}

}
