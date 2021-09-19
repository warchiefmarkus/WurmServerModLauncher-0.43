/*    */ package impl.org.controlsfx.i18n;
/*    */ 
/*    */ import javafx.beans.property.SimpleStringProperty;
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
/*    */ public class SimpleLocalizedStringProperty
/*    */   extends SimpleStringProperty
/*    */ {
/*    */   public SimpleLocalizedStringProperty() {}
/*    */   
/*    */   public SimpleLocalizedStringProperty(String initialValue) {
/* 43 */     super(initialValue);
/*    */   }
/*    */   
/*    */   public SimpleLocalizedStringProperty(Object bean, String name) {
/* 47 */     super(bean, name);
/*    */   }
/*    */ 
/*    */   
/*    */   public SimpleLocalizedStringProperty(Object bean, String name, String initialValue) {
/* 52 */     super(bean, name, initialValue);
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 56 */     return Localization.localize(super.getValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\i18n\SimpleLocalizedStringProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */