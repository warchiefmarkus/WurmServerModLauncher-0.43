/*     */ package org.fourthline.cling.support.model.dlna;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.seamless.util.Exceptions;
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
/*     */ public abstract class DLNAAttribute<T>
/*     */ {
/*  37 */   private static final Logger log = Logger.getLogger(DLNAAttribute.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   private T value;
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Type
/*     */   {
/*  47 */     DLNA_ORG_PN("DLNA.ORG_PN", new Class[] { DLNAProfileAttribute.class }),
/*  48 */     DLNA_ORG_OP("DLNA.ORG_OP", new Class[] { DLNAOperationsAttribute.class }),
/*  49 */     DLNA_ORG_PS("DLNA.ORG_PS", new Class[] { DLNAPlaySpeedAttribute.class }),
/*  50 */     DLNA_ORG_CI("DLNA.ORG_CI", new Class[] { DLNAConversionIndicatorAttribute.class }),
/*  51 */     DLNA_ORG_FLAGS("DLNA.ORG_FLAGS", new Class[] { DLNAFlagsAttribute.class });
/*     */     
/*  53 */     private static Map<String, Type> byName = new HashMap<String, Type>()
/*     */       {
/*     */       
/*     */       };
/*     */     private String attributeName;
/*     */     private Class<? extends DLNAAttribute>[] attributeTypes;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     @SafeVarargs
/*     */     Type(String attributeName, Class<? extends DLNAAttribute>... attributeClass) {
/*  66 */       this.attributeName = attributeName;
/*  67 */       this.attributeTypes = attributeClass;
/*     */     }
/*     */     
/*     */     public String getAttributeName() {
/*  71 */       return this.attributeName;
/*     */     }
/*     */     
/*     */     public Class<? extends DLNAAttribute>[] getAttributeTypes() {
/*  75 */       return this.attributeTypes;
/*     */     }
/*     */     
/*     */     public static Type valueOfAttributeName(String attributeName) {
/*  79 */       if (attributeName == null) {
/*  80 */         return null;
/*     */       }
/*  82 */       return byName.get(attributeName.toUpperCase(Locale.ROOT));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(T value) {
/*  89 */     this.value = value;
/*     */   }
/*     */   
/*     */   public T getValue() {
/*  93 */     return this.value;
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
/*     */   public abstract void setString(String paramString1, String paramString2) throws InvalidDLNAProtocolAttributeException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getString();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DLNAAttribute newInstance(Type type, String attributeValue, String contentFormat) {
/* 125 */     DLNAAttribute attr = null;
/* 126 */     for (int i = 0; i < (type.getAttributeTypes()).length && attr == null; i++) {
/* 127 */       Class<? extends DLNAAttribute> attributeClass = type.getAttributeTypes()[i];
/*     */       try {
/* 129 */         log.finest("Trying to parse DLNA '" + type + "' with class: " + attributeClass.getSimpleName());
/* 130 */         attr = attributeClass.newInstance();
/* 131 */         if (attributeValue != null) {
/* 132 */           attr.setString(attributeValue, contentFormat);
/*     */         }
/* 134 */       } catch (InvalidDLNAProtocolAttributeException ex) {
/* 135 */         log.finest("Invalid DLNA attribute value for tested type: " + attributeClass.getSimpleName() + " - " + ex.getMessage());
/* 136 */         attr = null;
/* 137 */       } catch (Exception ex) {
/* 138 */         log.severe("Error instantiating DLNA attribute of type '" + type + "' with value: " + attributeValue);
/* 139 */         log.log(Level.SEVERE, "Exception root cause: ", Exceptions.unwrap(ex));
/*     */       } 
/*     */     } 
/* 142 */     return attr;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 147 */     return "(" + getClass().getSimpleName() + ") '" + getValue() + "'";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\DLNAAttribute.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */