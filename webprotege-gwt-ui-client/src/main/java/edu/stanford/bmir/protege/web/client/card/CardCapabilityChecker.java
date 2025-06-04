package edu.stanford.bmir.protege.web.client.card;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.access.Capability;

import java.util.logging.*;

public class CardCapabilityChecker {
    private static final Logger logger = Logger.getLogger(CardCapabilityChecker.class.getName());

    public static boolean hasCapability(Capability requiredCapability, ImmutableSet<Capability> aquiredCapabilities) {
        logger.log(Level.FINE, "Checking has capability: " + requiredCapability.getId().getId());
        if(aquiredCapabilities.isEmpty()){
            logger.log(Level.SEVERE, "no capabilities found");
        }else{
            aquiredCapabilities.forEach(capability -> logger.log(Level.FINE, "current capability: " + capability.getId().getId()));
        }
        return aquiredCapabilities.stream().anyMatch(aquiredCapability -> {
            logger.log(Level.FINE, "Found capability: " + aquiredCapability.getId().getId());
            return aquiredCapability.getId().equals(requiredCapability.getId());
        });
    }
}
