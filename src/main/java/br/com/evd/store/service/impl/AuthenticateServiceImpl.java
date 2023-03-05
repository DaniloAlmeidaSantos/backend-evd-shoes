package br.com.evd.store.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.evd.store.model.dto.AuthenticateModelDTO;
import br.com.evd.store.service.AuthenticateService;
import br.com.evd.store.service.CryptoDataService;

@Component
public class AuthenticateServiceImpl implements AuthenticateService {

	@Autowired
	private CryptoDataService cryptoDataService;
	
	@Override
	public boolean authenticateUser(AuthenticateModelDTO request) {
		// Calling repository for returning password encrypted for validation
		String respositoryResponse = "danilo"; 
		Optional<Boolean> optional =  cryptoDataService.decryptData(respositoryResponse)
				.stream().map(response -> request.getPassword().equals(response)).findFirst();
		
		if (optional.isPresent()) {
			return true;
		}
		
		return false;
	}

}
