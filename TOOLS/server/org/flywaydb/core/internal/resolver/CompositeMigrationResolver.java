/*     */ package org.flywaydb.core.internal.resolver;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.flywaydb.core.api.FlywayException;
/*     */ import org.flywaydb.core.api.configuration.FlywayConfiguration;
/*     */ import org.flywaydb.core.api.resolver.MigrationResolver;
/*     */ import org.flywaydb.core.api.resolver.ResolvedMigration;
/*     */ import org.flywaydb.core.internal.dbsupport.DbSupport;
/*     */ import org.flywaydb.core.internal.resolver.jdbc.JdbcMigrationResolver;
/*     */ import org.flywaydb.core.internal.resolver.spring.SpringJdbcMigrationResolver;
/*     */ import org.flywaydb.core.internal.resolver.sql.SqlMigrationResolver;
/*     */ import org.flywaydb.core.internal.util.FeatureDetector;
/*     */ import org.flywaydb.core.internal.util.Location;
/*     */ import org.flywaydb.core.internal.util.Locations;
/*     */ import org.flywaydb.core.internal.util.PlaceholderReplacer;
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
/*     */ public class CompositeMigrationResolver
/*     */   implements MigrationResolver
/*     */ {
/*  48 */   private Collection<MigrationResolver> migrationResolvers = new ArrayList<MigrationResolver>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<ResolvedMigration> availableMigrations;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositeMigrationResolver(DbSupport dbSupport, Scanner scanner, FlywayConfiguration config, Locations locations, String encoding, String sqlMigrationPrefix, String repeatableSqlMigrationPrefix, String sqlMigrationSeparator, String sqlMigrationSuffix, PlaceholderReplacer placeholderReplacer, MigrationResolver... customMigrationResolvers) {
/*  76 */     if (!config.isSkipDefaultResolvers()) {
/*  77 */       for (Location location : locations.getLocations()) {
/*  78 */         this.migrationResolvers.add(new SqlMigrationResolver(dbSupport, scanner, location, placeholderReplacer, encoding, sqlMigrationPrefix, repeatableSqlMigrationPrefix, sqlMigrationSeparator, sqlMigrationSuffix));
/*     */         
/*  80 */         this.migrationResolvers.add(new JdbcMigrationResolver(scanner, location, config));
/*     */         
/*  82 */         if ((new FeatureDetector(scanner.getClassLoader())).isSpringJdbcAvailable()) {
/*  83 */           this.migrationResolvers.add(new SpringJdbcMigrationResolver(scanner, location, config));
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*  88 */     this.migrationResolvers.addAll(Arrays.asList(customMigrationResolvers));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ResolvedMigration> resolveMigrations() {
/*  99 */     if (this.availableMigrations == null) {
/* 100 */       this.availableMigrations = doFindAvailableMigrations();
/*     */     }
/*     */     
/* 103 */     return this.availableMigrations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<ResolvedMigration> doFindAvailableMigrations() throws FlywayException {
/* 114 */     List<ResolvedMigration> migrations = new ArrayList<ResolvedMigration>(collectMigrations(this.migrationResolvers));
/* 115 */     Collections.sort(migrations, new ResolvedMigrationComparator());
/*     */     
/* 117 */     checkForIncompatibilities(migrations);
/*     */     
/* 119 */     return migrations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Collection<ResolvedMigration> collectMigrations(Collection<MigrationResolver> migrationResolvers) {
/* 130 */     Set<ResolvedMigration> migrations = new HashSet<ResolvedMigration>();
/* 131 */     for (MigrationResolver migrationResolver : migrationResolvers) {
/* 132 */       migrations.addAll(migrationResolver.resolveMigrations());
/*     */     }
/* 134 */     return migrations;
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
/*     */   static void checkForIncompatibilities(List<ResolvedMigration> migrations) {
/* 146 */     for (int i = 0; i < migrations.size() - 1; i++) {
/* 147 */       ResolvedMigration current = migrations.get(i);
/* 148 */       ResolvedMigration next = migrations.get(i + 1);
/* 149 */       if ((new ResolvedMigrationComparator()).compare(current, next) == 0) {
/* 150 */         if (current.getVersion() != null) {
/* 151 */           throw new FlywayException(String.format("Found more than one migration with version %s\nOffenders:\n-> %s (%s)\n-> %s (%s)", new Object[] { current
/* 152 */                   .getVersion(), current
/* 153 */                   .getPhysicalLocation(), current
/* 154 */                   .getType(), next
/* 155 */                   .getPhysicalLocation(), next
/* 156 */                   .getType() }));
/*     */         }
/* 158 */         throw new FlywayException(String.format("Found more than one repeatable migration with description %s\nOffenders:\n-> %s (%s)\n-> %s (%s)", new Object[] { current
/* 159 */                 .getDescription(), current
/* 160 */                 .getPhysicalLocation(), current
/* 161 */                 .getType(), next
/* 162 */                 .getPhysicalLocation(), next
/* 163 */                 .getType() }));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\resolver\CompositeMigrationResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */