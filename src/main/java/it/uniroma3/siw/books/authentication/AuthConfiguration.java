package it.uniroma3.siw.books.authentication;



import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import it.uniroma3.siw.books.model.Credential;


@Configuration
@EnableWebSecurity
public class AuthConfiguration {


	@Autowired
	private DataSource dataSource;

	@Autowired
	  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	    auth.jdbcAuthentication()
	    .dataSource(dataSource)
	    .authoritiesByUsernameQuery("SELECT username, role from credential WHERE username=?")
	    .usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credential WHERE username=?");
	  }

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	protected SecurityFilterChain configure(final HttpSecurity httpSecurity)
	throws Exception{
		return httpSecurity
                .csrf(CsrfConfigurer::disable)
                .cors(CorsConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                		.requestMatchers("/imagesStatic/**").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        
                        .requestMatchers(HttpMethod.GET, "/", "/register", "/author/**","/book/**","/books/**","/authors/**","/fragments/**", "/genres/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/register", "/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority(Credential.ADMIN_ROLE)
                        .requestMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority(Credential.ADMIN_ROLE)
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                )
                // LOGOUT: qui definiamo il logout
                .logout(logout -> logout
                        // il logout è attivato con una richiesta GET a "/logout"
                        .logoutUrl("/logout")
                        // in caso di successo, si viene reindirizzati alla home
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .clearAuthentication(true).permitAll()
                )
                .build();


	}
}


