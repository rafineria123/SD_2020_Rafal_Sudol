package pl.okazje.project.services;

import org.springframework.session.Session;
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
                    if(user.get().getRole().equals("ADMIN") || user.get().hasPost(id)){
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
        return postRepository.findFirstByTitleOrderByCreateDateDesc(title);
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
        post.setCreateDate(new Date());
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
                posts = this.findAllBySearchOrderByCreateDateDesc("%"+byArgument.get("search")+"%");
            }else if(byArgument.containsKey("tag")){
                posts = this.findAllByTagOrderByCreateDateDesc((String)byArgument.get("tag"));
            }else if(byArgument.containsKey("shop")){
                posts = this.findAllByShopOrderByCreateDateDesc((String)byArgument.get("shop"));
            }else {
                posts = this.findAllByOrderByCreateDateDesc();
            }
        }
        return posts;
    }

    public List<Post> findAllByUserIncludeSorting(User user){
        List<Post> posts;
        Session session = sessionService.findActiveSessionForUser(user).get();
        if(session.getAttribute("forumSort")!=null&&session.getAttribute("forumSort").equals("comments")){
            posts = this.findAllByUserOrderByCommentDesc(user.getLogin());
        }else {
            posts = this.findAllByUserOrderByCreateDateDesc(user.getLogin());
        }
        return posts;
    }

    public List<Post> findAllByOrderByCreateDateDesc(){
        return postRepository.findAllByOrderByCreateDateDesc();
    }

    public List<Post> findAllByTagOrderByCreateDateDesc(String tag){
        return postRepository.findAllByTagOrderByCreateDateDesc(tag);
    }

    public List<Post> findAllByShopOrderByCreateDateDesc(String shop){
        return postRepository.findAllByShopOrderByCreateDateDesc(shop);
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

    public List<Post> findAllBySearchOrderByCreateDateDesc(String search){
        return postRepository.findAllBySearchOrderByCreateDateDesc(search);
    }

    public List<Post> findAllBySearchOrderByCommentDesc(String search){
        return postRepository.findAllBySearchOrderByCommentDesc(search);
    }

    public List<Post> findAllByUserOrderByCreateDateDesc(String user){
        return postRepository.findAllByUserOrderByCreateDateDesc(user);
    }

    public List<Post> findAllByUserOrderByCommentDesc(String user){
        return postRepository.findAllByUserOrderByCommentDesc(user);
    }

    public Post findFirstByTitleOrderByCreateDateDesc(String title){
        return postRepository.findFirstByTitleOrderByCreateDateDesc(title);
    }
}
