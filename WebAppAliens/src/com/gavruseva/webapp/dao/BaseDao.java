package com.gavruseva.webapp.dao;

import com.gavruseva.webapp.exception.ConnectionException;
import com.gavruseva.webapp.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface BaseDao<T> {
  Optional<T> findById(Long id) throws DaoException, ConnectionException;
  int insert(T t) throws DaoException;
  int update(T t)throws DaoException, ConnectionException;
  int delete(Long id)throws DaoException, ConnectionException;
  List<T> getAll()throws DaoException, ConnectionException;
}
