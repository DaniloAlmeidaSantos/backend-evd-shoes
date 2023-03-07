package br.com.evd.store.service;

import org.springframework.stereotype.Service;

import br.com.evd.store.model.dto.AuthenticateModelDTO;
import br.com.evd.store.model.dto.UserAuthenticatedModelDTO;

@Service
public interface AuthenticateService {
	UserAuthenticatedModelDTO authenticateUser(AuthenticateModelDTO request);
}
