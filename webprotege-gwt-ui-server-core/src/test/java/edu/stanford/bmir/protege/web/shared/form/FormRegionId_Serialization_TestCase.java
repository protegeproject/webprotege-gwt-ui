package edu.stanford.bmir.protege.web.shared.form;

import edu.stanford.bmir.protege.web.shared.form.field.FormFieldId;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-16
 */
public class FormRegionId_Serialization_TestCase {

    @Test
    public void shouldSerializeFormFieldId() throws IOException {
        JsonSerializationTestUtil.testSerialization(FormFieldId.get(UUID.randomUUID().toString()), FormRegionId.class);
    }

    @Test
    public void shouldSerializeGridColumnId() throws IOException {
        JsonSerializationTestUtil.testSerialization(GridColumnId.get(UUID.randomUUID().toString()), FormRegionId.class);
    }

    @Test
    public void shouldDesearializeStringAsFormFieldId() throws IOException {
        String serialization = UUID.randomUUID().toString();
        JsonSerializationTestUtil.testDeserialization(serialization,
                                                      FormFieldId.get(serialization),
                                                      FormRegionId.class);
    }
}
