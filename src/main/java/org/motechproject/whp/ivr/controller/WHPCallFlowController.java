package org.motechproject.whp.ivr.controller;


import org.apache.log4j.Logger;
import org.motechproject.decisiontree.model.Tree;
import org.motechproject.ivr.kookoo.KooKooIVRContext;
import org.motechproject.ivr.kookoo.extensions.CallFlowController;
import org.motechproject.whp.ivr.domain.CallState;
import org.motechproject.whp.ivr.WHPIVRContext;
import org.motechproject.whp.ivr.common.ControllerURLs;
import org.springframework.stereotype.Component;

@Component
public class WHPCallFlowController implements CallFlowController{
    private static Logger logger = Logger.getLogger(WHPCallFlowController.class);

    @Override
    public String urlFor(KooKooIVRContext kooKooIVRContext)  {
        WHPIVRContext whpIVRContext = new WHPIVRContext(kooKooIVRContext);
        CallState callState = whpIVRContext.callState();
        System.out.println("Call State "+callState.name());
        logger.info("Call State "+callState.name());
        if (callState.equals(CallState.STARTED)) return ControllerURLs.OUTBOX_URL;
        if (whpIVRContext.isDialState()) return ControllerURLs.DIAL_URL;
        if (callState.equals(CallState.OUTBOX)) return ControllerURLs.OUTBOX_URL;
        if (callState.equals(CallState.END_OF_FLOW)) return ControllerURLs.MENU_REPEAT;

        return null;
    }

    @Override
    public String decisionTreeName(KooKooIVRContext kooKooIVRContext) {
        return null;
    }

    @Override
    public Tree getTree(String s, KooKooIVRContext kooKooIVRContext) {
        return null;
    }

    @Override
    public void treeComplete(String s, KooKooIVRContext kooKooIVRContext) {

    }
}
