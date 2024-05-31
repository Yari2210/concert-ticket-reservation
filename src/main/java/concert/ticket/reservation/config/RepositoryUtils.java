package concert.ticket.reservation.config;

public class RepositoryUtils extends BaseObject {

    private static final long serialVersionUID = 4328418952292591406L;

    public static int calculateOffset(int page, int limit) {
        return ((limit * page) - limit);
    }
}
