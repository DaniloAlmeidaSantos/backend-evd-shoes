package br.com.evd.store.service.impl;

import org.springframework.stereotype.Component;

import br.com.evd.store.model.dto.UserModelDTO;
import br.com.evd.store.service.CryptoDataService;
import br.com.evd.store.service.UserService;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private CryptoDataService cryptoDataService;

    public boolean addUser(UserModelDTO request) {
        

        return false;
    }

}