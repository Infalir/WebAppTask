package com.gavruseva.webapp.command.order;

import com.gavruseva.webapp.command.Command;
import com.gavruseva.webapp.command.CommandResult;
import com.gavruseva.webapp.command.SessionAttribute;
import com.gavruseva.webapp.exception.CommandException;
import com.gavruseva.webapp.exception.ServiceException;
import com.gavruseva.webapp.service.impl.OrderServiceImpl;
import com.gavruseva.webapp.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateOrderCommand implements Command {
  private static final Logger logger = LogManager.getLogger();

  @Override
  public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws CommandException{
    HttpSession session = request.getSession();
    String username = (String) session.getAttribute(SessionAttribute.NAME);
    OrderServiceImpl orderService = new OrderServiceImpl();
    UserServiceImpl userService = new UserServiceImpl();
    Long userId = null;
    try {
      userId = userService.findUserByLogin(username).get().getModelId();
    } catch(ServiceException e){
      logger.error("Error occurred while finding a user", e);
      throw new CommandException(e);
    }
  }
}
