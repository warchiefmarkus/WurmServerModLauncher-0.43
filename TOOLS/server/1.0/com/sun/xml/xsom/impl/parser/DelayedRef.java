/*    */ package 1.0.com.sun.xml.xsom.impl.parser;
/*    */ 
/*    */ import com.sun.xml.xsom.XSDeclaration;
/*    */ import com.sun.xml.xsom.XSSchemaSet;
/*    */ import com.sun.xml.xsom.impl.SchemaImpl;
/*    */ import com.sun.xml.xsom.impl.UName;
/*    */ import com.sun.xml.xsom.impl.parser.Messages;
/*    */ import com.sun.xml.xsom.impl.parser.Patch;
/*    */ import com.sun.xml.xsom.impl.parser.PatcherManager;
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.SAXException;
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
/*    */ public abstract class DelayedRef
/*    */   implements Patch
/*    */ {
/*    */   protected final XSSchemaSet schema;
/*    */   private PatcherManager manager;
/*    */   private UName name;
/*    */   private Locator source;
/*    */   private Object ref;
/*    */   
/*    */   DelayedRef(PatcherManager _manager, Locator _source, SchemaImpl _schema, UName _name) {
/* 71 */     this.ref = null; this.schema = (XSSchemaSet)_schema.getParent(); this.manager = _manager; this.name = _name; this.source = _source; if (this.name == null) throw new InternalError();  this.manager.addPatcher(this);
/*    */   } public void run() throws SAXException { if (this.ref == null)
/* 73 */       resolve();  this.manager = null; this.name = null; this.source = null; } protected final Object _get() { if (this.ref == null) throw new InternalError("unresolved reference"); 
/* 74 */     return this.ref; }
/*    */    protected abstract Object resolveReference(UName paramUName);
/*    */   protected abstract String getErrorProperty();
/*    */   private void resolve() throws SAXException {
/* 78 */     this.ref = resolveReference(this.name);
/* 79 */     if (this.ref == null) {
/* 80 */       this.manager.reportError(Messages.format(getErrorProperty(), this.name.getQualifiedName()), this.source);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void redefine(XSDeclaration d) {
/* 90 */     if (!d.getTargetNamespace().equals(this.name.getNamespaceURI()) || !d.getName().equals(this.name.getName())) {
/*    */       return;
/*    */     }
/*    */     
/* 94 */     this.ref = d;
/* 95 */     this.manager = null;
/* 96 */     this.name = null;
/* 97 */     this.source = null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\DelayedRef.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */