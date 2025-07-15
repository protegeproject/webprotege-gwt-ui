package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableSet;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-05-01
 */
public class GridColumnVisibilityManager {

    public interface VisibleColumnsChangedHandler {
        void handleVisibleColumnsChanged();
    }


    private Set<FormRegionId> visibleColumns = new HashSet<>();

    private Set<VisibleColumnsChangedHandler> handlers = new LinkedHashSet<>();


    public void setVisibleColumns(@Nonnull ImmutableSet<FormRegionId> visibleColumns) {
        checkNotNull(visibleColumns);
        if(this.visibleColumns.containsAll(visibleColumns) && visibleColumns.containsAll(this.visibleColumns)) {
            return;
        }
        this.visibleColumns.clear();
        this.visibleColumns.addAll(visibleColumns);
        fireVisibleColumnsChanged();
    }

    @Nonnull
    public HandlerRegistration addVisibleColumnsChangedHandler(@Nonnull VisibleColumnsChangedHandler handler) {
        handlers.add(handler);
        return () -> handlers.remove(handler);
    }

    private void fireVisibleColumnsChanged() {
        new ArrayList<>(handlers)
                .forEach(VisibleColumnsChangedHandler::handleVisibleColumnsChanged);
    }

    @Nonnull
    public ImmutableSet<FormRegionId> getVisibleColumns() {
        return ImmutableSet.copyOf(visibleColumns);
    }

    public boolean isVisible(@Nonnull FormRegionId columnId) {
        return visibleColumns.contains(columnId);
    }
}
