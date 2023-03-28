package br.com.evd.store.service.impl;

import java.util.List;

import static br.com.evd.store.cache.CacheConstants.SERVICE_ON_MEMORY_CACHE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
		if (request != null) {
			return repository.updateStatus(request);
		}
		
		return false;
	}

	public List<UserModelDTO> getUsers() {
		List<UserModelDTO> data = repository.getUserList();
		
		if (data.size() > 0) {
			return data;
		}
		
		log.info("[ALERT] Not found users in database");
		return null;
	}

	@Cacheable(value = SERVICE_ON_MEMORY_CACHE, key = "#id", unless = "#result == null")
	public UserModelDTO getUser(long id) {
		UserModelDTO response = repository.getUser(id);
		return response;
	}

}