/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.attribute.GroupPrincipal;
/*     */ import java.nio.file.attribute.UserPrincipal;
/*     */ import java.nio.file.attribute.UserPrincipalLookupService;
/*     */ import java.nio.file.attribute.UserPrincipalNotFoundException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class UserLookupService
/*     */   extends UserPrincipalLookupService
/*     */ {
/*     */   private final boolean supportsGroups;
/*     */   
/*     */   public UserLookupService(boolean supportsGroups) {
/*  37 */     this.supportsGroups = supportsGroups;
/*     */   }
/*     */ 
/*     */   
/*     */   public UserPrincipal lookupPrincipalByName(String name) {
/*  42 */     return createUserPrincipal(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public GroupPrincipal lookupPrincipalByGroupName(String group) throws IOException {
/*  47 */     if (!this.supportsGroups) {
/*  48 */       throw new UserPrincipalNotFoundException(group);
/*     */     }
/*  50 */     return createGroupPrincipal(group);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static UserPrincipal createUserPrincipal(String name) {
/*  57 */     return new JimfsUserPrincipal(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static GroupPrincipal createGroupPrincipal(String name) {
/*  64 */     return new JimfsGroupPrincipal(name);
/*     */   }
/*     */ 
/*     */   
/*     */   private static abstract class NamedPrincipal
/*     */     implements UserPrincipal
/*     */   {
/*     */     protected final String name;
/*     */ 
/*     */     
/*     */     private NamedPrincipal(String name) {
/*  75 */       this.name = (String)Preconditions.checkNotNull(name);
/*     */     }
/*     */ 
/*     */     
/*     */     public final String getName() {
/*  80 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public final int hashCode() {
/*  85 */       return this.name.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public final String toString() {
/*  90 */       return this.name;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class JimfsUserPrincipal
/*     */     extends NamedPrincipal
/*     */   {
/*     */     private JimfsUserPrincipal(String name) {
/* 100 */       super(name);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 105 */       return (obj instanceof JimfsUserPrincipal && getName().equals(((JimfsUserPrincipal)obj).getName()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class JimfsGroupPrincipal
/*     */     extends NamedPrincipal
/*     */     implements GroupPrincipal
/*     */   {
/*     */     private JimfsGroupPrincipal(String name) {
/* 116 */       super(name);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 121 */       return (obj instanceof JimfsGroupPrincipal && ((JimfsGroupPrincipal)obj).name.equals(this.name));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\UserLookupService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */