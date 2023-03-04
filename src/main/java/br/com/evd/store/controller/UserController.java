package br.com.evd.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.evd.store.service.CipherDataService;

@RestController
@RequestMapping("/market")
public class UserController {
	
	@Autowired
	private CipherDataService cipherDataService;
	
	@GetMapping(value = "/user/login", produces = "application/json")
	public ResponseEntity<String> authenticate() {
		Object object = cipherDataService.encryptData("Danilo");
		System.out.println(object);
		return ResponseEntity.ok("Success");
	}
	
	
}
