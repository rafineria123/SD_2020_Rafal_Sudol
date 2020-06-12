package pl.okazje.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@ComponentScan({"pl.okazje.project.configurations","pl.okazje.project.controllers","pl.okazje.project.repositories","pl.okazje.project.entities"})
//@EnableJpaRepositories("pl.okazje.project.repositories")
//@EntityScan("entities")
public class ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}

}
