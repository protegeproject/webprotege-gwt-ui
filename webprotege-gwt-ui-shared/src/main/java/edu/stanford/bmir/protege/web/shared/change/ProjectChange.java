package edu.stanford.bmir.protege.web.shared.change;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24/02/15
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class ProjectChange implements IsSerializable, Serializable {

    @JsonCreator
    @Nonnull
    public static ProjectChange get(@JsonProperty("revisionNumber") @Nonnull RevisionNumber revisionNumber,
                                    @JsonProperty("userId") UserId author,
                                    @JsonProperty("timestamp") long timestamp,
                                    @JsonProperty("summary") String summary,
                                    @JsonProperty("changeCount") int changeCount,
                                    @JsonProperty("diff") Page<DiffElement<String, String>> diff) {
        return new AutoValue_ProjectChange(changeCount,
                                           revisionNumber,
                                           author,
                                           summary,
                                           timestamp,
                                           diff);
    }

    public abstract int getChangeCount();

    public abstract RevisionNumber getRevisionNumber();

    public abstract UserId getAuthor();
    
    public abstract String getSummary();

    public abstract long getTimestamp();

    public abstract Page<DiffElement<String, String>> getDiff();
}
