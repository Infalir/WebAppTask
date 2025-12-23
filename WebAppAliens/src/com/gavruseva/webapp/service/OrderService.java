package com.gavruseva.webapp.service;

import com.gavruseva.webapp.exception.ServiceException;
import com.gavruseva.webapp.model.Order;

public interface OrderService {
  Integer saveOrder(String username, Long imageId, Long inkId, String bodyPart) throws ServiceException;
}
