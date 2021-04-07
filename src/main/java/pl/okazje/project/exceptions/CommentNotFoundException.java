package pl.okazje.project.exceptions;

public class CommentNotFoundException extends ResourceNotFoundException {
    public CommentNotFoundException(Long id) {
        super("Could not find comment with id: "+id);
    }

}
