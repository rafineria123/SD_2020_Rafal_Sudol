package pl.okazje.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.okazje.project.entities.Discount;
import pl.okazje.project.entities.Shop;
import pl.okazje.project.entities.Tag;
import pl.okazje.project.entities.User;
import pl.okazje.project.repositories.DiscountRepository;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
                                discounts = this.findAllByTagAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc((String)byArgument.get("tag"));
                                break;
                            }
                            if(byArgument.containsKey("shop")){
                                discounts = this.findAllByShopAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc((String)byArgument.get("shop"));
                                break;
                            }
                            if(byArgument.containsKey("search")){
                                discounts = this.FindAllBySearchAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc("%"+byArgument.get("search")+"%");
                                break;
                            }
                            discounts = this.findAllByCreationdateBetweenNowAndYesterdayOrderByRatingDesc();
                            break;
                        case "week":
                            if(byArgument.containsKey("tag")){
                                discounts = this.findAllByTagAndCreationdateBetweenNowAndLastWeekOrderByRatingDesc((String)byArgument.get("tag"));
                                break;
                            }
                            if(byArgument.containsKey("shop")){
                                discounts = this.findAllByShopAndCreationdateBetweenNowAndLastWeekOrderByRatingDesc((String)byArgument.get("shop"));
                                break;
                            }
                            if(byArgument.containsKey("search")){
                                discounts = this.FindAllBySearchAndCreationdateBetweenNowAndLastWeekOrderByRatingDesc("%"+byArgument.get("search")+"%");
                                break;
                            }
                            discounts = this.findAllByCreationdateBetweenNowAndLastWeekOrderByRatingDesc();
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
                                discounts = this.findAllByTagAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc((String)byArgument.get("tag"));
                                break;
                            }
                            if(byArgument.containsKey("shop")){
                                discounts = this.findAllByShopAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc((String)byArgument.get("shop"));
                                break;
                            }
                            if(byArgument.containsKey("search")){
                                discounts = this.FindAllBySearchAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc("%"+byArgument.get("search")+"%");
                                break;
                            }
                            discounts = this.findAllByCreationdateBetweenNowAndYesterdayOrderByCommentDesc();
                            break;
                        case "week":
                            if(byArgument.containsKey("tag")){
                                discounts = this.findAllByTagAndCreationdateBetweenNowAndLastWeekOrderByCommentDesc((String)byArgument.get("tag"));
                                break;
                            }
                            if(byArgument.containsKey("shop")){
                                discounts = this.findAllByShopAndCreationdateBetweenNowAndLastWeekOrderByCommentDesc((String)byArgument.get("shop"));
                                break;
                            }
                            if(byArgument.containsKey("search")){
                                discounts = this.FindAllBySearchAndCreationdateBetweenNowAndLastWeekOrderByCommentDesc("%"+byArgument.get("search")+"%");
                                break;
                            }
                            discounts = this.findAllByCreationdateBetweenNowAndLastWeekOrderByCommentDesc();
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
                discounts = this.findAllByTagOrderByCreationdateDesc((String)byArgument.get("tag"));
            }else if(byArgument.containsKey("shop")){
                discounts = this.findAllByShopOrderByCreationdateDesc((String)byArgument.get("shop"));
            }else if(byArgument.containsKey("search")){
                discounts = this.FindAllBySearchOrderByCreationdateDesc("%"+byArgument.get("search")+"%");
            }else  {
                discounts = this.findAllByOrderByCreationdateDesc();
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
                    discounts = this.findAllByUseridOrderByRatingDesc(user.getUser_id());
                    break;
                case "comments":
                    discounts = this.findAllByUseridOrderByCommentDesc(user.getUser_id());
                    break;
            }
        }else {
            discounts = this.findAllByUseridOrderByCreationdateDesc(user.getUser_id());
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

    public List<Discount> findAllByOrderByCreationdateDesc(){
        return discountRepository.findAllByOrderByCreationdateDesc();
    }
    public List<Discount> findAllByOrderByRatingDesc(){
        return this.discountRepository.findAllByOrderByRatingDesc();
    }

    public List<Discount> findAllByCreationdateBetweenNowAndYesterdayOrderByRatingDesc(){
        return this.discountRepository.findAllByCreationdateBetweenNowAndYesterdayOrderByRatingDesc();
    }


    public List<Discount> findAllByCreationdateBetweenNowAndLastWeekOrderByRatingDesc(){
        return this.discountRepository.findAllByCreationdateBetweenNowAndLastWeekOrderByRatingDesc();
    }

    public List<Discount> findAllByOrderByCommentDesc(){
        return this.discountRepository.findAllByOrderByCommentDesc();
    }

    public List<Discount> findAllByCreationdateBetweenNowAndYesterdayOrderByCommentDesc(){
        return this.discountRepository.findAllByCreationdateBetweenNowAndYesterdayOrderByCommentDesc();
    }

    public List<Discount> findAllByCreationdateBetweenNowAndLastWeekOrderByCommentDesc(){
        return this.discountRepository.findAllByCreationdateBetweenNowAndLastWeekOrderByCommentDesc();
    }

    public List<Discount> findAllByTagOrderByCreationdateDesc(String tag){
        return this.discountRepository.findAllByTagOrderByCreationdateDesc(tag);
    }

    public List<Discount> findAllByShopOrderByCreationdateDesc(String shop){
        return this.discountRepository.findAllByShopOrderByCreationdateDesc(shop);
    }

    public List<Discount> findAllByTagOrderByRatingDesc(String tag){
        return this.discountRepository.findAllByTagOrderByRatingDesc(tag);
    }


    public List<Discount> findAllByTagAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc(String tag){
        return this.discountRepository.findAllByTagAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc(tag);
    }

    public List<Discount> findAllByTagAndCreationdateBetweenNowAndLastWeekOrderByRatingDesc(String tag){
        return this.discountRepository.findAllByTagAndCreationdateBetweenNowAndLastWeekOrderByRatingDesc(tag);
    }

    public List<Discount> findAllByTagOrderByCommentDesc(String tag){
        return this.discountRepository.findAllByTagOrderByCommentDesc(tag);
    }

    public List<Discount> findAllByTagAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc(String tag){
        return this.discountRepository.findAllByTagAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc(tag);
    }

    public List<Discount> findAllByTagAndCreationdateBetweenNowAndLastWeekOrderByCommentDesc(String tag){
        return this.discountRepository.findAllByTagAndCreationdateBetweenNowAndLastWeekOrderByCommentDesc(tag);
    }

    public List<Discount> findAllByShopOrderByRatingDesc(String shop){
        return this.discountRepository.findAllByShopOrderByRatingDesc(shop);
    }

    public List<Discount> findAllByShopAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc(String shop){
        return this.discountRepository.findAllByShopAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc(shop);
    }

    public List<Discount> findAllByShopAndCreationdateBetweenNowAndLastWeekOrderByRatingDesc(String shop){
        return this.discountRepository.findAllByShopAndCreationdateBetweenNowAndLastWeekOrderByRatingDesc(shop);
    }

    public List<Discount> findAllByShopOrderByCommentDesc(String shop){
        return this.discountRepository.findAllByShopOrderByCommentDesc(shop);
    }

    public List<Discount> findAllByShopAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc(String shop){
        return this.discountRepository.findAllByShopAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc(shop);
    }

    public List<Discount> findAllByShopAndCreationdateBetweenNowAndLastWeekOrderByCommentDesc(String shop){
        return this.discountRepository.findAllByShopAndCreationdateBetweenNowAndLastWeekOrderByCommentDesc(shop);
    }

    public List<Discount> FindAllBySearchOrderByCreationdateDesc(String search){
        return this.discountRepository.FindAllBySearchOrderByCreationdateDesc(search);
    }

    public List<Discount> FindAllBySearchOrderByCommentDesc(String search){
        return this.discountRepository.FindAllBySearchOrderByCommentDesc(search);
    }

    public List<Discount> FindAllBySearchAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc(String search){
        return this.discountRepository.FindAllBySearchAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc(search);
    }

    public List<Discount> FindAllBySearchAndCreationdateBetweenNowAndLastWeekOrderByCommentDesc(String search){
        return this.discountRepository.FindAllBySearchAndCreationdateBetweenNowAndLastWeekOrderByCommentDesc(search);
    }

    public List<Discount> FindAllBySearchOrderByRatingDesc(String search){
        return this.discountRepository.FindAllBySearchOrderByRatingDesc(search);
    }

    public List<Discount> FindAllBySearchAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc(String search){
        return this.discountRepository.FindAllBySearchAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc(search);
    }

    public List<Discount> FindAllBySearchAndCreationdateBetweenNowAndLastWeekOrderByRatingDesc(String search){
        return this.discountRepository.FindAllBySearchAndCreationdateBetweenNowAndLastWeekOrderByRatingDesc(search);
    }

    public List<Discount> findAllByUseridOrderByCreationdateDesc(Integer id){
        return this.discountRepository.findAllByUseridOrderByCreationdateDesc(id);
    }

    public List<Discount> findAllByUseridOrderByCommentDesc(Integer id){
        return this.discountRepository.findAllByUseridOrderByCommentDesc(id);
    }

    public List<Discount> findAllByUseridOrderByRatingDesc(Integer id){
        return this.discountRepository.findAllByUseridOrderByRatingDesc(id);
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
                    if(user.get().getROLE().equals("ADMIN") || user.get().hasDiscount(id)){
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

        Path currentPath = Paths.get("");
        Path absolutePath = currentPath.toAbsolutePath();
        File transferFile = new File(absolutePath+"/src/main/resources/static/images/" + file.getOriginalFilename());
        try {
            file.transferTo(transferFile);
        } catch (IOException e) {
            return false;
        }

        Discount discount = new Discount();
        try {
            if (typeBase.equals("OBNIZKA")) {
                discount.setCurrent_price(Double.parseDouble(current_price));
                discount.setOld_price(Double.parseDouble(old_price));
                discount.setShipment_price(Double.parseDouble(shipment_price));
                discount.setType(Discount.Type.OBNIZKA);
            }

            if (typeBase.equals("KOD")) {
                discount.setOld_price(Double.parseDouble(old_price));
                if (typeSuffix.equals("%")) {
                    discount.setType(Discount.Type.KODPROCENT);
                }
                if (typeSuffix.equals("PLN")) {
                    discount.setType(Discount.Type.KODNORMALNY);
                }
            }

            if (typeBase.equals("KUPON")) {
                discount.setOld_price(Double.parseDouble(old_price));
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
            return false;
        }
        discount.setContent(content);
        discount.setCreationdate(new Date());
        discount.setDiscount_link(url);
        discount.setTitle(title);
        try {
            discount.setExpire_date(new SimpleDateFormat("yyyy-MM-dd").parse(expire_date));
        } catch (ParseException e) {
            return false;
        }
        discount.setStatus(Discount.Status.AWAITING);
        Optional<User> tempUser = authenticationService.getCurrentUser();
        if(!tempUser.isPresent()){
            return false;
        }
        discount.setUser(tempUser.get());
        discount.setImage_url("images/" + file.getOriginalFilename());
        discountRepository.save(discount);
        return true;
    }

    public void acceptDiscount(Long id){
        Discount disc = this.findById(id).get();
        disc.setStatus(Discount.Status.ACCEPTED);
        discountRepository.save(disc);
        emailService.sendEmail(disc.getUser().getEmail(), "NORGIE - Okazja zatwierdzona", "Twoja okazja została zweryfikowana i zatwierdzona," +
                " juz niedługo pojawi się na stronie głównej.\n" +
                "Tytuł okazji: " + disc.getTitle());
    }

    public void deleteDiscount(Long id){
        Discount disc = this.findById(id).get();
        disc.setStatus(Discount.Status.DELETED);
        discountRepository.save(disc);
    }

}
