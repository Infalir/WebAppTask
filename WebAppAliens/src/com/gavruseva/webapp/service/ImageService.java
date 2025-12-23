package com.gavruseva.webapp.service;

import com.gavruseva.webapp.exception.ServiceException;

public interface ImageService {
  Double findPriceById(Long id) throws ServiceException;
}
