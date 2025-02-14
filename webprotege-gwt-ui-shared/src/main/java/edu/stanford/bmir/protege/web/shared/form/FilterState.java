package edu.stanford.bmir.protege.web.shared.form;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-25
 */
public enum FilterState implements IsSerializable, Serializable {

    UNFILTERED,

    FILTERED
}
