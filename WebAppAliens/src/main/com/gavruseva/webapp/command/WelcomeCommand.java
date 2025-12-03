package main.com.gavruseva.webapp.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.com.gavruseva.webapp.exception.IncorrectDataException;
import main.com.gavruseva.webapp.exception.ServiceException;
import main.com.gavruseva.webapp.page.Page;

import java.util.List;

public class WelcomeCommand implements Command {
  @Override
  public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException, IncorrectDataException {
    return new CommandResult(Page.WELCOME_PAGE.getPage(), false);
  }
}
