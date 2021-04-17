package edu.stanford.bmir.protege.web.shared.form;

import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldId;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static edu.stanford.bmir.protege.web.MockingUtils.mockOWLClass;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-16
 */
public class FormPageRequest_Serialization_TestCase {

    @Test
    public void shouldSerializeFormPageRequest() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                FormPageRequest.get(FormId.generate(),
                                    FormSubject.get(mockOWLClass()),
                                    FormFieldId.get(UUID.randomUUID().toString()),
                                    FormPageRequest.SourceType.CONTROL_STACK,
                                    PageRequest.requestFirstPage()),
                FormPageRequest.class
        );
    }
}
