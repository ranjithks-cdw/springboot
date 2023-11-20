package cdw.springboot.gatekeeper.aspect;

import cdw.springboot.gatekeeper.exceptions.GatekeeperException;
import jakarta.validation.ConstraintViolationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;

import java.text.SimpleDateFormat;
import java.util.Date;

import static cdw.springboot.gatekeeper.constants.AppConstants.*;

/**
 * Logger for the application. Uses AOP interfaces
 */
@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around(PATH_CONTROLLERS +" || " + PATH_SERVICES +" || " + PATH_REPOSITORIES)
    public Object loggingAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{

        String method = proceedingJoinPoint.getSignature().toShortString();
        Object[] args = proceedingJoinPoint.getArgs();
        String val = args.getClass().getName();

        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE_TIME);
        String datetime = dateFormat.format(new Date().getTime());
        Object result = null;
        logger.info("\nMethod : "+method+"\nArguments : "+args+"\nTime : "+datetime);

        try {
            result = proceedingJoinPoint.proceed();
        } catch (GatekeeperException e) {
            logger.error(e.getMessage());
            throw new GatekeeperException(e.getMessage(), e.getHttpStatus());
        } catch (TransactionSystemException ex) {
            logger.error(ex.getMessage());
            if (ex.getRootCause() instanceof ConstraintViolationException) {
                throw new ConstraintViolationException(((ConstraintViolationException) ex.getRootCause()).getConstraintViolations());
            }
            throw new GatekeeperException(ex.getMessage());
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            throw exception;
        }

        logger.info("\nReturn value : "+result);
        return result;
    }
}
