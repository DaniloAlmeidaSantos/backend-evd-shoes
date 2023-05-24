package br.com.evd.store.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.evd.store.model.dto.SalesToUserDTO;
import br.com.evd.store.model.dto.SellConfirmRequestDTO;
import br.com.evd.store.repository.config.DataSourceRepositoryConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class SellProductRepository extends DataSourceRepositoryConfig {

	public boolean sellProduct(SellConfirmRequestDTO request, long idSale) {
		try {
			Connection connection = super.openConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO TB_SALES_HISTORIC ");
			sb.append(" (IDUSER, IDPRODUCT, SALE_QUANT_PRODUCTS, SALE_TOTAL_PRICE, ID_PAYMENT, ID_SALE) ");
			sb.append(" VALUES (?, ?, ?, ?, ?, ?)");

			PreparedStatement stmt = connection.prepareStatement(sb.toString());
			stmt.setLong(1, request.getIdUser());
			stmt.setLong(2, request.getIdProduct());
			stmt.setInt(3, request.getQuantity());
			stmt.setDouble(4, request.getTotalPrice());
			stmt.setLong(5, request.getIdPayment());
			stmt.setLong(6, idSale);

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				log.info("[SALE PRODUCT] Product {} saled success.", request.getIdProduct());
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

		log.error("[SALE PRODUCT ERROR] Error to register sale product.");
		return false;
	}

	public boolean updateStatus(SellConfirmRequestDTO request, long idSale) {
		try {
			Connection connection = super.openConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO TB_SALES_HISTORIC ");
			sb.append(" (IDUSER, IDPRODUCT, SALE_QUANT_PRODUCTS, SALE_TOTAL_PRICE, ID_PAYMENT, ID_SALE) ");
			sb.append(" VALUES (?, ?, ?, ?, ?, ?)");

			PreparedStatement stmt = connection.prepareStatement(sb.toString());
			stmt.setLong(1, request.getIdUser());
			stmt.setLong(2, request.getIdProduct());
			stmt.setInt(3, request.getQuantity());
			stmt.setDouble(4, request.getTotalPrice());
			stmt.setLong(5, request.getIdPayment());
			stmt.setLong(6, idSale);

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				log.info("[SALE PRODUCT] Product {} saled success.", request.getIdProduct());
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

		log.error("[SALE PRODUCT ERROR] Error to register sale product.");
		return false;
	}

	public long confimrSell(SellConfirmRequestDTO request) {
		try {
			Connection connection = super.openConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO TB_SALE ");

			if (request.getIdAddress() == 0) {
				sb.append(" (SALE_STATUS, SALE_ADDRESS) ");
				sb.append(" VALUES (?, ?)");
			} else {
				sb.append(" (SALE_STATUS, SALE_ADDRESS, ID_ADDRESS) ");
				sb.append(" VALUES (?, ?, ?)");
			}

			PreparedStatement stmt = connection.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, request.getStatus());
			stmt.setString(2, request.getSaleAddress());

			if (request.getIdAddress() != 0)
				stmt.setLong(3, request.getIdAddress());

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				log.info("[SALE PRODUCT] Product {} include to table TB_SALE success.", request.getIdProduct());
				try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						return generatedKeys.getLong(1);
					} else {
						throw new SQLException("Creating sale to product failed, no ID obtained.");
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

		log.error("[SALE PRODUCT ERROR] Error to register sale product.");
		return -1;
	}

	public List<SalesToUserDTO> getSalesToUser(Long id) {
		List<SalesToUserDTO> list = new ArrayList<>();
		PreparedStatement stmt = null;
		try {
			Connection connection = super.openConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("SELECT ");
			sb.append(
					"	S.ID_SALE, I.FILE, P.NAMEPRODUCT, SH.SALE_QUANT_PRODUCTS, SH.SALE_TOTAL_PRICE, SH.SALE_DATE, S.SALE_STATUS ");
			sb.append("FROM TB_SALE S ");
			sb.append("	JOIN TB_SALES_HISTORIC SH ON SH.ID_SALE = S.ID_SALE ");
			sb.append("	JOIN TBPRODUCTS P ON P.IDPRODUCT = SH.IDPRODUCT ");
			sb.append(" JOIN TB_PRODUCTS_IMAGE I ON I.IDPRODUCT = P.IDPRODUCT AND I.FILEDEFAULT = 'S' ");

			if (id != null) {
				sb.append("WHERE SH.IDUSER = ? ");
			}

			sb.append(" ORDER BY SH.SALE_DATE DESC");

			stmt = connection.prepareStatement(sb.toString());
			if (id != null) {
				stmt.setLong(1, id);
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				SalesToUserDTO dto = new SalesToUserDTO();
				dto.setIdSale(rs.getLong("ID_SALE"));
				dto.setDate(rs.getString("SALE_DATE"));
				dto.setFile(rs.getString("FILE"));
				dto.setNameProduct(rs.getString("NAMEPRODUCT"));
				dto.setPrice(rs.getDouble("SALE_TOTAL_PRICE"));
				dto.setQuantity(rs.getInt("SALE_QUANT_PRODUCTS"));
				dto.setStatus(rs.getString("SALE_STATUS"));
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

	public List<SalesToUserDTO> getSummary(long id) {
		List<SalesToUserDTO> list = new ArrayList<>();
		PreparedStatement stmt = null;
		try {
			Connection connection = super.openConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("SELECT ");
			sb.append(
					"	S.ID_SALE, I.FILE, P.NAMEPRODUCT, SH.SALE_QUANT_PRODUCTS, SH.SALE_TOTAL_PRICE, SH.SALE_DATE, S.SALE_STATUS, S.SALE_ADDRESS, P.COST ");
			sb.append("FROM TB_SALE S ");
			sb.append("	JOIN TB_SALES_HISTORIC SH ON SH.ID_SALE = S.ID_SALE ");
			sb.append("	JOIN TBPRODUCTS P ON P.IDPRODUCT = SH.IDPRODUCT ");
			sb.append(" JOIN TB_PRODUCTS_IMAGE I ON I.IDPRODUCT = P.IDPRODUCT AND I.FILEDEFAULT = 'S' ");
			sb.append("WHERE SH.ID_SALE = ? ");

			stmt = connection.prepareStatement(sb.toString());
			stmt.setLong(1, id);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				SalesToUserDTO dto = new SalesToUserDTO();
				dto.setIdSale(rs.getLong("ID_SALE"));
				dto.setDate(rs.getString("SALE_DATE"));
				dto.setFile(rs.getString("FILE"));
				dto.setNameProduct(rs.getString("NAMEPRODUCT"));
				dto.setPrice(rs.getDouble("SALE_TOTAL_PRICE"));
				dto.setQuantity(rs.getInt("SALE_QUANT_PRODUCTS"));
				dto.setStatus(rs.getString("SALE_STATUS"));
				dto.setSaleAddress(rs.getString("SALE_ADDRESS"));
				dto.setUnitPrice(rs.getDouble("COST"));
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
