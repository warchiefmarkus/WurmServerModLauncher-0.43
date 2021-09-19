/*      */ package org.flywaydb.core;
/*      */ 
/*      */ import java.sql.Connection;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import javax.sql.DataSource;
/*      */ import org.flywaydb.core.api.FlywayException;
/*      */ import org.flywaydb.core.api.MigrationInfoService;
/*      */ import org.flywaydb.core.api.MigrationVersion;
/*      */ import org.flywaydb.core.api.callback.FlywayCallback;
/*      */ import org.flywaydb.core.api.configuration.FlywayConfiguration;
/*      */ import org.flywaydb.core.api.resolver.MigrationResolver;
/*      */ import org.flywaydb.core.internal.callback.SqlScriptFlywayCallback;
/*      */ import org.flywaydb.core.internal.command.DbBaseline;
/*      */ import org.flywaydb.core.internal.command.DbClean;
/*      */ import org.flywaydb.core.internal.command.DbMigrate;
/*      */ import org.flywaydb.core.internal.command.DbRepair;
/*      */ import org.flywaydb.core.internal.command.DbSchemas;
/*      */ import org.flywaydb.core.internal.command.DbValidate;
/*      */ import org.flywaydb.core.internal.dbsupport.DbSupport;
/*      */ import org.flywaydb.core.internal.dbsupport.DbSupportFactory;
/*      */ import org.flywaydb.core.internal.dbsupport.Schema;
/*      */ import org.flywaydb.core.internal.info.MigrationInfoServiceImpl;
/*      */ import org.flywaydb.core.internal.metadatatable.MetaDataTable;
/*      */ import org.flywaydb.core.internal.metadatatable.MetaDataTableImpl;
/*      */ import org.flywaydb.core.internal.resolver.CompositeMigrationResolver;
/*      */ import org.flywaydb.core.internal.util.ClassUtils;
/*      */ import org.flywaydb.core.internal.util.ConfigurationInjectionUtils;
/*      */ import org.flywaydb.core.internal.util.Location;
/*      */ import org.flywaydb.core.internal.util.Locations;
/*      */ import org.flywaydb.core.internal.util.PlaceholderReplacer;
/*      */ import org.flywaydb.core.internal.util.StringUtils;
/*      */ import org.flywaydb.core.internal.util.VersionPrinter;
/*      */ import org.flywaydb.core.internal.util.jdbc.DriverDataSource;
/*      */ import org.flywaydb.core.internal.util.jdbc.JdbcUtils;
/*      */ import org.flywaydb.core.internal.util.jdbc.TransactionCallback;
/*      */ import org.flywaydb.core.internal.util.jdbc.TransactionTemplate;
/*      */ import org.flywaydb.core.internal.util.logging.Log;
/*      */ import org.flywaydb.core.internal.util.logging.LogFactory;
/*      */ import org.flywaydb.core.internal.util.scanner.Scanner;
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
/*      */ public class Flyway
/*      */   implements FlywayConfiguration
/*      */ {
/*   73 */   private static final Log LOG = LogFactory.getLog(Flyway.class);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String PLACEHOLDERS_PROPERTY_PREFIX = "flyway.placeholders.";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   91 */   private Locations locations = new Locations(new String[] { "db/migration" });
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   96 */   private String encoding = "UTF-8";
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
/*  107 */   private String[] schemaNames = new String[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  115 */   private String table = "schema_version";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  121 */   private MigrationVersion target = MigrationVersion.LATEST;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean placeholderReplacement = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  131 */   private Map<String, String> placeholders = new HashMap<String, String>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  136 */   private String placeholderPrefix = "${";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  141 */   private String placeholderSuffix = "}";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  149 */   private String sqlMigrationPrefix = "V";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  157 */   private String repeatableSqlMigrationPrefix = "R";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  165 */   private String sqlMigrationSeparator = "__";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  173 */   private String sqlMigrationSuffix = ".sql";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean ignoreFutureMigrations = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   private boolean ignoreFailedFutureMigration;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean validateOnMigrate = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean cleanOnValidationError;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean cleanDisabled;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  223 */   private MigrationVersion baselineVersion = MigrationVersion.fromVersion("1");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  228 */   private String baselineDescription = "<< Flyway Baseline >>";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean baselineOnMigrate;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean outOfOrder;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  258 */   private FlywayCallback[] callbacks = new FlywayCallback[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean skipDefaultCallbacks;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  270 */   private MigrationResolver[] resolvers = new MigrationResolver[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean skipDefaultResolvers;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean createdDataSource;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private DataSource dataSource;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  291 */   private ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean dbConnectionInfoPrinted;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getLocations() {
/*  307 */     String[] result = new String[this.locations.getLocations().size()];
/*  308 */     for (int i = 0; i < this.locations.getLocations().size(); i++) {
/*  309 */       result[i] = ((Location)this.locations.getLocations().get(i)).toString();
/*      */     }
/*  311 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getEncoding() {
/*  316 */     return this.encoding;
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getSchemas() {
/*  321 */     return this.schemaNames;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getTable() {
/*  326 */     return this.table;
/*      */   }
/*      */ 
/*      */   
/*      */   public MigrationVersion getTarget() {
/*  331 */     return this.target;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPlaceholderReplacement() {
/*  340 */     return this.placeholderReplacement;
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, String> getPlaceholders() {
/*  345 */     return this.placeholders;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getPlaceholderPrefix() {
/*  350 */     return this.placeholderPrefix;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getPlaceholderSuffix() {
/*  355 */     return this.placeholderSuffix;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getSqlMigrationPrefix() {
/*  360 */     return this.sqlMigrationPrefix;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getRepeatableSqlMigrationPrefix() {
/*  365 */     return this.repeatableSqlMigrationPrefix;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getSqlMigrationSeparator() {
/*  370 */     return this.sqlMigrationSeparator;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getSqlMigrationSuffix() {
/*  375 */     return this.sqlMigrationSuffix;
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
/*      */   public boolean isIgnoreFutureMigrations() {
/*  390 */     return this.ignoreFutureMigrations;
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
/*      */   @Deprecated
/*      */   public boolean isIgnoreFailedFutureMigration() {
/*  408 */     LOG.warn("ignoreFailedFutureMigration has been deprecated and will be removed in Flyway 5.0. Use the more generic ignoreFutureMigrations instead.");
/*  409 */     return this.ignoreFailedFutureMigration;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isValidateOnMigrate() {
/*  418 */     return this.validateOnMigrate;
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
/*      */   public boolean isCleanOnValidationError() {
/*  432 */     return this.cleanOnValidationError;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCleanDisabled() {
/*  442 */     return this.cleanDisabled;
/*      */   }
/*      */ 
/*      */   
/*      */   public MigrationVersion getBaselineVersion() {
/*  447 */     return this.baselineVersion;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getBaselineDescription() {
/*  452 */     return this.baselineDescription;
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
/*      */   public boolean isBaselineOnMigrate() {
/*  472 */     return this.baselineOnMigrate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isOutOfOrder() {
/*  483 */     return this.outOfOrder;
/*      */   }
/*      */ 
/*      */   
/*      */   public MigrationResolver[] getResolvers() {
/*  488 */     return this.resolvers;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSkipDefaultResolvers() {
/*  493 */     return this.skipDefaultResolvers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DataSource getDataSource() {
/*  503 */     return this.dataSource;
/*      */   }
/*      */ 
/*      */   
/*      */   public ClassLoader getClassLoader() {
/*  508 */     return this.classLoader;
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
/*      */   public void setIgnoreFutureMigrations(boolean ignoreFutureMigrations) {
/*  523 */     this.ignoreFutureMigrations = ignoreFutureMigrations;
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
/*      */   @Deprecated
/*      */   public void setIgnoreFailedFutureMigration(boolean ignoreFailedFutureMigration) {
/*  541 */     LOG.warn("ignoreFailedFutureMigration has been deprecated and will be removed in Flyway 5.0. Use the more generic ignoreFutureMigrations instead.");
/*  542 */     this.ignoreFailedFutureMigration = ignoreFailedFutureMigration;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setValidateOnMigrate(boolean validateOnMigrate) {
/*  551 */     this.validateOnMigrate = validateOnMigrate;
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
/*      */   public void setCleanOnValidationError(boolean cleanOnValidationError) {
/*  565 */     this.cleanOnValidationError = cleanOnValidationError;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCleanDisabled(boolean cleanDisabled) {
/*  575 */     this.cleanDisabled = cleanDisabled;
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
/*      */   public void setLocations(String... locations) {
/*  590 */     this.locations = new Locations(locations);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEncoding(String encoding) {
/*  599 */     this.encoding = encoding;
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
/*      */   public void setSchemas(String... schemas) {
/*  614 */     this.schemaNames = schemas;
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
/*      */   public void setTable(String table) {
/*  626 */     this.table = table;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTarget(MigrationVersion target) {
/*  636 */     this.target = target;
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
/*      */   public void setTargetAsString(String target) {
/*  648 */     this.target = MigrationVersion.fromVersion(target);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPlaceholderReplacement(boolean placeholderReplacement) {
/*  657 */     this.placeholderReplacement = placeholderReplacement;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPlaceholders(Map<String, String> placeholders) {
/*  666 */     this.placeholders = placeholders;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPlaceholderPrefix(String placeholderPrefix) {
/*  675 */     if (!StringUtils.hasLength(placeholderPrefix)) {
/*  676 */       throw new FlywayException("placeholderPrefix cannot be empty!");
/*      */     }
/*  678 */     this.placeholderPrefix = placeholderPrefix;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPlaceholderSuffix(String placeholderSuffix) {
/*  687 */     if (!StringUtils.hasLength(placeholderSuffix)) {
/*  688 */       throw new FlywayException("placeholderSuffix cannot be empty!");
/*      */     }
/*  690 */     this.placeholderSuffix = placeholderSuffix;
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
/*      */   public void setSqlMigrationPrefix(String sqlMigrationPrefix) {
/*  702 */     this.sqlMigrationPrefix = sqlMigrationPrefix;
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
/*      */   public void setRepeatableSqlMigrationPrefix(String repeatableSqlMigrationPrefix) {
/*  714 */     this.repeatableSqlMigrationPrefix = repeatableSqlMigrationPrefix;
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
/*      */   public void setSqlMigrationSeparator(String sqlMigrationSeparator) {
/*  726 */     if (!StringUtils.hasLength(sqlMigrationSeparator)) {
/*  727 */       throw new FlywayException("sqlMigrationSeparator cannot be empty!");
/*      */     }
/*      */     
/*  730 */     this.sqlMigrationSeparator = sqlMigrationSeparator;
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
/*      */   public void setSqlMigrationSuffix(String sqlMigrationSuffix) {
/*  742 */     this.sqlMigrationSuffix = sqlMigrationSuffix;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDataSource(DataSource dataSource) {
/*  751 */     this.dataSource = dataSource;
/*  752 */     this.createdDataSource = false;
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
/*      */   public void setDataSource(String url, String user, String password, String... initSqls) {
/*  766 */     this.dataSource = (DataSource)new DriverDataSource(this.classLoader, null, url, user, password, initSqls);
/*  767 */     this.createdDataSource = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClassLoader(ClassLoader classLoader) {
/*  776 */     this.classLoader = classLoader;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBaselineVersion(MigrationVersion baselineVersion) {
/*  785 */     this.baselineVersion = baselineVersion;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBaselineVersionAsString(String baselineVersion) {
/*  794 */     this.baselineVersion = MigrationVersion.fromVersion(baselineVersion);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBaselineDescription(String baselineDescription) {
/*  803 */     this.baselineDescription = baselineDescription;
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
/*      */   public void setBaselineOnMigrate(boolean baselineOnMigrate) {
/*  823 */     this.baselineOnMigrate = baselineOnMigrate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOutOfOrder(boolean outOfOrder) {
/*  834 */     this.outOfOrder = outOfOrder;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FlywayCallback[] getCallbacks() {
/*  844 */     return this.callbacks;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSkipDefaultCallbacks() {
/*  849 */     return this.skipDefaultCallbacks;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCallbacks(FlywayCallback... callbacks) {
/*  858 */     this.callbacks = callbacks;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCallbacksAsClassNames(String... callbacks) {
/*  867 */     List<FlywayCallback> callbackList = ClassUtils.instantiateAll(callbacks, this.classLoader);
/*  868 */     setCallbacks(callbackList.<FlywayCallback>toArray(new FlywayCallback[callbacks.length]));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSkipDefaultCallbacks(boolean skipDefaultCallbacks) {
/*  877 */     this.skipDefaultCallbacks = skipDefaultCallbacks;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setResolvers(MigrationResolver... resolvers) {
/*  886 */     this.resolvers = resolvers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setResolversAsClassNames(String... resolvers) {
/*  895 */     List<MigrationResolver> resolverList = ClassUtils.instantiateAll(resolvers, this.classLoader);
/*  896 */     setResolvers(resolverList.<MigrationResolver>toArray(new MigrationResolver[resolvers.length]));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSkipDefaultResolvers(boolean skipDefaultResolvers) {
/*  905 */     this.skipDefaultResolvers = skipDefaultResolvers;
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
/*      */   public int migrate() throws FlywayException {
/*  917 */     return ((Integer)execute(new Command<Integer>()
/*      */         {
/*      */           public Integer execute(Connection connectionMetaDataTable, Connection connectionUserObjects, MigrationResolver migrationResolver, MetaDataTable metaDataTable, DbSupport dbSupport, Schema[] schemas, FlywayCallback[] flywayCallbacks) {
/*  920 */             if (Flyway.this.validateOnMigrate) {
/*  921 */               Flyway.this.doValidate(connectionMetaDataTable, dbSupport, migrationResolver, metaDataTable, schemas, flywayCallbacks, true);
/*      */             }
/*      */             
/*  924 */             (new DbSchemas(connectionMetaDataTable, schemas, metaDataTable)).create();
/*      */             
/*  926 */             if (!metaDataTable.hasSchemasMarker() && !metaDataTable.hasBaselineMarker() && !metaDataTable.hasAppliedMigrations()) {
/*  927 */               List<Schema> nonEmptySchemas = new ArrayList<Schema>();
/*  928 */               for (Schema schema : schemas) {
/*  929 */                 if (!schema.empty()) {
/*  930 */                   nonEmptySchemas.add(schema);
/*      */                 }
/*      */               } 
/*      */               
/*  934 */               if (Flyway.this.baselineOnMigrate || nonEmptySchemas.isEmpty()) {
/*  935 */                 if (Flyway.this.baselineOnMigrate && !nonEmptySchemas.isEmpty()) {
/*  936 */                   (new DbBaseline(connectionMetaDataTable, dbSupport, metaDataTable, schemas[0], Flyway.this.baselineVersion, Flyway.this.baselineDescription, flywayCallbacks)).baseline();
/*      */                 }
/*      */               }
/*  939 */               else if (nonEmptySchemas.size() == 1) {
/*  940 */                 Schema schema = nonEmptySchemas.get(0);
/*      */                 
/*  942 */                 if ((schema.allTables()).length != 1 || !schema.getTable(Flyway.this.table).exists()) {
/*  943 */                   throw new FlywayException("Found non-empty schema " + schema + " without metadata table! Use baseline()" + " or set baselineOnMigrate to true to initialize the metadata table.");
/*      */                 }
/*      */               }
/*      */               else {
/*      */                 
/*  948 */                 throw new FlywayException("Found non-empty schemas " + 
/*  949 */                     StringUtils.collectionToCommaDelimitedString(nonEmptySchemas) + " without metadata table! Use baseline()" + " or set baselineOnMigrate to true to initialize the metadata table.");
/*      */               } 
/*      */             } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  958 */             DbMigrate dbMigrate = new DbMigrate(connectionMetaDataTable, connectionUserObjects, dbSupport, metaDataTable, schemas[0], migrationResolver, Flyway.this.target, Flyway.this.ignoreFutureMigrations, Flyway.this.ignoreFailedFutureMigration, Flyway.this.outOfOrder, flywayCallbacks);
/*  959 */             return Integer.valueOf(dbMigrate.migrate());
/*      */           }
/*      */         })).intValue();
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
/*      */   public void validate() throws FlywayException {
/*  979 */     execute(new Command<Void>()
/*      */         {
/*      */           public Void execute(Connection connectionMetaDataTable, Connection connectionUserObjects, MigrationResolver migrationResolver, MetaDataTable metaDataTable, DbSupport dbSupport, Schema[] schemas, FlywayCallback[] flywayCallbacks) {
/*  982 */             Flyway.this.doValidate(connectionMetaDataTable, dbSupport, migrationResolver, metaDataTable, schemas, flywayCallbacks, false);
/*  983 */             return null;
/*      */           }
/*      */         });
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
/*      */   private void doValidate(Connection connectionMetaDataTable, DbSupport dbSupport, MigrationResolver migrationResolver, MetaDataTable metaDataTable, Schema[] schemas, FlywayCallback[] flywayCallbacks, boolean pending) {
/* 1002 */     String validationError = (new DbValidate(connectionMetaDataTable, dbSupport, metaDataTable, schemas[0], migrationResolver, this.target, this.outOfOrder, pending, this.ignoreFutureMigrations, flywayCallbacks)).validate();
/*      */     
/* 1004 */     if (validationError != null) {
/* 1005 */       if (this.cleanOnValidationError) {
/* 1006 */         (new DbClean(connectionMetaDataTable, dbSupport, metaDataTable, schemas, flywayCallbacks, this.cleanDisabled)).clean();
/*      */       } else {
/* 1008 */         throw new FlywayException("Validate failed: " + validationError);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clean() {
/* 1021 */     execute(new Command<Void>()
/*      */         {
/*      */           public Void execute(Connection connectionMetaDataTable, Connection connectionUserObjects, MigrationResolver migrationResolver, MetaDataTable metaDataTable, DbSupport dbSupport, Schema[] schemas, FlywayCallback[] flywayCallbacks)
/*      */           {
/* 1025 */             (new DbClean(connectionMetaDataTable, dbSupport, metaDataTable, schemas, flywayCallbacks, Flyway.this.cleanDisabled)).clean();
/* 1026 */             return null;
/*      */           }
/*      */         });
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
/*      */   public MigrationInfoService info() {
/* 1040 */     return execute(new Command<MigrationInfoService>()
/*      */         {
/*      */           public MigrationInfoService execute(final Connection connectionMetaDataTable, Connection connectionUserObjects, MigrationResolver migrationResolver, MetaDataTable metaDataTable, final DbSupport dbSupport, final Schema[] schemas, FlywayCallback[] flywayCallbacks) {
/*      */             try {
/* 1044 */               for (FlywayCallback callback : flywayCallbacks) {
/* 1045 */                 (new TransactionTemplate(connectionMetaDataTable)).execute(new TransactionCallback<Object>()
/*      */                     {
/*      */                       public Object doInTransaction() throws SQLException {
/* 1048 */                         dbSupport.changeCurrentSchemaTo(schemas[0]);
/* 1049 */                         callback.beforeInfo(connectionMetaDataTable);
/* 1050 */                         return null;
/*      */                       }
/*      */                     });
/*      */               } 
/*      */ 
/*      */               
/* 1056 */               MigrationInfoServiceImpl migrationInfoService = new MigrationInfoServiceImpl(migrationResolver, metaDataTable, Flyway.this.target, Flyway.this.outOfOrder, true, true);
/* 1057 */               migrationInfoService.refresh();
/*      */               
/* 1059 */               for (FlywayCallback callback : flywayCallbacks) {
/* 1060 */                 (new TransactionTemplate(connectionMetaDataTable)).execute(new TransactionCallback<Object>()
/*      */                     {
/*      */                       public Object doInTransaction() throws SQLException {
/* 1063 */                         dbSupport.changeCurrentSchemaTo(schemas[0]);
/* 1064 */                         callback.afterInfo(connectionMetaDataTable);
/* 1065 */                         return null;
/*      */                       }
/*      */                     });
/*      */               } 
/*      */               
/* 1070 */               return (MigrationInfoService)migrationInfoService;
/*      */             } finally {
/* 1072 */               dbSupport.restoreCurrentSchema();
/*      */             } 
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void baseline() throws FlywayException {
/* 1086 */     execute(new Command<Void>() {
/*      */           public Void execute(Connection connectionMetaDataTable, Connection connectionUserObjects, MigrationResolver migrationResolver, MetaDataTable metaDataTable, DbSupport dbSupport, Schema[] schemas, FlywayCallback[] flywayCallbacks) {
/* 1088 */             (new DbSchemas(connectionMetaDataTable, schemas, metaDataTable)).create();
/* 1089 */             (new DbBaseline(connectionMetaDataTable, dbSupport, metaDataTable, schemas[0], Flyway.this.baselineVersion, Flyway.this.baselineDescription, flywayCallbacks)).baseline();
/* 1090 */             return null;
/*      */           }
/*      */         });
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
/*      */   public void repair() throws FlywayException {
/* 1106 */     execute(new Command<Void>() {
/*      */           public Void execute(Connection connectionMetaDataTable, Connection connectionUserObjects, MigrationResolver migrationResolver, MetaDataTable metaDataTable, DbSupport dbSupport, Schema[] schemas, FlywayCallback[] flywayCallbacks) {
/* 1108 */             (new DbRepair(dbSupport, connectionMetaDataTable, schemas[0], migrationResolver, metaDataTable, flywayCallbacks)).repair();
/* 1109 */             return null;
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private MigrationResolver createMigrationResolver(DbSupport dbSupport, Scanner scanner) {
/* 1122 */     for (MigrationResolver resolver : this.resolvers) {
/* 1123 */       ConfigurationInjectionUtils.injectFlywayConfiguration(resolver, this);
/*      */     }
/*      */     
/* 1126 */     return (MigrationResolver)new CompositeMigrationResolver(dbSupport, scanner, this, this.locations, this.encoding, this.sqlMigrationPrefix, this.repeatableSqlMigrationPrefix, this.sqlMigrationSeparator, this.sqlMigrationSuffix, 
/*      */         
/* 1128 */         createPlaceholderReplacer(), this.resolvers);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private PlaceholderReplacer createPlaceholderReplacer() {
/* 1135 */     if (this.placeholderReplacement) {
/* 1136 */       return new PlaceholderReplacer(this.placeholders, this.placeholderPrefix, this.placeholderSuffix);
/*      */     }
/* 1138 */     return PlaceholderReplacer.NO_PLACEHOLDERS;
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
/*      */   public void configure(Properties properties) {
/* 1152 */     Map<String, String> props = new HashMap<String, String>();
/* 1153 */     for (Map.Entry<Object, Object> entry : properties.entrySet()) {
/* 1154 */       props.put(entry.getKey().toString(), entry.getValue().toString());
/*      */     }
/*      */     
/* 1157 */     String driverProp = getValueAndRemoveEntry(props, "flyway.driver");
/* 1158 */     String urlProp = getValueAndRemoveEntry(props, "flyway.url");
/* 1159 */     String userProp = getValueAndRemoveEntry(props, "flyway.user");
/* 1160 */     String passwordProp = getValueAndRemoveEntry(props, "flyway.password");
/*      */     
/* 1162 */     if (StringUtils.hasText(urlProp)) {
/* 1163 */       setDataSource((DataSource)new DriverDataSource(this.classLoader, driverProp, urlProp, userProp, passwordProp, new String[0]));
/* 1164 */     } else if (!StringUtils.hasText(urlProp) && (
/* 1165 */       StringUtils.hasText(driverProp) || StringUtils.hasText(userProp) || StringUtils.hasText(passwordProp))) {
/* 1166 */       LOG.warn("Discarding INCOMPLETE dataSource configuration! flyway.url must be set.");
/*      */     } 
/*      */     
/* 1169 */     String locationsProp = getValueAndRemoveEntry(props, "flyway.locations");
/* 1170 */     if (locationsProp != null) {
/* 1171 */       setLocations(StringUtils.tokenizeToStringArray(locationsProp, ","));
/*      */     }
/* 1173 */     String placeholderReplacementProp = getValueAndRemoveEntry(props, "flyway.placeholderReplacement");
/* 1174 */     if (placeholderReplacementProp != null) {
/* 1175 */       setPlaceholderReplacement(Boolean.parseBoolean(placeholderReplacementProp));
/*      */     }
/* 1177 */     String placeholderPrefixProp = getValueAndRemoveEntry(props, "flyway.placeholderPrefix");
/* 1178 */     if (placeholderPrefixProp != null) {
/* 1179 */       setPlaceholderPrefix(placeholderPrefixProp);
/*      */     }
/* 1181 */     String placeholderSuffixProp = getValueAndRemoveEntry(props, "flyway.placeholderSuffix");
/* 1182 */     if (placeholderSuffixProp != null) {
/* 1183 */       setPlaceholderSuffix(placeholderSuffixProp);
/*      */     }
/* 1185 */     String sqlMigrationPrefixProp = getValueAndRemoveEntry(props, "flyway.sqlMigrationPrefix");
/* 1186 */     if (sqlMigrationPrefixProp != null) {
/* 1187 */       setSqlMigrationPrefix(sqlMigrationPrefixProp);
/*      */     }
/* 1189 */     String repeatableSqlMigrationPrefixProp = getValueAndRemoveEntry(props, "flyway.repeatableSqlMigrationPrefix");
/* 1190 */     if (repeatableSqlMigrationPrefixProp != null) {
/* 1191 */       setRepeatableSqlMigrationPrefix(repeatableSqlMigrationPrefixProp);
/*      */     }
/* 1193 */     String sqlMigrationSeparatorProp = getValueAndRemoveEntry(props, "flyway.sqlMigrationSeparator");
/* 1194 */     if (sqlMigrationSeparatorProp != null) {
/* 1195 */       setSqlMigrationSeparator(sqlMigrationSeparatorProp);
/*      */     }
/* 1197 */     String sqlMigrationSuffixProp = getValueAndRemoveEntry(props, "flyway.sqlMigrationSuffix");
/* 1198 */     if (sqlMigrationSuffixProp != null) {
/* 1199 */       setSqlMigrationSuffix(sqlMigrationSuffixProp);
/*      */     }
/* 1201 */     String encodingProp = getValueAndRemoveEntry(props, "flyway.encoding");
/* 1202 */     if (encodingProp != null) {
/* 1203 */       setEncoding(encodingProp);
/*      */     }
/* 1205 */     String schemasProp = getValueAndRemoveEntry(props, "flyway.schemas");
/* 1206 */     if (schemasProp != null) {
/* 1207 */       setSchemas(StringUtils.tokenizeToStringArray(schemasProp, ","));
/*      */     }
/* 1209 */     String tableProp = getValueAndRemoveEntry(props, "flyway.table");
/* 1210 */     if (tableProp != null) {
/* 1211 */       setTable(tableProp);
/*      */     }
/* 1213 */     String cleanOnValidationErrorProp = getValueAndRemoveEntry(props, "flyway.cleanOnValidationError");
/* 1214 */     if (cleanOnValidationErrorProp != null) {
/* 1215 */       setCleanOnValidationError(Boolean.parseBoolean(cleanOnValidationErrorProp));
/*      */     }
/* 1217 */     String cleanDisabledProp = getValueAndRemoveEntry(props, "flyway.cleanDisabled");
/* 1218 */     if (cleanDisabledProp != null) {
/* 1219 */       setCleanDisabled(Boolean.parseBoolean(cleanDisabledProp));
/*      */     }
/* 1221 */     String validateOnMigrateProp = getValueAndRemoveEntry(props, "flyway.validateOnMigrate");
/* 1222 */     if (validateOnMigrateProp != null) {
/* 1223 */       setValidateOnMigrate(Boolean.parseBoolean(validateOnMigrateProp));
/*      */     }
/* 1225 */     String baselineVersionProp = getValueAndRemoveEntry(props, "flyway.baselineVersion");
/* 1226 */     if (baselineVersionProp != null) {
/* 1227 */       setBaselineVersion(MigrationVersion.fromVersion(baselineVersionProp));
/*      */     }
/* 1229 */     String baselineDescriptionProp = getValueAndRemoveEntry(props, "flyway.baselineDescription");
/* 1230 */     if (baselineDescriptionProp != null) {
/* 1231 */       setBaselineDescription(baselineDescriptionProp);
/*      */     }
/* 1233 */     String baselineOnMigrateProp = getValueAndRemoveEntry(props, "flyway.baselineOnMigrate");
/* 1234 */     if (baselineOnMigrateProp != null) {
/* 1235 */       setBaselineOnMigrate(Boolean.parseBoolean(baselineOnMigrateProp));
/*      */     }
/* 1237 */     String ignoreFutureMigrationsProp = getValueAndRemoveEntry(props, "flyway.ignoreFutureMigrations");
/* 1238 */     if (ignoreFutureMigrationsProp != null) {
/* 1239 */       setIgnoreFutureMigrations(Boolean.parseBoolean(ignoreFutureMigrationsProp));
/*      */     }
/* 1241 */     String ignoreFailedFutureMigrationProp = getValueAndRemoveEntry(props, "flyway.ignoreFailedFutureMigration");
/* 1242 */     if (ignoreFailedFutureMigrationProp != null) {
/* 1243 */       setIgnoreFailedFutureMigration(Boolean.parseBoolean(ignoreFailedFutureMigrationProp));
/*      */     }
/* 1245 */     String targetProp = getValueAndRemoveEntry(props, "flyway.target");
/* 1246 */     if (targetProp != null) {
/* 1247 */       setTarget(MigrationVersion.fromVersion(targetProp));
/*      */     }
/* 1249 */     String outOfOrderProp = getValueAndRemoveEntry(props, "flyway.outOfOrder");
/* 1250 */     if (outOfOrderProp != null) {
/* 1251 */       setOutOfOrder(Boolean.parseBoolean(outOfOrderProp));
/*      */     }
/* 1253 */     String resolversProp = getValueAndRemoveEntry(props, "flyway.resolvers");
/* 1254 */     if (StringUtils.hasLength(resolversProp)) {
/* 1255 */       setResolversAsClassNames(StringUtils.tokenizeToStringArray(resolversProp, ","));
/*      */     }
/* 1257 */     String skipDefaultResolversProp = getValueAndRemoveEntry(props, "flyway.skipDefaultResolvers");
/* 1258 */     if (skipDefaultResolversProp != null) {
/* 1259 */       setSkipDefaultResolvers(Boolean.parseBoolean(skipDefaultResolversProp));
/*      */     }
/* 1261 */     String callbacksProp = getValueAndRemoveEntry(props, "flyway.callbacks");
/* 1262 */     if (StringUtils.hasLength(callbacksProp)) {
/* 1263 */       setCallbacksAsClassNames(StringUtils.tokenizeToStringArray(callbacksProp, ","));
/*      */     }
/* 1265 */     String skipDefaultCallbacksProp = getValueAndRemoveEntry(props, "flyway.skipDefaultCallbacks");
/* 1266 */     if (skipDefaultCallbacksProp != null) {
/* 1267 */       setSkipDefaultCallbacks(Boolean.parseBoolean(skipDefaultCallbacksProp));
/*      */     }
/*      */     
/* 1270 */     Map<String, String> placeholdersFromProps = new HashMap<String, String>(this.placeholders);
/* 1271 */     Iterator<Map.Entry<String, String>> iterator = props.entrySet().iterator();
/* 1272 */     while (iterator.hasNext()) {
/* 1273 */       Map.Entry<String, String> entry = iterator.next();
/* 1274 */       String propertyName = entry.getKey();
/*      */       
/* 1276 */       if (propertyName.startsWith("flyway.placeholders.") && propertyName
/* 1277 */         .length() > "flyway.placeholders.".length()) {
/* 1278 */         String placeholderName = propertyName.substring("flyway.placeholders.".length());
/* 1279 */         String placeholderValue = entry.getValue();
/* 1280 */         placeholdersFromProps.put(placeholderName, placeholderValue);
/* 1281 */         iterator.remove();
/*      */       } 
/*      */     } 
/* 1284 */     setPlaceholders(placeholdersFromProps);
/*      */     
/* 1286 */     for (String key : props.keySet()) {
/* 1287 */       if (key.startsWith("flyway.")) {
/* 1288 */         LOG.warn("Unknown configuration property: " + key);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String getValueAndRemoveEntry(Map<String, String> map, String key) {
/* 1301 */     String value = map.get(key);
/* 1302 */     map.remove(key);
/* 1303 */     return value;
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
/*      */   <T> T execute(Command<T> command) {
/*      */     T result;
/* 1316 */     VersionPrinter.printVersion();
/*      */     
/* 1318 */     Connection connectionMetaDataTable = null;
/* 1319 */     Connection connectionUserObjects = null;
/*      */     
/*      */     try {
/* 1322 */       if (this.dataSource == null) {
/* 1323 */         throw new FlywayException("Unable to connect to the database. Configure the url, user and password!");
/*      */       }
/*      */       
/* 1326 */       connectionMetaDataTable = JdbcUtils.openConnection(this.dataSource);
/* 1327 */       connectionUserObjects = JdbcUtils.openConnection(this.dataSource);
/*      */       
/* 1329 */       DbSupport dbSupport = DbSupportFactory.createDbSupport(connectionMetaDataTable, !this.dbConnectionInfoPrinted);
/* 1330 */       this.dbConnectionInfoPrinted = true;
/* 1331 */       LOG.debug("DDL Transactions Supported: " + dbSupport.supportsDdlTransactions());
/*      */       
/* 1333 */       if (this.schemaNames.length == 0) {
/* 1334 */         Schema currentSchema = dbSupport.getOriginalSchema();
/* 1335 */         if (currentSchema == null) {
/* 1336 */           throw new FlywayException("Unable to determine schema for the metadata table. Set a default schema for the connection or specify one using the schemas property!");
/*      */         }
/*      */         
/* 1339 */         setSchemas(new String[] { currentSchema.getName() });
/*      */       } 
/*      */       
/* 1342 */       if (this.schemaNames.length == 1) {
/* 1343 */         LOG.debug("Schema: " + this.schemaNames[0]);
/*      */       } else {
/* 1345 */         LOG.debug("Schemas: " + StringUtils.arrayToCommaDelimitedString((Object[])this.schemaNames));
/*      */       } 
/*      */       
/* 1348 */       Schema[] schemas = new Schema[this.schemaNames.length];
/* 1349 */       for (int i = 0; i < this.schemaNames.length; i++) {
/* 1350 */         schemas[i] = dbSupport.getSchema(this.schemaNames[i]);
/*      */       }
/*      */       
/* 1353 */       Scanner scanner = new Scanner(this.classLoader);
/* 1354 */       MigrationResolver migrationResolver = createMigrationResolver(dbSupport, scanner);
/*      */       
/* 1356 */       Set<FlywayCallback> flywayCallbacks = new LinkedHashSet<FlywayCallback>(Arrays.asList(this.callbacks));
/* 1357 */       if (!this.skipDefaultCallbacks) {
/* 1358 */         flywayCallbacks.add(new SqlScriptFlywayCallback(dbSupport, scanner, this.locations, createPlaceholderReplacer(), this.encoding, this.sqlMigrationSuffix));
/*      */       }
/*      */ 
/*      */       
/* 1362 */       for (FlywayCallback callback : flywayCallbacks) {
/* 1363 */         ConfigurationInjectionUtils.injectFlywayConfiguration(callback, this);
/*      */       }
/*      */       
/* 1366 */       FlywayCallback[] flywayCallbacksArray = flywayCallbacks.<FlywayCallback>toArray(new FlywayCallback[flywayCallbacks.size()]);
/* 1367 */       MetaDataTableImpl metaDataTableImpl = new MetaDataTableImpl(dbSupport, schemas[0].getTable(this.table));
/* 1368 */       if (metaDataTableImpl.upgradeIfNecessary()) {
/* 1369 */         (new DbRepair(dbSupport, connectionMetaDataTable, schemas[0], migrationResolver, (MetaDataTable)metaDataTableImpl, flywayCallbacksArray)).repairChecksums();
/* 1370 */         LOG.info("Metadata table " + this.table + " successfully upgraded to the Flyway 4.0 format.");
/*      */       } 
/*      */       
/* 1373 */       result = command.execute(connectionMetaDataTable, connectionUserObjects, migrationResolver, (MetaDataTable)metaDataTableImpl, dbSupport, schemas, flywayCallbacksArray);
/*      */     } finally {
/* 1375 */       JdbcUtils.closeConnection(connectionUserObjects);
/* 1376 */       JdbcUtils.closeConnection(connectionMetaDataTable);
/*      */       
/* 1378 */       if (this.dataSource instanceof DriverDataSource && this.createdDataSource) {
/* 1379 */         ((DriverDataSource)this.dataSource).close();
/*      */       }
/*      */     } 
/* 1382 */     return result;
/*      */   }
/*      */   
/*      */   static interface Command<T> {
/*      */     T execute(Connection param1Connection1, Connection param1Connection2, MigrationResolver param1MigrationResolver, MetaDataTable param1MetaDataTable, DbSupport param1DbSupport, Schema[] param1ArrayOfSchema, FlywayCallback[] param1ArrayOfFlywayCallback);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\Flyway.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */