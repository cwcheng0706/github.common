/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年8月20日 下午8:06:56
 */
package com.zy.sec1.cfg;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.ConcurrentSessionFilter;

import com.zy.sec1.entity.User;

/**
 * @Project: sec1
 * @Author zy
 * @Company:
 * @Create Time: 2016年8月20日 下午8:06:56
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);
	
	
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		
//		auth.inMemoryAuthentication().withUser("admin").password("admin").roles("USER");
		
		//因为需要自动登录，就把eraseCredentials设为false
		auth.authenticationProvider(customerAuthenticationProvider())
			.eraseCredentials(false).userDetailsService(customerUserDetailsService())
			.passwordEncoder(passwordEncoder())
			;
		
		AuthenticationEventPublisher eventPublisher = new AuthenticationEventPublisher() {
			public void publishAuthenticationSuccess(Authentication authentication) {
				LOGGER.info("---------publishAuthenticationSuccess--------");
			}
			
			public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
				LOGGER.info("---------publishAuthenticationFailure--------");
			}
		};
		auth.authenticationEventPublisher(eventPublisher);
		
	}

	protected void configure(HttpSecurity http) throws Exception {
		LOGGER.info("-------configure-------");
//		super.configure(http);
//		http
//        .authorizeRequests()
//            .antMatchers("/","/index").permitAll()
//            .anyRequest().authenticated()
//            .and()
//        .formLogin()
//            .loginPage("/login")
//            .permitAll()
//            .and()
//        .logout()
//            .permitAll();
		
		http
			.authorizeRequests()
				.antMatchers("/","/redis/**","/chart/**","/rest/**").permitAll()
				.antMatchers("/error").permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.successHandler(authenticationSuccessHandler())
				.permitAll()
				.and()
			.logout()                                                                
				.logoutUrl("/logout")
//				.logoutSuccessUrl("/index")       
				.invalidateHttpSession(true)          
				.addLogoutHandler(customerLogoutHandler())  
//				.deleteCookies(cookieNamesToClear)
				
				;   
		
		http.rememberMe();
		
//		http.sessionManagement()
//			.maximumSessions(1)
//			.expiredUrl("/")
//			.sessionRegistry(getSessionRegistryImpl())
//			;
		
		http
	        .sessionManagement()
	        .maximumSessions(1)
	        .expiredUrl("/login?expired")
	        .sessionRegistry(getSessionRegistryImpl())
//	        .maxSessionsPreventsLogin(true)
	        .and()
//	        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
	        .invalidSessionUrl("/");
		
		http.csrf().disable();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationProvider customerAuthenticationProvider() {
		return new CustomerAuthenticationProvider();
	}
	@Bean
	public UserDetailsService customerUserDetailsService() {
		return new CustomerUserDetailsService();
	}
	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new CustomerAuthenticationSuccessHandler();
	}
	@Bean
	public CustomerLogoutHandler customerLogoutHandler() {
		return new CustomerLogoutHandler();
	}
	
	@Bean
	public ConcurrentSessionFilter getConcurrentSessionFilter() {
		ConcurrentSessionFilter concurrentSessionFilter = new ConcurrentSessionFilter(getSessionRegistryImpl());
		return concurrentSessionFilter;
	}
	
	@Bean
	public SessionRegistryImpl getSessionRegistryImpl() {
		return new SessionRegistryImpl();
	}
	
	
	/**
	 * session 单点登录
	 * @Author zy
	 * @Company: 
	 * @Create Time: 2016年8月29日 下午9:38:19
	 * @return
	 */
	@Bean
	public UsernamePasswordAuthenticationFilter getUsernamePasswordAuthenticationFilter() {
		UsernamePasswordAuthenticationFilter authenticationFilter = new UsernamePasswordAuthenticationFilter();
		authenticationFilter.setSessionAuthenticationStrategy(getCompositeSessionAuthenticationStrategy());
		
		List<AuthenticationProvider> providers = new ArrayList<AuthenticationProvider>();
		providers.add(customerAuthenticationProvider());
		ProviderManager providerManager = new ProviderManager(providers );
		authenticationFilter.setAuthenticationManager(providerManager);
		return authenticationFilter;
	}
	@Bean
	public CompositeSessionAuthenticationStrategy getCompositeSessionAuthenticationStrategy() {
		List<SessionAuthenticationStrategy> delegateStrategies = new ArrayList<SessionAuthenticationStrategy>();
		delegateStrategies.add(getConcurrentSessionControlStrategy());
		CompositeSessionAuthenticationStrategy authenticationStrategy = new CompositeSessionAuthenticationStrategy(delegateStrategies);
		return authenticationStrategy;
	}
	@Bean
	public ConcurrentSessionControlAuthenticationStrategy getConcurrentSessionControlStrategy() {
		ConcurrentSessionControlAuthenticationStrategy authenticationStrategy = new ConcurrentSessionControlAuthenticationStrategy(getSessionRegistryImpl());
		authenticationStrategy.setMaximumSessions(1);
		authenticationStrategy.setExceptionIfMaximumExceeded(false);
		return authenticationStrategy;
	}
	
	
	private class CustomerLogoutHandler implements LogoutHandler{
		public void logout(HttpServletRequest req, HttpServletResponse resp, Authentication auth){
			LOGGER.info("--------LogoutHandler-------");
			
			try {
//				req.getSession().invalidate();
				
				resp.sendRedirect("/");
			} catch (IOException e) {
				LOGGER.error(e.getMessage());
			}
		}
	};
	
	private class CustomerAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
		
		public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication auth)
				throws IOException, ServletException {
			LOGGER.info("--------AuthenticationSuccessHandler-------");
			HttpSession session = req.getSession();
			
			Object principal = auth.getPrincipal();
//			String name = auth.getName();
//			Object obj = auth.getDetails();
//			Object credentials = auth.getCredentials();
			
			session.setAttribute("user", principal);
			resp.sendRedirect("/");
//			req.getRequestDispatcher("/").forward(req, resp);
		}
	};
	
	private class CustomerAuthenticationProvider implements AuthenticationProvider{
		
		@Autowired
		UserDetailsService userDetailsService;
		@Autowired
		SessionRegistry sessionRegistry;
		
		public boolean supports(Class<?> authentication) {
//			LOGGER.info("--------supports------");
			return true;
		}
		
		public Authentication authenticate(Authentication authentication) throws AuthenticationException {
			
//			List<SessionInformation> sessions = sessionRegistry.getAllSessions(authentication.getPrincipal(), true);
//			if(null != sessions) {
//				for (Iterator iterator = sessions.iterator(); iterator.hasNext();) {
//					SessionInformation sessionInformation = (SessionInformation) iterator.next();
//					sessionInformation.expireNow();
//				}
//			}
			
			
			LOGGER.info("--------authenticate------");
			LOGGER.info("username【{}】,password【{}】,getPrincipal【{}】,getAuthorities【{}】",authentication.getName(),authentication.getCredentials(),authentication.getPrincipal(),authentication.getAuthorities());
	        WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) authentication.getDetails();
	        System.out.println("IP:" + webAuthenticationDetails.getRemoteAddress());
//	        System.out.println("SESSIONID:" + webAuthenticationDetails.getSessionId());
	        
	        //
	        UserDetails userDetails = (UserDetails)this.userDetailsService.loadUserByUsername(authentication.getName());
	        
	        if(!passwordEncoder().matches(String.valueOf(authentication.getCredentials()), userDetails.getPassword())) {
	        	throw new BadCredentialsException("用户名或密码错误");
	        }
	        
	        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userDetails, authentication.getCredentials(),userDetails.getAuthorities());
	        return result;
	        
		}
	}
	
	private class CustomerUserDetailsService implements UserDetailsService{
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			
//			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			System.out.println(principal);
			
			//自定义 查询 数据库对应角色 
			ArrayList<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
	        list.add(new SimpleGrantedAuthority("ROLE_SUPERVISOR"));
	        User details = new User(username, "$2a$10$HKRnfqobP9cU.4HFmUPm3eF8J2KVvG/1eujlzbhZkqstA98Bb74hS", list);
	        
	        //这里可以判断用户名是否存在。不存在 抛异常出去
	        
	        return details;
		}
		
	}
}
