package main.com.gavruseva.webapp.dao.impl;

import main.com.gavruseva.webapp.connection.ConnectionPool;
import main.com.gavruseva.webapp.dao.BaseDao;
import main.com.gavruseva.webapp.dao.constant.ImageTableConstants;
import main.com.gavruseva.webapp.exception.ConnectionException;
import main.com.gavruseva.webapp.exception.DAOException;
import main.com.gavruseva.webapp.model.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ImageDao implements BaseDao<Image> {
  private static final Logger logger = LogManager.getLogger();
  private static final String FIND_BY_ID_QUERY = "SELECT id, pictire_path, name, length, width, price FROM images WHERE id = ? LIMIT 1";
  private static final String INSERT_QUERY = "INSERT INTO images (pictire_path, name, length, width, price) VALUES (?, ?, ?, ?, ?)";
  private static final String DELETE_QUERY = "DELETE FROM images WHERE id = ?";
  private static final String UPDATE_QUERY = "UPDATE images SET pictire_path = ?, name = ?, length = ?, price = ?, width = ? WHERE id = ?";
  private static final String GET_ALL_QUERY = "SELECT id, pictire_path, name, length, width, price FROM images";

  @Override
  public Optional<Image> findById(Long id) throws DAOException, ConnectionException {
    Image image = null;

    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(FIND_BY_ID_QUERY)) {
      pStmt.setLong(1, id);

      ResultSet resultSet = pStmt.executeQuery();

      if (!resultSet.next()) {
        logger.warn("No Image with ID = {} has been found", id);
        return Optional.empty();
      }

      image = new Image();
      image.setModelId(resultSet.getLong(ImageTableConstants.ID.getFieldName()));
      image.setPicturePath(resultSet.getString(ImageTableConstants.PICTURE_PATH.getFieldName()));
      image.setName(resultSet.getString(ImageTableConstants.NAME.getFieldName()));
      image.setLength(resultSet.getDouble(ImageTableConstants.LENGTH.getFieldName()));
      image.setWidth(resultSet.getDouble(ImageTableConstants.WIDTH.getFieldName()));
      image.setPrice(resultSet.getDouble(ImageTableConstants.PRICE.getFieldName()));
      logger.info("Image {} has been found", image);
    } catch (SQLException e) {
      logger.error("Couldn't connect to a database", e);
      throw new DAOException("Couldn't connect to a database", e);
    }

    return Optional.of(image);
  }



  @Override
  public int insert(Image image) throws DAOException, ConnectionException{
    int rowsAffected = 0;
    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(INSERT_QUERY)) {
      pStmt.setString(1, image.getPicturePath());
      pStmt.setString(2, image.getName());
      pStmt.setDouble(3, image.getLength());
      pStmt.setDouble(4, image.getWidth());
      pStmt.setDouble(5, image.getPrice());
      rowsAffected = pStmt.executeUpdate();
      logger.info("{} rows have been affected during Insert", rowsAffected);
    } catch (SQLException e) {
      logger.error("Couldn't insert the Image", e);
      throw new DAOException("Could not insert the Image", e);
    }
    return rowsAffected;
  }

  @Override
  public int update(Image image) throws DAOException, ConnectionException{
    int rowsAffected = 0;
    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(UPDATE_QUERY)) {
      pStmt.setString(1, image.getPicturePath());
      pStmt.setString(2, image.getName());
      pStmt.setDouble(3, image.getLength());
      pStmt.setDouble(4, image.getWidth());
      pStmt.setDouble(5, image.getPrice());
      pStmt.setLong(6, image.getModelId());
      rowsAffected = pStmt.executeUpdate();
      logger.info("{} rows have been affected during Update", rowsAffected);
    } catch (SQLException e) {
      logger.error("Couldn't update the Image", e);
      throw new DAOException(e);
    }
    return rowsAffected;
  }

  @Override
  public int delete(Long id) throws DAOException, ConnectionException{
    int rowsAffected = 0;
    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(DELETE_QUERY)) {
      pStmt.setLong(1, id);
      rowsAffected = pStmt.executeUpdate();
      logger.info("{} rows have been affected during Delete", rowsAffected);
    } catch (SQLException e) {
      logger.error("Couldn't update the Image", e);
      throw new DAOException(e);
    }
    return rowsAffected;
  }

  @Override
  public List<Image> getAll() throws DAOException, ConnectionException {
    List<Image> images;

    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(GET_ALL_QUERY)) {
      ResultSet resultSet = pStmt.executeQuery();
      images = getImageListByResultSet(resultSet);
      logger.info("{} Images in the table", images.size());
    } catch (SQLException e) {
      logger.error("Couldn't retrieve all Images", e);
      throw new DAOException(e);
    }

    return images;
  }


  private List<Image> getImageListByResultSet(ResultSet resultSet) throws SQLException {
    List<Image> images = new ArrayList<>();

    while (resultSet.next()) {
      Image image = new Image();
      image.setModelId(resultSet.getLong(ImageTableConstants.ID.getFieldName()));
      image.setPicturePath(resultSet.getString(ImageTableConstants.PICTURE_PATH.getFieldName()));
      image.setName(resultSet.getString(ImageTableConstants.NAME.getFieldName()));
      image.setLength(resultSet.getDouble(ImageTableConstants.LENGTH.getFieldName()));
      image.setPrice(resultSet.getDouble(ImageTableConstants.PRICE.getFieldName()));
      image.setWidth(resultSet.getDouble(ImageTableConstants.WIDTH.getFieldName()));
      images.add(image);
    }

    return images;
  }
}
