package br.com.evd.store.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.evd.store.model.dto.AuthenticateModelDTO;
import br.com.evd.store.model.dto.UserAuthenticatedModelDTO;
import br.com.evd.store.repository.UserRepository;
import br.com.evd.store.service.AuthenticateService;
import br.com.evd.store.service.CryptoDataService;

@Component
public class AuthenticateServiceImpl implements AuthenticateService {

	@Autowired
	private CryptoDataService cryptoDataService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserAuthenticatedModelDTO authenticateUser(AuthenticateModelDTO request) {
		UserAuthenticatedModelDTO respositoryResponse = userRepository.authenticate(request); 
		Optional<Boolean> optional =  cryptoDataService.decryptData(respositoryResponse.getEcryptedPassword())
				.stream().map(response -> request.getPassword().equals(response)).findFirst();
		
		if (optional.get()) {
			return respositoryResponse;
		}
		
		return null;
	}

}
