package com.du.easytools.common.word.RequestResponseContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author :  dush
 * @date :  2022/10/14  16:38
 * @Decription
 */
public class RequestResponseContext {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseContext.class);

    public static HttpServletRequest getRequest(){
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            logger.debug("currentThread is not exits RequestContext");
            throw new IllegalStateException("currentThread is not exits RequestContext");
        }
        return attrs.getRequest();
    }
}
