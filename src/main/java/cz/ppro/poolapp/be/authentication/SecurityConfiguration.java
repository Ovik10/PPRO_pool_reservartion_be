package cz.ppro.poolapp.be.authentication;

import cz.ppro.poolapp.be.repository.AccountRepository;
import cz.ppro.poolapp.be.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableJpaRepositories(basePackageClasses = AccountRepository.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

    private final CustomUserDetailsService userDetailService;

    public SecurityConfiguration(CustomUserDetailsService customUserDetailsService){
        this.userDetailService= customUserDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)  {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/courses/create/**", "/courses/update/**", "/courses/remove/**").hasRole("Admin")
                .antMatchers( "/accounts/create/admin/**", "/roles/all", "/roles/detail/**", "/roles/create", "/roles/update/**", "/roles/remove/**").hasRole("Admin")
                .antMatchers( "/accounts/all/trainer", "/accounts/all/clients", "/accounts/detail/**", "/accounts/update/**", "/accountSignedCourse/allByClient/**").authenticated()
                .antMatchers(  "/tickets/all", "/tickets/account/**",  "/tickets/detail/**").authenticated()
                .antMatchers(   "/tickets/create/**", "/tickets/update/all", "/tickets/remove/**", "/ticketTypes/all").hasRole("Admin")
                .antMatchers("/news/**").permitAll()
                .anyRequest().permitAll()
                .and()
                .httpBasic();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userDetailService);
        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

