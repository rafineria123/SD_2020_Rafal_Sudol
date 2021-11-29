package pl.okazje.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@SpringBootApplication
//@ComponentScan({"pl.okazje.project.configurations","pl.okazje.project.controllers","pl.okazje.project.repositories","pl.okazje.project.entities"})
//@EnableJpaRepositories("pl.okazje.project.repositories")
//@EntityScan("entities")
@EnableJdbcHttpSession
public class ProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }


}
