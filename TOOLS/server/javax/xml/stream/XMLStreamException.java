/*    */ package javax.xml.stream;
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
/*    */ public class XMLStreamException
/*    */   extends Exception
/*    */ {
/*    */   protected Throwable nested;
/*    */   protected Location location;
/*    */   
/*    */   public XMLStreamException() {}
/*    */   
/*    */   public XMLStreamException(String msg) {
/* 28 */     super(msg);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public XMLStreamException(Throwable th) {
/* 37 */     this.nested = th;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public XMLStreamException(String msg, Throwable th) {
/* 47 */     super(msg);
/* 48 */     this.nested = th;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public XMLStreamException(String msg, Location location, Throwable th) {
/* 59 */     super("ParseError at [row,col]:[" + location.getLineNumber() + "," + location.getColumnNumber() + "]\n" + "Message: " + msg);
/*    */ 
/*    */     
/* 62 */     this.nested = th;
/* 63 */     this.location = location;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public XMLStreamException(String msg, Location location) {
/* 74 */     super("ParseError at [row,col]:[" + location.getLineNumber() + "," + location.getColumnNumber() + "]\n" + "Message: " + msg);
/*    */ 
/*    */     
/* 77 */     this.location = location;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Throwable getNestedException() {
/* 87 */     return this.nested;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Location getLocation() {
/* 96 */     return this.location;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\stream\XMLStreamException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */