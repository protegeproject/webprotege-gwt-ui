package edu.stanford.bmir.protege.web.shared.auth;

import com.google.common.testing.SerializableTester;
import com.google.gwt.junit.Platform;
import com.google.gwt.junit.client.GWTTestCase;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-11
 */
public class GwtTest_ChapResponse_Serialization extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "edu.stanford.bmir.protege.web.WebProtegeJUnit";
    }


    @Override
    protected void gwtSetUp() throws Exception {
        delayTestFinish(10000);
    }

    public void test_serialize() {
        SerializableTester.reserializeAndAssert(new ChapResponse(new byte [] {1, 2, 3, 4}));
    }
}
