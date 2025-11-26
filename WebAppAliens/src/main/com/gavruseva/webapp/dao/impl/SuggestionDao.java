package main.com.gavruseva.webapp.dao.impl;

import main.com.gavruseva.webapp.connection.ConnectionPool;
import main.com.gavruseva.webapp.dao.BaseDao;
import main.com.gavruseva.webapp.dao.constant.SuggestionTableConstants;
import main.com.gavruseva.webapp.exception.ConnectionException;
import main.com.gavruseva.webapp.exception.DAOException;
import main.com.gavruseva.webapp.model.Suggestion;
import main.com.gavruseva.webapp.model.Suggestion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SuggestionDao implements BaseDao<Suggestion> {  private static final Logger logger = LogManager.getLogger();
  private static final String FIND_BY_ID_QUERY = "SELECT id, pictire_path, user_id, length, width, suggestion_status FROM suggestions WHERE id = ? LIMIT 1";
  private static final String INSERT_QUERY = "INSERT INTO suggestions (pictire_path, user_id, length, width, suggestion_status) VALUES (?, ?, ?, ?, ?)";
  private static final String DELETE_QUERY = "DELETE FROM suggestions WHERE id = ?";
  private static final String UPDATE_QUERY = "UPDATE suggestions SET pictire_path = ?, user_id = ?, length = ?, width = ?, suggestion_status = ? WHERE id = ?";
  private static final String GET_ALL_QUERY = "SELECT id, pictire_path, user_id, length, width, suggestion_status FROM suggestions";

  @Override
  public Optional<Suggestion> findById(Long id) throws DAOException, ConnectionException {
    Suggestion suggestion = null;

    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(FIND_BY_ID_QUERY)) {
      pStmt.setLong(1, id);

      ResultSet resultSet = pStmt.executeQuery();

      if (!resultSet.next()) {
        logger.warn("No Suggestion with ID = {} has been found", id);
        return Optional.empty();
      }

      suggestion = new Suggestion();
      suggestion.setModelId(resultSet.getLong(SuggestionTableConstants.ID.getFieldName()));
      suggestion.setPicturePath(resultSet.getString(SuggestionTableConstants.PICTURE_PATH.getFieldName()));
      suggestion.setStatus(Suggestion.SuggestionStatus.valueOf(resultSet.getString(SuggestionTableConstants.SUGGESTION_STATUS.getFieldName())));
      suggestion.setLength(resultSet.getDouble(SuggestionTableConstants.LENGTH.getFieldName()));
      suggestion.setWidth(resultSet.getDouble(SuggestionTableConstants.WIDTH.getFieldName()));
      suggestion.setUserId(resultSet.getLong(SuggestionTableConstants.USER_ID.getFieldName()));
      logger.info("Suggestion {} has been found", suggestion);
    } catch (SQLException e) {
      logger.error("Couldn't connect to a database", e);
      throw new DAOException("Couldn't connect to a database", e);
    }

    return Optional.of(suggestion);
  }



  @Override
  public int insert(Suggestion suggestion) throws DAOException, ConnectionException{
    int rowsAffected = 0;
    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(INSERT_QUERY)) {
      pStmt.setString(1, suggestion.getPicturePath());
      pStmt.setLong(2, suggestion.getUserId());
      pStmt.setDouble(3, suggestion.getLength());
      pStmt.setDouble(4, suggestion.getWidth());
      pStmt.setString(5, suggestion.getStatus().toString());
      rowsAffected = pStmt.executeUpdate();
      logger.info("{} rows have been affected during Insert", rowsAffected);
    } catch (SQLException e) {
      logger.error("Couldn't insert the Suggestion", e);
      throw new DAOException("Could not insert the Suggestion", e);
    }
    return rowsAffected;
  }

  @Override
  public int update(Suggestion suggestion) throws DAOException, ConnectionException{
    int rowsAffected = 0;
    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(UPDATE_QUERY)) {
      pStmt.setString(1, suggestion.getPicturePath());
      pStmt.setLong(2, suggestion.getUserId());
      pStmt.setDouble(3, suggestion.getLength());
      pStmt.setDouble(4, suggestion.getWidth());
      pStmt.setString(5, suggestion.getStatus().toString());
      pStmt.setLong(6, suggestion.getModelId());
      rowsAffected = pStmt.executeUpdate();
      logger.info("{} rows have been affected during Update", rowsAffected);
    } catch (SQLException e) {
      logger.error("Couldn't update the Suggestion", e);
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
      logger.error("Couldn't update the Suggestion", e);
      throw new DAOException(e);
    }
    return rowsAffected;
  }

  @Override
  public List<Suggestion> getAll() throws DAOException, ConnectionException {
    List<Suggestion> suggestions;

    try (PreparedStatement pStmt = ConnectionPool.getInstance().getConnection().prepareStatement(GET_ALL_QUERY)) {
      ResultSet resultSet = pStmt.executeQuery();
      suggestions = getSuggestionListByResultSet(resultSet);
      logger.info("{} Suggestions in the table", suggestions.size());
    } catch (SQLException e) {
      logger.error("Couldn't retrieve all Suggestions", e);
      throw new DAOException(e);
    }

    return suggestions;
  }


  private List<Suggestion> getSuggestionListByResultSet(ResultSet resultSet) throws SQLException {
    List<Suggestion> suggestions = new ArrayList<>();

    while (resultSet.next()) {
      Suggestion suggestion = new Suggestion();
      suggestion.setModelId(resultSet.getLong(SuggestionTableConstants.ID.getFieldName()));
      suggestion.setPicturePath(resultSet.getString(SuggestionTableConstants.PICTURE_PATH.getFieldName()));
      suggestion.setStatus(Suggestion.SuggestionStatus.valueOf(resultSet.getString(SuggestionTableConstants.SUGGESTION_STATUS.getFieldName())));
      suggestion.setLength(resultSet.getDouble(SuggestionTableConstants.LENGTH.getFieldName()));
      suggestion.setUserId(resultSet.getLong(SuggestionTableConstants.USER_ID.getFieldName()));
      suggestion.setWidth(resultSet.getDouble(SuggestionTableConstants.WIDTH.getFieldName()));
      suggestions.add(suggestion);
    }

    return suggestions;
  }
}
