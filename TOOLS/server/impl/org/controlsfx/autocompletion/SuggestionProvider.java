/*     */ package impl.org.controlsfx.autocompletion;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import javafx.beans.property.BooleanProperty;
/*     */ import javafx.beans.property.SimpleBooleanProperty;
/*     */ import javafx.util.Callback;
/*     */ import org.controlsfx.control.textfield.AutoCompletionBinding;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SuggestionProvider<T>
/*     */   implements Callback<AutoCompletionBinding.ISuggestionRequest, Collection<T>>
/*     */ {
/*  50 */   private final List<T> possibleSuggestions = new ArrayList<>();
/*  51 */   private final Object possibleSuggestionsLock = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   private final BooleanProperty showAllIfEmptyProperty = (BooleanProperty)new SimpleBooleanProperty(false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final BooleanProperty showAllIfEmptyProperty() {
/*  66 */     return this.showAllIfEmptyProperty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isShowAllIfEmpty() {
/*  75 */     return this.showAllIfEmptyProperty.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setShowAllIfEmpty(boolean showAllIfEmpty) {
/*  85 */     this.showAllIfEmptyProperty.set(showAllIfEmpty);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPossibleSuggestions(T... newPossible) {
/*  93 */     addPossibleSuggestions(Arrays.asList(newPossible));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPossibleSuggestions(Collection<T> newPossible) {
/* 101 */     synchronized (this.possibleSuggestionsLock) {
/* 102 */       this.possibleSuggestions.addAll(newPossible);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearSuggestions() {
/* 110 */     synchronized (this.possibleSuggestionsLock) {
/* 111 */       this.possibleSuggestions.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final Collection<T> call(AutoCompletionBinding.ISuggestionRequest request) {
/* 117 */     List<T> suggestions = new ArrayList<>();
/* 118 */     if (!request.getUserText().isEmpty()) {
/* 119 */       synchronized (this.possibleSuggestionsLock) {
/* 120 */         for (T possibleSuggestion : this.possibleSuggestions) {
/* 121 */           if (isMatch(possibleSuggestion, request)) {
/* 122 */             suggestions.add(possibleSuggestion);
/*     */           }
/*     */         } 
/*     */       } 
/* 126 */       Collections.sort(suggestions, getComparator());
/*     */     }
/* 128 */     else if (isShowAllIfEmpty()) {
/* 129 */       synchronized (this.possibleSuggestionsLock) {
/* 130 */         suggestions.addAll(this.possibleSuggestions);
/*     */       } 
/*     */     } 
/*     */     
/* 134 */     return suggestions;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> SuggestionProvider<T> create(Collection<T> possibleSuggestions) {
/* 165 */     return create(null, possibleSuggestions);
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
/*     */   public static <T> SuggestionProvider<T> create(Callback<T, String> stringConverter, Collection<T> possibleSuggestions) {
/* 177 */     SuggestionProviderString<T> suggestionProvider = new SuggestionProviderString<>(stringConverter);
/* 178 */     suggestionProvider.addPossibleSuggestions(possibleSuggestions);
/* 179 */     return suggestionProvider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Comparator<T> getComparator();
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean isMatch(T paramT, AutoCompletionBinding.ISuggestionRequest paramISuggestionRequest);
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SuggestionProviderString<T>
/*     */     extends SuggestionProvider<T>
/*     */   {
/*     */     private Callback<T, String> stringConverter;
/*     */ 
/*     */ 
/*     */     
/* 200 */     private final Comparator<T> stringComparator = new Comparator<T>()
/*     */       {
/*     */         public int compare(T o1, T o2) {
/* 203 */           String o1str = (String)SuggestionProvider.SuggestionProviderString.this.stringConverter.call(o1);
/* 204 */           String o2str = (String)SuggestionProvider.SuggestionProviderString.this.stringConverter.call(o2);
/* 205 */           return o1str.compareTo(o2str);
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SuggestionProviderString(Callback<T, String> stringConverter) {
/* 214 */       this.stringConverter = stringConverter;
/*     */ 
/*     */       
/* 217 */       if (this.stringConverter == null) {
/* 218 */         this.stringConverter = new Callback<T, String>()
/*     */           {
/*     */             public String call(T obj) {
/* 221 */               return (obj != null) ? obj.toString() : "";
/*     */             }
/*     */           };
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected Comparator<T> getComparator() {
/* 230 */       return this.stringComparator;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean isMatch(T suggestion, AutoCompletionBinding.ISuggestionRequest request) {
/* 236 */       String userTextLower = request.getUserText().toLowerCase();
/* 237 */       String suggestionStr = ((String)this.stringConverter.call(suggestion)).toLowerCase();
/* 238 */       return suggestionStr.contains(userTextLower);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\autocompletion\SuggestionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */