package com.gavruseva.webapp.service.impl;

import com.gavruseva.webapp.dao.impl.OrderDao;
import com.gavruseva.webapp.exception.DAOException;
import com.gavruseva.webapp.exception.ServiceException;
import com.gavruseva.webapp.model.Order;
import com.gavruseva.webapp.service.OrderService;
import com.gavruseva.webapp.validator.OrderValidator;
import com.gavruseva.webapp.validator.impl.OrderValidatorImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OrderServiceImpl implements OrderService {
  private static final Logger logger = LogManager.getLogger();
  private static final OrderValidator validator = new OrderValidatorImpl();

  @Override
  public Integer saveOrder(Order order) throws ServiceException {
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
