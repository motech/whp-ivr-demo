package org.motechproject.whp.ivr.messages.blocks;


import org.motechproject.ivr.kookoo.KooKooIVRContext;
import org.motechproject.ivr.kookoo.KookooIVRResponseBuilder;
import org.motechproject.whp.ivr.WHPIVRContext;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.outbox.context.OutboxContext;

import java.util.List;

public class Block2 {
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

        ivrResponseBuilder.withPlayAudios(enterAdherence);

        kooKooIVRContext.cookies().add(OutboxContext.LAST_PLAYED_PATIENT_NO, "1");
        kooKooIVRContext.cookies().add(WHPIVRContext.PATIENT_ID, patientId);

        kooKooIVRContext.cookies().add(OutboxContext.LAST_PLAYED_VOICE_MESSAGE_ID, enterAdherence);
        kooKooIVRContext.cookies().add(OutboxContext.LAST_PLAYED_BLOCK_ID, WHPIVRContext.BLOCK_2);
    }

    public BlockCompleteStatus buildMessage(KookooIVRResponseBuilder ivrResponseBuilder, List<String> patientIds, KooKooIVRContext kooKooIVRContext, boolean validInput) {

        String lastPlayedpatientNo = kooKooIVRContext.cookies().getValue(OutboxContext.LAST_PLAYED_PATIENT_NO);
        String lastPlayedMessageId = kooKooIVRContext.cookies().getValue(OutboxContext.LAST_PLAYED_VOICE_MESSAGE_ID);

        int currentMessageId = Integer.valueOf(lastPlayedpatientNo) + 1;

        if(!validInput) {
            currentMessageId = currentMessageId--;
        }

        if (currentMessageId <= patientIds.size()) {
            ivrResponseBuilder.withPlayAudios(patientList);
            ivrResponseBuilder.withPlayAudios(WHPIVRMessage.getNumberFilename(currentMessageId));
            String patientId = patientIds.get(currentMessageId - 1);
            List<String> allNumberFileNames = WHPIVRMessage.getAllFileNames(patientId);
            ivrResponseBuilder.withPlayAudios(allNumberFileNames.toArray(new String[allNumberFileNames.size()]));
            ivrResponseBuilder.withPlayAudios(enterAdherence).collectDtmfLength(1);

            kooKooIVRContext.cookies().add(WHPIVRContext.PATIENT_ID, patientId);
            kooKooIVRContext.cookies().add(OutboxContext.LAST_PLAYED_PATIENT_NO, String.valueOf(currentMessageId));
            kooKooIVRContext.cookies().add(OutboxContext.LAST_PLAYED_VOICE_MESSAGE_ID, enterAdherence);
            kooKooIVRContext.cookies().add(OutboxContext.LAST_PLAYED_BLOCK_ID, "BLOCK2");

            return BlockCompleteStatus.InProgress;
        }
        return BlockCompleteStatus.Complete;
    }


}
