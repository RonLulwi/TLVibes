package tlvibes.logic.infrastructure;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class EmailValidator {
	/*
	The email address must start with a word character (alphanumeric character plus underscore), a hyphen, or a plus sign.
	The email address must contain at least one @ symbol.
	The email address must end with a period followed by at least two alphabetic characters (e.g. .com, .net, .org).
	 */
    private String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    private Pattern pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

    public boolean validate(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

