package edu.stanford.bmir.protege.web.shared.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.ShortForm;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableMap.toImmutableMap;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/03/2013
 */
@JsonTypeName("webprotege.events.entities.BrowserTextChanged")
public class BrowserTextChangedEvent extends ProjectEvent<BrowserTextChangedHandler> {

    public transient static final Event.Type<BrowserTextChangedHandler> ON_BROWSER_TEXT_CHANGED = new Event.Type<>();


    private OWLEntity entity;

    private String newBrowserText;

    private ImmutableMap<DictionaryLanguage, String> shortForms;

    public BrowserTextChangedEvent(OWLEntity entity, String newBrowserText, ProjectId projectId, ImmutableMap<DictionaryLanguage, String> shortForms) {
        super(projectId);
        this.entity = entity;
        this.newBrowserText = newBrowserText;
        this.shortForms = shortForms;
    }

    @JsonCreator
    protected BrowserTextChangedEvent(@JsonProperty("entity") OWLEntity entity,
                                      @JsonProperty("newBrowserText") String newBrowserText,
                                      @JsonProperty("projectId") ProjectId projectId,
                                      @JsonProperty("shortForms") ImmutableList<ShortForm> shortForms) {
        super(projectId);
        this.entity = entity;
        this.newBrowserText = newBrowserText;
        this.shortForms = shortForms.stream()
                .collect(toImmutableMap(ShortForm::getDictionaryLanguage, ShortForm::getShortForm));
    }

    /**
     * For serialization purposes only
     */
    private BrowserTextChangedEvent() {
    }

    public OWLEntity getEntity() {
        return entity;
    }

    public String getNewBrowserText() {
        return newBrowserText;
    }

    @JsonIgnore
    @Nonnull
    public ImmutableMap<DictionaryLanguage, String> getShortForms() {
        return shortForms;
    }

    @JsonProperty("shortForms")
    public ImmutableList<ShortForm> getShortFormsList() {
        return getShortForms().entrySet()
                .stream()
                .map(e -> ShortForm.get(e.getKey(), e.getValue()))
                .collect(toImmutableList());
    }


    @Override
    public Event.Type<BrowserTextChangedHandler> getAssociatedType() {
        return ON_BROWSER_TEXT_CHANGED;
    }

    @Override
    protected void dispatch(BrowserTextChangedHandler handler) {
        handler.browserTextChanged(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BrowserTextChangedEvent");
        sb.append("Entity(");
        sb.append(entity);
        sb.append(") NewBroswerText(");
        sb.append(newBrowserText);
        sb.append(")");
        sb.append(")");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getEntity(),
                                getNewBrowserText(),
                                getShortForms(),
                                getProjectId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BrowserTextChangedEvent)) {
            return false;
        }
        BrowserTextChangedEvent that = (BrowserTextChangedEvent) o;
        return entity.equals(that.entity)
                && newBrowserText.equals(that.newBrowserText)
                && shortForms.equals(that.shortForms)
                && getProjectId().equals(that.getProjectId());
    }
}
