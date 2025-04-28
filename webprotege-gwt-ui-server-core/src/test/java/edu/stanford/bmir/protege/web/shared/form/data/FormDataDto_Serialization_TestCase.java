package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.ExpansionState;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptorDto;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.FormSubjectFactoryDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import org.junit.Test;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-16
 */
public class FormDataDto_Serialization_TestCase {

    @Test
    public void shouldSerialize_FormDataDto() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                FormDataDto.get(FormSubjectDto.getFormSubject(mockOWLClassData()),
                                FormDescriptorDto.get(FormId.generate(),
                                                      LanguageMap.empty(),
                                                      ImmutableList.of(
                                                              FormFieldDescriptorDto.get(
                                                                      FormRegionId.get(UUID.randomUUID().toString()),
                                                                      OwlPropertyBinding.get(mockOWLObjectProperty()),
                                                                      LanguageMap.empty(),
                                                                      FieldRun.START,
                                                                      TextControlDescriptorDto.get(
                                                                              TextControlDescriptor.getDefault()
                                                                      ),
                                                                      Optionality.OPTIONAL,
                                                                      Repeatability.NON_REPEATABLE,
                                                                      FormFieldDeprecationStrategy.DELETE_VALUES,
                                                                      true,
                                                                      FormFieldAccessMode.READ_WRITE,
                                                                      ExpansionState.EXPANDED,
                                                                      LanguageMap.empty()
                                                              )
                                                      ),
                                                      FormSubjectFactoryDescriptor.get(
                                                              EntityType.CLASS,
                                                              mockOWLClass(),
                                                              Optional.empty()
                                                      )),
                                ImmutableList.of(
                                        FormFieldDataDto.get(
                                            FormFieldDescriptorDto.get(
                                                    FormRegionId.get(UUID.randomUUID().toString()),
                                                    OwlSubClassBinding.get(),
                                                    LanguageMap.empty(),
                                                    FieldRun.START,
                                                    ImageControlDescriptorDto.get(),
                                                    Optionality.OPTIONAL,
                                                    Repeatability.NON_REPEATABLE,
                                                    FormFieldDeprecationStrategy.DELETE_VALUES,
                                                    true,
                                                    FormFieldAccessMode.READ_WRITE,
                                                    ExpansionState.EXPANDED,
                                                    LanguageMap.empty()
                                            ),
                                                Page.of(
                                                    ImageControlDataDto.get(
                                                            new ImageControlDescriptor(), IRI.create("http://example.org"),
                                                            3
                                                    )
                                                )
                                        )
                                ),
                                2),
                FormDataDto.class
        );
    }
}
