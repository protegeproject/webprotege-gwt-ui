package edu.stanford.bmir.protege.web.client.gh;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.gh.GitHubRepositoryCoordinates;
import edu.stanford.bmir.protege.web.shared.obo.OboId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;

public class NewGitHubIssueHandler {

    private final DispatchServiceManager dispatch;

    @Inject
    public NewGitHubIssueHandler(DispatchServiceManager dispatch) {
        this.dispatch = dispatch;
    }

    public void handleCreateNewGitHubIssue(ProjectId projectId,
                                           OWLEntity entity) {
        dispatch.execute(GetLinkedGitHubRepositoryAction.create(projectId),
                result -> {
                    result.getRepositoryCoordinates().ifPresent(coords -> {
                        createNewIssueInRepo(projectId, entity, coords);
                    });
                });

    }

    private void createNewIssueInRepo(ProjectId projectId,
                                      OWLEntity entity,
                                      GitHubRepositoryCoordinates repoCoords) {
        dispatch.execute(GetEntityRenderingAction.create(projectId, entity), renderingResult -> {
            OWLEntityData entityData = renderingResult.getEntityData();
            String browserText = entityData.getBrowserText();
            String location = Window.Location.getHref();
            String idRendering = OboId.getOboId(entity.getIRI()).orElse(entity.getIRI().toString());
            String body = "This is an issue for **" + browserText + "** (`" + idRendering + "`)    [[View in WebProtégé]]("+location+")" + "\n\n\n<!-- This issue is for entity " + entity.getIRI() + " (leave this html comment intact to retain the link to this issue in WebProtégé or mention this IRI elsewhere in this issue. -->" ;
            String encodedBody = URL.encodeQueryString(body);
            String urlBase = "https://github.com/" + repoCoords.getFullName() + "/issues/new";
            Window.open(urlBase + "?body=" + encodedBody , "_blank", "");
        });

    }

}
