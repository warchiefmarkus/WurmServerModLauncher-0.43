/*    */ package com.sun.istack.tools;
/*    */ 
/*    */ import java.util.Collection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MaskingClassLoader
/*    */   extends ClassLoader
/*    */ {
/*    */   private final String[] masks;
/*    */   
/*    */   public MaskingClassLoader(String... masks) {
/* 19 */     this.masks = masks;
/*    */   }
/*    */   
/*    */   public MaskingClassLoader(Collection<String> masks) {
/* 23 */     this(masks.<String>toArray(new String[masks.size()]));
/*    */   }
/*    */   
/*    */   public MaskingClassLoader(ClassLoader parent, String... masks) {
/* 27 */     super(parent);
/* 28 */     this.masks = masks;
/*    */   }
/*    */   
/*    */   public MaskingClassLoader(ClassLoader parent, Collection<String> masks) {
/* 32 */     this(parent, masks.<String>toArray(new String[masks.size()]));
/*    */   }
/*    */ 
/*    */   
/*    */   protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
/* 37 */     for (String mask : this.masks) {
/* 38 */       if (name.startsWith(mask)) {
/* 39 */         throw new ClassNotFoundException();
/*    */       }
/*    */     } 
/* 42 */     return super.loadClass(name, resolve);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\istack\tools\MaskingClassLoader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */