/*    */ package org.flywaydb.core.internal.resolver;
/*    */ 
/*    */ import org.flywaydb.core.api.FlywayException;
/*    */ import org.flywaydb.core.api.MigrationVersion;
/*    */ import org.flywaydb.core.internal.util.Pair;
/*    */ import org.flywaydb.core.internal.util.StringUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MigrationInfoHelper
/*    */ {
/*    */   public static Pair<MigrationVersion, String> extractVersionAndDescription(String migrationName, String prefix, String separator, String suffix) {
/* 47 */     String cleanMigrationName = migrationName.substring(prefix.length(), migrationName.length() - suffix.length());
/*    */ 
/*    */     
/* 50 */     int descriptionPos = cleanMigrationName.indexOf(separator);
/* 51 */     if (descriptionPos < 0) {
/* 52 */       throw new FlywayException("Wrong migration name format: " + migrationName + "(It should look like this: " + prefix + "1_2" + separator + "Description" + suffix + ")");
/*    */     }
/*    */ 
/*    */     
/* 56 */     String version = cleanMigrationName.substring(0, descriptionPos);
/* 57 */     String description = cleanMigrationName.substring(descriptionPos + separator.length()).replaceAll("_", " ");
/* 58 */     if (StringUtils.hasText(version)) {
/* 59 */       return Pair.of(MigrationVersion.fromVersion(version), description);
/*    */     }
/* 61 */     return Pair.of(null, description);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\resolver\MigrationInfoHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */