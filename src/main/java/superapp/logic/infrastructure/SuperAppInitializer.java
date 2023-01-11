package superapp.logic.infrastructure;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

import superapp.data.SuperAppObjectEntity;
import superapp.data.UserEntity;
import superapp.data.UserRole;
import superapp.data.identifiers.ObjectId;
import superapp.data.interfaces.SuperAppObjectRepository;
import superapp.data.interfaces.UserEntityRepository;
import superapp.logic.boundaries.identifiers.UserId;

public class SuperAppInitializer implements CommandLineRunner{
	
	private SuperAppObjectRepository objectsRepository;
	private UserEntityRepository usersRepository;
	
	@Value("${scooter.defualtid}")
	private String scooterId;
	
	@Value("${miniappuser.defualtemail}")
	private String miniAppUserEmail;
	
	private ConfigProperties configProperties;
	
	
	@Autowired
	public SuperAppInitializer(SuperAppObjectRepository objectsRepository, UserEntityRepository usersRepository, ConfigProperties configProperties) {
		this.objectsRepository = objectsRepository;
		this.configProperties = configProperties;
		this.usersRepository = usersRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		
		ObjectId objectId = new ObjectId(configProperties.getSuperAppName(), scooterId);
		
		if(!this.objectsRepository.existsById(objectId)) {
			
			SuperAppObjectEntity objectEntity = new SuperAppObjectEntity();
			objectEntity.setActive(true);
			objectEntity.setAlias("scooter");
			objectEntity.setChildrens(null);
			objectEntity.setCreatedBy(null);
			objectEntity.setCreationTimestamp(new Date());
			objectEntity.setObjectDetails(new HashMap<String, Object>());
			objectEntity.setObjectId(objectId);
			objectEntity.setParent(null);
			objectEntity.setType("vendor");			
			this.objectsRepository.save(objectEntity);
		}
			
		
		
		UserId userId = new UserId(this.configProperties.getSuperAppName(), miniAppUserEmail);
		
		if(!this.usersRepository.existsById(userId)) {
		
			UserEntity miniAppUser = new UserEntity();		
			miniAppUser.setAvatar("commend invoker");
			miniAppUser.setRole(UserRole.MINIAPP_USER);
			miniAppUser.setUserId(userId);
			miniAppUser.setUsername("miniAppUser");
			this.usersRepository.save(miniAppUser);
		}
				
	}
	
}
