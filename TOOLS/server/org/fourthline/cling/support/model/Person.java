/*    */ package org.fourthline.cling.support.model;
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
/*    */ public class Person
/*    */ {
/*    */   private String name;
/*    */   
/*    */   public Person(String name) {
/* 26 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 30 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 35 */     if (this == o) return true; 
/* 36 */     if (o == null || getClass() != o.getClass()) return false;
/*    */     
/* 38 */     Person person = (Person)o;
/*    */     
/* 40 */     if (!this.name.equals(person.name)) return false;
/*    */     
/* 42 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 47 */     return this.name.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 52 */     return getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\Person.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */