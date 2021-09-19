/*    */ package org.controlsfx.property;
/*    */ 
/*    */ import java.beans.BeanInfo;
/*    */ import java.beans.IntrospectionException;
/*    */ import java.beans.Introspector;
/*    */ import java.beans.PropertyDescriptor;
/*    */ import java.util.function.Predicate;
/*    */ import javafx.collections.FXCollections;
/*    */ import javafx.collections.ObservableList;
/*    */ import org.controlsfx.control.PropertySheet;
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
/*    */ public final class BeanPropertyUtils
/*    */ {
/*    */   public static ObservableList<PropertySheet.Item> getProperties(Object bean) {
/* 65 */     return getProperties(bean, p -> true);
/*    */   }
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
/*    */   public static ObservableList<PropertySheet.Item> getProperties(Object bean, Predicate<PropertyDescriptor> test) {
/* 81 */     ObservableList<PropertySheet.Item> list = FXCollections.observableArrayList();
/*    */     try {
/* 83 */       BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass(), Object.class);
/* 84 */       for (PropertyDescriptor p : beanInfo.getPropertyDescriptors()) {
/* 85 */         if (test.test(p)) {
/* 86 */           list.add(new BeanProperty(bean, p));
/*    */         }
/*    */       } 
/* 89 */     } catch (IntrospectionException e) {
/* 90 */       e.printStackTrace();
/*    */     } 
/*    */     
/* 93 */     return list;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\property\BeanPropertyUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */