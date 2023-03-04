package br.com.evd.store.service;

import org.springframework.stereotype.Service;

@Service
public interface CipherDataService {
	Object encryptData(Object... objects);
	Object decryptData(Object... objects);
}
