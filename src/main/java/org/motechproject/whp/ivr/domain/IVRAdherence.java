package org.motechproject.whp.ivr.domain;


import lombok.Data;

@Data
public class IVRAdherence {

    private String patientId;
    private String adherence;


    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getAdherence() {
        return adherence;
    }

    public void setAdherence(String adherence) {
        this.adherence = adherence;
    }


    public IVRAdherence(String lastPatientId, String userInput) {
        this.patientId = lastPatientId;
        this.adherence = userInput;
    }
    public IVRAdherence(){

    }
}
