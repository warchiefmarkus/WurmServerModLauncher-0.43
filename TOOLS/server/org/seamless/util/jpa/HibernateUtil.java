/*    */ package org.seamless.util.jpa;
/*    */ 
/*    */ import org.hibernate.SessionFactory;
/*    */ import org.hibernate.cfg.Configuration;
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
/*    */ public class HibernateUtil
/*    */ {
/*    */   public static final Configuration configuration;
/*    */   public static final SessionFactory sessionFactory;
/*    */   
/*    */   static {
/*    */     try {
/* 30 */       configuration = (new Configuration()).configure();
/* 31 */       sessionFactory = configuration.buildSessionFactory();
/* 32 */     } catch (Throwable t) {
/* 33 */       throw new RuntimeException(t);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static Configuration getConfiguration() {
/* 38 */     return configuration;
/*    */   }
/*    */   
/*    */   public static SessionFactory getSessionFactory() {
/* 42 */     return sessionFactory;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\jpa\HibernateUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */