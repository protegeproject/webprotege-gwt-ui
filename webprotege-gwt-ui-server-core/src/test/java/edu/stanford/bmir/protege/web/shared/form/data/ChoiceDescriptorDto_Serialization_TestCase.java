package edu.stanford.bmir.protege.web.shared.form.data;

import edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptorDto;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-19
 */
public class ChoiceDescriptorDto_Serialization_TestCase {

    @Test
    public void shouldSerializeChoiceDescriptor() throws IOException {
        JsonSerializationTestUtil.testSerialization(ChoiceDescriptorDto.get(PrimitiveFormControlDataDto.get(true),
                                                                            LanguageMap.empty()),
                                                    ChoiceDescriptorDto.class);
    }
}
