package org.motechproject.whp.ivr.messages.blocks;

import org.motechproject.ivr.kookoo.KookooIVRResponseBuilder;

public class Block4 {
    public static final String  finalMessage= "001_04_01_completionMessage3";
    public static final String SIGNATURE_MUSIC = "signature_music";

    public void buildMessage(KookooIVRResponseBuilder ivrResponseBuilder) {
        ivrResponseBuilder.withPlayAudios(finalMessage).withPlayAudios(SIGNATURE_MUSIC);
    }
}
