package main.com.gavruseva.webapp.dao;

import main.com.gavruseva.webapp.exception.ConnectionException;
import main.com.gavruseva.webapp.exception.DAOException;

import java.util.List;
import java.util.Optional;

public interface BasicDao<T> {
  Optional<T> findById(Long id) throws DAOException, ConnectionException;
  int insert(T t) throws DAOException, ConnectionException;
  int update(T t)throws DAOException, ConnectionException;
  int delete(Long id)throws DAOException, ConnectionException;
  List<T> getAll()throws DAOException, ConnectionException;
}
