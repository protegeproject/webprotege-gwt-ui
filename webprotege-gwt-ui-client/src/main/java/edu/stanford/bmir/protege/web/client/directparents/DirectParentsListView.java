package edu.stanford.bmir.protege.web.client.directparents;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;
import java.util.List;

public interface DirectParentsListView extends IsWidget {

    void clearViews();

    void setDirectParentView(@Nonnull List<DirectParentView> directParentViews);
}
