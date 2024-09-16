package edu.stanford.bmir.protege.web.server.form.data;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlDataDto;
import edu.stanford.bmir.protege.web.shared.form.data.SingleChoiceControlDataDto;
import edu.stanford.bmir.protege.web.shared.form.field.FixedChoiceListSourceDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.SingleChoiceControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.SingleChoiceControlType;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

public class SingleChoiceControlDataDtoTest {

    @Test
    public void shouldDeserializeData() throws IOException {
        SingleChoiceControlDataDto data = SingleChoiceControlDataDto.get(
                SingleChoiceControlDescriptor.get(SingleChoiceControlType.COMBO_BOX,
                        FixedChoiceListSourceDescriptor.get(ImmutableList.of())),
                PrimitiveFormControlDataDto.get("Hello"),
                1
        );

        JsonSerializationTestUtil.testSerialization(data, SingleChoiceControlDataDto.class);
    }
}