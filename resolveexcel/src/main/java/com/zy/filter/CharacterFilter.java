package com.zy.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class CharacterFilter implements Filter {

	private static Logger logger = Logger.getLogger(CharacterFilter.class);
	
	public void destroy() {
		logger.info("---CharacterFilter  destory--");
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
			ServletException {
		logger.info("------------CharacterFilter.doFilter()-----------");
		
		String charst = "UTF-8";
		HttpServletRequest request = (HttpServletRequest) req;  
        HttpServletResponse response = (HttpServletResponse) resp;  
        request.setCharacterEncoding(charst);  
        response.setCharacterEncoding(charst);  
        response.setContentType("text/html;charset="+charst);  
        try{
        	chain.doFilter(request, response);  
        }catch(Exception e) {
        	logger.error(e);
        }
		
	}

	@SuppressWarnings("unused")
	public void init(FilterConfig arg0) throws ServletException {
		logger.info("---CharacterFilter  init--");
		
		Thread t = new Thread(new Runnable() {
			public void run() {
				while(true) {
					System.out.println("=======================================");
					System.out.println("sysoust-------");
					logger.debug("debug================");
					logger.info("info******************");
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
//		t.start();
	}

}
