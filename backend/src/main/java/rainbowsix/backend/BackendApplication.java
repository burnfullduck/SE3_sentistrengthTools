package rainbowsix.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}



//	@Bean
//	public FilterRegistrationBean corsFilterRegistration() {
//		FilterRegistrationBean registrationBean = new FilterRegistrationBean(new CorsFilter());
//		registrationBean.setOrder(0);
//		return registrationBean;
//	}


}
