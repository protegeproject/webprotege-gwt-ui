package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-07
 */
public enum FieldRun implements IsSerializable, Serializable {

    START,

    CONTINUE;

    public boolean isStart() {
        return this == START;
    }
}
