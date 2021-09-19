/*     */ package org.flywaydb.core.internal.dbsupport.phoenix;
/*     */ 
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
/*     */ import org.flywaydb.core.internal.dbsupport.Schema;
/*     */ import org.flywaydb.core.internal.dbsupport.Table;
/*     */ import org.flywaydb.core.internal.util.jdbc.RowMapper;
/*     */ import org.flywaydb.core.internal.util.logging.Log;
/*     */ import org.flywaydb.core.internal.util.logging.LogFactory;
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
/*     */ public class PhoenixSchema
/*     */   extends Schema<PhoenixDbSupport>
/*     */ {
/*  37 */   private static final Log LOG = LogFactory.getLog(PhoenixSchema.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PhoenixSchema(JdbcTemplate jdbcTemplate, PhoenixDbSupport dbSupport, String name) {
/*  47 */     super(jdbcTemplate, dbSupport, name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean doExists() throws SQLException {
/*  53 */     ResultSet rs = this.jdbcTemplate.getMetaData().getSchemas();
/*  54 */     while (rs.next()) {
/*  55 */       String schemaName = rs.getString("TABLE_SCHEM");
/*  56 */       if (schemaName == null) {
/*  57 */         if (this.name == null) {
/*  58 */           return true;
/*     */         }
/*     */         continue;
/*     */       } 
/*  62 */       if (this.name != null && schemaName.equals(this.name)) {
/*  63 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  67 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doEmpty() throws SQLException {
/*  72 */     return ((allTables()).length == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doCreate() throws SQLException {
/*  77 */     LOG.info("Phoenix does not support creating schemas. Schema not created: " + this.name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDrop() throws SQLException {
/*  82 */     LOG.info("Phoenix does not support dropping schemas directly. Running clean of objects instead");
/*  83 */     doClean();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doClean() throws SQLException {
/*  89 */     List<String> sequenceNames = listObjectsOfType("sequence");
/*  90 */     for (String statement : generateDropStatements("SEQUENCE", sequenceNames, "")) {
/*  91 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  95 */     List<String> viewNames = listObjectsOfType("view");
/*  96 */     for (String statement : generateDropStatements("VIEW", viewNames, "")) {
/*  97 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 103 */     List<String> indexPairs = listObjectsOfType("index");
/* 104 */     List<String> indexNames = new ArrayList<String>();
/* 105 */     List<String> indexTables = new ArrayList<String>();
/* 106 */     for (String indexPair : indexPairs) {
/* 107 */       String[] splits = indexPair.split(",");
/* 108 */       indexNames.add(splits[0]);
/* 109 */       indexTables.add("ON " + ((PhoenixDbSupport)this.dbSupport).quote(new String[] { this.name, splits[1] }));
/*     */     } 
/*     */ 
/*     */     
/* 113 */     List<String> statements = generateDropIndexStatements(indexNames, indexTables);
/* 114 */     for (String statement : statements) {
/* 115 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */ 
/*     */     
/* 119 */     List<String> tableNames = listObjectsOfType("table");
/* 120 */     for (String statement : generateDropStatements("TABLE", tableNames, "")) {
/* 121 */       this.jdbcTemplate.execute(statement, new Object[0]);
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
/*     */   private List<String> generateDropStatements(String objectType, List<String> objectNames, String dropStatementSuffix) {
/* 134 */     List<String> statements = new ArrayList<String>();
/* 135 */     for (String objectName : objectNames) {
/*     */       
/* 137 */       String dropStatement = "DROP " + objectType + " " + ((PhoenixDbSupport)this.dbSupport).quote(new String[] { this.name, objectName }) + " " + dropStatementSuffix;
/*     */       
/* 139 */       statements.add(dropStatement);
/*     */     } 
/* 141 */     return statements;
/*     */   }
/*     */   
/*     */   private List<String> generateDropIndexStatements(List<String> objectNames, List<String> dropStatementSuffixes) {
/* 145 */     List<String> statements = new ArrayList<String>();
/* 146 */     for (int i = 0; i < objectNames.size(); i++) {
/*     */       
/* 148 */       String dropStatement = "DROP INDEX " + ((PhoenixDbSupport)this.dbSupport).quote(new String[] { objectNames.get(i) }) + " " + (String)dropStatementSuffixes.get(i);
/*     */       
/* 150 */       statements.add(dropStatement);
/*     */     } 
/* 152 */     return statements;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Table[] doAllTables() throws SQLException {
/* 157 */     List<String> tableNames = listObjectsOfType("table");
/*     */     
/* 159 */     Table[] tables = new Table[tableNames.size()];
/* 160 */     for (int i = 0; i < tableNames.size(); i++) {
/* 161 */       tables[i] = new PhoenixTable(this.jdbcTemplate, this.dbSupport, this, tableNames.get(i));
/*     */     }
/* 163 */     return tables;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<String> listObjectsOfType(String type) throws SQLException {
/* 174 */     List<String> retVal = new ArrayList<String>();
/*     */ 
/*     */     
/* 177 */     String finalName = (this.name == null) ? "" : this.name;
/*     */ 
/*     */     
/* 180 */     if (type.equalsIgnoreCase("view")) {
/* 181 */       ResultSet rs = this.jdbcTemplate.getConnection().getMetaData().getTables(null, finalName, null, new String[] { "VIEW" });
/* 182 */       while (rs.next()) {
/* 183 */         String viewName = rs.getString("TABLE_NAME");
/* 184 */         if (viewName != null) {
/* 185 */           retVal.add(viewName);
/*     */         }
/*     */       }
/*     */     
/*     */     }
/* 190 */     else if (type.equalsIgnoreCase("table")) {
/* 191 */       ResultSet rs = this.jdbcTemplate.getMetaData().getTables(null, finalName, null, new String[] { "TABLE" });
/* 192 */       while (rs.next()) {
/* 193 */         String tableName = rs.getString("TABLE_NAME");
/* 194 */         Set<String> tables = new HashSet<String>();
/* 195 */         if (tableName != null) {
/* 196 */           tables.add(tableName);
/*     */         }
/* 198 */         retVal.addAll(tables);
/*     */       } 
/*     */     } else {
/*     */       
/* 202 */       if (type.equalsIgnoreCase("sequence")) {
/* 203 */         if (this.name == null) {
/* 204 */           String str = "SELECT SEQUENCE_NAME FROM SYSTEM.\"SEQUENCE\" WHERE SEQUENCE_SCHEMA IS NULL";
/* 205 */           return this.jdbcTemplate.queryForStringList(str, new String[0]);
/*     */         } 
/*     */         
/* 208 */         String query = "SELECT SEQUENCE_NAME FROM SYSTEM.\"SEQUENCE\" WHERE SEQUENCE_SCHEMA = ?";
/* 209 */         return this.jdbcTemplate.queryForStringList(query, new String[] { this.name });
/*     */       } 
/*     */ 
/*     */       
/* 213 */       if (type.equalsIgnoreCase("index")) {
/* 214 */         String query = "SELECT TABLE_NAME, DATA_TABLE_NAME FROM SYSTEM.CATALOG WHERE TABLE_SCHEM";
/*     */         
/* 216 */         if (this.name == null) {
/* 217 */           query = query + " IS NULL";
/*     */         } else {
/*     */           
/* 220 */           query = query + " = ?";
/*     */         } 
/* 222 */         query = query + " AND TABLE_TYPE = 'i'";
/*     */         
/* 224 */         String finalQuery = query.replaceFirst("\\?", "'" + this.name + "'");
/*     */         
/* 226 */         retVal = this.jdbcTemplate.query(finalQuery, new RowMapper<String>()
/*     */             {
/*     */               public String mapRow(ResultSet rs) throws SQLException {
/* 229 */                 return rs.getString("TABLE_NAME") + "," + rs.getString("DATA_TABLE_NAME"); }
/*     */             });
/*     */       } 
/*     */     } 
/* 233 */     return retVal;
/*     */   }
/*     */ 
/*     */   
/*     */   public Table getTable(String tableName) {
/* 238 */     return new PhoenixTable(this.jdbcTemplate, this.dbSupport, this, tableName);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 243 */     if (this == o) return true; 
/* 244 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/* 246 */     Schema schema = (Schema)o;
/* 247 */     if (this.name == null) {
/* 248 */       return (this.name == schema.getName());
/*     */     }
/*     */     
/* 251 */     return this.name.equals(schema.getName());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\phoenix\PhoenixSchema.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */