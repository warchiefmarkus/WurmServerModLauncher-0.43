/*     */ package com.sun.tools.xjc.generator.bean.field;
/*     */ 
/*     */ import com.sun.codemodel.JAssignmentTarget;
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.tools.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.xjc.generator.bean.MethodWriter;
/*     */ import com.sun.tools.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.xjc.outline.FieldAccessor;
/*     */ import com.sun.xml.bind.api.impl.NameConverter;
/*     */ import java.util.ArrayList;
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
/*     */ public class UntypedListField
/*     */   extends AbstractListField
/*     */ {
/*     */   private final JClass coreList;
/*     */   private JMethod $get;
/*     */   
/*     */   protected UntypedListField(ClassOutlineImpl context, CPropertyInfo prop, JClass coreList) {
/* 105 */     super(context, prop, !coreList.fullName().equals("java.util.ArrayList"));
/* 106 */     this.coreList = coreList.narrow(this.exposedType.boxify());
/* 107 */     generate();
/*     */   }
/*     */   
/*     */   protected final JClass getCoreListType() {
/* 111 */     return this.coreList;
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateAccessors() {
/* 116 */     MethodWriter writer = this.outline.createMethodWriter();
/* 117 */     Accessor acc = create(JExpr._this());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     this.$get = writer.declareMethod((JType)this.listT, "get" + this.prop.getName(true));
/* 124 */     writer.javadoc().append(this.prop.javadoc);
/* 125 */     JBlock block = this.$get.body();
/* 126 */     fixNullRef(block);
/* 127 */     block._return(acc.ref(true));
/*     */     
/* 129 */     String pname = NameConverter.standard.toVariableName(this.prop.getName(true));
/* 130 */     writer.javadoc().append("Gets the value of the " + pname + " property.\n\n" + "<p>\n" + "This accessor method returns a reference to the live list,\n" + "not a snapshot. Therefore any modification you make to the\n" + "returned list will be present inside the JAXB object.\n" + "This is why there is not a <CODE>set</CODE> method for the " + pname + " property.\n" + "\n" + "<p>\n" + "For example, to add a new item, do as follows:\n" + "<pre>\n" + "   get" + this.prop.getName(true) + "().add(newItem);\n" + "</pre>\n" + "\n\n");
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
/* 146 */     writer.javadoc().append("<p>\nObjects of the following type(s) are allowed in the list\n").append(listPossibleTypes(this.prop));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Accessor create(JExpression targetObject) {
/* 153 */     return new Accessor(targetObject);
/*     */   }
/*     */   
/*     */   class Accessor extends AbstractListField.Accessor {
/*     */     protected Accessor(JExpression $target) {
/* 158 */       super($target);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void toRawValue(JBlock block, JVar $var) {
/* 165 */       block.assign((JAssignmentTarget)$var, (JExpression)JExpr._new(UntypedListField.this.codeModel.ref(ArrayList.class).narrow(UntypedListField.this.exposedType.boxify())).arg((JExpression)this.$target.invoke(UntypedListField.this.$get)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void fromRawValue(JBlock block, String uniqueName, JExpression $var) {
/* 173 */       JVar $list = block.decl((JType)UntypedListField.this.listT, uniqueName + 'l', (JExpression)this.$target.invoke(UntypedListField.this.$get));
/* 174 */       block.invoke((JExpression)$list, "addAll").arg($var);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\field\UntypedListField.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */