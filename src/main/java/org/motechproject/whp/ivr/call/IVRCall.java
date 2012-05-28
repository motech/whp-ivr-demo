package org.motechproject.whp.ivr.call;

import org.motechproject.ivr.service.CallRequest;
import org.motechproject.ivr.service.IVRService;
import org.motechproject.whp.ivr.domain.IVRAdherenceResponse;
import org.motechproject.whp.ivr.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;

@Component
public class IVRCall {
    public static final String APPLICATION_URL = "application.url";
    protected IVRService ivrService;
    private Properties properties;

    @Autowired
    public IVRCall(IVRService ivrService, @Qualifier("ivrProperties") Properties properties) {
        this.ivrService = ivrService;
        this.properties = properties;
    }

    public void makeCall(IVRAdherenceResponse response, String callType, Map<String, String> params) {
        params.put(IVRService.EXTERNAL_ID, response.getResponseId());
        params.put(IVRService.CALL_TYPE, callType);
        CallRequest callRequest = new CallRequest(StringUtil.ivrMobilePhoneNumber(response.getProviderId()), params, getApplicationUrl());
        ivrService.initiateCall(callRequest);
    }

    public String getApplicationUrl() {
        return (String) properties.get(APPLICATION_URL);
    }
}