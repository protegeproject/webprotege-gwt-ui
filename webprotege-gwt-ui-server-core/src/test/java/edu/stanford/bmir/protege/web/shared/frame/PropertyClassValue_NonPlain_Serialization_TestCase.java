package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

import static edu.stanford.bmir.protege.web.MockingUtils.mockLiteral;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-15
 */
public class PropertyClassValue_NonPlain_Serialization_TestCase {

    @Test
    public void shouldSerialize() throws IOException {
        var pv = PropertyClassValue.get(MockingUtils.mockOWLObjectPropertyData(),
                                        MockingUtils.mockOWLClassData(),
                                        State.DERIVED);
        JsonSerializationTestUtil.testSerialization(pv, PropertyValue.class);
    }
}
