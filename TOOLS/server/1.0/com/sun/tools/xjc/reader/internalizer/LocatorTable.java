/*    */ package 1.0.com.sun.tools.xjc.reader.internalizer;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.w3c.dom.Element;
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.helpers.LocatorImpl;
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
/*    */ public final class LocatorTable
/*    */ {
/* 22 */   private final Map startLocations = new HashMap();
/*    */ 
/*    */   
/* 25 */   private final Map endLocations = new HashMap();
/*    */   
/*    */   public void storeStartLocation(Element e, Locator loc) {
/* 28 */     this.startLocations.put(e, new LocatorImpl(loc));
/*    */   }
/*    */   
/*    */   public void storeEndLocation(Element e, Locator loc) {
/* 32 */     this.endLocations.put(e, new LocatorImpl(loc));
/*    */   }
/*    */   
/*    */   public Locator getStartLocation(Element e) {
/* 36 */     return (Locator)this.startLocations.get(e);
/*    */   }
/*    */   
/*    */   public Locator getEndLocation(Element e) {
/* 40 */     return (Locator)this.endLocations.get(e);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\internalizer\LocatorTable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */