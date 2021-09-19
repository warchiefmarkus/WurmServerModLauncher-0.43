/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.util.Set;
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
/*     */ final class Options
/*     */ {
/*  48 */   public static final ImmutableSet<LinkOption> NOFOLLOW_LINKS = ImmutableSet.of(LinkOption.NOFOLLOW_LINKS);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   public static final ImmutableSet<LinkOption> FOLLOW_LINKS = ImmutableSet.of();
/*     */   
/*  56 */   private static final ImmutableSet<OpenOption> DEFAULT_READ = ImmutableSet.of(StandardOpenOption.READ);
/*     */   
/*  58 */   private static final ImmutableSet<OpenOption> DEFAULT_READ_NOFOLLOW_LINKS = ImmutableSet.of(StandardOpenOption.READ, LinkOption.NOFOLLOW_LINKS);
/*     */ 
/*     */   
/*  61 */   private static final ImmutableSet<OpenOption> DEFAULT_WRITE = ImmutableSet.of(StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ImmutableSet<LinkOption> getLinkOptions(LinkOption... options) {
/*  68 */     return (options.length == 0) ? FOLLOW_LINKS : NOFOLLOW_LINKS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ImmutableSet<OpenOption> getOptionsForChannel(Set<? extends OpenOption> options) {
/*  75 */     if (options.isEmpty()) {
/*  76 */       return DEFAULT_READ;
/*     */     }
/*     */     
/*  79 */     boolean append = options.contains(StandardOpenOption.APPEND);
/*  80 */     boolean write = (append || options.contains(StandardOpenOption.WRITE));
/*  81 */     boolean read = (!write || options.contains(StandardOpenOption.READ));
/*     */     
/*  83 */     if (read) {
/*  84 */       if (append) {
/*  85 */         throw new UnsupportedOperationException("'READ' + 'APPEND' not allowed");
/*     */       }
/*     */       
/*  88 */       if (!write)
/*     */       {
/*  90 */         return options.contains(LinkOption.NOFOLLOW_LINKS) ? DEFAULT_READ_NOFOLLOW_LINKS : DEFAULT_READ;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  99 */     if (options.contains(StandardOpenOption.WRITE)) {
/* 100 */       return ImmutableSet.copyOf(options);
/*     */     }
/* 102 */     return (new ImmutableSet.Builder()).add(StandardOpenOption.WRITE).addAll(options).build();
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
/*     */   public static ImmutableSet<OpenOption> getOptionsForInputStream(OpenOption... options) {
/* 114 */     boolean nofollowLinks = false;
/* 115 */     for (OpenOption option : options) {
/* 116 */       if (Preconditions.checkNotNull(option) != StandardOpenOption.READ) {
/* 117 */         if (option == LinkOption.NOFOLLOW_LINKS) {
/* 118 */           nofollowLinks = true;
/*     */         } else {
/* 120 */           throw new UnsupportedOperationException("'" + option + "' not allowed");
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 126 */     return nofollowLinks ? (ImmutableSet)NOFOLLOW_LINKS : (ImmutableSet)FOLLOW_LINKS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ImmutableSet<OpenOption> getOptionsForOutputStream(OpenOption... options) {
/* 134 */     if (options.length == 0) {
/* 135 */       return DEFAULT_WRITE;
/*     */     }
/*     */     
/* 138 */     ImmutableSet<OpenOption> result = ImmutableSet.copyOf((Object[])options);
/* 139 */     if (result.contains(StandardOpenOption.READ)) {
/* 140 */       throw new UnsupportedOperationException("'READ' not allowed");
/*     */     }
/* 142 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ImmutableSet<CopyOption> getMoveOptions(CopyOption... options) {
/* 149 */     return ImmutableSet.copyOf(Lists.asList(LinkOption.NOFOLLOW_LINKS, (Object[])options));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ImmutableSet<CopyOption> getCopyOptions(CopyOption... options) {
/* 156 */     ImmutableSet<CopyOption> result = ImmutableSet.copyOf((Object[])options);
/* 157 */     if (result.contains(StandardCopyOption.ATOMIC_MOVE)) {
/* 158 */       throw new UnsupportedOperationException("'ATOMIC_MOVE' not allowed");
/*     */     }
/* 160 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\Options.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */