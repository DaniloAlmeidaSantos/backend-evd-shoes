package br.com.evd.store.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import br.com.evd.store.model.dto.SellConfirmRequestDTO;
import br.com.evd.store.repository.config.DataSourceRepositoryConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class SellProductRepository extends DataSourceRepositoryConfig {

	public boolean sellProduct(SellConfirmRequestDTO request) {
		try {
			Connection connection = super.openConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO TB_SALES_HISTORIC ");
			sb.append(" (IDUSER, IDPRODUCT, SALE_QUANT_PRODUCTS, SALE_TOTAL_PRICE, ID_ADDRESS, ID_PAYMENT, SALE_STATUS ");
			sb.append(" VALUES (?, ?, ?, ?, ?, ?)");

			PreparedStatement stmt = connection.prepareStatement(sb.toString());
			stmt.setLong(1, request.getIdUser());
			stmt.setLong(2, request.getIdProduct());
			stmt.setInt(3, request.getQuantity());
			stmt.setDouble(4, request.getTotalPrice());
			stmt.setLong(5, request.getIdAddress());
			stmt.setLong(6, request.getIdPayment());
			stmt.setString(7, request.getStatus());

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
	
}
