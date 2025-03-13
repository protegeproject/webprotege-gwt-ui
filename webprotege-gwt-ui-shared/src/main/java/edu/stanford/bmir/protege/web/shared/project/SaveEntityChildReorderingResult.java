package edu.stanford.bmir.protege.web.shared.project;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static edu.stanford.bmir.protege.web.shared.project.SaveEntityChildReorderingAction.CHANNEL;


@JsonTypeName(CHANNEL)
public class SaveEntityChildReorderingResult implements Result {
}
