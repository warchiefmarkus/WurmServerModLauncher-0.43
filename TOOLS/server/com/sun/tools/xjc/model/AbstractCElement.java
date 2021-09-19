/*    */ package com.sun.tools.xjc.model;
/*    */ 
/*    */ import com.sun.xml.xsom.XSComponent;
/*    */ import javax.xml.bind.annotation.XmlTransient;
/*    */ import org.xml.sax.Locator;
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
/*    */ abstract class AbstractCElement
/*    */   extends AbstractCTypeInfoImpl
/*    */   implements CElement
/*    */ {
/*    */   @XmlTransient
/*    */   private final Locator locator;
/*    */   private boolean isAbstract;
/*    */   
/*    */   protected AbstractCElement(Model model, XSComponent source, Locator locator, CCustomizations customizations) {
/* 61 */     super(model, source, customizations);
/* 62 */     this.locator = locator;
/*    */   }
/*    */   
/*    */   public Locator getLocator() {
/* 66 */     return this.locator;
/*    */   }
/*    */   
/*    */   public boolean isAbstract() {
/* 70 */     return this.isAbstract;
/*    */   }
/*    */   
/*    */   public void setAbstract() {
/* 74 */     this.isAbstract = true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\AbstractCElement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */