package org.motechproject.whp.ivr.messages;

import org.motechproject.ivr.kookoo.KooKooIVRContext;
import org.motechproject.ivr.kookoo.KookooIVRResponseBuilder;
import org.motechproject.whp.ivr.WHPIVRContext;
import org.motechproject.whp.ivr.domain.IVRAdherenceResponse;
import org.motechproject.whp.ivr.messages.blocks.*;
import org.motechproject.whp.ivr.outbox.context.OutboxContext;

import java.util.List;

public class MessageController {

    private Block1 ivrMessageBlock1;
    private Block2 ivrMessageBlock2;
    private Block3 ivrMessageBlock3;
    private Block4 ivrMessageBlock4;
    public MessageController(){
         ivrMessageBlock1 = new Block1();
         ivrMessageBlock2 = new Block2();
         ivrMessageBlock3 = new Block3();
         ivrMessageBlock4 = new Block4();
    }

    public void routeMessage(KookooIVRResponseBuilder ivrResponseBuilder, List<String> patientIds, KooKooIVRContext kooKooIVRContext, IVRAdherenceResponse adherenceResponse, boolean validInput) {

        String lastPlayedBlockId = kooKooIVRContext.cookies().getValue(OutboxContext.LAST_PLAYED_BLOCK_ID);
        
        if(lastPlayedBlockId.equals(WHPIVRContext.BLOCK_2))   {
            BlockCompleteStatus blockCompleteStatus = ivrMessageBlock2.buildMessage(ivrResponseBuilder, patientIds, kooKooIVRContext, validInput);
            if(blockCompleteStatus.equals(BlockCompleteStatus.Complete)) {
                routeToBlock3(ivrResponseBuilder,patientIds,kooKooIVRContext,adherenceResponse);
                routeToBlock4(ivrResponseBuilder);
            }
        }
    }


    private void routeToBlock3(KookooIVRResponseBuilder ivrResponseBuilder, List<String> patientIds, KooKooIVRContext kooKooIVRContext, IVRAdherenceResponse adherenceResponse) {
        ivrMessageBlock3.buildMessage(ivrResponseBuilder, patientIds, kooKooIVRContext, adherenceResponse);
    }

    private void routeToBlock4(KookooIVRResponseBuilder ivrResponseBuilder) {
        ivrMessageBlock4.buildMessage(ivrResponseBuilder);
    }

    public void buildWelcomeMessage(KookooIVRResponseBuilder ivrResponseBuilder, List<String> patientIds) {
        ivrMessageBlock1.buildWelcomeMessage(ivrResponseBuilder,patientIds);
    }

    public void buildStartingIntructionWithPatientId(KookooIVRResponseBuilder ivrResponseBuilder, List<String> patientIds, KooKooIVRContext kooKooIVRContext) {
        ivrMessageBlock2.buildStartingIntructionWithPatientId(ivrResponseBuilder,patientIds,kooKooIVRContext);
    }
}
