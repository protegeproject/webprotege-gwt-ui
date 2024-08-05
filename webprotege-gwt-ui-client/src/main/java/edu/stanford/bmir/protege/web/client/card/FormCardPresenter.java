package edu.stanford.bmir.protege.web.client.card;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.form.FormPresenter;
import edu.stanford.bmir.protege.web.client.selection.EntitySelectionChangedEvent;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.GetEntityFormsAction;
import edu.stanford.bmir.protege.web.shared.lang.LangTagFilter;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.client.SelectionChangeEvent;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.stream.Collectors;

public class FormCardPresenter implements EntityCardPresenter {

    private final ProjectId projectId;

    private final FormId formId;

    private final FormPresenter formPresenter;

    private final DispatchServiceManager dispatch;


    private Optional<OWLEntity> entity;

    @AutoFactory
    @Inject
    public FormCardPresenter(FormId formId,
                             @Provided DispatchServiceManager dispatch,
                             @Provided ProjectId projectId,
                             @Provided FormPresenter formPresenter) {
        this.dispatch = dispatch;
        this.projectId = projectId;
        this.formId = formId;
        this.formPresenter = formPresenter;
    }

    @Override
    public LanguageMap getLabel() {
        return LanguageMap.of("en", "Stuff");
    }

    public void start(AcceptsOneWidget container, WebProtegeEventBus eventBus) {
        formPresenter.start(container);
        eventBus.addProjectEventHandler(projectId, EntitySelectionChangedEvent.getType(), event -> {
            setDisplayedEntity(event.getLastSelection());
        });
    }

    private void setDisplayedEntity(Optional<OWLEntity> entity) {
        this.entity = entity;
        updateDisplayedForm();
    }

    private void updateDisplayedForm() {
        formPresenter.clearData();
        entity.ifPresent(e -> {
            dispatch.execute(GetEntityFormsAction.create(projectId,
                            e,
                            ImmutableSet.copyOf(formPresenter.getPageRequest()),
                            LangTagFilter.get(ImmutableSet.of()),
                            ImmutableSet.copyOf(formPresenter.getOrderings().collect(Collectors.toList())),
                            ImmutableSet.of(formId),
                            formPresenter.getFilters()
                    ),
                    result -> {
                        if (result.getFormData().size() == 1) {
                            formPresenter.displayForm(result.getFormData().get(0));
                        }
                    });
        });
    }


}

