package com.gavruseva.webapp.command;

import com.gavruseva.webapp.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.gavruseva.webapp.page.Page;

public class WelcomeCommand implements Command {
  @Override
  public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
    return new CommandResult(Page.WELCOME_PAGE.getPage(), false);
  }
}
