package org.motechproject.whp.ivr.controller;

import org.motechproject.ivr.kookoo.KooKooIVRContext;
import org.motechproject.ivr.kookoo.KookooIVRResponseBuilder;
import org.motechproject.whp.ivr.WHPIVRContext;
import org.motechproject.whp.ivr.domain.IVRAdherenceResponse;
import org.motechproject.whp.ivr.messages.blocks.*;
import org.motechproject.whp.ivr.outbox.context.OutboxContext;

import java.util.List;

public class MessageRoutingController {

    private WelcomeMessageBlock welcomeMessageBlock;
    private AdherenceCaptureBlock adherenceCaptureBlock;
    private CompletionSummaryBlock completionSummaryBlock;
    private CompletionBlock completionBlock;
    public MessageRoutingController(){
         welcomeMessageBlock = new WelcomeMessageBlock();
         adherenceCaptureBlock = new AdherenceCaptureBlock();
         completionSummaryBlock = new CompletionSummaryBlock();
         completionBlock = new CompletionBlock();
    }

    public void routeMessage(KookooIVRResponseBuilder ivrResponseBuilder, List<String> patientIds, KooKooIVRContext kooKooIVRContext, IVRAdherenceResponse adherenceResponse, boolean validInput) {

        String lastPlayedBlockId = kooKooIVRContext.cookies().getValue(OutboxContext.LAST_PLAYED_BLOCK_ID);
        
        if(lastPlayedBlockId.equals(WHPIVRContext.BLOCK_ADHERENCE_CAPTURE))   {
            BlockCompleteStatus blockCompleteStatus = adherenceCaptureBlock.buildMessage(ivrResponseBuilder, patientIds, kooKooIVRContext, validInput);
            if(blockCompleteStatus.equals(BlockCompleteStatus.Complete)) {
                routeToCompletionSummaryBlock(ivrResponseBuilder, patientIds, kooKooIVRContext, adherenceResponse);
                routeToCompletionBlock(ivrResponseBuilder);
            }
        }
    }


    private void routeToCompletionSummaryBlock(KookooIVRResponseBuilder ivrResponseBuilder, List<String> patientIds, KooKooIVRContext kooKooIVRContext, IVRAdherenceResponse adherenceResponse) {
        completionSummaryBlock.buildMessage(ivrResponseBuilder, patientIds, kooKooIVRContext, adherenceResponse);
    }

    private void routeToCompletionBlock(KookooIVRResponseBuilder ivrResponseBuilder) {
        completionBlock.buildMessage(ivrResponseBuilder);
    }

    public void buildWelcomeMessage(KookooIVRResponseBuilder ivrResponseBuilder, List<String> patientIds) {
        welcomeMessageBlock.buildWelcomeMessage(ivrResponseBuilder, patientIds);
    }

    public void buildStartingIntructionWithPatientId(KookooIVRResponseBuilder ivrResponseBuilder, List<String> patientIds, KooKooIVRContext kooKooIVRContext) {
        adherenceCaptureBlock.buildStartingIntructionWithPatientId(ivrResponseBuilder, patientIds, kooKooIVRContext);
    }
}
