package pl.okazje.project.controllers;

import org.springframework.beans.support.PagedListHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.okazje.project.utills.DiscountFinder;
import pl.okazje.project.entities.*;
import pl.okazje.project.services.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
@RequestMapping("/settings")
public class SettingsController {

    private final int ITEMS_PER_PAGE = 4;

    private final ShopService shopService;
    private final TagService tagService;
    private final AuthenticationService authenticationService;
    private final DiscountService discountService;
    private final PostService postService;
    private final UserService userService;
    private final ConversationService conversationService;
    private final PasswordEncoder passwordEncoder;
    private final DiscountFinder discountFinder;

    public SettingsController(ShopService shopService, TagService tagService, AuthenticationService authenticationService, DiscountService discountService, PostService postService, UserService userService, ConversationService conversationService, PasswordEncoder passwordEncoder, DiscountFinder discountFinder) {
        this.shopService = shopService;
        this.tagService = tagService;
        this.authenticationService = authenticationService;
        this.discountService = discountService;
        this.postService = postService;
        this.userService = userService;
        this.conversationService = conversationService;
        this.passwordEncoder = passwordEncoder;
        this.discountFinder = discountFinder;
    }


    @GetMapping("")
    public ModelAndView getSettingsHomepage() {
        return getBaseModelAndView("user_profile_main");
    }

    @GetMapping("/discounts")
    public ModelAndView getSettingsDiscountsHomepage() {
        return getDiscountBaseModelAndView("user_profile_discounts", "/settings/discounts",
                new PagedListHolder(discountService.findAllByUserIncludeSorting(authenticationService.getCurrentUser().get())),
                0);
    }

    @GetMapping("/discounts/page/{id}")
    public ModelAndView getSettingsDiscountsPage(@PathVariable("id") String id) {
        return getDiscountBaseModelAndView("user_profile_discounts", "/settings/discounts",
                new PagedListHolder(discountService.findAllByUserIncludeSorting(authenticationService.getCurrentUser().get())),
                Integer.parseInt(id) - 1);
    }

    @GetMapping("/posts")
    public ModelAndView getSettingsPostsHomepage() {
        return getPostBaseModelAndView("user_profile_posts", 0);
    }

    @GetMapping("/posts/page/{id}")
    public ModelAndView getSettingsPostsPage(@PathVariable("id") String id) {
        return getPostBaseModelAndView("user_profile_posts", Integer.parseInt(id));
    }

    @GetMapping("/admin/discounts")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ModelAndView getSettingsAdminDiscounts() {
        return getDiscountBaseModelAndView("user_admin_profile_discounts", "/settings/admin/discounts",
                new PagedListHolder(discountService.findAllByStatusEquals(Discount.Status.AWAITING)), 0);
    }

    @GetMapping("/admin/discounts/page/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ModelAndView pageSettingsAdminDiscounts(@PathVariable("id") String id) {
        return getDiscountBaseModelAndView("user_admin_profile_discounts", "/settings/admin/discounts",
                new PagedListHolder(discountService.findAllByStatusEquals(Discount.Status.AWAITING)), Integer.parseInt(id) - 1);

    }

    @GetMapping("/messages")
    public ModelAndView profileMessages() {
        ModelAndView modelAndView = getBaseModelAndView("user_profile_messages");
        modelAndView.addObject("list_of_conversations", authenticationService.getCurrentUser().get().getConversationsSorted());
        return modelAndView;
    }

    @RequestMapping(value = {"/liked/page/{id}", "/liked"}, method = RequestMethod.GET)
    public ModelAndView liked(@PathVariable(required = false) String id) {
        return getDiscountBaseModelAndView("user_profile_liked", "/settings/liked",
                new PagedListHolder(discountService.findAllByUserAndLiked(authenticationService.getCurrentUser().get())),
                id == null || id.isEmpty() ? 0 : Integer.parseInt(id) - 1);

    }

    @GetMapping("/admin/functions")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ModelAndView functions() {
        return getBaseModelAndView("user_admin_profile_functions");
    }

    @PostMapping("/messages")
    public RedirectView sendMessageToUser(@ModelAttribute("login") String login, @ModelAttribute("message") String message, RedirectAttributes redir) {
        RedirectView redirectView = new RedirectView("/settings/messages", true);
        User currentUser = authenticationService.getCurrentUser().get();
        if (login.equals(currentUser.getLogin())) {
            redir.addFlashAttribute("bad_status", "Nie możesz pisac wiadomości do siebie.");
            return redirectView;
        }
        Optional<User> optionalOtherUser = userService.findFirstByLogin(login);
        if (!optionalOtherUser.isPresent()) {
            redir.addFlashAttribute("bad_status", "Użytkownik o takim loginie nie istnieje.");
            return redirectView;
        }
        conversationService.sendMessage(optionalOtherUser.get(), message);
        return new RedirectView("/messages/" + conversationService.findByUsers(currentUser.getUserId(),
                optionalOtherUser.get().getUserId()).get().getConversationId(), true);
    }

    @PostMapping("/changeUserDetails")
    public RedirectView changeUserDetails(@ModelAttribute("name") String name, @ModelAttribute("surname") String surname, @ModelAttribute("email") String email, RedirectAttributes redir) {
        userService.changeUserDetails(authenticationService.getCurrentUser().get(), name, surname, email);
        redir.addFlashAttribute("status", "Zmiany pomyślnie zapisane.");
        return new RedirectView("/settings", true);
    }

    @PostMapping("/changeDescription")
    public RedirectView changeDescription(@ModelAttribute("description") String description, RedirectAttributes redir) {
        userService.changeUserDescription(authenticationService.getCurrentUser().get(), description);
        redir.addFlashAttribute("status", "Zmiany pomyślnie zapisane.");
        return new RedirectView("/settings", true);
    }

    @PostMapping("/changePassword")
    public RedirectView changePassword(@ModelAttribute("currentpassword") String currentpassword, @ModelAttribute("newpassword") String newpassword,
                                       @ModelAttribute("newpasswordconfirmation") String newpasswordconfirmation, RedirectAttributes redir) {
        RedirectView redirectView = new RedirectView("/settings", true);
        if (!newpassword.equals(newpasswordconfirmation)) {
            redir.addFlashAttribute("bad_status", "Hasła muszą się powtarzać w dwóch ostatnich polach.");
            return redirectView;
        }
        User user = authenticationService.getCurrentUser().get();
        if (!passwordEncoder.matches(currentpassword, user.getPassword())) {
            redir.addFlashAttribute("bad_status", "Twoje obecne hasło się nie zgadza.");
            return redirectView;
        }
        userService.changePassword(user, newpassword);
        redir.addFlashAttribute("status", "Twoje hasło zostało zmienione.");
        return redirectView;
    }


    @PostMapping("/admin/xkom")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public @ResponseBody
    void xkom() {
        discountFinder.fetchXkom();
    }

    @PostMapping("/admin/amazon")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public @ResponseBody
    void amazon() {
        discountFinder.fetchAmazon("https://www.amazon.com/Best-Sellers-Womens-Fashion/zgbs/fashion/", "Moda");
    }

    @GetMapping("/admin/status")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public @ResponseBody String[] statusXkom(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String array[] = new String[5];
        if (discountFinder.status == DiscountFinder.Status.WORKING){

            array[0] = "working";
            return array;

        }
        if(discountFinder.status == DiscountFinder.Status.SUCCESS){

            array[0] = "success";
            return array;

        }
        if(discountFinder.status == DiscountFinder.Status.ERROR){
            array[0] = "error";
            return array;
        }


        return array;

    }

    private ModelAndView getBaseModelAndView(String viewName) {
        ModelAndView modelAndView = new ModelAndView(viewName);
        modelAndView.addObject("list_of_tags", tagService.findAll());
        modelAndView.addObject("list_of_shops", shopService.findAll());
        modelAndView.addObject("user", authenticationService.getCurrentUser().get());
        return modelAndView;
    }

    private ModelAndView getDiscountBaseModelAndView(String viewName, String addressPrefix, PagedListHolder<Discount> discountPages, int currentPage) {
        ModelAndView modelAndView = getBaseModelAndView(viewName);
        discountPages.setPageSize(ITEMS_PER_PAGE);
        discountPages.setPage(currentPage);
        modelAndView.addObject("list_of_discounts", discountPages.getPageList());
        modelAndView.addObject("quantity_of_pages", discountPages.getPageCount());
        modelAndView.addObject("number_of_page", discountPages.getPage() + 1);
        modelAndView.addObject("next_and_previous", addressPrefix + "/page/id");
        String[] arrayOfAddresses = new String[discountPages.getPageCount()];
        for (int i = 0; i < arrayOfAddresses.length; i++) {
            arrayOfAddresses[i] = addressPrefix + "/page/" + (i + 1);
        }
        modelAndView.addObject("array_of_addresses", arrayOfAddresses);
        return modelAndView;
    }

    private ModelAndView getPostBaseModelAndView(String viewName, int currentPage) {
        ModelAndView modelAndView = getBaseModelAndView(viewName);
        PagedListHolder<Post> postPages = new PagedListHolder(postService.findAllByUserIncludeSorting(authenticationService.getCurrentUser().get()));
        postPages.setPageSize(ITEMS_PER_PAGE);
        postPages.setPage(currentPage);
        modelAndView.addObject("list_of_posts", postPages.getPageList());
        modelAndView.addObject("quantity_of_pages", postPages.getPageCount());
        modelAndView.addObject("number_of_page", postPages.getPage() + 1);
        modelAndView.addObject("next_and_previous", "/settings/posts/page/id");
        String[] arrayOfAddresses = new String[postPages.getPageCount()];
        for (int i = 0; i < arrayOfAddresses.length; i++) {
            arrayOfAddresses[i] = "/settings/posts/page/" + (i + 1);
        }
        modelAndView.addObject("array_of_addresses", arrayOfAddresses);
        return modelAndView;
    }

}
