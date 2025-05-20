package edu.stanford.bmir.protege.web.client.card;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.access.Capability;

public class CardCapabilityChecker {

    public static boolean hasCapability(Capability requiredCapability, ImmutableSet<Capability> aquiredCapabilities) {
        return aquiredCapabilities.stream().anyMatch(aquiredCapability -> aquiredCapability.getId().equals(requiredCapability.getId()));
    }
}
