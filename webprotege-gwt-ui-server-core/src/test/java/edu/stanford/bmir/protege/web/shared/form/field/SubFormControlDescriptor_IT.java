package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.form.ExpansionState;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-09
 */
public class SubFormControlDescriptor_IT {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapperProvider().get();
    }

    @Test
    public void shouldSerializeAndDeserialize() throws IOException {

        var formDescriptor = new FormDescriptor(FormId.get("12345678-1234-1234-1234-123456789abc"),
                                                LanguageMap.of("en", "The sub form"),
                                                singletonList(
                                                        FormFieldDescriptor.get(
                                                                FormRegionId.get("12345678-1234-1234-1234-123456789def"),
                                                                OwlPropertyBinding.get(new OWLObjectPropertyImpl(
                                                                                               OWLRDFVocabulary.RDFS_LABEL.getIRI()),
                                                                                       null),
                                                                LanguageMap.of("en", "The Label"),
                                                                FieldRun.START,
                                                                FormFieldDeprecationStrategy.LEAVE_VALUES_INTACT,
                                                                new TextControlDescriptor(
                                                                        LanguageMap.empty(),
                                                                        StringType.SIMPLE_STRING,
                                                                        "en",
                                                                        LineMode.SINGLE_LINE,
                                                                        "Pattern",
                                                                        LanguageMap.empty()
                                                                ),
                                                                Repeatability.NON_REPEATABLE,
                                                                33,
                                                                Optionality.REQUIRED,
                                                                true,
                                                                ExpansionState.COLLAPSED,
                                                                LanguageMap.empty()
                                                        )
                                                ), Optional.empty());
        SubFormControlDescriptor descriptor = new SubFormControlDescriptor(formDescriptor);
        var serialized = objectMapper.writeValueAsString(descriptor);
        System.out.println(serialized);
        var subFormJson = SubFormControlDescriptor_IT.class.getResourceAsStream("/forms/SubFormDescriptor.json");
        var deserialized = objectMapper.readerFor(SubFormControlDescriptor.class)
                .readValue(subFormJson);
        assertThat(deserialized, is(descriptor));
    }
}
