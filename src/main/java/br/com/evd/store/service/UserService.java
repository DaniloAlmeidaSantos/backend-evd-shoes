package br.com.evd.store.service;

import org.springframework.stereotype.Service;

import br.com.evd.store.model.dto.UserModelDTO;

@Service
public interface UserService {
    
    boolean addUser(UserModelDTO request);

}
