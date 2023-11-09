package cdw.springboot.gatekeeper.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* cdw.springTraining.gatekeeper.controller.*.*(..))")
    public Object controllerLogging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{

        String method = proceedingJoinPoint.getSignature().toShortString();
        Object[] args = proceedingJoinPoint.getArgs();
        String val = args.getClass().getName();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
        String datetime = dateFormat.format(new Date().getTime());
        Object result = null;
        logger.info("\nMethod : "+method+"\nArguments : "+args+"\nTime : "+datetime);

        try
        {
            result = proceedingJoinPoint.proceed();
        }
        catch (Exception exception) {
            logger.error(exception.getMessage());
            throw exception;
        }

        logger.info("\nReturn value : "+result);
        return result;
    }
}
