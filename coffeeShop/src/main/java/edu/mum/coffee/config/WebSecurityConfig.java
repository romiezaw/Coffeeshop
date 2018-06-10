package edu.mum.coffee.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import edu.mum.coffee.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private static String REALM="COFFESHOP";

	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
//        http
//            .authorizeRequests()
//                .antMatchers("/", "/home", "/index").permitAll()
//                .anyRequest().authenticated()
//                .and()
//            .formLogin()
//            	.loginPage("/login")
//            	.permitAll()
//            	.and()
//            .logout()
//            	.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//            	.logoutSuccessUrl("/")
//                .permitAll();
        
        
        http.authorizeRequests().antMatchers("/webjars/**", "/css/**", "/images/**","/signup", "/login", "/h2-console/**").permitAll();
		http.csrf().disable();
		http.headers().frameOptions().disable();

		http.authorizeRequests().antMatchers("/products/**",
				"/product/**", "/person/**", "/persons/**", "/orders/**")
			//.hasRole("ADMIN")
			.permitAll()
			.and().httpBasic()
			.realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint());

		http.authorizeRequests().antMatchers("/placeOrder", "/my-account").authenticated();

		http.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
				.logout()
				.permitAll();
    }

//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("super").password("pw").roles("ADMIN");
//	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userDetailsService);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
	
	@Bean
	public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint(){
		return new CustomBasicAuthenticationEntryPoint();
	}
}