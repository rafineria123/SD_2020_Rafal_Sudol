package pl.okazje.project.configurations;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.userdetails.UserDetails;
import pl.okazje.project.services.UserService;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class WebConfig extends WebSecurityConfigurerAdapter {



    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserService userService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/register", "/resources/**",
                        "/css/**", "/js/**","/js/mixitup/dist/**","/js/mixitup/**", "/images/**").permitAll().and().formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .usernameParameter("login")
                .passwordParameter("password")
                .permitAll().failureHandler((req,res,exp)->{
                                                            if(exp.getClass().isAssignableFrom(BadCredentialsException.class)){
                                                                res.sendRedirect("/login?error=true");
                                                            }else if(exp.getClass().isAssignableFrom(DisabledException.class)){
                                                                res.sendRedirect("/login?token=true");
                                                            }else {
                                                                res.sendRedirect("/login?ban=true");
                                                            }
                                                        })
                .and()
                .logout()
                .permitAll().and()
                .logout()
                .logoutSuccessUrl("/login?logout")
                .permitAll().and().authorizeRequests().antMatchers("/settings/*","/settings","/messages").hasAnyAuthority("USER","ADMIN")
                .and().sessionManagement().maximumSessions(3).expiredUrl("/login").and().invalidSessionUrl("/login");

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
    {
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
