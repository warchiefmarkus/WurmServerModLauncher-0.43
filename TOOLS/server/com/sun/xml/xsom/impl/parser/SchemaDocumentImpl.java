/*    */ package com.sun.xml.xsom.impl.parser;
/*    */ 
/*    */ import com.sun.xml.xsom.XSSchema;
/*    */ import com.sun.xml.xsom.impl.SchemaImpl;
/*    */ import com.sun.xml.xsom.parser.SchemaDocument;
/*    */ import java.util.Collections;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
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
/*    */ public final class SchemaDocumentImpl
/*    */   implements SchemaDocument
/*    */ {
/*    */   private final SchemaImpl schema;
/*    */   private final String schemaDocumentURI;
/* 27 */   final Set<SchemaDocumentImpl> references = new HashSet<SchemaDocumentImpl>();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   final Set<SchemaDocumentImpl> referers = new HashSet<SchemaDocumentImpl>();
/*    */   
/*    */   protected SchemaDocumentImpl(SchemaImpl schema, String _schemaDocumentURI) {
/* 35 */     this.schema = schema;
/* 36 */     this.schemaDocumentURI = _schemaDocumentURI;
/*    */   }
/*    */   
/*    */   public String getSystemId() {
/* 40 */     return this.schemaDocumentURI;
/*    */   }
/*    */   
/*    */   public String getTargetNamespace() {
/* 44 */     return this.schema.getTargetNamespace();
/*    */   }
/*    */   
/*    */   public SchemaImpl getSchema() {
/* 48 */     return this.schema;
/*    */   }
/*    */   
/*    */   public Set<SchemaDocument> getReferencedDocuments() {
/* 52 */     return Collections.unmodifiableSet((Set)this.references);
/*    */   }
/*    */   
/*    */   public Set<SchemaDocument> getIncludedDocuments() {
/* 56 */     return getImportedDocuments(getTargetNamespace());
/*    */   }
/*    */   
/*    */   public Set<SchemaDocument> getImportedDocuments(String targetNamespace) {
/* 60 */     if (targetNamespace == null)
/* 61 */       throw new IllegalArgumentException(); 
/* 62 */     Set<SchemaDocument> r = new HashSet<SchemaDocument>();
/* 63 */     for (SchemaDocumentImpl doc : this.references) {
/* 64 */       if (doc.getTargetNamespace().equals(targetNamespace))
/* 65 */         r.add(doc); 
/*    */     } 
/* 67 */     return Collections.unmodifiableSet(r);
/*    */   }
/*    */   
/*    */   public boolean includes(SchemaDocument doc) {
/* 71 */     if (!this.references.contains(doc))
/* 72 */       return false; 
/* 73 */     return (doc.getSchema() == this.schema);
/*    */   }
/*    */   
/*    */   public boolean imports(SchemaDocument doc) {
/* 77 */     if (!this.references.contains(doc))
/* 78 */       return false; 
/* 79 */     return (doc.getSchema() != this.schema);
/*    */   }
/*    */   
/*    */   public Set<SchemaDocument> getReferers() {
/* 83 */     return Collections.unmodifiableSet((Set)this.referers);
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/* 87 */     SchemaDocumentImpl rhs = (SchemaDocumentImpl)o;
/*    */     
/* 89 */     if (this.schemaDocumentURI == null || rhs.schemaDocumentURI == null)
/* 90 */       return (this == rhs); 
/* 91 */     if (!this.schemaDocumentURI.equals(rhs.schemaDocumentURI))
/* 92 */       return false; 
/* 93 */     return (this.schema == rhs.schema);
/*    */   }
/*    */   public int hashCode() {
/* 96 */     if (this.schemaDocumentURI == null)
/* 97 */       return super.hashCode(); 
/* 98 */     return this.schemaDocumentURI.hashCode() ^ this.schema.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\SchemaDocumentImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */