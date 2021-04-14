package edu.stanford.bmir.protege.web.client.login;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.chgpwd.ResetPasswordPresenter;
import edu.stanford.bmir.protege.web.client.dispatch.*;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserManager;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.auth.*;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.place.SignUpPlace;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_ACCOUNT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/16
 */
@ApplicationSingleton
public class LoginPresenter implements Presenter {

    private final LoginView view;

    private final DispatchServiceManager dispatch;

    private final LoggedInUserManager loggedInUserManager;

    private final PlaceController placeController;

    private final ResetPasswordPresenter resetPasswordPresenter;

    private Optional<Place> nextPlace = Optional.empty();

    private final DispatchErrorMessageDisplay errorDisplay;

    private final ProgressDisplay progressDisplay;


    @Inject
    public LoginPresenter(@Nonnull LoginView view,
                          @Nonnull DispatchServiceManager dispatch,
                          @Nonnull LoggedInUserManager loggedInUserManager,
                          @Nonnull PlaceController placeController,
                          @Nonnull ResetPasswordPresenter resetPasswordPresenter,
                          DispatchErrorMessageDisplay errorDisplay,
                          ProgressDisplay progressDisplay) {
        this.view = checkNotNull(view);
        this.dispatch = dispatch;
        this.loggedInUserManager = checkNotNull(loggedInUserManager);
        this.placeController = checkNotNull(placeController);
        this.resetPasswordPresenter = checkNotNull(resetPasswordPresenter);
        this.errorDisplay = errorDisplay;
        this.progressDisplay = progressDisplay;
        view.setSignInHandler(this::handleSignIn);
        view.setForgotPasswordHandler(this::handleResetPassword);
        view.setSignUpForAccountHandler(this::handleSignUpForAccout);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        view.clearView();
        view.hideErrorMessages();
        boolean canCreateUser = loggedInUserManager.isAllowedApplicationAction(CREATE_ACCOUNT);
        view.setSignUpForAccountVisible(canCreateUser);
        container.setWidget(view);
    }

    public void setNextPlace(Place nextPlace) {
        this.nextPlace = Optional.of(nextPlace);
    }

    private void handleSignUpForAccout() {
        placeController.goTo(new SignUpPlace());
    }

    private void handleSignIn() {
        view.hideErrorMessages();
        String userName = view.getUserName();
        if(userName.isEmpty()) {
            view.showUserNameRequiredErrorMessage();
            return;
        }
        String password = view.getPassword();
        if(password.isEmpty()) {
            view.showPasswordRequiredErrorMessage();
            return;
        }
        SignInDetails signInDetails = new SignInDetails(userName, password);
        handleSignIn(signInDetails);
    }

    private void handleResetPassword() {
        resetPasswordPresenter.resetPassword();
    }

    private void handleSignIn(SignInDetails signInDetails) {
        final UserId userId = UserId.getUserId(signInDetails.getUserName());
        final Password password = Password.create(signInDetails.getClearTextPassword());
        dispatch.execute(PerformLoginAction.create(userId, password),
                         new DispatchServiceCallbackWithProgressDisplay<PerformLoginResult>(errorDisplay,
                                                                                            progressDisplay) {
                             @Override
                             public String getProgressDisplayTitle() {
                                 return "Signing In";
                             }

                             @Override
                             public String getProgressDisplayMessage() {
                                 return "Please wait.";
                             }

                             @Override
                             public void handleSuccess(PerformLoginResult performLoginResult) {
                                 handlePerformLoginResult(performLoginResult);
                             }
                         });
    }

    private void handlePerformLoginResult(@Nonnull PerformLoginResult result) {
        if(result.getAuthenticationResponse() == AuthenticationResponse.SUCCESS) {
            UserInSession userInSession = result.getUserInSession();
            loggedInUserManager.setLoggedInUser(userInSession);
            nextPlace.ifPresent(placeController::goTo);
        }
        else {
            view.showLoginFailedErrorMessage();
        }
    }


    @Override
    public String toString() {
        return toStringHelper("LoginPresenter")
                .toString();
    }
}
