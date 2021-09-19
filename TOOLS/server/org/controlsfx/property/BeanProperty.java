/*     */ package org.controlsfx.property;
/*     */ 
/*     */ import impl.org.controlsfx.i18n.Localization;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Optional;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.scene.control.Alert;
/*     */ import org.controlsfx.control.PropertySheet;
/*     */ import org.controlsfx.property.editor.PropertyEditor;
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
/*     */ public class BeanProperty
/*     */   implements PropertySheet.Item
/*     */ {
/*     */   public static final String CATEGORY_LABEL_KEY = "propertysheet.item.category.label";
/*     */   private final Object bean;
/*     */   private final PropertyDescriptor beanPropertyDescriptor;
/*     */   private final Method readMethod;
/*     */   private boolean editable = true;
/*  78 */   private Optional<ObservableValue<? extends Object>> observableValue = Optional.empty();
/*     */   
/*     */   public BeanProperty(Object bean, PropertyDescriptor propertyDescriptor) {
/*  81 */     this.bean = bean;
/*  82 */     this.beanPropertyDescriptor = propertyDescriptor;
/*  83 */     this.readMethod = propertyDescriptor.getReadMethod();
/*  84 */     if (this.beanPropertyDescriptor.getWriteMethod() == null) {
/*  85 */       setEditable(false);
/*     */     }
/*     */     
/*  88 */     findObservableValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  93 */     return this.beanPropertyDescriptor.getDisplayName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/*  98 */     return this.beanPropertyDescriptor.getShortDescription();
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getType() {
/* 103 */     return this.beanPropertyDescriptor.getPropertyType();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getValue() {
/*     */     try {
/* 109 */       return this.readMethod.invoke(this.bean, new Object[0]);
/* 110 */     } catch (IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException e) {
/* 111 */       e.printStackTrace();
/* 112 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(Object value) {
/* 118 */     Method writeMethod = this.beanPropertyDescriptor.getWriteMethod();
/* 119 */     if (writeMethod != null) {
/*     */       try {
/* 121 */         writeMethod.invoke(this.bean, new Object[] { value });
/* 122 */       } catch (IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException e) {
/* 123 */         e.printStackTrace();
/* 124 */       } catch (Throwable e) {
/* 125 */         if (e instanceof java.beans.PropertyVetoException) {
/* 126 */           Alert alert = new Alert(Alert.AlertType.ERROR);
/* 127 */           alert.setTitle(Localization.localize(Localization.asKey("bean.property.change.error.title")));
/* 128 */           alert.setHeaderText(Localization.localize(Localization.asKey("bean.property.change.error.masthead")));
/* 129 */           alert.setContentText(e.getLocalizedMessage());
/* 130 */           alert.showAndWait();
/*     */         } else {
/* 132 */           throw e;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCategory() {
/* 140 */     String category = (String)this.beanPropertyDescriptor.getValue("propertysheet.item.category.label");
/*     */ 
/*     */     
/* 143 */     if (category == null) {
/* 144 */       category = Localization.localize(Localization.asKey(this.beanPropertyDescriptor.isExpert() ? "bean.property.category.expert" : "bean.property.category.basic"));
/*     */     }
/*     */     
/* 147 */     return category;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getBean() {
/* 154 */     return this.bean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyDescriptor getPropertyDescriptor() {
/* 162 */     return this.beanPropertyDescriptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<Class<? extends PropertyEditor<?>>> getPropertyEditorClass() {
/* 169 */     if (this.beanPropertyDescriptor.getPropertyEditorClass() != null && PropertyEditor.class
/* 170 */       .isAssignableFrom(this.beanPropertyDescriptor.getPropertyEditorClass()))
/*     */     {
/* 172 */       return (Optional)Optional.of(this.beanPropertyDescriptor.getPropertyEditorClass());
/*     */     }
/*     */     
/* 175 */     return super.getPropertyEditorClass();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEditable() {
/* 180 */     return this.editable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEditable(boolean editable) {
/* 187 */     this.editable = editable;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<ObservableValue<? extends Object>> getObservableValue() {
/* 192 */     return this.observableValue;
/*     */   }
/*     */   
/*     */   private void findObservableValue() {
/*     */     try {
/* 197 */       String propName = this.beanPropertyDescriptor.getName() + "Property";
/* 198 */       Method m = getBean().getClass().getMethod(propName, new Class[0]);
/* 199 */       Object val = m.invoke(getBean(), new Object[0]);
/* 200 */       if (val != null && val instanceof ObservableValue) {
/* 201 */         this.observableValue = Optional.of((ObservableValue<? extends Object>)val);
/*     */       }
/* 203 */     } catch (NoSuchMethodException|SecurityException|IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException noSuchMethodException) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\property\BeanProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */