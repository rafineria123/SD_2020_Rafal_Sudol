package pl.okazje.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import pl.okazje.project.entities.Discount;
import pl.okazje.project.repositories.DiscountRepository;

import java.util.*;

@Service
public class DiscountService {


    private DiscountRepository discountRepository;

    @Autowired
    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }


    List<Discount> findAllByOrderByCreationdateDesc(){
        return this.discountRepository.findAllByOrderByCreationdateDesc();
    }

    List<Discount> findAllByOrderByRatingDesc(){
        return this.discountRepository.findAllByOrderByRatingDesc();
    }

    List<Discount> findAllByCreationdateBetweenNowAndYesterdayOrderByRatingDesc(){
        return this.discountRepository.findAllByCreationdateBetweenNowAndYesterdayOrderByRatingDesc();
    }


    List<Discount> findAllByCreationdateBetweenNowAndLastWeekOrderByRatingDesc(){
        return this.discountRepository.findAllByCreationdateBetweenNowAndLastWeekOrderByRatingDesc();
    }

    List<Discount> findAllByOrderByCommentDesc(){
        return this.discountRepository.findAllByOrderByCommentDesc();
    }

    List<Discount> findAllByCreationdateBetweenNowAndYesterdayOrderByCommentDesc(){
        return this.discountRepository.findAllByCreationdateBetweenNowAndYesterdayOrderByCommentDesc();
    }

    List<Discount> findAllByCreationdateBetweenNowAndLastWeekOrderByCommentDesc(){
        return this.discountRepository.findAllByCreationdateBetweenNowAndLastWeekOrderByCommentDesc();
    }

    List<Discount> findAllByTagOrderByCreationdateDesc(String tag){
        return this.discountRepository.findAllByTagOrderByCreationdateDesc(tag);
    }

    List<Discount> findAllByShopOrderByCreationdateDesc(String shop){
        return this.discountRepository.findAllByShopOrderByCreationdateDesc(shop);
    }

    List<Discount> findAllByTagOrderByRatingDesc(String tag){
        return this.discountRepository.findAllByTagOrderByRatingDesc(tag);
    }


    List<Discount> findAllByTagAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc(String tag){
        return this.discountRepository.findAllByTagAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc(tag);
    }

    List<Discount> findAllByTagAndCreationdateBetweenNowAndLastWeekOrderByRatingDesc(String tag){
        return this.discountRepository.findAllByTagAndCreationdateBetweenNowAndLastWeekOrderByRatingDesc(tag);
    }

    List<Discount> findAllByTagOrderByCommentDesc(String tag){
        return this.discountRepository.findAllByTagOrderByCommentDesc(tag);
    }

    List<Discount> findAllByTagAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc(String tag){
        return this.discountRepository.findAllByTagAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc(tag);
    }

    List<Discount> findAllByTagAndCreationdateBetweenNowAndLastWeekOrderByCommentDesc(String tag){
        return this.discountRepository.findAllByTagAndCreationdateBetweenNowAndLastWeekOrderByCommentDesc(tag);
    }

    List<Discount> findAllByShopOrderByRatingDesc(String shop){
        return this.discountRepository.findAllByShopOrderByRatingDesc(shop);
    }

    List<Discount> findAllByShopAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc(String shop){
        return this.discountRepository.findAllByShopAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc(shop);
    }

    List<Discount> findAllByShopAndCreationdateBetweenNowAndLastWeekOrderByRatingDesc(String shop){
        return this.discountRepository.findAllByShopAndCreationdateBetweenNowAndLastWeekOrderByRatingDesc(shop);
    }

    List<Discount> findAllByShopOrderByCommentDesc(String shop){
        return this.discountRepository.findAllByShopOrderByCommentDesc(shop);
    }

    List<Discount> findAllByShopAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc(String shop){
        return this.discountRepository.findAllByShopAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc(shop);
    }

    List<Discount> findAllByShopAndCreationdateBetweenNowAndLastWeekOrderByCommentDesc(String shop){
        return this.discountRepository.findAllByShopAndCreationdateBetweenNowAndLastWeekOrderByCommentDesc(shop);
    }

    List<Discount> FindAllBySearchOrderByCreationdateDesc(String search){
        return this.discountRepository.FindAllBySearchOrderByCreationdateDesc(search);
    }

    List<Discount> FindAllBySearchOrderByCommentDesc(String search){
        return this.discountRepository.FindAllBySearchOrderByCommentDesc(search);
    }

    List<Discount> FindAllBySearchAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc(String search){
        return this.discountRepository.FindAllBySearchAndCreationdateBetweenNowAndYesterdayOrderByCommentDesc(search);
    }

    List<Discount> FindAllBySearchAndCreationdateBetweenNowAndLastWeekOrderByCommentDesc(String search){
        return this.discountRepository.FindAllBySearchAndCreationdateBetweenNowAndLastWeekOrderByCommentDesc(search);
    }

    List<Discount> FindAllBySearchOrderByRatingDesc(String search){
        return this.discountRepository.FindAllBySearchOrderByRatingDesc(search);
    }

    List<Discount> FindAllBySearchAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc(String search){
        return this.discountRepository.FindAllBySearchAndCreationdateBetweenNowAndYesterdayOrderByRatingDesc(search);
    }

    List<Discount> FindAllBySearchAndCreationdateBetweenNowAndLastWeekOrderByRatingDesc(String search){
        return this.discountRepository.FindAllBySearchAndCreationdateBetweenNowAndLastWeekOrderByRatingDesc(search);
    }

    List<Discount> findAllByUseridOrderByCreationdateDesc(Integer id){
        return this.discountRepository.findAllByUseridOrderByCreationdateDesc(id);
    }

    List<Discount> findAllByUseridOrderByCommentDesc(Integer id){
        return this.discountRepository.findAllByUseridOrderByCommentDesc(id);
    }

    List<Discount> findAllByUseridOrderByRatingDesc(Integer id){
        return this.discountRepository.findAllByUseridOrderByRatingDesc(id);
    }

    List<Discount> findAllByStatusEquals(Discount.Status status){
        return this.discountRepository.findAllByStatusEquals(status);
    }

    List<Discount> findAllByUserAndLiked(String user){
        return this.discountRepository.findAllByUserAndLiked(user);
    }

    Optional<Discount> findFirstByTitleEquals(String title){
        return this.discountRepository.findFirstByTitleEquals(title);
    }

}
