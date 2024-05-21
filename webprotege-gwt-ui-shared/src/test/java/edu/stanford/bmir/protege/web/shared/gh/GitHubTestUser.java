package edu.stanford.bmir.protege.web.shared.gh;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-10-19
 */
public class GitHubTestUser {

    protected static final String NODE_ID = "MDQ6VXNlcjE=";

    protected static final String LOGIN = "octocat";

    protected static final String AVATAR_URL = "https://github.com/images/error/octocat_happy.gif";

    protected static final String URL = "https://api.github.com/users/octocat";

    protected static final String HTML_URL = "https://github.com/octocat";



    protected static GitHubUser getTestUser() {
        return GitHubUser.get(LOGIN, 1, NODE_ID, AVATAR_URL, URL, HTML_URL, GitHubUserType.USER, false);
    }

}
