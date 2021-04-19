package edu.stanford.bmir.protege.web.shared.form.data;

import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-19
 */
public class PrimitiveFormControlDataDto_Serilization_TestCase {

    @Test
    public void shouldSerializeEntityData() throws IOException {
        JsonSerializationTestUtil.testSerialization(PrimitiveFormControlDataDto.get(MockingUtils.mockOWLClassData()),
                                                    PrimitiveFormControlDataDto.class);
    }

    @Test
    public void shouldSerializeLiteral  () throws IOException {
        JsonSerializationTestUtil.testSerialization(PrimitiveFormControlDataDto.get(MockingUtils.mockOWLLiteralData()),
                                                    PrimitiveFormControlDataDto.class);
    }

    @Test
    public void shouldSerializeIri() throws IOException {
        JsonSerializationTestUtil.testSerialization(PrimitiveFormControlDataDto.get(MockingUtils.mockIriData()),
                                                    PrimitiveFormControlDataDto.class);
    }
}
