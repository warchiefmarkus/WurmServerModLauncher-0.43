/*     */ package org.flywaydb.core.internal.resolver.jdbc;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import org.flywaydb.core.api.FlywayException;
/*     */ import org.flywaydb.core.api.MigrationType;
/*     */ import org.flywaydb.core.api.MigrationVersion;
/*     */ import org.flywaydb.core.api.configuration.FlywayConfiguration;
/*     */ import org.flywaydb.core.api.migration.MigrationChecksumProvider;
/*     */ import org.flywaydb.core.api.migration.MigrationInfoProvider;
/*     */ import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
/*     */ import org.flywaydb.core.api.resolver.MigrationResolver;
/*     */ import org.flywaydb.core.api.resolver.ResolvedMigration;
/*     */ import org.flywaydb.core.internal.resolver.MigrationInfoHelper;
/*     */ import org.flywaydb.core.internal.resolver.ResolvedMigrationComparator;
/*     */ import org.flywaydb.core.internal.resolver.ResolvedMigrationImpl;
/*     */ import org.flywaydb.core.internal.util.ClassUtils;
/*     */ import org.flywaydb.core.internal.util.ConfigurationInjectionUtils;
/*     */ import org.flywaydb.core.internal.util.Location;
/*     */ import org.flywaydb.core.internal.util.Pair;
/*     */ import org.flywaydb.core.internal.util.StringUtils;
/*     */ import org.flywaydb.core.internal.util.scanner.Scanner;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JdbcMigrationResolver
/*     */   implements MigrationResolver
/*     */ {
/*     */   private final Location location;
/*     */   private Scanner scanner;
/*     */   private FlywayConfiguration configuration;
/*     */   
/*     */   public JdbcMigrationResolver(Scanner scanner, Location location, FlywayConfiguration configuration) {
/*  69 */     this.location = location;
/*  70 */     this.scanner = scanner;
/*  71 */     this.configuration = configuration;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ResolvedMigration> resolveMigrations() {
/*  76 */     List<ResolvedMigration> migrations = new ArrayList<ResolvedMigration>();
/*     */     
/*  78 */     if (!this.location.isClassPath()) {
/*  79 */       return migrations;
/*     */     }
/*     */     
/*     */     try {
/*  83 */       Class<?>[] classes = this.scanner.scanForClasses(this.location, JdbcMigration.class);
/*  84 */       for (Class<?> clazz : classes) {
/*  85 */         JdbcMigration jdbcMigration = (JdbcMigration)ClassUtils.instantiate(clazz.getName(), this.scanner.getClassLoader());
/*  86 */         ConfigurationInjectionUtils.injectFlywayConfiguration(jdbcMigration, this.configuration);
/*     */         
/*  88 */         ResolvedMigrationImpl migrationInfo = extractMigrationInfo(jdbcMigration);
/*  89 */         migrationInfo.setPhysicalLocation(ClassUtils.getLocationOnDisk(clazz));
/*  90 */         migrationInfo.setExecutor(new JdbcMigrationExecutor(jdbcMigration));
/*     */         
/*  92 */         migrations.add(migrationInfo);
/*     */       } 
/*  94 */     } catch (Exception e) {
/*  95 */       throw new FlywayException("Unable to resolve Jdbc Java migrations in location: " + this.location, e);
/*     */     } 
/*     */     
/*  98 */     Collections.sort(migrations, (Comparator<? super ResolvedMigration>)new ResolvedMigrationComparator());
/*  99 */     return migrations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ResolvedMigrationImpl extractMigrationInfo(JdbcMigration jdbcMigration) {
/*     */     MigrationVersion version;
/*     */     String description;
/* 109 */     Integer checksum = null;
/* 110 */     if (jdbcMigration instanceof MigrationChecksumProvider) {
/* 111 */       MigrationChecksumProvider checksumProvider = (MigrationChecksumProvider)jdbcMigration;
/* 112 */       checksum = checksumProvider.getChecksum();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 117 */     if (jdbcMigration instanceof MigrationInfoProvider) {
/* 118 */       MigrationInfoProvider infoProvider = (MigrationInfoProvider)jdbcMigration;
/* 119 */       version = infoProvider.getVersion();
/* 120 */       description = infoProvider.getDescription();
/* 121 */       if (!StringUtils.hasText(description)) {
/* 122 */         throw new FlywayException("Missing description for migration " + version);
/*     */       }
/*     */     } else {
/* 125 */       String prefix, shortName = ClassUtils.getShortName(jdbcMigration.getClass());
/*     */       
/* 127 */       if (shortName.startsWith("V") || shortName.startsWith("R")) {
/* 128 */         prefix = shortName.substring(0, 1);
/*     */       } else {
/* 130 */         throw new FlywayException("Invalid Jdbc migration class name: " + jdbcMigration.getClass().getName() + " => ensure it starts with V or R," + " or implement org.flywaydb.core.api.migration.MigrationInfoProvider for non-default naming");
/*     */       } 
/*     */ 
/*     */       
/* 134 */       Pair<MigrationVersion, String> info = MigrationInfoHelper.extractVersionAndDescription(shortName, prefix, "__", "");
/* 135 */       version = (MigrationVersion)info.getLeft();
/* 136 */       description = (String)info.getRight();
/*     */     } 
/*     */     
/* 139 */     ResolvedMigrationImpl resolvedMigration = new ResolvedMigrationImpl();
/* 140 */     resolvedMigration.setVersion(version);
/* 141 */     resolvedMigration.setDescription(description);
/* 142 */     resolvedMigration.setScript(jdbcMigration.getClass().getName());
/* 143 */     resolvedMigration.setChecksum(checksum);
/* 144 */     resolvedMigration.setType(MigrationType.JDBC);
/* 145 */     return resolvedMigration;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\resolver\jdbc\JdbcMigrationResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */