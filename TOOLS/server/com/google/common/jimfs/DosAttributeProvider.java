/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.attribute.BasicFileAttributeView;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.DosFileAttributeView;
/*     */ import java.nio.file.attribute.DosFileAttributes;
/*     */ import java.nio.file.attribute.FileAttributeView;
/*     */ import java.nio.file.attribute.FileTime;
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
/*     */ final class DosAttributeProvider
/*     */   extends AttributeProvider
/*     */ {
/*  42 */   private static final ImmutableSet<String> ATTRIBUTES = ImmutableSet.of("readonly", "hidden", "archive", "system");
/*     */ 
/*     */   
/*  45 */   private static final ImmutableSet<String> INHERITED_VIEWS = ImmutableSet.of("basic", "owner");
/*     */ 
/*     */   
/*     */   public String name() {
/*  49 */     return "dos";
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<String> inherits() {
/*  54 */     return INHERITED_VIEWS;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<String> fixedAttributes() {
/*  59 */     return ATTRIBUTES;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableMap<String, ?> defaultValues(Map<String, ?> userProvidedDefaults) {
/*  64 */     return ImmutableMap.of("dos:readonly", getDefaultValue("dos:readonly", userProvidedDefaults), "dos:hidden", getDefaultValue("dos:hidden", userProvidedDefaults), "dos:archive", getDefaultValue("dos:archive", userProvidedDefaults), "dos:system", getDefaultValue("dos:system", userProvidedDefaults));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Boolean getDefaultValue(String attribute, Map<String, ?> userProvidedDefaults) {
/*  72 */     Object userProvidedValue = userProvidedDefaults.get(attribute);
/*  73 */     if (userProvidedValue != null) {
/*  74 */       return checkType("dos", attribute, userProvidedValue, Boolean.class);
/*     */     }
/*     */     
/*  77 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object get(File file, String attribute) {
/*  83 */     if (ATTRIBUTES.contains(attribute)) {
/*  84 */       return file.getAttribute("dos", attribute);
/*     */     }
/*     */     
/*  87 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(File file, String view, String attribute, Object value, boolean create) {
/*  92 */     if (supports(attribute)) {
/*  93 */       checkNotCreate(view, attribute, create);
/*  94 */       file.setAttribute("dos", attribute, checkType(view, attribute, value, Boolean.class));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<DosFileAttributeView> viewType() {
/* 100 */     return DosFileAttributeView.class;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DosFileAttributeView view(FileLookup lookup, ImmutableMap<String, FileAttributeView> inheritedViews) {
/* 106 */     return new View(lookup, (BasicFileAttributeView)inheritedViews.get("basic"));
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<DosFileAttributes> attributesType() {
/* 111 */     return DosFileAttributes.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public DosFileAttributes readAttributes(File file) {
/* 116 */     return new Attributes(file);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class View
/*     */     extends AbstractAttributeView
/*     */     implements DosFileAttributeView
/*     */   {
/*     */     private final BasicFileAttributeView basicView;
/*     */     
/*     */     public View(FileLookup lookup, BasicFileAttributeView basicView) {
/* 127 */       super(lookup);
/* 128 */       this.basicView = (BasicFileAttributeView)Preconditions.checkNotNull(basicView);
/*     */     }
/*     */ 
/*     */     
/*     */     public String name() {
/* 133 */       return "dos";
/*     */     }
/*     */ 
/*     */     
/*     */     public DosFileAttributes readAttributes() throws IOException {
/* 138 */       return new DosAttributeProvider.Attributes(lookupFile());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setTimes(FileTime lastModifiedTime, FileTime lastAccessTime, FileTime createTime) throws IOException {
/* 144 */       this.basicView.setTimes(lastModifiedTime, lastAccessTime, createTime);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setReadOnly(boolean value) throws IOException {
/* 149 */       lookupFile().setAttribute("dos", "readonly", Boolean.valueOf(value));
/*     */     }
/*     */ 
/*     */     
/*     */     public void setHidden(boolean value) throws IOException {
/* 154 */       lookupFile().setAttribute("dos", "hidden", Boolean.valueOf(value));
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSystem(boolean value) throws IOException {
/* 159 */       lookupFile().setAttribute("dos", "system", Boolean.valueOf(value));
/*     */     }
/*     */ 
/*     */     
/*     */     public void setArchive(boolean value) throws IOException {
/* 164 */       lookupFile().setAttribute("dos", "archive", Boolean.valueOf(value));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class Attributes
/*     */     extends BasicAttributeProvider.Attributes
/*     */     implements DosFileAttributes
/*     */   {
/*     */     private final boolean readOnly;
/*     */     private final boolean hidden;
/*     */     private final boolean archive;
/*     */     private final boolean system;
/*     */     
/*     */     protected Attributes(File file) {
/* 179 */       super(file);
/* 180 */       this.readOnly = ((Boolean)file.getAttribute("dos", "readonly")).booleanValue();
/* 181 */       this.hidden = ((Boolean)file.getAttribute("dos", "hidden")).booleanValue();
/* 182 */       this.archive = ((Boolean)file.getAttribute("dos", "archive")).booleanValue();
/* 183 */       this.system = ((Boolean)file.getAttribute("dos", "system")).booleanValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isReadOnly() {
/* 188 */       return this.readOnly;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isHidden() {
/* 193 */       return this.hidden;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isArchive() {
/* 198 */       return this.archive;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSystem() {
/* 203 */       return this.system;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\DosAttributeProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */