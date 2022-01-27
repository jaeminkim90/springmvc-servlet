package hello.servlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan // 서블릿 자동 등록하고, 실행을 지원하는 애노테이션
@SpringBootApplication
public class ServletApplication {



	public static void main(String[] args) {
		SpringApplication.run(ServletApplication.class, args);
	}

}
