/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileAttributeView;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
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
/*     */ public abstract class AttributeProvider
/*     */ {
/*     */   public abstract String name();
/*     */   
/*     */   public ImmutableSet<String> inherits() {
/*  47 */     return ImmutableSet.of();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Class<? extends FileAttributeView> viewType();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract FileAttributeView view(FileLookup paramFileLookup, ImmutableMap<String, FileAttributeView> paramImmutableMap);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<String, ?> defaultValues(Map<String, ?> userDefaults) {
/*  75 */     return ImmutableMap.of();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract ImmutableSet<String> fixedAttributes();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supports(String attribute) {
/*  87 */     return fixedAttributes().contains(attribute);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<String> attributes(File file) {
/*  95 */     return fixedAttributes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public abstract Object get(File paramFile, String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void set(File paramFile, String paramString1, String paramString2, Object paramObject, boolean paramBoolean);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<? extends BasicFileAttributes> attributesType() {
/* 125 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicFileAttributes readAttributes(File file) {
/* 135 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static IllegalArgumentException unsettable(String view, String attribute) {
/* 144 */     throw new IllegalArgumentException("cannot set attribute '" + view + ":" + attribute + "'");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void checkNotCreate(String view, String attribute, boolean create) {
/* 152 */     if (create) {
/* 153 */       throw new UnsupportedOperationException("cannot set attribute '" + view + ":" + attribute + "' during file creation");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static <T> T checkType(String view, String attribute, Object value, Class<T> type) {
/* 163 */     Preconditions.checkNotNull(value);
/* 164 */     if (type.isInstance(value)) {
/* 165 */       return type.cast(value);
/*     */     }
/*     */     
/* 168 */     throw invalidType(view, attribute, value, new Class[] { type });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static IllegalArgumentException invalidType(String view, String attribute, Object value, Class<?>... expectedTypes) {
/* 177 */     Object expected = (expectedTypes.length == 1) ? expectedTypes[0] : ("one of " + Arrays.toString((Object[])expectedTypes));
/*     */     
/* 179 */     throw new IllegalArgumentException("invalid type " + value.getClass() + " for attribute '" + view + ":" + attribute + "': expected " + expected);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\AttributeProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */