package tlvibes.logic.services;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tlvibes.data.entities.MiniAppCommandEntity;
import tlvibes.data.entities.SuperAppObjectEntity;
import tlvibes.data.entities.UserEntity;
import tlvibes.data.interfaces.MiniAppCommandRepository;
import tlvibes.data.interfaces.SuperAppObjectRepository;
import tlvibes.data.interfaces.UserEntityRepository;
import tlvibes.logic.boundaries.MiniAppCommandBoundary;
import tlvibes.logic.boundaries.identifiers.CommandId;
import tlvibes.logic.convertes.MiniAppCommandsConverter;
import tlvibes.logic.infrastructure.ConfigProperties;
import tlvibes.logic.infrastructure.Guard;
import tlvibes.logic.infrastructure.IdGenerator;
import tlvibes.logic.interfaces.MiniAppCommandsService;

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
		this.setIdGenerator(idGenerator);
		this.userEntityRepository = userEntityRepository;
		this.superAppObjectRepositoy = superAppObjectRepositoy;
		this.commandRepository = commandRepository;
	}

	@Override
	public Object invokeCommand(MiniAppCommandBoundary boundary) {
		
		Guard.AgainstNull(boundary.getCommand(), boundary.getCommand().getClass().getName());
		Guard.AgainstNull(boundary.getInvokedBy(), boundary.getInvokedBy().getClass().getName());
		Guard.AgainstNull(boundary.getTargetObject(), boundary.getTargetObject().getClass().getName());
		Guard.AgainstNull(boundary.getCommandAttributes(), boundary.getCommandAttributes().getClass().getName());

		SuperAppObjectEntity targetObject = superAppObjectRepositoy.findById(boundary.getTargetObject()).get();
		
		Guard.AgainstNull(targetObject, targetObject.getClass().getName());

		UserEntity invoker = userEntityRepository.findById(boundary.getInvokedBy()).get();

		Guard.AgainstNull(invoker, invoker.getClass().getName());
		
		String internalCommandId = idGenerator.GenerateUUID().toString();
		
		boundary.setCommandId(new CommandId(internalCommandId, this.configProperties.getSuperAppName(),boundary.getCommandId().getMiniApp()));
		boundary.setInvocationTimestamp(new Date());
		
		MiniAppCommandEntity entity = converter.toEntity(boundary,targetObject,invoker);
		
		commandRepository.save(entity);
			
		return entity;
	}

	@Override
	public List<MiniAppCommandBoundary> getAllCommands() {			
		return StreamSupport
				.stream(this.commandRepository.findAll().spliterator(), false)
				.map(this.converter::toBoundary)
				.collect(Collectors.toList());

	}

	@Override
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName) {	
		return StreamSupport
				.stream(this.commandRepository.findAll().spliterator(), false)
				.filter(command -> command.getCommandId().getMiniApp().equals(miniAppName))
				.map(this.converter::toBoundary)
				.collect(Collectors.toList());

	}

	@Override
	public void deleteAllCommands() {
		this.commandRepository.deleteAll();;
	}

	public IdGenerator getIdGenerator() {
		return idGenerator;
	}

	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

}