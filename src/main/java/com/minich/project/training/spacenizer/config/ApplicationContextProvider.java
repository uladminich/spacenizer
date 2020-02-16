package com.minich.project.training.spacenizer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Hack to init javax.websocket instances with spring bean, example ServerWebSocket.ServerWebSocket()
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    @Autowired
    private ApplicationContext context;

    @PostConstruct
    public void init() {
        setApplicationContext(context);
    }

    private static class ApplicationContextHolder {

        private static final    InnerContextResource CONTEXT_PROV = new InnerContextResource();
        private ApplicationContextHolder() {
            super();
        }
    }

    private static final class InnerContextResource {

        private ApplicationContext context;

        private InnerContextResource(){
            super();
        }

        private void setContext(ApplicationContext context){
            this.context = context;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return ApplicationContextHolder.CONTEXT_PROV.context;
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) {
        ApplicationContextHolder.CONTEXT_PROV.setContext(ac);
    }
}
