/*    */ package javax.servlet.http;
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
/*    */ public class HttpSessionEvent
/*    */   extends EventObject
/*    */ {
/*    */   public HttpSessionEvent(HttpSession source) {
/* 73 */     super(source);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpSession getSession() {
/* 80 */     return (HttpSession)getSource();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\http\HttpSessionEvent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */