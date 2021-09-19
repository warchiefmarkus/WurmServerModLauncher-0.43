/*    */ package org.flywaydb.core.internal.info;
/*    */ 
/*    */ import org.flywaydb.core.api.MigrationInfo;
/*    */ import org.flywaydb.core.internal.util.DateUtils;
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
/*    */ public class MigrationInfoDumper
/*    */ {
/*    */   private static final String VERSION_TITLE = "Version";
/*    */   private static final String DESCRIPTION_TITLE = "Description";
/*    */   
/*    */   public static String dumpToAsciiTable(MigrationInfo[] migrationInfos) {
/* 43 */     int versionWidth = "Version".length();
/* 44 */     int descriptionWidth = "Description".length();
/*    */     
/* 46 */     for (MigrationInfo migrationInfo : migrationInfos) {
/* 47 */       versionWidth = Math.max(versionWidth, (migrationInfo.getVersion() == null) ? 0 : migrationInfo.getVersion().toString().length());
/* 48 */       descriptionWidth = Math.max(descriptionWidth, migrationInfo.getDescription().length());
/*    */     } 
/*    */ 
/*    */     
/* 52 */     String ruler = "+-" + StringUtils.trimOrPad("", versionWidth, '-') + "-+-" + StringUtils.trimOrPad("", descriptionWidth, '-') + "-+---------------------+---------+\n";
/*    */     
/* 54 */     StringBuilder table = new StringBuilder();
/* 55 */     table.append(ruler);
/* 56 */     table.append("| ").append(StringUtils.trimOrPad("Version", versionWidth, ' '))
/* 57 */       .append(" | ").append(StringUtils.trimOrPad("Description", descriptionWidth))
/* 58 */       .append(" | Installed on        | State   |\n");
/* 59 */     table.append(ruler);
/*    */     
/* 61 */     if (migrationInfos.length == 0) {
/* 62 */       table.append(StringUtils.trimOrPad("| No migrations found", ruler.length() - 2, ' ')).append("|\n");
/*    */     } else {
/* 64 */       for (MigrationInfo migrationInfo : migrationInfos) {
/* 65 */         String versionStr = (migrationInfo.getVersion() == null) ? "" : migrationInfo.getVersion().toString();
/* 66 */         table.append("| ").append(StringUtils.trimOrPad(versionStr, versionWidth));
/* 67 */         table.append(" | ").append(StringUtils.trimOrPad(migrationInfo.getDescription(), descriptionWidth));
/* 68 */         table.append(" | ").append(StringUtils.trimOrPad(DateUtils.formatDateAsIsoString(migrationInfo.getInstalledOn()), 19));
/* 69 */         table.append(" | ").append(StringUtils.trimOrPad(migrationInfo.getState().getDisplayName(), 7));
/* 70 */         table.append(" |\n");
/*    */       } 
/*    */     } 
/*    */     
/* 74 */     table.append(ruler);
/* 75 */     return table.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\info\MigrationInfoDumper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */