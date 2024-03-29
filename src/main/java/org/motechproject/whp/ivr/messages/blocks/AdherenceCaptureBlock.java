package org.motechproject.whp.ivr.messages.blocks;


import org.motechproject.ivr.kookoo.KooKooIVRContext;
import org.motechproject.ivr.kookoo.KookooIVRResponseBuilder;
import org.motechproject.whp.ivr.WHPIVRContext;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.outbox.context.OutboxContext;

import java.util.List;

public class AdherenceCaptureBlock {
    public static final String instructional_message_3 = "001_02_01_instructionalMessage3";
    public static final String patientList = "001_02_02_patientList";   //"PatientNo"
    public static final String enterAdherence = "001_02_05_enterAdherence";

    public void buildStartingIntructionWithPatientId(KookooIVRResponseBuilder ivrResponseBuilder, List<String> patientIds, KooKooIVRContext kooKooIVRContext) {

        ivrResponseBuilder.withPlayAudios(instructional_message_3);
        ivrResponseBuilder.withPlayAudios(patientList);
        ivrResponseBuilder.withPlayAudios(WHPIVRMessage.getNumberFilename(1));

        String patientId = patientIds.get(0);
        List<String> allNumberFileNames = WHPIVRMessage.getAllFileNames(patientId);
        ivrResponseBuilder.withPlayAudios(allNumberFileNames.toArray(new String[allNumberFileNames.size()]));

        addCallStateDataToContext(kooKooIVRContext, patientId);
        ivrResponseBuilder.withPlayAudios(enterAdherence);

    }

    private void addCallStateDataToContext(KooKooIVRContext kooKooIVRContext, String patientId) {
        kooKooIVRContext.cookies().add(OutboxContext.LAST_PLAYED_PATIENT_NO, "1");
        kooKooIVRContext.cookies().add(WHPIVRContext.PATIENT_ID, patientId);

        kooKooIVRContext.cookies().add(OutboxContext.LAST_PLAYED_VOICE_MESSAGE_ID, enterAdherence);
        kooKooIVRContext.cookies().add(OutboxContext.LAST_PLAYED_BLOCK_ID, WHPIVRContext.BLOCK_ADHERENCE_CAPTURE);
    }

    public BlockCompleteStatus buildMessage(KookooIVRResponseBuilder ivrResponseBuilder, List<String> patientIds, KooKooIVRContext kooKooIVRContext, boolean validInput) {

        String lastPlayedpatientNo = kooKooIVRContext.cookies().getValue(OutboxContext.LAST_PLAYED_PATIENT_NO);

        int currentMessageId = Integer.valueOf(lastPlayedpatientNo) ;
        String patientId = patientIds.get(currentMessageId - 1);

        if(validInput) {
            currentMessageId++;
        }

        if (currentMessageId <= patientIds.size()) {
            ivrResponseBuilder.withPlayAudios(patientList);
            ivrResponseBuilder.withPlayAudios(WHPIVRMessage.getNumberFilename(currentMessageId));
            List<String> allNumberFileNames = WHPIVRMessage.getAllFileNames(patientId);
            ivrResponseBuilder.withPlayAudios(allNumberFileNames.toArray(new String[allNumberFileNames.size()]));
            ivrResponseBuilder.withPlayAudios(enterAdherence).collectDtmfLength(1);

            addCallStateDataForMessageToContext(kooKooIVRContext, currentMessageId, patientId);
            return BlockCompleteStatus.InProgress;
        }
        return BlockCompleteStatus.Complete;
    }

    private void addCallStateDataForMessageToContext(KooKooIVRContext kooKooIVRContext, int currentMessageId, String patientId) {
        kooKooIVRContext.cookies().add(WHPIVRContext.PATIENT_ID, patientId);
        kooKooIVRContext.cookies().add(OutboxContext.LAST_PLAYED_PATIENT_NO, String.valueOf(currentMessageId));
        kooKooIVRContext.cookies().add(OutboxContext.LAST_PLAYED_VOICE_MESSAGE_ID, enterAdherence);
        kooKooIVRContext.cookies().add(OutboxContext.LAST_PLAYED_BLOCK_ID, "BLOCK2");
    }


}
