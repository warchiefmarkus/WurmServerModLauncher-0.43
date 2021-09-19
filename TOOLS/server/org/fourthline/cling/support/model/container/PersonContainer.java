/*    */ package org.fourthline.cling.support.model.container;
/*    */ 
/*    */ import org.fourthline.cling.support.model.DIDLObject;
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
/*    */ public class PersonContainer
/*    */   extends Container
/*    */ {
/* 25 */   public static final DIDLObject.Class CLASS = new DIDLObject.Class("object.container.person");
/*    */   
/*    */   public PersonContainer() {
/* 28 */     setClazz(CLASS);
/*    */   }
/*    */   
/*    */   public PersonContainer(Container other) {
/* 32 */     super(other);
/*    */   }
/*    */   
/*    */   public PersonContainer(String id, Container parent, String title, String creator, Integer childCount) {
/* 36 */     this(id, parent.getId(), title, creator, childCount);
/*    */   }
/*    */   
/*    */   public PersonContainer(String id, String parentID, String title, String creator, Integer childCount) {
/* 40 */     super(id, parentID, title, creator, CLASS, childCount);
/*    */   }
/*    */   
/*    */   public String getLanguage() {
/* 44 */     return (String)getFirstPropertyValue(DIDLObject.Property.DC.LANGUAGE.class);
/*    */   }
/*    */   
/*    */   public PersonContainer setLanguage(String language) {
/* 48 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.DC.LANGUAGE(language));
/* 49 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\container\PersonContainer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */