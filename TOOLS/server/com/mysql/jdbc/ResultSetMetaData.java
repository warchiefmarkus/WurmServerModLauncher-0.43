/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.sql.ResultSetMetaData;
/*     */ import java.sql.SQLException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResultSetMetaData
/*     */   implements ResultSetMetaData
/*     */ {
/*     */   Field[] fields;
/*     */   
/*     */   private static int clampedGetLength(Field f) {
/*  40 */     long fieldLength = f.getLength();
/*     */     
/*  42 */     if (fieldLength > 2147483647L) {
/*  43 */       fieldLength = 2147483647L;
/*     */     }
/*     */     
/*  46 */     return (int)fieldLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final boolean isDecimalType(int type) {
/*  58 */     switch (type) {
/*     */       case -7:
/*     */       case -6:
/*     */       case -5:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/*     */       case 8:
/*  69 */         return true;
/*     */     } 
/*     */     
/*  72 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean useOldAliasBehavior = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private ExceptionInterceptor exceptionInterceptor;
/*     */ 
/*     */ 
/*     */   
/*     */   public ResultSetMetaData(Field[] fields, boolean useOldAliasBehavior, ExceptionInterceptor exceptionInterceptor) {
/*  87 */     this.fields = fields;
/*  88 */     this.useOldAliasBehavior = useOldAliasBehavior;
/*  89 */     this.exceptionInterceptor = exceptionInterceptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCatalogName(int column) throws SQLException {
/* 104 */     Field f = getField(column);
/*     */     
/* 106 */     String database = f.getDatabaseName();
/*     */     
/* 108 */     return (database == null) ? "" : database;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getColumnCharacterEncoding(int column) throws SQLException {
/* 125 */     String mysqlName = getColumnCharacterSet(column);
/*     */     
/* 127 */     String javaName = null;
/*     */     
/* 129 */     if (mysqlName != null) {
/* 130 */       javaName = CharsetMapping.getJavaEncodingForMysqlEncoding(mysqlName, null);
/*     */     }
/*     */ 
/*     */     
/* 134 */     return javaName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getColumnCharacterSet(int column) throws SQLException {
/* 149 */     return getField(column).getCharacterSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getColumnClassName(int column) throws SQLException {
/* 175 */     Field f = getField(column);
/*     */     
/* 177 */     return getClassNameForJavaType(f.getSQLType(), f.isUnsigned(), f.getMysqlType(), (f.isBinary() || f.isBlob()), f.isOpaqueBinary());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumnCount() throws SQLException {
/* 193 */     return this.fields.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumnDisplaySize(int column) throws SQLException {
/* 208 */     Field f = getField(column);
/*     */     
/* 210 */     int lengthInBytes = clampedGetLength(f);
/*     */     
/* 212 */     return lengthInBytes / f.getMaxBytesPerCharacter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getColumnLabel(int column) throws SQLException {
/* 227 */     if (this.useOldAliasBehavior) {
/* 228 */       return getColumnName(column);
/*     */     }
/*     */     
/* 231 */     return getField(column).getColumnLabel();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getColumnName(int column) throws SQLException {
/* 246 */     if (this.useOldAliasBehavior) {
/* 247 */       return getField(column).getName();
/*     */     }
/*     */     
/* 250 */     String name = getField(column).getNameNoAliases();
/*     */     
/* 252 */     if (name != null && name.length() == 0) {
/* 253 */       return getField(column).getName();
/*     */     }
/*     */     
/* 256 */     return name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumnType(int column) throws SQLException {
/* 273 */     return getField(column).getSQLType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getColumnTypeName(int column) throws SQLException {
/* 288 */     Field field = getField(column);
/*     */     
/* 290 */     int mysqlType = field.getMysqlType();
/* 291 */     int jdbcType = field.getSQLType();
/*     */     
/* 293 */     switch (mysqlType) {
/*     */       case 16:
/* 295 */         return "BIT";
/*     */       case 0:
/*     */       case 246:
/* 298 */         return field.isUnsigned() ? "DECIMAL UNSIGNED" : "DECIMAL";
/*     */       
/*     */       case 1:
/* 301 */         return field.isUnsigned() ? "TINYINT UNSIGNED" : "TINYINT";
/*     */       
/*     */       case 2:
/* 304 */         return field.isUnsigned() ? "SMALLINT UNSIGNED" : "SMALLINT";
/*     */       
/*     */       case 3:
/* 307 */         return field.isUnsigned() ? "INT UNSIGNED" : "INT";
/*     */       
/*     */       case 4:
/* 310 */         return field.isUnsigned() ? "FLOAT UNSIGNED" : "FLOAT";
/*     */       
/*     */       case 5:
/* 313 */         return field.isUnsigned() ? "DOUBLE UNSIGNED" : "DOUBLE";
/*     */       
/*     */       case 6:
/* 316 */         return "NULL";
/*     */       
/*     */       case 7:
/* 319 */         return "TIMESTAMP";
/*     */       
/*     */       case 8:
/* 322 */         return field.isUnsigned() ? "BIGINT UNSIGNED" : "BIGINT";
/*     */       
/*     */       case 9:
/* 325 */         return field.isUnsigned() ? "MEDIUMINT UNSIGNED" : "MEDIUMINT";
/*     */       
/*     */       case 10:
/* 328 */         return "DATE";
/*     */       
/*     */       case 11:
/* 331 */         return "TIME";
/*     */       
/*     */       case 12:
/* 334 */         return "DATETIME";
/*     */       
/*     */       case 249:
/* 337 */         return "TINYBLOB";
/*     */       
/*     */       case 250:
/* 340 */         return "MEDIUMBLOB";
/*     */       
/*     */       case 251:
/* 343 */         return "LONGBLOB";
/*     */       
/*     */       case 252:
/* 346 */         if (getField(column).isBinary()) {
/* 347 */           return "BLOB";
/*     */         }
/*     */         
/* 350 */         return "TEXT";
/*     */       
/*     */       case 15:
/* 353 */         return "VARCHAR";
/*     */       
/*     */       case 253:
/* 356 */         if (jdbcType == -3) {
/* 357 */           return "VARBINARY";
/*     */         }
/*     */         
/* 360 */         return "VARCHAR";
/*     */       
/*     */       case 254:
/* 363 */         if (jdbcType == -2) {
/* 364 */           return "BINARY";
/*     */         }
/*     */         
/* 367 */         return "CHAR";
/*     */       
/*     */       case 247:
/* 370 */         return "ENUM";
/*     */       
/*     */       case 13:
/* 373 */         return "YEAR";
/*     */       
/*     */       case 248:
/* 376 */         return "SET";
/*     */       
/*     */       case 255:
/* 379 */         return "GEOMETRY";
/*     */     } 
/*     */     
/* 382 */     return "UNKNOWN";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Field getField(int columnIndex) throws SQLException {
/* 398 */     if (columnIndex < 1 || columnIndex > this.fields.length) {
/* 399 */       throw SQLError.createSQLException(Messages.getString("ResultSetMetaData.46"), "S1002", this.exceptionInterceptor);
/*     */     }
/*     */ 
/*     */     
/* 403 */     return this.fields[columnIndex - 1];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPrecision(int column) throws SQLException {
/* 418 */     Field f = getField(column);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 424 */     if (isDecimalType(f.getSQLType())) {
/* 425 */       if (f.getDecimals() > 0) {
/* 426 */         return clampedGetLength(f) - 1 + f.getPrecisionAdjustFactor();
/*     */       }
/*     */       
/* 429 */       return clampedGetLength(f) + f.getPrecisionAdjustFactor();
/*     */     } 
/*     */     
/* 432 */     switch (f.getMysqlType()) {
/*     */       case 249:
/*     */       case 250:
/*     */       case 251:
/*     */       case 252:
/* 437 */         return clampedGetLength(f);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 444 */     return clampedGetLength(f) / f.getMaxBytesPerCharacter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getScale(int column) throws SQLException {
/* 461 */     Field f = getField(column);
/*     */     
/* 463 */     if (isDecimalType(f.getSQLType())) {
/* 464 */       return f.getDecimals();
/*     */     }
/*     */     
/* 467 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSchemaName(int column) throws SQLException {
/* 484 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTableName(int column) throws SQLException {
/* 499 */     if (this.useOldAliasBehavior) {
/* 500 */       return getField(column).getTableName();
/*     */     }
/*     */     
/* 503 */     return getField(column).getTableNameNoAliases();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutoIncrement(int column) throws SQLException {
/* 518 */     Field f = getField(column);
/*     */     
/* 520 */     return f.isAutoIncrement();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCaseSensitive(int column) throws SQLException {
/*     */     String collationName;
/* 535 */     Field field = getField(column);
/*     */     
/* 537 */     int sqlType = field.getSQLType();
/*     */     
/* 539 */     switch (sqlType) {
/*     */       case -7:
/*     */       case -6:
/*     */       case -5:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/*     */       case 8:
/*     */       case 91:
/*     */       case 92:
/*     */       case 93:
/* 551 */         return false;
/*     */ 
/*     */       
/*     */       case -1:
/*     */       case 1:
/*     */       case 12:
/* 557 */         if (field.isBinary()) {
/* 558 */           return true;
/*     */         }
/*     */         
/* 561 */         collationName = field.getCollation();
/*     */         
/* 563 */         return (collationName != null && !collationName.endsWith("_ci"));
/*     */     } 
/*     */     
/* 566 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCurrency(int column) throws SQLException {
/* 582 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDefinitelyWritable(int column) throws SQLException {
/* 597 */     return isWritable(column);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int isNullable(int column) throws SQLException {
/* 612 */     if (!getField(column).isNotNull()) {
/* 613 */       return 1;
/*     */     }
/*     */     
/* 616 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReadOnly(int column) throws SQLException {
/* 631 */     return getField(column).isReadOnly();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSearchable(int column) throws SQLException {
/* 650 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSigned(int column) throws SQLException {
/* 665 */     Field f = getField(column);
/* 666 */     int sqlType = f.getSQLType();
/*     */     
/* 668 */     switch (sqlType) {
/*     */       case -6:
/*     */       case -5:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/*     */       case 8:
/* 678 */         return !f.isUnsigned();
/*     */       
/*     */       case 91:
/*     */       case 92:
/*     */       case 93:
/* 683 */         return false;
/*     */     } 
/*     */     
/* 686 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWritable(int column) throws SQLException {
/* 702 */     return !isReadOnly(column);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 711 */     StringBuffer toStringBuf = new StringBuffer();
/* 712 */     toStringBuf.append(super.toString());
/* 713 */     toStringBuf.append(" - Field level information: ");
/*     */     
/* 715 */     for (int i = 0; i < this.fields.length; i++) {
/* 716 */       toStringBuf.append("\n\t");
/* 717 */       toStringBuf.append(this.fields[i].toString());
/*     */     } 
/*     */     
/* 720 */     return toStringBuf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String getClassNameForJavaType(int javaType, boolean isUnsigned, int mysqlTypeIfKnown, boolean isBinaryOrBlob, boolean isOpaqueBinary) {
/* 727 */     switch (javaType) {
/*     */       case -7:
/*     */       case 16:
/* 730 */         return "java.lang.Boolean";
/*     */ 
/*     */       
/*     */       case -6:
/* 734 */         if (isUnsigned) {
/* 735 */           return "java.lang.Integer";
/*     */         }
/*     */         
/* 738 */         return "java.lang.Integer";
/*     */ 
/*     */       
/*     */       case 5:
/* 742 */         if (isUnsigned) {
/* 743 */           return "java.lang.Integer";
/*     */         }
/*     */         
/* 746 */         return "java.lang.Integer";
/*     */ 
/*     */       
/*     */       case 4:
/* 750 */         if (!isUnsigned || mysqlTypeIfKnown == 9)
/*     */         {
/* 752 */           return "java.lang.Integer";
/*     */         }
/*     */         
/* 755 */         return "java.lang.Long";
/*     */ 
/*     */       
/*     */       case -5:
/* 759 */         if (!isUnsigned) {
/* 760 */           return "java.lang.Long";
/*     */         }
/*     */         
/* 763 */         return "java.math.BigInteger";
/*     */       
/*     */       case 2:
/*     */       case 3:
/* 767 */         return "java.math.BigDecimal";
/*     */       
/*     */       case 7:
/* 770 */         return "java.lang.Float";
/*     */       
/*     */       case 6:
/*     */       case 8:
/* 774 */         return "java.lang.Double";
/*     */       
/*     */       case -1:
/*     */       case 1:
/*     */       case 12:
/* 779 */         if (!isOpaqueBinary) {
/* 780 */           return "java.lang.String";
/*     */         }
/*     */         
/* 783 */         return "[B";
/*     */ 
/*     */       
/*     */       case -4:
/*     */       case -3:
/*     */       case -2:
/* 789 */         if (mysqlTypeIfKnown == 255)
/* 790 */           return "[B"; 
/* 791 */         if (isBinaryOrBlob) {
/* 792 */           return "[B";
/*     */         }
/* 794 */         return "java.lang.String";
/*     */ 
/*     */       
/*     */       case 91:
/* 798 */         return "java.sql.Date";
/*     */       
/*     */       case 92:
/* 801 */         return "java.sql.Time";
/*     */       
/*     */       case 93:
/* 804 */         return "java.sql.Timestamp";
/*     */     } 
/*     */     
/* 807 */     return "java.lang.Object";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWrapperFor(Class iface) throws SQLException {
/* 829 */     return iface.isInstance(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object unwrap(Class iface) throws SQLException {
/*     */     try {
/* 850 */       return Util.cast(iface, this);
/* 851 */     } catch (ClassCastException cce) {
/* 852 */       throw SQLError.createSQLException("Unable to unwrap to " + iface.toString(), "S1009", this.exceptionInterceptor);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\ResultSetMetaData.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */