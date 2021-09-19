/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.attribute.FileAttributeView;
/*     */ import java.nio.file.attribute.FileOwnerAttributeView;
/*     */ import java.nio.file.attribute.UserPrincipal;
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
/*     */ final class OwnerAttributeProvider
/*     */   extends AttributeProvider
/*     */ {
/*  40 */   private static final ImmutableSet<String> ATTRIBUTES = ImmutableSet.of("owner");
/*     */   
/*  42 */   private static final UserPrincipal DEFAULT_OWNER = UserLookupService.createUserPrincipal("user");
/*     */ 
/*     */   
/*     */   public String name() {
/*  46 */     return "owner";
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<String> fixedAttributes() {
/*  51 */     return ATTRIBUTES;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableMap<String, ?> defaultValues(Map<String, ?> userProvidedDefaults) {
/*  56 */     Object userProvidedOwner = userProvidedDefaults.get("owner:owner");
/*     */     
/*  58 */     UserPrincipal owner = DEFAULT_OWNER;
/*  59 */     if (userProvidedOwner != null) {
/*  60 */       if (userProvidedOwner instanceof String) {
/*  61 */         owner = UserLookupService.createUserPrincipal((String)userProvidedOwner);
/*     */       } else {
/*  63 */         throw invalidType("owner", "owner", userProvidedOwner, new Class[] { String.class, UserPrincipal.class });
/*     */       } 
/*     */     }
/*     */     
/*  67 */     return ImmutableMap.of("owner:owner", owner);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object get(File file, String attribute) {
/*  73 */     if (attribute.equals("owner")) {
/*  74 */       return file.getAttribute("owner", "owner");
/*     */     }
/*  76 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(File file, String view, String attribute, Object value, boolean create) {
/*  81 */     if (attribute.equals("owner")) {
/*  82 */       UserPrincipal user = checkType(view, attribute, value, UserPrincipal.class);
/*     */       
/*  84 */       if (!(user instanceof UserLookupService.JimfsUserPrincipal)) {
/*  85 */         user = UserLookupService.createUserPrincipal(user.getName());
/*     */       }
/*  87 */       file.setAttribute("owner", "owner", user);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<FileOwnerAttributeView> viewType() {
/*  93 */     return FileOwnerAttributeView.class;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FileOwnerAttributeView view(FileLookup lookup, ImmutableMap<String, FileAttributeView> inheritedViews) {
/*  99 */     return new View(lookup);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class View
/*     */     extends AbstractAttributeView
/*     */     implements FileOwnerAttributeView
/*     */   {
/*     */     public View(FileLookup lookup) {
/* 108 */       super(lookup);
/*     */     }
/*     */ 
/*     */     
/*     */     public String name() {
/* 113 */       return "owner";
/*     */     }
/*     */ 
/*     */     
/*     */     public UserPrincipal getOwner() throws IOException {
/* 118 */       return (UserPrincipal)lookupFile().getAttribute("owner", "owner");
/*     */     }
/*     */ 
/*     */     
/*     */     public void setOwner(UserPrincipal owner) throws IOException {
/* 123 */       lookupFile().setAttribute("owner", "owner", Preconditions.checkNotNull(owner));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\OwnerAttributeProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */