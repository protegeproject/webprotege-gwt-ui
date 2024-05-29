package edu.stanford.bmir.protege.web.server.app;

import ch.qos.logback.classic.LoggerContext;
import edu.stanford.bmir.protege.web.server.filter.WebProtegeWebAppFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import static edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger.WebProtegeMarker;

public class WebProtegeServletContextListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(WebProtegeServletContextListener.class);

    public WebProtegeServletContextListener() {
    }

    public void contextInitialized(ServletContextEvent sce) {
        logger.info(WebProtegeMarker, "Initializing WebProtege");
        try {
            ServerComponent serverComponent = DaggerServerComponent.create();

            ServletContext servletContext = sce.getServletContext();

            servletContext.setAttribute(ServerComponent.class.getName(), serverComponent);

            servletContext.addServlet("DispatchService", serverComponent.getDispatchServlet())
                          .addMapping("/webprotege/dispatchservice");

            servletContext.addServlet("ProjectDownloadServlet", serverComponent.getProjectDownloadServlet())
                          .addMapping("/download");

            Runtime runtime = Runtime.getRuntime();
            logger.info("Max  Memory: {} MB", (runtime.maxMemory() / (1024 * 1024)));
            logger.info(WebProtegeMarker, "WebProtege initialization complete");
        } catch (Throwable error) {
            logger.error(WebProtegeMarker, "Encountered an error during initialization: {}", error.getMessage(), error);
            WebProtegeWebAppFilter.setError(error);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        try {
            logger.info(WebProtegeMarker, "Shutting down WebProtege");
            logger.info(WebProtegeMarker, "WebProtege shutdown complete");
            // Finally stop logging
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            loggerContext.stop();
        } finally {
            var servletContext = servletContextEvent.getServletContext();
            servletContext.removeAttribute(ServerComponent.class.getName());
        }

    }
}
