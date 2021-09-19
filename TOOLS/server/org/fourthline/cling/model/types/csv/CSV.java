/*    */ package org.fourthline.cling.model.types.csv;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.fourthline.cling.model.ModelUtil;
/*    */ import org.fourthline.cling.model.types.Datatype;
/*    */ import org.fourthline.cling.model.types.InvalidValueException;
/*    */ import org.seamless.util.Reflections;
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
/*    */ public abstract class CSV<T>
/*    */   extends ArrayList<T>
/*    */ {
/*    */   protected final Datatype.Builtin datatype;
/*    */   
/*    */   public CSV() {
/* 47 */     this.datatype = getBuiltinDatatype();
/*    */   }
/*    */   
/*    */   public CSV(String s) throws InvalidValueException {
/* 51 */     this.datatype = getBuiltinDatatype();
/* 52 */     addAll(parseString(s));
/*    */   }
/*    */   
/*    */   protected List parseString(String s) throws InvalidValueException {
/* 56 */     String[] strings = ModelUtil.fromCommaSeparatedList(s);
/* 57 */     List<Object> values = new ArrayList();
/* 58 */     for (String string : strings) {
/* 59 */       values.add(this.datatype.getDatatype().valueOf(string));
/*    */     }
/* 61 */     return values;
/*    */   }
/*    */   
/*    */   protected Datatype.Builtin getBuiltinDatatype() throws InvalidValueException {
/* 65 */     Class csvType = Reflections.getTypeArguments(ArrayList.class, getClass()).get(0);
/* 66 */     Datatype.Default defaultType = Datatype.Default.getByJavaType(csvType);
/* 67 */     if (defaultType == null) {
/* 68 */       throw new InvalidValueException("No built-in UPnP datatype for Java type of CSV: " + csvType);
/*    */     }
/* 70 */     return defaultType.getBuiltinType();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 75 */     List<String> stringValues = new ArrayList<>();
/* 76 */     for (T t : this) {
/* 77 */       stringValues.add(this.datatype.getDatatype().getString(t));
/*    */     }
/* 79 */     return ModelUtil.toCommaSeparatedList(stringValues.toArray(new Object[stringValues.size()]));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\csv\CSV.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */