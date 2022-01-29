package pl.okazje.project.entities.comments;

import pl.okazje.project.entities.Discount;
import pl.okazje.project.entities.Post;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PostComment extends Comment {


    @ManyToOne
    @JoinColumn(name="postId")
    private Post post;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}