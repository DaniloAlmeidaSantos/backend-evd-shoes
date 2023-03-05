package br.com.evd.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.evd.store.model.dto.ApiDefaultResponseDTO;
import br.com.evd.store.model.dto.AuthenticateModelDTO;
import br.com.evd.store.service.AuthenticateService;

@RestController
@RequestMapping("/market")
public class UserController {
	
	@Autowired
	private AuthenticateService authenticateService;
	
	@GetMapping(value = "/user/login", consumes = "application/json", produces = "application/json")
	public ResponseEntity<ApiDefaultResponseDTO> authenticate(@RequestBody AuthenticateModelDTO request) {		
		
		boolean isAuthenticated = authenticateService.authenticateUser(request);
		
		if (isAuthenticated) {
			return ResponseEntity.ok(new ApiDefaultResponseDTO("200", "Authenticate success!"));
		}
		
		return ResponseEntity.badRequest().body(new ApiDefaultResponseDTO("400", "Authenticate Failed!")); 
	}
	
	
}
