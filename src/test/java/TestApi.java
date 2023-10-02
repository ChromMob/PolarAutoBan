import top.polar.api.PolarApi;
import top.polar.api.event.Events;
import top.polar.api.server.Server;
import top.polar.api.user.repository.UserRepository;

public class TestApi implements PolarApi {
    @Override
    public Server server() {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserRepository userRepository() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Events events() {
        return null;
    }
}
