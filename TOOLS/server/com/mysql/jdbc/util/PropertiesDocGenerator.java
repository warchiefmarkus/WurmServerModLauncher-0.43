/*    */ package com.mysql.jdbc.util;
/*    */ 
/*    */ import com.mysql.jdbc.ConnectionPropertiesImpl;
/*    */ import java.sql.SQLException;
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
/*    */ public class PropertiesDocGenerator
/*    */   extends ConnectionPropertiesImpl
/*    */ {
/*    */   public static void main(String[] args) throws SQLException {
/* 38 */     System.out.println((new PropertiesDocGenerator()).exposeAsXml());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdb\\util\PropertiesDocGenerator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */