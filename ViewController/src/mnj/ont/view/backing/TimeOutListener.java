package mnj.ont.view.backing;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import oracle.adf.controller.v2.lifecycle.Lifecycle;
import oracle.adf.controller.v2.lifecycle.PagePhaseEvent;
import oracle.adf.controller.v2.lifecycle.PagePhaseListener;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

public class TimeOutListener implements PagePhaseListener {
    
    @SuppressWarnings("compatibility:-1406793285197613021")
    private static final long serialVersionUID = 1L;    
    public TimeOutListener() {
        super();
    }
    
    /**
       * This method determines whether session timed out or not,
       * If session timed out , redirects to corresponding Apps Login page
       * based on configured properties file
       * @param phaseEvent
       */
      public void beforePhase(PagePhaseEvent phaseEvent) {
      }    
    public void afterPhase(PagePhaseEvent pagePhaseEvent) {
           if (pagePhaseEvent.getPhaseId() == Lifecycle.PREPARE_RENDER_ID) {
               FacesContext facesCtx = FacesContext.getCurrentInstance();
               ExternalContext extCtx = facesCtx.getExternalContext();
               // Get HttpSession instance
               HttpSession session = (HttpSession)extCtx.getSession(false);
               // Get HttpServletRequest instance
               HttpServletRequest req =
                   (HttpServletRequest)extCtx.getRequest();
               if (session != null) {
                   int secsTimeout = session.getMaxInactiveInterval();
                   if (secsTimeout > 0) {
                       String appURL ="http://test.adfexpiresession/";
                       secsTimeout += 2;
                       String jsCall =
                           "document.acmeStartClientSessionTimers = function()\n" +
                           "{\n" +
                           "  if(document.acmeSessionTimeoutTimer != null)\n" +
                           "    window.clearTimeout(document.acmeSessionTimeoutTimer);\n" +
                           "\n" +
                           "  document.acmeSessionTimeoutTimer = window.setTimeout(\"document.acmeClientSessionTimeout();\", " +
                           secsTimeout * 100000 + ");\n" +
                           "\n" +
                           "}\n" +
                           "document.acmeStartClientSessionTimers();\n" +
                           "\n" +
                           "document.acmeClientSessionTimeout = function()\n" +
                           "{\n" +
                           "    window.location.href = '" + appURL + "' \n" +
                           "}";

                       ExtendedRenderKitService rks =
                           Service.getRenderKitService(facesCtx,
                                                       ExtendedRenderKitService.class);
                       rks.addScript(facesCtx, jsCall);
                   }
               }
           }
       }

       public PhaseId getPhaseId() {
           return PhaseId.RESTORE_VIEW;  
           } 
    }

