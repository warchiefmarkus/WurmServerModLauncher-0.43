/*    */ package coffee.keenan.network.config;
/*    */ 
/*    */ 
/*    */ public class DefaultConfiguration
/*    */   implements IConfiguration
/*    */ {
/*    */   public int getTimeout() {
/*  8 */     return 3000;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTestUrl() {
/* 14 */     return "www.google.com";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getTestPort() {
/* 20 */     return 80;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\coffee\keenan\network\config\DefaultConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */