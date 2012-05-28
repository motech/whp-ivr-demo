package org.motechproject.whp.ivr.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.motechproject.model.MotechBaseDataObject;

import java.util.ArrayList;
import java.util.List;

@Data
@TypeDiscriminator("doc.type == 'IVRAdherenceResponse'")
public class IVRAdherenceResponse extends MotechBaseDataObject {

   @JsonProperty("_id")
   private String responseId;

   @JsonProperty
   private String providerId;

   @JsonProperty
   private DateTime callTime;

   @JsonProperty
   private List<IVRAdherence> adherences = new ArrayList<IVRAdherence>(){};


    public String getResponseId() {
        return getId();
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public DateTime getCallTime() {
        return callTime;
    }

    public void setCallTime(DateTime callTime) {
        this.callTime = callTime;
    }

    public List<IVRAdherence> getAdherences() {
        return adherences;
    }

    public void setAdherences(List<IVRAdherence> adherences) {
        this.adherences = adherences;
    }


    public IVRAdherenceResponse(){};

    public void add(IVRAdherence adherence){
          adherences.add(adherence);
    }
}
