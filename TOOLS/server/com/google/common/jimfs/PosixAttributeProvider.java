/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.attribute.BasicFileAttributeView;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileAttributeView;
/*     */ import java.nio.file.attribute.FileOwnerAttributeView;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import java.nio.file.attribute.GroupPrincipal;
/*     */ import java.nio.file.attribute.PosixFileAttributeView;
/*     */ import java.nio.file.attribute.PosixFileAttributes;
/*     */ import java.nio.file.attribute.PosixFilePermission;
/*     */ import java.nio.file.attribute.PosixFilePermissions;
/*     */ import java.nio.file.attribute.UserPrincipal;
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
/*     */ final class PosixAttributeProvider
/*     */   extends AttributeProvider
/*     */ {
/*  50 */   private static final ImmutableSet<String> ATTRIBUTES = ImmutableSet.of("group", "permissions");
/*     */   
/*  52 */   private static final ImmutableSet<String> INHERITED_VIEWS = ImmutableSet.of("basic", "owner");
/*     */   
/*  54 */   private static final GroupPrincipal DEFAULT_GROUP = UserLookupService.createGroupPrincipal("group");
/*  55 */   private static final ImmutableSet<PosixFilePermission> DEFAULT_PERMISSIONS = Sets.immutableEnumSet(PosixFilePermissions.fromString("rw-r--r--"));
/*     */ 
/*     */ 
/*     */   
/*     */   public String name() {
/*  60 */     return "posix";
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<String> inherits() {
/*  65 */     return INHERITED_VIEWS;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<String> fixedAttributes() {
/*  70 */     return ATTRIBUTES;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<String, ?> defaultValues(Map<String, ?> userProvidedDefaults) {
/*  76 */     Object userProvidedGroup = userProvidedDefaults.get("posix:group");
/*     */     
/*  78 */     UserPrincipal group = DEFAULT_GROUP;
/*  79 */     if (userProvidedGroup != null) {
/*  80 */       if (userProvidedGroup instanceof String) {
/*  81 */         group = UserLookupService.createGroupPrincipal((String)userProvidedGroup);
/*     */       } else {
/*  83 */         throw new IllegalArgumentException("invalid type " + userProvidedGroup.getClass().getName() + " for attribute 'posix:group': should be one of " + String.class + " or " + GroupPrincipal.class);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  90 */     Object userProvidedPermissions = userProvidedDefaults.get("posix:permissions");
/*     */     
/*  92 */     ImmutableSet<PosixFilePermission> immutableSet = DEFAULT_PERMISSIONS;
/*  93 */     if (userProvidedPermissions != null) {
/*  94 */       if (userProvidedPermissions instanceof String) {
/*  95 */         immutableSet = Sets.immutableEnumSet(PosixFilePermissions.fromString((String)userProvidedPermissions));
/*     */       
/*     */       }
/*  98 */       else if (userProvidedPermissions instanceof Set) {
/*  99 */         immutableSet = toPermissions((Set)userProvidedPermissions);
/*     */       } else {
/* 101 */         throw new IllegalArgumentException("invalid type " + userProvidedPermissions.getClass().getName() + " for attribute 'posix:permissions': should be one of " + String.class + " or " + Set.class);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 108 */     return ImmutableMap.of("posix:group", group, "posix:permissions", immutableSet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object get(File file, String attribute) {
/* 116 */     switch (attribute) {
/*     */       case "group":
/* 118 */         return file.getAttribute("posix", "group");
/*     */       case "permissions":
/* 120 */         return file.getAttribute("posix", "permissions");
/*     */     } 
/* 122 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(File file, String view, String attribute, Object value, boolean create) {
/*     */     GroupPrincipal group;
/* 128 */     switch (attribute) {
/*     */       case "group":
/* 130 */         checkNotCreate(view, attribute, create);
/*     */         
/* 132 */         group = checkType(view, attribute, value, GroupPrincipal.class);
/* 133 */         if (!(group instanceof UserLookupService.JimfsGroupPrincipal)) {
/* 134 */           group = UserLookupService.createGroupPrincipal(group.getName());
/*     */         }
/* 136 */         file.setAttribute("posix", "group", group);
/*     */         break;
/*     */       case "permissions":
/* 139 */         file.setAttribute("posix", "permissions", toPermissions(checkType(view, attribute, value, Set.class)));
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ImmutableSet<PosixFilePermission> toPermissions(Set<?> set) {
/* 148 */     ImmutableSet<?> copy = ImmutableSet.copyOf(set);
/* 149 */     for (Object obj : copy) {
/* 150 */       if (!(obj instanceof PosixFilePermission)) {
/* 151 */         throw new IllegalArgumentException("invalid element for attribute 'posix:permissions': should be Set<PosixFilePermission>, found element of type " + obj.getClass());
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 158 */     return Sets.immutableEnumSet((Iterable)copy);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<PosixFileAttributeView> viewType() {
/* 163 */     return PosixFileAttributeView.class;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PosixFileAttributeView view(FileLookup lookup, ImmutableMap<String, FileAttributeView> inheritedViews) {
/* 169 */     return new View(lookup, (BasicFileAttributeView)inheritedViews.get("basic"), (FileOwnerAttributeView)inheritedViews.get("owner"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<PosixFileAttributes> attributesType() {
/* 177 */     return PosixFileAttributes.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public PosixFileAttributes readAttributes(File file) {
/* 182 */     return new Attributes(file);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class View
/*     */     extends AbstractAttributeView
/*     */     implements PosixFileAttributeView
/*     */   {
/*     */     private final BasicFileAttributeView basicView;
/*     */     
/*     */     private final FileOwnerAttributeView ownerView;
/*     */     
/*     */     protected View(FileLookup lookup, BasicFileAttributeView basicView, FileOwnerAttributeView ownerView) {
/* 195 */       super(lookup);
/* 196 */       this.basicView = (BasicFileAttributeView)Preconditions.checkNotNull(basicView);
/* 197 */       this.ownerView = (FileOwnerAttributeView)Preconditions.checkNotNull(ownerView);
/*     */     }
/*     */ 
/*     */     
/*     */     public String name() {
/* 202 */       return "posix";
/*     */     }
/*     */ 
/*     */     
/*     */     public PosixFileAttributes readAttributes() throws IOException {
/* 207 */       return new PosixAttributeProvider.Attributes(lookupFile());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setTimes(FileTime lastModifiedTime, FileTime lastAccessTime, FileTime createTime) throws IOException {
/* 213 */       this.basicView.setTimes(lastModifiedTime, lastAccessTime, createTime);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setPermissions(Set<PosixFilePermission> perms) throws IOException {
/* 218 */       lookupFile().setAttribute("posix", "permissions", ImmutableSet.copyOf(perms));
/*     */     }
/*     */ 
/*     */     
/*     */     public void setGroup(GroupPrincipal group) throws IOException {
/* 223 */       lookupFile().setAttribute("posix", "group", Preconditions.checkNotNull(group));
/*     */     }
/*     */ 
/*     */     
/*     */     public UserPrincipal getOwner() throws IOException {
/* 228 */       return this.ownerView.getOwner();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setOwner(UserPrincipal owner) throws IOException {
/* 233 */       this.ownerView.setOwner(owner);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class Attributes
/*     */     extends BasicAttributeProvider.Attributes
/*     */     implements PosixFileAttributes
/*     */   {
/*     */     private final UserPrincipal owner;
/*     */     
/*     */     private final GroupPrincipal group;
/*     */     private final ImmutableSet<PosixFilePermission> permissions;
/*     */     
/*     */     protected Attributes(File file) {
/* 248 */       super(file);
/* 249 */       this.owner = (UserPrincipal)file.getAttribute("owner", "owner");
/* 250 */       this.group = (GroupPrincipal)file.getAttribute("posix", "group");
/* 251 */       this.permissions = (ImmutableSet<PosixFilePermission>)file.getAttribute("posix", "permissions");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public UserPrincipal owner() {
/* 257 */       return this.owner;
/*     */     }
/*     */ 
/*     */     
/*     */     public GroupPrincipal group() {
/* 262 */       return this.group;
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableSet<PosixFilePermission> permissions() {
/* 267 */       return this.permissions;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\PosixAttributeProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */