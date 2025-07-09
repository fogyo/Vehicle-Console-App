package server;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("server")
@ComponentScan("useful_staff")
@ComponentScan("database")
public class AppConfiguration {

}
