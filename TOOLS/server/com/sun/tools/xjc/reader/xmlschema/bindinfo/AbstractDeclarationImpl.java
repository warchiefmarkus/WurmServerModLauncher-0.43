/*    */ package com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import com.sun.codemodel.JCodeModel;
/*    */ import com.sun.tools.xjc.reader.Ring;
/*    */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.xml.bind.annotation.XmlLocation;
/*    */ import com.sun.xml.xsom.XSComponent;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
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
/*    */ abstract class AbstractDeclarationImpl
/*    */   implements BIDeclaration
/*    */ {
/*    */   @XmlLocation
/*    */   Locator loc;
/*    */   protected BindInfo parent;
/*    */   private boolean isAcknowledged;
/*    */   
/*    */   @Deprecated
/*    */   protected AbstractDeclarationImpl(Locator loc) {
/* 83 */     this.isAcknowledged = false; this.loc = loc; } protected AbstractDeclarationImpl() { this.isAcknowledged = false; }
/*    */   public Locator getLocation() { return this.loc; }
/* 85 */   public void setParent(BindInfo p) { this.parent = p; } public final boolean isAcknowledged() { return this.isAcknowledged; }
/*    */   protected final XSComponent getOwner() { return this.parent.getOwner(); } protected final BGMBuilder getBuilder() {
/*    */     return this.parent.getBuilder();
/*    */   } protected final JCodeModel getCodeModel() {
/*    */     return (JCodeModel)Ring.get(JCodeModel.class);
/*    */   } public void onSetOwner() {} public Collection<BIDeclaration> getChildren() {
/* 91 */     return Collections.emptyList();
/*    */   }
/*    */   
/*    */   public void markAsAcknowledged() {
/* 95 */     this.isAcknowledged = true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\bindinfo\AbstractDeclarationImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */