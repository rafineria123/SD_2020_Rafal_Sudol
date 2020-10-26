//package pl.okazje.project;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.io.IOException;
//
//@Component
//public class ParsingBot {
//
//    @PostConstruct
//    public void init() throws IOException {
//
//        Document doc = Jsoup.connect("https://www.x-kom.pl/bestsellery").get();
//        Element div = doc.getElementById("listing-container");
//        Elements divsy = div.children();
//        Element div1 = divsy.get(0);
//        Element div2 = div1.child(0);
//        Element div3 = div2.child(1);
//        Element div4 = div3.child(2);
//        Element div5 = div4.child(0);
//        Element div6 = div5.child(0);
//        Element div7 = div6.child(0);
//        Element div8 = div7.child(0);
//        Element span1 = div8.child(0);
//        System.out.println(span1.text());
//
//
//    }
//}