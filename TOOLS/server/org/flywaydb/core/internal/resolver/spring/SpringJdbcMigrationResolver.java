/*     */ package org.flywaydb.core.internal.resolver.spring;
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
/*     */ import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
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
/*     */ 
/*     */ public class SpringJdbcMigrationResolver
/*     */   implements MigrationResolver
/*     */ {
/*     */   private final Location location;
/*     */   private Scanner scanner;
/*     */   private FlywayConfiguration configuration;
/*     */   
/*     */   public SpringJdbcMigrationResolver(Scanner scanner, Location location, FlywayConfiguration configuration) {
/*  70 */     this.location = location;
/*  71 */     this.scanner = scanner;
/*  72 */     this.configuration = configuration;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<ResolvedMigration> resolveMigrations() {
/*  77 */     List<ResolvedMigration> migrations = new ArrayList<ResolvedMigration>();
/*     */     
/*  79 */     if (!this.location.isClassPath()) {
/*  80 */       return migrations;
/*     */     }
/*     */     
/*     */     try {
/*  84 */       Class<?>[] classes = this.scanner.scanForClasses(this.location, SpringJdbcMigration.class);
/*  85 */       for (Class<?> clazz : classes) {
/*  86 */         SpringJdbcMigration springJdbcMigration = (SpringJdbcMigration)ClassUtils.instantiate(clazz.getName(), this.scanner.getClassLoader());
/*  87 */         ConfigurationInjectionUtils.injectFlywayConfiguration(springJdbcMigration, this.configuration);
/*     */         
/*  89 */         ResolvedMigrationImpl migrationInfo = extractMigrationInfo(springJdbcMigration);
/*  90 */         migrationInfo.setPhysicalLocation(ClassUtils.getLocationOnDisk(clazz));
/*  91 */         migrationInfo.setExecutor(new SpringJdbcMigrationExecutor(springJdbcMigration));
/*     */         
/*  93 */         migrations.add(migrationInfo);
/*     */       } 
/*  95 */     } catch (Exception e) {
/*  96 */       throw new FlywayException("Unable to resolve Spring Jdbc Java migrations in location: " + this.location, e);
/*     */     } 
/*     */     
/*  99 */     Collections.sort(migrations, (Comparator<? super ResolvedMigration>)new ResolvedMigrationComparator());
/* 100 */     return migrations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ResolvedMigrationImpl extractMigrationInfo(SpringJdbcMigration springJdbcMigration) {
/*     */     MigrationVersion version;
/*     */     String description;
/* 110 */     Integer checksum = null;
/* 111 */     if (springJdbcMigration instanceof MigrationChecksumProvider) {
/* 112 */       MigrationChecksumProvider checksumProvider = (MigrationChecksumProvider)springJdbcMigration;
/* 113 */       checksum = checksumProvider.getChecksum();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 118 */     if (springJdbcMigration instanceof MigrationInfoProvider) {
/* 119 */       MigrationInfoProvider infoProvider = (MigrationInfoProvider)springJdbcMigration;
/* 120 */       version = infoProvider.getVersion();
/* 121 */       description = infoProvider.getDescription();
/* 122 */       if (!StringUtils.hasText(description)) {
/* 123 */         throw new FlywayException("Missing description for migration " + version);
/*     */       }
/*     */     } else {
/* 126 */       String prefix, shortName = ClassUtils.getShortName(springJdbcMigration.getClass());
/*     */       
/* 128 */       if (shortName.startsWith("V") || shortName.startsWith("R")) {
/* 129 */         prefix = shortName.substring(0, 1);
/*     */       } else {
/* 131 */         throw new FlywayException("Invalid Jdbc migration class name: " + springJdbcMigration.getClass().getName() + " => ensure it starts with V or R," + " or implement org.flywaydb.core.api.migration.MigrationInfoProvider for non-default naming");
/*     */       } 
/*     */ 
/*     */       
/* 135 */       Pair<MigrationVersion, String> info = MigrationInfoHelper.extractVersionAndDescription(shortName, prefix, "__", "");
/* 136 */       version = (MigrationVersion)info.getLeft();
/* 137 */       description = (String)info.getRight();
/*     */     } 
/*     */     
/* 140 */     ResolvedMigrationImpl resolvedMigration = new ResolvedMigrationImpl();
/* 141 */     resolvedMigration.setVersion(version);
/* 142 */     resolvedMigration.setDescription(description);
/* 143 */     resolvedMigration.setScript(springJdbcMigration.getClass().getName());
/* 144 */     resolvedMigration.setChecksum(checksum);
/* 145 */     resolvedMigration.setType(MigrationType.SPRING_JDBC);
/* 146 */     return resolvedMigration;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\resolver\spring\SpringJdbcMigrationResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */