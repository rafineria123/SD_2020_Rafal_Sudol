package pl.okazje.project;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.okazje.project.entities.Discount;
import pl.okazje.project.repositories.DiscountRepository;
import pl.okazje.project.repositories.ShopRepository;
import pl.okazje.project.repositories.TagRepository;
import pl.okazje.project.repositories.UserRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Component
public class ParsingBot {

    @Autowired
    ShopRepository shopRepository;
    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    UserRepository userRepository;

    @PostConstruct
    public void init() throws IOException {

        Document docfirst = Jsoup.connect("https://www.x-kom.pl/bestsellery").get();
        Element div = docfirst.getElementById("listing-container");

        Elements divsy = div.children();
        for (Element e: divsy) {

            String link = "https:/www.x-kom.pl"+e.selectFirst("a[href]").attr("href");


            //images
            Document docscnd = Jsoup.connect("https://www.x-kom.pl"+e.selectFirst("a[href]").attr("href")).get();
            Element divscnd = docscnd.getElementById("app");
            divscnd = divscnd.child(0);
            divscnd = divscnd.child(0);
            divscnd = divscnd.child(3);
            String img = divscnd.select("img").first().attr("src");
            System.out.println(img);

            //title
            docscnd = Jsoup.connect("https://www.x-kom.pl"+e.selectFirst("a[href]").attr("href")).get();
            divscnd = docscnd.getElementById("app");
            divscnd = divscnd.child(0);
            divscnd = divscnd.child(0);
            divscnd = divscnd.child(3);
            divscnd = divscnd.child(1);
            String title = divscnd.select("h1").first().text();

            //price
            docscnd = Jsoup.connect("https://www.x-kom.pl"+e.selectFirst("a[href]").attr("href")).get();
            divscnd = docscnd.getElementById("app");
            divscnd = divscnd.child(0);
            divscnd = divscnd.child(0);
            divscnd = divscnd.child(3);
            divscnd = divscnd.child(1);
            divscnd = divscnd.child(1);
            divscnd = divscnd.child(1);
            divscnd = divscnd.child(0);
            divscnd = divscnd.child(0);
            divscnd = divscnd.child(0);

            String price = divscnd.text();


            //shipment
            docscnd = Jsoup.connect("https://www.x-kom.pl"+e.selectFirst("a[href]").attr("href")).get();
            divscnd = docscnd.getElementById("app");
            divscnd = divscnd.child(0);
            divscnd = divscnd.child(0);
            divscnd = divscnd.child(3);
            divscnd = divscnd.child(1);
            divscnd = divscnd.child(1);
            divscnd = divscnd.child(1);
            divscnd = divscnd.child(0);
            divscnd = divscnd.child(2);
            divscnd = divscnd.child(2);
            divscnd = divscnd.child(0);
            divscnd = divscnd.child(1);
            divscnd = divscnd.child(0);
            divscnd = divscnd.child(0);

            String shipment = divscnd.text();


            //description
            docscnd = Jsoup.connect("https://www.x-kom.pl"+e.selectFirst("a[href]").attr("href")).get();
            divscnd = docscnd.getElementById("Opis");
            divscnd = divscnd.parent();
            divscnd = divscnd.child(2);
            divscnd.select("p").first();

            String desc = divscnd.text();


            Discount discount = new Discount();
            discount.setImage_url(img);
            discount.setShop(shopRepository.findFirstByName("X-kom"));
            discount.setTag(tagRepository.findFirstByName("Gaming"));
            discount.setUser(userRepository.findUserByLogin("xkom"));
            discount.setStatus("zatwierdzone");
            Date dt = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(dt);
            c.add(Calendar.DATE, 2);
            dt = c.getTime();
            discount.setExpire_date(dt);
            discount.setCreationdate(new Date());
            String upToNCharacters1 = title.substring(0, Math.min(title.length(), 28));
            discount.setTitle(upToNCharacters1);
            discount.setShipment_price(0.0);
            discount.setDiscount_link(link);
            String upToNCharacters = desc.substring(0, Math.min(desc.length(), 100));
            discount.setContent(upToNCharacters);
            price = price.replaceAll("[^\\d.]", "");
//            price.replaceAll(",",".");
//            price.replaceFirst("0","");
//            price.replaceFirst("0","");
//            price.replaceFirst("0","");

            discount.setCurrent_price(Double.parseDouble(price));
            discount.setOld_price(discount.getCurrent_price()*1.3);

            discountRepository.save(discount);








        }



    }
}