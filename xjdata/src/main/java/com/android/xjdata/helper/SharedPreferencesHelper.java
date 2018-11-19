package com.android.xjdata.helper;

import android.content.Context;

import com.android.xjdata.common.Reflex;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author ccx
 * @date 2018/11/15
 */
public class SharedPreferencesHelper {

    private static SharedPreferencesHelper                        sSharedPreferences;
    private static android.content.SharedPreferences              sXj_config;
    private static Context                                        sApplicationContext;
    private        Map<String, android.content.SharedPreferences> mPreferencesMap = new HashMap<>();

    private SharedPreferencesHelper() {
    }

    static void init(Context context) {
        sApplicationContext = context.getApplicationContext();
        sXj_config = sApplicationContext.getSharedPreferences("xj_config", Context.MODE_PRIVATE);
    }

    public static SharedPreferencesHelper getInstance() {
        if (!Reflex.isInitialization()) {
            throw new IllegalStateException("common not init");
        }

        if (sSharedPreferences == null) {
            sSharedPreferences = new SharedPreferencesHelper();
        }
        return sSharedPreferences;
    }


    public void put(String key, Object object) {
        android.content.SharedPreferences.Editor edit = edit();
        if (object instanceof String) {
            edit.putString(key, ((String) object));
        } else if (object instanceof Boolean) {
            edit.putBoolean(key, ((Boolean) object));
        } else if (object instanceof Integer) {
            edit.putInt(key, ((Integer) object));
        } else if (object instanceof Float) {
            edit.putFloat(key, ((Float) object));
        } else if (object instanceof Set) {
            edit.putStringSet(key, (Set<String>) object);
        } else if (object instanceof Long) {
            edit.putLong(key, ((Long) object));
        } else {
            edit.putString(key, object.toString());
        }
        edit.apply();

    }

    public Object get(String key) {
        Map<String, ?> all = sXj_config.getAll();
        return all.get(key);
    }

    private android.content.SharedPreferences.Editor edit() {
        return sXj_config.edit();
    }


    public String getString(String name, String key) {
        return getString(name, key, "");
    }

    public String getString(String name, String key, String defValue) {
        android.content.SharedPreferences sharedPreferences = mPreferencesMap.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = sApplicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            mPreferencesMap.put(name, sharedPreferences);
        }
        return sharedPreferences.getString(key, defValue);

    }

    public void putString(String name, String key, String value) {
        android.content.SharedPreferences sharedPreferences = mPreferencesMap.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = sApplicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            mPreferencesMap.put(name, sharedPreferences);
        }
        sharedPreferences.edit().putString(key, value).apply();
    }

    public long getLong(String name, String key) {
        return getLong(name, key, -1);
    }

    public long getLong(String name, String key, long defValue) {
        android.content.SharedPreferences sharedPreferences = mPreferencesMap.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = sApplicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            mPreferencesMap.put(name, sharedPreferences);
        }
        return sharedPreferences.getLong(key, defValue);

    }

    public void putLong(String name, String key, long value) {
        android.content.SharedPreferences sharedPreferences = mPreferencesMap.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = sApplicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            mPreferencesMap.put(name, sharedPreferences);
        }
        sharedPreferences.edit().putLong(key, value).apply();
    }

    public float getFloat(String name, String key) {
        return getFloat(name, key, -1f);
    }

    public float getFloat(String name, String key, float defValue) {
        android.content.SharedPreferences sharedPreferences = mPreferencesMap.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = sApplicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            mPreferencesMap.put(name, sharedPreferences);
        }
        return sharedPreferences.getFloat(key, defValue);

    }

    public void putFloat(String name, String key, float value) {
        android.content.SharedPreferences sharedPreferences = mPreferencesMap.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = sApplicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            mPreferencesMap.put(name, sharedPreferences);
        }
        sharedPreferences.edit().putFloat(key, value).apply();
    }

    public int getInt(String name, String key) {
        return getInt(name, key, -1);
    }

    public int getInt(String name, String key, int defValue) {
        android.content.SharedPreferences sharedPreferences = mPreferencesMap.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = sApplicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            mPreferencesMap.put(name, sharedPreferences);
        }
        return sharedPreferences.getInt(key, defValue);

    }

    public void putInt(String name, String key, int value) {
        android.content.SharedPreferences sharedPreferences = mPreferencesMap.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = sApplicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            mPreferencesMap.put(name, sharedPreferences);
        }
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public Set<String> getSet(String name, String key) {
        return getSet(name, key, null);
    }

    public Set<String> getSet(String name, String key, Set<String> defValue) {
        android.content.SharedPreferences sharedPreferences = mPreferencesMap.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = sApplicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            mPreferencesMap.put(name, sharedPreferences);
        }
        return sharedPreferences.getStringSet(key, null);

    }

    public void putSet(String name, String key, Set<String> value) {
        android.content.SharedPreferences sharedPreferences = mPreferencesMap.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = sApplicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            mPreferencesMap.put(name, sharedPreferences);
        }
        sharedPreferences.edit().putStringSet(key, value).apply();
    }

    public boolean getBoolean(String name, String key) {
        return getBoolean(name, key, false);
    }

    public boolean getBoolean(String name, String key, boolean defValue) {
        android.content.SharedPreferences sharedPreferences = mPreferencesMap.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = sApplicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            mPreferencesMap.put(name, sharedPreferences);
        }
        return sharedPreferences.getBoolean(key, defValue);

    }

    public void putBoolean(String name, String key, boolean value) {
        android.content.SharedPreferences sharedPreferences = mPreferencesMap.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = sApplicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            mPreferencesMap.put(name, sharedPreferences);
        }
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

}
