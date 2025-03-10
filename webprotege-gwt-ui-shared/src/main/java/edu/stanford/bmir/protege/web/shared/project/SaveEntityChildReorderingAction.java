package edu.stanford.bmir.protege.web.shared.project;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import java.util.List;

import static edu.stanford.bmir.protege.web.shared.project.SaveEntityChildReorderingAction.CHANNEL;


@JsonTypeName(CHANNEL)
public class SaveEntityChildReorderingAction implements ProjectAction<SaveEntityChildReorderingResult> {

    public final static String CHANNEL = "webprotege.projects.SaveEntityChildReordering";

    private ProjectId projectId;

    private IRI entityIri;

    private String commitMessage;

    private List<String> orderedChildren;
    @GwtSerializationConstructor
    private SaveEntityChildReorderingAction() {
    }

    public SaveEntityChildReorderingAction(ProjectId projectId, IRI entityIri, List<String> orderedChildren, String commitMessage) {
        this.projectId = projectId;
        this.orderedChildren = orderedChildren;
        this.entityIri = entityIri;
        this.commitMessage = commitMessage;
    }

    public static SaveEntityChildReorderingAction create(@Nonnull ProjectId projectId,
                                                         @Nonnull IRI entityIri,
                                                         @Nonnull List<String> orderedChildren,
                                                         String commitMessage) {
        return new SaveEntityChildReorderingAction(projectId, entityIri, orderedChildren, commitMessage);
    }


    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public IRI getEntityIri() {
        return entityIri;
    }

    public List<String> getOrderedChildren() {
        return orderedChildren;
    }

    public String getCommitMessage() {
        return commitMessage;
    }
}
