package pl.okazje.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.okazje.project.entities.Discount;
import pl.okazje.project.repositories.DiscountRepository;
import pl.okazje.project.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;


    public List<Discount> sortDiscountByRating(){

        long DAY_IN_MS = 1000 * 60 * 60 * 24;

        Date min = new Date(System.currentTimeMillis() - (1 * DAY_IN_MS));
        Date max = new Date();

        ArrayList<Discount> discountArrayList = new ArrayList<>(discountRepository.sortDiscountByRating());
        Collections.sort(discountArrayList, (o1,o2) -> {
            if(!o1.isOutDated()&&o2.isOutDated()){
                return -1;
            }
            return 1;
        });


        return discountArrayList;


    }





}
