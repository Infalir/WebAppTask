package main.com.gavruseva.webapp.dao.impl;

import main.com.gavruseva.webapp.connection.ConnectionPool;
import main.com.gavruseva.webapp.dao.BaseDao;
import main.com.gavruseva.webapp.dao.constant.InkTableConstants;
import main.com.gavruseva.webapp.exception.ConnectionException;
import main.com.gavruseva.webapp.exception.DAOException;
import main.com.gavruseva.webapp.model.Ink;
import main.com.gavruseva.webapp.model.Ink;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InkDao implements BaseDao<Ink> {
  private static final Logger logger = LogManager.getLogger();
  private static final String FIND_BY_ID_QUERY = "SELECT id, name, color, allergen, manufacturer FROM inks WHERE id = ? LIMIT 1";
  private static final String INSERT_QUERY = "INSERT INTO inks  (name, color, allergen, manufacturer) VALUES (?, ?, ?, ?)";
  private static final String DELETE_QUERY = "DELETE FROM inks WHERE id = ?";
  private static final String UPDATE_QUERY = "UPDATE inks SET name = ?, color = ?, manufacturer = ?, allergen = ? WHERE id = ?";
  private static final String GET_ALL_QUERY = "SELECT id, name, color, allergen, manufacturer FROM inks";

  @Override
  public Optional<Ink> findById(Long id) throws DAOException, ConnectionException {
    Ink ink = null;

    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(FIND_BY_ID_QUERY)) {
      pStmt.setLong(1, id);

      ResultSet resultSet = pStmt.executeQuery();

      if (!resultSet.next()) {
        logger.warn("No Ink with ID = {} has been found", id);
        return Optional.empty();
      }

      ink = new Ink();
      ink.setModelId(resultSet.getLong(InkTableConstants.ID.getFieldName()));
      ink.setName(resultSet.getString(InkTableConstants.NAME.getFieldName()));
      ink.setColor(resultSet.getString(InkTableConstants.COLOR.getFieldName()));
      ink.setAllergen(resultSet.getString(InkTableConstants.ALLERGEN.getFieldName()));
      ink.setManufacturer(resultSet.getString(InkTableConstants.MANUFACTURER.getFieldName()));
      logger.info("Ink {} has been found", ink);
    } catch (SQLException e) {
      logger.error("Couldn't connect to a database", e);
      throw new DAOException("Couldn't connect to a database", e);
    }

    return Optional.of(ink);
  }



  @Override
  public int insert(Ink ink) throws DAOException, ConnectionException{
    int rowsAffected = 0;
    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(INSERT_QUERY)) {
      pStmt.setString(1, ink.getName());
      pStmt.setString(2, ink.getColor());
      pStmt.setString(3, ink.getAllergen());
      pStmt.setString(4, ink.getManufacturer());
      rowsAffected = pStmt.executeUpdate();
      logger.info("{} rows have been affected during Insert", rowsAffected);
    } catch (SQLException e) {
      logger.error("Couldn't insert the Ink", e);
      throw new DAOException("Could not insert the Ink", e);
    }
    return rowsAffected;
  }

  @Override
  public int update(Ink ink) throws DAOException, ConnectionException{
    int rowsAffected = 0;
    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(UPDATE_QUERY)) {
      pStmt.setString(1, ink.getName());
      pStmt.setString(2, ink.getColor());
      pStmt.setString(3, ink.getManufacturer());
      pStmt.setString(4, ink.getAllergen());
      pStmt.setLong(5, ink.getModelId());
      rowsAffected = pStmt.executeUpdate();
      logger.info("{} rows have been affected during Update", rowsAffected);
    } catch (SQLException e) {
      logger.error("Couldn't update the Ink", e);
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
      logger.error("Couldn't update the Ink", e);
      throw new DAOException(e);
    }
    return rowsAffected;
  }

  @Override
  public List<Ink> getAll() throws DAOException, ConnectionException {
    List<Ink> inks;

    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(GET_ALL_QUERY)) {
      ResultSet resultSet = pStmt.executeQuery();
      inks = getInkListByResultSet(resultSet);
      logger.info("{} Inks in the table", inks.size());
    } catch (SQLException e) {
      logger.error("Couldn't retrieve all Inks", e);
      throw new DAOException(e);
    }

    return inks;
  }


  private List<Ink> getInkListByResultSet(ResultSet resultSet) throws SQLException {
    List<Ink> inks = new ArrayList<>();

    while (resultSet.next()) {
      Ink ink = new Ink();
      ink.setModelId(resultSet.getLong(InkTableConstants.ID.getFieldName()));
      ink.setName(resultSet.getString(InkTableConstants.NAME.getFieldName()));
      ink.setColor(resultSet.getString(InkTableConstants.COLOR.getFieldName()));
      ink.setManufacturer(resultSet.getString(InkTableConstants.MANUFACTURER.getFieldName()));
      ink.setAllergen(resultSet.getString(InkTableConstants.ALLERGEN.getFieldName()));
      inks.add(ink);
    }

    return inks;
  }
}
