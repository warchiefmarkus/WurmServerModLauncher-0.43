/*    */ package com.sun.codemodel;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JCommentPart
/*    */   extends ArrayList<Object>
/*    */ {
/*    */   public JCommentPart append(Object o) {
/* 29 */     add(o);
/* 30 */     return this;
/*    */   }
/*    */   
/*    */   public boolean add(Object o) {
/* 34 */     flattenAppend(o);
/* 35 */     return true;
/*    */   }
/*    */   
/*    */   private void flattenAppend(Object value) {
/* 39 */     if (value == null)
/* 40 */       return;  if (value instanceof Object[]) {
/* 41 */       for (Object o : (Object[])value) {
/* 42 */         flattenAppend(o);
/*    */       }
/* 44 */     } else if (value instanceof java.util.Collection) {
/* 45 */       for (Object o : value)
/* 46 */         flattenAppend(o); 
/*    */     } else {
/* 48 */       super.add(value);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void format(JFormatter f, String indent) {
/* 55 */     if (!f.isPrinting()) {
/*    */       
/* 57 */       for (Object o : this) {
/* 58 */         if (o instanceof JClass)
/* 59 */           f.g((JClass)o); 
/*    */       } 
/*    */       return;
/*    */     } 
/* 63 */     if (!isEmpty()) {
/* 64 */       f.p(indent);
/*    */     }
/* 66 */     Iterator itr = iterator();
/* 67 */     while (itr.hasNext()) {
/* 68 */       Object o = itr.next();
/*    */       
/* 70 */       if (o instanceof String) {
/*    */         
/* 72 */         String s = (String)o; int idx;
/* 73 */         while ((idx = s.indexOf('\n')) != -1) {
/* 74 */           String line = s.substring(0, idx);
/* 75 */           if (line.length() > 0)
/* 76 */             f.p(line); 
/* 77 */           s = s.substring(idx + 1);
/* 78 */           f.nl().p(indent);
/*    */         } 
/* 80 */         if (s.length() != 0)
/* 81 */           f.p(s);  continue;
/*    */       } 
/* 83 */       if (o instanceof JClass) {
/*    */         
/* 85 */         ((JClass)o).printLink(f); continue;
/*    */       } 
/* 87 */       if (o instanceof JType) {
/* 88 */         f.g((JType)o); continue;
/*    */       } 
/* 90 */       throw new IllegalStateException();
/*    */     } 
/*    */     
/* 93 */     if (!isEmpty())
/* 94 */       f.nl(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JCommentPart.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */