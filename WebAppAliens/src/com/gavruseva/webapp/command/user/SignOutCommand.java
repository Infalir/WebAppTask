package com.gavruseva.webapp.command.user;

import com.gavruseva.webapp.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.gavruseva.webapp.command.Command;
import com.gavruseva.webapp.command.CommandResult;
import com.gavruseva.webapp.command.SessionAttribute;
import com.gavruseva.webapp.page.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SignOutCommand implements Command {
  private static final Logger logger = LogManager.getLogger(SignOutCommand.class.getName());

  @Override
  public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
    HttpSession session = request.getSession();
    String username = (String) session.getAttribute(SessionAttribute.NAME);
    logger.info("User (name: {}) has been logged out", username);
    session.removeAttribute(SessionAttribute.NAME);
    return new CommandResult(Page.LOGIN_PAGE.getPage(), false);
  }
}
