/*     */ package org.flywaydb.core.internal.metadatatable;
/*     */ 
/*     */ import java.util.Date;
/*     */ import org.flywaydb.core.api.MigrationType;
/*     */ import org.flywaydb.core.api.MigrationVersion;
/*     */ import org.flywaydb.core.internal.util.ObjectUtils;
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
/*     */ public class AppliedMigration
/*     */   implements Comparable<AppliedMigration>
/*     */ {
/*     */   private int installedRank;
/*     */   private MigrationVersion version;
/*     */   private String description;
/*     */   private MigrationType type;
/*     */   private String script;
/*     */   private Integer checksum;
/*     */   private Date installedOn;
/*     */   private String installedBy;
/*     */   private int executionTime;
/*     */   private boolean success;
/*     */   
/*     */   public AppliedMigration(int installedRank, MigrationVersion version, String description, MigrationType type, String script, Integer checksum, Date installedOn, String installedBy, int executionTime, boolean success) {
/*  95 */     this.installedRank = installedRank;
/*  96 */     this.version = version;
/*  97 */     this.description = description;
/*  98 */     this.type = type;
/*  99 */     this.script = script;
/* 100 */     this.checksum = checksum;
/* 101 */     this.installedOn = installedOn;
/* 102 */     this.installedBy = installedBy;
/* 103 */     this.executionTime = executionTime;
/* 104 */     this.success = success;
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
/*     */   public AppliedMigration(MigrationVersion version, String description, MigrationType type, String script, Integer checksum, int executionTime, boolean success) {
/* 120 */     this.version = version;
/* 121 */     this.description = abbreviateDescription(description);
/* 122 */     this.type = type;
/* 123 */     this.script = abbreviateScript(script);
/* 124 */     this.checksum = checksum;
/* 125 */     this.executionTime = executionTime;
/* 126 */     this.success = success;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String abbreviateDescription(String description) {
/* 136 */     if (description == null) {
/* 137 */       return null;
/*     */     }
/*     */     
/* 140 */     if (description.length() <= 200) {
/* 141 */       return description;
/*     */     }
/*     */     
/* 144 */     return description.substring(0, 197) + "...";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String abbreviateScript(String script) {
/* 154 */     if (script == null) {
/* 155 */       return null;
/*     */     }
/*     */     
/* 158 */     if (script.length() <= 1000) {
/* 159 */       return script;
/*     */     }
/*     */     
/* 162 */     return "..." + script.substring(3, 1000);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getInstalledRank() {
/* 169 */     return this.installedRank;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MigrationVersion getVersion() {
/* 176 */     return this.version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 183 */     return this.description;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MigrationType getType() {
/* 190 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getScript() {
/* 197 */     return this.script;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getChecksum() {
/* 204 */     return this.checksum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getInstalledOn() {
/* 211 */     return this.installedOn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInstalledBy() {
/* 218 */     return this.installedBy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getExecutionTime() {
/* 225 */     return this.executionTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSuccess() {
/* 232 */     return this.success;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 238 */     if (this == o) return true; 
/* 239 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/* 241 */     AppliedMigration that = (AppliedMigration)o;
/*     */     
/* 243 */     if (this.executionTime != that.executionTime) return false; 
/* 244 */     if (this.installedRank != that.installedRank) return false; 
/* 245 */     if (this.success != that.success) return false; 
/* 246 */     if ((this.checksum != null) ? !this.checksum.equals(that.checksum) : (that.checksum != null)) return false; 
/* 247 */     if (!this.description.equals(that.description)) return false; 
/* 248 */     if ((this.installedBy != null) ? !this.installedBy.equals(that.installedBy) : (that.installedBy != null)) return false; 
/* 249 */     if ((this.installedOn != null) ? !this.installedOn.equals(that.installedOn) : (that.installedOn != null)) return false; 
/* 250 */     if (!this.script.equals(that.script)) return false; 
/* 251 */     if (this.type != that.type) return false; 
/* 252 */     return ObjectUtils.nullSafeEquals(this.version, that.version);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 257 */     int result = this.installedRank;
/* 258 */     result = 31 * result + ((this.version != null) ? this.version.hashCode() : 0);
/* 259 */     result = 31 * result + this.description.hashCode();
/* 260 */     result = 31 * result + this.type.hashCode();
/* 261 */     result = 31 * result + this.script.hashCode();
/* 262 */     result = 31 * result + ((this.checksum != null) ? this.checksum.hashCode() : 0);
/* 263 */     result = 31 * result + ((this.installedOn != null) ? this.installedOn.hashCode() : 0);
/* 264 */     result = 31 * result + ((this.installedBy != null) ? this.installedBy.hashCode() : 0);
/* 265 */     result = 31 * result + this.executionTime;
/* 266 */     result = 31 * result + (this.success ? 1 : 0);
/* 267 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(AppliedMigration o) {
/* 272 */     return this.installedRank - o.installedRank;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\metadatatable\AppliedMigration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */