package com.android.common.common;

import android.util.Log;

import com.android.common.BuildConfig;

/**
 * @author chicunxiang
 */
public class Logger {


    public static void i(String tag, String s) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, s);
        }
    }

    public static void i(String s) {
        if (BuildConfig.DEBUG) {
            Log.i(Logger.class.getName(), s);
        }
    }


    public static void i(String tag, Object o) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, getTargetStr(o));
        }
    }


    public static void i(Object o) {
        if (BuildConfig.DEBUG) {
            Log.i(Logger.class.getName(), getTargetStr(o));
        }
    }


    public static void w(String tag, String s) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, s);
        }
    }

    public static void w(String s) {
        if (BuildConfig.DEBUG) {
            Log.w(Logger.class.getName(), s);
        }
    }


    public static void w(String tag, Object o) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, getTargetStr(o));
        }
    }

    public static void w(Object o) {
        if (BuildConfig.DEBUG) {
            Log.w(Logger.class.getName(), getTargetStr(o));
        }
    }


    public static void d(String tag, String s) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, s);
        }
    }

    public static void d(String s) {
        if (BuildConfig.DEBUG) {
            Log.d(Logger.class.getName(), s);
        }
    }

    public static void d(String tag, Object o) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, getTargetStr(o));
        }
    }

    public static void d(Object o) {
        if (BuildConfig.DEBUG) {
            Log.d(Logger.class.getName(), getTargetStr(o));
        }
    }

    public static void e(String tag, String s) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, s);
        }
    }

    public static void e(String s) {
        if (BuildConfig.DEBUG) {
            Log.e(Logger.class.getName(), s);
        }
    }

    public static void e(String tag, Object o) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, getTargetStr(o));
        }
    }

    public static void e(Object o) {
        if (BuildConfig.DEBUG) {
            Log.e(Logger.class.getName(), getTargetStr(o));
        }
    }

    public static void v(String tag, String s) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, s);
        }
    }

    public static void v(String s) {
        if (BuildConfig.DEBUG) {
            Log.v(Logger.class.getName(), s);
        }
    }

    public static void v(String tag, Object o) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, getTargetStr(o));
        }
    }

    public static void v(Object o) {
        if (BuildConfig.DEBUG) {
            Log.v(Logger.class.getName(), getTargetStr(o));
        }
    }


    private static String getTargetStr(Object o) {
        if (o instanceof String) {
            return (String) o;
        } else if (o instanceof Boolean) {
            return String.valueOf(o);
        } else if (o instanceof Float) {
            return String.valueOf(o);
        } else if (o instanceof Integer) {
            return String.valueOf(o);
        } else if (o instanceof Double) {
            return String.valueOf(o);
        } else if (o instanceof Long) {
            return String.valueOf(o);
        } else {
            if (o == null) {
                return null;
            } else {
                return o.toString();
            }
        }
    }

}
