package main.com.gavruseva.webapp.service;

import main.com.gavruseva.webapp.exception.ServiceException;
import main.com.gavruseva.webapp.model.User;

import java.util.Optional;

public interface UserService {
  Optional<User> login(String login, String password) throws ServiceException;
  Integer save(User user) throws ServiceException;
}
