package org.motechproject.whp.ivr.repository;


import org.ektorp.CouchDbConnector;
import org.ektorp.support.GenerateView;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.ivr.domain.IVRAdherenceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllIVRResponses extends MotechBaseRepository<IVRAdherenceResponse>{

    @Autowired
    public AllIVRResponses(@Qualifier("whpivrDbConnector") CouchDbConnector couchDbConnector) {
        super(IVRAdherenceResponse.class, couchDbConnector);
    }

    @Override
    public void add(IVRAdherenceResponse adherenceResponse) {
        super.add(adherenceResponse);
    }

    @GenerateView
    public IVRAdherenceResponse findById(String id) {
        return get(id);
        /*ViewQuery find_by_Id = createQuery("by_Id").key(id).includeDocs(true);
        IVRAdherenceResponse adherenceResponse = singleResult(db.queryView(find_by_Id, IVRAdherenceResponse.class));
        return adherenceResponse;*/
    }

    }

