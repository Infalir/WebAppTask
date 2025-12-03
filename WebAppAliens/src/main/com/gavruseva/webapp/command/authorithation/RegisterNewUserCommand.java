package main.com.gavruseva.webapp.command.authorithation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.com.gavruseva.webapp.command.Command;
import main.com.gavruseva.webapp.command.CommandResult;
import main.com.gavruseva.webapp.exception.IncorrectDataException;
import main.com.gavruseva.webapp.exception.ServiceException;
import main.com.gavruseva.webapp.model.User;
import main.com.gavruseva.webapp.page.Page;
import main.com.gavruseva.webapp.service.impl.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static java.util.Optional.of;
import static main.com.gavruseva.webapp.command.authorithation.AuthenticationParameters.*;

public class RegisterNewUserCommand implements Command {
  private static final Logger logger = LogManager.getLogger();

  @Override
  public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException, IncorrectDataException {
    Optional<String> login = of(request).map(httpServletRequest -> httpServletRequest.getParameter(NAME_FOR_REGISTER));
    Optional<String> password = of(request).map(httpServletRequest -> httpServletRequest.getParameter(PASSWORD_FOR_REGISTER));
    if (login.get().isEmpty() || password.get().isEmpty()) {
      logger.info("invalid login or password format was received:" + login + " " + password);
      return forwardToRegisterWithError(request, REGISTER_ERROR, ERROR_MESSAGE_TEXT);
    }
    User user = new User();
    user.setLogin(login.get());
    user.setPasswordHash(password.get());
    UserServiceImpl userServiceImpl = new UserServiceImpl();
    int userCount = userServiceImpl.save(user);
    if (userCount != 0) {
      logger.info("User was registered: login:" + login + " password:" + password);
      return forwardToLogin(request);
    } else {
      logger.info("invalid login or password format was received:" + login + " " + password);
      return forwardToRegisterWithError(request, REGISTER_ERROR, REGISTER_ERROR_MESSAGE_IF_EXIST);
    }
  }

  private CommandResult forwardToRegisterWithError(HttpServletRequest request, String ERROR, String ERROR_MESSAGE) {
    request.setAttribute(ERROR, ERROR_MESSAGE);
    return new CommandResult(Page.REGISTER_PAGE.getPage(), false);
  }

  private CommandResult forwardToLogin(HttpServletRequest request) {
    return new CommandResult(Page.LOGIN_PAGE.getPage(), false);
  }
}
