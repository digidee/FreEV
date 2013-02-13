package com.example.freespot.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LoggingDataSource {

  // Database fields
  private SQLiteDatabase database;
  private MySQLiteHelper dbHelper;
  private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
      MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_TOTALCOSTS, MySQLiteHelper.COLUMN_PRODUCT };

  public LoggingDataSource(Context context) {
    dbHelper = new MySQLiteHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public Logging createLog(String log, int totalCosts, String product) {
    ContentValues values = new ContentValues();
    values.put(MySQLiteHelper.COLUMN_NAME, log);
    values.put(MySQLiteHelper.COLUMN_TOTALCOSTS, totalCosts);
    values.put(MySQLiteHelper.COLUMN_PRODUCT, product);
    long insertId = database.insert(MySQLiteHelper.TABLE_LOGGING, null,
        values);
    Cursor cursor = database.query(MySQLiteHelper.TABLE_LOGGING,
        allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    Logging logging = cursorToLog(cursor);
    cursor.close();
    return logging;
  }

  
  public void deleteLog(Logging log) {
    long id = log.getId();
    System.out.println("Log deleted with id: " + id);
    database.delete(MySQLiteHelper.TABLE_LOGGING, MySQLiteHelper.COLUMN_ID
        + " = " + id, null);
  }

  public List<Logging> getAllLogs() {
    List<Logging> logs = new ArrayList<Logging>();

    Cursor cursor = database.query(MySQLiteHelper.TABLE_LOGGING,
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Logging log = cursorToLog(cursor);
      logs.add(log);
      cursor.moveToNext();
    }
    // Make sure to close the cursor
    cursor.close();
    return logs;
  }

  private Logging cursorToLog(Cursor cursor) {
    Logging log = new Logging();
    log.setId(cursor.getLong(0));
    log.setName(cursor.getString(1));
    log.setTotalCosts(cursor.getInt(2));
    log.setProduct(cursor.getString(3));
    return log;
  }
} 