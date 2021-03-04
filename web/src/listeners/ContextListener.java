package listeners;

import bms.engine.Engine;
import notifications.managers.NotificationManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("contextInitialized");

        Engine engine = new Engine();
        engine.loadState();
        servletContextEvent.getServletContext().setAttribute("engine", engine);

        NotificationManager notificationManager = new NotificationManager();
        servletContextEvent.getServletContext().setAttribute("notificationManager", notificationManager);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        Engine engine = (Engine)servletContextEvent.getServletContext().getAttribute("engine");
        engine.saveState();
        System.out.println("contextDestroyed");
    }
}
