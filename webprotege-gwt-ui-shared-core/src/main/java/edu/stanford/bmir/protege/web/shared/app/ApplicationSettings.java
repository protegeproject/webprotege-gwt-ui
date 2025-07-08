package edu.stanford.bmir.protege.web.shared.app;

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2017
 */
public class ApplicationSettings implements Serializable, IsSerializable {

    private String applicationName;

    private EmailAddress systemNotificationEmailAddress;


    private ApplicationLocation applicationLocation;


    private AccountCreationSetting accountCreationSetting;

    private List<UserId> accountCreators;

    private ProjectCreationSetting projectCreationSetting;

    private List<UserId> projectCreators;

    private ProjectUploadSetting projectUploadSetting;

    private List<UserId> projectUploaders;

    private NotificationEmailsSetting notificationEmailsSetting;

    private long maxUploadSize;

    @GwtSerializationConstructor
    private ApplicationSettings() {
    }

    public ApplicationSettings( String applicationName,
                                EmailAddress systemNotificationEmailAddress,
                                ApplicationLocation applicationLocation,
                                AccountCreationSetting accountCreationSetting,
                                List<UserId> accountCreators,
                                ProjectCreationSetting projectCreationSetting,
                                List<UserId> projectCreators,
                                ProjectUploadSetting projectUploadSetting,
                                List<UserId> projectUploaders,
                                NotificationEmailsSetting notificationEmailsSetting,
                               long maxUploadSize) {
        this.applicationName = checkNotNull(applicationName);
        this.systemNotificationEmailAddress = checkNotNull(systemNotificationEmailAddress);
        this.applicationLocation = checkNotNull(applicationLocation);
        this.accountCreationSetting = checkNotNull(accountCreationSetting);
        this.accountCreators = checkNotNull(accountCreators);
        this.projectCreationSetting = checkNotNull(projectCreationSetting);
        this.projectCreators = checkNotNull(projectCreators);
        this.projectUploadSetting = checkNotNull(projectUploadSetting);
        this.projectUploaders = checkNotNull(projectUploaders);
        this.notificationEmailsSetting = checkNotNull(notificationEmailsSetting);
        this.maxUploadSize = maxUploadSize;
    }


    public String getApplicationName() {
        return applicationName;
    }


    public EmailAddress getSystemNotificationEmailAddress() {
        return systemNotificationEmailAddress;
    }


    public AccountCreationSetting getAccountCreationSetting() {
        return accountCreationSetting;
    }


    public List<UserId> getAccountCreators() {
        return accountCreators;
    }


    public ProjectCreationSetting getProjectCreationSetting() {
        return projectCreationSetting;
    }


    public List<UserId> getProjectCreators() {
        return projectCreators;
    }


    public ProjectUploadSetting getProjectUploadSetting() {
        return projectUploadSetting;
    }


    public List<UserId> getProjectUploaders() {
        return projectUploaders;
    }


    public NotificationEmailsSetting getNotificationEmailsSetting() {
        return notificationEmailsSetting;
    }


    public ApplicationLocation getApplicationLocation() {
        return applicationLocation;
    }

    public long getMaxUploadSize() {
        return maxUploadSize;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(
                applicationName,
                systemNotificationEmailAddress,
                applicationLocation,
                accountCreationSetting,
                accountCreators,
                projectCreationSetting,
                projectCreators,
                projectUploadSetting,
                projectUploaders,
                notificationEmailsSetting,
                maxUploadSize
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ApplicationSettings)) {
            return false;
        }
        ApplicationSettings other = (ApplicationSettings) obj;
        return this.applicationName.equals(other.applicationName)
                && this.systemNotificationEmailAddress.equals(other.systemNotificationEmailAddress)
                && this.applicationLocation.equals(other.applicationLocation)
                && this.accountCreationSetting == other.accountCreationSetting
                && this.accountCreators.equals(other.accountCreators)
                && this.projectCreationSetting == other.projectCreationSetting
                && this.projectCreators.equals(other.projectCreators)
                && this.projectUploadSetting == other.projectUploadSetting
                && this.projectUploaders.equals(other.projectUploaders)
                && this.notificationEmailsSetting == other.notificationEmailsSetting
                && this.maxUploadSize == other.maxUploadSize;
    }


    @Override
    public String toString() {
        return toStringHelper("ApplicationSettings" )
                .add("name", applicationName)
                .add("systemNotificationEmail", systemNotificationEmailAddress)
                .addValue(applicationLocation)
                .addValue(accountCreationSetting)
                .add("accountCreators", accountCreators)
                .addValue(projectCreationSetting)
                .add("projectCreators", projectCreators)
                .addValue(projectUploaders)
                .add("projectUploaders", projectUploaders)
                .addValue(notificationEmailsSetting)
                .toString();
    }
}
