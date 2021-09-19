/*    */ package 1.0.com.sun.tools.xjc.grammar.xducer;
/*    */ 
/*    */ import com.sun.codemodel.JClass;
/*    */ import com.sun.codemodel.JCodeModel;
/*    */ import com.sun.codemodel.JExpr;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.msv.datatype.xsd.datetime.IDateTimeValueType;
/*    */ import com.sun.msv.grammar.ValueExp;
/*    */ import com.sun.tools.xjc.grammar.xducer.DeserializerContext;
/*    */ import com.sun.tools.xjc.grammar.xducer.SerializerContext;
/*    */ import com.sun.tools.xjc.grammar.xducer.TransducerImpl;
/*    */ import com.sun.xml.bind.util.CalendarConv;
/*    */ import java.util.Calendar;
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
/*    */ public class DateTransducer
/*    */   extends TransducerImpl
/*    */ {
/*    */   private final JCodeModel codeModel;
/*    */   private final JClass datatypeImpl;
/*    */   
/*    */   public DateTransducer(JCodeModel cm, JClass datatypeImpl) {
/* 33 */     this.codeModel = cm;
/* 34 */     this.datatypeImpl = datatypeImpl;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JExpression generateConstant(ValueExp exp) {
/* 42 */     Calendar data = ((IDateTimeValueType)exp.value).toCalendar();
/*    */     
/* 44 */     String str = CalendarConv.formatter.format(data.getTime());
/*    */     
/* 46 */     return (JExpression)this.codeModel.ref(CalendarConv.class).staticInvoke("createCalendar").arg(JExpr.lit(str));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public JExpression generateDeserializer(JExpression literal, DeserializerContext context) {
/* 52 */     return (JExpression)JExpr.cast(getReturnType(), (JExpression)this.datatypeImpl.staticRef("theInstance").invoke("createJavaObject").arg(literal).arg(JExpr._null()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public JExpression generateSerializer(JExpression value, SerializerContext context) {
/* 58 */     return (JExpression)this.datatypeImpl.staticRef("theInstance").invoke("serializeJavaObject").arg(value).arg(JExpr._null());
/*    */   }
/*    */ 
/*    */   
/*    */   public JType getReturnType() {
/* 63 */     return (JType)this.codeModel.ref(Calendar.class);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\xducer\DateTransducer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */