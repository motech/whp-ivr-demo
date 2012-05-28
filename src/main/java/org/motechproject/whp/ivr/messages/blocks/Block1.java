package org.motechproject.whp.ivr.messages.blocks;


import org.motechproject.ivr.kookoo.KookooIVRResponseBuilder;
import org.motechproject.whp.ivr.WHPIVRMessage;

import java.util.List;

public class Block1 {
    public static final String  welcome_message= "001_01_01_welcomeMessage";
    public static final String  instructional_message_1= "001_01_02_instructionalMessage1";
    public static final String  instructional_message_2= "001_01_04_instructionalMessage2";

    public void buildWelcomeMessage(KookooIVRResponseBuilder ivrResponseBuilder, List<String> patientIds) {
        ivrResponseBuilder.withPlayAudios(welcome_message);
        ivrResponseBuilder.withPlayAudios(instructional_message_1);
        ivrResponseBuilder.withPlayAudios(WHPIVRMessage.getNumberFilename(patientIds.size()));
        ivrResponseBuilder.withPlayAudios(instructional_message_2);
    }
}
