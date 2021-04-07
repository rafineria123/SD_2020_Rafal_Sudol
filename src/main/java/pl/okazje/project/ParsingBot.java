package pl.okazje.project;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
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

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

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

    public static int status = 0;
    public static String info = "";
    public static String good_scan = "";
    public static String bad_scan = "";
    public static String new_prom = "";

//    @PostConstruct
    public void init() throws IOException {

            fetchXkom();
            //fetchAmazon("https://www.amazon.com/Best-Sellers-Womens-Fashion/zgbs/fashion/", "Moda");
            //fetchAmazon("https://www.amazon.com/Best-Sellers-Grocery-Gourmet-Food/zgbs/grocery","Artykuły Spożywcze");
            //fetchAmazon("https://www.amazon.com/Best-Sellers-Sports-Outdoors/zgbs/sporting-goods", "Sport i Turystyka");



    }

    private static String translate(String langFrom, String langTo, String text) throws IOException {
        String urlStr = "https://script.google.com/macros/s/AKfycbxDUcLMCm5DQKS4dkzu-nDkleIqGHf9b5TYmUxD-91W3xYwXlA/exec" +
                "?q=" + URLEncoder.encode(text, "UTF-8") +
                "&target=" + langTo +
                "&source=" + langFrom;
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    public void fetchXkom(){

        status = 1;
        Thread t = new Thread(() -> {
            info = "Wyniki skanowania sklepu X-kom: ";
            int good_counter = 0;
            int bad_counter = 0;
            int new_prom_counter = 0;
        try {

            Document docfirst = null;
            try {
                docfirst = Jsoup.connect("https://www.x-kom.pl/bestsellery").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Element div = docfirst.getElementById("listing-container");

            Elements divsy = div.children();
            for (Element e : divsy) {
                try{


                    String link = "https:/www.x-kom.pl" + e.selectFirst("a[href]").attr("href");


                    //images
                    Document docscnd = null;
                    try {
                        docscnd = Jsoup.connect("https://www.x-kom.pl" + e.selectFirst("a[href]").attr("href")).get();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    Element divscnd = docscnd.getElementById("app");
                    divscnd = divscnd.child(0);
                    divscnd = divscnd.child(0);
                    divscnd = divscnd.child(3);
                    String img = divscnd.select("img").first().attr("src");

                    //title
                    try {
                        docscnd = Jsoup.connect("https://www.x-kom.pl" + e.selectFirst("a[href]").attr("href")).get();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    divscnd = docscnd.getElementById("app");
                    divscnd = divscnd.child(0);
                    divscnd = divscnd.child(0);
                    divscnd = divscnd.child(3);
                    divscnd = divscnd.child(1);
                    String title = divscnd.select("h1").first().text();

                    //price
                    try {
                        docscnd = Jsoup.connect("https://www.x-kom.pl" + e.selectFirst("a[href]").attr("href")).get();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
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
                    try {
                        docscnd = Jsoup.connect("https://www.x-kom.pl" + e.selectFirst("a[href]").attr("href")).get();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
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
                    try {
                        docscnd = Jsoup.connect("https://www.x-kom.pl" + e.selectFirst("a[href]").attr("href")).get();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    divscnd = docscnd.getElementById("Opis");
                    divscnd = divscnd.parent();
                    divscnd = divscnd.child(2);
                    divscnd.select("p").first();

                    String desc = divscnd.text();

                    good_counter++;




                    String upToNCharacters1 = title.substring(0, Math.min(title.length(), 80));
                    if(!discountRepository.findDiscountByTitleEquals(upToNCharacters1).isPresent()){

                        new_prom_counter++;
                        Discount discount = new Discount();
                        discount.setImage_url(img);
                        discount.setShop(shopRepository.findFirstByName("X-kom"));
                        discount.setTag(tagRepository.findFirstByName("Elektronika"));
                        discount.setUser(userRepository.findFirstByLogin("xkom"));
                        discount.setStatus(Discount.Status.ZATWIERDZONE);
                        Date dt = new Date();
                        Calendar c = Calendar.getInstance();
                        c.setTime(dt);
                        c.add(Calendar.DATE, 2);
                        dt = c.getTime();
                        discount.setExpire_date(dt);
                        discount.setCreationdate(new Date());
                        discount.setTitle(upToNCharacters1);
                        discount.setShipment_price(0.0);
                        discount.setDiscount_link(link);
                        String upToNCharacters = desc.substring(0, Math.min(desc.length(), 500));
                        discount.setContent(upToNCharacters);
                        price = price.replaceAll("[^\\d,.]", "");
                        price = price.replaceAll(",", ".");

                        discount.setCurrent_price(Double.parseDouble(price));
                        discount.setOld_price(discount.getCurrent_price() * 1.3);

                        discountRepository.save(discount);

                    }else {

                    }



                }catch (Exception eb){
                    eb.printStackTrace();
                    bad_counter++;
                }
            }


            //System.out.println(translate("en", "pl", "Meet the all-new Echo Dot - Our most popular smart speaker with Alexa. The sleek, compact design delivers crisp vocals and balanced bass for full sound."));


        }catch (Exception e){

            System.out.println("Błąd przy parsowaniu jednego z produktow. Ten produkt zostanie pominięty.");


        }
        status = 0;
        good_scan="Przeskanowano "+good_counter+" promocji.";
        bad_scan ="Błednie przeskanowane promocje: "+bad_counter+".";
        new_prom ="Dodano "+new_prom_counter+" nowych promocji.";
        });
        t.start();


    }

    public void fetchAmazon(String linktopage, String category){

        status = 1;
            Thread t = new Thread(() -> {
                info = "Wyniki skanowania sklepu Amazon: ";
                int good_counter = 0;
                int bad_counter = 0;
                int new_prom_counter = 0;
        try {
            Document docfirst = Jsoup.connect(linktopage).get();
            Element div = docfirst.getElementById("zg-ordered-list");
            Elements divy = div.children();
            int counter = 0;
            for (Element d : divy) {
                try {


                    //link to discount
                    String linktodiscount = "https://www.amazon.com" + d.select("a").first().attr("href");
                    String linktoimg = d.select("img").first().attr("src");
                    String title = d.select("a").first().child(1).text();
                    System.out.println(linktoimg);
                    System.out.println(linktodiscount);

                    WebClient webClient = new WebClient();
                    HtmlPage myPage = webClient.getPage(linktodiscount);
                    docfirst = Jsoup.parse(myPage.asXml());
                    //docfirst = Jsoup.connect(linktodiscount).get();
//                    div = docfirst.getElementById("imgTagWrapperId");
//                    //link to img
//                    String linktoimg = div.select("img").first().attr("src");
                    div = docfirst.getElementById("productDescription");
                    String desc = div.select("p").first().text();
                    div = docfirst.getElementById("priceblock_ourprice");
                    String pricetofix = div.text();
                    pricetofix = pricetofix.replaceAll("[^a-zA-Z0-9 .,]|(?<!\\d)[.,]|[.,](?!\\d)", "");
                    pricetofix = pricetofix.replaceAll(",", ".");
                    Double price = Double.parseDouble(pricetofix);

                    good_counter++;
                    if (!discountRepository.findDiscountByTitleEquals(translate("en", "pl", title)).isPresent()){
                        Discount discount = new Discount();
                    discount.setImage_url(linktoimg);
                    discount.setShop(shopRepository.findFirstByName("Amazon"));
                    discount.setTag(tagRepository.findFirstByName(category));
                    discount.setUser(userRepository.findFirstByLogin("amazon"));
                    discount.setStatus(Discount.Status.ZATWIERDZONE);
                    Date dt = new Date();
                    Calendar c = Calendar.getInstance();
                    c.setTime(dt);
                    c.add(Calendar.DATE, 5);
                    dt = c.getTime();
                    discount.setExpire_date(dt);
                    discount.setCreationdate(new Date());
                    discount.setTitle(translate("en", "pl", title));
                    discount.setShipment_price(0.0);
                    discount.setDiscount_link(linktodiscount);
                    discount.setContent(translate("en", "pl", desc));

                    discount.setCurrent_price(price);
                    discount.setOld_price(discount.getCurrent_price() * 1.3);
                    discountRepository.save(discount);
                    new_prom_counter++;
                }


                    if (counter > 1) {
                        break;
                    }
                    counter++;
                } catch (Exception e) {
                    e.printStackTrace();
                    bad_counter++;
                }


            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        good_scan="Przeskanowano "+good_counter+" promocji.";
        bad_scan ="Błednie przeskanowane promocje: "+bad_counter+".";
        new_prom ="Dodano "+new_prom_counter+" nowych promocji.";
        status = 0;
            });
        t.start();


    }
}