package tlvibes.logic.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tlvibes.data.entities.MiniAppCommandEntity;
import tlvibes.logic.boundaries.MiniAppCommandBoundary;
import tlvibes.logic.boundaries.identifiers.ObjectId;
import tlvibes.logic.convertes.MiniAppCommandsConverter;
import tlvibes.logic.infrastructure.ConfigProperties;
import tlvibes.logic.interfaces.MiniAppCommandsService;

@Service
public class MockupMiniAppCommandsService implements MiniAppCommandsService {
	
	private MiniAppCommandsConverter converter;
	private List<MiniAppCommandEntity> demoes;
	private ConfigProperties configProperties;
	
	
	@Autowired
	public MockupMiniAppCommandsService(MiniAppCommandsConverter converter, ConfigProperties configProperties) {
		this.converter = converter;
		this.configProperties = configProperties;
	}
	
	@PostConstruct
	public void init() {
		this.demoes = Collections.synchronizedList( 
				new ArrayList<>());
	}

	@Override
	public Object invokeCommand(MiniAppCommandBoundary boundary) {
		
		if(boundary.getCommandId() == null) 
			throw new RuntimeException("commandId is missing");
		
		boundary.getCommandId().setSupperApp(this.configProperties.getSuperAppName());
		boundary.getCommandId().setInternalCommanId(UUID.randomUUID().toString());
		
		if(boundary.getCommandId().getMiniApp() == null)
			throw new RuntimeException("commandId.miniApp is missing");
				
		boundary.setInvocationTimestamp(new Date());
		
		if(boundary.getCommand() == null)
			throw new RuntimeException("command is missing");
		
		ObjectId targetObject = boundary.getTargetObject();
		
		if(targetObject == null) 
			throw new RuntimeException("targetObject is missing");
		
		if(targetObject.getInternalObjectId() == null)                             
			throw new RuntimeException("targetObject.internalObjectId is missing");
		
		if(targetObject.getSupperApp() == null)
			throw new RuntimeException("targetObject.supperApp is missing");
				
		if(boundary.getInvokedBy() == null)
			throw new RuntimeException("invokedBy is missing");
		
		if(boundary.getInvokedBy().getEmail() == null)
			throw new RuntimeException("invokedBy.email is missing");
		
		if(boundary.getInvokedBy().getSuperApp() == null)
			throw new RuntimeException("invokedBy.supperApp is missing");
		
		MiniAppCommandEntity entity = converter.toEntity(boundary);
		demoes.add(entity);
			
		return entity;
	}

	@Override
	public List<MiniAppCommandBoundary> getAllCommands() {	
		//TODO test getAllCommands function
		return this.demoes 
				.stream()
				.map(this.converter::toBoundary)
				.collect(Collectors.toList()); 
	}

	@Override
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName) {
		//TODO test getAllMiniAppCommands function
		return this.demoes
				.stream()
				.filter(demo->demo.getCommandId().getMiniApp().equals(miniAppName))
				.map(this.converter::toBoundary)
				.collect(Collectors.toList());
	}

	@Override
	public void deleteAllCommands() {
		//TODO test deleteAllCommands function
		this.demoes.clear();

	}

}