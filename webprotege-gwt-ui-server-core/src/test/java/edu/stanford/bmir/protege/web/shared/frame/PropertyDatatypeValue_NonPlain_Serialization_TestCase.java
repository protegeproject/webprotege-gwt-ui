package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-15
 */
public class PropertyDatatypeValue_NonPlain_Serialization_TestCase {


    @Test
    public void shouldSerialize() throws IOException {
        var pv = PropertyDatatypeValue.get(MockingUtils.mockOWLDataPropertyData(),
                                        MockingUtils.mockOWLDatatypeData(),
                                        State.DERIVED);
        JsonSerializationTestUtil.testSerialization(pv, PropertyValue.class);
    }
}
