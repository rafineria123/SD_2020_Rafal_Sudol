package pl.okazje.project.configurations;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.okazje.project.services.UserService;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    public WebConfig(UserService userService) {
        this.userService = userService;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // resources access
        http.csrf().disable()
                .authorizeRequests().antMatchers("/register", "/resources/**", "/css/**", "/js/**", "/js/mixitup/dist/**", "/js/mixitup/**", "/images/**").permitAll()
                //set login page & exceptions
                .and().formLogin().loginPage("/login")
                .usernameParameter("login").passwordParameter("password").permitAll()
                .failureHandler((req, res, exp) -> {
                    if (exp.getClass().isAssignableFrom(BadCredentialsException.class)) {
                        res.sendRedirect("/login?error=true");
                    } else if (exp.getClass().isAssignableFrom(DisabledException.class)) {
                        res.sendRedirect("/login?token=true");
                    } else {
                        res.sendRedirect("/login?ban=true");
                    }
                })
                .and().logout().permitAll().and().logout().logoutSuccessUrl("/login?logout").permitAll()
                // session
                .and().sessionManagement().maximumSessions(3).expiredUrl("/login?session=true").and().invalidSessionUrl("/login?session=true");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
