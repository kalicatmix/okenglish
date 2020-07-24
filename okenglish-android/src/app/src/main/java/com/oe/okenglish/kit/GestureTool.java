package com.oe.okenglish.kit;

import android.util.Log;
import android.view.MotionEvent;

public class GestureTool {
    private GestureListener gestureListener;
    private float startY=0;
    public GestureListener getGestureListener() {
        return gestureListener;
    }

    public void setGestureListener(GestureListener gestureListener) {
        this.gestureListener = gestureListener;
    }

    public static class Builder{
        private GestureTool gestureTool=new GestureTool();
        public GestureTool build(){
            return  gestureTool;
        }
        public Builder setGestureListener(GestureListener gestureListener){
           gestureTool.setGestureListener(gestureListener);
           return this;
        }
    }
    public static interface GestureListener{
       public boolean onScrollUp();
       public boolean onScrollDown();
       public boolean onMove(MotionEvent event);
    }
    public boolean onTouchEvent(MotionEvent event){
       switch (event.getAction()){
           case MotionEvent.ACTION_UP:
               if((startY-event.getRawY())>0){
                   gestureListener.onScrollDown();
               }else{
                   gestureListener.onScrollUp();
               }
               startY=0;
               break;
           case MotionEvent.ACTION_DOWN:
               startY=event.getRawY();
               break;
           case MotionEvent.ACTION_MOVE:
               gestureListener.onMove(event);
               break;

       }
        return true;
    }
}
