/*     */ package org.flywaydb.core.internal.util;
/*     */ 
/*     */ import org.flywaydb.core.internal.util.logging.Log;
/*     */ import org.flywaydb.core.internal.util.logging.LogFactory;
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
/*     */ public final class FeatureDetector
/*     */ {
/*  25 */   private static final Log LOG = LogFactory.getLog(FeatureDetector.class);
/*     */   
/*     */   private ClassLoader classLoader;
/*     */   
/*     */   private Boolean apacheCommonsLoggingAvailable;
/*     */   private Boolean slf4jAvailable;
/*     */   private Boolean springJdbcAvailable;
/*     */   private Boolean jbossVFSv2Available;
/*     */   private Boolean jbossVFSv3Available;
/*     */   private Boolean osgiFrameworkAvailable;
/*     */   private Boolean androidAvailable;
/*     */   
/*     */   public FeatureDetector(ClassLoader classLoader) {
/*  38 */     this.classLoader = classLoader;
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
/*     */   public boolean isApacheCommonsLoggingAvailable() {
/*  82 */     if (this.apacheCommonsLoggingAvailable == null) {
/*  83 */       this.apacheCommonsLoggingAvailable = Boolean.valueOf(ClassUtils.isPresent("org.apache.commons.logging.Log", this.classLoader));
/*     */     }
/*     */     
/*  86 */     return this.apacheCommonsLoggingAvailable.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSlf4jAvailable() {
/*  95 */     if (this.slf4jAvailable == null) {
/*  96 */       this.slf4jAvailable = Boolean.valueOf(ClassUtils.isPresent("org.slf4j.Logger", this.classLoader));
/*     */     }
/*     */     
/*  99 */     return this.slf4jAvailable.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSpringJdbcAvailable() {
/* 108 */     if (this.springJdbcAvailable == null) {
/* 109 */       this.springJdbcAvailable = Boolean.valueOf(ClassUtils.isPresent("org.springframework.jdbc.core.JdbcTemplate", this.classLoader));
/* 110 */       LOG.debug("Spring Jdbc available: " + this.springJdbcAvailable);
/*     */     } 
/*     */     
/* 113 */     return this.springJdbcAvailable.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isJBossVFSv2Available() {
/* 122 */     if (this.jbossVFSv2Available == null) {
/* 123 */       this.jbossVFSv2Available = Boolean.valueOf(ClassUtils.isPresent("org.jboss.virtual.VFS", this.classLoader));
/* 124 */       LOG.debug("JBoss VFS v2 available: " + this.jbossVFSv2Available);
/*     */     } 
/*     */     
/* 127 */     return this.jbossVFSv2Available.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isJBossVFSv3Available() {
/* 136 */     if (this.jbossVFSv3Available == null) {
/* 137 */       this.jbossVFSv3Available = Boolean.valueOf(ClassUtils.isPresent("org.jboss.vfs.VFS", this.classLoader));
/* 138 */       LOG.debug("JBoss VFS v3 available: " + this.jbossVFSv3Available);
/*     */     } 
/*     */     
/* 141 */     return this.jbossVFSv3Available.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOsgiFrameworkAvailable() {
/* 150 */     if (this.osgiFrameworkAvailable == null) {
/* 151 */       this.osgiFrameworkAvailable = Boolean.valueOf(ClassUtils.isPresent("org.osgi.framework.Bundle", FeatureDetector.class.getClassLoader()));
/* 152 */       LOG.debug("OSGi framework available: " + this.osgiFrameworkAvailable);
/*     */     } 
/*     */     
/* 155 */     return this.osgiFrameworkAvailable.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAndroidAvailable() {
/* 164 */     if (this.androidAvailable == null) {
/* 165 */       this.androidAvailable = Boolean.valueOf("Android Runtime".equals(System.getProperty("java.runtime.name")));
/*     */     }
/*     */     
/* 168 */     return this.androidAvailable.booleanValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\FeatureDetector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */