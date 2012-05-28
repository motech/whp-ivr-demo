package org.motechproject.whp.ivr.controller;


import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class PatientMapper {
    static Properties properties;
    
    public  PatientMapper(){
        properties = new Properties();
        try {
            InputStream resourceAsStream = this.getClass().getClassLoader()
                    .getResourceAsStream("patient-mapping.properties");
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    public List<String> patientIds(String providerNo){
      String patientIds = properties.getProperty(providerNo); 
      return Arrays.asList(patientIds.split(","));
        
    }
}
