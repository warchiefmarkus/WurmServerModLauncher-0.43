/*    */ package com.sun.xml.txw2;
/*    */ 
/*    */ import javax.xml.namespace.QName;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface DatatypeWriter<DT>
/*    */ {
/* 52 */   public static final DatatypeWriter<?>[] BUILDIN = new DatatypeWriter[] { new DatatypeWriter<String>()
/*    */       {
/*    */         public Class<String> getType() {
/* 55 */           return String.class;
/*    */         }
/*    */         public void print(String s, NamespaceResolver resolver, StringBuilder buf) {
/* 58 */           buf.append(s);
/*    */         }
/*    */       }, new DatatypeWriter<Integer>()
/*    */       {
/*    */         public Class<Integer> getType() {
/* 63 */           return Integer.class;
/*    */         }
/*    */         public void print(Integer i, NamespaceResolver resolver, StringBuilder buf) {
/* 66 */           buf.append(i);
/*    */         }
/*    */       }, new DatatypeWriter<Float>()
/*    */       {
/*    */         public Class<Float> getType() {
/* 71 */           return Float.class;
/*    */         }
/*    */         public void print(Float f, NamespaceResolver resolver, StringBuilder buf) {
/* 74 */           buf.append(f);
/*    */         }
/*    */       }, new DatatypeWriter<Double>()
/*    */       {
/*    */         public Class<Double> getType() {
/* 79 */           return Double.class;
/*    */         }
/*    */         public void print(Double d, NamespaceResolver resolver, StringBuilder buf) {
/* 82 */           buf.append(d);
/*    */         }
/*    */       }, new DatatypeWriter<QName>()
/*    */       {
/*    */         public Class<QName> getType() {
/* 87 */           return QName.class;
/*    */         }
/*    */         public void print(QName qn, NamespaceResolver resolver, StringBuilder buf) {
/* 90 */           String p = resolver.getPrefix(qn.getNamespaceURI());
/* 91 */           if (p.length() != 0)
/* 92 */             buf.append(p).append(':'); 
/* 93 */           buf.append(qn.getLocalPart());
/*    */         }
/*    */       } };
/*    */   
/*    */   Class<DT> getType();
/*    */   
/*    */   void print(DT paramDT, NamespaceResolver paramNamespaceResolver, StringBuilder paramStringBuilder);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\DatatypeWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */