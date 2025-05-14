package edu.stanford.bmir.protege.web.client.inject;

import dagger.Subcomponent;
import edu.stanford.bmir.protege.web.client.form.FormEditorPresenter;
import edu.stanford.bmir.protege.web.client.form.FormsManagerPresenter;
import edu.stanford.bmir.protege.web.client.lang.DefaultDisplayNameSettingsPresenter;
import edu.stanford.bmir.protege.web.client.perspective.PerspectivesManagerPresenter;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorClientModule;
import edu.stanford.bmir.protege.web.client.project.ProjectPrefixDeclarationsPresenter;
import edu.stanford.bmir.protege.web.client.project.ProjectPresenter;
import edu.stanford.bmir.protege.web.client.projectsettings.ProjectSettingsPresenter;
import edu.stanford.bmir.protege.web.client.role.ProjectRoleAssignmentsPresenter;
import edu.stanford.bmir.protege.web.client.role.ProjectRolesPresenter;
import edu.stanford.bmir.protege.web.client.search.EntitySearchSettingsPresenter;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.client.sharing.SharingSettingsPresenter;
import edu.stanford.bmir.protege.web.client.tag.ProjectTagsPresenter;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Oct 2016
 */
@Subcomponent(
        modules = {
                ClientProjectModule.class,
                ClientIssuesModule.class,
                PrimitiveDataEditorClientModule.class,
                CardModule.class
        }
)
@ProjectSingleton
public interface ClientProjectComponent extends HasDispose {

    ProjectId getProjectId();

    FormEditorPresenter getFormEditorPresenter();

    ProjectPresenter getProjectPresenter();

    SharingSettingsPresenter getSharingSettingsPresenter();

    ProjectSettingsPresenter getProjectSettingsPresenter();

    ProjectPrefixDeclarationsPresenter getProjectPrefixesPresenter();

    ProjectTagsPresenter getProjectTagsPresenter();

    DefaultDisplayNameSettingsPresenter getLanguageSettingsPresenter();

    FormsManagerPresenter getFormsPresenter();

    EntitySearchSettingsPresenter getEntitySearchSettingsPresenter();

    PerspectivesManagerPresenter getPerspectivesManagerPresenter();

    SelectionModel getProjectSelectionModel();

    default void dispose() {
        getProjectPresenter().dispose();
        getProjectSelectionModel().dispose();
    }

    ProjectRolesPresenter getProjectRolesPresenter();

    ProjectRoleAssignmentsPresenter getProjectRoleAssignmentsPresenter();
}
