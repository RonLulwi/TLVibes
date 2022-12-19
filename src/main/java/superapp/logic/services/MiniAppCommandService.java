package superapp.logic.services;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import superapp.data.entities.MiniAppCommandEntity;
import superapp.data.entities.SuperAppObjectEntity;
import superapp.data.entities.UserEntity;
import superapp.data.interfaces.MiniAppCommandRepository;
import superapp.data.interfaces.SuperAppObjectRepository;
import superapp.data.interfaces.UserEntityRepository;
import superapp.logic.boundaries.MiniAppCommandBoundary;
import superapp.logic.boundaries.identifiers.CommandId;
import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;
import superapp.logic.boundaries.identifiers.UserId;
import superapp.logic.convertes.MiniAppCommandsConverter;
import superapp.logic.infrastructure.ConfigProperties;
import superapp.logic.infrastructure.Guard;
import superapp.logic.infrastructure.IdGenerator;
import superapp.logic.interfaces.MiniAppCommandsService;

@Service
public class MiniAppCommandService implements MiniAppCommandsService {
	
	private UserEntityRepository userEntityRepository;
	private SuperAppObjectRepository superAppObjectRepositoy;
	private MiniAppCommandRepository commandRepository;
	private MiniAppCommandsConverter converter;
	private ConfigProperties configProperties;
	private IdGenerator idGenerator;
	
	
	@Autowired
	public MiniAppCommandService(MiniAppCommandsConverter converter,
			ConfigProperties configProperties,IdGenerator idGenerator,
			UserEntityRepository userEntityRepository,
			SuperAppObjectRepository superAppObjectRepositoy,
			MiniAppCommandRepository commandRepository) {
		this.converter = converter;
		this.configProperties = configProperties;
		this.idGenerator = idGenerator;
		this.userEntityRepository = userEntityRepository;
		this.superAppObjectRepositoy = superAppObjectRepositoy;
		this.commandRepository = commandRepository;
	}

	@Override
	@Transactional()
	public Object invokeCommand(MiniAppCommandBoundary boundary) {
		
		Guard.AgainstNull(boundary, boundary.getClass().getName());
		Guard.AgainstNull(boundary.getCommand(), boundary.getCommand().getClass().getName());
		Guard.AgainstNull(boundary.getInvokedBy(), boundary.getInvokedBy().getClass().getName());
		Guard.AgainstNull(boundary.getTargetObject(), boundary.getTargetObject().getClass().getName());

		MiniAppCommandEntity entity = ConvertCommandEnityToBoundary(boundary);
		
		var returned = commandRepository.save(entity);
			
		return ConvertCommandBoundaryToEntity(returned);
	}


	@Override
	@Transactional(readOnly = true)
	public List<MiniAppCommandBoundary> getAllCommands() {			
		return StreamSupport
				.stream(this.commandRepository.findAll().spliterator(), false)
				.map(entity -> ConvertCommandBoundaryToEntity(entity))
				.collect(Collectors.toList());

	}

	@Override	
	@Transactional(readOnly = true)
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName) {	
		return StreamSupport
				.stream(this.commandRepository.findAll().spliterator(), false)
				.filter(command -> command.getCommandId().getMiniapp().equals(miniAppName))
				.map(entity -> ConvertCommandBoundaryToEntity(entity))
				.collect(Collectors.toList());

	}

	@Override
	@Transactional
	public void deleteAllCommands() {
		this.commandRepository.deleteAll();;
	}

	private MiniAppCommandBoundary ConvertCommandBoundaryToEntity(MiniAppCommandEntity entity) {

		SuperAppObjectIdBoundary targetId = entity.getTargetObject().getObjectId();
		
		UserId invokerId = entity.getInvokedBy().getUserId();

		return converter.toBoundary(entity, targetId, invokerId);
	}

	private MiniAppCommandEntity ConvertCommandEnityToBoundary(MiniAppCommandBoundary boundary) {
		SuperAppObjectEntity targetObject = superAppObjectRepositoy.findById(boundary.getTargetObject()).get();
		
		Guard.AgainstNull(targetObject, targetObject.getClass().getName());

		UserEntity invoker = userEntityRepository.findById(boundary.getInvokedBy()).get();

		Guard.AgainstNull(invoker, invoker.getClass().getName());
		
		String internalCommandId = idGenerator.GenerateUUID().toString();
		
		boundary.setCommandId(new CommandId(internalCommandId, this.configProperties.getSuperAppName(),boundary.getCommandId().getMiniapp()));
		
		boundary.setInvocationTimestamp(new Date());
		
		MiniAppCommandEntity entity = converter.toEntity(boundary,targetObject,invoker);
		
		return entity;
	}

}