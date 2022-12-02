package demo.logic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigProperties {
	
	@Value("${spring.application.name}")
	private String superAppName;
	
	
	
	
	public String getSuperAppName() {
		return superAppName;
	}
	

}
