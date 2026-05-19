package edu.stanford.bmir.protege.web.client.search;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.uuid.UuidV4Provider;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchFilter;
import edu.stanford.bmir.protege.web.shared.search.GetSearchSettingsAction;
import edu.stanford.bmir.protege.web.shared.search.SetSearchSettingsAction;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-16
 */
public class EntitySearchSettingsService {

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final UuidV4Provider uuidV4Provider;

    @Inject
    public EntitySearchSettingsService(DispatchServiceManager dispatch, @Nonnull ProjectId projectId, @Nonnull UuidV4Provider uuidV4Provider) {
        this.dispatch = checkNotNull(dispatch);
        this.projectId = checkNotNull(projectId);
        this.uuidV4Provider = checkNotNull(uuidV4Provider);
    }

    public void getFilters(Consumer<ImmutableList<EntitySearchFilter>> filters) {
        dispatch.execute(GetSearchSettingsAction.create(projectId),
                         result -> filters.accept(result.getFilters()));
    }

    public void setFilters(@Nonnull ImmutableList<EntitySearchFilter> filters) {
        dispatch.execute(SetSearchSettingsAction.create(ChangeRequestId.get(uuidV4Provider.get()),
                                                        projectId,
                                                        ImmutableList.of(),
                                                        filters),
                         result -> {});
    }
}
