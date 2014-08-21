package pl.edu.icm.saos.api.exceptions;

/**
 * @author pavtel
 */
public class WrongRequestParameterException extends Exception {

    private static final String MESSAGE_FORMAT = "parameter '%s' : ";
    private static final long serialVersionUID = 4688793562774681215L;

    private String parameterName;

    public WrongRequestParameterException(String parameterName, String message) {
        super(String.format(MESSAGE_FORMAT, parameterName)+message);
        this.parameterName = parameterName;
    }

    public String getParameterName() {
        return parameterName;
    }
}
