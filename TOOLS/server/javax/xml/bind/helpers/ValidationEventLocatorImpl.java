/*     */ package javax.xml.bind.helpers;
/*     */ 
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.text.MessageFormat;
/*     */ import javax.xml.bind.ValidationEventLocator;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ValidationEventLocatorImpl
/*     */   implements ValidationEventLocator
/*     */ {
/*     */   public ValidationEventLocatorImpl() {}
/*     */   
/*     */   public ValidationEventLocatorImpl(Locator loc) {
/*  53 */     if (loc == null) {
/*  54 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "loc"));
/*     */     }
/*     */ 
/*     */     
/*  58 */     this.url = toURL(loc.getSystemId());
/*  59 */     this.columnNumber = loc.getColumnNumber();
/*  60 */     this.lineNumber = loc.getLineNumber();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValidationEventLocatorImpl(SAXParseException e) {
/*  76 */     if (e == null) {
/*  77 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "e"));
/*     */     }
/*     */ 
/*     */     
/*  81 */     this.url = toURL(e.getSystemId());
/*  82 */     this.columnNumber = e.getColumnNumber();
/*  83 */     this.lineNumber = e.getLineNumber();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValidationEventLocatorImpl(Node _node) {
/*  97 */     if (_node == null) {
/*  98 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "_node"));
/*     */     }
/*     */ 
/*     */     
/* 102 */     this.node = _node;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValidationEventLocatorImpl(Object _object) {
/* 116 */     if (_object == null) {
/* 117 */       throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "_object"));
/*     */     }
/*     */ 
/*     */     
/* 121 */     this.object = _object;
/*     */   }
/*     */ 
/*     */   
/*     */   private static URL toURL(String systemId) {
/*     */     try {
/* 127 */       return new URL(systemId);
/* 128 */     } catch (MalformedURLException e) {
/*     */       
/* 130 */       return null;
/*     */     } 
/*     */   }
/*     */   
/* 134 */   private URL url = null;
/* 135 */   private int offset = -1;
/* 136 */   private int lineNumber = -1;
/* 137 */   private int columnNumber = -1;
/* 138 */   private Object object = null;
/* 139 */   private Node node = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getURL() {
/* 146 */     return this.url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setURL(URL _url) {
/* 155 */     this.url = _url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOffset() {
/* 162 */     return this.offset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOffset(int _offset) {
/* 171 */     this.offset = _offset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLineNumber() {
/* 178 */     return this.lineNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLineNumber(int _lineNumber) {
/* 187 */     this.lineNumber = _lineNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumnNumber() {
/* 194 */     return this.columnNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColumnNumber(int _columnNumber) {
/* 203 */     this.columnNumber = _columnNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getObject() {
/* 210 */     return this.object;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setObject(Object _object) {
/* 219 */     this.object = _object;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getNode() {
/* 226 */     return this.node;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNode(Node _node) {
/* 235 */     this.node = _node;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 245 */     return MessageFormat.format("[node={0},object={1},url={2},line={3},col={4},offset={5}]", new Object[] { getNode(), getObject(), getURL(), String.valueOf(getLineNumber()), String.valueOf(getColumnNumber()), String.valueOf(getOffset()) });
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\helpers\ValidationEventLocatorImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */