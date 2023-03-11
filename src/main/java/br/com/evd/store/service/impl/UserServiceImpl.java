package br.com.evd.store.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.evd.store.model.dto.UserModelDTO;
import br.com.evd.store.repository.UserRepository;
import br.com.evd.store.service.CryptoDataService;
import br.com.evd.store.service.UserService;

@Component
public class UserServiceImpl implements UserService {

	@Autowired
	private CryptoDataService cryptoDataService;

	@Autowired
	private UserRepository repository;

	public boolean addUser(UserModelDTO request) {
		
		List<String> encrypteds = cryptoDataService.encryptData(request.getPassword());
		request.setPassword(encrypteds.get(0));
		
		return repository.register(request);
	}

}