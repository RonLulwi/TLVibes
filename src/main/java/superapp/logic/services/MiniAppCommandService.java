package superapp.logic.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import superapp.data.entities.MiniAppCommandEntity;
import superapp.data.interfaces.MiniAppCommandRepository;
import superapp.data.interfaces.SuperAppObjectRepository;
import superapp.data.interfaces.UserEntityRepository;
import superapp.logic.boundaries.MiniAppCommandBoundary;
import superapp.logic.boundaries.identifiers.CommandId;
import superapp.logic.convertes.MiniAppCommandsConverter;
import superapp.logic.infrastructure.ConfigProperties;
import superapp.logic.infrastructure.Guard;
import superapp.logic.infrastructure.IdGenerator;
import superapp.logic.interfaces.MiniAppCommandsService;
import superapp.logic.superAppInvokables.CommandFactory;

@Service
public class MiniAppCommandService implements MiniAppCommandsService {
	
	private MiniAppCommandRepository commandRepository;
	private MiniAppCommandsConverter converter;
	private ConfigProperties configProperties;
	private IdGenerator idGenerator;
	private CommandFactory commandFactory;
    private ApplicationContext appContext;


	@Autowired
	public MiniAppCommandService(MiniAppCommandsConverter converter,
			ConfigProperties configProperties,IdGenerator idGenerator,
			UserEntityRepository userEntityRepository,
			SuperAppObjectRepository superAppObjectRepositoy,
			MiniAppCommandRepository commandRepository,
			CommandFactory commandFactory,
			ApplicationContext appContext) {
		this.converter = converter;
		this.commandRepository = commandRepository;
		this.idGenerator = idGenerator;
		this.configProperties = configProperties;
		this.commandFactory = commandFactory;
		this.appContext = appContext;
	}

	@Override
	@Transactional()
	public Object invokeCommand(MiniAppCommandBoundary boundary) {
		
		Guard.AgainstNull(boundary, boundary.getClass().getName());
		Guard.AgainstNull(boundary.getCommand(), boundary.getCommand().getClass().getName());
		Guard.AgainstNull(boundary.getInvokedBy(), boundary.getInvokedBy().getClass().getName());
		Guard.AgainstNull(boundary.getTargetObject(), boundary.getTargetObject().getClass().getName());
		Guard.AgainstNull(boundary.getCommandId().getMiniapp(), boundary.getCommandId().getMiniapp().getClass().getName());

		CommandId commandId = new CommandId(
				configProperties.getSuperAppName(),
				boundary.getCommandId().getMiniapp(),
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
		return StreamSupport
				.stream(this.commandRepository.findAll().spliterator(), false)
				.map(entity -> converter.toBoundary(entity))
				.collect(Collectors.toList());

	}

	@Override	
	@Transactional(readOnly = true)
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName) {	
		return StreamSupport
				.stream(this.commandRepository.findAll().spliterator(), false)
				.filter(command -> command.getCommandId().getMiniapp().equals(miniAppName))
				.map(entity -> converter.toBoundary(entity))
				.collect(Collectors.toList());

	}

	@Override
	@Transactional
	public void deleteAllCommands() {
		this.commandRepository.deleteAll();;
	}
}