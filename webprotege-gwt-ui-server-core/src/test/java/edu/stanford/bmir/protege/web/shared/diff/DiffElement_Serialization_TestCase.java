package edu.stanford.bmir.protege.web.shared.diff;

import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class DiffElement_Serialization_TestCase {

    @Test
    public void shouldSerializeDiffElement() throws IOException {
        var diff = new DiffElement<>(DiffOperation.ADD, "Source", "<html><body>The \"Diff\"</body></html>");
        JsonSerializationTestUtil.testSerialization(diff, DiffElement.class);
    }
}
