/*    */ package javax.servlet;
/*    */ 
/*    */ import java.util.EventObject;
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
/*    */ public class ServletRequestEvent
/*    */   extends EventObject
/*    */ {
/*    */   private final transient ServletRequest request;
/*    */   
/*    */   public ServletRequestEvent(ServletContext sc, ServletRequest request) {
/* 77 */     super(sc);
/* 78 */     this.request = request;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ServletRequest getServletRequest() {
/* 85 */     return this.request;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ServletContext getServletContext() {
/* 92 */     return (ServletContext)getSource();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\ServletRequestEvent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */