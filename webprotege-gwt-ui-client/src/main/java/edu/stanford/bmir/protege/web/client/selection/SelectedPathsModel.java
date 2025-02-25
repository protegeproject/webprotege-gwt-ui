package edu.stanford.bmir.protege.web.client.selection;

import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.shared.ViewNodeId;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@ProjectSingleton
public class SelectedPathsModel {

    private List<List<OWLEntity>> selectedPaths = new ArrayList<>();

    @Inject
    public SelectedPathsModel() {
    }

    public void clearSelectedPaths() {
//        Window.alert("Clear selected paths");
        selectedPaths.clear();
    }

    public void setSelectedPaths(List<List<OWLEntity>> selectedPaths) {
        this.selectedPaths.clear();
        this.selectedPaths.addAll(selectedPaths);
//        Window.alert("Set selected paths: " + selectedPaths);
    }

    public List<List<OWLEntity>> getSelectedPaths() {
        return this.selectedPaths
                .stream()
                .map(ArrayList::new)
                .collect(Collectors.toList());
    }

    public void setSelectedPaths(Collection<Path<OWLEntity>> selectedPaths) {
        List<List<OWLEntity>> paths = selectedPaths.stream()
                .map(p -> new ArrayList<>(p.asList()))
                .collect(Collectors.toList());
        setSelectedPaths(paths);
    }
}
