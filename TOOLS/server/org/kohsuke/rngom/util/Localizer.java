/*    */ package org.kohsuke.rngom.util;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.MissingResourceException;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Localizer
/*    */ {
/*    */   private final Class cls;
/*    */   private ResourceBundle bundle;
/*    */   private final Localizer parent;
/*    */   
/*    */   public Localizer(Class cls) {
/* 20 */     this(null, cls);
/*    */   }
/*    */   
/*    */   public Localizer(Localizer parent, Class cls) {
/* 24 */     this.parent = parent;
/* 25 */     this.cls = cls;
/*    */   }
/*    */   
/*    */   private String getString(String key) {
/*    */     try {
/* 30 */       return getBundle().getString(key);
/* 31 */     } catch (MissingResourceException e) {
/*    */       
/* 33 */       if (this.parent != null) {
/* 34 */         return this.parent.getString(key);
/*    */       }
/* 36 */       throw e;
/*    */     } 
/*    */   }
/*    */   
/*    */   public String message(String key) {
/* 41 */     return MessageFormat.format(getString(key), new Object[0]);
/*    */   }
/*    */   
/*    */   public String message(String key, Object arg) {
/* 45 */     return MessageFormat.format(getString(key), new Object[] { arg });
/*    */   }
/*    */ 
/*    */   
/*    */   public String message(String key, Object arg1, Object arg2) {
/* 50 */     return MessageFormat.format(getString(key), new Object[] { arg1, arg2 });
/*    */   }
/*    */ 
/*    */   
/*    */   public String message(String key, Object[] args) {
/* 55 */     return MessageFormat.format(getString(key), args);
/*    */   }
/*    */   
/*    */   private ResourceBundle getBundle() {
/* 59 */     if (this.bundle == null) {
/* 60 */       String s = this.cls.getName();
/* 61 */       int i = s.lastIndexOf('.');
/* 62 */       if (i > 0) {
/* 63 */         s = s.substring(0, i + 1);
/*    */       } else {
/* 65 */         s = "";
/* 66 */       }  this.bundle = ResourceBundle.getBundle(s + "Messages");
/*    */     } 
/* 68 */     return this.bundle;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngo\\util\Localizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */