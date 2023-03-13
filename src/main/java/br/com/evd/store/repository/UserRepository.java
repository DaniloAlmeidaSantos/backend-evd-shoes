package br.com.evd.store.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.evd.store.model.dto.AuthenticateModelDTO;
import br.com.evd.store.model.dto.UpdateStatusModelDTO;
import br.com.evd.store.model.dto.UserAuthenticatedModelDTO;
import br.com.evd.store.model.dto.UserModelDTO;
import br.com.evd.store.model.dto.UserTypeModelDTO;
import br.com.evd.store.repository.config.DataSourceRepositoryConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class UserRepository extends DataSourceRepositoryConfig {

	public UserAuthenticatedModelDTO authenticate(AuthenticateModelDTO request) {
		try {
			Connection connection = super.openConnection();

			StringBuilder sb = new StringBuilder();
			sb.append(
					"SELECT U.USERNAME NOME, U.EMAIL EMAIL, U.PASSWORD PASS, UT.TYPEDESC TIPOUSU, U.STATUS STATUS ");
			sb.append(" FROM TBUSER U ");
			sb.append("    JOIN TBUSERTYPE UT ON UT.IDTYPE = U.IDTYPE ");
			sb.append(" WHERE EMAIL = ?");

			PreparedStatement stmt = connection.prepareStatement(sb.toString());
			stmt.setString(1, request.getEmail());

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				return UserAuthenticatedModelDTO.builder().ecryptedPassword(rs.getString("PASS"))
						.email(rs.getString("EMAIL")).username(rs.getString("NOME")).userType(rs.getString("TIPOUSU"))
						.status(rs.getString("STATUS")).build();
			}
		} catch (SQLException e) {
			log.error("[ERROR] Error to connect in database {} ", e.getMessage());
		} finally {
			try {
				super.closeConnection();
			} catch (SQLException e) {
				log.error("[ERROR] Error to close connection");
			}
		}

		return null;
	}

	public boolean register(UserModelDTO request) {
		try {
			Connection connection = super.openConnection();

			String query = "INSERT INTO TBUSER (CPF, EMAIL, IDTYPE, PASSWORD, USERNAME) VALUES (?, ?, ?, ?, ?)";

			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, request.getCpf());
			stmt.setString(2, request.getEmail());
			stmt.setLong(3, request.getUserType().getTypeId());
			stmt.setString(4, request.getPassword());
			stmt.setString(5, request.getUsername());

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				log.info("[ADD USER] User {} registered.", request.getEmail());
				return true;
			}
		} catch (SQLException e) {
			log.error("[ERROR] Error to connect in database {} ", e.getMessage());
		} finally {
			try {
				super.closeConnection();
			} catch (SQLException e) {
				log.error("[ERROR] Error to close connection");
			}
		}
		
		log.info("[ERROR] Error to register user {}.", request.getEmail());
		return false;
	}

	public List<UserModelDTO> getUserList() {

		List<UserModelDTO> list = new ArrayList<>();

		try {
			Connection connection = super.openConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("SELECT U.USERNAME NOME,  U.EMAIL EMAIL, UT.TYPEDESC TIPOUSU, U.STATUS STATUS ");
			sb.append(" FROM TBUSER U ");
			sb.append("    JOIN TBUSERTYPE UT ON UT.IDTYPE = U.IDTYPE ");

			PreparedStatement stmt = connection.prepareStatement(sb.toString());

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				UserModelDTO dto = new UserModelDTO();
				dto.setUsername(rs.getString("NOME"));
				dto.setEmail(rs.getString("EMAIL"));
				dto.setUserType(new UserTypeModelDTO(0L, rs.getString("TIPOUSU")));
				dto.setStatus(rs.getString("STATUS"));
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("[ERROR] Error to connect in database {} ", e.getMessage());
		} finally {
			try {
				super.closeConnection();
			} catch (SQLException e) {
				log.error("[ERROR] Error to close connection");
			}
		}

		return list;
	}

	public UserModelDTO getUser(long id) {
		try {
			Connection connection = super.openConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("SELECT U.USERNAME NOME, U.CPF CPF, U.EMAIL EMAIL, UT.TYPEDESC TIPOUSU, UT.IDTYPE IDTIPOUSU, U.STATUS STATUS, U.PASSWORD SENHA ");
			sb.append(" FROM TBUSER U ");
			sb.append("    JOIN TBUSERTYPE UT ON UT.IDTYPE = U.IDTYPE ");
			sb.append(" WHERE U.IDUSER =  ?");

			PreparedStatement stmt = connection.prepareStatement(sb.toString());
			stmt.setLong(1, id);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				UserModelDTO dto = new UserModelDTO();
				dto.setUsername(rs.getString("NOME"));
				dto.setCpf(rs.getString("CPF"));
				dto.setEmail(rs.getString("EMAIL"));
				dto.setUserType(new UserTypeModelDTO(rs.getLong("IDTIPOUSU"), rs.getString("TIPOUSU")));
				dto.setStatus(rs.getString("STATUS"));
				dto.setPassword(rs.getString("SENHA"));
				
				log.info("[INFO] User {} founded success", id);
				
				return dto;
			}
		} catch (SQLException e) {
			log.error("[ERROR] Error to connect in database {} ", e.getMessage());
		} finally {
			try {
				super.closeConnection();
			} catch (SQLException e) {
				log.error("[ERROR] Error to close connection");
			}
		}
		return null;
	}

	public boolean updateUser(UserModelDTO request) {

		try {
			Connection connection = super.openConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("UPDATE TBUSER ");
			sb.append("SET USERNAME = ?, CPF = ?, PASSWORD = ?, IDTYPE = ? ");
			sb.append("WHERE IDUSER = ?");

			PreparedStatement stmt = connection.prepareStatement(sb.toString());
			stmt.setString(1, request.getUsername());
			stmt.setString(2, request.getCpf());
			stmt.setString(3, request.getPassword());
			stmt.setLong(4, request.getUserType().getTypeId());
			stmt.setLong(5, request.getIdUser());

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				return true;
			}
		} catch (Exception e) {
			log.error("[ERROR] Error to connect in database {} ", e.getMessage());
		} finally {
			try {
				super.closeConnection();
			} catch (SQLException e) {
				log.error("[ERROR] Error to close connection");
			}
		}

		return false;
	}

	public boolean updateStatus(UpdateStatusModelDTO request) {

		try {
			Connection connection = super.openConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("UPDATE TBUSER ");
			sb.append(" SET STATUS = ?");
			sb.append(" WHERE IDUSER = ?");

			PreparedStatement stmt = connection.prepareStatement(sb.toString());
			stmt.setString(1, request.getStatus());
			stmt.setLong(2, request.getUserId());

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				return true;
			}
		} catch (Exception e) {
			log.error("[ERROR] Error to connect in database {} ", e.getMessage());
		} finally {
			try {
				super.closeConnection();
			} catch (SQLException e) {
				log.error("[ERROR] Error to close connection");
			}
		}

		return false;
	}

}
