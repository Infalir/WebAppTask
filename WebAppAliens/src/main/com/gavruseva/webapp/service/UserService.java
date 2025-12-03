package main.com.gavruseva.webapp.service;

import main.com.gavruseva.webapp.dao.impl.UserDao;
import main.com.gavruseva.webapp.exception.DAOException;
import main.com.gavruseva.webapp.exception.ServiceException;
import main.com.gavruseva.webapp.model.User;
import main.com.gavruseva.webapp.validator.CredentialsValidator;
import main.com.gavruseva.webapp.validator.impl.CredentialsValidatorImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class UserService {
  private static final Logger logger = LogManager.getLogger();
  private static final CredentialsValidator validator = new CredentialsValidatorImpl();

  public Optional<User> login(String login, String password) throws ServiceException {
    try {
      if (!validator.isFullyValid(login, password)){
        logger.error("Incorrect login or password");
        return Optional.empty();
      }
      return UserDao.getInstance().login(login, password);
    } catch (DAOException e) {
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
    } catch (DAOException exception) {
      throw new ServiceException(exception.getMessage(), exception);
    }
  }
}
