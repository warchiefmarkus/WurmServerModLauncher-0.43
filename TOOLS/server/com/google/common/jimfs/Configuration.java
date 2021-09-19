/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.Nullable;
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
/*     */ public final class Configuration
/*     */ {
/*     */   final PathType pathType;
/*     */   final ImmutableSet<PathNormalization> nameDisplayNormalization;
/*     */   final ImmutableSet<PathNormalization> nameCanonicalNormalization;
/*     */   final boolean pathEqualityUsesCanonicalForm;
/*     */   final int blockSize;
/*     */   final long maxSize;
/*     */   final long maxCacheSize;
/*     */   final ImmutableSet<String> attributeViews;
/*     */   final ImmutableSet<AttributeProvider> attributeProviders;
/*     */   final ImmutableMap<String, Object> defaultAttributeValues;
/*     */   final WatchServiceConfiguration watchServiceConfig;
/*     */   final ImmutableSet<String> roots;
/*     */   final String workingDirectory;
/*     */   final ImmutableSet<Feature> supportedFeatures;
/*     */   
/*     */   public static Configuration unix() {
/*  86 */     return UnixHolder.UNIX;
/*     */   }
/*     */   
/*     */   private static final class UnixHolder {
/*  90 */     private static final Configuration UNIX = Configuration.builder(PathType.unix()).setRoots("/", new String[0]).setWorkingDirectory("/work").setAttributeViews("basic", new String[0]).setSupportedFeatures(new Feature[] { Feature.LINKS, Feature.SYMBOLIC_LINKS, Feature.SECURE_DIRECTORY_STREAM, Feature.FILE_CHANNEL }).build();
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
/*     */   public static Configuration osX() {
/* 133 */     return OsxHolder.OS_X;
/*     */   }
/*     */   
/*     */   private static final class OsxHolder {
/* 137 */     private static final Configuration OS_X = Configuration.unix().toBuilder().setNameDisplayNormalization(PathNormalization.NFC, new PathNormalization[0]).setNameCanonicalNormalization(PathNormalization.NFD, new PathNormalization[] { PathNormalization.CASE_FOLD_ASCII }).setSupportedFeatures(new Feature[] { Feature.LINKS, Feature.SYMBOLIC_LINKS, Feature.FILE_CHANNEL }).build();
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
/*     */   public static Configuration windows() {
/* 175 */     return WindowsHolder.WINDOWS;
/*     */   }
/*     */   
/*     */   private static final class WindowsHolder {
/* 179 */     private static final Configuration WINDOWS = Configuration.builder(PathType.windows()).setRoots("C:\\", new String[0]).setWorkingDirectory("C:\\work").setNameCanonicalNormalization(PathNormalization.CASE_FOLD_ASCII, new PathNormalization[0]).setPathEqualityUsesCanonicalForm(true).setAttributeViews("basic", new String[0]).setSupportedFeatures(new Feature[] { Feature.LINKS, Feature.SYMBOLIC_LINKS, Feature.FILE_CHANNEL }).build();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Configuration forCurrentPlatform() {
/* 203 */     String os = System.getProperty("os.name");
/*     */     
/* 205 */     if (os.contains("Windows"))
/* 206 */       return windows(); 
/* 207 */     if (os.contains("OS X")) {
/* 208 */       return osX();
/*     */     }
/* 210 */     return unix();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder builder(PathType pathType) {
/* 218 */     return new Builder(pathType);
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
/*     */   private Configuration(Builder builder) {
/* 249 */     this.pathType = builder.pathType;
/* 250 */     this.nameDisplayNormalization = builder.nameDisplayNormalization;
/* 251 */     this.nameCanonicalNormalization = builder.nameCanonicalNormalization;
/* 252 */     this.pathEqualityUsesCanonicalForm = builder.pathEqualityUsesCanonicalForm;
/* 253 */     this.blockSize = builder.blockSize;
/* 254 */     this.maxSize = builder.maxSize;
/* 255 */     this.maxCacheSize = builder.maxCacheSize;
/* 256 */     this.attributeViews = builder.attributeViews;
/* 257 */     this.attributeProviders = (builder.attributeProviders == null) ? ImmutableSet.of() : ImmutableSet.copyOf(builder.attributeProviders);
/*     */ 
/*     */ 
/*     */     
/* 261 */     this.defaultAttributeValues = (builder.defaultAttributeValues == null) ? ImmutableMap.of() : ImmutableMap.copyOf(builder.defaultAttributeValues);
/*     */ 
/*     */ 
/*     */     
/* 265 */     this.watchServiceConfig = builder.watchServiceConfig;
/* 266 */     this.roots = builder.roots;
/* 267 */     this.workingDirectory = builder.workingDirectory;
/* 268 */     this.supportedFeatures = builder.supportedFeatures;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Builder toBuilder() {
/* 275 */     return new Builder(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Builder
/*     */   {
/*     */     public static final int DEFAULT_BLOCK_SIZE = 8192;
/*     */ 
/*     */     
/*     */     public static final long DEFAULT_MAX_SIZE = 4294967296L;
/*     */ 
/*     */     
/*     */     public static final long DEFAULT_MAX_CACHE_SIZE = -1L;
/*     */ 
/*     */     
/*     */     private final PathType pathType;
/*     */ 
/*     */     
/* 294 */     private ImmutableSet<PathNormalization> nameDisplayNormalization = ImmutableSet.of();
/* 295 */     private ImmutableSet<PathNormalization> nameCanonicalNormalization = ImmutableSet.of();
/*     */     
/*     */     private boolean pathEqualityUsesCanonicalForm = false;
/*     */     
/* 299 */     private int blockSize = 8192;
/* 300 */     private long maxSize = 4294967296L;
/* 301 */     private long maxCacheSize = -1L;
/*     */ 
/*     */     
/* 304 */     private ImmutableSet<String> attributeViews = ImmutableSet.of();
/* 305 */     private Set<AttributeProvider> attributeProviders = null;
/*     */     
/*     */     private Map<String, Object> defaultAttributeValues;
/*     */     
/* 309 */     private WatchServiceConfiguration watchServiceConfig = WatchServiceConfiguration.DEFAULT;
/*     */ 
/*     */     
/* 312 */     private ImmutableSet<String> roots = ImmutableSet.of();
/*     */     private String workingDirectory;
/* 314 */     private ImmutableSet<Feature> supportedFeatures = ImmutableSet.of();
/*     */     
/*     */     private Builder(PathType pathType) {
/* 317 */       this.pathType = (PathType)Preconditions.checkNotNull(pathType);
/*     */     }
/*     */     
/*     */     private Builder(Configuration configuration) {
/* 321 */       this.pathType = configuration.pathType;
/* 322 */       this.nameDisplayNormalization = configuration.nameDisplayNormalization;
/* 323 */       this.nameCanonicalNormalization = configuration.nameCanonicalNormalization;
/* 324 */       this.pathEqualityUsesCanonicalForm = configuration.pathEqualityUsesCanonicalForm;
/* 325 */       this.blockSize = configuration.blockSize;
/* 326 */       this.maxSize = configuration.maxSize;
/* 327 */       this.maxCacheSize = configuration.maxCacheSize;
/* 328 */       this.attributeViews = configuration.attributeViews;
/* 329 */       this.attributeProviders = configuration.attributeProviders.isEmpty() ? null : new HashSet<>((Collection<? extends AttributeProvider>)configuration.attributeProviders);
/*     */ 
/*     */ 
/*     */       
/* 333 */       this.defaultAttributeValues = configuration.defaultAttributeValues.isEmpty() ? null : new HashMap<>((Map<? extends String, ?>)configuration.defaultAttributeValues);
/*     */ 
/*     */ 
/*     */       
/* 337 */       this.watchServiceConfig = configuration.watchServiceConfig;
/* 338 */       this.roots = configuration.roots;
/* 339 */       this.workingDirectory = configuration.workingDirectory;
/* 340 */       this.supportedFeatures = configuration.supportedFeatures;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setNameDisplayNormalization(PathNormalization first, PathNormalization... more) {
/* 348 */       this.nameDisplayNormalization = checkNormalizations(Lists.asList(first, (Object[])more));
/* 349 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setNameCanonicalNormalization(PathNormalization first, PathNormalization... more) {
/* 359 */       this.nameCanonicalNormalization = checkNormalizations(Lists.asList(first, (Object[])more));
/* 360 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     private ImmutableSet<PathNormalization> checkNormalizations(List<PathNormalization> normalizations) {
/* 365 */       PathNormalization none = null;
/* 366 */       PathNormalization normalization = null;
/* 367 */       PathNormalization caseFold = null;
/* 368 */       for (PathNormalization n : normalizations) {
/* 369 */         Preconditions.checkNotNull(n);
/* 370 */         checkNormalizationNotSet(n, none);
/*     */         
/* 372 */         switch (n) {
/*     */           case NONE:
/* 374 */             none = n;
/*     */             continue;
/*     */           case NFC:
/*     */           case NFD:
/* 378 */             checkNormalizationNotSet(n, normalization);
/* 379 */             normalization = n;
/*     */             continue;
/*     */           case CASE_FOLD_UNICODE:
/*     */           case CASE_FOLD_ASCII:
/* 383 */             checkNormalizationNotSet(n, caseFold);
/* 384 */             caseFold = n;
/*     */             continue;
/*     */         } 
/* 387 */         throw new AssertionError();
/*     */       } 
/*     */ 
/*     */       
/* 391 */       if (none != null) {
/* 392 */         return ImmutableSet.of();
/*     */       }
/* 394 */       return Sets.immutableEnumSet(normalizations);
/*     */     }
/*     */ 
/*     */     
/*     */     private static void checkNormalizationNotSet(PathNormalization n, @Nullable PathNormalization set) {
/* 399 */       if (set != null) {
/* 400 */         throw new IllegalArgumentException("can't set normalization " + n + ": normalization " + set + " already set");
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setPathEqualityUsesCanonicalForm(boolean useCanonicalForm) {
/* 412 */       this.pathEqualityUsesCanonicalForm = useCanonicalForm;
/* 413 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setBlockSize(int blockSize) {
/* 423 */       Preconditions.checkArgument((blockSize > 0), "blockSize (%s) must be positive", new Object[] { Integer.valueOf(blockSize) });
/* 424 */       this.blockSize = blockSize;
/* 425 */       return this;
/*     */     }
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
/*     */     public Builder setMaxSize(long maxSize) {
/* 443 */       Preconditions.checkArgument((maxSize > 0L), "maxSize (%s) must be positive", new Object[] { Long.valueOf(maxSize) });
/* 444 */       this.maxSize = maxSize;
/* 445 */       return this;
/*     */     }
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
/*     */     public Builder setMaxCacheSize(long maxCacheSize) {
/* 462 */       Preconditions.checkArgument((maxCacheSize >= 0L), "maxCacheSize (%s) may not be negative", new Object[] { Long.valueOf(maxCacheSize) });
/* 463 */       this.maxCacheSize = maxCacheSize;
/* 464 */       return this;
/*     */     }
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
/*     */     public Builder setAttributeViews(String first, String... more) {
/* 518 */       this.attributeViews = ImmutableSet.copyOf(Lists.asList(first, (Object[])more));
/* 519 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAttributeProvider(AttributeProvider provider) {
/* 526 */       Preconditions.checkNotNull(provider);
/* 527 */       if (this.attributeProviders == null) {
/* 528 */         this.attributeProviders = new HashSet<>();
/*     */       }
/* 530 */       this.attributeProviders.add(provider);
/* 531 */       return this;
/*     */     }
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
/*     */     public Builder setDefaultAttributeValue(String attribute, Object value) {
/* 581 */       Preconditions.checkArgument(ATTRIBUTE_PATTERN.matcher(attribute).matches(), "attribute (%s) must be of the form \"view:attribute\"", new Object[] { attribute });
/*     */ 
/*     */ 
/*     */       
/* 585 */       Preconditions.checkNotNull(value);
/*     */       
/* 587 */       if (this.defaultAttributeValues == null) {
/* 588 */         this.defaultAttributeValues = new HashMap<>();
/*     */       }
/*     */       
/* 591 */       this.defaultAttributeValues.put(attribute, value);
/* 592 */       return this;
/*     */     }
/*     */     
/* 595 */     private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile("[^:]+:[^:]+");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setRoots(String first, String... more) {
/* 606 */       List<String> roots = Lists.asList(first, (Object[])more);
/* 607 */       for (String root : roots) {
/* 608 */         PathType.ParseResult parseResult = this.pathType.parsePath(root);
/* 609 */         Preconditions.checkArgument(parseResult.isRoot(), "invalid root: %s", new Object[] { root });
/*     */       } 
/* 611 */       this.roots = ImmutableSet.copyOf(roots);
/* 612 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setWorkingDirectory(String workingDirectory) {
/* 624 */       PathType.ParseResult parseResult = this.pathType.parsePath(workingDirectory);
/* 625 */       Preconditions.checkArgument(parseResult.isAbsolute(), "working directory must be an absolute path: %s", new Object[] { workingDirectory });
/*     */ 
/*     */ 
/*     */       
/* 629 */       this.workingDirectory = (String)Preconditions.checkNotNull(workingDirectory);
/* 630 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setSupportedFeatures(Feature... features) {
/* 638 */       this.supportedFeatures = Sets.immutableEnumSet(Arrays.asList(features));
/* 639 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setWatchServiceConfiguration(WatchServiceConfiguration config) {
/* 649 */       this.watchServiceConfig = (WatchServiceConfiguration)Preconditions.checkNotNull(config);
/* 650 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Configuration build() {
/* 657 */       return new Configuration(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\Configuration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */