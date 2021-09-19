/*     */ package org.flywaydb.core.internal.info;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import org.flywaydb.core.api.MigrationInfo;
/*     */ import org.flywaydb.core.api.MigrationInfoService;
/*     */ import org.flywaydb.core.api.MigrationState;
/*     */ import org.flywaydb.core.api.MigrationType;
/*     */ import org.flywaydb.core.api.MigrationVersion;
/*     */ import org.flywaydb.core.api.resolver.MigrationResolver;
/*     */ import org.flywaydb.core.api.resolver.ResolvedMigration;
/*     */ import org.flywaydb.core.internal.metadatatable.AppliedMigration;
/*     */ import org.flywaydb.core.internal.metadatatable.MetaDataTable;
/*     */ import org.flywaydb.core.internal.util.ObjectUtils;
/*     */ import org.flywaydb.core.internal.util.Pair;
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
/*     */ public class MigrationInfoServiceImpl
/*     */   implements MigrationInfoService
/*     */ {
/*     */   private final MigrationResolver migrationResolver;
/*     */   private final MetaDataTable metaDataTable;
/*     */   private MigrationVersion target;
/*     */   private boolean outOfOrder;
/*     */   private final boolean pending;
/*     */   private final boolean future;
/*     */   private List<MigrationInfoImpl> migrationInfos;
/*     */   
/*     */   public MigrationInfoServiceImpl(MigrationResolver migrationResolver, MetaDataTable metaDataTable, MigrationVersion target, boolean outOfOrder, boolean pending, boolean future) {
/*  93 */     this.migrationResolver = migrationResolver;
/*  94 */     this.metaDataTable = metaDataTable;
/*  95 */     this.target = target;
/*  96 */     this.outOfOrder = outOfOrder;
/*  97 */     this.pending = pending;
/*  98 */     this.future = future;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void refresh() {
/* 105 */     Collection<ResolvedMigration> availableMigrations = this.migrationResolver.resolveMigrations();
/* 106 */     List<AppliedMigration> appliedMigrations = this.metaDataTable.allAppliedMigrations();
/*     */     
/* 108 */     this.migrationInfos = mergeAvailableAndAppliedMigrations(availableMigrations, appliedMigrations);
/*     */     
/* 110 */     if (MigrationVersion.CURRENT == this.target) {
/* 111 */       MigrationInfo current = current();
/* 112 */       if (current == null) {
/* 113 */         this.target = MigrationVersion.EMPTY;
/*     */       } else {
/* 115 */         this.target = current.getVersion();
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
/*     */   private List<MigrationInfoImpl> mergeAvailableAndAppliedMigrations(Collection<ResolvedMigration> resolvedMigrations, List<AppliedMigration> appliedMigrations) {
/* 128 */     MigrationInfoContext context = new MigrationInfoContext();
/* 129 */     context.outOfOrder = this.outOfOrder;
/* 130 */     context.pending = this.pending;
/* 131 */     context.future = this.future;
/* 132 */     context.target = this.target;
/*     */     
/* 134 */     Map<MigrationVersion, ResolvedMigration> resolvedMigrationsMap = new TreeMap<MigrationVersion, ResolvedMigration>();
/* 135 */     Map<String, ResolvedMigration> resolvedRepeatableMigrationsMap = new TreeMap<String, ResolvedMigration>();
/* 136 */     for (ResolvedMigration resolvedMigration : resolvedMigrations) {
/* 137 */       MigrationVersion version = resolvedMigration.getVersion();
/* 138 */       if (version != null) {
/* 139 */         if (version.compareTo(context.lastResolved) > 0) {
/* 140 */           context.lastResolved = version;
/*     */         }
/* 142 */         resolvedMigrationsMap.put(version, resolvedMigration); continue;
/*     */       } 
/* 144 */       resolvedRepeatableMigrationsMap.put(resolvedMigration.getDescription(), resolvedMigration);
/*     */     } 
/*     */ 
/*     */     
/* 148 */     Map<MigrationVersion, Pair<AppliedMigration, Boolean>> appliedMigrationsMap = new TreeMap<MigrationVersion, Pair<AppliedMigration, Boolean>>();
/*     */     
/* 150 */     List<AppliedMigration> appliedRepeatableMigrations = new ArrayList<AppliedMigration>();
/* 151 */     for (AppliedMigration appliedMigration : appliedMigrations) {
/* 152 */       MigrationVersion version = appliedMigration.getVersion();
/* 153 */       boolean outOfOrder = false;
/* 154 */       if (version != null) {
/* 155 */         if (version.compareTo(context.lastApplied) > 0) {
/* 156 */           context.lastApplied = version;
/*     */         } else {
/* 158 */           outOfOrder = true;
/*     */         } 
/*     */       }
/* 161 */       if (appliedMigration.getType() == MigrationType.SCHEMA) {
/* 162 */         context.schema = version;
/*     */       }
/* 164 */       if (appliedMigration.getType() == MigrationType.BASELINE) {
/* 165 */         context.baseline = version;
/*     */       }
/* 167 */       if (version != null) {
/* 168 */         appliedMigrationsMap.put(version, Pair.of(appliedMigration, Boolean.valueOf(outOfOrder))); continue;
/*     */       } 
/* 170 */       appliedRepeatableMigrations.add(appliedMigration);
/*     */     } 
/*     */ 
/*     */     
/* 174 */     Set<MigrationVersion> allVersions = new HashSet<MigrationVersion>();
/* 175 */     allVersions.addAll(resolvedMigrationsMap.keySet());
/* 176 */     allVersions.addAll(appliedMigrationsMap.keySet());
/*     */     
/* 178 */     List<MigrationInfoImpl> migrationInfos = new ArrayList<MigrationInfoImpl>();
/* 179 */     for (MigrationVersion version : allVersions) {
/* 180 */       ResolvedMigration resolvedMigration = resolvedMigrationsMap.get(version);
/* 181 */       Pair<AppliedMigration, Boolean> appliedMigrationInfo = appliedMigrationsMap.get(version);
/* 182 */       if (appliedMigrationInfo == null) {
/* 183 */         migrationInfos.add(new MigrationInfoImpl(resolvedMigration, null, context, false)); continue;
/*     */       } 
/* 185 */       migrationInfos.add(new MigrationInfoImpl(resolvedMigration, (AppliedMigration)appliedMigrationInfo.getLeft(), context, ((Boolean)appliedMigrationInfo.getRight()).booleanValue()));
/*     */     } 
/*     */ 
/*     */     
/* 189 */     Set<ResolvedMigration> pendingResolvedRepeatableMigrations = new HashSet<ResolvedMigration>(resolvedRepeatableMigrationsMap.values());
/* 190 */     for (AppliedMigration appliedRepeatableMigration : appliedRepeatableMigrations) {
/* 191 */       ResolvedMigration resolvedMigration = resolvedRepeatableMigrationsMap.get(appliedRepeatableMigration.getDescription());
/* 192 */       if (resolvedMigration != null && ObjectUtils.nullSafeEquals(appliedRepeatableMigration.getChecksum(), resolvedMigration.getChecksum())) {
/* 193 */         pendingResolvedRepeatableMigrations.remove(resolvedMigration);
/*     */       }
/* 195 */       if (!context.latestRepeatableRuns.containsKey(appliedRepeatableMigration.getDescription()) || appliedRepeatableMigration
/* 196 */         .getInstalledRank() > ((Integer)context.latestRepeatableRuns.get(appliedRepeatableMigration.getDescription())).intValue()) {
/* 197 */         context.latestRepeatableRuns.put(appliedRepeatableMigration.getDescription(), Integer.valueOf(appliedRepeatableMigration.getInstalledRank()));
/*     */       }
/* 199 */       migrationInfos.add(new MigrationInfoImpl(resolvedMigration, appliedRepeatableMigration, context, false));
/*     */     } 
/*     */     
/* 202 */     for (ResolvedMigration pendingResolvedRepeatableMigration : pendingResolvedRepeatableMigrations) {
/* 203 */       migrationInfos.add(new MigrationInfoImpl(pendingResolvedRepeatableMigration, null, context, false));
/*     */     }
/*     */     
/* 206 */     Collections.sort(migrationInfos);
/*     */     
/* 208 */     return migrationInfos;
/*     */   }
/*     */   
/*     */   public MigrationInfo[] all() {
/* 212 */     return this.migrationInfos.<MigrationInfo>toArray((MigrationInfo[])new MigrationInfoImpl[this.migrationInfos.size()]);
/*     */   }
/*     */   
/*     */   public MigrationInfo current() {
/* 216 */     for (int i = this.migrationInfos.size() - 1; i >= 0; i--) {
/* 217 */       MigrationInfo migrationInfo = this.migrationInfos.get(i);
/* 218 */       if (migrationInfo.getState().isApplied() && migrationInfo.getVersion() != null) {
/* 219 */         return migrationInfo;
/*     */       }
/*     */     } 
/*     */     
/* 223 */     return null;
/*     */   }
/*     */   
/*     */   public MigrationInfoImpl[] pending() {
/* 227 */     List<MigrationInfoImpl> pendingMigrations = new ArrayList<MigrationInfoImpl>();
/* 228 */     for (MigrationInfoImpl migrationInfo : this.migrationInfos) {
/* 229 */       if (MigrationState.PENDING == migrationInfo.getState()) {
/* 230 */         pendingMigrations.add(migrationInfo);
/*     */       }
/*     */     } 
/*     */     
/* 234 */     return pendingMigrations.<MigrationInfoImpl>toArray(new MigrationInfoImpl[pendingMigrations.size()]);
/*     */   }
/*     */   
/*     */   public MigrationInfo[] applied() {
/* 238 */     List<MigrationInfo> appliedMigrations = new ArrayList<MigrationInfo>();
/* 239 */     for (MigrationInfo migrationInfo : this.migrationInfos) {
/* 240 */       if (migrationInfo.getState().isApplied()) {
/* 241 */         appliedMigrations.add(migrationInfo);
/*     */       }
/*     */     } 
/*     */     
/* 245 */     return appliedMigrations.<MigrationInfo>toArray(new MigrationInfo[appliedMigrations.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MigrationInfo[] resolved() {
/* 254 */     List<MigrationInfo> resolvedMigrations = new ArrayList<MigrationInfo>();
/* 255 */     for (MigrationInfo migrationInfo : this.migrationInfos) {
/* 256 */       if (migrationInfo.getState().isResolved()) {
/* 257 */         resolvedMigrations.add(migrationInfo);
/*     */       }
/*     */     } 
/*     */     
/* 261 */     return resolvedMigrations.<MigrationInfo>toArray(new MigrationInfo[resolvedMigrations.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MigrationInfo[] failed() {
/* 270 */     List<MigrationInfo> failedMigrations = new ArrayList<MigrationInfo>();
/* 271 */     for (MigrationInfo migrationInfo : this.migrationInfos) {
/* 272 */       if (migrationInfo.getState().isFailed()) {
/* 273 */         failedMigrations.add(migrationInfo);
/*     */       }
/*     */     } 
/*     */     
/* 277 */     return failedMigrations.<MigrationInfo>toArray(new MigrationInfo[failedMigrations.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MigrationInfo[] future() {
/* 286 */     List<MigrationInfo> futureMigrations = new ArrayList<MigrationInfo>();
/* 287 */     for (MigrationInfo migrationInfo : this.migrationInfos) {
/* 288 */       if (migrationInfo.getState() == MigrationState.FUTURE_SUCCESS || migrationInfo
/* 289 */         .getState() == MigrationState.FUTURE_FAILED) {
/* 290 */         futureMigrations.add(migrationInfo);
/*     */       }
/*     */     } 
/*     */     
/* 294 */     return futureMigrations.<MigrationInfo>toArray(new MigrationInfo[futureMigrations.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MigrationInfo[] outOfOrder() {
/* 303 */     List<MigrationInfo> outOfOrderMigrations = new ArrayList<MigrationInfo>();
/* 304 */     for (MigrationInfo migrationInfo : this.migrationInfos) {
/* 305 */       if (migrationInfo.getState() == MigrationState.OUT_OF_ORDER) {
/* 306 */         outOfOrderMigrations.add(migrationInfo);
/*     */       }
/*     */     } 
/*     */     
/* 310 */     return outOfOrderMigrations.<MigrationInfo>toArray(new MigrationInfo[outOfOrderMigrations.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String validate() {
/* 319 */     for (MigrationInfoImpl migrationInfo : this.migrationInfos) {
/* 320 */       String message = migrationInfo.validate();
/* 321 */       if (message != null) {
/* 322 */         return message;
/*     */       }
/*     */     } 
/* 325 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\info\MigrationInfoServiceImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */