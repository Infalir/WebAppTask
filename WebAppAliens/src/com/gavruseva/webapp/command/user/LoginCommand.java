package com.gavruseva.webapp.command.user;

import com.gavruseva.webapp.exception.CommandException;
import com.gavruseva.webapp.hasher.PasswordEncryptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.gavruseva.webapp.command.Command;
import com.gavruseva.webapp.command.CommandResult;
import com.gavruseva.webapp.command.SessionAttribute;
import com.gavruseva.webapp.exception.ServiceException;
import com.gavruseva.webapp.model.User;
import com.gavruseva.webapp.page.Page;
import com.gavruseva.webapp.service.impl.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static java.util.Optional.of;
import static com.gavruseva.webapp.command.user.AuthenticationParameters.*;

public class LoginCommand implements Command {
  private static final Logger logger = LogManager.getLogger();

  private void setAttributesToSession(String name, HttpServletRequest request) {
    HttpSession session = request.getSession();
    session.setAttribute(SessionAttribute.NAME, name);
  }

  @Override
  public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
    boolean isUserFind = false;
    Optional<String> login = of(request).map(httpServletRequest -> httpServletRequest.getParameter(LOGIN));
    Optional<String> password = of(request).map(httpServletRequest -> httpServletRequest.getParameter(PASSWORD));
    if (login.get().isEmpty() || password.get().isEmpty()) {
      return forwardLoginWithError(request, ERROR_MESSAGE, ERROR_MESSAGE_TEXT);
    }
    isUserFind = initializeUserIfExist(login.get(), password.get(), request);
    if (!isUserFind) {
      logger.info("User with such login and password doesn't exist");
      return forwardLoginWithError(request, ERROR_MESSAGE, AUTHENTICATION_ERROR_TEXT);
    } else {
      logger.info("user has been authorized: login: {}, password: {}", login, password);
      return new CommandResult(COMMAND_WELCOME, false);
    }
  }

  public boolean initializeUserIfExist(String login, String password, HttpServletRequest request) throws CommandException {
    UserServiceImpl userServiceImpl = new UserServiceImpl();
    boolean userExist = false;
    try {
      Optional<User> user = userServiceImpl.login(login, password);
      if (user.isPresent() && PasswordEncryptor.verifyPassword(password, user.get().getPasswordHash())) {
        setAttributesToSession(user.get().getLogin(), request);
        userExist = true;
      }
    } catch (ServiceException e) {
      logger.error("Error occurred while looking up user", e);
      throw new CommandException(e);
    }
    return userExist;
  }

  private CommandResult forwardLoginWithError(HttpServletRequest request, final String ERROR, final String ERROR_MESSAGE) {
    request.setAttribute(ERROR, ERROR_MESSAGE);
    return new CommandResult(Page.LOGIN_PAGE.getPage(), false);
  }
}
