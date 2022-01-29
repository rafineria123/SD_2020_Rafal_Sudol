package pl.okazje.project.services;

import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;
import pl.okazje.project.entities.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Optional;

@Service
public class SessionService {

    private final FindByIndexNameSessionRepository sessionRepository;
    private final HttpServletRequest request;

    public SessionService(FindByIndexNameSessionRepository sessionRepository, HttpServletRequest request) {
        this.sessionRepository = sessionRepository;
        this.request = request;
    }

    public Optional<Session> findActiveSessionForUser(User user){
        Collection<? extends Session> usersSessions = this.sessionRepository.findByPrincipalName(user.getUsername()).values();
        for (Session s:usersSessions) {
            if(!s.isExpired()){
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }

    public void delete(Session s){
        sessionRepository.deleteById(s.getId());
    }

    public HttpSession getCurrentSession(){
        return request.getSession();
    }

    public void setCurrentSessionSortAndDateAttributes(String sort, String date){
        HttpSession session = this.getCurrentSession();
        if(sort != null && !sort.isEmpty()) {
            session.setAttribute("sort", sort);
        }
        if(date!=null && !date.isEmpty()){
            session.setAttribute("date", date);
        }else {
            if(session.getAttribute("date")==null||session.getAttribute("date").toString().isEmpty()){
                session.setAttribute("date", "all");
            }
        }
    }

    public void setCurrentSessionForumSortAttribute(String sort){
        HttpSession session = this.getCurrentSession();
        if(sort != null && !sort.isEmpty()) {
            session.setAttribute("forumSort", sort);
        }
    }

    public void toggleCurrentSessionFilter(){
        HttpSession session = this.getCurrentSession();
        if(session.getAttribute("filter")==null){
            session.setAttribute("filter", "true");
        }else{
            if(session.getAttribute("filter").equals("true")){
                session.setAttribute("filter", "false");
            }else{
                session.setAttribute("filter","true");
            }
        }
    }
}
