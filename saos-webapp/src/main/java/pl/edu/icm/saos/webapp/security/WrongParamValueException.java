package pl.edu.icm.saos.webapp.security;

/**
 * Exception thrown when some parameter in query string
 * have incorrect value
 * 
 * @author madryk
 */
public class WrongParamValueException extends RuntimeException {

    private static final long serialVersionUID = 1L;


    private String paramName;

    private String errorDetailsMessageCode;

    private Object[] errorDetailsMessageArgs;


    //------------------------ CONSTRUCTORS --------------------------

    public WrongParamValueException(String paramName, String errorDetailsMessageCode, Object ... errorDetailsMessageArgs) {
        super();
        this.paramName = paramName;
        this.errorDetailsMessageCode = errorDetailsMessageCode;
        this.errorDetailsMessageArgs = errorDetailsMessageArgs;
    }


    //------------------------ GETTERS --------------------------

    /**
     * Returns name of parameter that have incorrect value
     */
    public String getParamName() {
        return paramName;
    }

    /**
     * Returns message code describing why value of parameter was rejected
     */
    public String getErrorDetailsMessageCode() {
        return errorDetailsMessageCode;
    }

    /**
     * Returns arguments for resolving message code under {@link #getErrorDetailsMessageCode()}
     */
    public Object[] getErrorDetailsMessageArgs() {
        return errorDetailsMessageArgs;
    }
}
