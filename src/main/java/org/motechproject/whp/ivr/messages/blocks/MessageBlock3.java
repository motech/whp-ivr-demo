package org.motechproject.whp.ivr.messages.blocks;


import org.motechproject.ivr.kookoo.KooKooIVRContext;
import org.motechproject.ivr.kookoo.KookooIVRResponseBuilder;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.domain.IVRAdherence;
import org.motechproject.whp.ivr.domain.IVRAdherenceResponse;

import java.util.Iterator;
import java.util.List;

public class MessageBlock3 {
    public static final String completionMessage1 = "001_03_01_completionMessage1";
    public static final String replayAdherence = "001_03_02_replayAdherence";
    public static final String adherence = "001_03_04_adherence";
    public static final String skipped = "001_03_06_completionMessage2";
    public static final String nextPatient = "001_03_07_nextPatient";

    public BlockCompleteStatus buildMessage(KookooIVRResponseBuilder ivrResponseBuilder, List<String> patientIds, KooKooIVRContext kooKooIVRContext, IVRAdherenceResponse adherenceResponse) {
        ivrResponseBuilder.withPlayAudios(completionMessage1);
        ivrResponseBuilder.withPlayAudios(replayAdherence);

        playMessagesForAllPatients(ivrResponseBuilder, patientIds, adherenceResponse);
        return BlockCompleteStatus.Complete;
    }

    private void playMessagesForAllPatients(KookooIVRResponseBuilder ivrResponseBuilder, List<String> patientIds, IVRAdherenceResponse adherenceResponse) {
        for (int i = 0; i < patientIds.size(); i++) {
            String patientId = patientIds.get(i);
            List<String> allNumberFileNames = WHPIVRMessage.getAllFileNames(patientId);
            ivrResponseBuilder.withPlayAudios(allNumberFileNames.toArray(new String[allNumberFileNames.size()]));
            ivrResponseBuilder.withPlayAudios(adherence);

            String adherence = adherenceForPatient(patientId, adherenceResponse);
            processSkipMessage(ivrResponseBuilder, adherence);
            if((i+1) < patientIds.size())
                ivrResponseBuilder.withPlayAudios(nextPatient);
        }
    }

    private void processSkipMessage(KookooIVRResponseBuilder ivrResponseBuilder, String adherence) {
        if(adherence.equals("*"))
            ivrResponseBuilder.withPlayAudios(skipped);
        else {
            try{
                int adherenceNo = Integer.parseInt(adherence);
                ivrResponseBuilder.withPlayAudios(WHPIVRMessage.getNumberFilename(adherenceNo));
            }catch(Exception e){
            }
        }
    }

    private String adherenceForPatient(String patientId, IVRAdherenceResponse adherenceResponse) {
        List<IVRAdherence> adherences = adherenceResponse.getAdherences();
        Iterator<IVRAdherence> iterator = adherences.iterator();
        while(iterator.hasNext()){
            IVRAdherence ivrAdherence = iterator.next();
            if(ivrAdherence.getPatientId().equals(patientId))
                return  ivrAdherence.getAdherence();
        }
        return "0";
    }
}
