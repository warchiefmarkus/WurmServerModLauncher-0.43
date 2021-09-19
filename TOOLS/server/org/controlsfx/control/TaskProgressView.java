/*     */ package org.controlsfx.control;
/*     */ 
/*     */ import impl.org.controlsfx.skin.TaskProgressViewSkin;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ListChangeListener;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.concurrent.Task;
/*     */ import javafx.concurrent.WorkerStateEvent;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Skin;
/*     */ import javafx.util.Callback;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TaskProgressView<T extends Task<?>>
/*     */   extends ControlsFXControl
/*     */ {
/*     */   private final ObservableList<T> tasks;
/*     */   private ObjectProperty<Callback<T, Node>> graphicFactory;
/*     */   
/*     */   public TaskProgressView() {
/* 112 */     this
/* 113 */       .tasks = FXCollections.observableArrayList(); getStyleClass().add("task-progress-view"); final EventHandler<WorkerStateEvent> taskHandler = evt -> { if (evt.getEventType().equals(WorkerStateEvent.WORKER_STATE_SUCCEEDED) || evt.getEventType().equals(WorkerStateEvent.WORKER_STATE_CANCELLED) || evt.getEventType().equals(WorkerStateEvent.WORKER_STATE_FAILED))
/*     */           getTasks().remove(evt.getSource()); 
/*     */       }; getTasks().addListener(new ListChangeListener<Task<?>>() { public void onChanged(ListChangeListener.Change<? extends Task<?>> c) { while (c.next()) { if (c.wasAdded()) { for (Task<?> task : (Iterable<Task<?>>)c.getAddedSubList())
/*     */                   task.addEventHandler(WorkerStateEvent.ANY, taskHandler);  continue; }
/*     */                if (c.wasRemoved())
/*     */                 for (Task<?> task : (Iterable<Task<?>>)c.getRemoved())
/*     */                   task.removeEventHandler(WorkerStateEvent.ANY, taskHandler);   }
/*     */              } });
/* 121 */   } public final ObservableList<T> getTasks() { return this.tasks; }
/*     */ 
/*     */   
/*     */   public String getUserAgentStylesheet() {
/*     */     return getUserAgentStylesheet(TaskProgressView.class, "taskprogressview.css");
/*     */   }
/*     */   
/*     */   protected Skin<?> createDefaultSkin() {
/*     */     return (Skin<?>)new TaskProgressViewSkin(this);
/*     */   }
/*     */   
/*     */   public final ObjectProperty<Callback<T, Node>> graphicFactoryProperty() {
/* 133 */     if (this.graphicFactory == null) {
/* 134 */       this.graphicFactory = (ObjectProperty<Callback<T, Node>>)new SimpleObjectProperty(this, "graphicFactory");
/*     */     }
/*     */ 
/*     */     
/* 138 */     return this.graphicFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Callback<T, Node> getGraphicFactory() {
/* 147 */     return (this.graphicFactory == null) ? null : (Callback<T, Node>)this.graphicFactory.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setGraphicFactory(Callback<T, Node> factory) {
/* 156 */     graphicFactoryProperty().set(factory);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\TaskProgressView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */