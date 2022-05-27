package pl.okazje.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.restart.RestartEndpoint;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class RestartService {
    @Autowired
    private RestartEndpoint restartEndpoint;

    public void restartApp() {
        restartEndpoint.restart();
    }

    @Scheduled(fixedDelay = 200000)
    private void scheduledRestart(){
        restartApp();
    }
}
