package tlvibes.logic.infrastructure;

public class Guard {

    public static void AgainstNull(Object argument, String argumentName) throws RuntimeException   
    {
        if (argument == null)
        {
            throw new RuntimeException(String.format("%s is null",argumentName));
        }
    }

}
