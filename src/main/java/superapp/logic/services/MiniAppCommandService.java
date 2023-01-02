package superapp.logic.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import superapp.data.MiniAppCommandEntity;
import superapp.data.UserRole;
import superapp.data.interfaces.MiniAppCommandRepository;
import superapp.data.interfaces.SuperAppObjectRepository;
import superapp.data.interfaces.UserEntityRepository;
import superapp.logic.EnhancedMiniAppCommandsService;
import superapp.logic.boundaries.MiniAppCommandBoundary;
import superapp.logic.boundaries.UserBoundary;
import superapp.logic.boundaries.identifiers.CommandId;
import superapp.logic.convertes.MiniAppCommandsConverter;
import superapp.logic.convertes.ObjectConvertor;
import superapp.logic.infrastructure.ConfigProperties;
import superapp.logic.infrastructure.DeprecatedFunctionException;
import superapp.logic.infrastructure.Guard;
import superapp.logic.infrastructure.IdGenerator;
import superapp.logic.superAppInvokables.CommandFactory;

@Service
public class MiniAppCommandService implements EnhancedMiniAppCommandsService {
	
	private MiniAppCommandRepository commandRepository;
	private MiniAppCommandsConverter converter;
	private ConfigProperties configProperties;
	private IdGenerator idGenerator;
	private CommandFactory commandFactory;
	private UserService userService;
	private SuperAppObjectRepository superAppObjectRepositoy;
	private ObjectConvertor objectConvertor;

	@Autowired
	public MiniAppCommandService(MiniAppCommandsConverter converter, ConfigProperties configProperties,IdGenerator idGenerator,
			UserService userService, ObjectConvertor objectConvertor, SuperAppObjectRepository superAppObjectRepositoy,MiniAppCommandRepository commandRepository,CommandFactory commandFactory) {
		this.converter = converter;
		this.commandRepository = commandRepository;
		this.idGenerator = idGenerator;
		this.configProperties = configProperties;
		this.commandFactory = commandFactory;
		this.userService = userService;
		this.objectConvertor = objectConvertor;
		this.superAppObjectRepositoy = superAppObjectRepositoy;
	}

	@Override
	@Transactional()
	public Object invokeCommand(String miniAppName, MiniAppCommandBoundary boundary) {
		Guard.AgainstNull(boundary, boundary.getClass().getName());
		try {
			Guard.AgainstNull(boundary.getCommand(), boundary.getCommand().getClass().getName());	
		}
		catch(Exception e) {
			throw new MissingCommandOnPostRequestException("Command property is missing!");
		}
		Guard.AgainstNull(boundary.getInvokedBy(), boundary.getInvokedBy().getClass().getName());
		Guard.AgainstNull(boundary.getTargetObject(), boundary.getTargetObject().getClass().getName());
		Guard.AgainstNull(miniAppName, miniAppName);
		
		String userSuperApp = boundary.getInvokedBy().get("userId").getSuperapp();
		String userEmail = boundary.getInvokedBy().get("userId").getEmail();
		
		UserBoundary user = userService.login(userSuperApp, userEmail);
		if(user.getRole() != UserRole.MINIAPP_USER)
			throw new UnAuthoriezedRoleRequestException("Only MiniApp user has permission!");
		
		String objectSuperApp = boundary.getTargetObject().get("objectId").getSuperapp();
		String objectInternalId = boundary.getTargetObject().get("objectId").getInternalObjectId();
		
		userService.validateObjectActive(objectSuperApp, objectInternalId, this.superAppObjectRepositoy, this.objectConvertor);
		
		CommandId commandId = new CommandId(
				configProperties.getSuperAppName(),
				miniAppName,
				idGenerator.GenerateUUID().toString());
		
		MiniAppCommandEntity commandEntity = converter.toEntity(boundary,commandId);
		
		
		var invokableCommand = commandFactory.GetCommand(commandEntity.getCommand());
		
		var response = invokableCommand.Invoke(commandEntity);
		
		commandRepository.save(commandEntity);
		
		return response;
	}
	


	@Override
	@Transactional(readOnly = true)
	public List<MiniAppCommandBoundary> getAllCommands() {			
		throw new DeprecatedFunctionException("Unsupported paging getAllCommands function is deprecated ");
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public List<MiniAppCommandBoundary> getAllCommands(String userSuperApp, String userEmail, int size ,int page) {			
		
		UserBoundary user = userService.login(userSuperApp, userEmail);
		if(user.getRole() != UserRole.ADMIN)
			throw new UnAuthoriezedRoleRequestException("Only ADMIN has permission!");

		return this.commandRepository
				.findAll(PageRequest.of(page, size, Direction.DESC, "commandId.miniapp"))
				.stream()
				.map(this.converter::toBoundary)
				.collect(Collectors.toList());

	}

	@Override	
	@Transactional(readOnly = true)
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName) {	
		throw new DeprecatedFunctionException("Unsupported paging getAllMiniAppCommands function is deprecated ");
	}
	
	@Override	
	@Transactional(readOnly = true)
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String userSuperApp, String userEmail, String miniAppName,int size,int page) {

		UserBoundary user = userService.login(userSuperApp, userEmail);
		if(user.getRole() != UserRole.ADMIN)
			throw new UnAuthoriezedRoleRequestException("Only ADMIN has permission!");

		return  this.commandRepository
				.findAllByCommandId_Miniapp(miniAppName,PageRequest.of(page, size, Direction.DESC,
						"commandId.miniapp","commandId.internalCommandId"))
				.stream()
				.map(this.converter::toBoundary)
				.collect(Collectors.toList());
	}
	
	@Override
	@Transactional
	public void deleteAllCommands() {
		throw new DeprecatedFunctionException("Unsupported paging deleteAllCommands function is deprecated ");
	}
	
	@Override
	@Transactional
	public void deleteAllCommands(String userSuperApp, String userEmail) {

		UserBoundary user = userService.login(userSuperApp, userEmail);
		if(user.getRole() != UserRole.ADMIN)
			throw new UnAuthoriezedRoleRequestException("Only ADMIN has permission!");

		this.commandRepository.deleteAll();;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}






























