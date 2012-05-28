package org.motechproject.whp.ivr.controller;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.motechproject.ivr.kookoo.KooKooIVRContext;
import org.motechproject.ivr.kookoo.KookooIVRResponseBuilder;
import org.motechproject.ivr.kookoo.controller.SafeIVRController;
import org.motechproject.ivr.kookoo.controller.StandardResponseController;
import org.motechproject.ivr.kookoo.service.KookooCallDetailRecordsService;
import org.motechproject.ivr.message.IVRMessage;
import org.motechproject.outbox.api.service.VoiceOutboxService;
import org.motechproject.whp.ivr.WHPIVRContext;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.common.ControllerURLs;
import org.motechproject.whp.ivr.domain.IVRAdherence;
import org.motechproject.whp.ivr.domain.IVRAdherenceResponse;
import org.motechproject.whp.ivr.messages.MessageController;
import org.motechproject.whp.ivr.messages.blocks.Block1;
import org.motechproject.whp.ivr.messages.blocks.Block2;
import org.motechproject.whp.ivr.outbox.context.OutboxContext;
import org.motechproject.whp.ivr.repository.AllIVRResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(ControllerURLs.OUTBOX_URL)
public class OutboxController extends SafeIVRController {
    public static final String PROVIDER_NO = "providerNo";
    private VoiceOutboxService outboxService;
    private static Logger logger = Logger.getLogger(OutboxController.class);
    PatientMapper patientMapper;
    MessageController messageController;
    AllIVRResponses ivrResponses;
    public static final String SIGNATURE_MUSIC = "signature_music";
    Block1 block1;


    @Autowired
    public OutboxController(IVRMessage ivrMessage,
                            KookooCallDetailRecordsService callDetailRecordsService,
                            StandardResponseController standardResponseController,
                            AllIVRResponses ivrResponses) {

        super(ivrMessage, callDetailRecordsService, standardResponseController);
        this.outboxService = outboxService;
        patientMapper = new PatientMapper();
        messageController = new MessageController();
        this.ivrResponses = ivrResponses;

        block1 = new Block1();
    }

    @Override
    public KookooIVRResponseBuilder newCall(KooKooIVRContext kooKooIVRContext) {
        String providerNo = kooKooIVRContext.callerId();
        List<String> patientIds = patientMapper.patientIds(providerNo);


        String patientId = patientIds.get(0);
        logger.info("Patient id " + patientId);

        KookooIVRResponseBuilder ivrResponseBuilder = new KookooIVRResponseBuilder().withDefaultLanguage();
        ivrResponseBuilder.withPlayAudios(SIGNATURE_MUSIC);

        messageController.buildWelcomeMessage(ivrResponseBuilder, patientIds);
        messageController.buildStartingIntructionWithPatientId(ivrResponseBuilder, patientIds, kooKooIVRContext);

        kooKooIVRContext.cookies().add(PROVIDER_NO, providerNo);

        return ivrResponseBuilder.collectDtmfLength(1);
    }


    @Override
    public KookooIVRResponseBuilder gotDTMF(KooKooIVRContext kooKooIVRContext) {
        String userInput = kooKooIVRContext.userInput();
        boolean isValidInput = true;

        try {
            if (Integer.parseInt(userInput) > 7)
                isValidInput = false;

        } catch (Exception e) {
            if (!"*".equals(userInput))
                isValidInput = false;

        }
        String lastPatientId = kooKooIVRContext.cookies().getValue(WHPIVRContext.PATIENT_ID);
        String providerNo = kooKooIVRContext.cookies().getValue(WHPIVRContext.PROVIDER_NO);

        String ivrResponseId = kooKooIVRContext.cookies().getValue(OutboxContext.IVR_RESPONSE_ID);
        IVRAdherenceResponse adherenceResponse = saveAdherence(userInput, lastPatientId, providerNo, ivrResponseId);
        kooKooIVRContext.cookies().add(OutboxContext.IVR_RESPONSE_ID, adherenceResponse.getId());

        KookooIVRResponseBuilder ivrResponseBuilder = new KookooIVRResponseBuilder().withDefaultLanguage();
        List<String> patientIds = patientMapper.patientIds(providerNo);

        messageController.routeMessage(ivrResponseBuilder, patientIds, kooKooIVRContext, adherenceResponse, isValidInput);

        logger.info("Patient id retrieved from session " + lastPatientId);
        logger.info("User input is  " + userInput);

        return ivrResponseBuilder;
    }

    private IVRAdherenceResponse saveAdherence(String userInput, String lastPatientId, String providerNo, String ivrResponseId) {
        IVRAdherenceResponse adherenceResponse = null;
        if (ivrResponseId == null) {
            adherenceResponse = new IVRAdherenceResponse();
            adherenceResponse.setProviderId(providerNo);
            adherenceResponse.setCallTime(DateTime.now());
            ivrResponses.add(adherenceResponse);
        } else {
            adherenceResponse = ivrResponses.findById(ivrResponseId);
        }
        IVRAdherence adherence = new IVRAdherence(lastPatientId, userInput);
        adherenceResponse.add(adherence);

        ivrResponses.update(adherenceResponse);
        return adherenceResponse;
    }


}