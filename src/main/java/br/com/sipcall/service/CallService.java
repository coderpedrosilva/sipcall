package br.com.sipcall.service;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import br.com.sipcall.model.CallKey;

@Service
public class CallService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallService.class);

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private ApplicationContext applicationContext;

    private final HashMap<CallKey, HashMap<String, String>> calls = new HashMap<>();

    @PostConstruct
    public void init() {
        LOGGER.info("CallService init method called");
        if (servletContext != null) {
            LOGGER.info("Servlet context not null");
            if (applicationContext != null) {
                LOGGER.info("Application context not null");
                servletContext.setAttribute("applicationContext", applicationContext);
            } else {
                LOGGER.info("Application context null");
            }
        } else {
            LOGGER.info("Servlet context null");
        }
    }

    public String returnService() {
        LOGGER.info("returnService called...");
        return "Teste Weblogic com multiModules e service e @Autowired";
    }

    public synchronized void unregisterCall(String calling, String called) {
        LOGGER.info("unregisterCall called for calling=" + calling + " and called=" + called);
        calls.remove(new CallKey(calling, called));
    }

    public synchronized void registerCall(String calling, String called) {
        LOGGER.info("registerCall called for calling=" + calling + " and called=" + called);
        HashMap<String, String> callData = new HashMap<>();
        calls.put(new CallKey(calling, called), callData);
    }

    public synchronized boolean answerCall(String calling, String called) {
        LOGGER.info("answerCall called for calling=" + calling + " and called=" + called);
        CallKey key = new CallKey(calling, called);
        LOGGER.info("answerCall got key");
        HashMap<String, String> callData = calls.get(key);
        if (callData != null) {
            LOGGER.info("answerCall got callData");
            callData.put("answered", "200ok");
            return true;
        } else {
            LOGGER.info("validateInvite call no exists");
            return false;
        }
    }

    public boolean validateInvite(String calling, String called) {
        LOGGER.info("validateInvite called for calling=" + calling + " ,called=" + called);
        CallKey key = new CallKey(calling, called);
        LOGGER.info("validateInvite got key");
        HashMap<String, String> callData = calls.get(key);
        if (callData != null) {
            LOGGER.info("validateInvite call exists");
            return true;
        } else {
            LOGGER.info("validateInvite call no exists");
            return false;
        }
    }

    public boolean validate200Ok(String calling, String called) {
        LOGGER.info("validate200Ok called for calling=" + calling + " ,called=" + called);
        CallKey key = new CallKey(calling, called);
        LOGGER.info("validate200Ok got key");
        HashMap<String, String> callData = calls.get(key);
        if (callData != null) {
            LOGGER.info("validate200Ok got callData");
            String answered = callData.get("answered");
            if (answered != null) {
                LOGGER.info("validate200Ok, call answered = " + answered);
                return true;
            } else {
                LOGGER.info("validate200Ok, call not answered");
                return false;
            }
        } else {
            LOGGER.info("validate200Ok call no exists");
            return false;
        }
    }

}
