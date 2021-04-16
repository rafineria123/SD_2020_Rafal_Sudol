package pl.okazje.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.okazje.project.entities.*;
import pl.okazje.project.repositories.PostRepository;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final AuthenticationService authenticationService;
    private final TagService tagService;
    private final ShopService shopService;
    private final SessionService sessionService;

    @Autowired
    public PostService(PostRepository postRepository, AuthenticationService authenticationService, TagService tagService, ShopService shopService, SessionService sessionService) {
        this.postRepository = postRepository;
        this.authenticationService = authenticationService;
        this.tagService = tagService;
        this.shopService = shopService;
        this.sessionService = sessionService;
    }

    public void save(Post post){
        postRepository.save(post);
    }

    public void deletePost(Long id){
        Post post = findById(id).get();
        post.setStatus(Post.Status.DELETED);
        this.save(post);
    }

    public Optional<Post> findById(Long id){
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            Optional<User> user = authenticationService.getCurrentUser();
            if(post.get().isDeleted()||post.get().isAwaiting()){
                if(user.isPresent()){
                    if(user.get().getROLE().equals("ADMIN") || user.get().hasPost(id)){
                        return post;
                    }
                }
                return Optional.empty();
            }
            return post;
        }
        return Optional.empty();
    }

    public Post findByTitle(String title){
        return postRepository.findFirstByTitleOrderByCreationdateDesc(title);
    }

    public boolean addPost(String content, String title, String tag, String shop){
        if(content.isEmpty()||title.isEmpty()||tag.isEmpty()||shop.isEmpty()){
            return false;
        }
        Post post = new Post();
        try {

            Optional<Tag> optionalTag = tagService.findById(Long.parseLong(tag));
            Optional<Shop> optionalShop = shopService.findById(Long.parseLong(shop));
            if(optionalShop.isPresent()&&optionalTag.isPresent()){
                post.setTag(optionalTag.get());
                post.setShop(optionalShop.get());
            }else {
                return false;
            }
        }catch (NumberFormatException e){
            return false;
        }
        post.setContent(content);
        post.setCreationdate(new Date());
        post.setTitle(title);
        post.setStatus(Post.Status.ACCEPTED);
        Optional<User> tempUser = authenticationService.getCurrentUser();
        if(!tempUser.isPresent()){
            return false;
        }
        post.setUser(tempUser.get());
        this.save(post);
        return true;
    }

    public List<Post> findAllIncludeSorting(Map byArgument){
        List<Post> posts;
        HttpSession session = sessionService.getCurrentSession();
        if(session.getAttribute("forumSort")!=null&&session.getAttribute("forumSort").equals("comments")){
            if(byArgument.containsKey("tag")){
                posts = this.findAllByTagOrderByCommentDesc((String)byArgument.get("tag"));
            }else if(byArgument.containsKey("shop")){
                posts = this.findAllByShopOrderByCommentDesc((String)byArgument.get("shop"));
            }else if(byArgument.containsKey("search")){
                posts = this.findAllBySearchOrderByCommentDesc("%"+byArgument.get("search")+"%");
            }else {
                posts = this.findAllByOrderByCommentDesc();
               }
        }else {
            if(byArgument.containsKey("search")){
                posts = this.findAllBySearchOrderByCreationdateDesc("%"+byArgument.get("search")+"%");
            }else if(byArgument.containsKey("tag")){
                posts = this.findAllByTagOrderByCreationdateDesc((String)byArgument.get("tag"));
            }else if(byArgument.containsKey("shop")){
                posts = this.findAllByShopOrderByCreationdateDesc((String)byArgument.get("shop"));
            }else {
                posts = this.findAllByOrderByCreationdateDesc();
            }
        }
        return posts;
    }

    public List<Post> findAllByOrderByCreationdateDesc(){
        return postRepository.findAllByOrderByCreationdateDesc();
    }


    public List<Post> findAllByTagOrderByCreationdateDesc(String tag){
        return postRepository.findAllByTagOrderByCreationdateDesc(tag);
    }


    public List<Post> findAllByShopOrderByCreationdateDesc(String shop){
        return postRepository.findAllByShopOrderByCreationdateDesc(shop);
    }


    public List<Post> findAllByOrderByCommentDesc(){
        return postRepository.findAllByOrderByCommentDesc();
    }


    public List<Post> findAllByTagOrderByCommentDesc(String tag){
        return postRepository.findAllByTagOrderByCommentDesc(tag);
    }


    public List<Post> findAllByShopOrderByCommentDesc(String shop){
        return postRepository.findAllByShopOrderByCommentDesc(shop);
    }


    public List<Post> findAllBySearchOrderByCreationdateDesc(String search){
        return postRepository.findAllBySearchOrderByCreationdateDesc(search);
    }


    public List<Post> findAllBySearchOrderByCommentDesc(String search){
        return postRepository.findAllBySearchOrderByCommentDesc(search);
    }


    public List<Post> FindAllByUserOrderByCreationdateDesc(String user){
        return postRepository.FindAllByUserOrderByCreationdateDesc(user);
    }

    public Post findFirstByTitleOrderByCreationdateDesc(String title){
        return postRepository.findFirstByTitleOrderByCreationdateDesc(title);
    }
}
