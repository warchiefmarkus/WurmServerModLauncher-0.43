/*     */ package 1.0.com.sun.xml.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSContentType;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSSchema;
/*     */ import com.sun.xml.xsom.XSSchemaSet;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.impl.EmptyImpl;
/*     */ import com.sun.xml.xsom.impl.SchemaImpl;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ import org.xml.sax.Locator;
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
/*     */ public class SchemaSetImpl
/*     */   implements XSSchemaSet
/*     */ {
/*  47 */   private final Map schemas = new HashMap();
/*  48 */   private final Vector schemas2 = new Vector();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SchemaImpl createSchema(String targetNamespace, Locator location) {
/*  55 */     SchemaImpl obj = (SchemaImpl)this.schemas.get(targetNamespace);
/*  56 */     if (obj == null) {
/*  57 */       obj = new SchemaImpl(this, location, targetNamespace);
/*  58 */       this.schemas.put(targetNamespace, obj);
/*  59 */       this.schemas2.add(obj);
/*     */     } 
/*  61 */     return obj;
/*     */   }
/*     */   
/*     */   public int getSchemaSize() {
/*  65 */     return this.schemas.size();
/*     */   }
/*     */   public XSSchema getSchema(String targetNamespace) {
/*  68 */     return (XSSchema)this.schemas.get(targetNamespace);
/*     */   }
/*     */   public XSSchema getSchema(int idx) {
/*  71 */     return this.schemas2.get(idx);
/*     */   }
/*     */   public Iterator iterateSchema() {
/*  74 */     return this.schemas2.iterator();
/*     */   }
/*     */   
/*     */   public XSSimpleType getSimpleType(String ns, String localName) {
/*  78 */     XSSchema schema = getSchema(ns);
/*  79 */     if (schema == null) return null;
/*     */     
/*  81 */     return schema.getSimpleType(localName);
/*     */   }
/*     */   
/*     */   public XSElementDecl getElementDecl(String ns, String localName) {
/*  85 */     XSSchema schema = getSchema(ns);
/*  86 */     if (schema == null) return null;
/*     */     
/*  88 */     return schema.getElementDecl(localName);
/*     */   }
/*     */   
/*     */   public XSAttributeDecl getAttributeDecl(String ns, String localName) {
/*  92 */     XSSchema schema = getSchema(ns);
/*  93 */     if (schema == null) return null;
/*     */     
/*  95 */     return schema.getAttributeDecl(localName);
/*     */   }
/*     */   
/*     */   public XSModelGroupDecl getModelGroupDecl(String ns, String localName) {
/*  99 */     XSSchema schema = getSchema(ns);
/* 100 */     if (schema == null) return null;
/*     */     
/* 102 */     return schema.getModelGroupDecl(localName);
/*     */   }
/*     */   
/*     */   public XSAttGroupDecl getAttGroupDecl(String ns, String localName) {
/* 106 */     XSSchema schema = getSchema(ns);
/* 107 */     if (schema == null) return null;
/*     */     
/* 109 */     return schema.getAttGroupDecl(localName);
/*     */   }
/*     */   
/*     */   public XSComplexType getComplexType(String ns, String localName) {
/* 113 */     XSSchema schema = getSchema(ns);
/* 114 */     if (schema == null) return null;
/*     */     
/* 116 */     return schema.getComplexType(localName);
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
/*     */   public Iterator iterateElementDecls() {
/* 156 */     return (Iterator)new Object(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator iterateTypes() {
/* 164 */     return (Iterator)new Object(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator iterateAttributeDecls() {
/* 172 */     return (Iterator)new Object(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator iterateAttGroupDecls() {
/* 179 */     return (Iterator)new Object(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator iterateModelGroupDecls() {
/* 186 */     return (Iterator)new Object(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator iterateSimpleTypes() {
/* 193 */     return (Iterator)new Object(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator iterateComplexTypes() {
/* 200 */     return (Iterator)new Object(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator iterateNotations() {
/* 207 */     return (Iterator)new Object(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 217 */   public final EmptyImpl empty = new EmptyImpl(); public XSContentType getEmpty() {
/* 218 */     return (XSContentType)this.empty;
/*     */   } public XSSimpleType getAnySimpleType() {
/* 220 */     return (XSSimpleType)this.anySimpleType;
/* 221 */   } public final AnySimpleType anySimpleType = new AnySimpleType(this);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XSComplexType getAnyType() {
/* 264 */     return (XSComplexType)this.anyType;
/* 265 */   } public final AnyType anyType = new AnyType(this);
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
/* 313 */   private static final Iterator emptyIterator = (Iterator)new Object();
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\SchemaSetImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */