package pl.okazje.project.entities.ratings;

import pl.okazje.project.entities.comments.Comment;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class CommentRating extends Rating{

    @ManyToOne
    @JoinColumn(name="commentId", nullable=true)
    private Comment comment;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
