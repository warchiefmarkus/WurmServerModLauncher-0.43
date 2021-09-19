/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.sql.DatabaseMetaData;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Statement;
/*      */ import java.util.regex.PatternSyntaxException;
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
/*      */ public class Field
/*      */ {
/*      */   private static final int AUTO_INCREMENT_FLAG = 512;
/*      */   private static final int NO_CHARSET_INFO = -1;
/*      */   private byte[] buffer;
/*   46 */   private int charsetIndex = 0;
/*      */   
/*   48 */   private String charsetName = null;
/*      */   
/*      */   private int colDecimals;
/*      */   
/*      */   private short colFlag;
/*      */   
/*   54 */   private String collationName = null;
/*      */   
/*   56 */   private ConnectionImpl connection = null;
/*      */   
/*   58 */   private String databaseName = null;
/*      */   
/*   60 */   private int databaseNameLength = -1;
/*      */ 
/*      */   
/*   63 */   private int databaseNameStart = -1;
/*      */   
/*   65 */   private int defaultValueLength = -1;
/*      */ 
/*      */   
/*   68 */   private int defaultValueStart = -1;
/*      */   
/*   70 */   private String fullName = null;
/*      */   
/*   72 */   private String fullOriginalName = null;
/*      */   
/*      */   private boolean isImplicitTempTable = false;
/*      */   
/*      */   private long length;
/*      */   
/*   78 */   private int mysqlType = -1;
/*      */   
/*      */   private String name;
/*      */   
/*      */   private int nameLength;
/*      */   
/*      */   private int nameStart;
/*      */   
/*   86 */   private String originalColumnName = null;
/*      */   
/*   88 */   private int originalColumnNameLength = -1;
/*      */ 
/*      */   
/*   91 */   private int originalColumnNameStart = -1;
/*      */   
/*   93 */   private String originalTableName = null;
/*      */   
/*   95 */   private int originalTableNameLength = -1;
/*      */ 
/*      */   
/*   98 */   private int originalTableNameStart = -1;
/*      */   
/*  100 */   private int precisionAdjustFactor = 0;
/*      */   
/*  102 */   private int sqlType = -1;
/*      */ 
/*      */ 
/*      */   
/*      */   private String tableName;
/*      */ 
/*      */   
/*      */   private int tableNameLength;
/*      */ 
/*      */   
/*      */   private int tableNameStart;
/*      */ 
/*      */   
/*      */   private boolean useOldNameMetadata = false;
/*      */ 
/*      */   
/*      */   private boolean isSingleBit;
/*      */ 
/*      */   
/*      */   private int maxBytesPerChar;
/*      */ 
/*      */ 
/*      */   
/*      */   Field(ConnectionImpl conn, byte[] buffer, int databaseNameStart, int databaseNameLength, int tableNameStart, int tableNameLength, int originalTableNameStart, int originalTableNameLength, int nameStart, int nameLength, int originalColumnNameStart, int originalColumnNameLength, long length, int mysqlType, short colFlag, int colDecimals, int defaultValueStart, int defaultValueLength, int charsetIndex) throws SQLException {
/*  126 */     this.connection = conn;
/*  127 */     this.buffer = buffer;
/*  128 */     this.nameStart = nameStart;
/*  129 */     this.nameLength = nameLength;
/*  130 */     this.tableNameStart = tableNameStart;
/*  131 */     this.tableNameLength = tableNameLength;
/*  132 */     this.length = length;
/*  133 */     this.colFlag = colFlag;
/*  134 */     this.colDecimals = colDecimals;
/*  135 */     this.mysqlType = mysqlType;
/*      */ 
/*      */     
/*  138 */     this.databaseNameStart = databaseNameStart;
/*  139 */     this.databaseNameLength = databaseNameLength;
/*      */     
/*  141 */     this.originalTableNameStart = originalTableNameStart;
/*  142 */     this.originalTableNameLength = originalTableNameLength;
/*      */     
/*  144 */     this.originalColumnNameStart = originalColumnNameStart;
/*  145 */     this.originalColumnNameLength = originalColumnNameLength;
/*      */     
/*  147 */     this.defaultValueStart = defaultValueStart;
/*  148 */     this.defaultValueLength = defaultValueLength;
/*      */ 
/*      */ 
/*      */     
/*  152 */     this.charsetIndex = charsetIndex;
/*      */ 
/*      */ 
/*      */     
/*  156 */     this.sqlType = MysqlDefs.mysqlToJavaType(this.mysqlType);
/*      */     
/*  158 */     checkForImplicitTemporaryTable();
/*      */     
/*  160 */     boolean isFromFunction = (this.originalTableNameLength == 0);
/*      */     
/*  162 */     if (this.mysqlType == 252) {
/*  163 */       if ((this.connection != null && this.connection.getBlobsAreStrings()) || (this.connection.getFunctionsNeverReturnBlobs() && isFromFunction)) {
/*      */         
/*  165 */         this.sqlType = 12;
/*  166 */         this.mysqlType = 15;
/*  167 */       } else if (this.charsetIndex == 63 || !this.connection.versionMeetsMinimum(4, 1, 0)) {
/*      */         
/*  169 */         if (this.connection.getUseBlobToStoreUTF8OutsideBMP() && shouldSetupForUtf8StringInBlob()) {
/*      */           
/*  171 */           setupForUtf8StringInBlob();
/*      */         } else {
/*  173 */           setBlobTypeBasedOnLength();
/*  174 */           this.sqlType = MysqlDefs.mysqlToJavaType(this.mysqlType);
/*      */         } 
/*      */       } else {
/*      */         
/*  178 */         this.mysqlType = 253;
/*  179 */         this.sqlType = -1;
/*      */       } 
/*      */     }
/*      */     
/*  183 */     if (this.sqlType == -6 && this.length == 1L && this.connection.getTinyInt1isBit())
/*      */     {
/*      */       
/*  186 */       if (conn.getTinyInt1isBit()) {
/*  187 */         if (conn.getTransformedBitIsBoolean()) {
/*  188 */           this.sqlType = 16;
/*      */         } else {
/*  190 */           this.sqlType = -7;
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*  196 */     if (!isNativeNumericType() && !isNativeDateTimeType()) {
/*  197 */       this.charsetName = this.connection.getCharsetNameForIndex(this.charsetIndex);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  204 */       boolean isBinary = isBinary();
/*      */       
/*  206 */       if (this.connection.versionMeetsMinimum(4, 1, 0) && this.mysqlType == 253 && isBinary && this.charsetIndex == 63)
/*      */       {
/*      */ 
/*      */         
/*  210 */         if (this.connection != null && this.connection.getFunctionsNeverReturnBlobs() && isFromFunction) {
/*  211 */           this.sqlType = 12;
/*  212 */           this.mysqlType = 15;
/*  213 */         } else if (isOpaqueBinary()) {
/*  214 */           this.sqlType = -3;
/*      */         } 
/*      */       }
/*      */       
/*  218 */       if (this.connection.versionMeetsMinimum(4, 1, 0) && this.mysqlType == 254 && isBinary && this.charsetIndex == 63)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  228 */         if (isOpaqueBinary() && !this.connection.getBlobsAreStrings()) {
/*  229 */           this.sqlType = -2;
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  235 */       if (this.mysqlType == 16) {
/*  236 */         this.isSingleBit = (this.length == 0L);
/*      */         
/*  238 */         if (this.connection != null && (this.connection.versionMeetsMinimum(5, 0, 21) || this.connection.versionMeetsMinimum(5, 1, 10)) && this.length == 1L)
/*      */         {
/*  240 */           this.isSingleBit = true;
/*      */         }
/*      */         
/*  243 */         if (this.isSingleBit) {
/*  244 */           this.sqlType = -7;
/*      */         } else {
/*  246 */           this.sqlType = -3;
/*  247 */           this.colFlag = (short)(this.colFlag | 0x80);
/*  248 */           this.colFlag = (short)(this.colFlag | 0x10);
/*  249 */           isBinary = true;
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  256 */       if (this.sqlType == -4 && !isBinary) {
/*  257 */         this.sqlType = -1;
/*  258 */       } else if (this.sqlType == -3 && !isBinary) {
/*  259 */         this.sqlType = 12;
/*      */       } 
/*      */     } else {
/*  262 */       this.charsetName = "US-ASCII";
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  268 */     if (!isUnsigned()) {
/*  269 */       switch (this.mysqlType) {
/*      */         case 0:
/*      */         case 246:
/*  272 */           this.precisionAdjustFactor = -1;
/*      */           break;
/*      */         
/*      */         case 4:
/*      */         case 5:
/*  277 */           this.precisionAdjustFactor = 1;
/*      */           break;
/*      */       } 
/*      */     
/*      */     } else {
/*  282 */       switch (this.mysqlType) {
/*      */         case 4:
/*      */         case 5:
/*  285 */           this.precisionAdjustFactor = 1;
/*      */           break;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean shouldSetupForUtf8StringInBlob() throws SQLException {
/*  293 */     String includePattern = this.connection.getUtf8OutsideBmpIncludedColumnNamePattern();
/*      */     
/*  295 */     String excludePattern = this.connection.getUtf8OutsideBmpExcludedColumnNamePattern();
/*      */ 
/*      */     
/*  298 */     if (excludePattern != null && !StringUtils.isEmptyOrWhitespaceOnly(excludePattern)) {
/*      */       
/*      */       try {
/*  301 */         if (getOriginalName().matches(excludePattern)) {
/*  302 */           if (includePattern != null && !StringUtils.isEmptyOrWhitespaceOnly(includePattern)) {
/*      */             
/*      */             try {
/*  305 */               if (getOriginalName().matches(includePattern)) {
/*  306 */                 return true;
/*      */               }
/*  308 */             } catch (PatternSyntaxException pse) {
/*  309 */               SQLException sqlEx = SQLError.createSQLException("Illegal regex specified for \"utf8OutsideBmpIncludedColumnNamePattern\"", "S1009", this.connection.getExceptionInterceptor());
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  314 */               if (!this.connection.getParanoid()) {
/*  315 */                 sqlEx.initCause(pse);
/*      */               }
/*      */               
/*  318 */               throw sqlEx;
/*      */             } 
/*      */           }
/*      */           
/*  322 */           return false;
/*      */         } 
/*  324 */       } catch (PatternSyntaxException pse) {
/*  325 */         SQLException sqlEx = SQLError.createSQLException("Illegal regex specified for \"utf8OutsideBmpExcludedColumnNamePattern\"", "S1009", this.connection.getExceptionInterceptor());
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  330 */         if (!this.connection.getParanoid()) {
/*  331 */           sqlEx.initCause(pse);
/*      */         }
/*      */         
/*  334 */         throw sqlEx;
/*      */       } 
/*      */     }
/*      */     
/*  338 */     return true;
/*      */   }
/*      */   
/*      */   private void setupForUtf8StringInBlob() {
/*  342 */     if (this.length == 255L || this.length == 65535L) {
/*  343 */       this.mysqlType = 15;
/*  344 */       this.sqlType = 12;
/*      */     } else {
/*  346 */       this.mysqlType = 253;
/*  347 */       this.sqlType = -1;
/*      */     } 
/*      */     
/*  350 */     this.charsetIndex = 33;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Field(ConnectionImpl conn, byte[] buffer, int nameStart, int nameLength, int tableNameStart, int tableNameLength, int length, int mysqlType, short colFlag, int colDecimals) throws SQLException {
/*  359 */     this(conn, buffer, -1, -1, tableNameStart, tableNameLength, -1, -1, nameStart, nameLength, -1, -1, length, mysqlType, colFlag, colDecimals, -1, -1, -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Field(String tableName, String columnName, int jdbcType, int length) {
/*  368 */     this.tableName = tableName;
/*  369 */     this.name = columnName;
/*  370 */     this.length = length;
/*  371 */     this.sqlType = jdbcType;
/*  372 */     this.colFlag = 0;
/*  373 */     this.colDecimals = 0;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Field(String tableName, String columnName, int charsetIndex, int jdbcType, int length) {
/*  394 */     this.tableName = tableName;
/*  395 */     this.name = columnName;
/*  396 */     this.length = length;
/*  397 */     this.sqlType = jdbcType;
/*  398 */     this.colFlag = 0;
/*  399 */     this.colDecimals = 0;
/*  400 */     this.charsetIndex = charsetIndex;
/*      */     
/*  402 */     switch (this.sqlType) {
/*      */       case -3:
/*      */       case -2:
/*  405 */         this.colFlag = (short)(this.colFlag | 0x80);
/*  406 */         this.colFlag = (short)(this.colFlag | 0x10);
/*      */         break;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkForImplicitTemporaryTable() {
/*  412 */     this.isImplicitTempTable = (this.tableNameLength > 5 && this.buffer[this.tableNameStart] == 35 && this.buffer[this.tableNameStart + 1] == 115 && this.buffer[this.tableNameStart + 2] == 113 && this.buffer[this.tableNameStart + 3] == 108 && this.buffer[this.tableNameStart + 4] == 95);
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
/*      */   public String getCharacterSet() throws SQLException {
/*  426 */     return this.charsetName;
/*      */   }
/*      */   
/*      */   public void setCharacterSet(String javaEncodingName) throws SQLException {
/*  430 */     this.charsetName = javaEncodingName;
/*  431 */     this.charsetIndex = CharsetMapping.getCharsetIndexForMysqlEncodingName(javaEncodingName);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized String getCollation() throws SQLException {
/*  436 */     if (this.collationName == null && 
/*  437 */       this.connection != null && 
/*  438 */       this.connection.versionMeetsMinimum(4, 1, 0)) {
/*  439 */       if (this.connection.getUseDynamicCharsetInfo()) {
/*  440 */         DatabaseMetaData dbmd = this.connection.getMetaData();
/*      */ 
/*      */         
/*  443 */         String quotedIdStr = dbmd.getIdentifierQuoteString();
/*      */         
/*  445 */         if (" ".equals(quotedIdStr)) {
/*  446 */           quotedIdStr = "";
/*      */         }
/*      */         
/*  449 */         String csCatalogName = getDatabaseName();
/*  450 */         String csTableName = getOriginalTableName();
/*  451 */         String csColumnName = getOriginalName();
/*      */         
/*  453 */         if (csCatalogName != null && csCatalogName.length() != 0 && csTableName != null && csTableName.length() != 0 && csColumnName != null && csColumnName.length() != 0) {
/*      */ 
/*      */ 
/*      */           
/*  457 */           StringBuffer queryBuf = new StringBuffer(csCatalogName.length() + csTableName.length() + 28);
/*      */ 
/*      */           
/*  460 */           queryBuf.append("SHOW FULL COLUMNS FROM ");
/*  461 */           queryBuf.append(quotedIdStr);
/*  462 */           queryBuf.append(csCatalogName);
/*  463 */           queryBuf.append(quotedIdStr);
/*  464 */           queryBuf.append(".");
/*  465 */           queryBuf.append(quotedIdStr);
/*  466 */           queryBuf.append(csTableName);
/*  467 */           queryBuf.append(quotedIdStr);
/*      */           
/*  469 */           Statement collationStmt = null;
/*  470 */           ResultSet collationRs = null;
/*      */           
/*      */           try {
/*  473 */             collationStmt = this.connection.createStatement();
/*      */             
/*  475 */             collationRs = collationStmt.executeQuery(queryBuf.toString());
/*      */ 
/*      */             
/*  478 */             while (collationRs.next()) {
/*  479 */               if (csColumnName.equals(collationRs.getString("Field"))) {
/*      */                 
/*  481 */                 this.collationName = collationRs.getString("Collation");
/*      */ 
/*      */                 
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           } finally {
/*  488 */             if (collationRs != null) {
/*  489 */               collationRs.close();
/*  490 */               collationRs = null;
/*      */             } 
/*      */             
/*  493 */             if (collationStmt != null) {
/*  494 */               collationStmt.close();
/*  495 */               collationStmt = null;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } else {
/*  500 */         this.collationName = CharsetMapping.INDEX_TO_COLLATION[this.charsetIndex];
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  506 */     return this.collationName;
/*      */   }
/*      */   
/*      */   public String getColumnLabel() throws SQLException {
/*  510 */     return getName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDatabaseName() throws SQLException {
/*  519 */     if (this.databaseName == null && this.databaseNameStart != -1 && this.databaseNameLength != -1)
/*      */     {
/*  521 */       this.databaseName = getStringFromBytes(this.databaseNameStart, this.databaseNameLength);
/*      */     }
/*      */ 
/*      */     
/*  525 */     return this.databaseName;
/*      */   }
/*      */   
/*      */   int getDecimals() {
/*  529 */     return this.colDecimals;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getFullName() throws SQLException {
/*  538 */     if (this.fullName == null) {
/*  539 */       StringBuffer fullNameBuf = new StringBuffer(getTableName().length() + 1 + getName().length());
/*      */       
/*  541 */       fullNameBuf.append(this.tableName);
/*      */ 
/*      */       
/*  544 */       fullNameBuf.append('.');
/*  545 */       fullNameBuf.append(this.name);
/*  546 */       this.fullName = fullNameBuf.toString();
/*  547 */       fullNameBuf = null;
/*      */     } 
/*      */     
/*  550 */     return this.fullName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getFullOriginalName() throws SQLException {
/*  559 */     getOriginalName();
/*      */     
/*  561 */     if (this.originalColumnName == null) {
/*  562 */       return null;
/*      */     }
/*      */     
/*  565 */     if (this.fullName == null) {
/*  566 */       StringBuffer fullOriginalNameBuf = new StringBuffer(getOriginalTableName().length() + 1 + getOriginalName().length());
/*      */ 
/*      */       
/*  569 */       fullOriginalNameBuf.append(this.originalTableName);
/*      */ 
/*      */       
/*  572 */       fullOriginalNameBuf.append('.');
/*  573 */       fullOriginalNameBuf.append(this.originalColumnName);
/*  574 */       this.fullOriginalName = fullOriginalNameBuf.toString();
/*  575 */       fullOriginalNameBuf = null;
/*      */     } 
/*      */     
/*  578 */     return this.fullOriginalName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLength() {
/*  587 */     return this.length;
/*      */   }
/*      */   
/*      */   public synchronized int getMaxBytesPerCharacter() throws SQLException {
/*  591 */     if (this.maxBytesPerChar == 0) {
/*  592 */       this.maxBytesPerChar = this.connection.getMaxBytesPerChar(getCharacterSet());
/*      */     }
/*      */     
/*  595 */     return this.maxBytesPerChar;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMysqlType() {
/*  604 */     return this.mysqlType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() throws SQLException {
/*  613 */     if (this.name == null) {
/*  614 */       this.name = getStringFromBytes(this.nameStart, this.nameLength);
/*      */     }
/*      */     
/*  617 */     return this.name;
/*      */   }
/*      */   
/*      */   public String getNameNoAliases() throws SQLException {
/*  621 */     if (this.useOldNameMetadata) {
/*  622 */       return getName();
/*      */     }
/*      */     
/*  625 */     if (this.connection != null && this.connection.versionMeetsMinimum(4, 1, 0))
/*      */     {
/*  627 */       return getOriginalName();
/*      */     }
/*      */     
/*  630 */     return getName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getOriginalName() throws SQLException {
/*  639 */     if (this.originalColumnName == null && this.originalColumnNameStart != -1 && this.originalColumnNameLength != -1)
/*      */     {
/*      */       
/*  642 */       this.originalColumnName = getStringFromBytes(this.originalColumnNameStart, this.originalColumnNameLength);
/*      */     }
/*      */ 
/*      */     
/*  646 */     return this.originalColumnName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getOriginalTableName() throws SQLException {
/*  655 */     if (this.originalTableName == null && this.originalTableNameStart != -1 && this.originalTableNameLength != -1)
/*      */     {
/*      */       
/*  658 */       this.originalTableName = getStringFromBytes(this.originalTableNameStart, this.originalTableNameLength);
/*      */     }
/*      */ 
/*      */     
/*  662 */     return this.originalTableName;
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
/*      */   public int getPrecisionAdjustFactor() {
/*  674 */     return this.precisionAdjustFactor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSQLType() {
/*  683 */     return this.sqlType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String getStringFromBytes(int stringStart, int stringLength) throws SQLException {
/*  692 */     if (stringStart == -1 || stringLength == -1) {
/*  693 */       return null;
/*      */     }
/*      */     
/*  696 */     String stringVal = null;
/*      */     
/*  698 */     if (this.connection != null) {
/*  699 */       if (this.connection.getUseUnicode()) {
/*  700 */         String encoding = this.connection.getCharacterSetMetadata();
/*      */         
/*  702 */         if (encoding == null) {
/*  703 */           encoding = this.connection.getEncoding();
/*      */         }
/*      */         
/*  706 */         if (encoding != null) {
/*  707 */           SingleByteCharsetConverter converter = null;
/*      */           
/*  709 */           if (this.connection != null) {
/*  710 */             converter = this.connection.getCharsetConverter(encoding);
/*      */           }
/*      */ 
/*      */           
/*  714 */           if (converter != null) {
/*  715 */             stringVal = converter.toString(this.buffer, stringStart, stringLength);
/*      */           }
/*      */           else {
/*      */             
/*  719 */             byte[] stringBytes = new byte[stringLength];
/*      */             
/*  721 */             int endIndex = stringStart + stringLength;
/*  722 */             int pos = 0;
/*      */             
/*  724 */             for (int i = stringStart; i < endIndex; i++) {
/*  725 */               stringBytes[pos++] = this.buffer[i];
/*      */             }
/*      */             
/*      */             try {
/*  729 */               stringVal = new String(stringBytes, encoding);
/*  730 */             } catch (UnsupportedEncodingException ue) {
/*  731 */               throw new RuntimeException(Messages.getString("Field.12") + encoding + Messages.getString("Field.13"));
/*      */             }
/*      */           
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/*  738 */           stringVal = StringUtils.toAsciiString(this.buffer, stringStart, stringLength);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  743 */         stringVal = StringUtils.toAsciiString(this.buffer, stringStart, stringLength);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  748 */       stringVal = StringUtils.toAsciiString(this.buffer, stringStart, stringLength);
/*      */     } 
/*      */ 
/*      */     
/*  752 */     return stringVal;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTable() throws SQLException {
/*  761 */     return getTableName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTableName() throws SQLException {
/*  770 */     if (this.tableName == null) {
/*  771 */       this.tableName = getStringFromBytes(this.tableNameStart, this.tableNameLength);
/*      */     }
/*      */ 
/*      */     
/*  775 */     return this.tableName;
/*      */   }
/*      */   
/*      */   public String getTableNameNoAliases() throws SQLException {
/*  779 */     if (this.connection.versionMeetsMinimum(4, 1, 0)) {
/*  780 */       return getOriginalTableName();
/*      */     }
/*      */     
/*  783 */     return getTableName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAutoIncrement() {
/*  792 */     return ((this.colFlag & 0x200) > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isBinary() {
/*  801 */     return ((this.colFlag & 0x80) > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isBlob() {
/*  810 */     return ((this.colFlag & 0x10) > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isImplicitTemporaryTable() {
/*  819 */     return this.isImplicitTempTable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isMultipleKey() {
/*  828 */     return ((this.colFlag & 0x8) > 0);
/*      */   }
/*      */   
/*      */   boolean isNotNull() {
/*  832 */     return ((this.colFlag & 0x1) > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isOpaqueBinary() throws SQLException {
/*  842 */     if (this.charsetIndex == 63 && isBinary() && (getMysqlType() == 254 || getMysqlType() == 253)) {
/*      */ 
/*      */ 
/*      */       
/*  846 */       if (this.originalTableNameLength == 0 && this.connection != null && !this.connection.versionMeetsMinimum(5, 0, 25))
/*      */       {
/*  848 */         return false;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  854 */       return !isImplicitTemporaryTable();
/*      */     } 
/*      */     
/*  857 */     return (this.connection.versionMeetsMinimum(4, 1, 0) && "binary".equalsIgnoreCase(getCharacterSet()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPrimaryKey() {
/*  868 */     return ((this.colFlag & 0x2) > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isReadOnly() throws SQLException {
/*  878 */     if (this.connection.versionMeetsMinimum(4, 1, 0)) {
/*  879 */       String orgColumnName = getOriginalName();
/*  880 */       String orgTableName = getOriginalTableName();
/*      */       
/*  882 */       return (orgColumnName == null || orgColumnName.length() <= 0 || orgTableName == null || orgTableName.length() <= 0);
/*      */     } 
/*      */ 
/*      */     
/*  886 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUniqueKey() {
/*  895 */     return ((this.colFlag & 0x4) > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUnsigned() {
/*  904 */     return ((this.colFlag & 0x20) > 0);
/*      */   }
/*      */   
/*      */   public void setUnsigned() {
/*  908 */     this.colFlag = (short)(this.colFlag | 0x20);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isZeroFill() {
/*  917 */     return ((this.colFlag & 0x40) > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setBlobTypeBasedOnLength() {
/*  926 */     if (this.length == 255L) {
/*  927 */       this.mysqlType = 249;
/*  928 */     } else if (this.length == 65535L) {
/*  929 */       this.mysqlType = 252;
/*  930 */     } else if (this.length == 16777215L) {
/*  931 */       this.mysqlType = 250;
/*  932 */     } else if (this.length == 4294967295L) {
/*  933 */       this.mysqlType = 251;
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean isNativeNumericType() {
/*  938 */     return ((this.mysqlType >= 1 && this.mysqlType <= 5) || this.mysqlType == 8 || this.mysqlType == 13);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isNativeDateTimeType() {
/*  945 */     return (this.mysqlType == 10 || this.mysqlType == 14 || this.mysqlType == 12 || this.mysqlType == 11 || this.mysqlType == 7);
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
/*      */   public void setConnection(ConnectionImpl conn) {
/*  959 */     this.connection = conn;
/*      */     
/*  961 */     if (this.charsetName == null || this.charsetIndex == 0) {
/*  962 */       this.charsetName = this.connection.getEncoding();
/*      */     }
/*      */   }
/*      */   
/*      */   void setMysqlType(int type) {
/*  967 */     this.mysqlType = type;
/*  968 */     this.sqlType = MysqlDefs.mysqlToJavaType(this.mysqlType);
/*      */   }
/*      */   
/*      */   protected void setUseOldNameMetadata(boolean useOldNameMetadata) {
/*  972 */     this.useOldNameMetadata = useOldNameMetadata;
/*      */   }
/*      */   
/*      */   public String toString() {
/*      */     try {
/*  977 */       StringBuffer asString = new StringBuffer();
/*  978 */       asString.append(super.toString());
/*  979 */       asString.append("[");
/*  980 */       asString.append("catalog=");
/*  981 */       asString.append(getDatabaseName());
/*  982 */       asString.append(",tableName=");
/*  983 */       asString.append(getTableName());
/*  984 */       asString.append(",originalTableName=");
/*  985 */       asString.append(getOriginalTableName());
/*  986 */       asString.append(",columnName=");
/*  987 */       asString.append(getName());
/*  988 */       asString.append(",originalColumnName=");
/*  989 */       asString.append(getOriginalName());
/*  990 */       asString.append(",mysqlType=");
/*  991 */       asString.append(getMysqlType());
/*  992 */       asString.append("(");
/*  993 */       asString.append(MysqlDefs.typeToName(getMysqlType()));
/*  994 */       asString.append(")");
/*  995 */       asString.append(",flags=");
/*      */       
/*  997 */       if (isAutoIncrement()) {
/*  998 */         asString.append(" AUTO_INCREMENT");
/*      */       }
/*      */       
/* 1001 */       if (isPrimaryKey()) {
/* 1002 */         asString.append(" PRIMARY_KEY");
/*      */       }
/*      */       
/* 1005 */       if (isUniqueKey()) {
/* 1006 */         asString.append(" UNIQUE_KEY");
/*      */       }
/*      */       
/* 1009 */       if (isBinary()) {
/* 1010 */         asString.append(" BINARY");
/*      */       }
/*      */       
/* 1013 */       if (isBlob()) {
/* 1014 */         asString.append(" BLOB");
/*      */       }
/*      */       
/* 1017 */       if (isMultipleKey()) {
/* 1018 */         asString.append(" MULTI_KEY");
/*      */       }
/*      */       
/* 1021 */       if (isUnsigned()) {
/* 1022 */         asString.append(" UNSIGNED");
/*      */       }
/*      */       
/* 1025 */       if (isZeroFill()) {
/* 1026 */         asString.append(" ZEROFILL");
/*      */       }
/*      */       
/* 1029 */       asString.append(", charsetIndex=");
/* 1030 */       asString.append(this.charsetIndex);
/* 1031 */       asString.append(", charsetName=");
/* 1032 */       asString.append(this.charsetName);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1041 */       asString.append("]");
/*      */       
/* 1043 */       return asString.toString();
/* 1044 */     } catch (Throwable t) {
/* 1045 */       return super.toString();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected boolean isSingleBit() {
/* 1050 */     return this.isSingleBit;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\Field.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */