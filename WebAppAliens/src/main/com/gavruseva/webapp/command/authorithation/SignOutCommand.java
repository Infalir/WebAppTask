package main.com.gavruseva.webapp.command.authorithation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import main.com.gavruseva.webapp.command.Command;
import main.com.gavruseva.webapp.command.CommandResult;
import main.com.gavruseva.webapp.command.session.SessionAttribute;
import main.com.gavruseva.webapp.exception.IncorrectDataException;
import main.com.gavruseva.webapp.exception.ServiceException;
import main.com.gavruseva.webapp.page.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SignOutCommand implements Command {
  private static final Logger logger = LogManager.getLogger(SignOutCommand.class.getName());

  @Override
  public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException, IncorrectDataException {
    HttpSession session = request.getSession();
    String username = (String) session.getAttribute(SessionAttribute.NAME);
    logger.info("User (name: {}) has been logged out", username);
    session.removeAttribute(SessionAttribute.NAME);
    return new CommandResult(Page.LOGIN_PAGE.getPage(), false);
  }
}
