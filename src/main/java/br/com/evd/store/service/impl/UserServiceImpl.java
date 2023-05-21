package br.com.evd.store.service.impl;

import java.util.List;

import static br.com.evd.store.cache.CacheConstants.SERVICE_ON_MEMORY_CACHE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import br.com.evd.store.enums.UserTypeEnum;
import br.com.evd.store.model.dto.UpdateStatusModelDTO;
import br.com.evd.store.model.dto.UserAddressModelDTO;
import br.com.evd.store.model.dto.UserModelDTO;
import br.com.evd.store.model.dto.UserTypeModelDTO;
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

		long idObtained = repository.register(request);

		UserTypeModelDTO type = request.getUserType();

		if (idObtained > 0 && type.getTypeId() != UserTypeEnum.CUSTOMER.getId()) {
			return true;
		} else if (type.getTypeId() == UserTypeEnum.CUSTOMER.getId()) {

			for (UserAddressModelDTO address : request.getAddresses()) {
				try {
					address.setIdUser(idObtained);
					repository.registerAddress(address);
				} catch (Exception e) {
					log.error("[ERROR] Error to register address {}. ", e.getMessage());
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

		if (request.getUserType().getTypeId() == UserTypeEnum.CUSTOMER.getId()) {
			request.getAddresses().stream().forEach(data -> {
				try {
					if (data.getIdAddress() == 0) {
						log.info("[UPDATE USER] Inserting new addresses {} to {}", request.getUsername(),
								data.getStreetName());
						data.setIdUser(request.getIdUser());
						repository.registerAddress(data);
					} else {
						repository.updateAddressesDefault(data);						
					}
				} catch (Exception e) {
					log.error("[ERROR] Error to register address.");
				}
			});

			log.info("[UPDATE USER] Updating user {}", request);
			return repository.updateUserCustomer(request);
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
	
	@Cacheable(cacheNames = {SERVICE_ON_MEMORY_CACHE}, key = "#id", unless = "#result == null")
	public UserModelDTO getUser(long id) {
		UserModelDTO response = repository.getUser(id);

		if (response.getUserType().getGroupName().equals(UserTypeEnum.CUSTOMER.getDescType())) {
			response.setAddresses(getAddress(id));
		}

		return response;
	}

	@Override
	public boolean addAddress(UserAddressModelDTO request) {
		if (request != null) {
			repository.registerAddress(request);
			return true;
		}
		return false;
	}

	@Override
	public boolean updateStatusAddress(UpdateStatusModelDTO request) {
		if (request != null) {
			repository.inactivateAddress(request);
			return true;
		}
		return false;
	}
	
	@Override
	@Cacheable(cacheNames = {SERVICE_ON_MEMORY_CACHE}, key = "#id", unless = "#result == null")
	public UserAddressModelDTO getAddresses(long id) {
		log.info("[ADDRESSES] Getting addresses to user id {} ", id);
		List<UserAddressModelDTO> addresses = repository.getAddressList(id);
		
		for (UserAddressModelDTO address : addresses) {			
			if (address.getDeliveryAddress().equals("S")) {
				return address;
			}
		}
		
		log.info("[ADDRESSES] Addresses not found to user id {} ", id);
		return null;
	}

	private List<UserAddressModelDTO> getAddress(long id) {
		List<UserAddressModelDTO> data = repository.getAddressList(id);

		if (data.size() > 0) {
			return data;
		}

		log.info("[ALERT] Not found address in database");
		return null;
	}
}