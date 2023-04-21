package br.com.evd.store.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.evd.store.enums.UserTypeEnum;
import br.com.evd.store.model.dto.ProductImageModelDTO;
import br.com.evd.store.model.dto.UpdateStatusModelDTO;
import br.com.evd.store.model.dto.UserAddressModelDTO;
import br.com.evd.store.model.dto.UserModelDTO;
import br.com.evd.store.model.dto.UserTypeModelDTO;
import br.com.evd.store.repository.UserRepository;
import br.com.evd.store.service.CryptoDataService;
import br.com.evd.store.service.UserService;
import br.com.evd.store.utils.Base64Utils;
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

		long idObtained = repository.register(request);
		
		UserTypeModelDTO type = request.getUserType();
		
		if(idObtained > 0 && !type.getGroupName().equals(UserTypeEnum.CUSTOMER.getDescType())) {
			return true;
		} else if (type.getGroupName().equals(UserTypeEnum.CUSTOMER.getDescType())) {
			
			for(UserAddressModelDTO address : request.getAddresses()) {
				try {
					repository.registerAddress(address);
				} catch (Exception e) {
					log.error("[ERROR] Error to register address.");
				}
			}
			log.info("[INFO] Customer {} created success ", request.getUsername());
			return true;
		}
		
		return false;
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

	public UserModelDTO getUser(long id) {
		UserModelDTO response = repository.getUser(id);
		return response;
	}

	@Override
	public boolean addAddress (UserAddressModelDTO request) {
		 if(request != null) {
			 repository.registerAddress(request);
			 return true;
		 }
		 return false;
	}

	@Override
	public List<UserAddressModelDTO> getAddress() {
		List<UserAddressModelDTO> data = repository.getAddressList();
		
		if(data.size() > 0) {
			return data;
		}
		
		log.info("[ALERT] Not found address in database");
		return null;
	}

	@Override
	public boolean updateStatusAddress(UpdateStatusModelDTO request) {
		if(request != null) {
			repository.inactivateAddress(request);
			return true;
		}
		return false;
	}
	
	

}