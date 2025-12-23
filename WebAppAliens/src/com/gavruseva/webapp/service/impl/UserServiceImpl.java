package com.gavruseva.webapp.service.impl;

import com.gavruseva.webapp.dao.impl.UserDao;
import com.gavruseva.webapp.exception.DaoException;
import com.gavruseva.webapp.exception.ServiceException;
import com.gavruseva.webapp.model.User;
import com.gavruseva.webapp.service.UserService;
import com.gavruseva.webapp.validator.CredentialsValidator;
import com.gavruseva.webapp.validator.impl.CredentialsValidatorImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class UserServiceImpl implements UserService {
  private static final Logger logger = LogManager.getLogger();
  private static final CredentialsValidator validator = new CredentialsValidatorImpl();

  public Optional<User> login(String login, String password) throws ServiceException {
    try {
      if (!validator.isFullyValid(login, password)){
        logger.error("Incorrect login or password");
        return Optional.empty();
      }
      return UserDao.getInstance().login(login, password);
    } catch (DaoException e) {
      logger.error("Couldn't execute connection to a database", e);
      throw new ServiceException(e.getMessage(), e);
    }
  }

  public Integer save(User user) throws ServiceException {
    try  {
      if(!validator.isLoginValid(user.getLogin())){
        logger.error("Incorrect login");
        return 0;
      }

      if (!UserDao.getInstance().findLogin(user.getLogin()).isEmpty()) {
        return UserDao.getInstance().insert(user);
      } else {
        return 0;
      }
    } catch (DaoException e) {
      logger.error("Couldn't execute connection to a database", e);
      throw new ServiceException(e.getMessage(), e);
    }
  }

  public Optional<User> findUserByLogin(String login) throws ServiceException {
    try{
      return UserDao.getInstance().findByLogin(login);
    }
    catch (DaoException e) {
      logger.error("Couldn't execute connection to a database", e);
      throw new ServiceException(e.getMessage(), e);
    }
  }
}
