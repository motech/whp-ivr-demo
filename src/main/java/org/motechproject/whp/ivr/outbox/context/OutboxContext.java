package org.motechproject.whp.ivr.outbox.context;

import org.motechproject.ivr.kookoo.KooKooIVRContext;
import org.motechproject.util.Cookies;
import org.motechproject.whp.ivr.WHPIVRContext;

import javax.servlet.http.HttpServletRequest;

// This class is created instead of using TAMAIVRContext because we might want to move Outbox IVR to platform
public class OutboxContext {
    private Cookies cookies;

    public static final String LAST_PLAYED_VOICE_MESSAGE_ID = "LastPlayedVoiceMessageID";
    private static final String OUTBOX_COMPLETED = "outboxCompleted";
    private KooKooIVRContext kooKooIVRContext;
    private HttpServletRequest request;
    public static final String LAST_PLAYED_BLOCK_ID = "lastPlayedBlockId";
    public static final String LAST_PLAYED_PATIENT_NO = "lastPlayedPatientNo";
    public static final String IVR_RESPONSE_ID = "iverResponseId";

    protected OutboxContext() {
    }

    public OutboxContext(KooKooIVRContext kooKooIVRContext) {
        this.kooKooIVRContext = kooKooIVRContext;
        this.cookies = kooKooIVRContext.cookies();
        this.request = kooKooIVRContext.httpRequest();
        cookies.add(OUTBOX_COMPLETED, Boolean.toString(false));
    }

    public String partyId() {
        return (String) request.getSession().getAttribute(WHPIVRContext.PATIENT_ID);
    }

    public String lastPlayedMessageId() {
        return cookies.getValue(LAST_PLAYED_VOICE_MESSAGE_ID);
    }

    public void lastPlayedMessageId(String messageId) {
        cookies.add(LAST_PLAYED_VOICE_MESSAGE_ID, messageId);
    }

    public String preferredLanguage() {
        return kooKooIVRContext.preferredLanguage();
    }

    public String callId() {
        return kooKooIVRContext.callId();
    }

    public boolean hasOutboxCompleted() {
        return Boolean.parseBoolean(cookies.getValue(OUTBOX_COMPLETED));
    }

    public void outboxCompleted() {
        cookies.add(OUTBOX_COMPLETED, Boolean.toString(true));
    }
}
