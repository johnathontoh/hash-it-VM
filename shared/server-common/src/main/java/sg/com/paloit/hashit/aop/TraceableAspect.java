package sg.com.paloit.hashit.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Aspect
@Order(value = 0)
@Component
public class TraceableAspect {
    private static final Logger LOG = LoggerFactory.getLogger(TraceableAspect.class);

    @Around("@annotation(sg.com.paloit.hashit.annotation.Traceable)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        LOG.info(point.getSignature().toShortString() + " with args: {}", (Object)point.getArgs());

        long start = System.currentTimeMillis();
        try {
            Object proceed = point.proceed();
            long executionTime = System.currentTimeMillis() - start;
            LOG.info(point.getSignature().toShortString() + " executed in " + executionTime + "ms, result: [{}]",
                    result(proceed));
            return proceed;
        } catch (Exception ex) {
            LOG.info(point.getSignature().toShortString() + " failed", ex);
            throw ex;
        }
    }

    static Object result(Object obj) {
        if (obj == null) {
            return "null";
        } else if (obj instanceof Collection) {
            return "collection size: " + ((Collection) obj).size();
        } else if (obj.getClass().isArray()) {
            return "array length: " + ((Object[]) obj).length;
        } else return obj;
    }
}
