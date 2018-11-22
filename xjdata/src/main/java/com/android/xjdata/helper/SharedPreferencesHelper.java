package com.android.xjdata.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.xjdata.common.Reflex;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * @author ccx
 * @date 2018/11/15
 */
public class SharedPreferencesHelper {

    @SuppressLint("StaticFieldLeak")
    private static SharedPreferencesHelper                        sSharedPreferences;
    private static SharedPreferences              sXj_config;
    private static Context                                        sApplicationContext;
    private        Map<String, SharedPreferences> mPreferencesMap = new HashMap<>();

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
        SharedPreferences.Editor edit = edit();
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
        SharedPreferencesCompat.apply(edit);
    }

    public Object get(String key) {
        Map<String, ?> all = sXj_config.getAll();
        return all.get(key);
    }

    private SharedPreferences.Editor edit() {
        return sXj_config.edit();
    }


    public String getString(String name, String key) {
        return getString(name, key, "");
    }

    public String getString(String name, String key, String defValue) {
        SharedPreferences sharedPreferences = mPreferencesMap.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = sApplicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            mPreferencesMap.put(name, sharedPreferences);
        }
        return sharedPreferences.getString(key, defValue);

    }

    public void putString(String name, String key, String value) {
        SharedPreferences sharedPreferences = mPreferencesMap.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = sApplicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            mPreferencesMap.put(name, sharedPreferences);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit().putString(key, value);
        SharedPreferencesCompat.apply(editor);
    }

    public long getLong(String name, String key) {
        return getLong(name, key, -1);
    }

    public long getLong(String name, String key, long defValue) {
        SharedPreferences sharedPreferences = mPreferencesMap.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = sApplicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            mPreferencesMap.put(name, sharedPreferences);
        }
        return sharedPreferences.getLong(key, defValue);

    }

    public void putLong(String name, String key, long value) {
        SharedPreferences sharedPreferences = mPreferencesMap.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = sApplicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            mPreferencesMap.put(name, sharedPreferences);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit().putLong(key, value);
        SharedPreferencesCompat.apply(editor);
    }

    public float getFloat(String name, String key) {
        return getFloat(name, key, -1f);
    }

    public float getFloat(String name, String key, float defValue) {
        SharedPreferences sharedPreferences = mPreferencesMap.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = sApplicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            mPreferencesMap.put(name, sharedPreferences);
        }
        return sharedPreferences.getFloat(key, defValue);

    }

    public void putFloat(String name, String key, float value) {
        SharedPreferences sharedPreferences = mPreferencesMap.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = sApplicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            mPreferencesMap.put(name, sharedPreferences);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit().putFloat(key, value);
        SharedPreferencesCompat.apply(editor);
    }

    public int getInt(String name, String key) {
        return getInt(name, key, -1);
    }

    public int getInt(String name, String key, int defValue) {
        SharedPreferences sharedPreferences = mPreferencesMap.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = sApplicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            mPreferencesMap.put(name, sharedPreferences);
        }
        return sharedPreferences.getInt(key, defValue);

    }

    public void putInt(String name, String key, int value) {
        SharedPreferences sharedPreferences = mPreferencesMap.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = sApplicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            mPreferencesMap.put(name, sharedPreferences);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit().putInt(key, value);
        SharedPreferencesCompat.apply(editor);
    }

    public Set<String> getSet(String name, String key) {
        return getSet(name, key, null);
    }

    public Set<String> getSet(String name, String key, Set<String> defValue) {
        SharedPreferences sharedPreferences = mPreferencesMap.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = sApplicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            mPreferencesMap.put(name, sharedPreferences);
        }
        return sharedPreferences.getStringSet(key, null);

    }

    public void putSet(String name, String key, Set<String> value) {
        SharedPreferences sharedPreferences = mPreferencesMap.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = sApplicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            mPreferencesMap.put(name, sharedPreferences);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit().putStringSet(key, value);
        SharedPreferencesCompat.apply(editor);
    }

    public boolean getBoolean(String name, String key) {
        return getBoolean(name, key, false);
    }

    public boolean getBoolean(String name, String key, boolean defValue) {
        SharedPreferences sharedPreferences = mPreferencesMap.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = sApplicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            mPreferencesMap.put(name, sharedPreferences);
        }
        return sharedPreferences.getBoolean(key, defValue);

    }

    public void putBoolean(String name, String key, boolean value) {
        SharedPreferences sharedPreferences = mPreferencesMap.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = sApplicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            mPreferencesMap.put(name, sharedPreferences);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit().putBoolean(key, value);
        SharedPreferencesCompat.apply(editor);
    }


    public void clear(String fileName) {
        SharedPreferences sharedPreferences = mPreferencesMap.get(fileName);
        if (sharedPreferences != null) {
            SharedPreferences.Editor clear = sharedPreferences.edit().clear();
            SharedPreferencesCompat.apply(clear);
        }
    }

    public boolean contains(String fileName, String key) {
        SharedPreferences sharedPreferences = mPreferencesMap.get(fileName);
        if (sharedPreferences != null) {
            return sharedPreferences.contains(key);
        }
        return false;
    }


    /**
     * 版本兼容类
     */
    static class SharedPreferencesCompat {

        private static Method sMethod = findApplyMethod();

        /**
         * 查找apply方法
         *
         * @return
         */
        private static Method findApplyMethod() {
            try {
                Class<SharedPreferences.Editor> editorClass = SharedPreferences.Editor.class;
                return editorClass.getMethod("apply");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 早期版本没有apply方法
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sMethod != null) {
                    sMethod.invoke(editor);
                    return;
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            editor.commit();
        }
    }
}
