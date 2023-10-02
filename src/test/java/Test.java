import me.chrommob.polarautoban.config.ConfigKey;
import me.chrommob.polarautoban.config.ConfigManager;
import me.chrommob.polarautoban.config.ConfigWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        ConfigManager cm = new ConfigManager(new File("test"));
        List<ConfigKey> keys = new ArrayList<>();
        ConfigKey ck = new ConfigKey("test", "test");
        ConfigKey ck1 = new ConfigKey("test1", "test1");
        ConfigKey ck2 = new ConfigKey("test2", List.of(ck, ck1));
        keys.add(ck);
        keys.add(ck2);

        keys.add(ck2);

        ConfigWrapper cw = new ConfigWrapper("test", keys);
        cm.addConfig(cw);
        System.out.println(cm.getConfigWrapper("test").getKey("test").getAsString());

    }
}
