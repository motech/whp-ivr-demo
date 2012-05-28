package org.motechproject.whp.ivr;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.motechproject.ivr.kookoo.KooKooIVRContext;
import org.motechproject.ivr.kookoo.KookooRequest;
import org.motechproject.ivr.kookoo.eventlogging.CallEventConstants;
import org.motechproject.ivr.model.CallDirection;
import org.motechproject.util.Cookies;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

public class WHPIVRContext {
    static final String CALLER_ID = "caller_id";
    static final String NUMBER_OF_ATTEMPTS = "number_of_attempts";
    private static final String SYMPTOM_REPORTING_TREE = "symptom_reporting_tree";
    public static final String PATIENT_ID = "patient_id";
    private static final String CALL_START_TIME = "call_time";
    public static final String SWITCH_TO_DIAL_STATE = "switch_to_dial_state";

    protected KookooRequest kookooRequest;
    protected HttpServletRequest httpRequest;
    private Cookies cookies;
    private KooKooIVRContext kooKooIVRContext;
    public static final String PROVIDER_NO = "providerNo";

    public static final String BLOCK_2 = "BLOCK2";



    protected WHPIVRContext() {
    }

    public WHPIVRContext(KooKooIVRContext kooKooIVRContext) {
        this(kooKooIVRContext.kooKooRequest(), kooKooIVRContext.httpRequest(), kooKooIVRContext.cookies());
        this.kooKooIVRContext = kooKooIVRContext;
    }

    WHPIVRContext(KookooRequest kookooRequest, HttpServletRequest httpRequest, Cookies cookies) {
        this.kookooRequest = kookooRequest;
        this.httpRequest = httpRequest;
        this.cookies = cookies;
    }

    public KooKooIVRContext getKooKooIVRContext() {
        return kooKooIVRContext;
    }

    public void initialize() {
        callerId(requestedCallerId());
        setInSession(NUMBER_OF_ATTEMPTS, "0");
    }

    private void setInSession(String name, Object value) {
        httpRequest.getSession().setAttribute(name, value);
    }

    public void addLastCompletedTreeToListOfCompletedTrees(String treeName) {
        kooKooIVRContext.addToListOfCompletedTrees(treeName);
    }

    public List<String> getListOfCompletedTrees() {
        return kooKooIVRContext.getListOfCompletedTrees();
    }

    public boolean hasTraversedTree(String treeName) {
        return getListOfCompletedTrees() != null && getListOfCompletedTrees().contains(treeName);
    }

    public boolean hasTraversedAnyTree() {
        return CollectionUtils.isNotEmpty(getListOfCompletedTrees());
    }

    protected void callerId(String callerId) {
        setInSession(CALLER_ID, callerId);
    }

    public String dtmfInput() {
        return kookooRequest.getInput();
    }

    public String callerId() {
        return fromSession(CALLER_ID);
    }

    private String fromSession(String name) {
        return (String) httpRequest.getSession().getAttribute(name);
    }

    public int numberOfLoginAttempts() {
        String numberOfAttempts = fromSession(NUMBER_OF_ATTEMPTS);
        return Integer.parseInt(StringUtils.isEmpty(numberOfAttempts) ? "0" : numberOfAttempts);
    }

    public CallState callState() {
        String value = fromSession(CallEventConstants.CALL_STATE);
        return (value == null) ? CallState.STARTED : Enum.valueOf(CallState.class, value);
    }

    public void numberOfLoginAttempts(int numberOfAttempts) {
        setInSession(NUMBER_OF_ATTEMPTS, Integer.toString(numberOfAttempts));
    }

    public String callId() {
        return kooKooIVRContext.callId();
    }

    public String callDetailRecordId() {
        return kooKooIVRContext.callDetailRecordId();
    }

    public CallDirection callDirection() {
        return kookooRequest.getCallDirection();
    }

    public boolean isIncomingCall() {
        return callDirection().equals(CallDirection.Inbound);
    }

    public boolean isOutgoingCall() {
        return callDirection().equals(CallDirection.Outbound);
    }

    public DateTime callStartTime() {
        return (DateTime) httpRequest.getSession().getAttribute(CALL_START_TIME);
    }

    public String patientDocumentId() {
        return fromSession(PATIENT_ID);
    }


    public void callState(CallState callState) {
        setInSession(CallEventConstants.CALL_STATE, callState.toString());
        log(CallEventConstants.CALL_STATE, callState.toString());
    }

    private void log(String key, String value) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(key, value);
        setDataToLog(map);
    }


    public String requestedCallerId() {
        return kookooRequest.getCid();
    }

    public boolean isDialState() {
        return Boolean.valueOf(cookies.getValue(SWITCH_TO_DIAL_STATE));
    }

    public String preferredLanguage() {
        return kooKooIVRContext.preferredLanguage();
    }

    public void currentDecisionTreePath(String path) {
        kooKooIVRContext.currentDecisionTreePath(path);
    }

    public void symptomReportingTree(String symptomReportingTree) {
        setInSession(SYMPTOM_REPORTING_TREE, symptomReportingTree);
    }

    public String symptomReportingTree() {
        return fromSession(SYMPTOM_REPORTING_TREE);
    }



    public void setDataToLog(HashMap<String, String> map) {
        kooKooIVRContext.dataToLog(map);
    }

    public boolean isAnswered() {
        return kooKooIVRContext.isAnswered();
    }

    protected Cookies cookies() {
        return cookies;
    }

}
