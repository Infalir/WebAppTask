package com.gavruseva.webapp.service.impl;

import com.gavruseva.webapp.dao.impl.ImageDao;
import com.gavruseva.webapp.exception.DAOException;
import com.gavruseva.webapp.exception.ServiceException;
import com.gavruseva.webapp.model.Image;
import com.gavruseva.webapp.service.ImageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ImageServiceImpl implements ImageService {
  private static final Logger logger = LogManager.getLogger();

  @Override
  public Double findPriceById(Long id) throws ServiceException{
    try{
      if(ImageDao.getInstance().findById(id).isPresent()){
        return ImageDao.getInstance().findById(id).get().getPrice();
      }
      logger.warn("Such image doesn't exist");
      throw new ServiceException("Such image doesn't exist");
    } catch (DAOException e){
      throw new ServiceException(e);
    }
  }
}
