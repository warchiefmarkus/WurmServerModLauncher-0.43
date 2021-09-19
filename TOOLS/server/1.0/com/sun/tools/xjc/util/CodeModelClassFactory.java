/*    */ package 1.0.com.sun.tools.xjc.util;
/*    */ 
/*    */ import com.sun.codemodel.JClassAlreadyExistsException;
/*    */ import com.sun.codemodel.JClassContainer;
/*    */ import com.sun.codemodel.JDefinedClass;
/*    */ import com.sun.tools.xjc.ErrorReceiver;
/*    */ import com.sun.tools.xjc.util.Messages;
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.SAXParseException;
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
/*    */ public final class CodeModelClassFactory
/*    */ {
/*    */   private ErrorReceiver errorReceiver;
/* 33 */   private int ticketMaster = 0;
/*    */ 
/*    */   
/*    */   public CodeModelClassFactory(ErrorReceiver _errorReceiver) {
/* 37 */     this.errorReceiver = _errorReceiver;
/*    */   }
/*    */   
/*    */   public JDefinedClass createClass(JClassContainer parent, String name, Locator source) {
/* 41 */     return createClass(parent, 1, name, source);
/*    */   }
/*    */   public JDefinedClass createClass(JClassContainer parent, int mod, String name, Locator source) {
/* 44 */     return createClass(parent, mod, name, source, false);
/*    */   }
/*    */   
/*    */   public JDefinedClass createInterface(JClassContainer parent, String name, Locator source) {
/* 48 */     return createInterface(parent, 1, name, source);
/*    */   }
/*    */   public JDefinedClass createInterface(JClassContainer parent, int mod, String name, Locator source) {
/* 51 */     return createClass(parent, mod, name, source, true);
/*    */   }
/*    */ 
/*    */   
/*    */   private JDefinedClass createClass(JClassContainer parent, int mod, String name, Locator source, boolean isInterface) {
/*    */     try {
/* 57 */       JDefinedClass r = parent._class(mod, name, isInterface);
/*    */ 
/*    */       
/* 60 */       r.metadata = source;
/*    */       
/* 62 */       return r;
/* 63 */     } catch (JClassAlreadyExistsException e) {
/*    */       
/* 65 */       JDefinedClass cls = e.getExistingClass();
/*    */ 
/*    */       
/* 68 */       this.errorReceiver.error(new SAXParseException(Messages.format("CodeModelClassFactory.ClassNameCollision", cls.fullName()), (Locator)cls.metadata));
/*    */ 
/*    */       
/* 71 */       this.errorReceiver.error(new SAXParseException(Messages.format("CodeModelClassFactory.ClassNameCollision.Source", name), source));
/*    */ 
/*    */ 
/*    */       
/* 75 */       if (!name.equals(cls.name()))
/*    */       {
/* 77 */         this.errorReceiver.error(new SAXParseException(Messages.format("CodeModelClassFactory.CaseSensitivityCollision", name, cls.name()), null));
/*    */       }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/*    */       try {
/* 86 */         return parent.owner()._class("$$$garbage$$$" + this.ticketMaster++);
/* 87 */       } catch (JClassAlreadyExistsException ee) {
/* 88 */         return ee.getExistingClass();
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void setErrorHandler(ErrorReceiver errorReceiver) {
/* 94 */     this.errorReceiver = errorReceiver;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xj\\util\CodeModelClassFactory.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */