package tlvibes.logic.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import tlvibes.data.entities.MiniAppCommandEntity;
import tlvibes.logic.boundaries.MiniAppCommandBoundary;
import tlvibes.logic.boundaries.identifiers.CommandId;
import tlvibes.logic.boundaries.identifiers.ObjectId;
import tlvibes.logic.convertes.MiniAppCommandsConverter;
import tlvibes.logic.infrastructure.ConfigProperties;
import tlvibes.logic.infrastructure.Guard;
import tlvibes.logic.infrastructure.IdGenerator;
import tlvibes.logic.interfaces.MiniAppCommandsService;

@Service
public class MockupMiniAppCommandsService implements MiniAppCommandsService {
	
	private MiniAppCommandsConverter converter;
	private List<MiniAppCommandEntity> demoes;
	private ConfigProperties configProperties;
	private IdGenerator idGenerator;
	
	
	@Autowired
	public MockupMiniAppCommandsService(MiniAppCommandsConverter converter,
			ConfigProperties configProperties,IdGenerator idGenerator) {
		this.converter = converter;
		this.configProperties = configProperties;
		this.setIdGenerator(idGenerator);
	}
	
	@PostConstruct
	public void init() {
		this.demoes = Collections.synchronizedList( 
				new ArrayList<>());
	}

	@Override
	public Object invokeCommand(MiniAppCommandBoundary boundary) {
		
		Guard.AgainstNull(boundary.getCommandId(), boundary.getCommandId().getClass().getName());
		Guard.AgainstNull(boundary.getCommandId().getMiniApp(), boundary.getCommandId().getMiniApp());
		Guard.AgainstNull(boundary.getCommand(), boundary.getCommand().getClass().getName());
		Guard.AgainstNull(boundary.getInvokedBy(), boundary.getInvokedBy().getClass().getName());
		Guard.AgainstNull(boundary.getCommand(), boundary.getCommand().getClass().getName());
		Guard.AgainstNull(boundary.getTargetObject(), boundary.getTargetObject().getClass().getName());

		String internalCommandId = idGenerator.GenerateUUID().toString();
		String internalTargetId = idGenerator.GenerateUUID().toString();
		
		boundary.setCommandId(new CommandId(internalCommandId, this.configProperties.getSuperAppName(),boundary.getCommandId().getMiniApp()));
		boundary.setInvocationTimestamp(new Date());
		boundary.getInvokedBy().setSuperApp(this.configProperties.getSuperAppName());
		boundary.setTargetObject(new ObjectId(this.configProperties.getSuperAppName(),internalTargetId));
;
		
		MiniAppCommandEntity entity = converter.toEntity(boundary);
		demoes.add(entity);
			
		return entity;
	}

	@Override
	public List<MiniAppCommandBoundary> getAllCommands() {	
		return this.demoes 
				.stream()
				.map(this.converter::toBoundary)
				.collect(Collectors.toList()); 
	}

	@Override
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName) {
		return this.demoes
				.stream()
				.filter(demo->demo.getCommandId().getMiniApp().equals(miniAppName))
				.map(this.converter::toBoundary)
				.collect(Collectors.toList());
	}

	@Override
	public void deleteAllCommands() {
		this.demoes.clear();
	}

	public IdGenerator getIdGenerator() {
		return idGenerator;
	}

	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

}