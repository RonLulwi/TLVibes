package tlvibes.logic.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


//@ConfigurationProperties
@Component
@Configuration
public class ConfigProperties {
	
	@Value("${spring.application.name}")
	private String superAppName;
	
	public String getSuperAppName() {
		return superAppName;
	}
	

}
