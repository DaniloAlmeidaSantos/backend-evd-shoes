package br.com.evd.store.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.evd.store.model.dto.AuthenticateModelDTO;
import br.com.evd.store.model.dto.UserAuthenticatedModelDTO;
import br.com.evd.store.repository.UserRepository;
import br.com.evd.store.service.AuthenticateService;
import br.com.evd.store.service.CryptoDataService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthenticateServiceImpl implements AuthenticateService {

	@Autowired
	private CryptoDataService cryptoDataService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserAuthenticatedModelDTO authenticateUser(AuthenticateModelDTO request) {
		UserAuthenticatedModelDTO respositoryResponse = userRepository.authenticate(request);
		
		if (respositoryResponse.getStatus() == null || !respositoryResponse.getStatus().equals("A")) {
			log.info("[ATTENTION] User is inactive, user not valid to authenticate.");
			return null;
		}
		
		Optional<Boolean> optional =  cryptoDataService.decryptData(respositoryResponse.getEcryptedPassword())
				.stream().map(response -> request.getPassword().equals(response)).findFirst();
		
		if (optional.get()) {
			log.info("User {} authenticated", respositoryResponse.getUsername());
			return respositoryResponse;
		}
		
		log.info("User {} not authenticated", respositoryResponse.getUsername());
		return null;
	}

}
