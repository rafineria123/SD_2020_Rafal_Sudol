package pl.okazje.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.okazje.project.entities.*;
import pl.okazje.project.entities.Discount;
import pl.okazje.project.entities.bans.DiscountBan;
import pl.okazje.project.entities.User;
import pl.okazje.project.repositories.DiscountRepository;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DiscountService {


    private final DiscountRepository discountRepository;
    private final AuthenticationService authenticationService;
    private final ShopService shopService;
    private final TagService tagService;
    private final EmailService emailService;
    private final SessionService sessionService;

    @Autowired
    public DiscountService(EmailService emailService, DiscountRepository discountRepository, AuthenticationService authenticationService, ShopService shopService, TagService tagService, SessionService sessionService) {
        this.discountRepository = discountRepository;
        this.authenticationService = authenticationService;
        this.shopService = shopService;
        this.tagService = tagService;
        this.emailService = emailService;
        this.sessionService = sessionService;
    }


    public List<Discount> findAllIncludeSortingAndFiltering(Map byArgument){
        List<Discount> discounts = Collections.emptyList();
        HttpSession session = sessionService.getCurrentSession();
        if(session.getAttribute("sort")!=null&&!session.getAttribute("sort").equals("date")){
            switch((String)session.getAttribute("sort")){
                case "rating":
                    switch ((String)session.getAttribute("date")){
                        case "day":
                            if(byArgument.containsKey("tag")){
                                discounts = this.findAllByTagAndCreateDateBetweenNowAndYesterdayOrderByRatingDesc((String)byArgument.get("tag"));
                                break;
                            }
                            if(byArgument.containsKey("shop")){
                                discounts = this.findAllByShopAndCreateDateBetweenNowAndYesterdayOrderByRatingDesc((String)byArgument.get("shop"));
                                break;
                            }
                            if(byArgument.containsKey("search")){
                                discounts = this.FindAllBySearchAndCreateDateBetweenNowAndYesterdayOrderByRatingDesc("%"+byArgument.get("search")+"%");
                                break;
                            }
                            discounts = this.findAllByCreateDateBetweenNowAndYesterdayOrderByRatingDesc();
                            break;
                        case "week":
                            if(byArgument.containsKey("tag")){
                                discounts = this.findAllByTagAndCreateDateBetweenNowAndLastWeekOrderByRatingDesc((String)byArgument.get("tag"));
                                break;
                            }
                            if(byArgument.containsKey("shop")){
                                discounts = this.findAllByShopAndCreateDateBetweenNowAndLastWeekOrderByRatingDesc((String)byArgument.get("shop"));
                                break;
                            }
                            if(byArgument.containsKey("search")){
                                discounts = this.FindAllBySearchAndCreateDateBetweenNowAndLastWeekOrderByRatingDesc("%"+byArgument.get("search")+"%");
                                break;
                            }
                            discounts = this.findAllByCreateDateBetweenNowAndLastWeekOrderByRatingDesc();
                            break;
                        default:
                            if(byArgument.containsKey("tag")){
                                discounts = this.findAllByTagOrderByRatingDesc((String)byArgument.get("tag"));
                                break;
                            }
                            if(byArgument.containsKey("shop")){
                                discounts = this.findAllByShopOrderByRatingDesc((String)byArgument.get("shop"));
                                break;
                            }
                            if(byArgument.containsKey("search")){
                                discounts = this.FindAllBySearchOrderByRatingDesc("%"+byArgument.get("search")+"%");
                                break;
                            }
                            discounts = this.findAllByOrderByRatingDesc();
                            break;
                    }
                    break;
                case "comments":
                    switch ((String)session.getAttribute("date")){
                        case "day":
                            if(byArgument.containsKey("tag")){
                                discounts = this.findAllByTagAndCreateDateBetweenNowAndYesterdayOrderByCommentDesc((String)byArgument.get("tag"));
                                break;
                            }
                            if(byArgument.containsKey("shop")){
                                discounts = this.findAllByShopAndCreateDateBetweenNowAndYesterdayOrderByCommentDesc((String)byArgument.get("shop"));
                                break;
                            }
                            if(byArgument.containsKey("search")){
                                discounts = this.FindAllBySearchAndCreateDateBetweenNowAndYesterdayOrderByCommentDesc("%"+byArgument.get("search")+"%");
                                break;
                            }
                            discounts = this.findAllByCreationdateBetweenNowAndYesterdayOrderByCommentDesc();
                            break;
                        case "week":
                            if(byArgument.containsKey("tag")){
                                discounts = this.findAllByTagAndCreateDateBetweenNowAndLastWeekOrderByCommentDesc((String)byArgument.get("tag"));
                                break;
                            }
                            if(byArgument.containsKey("shop")){
                                discounts = this.findAllByShopAndCreateDateBetweenNowAndLastWeekOrderByCommentDesc((String)byArgument.get("shop"));
                                break;
                            }
                            if(byArgument.containsKey("search")){
                                discounts = this.FindAllBySearchAndCreateDateBetweenNowAndLastWeekOrderByCommentDesc("%"+byArgument.get("search")+"%");
                                break;
                            }
                            discounts = this.findAllByCreateDateBetweenNowAndLastWeekOrderByCommentDesc();
                            break;
                        default:
                            if(byArgument.containsKey("tag")){
                                discounts = this.findAllByTagOrderByCommentDesc((String)byArgument.get("tag"));
                                break;
                            }
                            if(byArgument.containsKey("shop")){
                                discounts = this.findAllByShopOrderByCommentDesc((String)byArgument.get("shop"));
                                break;
                            }
                            if(byArgument.containsKey("search")){
                                discounts = this.FindAllBySearchOrderByCommentDesc("%"+byArgument.get("search")+"%");
                                break;
                            }
                            discounts = this.findAllByOrderByCommentDesc();
                            break;
                    }
                    break;
            }
        }else {
            if(byArgument.containsKey("tag")){
                discounts = this.findAllByTagOrderByCreateDateDesc((String)byArgument.get("tag"));
            }else if(byArgument.containsKey("shop")){
                discounts = this.findAllByShopOrderByCreateDateDesc((String)byArgument.get("shop"));
            }else if(byArgument.containsKey("search")){
                discounts = this.FindAllBySearchOrderByCreateDateDesc("%"+byArgument.get("search")+"%");
            }else  {
                discounts = this.findAllByOrderByCreateDateDesc();
            }
        }

        discounts = this.filter(discounts);
        return discounts;
    }

    public List<Discount> findAllByUserIncludeSorting(User user){
        List<Discount> discounts = Collections.emptyList();
        Session session = sessionService.findActiveSessionForUser(user).get();
        if(session.getAttribute("sort")!=null&&!session.getAttribute("sort").equals("date")){
            switch((String)session.getAttribute("sort")){
                case "rating":
                    discounts = this.findAllByUserIdOrderByRatingDesc(user.getUserId());
                    break;
                case "comments":
                    discounts = this.findAllByUserIdOrderByCommentDesc(user.getUserId());
                    break;
            }
        }else {
            discounts = this.findAllByUserIdOrderByCreateDateDesc(user.getUserId());
        }
        return discounts;
    }

    public List<Discount> filter(List<Discount> discounts){
        if(sessionService.getCurrentSession().getAttribute("filter")!=null&&sessionService.getCurrentSession().getAttribute("filter").equals("true")){
            Iterator<Discount> i = discounts.iterator();
            while (i.hasNext()) {
                Discount d = i.next();
                if(d.isOutDated()){
                    i.remove();
                }
            }
        }
        return discounts;
    }

    public List<Discount> removeAllBannedDiscounts(List<Discount> discounts){
        for (Discount d:discounts) {

        }
        return null;
    }

    public List<Discount> findAllByOrderByCreateDateDesc(){
        return discountRepository.findAllByOrderByCreateDateDesc();
    }
    public List<Discount> findAllByOrderByRatingDesc(){
        return this.discountRepository.findAllByOrderByRatingDesc();
    }

    public List<Discount> findAllByCreateDateBetweenNowAndYesterdayOrderByRatingDesc(){
        return this.discountRepository.findAllByCreateDateBetweenNowAndYesterdayOrderByRatingDesc();
    }


    public List<Discount> findAllByCreateDateBetweenNowAndLastWeekOrderByRatingDesc(){
        return this.discountRepository.findAllByCreateDateBetweenNowAndLastWeekOrderByRatingDesc();
    }

    public List<Discount> findAllByOrderByCommentDesc(){
        return this.discountRepository.findAllByOrderByCommentDesc();
    }

    public List<Discount> findAllByCreationdateBetweenNowAndYesterdayOrderByCommentDesc(){
        return this.discountRepository.findAllByCreateDateBetweenNowAndYesterdayOrderByCommentDesc();
    }

    public List<Discount> findAllByCreateDateBetweenNowAndLastWeekOrderByCommentDesc(){
        return this.discountRepository.findAllByCreateDateBetweenNowAndLastWeekOrderByCommentDesc();
    }

    public List<Discount> findAllByTagOrderByCreateDateDesc(String tag){
        return this.discountRepository.findAllByTagOrderByCreateDateDesc(tag);
    }

    public List<Discount> findAllByShopOrderByCreateDateDesc(String shop){
        return this.discountRepository.findAllByShopOrderByCreateDateDesc(shop);
    }

    public List<Discount> findAllByTagOrderByRatingDesc(String tag){
        return this.discountRepository.findAllByTagOrderByRatingDesc(tag);
    }


    public List<Discount> findAllByTagAndCreateDateBetweenNowAndYesterdayOrderByRatingDesc(String tag){
        return this.discountRepository.findAllByTagAndCreateDateBetweenNowAndYesterdayOrderByRatingDesc(tag);
    }

    public List<Discount> findAllByTagAndCreateDateBetweenNowAndLastWeekOrderByRatingDesc(String tag){
        return this.discountRepository.findAllByTagAndCreateDateBetweenNowAndLastWeekOrderByRatingDesc(tag);
    }

    public List<Discount> findAllByTagOrderByCommentDesc(String tag){
        return this.discountRepository.findAllByTagOrderByCommentDesc(tag);
    }

    public List<Discount> findAllByTagAndCreateDateBetweenNowAndYesterdayOrderByCommentDesc(String tag){
        return this.discountRepository.findAllByTagAndCreateDateBetweenNowAndYesterdayOrderByCommentDesc(tag);
    }

    public List<Discount> findAllByTagAndCreateDateBetweenNowAndLastWeekOrderByCommentDesc(String tag){
        return this.discountRepository.findAllByTagAndCreateDateBetweenNowAndLastWeekOrderByCommentDesc(tag);
    }

    public List<Discount> findAllByShopOrderByRatingDesc(String shop){
        return this.discountRepository.findAllByShopOrderByRatingDesc(shop);
    }

    public List<Discount> findAllByShopAndCreateDateBetweenNowAndYesterdayOrderByRatingDesc(String shop){
        return this.discountRepository.findAllByShopAndCreateDateBetweenNowAndYesterdayOrderByRatingDesc(shop);
    }

    public List<Discount> findAllByShopAndCreateDateBetweenNowAndLastWeekOrderByRatingDesc(String shop){
        return this.discountRepository.findAllByShopAndCreateDateBetweenNowAndLastWeekOrderByRatingDesc(shop);
    }

    public List<Discount> findAllByShopOrderByCommentDesc(String shop){
        return this.discountRepository.findAllByShopOrderByCommentDesc(shop);
    }

    public List<Discount> findAllByShopAndCreateDateBetweenNowAndYesterdayOrderByCommentDesc(String shop){
        return this.discountRepository.findAllByShopAndCreateDateBetweenNowAndYesterdayOrderByCommentDesc(shop);
    }

    public List<Discount> findAllByShopAndCreateDateBetweenNowAndLastWeekOrderByCommentDesc(String shop){
        return this.discountRepository.findAllByShopAndCreateDateBetweenNowAndLastWeekOrderByCommentDesc(shop);
    }

    public List<Discount> FindAllBySearchOrderByCreateDateDesc(String search){
        return this.discountRepository.FindAllBySearchOrderByCreateDateDesc(search);
    }

    public List<Discount> FindAllBySearchOrderByCommentDesc(String search){
        return this.discountRepository.FindAllBySearchOrderByCommentDesc(search);
    }

    public List<Discount> FindAllBySearchAndCreateDateBetweenNowAndYesterdayOrderByCommentDesc(String search){
        return this.discountRepository.FindAllBySearchAndCreateDateBetweenNowAndYesterdayOrderByCommentDesc(search);
    }

    public List<Discount> FindAllBySearchAndCreateDateBetweenNowAndLastWeekOrderByCommentDesc(String search){
        return this.discountRepository.FindAllBySearchAndCreateDateBetweenNowAndLastWeekOrderByCommentDesc(search);
    }

    public List<Discount> FindAllBySearchOrderByRatingDesc(String search){
        return this.discountRepository.FindAllBySearchOrderByRatingDesc(search);
    }

    public List<Discount> FindAllBySearchAndCreateDateBetweenNowAndYesterdayOrderByRatingDesc(String search){
        return this.discountRepository.FindAllBySearchAndCreateDateBetweenNowAndYesterdayOrderByRatingDesc(search);
    }

    public List<Discount> FindAllBySearchAndCreateDateBetweenNowAndLastWeekOrderByRatingDesc(String search){
        return this.discountRepository.FindAllBySearchAndCreateDateBetweenNowAndLastWeekOrderByRatingDesc(search);
    }

    public List<Discount> findAllByUserIdOrderByCreateDateDesc(Integer id){
        return this.discountRepository.findAllByUserIdOrderByCreateDateDesc(id);
    }

    public List<Discount> findAllByUserIdOrderByCommentDesc(Integer id){
        return this.discountRepository.findAllByUserIdOrderByCommentDesc(id);
    }

    public List<Discount> findAllByUserIdOrderByRatingDesc(Integer id){
        return this.discountRepository.findAllByUserIdOrderByRatingDesc(id);
    }

    public List<Discount> findAllByStatusEquals(Discount.Status status){
        return this.discountRepository.findAllByStatusEquals(status);
    }

    public List<Discount> findAllByUserAndLiked(User user){
        return this.discountRepository.findAllByUserAndLiked(user.getLogin());
    }

    public Optional<Discount> findFirstByTitleEquals(String title){
        return this.discountRepository.findFirstByTitleEquals(title);
    }

    public Optional<Discount> findById(Long id){
        Optional<Discount> discount = discountRepository.findById(id);
        if (discount.isPresent()) {
            Optional<User> user = authenticationService.getCurrentUser();
            if(discount.get().isDeleted()||discount.get().isAwaiting()){
                if(user.isPresent()){
                    if(user.get().getRole().equals("ADMIN") || user.get().hasDiscount(id)){
                        return discount;
                    }
                }
                return Optional.empty();
            }
            return discount;
        }
        return Optional.empty();
    }

    public boolean addDiscount(String url, String tag, String shop, String title, String old_price, String current_price,
                               String shipment_price, String content, String expire_date, String typeBase, String typeSuffix, MultipartFile file){

        if(content.isEmpty()||title.isEmpty()||url.isEmpty()||tag.isEmpty()||shop.isEmpty()||expire_date.isEmpty()||typeBase.isEmpty()||file.isEmpty()){
            return false;
        }
        if(typeBase.equals("OBNIZKA")){
            if(current_price.isEmpty()||old_price.isEmpty()||shipment_price.isEmpty()){
                return false;
            }
        }
        if(typeBase.equals("KOD")||typeBase.equals("KUPON")){
            if(old_price.isEmpty()||typeSuffix.isEmpty()){
                return false;
            }
        }


        File transferFile = new File("C:/zdjeciaprojekt/images/" + file.getOriginalFilename());
        try {
            file.transferTo(transferFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        Discount discount = new Discount();
        try {
            if (typeBase.equals("OBNIZKA")) {
                discount.setCurrentPrice(Double.parseDouble(current_price));
                discount.setOldPrice(Double.parseDouble(old_price));
                discount.setShipmentPrice(Double.parseDouble(shipment_price));
                discount.setType(Discount.Type.OBNIZKA);
            }

            if (typeBase.equals("KOD")) {
                discount.setOldPrice(Double.parseDouble(old_price));
                if (typeSuffix.equals("%")) {
                    discount.setType(Discount.Type.KODPROCENT);
                }
                if (typeSuffix.equals("PLN")) {
                    discount.setType(Discount.Type.KODNORMALNY);
                }
            }

            if (typeBase.equals("KUPON")) {
                discount.setOldPrice(Double.parseDouble(old_price));
                if (typeSuffix.equals("%")) {
                    discount.setType(Discount.Type.KUPONPROCENT);
                }
                if (typeSuffix.equals("PLN")) {
                    discount.setType(Discount.Type.KUPONNORMALNY);
                }
            }
            Optional<Tag> optionalTag = tagService.findById(Long.parseLong(tag));
            Optional<Shop> optionalShop = shopService.findById(Long.parseLong(shop));
            if(optionalShop.isPresent()&&optionalTag.isPresent()){
                discount.setTag(optionalTag.get());
                discount.setShop(optionalShop.get());
            }else {
                return false;
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
            return false;
        }
        discount.setContent(content);
        discount.setCreateDate(new Date());
        discount.setDiscountLink(url);
        discount.setTitle(title);
        try {
            discount.setExpireDate(new SimpleDateFormat("yyyy-MM-dd").parse(expire_date));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        discount.setStatus(Discount.Status.AWAITING);
        Optional<User> tempUser = authenticationService.getCurrentUser();
        if(!tempUser.isPresent()){
            return false;
        }
        discount.setUser(tempUser.get());
        discount.setImageUrl("images/" + file.getOriginalFilename());
        try {
            discountRepository.save(discount);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean editDiscount(String discountId, String url, String tag, String shop, String title, String old_price, String current_price,
                               String shipment_price, String content, String expire_date, String typeBase, String typeSuffix, MultipartFile file){

        if(content.isEmpty()||title.isEmpty()||url.isEmpty()||tag.isEmpty()||shop.isEmpty()||expire_date.isEmpty()||typeBase.isEmpty()){
            return false;
        }
        if(typeBase.equals("OBNIZKA")){
            if(current_price.isEmpty()||old_price.isEmpty()||shipment_price.isEmpty()){
                return false;
            }
        }
        if(typeBase.equals("KOD")||typeBase.equals("KUPON")){
            if(old_price.isEmpty()||typeSuffix.isEmpty()){
                return false;
            }
        }
        System.out.println(file.getName());
        if(!file.isEmpty()){
        File transferFile = new File("C:/zdjeciaprojekt/images/" + file.getOriginalFilename());
        try {
            file.transferTo(transferFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        }

        Discount discount = this.findById(Long.parseLong(discountId)).get();
        try {
            if (typeBase.equals("OBNIZKA")) {
                discount.setCurrentPrice(Double.parseDouble(current_price));
                discount.setOldPrice(Double.parseDouble(old_price));
                discount.setShipmentPrice(Double.parseDouble(shipment_price));
                discount.setType(Discount.Type.OBNIZKA);
            }

            if (typeBase.equals("KOD")) {
                discount.setOldPrice(Double.parseDouble(old_price));
                if (typeSuffix.equals("%")) {
                    discount.setType(Discount.Type.KODPROCENT);
                }
                if (typeSuffix.equals("PLN")) {
                    discount.setType(Discount.Type.KODNORMALNY);
                }
            }

            if (typeBase.equals("KUPON")) {
                discount.setOldPrice(Double.parseDouble(old_price));
                if (typeSuffix.equals("%")) {
                    discount.setType(Discount.Type.KUPONPROCENT);
                }
                if (typeSuffix.equals("PLN")) {
                    discount.setType(Discount.Type.KUPONNORMALNY);
                }
            }
            Optional<Tag> optionalTag = tagService.findById(Long.parseLong(tag));
            Optional<Shop> optionalShop = shopService.findById(Long.parseLong(shop));
            if(optionalShop.isPresent()&&optionalTag.isPresent()){
                discount.setTag(optionalTag.get());
                discount.setShop(optionalShop.get());
            }else {
                return false;
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
            return false;
        }
        discount.setContent(content);
        discount.setCreateDate(new Date());
        discount.setDiscountLink(url);
        discount.setTitle(title);
        try {
            discount.setExpireDate(new SimpleDateFormat("yyyy-MM-dd").parse(expire_date));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        discount.setStatus(Discount.Status.AWAITING);
        Optional<User> tempUser = authenticationService.getCurrentUser();
        if(!tempUser.isPresent()){
            return false;
        }
        discount.setUser(tempUser.get());
        if(!file.isEmpty()){
            discount.setImageUrl("images/" + file.getOriginalFilename());
        }
        try {
            discountRepository.save(discount);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void acceptDiscount(Long id){
        Discount disc = this.findById(id).get();
        disc.setStatus(Discount.Status.ACCEPTED);
        discountRepository.save(disc);
        emailService.sendEmail(disc.getUser().getEmail(), "NORGIE - Okazja zatwierdzona", "Twoja promocja została zweryfikowana i zatwierdzona," +
                " juz niedługo pojawi się na stronie głównej.\n" +
                "Tytuł okazji: " + disc.getTitle());
    }

    public void deleteDiscount(Long id, String reason){
        Discount disc = this.findById(id).get();
        disc.setStatus(Discount.Status.DELETED);
        DiscountBan ban = new DiscountBan();
        ban.setReason(reason);
        disc.setBan(ban);
        discountRepository.save(disc);
        emailService.sendEmail(disc.getUser().getEmail(), "NORGIE - Okazja odrzucona",  "Twoja promocja nie spełnia wymogów naszej strony i" +
                        " została zablokowana przez moderatora.\n"+
                "Tytuł okazji:  "+disc.getTitle() +"\n"+
                "Powód: " +disc.getBan().getReason()
                );
    }

}
