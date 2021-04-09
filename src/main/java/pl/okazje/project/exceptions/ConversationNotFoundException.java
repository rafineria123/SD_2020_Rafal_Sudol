package pl.okazje.project.exceptions;

public class ConversationNotFoundException extends ResourceNotFoundException {
    public ConversationNotFoundException(Long id) {
        super("Could not find conversation with id: "+id);
    }

}
