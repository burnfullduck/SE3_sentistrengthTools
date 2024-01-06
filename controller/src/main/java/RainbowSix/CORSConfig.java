package RainbowSix;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CORSConfig {

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOrigin("*");
		// 允许token放置于请求头
		corsConfiguration.addExposedHeader("token");
		// 2
		corsConfiguration.addAllowedHeader("*");
		// 3
		corsConfiguration.addAllowedMethod("*");

		source.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(source);
	}

}
