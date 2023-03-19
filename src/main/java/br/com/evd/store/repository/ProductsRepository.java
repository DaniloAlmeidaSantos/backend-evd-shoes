package br.com.evd.store.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.evd.store.model.dto.ProductImageModelDTO;
import br.com.evd.store.model.dto.ProductsModelDTO;
import br.com.evd.store.repository.config.DataSourceRepositoryConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ProductsRepository extends DataSourceRepositoryConfig {

	public long addProduct(ProductsModelDTO request) {
		Long idObtained = null;
		
		try {
			Connection connection = super.openConnection();

			String query = "INSERT INTO TBPRODUCTS (BRAND, COST, DESCRIPTION, NAMEPRODUCT, STATUS, RATIO) VALUES (?, ?, ?, ?, ?, ?)";

			PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, request.getBrand());
			stmt.setDouble(2, request.getCost());
			stmt.setString(3, request.getDescription());
			stmt.setString(4, request.getNameProduct());
			stmt.setString(5, request.getStatus());

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				log.info("[ADD PRODUCT] Product {} registered.", request.getNameProduct());
				
				try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
		            if (generatedKeys.next()) {
		            	idObtained = generatedKeys.getLong(1);
		            	generatedKeys.close();
		            	
		            	query = "INSERT INTO TBSTOCK (AMOUNT, IDPRODUCT) VALUES (?, ?)";
		            	PreparedStatement stmtStock = connection.prepareStatement(query);
		            	stmtStock.setInt(1, request.getQuantity());
		            	stmtStock.setDouble(2, idObtained);
		            	
		            	rowsAffected = stmtStock.executeUpdate();
		            	
		            	if (rowsAffected == 0) {
			                throw new SQLException("Creating stock to product failed.");
		            	}
		            } else {
		                throw new SQLException("Creating product failed, no ID obtained.");
		            }
		        }
			}
			
			return idObtained;
		} catch (SQLException e) {
			log.error("[ERROR] Error to connect in database {} ", e.getMessage());
		} finally {
			try {
				super.closeConnection();
			} catch (SQLException e) {
				log.error("[ERROR] Error to close connection");
			}
		}

		log.info("[ERROR] Error to register Product {}.", request.getNameProduct());
		return idObtained;
	}
	
	public boolean addImage(ProductImageModelDTO request, String file) {
		try {
			Connection connection = super.openConnection();

			String query = "INSERT INTO TB_PRODUCTS_IMAGE (FILE, IDPRODUCT, MIMETYPE, NAME) VALUES (?, ?, ?, ?)";

			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, file);
			stmt.setLong(2, request.getIdProduct());
			stmt.setString(3, request.getMimeType());
			stmt.setString(4, request.getName());

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				log.info("[ADD PRODUCT] Image {} registered.", request.getName());
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

		log.info("[ERROR] Error to register image to product {}.", request.getName());
		return false;
	}
	
	public List<ProductsModelDTO> getAllProducts() {
		List<ProductsModelDTO> list = new ArrayList<>();
		
		try {
			Connection connection = super.openConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("SELECT ");
			sb.append(" P.IDPRODUCT IDPRODUTO, P.NAMEPRODUCT NOMEPRODUTO, P.COST PRECO, P.STATUS STATUS, S.AMOUNT QTDE ");
			sb.append("FROM TBPRODUCTS P ");
			sb.append("  JOIN TBSTOCK S ON S.IDPRODUCT = P.IDPRODUCT");

			PreparedStatement stmt = connection.prepareStatement(sb.toString());
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				ProductsModelDTO dto = new ProductsModelDTO();
				dto.setCost(rs.getDouble("PRECO"));
				dto.setNameProduct(rs.getString("NOMEPRODUTO"));
				dto.setIdProduct(rs.getLong("IDPRODUTO"));
				dto.setStatus(rs.getString("STATUS"));
				dto.setQuantity(rs.getInt("QTDE"));
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
