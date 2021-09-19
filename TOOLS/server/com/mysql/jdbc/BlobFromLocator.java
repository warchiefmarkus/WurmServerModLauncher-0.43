/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.sql.Blob;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class BlobFromLocator
/*     */   implements Blob
/*     */ {
/*  55 */   private List primaryKeyColumns = null;
/*     */   
/*  57 */   private List primaryKeyValues = null;
/*     */ 
/*     */   
/*     */   private ResultSetImpl creatorResultSet;
/*     */   
/*  62 */   private String blobColumnName = null;
/*     */   
/*  64 */   private String tableName = null;
/*     */   
/*  66 */   private int numColsInResultSet = 0;
/*     */   
/*  68 */   private int numPrimaryKeys = 0;
/*     */ 
/*     */   
/*     */   private String quotedId;
/*     */ 
/*     */   
/*     */   private ExceptionInterceptor exceptionInterceptor;
/*     */ 
/*     */ 
/*     */   
/*     */   BlobFromLocator(ResultSetImpl creatorResultSetToSet, int blobColumnIndex, ExceptionInterceptor exceptionInterceptor) throws SQLException {
/*  79 */     this.exceptionInterceptor = exceptionInterceptor;
/*  80 */     this.creatorResultSet = creatorResultSetToSet;
/*     */     
/*  82 */     this.numColsInResultSet = this.creatorResultSet.fields.length;
/*  83 */     this.quotedId = this.creatorResultSet.connection.getMetaData().getIdentifierQuoteString();
/*     */ 
/*     */     
/*  86 */     if (this.numColsInResultSet > 1) {
/*  87 */       this.primaryKeyColumns = new ArrayList();
/*  88 */       this.primaryKeyValues = new ArrayList();
/*     */       
/*  90 */       for (int i = 0; i < this.numColsInResultSet; i++) {
/*  91 */         if (this.creatorResultSet.fields[i].isPrimaryKey()) {
/*  92 */           StringBuffer keyName = new StringBuffer();
/*  93 */           keyName.append(this.quotedId);
/*     */           
/*  95 */           String originalColumnName = this.creatorResultSet.fields[i].getOriginalName();
/*     */ 
/*     */           
/*  98 */           if (originalColumnName != null && originalColumnName.length() > 0) {
/*     */             
/* 100 */             keyName.append(originalColumnName);
/*     */           } else {
/* 102 */             keyName.append(this.creatorResultSet.fields[i].getName());
/*     */           } 
/*     */ 
/*     */           
/* 106 */           keyName.append(this.quotedId);
/*     */           
/* 108 */           this.primaryKeyColumns.add(keyName.toString());
/* 109 */           this.primaryKeyValues.add(this.creatorResultSet.getString(i + 1));
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 114 */       notEnoughInformationInQuery();
/*     */     } 
/*     */     
/* 117 */     this.numPrimaryKeys = this.primaryKeyColumns.size();
/*     */     
/* 119 */     if (this.numPrimaryKeys == 0) {
/* 120 */       notEnoughInformationInQuery();
/*     */     }
/*     */     
/* 123 */     if (this.creatorResultSet.fields[0].getOriginalTableName() != null) {
/* 124 */       StringBuffer tableNameBuffer = new StringBuffer();
/*     */       
/* 126 */       String databaseName = this.creatorResultSet.fields[0].getDatabaseName();
/*     */ 
/*     */       
/* 129 */       if (databaseName != null && databaseName.length() > 0) {
/* 130 */         tableNameBuffer.append(this.quotedId);
/* 131 */         tableNameBuffer.append(databaseName);
/* 132 */         tableNameBuffer.append(this.quotedId);
/* 133 */         tableNameBuffer.append('.');
/*     */       } 
/*     */       
/* 136 */       tableNameBuffer.append(this.quotedId);
/* 137 */       tableNameBuffer.append(this.creatorResultSet.fields[0].getOriginalTableName());
/*     */       
/* 139 */       tableNameBuffer.append(this.quotedId);
/*     */       
/* 141 */       this.tableName = tableNameBuffer.toString();
/*     */     } else {
/* 143 */       StringBuffer tableNameBuffer = new StringBuffer();
/*     */       
/* 145 */       tableNameBuffer.append(this.quotedId);
/* 146 */       tableNameBuffer.append(this.creatorResultSet.fields[0].getTableName());
/*     */       
/* 148 */       tableNameBuffer.append(this.quotedId);
/*     */       
/* 150 */       this.tableName = tableNameBuffer.toString();
/*     */     } 
/*     */     
/* 153 */     this.blobColumnName = this.quotedId + this.creatorResultSet.getString(blobColumnIndex) + this.quotedId;
/*     */   }
/*     */ 
/*     */   
/*     */   private void notEnoughInformationInQuery() throws SQLException {
/* 158 */     throw SQLError.createSQLException("Emulated BLOB locators must come from a ResultSet with only one table selected, and all primary keys selected", "S1000", this.exceptionInterceptor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OutputStream setBinaryStream(long indexToWriteAt) throws SQLException {
/* 168 */     throw SQLError.notImplemented();
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
/*     */   public InputStream getBinaryStream() throws SQLException {
/* 181 */     return new BufferedInputStream(new LocatorInputStream(this), this.creatorResultSet.connection.getLocatorFetchBufferSize());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int setBytes(long writeAt, byte[] bytes, int offset, int length) throws SQLException {
/* 190 */     PreparedStatement pStmt = null;
/*     */     
/* 192 */     if (offset + length > bytes.length) {
/* 193 */       length = bytes.length - offset;
/*     */     }
/*     */     
/* 196 */     byte[] bytesToWrite = new byte[length];
/* 197 */     System.arraycopy(bytes, offset, bytesToWrite, 0, length);
/*     */ 
/*     */     
/* 200 */     StringBuffer query = new StringBuffer("UPDATE ");
/* 201 */     query.append(this.tableName);
/* 202 */     query.append(" SET ");
/* 203 */     query.append(this.blobColumnName);
/* 204 */     query.append(" = INSERT(");
/* 205 */     query.append(this.blobColumnName);
/* 206 */     query.append(", ");
/* 207 */     query.append(writeAt);
/* 208 */     query.append(", ");
/* 209 */     query.append(length);
/* 210 */     query.append(", ?) WHERE ");
/*     */     
/* 212 */     query.append(this.primaryKeyColumns.get(0));
/* 213 */     query.append(" = ?");
/*     */     int i;
/* 215 */     for (i = 1; i < this.numPrimaryKeys; i++) {
/* 216 */       query.append(" AND ");
/* 217 */       query.append(this.primaryKeyColumns.get(i));
/* 218 */       query.append(" = ?");
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 223 */       pStmt = this.creatorResultSet.connection.prepareStatement(query.toString());
/*     */ 
/*     */       
/* 226 */       pStmt.setBytes(1, bytesToWrite);
/*     */       
/* 228 */       for (i = 0; i < this.numPrimaryKeys; i++) {
/* 229 */         pStmt.setString(i + 2, this.primaryKeyValues.get(i));
/*     */       }
/*     */       
/* 232 */       int rowsUpdated = pStmt.executeUpdate();
/*     */       
/* 234 */       if (rowsUpdated != 1) {
/* 235 */         throw SQLError.createSQLException("BLOB data not found! Did primary keys change?", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     finally {
/*     */       
/* 240 */       if (pStmt != null) {
/*     */         try {
/* 242 */           pStmt.close();
/* 243 */         } catch (SQLException sqlEx) {}
/*     */ 
/*     */ 
/*     */         
/* 247 */         pStmt = null;
/*     */       } 
/*     */     } 
/*     */     
/* 251 */     return (int)length();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int setBytes(long writeAt, byte[] bytes) throws SQLException {
/* 258 */     return setBytes(writeAt, bytes, 0, bytes.length);
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
/*     */   public byte[] getBytes(long pos, int length) throws SQLException {
/* 277 */     PreparedStatement pStmt = null;
/*     */ 
/*     */     
/*     */     try {
/* 281 */       pStmt = createGetBytesStatement();
/*     */       
/* 283 */       return getBytesInternal(pStmt, pos, length);
/*     */     } finally {
/* 285 */       if (pStmt != null) {
/*     */         try {
/* 287 */           pStmt.close();
/* 288 */         } catch (SQLException sqlEx) {}
/*     */ 
/*     */ 
/*     */         
/* 292 */         pStmt = null;
/*     */       } 
/*     */     } 
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
/*     */   public long length() throws SQLException {
/* 307 */     ResultSet blobRs = null;
/* 308 */     PreparedStatement pStmt = null;
/*     */ 
/*     */     
/* 311 */     StringBuffer query = new StringBuffer("SELECT LENGTH(");
/* 312 */     query.append(this.blobColumnName);
/* 313 */     query.append(") FROM ");
/* 314 */     query.append(this.tableName);
/* 315 */     query.append(" WHERE ");
/*     */     
/* 317 */     query.append(this.primaryKeyColumns.get(0));
/* 318 */     query.append(" = ?");
/*     */     int i;
/* 320 */     for (i = 1; i < this.numPrimaryKeys; i++) {
/* 321 */       query.append(" AND ");
/* 322 */       query.append(this.primaryKeyColumns.get(i));
/* 323 */       query.append(" = ?");
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 328 */       pStmt = this.creatorResultSet.connection.prepareStatement(query.toString());
/*     */ 
/*     */       
/* 331 */       for (i = 0; i < this.numPrimaryKeys; i++) {
/* 332 */         pStmt.setString(i + 1, this.primaryKeyValues.get(i));
/*     */       }
/*     */       
/* 335 */       blobRs = pStmt.executeQuery();
/*     */       
/* 337 */       if (blobRs.next()) {
/* 338 */         return blobRs.getLong(1);
/*     */       }
/*     */       
/* 341 */       throw SQLError.createSQLException("BLOB data not found! Did primary keys change?", "S1000", this.exceptionInterceptor);
/*     */     }
/*     */     finally {
/*     */       
/* 345 */       if (blobRs != null) {
/*     */         try {
/* 347 */           blobRs.close();
/* 348 */         } catch (SQLException sqlEx) {}
/*     */ 
/*     */ 
/*     */         
/* 352 */         blobRs = null;
/*     */       } 
/*     */       
/* 355 */       if (pStmt != null) {
/*     */         try {
/* 357 */           pStmt.close();
/* 358 */         } catch (SQLException sqlEx) {}
/*     */ 
/*     */ 
/*     */         
/* 362 */         pStmt = null;
/*     */       } 
/*     */     } 
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
/*     */   public long position(Blob pattern, long start) throws SQLException {
/* 382 */     return position(pattern.getBytes(0L, (int)pattern.length()), start);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long position(byte[] pattern, long start) throws SQLException {
/* 389 */     ResultSet blobRs = null;
/* 390 */     PreparedStatement pStmt = null;
/*     */ 
/*     */     
/* 393 */     StringBuffer query = new StringBuffer("SELECT LOCATE(");
/* 394 */     query.append("?, ");
/* 395 */     query.append(this.blobColumnName);
/* 396 */     query.append(", ");
/* 397 */     query.append(start);
/* 398 */     query.append(") FROM ");
/* 399 */     query.append(this.tableName);
/* 400 */     query.append(" WHERE ");
/*     */     
/* 402 */     query.append(this.primaryKeyColumns.get(0));
/* 403 */     query.append(" = ?");
/*     */     int i;
/* 405 */     for (i = 1; i < this.numPrimaryKeys; i++) {
/* 406 */       query.append(" AND ");
/* 407 */       query.append(this.primaryKeyColumns.get(i));
/* 408 */       query.append(" = ?");
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 413 */       pStmt = this.creatorResultSet.connection.prepareStatement(query.toString());
/*     */       
/* 415 */       pStmt.setBytes(1, pattern);
/*     */       
/* 417 */       for (i = 0; i < this.numPrimaryKeys; i++) {
/* 418 */         pStmt.setString(i + 2, this.primaryKeyValues.get(i));
/*     */       }
/*     */       
/* 421 */       blobRs = pStmt.executeQuery();
/*     */       
/* 423 */       if (blobRs.next()) {
/* 424 */         return blobRs.getLong(1);
/*     */       }
/*     */       
/* 427 */       throw SQLError.createSQLException("BLOB data not found! Did primary keys change?", "S1000", this.exceptionInterceptor);
/*     */     }
/*     */     finally {
/*     */       
/* 431 */       if (blobRs != null) {
/*     */         try {
/* 433 */           blobRs.close();
/* 434 */         } catch (SQLException sqlEx) {}
/*     */ 
/*     */ 
/*     */         
/* 438 */         blobRs = null;
/*     */       } 
/*     */       
/* 441 */       if (pStmt != null) {
/*     */         try {
/* 443 */           pStmt.close();
/* 444 */         } catch (SQLException sqlEx) {}
/*     */ 
/*     */ 
/*     */         
/* 448 */         pStmt = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void truncate(long length) throws SQLException {
/* 457 */     PreparedStatement pStmt = null;
/*     */ 
/*     */     
/* 460 */     StringBuffer query = new StringBuffer("UPDATE ");
/* 461 */     query.append(this.tableName);
/* 462 */     query.append(" SET ");
/* 463 */     query.append(this.blobColumnName);
/* 464 */     query.append(" = LEFT(");
/* 465 */     query.append(this.blobColumnName);
/* 466 */     query.append(", ");
/* 467 */     query.append(length);
/* 468 */     query.append(") WHERE ");
/*     */     
/* 470 */     query.append(this.primaryKeyColumns.get(0));
/* 471 */     query.append(" = ?");
/*     */     int i;
/* 473 */     for (i = 1; i < this.numPrimaryKeys; i++) {
/* 474 */       query.append(" AND ");
/* 475 */       query.append(this.primaryKeyColumns.get(i));
/* 476 */       query.append(" = ?");
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 481 */       pStmt = this.creatorResultSet.connection.prepareStatement(query.toString());
/*     */ 
/*     */       
/* 484 */       for (i = 0; i < this.numPrimaryKeys; i++) {
/* 485 */         pStmt.setString(i + 1, this.primaryKeyValues.get(i));
/*     */       }
/*     */       
/* 488 */       int rowsUpdated = pStmt.executeUpdate();
/*     */       
/* 490 */       if (rowsUpdated != 1) {
/* 491 */         throw SQLError.createSQLException("BLOB data not found! Did primary keys change?", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     finally {
/*     */       
/* 496 */       if (pStmt != null) {
/*     */         try {
/* 498 */           pStmt.close();
/* 499 */         } catch (SQLException sqlEx) {}
/*     */ 
/*     */ 
/*     */         
/* 503 */         pStmt = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   PreparedStatement createGetBytesStatement() throws SQLException {
/* 509 */     StringBuffer query = new StringBuffer("SELECT SUBSTRING(");
/*     */     
/* 511 */     query.append(this.blobColumnName);
/* 512 */     query.append(", ");
/* 513 */     query.append("?");
/* 514 */     query.append(", ");
/* 515 */     query.append("?");
/* 516 */     query.append(") FROM ");
/* 517 */     query.append(this.tableName);
/* 518 */     query.append(" WHERE ");
/*     */     
/* 520 */     query.append(this.primaryKeyColumns.get(0));
/* 521 */     query.append(" = ?");
/*     */     
/* 523 */     for (int i = 1; i < this.numPrimaryKeys; i++) {
/* 524 */       query.append(" AND ");
/* 525 */       query.append(this.primaryKeyColumns.get(i));
/* 526 */       query.append(" = ?");
/*     */     } 
/*     */     
/* 529 */     return this.creatorResultSet.connection.prepareStatement(query.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   byte[] getBytesInternal(PreparedStatement pStmt, long pos, int length) throws SQLException {
/* 536 */     ResultSet blobRs = null;
/*     */ 
/*     */     
/*     */     try {
/* 540 */       pStmt.setLong(1, pos);
/* 541 */       pStmt.setInt(2, length);
/*     */       
/* 543 */       for (int i = 0; i < this.numPrimaryKeys; i++) {
/* 544 */         pStmt.setString(i + 3, this.primaryKeyValues.get(i));
/*     */       }
/*     */       
/* 547 */       blobRs = pStmt.executeQuery();
/*     */       
/* 549 */       if (blobRs.next()) {
/* 550 */         return ((ResultSetImpl)blobRs).getBytes(1, true);
/*     */       }
/*     */       
/* 553 */       throw SQLError.createSQLException("BLOB data not found! Did primary keys change?", "S1000", this.exceptionInterceptor);
/*     */     }
/*     */     finally {
/*     */       
/* 557 */       if (blobRs != null) {
/*     */         try {
/* 559 */           blobRs.close();
/* 560 */         } catch (SQLException sqlEx) {}
/*     */ 
/*     */ 
/*     */         
/* 564 */         blobRs = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   class LocatorInputStream extends InputStream {
/*     */     long currentPositionInBlob;
/*     */     long length;
/*     */     PreparedStatement pStmt;
/*     */     private final BlobFromLocator this$0;
/*     */     
/*     */     LocatorInputStream(BlobFromLocator this$0) throws SQLException {
/* 576 */       this.this$0 = this$0; this.currentPositionInBlob = 0L; this.length = 0L; this.pStmt = null;
/* 577 */       this.length = this$0.length();
/* 578 */       this.pStmt = this$0.createGetBytesStatement();
/*     */     }
/*     */     LocatorInputStream(BlobFromLocator this$0, long pos, long len) throws SQLException {
/* 581 */       this.this$0 = this$0; this.currentPositionInBlob = 0L; this.length = 0L; this.pStmt = null;
/* 582 */       this.length = pos + len;
/* 583 */       this.currentPositionInBlob = pos;
/* 584 */       long blobLength = this$0.length();
/*     */       
/* 586 */       if (pos + len > blobLength) {
/* 587 */         throw SQLError.createSQLException(Messages.getString("Blob.invalidStreamLength", new Object[] { new Long(blobLength), new Long(pos), new Long(len) }), "S1009", this$0.exceptionInterceptor);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 593 */       if (pos < 1L) {
/* 594 */         throw SQLError.createSQLException(Messages.getString("Blob.invalidStreamPos"), "S1009", this$0.exceptionInterceptor);
/*     */       }
/*     */ 
/*     */       
/* 598 */       if (pos > blobLength) {
/* 599 */         throw SQLError.createSQLException(Messages.getString("Blob.invalidStreamPos"), "S1009", this$0.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public int read() throws IOException {
/* 605 */       if (this.currentPositionInBlob + 1L > this.length) {
/* 606 */         return -1;
/*     */       }
/*     */       
/*     */       try {
/* 610 */         byte[] asBytes = this.this$0.getBytesInternal(this.pStmt, this.currentPositionInBlob++ + 1L, 1);
/*     */ 
/*     */         
/* 613 */         if (asBytes == null) {
/* 614 */           return -1;
/*     */         }
/*     */         
/* 617 */         return asBytes[0];
/* 618 */       } catch (SQLException sqlEx) {
/* 619 */         throw new IOException(sqlEx.toString());
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int read(byte[] b, int off, int len) throws IOException {
/* 629 */       if (this.currentPositionInBlob + 1L > this.length) {
/* 630 */         return -1;
/*     */       }
/*     */       
/*     */       try {
/* 634 */         byte[] asBytes = this.this$0.getBytesInternal(this.pStmt, this.currentPositionInBlob + 1L, len);
/*     */ 
/*     */         
/* 637 */         if (asBytes == null) {
/* 638 */           return -1;
/*     */         }
/*     */         
/* 641 */         System.arraycopy(asBytes, 0, b, off, asBytes.length);
/*     */         
/* 643 */         this.currentPositionInBlob += asBytes.length;
/*     */         
/* 645 */         return asBytes.length;
/* 646 */       } catch (SQLException sqlEx) {
/* 647 */         throw new IOException(sqlEx.toString());
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int read(byte[] b) throws IOException {
/* 657 */       if (this.currentPositionInBlob + 1L > this.length) {
/* 658 */         return -1;
/*     */       }
/*     */       
/*     */       try {
/* 662 */         byte[] asBytes = this.this$0.getBytesInternal(this.pStmt, this.currentPositionInBlob + 1L, b.length);
/*     */ 
/*     */         
/* 665 */         if (asBytes == null) {
/* 666 */           return -1;
/*     */         }
/*     */         
/* 669 */         System.arraycopy(asBytes, 0, b, 0, asBytes.length);
/*     */         
/* 671 */         this.currentPositionInBlob += asBytes.length;
/*     */         
/* 673 */         return asBytes.length;
/* 674 */       } catch (SQLException sqlEx) {
/* 675 */         throw new IOException(sqlEx.toString());
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 685 */       if (this.pStmt != null) {
/*     */         try {
/* 687 */           this.pStmt.close();
/* 688 */         } catch (SQLException sqlEx) {
/* 689 */           throw new IOException(sqlEx.toString());
/*     */         } 
/*     */       }
/*     */       
/* 693 */       super.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public void free() throws SQLException {
/* 698 */     this.creatorResultSet = null;
/* 699 */     this.primaryKeyColumns = null;
/* 700 */     this.primaryKeyValues = null;
/*     */   }
/*     */   
/*     */   public InputStream getBinaryStream(long pos, long length) throws SQLException {
/* 704 */     return new LocatorInputStream(this, pos, length);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\BlobFromLocator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */