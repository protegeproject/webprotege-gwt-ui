package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import org.junit.*;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.ResolvableType;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-06-18
 */
public class TextControlDescriptor_Serialization_TestCase {

    JacksonTester<TextControlDescriptor> tester;

    @Before
    public void setUp() throws Exception {
        tester = new JacksonTester<>(TextControlDescriptor_Serialization_TestCase.class,
                                     ResolvableType.forClass(TextControlDescriptor.class),
                                     new ObjectMapperProvider().get());

    }

    @Test
    public void shouldDeserialize() throws IOException {
        /*
        {
          "@type": "TEXT",
          "placeholder": {
            "en": "The placeholder"
          },
          "stringType": "SPECIFIC_LANG_STRING",
          "specificLangTag": "en",
          "lineMode": "SINGLE_LINE",
          "pattern": "Pattern",
          "patternViolationErrorMessage": {
            "en": "An error message"
          }
        }
         */
        var resourceAsStream = TextControlDescriptor_Serialization_TestCase.class.getResourceAsStream(
                "/forms/TextControlDescriptor.json");
        var read = tester.read(resourceAsStream);
        assertThat(read.getObject().getPlaceholder().get("en")).isEqualTo("The placeholder");
        assertThat(read.getObject().getStringType()).isEqualTo(StringType.SPECIFIC_LANG_STRING);
        assertThat(read.getObject().getLineMode()).isEqualTo(LineMode.SINGLE_LINE);
        assertThat(read.getObject().getPattern()).isEqualTo("Pattern");
        assertThat(read.getObject().getPatternViolationErrorMessage().get("en")).isEqualTo("An error message");
    }

    @Test
    public void shouldDeserializeWithNonIncludedEmpties() throws IOException {
        var resourceAsStream = TextControlDescriptor_Serialization_TestCase.class.getResourceAsStream(
                "/forms/TextControlDescriptor.min.json");
        var read = tester.read(resourceAsStream);
        assertThat(read.getObject().getPlaceholder().asMap()).isEmpty();
        assertThat(read.getObject().getStringType()).isEqualTo(StringType.SPECIFIC_LANG_STRING);
        assertThat(read.getObject().getLineMode()).isEqualTo(LineMode.SINGLE_LINE);
        assertThat(read.getObject().getPattern()).isEqualTo("");
        assertThat(read.getObject().getPatternViolationErrorMessage().asMap()).isEmpty();
    }

    @Test
    public void shouldSerializeWithoutEmpties() throws IOException {
        var desc = new TextControlDescriptor(LanguageMap.empty(),
                                  StringType.SPECIFIC_LANG_STRING,
                                  "",
                                  LineMode.SINGLE_LINE,
                                  "",
                                  LanguageMap.empty());
        var written = tester.write(desc);
        assertThat(written).hasJsonPathValue("stringType");
        assertThat(written).hasJsonPathValue("lineMode");
        assertThat(written).doesNotHaveJsonPath("placeholder");
        assertThat(written).doesNotHaveJsonPath("pattern");
        assertThat(written).doesNotHaveJsonPath("patternViolationErrorMessage");
    }
}
