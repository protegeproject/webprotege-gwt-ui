package edu.stanford.bmir.protege.web.shared.frame;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
@JsonTypeName("webprotege.frames.SetManchesterSyntaxFrame")
public class SetManchesterSyntaxFrameResult implements Result {

    private String frameText;


    private SetManchesterSyntaxFrameResult() {
    }

    private SetManchesterSyntaxFrameResult(String frameText) {
        this.frameText = checkNotNull(frameText);
    }

    public static SetManchesterSyntaxFrameResult create(String frameText) {
        return new SetManchesterSyntaxFrameResult(frameText);
    }

    public String getFrameText() {
        return frameText;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(frameText);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SetManchesterSyntaxFrameResult)) {
            return false;
        }
        SetManchesterSyntaxFrameResult other = (SetManchesterSyntaxFrameResult) obj;
        return this.frameText.equals(other.frameText);
    }


    @Override
    public String toString() {
        return toStringHelper("SetManchesterSyntaxFrameResult")
                .add("frameText", frameText)
                .toString();
    }
}
