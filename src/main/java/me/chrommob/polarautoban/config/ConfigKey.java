package me.chrommob.polarautoban.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigKey {
    private final String key;
    private Object value;
    private final Map<String, ConfigKey> children = new HashMap<>();
    public ConfigKey(String key, Object value) {
        this.key = key;
        if (value instanceof List<?> list) {
            this.value = null;
            if (list.size() > 0) {
                Object first = list.get(0);
                if (first instanceof ConfigKey) {
                    list.forEach(child -> {
                        ConfigKey configKey = (ConfigKey) child;
                        children.put(configKey.get(), configKey);
                    });
                }
            }
        } else {
            this.value = value;
        }
    }

    public Map<String, ConfigKey> getChildren() {
        return children;
    }

    public ConfigKey getKey(String key) {
        return children.get(key);
    }

    public void setValue(Object value) {
        if (value instanceof List<?> list) {
            if (list.size() > 0) {
                Object first = list.get(0);
                if (first instanceof ConfigKey) {
                    list.forEach(child -> {
                        ConfigKey configKey = (ConfigKey) child;
                        children.put(configKey.get(), configKey);
                    });
                }
            }
        } else {
            this.value = value;
        }
    }

    public String get() {
        return key;
    }

    public Object getAsObject() {
        return value;
    }

    public String getAsString() {
        if (value instanceof String) {
            return (String) value;
        }
        return value.toString();
    }

    public int getAsInt() {
        if (value instanceof Integer) {
            return (int) value;
        }
        if (value instanceof String) {
            return Integer.parseInt((String) value);
        }
        throw new ClassCastException("Cannot cast " + value.getClass().getName() + " to int");
    }

    public double getAsDouble() {
        if (value instanceof Double) {
            return (double) value;
        }
        if (value instanceof String) {
            return Double.parseDouble((String) value);
        }
        throw new ClassCastException("Cannot cast " + value.getClass().getName() + " to double");
    }

    public float getAsFloat() {
        if (value instanceof Float) {
            return (float) value;
        }
        if (value instanceof String) {
            return Float.parseFloat((String) value);
        }
        throw new ClassCastException("Cannot cast " + value.getClass().getName() + " to float");
    }

    public <T> T getAsType(Class<T> type) throws ClassCastException {
        return type.cast(value);
    }
}
