package pl.okazje.project.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.okazje.project.entities.Discount;
import pl.okazje.project.services.DiscountService;

import java.util.List;

@RestController
@RequestMapping("/app/discounts")
public class DiscountRestController {

    private DiscountService discountService;

    public DiscountRestController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping
    public List<Discount> findAll() {
        return discountService.findAllByOrderByCreateDateDesc();
    }


}
