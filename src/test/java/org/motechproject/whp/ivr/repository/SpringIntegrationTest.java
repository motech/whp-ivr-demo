package org.motechproject.whp.ivr.repository;

import org.ektorp.BulkDeleteDocument;
import org.ektorp.CouchDbConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.motechproject.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public abstract class SpringIntegrationTest {

    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    @Qualifier("whpivrDbConnector")
    @Autowired
    protected CouchDbConnector whpDbConnector;

    protected ArrayList<BulkDeleteDocument> toDelete;

    @Before
    public void before() {
        toDelete = new ArrayList<BulkDeleteDocument>();
    }

    @After
    public void after() {
        deleteAll();
    }

    protected void deleteAll() {
        if (toDelete.size() > 0)
            whpDbConnector.executeBulk(toDelete);
        toDelete.clear();
    }

    protected void markForDeletion(Object... documents) {
        for (Object document : documents)
            markForDeletion(document);
    }

    protected void markForDeletion(List documents) {
        markForDeletion(documents.toArray());
    }

    protected void markForDeletion(Object document) {
        toDelete.add(BulkDeleteDocument.of(document));
    }

    protected String unique(String name) {
        return name + DateUtil.now().toInstant().getMillis();
    }

}
