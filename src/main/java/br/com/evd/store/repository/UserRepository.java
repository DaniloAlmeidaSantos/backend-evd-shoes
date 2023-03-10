package br.com.evd.store.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import br.com.evd.store.model.dto.AuthenticateModelDTO;
import br.com.evd.store.model.dto.UserAuthenticatedModelDTO;
import br.com.evd.store.model.dto.UserModelDTO;
import br.com.evd.store.repository.config.DataSourceRepositoryConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class UserRepository extends DataSourceRepositoryConfig {

	public UserAuthenticatedModelDTO authenticate(AuthenticateModelDTO request) {
		try {
			Connection connection = super.openConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("SELECT 	U.USERNAME NOME, U.EMAIL EMAIL, U.PASSWORD PASS, UT.TYPEDESC TIPOUSU");
			sb.append(" FROM TBUSER U ");
			sb.append("    JOIN TBUSERTYPE UT ON UT.IDTYPE = U.IDTYPE ");
			sb.append(" WHERE EMAIL = ?");

			PreparedStatement stmt = connection.prepareStatement(sb.toString());
			stmt.setString(1, request.getEmail());

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				return UserAuthenticatedModelDTO.builder()
						.ecryptedPassword(rs.getString("PASS")).email(rs.getString("EMAIL"))
						.username(rs.getString("NOME")).userType(rs.getString("TIPOUSU")).build();
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

	public UserAuthenticatedModelDTO register(UserModelDTO request) {
		try {
			Connection connection = super.openConnection();

			String query = "INSERT INTO TBUSER (CPF, EMAIL, IDTYPE, IDUSER, PASSWORD, USERNAME) VALUES (?, ?, ?, ?, ?, ?)";

			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, request.getCpf());
			stmt.setString(2, request.getEmail());
			stmt.setString(2, request.);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				return UserAuthenticatedModelDTO.builder()
						.ecryptedPassword(rs.getString("PASS")).email(rs.getString("EMAIL"))
						.username(rs.getString("NOME")).userType(rs.getString("TIPOUSU")).build();
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

}
