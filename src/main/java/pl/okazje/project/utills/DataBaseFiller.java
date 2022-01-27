package pl.okazje.project.utills;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.okazje.project.entities.Discount;
import pl.okazje.project.repositories.DiscountRepository;
import pl.okazje.project.services.DiscountService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class DataBaseFiller {

    @Autowired
    private DiscountRepository discountRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        List<Discount> listOfDiscounts = new ArrayList<>();
        discountRepository.findAll().forEach(listOfDiscounts::add);
        Random r = new Random();
        for(int i = 0;i<12;i++){
            Date dt = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(dt);
            c.add(Calendar.DATE, r.nextInt(10)+1);
            dt = c.getTime();
            listOfDiscounts.get(i).setExpireDate(dt);
            dt = new Date();
            c.setTime(dt);
            c.add(Calendar.DATE, -(r.nextInt(10)+1));
            dt = c.getTime();
            listOfDiscounts.get(i).setCreateDate(dt);
        }
        discountRepository.saveAll(listOfDiscounts);
    }
}
