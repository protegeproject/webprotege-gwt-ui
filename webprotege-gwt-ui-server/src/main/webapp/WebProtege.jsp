<%@ page import="edu.stanford.bmir.protege.web.server.access.AccessManager" %>
<%@ page import="edu.stanford.bmir.protege.web.server.filemanager.StyleCustomizationFileManager" %>
<%@ page import="java.io.IOException" %>
<%@ page import="edu.stanford.bmir.protege.web.server.app.ApplicationSettingsChecker" %>
<%@ page import="edu.stanford.bmir.protege.web.server.app.ServerComponent" %>
<!DOCTYPE html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>


    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="Content-Language" content="<%= request.getLocale() %>">

    <meta name="gwt:property" content="locale=<%= request.getLocale() %>">

    <title><%writeApplicationName(out);%></title>

    <link rel="shortcut icon" type="image/x-icon" href="ICAT-X4.svg"/>

    <link rel="stylesheet" href="css/WebProtege.css" type="text/css">
    <link rel="stylesheet" href="css/icd11ect-1.7.css">
    <link href="https://cdn.jsdelivr.net/css-toggle-switch/latest/toggle-switch.css" rel="stylesheet" />

    <script src="https://dagrejs.github.io/project/graphlib/v2.1.2/graphlib.js"></script>
    <script src="js/dagre/dagre.js"></script>
    <script src="js/polyfill/pathseg/pathseg.js"></script>
    <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script src="https://code.jquery.com/ui/1.14.1/jquery-ui.js"></script>
    <link rel="stylesheet" href="https://code.jquery.com/ui/1.14.1/themes/base/jquery-ui.css">

    <script src="https://d3js.org/d3.v5.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>

    <script src="https://unpkg.com/popper.js@1.14.4/dist/umd/popper.js"></script>
    <script src="https://unpkg.com/tooltip.js@1.3.0/dist/umd/tooltip.js"></script>
    <script type="module" src="https://unpkg.com/uuid@8.3.1/dist/umd/uuidv4.min.js"></script>
    <script src="js/ect/icd11ect-1.7.js"></script>

    <script src="https://cdn.jsdelivr.net/npm/@stomp/stompjs@5.0.0/bundles/stomp.umd.min.js"></script>
    <script type="text/javascript" language="javascript" src="webprotege/webprotege.nocache.js"></script>

    ${application.analytics}
</head>

<body>
<%
    /*
        Technically, the style element is not allowed here in the body element. However, in practice, all the
        major browsers support this.  Numerous major websites, including http://www.google.com, insert style
        elements into the body for various reasons.

        We do this here because we want to support the overriding of styles in CSS resources (for white labelling).
        In GWT, CSS resources are injected at runtime by appending style elements as children of the head element.
        This means that styles from CSS resources will always come after any inline style that we would put into the
        head element.  Placing the style element down here means we can work around this behaviour in an easy way.
        We may revisit this in the future, but this works fine for now.
    */
    injectStyleCustomizationIfPresent(out);
%>
<%
    checkApplicationSettings(out);
%>
<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1'
        style="position:absolute;width:0;height:0;border:0"></iframe>
<iframe src="" id="__download" style="width:0;height:0;border:0"></iframe>
</body>
</html>

<%!
    private final StyleCustomizationFileManager styleCustomizationFileManager = new StyleCustomizationFileManager();

    private ServerComponent getServerComponent() {
        ServletContext context = getServletConfig().getServletContext();
        return (ServerComponent) context.getAttribute(ServerComponent.class.getName());
    }

    private void writeApplicationName(JspWriter out) throws IOException {
        ServerComponent component = getServerComponent();
        out.print(component.getApplicationNameSupplier().get());
    }

    private AccessManager getAccessManager() {
        ServerComponent component = getServerComponent();
        return component.getAccessManager();
    }

    private ApplicationSettingsChecker getApplicationSettingsChecker() {
        return getServerComponent().getApplicationSettingsChecker();
    }

    private void checkApplicationSettings(JspWriter writer) throws IOException {
        ApplicationSettingsChecker checker = getApplicationSettingsChecker();
        if(!checker.isProperlyConfigured()) {
            writer.println("<div class=\"wp-configuration-error-banner\">");
            String appName = getServerComponent().getApplicationNameSupplier().get();
            writer.println(String.format("%s is not configured properly",
                                         appName));
            writer.println("</div>");
        }
    }

    private void injectStyleCustomizationIfPresent(JspWriter writer) throws IOException {
        String styleCustomization = getStyleCustomization();
        if (!styleCustomization.isEmpty()) {
            writer.println("<style>");
            writer.println();
            writer.println("/* Custom styles that override the default WebProtege styles */");
            writer.println();
            writer.println(styleCustomization);
            writer.println();
            writer.println("/* End of custom styles */");
            writer.println();
            writer.println("</style>");
        }
    }

    private String getStyleCustomization() {
        return styleCustomizationFileManager.getStyleCustomization();
    }
%>
