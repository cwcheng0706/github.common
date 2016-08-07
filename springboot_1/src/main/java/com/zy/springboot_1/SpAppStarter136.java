/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年8月7日 下午1:05:28
 */
package com.zy.springboot_1;

import java.io.PrintStream;

import org.springframework.boot.Banner;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @Project: springboot_1
 * @Author zy
 * @Company:
 * @Create Time: 2016年8月7日 下午1:05:28
 */
//@Configuration
//@EnableAutoConfiguration
//@ComponentScan
//@ConfigurationProperties(locations = "classpath:zy.properties") 
//@EnableWebMvc
@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
public class SpAppStarter136 extends SpringBootServletInitializer{

	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpAppStarter136.class);
    }

	public static void main(String[] args) {

		// System.setProperty("spring.devtools.restart.enabled", "true");

		// SpringApplication.run(SpringbootStarter1.class, args);

		SpringApplication app = new SpringApplication(SpAppStarter136.class);
		app.setBanner(new ZyBanner());
		app.setBannerMode(Mode.CONSOLE);
		app.run(args);

		
		
		
	}

	
	
	
	static class ZyBanner implements Banner {
		private static final String[] BANNER = { "",
				"  .   ____          _            __ _ _",
				" /\\\\ / ___'_ _朱_ _ _(_)_ __勇  __ _ \\ \\ \\ \\",
				"( ( )\\___ | '_ | '_| | '_ \\/ _` | \\ \\ \\ \\",
				" \\\\/  ___)| |_)| | | | | || (_| |  ) ) ) )",
				"  '  |____| .__|_| |_|_| |_\\__, | / / / /",
				" =========|_|==============|___/=/_/_/_/" };

		private static final String SPRING_BOOT = " :: Spring Boot :: ";

		private static final int STRAP_LINE_SIZE = 42;

		@Override
		public void printBanner(Environment environment, Class<?> sourceClass, PrintStream printStream) {
			for (String line : BANNER) {
				printStream.println(line);
			}
			String version = SpringBootVersion.getVersion();
			version = (version == null ? "" : " (v" + version + ")");
			String padding = "";
			while (padding.length() < STRAP_LINE_SIZE - (version.length() + SPRING_BOOT.length())) {
				padding += " ";
			}

			printStream.println(AnsiOutput.toString(AnsiColor.GREEN, SPRING_BOOT, AnsiColor.DEFAULT, padding,
					AnsiStyle.FAINT, version));
			printStream.println();
		}

	}
}
