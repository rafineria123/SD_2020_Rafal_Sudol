package pl.okazje.project.utills;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import pl.okazje.project.entities.Discount;
import pl.okazje.project.repositories.DiscountRepository;
import pl.okazje.project.repositories.ShopRepository;
import pl.okazje.project.repositories.TagRepository;
import pl.okazje.project.repositories.UserRepository;
import pl.okazje.project.services.DiscountService;
import pl.okazje.project.services.ShopService;
import pl.okazje.project.services.TagService;
import pl.okazje.project.services.UserService;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

// TODO: bot to fetch discounts from other web pages.https://www.x-kom.pl/bestsellery?f%5BproductMarks%5D%5BCrossedPrice%5D=1&f%5BproductMarks%5D%5BPromotion%5D=1&f%5BproductMarks%5D%5BLastItems%5D=1
@Component
public class DiscountFinder {

    public enum Status{AWAITING, WORKING, SUCCESS, ERROR}

    ShopService shopService;
    DiscountRepository discountRepository;
    TagService tagService;
    UserService userService;

    public Status status;

    public DiscountFinder(ShopService shopService, DiscountRepository discountRepository, TagService tagService, UserService userService) {
        this.shopService = shopService;
        this.discountRepository = discountRepository;
        this.tagService = tagService;
        this.userService = userService;
        this.status = Status.AWAITING;
    }


    public void fetchXkom(){

        status = Status.WORKING;

            Thread t = new Thread(() -> {
                    Document allItemsPage = null;
                    try {
                        allItemsPage = Jsoup.connect("https://www.x-kom.pl/bestsellery?f%5BproductMarks%5D%5BCrossedPrice%5D=1&f%5BproductMarks%5D%5BPromotion%5D=1&f%5BproductMarks%5D%5BLastItems%5D=1").get();
                    } catch (IOException e) {
                        status = Status.ERROR;
                        return;
                    }
                    Element allItemsContainer = allItemsPage.getElementById("listing-container");
                    Elements allItems = allItemsContainer.children();
                    for (Element item : allItems) {
                        String itemLink = "https://www.x-kom.pl" + item.selectFirst("a[href]").attr("href");

                        Document itemPage = null;
                        try {
                            itemPage = Jsoup.connect(itemLink).get();
                        } catch (IOException e) {
                            status = Status.ERROR;
                            return;
                        }
                        try {

                            Element itemContainer = itemPage.getElementById("app");
                            String title = itemContainer.selectFirst("h1").text();
                            String cutTitle = title.substring(0, Math.min(title.length(), 80));
                            String imageUrl = item.selectFirst("img[src$=.jpg]").attr("src");
                            imageUrl = imageUrl.replaceAll("mini", "medium");
                            Elements itemPrices = itemContainer.getElementsByClass("fZcwdN").get(0).children();
                            String oldPrice = itemPrices.get(0).text();
                            String currentPrice = itemPrices.get(1).text();
                            Elements itemDescription = itemContainer.getElementsByClass("product-description");
                            String description = "";
                            for (Element e : itemDescription.select("p")) {
                                description = description + e.text() + "\n";
                            }
                            description = description.substring(0, Math.min(description.length(), 500));
                            description = description.substring(0, description.lastIndexOf('.') + 1);

                            if (!discountRepository.findFirstByTitleEquals(cutTitle).isPresent()) {
                                Discount discount = new Discount();
                                discount.setImageUrl(imageUrl);
                                discount.setShop(shopService.findFirstByName("X-kom").get());
                                discount.setTag(tagService.findFirstByName("Elektronika").get());
                                discount.setUser(userService.findFirstByLogin("xkom").get());
                                discount.setStatus(Discount.Status.ACCEPTED);
                                Date dt = new Date();
                                Calendar c = Calendar.getInstance();
                                c.setTime(dt);
                                c.add(Calendar.DATE, 2);
                                dt = c.getTime();
                                discount.setExpireDate(dt);
                                discount.setCreateDate(new Date());
                                discount.setTitle(cutTitle);
                                discount.setShipmentPrice(0.0);
                                discount.setDiscountLink(itemLink);
                                discount.setContent(description);
                                oldPrice = oldPrice.replaceAll("[^\\d,.]", "");
                                oldPrice = oldPrice.replaceAll(",", ".");
                                currentPrice = currentPrice.replaceAll("[^\\d,.]", "");
                                currentPrice = currentPrice.replaceAll(",", ".");

                                discount.setCurrentPrice(Double.parseDouble(currentPrice));
                                discount.setOldPrice(Double.parseDouble(oldPrice));

                                discountRepository.save(discount);

                            }
                        }catch (Exception e){
                            continue;
                        }


                    }
                    status = Status.SUCCESS;

            });
            t.start();

    }

    public void fetchAmazon(String linkToPage, String category){

        status = Status.WORKING;
        try {
            Thread t = new Thread(() -> {
                Document docfirst = null;
                try {
                    docfirst = Jsoup.connect(linkToPage).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(docfirst.nodeName());
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
                        div = docfirst.getElementById("productDescription");
                        String desc = div.select("p").first().text();
                        div = docfirst.getElementById("price_inside_buybox");
                        String pricetofix = div.text();
                        pricetofix = pricetofix.replaceAll("[^a-zA-Z0-9 .,]|(?<!\\d)[.,]|[.,](?!\\d)", "");
                        pricetofix = pricetofix.replaceAll(",", ".");
                        Double price = Double.parseDouble(pricetofix);
                        if (!discountRepository.findFirstByTitleEquals(translate("en", "pl", title)).isPresent()) {
                            Discount discount = new Discount();
                            discount.setImageUrl(linktoimg);
                            discount.setShop(shopService.findFirstByName("Amazon").get());
                            discount.setTag(tagService.findFirstByName(category).get());
                            discount.setUser(userService.findFirstByLogin("amazon").get());
                            discount.setStatus(Discount.Status.ACCEPTED);
                            Date dt = new Date();
                            Calendar c = Calendar.getInstance();
                            c.setTime(dt);
                            c.add(Calendar.DATE, 5);
                            dt = c.getTime();
                            discount.setExpireDate(dt);
                            discount.setCreateDate(new Date());
                            String wronglyCodedTitle = translate("en", "pl", title);
                            discount.setTitle(wronglyCodedTitle);
                            discount.setShipmentPrice(0.0);
                            discount.setDiscountLink(linktodiscount);
                            String wronglyCodedContent = translate("en", "pl", desc);
                            discount.setContent(wronglyCodedContent);
                            discount.setCurrentPrice(price);
                            discount.setOldPrice(discount.getCurrentPrice() * 1.3);
                            discountRepository.save(discount);
                        }


                        if (counter > 1) {
                            break;
                        }
                        counter++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                status = Status.SUCCESS;
            });
            t.start();
        }catch (Exception e){
            status = Status.ERROR;
        }


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

}