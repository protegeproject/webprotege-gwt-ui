package edu.stanford.bmir.protege.web.client.form;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Consumer;

public class FormDescriptorComponentPresenterHierarchyNode {

    private final FormDescriptorComponentPresenter presenter;

    private final LinkedHashSet<FormDescriptorComponentPresenterHierarchyNode> children = new LinkedHashSet<>();

    public FormDescriptorComponentPresenterHierarchyNode(FormDescriptorComponentPresenter presenter) {
        this.presenter = presenter;
    }

    public void addChild(FormDescriptorComponentPresenterHierarchyNode child) {
        this.children.add(child);
    }

    public List<FormDescriptorComponentPresenterHierarchyNode> getChildren() {
        return new ArrayList<>(children);
    }

    public FormDescriptorComponentPresenterHierarchyNode addChildForPresenter(FormDescriptorComponentPresenter presenter) {
        FormDescriptorComponentPresenterHierarchyNode node = new FormDescriptorComponentPresenterHierarchyNode(presenter);
        addChild(node);
        return node;
    }

    public void dump(StringBuilder stringBuilder) {
        dump(stringBuilder, 0);
    }

    protected void dump(StringBuilder stringBuilder, int level) {
        for(int i = 0; i < level; i++) {
            stringBuilder.append("    ");
        }
        stringBuilder.append(presenter.getClass().getSimpleName());
        if(presenter instanceof FormFieldDescriptorPresenter) {
            FormFieldDescriptorPresenter fdp = (FormFieldDescriptorPresenter) presenter;
            stringBuilder.append(" [label=" ).append(fdp.getHeaderLabel()).append("]" );
        }
        stringBuilder.append("\n");
        for(FormDescriptorComponentPresenterHierarchyNode child : children) {
            child.dump(stringBuilder, level + 1);
        }
    }

    public void visit(Consumer<FormDescriptorComponentPresenter> consumer) {
        consumer.accept(this.presenter);
        for(FormDescriptorComponentPresenterHierarchyNode child : children) {
            child.visit(consumer);
        }
    }
}
