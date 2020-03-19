import job.TestQutaz;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        TestQutaz.main(null);

        System.out.println("----------------------------service start");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        System.out.println("----------------------------service stop");
    }
}
