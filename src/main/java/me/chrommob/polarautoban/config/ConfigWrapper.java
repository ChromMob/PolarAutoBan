package me.chrommob.polarautoban.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConfigWrapper {
    private final String name;
    private final Map<String, ConfigKey> keys = new HashMap<>();

    public ConfigWrapper(String name, List<ConfigKey> keys) {
        this.name = name;
        keys.forEach(key -> this.keys.put(key.get(), key));
    }

    public String getName() {
        return name;
    }

    public ConfigKey getKey(String key) {
        return keys.get(key);
    }

    public void setConfig(Map<String, Object> loadedConfig) {
            loadedConfig.forEach((name, value) -> {
            ConfigKey key = keys.get(name);
            if (key != null) {
                key.setValue(value);
            }
        });
    }

    public Map<String, Object> getConfig() {
        Map<String, Object> config = new LinkedHashMap<>();
        keys.forEach((name, key) -> config.putAll(read(key)));
        return config;
    }

    private Map<String, Object> read(ConfigKey key) {
        Map<String, Object> config = new LinkedHashMap<>();
        Object value;
        if (key.getChildren().size() > 0) {
            value = new LinkedHashMap<>();
            key.getChildren().forEach((name, child) -> {
                Map<String, Object> childConfig = read(child);
                if (childConfig.size() > 0) {
                    ((Map<String, Object>) value).put((String) childConfig.keySet().toArray()[0], childConfig.values().toArray()[0]);
                }
            });
        } else {
            value = key.getAsObject();
        }
        config.put(key.get(), value);
        return config;
    }
}
