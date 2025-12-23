package com.gavruseva.webapp.command.order;

import com.gavruseva.webapp.command.Command;
import com.gavruseva.webapp.command.CommandResult;
import com.gavruseva.webapp.command.SessionAttribute;
import com.gavruseva.webapp.exception.CommandException;
import com.gavruseva.webapp.exception.ServiceException;
import com.gavruseva.webapp.page.Page;
import com.gavruseva.webapp.service.impl.OrderServiceImpl;
import com.gavruseva.webapp.service.impl.UserServiceImpl;
import static com.gavruseva.webapp.command.order.OrderParameters.*;
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
    Long imageId = Long.valueOf(request.getParameter(IMAGE_ID));
    Long inkId = Long.valueOf(request.getParameter(INK_ID));
    String bodyPart = request.getParameter(BODY_PART);

    OrderServiceImpl orderService = new OrderServiceImpl();

    try {
      int orderCount = orderService.saveOrder(username, imageId, inkId, bodyPart);
      if(orderCount != 0) {
        logger.info("Order has been created");
        return new CommandResult(Page.MAIN_PAGE.getPage(), true);
      }
      else {
        logger.info("Order has not been created, something went wrong");
        request.setAttribute(ERROR_ATTRIBUTE, ERROR_MESSAGE);
        return new CommandResult(Page.ORDER_PAGE.getPage(), false);
      }
    } catch(ServiceException e){
      logger.error("Error occurred while finding a user", e);
      request.setAttribute(ERROR_ATTRIBUTE, ERROR_MESSAGE);
      return new CommandResult(Page.ORDER_PAGE.getPage(), false);
    }
  }
}
