package br.com.evd.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.evd.store.model.dto.ApiDefaultResponseDTO;
import br.com.evd.store.model.dto.AuthenticateModelDTO;
import br.com.evd.store.model.dto.UpdateStatusModelDTO;
import br.com.evd.store.model.dto.UserAuthenticatedModelDTO;
import br.com.evd.store.model.dto.UserModelDTO;
import br.com.evd.store.service.AuthenticateService;
import br.com.evd.store.service.CryptoDataService;
import br.com.evd.store.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/backoffice")
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	private AuthenticateService authenticateService;

	@Autowired
	private UserService userService;

	@Autowired
	private CryptoDataService cryptoDataService;

	@PostMapping(value = "/user/login", consumes = "application/json", produces = "application/json")
	public ResponseEntity<UserAuthenticatedModelDTO> authenticate(@RequestBody AuthenticateModelDTO request) {

		UserAuthenticatedModelDTO userAuthDto = authenticateService.authenticateUser(request);

		if (userAuthDto != null) {
			return ResponseEntity.ok(userAuthDto);
		}

		return ResponseEntity.badRequest().body(null);
	}

	@PostMapping(value = "/user/register", consumes = "application/json", produces = "application/json")
	public ResponseEntity<ApiDefaultResponseDTO> register(@RequestBody UserModelDTO request) {

		boolean isRegistered = userService.addUser(request);

		log.info("[ADD USER] User add ? {} ", isRegistered);
		if (isRegistered) {
			return new ResponseEntity<ApiDefaultResponseDTO>(
					new ApiDefaultResponseDTO("201", "User " + request.getUsername() + " created"), HttpStatus.CREATED);
		}

		return ResponseEntity.badRequest().body(new ApiDefaultResponseDTO("400", "Error to register user."));
	}

	@GetMapping(value = "/user/list", produces = "application/json")
	public ResponseEntity<List<UserModelDTO>> getListUsers() {

		List<UserModelDTO> userDataList = userService.getUsers();

		if (userDataList != null) {
			return ResponseEntity.ok(userDataList);
		}
		return ResponseEntity.badRequest().body(null);
	}

	@GetMapping(value = "/user", produces = "application/json")
	public ResponseEntity<UserModelDTO> getUser(@RequestParam long id) {
		UserModelDTO userData = userService.getUser(id);

		if (userData != null) {
			return ResponseEntity.ok(userData);
		}

		return ResponseEntity.badRequest().body(null);
	}

	@PutMapping(value = "/user/update", consumes = "application/json", produces = "application/json")
	public ResponseEntity<ApiDefaultResponseDTO> update(@RequestBody UserModelDTO request) {

		boolean isUpdated = userService.updateUser(request);

		if (isUpdated) {
			return new ResponseEntity<ApiDefaultResponseDTO>(
					new ApiDefaultResponseDTO("200", "User " + request.getUsername() + " updated"), HttpStatus.OK);
		}

		return ResponseEntity.badRequest().body(new ApiDefaultResponseDTO("400", "Error to update user."));
	}

	@PutMapping(value = "/user/update/status", consumes = "application/json", produces = "application/json")
	public ResponseEntity<ApiDefaultResponseDTO> updateStatus(@RequestBody UpdateStatusModelDTO request) {

		boolean isUpdated = userService.updateStatus(request);

		if (isUpdated) {
			return new ResponseEntity<ApiDefaultResponseDTO>(
					new ApiDefaultResponseDTO("200", "User updated"), HttpStatus.OK);
		}

		return ResponseEntity.badRequest().body(new ApiDefaultResponseDTO("400", "Error to update user."));
	}

	// Testar a encriptação
	@GetMapping(value = "/teste")
	public ResponseEntity<ApiDefaultResponseDTO> authenticate() {
		List<String> passwordHashed = cryptoDataService.encryptData("12345");
		List<String> passwordDecrypted = cryptoDataService.decryptData(passwordHashed.get(0));
		System.out.println(passwordDecrypted.toString());

		return ResponseEntity.badRequest().body(new ApiDefaultResponseDTO("400", passwordHashed.get(0)));
	}

}
