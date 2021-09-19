/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Ordering;
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
/*     */ final class Name
/*     */ {
/*  44 */   static final Name EMPTY = new Name("", "");
/*     */ 
/*     */   
/*  47 */   public static final Name SELF = new Name(".", ".");
/*     */ 
/*     */   
/*  50 */   public static final Name PARENT = new Name("..", "..");
/*     */   
/*     */   private final String display;
/*     */   private final String canonical;
/*     */   
/*     */   @VisibleForTesting
/*     */   static Name simple(String name) {
/*  57 */     switch (name) {
/*     */       case ".":
/*  59 */         return SELF;
/*     */       case "..":
/*  61 */         return PARENT;
/*     */     } 
/*  63 */     return new Name(name, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Name create(String display, String canonical) {
/*  71 */     return new Name(display, canonical);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Name(String display, String canonical) {
/*  78 */     this.display = (String)Preconditions.checkNotNull(display);
/*  79 */     this.canonical = (String)Preconditions.checkNotNull(canonical);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/*  84 */     if (obj instanceof Name) {
/*  85 */       Name other = (Name)obj;
/*  86 */       return this.canonical.equals(other.canonical);
/*     */     } 
/*  88 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  93 */     return Util.smearHash(this.canonical.hashCode());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  98 */     return this.display;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Ordering<Name> displayOrdering() {
/* 105 */     return DISPLAY_ORDERING;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Ordering<Name> canonicalOrdering() {
/* 112 */     return CANONICAL_ORDERING;
/*     */   }
/*     */   
/* 115 */   private static final Ordering<Name> DISPLAY_ORDERING = Ordering.natural().onResultOf(new Function<Name, String>()
/*     */       {
/*     */ 
/*     */         
/*     */         public String apply(Name name)
/*     */         {
/* 121 */           return name.display;
/*     */         }
/*     */       });
/*     */   
/* 125 */   private static final Ordering<Name> CANONICAL_ORDERING = Ordering.natural().onResultOf(new Function<Name, String>()
/*     */       {
/*     */ 
/*     */         
/*     */         public String apply(Name name)
/*     */         {
/* 131 */           return name.canonical;
/*     */         }
/*     */       });
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\Name.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */