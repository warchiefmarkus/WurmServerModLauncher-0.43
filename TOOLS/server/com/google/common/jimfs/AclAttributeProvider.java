/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.attribute.AclEntry;
/*     */ import java.nio.file.attribute.AclFileAttributeView;
/*     */ import java.nio.file.attribute.FileAttributeView;
/*     */ import java.nio.file.attribute.FileOwnerAttributeView;
/*     */ import java.nio.file.attribute.UserPrincipal;
/*     */ import java.util.List;
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
/*     */ final class AclAttributeProvider
/*     */   extends AttributeProvider
/*     */ {
/*  43 */   private static final ImmutableSet<String> ATTRIBUTES = ImmutableSet.of("acl");
/*     */   
/*  45 */   private static final ImmutableSet<String> INHERITED_VIEWS = ImmutableSet.of("owner");
/*     */   
/*  47 */   private static final ImmutableList<AclEntry> DEFAULT_ACL = ImmutableList.of();
/*     */ 
/*     */   
/*     */   public String name() {
/*  51 */     return "acl";
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<String> inherits() {
/*  56 */     return INHERITED_VIEWS;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<String> fixedAttributes() {
/*  61 */     return ATTRIBUTES;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableMap<String, ?> defaultValues(Map<String, ?> userProvidedDefaults) {
/*  66 */     Object userProvidedAcl = userProvidedDefaults.get("acl:acl");
/*     */     
/*  68 */     ImmutableList<AclEntry> acl = DEFAULT_ACL;
/*  69 */     if (userProvidedAcl != null) {
/*  70 */       acl = toAcl(checkType("acl", "acl", userProvidedAcl, List.class));
/*     */     }
/*     */     
/*  73 */     return ImmutableMap.of("acl:acl", acl);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object get(File file, String attribute) {
/*  79 */     if (attribute.equals("acl")) {
/*  80 */       return file.getAttribute("acl", "acl");
/*     */     }
/*     */     
/*  83 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(File file, String view, String attribute, Object value, boolean create) {
/*  88 */     if (attribute.equals("acl")) {
/*  89 */       checkNotCreate(view, attribute, create);
/*  90 */       file.setAttribute("acl", "acl", toAcl(checkType(view, attribute, value, List.class)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static ImmutableList<AclEntry> toAcl(List<?> list) {
/*  96 */     ImmutableList<?> copy = ImmutableList.copyOf(list);
/*  97 */     for (Object obj : copy) {
/*  98 */       if (!(obj instanceof AclEntry)) {
/*  99 */         throw new IllegalArgumentException("invalid element for attribute 'acl:acl': should be List<AclEntry>, found element of type " + obj.getClass());
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 105 */     return (ImmutableList)copy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<AclFileAttributeView> viewType() {
/* 110 */     return AclFileAttributeView.class;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AclFileAttributeView view(FileLookup lookup, ImmutableMap<String, FileAttributeView> inheritedViews) {
/* 116 */     return new View(lookup, (FileOwnerAttributeView)inheritedViews.get("owner"));
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class View
/*     */     extends AbstractAttributeView
/*     */     implements AclFileAttributeView
/*     */   {
/*     */     private final FileOwnerAttributeView ownerView;
/*     */     
/*     */     public View(FileLookup lookup, FileOwnerAttributeView ownerView) {
/* 127 */       super(lookup);
/* 128 */       this.ownerView = (FileOwnerAttributeView)Preconditions.checkNotNull(ownerView);
/*     */     }
/*     */ 
/*     */     
/*     */     public String name() {
/* 133 */       return "acl";
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public List<AclEntry> getAcl() throws IOException {
/* 139 */       return (List<AclEntry>)lookupFile().getAttribute("acl", "acl");
/*     */     }
/*     */ 
/*     */     
/*     */     public void setAcl(List<AclEntry> acl) throws IOException {
/* 144 */       Preconditions.checkNotNull(acl);
/* 145 */       lookupFile().setAttribute("acl", "acl", ImmutableList.copyOf(acl));
/*     */     }
/*     */ 
/*     */     
/*     */     public UserPrincipal getOwner() throws IOException {
/* 150 */       return this.ownerView.getOwner();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setOwner(UserPrincipal owner) throws IOException {
/* 155 */       this.ownerView.setOwner(owner);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\AclAttributeProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */