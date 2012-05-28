package org.motechproject.whp.ivr.repository;

import junit.framework.Assert;
import org.joda.time.DateTime;
import org.junit.Test;
import org.motechproject.whp.ivr.domain.IVRAdherence;
import org.motechproject.whp.ivr.domain.IVRAdherenceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = "classpath*:/application*Context.xml")
public class AllIVRResponsesTest extends SpringIntegrationTest {

    @Autowired
    AllIVRResponses allIVRResponses;

    @Test
    public void testAdd() throws Exception {

        IVRAdherenceResponse adherenceResponse= new IVRAdherenceResponse();
        adherenceResponse.setCallTime(new DateTime());
        adherenceResponse.setProviderId("adcd12345");

        IVRAdherence adherence = new IVRAdherence("p100123","5");
        adherenceResponse.add(adherence);

        allIVRResponses.add(adherenceResponse);

        Assert.assertNotNull(allIVRResponses.findById(adherenceResponse.getId()));

        allIVRResponses.remove(adherenceResponse);

    }

    public void testFindByAdherenceResponseId() throws Exception {

    }


}
