package edu.stanford.bmir.protege.web.shared.card;

public interface CardContentDescriptorVisitor<R> {


    R visit(FormCardContentDescriptor formCardContentDescriptor);

    R visit(PortletCardContentDescriptor portletCardContentDescriptor);
}
