package superapp;

import org.springframework.stereotype.Component;

@Component
public class ControllersTestsHelper {
	
	public String userPrefix = "/superapp/users/";
	public String objectPrefix = "/superapp/objects/";

	public String GetBaseUserBoundaryAsJson() {
		return "{\r\n"
				+ "    \"email\": \"niv@demo.org\",\r\n"
				+ "    \"role\": \"SUPERAPP_USER\",\r\n"
				+ "    \"username\": \"niv\",\r\n"
				+ "    \"avatar\": \"N\"\r\n"
				+ "}";
	}
	
	public String GetBaseCommandBoundaryAsJson() {
		return "{\r\n"
				+ "    \"commandId\" : {\r\n"
				+ "        \"superapp\" : \"2023a.Assaf.Ariely\",\r\n"
				+ "        \"miniapp\" : \"dummyApp\",\r\n"
				+ "        \"internalCommandId\" : \"490edcaf-08db-4eac-a1e3-2cbf7932c412\"\r\n"
				+ "    },\r\n"
				+ "    \"command\" : \"doSomthing\",\r\n"
				+ "    \"targetObject\" : {\r\n"
				+ "        \"objectId\" : {\r\n"
				+ "            \"superapp\": \"2023a.Assaf.Ariely\",\r\n"
				+ "            \"internalObjectId\" : \"8a7257dd-b534-4747-ae3c-9f551f93eeaa\"\r\n"
				+ "        }\r\n"
				+ "    },\r\n"
				+ "    \"invocationTimeStamp\" : \"2022-11-26T15:15:18.479+00:00\",\r\n"
				+ "    \"invokedBy\" : {\r\n"
				+ "        \"userId\" : {\r\n"
				+ "            \"superapp\": \"2023a.Assaf.Ariely\",\r\n"
				+ "            \"email\" : \"niv@demo.org\"\r\n"
				+ "        }\r\n"
				+ "    },\r\n"
				+ "    \"commandAttributes\" : {\r\n"
				+ "        \"key1\" : {\r\n"
				+ "            \"key2subkey1\" : \"can be anything you wish, even a nested json\"\r\n"
				+ "        }\r\n"
				+ "    }\r\n"
				+ "}\r\n";
	}

	public String GetBaseObjectBoundaryAsJson() {
		return "{\r\n"
				+ "    \"objectId\": {\r\n"
				+ "        \"superapp\" : \"nivsminiapp\",\r\n"
				+ "        \"internalObjectId\": \"1000\"\r\n"
				+ "    },\r\n"
				+ "    \"type\": \"dummyType\",\r\n"
				+ "    \"alias\": \"niv's demo instance\",\r\n"
				+ "    \"active\": true,\r\n"
				+ "    \"creationTimestamp\": \"2022-11-26T15:15:18.479+00:00\",\r\n"
				+ "    \"objectDetails\": {\r\n"
				+ "        \"key1\" : \"11\",\r\n"
				+ "        \"key2\" : \"12\"\r\n"
				+ "    },\r\n"
				+ "    \"createdBy\": {\r\n"
				+ "        \"userId\" : {\r\n"
				+ "            \"superapp\": \"2023a.Assaf.Ariely\",\r\n"
				+ "            \"email\": \"niv@demo.org\"\r\n"
				+ "        }\r\n"
				+ "    }\r\n"
				+ "}";
	}

}
