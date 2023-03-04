package br.com.evd.store.service;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface CryptoDataService {
	List<byte[]> encryptData(Object... objects);
	List<byte[]> decryptData(Object... objects);
}
