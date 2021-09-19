/*     */ package org.fourthline.cling.support.contentdirectory;
/*     */ 
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.fourthline.cling.binding.annotations.UpnpAction;
/*     */ import org.fourthline.cling.binding.annotations.UpnpInputArgument;
/*     */ import org.fourthline.cling.binding.annotations.UpnpOutputArgument;
/*     */ import org.fourthline.cling.binding.annotations.UpnpService;
/*     */ import org.fourthline.cling.binding.annotations.UpnpServiceId;
/*     */ import org.fourthline.cling.binding.annotations.UpnpServiceType;
/*     */ import org.fourthline.cling.binding.annotations.UpnpStateVariable;
/*     */ import org.fourthline.cling.binding.annotations.UpnpStateVariables;
/*     */ import org.fourthline.cling.model.types.ErrorCode;
/*     */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
/*     */ import org.fourthline.cling.model.types.csv.CSV;
/*     */ import org.fourthline.cling.model.types.csv.CSVString;
/*     */ import org.fourthline.cling.support.model.BrowseFlag;
/*     */ import org.fourthline.cling.support.model.BrowseResult;
/*     */ import org.fourthline.cling.support.model.DIDLContent;
/*     */ import org.fourthline.cling.support.model.SortCriterion;
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
/*     */ @UpnpService(serviceId = @UpnpServiceId("ContentDirectory"), serviceType = @UpnpServiceType(value = "ContentDirectory", version = 1))
/*     */ @UpnpStateVariables({@UpnpStateVariable(name = "A_ARG_TYPE_ObjectID", sendEvents = false, datatype = "string"), @UpnpStateVariable(name = "A_ARG_TYPE_Result", sendEvents = false, datatype = "string"), @UpnpStateVariable(name = "A_ARG_TYPE_BrowseFlag", sendEvents = false, datatype = "string", allowedValuesEnum = BrowseFlag.class), @UpnpStateVariable(name = "A_ARG_TYPE_Filter", sendEvents = false, datatype = "string"), @UpnpStateVariable(name = "A_ARG_TYPE_SortCriteria", sendEvents = false, datatype = "string"), @UpnpStateVariable(name = "A_ARG_TYPE_Index", sendEvents = false, datatype = "ui4"), @UpnpStateVariable(name = "A_ARG_TYPE_Count", sendEvents = false, datatype = "ui4"), @UpnpStateVariable(name = "A_ARG_TYPE_UpdateID", sendEvents = false, datatype = "ui4"), @UpnpStateVariable(name = "A_ARG_TYPE_URI", sendEvents = false, datatype = "uri"), @UpnpStateVariable(name = "A_ARG_TYPE_SearchCriteria", sendEvents = false, datatype = "string")})
/*     */ public abstract class AbstractContentDirectoryService
/*     */ {
/*     */   public static final String CAPS_WILDCARD = "*";
/*     */   @UpnpStateVariable(sendEvents = false)
/*     */   private final CSV<String> searchCapabilities;
/*     */   @UpnpStateVariable(sendEvents = false)
/*     */   private final CSV<String> sortCapabilities;
/*     */   @UpnpStateVariable(sendEvents = true, defaultValue = "0", eventMaximumRateMilliseconds = 200)
/* 107 */   private UnsignedIntegerFourBytes systemUpdateID = new UnsignedIntegerFourBytes(0L);
/*     */ 
/*     */ 
/*     */   
/*     */   protected final PropertyChangeSupport propertyChangeSupport;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractContentDirectoryService() {
/* 117 */     this(new ArrayList<>(), new ArrayList<>(), null);
/*     */   }
/*     */   
/*     */   protected AbstractContentDirectoryService(List<String> searchCapabilities, List<String> sortCapabilities) {
/* 121 */     this(searchCapabilities, sortCapabilities, null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractContentDirectoryService(List<String> searchCapabilities, List<String> sortCapabilities, PropertyChangeSupport propertyChangeSupport) {
/* 126 */     this.propertyChangeSupport = (propertyChangeSupport != null) ? propertyChangeSupport : new PropertyChangeSupport(this);
/* 127 */     this.searchCapabilities = (CSV<String>)new CSVString();
/* 128 */     this.searchCapabilities.addAll(searchCapabilities);
/* 129 */     this.sortCapabilities = (CSV<String>)new CSVString();
/* 130 */     this.sortCapabilities.addAll(sortCapabilities);
/*     */   }
/*     */   
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "SearchCaps")})
/*     */   public CSV<String> getSearchCapabilities() {
/* 135 */     return this.searchCapabilities;
/*     */   }
/*     */   
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "SortCaps")})
/*     */   public CSV<String> getSortCapabilities() {
/* 140 */     return this.sortCapabilities;
/*     */   }
/*     */   
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "Id")})
/*     */   public synchronized UnsignedIntegerFourBytes getSystemUpdateID() {
/* 145 */     return this.systemUpdateID;
/*     */   }
/*     */   
/*     */   public PropertyChangeSupport getPropertyChangeSupport() {
/* 149 */     return this.propertyChangeSupport;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void changeSystemUpdateID() {
/* 160 */     Long oldUpdateID = getSystemUpdateID().getValue();
/* 161 */     this.systemUpdateID.increment(true);
/* 162 */     getPropertyChangeSupport().firePropertyChange("SystemUpdateID", oldUpdateID, 
/*     */ 
/*     */         
/* 165 */         getSystemUpdateID().getValue());
/*     */   }
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
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "Result", stateVariable = "A_ARG_TYPE_Result", getterName = "getResult"), @UpnpOutputArgument(name = "NumberReturned", stateVariable = "A_ARG_TYPE_Count", getterName = "getCount"), @UpnpOutputArgument(name = "TotalMatches", stateVariable = "A_ARG_TYPE_Count", getterName = "getTotalMatches"), @UpnpOutputArgument(name = "UpdateID", stateVariable = "A_ARG_TYPE_UpdateID", getterName = "getContainerUpdateID")})
/*     */   public BrowseResult browse(@UpnpInputArgument(name = "ObjectID", aliases = {"ContainerID"}) String objectId, @UpnpInputArgument(name = "BrowseFlag") String browseFlag, @UpnpInputArgument(name = "Filter") String filter, @UpnpInputArgument(name = "StartingIndex", stateVariable = "A_ARG_TYPE_Index") UnsignedIntegerFourBytes firstResult, @UpnpInputArgument(name = "RequestedCount", stateVariable = "A_ARG_TYPE_Count") UnsignedIntegerFourBytes maxResults, @UpnpInputArgument(name = "SortCriteria") String orderBy) throws ContentDirectoryException {
/*     */     SortCriterion[] orderByCriteria;
/*     */     try {
/* 194 */       orderByCriteria = SortCriterion.valueOf(orderBy);
/* 195 */     } catch (Exception ex) {
/* 196 */       throw new ContentDirectoryException(ContentDirectoryErrorCode.UNSUPPORTED_SORT_CRITERIA, ex.toString());
/*     */     } 
/*     */     
/*     */     try {
/* 200 */       return browse(objectId, 
/*     */           
/* 202 */           BrowseFlag.valueOrNullOf(browseFlag), filter, firstResult
/*     */           
/* 204 */           .getValue().longValue(), maxResults.getValue().longValue(), orderByCriteria);
/*     */     
/*     */     }
/* 207 */     catch (ContentDirectoryException ex) {
/* 208 */       throw ex;
/* 209 */     } catch (Exception ex) {
/* 210 */       throw new ContentDirectoryException(ErrorCode.ACTION_FAILED, ex.toString());
/*     */     } 
/*     */   }
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
/*     */   public abstract BrowseResult browse(String paramString1, BrowseFlag paramBrowseFlag, String paramString2, long paramLong1, long paramLong2, SortCriterion[] paramArrayOfSortCriterion) throws ContentDirectoryException;
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
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "Result", stateVariable = "A_ARG_TYPE_Result", getterName = "getResult"), @UpnpOutputArgument(name = "NumberReturned", stateVariable = "A_ARG_TYPE_Count", getterName = "getCount"), @UpnpOutputArgument(name = "TotalMatches", stateVariable = "A_ARG_TYPE_Count", getterName = "getTotalMatches"), @UpnpOutputArgument(name = "UpdateID", stateVariable = "A_ARG_TYPE_UpdateID", getterName = "getContainerUpdateID")})
/*     */   public BrowseResult search(@UpnpInputArgument(name = "ContainerID", stateVariable = "A_ARG_TYPE_ObjectID") String containerId, @UpnpInputArgument(name = "SearchCriteria") String searchCriteria, @UpnpInputArgument(name = "Filter") String filter, @UpnpInputArgument(name = "StartingIndex", stateVariable = "A_ARG_TYPE_Index") UnsignedIntegerFourBytes firstResult, @UpnpInputArgument(name = "RequestedCount", stateVariable = "A_ARG_TYPE_Count") UnsignedIntegerFourBytes maxResults, @UpnpInputArgument(name = "SortCriteria") String orderBy) throws ContentDirectoryException {
/*     */     SortCriterion[] orderByCriteria;
/*     */     try {
/* 255 */       orderByCriteria = SortCriterion.valueOf(orderBy);
/* 256 */     } catch (Exception ex) {
/* 257 */       throw new ContentDirectoryException(ContentDirectoryErrorCode.UNSUPPORTED_SORT_CRITERIA, ex.toString());
/*     */     } 
/*     */     
/*     */     try {
/* 261 */       return search(containerId, searchCriteria, filter, firstResult
/*     */ 
/*     */ 
/*     */           
/* 265 */           .getValue().longValue(), maxResults.getValue().longValue(), orderByCriteria);
/*     */     
/*     */     }
/* 268 */     catch (ContentDirectoryException ex) {
/* 269 */       throw ex;
/* 270 */     } catch (Exception ex) {
/* 271 */       throw new ContentDirectoryException(ErrorCode.ACTION_FAILED, ex.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BrowseResult search(String containerId, String searchCriteria, String filter, long firstResult, long maxResults, SortCriterion[] orderBy) throws ContentDirectoryException {
/*     */     try {
/* 285 */       return new BrowseResult((new DIDLParser()).generate(new DIDLContent()), 0L, 0L);
/* 286 */     } catch (Exception ex) {
/* 287 */       throw new ContentDirectoryException(ErrorCode.ACTION_FAILED, ex.toString());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\contentdirectory\AbstractContentDirectoryService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */