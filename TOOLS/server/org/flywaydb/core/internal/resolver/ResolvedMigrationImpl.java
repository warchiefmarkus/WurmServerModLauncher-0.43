/*     */ package org.flywaydb.core.internal.resolver;
/*     */ 
/*     */ import org.flywaydb.core.api.MigrationType;
/*     */ import org.flywaydb.core.api.MigrationVersion;
/*     */ import org.flywaydb.core.api.resolver.MigrationExecutor;
/*     */ import org.flywaydb.core.api.resolver.ResolvedMigration;
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
/*     */ public class ResolvedMigrationImpl
/*     */   implements ResolvedMigration
/*     */ {
/*     */   private MigrationVersion version;
/*     */   private String description;
/*     */   private String script;
/*     */   private Integer checksum;
/*     */   private MigrationType type;
/*     */   private String physicalLocation;
/*     */   private MigrationExecutor executor;
/*     */   
/*     */   public MigrationVersion getVersion() {
/*  65 */     return this.version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVersion(MigrationVersion version) {
/*  72 */     this.version = version;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/*  77 */     return this.description;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDescription(String description) {
/*  84 */     this.description = description;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getScript() {
/*  89 */     return this.script;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScript(String script) {
/*  96 */     this.script = script;
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getChecksum() {
/* 101 */     return this.checksum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChecksum(Integer checksum) {
/* 108 */     this.checksum = checksum;
/*     */   }
/*     */ 
/*     */   
/*     */   public MigrationType getType() {
/* 113 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(MigrationType type) {
/* 120 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPhysicalLocation() {
/* 125 */     return this.physicalLocation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPhysicalLocation(String physicalLocation) {
/* 132 */     this.physicalLocation = physicalLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public MigrationExecutor getExecutor() {
/* 137 */     return this.executor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExecutor(MigrationExecutor executor) {
/* 144 */     this.executor = executor;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(ResolvedMigrationImpl o) {
/* 149 */     return this.version.compareTo(o.version);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 155 */     if (this == o) return true; 
/* 156 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/* 158 */     ResolvedMigrationImpl migration = (ResolvedMigrationImpl)o;
/*     */     
/* 160 */     if ((this.checksum != null) ? !this.checksum.equals(migration.checksum) : (migration.checksum != null)) return false; 
/* 161 */     if ((this.description != null) ? !this.description.equals(migration.description) : (migration.description != null))
/* 162 */       return false; 
/* 163 */     if ((this.script != null) ? !this.script.equals(migration.script) : (migration.script != null)) return false; 
/* 164 */     if (this.type != migration.type) return false; 
/* 165 */     return ObjectUtils.nullSafeEquals(this.version, migration.version);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 170 */     int result = (this.version != null) ? this.version.hashCode() : 0;
/* 171 */     result = 31 * result + ((this.description != null) ? this.description.hashCode() : 0);
/* 172 */     result = 31 * result + ((this.script != null) ? this.script.hashCode() : 0);
/* 173 */     result = 31 * result + ((this.checksum != null) ? this.checksum.hashCode() : 0);
/* 174 */     result = 31 * result + this.type.hashCode();
/* 175 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\resolver\ResolvedMigrationImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */