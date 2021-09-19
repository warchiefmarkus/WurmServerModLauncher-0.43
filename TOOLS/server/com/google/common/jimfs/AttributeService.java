/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.attribute.FileAttributeView;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ final class AttributeService
/*     */ {
/*     */   private static final String ALL_ATTRIBUTES = "*";
/*     */   private final ImmutableMap<String, AttributeProvider> providersByName;
/*     */   private final ImmutableMap<Class<?>, AttributeProvider> providersByViewType;
/*     */   private final ImmutableMap<Class<?>, AttributeProvider> providersByAttributesType;
/*     */   private final ImmutableList<FileAttribute<?>> defaultValues;
/*     */   
/*     */   public AttributeService(Configuration configuration) {
/*  63 */     this(getProviders(configuration), (Map<String, ?>)configuration.defaultAttributeValues);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeService(Iterable<? extends AttributeProvider> providers, Map<String, ?> userProvidedDefaults) {
/*  72 */     ImmutableMap.Builder<String, AttributeProvider> byViewNameBuilder = ImmutableMap.builder();
/*  73 */     ImmutableMap.Builder<Class<?>, AttributeProvider> byViewTypeBuilder = ImmutableMap.builder();
/*  74 */     ImmutableMap.Builder<Class<?>, AttributeProvider> byAttributesTypeBuilder = ImmutableMap.builder();
/*     */ 
/*     */     
/*  77 */     ImmutableList.Builder<FileAttribute<?>> defaultAttributesBuilder = ImmutableList.builder();
/*     */     
/*  79 */     for (AttributeProvider provider : providers) {
/*  80 */       byViewNameBuilder.put(provider.name(), provider);
/*  81 */       byViewTypeBuilder.put(provider.viewType(), provider);
/*  82 */       if (provider.attributesType() != null) {
/*  83 */         byAttributesTypeBuilder.put(provider.attributesType(), provider);
/*     */       }
/*     */       
/*  86 */       for (Map.Entry<String, ?> entry : (Iterable<Map.Entry<String, ?>>)provider.defaultValues(userProvidedDefaults).entrySet()) {
/*  87 */         defaultAttributesBuilder.add(new SimpleFileAttribute(entry.getKey(), entry.getValue()));
/*     */       }
/*     */     } 
/*     */     
/*  91 */     this.providersByName = byViewNameBuilder.build();
/*  92 */     this.providersByViewType = byViewTypeBuilder.build();
/*  93 */     this.providersByAttributesType = byAttributesTypeBuilder.build();
/*  94 */     this.defaultValues = defaultAttributesBuilder.build();
/*     */   }
/*     */   
/*     */   private static Iterable<AttributeProvider> getProviders(Configuration configuration) {
/*  98 */     Map<String, AttributeProvider> result = new HashMap<>();
/*     */     
/* 100 */     for (AttributeProvider provider : configuration.attributeProviders) {
/* 101 */       result.put(provider.name(), provider);
/*     */     }
/*     */     
/* 104 */     for (String view : configuration.attributeViews) {
/* 105 */       addStandardProvider(result, view);
/*     */     }
/*     */     
/* 108 */     addMissingProviders(result);
/*     */     
/* 110 */     return Collections.unmodifiableCollection(result.values());
/*     */   }
/*     */   
/*     */   private static void addMissingProviders(Map<String, AttributeProvider> providers) {
/* 114 */     Set<String> missingViews = new HashSet<>();
/* 115 */     for (AttributeProvider provider : providers.values()) {
/* 116 */       for (String inheritedView : provider.inherits()) {
/* 117 */         if (!providers.containsKey(inheritedView)) {
/* 118 */           missingViews.add(inheritedView);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 123 */     if (missingViews.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 128 */     for (String view : missingViews) {
/* 129 */       addStandardProvider(providers, view);
/*     */     }
/*     */ 
/*     */     
/* 133 */     addMissingProviders(providers);
/*     */   }
/*     */   
/*     */   private static void addStandardProvider(Map<String, AttributeProvider> result, String view) {
/* 137 */     AttributeProvider provider = StandardAttributeProviders.get(view);
/*     */     
/* 139 */     if (provider == null) {
/* 140 */       if (!result.containsKey(view)) {
/* 141 */         throw new IllegalStateException("no provider found for attribute view '" + view + "'");
/*     */       }
/*     */     } else {
/* 144 */       result.put(provider.name(), provider);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<String> supportedFileAttributeViews() {
/* 152 */     return this.providersByName.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsFileAttributeView(Class<? extends FileAttributeView> type) {
/* 159 */     return this.providersByViewType.containsKey(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInitialAttributes(File file, FileAttribute<?>... attrs) {
/* 167 */     for (int i = 0; i < this.defaultValues.size(); i++) {
/* 168 */       FileAttribute<?> attribute = (FileAttribute)this.defaultValues.get(i);
/*     */       
/* 170 */       int separatorIndex = attribute.name().indexOf(':');
/* 171 */       String view = attribute.name().substring(0, separatorIndex);
/* 172 */       String attr = attribute.name().substring(separatorIndex + 1);
/* 173 */       file.setAttribute(view, attr, attribute.value());
/*     */     } 
/*     */     
/* 176 */     for (FileAttribute<?> attr : attrs) {
/* 177 */       setAttribute(file, attr.name(), attr.value(), true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyAttributes(File file, File copy, AttributeCopyOption copyOption) {
/* 185 */     switch (copyOption) {
/*     */       case ALL:
/* 187 */         file.copyAttributes(copy);
/*     */         break;
/*     */       case BASIC:
/* 190 */         file.copyBasicAttributes(copy);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getAttribute(File file, String attribute) {
/* 202 */     String view = getViewName(attribute);
/* 203 */     String attr = getSingleAttribute(attribute);
/* 204 */     return getAttribute(file, view, attr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getAttribute(File file, String view, String attribute) {
/* 212 */     Object value = getAttributeInternal(file, view, attribute);
/* 213 */     if (value == null) {
/* 214 */       throw new IllegalArgumentException("invalid attribute for view '" + view + "': " + attribute);
/*     */     }
/* 216 */     return value;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Object getAttributeInternal(File file, String view, String attribute) {
/* 221 */     AttributeProvider provider = (AttributeProvider)this.providersByName.get(view);
/* 222 */     if (provider == null) {
/* 223 */       return null;
/*     */     }
/*     */     
/* 226 */     Object value = provider.get(file, attribute);
/* 227 */     if (value == null) {
/* 228 */       for (String inheritedView : provider.inherits()) {
/* 229 */         value = getAttributeInternal(file, inheritedView, attribute);
/* 230 */         if (value != null) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 236 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttribute(File file, String attribute, Object value, boolean create) {
/* 243 */     String view = getViewName(attribute);
/* 244 */     String attr = getSingleAttribute(attribute);
/* 245 */     setAttributeInternal(file, view, attr, value, create);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setAttributeInternal(File file, String view, String attribute, Object value, boolean create) {
/* 250 */     AttributeProvider provider = (AttributeProvider)this.providersByName.get(view);
/*     */     
/* 252 */     if (provider != null) {
/* 253 */       if (provider.supports(attribute)) {
/* 254 */         provider.set(file, view, attribute, value, create);
/*     */         
/*     */         return;
/*     */       } 
/* 258 */       for (String inheritedView : provider.inherits()) {
/* 259 */         AttributeProvider inheritedProvider = (AttributeProvider)this.providersByName.get(inheritedView);
/* 260 */         if (inheritedProvider.supports(attribute)) {
/* 261 */           inheritedProvider.set(file, view, attribute, value, create);
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/* 267 */     throw new IllegalArgumentException("cannot set attribute '" + view + ":" + attribute + "'");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <V extends FileAttributeView> V getFileAttributeView(FileLookup lookup, Class<V> type) {
/* 277 */     AttributeProvider provider = (AttributeProvider)this.providersByViewType.get(type);
/*     */     
/* 279 */     if (provider != null) {
/* 280 */       return (V)provider.view(lookup, createInheritedViews(lookup, provider));
/*     */     }
/*     */     
/* 283 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private ImmutableMap<String, FileAttributeView> createInheritedViews(FileLookup lookup, AttributeProvider provider) {
/* 288 */     if (provider.inherits().isEmpty()) {
/* 289 */       return ImmutableMap.of();
/*     */     }
/*     */     
/* 292 */     Map<String, FileAttributeView> inheritedViews = new HashMap<>();
/* 293 */     createInheritedViews(lookup, provider, inheritedViews);
/* 294 */     return ImmutableMap.copyOf(inheritedViews);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createInheritedViews(FileLookup lookup, AttributeProvider provider, Map<String, FileAttributeView> inheritedViews) {
/* 302 */     for (String inherited : provider.inherits()) {
/* 303 */       if (!inheritedViews.containsKey(inherited)) {
/* 304 */         AttributeProvider inheritedProvider = (AttributeProvider)this.providersByName.get(inherited);
/* 305 */         FileAttributeView inheritedView = getFileAttributeView(lookup, inheritedProvider.viewType(), inheritedViews);
/*     */ 
/*     */         
/* 308 */         inheritedViews.put(inherited, inheritedView);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private FileAttributeView getFileAttributeView(FileLookup lookup, Class<? extends FileAttributeView> viewType, Map<String, FileAttributeView> inheritedViews) {
/* 317 */     AttributeProvider provider = (AttributeProvider)this.providersByViewType.get(viewType);
/* 318 */     createInheritedViews(lookup, provider, inheritedViews);
/* 319 */     return provider.view(lookup, ImmutableMap.copyOf(inheritedViews));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<String, Object> readAttributes(File file, String attributes) {
/* 326 */     String view = getViewName(attributes);
/* 327 */     ImmutableList<String> immutableList = getAttributeNames(attributes);
/*     */     
/* 329 */     if (immutableList.size() > 1 && immutableList.contains("*"))
/*     */     {
/* 331 */       throw new IllegalArgumentException("invalid attributes: " + attributes);
/*     */     }
/*     */     
/* 334 */     Map<String, Object> result = new HashMap<>();
/* 335 */     if (immutableList.size() == 1 && immutableList.contains("*")) {
/*     */       
/* 337 */       AttributeProvider provider = (AttributeProvider)this.providersByName.get(view);
/* 338 */       readAll(file, provider, result);
/*     */       
/* 340 */       for (String inheritedView : provider.inherits()) {
/* 341 */         AttributeProvider inheritedProvider = (AttributeProvider)this.providersByName.get(inheritedView);
/* 342 */         readAll(file, inheritedProvider, result);
/*     */       } 
/*     */     } else {
/*     */       
/* 346 */       for (String attr : immutableList) {
/* 347 */         result.put(attr, getAttribute(file, view, attr));
/*     */       }
/*     */     } 
/*     */     
/* 351 */     return ImmutableMap.copyOf(result);
/*     */   }
/*     */   
/*     */   private static void readAll(File file, AttributeProvider provider, Map<String, Object> map) {
/* 355 */     for (String attribute : provider.attributes(file)) {
/* 356 */       Object value = provider.get(file, attribute);
/*     */ 
/*     */ 
/*     */       
/* 360 */       if (value != null) {
/* 361 */         map.put(attribute, value);
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
/*     */   public <A extends java.nio.file.attribute.BasicFileAttributes> A readAttributes(File file, Class<A> type) {
/* 373 */     AttributeProvider provider = (AttributeProvider)this.providersByAttributesType.get(type);
/* 374 */     if (provider != null) {
/* 375 */       return (A)provider.readAttributes(file);
/*     */     }
/*     */     
/* 378 */     throw new UnsupportedOperationException("unsupported attributes type: " + type);
/*     */   }
/*     */   
/*     */   private static String getViewName(String attribute) {
/* 382 */     int separatorIndex = attribute.indexOf(':');
/*     */     
/* 384 */     if (separatorIndex == -1) {
/* 385 */       return "basic";
/*     */     }
/*     */ 
/*     */     
/* 389 */     if (separatorIndex == 0 || separatorIndex == attribute.length() - 1 || attribute.indexOf(':', separatorIndex + 1) != -1)
/*     */     {
/*     */       
/* 392 */       throw new IllegalArgumentException("illegal attribute format: " + attribute);
/*     */     }
/*     */     
/* 395 */     return attribute.substring(0, separatorIndex);
/*     */   }
/*     */   
/* 398 */   private static final Splitter ATTRIBUTE_SPLITTER = Splitter.on(',');
/*     */   
/*     */   private static ImmutableList<String> getAttributeNames(String attributes) {
/* 401 */     int separatorIndex = attributes.indexOf(':');
/* 402 */     String attributesPart = attributes.substring(separatorIndex + 1);
/*     */     
/* 404 */     return ImmutableList.copyOf(ATTRIBUTE_SPLITTER.split(attributesPart));
/*     */   }
/*     */   
/*     */   private static String getSingleAttribute(String attribute) {
/* 408 */     ImmutableList<String> attributeNames = getAttributeNames(attribute);
/*     */     
/* 410 */     if (attributeNames.size() != 1 || "*".equals(attributeNames.get(0))) {
/* 411 */       throw new IllegalArgumentException("must specify a single attribute: " + attribute);
/*     */     }
/*     */     
/* 414 */     return (String)attributeNames.get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class SimpleFileAttribute<T>
/*     */     implements FileAttribute<T>
/*     */   {
/*     */     private final String name;
/*     */     
/*     */     private final T value;
/*     */     
/*     */     SimpleFileAttribute(String name, T value) {
/* 426 */       this.name = (String)Preconditions.checkNotNull(name);
/* 427 */       this.value = (T)Preconditions.checkNotNull(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public String name() {
/* 432 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public T value() {
/* 437 */       return this.value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\AttributeService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */