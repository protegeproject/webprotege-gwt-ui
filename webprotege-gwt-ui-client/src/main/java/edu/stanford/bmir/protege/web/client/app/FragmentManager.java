package edu.stanford.bmir.protege.web.client.app;

import com.google.gwt.http.client.URL;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;

import java.util.logging.Logger;

/**
 * Utility class for managing URL fragment restoration after user authentication redirects to keycloak.
 * This is needed because GWT uses URL fragments to store place/location information but fragments
 * get stripped by the browser.
 * <p>
 * This class checks for a URL fragment passed as a query parameter or stored in local storage,
 * and applies it to the current URL to restore user navigation state on the client-side.
 */
public class FragmentManager {

    private static final Logger logger = Logger.getLogger(FragmentManager.class.getName());

    /**
     * The name of the query parameter and localStorage key used to store the fragment.
     */
    public static final String FRAGMENT_KEY_NAME = "fragment";

    /**
     * Restores the URL fragment after a post-login redirect.
     * <p>
     * If a fragment is found in the query parameters (as {@code ?fragment=...}), it is decoded
     * and used to redirect the user to the appropriate location in WebProtege. If not found, it checks
     * local storage for a previously saved fragment and uses that instead.
     */
    public static void handlePostLoginFragment() {
        // Check if fragment was passed as a query parameter (e.g., ?fragment=encoded_fragment)
        String fragmentParam = Window.Location.getParameter(FRAGMENT_KEY_NAME);
        if (isNonEmpty(fragmentParam)) {
            String decodedFragment = URL.decodeQueryString(fragmentParam);
            redirectToFragment(decodedFragment);
            return;
        }

        // Fallback: check if a fragment is stored in localStorage
        Storage localStorage = Storage.getLocalStorageIfSupported();
        if (localStorage != null) {
            String savedHash = localStorage.getItem(FRAGMENT_KEY_NAME);
            if (isNonEmpty(savedHash)) {
                logger.info("Applying locally stored fragment and redirecting: " + savedHash);
                localStorage.removeItem(FRAGMENT_KEY_NAME);
                redirectToFragment(stripLeadingHash(savedHash));
            }
        }
    }

    /**
     * Returns {@code true} if the string is not {@code null} and contains non-whitespace characters.
     *
     * @param s the string to test
     * @return {@code true} if the string is non-null and not empty after trimming
     */
    private static boolean isNonEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    /**
     * Redirects the browser to the current path with the specified fragment appended.
     *
     * @param fragment the fragment (anchor) to apply to the URL, without the leading {@code #}
     */
    private static void redirectToFragment(String fragment) {
        String nextLocation = Window.Location.getPath() + "#" + fragment;
        Window.Location.assign(nextLocation);
    }

    /**
     * Removes a leading {@code #} character from a fragment string, if present.
     *
     * @param fragment the raw fragment string
     * @return the fragment without a leading {@code #}, or the original string if none is found
     */
    private static String stripLeadingHash(String fragment) {
        return fragment.startsWith("#") ? fragment.substring(1) : fragment;
    }

    /**
     * Stores the current URL fragment (if present) in the browser's local storage.
     * <p>
     * This is typically called before redirecting to an external login provider like Keycloak.
     * Since URL fragments are not preserved across full-page reloads or redirects,
     * this allows the application to restore the client-side route after login.
     */
    public static void storeCurrentWindowLocationFragment() {
        // Stores the route in local storage for later use by onModuleLoad() to restore
        // the location.
        String hash = Window.Location.getHash();
        Storage localStorage = Storage.getLocalStorageIfSupported();
        if (localStorage != null && hash != null && !hash.isEmpty()) {
            localStorage.setItem(FRAGMENT_KEY_NAME, hash);
        }
    }
}

