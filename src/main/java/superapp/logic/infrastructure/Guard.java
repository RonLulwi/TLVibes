package superapp.logic.infrastructure;

import java.util.Map;
import java.util.Optional;

public class Guard {

    public static void AgainstNull(Object argument, String argumentName) throws RuntimeException   
    {
        if (argument == null)
        {
            throw new RuntimeException(String.format("%s is null",argumentName));
        }
    }
    
    public static void AgainstNullOrEmpty(String argument, String argumentName) throws RuntimeException   
    {
        if (argument == null || argument == "")
        {
            throw new RuntimeException(String.format("%s is null",argumentName));
        }
    }

    public static void AgainstNullRequest(Map<String,Object> keyValuePairs) throws RuntimeException   
    {
    	for (Map.Entry<String, Object> entry : keyValuePairs.entrySet()) {
            if (entry.getValue() == null)
            {
                throw new RuntimeException(String.format("%s is null",entry.getKey()));
            }
    	}
    }

    public static <T> void AgainstNullOptinalIdNotFound(Optional<T> argument, String id,String className) throws RuntimeException   
    {
        if (argument.isEmpty())
        {
            throw new RuntimeException(String.format("Could not find an object %s by id %s",className, id));
        }
    }

}
