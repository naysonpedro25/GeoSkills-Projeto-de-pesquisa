package br.com.geoskills.model;

import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import br.com.geoskills.R;

public class ContainerView {

   private final View view;
   private View childView;
   private DraggedView draggedView;
   private boolean closeDrop;
   private final String tag;
   private ViewGroup oldParent;
   private ViewGroup currentParent;

   public ContainerView(View view, View childView, boolean closeDrop,  String tag) {
      this.view = view;
      this.childView = childView;
      this.closeDrop = closeDrop;
      this.tag = tag;
      addOnDrag();
      Log.i("ContainerView", childView.toString());
   }

   public void addOnDrag() {

      view.setOnDragListener(new View.OnDragListener() {
         @Override
         public boolean onDrag(View v, DragEvent event) {
            if (!closeDrop) {
               View viewDragged = (View) event.getLocalState();
               draggedView = DraggedView.getDraggedView(viewDragged);
               switch (event.getAction()) {
                  case DragEvent.ACTION_DRAG_STARTED: {
                     viewDragged.setVisibility(View.INVISIBLE);
                     viewDragged.animate().alpha(0.5f).setDuration(300).start();
                     viewDragged.animate().scaleX(0.9f).scaleY(0.9f).setListener(null).setDuration(300).start();

                     return true;
                  }
                  case DragEvent.ACTION_DRAG_ENTERED: {
                     ((ImageView) childView).setImageResource(R.drawable.item_puzzle_bg_selected);
                     return true;
                  }
                  case DragEvent.ACTION_DRAG_LOCATION: {
                     return true;
                  }
                  case DragEvent.ACTION_DRAG_EXITED: {


                     ((ImageView) childView).setImageResource(R.drawable.item_puzzle_bg);
                     return true;
                  }
                  case DragEvent.ACTION_DROP: {
                        Log.i("MYTAG", tag);
                     currentParent = (LinearLayout) v; // o q vai receber
                     oldParent = (LinearLayout) viewDragged.getParent(); // o antigo

                     if (oldParent != currentParent) {
                        oldParent.removeView(viewDragged);
                        currentParent.removeView(childView);
                        currentParent.addView(viewDragged);
                     }
                     draggedView.setCloseDrag(true);
                     closeDrop = true;
                     viewDragged.setVisibility(View.VISIBLE);


                     viewDragged.animate().alpha(1f).setDuration(300).start();

                     viewDragged.animate().scaleX(1f).scaleY(1f).setInterpolator(new DecelerateInterpolator()).setDuration(300).start();
//                  reset_1.setOnClickListener(b -> {
//                     reset(viewDragged, oldParent);
//                     if (currentParent.getChildCount() == 1) {
//                        closeDrag = true;
//                     } else {
//                        closeDrag = false;
//                     }
//
//                  });

//                  if (currentParent.getChildCount() == 1) {
//                     closeDrag = true;
//                  } else {
//                     closeDrag = false;
//                  }

                     return true;
                  }
                  case DragEvent.ACTION_DRAG_ENDED: {

                     viewDragged.setVisibility(View.VISIBLE);
//                  v.setBackgroundResource(R.drawable.border);
                     viewDragged.animate().alpha(1f).setDuration(300).start();
                     viewDragged.animate().scaleX(1f).scaleY(1f).setDuration(300).start();
                     return true;
                  }
               }

            }
            return false;
         }
      });

   }

   public void resetDrags(){

      try {

         if (oldParent != currentParent && childView != null && draggedView != null && currentParent != null && oldParent != null) {
            View viewDragged = draggedView.getView();
            currentParent.removeView(viewDragged);
            oldParent.addView(viewDragged);
            currentParent.addView(childView);

            ((ImageView) childView).setImageResource(R.drawable.item_puzzle_bg);

            draggedView.setCloseDrag(false);
            closeDrop = false;
         }
      }catch (Exception e){
         Log.i("ContainerView", e.getMessage());
      }
   }

   public boolean verifyTagDragIsEquals(){
      Log.i("ContainerView", "Container tag > "+ tag);
      Log.i("ContainerView", "Drag tag > " +draggedView.getTag());
      return draggedView.getTag().equals(tag);
   }

   public View getView() {
      return view;
   }

   public View getChildView() {
      return childView;
   }
   public DraggedView getDraggedView(){
      return draggedView;
   }

   public void setChildView(View childView) {
      this.childView = childView;
   }

   public boolean isCloseDrop() {
      return closeDrop;
   }

   public void setCloseDrop(boolean closeDrop) {
      this.closeDrop = closeDrop;
   }

   public String getTag() {
      return tag;
   }



}
