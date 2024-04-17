package edu.stanford.bmir.protege.web.shared.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.hierarchies.GetProjectEvents")
public abstract class GetProjectEventsResult<E extends WebProtegeEvent<?>> implements Result {

    @JsonCreator
    public static GetProjectEventsResult create(@JsonProperty("events") EventList events) {
        return new AutoValue_GetProjectEventsResult(events);
    }

    public abstract EventList<E> getEvents();

}
