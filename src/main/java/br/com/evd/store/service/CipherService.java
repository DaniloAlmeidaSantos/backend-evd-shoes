package br.com.evd.store.service;

import javax.crypto.Cipher;

import org.springframework.stereotype.Service;

@Service
public interface CipherService {
	Cipher getCipher(String typeCipher);
}
