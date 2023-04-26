package br.com.evd.store.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.evd.store.model.dto.AuthenticateModelDTO;
import br.com.evd.store.model.dto.UpdateStatusModelDTO;
import br.com.evd.store.model.dto.UserAddressModelDTO;
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
			sb.append("SELECT U.IDUSER IDUSER, U.USERNAME NOME, U.EMAIL EMAIL, U.PASSWORD PASS, UT.TYPEDESC TIPOUSU, U.STATUS STATUS ");
			sb.append(" FROM TBUSER U ");
			sb.append("    JOIN TBUSERTYPE UT ON UT.IDTYPE = U.IDTYPE ");
			sb.append(" WHERE EMAIL = ?");

			PreparedStatement stmt = connection.prepareStatement(sb.toString());
			stmt.setString(1, request.getEmail());

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				return UserAuthenticatedModelDTO.builder().idUser(rs.getLong("IDUSER")).ecryptedPassword(rs.getString("PASS"))
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

	public long register(UserModelDTO request) {
		try {
			Connection connection = super.openConnection();

			String query = "INSERT INTO TBUSER (CPF, EMAIL, IDTYPE, PASSWORD, USERNAME, DATE_OF_BIRTH, GENRE) VALUES (?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, request.getCpf());
			stmt.setString(2, request.getEmail());
			stmt.setLong(3, request.getUserType().getTypeId());
			stmt.setString(4, request.getPassword());
			stmt.setString(5, request.getUsername());
			stmt.setString(6, request.getDateOfBirth());
			stmt.setString(7, request.getGenre());

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
		            if (generatedKeys.next()) {
		            	return generatedKeys.getLong(1);
		            } else {
		                throw new SQLException("Creating user failed, no ID obtained.");
		            }
		        }
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
		return -1;
	}

	public List<UserModelDTO> getUserList() {

		List<UserModelDTO> list = new ArrayList<>();

		try {
			Connection connection = super.openConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("SELECT U.IDUSER USERID, U.USERNAME NOME,  U.EMAIL EMAIL, UT.TYPEDESC TIPOUSU, U.STATUS STATUS ");
			sb.append(" FROM TBUSER U ");
			sb.append("    JOIN TBUSERTYPE UT ON UT.IDTYPE = U.IDTYPE ");

			PreparedStatement stmt = connection.prepareStatement(sb.toString());

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				UserModelDTO dto = new UserModelDTO();
				dto.setIdUser(rs.getLong("USERID"));
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
			sb.append(
					"SELECT U.IDUSER IDUSER, U.USERNAME NOME, U.CPF CPF, U.EMAIL EMAIL, UT.TYPEDESC TIPOUSU,  ");
			sb.append(" UT.IDTYPE IDTIPOUSU, U.STATUS STATUS, U.PASSWORD SENHA, U.DATE_OF_BIRTH DT_NASC, U.GENRE GENERO ");
			sb.append(" FROM TBUSER U ");
			sb.append("    JOIN TBUSERTYPE UT ON UT.IDTYPE = U.IDTYPE ");
			sb.append(" WHERE U.IDUSER =  ?");

			PreparedStatement stmt = connection.prepareStatement(sb.toString());
			stmt.setLong(1, id);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				UserModelDTO dto = new UserModelDTO();
				dto.setIdUser(rs.getLong("IDUSER"));
				dto.setUsername(rs.getString("NOME"));
				dto.setCpf(rs.getString("CPF"));
				dto.setEmail(rs.getString("EMAIL"));
				dto.setUserType(new UserTypeModelDTO(rs.getLong("IDTIPOUSU"), rs.getString("TIPOUSU")));
				dto.setStatus(rs.getString("STATUS"));
				dto.setPassword(rs.getString("SENHA"));
				dto.setGenre(rs.getString("GENERO"));
				dto.setDateOfBirth(rs.getString("DT_NASC"));

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
	
	public boolean updateUserCustomer(UserModelDTO request) {

		try {
			Connection connection = super.openConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("UPDATE TBUSER ");
			sb.append("SET USERNAME = ?, GENRE = ?, PASSWORD = ?, DATE_OF_BIRTH = ? ");
			sb.append("WHERE IDUSER = ?");

			PreparedStatement stmt = connection.prepareStatement(sb.toString());
			stmt.setString(1, request.getUsername());
			stmt.setString(2, request.getGenre());
			stmt.setString(3, request.getPassword());
			stmt.setString(4, request.getDateOfBirth());
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
	
	public boolean updateAddressesDefault(UserAddressModelDTO request) {

		try {
			Connection connection = super.openConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("UPDATE TB_USER_ADDRESS ");
			sb.append(" SET DELIVERY_ADDRESS = ?, INVOICE_ADDRESS = ?");
			sb.append(" WHERE ID_ADDRESS = ?");

			PreparedStatement stmt = connection.prepareStatement(sb.toString());
			stmt.setString(1, request.getInvoiceAddress());
			stmt.setString(2, request.getDeliveryAddress());
			stmt.setLong(3, request.getIdAddress());

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

	public boolean registerAddress(UserAddressModelDTO request) {
		try {
			Connection connection = super.openConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO TB_USER_ADDRESS ");
			sb.append("(STREET_NAME, ADDRESS_NUMBER, ADDRESS_CEP, ADDRESS_COMPLEMENT, ");
			sb.append("ADDRESS_DISTRICT, ADDRESS_CITY, ADDRESS_UF, DELIVERY_ADDRESS, INVOICE_ADDRESS, IDUSER) ");
			sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			PreparedStatement stmt = connection.prepareStatement(sb.toString());
			stmt.setString(1, request.getStreetName());
			stmt.setString(2, request.getNumber());
			stmt.setString(3, request.getCep());
			stmt.setString(4, request.getComplement());
			stmt.setString(5, request.getDistrict());
			stmt.setString(6, request.getCity());
			stmt.setString(7, request.getUf());
			stmt.setString(8, request.getDeliveryAddress());
			stmt.setString(9, request.getInvoiceAddress());
			stmt.setLong(10, request.getIdUser());

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				log.info("[ADD ADDRESS] Address {} registered.", request.getStreetName());
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
		
		log.error("[ERROR] Error to register address, ID_USER not pertenced of the type client {} ",
				request.getIdUser());
		return false;
	}


	public boolean inactivateAddress(UpdateStatusModelDTO request) {
		try {
			Connection connection = super.openConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("UPDATE TB_USER_ADDRESS ");
			sb.append(" SET STATUS = ?");
			sb.append(" WHERE ID_ADDRESS = ?");

			PreparedStatement stmt = connection.prepareStatement(sb.toString());
			stmt.setString(1, request.getStatus());
			stmt.setLong(2, request.getIdAddress());

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
	
	public List<UserAddressModelDTO> getAddressList(long id) {
		List<UserAddressModelDTO> list = new ArrayList<>();
		
		try {
			Connection connection = super.openConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("SELECT A.ID_ADDRESS IDADDRESS, A.STREET_NAME RUA,  A.ADDRESS_NUMBER NUMERO, A.ADDRESS_CEP CEP, ");
			sb.append("A.ADDRESS_COMPLEMENT COMPLEMENTO, A.ADDRESS_DISTRICT BAIRRO, A.ADDRESS_CITY CIDADE, A.ADDRESS_UF UF, ");
			sb.append("A.DELIVERY_ADDRESS ENTREGA, A.INVOICE_ADDRESS PAGAMENTO, A.STATUS AS STATUS FROM TB_USER_ADDRESS A ");
			sb.append("WHERE IDUSER = ? AND STATUS <> 'INATIVO'");

			PreparedStatement stmt = connection.prepareStatement(sb.toString());
			
			stmt.setLong(1, id);
			
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				UserAddressModelDTO dto = new UserAddressModelDTO();
				dto.setIdAddress(rs.getLong("IDADDRESS"));
				dto.setStreetName(rs.getString("RUA"));
				dto.setNumber(rs.getString("NUMERO"));
				dto.setCep(rs.getString("CEP"));
				dto.setComplement(rs.getString("COMPLEMENTO"));
				dto.setDistrict(rs.getString("BAIRRO"));
				dto.setCity(rs.getString("CIDADE"));
				dto.setUf(rs.getString("UF"));
				dto.setInvoiceAddress(rs.getString("PAGAMENTO"));
				dto.setDeliveryAddress(rs.getString("ENTREGA"));
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
}
