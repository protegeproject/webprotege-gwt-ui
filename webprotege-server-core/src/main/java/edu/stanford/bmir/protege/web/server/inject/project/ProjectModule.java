package edu.stanford.bmir.protege.web.server.inject.project;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.search.EntitySearchFilterIndexesManager;
import edu.stanford.bmir.protege.web.server.shortform.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.object.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/03/2014
 *         <p>
 *         A  module for a project.  The module ensures that any object graph contains project specific objects for the
 *         specified project (e.g. root ontology, short form provider etc.)
 */
@Module
public class ProjectModule {

    private final ProjectId projectId;

    public ProjectModule(ProjectId projectId) {
        this.projectId = projectId;
    }

    @Provides
    @ProjectSingleton
    OWLDataFactory providesDataFactory() {
        return new OWLDataFactoryImpl();
    }

    @Provides
    @ProjectSingleton
    public OWLEntityProvider providesOWLEntityProvider(OWLDataFactory dataFactory) {
        return dataFactory;
    }

    @Provides
    @ProjectSingleton
    public OWLEntityByTypeProvider provideOwlEntityByTypeProvider(OWLDataFactory dataFactory) {
        return dataFactory;
    }

    @Provides
    @ProjectSingleton
    public ProjectId provideProjectId() {
        return projectId;
    }

    @Provides
    public OWLAnnotationPropertyProvider provideAnnotationPropertyProvider(OWLDataFactory factory) {
        return factory;
    }

    @Provides
    public HasLang provideHasLang() {
        return () -> "en";
    }

    @Provides
    OWLObjectSelector<OWLClassExpression> provideClassExpressionSelector(OWLClassExpressionSelector selector) {
        return selector;
    }

    @Provides
    OWLObjectSelector<OWLObjectPropertyExpression> provideObjectPropertyExpressionSelector(
            OWLObjectPropertyExpressionSelector selector) {
        return selector;
    }

    @Provides
    OWLObjectSelector<OWLDataPropertyExpression> provideDataPropertyExpressionSelector(OWLDataPropertyExpressionSelector selector) {
        return selector;
    }

    @Provides
    OWLObjectSelector<OWLIndividual> provideIndividualSelector(OWLIndividualSelector selector) {
        return selector;
    }

    @Provides
    OWLObjectSelector<SWRLAtom> provideSWRLAtomSelector(SWRLAtomSelector selector) {
        return selector;
    }

    @Provides
    @ProjectSingleton
    EntitySearchFilterIndexesManager provideEntitySearchFilterIndexesManager(LuceneIndexWriterImpl writer) {
        return writer;
    }
}

