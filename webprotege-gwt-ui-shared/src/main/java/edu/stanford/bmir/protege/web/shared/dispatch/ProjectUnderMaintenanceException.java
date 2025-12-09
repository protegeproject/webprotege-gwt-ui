package edu.stanford.bmir.protege.web.shared.dispatch;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ProjectUnderMaintenanceException extends RuntimeException implements IsSerializable {


    private ProjectUnderMaintenanceException() {
    }

    public ProjectUnderMaintenanceException(String message) {
        super(message);
    }
}
