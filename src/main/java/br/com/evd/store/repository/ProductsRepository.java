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
import br.com.evd.store.model.dto.ProductsStatusRequestModelDTO;
import br.com.evd.store.repository.config.DataSourceRepositoryConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ProductsRepository extends DataSourceRepositoryConfig {

	public long addProduct(ProductsModelDTO request) {
		Long idObtained = null;
		
		try {
			Connection connection = super.openConnection();

			String query = "INSERT INTO TBPRODUCTS (BRAND, COST, DESCRIPTION, NAMEPRODUCT, RATIO) VALUES (?, ?, ?, ?, ?)";

			PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, request.getBrand());
			stmt.setDouble(2, request.getCost());
			stmt.setString(3, request.getDescription());
			stmt.setString(4, request.getNameProduct());
			stmt.setDouble(5, request.getRatio());

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				log.info("[ADD PRODUCT] Product {} registered.", request.getNameProduct());
				
				try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
		            if (generatedKeys.next()) {
		            	idObtained = generatedKeys.getLong(1);
		            	
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
		            
		            generatedKeys.close();
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
	
	public boolean addImage(ProductImageModelDTO request) {
		try {
			Connection connection = super.openConnection();

			String query = "INSERT INTO TB_PRODUCTS_IMAGE (FILE, IDPRODUCT, MIMETYPE, NAME) VALUES (?, ?, ?, ?)";

			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, request.getFile());
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
	
	public List<ProductsModelDTO> getAllProducts(String nameProduct) {
		List<ProductsModelDTO> list = new ArrayList<>();
		PreparedStatement stmt = null;
		try {
			Connection connection = super.openConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("SELECT ");
			sb.append(" P.IDPRODUCT IDPRODUTO, P.NAMEPRODUCT NOMEPRODUTO, P.COST PRECO, P.STATUS STATUS, S.AMOUNT QTDE ");
			sb.append("FROM TBPRODUCTS P ");
			sb.append("  JOIN TBSTOCK S ON S.IDPRODUCT = P.IDPRODUCT ");
			if (nameProduct != null) {
				log.info("[INFO] Getting like responses to {} ", nameProduct);	
				sb.append(" WHERE P.NAMEPRODUCT LIKE ? ");
			}
			sb.append(" ORDER BY P.IDPRODUCT DESC ");
			
			stmt = connection.prepareStatement(sb.toString());
			if (nameProduct != null) {
				stmt.setString(1, nameProduct + "%");
			}
			
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
	
	public boolean updateStatus(ProductsStatusRequestModelDTO request) {
		
		try {
			Connection connection = super.openConnection();

			String query = "UPDATE TBPRODUCTS SET STATUS = ? WHERE IDPRODUCT = ?";

			PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, request.getStatus());
			stmt.setLong(2, request.idProduct);

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				log.info("[INFO] Product status {} updated.", request.getIdProduct());				
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
		
		log.info("[ERROR] Error to update status the product {} ", request.getIdProduct());
		return false;
	}
	
	public ProductsModelDTO getProduct(long id) {
		PreparedStatement stmt = null;
		try {
			Connection connection = super.openConnection();

			StringBuilder sb = new StringBuilder();
			sb.append("SELECT ");
			sb.append(" P.IDPRODUCT IDPRODUTO, P.NAMEPRODUCT NOMEPRODUTO, P.COST PRECO, P.STATUS STATUS, S.AMOUNT QTDE, P.BRAND MARCA, P.RATIO RATIO, P.DESCRIPTION DESCRICAO ");
			sb.append("FROM TBPRODUCTS P ");
			sb.append("  JOIN TBSTOCK S ON S.IDPRODUCT = P.IDPRODUCT ");
			sb.append("WHERE P.IDPRODUCT = ? ");
			
			stmt = connection.prepareStatement(sb.toString());
			stmt.setLong(1, id);
			
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				ProductsModelDTO dto = new ProductsModelDTO();
				dto.setCost(rs.getDouble("PRECO"));
				dto.setNameProduct(rs.getString("NOMEPRODUTO"));
				dto.setIdProduct(rs.getLong("IDPRODUTO"));
				dto.setStatus(rs.getString("STATUS"));
				dto.setDescription(rs.getString("DESCRICAO"));
				dto.setBrand(rs.getString("MARCA"));
				dto.setRatio(rs.getDouble("RATIO"));
				dto.setQuantity(rs.getInt("QTDE"));
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
	
	public List<ProductImageModelDTO> getImages(long id) {
		List<ProductImageModelDTO> images = new ArrayList<>();
		PreparedStatement stmt = null;
		try {
			Connection connection = super.openConnection();

			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT ");
			sb.append(" 	IDIMAGE ID, NAME NOME, FILE IMAGEM, MIMETYPE  MIME ");
			sb.append(" FROM TB_PRODUCTS_IMAGE ");
			sb.append(" WHERE IDPRODUCT = ? ");
			
			stmt = connection.prepareStatement(sb.toString());
			stmt.setLong(1, id);
			
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				ProductImageModelDTO dto = new ProductImageModelDTO();
				dto.setFile(rs.getString("IMAGEM"));
				dto.setIdImage(rs.getLong("ID"));
				dto.setName(rs.getString("NOME"));
				dto.setMimeType(rs.getString("MIME"));
				images.add(dto);
			}
			
			return images;
		} catch (SQLException e) {
			log.error("[ERROR] Error to connect in database {} ", e.getMessage());
		} finally {
			try {
				super.closeConnection();
			} catch (SQLException e) {
				log.error("[ERROR] Error to close connection");
			}
		}

		return images;
	}
}
