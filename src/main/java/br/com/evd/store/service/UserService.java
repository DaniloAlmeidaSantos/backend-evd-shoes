package br.com.evd.store.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.evd.store.model.dto.UpdateStatusModelDTO;
import br.com.evd.store.model.dto.UserModelDTO;

@Service
public interface UserService {
	boolean addUser(UserModelDTO request);
	boolean updateUser(UserModelDTO request);
	boolean updateStatus(UpdateStatusModelDTO request);
	List<UserModelDTO> getUsers();
	UserModelDTO getUser(long id);
}
