package com.android.xjdata.manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.xjdata.model.RawBasic;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ccx
 * @date 2018/11/16
 */
public abstract class BaseRawManager extends SQLiteOpenHelper {
    public BaseRawManager(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        reCreateTable(db, 0);
    }

    /**
     * 重新创建表
     * @param db
     * @param i
     */
    public abstract void reCreateTable(SQLiteDatabase db, int i);

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion != oldVersion) {
            reCreateTable(db, oldVersion);
        }
    }

    static class Column {
        static final String COLUMN_ID          = "_id";
        static final String COLUMN_CREATE_TIME = "create_time";
        static final String COLUMN_LAST_UPDATE = "last_update";
        static final String COLUMN_STATUS      = "status";
    }

    /**
     * 只有value
     * <p>
     * | _id | value | create_time | last_update |
     */
    protected static class ValueColumn
            extends BaseRawManager.Column {
        static final String COLUMN_VALUE = "value";

        public static String createTableSQL(String tableName) {
            return "CREATE TABLE " + tableName + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_VALUE + " TEXT, " +
                    COLUMN_CREATE_TIME + " TEXT, " +
                    COLUMN_LAST_UPDATE + " TEXT, " +
                    COLUMN_STATUS + " TEXT" +
                    ");";
        }
    }

    /**
     * 有name+value
     * <p>
     * | _id | name | value | create_time | last_update |
     */
    protected static class NameValueColumn
            extends BaseRawManager.ValueColumn {
        private static final String COLUMN_NAME = "name";

        public static String createTableSQL(String tableName) {
            return "CREATE TABLE " + tableName + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_VALUE + " TEXT, " +
                    COLUMN_CREATE_TIME + " TEXT, " +
                    COLUMN_LAST_UPDATE + " TEXT, " +
                    COLUMN_STATUS + " TEXT" +
                    ");";
        }
    }


    public List<List<RawBasic>> getValues(String tableName, String... col) {
        synchronized (BaseRawManager.class) {
            SQLiteDatabase db = getReadableDatabase();
            try {
                List<List<RawBasic>> localMap = getRawColumns(db, tableName, col);
                close(db);
                return localMap;
            } finally {
                close(db);
            }
        }
    }

    /**
     * 查询一张表的多个列
     *
     * @param db
     * @param tableName
     * @param col
     * @return
     */
    private List<List<RawBasic>> getRawColumns(SQLiteDatabase db, String tableName, String... col) {
        List<List<RawBasic>> values   = new ArrayList<>();
        int                  rawCount = getRawCount(db, tableName);
        try {
            for (int i = 0; i < rawCount; i += 1000) {
                // 分批查询，防止一瞬间查询过多
                List<List<RawBasic>> batches = getRawColumns(db, tableName, i, 1000, col);
                values.addAll(batches);
                if (batches.size() < 1000) {
                    break;
                }
            }
        } catch (Throwable ignored) {
        }
        return values;
    }

    /**
     * 查询数量
     * @param db
     * @param tableName
     * @return
     */
    private int getRawCount(SQLiteDatabase db, String tableName) {
        Cursor cursor = null;
        try {
            cursor = db.query(tableName, new String[]{"count(1)"}, null, null, null, null, null);
            if ((null != cursor) && (cursor.moveToFirst())) {
                return cursor.getInt(0);
            }
        } catch (Throwable ignored) {
        } finally {
            close(cursor);
        }
        return 0;
    }


    private List<List<RawBasic>> getRawColumns(SQLiteDatabase db, String tableName, int offset, int count, String... col) {
        Cursor               cursor = null;
        List<List<RawBasic>> values = new ArrayList<>();
        try {
            cursor = db.query(tableName, col, null, null, null, null, null, offset + ", " + count);
            if ((null != cursor) && (cursor.moveToFirst())) {
                while (cursor.moveToNext()) {
                    List<RawBasic> list = new ArrayList<>();
                    for (String name : col) {
                        String value = cursor.getString(cursor.getColumnIndex(name));
                        list.add(new RawBasic(name, value));
                    }
                    values.add(list);
                }
            }
        } finally {
            close(cursor);
        }
        return values;
    }

    public void close(Cursor cursor) {
        try {
            if (null != cursor) {
                cursor.close();
                cursor = null;
            }
        } catch (Throwable ignored) {
        }
    }

    private void close(SQLiteDatabase db) {
        close(db, false);
    }

    private void close(SQLiteDatabase db, boolean trans) {
        // 判断是否有事务
        if (trans) {
            try {
                if (null != db) {
                    db.endTransaction();
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        try {
            if (null != db) {
                db.close();
                db = null;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
