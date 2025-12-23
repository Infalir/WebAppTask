package com.gavruseva.webapp.service.impl;

import com.gavruseva.webapp.dao.impl.OrderDao;
import com.gavruseva.webapp.exception.DAOException;
import com.gavruseva.webapp.exception.ServiceException;
import com.gavruseva.webapp.model.Order;
import com.gavruseva.webapp.service.ImageService;
import com.gavruseva.webapp.service.OrderService;
import com.gavruseva.webapp.service.UserService;
import com.gavruseva.webapp.validator.OrderValidator;
import com.gavruseva.webapp.validator.impl.OrderValidatorImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OrderServiceImpl implements OrderService {
  private static final Logger logger = LogManager.getLogger();
  private static final OrderValidator validator = new OrderValidatorImpl();
  private UserService userService = new UserServiceImpl();
  private ImageService imageService = new ImageServiceImpl();

  @Override
  public Integer saveOrder(String username, Long imageId, Long inkId, String bodyPart) throws ServiceException {
    Long userId = null;
    if(userService.findUserByLogin(username).isPresent()){
      userId = userService.findUserByLogin(username).get().getModelId();
    }
    Double price = imageService.findPriceById(imageId);
    Order order = new Order();
    order.setUserId(userId);
    order.setImageId(imageId);
    order.setInkId(inkId);
    order.setBodyPart(bodyPart);
    order.setOrderStatus(Order.OrderStatus.CREATED);
    order.setOrderPrice(price);
    if(!validator.isValid(order)){
      logger.warn("Order is not valid");
      return null;
    }
    try{
      return OrderDao.getInstance().insert(order);
    } catch (DAOException e){
      throw new ServiceException(e.getMessage(), e);
    }
  }
}
