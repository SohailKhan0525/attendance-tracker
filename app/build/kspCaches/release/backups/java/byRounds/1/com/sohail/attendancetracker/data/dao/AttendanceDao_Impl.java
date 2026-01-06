package com.sohail.attendancetracker.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.sohail.attendancetracker.data.model.AttendanceRecord;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AttendanceDao_Impl implements AttendanceDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AttendanceRecord> __insertionAdapterOfAttendanceRecord;

  private final EntityDeletionOrUpdateAdapter<AttendanceRecord> __deletionAdapterOfAttendanceRecord;

  private final EntityDeletionOrUpdateAdapter<AttendanceRecord> __updateAdapterOfAttendanceRecord;

  private final SharedSQLiteStatement __preparedStmtOfDeleteRecordsForDate;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllRecords;

  public AttendanceDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAttendanceRecord = new EntityInsertionAdapter<AttendanceRecord>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `attendance_records` (`id`,`date`,`periodNumber`,`subjectName`,`isPresent`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AttendanceRecord entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getDate());
        statement.bindLong(3, entity.getPeriodNumber());
        statement.bindString(4, entity.getSubjectName());
        final int _tmp = entity.isPresent() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindLong(6, entity.getTimestamp());
      }
    };
    this.__deletionAdapterOfAttendanceRecord = new EntityDeletionOrUpdateAdapter<AttendanceRecord>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `attendance_records` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AttendanceRecord entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfAttendanceRecord = new EntityDeletionOrUpdateAdapter<AttendanceRecord>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `attendance_records` SET `id` = ?,`date` = ?,`periodNumber` = ?,`subjectName` = ?,`isPresent` = ?,`timestamp` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AttendanceRecord entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getDate());
        statement.bindLong(3, entity.getPeriodNumber());
        statement.bindString(4, entity.getSubjectName());
        final int _tmp = entity.isPresent() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindLong(6, entity.getTimestamp());
        statement.bindLong(7, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteRecordsForDate = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM attendance_records WHERE date = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllRecords = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM attendance_records";
        return _query;
      }
    };
  }

  @Override
  public Object insertRecord(final AttendanceRecord record,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfAttendanceRecord.insertAndReturnId(record);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertRecords(final List<AttendanceRecord> records,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAttendanceRecord.insert(records);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteRecord(final AttendanceRecord record,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfAttendanceRecord.handle(record);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateRecord(final AttendanceRecord record,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfAttendanceRecord.handle(record);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteRecordsForDate(final String date,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteRecordsForDate.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, date);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteRecordsForDate.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllRecords(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllRecords.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAllRecords.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AttendanceRecord>> getRecordsForDate(final String date) {
    final String _sql = "SELECT * FROM attendance_records WHERE date = ? ORDER BY periodNumber";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"attendance_records"}, new Callable<List<AttendanceRecord>>() {
      @Override
      @NonNull
      public List<AttendanceRecord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfPeriodNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "periodNumber");
          final int _cursorIndexOfSubjectName = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectName");
          final int _cursorIndexOfIsPresent = CursorUtil.getColumnIndexOrThrow(_cursor, "isPresent");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<AttendanceRecord> _result = new ArrayList<AttendanceRecord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AttendanceRecord _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final int _tmpPeriodNumber;
            _tmpPeriodNumber = _cursor.getInt(_cursorIndexOfPeriodNumber);
            final String _tmpSubjectName;
            _tmpSubjectName = _cursor.getString(_cursorIndexOfSubjectName);
            final boolean _tmpIsPresent;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPresent);
            _tmpIsPresent = _tmp != 0;
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new AttendanceRecord(_tmpId,_tmpDate,_tmpPeriodNumber,_tmpSubjectName,_tmpIsPresent,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getRecordsForDateSync(final String date,
      final Continuation<? super List<AttendanceRecord>> $completion) {
    final String _sql = "SELECT * FROM attendance_records WHERE date = ? ORDER BY periodNumber";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<AttendanceRecord>>() {
      @Override
      @NonNull
      public List<AttendanceRecord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfPeriodNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "periodNumber");
          final int _cursorIndexOfSubjectName = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectName");
          final int _cursorIndexOfIsPresent = CursorUtil.getColumnIndexOrThrow(_cursor, "isPresent");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<AttendanceRecord> _result = new ArrayList<AttendanceRecord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AttendanceRecord _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final int _tmpPeriodNumber;
            _tmpPeriodNumber = _cursor.getInt(_cursorIndexOfPeriodNumber);
            final String _tmpSubjectName;
            _tmpSubjectName = _cursor.getString(_cursorIndexOfSubjectName);
            final boolean _tmpIsPresent;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPresent);
            _tmpIsPresent = _tmp != 0;
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new AttendanceRecord(_tmpId,_tmpDate,_tmpPeriodNumber,_tmpSubjectName,_tmpIsPresent,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AttendanceRecord>> getRecordsForSubject(final String subjectName) {
    final String _sql = "SELECT * FROM attendance_records WHERE subjectName = ? ORDER BY date DESC, periodNumber";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, subjectName);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"attendance_records"}, new Callable<List<AttendanceRecord>>() {
      @Override
      @NonNull
      public List<AttendanceRecord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfPeriodNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "periodNumber");
          final int _cursorIndexOfSubjectName = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectName");
          final int _cursorIndexOfIsPresent = CursorUtil.getColumnIndexOrThrow(_cursor, "isPresent");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<AttendanceRecord> _result = new ArrayList<AttendanceRecord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AttendanceRecord _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final int _tmpPeriodNumber;
            _tmpPeriodNumber = _cursor.getInt(_cursorIndexOfPeriodNumber);
            final String _tmpSubjectName;
            _tmpSubjectName = _cursor.getString(_cursorIndexOfSubjectName);
            final boolean _tmpIsPresent;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPresent);
            _tmpIsPresent = _tmp != 0;
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new AttendanceRecord(_tmpId,_tmpDate,_tmpPeriodNumber,_tmpSubjectName,_tmpIsPresent,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<AttendanceRecord>> getAllRecords() {
    final String _sql = "SELECT * FROM attendance_records ORDER BY date DESC, periodNumber DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"attendance_records"}, new Callable<List<AttendanceRecord>>() {
      @Override
      @NonNull
      public List<AttendanceRecord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfPeriodNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "periodNumber");
          final int _cursorIndexOfSubjectName = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectName");
          final int _cursorIndexOfIsPresent = CursorUtil.getColumnIndexOrThrow(_cursor, "isPresent");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<AttendanceRecord> _result = new ArrayList<AttendanceRecord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AttendanceRecord _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final int _tmpPeriodNumber;
            _tmpPeriodNumber = _cursor.getInt(_cursorIndexOfPeriodNumber);
            final String _tmpSubjectName;
            _tmpSubjectName = _cursor.getString(_cursorIndexOfSubjectName);
            final boolean _tmpIsPresent;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPresent);
            _tmpIsPresent = _tmp != 0;
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new AttendanceRecord(_tmpId,_tmpDate,_tmpPeriodNumber,_tmpSubjectName,_tmpIsPresent,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllRecordsSync(final Continuation<? super List<AttendanceRecord>> $completion) {
    final String _sql = "SELECT * FROM attendance_records ORDER BY date DESC, periodNumber DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<AttendanceRecord>>() {
      @Override
      @NonNull
      public List<AttendanceRecord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfPeriodNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "periodNumber");
          final int _cursorIndexOfSubjectName = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectName");
          final int _cursorIndexOfIsPresent = CursorUtil.getColumnIndexOrThrow(_cursor, "isPresent");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<AttendanceRecord> _result = new ArrayList<AttendanceRecord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AttendanceRecord _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final int _tmpPeriodNumber;
            _tmpPeriodNumber = _cursor.getInt(_cursorIndexOfPeriodNumber);
            final String _tmpSubjectName;
            _tmpSubjectName = _cursor.getString(_cursorIndexOfSubjectName);
            final boolean _tmpIsPresent;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPresent);
            _tmpIsPresent = _tmp != 0;
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new AttendanceRecord(_tmpId,_tmpDate,_tmpPeriodNumber,_tmpSubjectName,_tmpIsPresent,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<String>> getAllDates() {
    final String _sql = "SELECT DISTINCT date FROM attendance_records ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"attendance_records"}, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<AttendanceRecord>> getRecordsBetweenDates(final String startDate,
      final String endDate) {
    final String _sql = "SELECT * FROM attendance_records WHERE date >= ? AND date <= ? ORDER BY date, periodNumber";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindString(_argIndex, endDate);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"attendance_records"}, new Callable<List<AttendanceRecord>>() {
      @Override
      @NonNull
      public List<AttendanceRecord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfPeriodNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "periodNumber");
          final int _cursorIndexOfSubjectName = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectName");
          final int _cursorIndexOfIsPresent = CursorUtil.getColumnIndexOrThrow(_cursor, "isPresent");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<AttendanceRecord> _result = new ArrayList<AttendanceRecord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AttendanceRecord _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final int _tmpPeriodNumber;
            _tmpPeriodNumber = _cursor.getInt(_cursorIndexOfPeriodNumber);
            final String _tmpSubjectName;
            _tmpSubjectName = _cursor.getString(_cursorIndexOfSubjectName);
            final boolean _tmpIsPresent;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPresent);
            _tmpIsPresent = _tmp != 0;
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new AttendanceRecord(_tmpId,_tmpDate,_tmpPeriodNumber,_tmpSubjectName,_tmpIsPresent,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getTotalPresent(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM attendance_records WHERE isPresent = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTotalRecords(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM attendance_records";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getSubjectPresent(final String subjectName,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM attendance_records WHERE subjectName = ? AND isPresent = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, subjectName);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getSubjectTotal(final String subjectName,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM attendance_records WHERE subjectName = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, subjectName);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
