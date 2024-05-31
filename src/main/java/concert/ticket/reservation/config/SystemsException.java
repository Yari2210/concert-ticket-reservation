package concert.ticket.reservation.config;

public class SystemsException extends BaseException {
    private static final long serialVersionUID = -3557619039782448828L;

    public SystemsException() {
        super();
        type = TYPE_SYSTEMS;
    }

    public SystemsException(String message) {
        super(message);
        type = TYPE_SYSTEMS;
    }

    public SystemsException(String message, Throwable cause) {
        super(message, cause);
        type = TYPE_SYSTEMS;
    }

    public SystemsException(Throwable cause) {
        super(cause);
        type = TYPE_SYSTEMS;
    }

    public SystemsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        type = TYPE_SYSTEMS;
    }

    public SystemsException(Status status) {
        super(status.getResponsemessage());
        this.status = status;
        type = TYPE_SYSTEMS;
    }

    public SystemsException(String key, String moduleName, String parameter) {
        super(String.format("%s not found with %s : '%s'", key, moduleName, parameter));
        this.key = key;
        this.moduleName = moduleName;
        this.parameter = parameter;
        type = TYPE_SYSTEMS;
    }
}
