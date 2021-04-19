package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlDataDto;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-19
 */
public class SingleChoiceControlDescriptorDto_Serialization_TestCase {

    @Test
    public void shouldSerialize() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                SingleChoiceControlDescriptorDto.get(SingleChoiceControlType.SEGMENTED_BUTTON,
                                                     ImmutableList.of(
                                                             ChoiceDescriptorDto.get(PrimitiveFormControlDataDto.get("Choice1"), LanguageMap
                                                                     .empty())
                                                     ),
                                                     FixedChoiceListSourceDescriptor.get(ImmutableList.of())),
                SingleChoiceControlDescriptorDto.class
        );
    }
}
