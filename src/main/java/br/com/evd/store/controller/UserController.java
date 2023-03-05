package br.com.evd.store.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.evd.store.service.CryptoDataService;

@RestController
@RequestMapping("/market")
public class UserController {
	
	@Autowired
	private CryptoDataService cipherDataService;
	
	@GetMapping(value = "/user/login", produces = "application/json")
	public ResponseEntity<String> authenticate() throws UnsupportedEncodingException {		
		List<byte[]> bs = cipherDataService.encryptData("Danilo", "Danilo");
		
		for (byte[] bs2 : bs) {
			String encrypted = new String(bs2, "UTF8");
			System.out.println(encrypted);
		}
		
		return ResponseEntity.ok("Success");
	}
	
	
}
