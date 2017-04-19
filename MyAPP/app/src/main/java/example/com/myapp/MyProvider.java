package example.com.myapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by yangjieqiong on 2017/4/10.
 */

public class MyProvider extends ContentProvider {
    public static final int blacklist_DIR = 0;
    public static final int blacklist_ITEM = 1;
    private MyDatabaseHelper myDatabaseHelper;
    private static UriMatcher uriMatcher;
    public static final String AUTHORITY = "example.com.myapp";

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "BlackList", blacklist_DIR);
        uriMatcher.addURI(AUTHORITY, "BlackList/#", blacklist_ITEM);
    }

    @Override
    public boolean onCreate() {
        Log.d("MyProvider","onCreate");
        myDatabaseHelper = new MyDatabaseHelper(getContext(), "blacklist.db", null, 1);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = myDatabaseHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case blacklist_DIR:
                // 查询BlackList表中的所有数据
                cursor = db.query("BlackList", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case blacklist_ITEM:
                String bookId = uri.getPathSegments().get(1);
                cursor = db.query("BlackList", projection, "id = ?", new String[]{bookId}, null, null, sortOrder);
                // 查询BlackList表中的单条数据
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case blacklist_DIR:
                return "vnd.android.cursor.dir/vnd.example.com.myapp.BlackList ";
            case blacklist_ITEM:
                return "vnd.android.cursor.item/vnd.example.com.myapp.BlackList ";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)) {
            case blacklist_DIR:
            case blacklist_ITEM:
                long newBookId = db.insert("BlackList", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/BlackList/" + newBookId);
                break;
        }
        return uriReturn;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        int deletedRows = 0;
        switch (uriMatcher.match(uri)) {
            case blacklist_DIR:
                // 删除BlackList表中的所有数据
                deletedRows = db.delete("BlackList", selection, selectionArgs);
                break;
            case blacklist_ITEM:
                // 删除BlackList表中的单条数据
                String bookId = uri.getPathSegments().get(1);
                deletedRows = db.delete("BlackList", "id = ?", new String[]{bookId});
                break;
        }
        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        // 更新数据
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        int updatedRows = 0;
        switch (uriMatcher.match(uri)) {
            case blacklist_DIR:
                updatedRows = db.update("BlackList", values, selection, selectionArgs);
                break;
            case blacklist_ITEM:
                String Id = uri.getPathSegments().get(1);
                updatedRows = db.update("BlackList", values, "id = ?", new String[]{Id});
                break;
        }
        return updatedRows;
    }
}
