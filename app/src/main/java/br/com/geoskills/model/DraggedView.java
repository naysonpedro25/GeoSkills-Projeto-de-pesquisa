package br.com.geoskills.model;

import android.content.ClipData;
import android.content.ClipDescription;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

public class DraggedView {
   private View view;
   private boolean closeDrag = false;
   private final String tag;
   private static final Map<View, DraggedView> viewDraggedViewMap = new HashMap<>();

   public View getView() {
      return view;
   }

   public boolean isCloseDrag() {
      return closeDrag;
   }

   public void setCloseDrag(boolean closeDrag) {
      this.closeDrag = closeDrag;
   }

   public DraggedView(View view, String tag, int img) {
      this.view = view;
      this.tag = tag;
      ((ImageView) view).setImageResource(img);
      addOnTouch();
      viewDraggedViewMap.put(view, this);
   }

   public static DraggedView getDraggedView(View view) {
      return viewDraggedViewMap.get(view);
   }

   private void addOnTouch(){

      view.setOnTouchListener(new View.OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {
            if(!closeDrag){
               Log.i("TAGVIEW ", tag);
               ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
               ClipData dragData = new ClipData((CharSequence) v.getTag(), new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
               View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
               myShadowBuilder.getView().startDragAndDrop(dragData, myShadowBuilder, v, View.DRAG_FLAG_OPAQUE);

            return true;
            }
            return false;
         }
      });
   }

   public String getTag() {
      return tag;
   }
}
