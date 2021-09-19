/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import com.mysql.jdbc.log.Log;
/*      */ import com.mysql.jdbc.log.StandardLogger;
/*      */ import java.io.Serializable;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.Field;
/*      */ import java.sql.DriverPropertyInfo;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.TreeMap;
/*      */ import javax.naming.RefAddr;
/*      */ import javax.naming.Reference;
/*      */ import javax.naming.StringRefAddr;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ConnectionPropertiesImpl
/*      */   implements Serializable, ConnectionProperties
/*      */ {
/*      */   private static final long serialVersionUID = 4257801713007640580L;
/*      */   
/*      */   class BooleanConnectionProperty
/*      */     extends ConnectionProperty
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 2540132501709159404L;
/*      */     private final ConnectionPropertiesImpl this$0;
/*      */     
/*      */     BooleanConnectionProperty(ConnectionPropertiesImpl this$0, String propertyNameToSet, boolean defaultValueToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory) {
/*   73 */       super(this$0, propertyNameToSet, Boolean.valueOf(defaultValueToSet), null, 0, 0, descriptionToSet, sinceVersionToSet, category, orderInCategory);
/*      */       this.this$0 = this$0;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     String[] getAllowableValues() {
/*   82 */       return new String[] { "true", "false", "yes", "no" };
/*      */     }
/*      */     
/*      */     boolean getValueAsBoolean() {
/*   86 */       return ((Boolean)this.valueAsObject).booleanValue();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean hasValueConstraints() {
/*   93 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void initializeFrom(String extractedValue) throws SQLException {
/*  100 */       if (extractedValue != null) {
/*  101 */         validateStringValues(extractedValue);
/*      */         
/*  103 */         this.valueAsObject = Boolean.valueOf((extractedValue.equalsIgnoreCase("TRUE") || extractedValue.equalsIgnoreCase("YES")));
/*      */       }
/*      */       else {
/*      */         
/*  107 */         this.valueAsObject = this.defaultValue;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean isRangeBased() {
/*  115 */       return false;
/*      */     }
/*      */     
/*      */     void setValue(boolean valueFlag) {
/*  119 */       this.valueAsObject = Boolean.valueOf(valueFlag);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   abstract class ConnectionProperty
/*      */     implements Serializable
/*      */   {
/*      */     String[] allowableValues;
/*      */     
/*      */     String categoryName;
/*      */     
/*      */     Object defaultValue;
/*      */     
/*      */     int lowerBound;
/*      */     
/*      */     int order;
/*      */     
/*      */     String propertyName;
/*      */     String sinceVersion;
/*      */     int upperBound;
/*      */     Object valueAsObject;
/*      */     boolean required;
/*      */     String description;
/*      */     private final ConnectionPropertiesImpl this$0;
/*      */     
/*      */     public ConnectionProperty(ConnectionPropertiesImpl this$0) {
/*  146 */       this.this$0 = this$0;
/*      */     }
/*      */ 
/*      */     
/*      */     ConnectionProperty(ConnectionPropertiesImpl this$0, String propertyNameToSet, Object defaultValueToSet, String[] allowableValuesToSet, int lowerBoundToSet, int upperBoundToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory) {
/*  151 */       this.this$0 = this$0;
/*      */       
/*  153 */       this.description = descriptionToSet;
/*  154 */       this.propertyName = propertyNameToSet;
/*  155 */       this.defaultValue = defaultValueToSet;
/*  156 */       this.valueAsObject = defaultValueToSet;
/*  157 */       this.allowableValues = allowableValuesToSet;
/*  158 */       this.lowerBound = lowerBoundToSet;
/*  159 */       this.upperBound = upperBoundToSet;
/*  160 */       this.required = false;
/*  161 */       this.sinceVersion = sinceVersionToSet;
/*  162 */       this.categoryName = category;
/*  163 */       this.order = orderInCategory;
/*      */     }
/*      */     
/*      */     String[] getAllowableValues() {
/*  167 */       return this.allowableValues;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     String getCategoryName() {
/*  174 */       return this.categoryName;
/*      */     }
/*      */     
/*      */     Object getDefaultValue() {
/*  178 */       return this.defaultValue;
/*      */     }
/*      */     
/*      */     int getLowerBound() {
/*  182 */       return this.lowerBound;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int getOrder() {
/*  189 */       return this.order;
/*      */     }
/*      */     
/*      */     String getPropertyName() {
/*  193 */       return this.propertyName;
/*      */     }
/*      */     
/*      */     int getUpperBound() {
/*  197 */       return this.upperBound;
/*      */     }
/*      */     
/*      */     Object getValueAsObject() {
/*  201 */       return this.valueAsObject;
/*      */     }
/*      */     
/*      */     abstract boolean hasValueConstraints();
/*      */     
/*      */     void initializeFrom(Properties extractFrom) throws SQLException {
/*  207 */       String extractedValue = extractFrom.getProperty(getPropertyName());
/*  208 */       extractFrom.remove(getPropertyName());
/*  209 */       initializeFrom(extractedValue);
/*      */     }
/*      */     
/*      */     void initializeFrom(Reference ref) throws SQLException {
/*  213 */       RefAddr refAddr = ref.get(getPropertyName());
/*      */       
/*  215 */       if (refAddr != null) {
/*  216 */         String refContentAsString = (String)refAddr.getContent();
/*      */         
/*  218 */         initializeFrom(refContentAsString);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     abstract void initializeFrom(String param1String) throws SQLException;
/*      */ 
/*      */     
/*      */     abstract boolean isRangeBased();
/*      */ 
/*      */     
/*      */     void setCategoryName(String categoryName) {
/*  231 */       this.categoryName = categoryName;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void setOrder(int order) {
/*  239 */       this.order = order;
/*      */     }
/*      */     
/*      */     void setValueAsObject(Object obj) {
/*  243 */       this.valueAsObject = obj;
/*      */     }
/*      */     
/*      */     void storeTo(Reference ref) {
/*  247 */       if (getValueAsObject() != null) {
/*  248 */         ref.add(new StringRefAddr(getPropertyName(), getValueAsObject().toString()));
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     DriverPropertyInfo getAsDriverPropertyInfo() {
/*  254 */       DriverPropertyInfo dpi = new DriverPropertyInfo(this.propertyName, null);
/*  255 */       dpi.choices = getAllowableValues();
/*  256 */       dpi.value = (this.valueAsObject != null) ? this.valueAsObject.toString() : null;
/*  257 */       dpi.required = this.required;
/*  258 */       dpi.description = this.description;
/*      */       
/*  260 */       return dpi;
/*      */     }
/*      */ 
/*      */     
/*      */     void validateStringValues(String valueToValidate) throws SQLException {
/*  265 */       String[] validateAgainst = getAllowableValues();
/*      */       
/*  267 */       if (valueToValidate == null) {
/*      */         return;
/*      */       }
/*      */       
/*  271 */       if (validateAgainst == null || validateAgainst.length == 0) {
/*      */         return;
/*      */       }
/*      */       
/*  275 */       for (int i = 0; i < validateAgainst.length; i++) {
/*  276 */         if (validateAgainst[i] != null && validateAgainst[i].equalsIgnoreCase(valueToValidate)) {
/*      */           return;
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  282 */       StringBuffer errorMessageBuf = new StringBuffer();
/*      */       
/*  284 */       errorMessageBuf.append("The connection property '");
/*  285 */       errorMessageBuf.append(getPropertyName());
/*  286 */       errorMessageBuf.append("' only accepts values of the form: ");
/*      */       
/*  288 */       if (validateAgainst.length != 0) {
/*  289 */         errorMessageBuf.append("'");
/*  290 */         errorMessageBuf.append(validateAgainst[0]);
/*  291 */         errorMessageBuf.append("'");
/*      */         
/*  293 */         for (int j = 1; j < validateAgainst.length - 1; j++) {
/*  294 */           errorMessageBuf.append(", ");
/*  295 */           errorMessageBuf.append("'");
/*  296 */           errorMessageBuf.append(validateAgainst[j]);
/*  297 */           errorMessageBuf.append("'");
/*      */         } 
/*      */         
/*  300 */         errorMessageBuf.append(" or '");
/*  301 */         errorMessageBuf.append(validateAgainst[validateAgainst.length - 1]);
/*      */         
/*  303 */         errorMessageBuf.append("'");
/*      */       } 
/*      */       
/*  306 */       errorMessageBuf.append(". The value '");
/*  307 */       errorMessageBuf.append(valueToValidate);
/*  308 */       errorMessageBuf.append("' is not in this set.");
/*      */       
/*  310 */       throw SQLError.createSQLException(errorMessageBuf.toString(), "S1009", this.this$0.getExceptionInterceptor());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   class IntegerConnectionProperty
/*      */     extends ConnectionProperty
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -3004305481796850832L;
/*      */     int multiplier;
/*      */     private final ConnectionPropertiesImpl this$0;
/*      */     
/*      */     public IntegerConnectionProperty(ConnectionPropertiesImpl this$0, String propertyNameToSet, Object defaultValueToSet, String[] allowableValuesToSet, int lowerBoundToSet, int upperBoundToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory) {
/*  324 */       super(propertyNameToSet, defaultValueToSet, allowableValuesToSet, lowerBoundToSet, upperBoundToSet, descriptionToSet, sinceVersionToSet, category, orderInCategory);
/*      */ 
/*      */       
/*      */       ConnectionPropertiesImpl.this = ConnectionPropertiesImpl.this;
/*      */       
/*  329 */       this.multiplier = 1;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     IntegerConnectionProperty(ConnectionPropertiesImpl this$0, String propertyNameToSet, int defaultValueToSet, int lowerBoundToSet, int upperBoundToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory) {
/*  335 */       super(propertyNameToSet, new Integer(defaultValueToSet), null, lowerBoundToSet, upperBoundToSet, descriptionToSet, sinceVersionToSet, category, orderInCategory);
/*      */       ConnectionPropertiesImpl.this = ConnectionPropertiesImpl.this;
/*      */       this.multiplier = 1;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     IntegerConnectionProperty(String propertyNameToSet, int defaultValueToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory) {
/*  353 */       this(propertyNameToSet, defaultValueToSet, 0, 0, descriptionToSet, sinceVersionToSet, category, orderInCategory);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     String[] getAllowableValues() {
/*  361 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int getLowerBound() {
/*  368 */       return this.lowerBound;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int getUpperBound() {
/*  375 */       return this.upperBound;
/*      */     }
/*      */     
/*      */     int getValueAsInt() {
/*  379 */       return ((Integer)this.valueAsObject).intValue();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean hasValueConstraints() {
/*  386 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void initializeFrom(String extractedValue) throws SQLException {
/*  393 */       if (extractedValue != null) {
/*      */         
/*      */         try {
/*  396 */           int intValue = Double.valueOf(extractedValue).intValue();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  407 */           this.valueAsObject = new Integer(intValue * this.multiplier);
/*  408 */         } catch (NumberFormatException nfe) {
/*  409 */           throw SQLError.createSQLException("The connection property '" + getPropertyName() + "' only accepts integer values. The value '" + extractedValue + "' can not be converted to an integer.", "S1009", ConnectionPropertiesImpl.this.getExceptionInterceptor());
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  417 */         this.valueAsObject = this.defaultValue;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean isRangeBased() {
/*  425 */       return (getUpperBound() != getLowerBound());
/*      */     }
/*      */     
/*      */     void setValue(int valueFlag) {
/*  429 */       this.valueAsObject = new Integer(valueFlag);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public class LongConnectionProperty
/*      */     extends IntegerConnectionProperty
/*      */   {
/*      */     private static final long serialVersionUID = 6068572984340480895L;
/*      */     private final ConnectionPropertiesImpl this$0;
/*      */     
/*      */     LongConnectionProperty(ConnectionPropertiesImpl this$0, String propertyNameToSet, long defaultValueToSet, long lowerBoundToSet, long upperBoundToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory) {
/*  441 */       super(propertyNameToSet, new Long(defaultValueToSet), (String[])null, (int)lowerBoundToSet, (int)upperBoundToSet, descriptionToSet, sinceVersionToSet, category, orderInCategory);
/*      */       ConnectionPropertiesImpl.this = ConnectionPropertiesImpl.this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     LongConnectionProperty(String propertyNameToSet, long defaultValueToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory) {
/*  450 */       this(propertyNameToSet, defaultValueToSet, 0L, 0L, descriptionToSet, sinceVersionToSet, category, orderInCategory);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void setValue(long value) {
/*  457 */       this.valueAsObject = new Long(value);
/*      */     }
/*      */     
/*      */     long getValueAsLong() {
/*  461 */       return ((Long)this.valueAsObject).longValue();
/*      */     }
/*      */     
/*      */     void initializeFrom(String extractedValue) throws SQLException {
/*  465 */       if (extractedValue != null) {
/*      */         
/*      */         try {
/*  468 */           long longValue = Double.valueOf(extractedValue).longValue();
/*      */           
/*  470 */           this.valueAsObject = new Long(longValue);
/*  471 */         } catch (NumberFormatException nfe) {
/*  472 */           throw SQLError.createSQLException("The connection property '" + getPropertyName() + "' only accepts long integer values. The value '" + extractedValue + "' can not be converted to a long integer.", "S1009", ConnectionPropertiesImpl.this.getExceptionInterceptor());
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  480 */         this.valueAsObject = this.defaultValue;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   class MemorySizeConnectionProperty
/*      */     extends IntegerConnectionProperty
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 7351065128998572656L;
/*      */     private String valueAsString;
/*      */     private final ConnectionPropertiesImpl this$0;
/*      */     
/*      */     MemorySizeConnectionProperty(ConnectionPropertiesImpl this$0, String propertyNameToSet, int defaultValueToSet, int lowerBoundToSet, int upperBoundToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory) {
/*  495 */       super(this$0, propertyNameToSet, defaultValueToSet, lowerBoundToSet, upperBoundToSet, descriptionToSet, sinceVersionToSet, category, orderInCategory);
/*      */       this.this$0 = this$0;
/*      */     }
/*      */ 
/*      */     
/*      */     void initializeFrom(String extractedValue) throws SQLException {
/*  501 */       this.valueAsString = extractedValue;
/*      */       
/*  503 */       if (extractedValue != null) {
/*  504 */         if (extractedValue.endsWith("k") || extractedValue.endsWith("K") || extractedValue.endsWith("kb") || extractedValue.endsWith("Kb") || extractedValue.endsWith("kB")) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  509 */           this.multiplier = 1024;
/*  510 */           int indexOfK = StringUtils.indexOfIgnoreCase(extractedValue, "k");
/*      */           
/*  512 */           extractedValue = extractedValue.substring(0, indexOfK);
/*  513 */         } else if (extractedValue.endsWith("m") || extractedValue.endsWith("M") || extractedValue.endsWith("G") || extractedValue.endsWith("mb") || extractedValue.endsWith("Mb") || extractedValue.endsWith("mB")) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  519 */           this.multiplier = 1048576;
/*  520 */           int indexOfM = StringUtils.indexOfIgnoreCase(extractedValue, "m");
/*      */           
/*  522 */           extractedValue = extractedValue.substring(0, indexOfM);
/*  523 */         } else if (extractedValue.endsWith("g") || extractedValue.endsWith("G") || extractedValue.endsWith("gb") || extractedValue.endsWith("Gb") || extractedValue.endsWith("gB")) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  528 */           this.multiplier = 1073741824;
/*  529 */           int indexOfG = StringUtils.indexOfIgnoreCase(extractedValue, "g");
/*      */           
/*  531 */           extractedValue = extractedValue.substring(0, indexOfG);
/*      */         } 
/*      */       }
/*      */       
/*  535 */       super.initializeFrom(extractedValue);
/*      */     }
/*      */     
/*      */     void setValue(String value) throws SQLException {
/*  539 */       initializeFrom(value);
/*      */     }
/*      */     
/*      */     String getValueAsString() {
/*  543 */       return this.valueAsString;
/*      */     }
/*      */   }
/*      */   
/*      */   class StringConnectionProperty
/*      */     extends ConnectionProperty
/*      */     implements Serializable {
/*      */     private static final long serialVersionUID = 5432127962785948272L;
/*      */     private final ConnectionPropertiesImpl this$0;
/*      */     
/*      */     StringConnectionProperty(String propertyNameToSet, String defaultValueToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory) {
/*  554 */       this(propertyNameToSet, defaultValueToSet, (String[])null, descriptionToSet, sinceVersionToSet, category, orderInCategory);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     StringConnectionProperty(String propertyNameToSet, String defaultValueToSet, String[] allowableValuesToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory) {
/*  572 */       super(propertyNameToSet, defaultValueToSet, allowableValuesToSet, 0, 0, descriptionToSet, sinceVersionToSet, category, orderInCategory);
/*      */       ConnectionPropertiesImpl.this = ConnectionPropertiesImpl.this;
/*      */     }
/*      */ 
/*      */     
/*      */     String getValueAsString() {
/*  578 */       return (String)this.valueAsObject;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean hasValueConstraints() {
/*  585 */       return (this.allowableValues != null && this.allowableValues.length > 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void initializeFrom(String extractedValue) throws SQLException {
/*  593 */       if (extractedValue != null) {
/*  594 */         validateStringValues(extractedValue);
/*      */         
/*  596 */         this.valueAsObject = extractedValue;
/*      */       } else {
/*  598 */         this.valueAsObject = this.defaultValue;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean isRangeBased() {
/*  606 */       return false;
/*      */     }
/*      */     
/*      */     void setValue(String valueFlag) {
/*  610 */       this.valueAsObject = valueFlag;
/*      */     }
/*      */   }
/*      */   
/*  614 */   private static final String CONNECTION_AND_AUTH_CATEGORY = Messages.getString("ConnectionProperties.categoryConnectionAuthentication");
/*      */   
/*  616 */   private static final String NETWORK_CATEGORY = Messages.getString("ConnectionProperties.categoryNetworking");
/*      */   
/*  618 */   private static final String DEBUGING_PROFILING_CATEGORY = Messages.getString("ConnectionProperties.categoryDebuggingProfiling");
/*      */   
/*  620 */   private static final String HA_CATEGORY = Messages.getString("ConnectionProperties.categorryHA");
/*      */   
/*  622 */   private static final String MISC_CATEGORY = Messages.getString("ConnectionProperties.categoryMisc");
/*      */   
/*  624 */   private static final String PERFORMANCE_CATEGORY = Messages.getString("ConnectionProperties.categoryPerformance");
/*      */   
/*  626 */   private static final String SECURITY_CATEGORY = Messages.getString("ConnectionProperties.categorySecurity");
/*      */   
/*  628 */   private static final String[] PROPERTY_CATEGORIES = new String[] { CONNECTION_AND_AUTH_CATEGORY, NETWORK_CATEGORY, HA_CATEGORY, SECURITY_CATEGORY, PERFORMANCE_CATEGORY, DEBUGING_PROFILING_CATEGORY, MISC_CATEGORY };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  633 */   private static final ArrayList PROPERTY_LIST = new ArrayList();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  638 */   private static final String STANDARD_LOGGER_NAME = StandardLogger.class.getName();
/*      */   
/*      */   protected static final String ZERO_DATETIME_BEHAVIOR_CONVERT_TO_NULL = "convertToNull";
/*      */   
/*      */   protected static final String ZERO_DATETIME_BEHAVIOR_EXCEPTION = "exception";
/*      */   
/*      */   protected static final String ZERO_DATETIME_BEHAVIOR_ROUND = "round";
/*      */   
/*      */   static {
/*      */     try {
/*  648 */       Field[] declaredFields = ConnectionPropertiesImpl.class.getDeclaredFields();
/*      */ 
/*      */       
/*  651 */       for (int i = 0; i < declaredFields.length; i++) {
/*  652 */         if (ConnectionProperty.class.isAssignableFrom(declaredFields[i].getType()))
/*      */         {
/*  654 */           PROPERTY_LIST.add(declaredFields[i]);
/*      */         }
/*      */       } 
/*  657 */     } catch (Exception ex) {
/*  658 */       RuntimeException rtEx = new RuntimeException();
/*  659 */       rtEx.initCause(ex);
/*      */       
/*  661 */       throw rtEx;
/*      */     } 
/*      */   }
/*      */   
/*      */   public ExceptionInterceptor getExceptionInterceptor() {
/*  666 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static DriverPropertyInfo[] exposeAsDriverPropertyInfo(Properties info, int slotsToReserve) throws SQLException {
/*  685 */     return (new ConnectionPropertiesImpl() {  }).exposeAsDriverPropertyInfoInternal(info, slotsToReserve);
/*      */   }
/*      */ 
/*      */   
/*  689 */   private BooleanConnectionProperty allowLoadLocalInfile = new BooleanConnectionProperty(this, "allowLoadLocalInfile", true, Messages.getString("ConnectionProperties.loadDataLocal"), "3.0.3", SECURITY_CATEGORY, 2147483647);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  695 */   private BooleanConnectionProperty allowMultiQueries = new BooleanConnectionProperty(this, "allowMultiQueries", false, Messages.getString("ConnectionProperties.allowMultiQueries"), "3.1.1", SECURITY_CATEGORY, 1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  701 */   private BooleanConnectionProperty allowNanAndInf = new BooleanConnectionProperty(this, "allowNanAndInf", false, Messages.getString("ConnectionProperties.allowNANandINF"), "3.1.5", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  707 */   private BooleanConnectionProperty allowUrlInLocalInfile = new BooleanConnectionProperty(this, "allowUrlInLocalInfile", false, Messages.getString("ConnectionProperties.allowUrlInLoadLocal"), "3.1.4", SECURITY_CATEGORY, 2147483647);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  713 */   private BooleanConnectionProperty alwaysSendSetIsolation = new BooleanConnectionProperty(this, "alwaysSendSetIsolation", true, Messages.getString("ConnectionProperties.alwaysSendSetIsolation"), "3.1.7", PERFORMANCE_CATEGORY, 2147483647);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  719 */   private BooleanConnectionProperty autoClosePStmtStreams = new BooleanConnectionProperty(this, "autoClosePStmtStreams", false, Messages.getString("ConnectionProperties.autoClosePstmtStreams"), "3.1.12", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  727 */   private BooleanConnectionProperty autoDeserialize = new BooleanConnectionProperty(this, "autoDeserialize", false, Messages.getString("ConnectionProperties.autoDeserialize"), "3.1.5", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  733 */   private BooleanConnectionProperty autoGenerateTestcaseScript = new BooleanConnectionProperty(this, "autoGenerateTestcaseScript", false, Messages.getString("ConnectionProperties.autoGenerateTestcaseScript"), "3.1.9", DEBUGING_PROFILING_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean autoGenerateTestcaseScriptAsBoolean = false;
/*      */ 
/*      */   
/*  740 */   private BooleanConnectionProperty autoReconnect = new BooleanConnectionProperty(this, "autoReconnect", false, Messages.getString("ConnectionProperties.autoReconnect"), "1.1", HA_CATEGORY, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  746 */   private BooleanConnectionProperty autoReconnectForPools = new BooleanConnectionProperty(this, "autoReconnectForPools", false, Messages.getString("ConnectionProperties.autoReconnectForPools"), "3.1.3", HA_CATEGORY, 1);
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean autoReconnectForPoolsAsBoolean = false;
/*      */ 
/*      */ 
/*      */   
/*  754 */   private MemorySizeConnectionProperty blobSendChunkSize = new MemorySizeConnectionProperty(this, "blobSendChunkSize", 1048576, 1, 2147483647, Messages.getString("ConnectionProperties.blobSendChunkSize"), "3.1.9", PERFORMANCE_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  762 */   private BooleanConnectionProperty autoSlowLog = new BooleanConnectionProperty(this, "autoSlowLog", true, Messages.getString("ConnectionProperties.autoSlowLog"), "5.1.4", DEBUGING_PROFILING_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  767 */   private BooleanConnectionProperty blobsAreStrings = new BooleanConnectionProperty(this, "blobsAreStrings", false, "Should the driver always treat BLOBs as Strings - specifically to work around dubious metadata returned by the server for GROUP BY clauses?", "5.0.8", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  773 */   private BooleanConnectionProperty functionsNeverReturnBlobs = new BooleanConnectionProperty(this, "functionsNeverReturnBlobs", false, "Should the driver always treat data from functions returning BLOBs as Strings - specifically to work around dubious metadata returned by the server for GROUP BY clauses?", "5.0.8", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  779 */   private BooleanConnectionProperty cacheCallableStatements = new BooleanConnectionProperty(this, "cacheCallableStmts", false, Messages.getString("ConnectionProperties.cacheCallableStatements"), "3.1.2", PERFORMANCE_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  784 */   private BooleanConnectionProperty cachePreparedStatements = new BooleanConnectionProperty(this, "cachePrepStmts", false, Messages.getString("ConnectionProperties.cachePrepStmts"), "3.0.10", PERFORMANCE_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  790 */   private BooleanConnectionProperty cacheResultSetMetadata = new BooleanConnectionProperty(this, "cacheResultSetMetadata", false, Messages.getString("ConnectionProperties.cacheRSMetadata"), "3.1.1", PERFORMANCE_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean cacheResultSetMetaDataAsBoolean;
/*      */ 
/*      */ 
/*      */   
/*  798 */   private BooleanConnectionProperty cacheServerConfiguration = new BooleanConnectionProperty(this, "cacheServerConfiguration", false, Messages.getString("ConnectionProperties.cacheServerConfiguration"), "3.1.5", PERFORMANCE_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  804 */   private IntegerConnectionProperty callableStatementCacheSize = new IntegerConnectionProperty("callableStmtCacheSize", 100, 0, 2147483647, Messages.getString("ConnectionProperties.callableStmtCacheSize"), "3.1.2", PERFORMANCE_CATEGORY, 5);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  812 */   private BooleanConnectionProperty capitalizeTypeNames = new BooleanConnectionProperty(this, "capitalizeTypeNames", true, Messages.getString("ConnectionProperties.capitalizeTypeNames"), "2.0.7", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  818 */   private StringConnectionProperty characterEncoding = new StringConnectionProperty("characterEncoding", null, Messages.getString("ConnectionProperties.characterEncoding"), "1.1g", MISC_CATEGORY, 5);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  824 */   private String characterEncodingAsString = null;
/*      */   
/*  826 */   private StringConnectionProperty characterSetResults = new StringConnectionProperty("characterSetResults", null, Messages.getString("ConnectionProperties.characterSetResults"), "3.0.13", MISC_CATEGORY, 6);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  831 */   private StringConnectionProperty clientInfoProvider = new StringConnectionProperty("clientInfoProvider", "com.mysql.jdbc.JDBC4CommentClientInfoProvider", Messages.getString("ConnectionProperties.clientInfoProvider"), "5.1.0", DEBUGING_PROFILING_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  837 */   private BooleanConnectionProperty clobberStreamingResults = new BooleanConnectionProperty(this, "clobberStreamingResults", false, Messages.getString("ConnectionProperties.clobberStreamingResults"), "3.0.9", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  843 */   private StringConnectionProperty clobCharacterEncoding = new StringConnectionProperty("clobCharacterEncoding", null, Messages.getString("ConnectionProperties.clobCharacterEncoding"), "5.0.0", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  849 */   private BooleanConnectionProperty compensateOnDuplicateKeyUpdateCounts = new BooleanConnectionProperty(this, "compensateOnDuplicateKeyUpdateCounts", false, Messages.getString("ConnectionProperties.compensateOnDuplicateKeyUpdateCounts"), "5.1.7", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  854 */   private StringConnectionProperty connectionCollation = new StringConnectionProperty("connectionCollation", null, Messages.getString("ConnectionProperties.connectionCollation"), "3.0.13", MISC_CATEGORY, 7);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  860 */   private StringConnectionProperty connectionLifecycleInterceptors = new StringConnectionProperty("connectionLifecycleInterceptors", null, Messages.getString("ConnectionProperties.connectionLifecycleInterceptors"), "5.1.4", CONNECTION_AND_AUTH_CATEGORY, 2147483647);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  866 */   private IntegerConnectionProperty connectTimeout = new IntegerConnectionProperty("connectTimeout", 0, 0, 2147483647, Messages.getString("ConnectionProperties.connectTimeout"), "3.0.1", CONNECTION_AND_AUTH_CATEGORY, 9);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  871 */   private BooleanConnectionProperty continueBatchOnError = new BooleanConnectionProperty(this, "continueBatchOnError", true, Messages.getString("ConnectionProperties.continueBatchOnError"), "3.0.3", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  877 */   private BooleanConnectionProperty createDatabaseIfNotExist = new BooleanConnectionProperty(this, "createDatabaseIfNotExist", false, Messages.getString("ConnectionProperties.createDatabaseIfNotExist"), "3.1.9", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  883 */   private IntegerConnectionProperty defaultFetchSize = new IntegerConnectionProperty("defaultFetchSize", 0, Messages.getString("ConnectionProperties.defaultFetchSize"), "3.1.9", PERFORMANCE_CATEGORY, -2147483648);
/*      */   
/*  885 */   private BooleanConnectionProperty detectServerPreparedStmts = new BooleanConnectionProperty(this, "useServerPrepStmts", false, Messages.getString("ConnectionProperties.useServerPrepStmts"), "3.1.0", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  891 */   private BooleanConnectionProperty dontTrackOpenResources = new BooleanConnectionProperty(this, "dontTrackOpenResources", false, Messages.getString("ConnectionProperties.dontTrackOpenResources"), "3.1.7", PERFORMANCE_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  897 */   private BooleanConnectionProperty dumpQueriesOnException = new BooleanConnectionProperty(this, "dumpQueriesOnException", false, Messages.getString("ConnectionProperties.dumpQueriesOnException"), "3.1.3", DEBUGING_PROFILING_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  903 */   private BooleanConnectionProperty dynamicCalendars = new BooleanConnectionProperty(this, "dynamicCalendars", false, Messages.getString("ConnectionProperties.dynamicCalendars"), "3.1.5", PERFORMANCE_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  909 */   private BooleanConnectionProperty elideSetAutoCommits = new BooleanConnectionProperty(this, "elideSetAutoCommits", false, Messages.getString("ConnectionProperties.eliseSetAutoCommit"), "3.1.3", PERFORMANCE_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  915 */   private BooleanConnectionProperty emptyStringsConvertToZero = new BooleanConnectionProperty(this, "emptyStringsConvertToZero", true, Messages.getString("ConnectionProperties.emptyStringsConvertToZero"), "3.1.8", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  920 */   private BooleanConnectionProperty emulateLocators = new BooleanConnectionProperty(this, "emulateLocators", false, Messages.getString("ConnectionProperties.emulateLocators"), "3.1.0", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */   
/*  924 */   private BooleanConnectionProperty emulateUnsupportedPstmts = new BooleanConnectionProperty(this, "emulateUnsupportedPstmts", true, Messages.getString("ConnectionProperties.emulateUnsupportedPstmts"), "3.1.7", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  930 */   private BooleanConnectionProperty enablePacketDebug = new BooleanConnectionProperty(this, "enablePacketDebug", false, Messages.getString("ConnectionProperties.enablePacketDebug"), "3.1.3", DEBUGING_PROFILING_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  936 */   private BooleanConnectionProperty enableQueryTimeouts = new BooleanConnectionProperty(this, "enableQueryTimeouts", true, Messages.getString("ConnectionProperties.enableQueryTimeouts"), "5.0.6", PERFORMANCE_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  943 */   private BooleanConnectionProperty explainSlowQueries = new BooleanConnectionProperty(this, "explainSlowQueries", false, Messages.getString("ConnectionProperties.explainSlowQueries"), "3.1.2", DEBUGING_PROFILING_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  949 */   private StringConnectionProperty exceptionInterceptors = new StringConnectionProperty("exceptionInterceptors", null, Messages.getString("ConnectionProperties.exceptionInterceptors"), "5.1.8", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  956 */   private BooleanConnectionProperty failOverReadOnly = new BooleanConnectionProperty(this, "failOverReadOnly", true, Messages.getString("ConnectionProperties.failoverReadOnly"), "3.0.12", HA_CATEGORY, 2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  962 */   private BooleanConnectionProperty gatherPerformanceMetrics = new BooleanConnectionProperty(this, "gatherPerfMetrics", false, Messages.getString("ConnectionProperties.gatherPerfMetrics"), "3.1.2", DEBUGING_PROFILING_CATEGORY, 1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  968 */   private BooleanConnectionProperty generateSimpleParameterMetadata = new BooleanConnectionProperty(this, "generateSimpleParameterMetadata", false, Messages.getString("ConnectionProperties.generateSimpleParameterMetadata"), "5.0.5", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */   
/*      */   private boolean highAvailabilityAsBoolean = false;
/*      */   
/*  973 */   private BooleanConnectionProperty holdResultsOpenOverStatementClose = new BooleanConnectionProperty(this, "holdResultsOpenOverStatementClose", false, Messages.getString("ConnectionProperties.holdRSOpenOverStmtClose"), "3.1.7", PERFORMANCE_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  979 */   private BooleanConnectionProperty includeInnodbStatusInDeadlockExceptions = new BooleanConnectionProperty(this, "includeInnodbStatusInDeadlockExceptions", false, "Include the output of \"SHOW ENGINE INNODB STATUS\" in exception messages when deadlock exceptions are detected?", "5.0.7", DEBUGING_PROFILING_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  985 */   private BooleanConnectionProperty ignoreNonTxTables = new BooleanConnectionProperty(this, "ignoreNonTxTables", false, Messages.getString("ConnectionProperties.ignoreNonTxTables"), "3.0.9", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  991 */   private IntegerConnectionProperty initialTimeout = new IntegerConnectionProperty("initialTimeout", 2, 1, 2147483647, Messages.getString("ConnectionProperties.initialTimeout"), "1.1", HA_CATEGORY, 5);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  996 */   private BooleanConnectionProperty isInteractiveClient = new BooleanConnectionProperty(this, "interactiveClient", false, Messages.getString("ConnectionProperties.interactiveClient"), "3.1.0", CONNECTION_AND_AUTH_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1002 */   private BooleanConnectionProperty jdbcCompliantTruncation = new BooleanConnectionProperty(this, "jdbcCompliantTruncation", true, Messages.getString("ConnectionProperties.jdbcCompliantTruncation"), "3.1.2", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1008 */   private boolean jdbcCompliantTruncationForReads = this.jdbcCompliantTruncation.getValueAsBoolean();
/*      */ 
/*      */   
/* 1011 */   protected MemorySizeConnectionProperty largeRowSizeThreshold = new MemorySizeConnectionProperty(this, "largeRowSizeThreshold", 2048, 0, 2147483647, Messages.getString("ConnectionProperties.largeRowSizeThreshold"), "5.1.1", PERFORMANCE_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1016 */   private StringConnectionProperty loadBalanceStrategy = new StringConnectionProperty("loadBalanceStrategy", "random", new String[] { "random", "bestResponseTime" }, Messages.getString("ConnectionProperties.loadBalanceStrategy"), "5.0.6", PERFORMANCE_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1023 */   private IntegerConnectionProperty loadBalanceBlacklistTimeout = new IntegerConnectionProperty("loadBalanceBlacklistTimeout", 0, 0, 2147483647, Messages.getString("ConnectionProperties.loadBalanceBlacklistTimeout"), "5.1.0", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1029 */   private StringConnectionProperty localSocketAddress = new StringConnectionProperty("localSocketAddress", null, Messages.getString("ConnectionProperties.localSocketAddress"), "5.0.5", CONNECTION_AND_AUTH_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */   
/* 1033 */   private MemorySizeConnectionProperty locatorFetchBufferSize = new MemorySizeConnectionProperty(this, "locatorFetchBufferSize", 1048576, 0, 2147483647, Messages.getString("ConnectionProperties.locatorFetchBufferSize"), "3.2.1", PERFORMANCE_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1041 */   private StringConnectionProperty loggerClassName = new StringConnectionProperty("logger", STANDARD_LOGGER_NAME, Messages.getString("ConnectionProperties.logger", new Object[] { Log.class.getName(), STANDARD_LOGGER_NAME }), "3.1.1", DEBUGING_PROFILING_CATEGORY, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1047 */   private BooleanConnectionProperty logSlowQueries = new BooleanConnectionProperty(this, "logSlowQueries", false, Messages.getString("ConnectionProperties.logSlowQueries"), "3.1.2", DEBUGING_PROFILING_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1053 */   private BooleanConnectionProperty logXaCommands = new BooleanConnectionProperty(this, "logXaCommands", false, Messages.getString("ConnectionProperties.logXaCommands"), "5.0.5", DEBUGING_PROFILING_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1059 */   private BooleanConnectionProperty maintainTimeStats = new BooleanConnectionProperty(this, "maintainTimeStats", true, Messages.getString("ConnectionProperties.maintainTimeStats"), "3.1.9", PERFORMANCE_CATEGORY, 2147483647);
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean maintainTimeStatsAsBoolean = true;
/*      */ 
/*      */ 
/*      */   
/* 1067 */   private IntegerConnectionProperty maxQuerySizeToLog = new IntegerConnectionProperty("maxQuerySizeToLog", 2048, 0, 2147483647, Messages.getString("ConnectionProperties.maxQuerySizeToLog"), "3.1.3", DEBUGING_PROFILING_CATEGORY, 4);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1075 */   private IntegerConnectionProperty maxReconnects = new IntegerConnectionProperty("maxReconnects", 3, 1, 2147483647, Messages.getString("ConnectionProperties.maxReconnects"), "1.1", HA_CATEGORY, 4);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1083 */   private IntegerConnectionProperty retriesAllDown = new IntegerConnectionProperty("retriesAllDown", 120, 0, 2147483647, Messages.getString("ConnectionProperties.retriesAllDown"), "5.1.6", HA_CATEGORY, 4);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1091 */   private IntegerConnectionProperty maxRows = new IntegerConnectionProperty("maxRows", -1, -1, 2147483647, Messages.getString("ConnectionProperties.maxRows"), Messages.getString("ConnectionProperties.allVersions"), MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1096 */   private int maxRowsAsInt = -1;
/*      */   
/* 1098 */   private IntegerConnectionProperty metadataCacheSize = new IntegerConnectionProperty("metadataCacheSize", 50, 1, 2147483647, Messages.getString("ConnectionProperties.metadataCacheSize"), "3.1.1", PERFORMANCE_CATEGORY, 5);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1106 */   private IntegerConnectionProperty netTimeoutForStreamingResults = new IntegerConnectionProperty("netTimeoutForStreamingResults", 600, 0, 2147483647, Messages.getString("ConnectionProperties.netTimeoutForStreamingResults"), "5.1.0", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1112 */   private BooleanConnectionProperty noAccessToProcedureBodies = new BooleanConnectionProperty(this, "noAccessToProcedureBodies", false, "When determining procedure parameter types for CallableStatements, and the connected user  can't access procedure bodies through \"SHOW CREATE PROCEDURE\" or select on mysql.proc  should the driver instead create basic metadata (all parameters reported as IN VARCHARs, but allowing registerOutParameter() to be called on them anyway) instead  of throwing an exception?", "5.0.3", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1122 */   private BooleanConnectionProperty noDatetimeStringSync = new BooleanConnectionProperty(this, "noDatetimeStringSync", false, Messages.getString("ConnectionProperties.noDatetimeStringSync"), "3.1.7", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1128 */   private BooleanConnectionProperty noTimezoneConversionForTimeType = new BooleanConnectionProperty(this, "noTimezoneConversionForTimeType", false, Messages.getString("ConnectionProperties.noTzConversionForTimeType"), "5.0.0", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1134 */   private BooleanConnectionProperty nullCatalogMeansCurrent = new BooleanConnectionProperty(this, "nullCatalogMeansCurrent", true, Messages.getString("ConnectionProperties.nullCatalogMeansCurrent"), "3.1.8", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1140 */   private BooleanConnectionProperty nullNamePatternMatchesAll = new BooleanConnectionProperty(this, "nullNamePatternMatchesAll", true, Messages.getString("ConnectionProperties.nullNamePatternMatchesAll"), "3.1.8", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1146 */   private IntegerConnectionProperty packetDebugBufferSize = new IntegerConnectionProperty("packetDebugBufferSize", 20, 0, 2147483647, Messages.getString("ConnectionProperties.packetDebugBufferSize"), "3.1.3", DEBUGING_PROFILING_CATEGORY, 7);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1154 */   private BooleanConnectionProperty padCharsWithSpace = new BooleanConnectionProperty(this, "padCharsWithSpace", false, Messages.getString("ConnectionProperties.padCharsWithSpace"), "5.0.6", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1162 */   private BooleanConnectionProperty paranoid = new BooleanConnectionProperty(this, "paranoid", false, Messages.getString("ConnectionProperties.paranoid"), "3.0.1", SECURITY_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1168 */   private BooleanConnectionProperty pedantic = new BooleanConnectionProperty(this, "pedantic", false, Messages.getString("ConnectionProperties.pedantic"), "3.0.0", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */   
/* 1172 */   private BooleanConnectionProperty pinGlobalTxToPhysicalConnection = new BooleanConnectionProperty(this, "pinGlobalTxToPhysicalConnection", false, Messages.getString("ConnectionProperties.pinGlobalTxToPhysicalConnection"), "5.0.1", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */   
/* 1176 */   private BooleanConnectionProperty populateInsertRowWithDefaultValues = new BooleanConnectionProperty(this, "populateInsertRowWithDefaultValues", false, Messages.getString("ConnectionProperties.populateInsertRowWithDefaultValues"), "5.0.5", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1181 */   private IntegerConnectionProperty preparedStatementCacheSize = new IntegerConnectionProperty("prepStmtCacheSize", 25, 0, 2147483647, Messages.getString("ConnectionProperties.prepStmtCacheSize"), "3.0.10", PERFORMANCE_CATEGORY, 10);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1186 */   private IntegerConnectionProperty preparedStatementCacheSqlLimit = new IntegerConnectionProperty("prepStmtCacheSqlLimit", 256, 1, 2147483647, Messages.getString("ConnectionProperties.prepStmtCacheSqlLimit"), "3.0.10", PERFORMANCE_CATEGORY, 11);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1194 */   private BooleanConnectionProperty processEscapeCodesForPrepStmts = new BooleanConnectionProperty(this, "processEscapeCodesForPrepStmts", true, Messages.getString("ConnectionProperties.processEscapeCodesForPrepStmts"), "3.1.12", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1201 */   private StringConnectionProperty profilerEventHandler = new StringConnectionProperty("profilerEventHandler", "com.mysql.jdbc.profiler.LoggingProfilerEventHandler", Messages.getString("ConnectionProperties.profilerEventHandler"), "5.1.6", DEBUGING_PROFILING_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1207 */   private StringConnectionProperty profileSql = new StringConnectionProperty("profileSql", null, Messages.getString("ConnectionProperties.profileSqlDeprecated"), "2.0.14", DEBUGING_PROFILING_CATEGORY, 3);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1213 */   private BooleanConnectionProperty profileSQL = new BooleanConnectionProperty(this, "profileSQL", false, Messages.getString("ConnectionProperties.profileSQL"), "3.1.0", DEBUGING_PROFILING_CATEGORY, 1);
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean profileSQLAsBoolean = false;
/*      */ 
/*      */ 
/*      */   
/* 1221 */   private StringConnectionProperty propertiesTransform = new StringConnectionProperty("propertiesTransform", null, Messages.getString("ConnectionProperties.connectionPropertiesTransform"), "3.1.4", CONNECTION_AND_AUTH_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1227 */   private IntegerConnectionProperty queriesBeforeRetryMaster = new IntegerConnectionProperty("queriesBeforeRetryMaster", 50, 1, 2147483647, Messages.getString("ConnectionProperties.queriesBeforeRetryMaster"), "3.0.2", HA_CATEGORY, 7);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1235 */   private BooleanConnectionProperty queryTimeoutKillsConnection = new BooleanConnectionProperty(this, "queryTimeoutKillsConnection", false, Messages.getString("ConnectionProperties.queryTimeoutKillsConnection"), "5.1.9", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */   
/* 1239 */   private BooleanConnectionProperty reconnectAtTxEnd = new BooleanConnectionProperty(this, "reconnectAtTxEnd", false, Messages.getString("ConnectionProperties.reconnectAtTxEnd"), "3.0.10", HA_CATEGORY, 4);
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean reconnectTxAtEndAsBoolean = false;
/*      */ 
/*      */   
/* 1246 */   private BooleanConnectionProperty relaxAutoCommit = new BooleanConnectionProperty(this, "relaxAutoCommit", false, Messages.getString("ConnectionProperties.relaxAutoCommit"), "2.0.13", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1252 */   private IntegerConnectionProperty reportMetricsIntervalMillis = new IntegerConnectionProperty("reportMetricsIntervalMillis", 30000, 0, 2147483647, Messages.getString("ConnectionProperties.reportMetricsIntervalMillis"), "3.1.2", DEBUGING_PROFILING_CATEGORY, 3);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1260 */   private BooleanConnectionProperty requireSSL = new BooleanConnectionProperty(this, "requireSSL", false, Messages.getString("ConnectionProperties.requireSSL"), "3.1.0", SECURITY_CATEGORY, 3);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1265 */   private StringConnectionProperty resourceId = new StringConnectionProperty("resourceId", null, Messages.getString("ConnectionProperties.resourceId"), "5.0.1", HA_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1272 */   private IntegerConnectionProperty resultSetSizeThreshold = new IntegerConnectionProperty("resultSetSizeThreshold", 100, Messages.getString("ConnectionProperties.resultSetSizeThreshold"), "5.0.5", DEBUGING_PROFILING_CATEGORY, -2147483648);
/*      */ 
/*      */   
/* 1275 */   private BooleanConnectionProperty retainStatementAfterResultSetClose = new BooleanConnectionProperty(this, "retainStatementAfterResultSetClose", false, Messages.getString("ConnectionProperties.retainStatementAfterResultSetClose"), "3.1.11", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1281 */   private BooleanConnectionProperty rewriteBatchedStatements = new BooleanConnectionProperty(this, "rewriteBatchedStatements", false, Messages.getString("ConnectionProperties.rewriteBatchedStatements"), "3.1.13", PERFORMANCE_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1287 */   private BooleanConnectionProperty rollbackOnPooledClose = new BooleanConnectionProperty(this, "rollbackOnPooledClose", true, Messages.getString("ConnectionProperties.rollbackOnPooledClose"), "3.0.15", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1293 */   private BooleanConnectionProperty roundRobinLoadBalance = new BooleanConnectionProperty(this, "roundRobinLoadBalance", false, Messages.getString("ConnectionProperties.roundRobinLoadBalance"), "3.1.2", HA_CATEGORY, 5);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1299 */   private BooleanConnectionProperty runningCTS13 = new BooleanConnectionProperty(this, "runningCTS13", false, Messages.getString("ConnectionProperties.runningCTS13"), "3.1.7", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1305 */   private IntegerConnectionProperty secondsBeforeRetryMaster = new IntegerConnectionProperty("secondsBeforeRetryMaster", 30, 1, 2147483647, Messages.getString("ConnectionProperties.secondsBeforeRetryMaster"), "3.0.2", HA_CATEGORY, 8);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1313 */   private IntegerConnectionProperty selfDestructOnPingSecondsLifetime = new IntegerConnectionProperty("selfDestructOnPingSecondsLifetime", 0, 0, 2147483647, Messages.getString("ConnectionProperties.selfDestructOnPingSecondsLifetime"), "5.1.6", HA_CATEGORY, 2147483647);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1321 */   private IntegerConnectionProperty selfDestructOnPingMaxOperations = new IntegerConnectionProperty("selfDestructOnPingMaxOperations", 0, 0, 2147483647, Messages.getString("ConnectionProperties.selfDestructOnPingMaxOperations"), "5.1.6", HA_CATEGORY, 2147483647);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1329 */   private StringConnectionProperty serverTimezone = new StringConnectionProperty("serverTimezone", null, Messages.getString("ConnectionProperties.serverTimezone"), "3.0.2", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1335 */   private StringConnectionProperty sessionVariables = new StringConnectionProperty("sessionVariables", null, Messages.getString("ConnectionProperties.sessionVariables"), "3.1.8", MISC_CATEGORY, 2147483647);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1340 */   private IntegerConnectionProperty slowQueryThresholdMillis = new IntegerConnectionProperty("slowQueryThresholdMillis", 2000, 0, 2147483647, Messages.getString("ConnectionProperties.slowQueryThresholdMillis"), "3.1.2", DEBUGING_PROFILING_CATEGORY, 9);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1348 */   private LongConnectionProperty slowQueryThresholdNanos = new LongConnectionProperty("slowQueryThresholdNanos", 0L, Messages.getString("ConnectionProperties.slowQueryThresholdNanos"), "5.0.7", DEBUGING_PROFILING_CATEGORY, 10);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1356 */   private StringConnectionProperty socketFactoryClassName = new StringConnectionProperty("socketFactory", StandardSocketFactory.class.getName(), Messages.getString("ConnectionProperties.socketFactory"), "3.0.3", CONNECTION_AND_AUTH_CATEGORY, 4);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1362 */   private IntegerConnectionProperty socketTimeout = new IntegerConnectionProperty("socketTimeout", 0, 0, 2147483647, Messages.getString("ConnectionProperties.socketTimeout"), "3.0.1", CONNECTION_AND_AUTH_CATEGORY, 10);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1370 */   private StringConnectionProperty statementInterceptors = new StringConnectionProperty("statementInterceptors", null, Messages.getString("ConnectionProperties.statementInterceptors"), "5.1.1", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */   
/* 1373 */   private BooleanConnectionProperty strictFloatingPoint = new BooleanConnectionProperty(this, "strictFloatingPoint", false, Messages.getString("ConnectionProperties.strictFloatingPoint"), "3.0.0", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1378 */   private BooleanConnectionProperty strictUpdates = new BooleanConnectionProperty(this, "strictUpdates", true, Messages.getString("ConnectionProperties.strictUpdates"), "3.0.4", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1384 */   private BooleanConnectionProperty overrideSupportsIntegrityEnhancementFacility = new BooleanConnectionProperty(this, "overrideSupportsIntegrityEnhancementFacility", false, Messages.getString("ConnectionProperties.overrideSupportsIEF"), "3.1.12", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1390 */   private BooleanConnectionProperty tcpNoDelay = new BooleanConnectionProperty(this, "tcpNoDelay", Boolean.valueOf("true").booleanValue(), Messages.getString("ConnectionProperties.tcpNoDelay"), "5.0.7", NETWORK_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1396 */   private BooleanConnectionProperty tcpKeepAlive = new BooleanConnectionProperty(this, "tcpKeepAlive", Boolean.valueOf("true").booleanValue(), Messages.getString("ConnectionProperties.tcpKeepAlive"), "5.0.7", NETWORK_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1402 */   private IntegerConnectionProperty tcpRcvBuf = new IntegerConnectionProperty("tcpRcvBuf", Integer.parseInt("0"), 0, 2147483647, Messages.getString("ConnectionProperties.tcpSoRcvBuf"), "5.0.7", NETWORK_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1409 */   private IntegerConnectionProperty tcpSndBuf = new IntegerConnectionProperty("tcpSndBuf", Integer.parseInt("0"), 0, 2147483647, Messages.getString("ConnectionProperties.tcpSoSndBuf"), "5.0.7", NETWORK_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1416 */   private IntegerConnectionProperty tcpTrafficClass = new IntegerConnectionProperty("tcpTrafficClass", Integer.parseInt("0"), 0, 255, Messages.getString("ConnectionProperties.tcpTrafficClass"), "5.0.7", NETWORK_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1423 */   private BooleanConnectionProperty tinyInt1isBit = new BooleanConnectionProperty(this, "tinyInt1isBit", true, Messages.getString("ConnectionProperties.tinyInt1isBit"), "3.0.16", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1429 */   private BooleanConnectionProperty traceProtocol = new BooleanConnectionProperty(this, "traceProtocol", false, Messages.getString("ConnectionProperties.traceProtocol"), "3.1.2", DEBUGING_PROFILING_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1434 */   private BooleanConnectionProperty treatUtilDateAsTimestamp = new BooleanConnectionProperty(this, "treatUtilDateAsTimestamp", true, Messages.getString("ConnectionProperties.treatUtilDateAsTimestamp"), "5.0.5", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1439 */   private BooleanConnectionProperty transformedBitIsBoolean = new BooleanConnectionProperty(this, "transformedBitIsBoolean", false, Messages.getString("ConnectionProperties.transformedBitIsBoolean"), "3.1.9", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1445 */   private BooleanConnectionProperty useBlobToStoreUTF8OutsideBMP = new BooleanConnectionProperty(this, "useBlobToStoreUTF8OutsideBMP", false, Messages.getString("ConnectionProperties.useBlobToStoreUTF8OutsideBMP"), "5.1.3", MISC_CATEGORY, 128);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1451 */   private StringConnectionProperty utf8OutsideBmpExcludedColumnNamePattern = new StringConnectionProperty("utf8OutsideBmpExcludedColumnNamePattern", null, Messages.getString("ConnectionProperties.utf8OutsideBmpExcludedColumnNamePattern"), "5.1.3", MISC_CATEGORY, 129);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1457 */   private StringConnectionProperty utf8OutsideBmpIncludedColumnNamePattern = new StringConnectionProperty("utf8OutsideBmpIncludedColumnNamePattern", null, Messages.getString("ConnectionProperties.utf8OutsideBmpIncludedColumnNamePattern"), "5.1.3", MISC_CATEGORY, 129);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1463 */   private BooleanConnectionProperty useCompression = new BooleanConnectionProperty(this, "useCompression", false, Messages.getString("ConnectionProperties.useCompression"), "3.0.17", CONNECTION_AND_AUTH_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1469 */   private BooleanConnectionProperty useColumnNamesInFindColumn = new BooleanConnectionProperty(this, "useColumnNamesInFindColumn", false, Messages.getString("ConnectionProperties.useColumnNamesInFindColumn"), "5.1.7", MISC_CATEGORY, 2147483647);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1475 */   private StringConnectionProperty useConfigs = new StringConnectionProperty("useConfigs", null, Messages.getString("ConnectionProperties.useConfigs"), "3.1.5", CONNECTION_AND_AUTH_CATEGORY, 2147483647);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1481 */   private BooleanConnectionProperty useCursorFetch = new BooleanConnectionProperty(this, "useCursorFetch", false, Messages.getString("ConnectionProperties.useCursorFetch"), "5.0.0", PERFORMANCE_CATEGORY, 2147483647);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1487 */   private BooleanConnectionProperty useDynamicCharsetInfo = new BooleanConnectionProperty(this, "useDynamicCharsetInfo", true, Messages.getString("ConnectionProperties.useDynamicCharsetInfo"), "5.0.6", PERFORMANCE_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1493 */   private BooleanConnectionProperty useDirectRowUnpack = new BooleanConnectionProperty(this, "useDirectRowUnpack", true, "Use newer result set row unpacking code that skips a copy from network buffers  to a MySQL packet instance and instead reads directly into the result set row data buffers.", "5.1.1", PERFORMANCE_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1499 */   private BooleanConnectionProperty useFastIntParsing = new BooleanConnectionProperty(this, "useFastIntParsing", true, Messages.getString("ConnectionProperties.useFastIntParsing"), "3.1.4", PERFORMANCE_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1505 */   private BooleanConnectionProperty useFastDateParsing = new BooleanConnectionProperty(this, "useFastDateParsing", true, Messages.getString("ConnectionProperties.useFastDateParsing"), "5.0.5", PERFORMANCE_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1511 */   private BooleanConnectionProperty useHostsInPrivileges = new BooleanConnectionProperty(this, "useHostsInPrivileges", true, Messages.getString("ConnectionProperties.useHostsInPrivileges"), "3.0.2", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1516 */   private BooleanConnectionProperty useInformationSchema = new BooleanConnectionProperty(this, "useInformationSchema", false, Messages.getString("ConnectionProperties.useInformationSchema"), "5.0.0", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1521 */   private BooleanConnectionProperty useJDBCCompliantTimezoneShift = new BooleanConnectionProperty(this, "useJDBCCompliantTimezoneShift", false, Messages.getString("ConnectionProperties.useJDBCCompliantTimezoneShift"), "5.0.0", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1528 */   private BooleanConnectionProperty useLocalSessionState = new BooleanConnectionProperty(this, "useLocalSessionState", false, Messages.getString("ConnectionProperties.useLocalSessionState"), "3.1.7", PERFORMANCE_CATEGORY, 5);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1534 */   private BooleanConnectionProperty useLocalTransactionState = new BooleanConnectionProperty(this, "useLocalTransactionState", false, Messages.getString("ConnectionProperties.useLocalTransactionState"), "5.1.7", PERFORMANCE_CATEGORY, 6);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1540 */   private BooleanConnectionProperty useLegacyDatetimeCode = new BooleanConnectionProperty(this, "useLegacyDatetimeCode", true, Messages.getString("ConnectionProperties.useLegacyDatetimeCode"), "5.1.6", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1546 */   private BooleanConnectionProperty useNanosForElapsedTime = new BooleanConnectionProperty(this, "useNanosForElapsedTime", false, Messages.getString("ConnectionProperties.useNanosForElapsedTime"), "5.0.7", DEBUGING_PROFILING_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1553 */   private BooleanConnectionProperty useOldAliasMetadataBehavior = new BooleanConnectionProperty(this, "useOldAliasMetadataBehavior", false, Messages.getString("ConnectionProperties.useOldAliasMetadataBehavior"), "5.0.4", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1561 */   private BooleanConnectionProperty useOldUTF8Behavior = new BooleanConnectionProperty(this, "useOldUTF8Behavior", false, Messages.getString("ConnectionProperties.useOldUtf8Behavior"), "3.1.6", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean useOldUTF8BehaviorAsBoolean = false;
/*      */ 
/*      */ 
/*      */   
/* 1569 */   private BooleanConnectionProperty useOnlyServerErrorMessages = new BooleanConnectionProperty(this, "useOnlyServerErrorMessages", true, Messages.getString("ConnectionProperties.useOnlyServerErrorMessages"), "3.0.15", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1575 */   private BooleanConnectionProperty useReadAheadInput = new BooleanConnectionProperty(this, "useReadAheadInput", true, Messages.getString("ConnectionProperties.useReadAheadInput"), "3.1.5", PERFORMANCE_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1581 */   private BooleanConnectionProperty useSqlStateCodes = new BooleanConnectionProperty(this, "useSqlStateCodes", true, Messages.getString("ConnectionProperties.useSqlStateCodes"), "3.1.3", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1587 */   private BooleanConnectionProperty useSSL = new BooleanConnectionProperty(this, "useSSL", false, Messages.getString("ConnectionProperties.useSSL"), "3.0.2", SECURITY_CATEGORY, 2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1593 */   private BooleanConnectionProperty useSSPSCompatibleTimezoneShift = new BooleanConnectionProperty(this, "useSSPSCompatibleTimezoneShift", false, Messages.getString("ConnectionProperties.useSSPSCompatibleTimezoneShift"), "5.0.5", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1599 */   private BooleanConnectionProperty useStreamLengthsInPrepStmts = new BooleanConnectionProperty(this, "useStreamLengthsInPrepStmts", true, Messages.getString("ConnectionProperties.useStreamLengthsInPrepStmts"), "3.0.2", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1605 */   private BooleanConnectionProperty useTimezone = new BooleanConnectionProperty(this, "useTimezone", false, Messages.getString("ConnectionProperties.useTimezone"), "3.0.2", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1611 */   private BooleanConnectionProperty useUltraDevWorkAround = new BooleanConnectionProperty(this, "ultraDevHack", false, Messages.getString("ConnectionProperties.ultraDevHack"), "2.0.3", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1617 */   private BooleanConnectionProperty useUnbufferedInput = new BooleanConnectionProperty(this, "useUnbufferedInput", true, Messages.getString("ConnectionProperties.useUnbufferedInput"), "3.0.11", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1622 */   private BooleanConnectionProperty useUnicode = new BooleanConnectionProperty(this, "useUnicode", true, Messages.getString("ConnectionProperties.useUnicode"), "1.1g", MISC_CATEGORY, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean useUnicodeAsBoolean = true;
/*      */ 
/*      */ 
/*      */   
/* 1631 */   private BooleanConnectionProperty useUsageAdvisor = new BooleanConnectionProperty(this, "useUsageAdvisor", false, Messages.getString("ConnectionProperties.useUsageAdvisor"), "3.1.1", DEBUGING_PROFILING_CATEGORY, 10);
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean useUsageAdvisorAsBoolean = false;
/*      */ 
/*      */ 
/*      */   
/* 1639 */   private BooleanConnectionProperty yearIsDateType = new BooleanConnectionProperty(this, "yearIsDateType", true, Messages.getString("ConnectionProperties.yearIsDateType"), "3.1.9", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1645 */   private StringConnectionProperty zeroDateTimeBehavior = new StringConnectionProperty("zeroDateTimeBehavior", "exception", new String[] { "exception", "round", "convertToNull" }, Messages.getString("ConnectionProperties.zeroDateTimeBehavior", new Object[] { "exception", "round", "convertToNull" }), "3.1.4", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1655 */   private BooleanConnectionProperty useJvmCharsetConverters = new BooleanConnectionProperty(this, "useJvmCharsetConverters", false, Messages.getString("ConnectionProperties.useJvmCharsetConverters"), "5.0.1", PERFORMANCE_CATEGORY, -2147483648);
/*      */ 
/*      */   
/* 1658 */   private BooleanConnectionProperty useGmtMillisForDatetimes = new BooleanConnectionProperty(this, "useGmtMillisForDatetimes", false, Messages.getString("ConnectionProperties.useGmtMillisForDatetimes"), "3.1.12", MISC_CATEGORY, -2147483648);
/*      */   
/* 1660 */   private BooleanConnectionProperty dumpMetadataOnColumnNotFound = new BooleanConnectionProperty(this, "dumpMetadataOnColumnNotFound", false, Messages.getString("ConnectionProperties.dumpMetadataOnColumnNotFound"), "3.1.13", DEBUGING_PROFILING_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */   
/* 1664 */   private StringConnectionProperty clientCertificateKeyStoreUrl = new StringConnectionProperty("clientCertificateKeyStoreUrl", null, Messages.getString("ConnectionProperties.clientCertificateKeyStoreUrl"), "5.1.0", SECURITY_CATEGORY, 5);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1669 */   private StringConnectionProperty trustCertificateKeyStoreUrl = new StringConnectionProperty("trustCertificateKeyStoreUrl", null, Messages.getString("ConnectionProperties.trustCertificateKeyStoreUrl"), "5.1.0", SECURITY_CATEGORY, 8);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1674 */   private StringConnectionProperty clientCertificateKeyStoreType = new StringConnectionProperty("clientCertificateKeyStoreType", null, Messages.getString("ConnectionProperties.clientCertificateKeyStoreType"), "5.1.0", SECURITY_CATEGORY, 6);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1679 */   private StringConnectionProperty clientCertificateKeyStorePassword = new StringConnectionProperty("clientCertificateKeyStorePassword", null, Messages.getString("ConnectionProperties.clientCertificateKeyStorePassword"), "5.1.0", SECURITY_CATEGORY, 7);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1684 */   private StringConnectionProperty trustCertificateKeyStoreType = new StringConnectionProperty("trustCertificateKeyStoreType", null, Messages.getString("ConnectionProperties.trustCertificateKeyStoreType"), "5.1.0", SECURITY_CATEGORY, 9);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1689 */   private StringConnectionProperty trustCertificateKeyStorePassword = new StringConnectionProperty("trustCertificateKeyStorePassword", null, Messages.getString("ConnectionProperties.trustCertificateKeyStorePassword"), "5.1.0", SECURITY_CATEGORY, 10);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1694 */   private BooleanConnectionProperty verifyServerCertificate = new BooleanConnectionProperty(this, "verifyServerCertificate", true, Messages.getString("ConnectionProperties.verifyServerCertificate"), "5.1.6", SECURITY_CATEGORY, 4);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1700 */   private BooleanConnectionProperty useAffectedRows = new BooleanConnectionProperty(this, "useAffectedRows", false, Messages.getString("ConnectionProperties.useAffectedRows"), "5.1.7", MISC_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1705 */   private StringConnectionProperty passwordCharacterEncoding = new StringConnectionProperty("passwordCharacterEncoding", null, Messages.getString("ConnectionProperties.passwordCharacterEncoding"), "5.1.7", SECURITY_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1710 */   private IntegerConnectionProperty maxAllowedPacket = new IntegerConnectionProperty("maxAllowedPacket", -1, Messages.getString("ConnectionProperties.maxAllowedPacket"), "5.1.8", NETWORK_CATEGORY, -2147483648);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected DriverPropertyInfo[] exposeAsDriverPropertyInfoInternal(Properties info, int slotsToReserve) throws SQLException {
/* 1716 */     initializeProperties(info);
/*      */     
/* 1718 */     int numProperties = PROPERTY_LIST.size();
/*      */     
/* 1720 */     int listSize = numProperties + slotsToReserve;
/*      */     
/* 1722 */     DriverPropertyInfo[] driverProperties = new DriverPropertyInfo[listSize];
/*      */     
/* 1724 */     for (int i = slotsToReserve; i < listSize; i++) {
/* 1725 */       Field propertyField = PROPERTY_LIST.get(i - slotsToReserve);
/*      */ 
/*      */       
/*      */       try {
/* 1729 */         ConnectionProperty propToExpose = (ConnectionProperty)propertyField.get(this);
/*      */ 
/*      */         
/* 1732 */         if (info != null) {
/* 1733 */           propToExpose.initializeFrom(info);
/*      */         }
/*      */ 
/*      */         
/* 1737 */         driverProperties[i] = propToExpose.getAsDriverPropertyInfo();
/* 1738 */       } catch (IllegalAccessException iae) {
/* 1739 */         throw SQLError.createSQLException(Messages.getString("ConnectionProperties.InternalPropertiesFailure"), "S1000", getExceptionInterceptor());
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1744 */     return driverProperties;
/*      */   }
/*      */ 
/*      */   
/*      */   protected Properties exposeAsProperties(Properties info) throws SQLException {
/* 1749 */     if (info == null) {
/* 1750 */       info = new Properties();
/*      */     }
/*      */     
/* 1753 */     int numPropertiesToSet = PROPERTY_LIST.size();
/*      */     
/* 1755 */     for (int i = 0; i < numPropertiesToSet; i++) {
/* 1756 */       Field propertyField = PROPERTY_LIST.get(i);
/*      */ 
/*      */       
/*      */       try {
/* 1760 */         ConnectionProperty propToGet = (ConnectionProperty)propertyField.get(this);
/*      */ 
/*      */         
/* 1763 */         Object propValue = propToGet.getValueAsObject();
/*      */         
/* 1765 */         if (propValue != null) {
/* 1766 */           info.setProperty(propToGet.getPropertyName(), propValue.toString());
/*      */         }
/*      */       }
/* 1769 */       catch (IllegalAccessException iae) {
/* 1770 */         throw SQLError.createSQLException("Internal properties failure", "S1000", getExceptionInterceptor());
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1775 */     return info;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String exposeAsXml() throws SQLException {
/* 1782 */     StringBuffer xmlBuf = new StringBuffer();
/* 1783 */     xmlBuf.append("<ConnectionProperties>");
/*      */     
/* 1785 */     int numPropertiesToSet = PROPERTY_LIST.size();
/*      */     
/* 1787 */     int numCategories = PROPERTY_CATEGORIES.length;
/*      */     
/* 1789 */     Map propertyListByCategory = new HashMap();
/*      */     
/* 1791 */     for (int i = 0; i < numCategories; i++) {
/* 1792 */       propertyListByCategory.put(PROPERTY_CATEGORIES[i], new Map[] { new TreeMap(), new TreeMap() });
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1802 */     StringConnectionProperty userProp = new StringConnectionProperty("user", null, Messages.getString("ConnectionProperties.Username"), Messages.getString("ConnectionProperties.allVersions"), CONNECTION_AND_AUTH_CATEGORY, -2147483647);
/*      */ 
/*      */ 
/*      */     
/* 1806 */     StringConnectionProperty passwordProp = new StringConnectionProperty("password", null, Messages.getString("ConnectionProperties.Password"), Messages.getString("ConnectionProperties.allVersions"), CONNECTION_AND_AUTH_CATEGORY, -2147483646);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1811 */     Map[] connectionSortMaps = (Map[])propertyListByCategory.get(CONNECTION_AND_AUTH_CATEGORY);
/*      */     
/* 1813 */     TreeMap userMap = new TreeMap();
/* 1814 */     userMap.put(userProp.getPropertyName(), userProp);
/*      */     
/* 1816 */     connectionSortMaps[0].put(new Integer(userProp.getOrder()), userMap);
/*      */     
/* 1818 */     TreeMap passwordMap = new TreeMap();
/* 1819 */     passwordMap.put(passwordProp.getPropertyName(), passwordProp);
/*      */     
/* 1821 */     connectionSortMaps[0].put(new Integer(passwordProp.getOrder()), passwordMap);
/*      */ 
/*      */     
/*      */     try {
/* 1825 */       for (int k = 0; k < numPropertiesToSet; k++) {
/* 1826 */         Field propertyField = PROPERTY_LIST.get(k);
/*      */         
/* 1828 */         ConnectionProperty propToGet = (ConnectionProperty)propertyField.get(this);
/*      */         
/* 1830 */         Map[] sortMaps = (Map[])propertyListByCategory.get(propToGet.getCategoryName());
/*      */         
/* 1832 */         int orderInCategory = propToGet.getOrder();
/*      */         
/* 1834 */         if (orderInCategory == Integer.MIN_VALUE) {
/* 1835 */           sortMaps[1].put(propToGet.getPropertyName(), propToGet);
/*      */         } else {
/* 1837 */           Integer order = new Integer(orderInCategory);
/*      */           
/* 1839 */           Map orderMap = sortMaps[0].get(order);
/*      */           
/* 1841 */           if (orderMap == null) {
/* 1842 */             orderMap = new TreeMap();
/* 1843 */             sortMaps[0].put(order, orderMap);
/*      */           } 
/*      */           
/* 1846 */           orderMap.put(propToGet.getPropertyName(), propToGet);
/*      */         } 
/*      */       } 
/*      */       
/* 1850 */       for (int j = 0; j < numCategories; j++) {
/* 1851 */         Map[] sortMaps = (Map[])propertyListByCategory.get(PROPERTY_CATEGORIES[j]);
/*      */         
/* 1853 */         Iterator orderedIter = sortMaps[0].values().iterator();
/* 1854 */         Iterator alphaIter = sortMaps[1].values().iterator();
/*      */         
/* 1856 */         xmlBuf.append("\n <PropertyCategory name=\"");
/* 1857 */         xmlBuf.append(PROPERTY_CATEGORIES[j]);
/* 1858 */         xmlBuf.append("\">");
/*      */         
/* 1860 */         while (orderedIter.hasNext()) {
/* 1861 */           Iterator orderedAlphaIter = ((Map)orderedIter.next()).values().iterator();
/*      */           
/* 1863 */           while (orderedAlphaIter.hasNext()) {
/* 1864 */             ConnectionProperty propToGet = orderedAlphaIter.next();
/*      */ 
/*      */             
/* 1867 */             xmlBuf.append("\n  <Property name=\"");
/* 1868 */             xmlBuf.append(propToGet.getPropertyName());
/* 1869 */             xmlBuf.append("\" required=\"");
/* 1870 */             xmlBuf.append(propToGet.required ? "Yes" : "No");
/*      */             
/* 1872 */             xmlBuf.append("\" default=\"");
/*      */             
/* 1874 */             if (propToGet.getDefaultValue() != null) {
/* 1875 */               xmlBuf.append(propToGet.getDefaultValue());
/*      */             }
/*      */             
/* 1878 */             xmlBuf.append("\" sortOrder=\"");
/* 1879 */             xmlBuf.append(propToGet.getOrder());
/* 1880 */             xmlBuf.append("\" since=\"");
/* 1881 */             xmlBuf.append(propToGet.sinceVersion);
/* 1882 */             xmlBuf.append("\">\n");
/* 1883 */             xmlBuf.append("    ");
/* 1884 */             xmlBuf.append(propToGet.description);
/* 1885 */             xmlBuf.append("\n  </Property>");
/*      */           } 
/*      */         } 
/*      */         
/* 1889 */         while (alphaIter.hasNext()) {
/* 1890 */           ConnectionProperty propToGet = alphaIter.next();
/*      */ 
/*      */           
/* 1893 */           xmlBuf.append("\n  <Property name=\"");
/* 1894 */           xmlBuf.append(propToGet.getPropertyName());
/* 1895 */           xmlBuf.append("\" required=\"");
/* 1896 */           xmlBuf.append(propToGet.required ? "Yes" : "No");
/*      */           
/* 1898 */           xmlBuf.append("\" default=\"");
/*      */           
/* 1900 */           if (propToGet.getDefaultValue() != null) {
/* 1901 */             xmlBuf.append(propToGet.getDefaultValue());
/*      */           }
/*      */           
/* 1904 */           xmlBuf.append("\" sortOrder=\"alpha\" since=\"");
/* 1905 */           xmlBuf.append(propToGet.sinceVersion);
/* 1906 */           xmlBuf.append("\">\n");
/* 1907 */           xmlBuf.append("    ");
/* 1908 */           xmlBuf.append(propToGet.description);
/* 1909 */           xmlBuf.append("\n  </Property>");
/*      */         } 
/*      */         
/* 1912 */         xmlBuf.append("\n </PropertyCategory>");
/*      */       } 
/* 1914 */     } catch (IllegalAccessException iae) {
/* 1915 */       throw SQLError.createSQLException("Internal properties failure", "S1000", getExceptionInterceptor());
/*      */     } 
/*      */ 
/*      */     
/* 1919 */     xmlBuf.append("\n</ConnectionProperties>");
/*      */     
/* 1921 */     return xmlBuf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getAllowLoadLocalInfile() {
/* 1928 */     return this.allowLoadLocalInfile.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getAllowMultiQueries() {
/* 1935 */     return this.allowMultiQueries.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getAllowNanAndInf() {
/* 1942 */     return this.allowNanAndInf.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getAllowUrlInLocalInfile() {
/* 1949 */     return this.allowUrlInLocalInfile.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getAlwaysSendSetIsolation() {
/* 1956 */     return this.alwaysSendSetIsolation.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getAutoDeserialize() {
/* 1963 */     return this.autoDeserialize.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getAutoGenerateTestcaseScript() {
/* 1970 */     return this.autoGenerateTestcaseScriptAsBoolean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getAutoReconnectForPools() {
/* 1977 */     return this.autoReconnectForPoolsAsBoolean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getBlobSendChunkSize() {
/* 1984 */     return this.blobSendChunkSize.getValueAsInt();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getCacheCallableStatements() {
/* 1991 */     return this.cacheCallableStatements.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getCachePreparedStatements() {
/* 1998 */     return ((Boolean)this.cachePreparedStatements.getValueAsObject()).booleanValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getCacheResultSetMetadata() {
/* 2006 */     return this.cacheResultSetMetaDataAsBoolean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getCacheServerConfiguration() {
/* 2013 */     return this.cacheServerConfiguration.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getCallableStatementCacheSize() {
/* 2020 */     return this.callableStatementCacheSize.getValueAsInt();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getCapitalizeTypeNames() {
/* 2027 */     return this.capitalizeTypeNames.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getCharacterSetResults() {
/* 2034 */     return this.characterSetResults.getValueAsString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getClobberStreamingResults() {
/* 2041 */     return this.clobberStreamingResults.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getClobCharacterEncoding() {
/* 2048 */     return this.clobCharacterEncoding.getValueAsString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getConnectionCollation() {
/* 2055 */     return this.connectionCollation.getValueAsString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getConnectTimeout() {
/* 2062 */     return this.connectTimeout.getValueAsInt();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getContinueBatchOnError() {
/* 2069 */     return this.continueBatchOnError.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getCreateDatabaseIfNotExist() {
/* 2076 */     return this.createDatabaseIfNotExist.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getDefaultFetchSize() {
/* 2083 */     return this.defaultFetchSize.getValueAsInt();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getDontTrackOpenResources() {
/* 2090 */     return this.dontTrackOpenResources.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getDumpQueriesOnException() {
/* 2097 */     return this.dumpQueriesOnException.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getDynamicCalendars() {
/* 2104 */     return this.dynamicCalendars.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getElideSetAutoCommits() {
/* 2111 */     return this.elideSetAutoCommits.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getEmptyStringsConvertToZero() {
/* 2118 */     return this.emptyStringsConvertToZero.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getEmulateLocators() {
/* 2125 */     return this.emulateLocators.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getEmulateUnsupportedPstmts() {
/* 2132 */     return this.emulateUnsupportedPstmts.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getEnablePacketDebug() {
/* 2139 */     return this.enablePacketDebug.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getEncoding() {
/* 2146 */     return this.characterEncodingAsString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getExplainSlowQueries() {
/* 2153 */     return this.explainSlowQueries.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getFailOverReadOnly() {
/* 2160 */     return this.failOverReadOnly.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getGatherPerformanceMetrics() {
/* 2167 */     return this.gatherPerformanceMetrics.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean getHighAvailability() {
/* 2176 */     return this.highAvailabilityAsBoolean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getHoldResultsOpenOverStatementClose() {
/* 2183 */     return this.holdResultsOpenOverStatementClose.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getIgnoreNonTxTables() {
/* 2190 */     return this.ignoreNonTxTables.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getInitialTimeout() {
/* 2197 */     return this.initialTimeout.getValueAsInt();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getInteractiveClient() {
/* 2204 */     return this.isInteractiveClient.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getIsInteractiveClient() {
/* 2211 */     return this.isInteractiveClient.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getJdbcCompliantTruncation() {
/* 2218 */     return this.jdbcCompliantTruncation.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getLocatorFetchBufferSize() {
/* 2225 */     return this.locatorFetchBufferSize.getValueAsInt();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getLogger() {
/* 2232 */     return this.loggerClassName.getValueAsString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getLoggerClassName() {
/* 2239 */     return this.loggerClassName.getValueAsString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getLogSlowQueries() {
/* 2246 */     return this.logSlowQueries.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getMaintainTimeStats() {
/* 2253 */     return this.maintainTimeStatsAsBoolean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxQuerySizeToLog() {
/* 2260 */     return this.maxQuerySizeToLog.getValueAsInt();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxReconnects() {
/* 2267 */     return this.maxReconnects.getValueAsInt();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxRows() {
/* 2274 */     return this.maxRowsAsInt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMetadataCacheSize() {
/* 2281 */     return this.metadataCacheSize.getValueAsInt();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getNoDatetimeStringSync() {
/* 2288 */     return this.noDatetimeStringSync.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getNullCatalogMeansCurrent() {
/* 2295 */     return this.nullCatalogMeansCurrent.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getNullNamePatternMatchesAll() {
/* 2302 */     return this.nullNamePatternMatchesAll.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getPacketDebugBufferSize() {
/* 2309 */     return this.packetDebugBufferSize.getValueAsInt();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getParanoid() {
/* 2316 */     return this.paranoid.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getPedantic() {
/* 2323 */     return this.pedantic.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getPreparedStatementCacheSize() {
/* 2330 */     return ((Integer)this.preparedStatementCacheSize.getValueAsObject()).intValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getPreparedStatementCacheSqlLimit() {
/* 2338 */     return ((Integer)this.preparedStatementCacheSqlLimit.getValueAsObject()).intValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getProfileSql() {
/* 2346 */     return this.profileSQLAsBoolean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getProfileSQL() {
/* 2353 */     return this.profileSQL.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPropertiesTransform() {
/* 2360 */     return this.propertiesTransform.getValueAsString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getQueriesBeforeRetryMaster() {
/* 2367 */     return this.queriesBeforeRetryMaster.getValueAsInt();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getReconnectAtTxEnd() {
/* 2374 */     return this.reconnectTxAtEndAsBoolean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getRelaxAutoCommit() {
/* 2381 */     return this.relaxAutoCommit.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getReportMetricsIntervalMillis() {
/* 2388 */     return this.reportMetricsIntervalMillis.getValueAsInt();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getRequireSSL() {
/* 2395 */     return this.requireSSL.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   protected boolean getRetainStatementAfterResultSetClose() {
/* 2399 */     return this.retainStatementAfterResultSetClose.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getRollbackOnPooledClose() {
/* 2406 */     return this.rollbackOnPooledClose.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getRoundRobinLoadBalance() {
/* 2413 */     return this.roundRobinLoadBalance.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getRunningCTS13() {
/* 2420 */     return this.runningCTS13.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSecondsBeforeRetryMaster() {
/* 2427 */     return this.secondsBeforeRetryMaster.getValueAsInt();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getServerTimezone() {
/* 2434 */     return this.serverTimezone.getValueAsString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSessionVariables() {
/* 2441 */     return this.sessionVariables.getValueAsString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSlowQueryThresholdMillis() {
/* 2448 */     return this.slowQueryThresholdMillis.getValueAsInt();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSocketFactoryClassName() {
/* 2455 */     return this.socketFactoryClassName.getValueAsString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSocketTimeout() {
/* 2462 */     return this.socketTimeout.getValueAsInt();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getStrictFloatingPoint() {
/* 2469 */     return this.strictFloatingPoint.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getStrictUpdates() {
/* 2476 */     return this.strictUpdates.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getTinyInt1isBit() {
/* 2483 */     return this.tinyInt1isBit.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getTraceProtocol() {
/* 2490 */     return this.traceProtocol.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getTransformedBitIsBoolean() {
/* 2497 */     return this.transformedBitIsBoolean.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseCompression() {
/* 2504 */     return this.useCompression.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseFastIntParsing() {
/* 2511 */     return this.useFastIntParsing.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseHostsInPrivileges() {
/* 2518 */     return this.useHostsInPrivileges.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseInformationSchema() {
/* 2525 */     return this.useInformationSchema.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseLocalSessionState() {
/* 2532 */     return this.useLocalSessionState.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseOldUTF8Behavior() {
/* 2539 */     return this.useOldUTF8BehaviorAsBoolean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseOnlyServerErrorMessages() {
/* 2546 */     return this.useOnlyServerErrorMessages.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseReadAheadInput() {
/* 2553 */     return this.useReadAheadInput.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseServerPreparedStmts() {
/* 2560 */     return this.detectServerPreparedStmts.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseSqlStateCodes() {
/* 2567 */     return this.useSqlStateCodes.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseSSL() {
/* 2574 */     return this.useSSL.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseStreamLengthsInPrepStmts() {
/* 2581 */     return this.useStreamLengthsInPrepStmts.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseTimezone() {
/* 2588 */     return this.useTimezone.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseUltraDevWorkAround() {
/* 2595 */     return this.useUltraDevWorkAround.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseUnbufferedInput() {
/* 2602 */     return this.useUnbufferedInput.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseUnicode() {
/* 2609 */     return this.useUnicodeAsBoolean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseUsageAdvisor() {
/* 2616 */     return this.useUsageAdvisorAsBoolean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getYearIsDateType() {
/* 2623 */     return this.yearIsDateType.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getZeroDateTimeBehavior() {
/* 2630 */     return this.zeroDateTimeBehavior.getValueAsString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initializeFromRef(Reference ref) throws SQLException {
/* 2644 */     int numPropertiesToSet = PROPERTY_LIST.size();
/*      */     
/* 2646 */     for (int i = 0; i < numPropertiesToSet; i++) {
/* 2647 */       Field propertyField = PROPERTY_LIST.get(i);
/*      */ 
/*      */       
/*      */       try {
/* 2651 */         ConnectionProperty propToSet = (ConnectionProperty)propertyField.get(this);
/*      */ 
/*      */         
/* 2654 */         if (ref != null) {
/* 2655 */           propToSet.initializeFrom(ref);
/*      */         }
/* 2657 */       } catch (IllegalAccessException iae) {
/* 2658 */         throw SQLError.createSQLException("Internal properties failure", "S1000", getExceptionInterceptor());
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 2663 */     postInitialization();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initializeProperties(Properties info) throws SQLException {
/* 2676 */     if (info != null) {
/*      */       
/* 2678 */       String profileSqlLc = info.getProperty("profileSql");
/*      */       
/* 2680 */       if (profileSqlLc != null) {
/* 2681 */         info.put("profileSQL", profileSqlLc);
/*      */       }
/*      */       
/* 2684 */       Properties infoCopy = (Properties)info.clone();
/*      */       
/* 2686 */       infoCopy.remove("HOST");
/* 2687 */       infoCopy.remove("user");
/* 2688 */       infoCopy.remove("password");
/* 2689 */       infoCopy.remove("DBNAME");
/* 2690 */       infoCopy.remove("PORT");
/* 2691 */       infoCopy.remove("profileSql");
/*      */       
/* 2693 */       int numPropertiesToSet = PROPERTY_LIST.size();
/*      */       
/* 2695 */       for (int i = 0; i < numPropertiesToSet; i++) {
/* 2696 */         Field propertyField = PROPERTY_LIST.get(i);
/*      */ 
/*      */         
/*      */         try {
/* 2700 */           ConnectionProperty propToSet = (ConnectionProperty)propertyField.get(this);
/*      */ 
/*      */           
/* 2703 */           propToSet.initializeFrom(infoCopy);
/* 2704 */         } catch (IllegalAccessException iae) {
/* 2705 */           throw SQLError.createSQLException(Messages.getString("ConnectionProperties.unableToInitDriverProperties") + iae.toString(), "S1000", getExceptionInterceptor());
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2712 */       postInitialization();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void postInitialization() throws SQLException {
/* 2719 */     if (this.profileSql.getValueAsObject() != null) {
/* 2720 */       this.profileSQL.initializeFrom(this.profileSql.getValueAsObject().toString());
/*      */     }
/*      */ 
/*      */     
/* 2724 */     this.reconnectTxAtEndAsBoolean = ((Boolean)this.reconnectAtTxEnd.getValueAsObject()).booleanValue();
/*      */ 
/*      */ 
/*      */     
/* 2728 */     if (getMaxRows() == 0)
/*      */     {
/*      */       
/* 2731 */       this.maxRows.setValueAsObject(Constants.integerValueOf(-1));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2737 */     String testEncoding = getEncoding();
/*      */     
/* 2739 */     if (testEncoding != null) {
/*      */       
/*      */       try {
/*      */         
/* 2743 */         String testString = "abc";
/* 2744 */         testString.getBytes(testEncoding);
/* 2745 */       } catch (UnsupportedEncodingException UE) {
/* 2746 */         throw SQLError.createSQLException(Messages.getString("ConnectionProperties.unsupportedCharacterEncoding", new Object[] { testEncoding }), "0S100", getExceptionInterceptor());
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2755 */     if (((Boolean)this.cacheResultSetMetadata.getValueAsObject()).booleanValue()) {
/*      */       
/*      */       try {
/* 2758 */         Class.forName("java.util.LinkedHashMap");
/* 2759 */       } catch (ClassNotFoundException cnfe) {
/* 2760 */         this.cacheResultSetMetadata.setValue(false);
/*      */       } 
/*      */     }
/*      */     
/* 2764 */     this.cacheResultSetMetaDataAsBoolean = this.cacheResultSetMetadata.getValueAsBoolean();
/*      */     
/* 2766 */     this.useUnicodeAsBoolean = this.useUnicode.getValueAsBoolean();
/* 2767 */     this.characterEncodingAsString = (String)this.characterEncoding.getValueAsObject();
/*      */     
/* 2769 */     this.highAvailabilityAsBoolean = this.autoReconnect.getValueAsBoolean();
/* 2770 */     this.autoReconnectForPoolsAsBoolean = this.autoReconnectForPools.getValueAsBoolean();
/*      */     
/* 2772 */     this.maxRowsAsInt = ((Integer)this.maxRows.getValueAsObject()).intValue();
/*      */     
/* 2774 */     this.profileSQLAsBoolean = this.profileSQL.getValueAsBoolean();
/* 2775 */     this.useUsageAdvisorAsBoolean = this.useUsageAdvisor.getValueAsBoolean();
/*      */     
/* 2777 */     this.useOldUTF8BehaviorAsBoolean = this.useOldUTF8Behavior.getValueAsBoolean();
/*      */     
/* 2779 */     this.autoGenerateTestcaseScriptAsBoolean = this.autoGenerateTestcaseScript.getValueAsBoolean();
/*      */     
/* 2781 */     this.maintainTimeStatsAsBoolean = this.maintainTimeStats.getValueAsBoolean();
/*      */     
/* 2783 */     this.jdbcCompliantTruncationForReads = getJdbcCompliantTruncation();
/*      */     
/* 2785 */     if (getUseCursorFetch())
/*      */     {
/*      */       
/* 2788 */       setDetectServerPreparedStmts(true);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllowLoadLocalInfile(boolean property) {
/* 2796 */     this.allowLoadLocalInfile.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllowMultiQueries(boolean property) {
/* 2803 */     this.allowMultiQueries.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllowNanAndInf(boolean flag) {
/* 2810 */     this.allowNanAndInf.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllowUrlInLocalInfile(boolean flag) {
/* 2817 */     this.allowUrlInLocalInfile.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAlwaysSendSetIsolation(boolean flag) {
/* 2824 */     this.alwaysSendSetIsolation.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAutoDeserialize(boolean flag) {
/* 2831 */     this.autoDeserialize.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAutoGenerateTestcaseScript(boolean flag) {
/* 2838 */     this.autoGenerateTestcaseScript.setValue(flag);
/* 2839 */     this.autoGenerateTestcaseScriptAsBoolean = this.autoGenerateTestcaseScript.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAutoReconnect(boolean flag) {
/* 2847 */     this.autoReconnect.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAutoReconnectForConnectionPools(boolean property) {
/* 2854 */     this.autoReconnectForPools.setValue(property);
/* 2855 */     this.autoReconnectForPoolsAsBoolean = this.autoReconnectForPools.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAutoReconnectForPools(boolean flag) {
/* 2863 */     this.autoReconnectForPools.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBlobSendChunkSize(String value) throws SQLException {
/* 2870 */     this.blobSendChunkSize.setValue(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCacheCallableStatements(boolean flag) {
/* 2877 */     this.cacheCallableStatements.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCachePreparedStatements(boolean flag) {
/* 2884 */     this.cachePreparedStatements.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCacheResultSetMetadata(boolean property) {
/* 2891 */     this.cacheResultSetMetadata.setValue(property);
/* 2892 */     this.cacheResultSetMetaDataAsBoolean = this.cacheResultSetMetadata.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCacheServerConfiguration(boolean flag) {
/* 2900 */     this.cacheServerConfiguration.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCallableStatementCacheSize(int size) {
/* 2907 */     this.callableStatementCacheSize.setValue(size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCapitalizeDBMDTypes(boolean property) {
/* 2914 */     this.capitalizeTypeNames.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCapitalizeTypeNames(boolean flag) {
/* 2921 */     this.capitalizeTypeNames.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCharacterEncoding(String encoding) {
/* 2928 */     this.characterEncoding.setValue(encoding);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCharacterSetResults(String characterSet) {
/* 2935 */     this.characterSetResults.setValue(characterSet);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClobberStreamingResults(boolean flag) {
/* 2942 */     this.clobberStreamingResults.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClobCharacterEncoding(String encoding) {
/* 2949 */     this.clobCharacterEncoding.setValue(encoding);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConnectionCollation(String collation) {
/* 2956 */     this.connectionCollation.setValue(collation);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConnectTimeout(int timeoutMs) {
/* 2963 */     this.connectTimeout.setValue(timeoutMs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setContinueBatchOnError(boolean property) {
/* 2970 */     this.continueBatchOnError.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCreateDatabaseIfNotExist(boolean flag) {
/* 2977 */     this.createDatabaseIfNotExist.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDefaultFetchSize(int n) {
/* 2984 */     this.defaultFetchSize.setValue(n);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDetectServerPreparedStmts(boolean property) {
/* 2991 */     this.detectServerPreparedStmts.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDontTrackOpenResources(boolean flag) {
/* 2998 */     this.dontTrackOpenResources.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDumpQueriesOnException(boolean flag) {
/* 3005 */     this.dumpQueriesOnException.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDynamicCalendars(boolean flag) {
/* 3012 */     this.dynamicCalendars.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setElideSetAutoCommits(boolean flag) {
/* 3019 */     this.elideSetAutoCommits.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEmptyStringsConvertToZero(boolean flag) {
/* 3026 */     this.emptyStringsConvertToZero.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEmulateLocators(boolean property) {
/* 3033 */     this.emulateLocators.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEmulateUnsupportedPstmts(boolean flag) {
/* 3040 */     this.emulateUnsupportedPstmts.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEnablePacketDebug(boolean flag) {
/* 3047 */     this.enablePacketDebug.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEncoding(String property) {
/* 3054 */     this.characterEncoding.setValue(property);
/* 3055 */     this.characterEncodingAsString = this.characterEncoding.getValueAsString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExplainSlowQueries(boolean flag) {
/* 3063 */     this.explainSlowQueries.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFailOverReadOnly(boolean flag) {
/* 3070 */     this.failOverReadOnly.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setGatherPerformanceMetrics(boolean flag) {
/* 3077 */     this.gatherPerformanceMetrics.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setHighAvailability(boolean property) {
/* 3086 */     this.autoReconnect.setValue(property);
/* 3087 */     this.highAvailabilityAsBoolean = this.autoReconnect.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHoldResultsOpenOverStatementClose(boolean flag) {
/* 3094 */     this.holdResultsOpenOverStatementClose.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIgnoreNonTxTables(boolean property) {
/* 3101 */     this.ignoreNonTxTables.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInitialTimeout(int property) {
/* 3108 */     this.initialTimeout.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsInteractiveClient(boolean property) {
/* 3115 */     this.isInteractiveClient.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setJdbcCompliantTruncation(boolean flag) {
/* 3122 */     this.jdbcCompliantTruncation.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLocatorFetchBufferSize(String value) throws SQLException {
/* 3129 */     this.locatorFetchBufferSize.setValue(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLogger(String property) {
/* 3136 */     this.loggerClassName.setValueAsObject(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLoggerClassName(String className) {
/* 3143 */     this.loggerClassName.setValue(className);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLogSlowQueries(boolean flag) {
/* 3150 */     this.logSlowQueries.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaintainTimeStats(boolean flag) {
/* 3157 */     this.maintainTimeStats.setValue(flag);
/* 3158 */     this.maintainTimeStatsAsBoolean = this.maintainTimeStats.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaxQuerySizeToLog(int sizeInBytes) {
/* 3166 */     this.maxQuerySizeToLog.setValue(sizeInBytes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaxReconnects(int property) {
/* 3173 */     this.maxReconnects.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaxRows(int property) {
/* 3180 */     this.maxRows.setValue(property);
/* 3181 */     this.maxRowsAsInt = this.maxRows.getValueAsInt();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMetadataCacheSize(int value) {
/* 3188 */     this.metadataCacheSize.setValue(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNoDatetimeStringSync(boolean flag) {
/* 3195 */     this.noDatetimeStringSync.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNullCatalogMeansCurrent(boolean value) {
/* 3202 */     this.nullCatalogMeansCurrent.setValue(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNullNamePatternMatchesAll(boolean value) {
/* 3209 */     this.nullNamePatternMatchesAll.setValue(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPacketDebugBufferSize(int size) {
/* 3216 */     this.packetDebugBufferSize.setValue(size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setParanoid(boolean property) {
/* 3223 */     this.paranoid.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPedantic(boolean property) {
/* 3230 */     this.pedantic.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPreparedStatementCacheSize(int cacheSize) {
/* 3237 */     this.preparedStatementCacheSize.setValue(cacheSize);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPreparedStatementCacheSqlLimit(int cacheSqlLimit) {
/* 3244 */     this.preparedStatementCacheSqlLimit.setValue(cacheSqlLimit);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setProfileSql(boolean property) {
/* 3251 */     this.profileSQL.setValue(property);
/* 3252 */     this.profileSQLAsBoolean = this.profileSQL.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setProfileSQL(boolean flag) {
/* 3259 */     this.profileSQL.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPropertiesTransform(String value) {
/* 3266 */     this.propertiesTransform.setValue(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setQueriesBeforeRetryMaster(int property) {
/* 3273 */     this.queriesBeforeRetryMaster.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setReconnectAtTxEnd(boolean property) {
/* 3280 */     this.reconnectAtTxEnd.setValue(property);
/* 3281 */     this.reconnectTxAtEndAsBoolean = this.reconnectAtTxEnd.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRelaxAutoCommit(boolean property) {
/* 3289 */     this.relaxAutoCommit.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setReportMetricsIntervalMillis(int millis) {
/* 3296 */     this.reportMetricsIntervalMillis.setValue(millis);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRequireSSL(boolean property) {
/* 3303 */     this.requireSSL.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRetainStatementAfterResultSetClose(boolean flag) {
/* 3310 */     this.retainStatementAfterResultSetClose.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRollbackOnPooledClose(boolean flag) {
/* 3317 */     this.rollbackOnPooledClose.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRoundRobinLoadBalance(boolean flag) {
/* 3324 */     this.roundRobinLoadBalance.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRunningCTS13(boolean flag) {
/* 3331 */     this.runningCTS13.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSecondsBeforeRetryMaster(int property) {
/* 3338 */     this.secondsBeforeRetryMaster.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setServerTimezone(String property) {
/* 3345 */     this.serverTimezone.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSessionVariables(String variables) {
/* 3352 */     this.sessionVariables.setValue(variables);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSlowQueryThresholdMillis(int millis) {
/* 3359 */     this.slowQueryThresholdMillis.setValue(millis);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSocketFactoryClassName(String property) {
/* 3366 */     this.socketFactoryClassName.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSocketTimeout(int property) {
/* 3373 */     this.socketTimeout.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setStrictFloatingPoint(boolean property) {
/* 3380 */     this.strictFloatingPoint.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setStrictUpdates(boolean property) {
/* 3387 */     this.strictUpdates.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTinyInt1isBit(boolean flag) {
/* 3394 */     this.tinyInt1isBit.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTraceProtocol(boolean flag) {
/* 3401 */     this.traceProtocol.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTransformedBitIsBoolean(boolean flag) {
/* 3408 */     this.transformedBitIsBoolean.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseCompression(boolean property) {
/* 3415 */     this.useCompression.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseFastIntParsing(boolean flag) {
/* 3422 */     this.useFastIntParsing.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseHostsInPrivileges(boolean property) {
/* 3429 */     this.useHostsInPrivileges.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseInformationSchema(boolean flag) {
/* 3436 */     this.useInformationSchema.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseLocalSessionState(boolean flag) {
/* 3443 */     this.useLocalSessionState.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseOldUTF8Behavior(boolean flag) {
/* 3450 */     this.useOldUTF8Behavior.setValue(flag);
/* 3451 */     this.useOldUTF8BehaviorAsBoolean = this.useOldUTF8Behavior.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseOnlyServerErrorMessages(boolean flag) {
/* 3459 */     this.useOnlyServerErrorMessages.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseReadAheadInput(boolean flag) {
/* 3466 */     this.useReadAheadInput.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseServerPreparedStmts(boolean flag) {
/* 3473 */     this.detectServerPreparedStmts.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseSqlStateCodes(boolean flag) {
/* 3480 */     this.useSqlStateCodes.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseSSL(boolean property) {
/* 3487 */     this.useSSL.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseStreamLengthsInPrepStmts(boolean property) {
/* 3494 */     this.useStreamLengthsInPrepStmts.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseTimezone(boolean property) {
/* 3501 */     this.useTimezone.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseUltraDevWorkAround(boolean property) {
/* 3508 */     this.useUltraDevWorkAround.setValue(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseUnbufferedInput(boolean flag) {
/* 3515 */     this.useUnbufferedInput.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseUnicode(boolean flag) {
/* 3522 */     this.useUnicode.setValue(flag);
/* 3523 */     this.useUnicodeAsBoolean = this.useUnicode.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseUsageAdvisor(boolean useUsageAdvisorFlag) {
/* 3530 */     this.useUsageAdvisor.setValue(useUsageAdvisorFlag);
/* 3531 */     this.useUsageAdvisorAsBoolean = this.useUsageAdvisor.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setYearIsDateType(boolean flag) {
/* 3539 */     this.yearIsDateType.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setZeroDateTimeBehavior(String behavior) {
/* 3546 */     this.zeroDateTimeBehavior.setValue(behavior);
/*      */   }
/*      */   
/*      */   protected void storeToRef(Reference ref) throws SQLException {
/* 3550 */     int numPropertiesToSet = PROPERTY_LIST.size();
/*      */     
/* 3552 */     for (int i = 0; i < numPropertiesToSet; i++) {
/* 3553 */       Field propertyField = PROPERTY_LIST.get(i);
/*      */ 
/*      */       
/*      */       try {
/* 3557 */         ConnectionProperty propToStore = (ConnectionProperty)propertyField.get(this);
/*      */ 
/*      */         
/* 3560 */         if (ref != null) {
/* 3561 */           propToStore.storeTo(ref);
/*      */         }
/* 3563 */       } catch (IllegalAccessException iae) {
/* 3564 */         throw SQLError.createSQLException(Messages.getString("ConnectionProperties.errorNotExpected"), getExceptionInterceptor());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean useUnbufferedInput() {
/* 3573 */     return this.useUnbufferedInput.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseCursorFetch() {
/* 3580 */     return this.useCursorFetch.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseCursorFetch(boolean flag) {
/* 3587 */     this.useCursorFetch.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getOverrideSupportsIntegrityEnhancementFacility() {
/* 3594 */     return this.overrideSupportsIntegrityEnhancementFacility.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOverrideSupportsIntegrityEnhancementFacility(boolean flag) {
/* 3601 */     this.overrideSupportsIntegrityEnhancementFacility.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getNoTimezoneConversionForTimeType() {
/* 3608 */     return this.noTimezoneConversionForTimeType.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNoTimezoneConversionForTimeType(boolean flag) {
/* 3615 */     this.noTimezoneConversionForTimeType.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseJDBCCompliantTimezoneShift() {
/* 3622 */     return this.useJDBCCompliantTimezoneShift.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseJDBCCompliantTimezoneShift(boolean flag) {
/* 3629 */     this.useJDBCCompliantTimezoneShift.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getAutoClosePStmtStreams() {
/* 3636 */     return this.autoClosePStmtStreams.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAutoClosePStmtStreams(boolean flag) {
/* 3643 */     this.autoClosePStmtStreams.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getProcessEscapeCodesForPrepStmts() {
/* 3650 */     return this.processEscapeCodesForPrepStmts.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setProcessEscapeCodesForPrepStmts(boolean flag) {
/* 3657 */     this.processEscapeCodesForPrepStmts.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseGmtMillisForDatetimes() {
/* 3664 */     return this.useGmtMillisForDatetimes.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseGmtMillisForDatetimes(boolean flag) {
/* 3671 */     this.useGmtMillisForDatetimes.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getDumpMetadataOnColumnNotFound() {
/* 3678 */     return this.dumpMetadataOnColumnNotFound.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDumpMetadataOnColumnNotFound(boolean flag) {
/* 3685 */     this.dumpMetadataOnColumnNotFound.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getResourceId() {
/* 3692 */     return this.resourceId.getValueAsString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setResourceId(String resourceId) {
/* 3699 */     this.resourceId.setValue(resourceId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getRewriteBatchedStatements() {
/* 3706 */     return this.rewriteBatchedStatements.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRewriteBatchedStatements(boolean flag) {
/* 3713 */     this.rewriteBatchedStatements.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getJdbcCompliantTruncationForReads() {
/* 3720 */     return this.jdbcCompliantTruncationForReads;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setJdbcCompliantTruncationForReads(boolean jdbcCompliantTruncationForReads) {
/* 3728 */     this.jdbcCompliantTruncationForReads = jdbcCompliantTruncationForReads;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseJvmCharsetConverters() {
/* 3735 */     return this.useJvmCharsetConverters.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseJvmCharsetConverters(boolean flag) {
/* 3742 */     this.useJvmCharsetConverters.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getPinGlobalTxToPhysicalConnection() {
/* 3749 */     return this.pinGlobalTxToPhysicalConnection.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPinGlobalTxToPhysicalConnection(boolean flag) {
/* 3756 */     this.pinGlobalTxToPhysicalConnection.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setGatherPerfMetrics(boolean flag) {
/* 3768 */     setGatherPerformanceMetrics(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getGatherPerfMetrics() {
/* 3775 */     return getGatherPerformanceMetrics();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUltraDevHack(boolean flag) {
/* 3782 */     setUseUltraDevWorkAround(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUltraDevHack() {
/* 3789 */     return getUseUltraDevWorkAround();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInteractiveClient(boolean property) {
/* 3796 */     setIsInteractiveClient(property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSocketFactory(String name) {
/* 3803 */     setSocketFactoryClassName(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSocketFactory() {
/* 3810 */     return getSocketFactoryClassName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseServerPrepStmts(boolean flag) {
/* 3817 */     setUseServerPreparedStmts(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseServerPrepStmts() {
/* 3824 */     return getUseServerPreparedStmts();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCacheCallableStmts(boolean flag) {
/* 3831 */     setCacheCallableStatements(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getCacheCallableStmts() {
/* 3838 */     return getCacheCallableStatements();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCachePrepStmts(boolean flag) {
/* 3845 */     setCachePreparedStatements(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getCachePrepStmts() {
/* 3852 */     return getCachePreparedStatements();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCallableStmtCacheSize(int cacheSize) {
/* 3859 */     setCallableStatementCacheSize(cacheSize);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getCallableStmtCacheSize() {
/* 3866 */     return getCallableStatementCacheSize();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPrepStmtCacheSize(int cacheSize) {
/* 3873 */     setPreparedStatementCacheSize(cacheSize);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getPrepStmtCacheSize() {
/* 3880 */     return getPreparedStatementCacheSize();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPrepStmtCacheSqlLimit(int sqlLimit) {
/* 3887 */     setPreparedStatementCacheSqlLimit(sqlLimit);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getPrepStmtCacheSqlLimit() {
/* 3894 */     return getPreparedStatementCacheSqlLimit();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getNoAccessToProcedureBodies() {
/* 3901 */     return this.noAccessToProcedureBodies.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNoAccessToProcedureBodies(boolean flag) {
/* 3908 */     this.noAccessToProcedureBodies.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseOldAliasMetadataBehavior() {
/* 3915 */     return this.useOldAliasMetadataBehavior.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseOldAliasMetadataBehavior(boolean flag) {
/* 3922 */     this.useOldAliasMetadataBehavior.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getClientCertificateKeyStorePassword() {
/* 3929 */     return this.clientCertificateKeyStorePassword.getValueAsString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClientCertificateKeyStorePassword(String value) {
/* 3937 */     this.clientCertificateKeyStorePassword.setValue(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getClientCertificateKeyStoreType() {
/* 3944 */     return this.clientCertificateKeyStoreType.getValueAsString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClientCertificateKeyStoreType(String value) {
/* 3952 */     this.clientCertificateKeyStoreType.setValue(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getClientCertificateKeyStoreUrl() {
/* 3959 */     return this.clientCertificateKeyStoreUrl.getValueAsString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClientCertificateKeyStoreUrl(String value) {
/* 3967 */     this.clientCertificateKeyStoreUrl.setValue(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTrustCertificateKeyStorePassword() {
/* 3974 */     return this.trustCertificateKeyStorePassword.getValueAsString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTrustCertificateKeyStorePassword(String value) {
/* 3982 */     this.trustCertificateKeyStorePassword.setValue(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTrustCertificateKeyStoreType() {
/* 3989 */     return this.trustCertificateKeyStoreType.getValueAsString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTrustCertificateKeyStoreType(String value) {
/* 3997 */     this.trustCertificateKeyStoreType.setValue(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTrustCertificateKeyStoreUrl() {
/* 4004 */     return this.trustCertificateKeyStoreUrl.getValueAsString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTrustCertificateKeyStoreUrl(String value) {
/* 4012 */     this.trustCertificateKeyStoreUrl.setValue(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseSSPSCompatibleTimezoneShift() {
/* 4019 */     return this.useSSPSCompatibleTimezoneShift.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseSSPSCompatibleTimezoneShift(boolean flag) {
/* 4026 */     this.useSSPSCompatibleTimezoneShift.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getTreatUtilDateAsTimestamp() {
/* 4033 */     return this.treatUtilDateAsTimestamp.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTreatUtilDateAsTimestamp(boolean flag) {
/* 4040 */     this.treatUtilDateAsTimestamp.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseFastDateParsing() {
/* 4047 */     return this.useFastDateParsing.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseFastDateParsing(boolean flag) {
/* 4054 */     this.useFastDateParsing.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getLocalSocketAddress() {
/* 4061 */     return this.localSocketAddress.getValueAsString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLocalSocketAddress(String address) {
/* 4068 */     this.localSocketAddress.setValue(address);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseConfigs(String configs) {
/* 4075 */     this.useConfigs.setValue(configs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getUseConfigs() {
/* 4082 */     return this.useConfigs.getValueAsString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getGenerateSimpleParameterMetadata() {
/* 4090 */     return this.generateSimpleParameterMetadata.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setGenerateSimpleParameterMetadata(boolean flag) {
/* 4097 */     this.generateSimpleParameterMetadata.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getLogXaCommands() {
/* 4104 */     return this.logXaCommands.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLogXaCommands(boolean flag) {
/* 4111 */     this.logXaCommands.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getResultSetSizeThreshold() {
/* 4118 */     return this.resultSetSizeThreshold.getValueAsInt();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setResultSetSizeThreshold(int threshold) {
/* 4125 */     this.resultSetSizeThreshold.setValue(threshold);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getNetTimeoutForStreamingResults() {
/* 4132 */     return this.netTimeoutForStreamingResults.getValueAsInt();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNetTimeoutForStreamingResults(int value) {
/* 4139 */     this.netTimeoutForStreamingResults.setValue(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getEnableQueryTimeouts() {
/* 4146 */     return this.enableQueryTimeouts.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEnableQueryTimeouts(boolean flag) {
/* 4153 */     this.enableQueryTimeouts.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getPadCharsWithSpace() {
/* 4160 */     return this.padCharsWithSpace.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPadCharsWithSpace(boolean flag) {
/* 4167 */     this.padCharsWithSpace.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseDynamicCharsetInfo() {
/* 4174 */     return this.useDynamicCharsetInfo.getValueAsBoolean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseDynamicCharsetInfo(boolean flag) {
/* 4181 */     this.useDynamicCharsetInfo.setValue(flag);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getClientInfoProvider() {
/* 4188 */     return this.clientInfoProvider.getValueAsString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClientInfoProvider(String classname) {
/* 4195 */     this.clientInfoProvider.setValue(classname);
/*      */   }
/*      */   
/*      */   public boolean getPopulateInsertRowWithDefaultValues() {
/* 4199 */     return this.populateInsertRowWithDefaultValues.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setPopulateInsertRowWithDefaultValues(boolean flag) {
/* 4203 */     this.populateInsertRowWithDefaultValues.setValue(flag);
/*      */   }
/*      */   
/*      */   public String getLoadBalanceStrategy() {
/* 4207 */     return this.loadBalanceStrategy.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setLoadBalanceStrategy(String strategy) {
/* 4211 */     this.loadBalanceStrategy.setValue(strategy);
/*      */   }
/*      */   
/*      */   public boolean getTcpNoDelay() {
/* 4215 */     return this.tcpNoDelay.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setTcpNoDelay(boolean flag) {
/* 4219 */     this.tcpNoDelay.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getTcpKeepAlive() {
/* 4223 */     return this.tcpKeepAlive.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setTcpKeepAlive(boolean flag) {
/* 4227 */     this.tcpKeepAlive.setValue(flag);
/*      */   }
/*      */   
/*      */   public int getTcpRcvBuf() {
/* 4231 */     return this.tcpRcvBuf.getValueAsInt();
/*      */   }
/*      */   
/*      */   public void setTcpRcvBuf(int bufSize) {
/* 4235 */     this.tcpRcvBuf.setValue(bufSize);
/*      */   }
/*      */   
/*      */   public int getTcpSndBuf() {
/* 4239 */     return this.tcpSndBuf.getValueAsInt();
/*      */   }
/*      */   
/*      */   public void setTcpSndBuf(int bufSize) {
/* 4243 */     this.tcpSndBuf.setValue(bufSize);
/*      */   }
/*      */   
/*      */   public int getTcpTrafficClass() {
/* 4247 */     return this.tcpTrafficClass.getValueAsInt();
/*      */   }
/*      */   
/*      */   public void setTcpTrafficClass(int classFlags) {
/* 4251 */     this.tcpTrafficClass.setValue(classFlags);
/*      */   }
/*      */   
/*      */   public boolean getUseNanosForElapsedTime() {
/* 4255 */     return this.useNanosForElapsedTime.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setUseNanosForElapsedTime(boolean flag) {
/* 4259 */     this.useNanosForElapsedTime.setValue(flag);
/*      */   }
/*      */   
/*      */   public long getSlowQueryThresholdNanos() {
/* 4263 */     return this.slowQueryThresholdNanos.getValueAsLong();
/*      */   }
/*      */   
/*      */   public void setSlowQueryThresholdNanos(long nanos) {
/* 4267 */     this.slowQueryThresholdNanos.setValue(nanos);
/*      */   }
/*      */   
/*      */   public String getStatementInterceptors() {
/* 4271 */     return this.statementInterceptors.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setStatementInterceptors(String value) {
/* 4275 */     this.statementInterceptors.setValue(value);
/*      */   }
/*      */   
/*      */   public boolean getUseDirectRowUnpack() {
/* 4279 */     return this.useDirectRowUnpack.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setUseDirectRowUnpack(boolean flag) {
/* 4283 */     this.useDirectRowUnpack.setValue(flag);
/*      */   }
/*      */   
/*      */   public String getLargeRowSizeThreshold() {
/* 4287 */     return this.largeRowSizeThreshold.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setLargeRowSizeThreshold(String value) {
/*      */     try {
/* 4292 */       this.largeRowSizeThreshold.setValue(value);
/* 4293 */     } catch (SQLException sqlEx) {
/* 4294 */       RuntimeException ex = new RuntimeException(sqlEx.getMessage());
/* 4295 */       ex.initCause(sqlEx);
/*      */       
/* 4297 */       throw ex;
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean getUseBlobToStoreUTF8OutsideBMP() {
/* 4302 */     return this.useBlobToStoreUTF8OutsideBMP.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setUseBlobToStoreUTF8OutsideBMP(boolean flag) {
/* 4306 */     this.useBlobToStoreUTF8OutsideBMP.setValue(flag);
/*      */   }
/*      */   
/*      */   public String getUtf8OutsideBmpExcludedColumnNamePattern() {
/* 4310 */     return this.utf8OutsideBmpExcludedColumnNamePattern.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setUtf8OutsideBmpExcludedColumnNamePattern(String regexPattern) {
/* 4314 */     this.utf8OutsideBmpExcludedColumnNamePattern.setValue(regexPattern);
/*      */   }
/*      */   
/*      */   public String getUtf8OutsideBmpIncludedColumnNamePattern() {
/* 4318 */     return this.utf8OutsideBmpIncludedColumnNamePattern.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setUtf8OutsideBmpIncludedColumnNamePattern(String regexPattern) {
/* 4322 */     this.utf8OutsideBmpIncludedColumnNamePattern.setValue(regexPattern);
/*      */   }
/*      */   
/*      */   public boolean getIncludeInnodbStatusInDeadlockExceptions() {
/* 4326 */     return this.includeInnodbStatusInDeadlockExceptions.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setIncludeInnodbStatusInDeadlockExceptions(boolean flag) {
/* 4330 */     this.includeInnodbStatusInDeadlockExceptions.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getBlobsAreStrings() {
/* 4334 */     return this.blobsAreStrings.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setBlobsAreStrings(boolean flag) {
/* 4338 */     this.blobsAreStrings.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getFunctionsNeverReturnBlobs() {
/* 4342 */     return this.functionsNeverReturnBlobs.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setFunctionsNeverReturnBlobs(boolean flag) {
/* 4346 */     this.functionsNeverReturnBlobs.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getAutoSlowLog() {
/* 4350 */     return this.autoSlowLog.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setAutoSlowLog(boolean flag) {
/* 4354 */     this.autoSlowLog.setValue(flag);
/*      */   }
/*      */   
/*      */   public String getConnectionLifecycleInterceptors() {
/* 4358 */     return this.connectionLifecycleInterceptors.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setConnectionLifecycleInterceptors(String interceptors) {
/* 4362 */     this.connectionLifecycleInterceptors.setValue(interceptors);
/*      */   }
/*      */   
/*      */   public String getProfilerEventHandler() {
/* 4366 */     return this.profilerEventHandler.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setProfilerEventHandler(String handler) {
/* 4370 */     this.profilerEventHandler.setValue(handler);
/*      */   }
/*      */   
/*      */   public boolean getVerifyServerCertificate() {
/* 4374 */     return this.verifyServerCertificate.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setVerifyServerCertificate(boolean flag) {
/* 4378 */     this.verifyServerCertificate.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getUseLegacyDatetimeCode() {
/* 4382 */     return this.useLegacyDatetimeCode.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setUseLegacyDatetimeCode(boolean flag) {
/* 4386 */     this.useLegacyDatetimeCode.setValue(flag);
/*      */   }
/*      */   
/*      */   public int getSelfDestructOnPingSecondsLifetime() {
/* 4390 */     return this.selfDestructOnPingSecondsLifetime.getValueAsInt();
/*      */   }
/*      */   
/*      */   public void setSelfDestructOnPingSecondsLifetime(int seconds) {
/* 4394 */     this.selfDestructOnPingSecondsLifetime.setValue(seconds);
/*      */   }
/*      */   
/*      */   public int getSelfDestructOnPingMaxOperations() {
/* 4398 */     return this.selfDestructOnPingMaxOperations.getValueAsInt();
/*      */   }
/*      */   
/*      */   public void setSelfDestructOnPingMaxOperations(int maxOperations) {
/* 4402 */     this.selfDestructOnPingMaxOperations.setValue(maxOperations);
/*      */   }
/*      */   
/*      */   public boolean getUseColumnNamesInFindColumn() {
/* 4406 */     return this.useColumnNamesInFindColumn.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setUseColumnNamesInFindColumn(boolean flag) {
/* 4410 */     this.useColumnNamesInFindColumn.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getUseLocalTransactionState() {
/* 4414 */     return this.useLocalTransactionState.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setUseLocalTransactionState(boolean flag) {
/* 4418 */     this.useLocalTransactionState.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getCompensateOnDuplicateKeyUpdateCounts() {
/* 4422 */     return this.compensateOnDuplicateKeyUpdateCounts.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setCompensateOnDuplicateKeyUpdateCounts(boolean flag) {
/* 4426 */     this.compensateOnDuplicateKeyUpdateCounts.setValue(flag);
/*      */   }
/*      */   
/*      */   public int getLoadBalanceBlacklistTimeout() {
/* 4430 */     return this.loadBalanceBlacklistTimeout.getValueAsInt();
/*      */   }
/*      */   
/*      */   public void setLoadBalanceBlacklistTimeout(int loadBalanceBlacklistTimeout) {
/* 4434 */     this.loadBalanceBlacklistTimeout.setValue(loadBalanceBlacklistTimeout);
/*      */   }
/*      */   
/*      */   public void setRetriesAllDown(int retriesAllDown) {
/* 4438 */     this.retriesAllDown.setValue(retriesAllDown);
/*      */   }
/*      */   
/*      */   public int getRetriesAllDown() {
/* 4442 */     return this.retriesAllDown.getValueAsInt();
/*      */   }
/*      */   
/*      */   public void setUseAffectedRows(boolean flag) {
/* 4446 */     this.useAffectedRows.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getUseAffectedRows() {
/* 4450 */     return this.useAffectedRows.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setPasswordCharacterEncoding(String characterSet) {
/* 4454 */     this.passwordCharacterEncoding.setValue(characterSet);
/*      */   }
/*      */   
/*      */   public String getPasswordCharacterEncoding() {
/* 4458 */     return this.passwordCharacterEncoding.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setExceptionInterceptors(String exceptionInterceptors) {
/* 4462 */     this.exceptionInterceptors.setValue(exceptionInterceptors);
/*      */   }
/*      */   
/*      */   public String getExceptionInterceptors() {
/* 4466 */     return this.exceptionInterceptors.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setMaxAllowedPacket(int max) {
/* 4470 */     this.maxAllowedPacket.setValue(max);
/*      */   }
/*      */   
/*      */   public int getMaxAllowedPacket() {
/* 4474 */     return this.maxAllowedPacket.getValueAsInt();
/*      */   }
/*      */   
/*      */   public boolean getQueryTimeoutKillsConnection() {
/* 4478 */     return this.queryTimeoutKillsConnection.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setQueryTimeoutKillsConnection(boolean queryTimeoutKillsConnection) {
/* 4482 */     this.queryTimeoutKillsConnection.setValue(queryTimeoutKillsConnection);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\ConnectionPropertiesImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */