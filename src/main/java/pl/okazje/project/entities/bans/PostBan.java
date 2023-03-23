package pl.okazje.project.entities.bans;

import pl.okazje.project.entities.Post;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity(name = "postban")
public class PostBan extends Ban {

    @OneToOne(mappedBy = "ban")
    private Post post;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
