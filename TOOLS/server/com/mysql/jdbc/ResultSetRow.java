/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.sql.Date;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.Calendar;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.TimeZone;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class ResultSetRow
/*      */ {
/*      */   protected ExceptionInterceptor exceptionInterceptor;
/*      */   protected Field[] metadata;
/*      */   
/*      */   protected ResultSetRow(ExceptionInterceptor exceptionInterceptor) {
/*   53 */     this.exceptionInterceptor = exceptionInterceptor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void closeOpenStreams();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract InputStream getBinaryInputStream(int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract byte[] getColumnValue(int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Date getDateFast(int columnIndex, byte[] dateAsBytes, int offset, int length, ConnectionImpl conn, ResultSetImpl rs, Calendar targetCalendar) throws SQLException {
/*   97 */     int year = 0;
/*   98 */     int month = 0;
/*   99 */     int day = 0;
/*      */     
/*      */     try {
/*  102 */       if (dateAsBytes == null) {
/*  103 */         return null;
/*      */       }
/*      */       
/*  106 */       boolean allZeroDate = true;
/*      */       
/*  108 */       boolean onlyTimePresent = false;
/*      */       int i;
/*  110 */       for (i = 0; i < length; i++) {
/*  111 */         if (dateAsBytes[offset + i] == 58) {
/*  112 */           onlyTimePresent = true;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*  117 */       for (i = 0; i < length; i++) {
/*  118 */         byte b = dateAsBytes[offset + i];
/*      */         
/*  120 */         if (b == 32 || b == 45 || b == 47) {
/*  121 */           onlyTimePresent = false;
/*      */         }
/*      */         
/*  124 */         if (b != 48 && b != 32 && b != 58 && b != 45 && b != 47 && b != 46) {
/*      */           
/*  126 */           allZeroDate = false;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */       
/*  132 */       if (!onlyTimePresent && allZeroDate) {
/*      */         
/*  134 */         if ("convertToNull".equals(conn.getZeroDateTimeBehavior()))
/*      */         {
/*      */           
/*  137 */           return null; } 
/*  138 */         if ("exception".equals(conn.getZeroDateTimeBehavior()))
/*      */         {
/*  140 */           throw SQLError.createSQLException("Value '" + new String(dateAsBytes) + "' can not be represented as java.sql.Date", "S1009", this.exceptionInterceptor);
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  148 */         return rs.fastDateCreate(targetCalendar, 1, 1, 1);
/*      */       } 
/*  150 */       if (this.metadata[columnIndex].getMysqlType() == 7) {
/*      */         
/*  152 */         switch (length) {
/*      */           case 19:
/*      */           case 21:
/*      */           case 29:
/*  156 */             year = StringUtils.getInt(dateAsBytes, offset + 0, offset + 4);
/*      */             
/*  158 */             month = StringUtils.getInt(dateAsBytes, offset + 5, offset + 7);
/*      */             
/*  160 */             day = StringUtils.getInt(dateAsBytes, offset + 8, offset + 10);
/*      */ 
/*      */             
/*  163 */             return rs.fastDateCreate(targetCalendar, year, month, day);
/*      */ 
/*      */           
/*      */           case 8:
/*      */           case 14:
/*  168 */             year = StringUtils.getInt(dateAsBytes, offset + 0, offset + 4);
/*      */             
/*  170 */             month = StringUtils.getInt(dateAsBytes, offset + 4, offset + 6);
/*      */             
/*  172 */             day = StringUtils.getInt(dateAsBytes, offset + 6, offset + 8);
/*      */ 
/*      */             
/*  175 */             return rs.fastDateCreate(targetCalendar, year, month, day);
/*      */ 
/*      */           
/*      */           case 6:
/*      */           case 10:
/*      */           case 12:
/*  181 */             year = StringUtils.getInt(dateAsBytes, offset + 0, offset + 2);
/*      */ 
/*      */             
/*  184 */             if (year <= 69) {
/*  185 */               year += 100;
/*      */             }
/*      */             
/*  188 */             month = StringUtils.getInt(dateAsBytes, offset + 2, offset + 4);
/*      */             
/*  190 */             day = StringUtils.getInt(dateAsBytes, offset + 4, offset + 6);
/*      */ 
/*      */             
/*  193 */             return rs.fastDateCreate(targetCalendar, year + 1900, month, day);
/*      */ 
/*      */           
/*      */           case 4:
/*  197 */             year = StringUtils.getInt(dateAsBytes, offset + 0, offset + 4);
/*      */ 
/*      */             
/*  200 */             if (year <= 69) {
/*  201 */               year += 100;
/*      */             }
/*      */             
/*  204 */             month = StringUtils.getInt(dateAsBytes, offset + 2, offset + 4);
/*      */ 
/*      */             
/*  207 */             return rs.fastDateCreate(targetCalendar, year + 1900, month, 1);
/*      */ 
/*      */           
/*      */           case 2:
/*  211 */             year = StringUtils.getInt(dateAsBytes, offset + 0, offset + 2);
/*      */ 
/*      */             
/*  214 */             if (year <= 69) {
/*  215 */               year += 100;
/*      */             }
/*      */             
/*  218 */             return rs.fastDateCreate(targetCalendar, year + 1900, 1, 1);
/*      */         } 
/*      */ 
/*      */         
/*  222 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[] { new String(dateAsBytes), Constants.integerValueOf(columnIndex + 1) }), "S1009", this.exceptionInterceptor);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  234 */       if (this.metadata[columnIndex].getMysqlType() == 13) {
/*      */         
/*  236 */         if (length == 2 || length == 1) {
/*  237 */           year = StringUtils.getInt(dateAsBytes, offset, offset + length);
/*      */ 
/*      */           
/*  240 */           if (year <= 69) {
/*  241 */             year += 100;
/*      */           }
/*      */           
/*  244 */           year += 1900;
/*      */         } else {
/*  246 */           year = StringUtils.getInt(dateAsBytes, offset + 0, offset + 4);
/*      */         } 
/*      */ 
/*      */         
/*  250 */         return rs.fastDateCreate(targetCalendar, year, 1, 1);
/*  251 */       }  if (this.metadata[columnIndex].getMysqlType() == 11) {
/*  252 */         return rs.fastDateCreate(targetCalendar, 1970, 1, 1);
/*      */       }
/*  254 */       if (length < 10) {
/*  255 */         if (length == 8) {
/*  256 */           return rs.fastDateCreate(targetCalendar, 1970, 1, 1);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  261 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[] { new String(dateAsBytes), Constants.integerValueOf(columnIndex + 1) }), "S1009", this.exceptionInterceptor);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  274 */       if (length != 18) {
/*  275 */         year = StringUtils.getInt(dateAsBytes, offset + 0, offset + 4);
/*      */         
/*  277 */         month = StringUtils.getInt(dateAsBytes, offset + 5, offset + 7);
/*      */         
/*  279 */         day = StringUtils.getInt(dateAsBytes, offset + 8, offset + 10);
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  284 */         StringTokenizer st = new StringTokenizer(new String(dateAsBytes, offset, length, "ISO8859_1"), "- ");
/*      */ 
/*      */         
/*  287 */         year = Integer.parseInt(st.nextToken());
/*  288 */         month = Integer.parseInt(st.nextToken());
/*  289 */         day = Integer.parseInt(st.nextToken());
/*      */       } 
/*      */ 
/*      */       
/*  293 */       return rs.fastDateCreate(targetCalendar, year, month, day);
/*  294 */     } catch (SQLException sqlEx) {
/*  295 */       throw sqlEx;
/*  296 */     } catch (Exception e) {
/*  297 */       SQLException sqlEx = SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[] { new String(dateAsBytes), Constants.integerValueOf(columnIndex + 1) }), "S1009", this.exceptionInterceptor);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  302 */       sqlEx.initCause(e);
/*      */       
/*  304 */       throw sqlEx;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract Date getDateFast(int paramInt, ConnectionImpl paramConnectionImpl, ResultSetImpl paramResultSetImpl, Calendar paramCalendar) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int getInt(int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract long getLong(int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Date getNativeDate(int columnIndex, byte[] bits, int offset, int length, ConnectionImpl conn, ResultSetImpl rs, Calendar cal) throws SQLException {
/*  339 */     int year = 0;
/*  340 */     int month = 0;
/*  341 */     int day = 0;
/*      */     
/*  343 */     if (length != 0) {
/*  344 */       year = bits[offset + 0] & 0xFF | (bits[offset + 1] & 0xFF) << 8;
/*      */       
/*  346 */       month = bits[offset + 2];
/*  347 */       day = bits[offset + 3];
/*      */     } 
/*      */     
/*  350 */     if (year == 0 && month == 0 && day == 0) {
/*  351 */       if ("convertToNull".equals(conn.getZeroDateTimeBehavior()))
/*      */       {
/*  353 */         return null; } 
/*  354 */       if ("exception".equals(conn.getZeroDateTimeBehavior()))
/*      */       {
/*  356 */         throw SQLError.createSQLException("Value '0000-00-00' can not be represented as java.sql.Date", "S1009", this.exceptionInterceptor);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  362 */       year = 1;
/*  363 */       month = 1;
/*  364 */       day = 1;
/*      */     } 
/*      */     
/*  367 */     if (!rs.useLegacyDatetimeCode) {
/*  368 */       return TimeUtil.fastDateCreate(year, month, day, cal);
/*      */     }
/*      */     
/*  371 */     return rs.fastDateCreate((cal == null) ? rs.getCalendarInstanceForSessionOrNew() : cal, year, month, day);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract Date getNativeDate(int paramInt, ConnectionImpl paramConnectionImpl, ResultSetImpl paramResultSetImpl, Calendar paramCalendar) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object getNativeDateTimeValue(int columnIndex, byte[] bits, int offset, int length, Calendar targetCalendar, int jdbcType, int mysqlType, TimeZone tz, boolean rollForward, ConnectionImpl conn, ResultSetImpl rs) throws SQLException {
/*  383 */     int year = 0;
/*  384 */     int month = 0;
/*  385 */     int day = 0;
/*      */     
/*  387 */     int hour = 0;
/*  388 */     int minute = 0;
/*  389 */     int seconds = 0;
/*      */     
/*  391 */     int nanos = 0;
/*      */     
/*  393 */     if (bits == null)
/*      */     {
/*  395 */       return null;
/*      */     }
/*      */     
/*  398 */     Calendar sessionCalendar = conn.getUseJDBCCompliantTimezoneShift() ? conn.getUtcCalendar() : rs.getCalendarInstanceForSessionOrNew();
/*      */ 
/*      */ 
/*      */     
/*  402 */     boolean populatedFromDateTimeValue = false;
/*      */     
/*  404 */     switch (mysqlType) {
/*      */       case 7:
/*      */       case 12:
/*  407 */         populatedFromDateTimeValue = true;
/*      */         
/*  409 */         if (length != 0) {
/*  410 */           year = bits[offset + 0] & 0xFF | (bits[offset + 1] & 0xFF) << 8;
/*      */           
/*  412 */           month = bits[offset + 2];
/*  413 */           day = bits[offset + 3];
/*      */           
/*  415 */           if (length > 4) {
/*  416 */             hour = bits[offset + 4];
/*  417 */             minute = bits[offset + 5];
/*  418 */             seconds = bits[offset + 6];
/*      */           } 
/*      */           
/*  421 */           if (length > 7)
/*      */           {
/*  423 */             nanos = (bits[offset + 7] & 0xFF | (bits[offset + 8] & 0xFF) << 8 | (bits[offset + 9] & 0xFF) << 16 | (bits[offset + 10] & 0xFF) << 24) * 1000;
/*      */           }
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 10:
/*  431 */         populatedFromDateTimeValue = true;
/*      */         
/*  433 */         if (bits.length != 0) {
/*  434 */           year = bits[offset + 0] & 0xFF | (bits[offset + 1] & 0xFF) << 8;
/*      */           
/*  436 */           month = bits[offset + 2];
/*  437 */           day = bits[offset + 3];
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 11:
/*  442 */         populatedFromDateTimeValue = true;
/*      */         
/*  444 */         if (bits.length != 0) {
/*      */ 
/*      */           
/*  447 */           hour = bits[offset + 5];
/*  448 */           minute = bits[offset + 6];
/*  449 */           seconds = bits[offset + 7];
/*      */         } 
/*      */         
/*  452 */         year = 1970;
/*  453 */         month = 1;
/*  454 */         day = 1;
/*      */         break;
/*      */       
/*      */       default:
/*  458 */         populatedFromDateTimeValue = false;
/*      */         break;
/*      */     } 
/*  461 */     switch (jdbcType) {
/*      */       case 92:
/*  463 */         if (populatedFromDateTimeValue) {
/*  464 */           if (!rs.useLegacyDatetimeCode) {
/*  465 */             return TimeUtil.fastTimeCreate(hour, minute, seconds, targetCalendar, this.exceptionInterceptor);
/*      */           }
/*      */           
/*  468 */           Time time = TimeUtil.fastTimeCreate(rs.getCalendarInstanceForSessionOrNew(), hour, minute, seconds, this.exceptionInterceptor);
/*      */ 
/*      */ 
/*      */           
/*  472 */           Time adjustedTime = TimeUtil.changeTimezone(conn, sessionCalendar, targetCalendar, time, conn.getServerTimezoneTZ(), tz, rollForward);
/*      */ 
/*      */ 
/*      */           
/*  476 */           return adjustedTime;
/*      */         } 
/*      */         
/*  479 */         return rs.getNativeTimeViaParseConversion(columnIndex + 1, targetCalendar, tz, rollForward);
/*      */ 
/*      */       
/*      */       case 91:
/*  483 */         if (populatedFromDateTimeValue) {
/*  484 */           if (year == 0 && month == 0 && day == 0) {
/*  485 */             if ("convertToNull".equals(conn.getZeroDateTimeBehavior()))
/*      */             {
/*      */               
/*  488 */               return null; } 
/*  489 */             if ("exception".equals(conn.getZeroDateTimeBehavior()))
/*      */             {
/*  491 */               throw new SQLException("Value '0000-00-00' can not be represented as java.sql.Date", "S1009");
/*      */             }
/*      */ 
/*      */ 
/*      */             
/*  496 */             year = 1;
/*  497 */             month = 1;
/*  498 */             day = 1;
/*      */           } 
/*      */           
/*  501 */           if (!rs.useLegacyDatetimeCode) {
/*  502 */             return TimeUtil.fastDateCreate(year, month, day, targetCalendar);
/*      */           }
/*      */           
/*  505 */           return rs.fastDateCreate(rs.getCalendarInstanceForSessionOrNew(), year, month, day);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  511 */         return rs.getNativeDateViaParseConversion(columnIndex + 1);
/*      */       case 93:
/*  513 */         if (populatedFromDateTimeValue) {
/*  514 */           if (year == 0 && month == 0 && day == 0) {
/*  515 */             if ("convertToNull".equals(conn.getZeroDateTimeBehavior()))
/*      */             {
/*      */               
/*  518 */               return null; } 
/*  519 */             if ("exception".equals(conn.getZeroDateTimeBehavior()))
/*      */             {
/*  521 */               throw new SQLException("Value '0000-00-00' can not be represented as java.sql.Timestamp", "S1009");
/*      */             }
/*      */ 
/*      */ 
/*      */             
/*  526 */             year = 1;
/*  527 */             month = 1;
/*  528 */             day = 1;
/*      */           } 
/*      */           
/*  531 */           if (!rs.useLegacyDatetimeCode) {
/*  532 */             return TimeUtil.fastTimestampCreate(tz, year, month, day, hour, minute, seconds, nanos);
/*      */           }
/*      */ 
/*      */           
/*  536 */           Timestamp ts = rs.fastTimestampCreate(rs.getCalendarInstanceForSessionOrNew(), year, month, day, hour, minute, seconds, nanos);
/*      */ 
/*      */ 
/*      */           
/*  540 */           Timestamp adjustedTs = TimeUtil.changeTimezone(conn, sessionCalendar, targetCalendar, ts, conn.getServerTimezoneTZ(), tz, rollForward);
/*      */ 
/*      */ 
/*      */           
/*  544 */           return adjustedTs;
/*      */         } 
/*      */         
/*  547 */         return rs.getNativeTimestampViaParseConversion(columnIndex + 1, targetCalendar, tz, rollForward);
/*      */     } 
/*      */ 
/*      */     
/*  551 */     throw new SQLException("Internal error - conversion method doesn't support this type", "S1000");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract Object getNativeDateTimeValue(int paramInt1, Calendar paramCalendar, int paramInt2, int paramInt3, TimeZone paramTimeZone, boolean paramBoolean, ConnectionImpl paramConnectionImpl, ResultSetImpl paramResultSetImpl) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected double getNativeDouble(byte[] bits, int offset) {
/*  563 */     long valueAsLong = (bits[offset + 0] & 0xFF) | (bits[offset + 1] & 0xFF) << 8L | (bits[offset + 2] & 0xFF) << 16L | (bits[offset + 3] & 0xFF) << 24L | (bits[offset + 4] & 0xFF) << 32L | (bits[offset + 5] & 0xFF) << 40L | (bits[offset + 6] & 0xFF) << 48L | (bits[offset + 7] & 0xFF) << 56L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  572 */     return Double.longBitsToDouble(valueAsLong);
/*      */   }
/*      */   
/*      */   public abstract double getNativeDouble(int paramInt) throws SQLException;
/*      */   
/*      */   protected float getNativeFloat(byte[] bits, int offset) {
/*  578 */     int asInt = bits[offset + 0] & 0xFF | (bits[offset + 1] & 0xFF) << 8 | (bits[offset + 2] & 0xFF) << 16 | (bits[offset + 3] & 0xFF) << 24;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  583 */     return Float.intBitsToFloat(asInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public abstract float getNativeFloat(int paramInt) throws SQLException;
/*      */   
/*      */   protected int getNativeInt(byte[] bits, int offset) {
/*  590 */     int valueAsInt = bits[offset + 0] & 0xFF | (bits[offset + 1] & 0xFF) << 8 | (bits[offset + 2] & 0xFF) << 16 | (bits[offset + 3] & 0xFF) << 24;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  595 */     return valueAsInt;
/*      */   }
/*      */   
/*      */   public abstract int getNativeInt(int paramInt) throws SQLException;
/*      */   
/*      */   protected long getNativeLong(byte[] bits, int offset) {
/*  601 */     long valueAsLong = (bits[offset + 0] & 0xFF) | (bits[offset + 1] & 0xFF) << 8L | (bits[offset + 2] & 0xFF) << 16L | (bits[offset + 3] & 0xFF) << 24L | (bits[offset + 4] & 0xFF) << 32L | (bits[offset + 5] & 0xFF) << 40L | (bits[offset + 6] & 0xFF) << 48L | (bits[offset + 7] & 0xFF) << 56L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  610 */     return valueAsLong;
/*      */   }
/*      */   
/*      */   public abstract long getNativeLong(int paramInt) throws SQLException;
/*      */   
/*      */   protected short getNativeShort(byte[] bits, int offset) {
/*  616 */     short asShort = (short)(bits[offset + 0] & 0xFF | (bits[offset + 1] & 0xFF) << 8);
/*      */     
/*  618 */     return asShort;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract short getNativeShort(int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */   
/*      */   protected Time getNativeTime(int columnIndex, byte[] bits, int offset, int length, Calendar targetCalendar, TimeZone tz, boolean rollForward, ConnectionImpl conn, ResultSetImpl rs) throws SQLException {
/*  628 */     int hour = 0;
/*  629 */     int minute = 0;
/*  630 */     int seconds = 0;
/*      */     
/*  632 */     if (length != 0) {
/*      */ 
/*      */       
/*  635 */       hour = bits[offset + 5];
/*  636 */       minute = bits[offset + 6];
/*  637 */       seconds = bits[offset + 7];
/*      */     } 
/*      */     
/*  640 */     if (!rs.useLegacyDatetimeCode) {
/*  641 */       return TimeUtil.fastTimeCreate(hour, minute, seconds, targetCalendar, this.exceptionInterceptor);
/*      */     }
/*      */     
/*  644 */     Calendar sessionCalendar = rs.getCalendarInstanceForSessionOrNew();
/*      */     
/*  646 */     synchronized (sessionCalendar) {
/*  647 */       Time time = TimeUtil.fastTimeCreate(sessionCalendar, hour, minute, seconds, this.exceptionInterceptor);
/*      */ 
/*      */       
/*  650 */       Time adjustedTime = TimeUtil.changeTimezone(conn, sessionCalendar, targetCalendar, time, conn.getServerTimezoneTZ(), tz, rollForward);
/*      */ 
/*      */ 
/*      */       
/*  654 */       return adjustedTime;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract Time getNativeTime(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean, ConnectionImpl paramConnectionImpl, ResultSetImpl paramResultSetImpl) throws SQLException;
/*      */ 
/*      */ 
/*      */   
/*      */   protected Timestamp getNativeTimestamp(byte[] bits, int offset, int length, Calendar targetCalendar, TimeZone tz, boolean rollForward, ConnectionImpl conn, ResultSetImpl rs) throws SQLException {
/*  665 */     int year = 0;
/*  666 */     int month = 0;
/*  667 */     int day = 0;
/*      */     
/*  669 */     int hour = 0;
/*  670 */     int minute = 0;
/*  671 */     int seconds = 0;
/*      */     
/*  673 */     int nanos = 0;
/*      */     
/*  675 */     if (length != 0) {
/*  676 */       year = bits[offset + 0] & 0xFF | (bits[offset + 1] & 0xFF) << 8;
/*  677 */       month = bits[offset + 2];
/*  678 */       day = bits[offset + 3];
/*      */       
/*  680 */       if (length > 4) {
/*  681 */         hour = bits[offset + 4];
/*  682 */         minute = bits[offset + 5];
/*  683 */         seconds = bits[offset + 6];
/*      */       } 
/*      */       
/*  686 */       if (length > 7)
/*      */       {
/*  688 */         nanos = (bits[offset + 7] & 0xFF | (bits[offset + 8] & 0xFF) << 8 | (bits[offset + 9] & 0xFF) << 16 | (bits[offset + 10] & 0xFF) << 24) * 1000;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  694 */     if (year == 0 && month == 0 && day == 0) {
/*  695 */       if ("convertToNull".equals(conn.getZeroDateTimeBehavior()))
/*      */       {
/*      */         
/*  698 */         return null; } 
/*  699 */       if ("exception".equals(conn.getZeroDateTimeBehavior()))
/*      */       {
/*  701 */         throw SQLError.createSQLException("Value '0000-00-00' can not be represented as java.sql.Timestamp", "S1009", this.exceptionInterceptor);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  707 */       year = 1;
/*  708 */       month = 1;
/*  709 */       day = 1;
/*      */     } 
/*      */     
/*  712 */     if (!rs.useLegacyDatetimeCode) {
/*  713 */       return TimeUtil.fastTimestampCreate(tz, year, month, day, hour, minute, seconds, nanos);
/*      */     }
/*      */ 
/*      */     
/*  717 */     Calendar sessionCalendar = conn.getUseJDBCCompliantTimezoneShift() ? conn.getUtcCalendar() : rs.getCalendarInstanceForSessionOrNew();
/*      */ 
/*      */ 
/*      */     
/*  721 */     synchronized (sessionCalendar) {
/*  722 */       Timestamp ts = rs.fastTimestampCreate(sessionCalendar, year, month, day, hour, minute, seconds, nanos);
/*      */ 
/*      */       
/*  725 */       Timestamp adjustedTs = TimeUtil.changeTimezone(conn, sessionCalendar, targetCalendar, ts, conn.getServerTimezoneTZ(), tz, rollForward);
/*      */ 
/*      */ 
/*      */       
/*  729 */       return adjustedTs;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract Timestamp getNativeTimestamp(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean, ConnectionImpl paramConnectionImpl, ResultSetImpl paramResultSetImpl) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract Reader getReader(int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract String getString(int paramInt, String paramString, ConnectionImpl paramConnectionImpl) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getString(String encoding, ConnectionImpl conn, byte[] value, int offset, int length) throws SQLException {
/*  783 */     String stringVal = null;
/*      */     
/*  785 */     if (conn != null && conn.getUseUnicode()) {
/*      */       try {
/*  787 */         if (encoding == null) {
/*  788 */           stringVal = new String(value);
/*      */         } else {
/*  790 */           SingleByteCharsetConverter converter = conn.getCharsetConverter(encoding);
/*      */ 
/*      */           
/*  793 */           if (converter != null) {
/*  794 */             stringVal = converter.toString(value, offset, length);
/*      */           } else {
/*  796 */             stringVal = new String(value, offset, length, encoding);
/*      */           } 
/*      */         } 
/*  799 */       } catch (UnsupportedEncodingException E) {
/*  800 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Unsupported_character_encoding____101") + encoding + "'.", "0S100", this.exceptionInterceptor);
/*      */       
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  807 */       stringVal = StringUtils.toAsciiString(value, offset, length);
/*      */     } 
/*      */     
/*  810 */     return stringVal;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Time getTimeFast(int columnIndex, byte[] timeAsBytes, int offset, int length, Calendar targetCalendar, TimeZone tz, boolean rollForward, ConnectionImpl conn, ResultSetImpl rs) throws SQLException {
/*  818 */     int hr = 0;
/*  819 */     int min = 0;
/*  820 */     int sec = 0;
/*      */ 
/*      */     
/*      */     try {
/*  824 */       if (timeAsBytes == null) {
/*  825 */         return null;
/*      */       }
/*      */       
/*  828 */       boolean allZeroTime = true;
/*  829 */       boolean onlyTimePresent = false;
/*      */       int i;
/*  831 */       for (i = 0; i < length; i++) {
/*  832 */         if (timeAsBytes[offset + i] == 58) {
/*  833 */           onlyTimePresent = true;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*  838 */       for (i = 0; i < length; i++) {
/*  839 */         byte b = timeAsBytes[offset + i];
/*      */         
/*  841 */         if (b == 32 || b == 45 || b == 47) {
/*  842 */           onlyTimePresent = false;
/*      */         }
/*      */         
/*  845 */         if (b != 48 && b != 32 && b != 58 && b != 45 && b != 47 && b != 46) {
/*      */           
/*  847 */           allZeroTime = false;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */       
/*  853 */       if (!onlyTimePresent && allZeroTime) {
/*  854 */         if ("convertToNull".equals(conn.getZeroDateTimeBehavior()))
/*      */         {
/*  856 */           return null; } 
/*  857 */         if ("exception".equals(conn.getZeroDateTimeBehavior()))
/*      */         {
/*  859 */           throw SQLError.createSQLException("Value '" + new String(timeAsBytes) + "' can not be represented as java.sql.Time", "S1009", this.exceptionInterceptor);
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  867 */         return rs.fastTimeCreate(targetCalendar, 0, 0, 0);
/*      */       } 
/*      */       
/*  870 */       Field timeColField = this.metadata[columnIndex];
/*      */       
/*  872 */       if (timeColField.getMysqlType() == 7) {
/*      */         
/*  874 */         switch (length) {
/*      */           
/*      */           case 19:
/*  877 */             hr = StringUtils.getInt(timeAsBytes, offset + length - 8, offset + length - 6);
/*      */             
/*  879 */             min = StringUtils.getInt(timeAsBytes, offset + length - 5, offset + length - 3);
/*      */             
/*  881 */             sec = StringUtils.getInt(timeAsBytes, offset + length - 2, offset + length);
/*      */             break;
/*      */ 
/*      */ 
/*      */           
/*      */           case 12:
/*      */           case 14:
/*  888 */             hr = StringUtils.getInt(timeAsBytes, offset + length - 6, offset + length - 4);
/*      */             
/*  890 */             min = StringUtils.getInt(timeAsBytes, offset + length - 4, offset + length - 2);
/*      */             
/*  892 */             sec = StringUtils.getInt(timeAsBytes, offset + length - 2, offset + length);
/*      */             break;
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           case 10:
/*  899 */             hr = StringUtils.getInt(timeAsBytes, offset + 6, offset + 8);
/*      */             
/*  901 */             min = StringUtils.getInt(timeAsBytes, offset + 8, offset + 10);
/*      */             
/*  903 */             sec = 0;
/*      */             break;
/*      */ 
/*      */ 
/*      */           
/*      */           default:
/*  909 */             throw SQLError.createSQLException(Messages.getString("ResultSet.Timestamp_too_small_to_convert_to_Time_value_in_column__257") + (columnIndex + 1) + "(" + timeColField + ").", "S1009", this.exceptionInterceptor);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  919 */         SQLWarning precisionLost = new SQLWarning(Messages.getString("ResultSet.Precision_lost_converting_TIMESTAMP_to_Time_with_getTime()_on_column__261") + columnIndex + "(" + timeColField + ").");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*  928 */       else if (timeColField.getMysqlType() == 12) {
/*  929 */         hr = StringUtils.getInt(timeAsBytes, offset + 11, offset + 13);
/*  930 */         min = StringUtils.getInt(timeAsBytes, offset + 14, offset + 16);
/*  931 */         sec = StringUtils.getInt(timeAsBytes, offset + 17, offset + 19);
/*      */         
/*  933 */         SQLWarning precisionLost = new SQLWarning(Messages.getString("ResultSet.Precision_lost_converting_DATETIME_to_Time_with_getTime()_on_column__264") + (columnIndex + 1) + "(" + timeColField + ").");
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  943 */         if (timeColField.getMysqlType() == 10) {
/*  944 */           return rs.fastTimeCreate(null, 0, 0, 0);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  949 */         if (length != 5 && length != 8) {
/*  950 */           throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Time____267") + new String(timeAsBytes) + Messages.getString("ResultSet.___in_column__268") + (columnIndex + 1), "S1009", this.exceptionInterceptor);
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  958 */         hr = StringUtils.getInt(timeAsBytes, offset + 0, offset + 2);
/*  959 */         min = StringUtils.getInt(timeAsBytes, offset + 3, offset + 5);
/*  960 */         sec = (length == 5) ? 0 : StringUtils.getInt(timeAsBytes, offset + 6, offset + 8);
/*      */       } 
/*      */ 
/*      */       
/*  964 */       Calendar sessionCalendar = rs.getCalendarInstanceForSessionOrNew();
/*      */       
/*  966 */       if (!rs.useLegacyDatetimeCode) {
/*  967 */         return rs.fastTimeCreate(targetCalendar, hr, min, sec);
/*      */       }
/*      */       
/*  970 */       synchronized (sessionCalendar) {
/*  971 */         return TimeUtil.changeTimezone(conn, sessionCalendar, targetCalendar, rs.fastTimeCreate(sessionCalendar, hr, min, sec), conn.getServerTimezoneTZ(), tz, rollForward);
/*      */       
/*      */       }
/*      */     
/*      */     }
/*  976 */     catch (Exception ex) {
/*  977 */       SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", this.exceptionInterceptor);
/*      */       
/*  979 */       sqlEx.initCause(ex);
/*      */       
/*  981 */       throw sqlEx;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract Time getTimeFast(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean, ConnectionImpl paramConnectionImpl, ResultSetImpl paramResultSetImpl) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Timestamp getTimestampFast(int columnIndex, byte[] timestampAsBytes, int offset, int length, Calendar targetCalendar, TimeZone tz, boolean rollForward, ConnectionImpl conn, ResultSetImpl rs) throws SQLException {
/*      */     try {
/*  995 */       Calendar sessionCalendar = conn.getUseJDBCCompliantTimezoneShift() ? conn.getUtcCalendar() : rs.getCalendarInstanceForSessionOrNew();
/*      */ 
/*      */ 
/*      */       
/*  999 */       synchronized (sessionCalendar) {
/* 1000 */         boolean hasDash, hasColon; int j; boolean allZeroTimestamp = true;
/*      */         
/* 1002 */         boolean onlyTimePresent = false;
/*      */         int i;
/* 1004 */         for (i = 0; i < length; i++) {
/* 1005 */           if (timestampAsBytes[offset + i] == 58) {
/* 1006 */             onlyTimePresent = true;
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/* 1011 */         for (i = 0; i < length; i++) {
/* 1012 */           byte b = timestampAsBytes[offset + i];
/*      */           
/* 1014 */           if (b == 32 || b == 45 || b == 47) {
/* 1015 */             onlyTimePresent = false;
/*      */           }
/*      */           
/* 1018 */           if (b != 48 && b != 32 && b != 58 && b != 45 && b != 47 && b != 46) {
/*      */             
/* 1020 */             allZeroTimestamp = false;
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */         
/* 1026 */         if (!onlyTimePresent && allZeroTimestamp) {
/*      */           
/* 1028 */           if ("convertToNull".equals(conn.getZeroDateTimeBehavior()))
/*      */           {
/*      */             
/* 1031 */             return null; } 
/* 1032 */           if ("exception".equals(conn.getZeroDateTimeBehavior()))
/*      */           {
/* 1034 */             throw SQLError.createSQLException("Value '" + timestampAsBytes + "' can not be represented as java.sql.Timestamp", "S1009", this.exceptionInterceptor);
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1042 */           if (!rs.useLegacyDatetimeCode) {
/* 1043 */             return TimeUtil.fastTimestampCreate(tz, 1, 1, 1, 0, 0, 0, 0);
/*      */           }
/*      */ 
/*      */           
/* 1047 */           return rs.fastTimestampCreate(null, 1, 1, 1, 0, 0, 0, 0);
/*      */         } 
/* 1049 */         if (this.metadata[columnIndex].getMysqlType() == 13) {
/*      */           
/* 1051 */           if (!rs.useLegacyDatetimeCode) {
/* 1052 */             return TimeUtil.fastTimestampCreate(tz, StringUtils.getInt(timestampAsBytes, offset, 4), 1, 1, 0, 0, 0, 0);
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 1057 */           return TimeUtil.changeTimezone(conn, sessionCalendar, targetCalendar, rs.fastTimestampCreate(sessionCalendar, StringUtils.getInt(timestampAsBytes, offset, 4), 1, 1, 0, 0, 0, 0), conn.getServerTimezoneTZ(), tz, rollForward);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1064 */         if (timestampAsBytes[offset + length - 1] == 46) {
/* 1065 */           length--;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 1070 */         int year = 0;
/* 1071 */         int month = 0;
/* 1072 */         int day = 0;
/* 1073 */         int hour = 0;
/* 1074 */         int minutes = 0;
/* 1075 */         int seconds = 0;
/* 1076 */         int nanos = 0;
/*      */         
/* 1078 */         switch (length) {
/*      */           case 19:
/*      */           case 20:
/*      */           case 21:
/*      */           case 22:
/*      */           case 23:
/*      */           case 24:
/*      */           case 25:
/*      */           case 26:
/*      */           case 29:
/* 1088 */             year = StringUtils.getInt(timestampAsBytes, offset + 0, offset + 4);
/*      */             
/* 1090 */             month = StringUtils.getInt(timestampAsBytes, offset + 5, offset + 7);
/*      */             
/* 1092 */             day = StringUtils.getInt(timestampAsBytes, offset + 8, offset + 10);
/*      */             
/* 1094 */             hour = StringUtils.getInt(timestampAsBytes, offset + 11, offset + 13);
/*      */             
/* 1096 */             minutes = StringUtils.getInt(timestampAsBytes, offset + 14, offset + 16);
/*      */             
/* 1098 */             seconds = StringUtils.getInt(timestampAsBytes, offset + 17, offset + 19);
/*      */ 
/*      */             
/* 1101 */             nanos = 0;
/*      */             
/* 1103 */             if (length > 19) {
/* 1104 */               int decimalIndex = -1;
/*      */               
/* 1106 */               for (int k = 0; k < length; k++) {
/* 1107 */                 if (timestampAsBytes[offset + k] == 46) {
/* 1108 */                   decimalIndex = k;
/*      */                 }
/*      */               } 
/*      */               
/* 1112 */               if (decimalIndex != -1) {
/* 1113 */                 if (decimalIndex + 2 <= length) {
/* 1114 */                   nanos = StringUtils.getInt(timestampAsBytes, decimalIndex + 1, offset + length);
/*      */ 
/*      */ 
/*      */                   
/* 1118 */                   int numDigits = offset + length - decimalIndex + 1;
/*      */                   
/* 1120 */                   if (numDigits < 9) {
/* 1121 */                     int factor = (int)Math.pow(10.0D, (9 - numDigits));
/* 1122 */                     nanos *= factor;
/*      */                   }  break;
/*      */                 } 
/* 1125 */                 throw new IllegalArgumentException();
/*      */               } 
/*      */             } 
/*      */             break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           case 14:
/* 1139 */             year = StringUtils.getInt(timestampAsBytes, offset + 0, offset + 4);
/*      */             
/* 1141 */             month = StringUtils.getInt(timestampAsBytes, offset + 4, offset + 6);
/*      */             
/* 1143 */             day = StringUtils.getInt(timestampAsBytes, offset + 6, offset + 8);
/*      */             
/* 1145 */             hour = StringUtils.getInt(timestampAsBytes, offset + 8, offset + 10);
/*      */             
/* 1147 */             minutes = StringUtils.getInt(timestampAsBytes, offset + 10, offset + 12);
/*      */             
/* 1149 */             seconds = StringUtils.getInt(timestampAsBytes, offset + 12, offset + 14);
/*      */             break;
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           case 12:
/* 1156 */             year = StringUtils.getInt(timestampAsBytes, offset + 0, offset + 2);
/*      */ 
/*      */             
/* 1159 */             if (year <= 69) {
/* 1160 */               year += 100;
/*      */             }
/*      */             
/* 1163 */             year += 1900;
/*      */             
/* 1165 */             month = StringUtils.getInt(timestampAsBytes, offset + 2, offset + 4);
/*      */             
/* 1167 */             day = StringUtils.getInt(timestampAsBytes, offset + 4, offset + 6);
/*      */             
/* 1169 */             hour = StringUtils.getInt(timestampAsBytes, offset + 6, offset + 8);
/*      */             
/* 1171 */             minutes = StringUtils.getInt(timestampAsBytes, offset + 8, offset + 10);
/*      */             
/* 1173 */             seconds = StringUtils.getInt(timestampAsBytes, offset + 10, offset + 12);
/*      */             break;
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           case 10:
/* 1180 */             hasDash = false;
/*      */             
/* 1182 */             for (j = 0; j < length; j++) {
/* 1183 */               if (timestampAsBytes[offset + j] == 45) {
/* 1184 */                 hasDash = true;
/*      */                 
/*      */                 break;
/*      */               } 
/*      */             } 
/* 1189 */             if (this.metadata[columnIndex].getMysqlType() == 10 || hasDash) {
/*      */               
/* 1191 */               year = StringUtils.getInt(timestampAsBytes, offset + 0, offset + 4);
/*      */               
/* 1193 */               month = StringUtils.getInt(timestampAsBytes, offset + 5, offset + 7);
/*      */               
/* 1195 */               day = StringUtils.getInt(timestampAsBytes, offset + 8, offset + 10);
/*      */               
/* 1197 */               hour = 0;
/* 1198 */               minutes = 0; break;
/*      */             } 
/* 1200 */             year = StringUtils.getInt(timestampAsBytes, offset + 0, offset + 2);
/*      */ 
/*      */             
/* 1203 */             if (year <= 69) {
/* 1204 */               year += 100;
/*      */             }
/*      */             
/* 1207 */             month = StringUtils.getInt(timestampAsBytes, offset + 2, offset + 4);
/*      */             
/* 1209 */             day = StringUtils.getInt(timestampAsBytes, offset + 4, offset + 6);
/*      */             
/* 1211 */             hour = StringUtils.getInt(timestampAsBytes, offset + 6, offset + 8);
/*      */             
/* 1213 */             minutes = StringUtils.getInt(timestampAsBytes, offset + 8, offset + 10);
/*      */ 
/*      */             
/* 1216 */             year += 1900;
/*      */             break;
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           case 8:
/* 1223 */             hasColon = false;
/*      */             
/* 1225 */             for (j = 0; j < length; j++) {
/* 1226 */               if (timestampAsBytes[offset + j] == 58) {
/* 1227 */                 hasColon = true;
/*      */                 
/*      */                 break;
/*      */               } 
/*      */             } 
/* 1232 */             if (hasColon) {
/* 1233 */               hour = StringUtils.getInt(timestampAsBytes, offset + 0, offset + 2);
/*      */               
/* 1235 */               minutes = StringUtils.getInt(timestampAsBytes, offset + 3, offset + 5);
/*      */               
/* 1237 */               seconds = StringUtils.getInt(timestampAsBytes, offset + 6, offset + 8);
/*      */ 
/*      */               
/* 1240 */               year = 1970;
/* 1241 */               month = 1;
/* 1242 */               day = 1;
/*      */               
/*      */               break;
/*      */             } 
/*      */             
/* 1247 */             year = StringUtils.getInt(timestampAsBytes, offset + 0, offset + 4);
/*      */             
/* 1249 */             month = StringUtils.getInt(timestampAsBytes, offset + 4, offset + 6);
/*      */             
/* 1251 */             day = StringUtils.getInt(timestampAsBytes, offset + 6, offset + 8);
/*      */ 
/*      */             
/* 1254 */             year -= 1900;
/* 1255 */             month--;
/*      */             break;
/*      */ 
/*      */ 
/*      */           
/*      */           case 6:
/* 1261 */             year = StringUtils.getInt(timestampAsBytes, offset + 0, offset + 2);
/*      */ 
/*      */             
/* 1264 */             if (year <= 69) {
/* 1265 */               year += 100;
/*      */             }
/*      */             
/* 1268 */             year += 1900;
/*      */             
/* 1270 */             month = StringUtils.getInt(timestampAsBytes, offset + 2, offset + 4);
/*      */             
/* 1272 */             day = StringUtils.getInt(timestampAsBytes, offset + 4, offset + 6);
/*      */             break;
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           case 4:
/* 1279 */             year = StringUtils.getInt(timestampAsBytes, offset + 0, offset + 2);
/*      */ 
/*      */             
/* 1282 */             if (year <= 69) {
/* 1283 */               year += 100;
/*      */             }
/*      */             
/* 1286 */             month = StringUtils.getInt(timestampAsBytes, offset + 2, offset + 4);
/*      */ 
/*      */             
/* 1289 */             day = 1;
/*      */             break;
/*      */ 
/*      */ 
/*      */           
/*      */           case 2:
/* 1295 */             year = StringUtils.getInt(timestampAsBytes, offset + 0, offset + 2);
/*      */ 
/*      */             
/* 1298 */             if (year <= 69) {
/* 1299 */               year += 100;
/*      */             }
/*      */             
/* 1302 */             year += 1900;
/* 1303 */             month = 1;
/* 1304 */             day = 1;
/*      */             break;
/*      */ 
/*      */ 
/*      */           
/*      */           default:
/* 1310 */             throw new SQLException("Bad format for Timestamp '" + new String(timestampAsBytes) + "' in column " + (columnIndex + 1) + ".", "S1009");
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1318 */         if (!rs.useLegacyDatetimeCode) {
/* 1319 */           return TimeUtil.fastTimestampCreate(tz, year, month, day, hour, minutes, seconds, nanos);
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1325 */         return TimeUtil.changeTimezone(conn, sessionCalendar, targetCalendar, rs.fastTimestampCreate(sessionCalendar, year, month, day, hour, minutes, seconds, nanos), conn.getServerTimezoneTZ(), tz, rollForward);
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 1335 */     catch (Exception e) {
/* 1336 */       SQLException sqlEx = SQLError.createSQLException("Cannot convert value '" + getString(columnIndex, "ISO8859_1", conn) + "' from column " + (columnIndex + 1) + " to TIMESTAMP.", "S1009", this.exceptionInterceptor);
/*      */ 
/*      */ 
/*      */       
/* 1340 */       sqlEx.initCause(e);
/*      */       
/* 1342 */       throw sqlEx;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract Timestamp getTimestampFast(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean, ConnectionImpl paramConnectionImpl, ResultSetImpl paramResultSetImpl) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract boolean isFloatingPointNumber(int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract boolean isNull(int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract long length(int paramInt) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void setColumnValue(int paramInt, byte[] paramArrayOfbyte) throws SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSetRow setMetadata(Field[] f) throws SQLException {
/* 1410 */     this.metadata = f;
/*      */     
/* 1412 */     return this;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\ResultSetRow.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */