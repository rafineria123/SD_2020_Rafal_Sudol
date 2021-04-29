package pl.okazje.project.exceptions;

public class ConversationNotFoundException extends ResourceNotFoundException {
    public ConversationNotFoundException(Long id) {
        super("Could not find conversation with id: "+id);
    }
    public ConversationNotFoundException(int id1, int id2) {
        super("Could not find conversation for users with id: "+id1+" and "+id2);
    }

}
