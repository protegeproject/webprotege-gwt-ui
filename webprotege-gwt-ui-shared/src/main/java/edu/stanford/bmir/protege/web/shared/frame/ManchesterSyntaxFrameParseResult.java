package edu.stanford.bmir.protege.web.shared.frame;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public enum ManchesterSyntaxFrameParseResult {

    UNCHANGED,
    CHANGED,
    ERROR;


    public static ManchesterSyntaxFrameParseResult getResult(String result) {
        for(ManchesterSyntaxFrameParseResult value : ManchesterSyntaxFrameParseResult.values()){
            if(value.toString().equals(result)){
                return value;
            }
        }
        return UNCHANGED;
    }
}
