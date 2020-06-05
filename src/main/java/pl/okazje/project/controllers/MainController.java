package pl.okazje.project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Controller
public class MainController {

    @GetMapping("/")
    public ModelAndView homePage() {
        ModelAndView modelAndView = new ModelAndView("home");
        return modelAndView;
    }

    @GetMapping("/test")
    public ModelAndView testPage() {
        ModelAndView modelAndView = new ModelAndView("test");
        return modelAndView;

    }



}
