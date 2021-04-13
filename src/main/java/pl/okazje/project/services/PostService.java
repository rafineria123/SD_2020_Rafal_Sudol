package pl.okazje.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;
import pl.okazje.project.entities.*;
import pl.okazje.project.repositories.PostRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final AuthenticationService authenticationService;
    private final TagService tagService;
    private final ShopService shopService;

    @Autowired
    public PostService(PostRepository postRepository, AuthenticationService authenticationService, TagService tagService, ShopService shopService) {
        this.postRepository = postRepository;
        this.authenticationService = authenticationService;
        this.tagService = tagService;
        this.shopService = shopService;
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
}
