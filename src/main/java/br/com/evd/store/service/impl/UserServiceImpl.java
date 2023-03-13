package br.com.evd.store.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.evd.store.model.dto.UpdateStatusModelDTO;
import br.com.evd.store.model.dto.UserModelDTO;
import br.com.evd.store.repository.UserRepository;
import br.com.evd.store.service.CryptoDataService;
import br.com.evd.store.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserServiceImpl implements UserService {

	@Autowired
	private CryptoDataService cryptoDataService;

	@Autowired
	private UserRepository repository;

	public boolean addUser(UserModelDTO request) {
		log.info("[REGISTER USER] Encrypting new password.");
		List<String> encrypteds = cryptoDataService.encryptData(request.getPassword());
		request.setPassword(encrypteds.get(0));

		return repository.register(request);
	}

	@Override
	public boolean updateUser(UserModelDTO request) {
		if (request.isNewPassword()) {
			log.info("[UPDATE USER] Encrypting new password.");
			List<String> encrypteds = cryptoDataService.encryptData(request.getPassword());
			request.setPassword(encrypteds.get(0));
		}
		log.info("[UPDATE USER] Updating user {}", request);
		return repository.updateUser(request);
	}

	@Override
	public boolean updateStatus(UpdateStatusModelDTO request) {
		return repository.updateStatus(request);
	}

	@Override
	public List<UserModelDTO> getUsers() {
		return repository.getUserList();
	}

	public UserModelDTO getUser(long id) {
		return repository.getUser(id);
	}

}