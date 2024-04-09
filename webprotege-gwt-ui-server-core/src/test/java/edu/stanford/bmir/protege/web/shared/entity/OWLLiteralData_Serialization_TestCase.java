package edu.stanford.bmir.protege.web.shared.entity;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.junit.Test;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImpl;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-08
 */
public class OWLLiteralData_Serialization_TestCase {

    @Test
    public void shouldSerializeOWLLiteralData() throws IOException {
        OWLLiteralData data = OWLLiteralData.get(MockingUtils.mockLiteral());
        JsonSerializationTestUtil.testSerialization(data, OWLLiteralData.class);
    }

    @Test
    public void shouldSerializeOWLLiteralDataWithLang() throws IOException {
        OWLLiteralData data = OWLLiteralData.get(DataFactory.getOWLLiteral("Hello", "en"));
        JsonSerializationTestUtil.testSerialization(data, OWLLiteralData.class);
    }

    @Test
    public void shouldSerializeOWLLiteralDataWithDatatype() throws IOException {
        OWLLiteralData data = OWLLiteralData.get(DataFactory.getOWLLiteral(33));
        JsonSerializationTestUtil.testSerialization(data, OWLLiteralData.class);
    }
}
