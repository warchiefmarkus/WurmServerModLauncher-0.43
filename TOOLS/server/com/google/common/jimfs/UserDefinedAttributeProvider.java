/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.file.attribute.FileAttributeView;
/*     */ import java.nio.file.attribute.UserDefinedFileAttributeView;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class UserDefinedAttributeProvider
/*     */   extends AttributeProvider
/*     */ {
/*     */   public String name() {
/*  44 */     return "user";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<String> fixedAttributes() {
/*  50 */     return ImmutableSet.of();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supports(String attribute) {
/*  56 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<String> attributes(File file) {
/*  61 */     return userDefinedAttributes(file);
/*     */   }
/*     */   
/*     */   private static ImmutableSet<String> userDefinedAttributes(File file) {
/*  65 */     ImmutableSet.Builder<String> builder = ImmutableSet.builder();
/*  66 */     for (String attribute : file.getAttributeNames("user")) {
/*  67 */       builder.add(attribute);
/*     */     }
/*  69 */     return builder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object get(File file, String attribute) {
/*  74 */     Object value = file.getAttribute("user", attribute);
/*  75 */     if (value instanceof byte[]) {
/*  76 */       byte[] bytes = (byte[])value;
/*  77 */       return bytes.clone();
/*     */     } 
/*  79 */     return null;
/*     */   }
/*     */   
/*     */   public void set(File file, String view, String attribute, Object value, boolean create) {
/*     */     byte[] bytes;
/*  84 */     Preconditions.checkNotNull(value);
/*  85 */     checkNotCreate(view, attribute, create);
/*     */ 
/*     */     
/*  88 */     if (value instanceof byte[]) {
/*  89 */       bytes = (byte[])((byte[])value).clone();
/*  90 */     } else if (value instanceof ByteBuffer) {
/*     */       
/*  92 */       ByteBuffer buffer = (ByteBuffer)value;
/*  93 */       bytes = new byte[buffer.remaining()];
/*  94 */       buffer.get(bytes);
/*     */     } else {
/*  96 */       throw invalidType(view, attribute, value, new Class[] { byte[].class, ByteBuffer.class });
/*     */     } 
/*     */     
/*  99 */     file.setAttribute("user", attribute, bytes);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<UserDefinedFileAttributeView> viewType() {
/* 104 */     return UserDefinedFileAttributeView.class;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UserDefinedFileAttributeView view(FileLookup lookup, ImmutableMap<String, FileAttributeView> inheritedViews) {
/* 110 */     return new View(lookup);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class View
/*     */     extends AbstractAttributeView
/*     */     implements UserDefinedFileAttributeView
/*     */   {
/*     */     public View(FileLookup lookup) {
/* 119 */       super(lookup);
/*     */     }
/*     */ 
/*     */     
/*     */     public String name() {
/* 124 */       return "user";
/*     */     }
/*     */ 
/*     */     
/*     */     public List<String> list() throws IOException {
/* 129 */       return (List<String>)UserDefinedAttributeProvider.userDefinedAttributes(lookupFile()).asList();
/*     */     }
/*     */     
/*     */     private byte[] getStoredBytes(String name) throws IOException {
/* 133 */       byte[] bytes = (byte[])lookupFile().getAttribute(name(), name);
/* 134 */       if (bytes == null) {
/* 135 */         throw new IllegalArgumentException("attribute '" + name() + ":" + name + "' is not set");
/*     */       }
/* 137 */       return bytes;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size(String name) throws IOException {
/* 142 */       return (getStoredBytes(name)).length;
/*     */     }
/*     */ 
/*     */     
/*     */     public int read(String name, ByteBuffer dst) throws IOException {
/* 147 */       byte[] bytes = getStoredBytes(name);
/* 148 */       dst.put(bytes);
/* 149 */       return bytes.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public int write(String name, ByteBuffer src) throws IOException {
/* 154 */       byte[] bytes = new byte[src.remaining()];
/* 155 */       src.get(bytes);
/* 156 */       lookupFile().setAttribute(name(), name, bytes);
/* 157 */       return bytes.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public void delete(String name) throws IOException {
/* 162 */       lookupFile().deleteAttribute(name(), name);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\UserDefinedAttributeProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */