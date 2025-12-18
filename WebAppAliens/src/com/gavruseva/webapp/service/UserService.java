package com.gavruseva.webapp.service;

import com.gavruseva.webapp.exception.ServiceException;
import com.gavruseva.webapp.model.User;

import java.util.Optional;

public interface UserService {
  Optional<User> login(String login, String password) throws ServiceException;
  Integer save(User user) throws ServiceException;
  Optional<User> findUserByLogin(String login) throws ServiceException;
}
