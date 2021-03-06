package net.gangelov.memories;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.SQLException;

public class Initializer implements ServletContextListener
{
    public void contextInitialized(ServletContextEvent contextEvent) {
        System.out.println("Initializing application");
        try {
            Class.forName("org.postgresql.Driver");
            net.gangelov.orm.Model.initialize(
                    Config.DB_CONNECTION_STRING,
                    "net.gangelov.memories.models"
            );
//            Database.connect("jdbc:postgresql://localhost/memories?user=postgres&password=password");
        } catch (SQLException e) {
            System.out.println("Cannot connect to database");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Cannot load postgresql driver");
            e.printStackTrace();
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
    }

    public void contextDestroyed(ServletContextEvent contextEvent) { }
}