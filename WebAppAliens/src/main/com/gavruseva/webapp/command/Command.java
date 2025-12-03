package main.com.gavruseva.webapp.command;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.com.gavruseva.webapp.exception.IncorrectDataException;
import main.com.gavruseva.webapp.exception.ServiceException;

import java.io.IOException;

public interface Command {
  CommandResult execute(HttpServletRequest request, HttpServletResponse
          response) throws ServiceException, IncorrectDataException, ServletException, IOException;
}
