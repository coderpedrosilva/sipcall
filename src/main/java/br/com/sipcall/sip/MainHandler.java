package br.com.sipcall.sip;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.sip.Proxy;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.annotation.Bye;
import javax.servlet.sip.annotation.Cancel;
import javax.servlet.sip.annotation.ErrorResponse;
import javax.servlet.sip.annotation.Invite;
import javax.servlet.sip.annotation.Options;
import javax.servlet.sip.annotation.SipApplication;
import javax.servlet.sip.annotation.SipServlet;
import javax.servlet.sip.annotation.SuccessResponse;
import javax.servlet.sip.SipURI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationContext;

import java.io.IOException;
import static javax.servlet.sip.ConcurrencyMode.APPLICATIONSESSION;

import br.com.sipcall.service.CallService;

@SipServlet(name = "main", loadOnStartup = 1)
@SipApplication(name = "sipcall", sessionTimeout = 120, distributable = true, concurrencyMode = APPLICATIONSESSION, mainServlet = "main")
public class MainHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainHandler.class);

    @Inject
    SipFactory sipFactory;

    static private ApplicationContext ac = null;
    static private CallService service = null;

    @Invite
    public void handleInvite(SipServletRequest request) throws ServletException, IOException {
        LOGGER.info("INVITE received, original proxy destination " + request.getRequestURI());
        SipURI uri = (SipURI) request.getRequestURI().clone();
        uri.setHost("192.168.1.15");
        uri.setPort(5080);
        request.setRequestURI((SipURI) uri);
        LOGGER.info("INVITE received, new proxy destination " + request.getRequestURI());

        // App composition: set route so that the proxy is invoked next
        if (ac == null) {
            ac = (ApplicationContext) request.getServletContext().getAttribute("applicationContext");
            if (ac != null) {
                LOGGER.info("Saved ApplicationContext");
                service = (CallService) ac.getBean(CallService.class);
                if (service != null) {
                    LOGGER.info("Saved ChamadaService service");
                } else {
                    LOGGER.info("Failed to get ChamadaService");
                }
            } else {
                LOGGER.info("Failed to get ApplicationContext");
            }
        }

        if (service != null) {
            LOGGER.info("Got ChamadaService service");
            String from = request.getFrom().getValue();
            from = from.substring(from.indexOf(":") + 1, from.indexOf("@"));
            String to = request.getTo().getValue();
            to = to.substring(to.indexOf(":") + 1, to.indexOf("@"));
            LOGGER.info("From:[ " + from + " ]");
            LOGGER.info("To:[ " + to + " ]");
            service.registerCall(from, to); // Registrar a chamada sem usar o pin
        } else {
            LOGGER.info("Failed to get TesteService");
        }

        Proxy p = request.getProxy();
        p.setRecordRoute(true);
        p.setRecurse(true);
        p.setSupervised(true);
        p.proxyTo(request.getRequestURI());
    }

    @Bye
    public void handleBye(SipServletRequest request) throws ServletException, IOException {
        LOGGER.info("BYE received");
        if (ac == null) {
            ac = (ApplicationContext) request.getServletContext().getAttribute("applicationContext");
            if (ac != null) {
                LOGGER.info("Saved ApplicationContext");
                service = (CallService) ac.getBean(CallService.class);
                if (service != null) {
                    LOGGER.info("Saved ChamadaService service");
                } else {
                    LOGGER.info("Failed to get ChamadaService");
                }
            } else {
                LOGGER.info("Failed to get ApplicationContext");
            }
        }

        if (service != null) {
            LOGGER.info("Got ChamadaService service");
            String from = request.getFrom().getValue();
            from = from.substring(from.indexOf(":") + 1, from.indexOf("@"));
            String to = request.getTo().getValue();
            to = to.substring(to.indexOf(":") + 1, to.indexOf("@"));
            LOGGER.info("From:[ " + from + " ]");
            LOGGER.info("To:[ " + to + " ]");
            service.unregisterCall(from, to);
        } else {
            LOGGER.info("Failed to get ChamadaService");
        }
    }

    @Cancel
    public void handleCancel(SipServletRequest request) throws ServletException, IOException {
        LOGGER.info("CANCEL received");
        if (ac == null) {
            ac = (ApplicationContext) request.getServletContext().getAttribute("applicationContext");
            if (ac != null) {
                LOGGER.info("Saved ApplicationContext");
                service = (CallService) ac.getBean(CallService.class);
                if (service != null) {
                    LOGGER.info("Saved ChamadaService service");
                } else {
                    LOGGER.info("Failed to get ChamadaService");
                }
            } else {
                LOGGER.info("Failed to get ApplicationContext");
            }
        }

        if (service != null) {
            LOGGER.info("Got ChamadaService service");
            String from = request.getFrom().getValue();
            from = from.substring(from.indexOf(":") + 1, from.indexOf("@"));
            String to = request.getTo().getValue();
            to = to.substring(to.indexOf(":") + 1, to.indexOf("@"));
            LOGGER.info("From:[ " + from + " ]");
            LOGGER.info("To:[ " + to + " ]");
            service.unregisterCall(from, to);
        } else {
            LOGGER.info("Failed to get ChamadaService");
        }
    }

    @SuccessResponse
    public void handleSuccessResponse(SipServletResponse res) {
        int status = res.getStatus();
        LOGGER.info("RESPONSE received status=" + status);
        String m = res.getRequest().getMethod();
        LOGGER.info("RESPONSE received 200OK for METHOD=" + m);
        if ("INVITE".equals(m)) {
            LOGGER.info("RESPONSE 200OK for INVITE received");
            if (service != null) {
                LOGGER.info("Got TesteService service");
                String from = res.getFrom().getValue();
                from = from.substring(from.indexOf(":") + 1, from.indexOf("@"));
                String to = res.getTo().getValue();
                to = to.substring(to.indexOf(":") + 1, to.indexOf("@"));
                LOGGER.info("From:[ " + from + " ]");
                LOGGER.info("To:[ " + to + " ]");
                if (service.answerCall(from, to)) {
                    LOGGER.info("Success to answerCall service");
                } else {
                    LOGGER.info("Failed to answerCall service");
                }
            } else {
                LOGGER.info("Failed to get ChamadaService");
            }
        } else if ("BYE".equals(m) || "CANCEL".equals(m)) {
            LOGGER.info("RESPONSE 200OK for BYE or CANCEL received");
            if (service != null) {
                LOGGER.info("Got ChamadaService service");
                String from = res.getFrom().getValue();
                from = from.substring(from.indexOf(":") + 1, from.indexOf("@"));
                String to = res.getTo().getValue();
                to = to.substring(to.indexOf(":") + 1, to.indexOf("@"));
                LOGGER.info("From:[ " + from + " ]");
                LOGGER.info("To:[ " + to + " ]");
                service.unregisterCall(from, to);
            } else {
                LOGGER.info("Failed to get ChamadaService");
            }
        }
    }

    @ErrorResponse
    public void handleErrorResponse(SipServletResponse res) {
        int status = res.getStatus();
        LOGGER.info("ERROR RESPONSE received status=" + status);
        String m = res.getRequest().getMethod();
        LOGGER.info("ERROR RESPONSE received METHOD=" + m);
        if ("INVITE".equals(m)) {
            LOGGER.info("ERROR RESPONSE for INVITE received with status=" + status);
            if (service != null) {
                LOGGER.info("Got ChamadaService service");
                String from = res.getFrom().getValue();
                from = from.substring(from.indexOf(":") + 1, from.indexOf("@"));
                String to = res.getTo().getValue();
                to = to.substring(to.indexOf(":") + 1, to.indexOf("@"));
                LOGGER.info("From:[ " + from + " ]");
                LOGGER.info("To:[ " + to + " ]");
                service.unregisterCall(from, to);
            } else {
                LOGGER.info("Failed to get ChamadaService");
            }
        }
    }

    @Options
    public void handleOptions(SipServletRequest request) throws ServletException, IOException {
        SipServletResponse resp = request.createResponse(SipServletResponse.SC_OK);
        resp.send();
    }
}
