package server;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("server")
@ComponentScan("useful_staff")
@ComponentScan("database")
@ComponentScan("threads")
@PropertySource("classpath:config.properties")
public class AppConfiguration {

}
