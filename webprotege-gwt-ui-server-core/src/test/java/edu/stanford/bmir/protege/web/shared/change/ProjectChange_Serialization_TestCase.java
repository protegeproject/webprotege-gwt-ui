package edu.stanford.bmir.protege.web.shared.change;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.diff.DiffOperation;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.junit.Test;

import java.io.IOException;

import static edu.stanford.bmir.protege.web.MockingUtils.mockUserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-16
 */
public class ProjectChange_Serialization_TestCase {

    @Test
    public void shouldSerialize_ProjectChange() throws IOException {
        JsonSerializationTestUtil.testSerialization(
                ProjectChange.get(RevisionNumber.getHeadRevisionNumber(),
                                  mockUserId(),
                                  1L,
                                  "Summary",
                                  3,
                                  Page.of(ImmutableList.of(
                                          new DiffElement<>(DiffOperation.ADD,
                                                            "Source",
                                                            "Line")
                                  ))),
                ProjectChange.class
        );
    }
}
