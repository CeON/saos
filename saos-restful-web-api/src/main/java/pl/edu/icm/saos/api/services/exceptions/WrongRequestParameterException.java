package pl.edu.icm.saos.api.services.exceptions;

/**
 * @author pavtel
 */
public class WrongRequestParameterException extends RuntimeException {

    private static final String MESSAGE_FORMAT = "parameter '%s' : ";
    private static final long serialVersionUID = 4688793562774681215L;

    private String parameterName;

    public WrongRequestParameterException(String parameterName, String message) {
        super(String.format(MESSAGE_FORMAT, parameterName)+message);
        this.parameterName = parameterName;
    }

    public WrongRequestParameterException(String message) {
        super(message);
    }

    public String getParameterName() {
        return parameterName;
    }
}
