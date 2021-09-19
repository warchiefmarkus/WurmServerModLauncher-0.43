/*     */ package com.sun.xml.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.bind.v2.model.annotation.AnnotationReader;
/*     */ import com.sun.xml.bind.v2.model.annotation.AnnotationSource;
/*     */ import com.sun.xml.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
/*     */ import java.lang.annotation.Annotation;
/*     */ import javax.activation.MimeType;
/*     */ import javax.activation.MimeTypeParseException;
/*     */ import javax.xml.bind.annotation.XmlMimeType;
/*     */ import javax.xml.bind.annotation.XmlSchemaType;
/*     */ import javax.xml.bind.annotation.XmlSchemaTypes;
/*     */ import javax.xml.namespace.QName;
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
/*     */ final class Util
/*     */ {
/*     */   static <T, C, F, M> QName calcSchemaType(AnnotationReader<T, C, F, M> reader, AnnotationSource primarySource, C enclosingClass, T individualType, Locatable src) {
/*  61 */     XmlSchemaType xst = (XmlSchemaType)primarySource.readAnnotation(XmlSchemaType.class);
/*  62 */     if (xst != null) {
/*  63 */       return new QName(xst.namespace(), xst.name());
/*     */     }
/*     */ 
/*     */     
/*  67 */     XmlSchemaTypes xsts = (XmlSchemaTypes)reader.getPackageAnnotation(XmlSchemaTypes.class, enclosingClass, src);
/*  68 */     XmlSchemaType[] values = null;
/*  69 */     if (xsts != null) {
/*  70 */       values = xsts.value();
/*     */     } else {
/*  72 */       xst = (XmlSchemaType)reader.getPackageAnnotation(XmlSchemaType.class, enclosingClass, src);
/*  73 */       if (xst != null) {
/*  74 */         values = new XmlSchemaType[1];
/*  75 */         values[0] = xst;
/*     */       } 
/*     */     } 
/*  78 */     if (values != null) {
/*  79 */       for (XmlSchemaType item : values) {
/*  80 */         if (reader.getClassValue((Annotation)item, "type").equals(individualType)) {
/*  81 */           return new QName(item.namespace(), item.name());
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*  86 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   static MimeType calcExpectedMediaType(AnnotationSource primarySource, ModelBuilder builder) {
/*  91 */     XmlMimeType xmt = (XmlMimeType)primarySource.readAnnotation(XmlMimeType.class);
/*  92 */     if (xmt == null) {
/*  93 */       return null;
/*     */     }
/*     */     try {
/*  96 */       return new MimeType(xmt.value());
/*  97 */     } catch (MimeTypeParseException e) {
/*  98 */       builder.reportError(new IllegalAnnotationException(Messages.ILLEGAL_MIME_TYPE.format(new Object[] { xmt.value(), e.getMessage() }, ), (Annotation)xmt));
/*     */ 
/*     */ 
/*     */       
/* 102 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\Util.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */