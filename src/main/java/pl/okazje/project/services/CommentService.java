package pl.okazje.project.services;

import org.springframework.stereotype.Service;
import pl.okazje.project.entities.Comment;
import pl.okazje.project.exceptions.CommentNotFoundException;
import pl.okazje.project.repositories.CommentRepository;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void save(Comment comment){
        commentRepository.save(comment);
    }

    public Comment findById(Long id){

        if(commentRepository.findById(id).isPresent()){
            return commentRepository.findById(id).get();
        }
        throw new CommentNotFoundException(id);

    }
}
