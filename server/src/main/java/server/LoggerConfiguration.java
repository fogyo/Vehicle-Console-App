package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LoggerConfiguration {
    private final Logger logger;

    public LoggerConfiguration(Class<?> clazz) {
        this.logger = LogManager.getLogger(clazz);
    }

    public void writeInfo(String message) {
        if (logger.isInfoEnabled()) {
            logger.info(message);
        }
    }
    
    public void writeDebug(String message) {
        logger.debug(message);
    }
    
    public void writeError(String message) {
        logger.error(message);
    }
    
    public void writeWarn(String message) {
        logger.warn(message);
    }
}