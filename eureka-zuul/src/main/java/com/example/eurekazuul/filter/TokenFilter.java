package com.example.eurekazuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: MrWang
 * @date: 2018/9/11
 */

@Component
public class TokenFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(TokenFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String accessToken = request.getParameter("accessToken");
        if (accessToken!=null){
            if (accessToken.equals("chuanzige")){
                log.info("accessToken ok");
                return null;
            }else {
                log.warn("accessToken is error");
                requestContext.setSendZuulResponse(false);
                requestContext.setResponseStatusCode(401);
                return null;
            }
        }
        log.warn("accessToken is empty");
        requestContext.setSendZuulResponse(false);
        requestContext.setResponseStatusCode(401);
        return null;
    }
}
