package edu.stanford.bmir.protege.web.shared.icd;

import org.semanticweb.owlapi.model.IRI;

public class OldHistoryEncoding {

    public static String encodeIriToHostedLink(IRI iri){
       return iri.toString().replace(":", "$").replace("/", "^");
    }
}
