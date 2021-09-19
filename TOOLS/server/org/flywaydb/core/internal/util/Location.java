/*     */ package org.flywaydb.core.internal.util;
/*     */ 
/*     */ import org.flywaydb.core.api.FlywayException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Location
/*     */   implements Comparable<Location>
/*     */ {
/*     */   private static final String CLASSPATH_PREFIX = "classpath:";
/*     */   public static final String FILESYSTEM_PREFIX = "filesystem:";
/*     */   private String prefix;
/*     */   private String path;
/*     */   
/*     */   public Location(String descriptor) {
/*  50 */     String normalizedDescriptor = descriptor.trim().replace("\\", "/");
/*     */     
/*  52 */     if (normalizedDescriptor.contains(":")) {
/*  53 */       this.prefix = normalizedDescriptor.substring(0, normalizedDescriptor.indexOf(":") + 1);
/*  54 */       this.path = normalizedDescriptor.substring(normalizedDescriptor.indexOf(":") + 1);
/*     */     } else {
/*  56 */       this.prefix = "classpath:";
/*  57 */       this.path = normalizedDescriptor;
/*     */     } 
/*     */     
/*  60 */     if (isClassPath()) {
/*  61 */       this.path = this.path.replace(".", "/");
/*  62 */       if (this.path.startsWith("/")) {
/*  63 */         this.path = this.path.substring(1);
/*     */       }
/*     */     }
/*  66 */     else if (!isFileSystem()) {
/*  67 */       throw new FlywayException("Unknown prefix for location (should be either filesystem: or classpath:): " + normalizedDescriptor);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  72 */     if (this.path.endsWith("/")) {
/*  73 */       this.path = this.path.substring(0, this.path.length() - 1);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClassPath() {
/*  83 */     return "classpath:".equals(this.prefix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFileSystem() {
/*  92 */     return "filesystem:".equals(this.prefix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isParentOf(Location other) {
/* 102 */     return (other.getDescriptor() + "/").startsWith(getDescriptor() + "/");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPrefix() {
/* 109 */     return this.prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPath() {
/* 116 */     return this.path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescriptor() {
/* 123 */     return this.prefix + this.path;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(Location o) {
/* 128 */     return getDescriptor().compareTo(o.getDescriptor());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 133 */     if (this == o) return true; 
/* 134 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/* 136 */     Location location = (Location)o;
/*     */     
/* 138 */     return getDescriptor().equals(location.getDescriptor());
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 143 */     return getDescriptor().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 151 */     return getDescriptor();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\Location.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */