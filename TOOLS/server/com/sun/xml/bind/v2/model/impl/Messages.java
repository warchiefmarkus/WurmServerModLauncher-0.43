/*     */ package com.sun.xml.bind.v2.model.impl;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ResourceBundle;
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
/*     */ enum Messages
/*     */ {
/*     */   private static final ResourceBundle rb;
/*  47 */   ID_MUST_BE_STRING,
/*     */   
/*  49 */   MUTUALLY_EXCLUSIVE_ANNOTATIONS,
/*  50 */   DUPLICATE_ANNOTATIONS,
/*  51 */   NO_DEFAULT_CONSTRUCTOR,
/*  52 */   CANT_HANDLE_INTERFACE,
/*  53 */   CANT_HANDLE_INNER_CLASS,
/*  54 */   ANNOTATION_ON_WRONG_METHOD,
/*  55 */   GETTER_SETTER_INCOMPATIBLE_TYPE,
/*  56 */   DUPLICATE_ENTRY_IN_PROP_ORDER,
/*  57 */   DUPLICATE_PROPERTIES,
/*     */   
/*  59 */   XML_ELEMENT_MAPPING_ON_NON_IXMLELEMENT_METHOD,
/*  60 */   SCOPE_IS_NOT_COMPLEXTYPE,
/*  61 */   CONFLICTING_XML_ELEMENT_MAPPING,
/*     */   
/*  63 */   REFERENCE_TO_NON_ELEMENT,
/*     */   
/*  65 */   NON_EXISTENT_ELEMENT_MAPPING,
/*     */   
/*  67 */   TWO_ATTRIBUTE_WILDCARDS,
/*  68 */   SUPER_CLASS_HAS_WILDCARD,
/*  69 */   INVALID_ATTRIBUTE_WILDCARD_TYPE,
/*  70 */   PROPERTY_MISSING_FROM_ORDER,
/*  71 */   PROPERTY_ORDER_CONTAINS_UNUSED_ENTRY,
/*     */   
/*  73 */   INVALID_XML_ENUM_VALUE,
/*  74 */   FAILED_TO_INITIALE_DATATYPE_FACTORY,
/*  75 */   NO_IMAGE_WRITER,
/*     */   
/*  77 */   ILLEGAL_MIME_TYPE,
/*  78 */   ILLEGAL_ANNOTATION,
/*     */   
/*  80 */   MULTIPLE_VALUE_PROPERTY,
/*  81 */   ELEMENT_AND_VALUE_PROPERTY,
/*  82 */   CONFLICTING_XML_TYPE_MAPPING,
/*  83 */   XMLVALUE_IN_DERIVED_TYPE,
/*  84 */   SIMPLE_TYPE_IS_REQUIRED,
/*  85 */   PROPERTY_COLLISION,
/*  86 */   INVALID_IDREF,
/*  87 */   INVALID_XML_ELEMENT_REF,
/*  88 */   NO_XML_ELEMENT_DECL,
/*  89 */   XML_ELEMENT_WRAPPER_ON_NON_COLLECTION,
/*     */   
/*  91 */   ANNOTATION_NOT_ALLOWED,
/*  92 */   XMLLIST_NEEDS_SIMPLETYPE,
/*  93 */   XMLLIST_ON_SINGLE_PROPERTY,
/*  94 */   NO_FACTORY_METHOD,
/*  95 */   FACTORY_CLASS_NEEDS_FACTORY_METHOD,
/*     */   
/*  97 */   INCOMPATIBLE_API_VERSION,
/*  98 */   INCOMPATIBLE_API_VERSION_MUSTANG,
/*  99 */   RUNNING_WITH_1_0_RUNTIME,
/*     */   
/* 101 */   MISSING_JAXB_PROPERTIES,
/* 102 */   TRANSIENT_FIELD_NOT_BINDABLE,
/* 103 */   THERE_MUST_BE_VALUE_IN_XMLVALUE,
/* 104 */   UNMATCHABLE_ADAPTER,
/* 105 */   ANONYMOUS_ARRAY_ITEM,
/*     */   
/* 107 */   ACCESSORFACTORY_INSTANTIATION_EXCEPTION,
/* 108 */   ACCESSORFACTORY_ACCESS_EXCEPTION,
/* 109 */   CUSTOM_ACCESSORFACTORY_PROPERTY_ERROR,
/* 110 */   CUSTOM_ACCESSORFACTORY_FIELD_ERROR,
/* 111 */   XMLGREGORIANCALENDAR_INVALID,
/* 112 */   XMLGREGORIANCALENDAR_SEC,
/* 113 */   XMLGREGORIANCALENDAR_MIN,
/* 114 */   XMLGREGORIANCALENDAR_HR,
/* 115 */   XMLGREGORIANCALENDAR_DAY,
/* 116 */   XMLGREGORIANCALENDAR_MONTH,
/* 117 */   XMLGREGORIANCALENDAR_YEAR,
/* 118 */   XMLGREGORIANCALENDAR_TIMEZONE;
/*     */   
/*     */   static {
/* 121 */     rb = ResourceBundle.getBundle(Messages.class.getName());
/*     */   }
/*     */   public String toString() {
/* 124 */     return format(new Object[0]);
/*     */   }
/*     */   
/*     */   public String format(Object... args) {
/* 128 */     return MessageFormat.format(rb.getString(name()), args);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\Messages.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */