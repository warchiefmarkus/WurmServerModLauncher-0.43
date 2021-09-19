/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.nio.file.attribute.FileAttributeView;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import java.nio.file.attribute.GroupPrincipal;
/*     */ import java.nio.file.attribute.PosixFilePermission;
/*     */ import java.nio.file.attribute.UserPrincipal;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class UnixAttributeProvider
/*     */   extends AttributeProvider
/*     */ {
/*  41 */   private static final ImmutableSet<String> ATTRIBUTES = ImmutableSet.of("uid", "ino", "dev", "nlink", "rdev", "ctime", (Object[])new String[] { "mode", "gid" });
/*     */ 
/*     */   
/*  44 */   private static final ImmutableSet<String> INHERITED_VIEWS = ImmutableSet.of("basic", "owner", "posix");
/*     */ 
/*     */   
/*  47 */   private final AtomicInteger uidGenerator = new AtomicInteger();
/*  48 */   private final ConcurrentMap<Object, Integer> idCache = new ConcurrentHashMap<>();
/*     */ 
/*     */   
/*     */   public String name() {
/*  52 */     return "unix";
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<String> inherits() {
/*  57 */     return INHERITED_VIEWS;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<String> fixedAttributes() {
/*  62 */     return ATTRIBUTES;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<UnixFileAttributeView> viewType() {
/*  67 */     return UnixFileAttributeView.class;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnixFileAttributeView view(FileLookup lookup, ImmutableMap<String, FileAttributeView> inheritedViews) {
/*  75 */     throw new UnsupportedOperationException();
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
/*     */   private Integer getUniqueId(Object object) {
/*  89 */     Integer id = this.idCache.get(object);
/*  90 */     if (id == null) {
/*  91 */       id = Integer.valueOf(this.uidGenerator.incrementAndGet());
/*  92 */       Integer existing = this.idCache.putIfAbsent(object, id);
/*  93 */       if (existing != null) {
/*  94 */         return existing;
/*     */       }
/*     */     } 
/*  97 */     return id;
/*     */   }
/*     */   public Object get(File file, String attribute) {
/*     */     UserPrincipal user;
/*     */     GroupPrincipal group;
/*     */     Set<PosixFilePermission> permissions;
/* 103 */     switch (attribute) {
/*     */       case "uid":
/* 105 */         user = (UserPrincipal)file.getAttribute("owner", "owner");
/* 106 */         return getUniqueId(user);
/*     */       case "gid":
/* 108 */         group = (GroupPrincipal)file.getAttribute("posix", "group");
/* 109 */         return getUniqueId(group);
/*     */       case "mode":
/* 111 */         permissions = (Set<PosixFilePermission>)file.getAttribute("posix", "permissions");
/*     */         
/* 113 */         return Integer.valueOf(toMode(permissions));
/*     */       case "ctime":
/* 115 */         return FileTime.fromMillis(file.getCreationTime());
/*     */       case "rdev":
/* 117 */         return Long.valueOf(0L);
/*     */       case "dev":
/* 119 */         return Long.valueOf(1L);
/*     */       case "ino":
/* 121 */         return Integer.valueOf(file.id());
/*     */       case "nlink":
/* 123 */         return Integer.valueOf(file.links());
/*     */     } 
/* 125 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(File file, String view, String attribute, Object value, boolean create) {
/* 131 */     throw unsettable(view, attribute);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int toMode(Set<PosixFilePermission> permissions) {
/* 136 */     int result = 0;
/* 137 */     for (PosixFilePermission permission : permissions) {
/* 138 */       Preconditions.checkNotNull(permission);
/* 139 */       switch (permission) {
/*     */         case OWNER_READ:
/* 141 */           result |= 0x100;
/*     */           continue;
/*     */         case OWNER_WRITE:
/* 144 */           result |= 0x80;
/*     */           continue;
/*     */         case OWNER_EXECUTE:
/* 147 */           result |= 0x40;
/*     */           continue;
/*     */         case GROUP_READ:
/* 150 */           result |= 0x20;
/*     */           continue;
/*     */         case GROUP_WRITE:
/* 153 */           result |= 0x10;
/*     */           continue;
/*     */         case GROUP_EXECUTE:
/* 156 */           result |= 0x8;
/*     */           continue;
/*     */         case OTHERS_READ:
/* 159 */           result |= 0x4;
/*     */           continue;
/*     */         case OTHERS_WRITE:
/* 162 */           result |= 0x2;
/*     */           continue;
/*     */         case OTHERS_EXECUTE:
/* 165 */           result |= 0x1;
/*     */           continue;
/*     */       } 
/* 168 */       throw new AssertionError();
/*     */     } 
/*     */     
/* 171 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\UnixAttributeProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */