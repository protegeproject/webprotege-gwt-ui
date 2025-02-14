package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public enum Repeatability implements IsSerializable, Serializable {

    NON_REPEATABLE,

    REPEATABLE_VERTICALLY,

    REPEATABLE_HORIZONTALLY;

    public boolean isRepeatable() {
        return this != NON_REPEATABLE;
    }
}
