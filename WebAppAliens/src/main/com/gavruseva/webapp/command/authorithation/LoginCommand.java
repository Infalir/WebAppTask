package main.com.gavruseva.webapp.command.authorithation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import main.com.gavruseva.webapp.command.Command;
import main.com.gavruseva.webapp.command.CommandResult;
import main.com.gavruseva.webapp.command.session.SessionAttribute;
import main.com.gavruseva.webapp.exception.IncorrectDataException;
import main.com.gavruseva.webapp.exception.ServiceException;
import main.com.gavruseva.webapp.model.User;
import main.com.gavruseva.webapp.page.Page;
import main.com.gavruseva.webapp.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Optional;

import static java.util.Optional.of;
import static main.com.gavruseva.webapp.command.authorithation.AuthenticationParameters.*;

public class LoginCommand implements Command {
  private static final Logger logger = LogManager.getLogger();

  private void setAttributesToSession(String name, HttpServletRequest request) {
    HttpSession session = request.getSession();
    session.setAttribute(SessionAttribute.NAME, name);
  }

  @Override
  public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException, IncorrectDataException, IOException {
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

  public boolean initializeUserIfExist(String login, String password, HttpServletRequest request) throws ServiceException {
    UserService userService = new UserService();
    Optional<User> user = userService.login(login, password);
    boolean userExist = false;
    if (user.isPresent()) {
      setAttributesToSession(user.get().getLogin(), request);
      userExist = true;
    }
    return userExist;
  }

  private CommandResult forwardLoginWithError(HttpServletRequest request, final String ERROR, final String ERROR_MESSAGE) {
    request.setAttribute(ERROR, ERROR_MESSAGE);
    return new CommandResult(Page.LOGIN_PAGE.getPage(), false);
  }
}
