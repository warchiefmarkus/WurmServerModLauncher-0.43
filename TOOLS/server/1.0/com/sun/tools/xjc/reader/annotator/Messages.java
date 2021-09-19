/*    */ package 1.0.com.sun.tools.xjc.reader.annotator;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ class Messages {
/*    */   static final String ENUM_FACET_UNSUPPORTED = "DatatypeSimplifier.EnumFacetUnsupported";
/*    */   static final String PATTERN_FACET_UNSUPPORTED = "DatatypeSimplifier.PatternFacetUnsupported";
/*    */   static final String ERR_MULTIPLE_SUPERCLASS_BODY = "Normalizer.MultipleSuperClassBody";
/*    */   static final String ERR_MULTIPLE_INHERITANCE = "Normalizer.MultipleInheritance";
/*    */   public static final String ERR_BAD_SUPERCLASS_USE = "Normalizer.BadSuperClassUse";
/*    */   public static final String ERR_BAD_ITEM_USE = "Normalizer.BadItemUse";
/*    */   public static final String ERR_MISSING_SUPERCLASS_BODY = "Normalizer.MissingSuperClassBody";
/*    */   public static final String ERR_BAD_SUPERCLASS_MULTIPLICITY = "Normalizer.BadSuperClassMultiplicity";
/*    */   
/*    */   static String format(String property) {
/* 17 */     return format(property, null);
/*    */   }
/*    */   public static final String ERR_BAD_SUPERCLASS_BODY_MULTIPLICITY = "Normalizer.BadSuperClassBodyMultiplicity"; public static final String ERR_BAD_INTERFACE_CLASS_MULTIPLICITY = "Normalizer.BadInterfaceToClassMultiplicity"; public static final String ERR_CONFLICT_BETWEEN_USERTYPE_AND_ACTUALTYPE = "Normalizer.ConflictBetweenUserTypeAndActualType"; public static final String ERR_DELEGATION_MULTIPLICITY_MUST_BE_1 = "Normalizer.DelegationMultiplicityMustBe1"; public static final String ERR_DELEGATION_MUST_BE_INTERFACE = "Normalizer.DelegationMustBeInterface"; public static final String ERR_EMPTY_PROPERTY = "Normalizer.EmptyProperty"; static final String ERR_PROPERTYNAME_COLLISION = "FieldCollisionChecker.PropertyNameCollision"; static final String ERR_PROPERTYNAME_COLLISION_SOURCE = "FieldCollisionChecker.PropertyNameCollision.Source"; static final String ERR_RESERVEDWORD_COLLISION = "FieldCollisionChecker.ReservedWordCollision";
/*    */   static String format(String property, Object arg1) {
/* 21 */     return format(property, new Object[] { arg1 });
/*    */   }
/*    */   
/*    */   static String format(String property, Object arg1, Object arg2) {
/* 25 */     return format(property, new Object[] { arg1, arg2 });
/*    */   }
/*    */   
/*    */   static String format(String property, Object arg1, Object arg2, Object arg3) {
/* 29 */     return format(property, new Object[] { arg1, arg2, arg3 });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static String format(String property, Object[] args) {
/* 36 */     String text = ResourceBundle.getBundle(com.sun.tools.xjc.reader.annotator.Messages.class.getName()).getString(property);
/* 37 */     return MessageFormat.format(text, args);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\annotator\Messages.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */