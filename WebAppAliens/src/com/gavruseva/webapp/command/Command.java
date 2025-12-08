package com.gavruseva.webapp.command;

import com.gavruseva.webapp.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Command {
  CommandResult execute(HttpServletRequest request, HttpServletResponse
          response) throws CommandException;
}
