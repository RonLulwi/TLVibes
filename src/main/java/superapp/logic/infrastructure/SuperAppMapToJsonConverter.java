package superapp.logic.infrastructure;

import java.util.Map;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SuperAppMapToJsonConverter implements AttributeConverter<Map<String, Object>, String> {
	private ObjectMapper jackson;
	
	public SuperAppMapToJsonConverter() {
		this.jackson = new ObjectMapper();
	}

	@Override
	public String convertToDatabaseColumn(Map<String, Object> attribute) {
		try {
			return this.jackson.writeValueAsString(attribute);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Map<String, Object> convertToEntityAttribute(String dbData) {
		try {
			return this.jackson.readValue(dbData, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

}
