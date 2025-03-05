package edu.stanford.bmir.protege.web.shared.card;

public interface EntityCardContentDescriptorVisitor<R> {


    R visit(FormContentDescriptor descriptor);

    R visit(CustomContentDescriptor descriptor);
}
