package edu.stanford.bmir.protege.web.server.app;

import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-04
 */
public class UserDetailsManagerImpl implements UserDetailsManager {

    @Inject
    public UserDetailsManagerImpl() {
    }

    @Override
    public Optional<UserDetails> getUserDetails(UserId userId) {
        return Optional.empty();
    }

    @Override
    public Optional<String> getEmail(UserId userId) {
        return Optional.empty();
    }
}
