package com.example.ninemenmorris.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ninemenmorris.R;
import com.example.ninemenmorris.controller.MVCController;
import com.example.ninemenmorris.model.NineMenMorrisRules;

public class MVCView implements Parcelable{
    private final static String FRAME_FIELD_TAG = "frame_field_";
    private final static String BLUE_MARKER_TAG = "blue_marker_";
    private final static String RED_MARKER_TAG = "red_marker_";
    private final static String REMOVE_TAG = "removed";
    private final static String DROP_TAG = "dropped";
    private final static String PLACE_TAG = "placed";
    private final static String REPLACE_TAG = "replace";
    private static boolean GAME_OVER;

    View rootView;
    MVCController mvcController;
    private NineMenMorrisRules rules;
    private Context mContext;
    private final int BLUE_MARKER = 4;
    private final int RED_MARKER = 5;
    private int player = 0;

    private View.OnTouchListener touchListener;
    private View.OnDragListener dragListener;

    private FrameLayout[] frameLayout;
    private ImageView[] blueMarkers;
    private ImageView[] redMarkers;
    private TextView playerTurnTextView;

    private int playerTurnVal;
    private boolean valid;
    private int lastMarkerPos;
    private boolean isThreeInARow;

    public MVCView(Context mContext, ViewGroup container) {
        this.rootView = LayoutInflater.from(mContext).inflate(R.layout.activity_main,container);
        this.mContext = mContext;
        this.rules = new NineMenMorrisRules();
        this.mvcController = new MVCController(rules, this);
        initComponents();
    }

    public MVCView(MVCView restoredView, Context context, ViewGroup container){
        this.mContext = context;
        this.rootView = LayoutInflater.from(mContext).inflate(R.layout.activity_main,container);
        this.rules = new NineMenMorrisRules();
        this.rules = restoredView.rules;
        this.playerTurnTextView = rootView.findViewById(R.id.textview_player_turn);
        this.playerTurnTextView.setText(restoredView.playerTurnTextView.getText().toString());
        this.playerTurnVal = restoredView.playerTurnVal;
        this.isThreeInARow = restoredView.isThreeInARow;
        this.valid = restoredView.valid;
        this.lastMarkerPos = restoredView.lastMarkerPos;
        initTouchListener();
        initViewComponents();
        updateMarkers();

        for(int i = 0; i < blueMarkers.length; i++){
            if(restoredView.blueMarkers[i].getTag().toString().equals(REMOVE_TAG)){
                int res = mContext.getResources().getIdentifier(BLUE_MARKER_TAG + i, "id", mContext.getPackageName());
                this.blueMarkers[i] = rootView.findViewById(res);
                this.blueMarkers[i].setVisibility(View.INVISIBLE);
                this.blueMarkers[i].setTag(REMOVE_TAG);
            }else if(restoredView.blueMarkers[i].getTag().toString().equals(PLACE_TAG)){
                ViewGroup restoredOwner = (ViewGroup) restoredView.blueMarkers[i].getParent();
                ViewGroup owner = (ViewGroup) this.blueMarkers[i].getParent();
                owner.removeView(this.blueMarkers[i]);
                System.out.println(restoredOwner.getContentDescription().toString());
                int index = Integer.parseInt(restoredOwner.getContentDescription().toString());
                FrameLayout cont = (FrameLayout) this.frameLayout[index];
                cont.addView(this.blueMarkers[i], 100, 100);
                this.blueMarkers[i].setVisibility(View.VISIBLE);
                this.blueMarkers[i].setTag(PLACE_TAG);
                cont.setTag(DROP_TAG);
            }else if(restoredView.blueMarkers[i].getTag().toString().equals(REPLACE_TAG)){
                ViewGroup restoredOwner = (ViewGroup) restoredView.blueMarkers[i].getParent();
                ViewGroup owner = (ViewGroup) this.blueMarkers[i].getParent();
                owner.removeView(this.blueMarkers[i]);
                System.out.println(restoredOwner.getContentDescription().toString());
                int index = Integer.parseInt(restoredOwner.getContentDescription().toString());
                FrameLayout cont = (FrameLayout) this.frameLayout[index];
                cont.addView(this.blueMarkers[i], 100, 100);
                this.blueMarkers[i].setVisibility(View.VISIBLE);
                this.blueMarkers[i].setTag(REPLACE_TAG);
                cont.setTag(DROP_TAG);
            }
        }

        for(int i = 0; i < redMarkers.length; i++){
            if(restoredView.redMarkers[i].getTag().toString().equals(REMOVE_TAG)){
                int res = mContext.getResources().getIdentifier(RED_MARKER_TAG + i, "id", mContext.getPackageName());
                this.redMarkers[i] = rootView.findViewById(res);
                this.redMarkers[i].setVisibility(View.INVISIBLE);
                this.redMarkers[i].setTag(REMOVE_TAG);
            }else if(restoredView.redMarkers[i].getTag().toString().equals(PLACE_TAG)){
                ViewGroup restoredOwner = (ViewGroup) restoredView.redMarkers[i].getParent();
                ViewGroup owner = (ViewGroup) this.redMarkers[i].getParent();
                owner.removeView(this.redMarkers[i]);
                System.out.println(restoredOwner.getContentDescription().toString());
                int index = Integer.parseInt(restoredOwner.getContentDescription().toString());
                FrameLayout cont = (FrameLayout) this.frameLayout[index];
                cont.addView(this.redMarkers[i], 100, 100);
                this.redMarkers[i].setVisibility(View.VISIBLE);
                this.redMarkers[i].setTag(PLACE_TAG);
                cont.setTag(DROP_TAG);
            }else if(restoredView.redMarkers[i].getTag().toString().equals(REPLACE_TAG)){
                ViewGroup restoredOwner = (ViewGroup) restoredView.redMarkers[i].getParent();
                ViewGroup owner = (ViewGroup) this.redMarkers[i].getParent();
                owner.removeView(this.redMarkers[i]);
                System.out.println(restoredOwner.getContentDescription().toString());
                int index = Integer.parseInt(restoredOwner.getContentDescription().toString());
                FrameLayout cont = (FrameLayout) this.frameLayout[index];
                cont.addView(this.redMarkers[i], 100, 100);
                this.redMarkers[i].setVisibility(View.VISIBLE);
                this.redMarkers[i].setTag(REPLACE_TAG);
                cont.setTag(DROP_TAG);
            }
        }

    }

    private void updateMarkers() {
    }

    protected MVCView(Parcel in) {
        rules = in.readParcelable(NineMenMorrisRules.class.getClassLoader());
        player = in.readInt();
        playerTurnVal = in.readInt();
        rules = in.readParcelable(NineMenMorrisRules.class.getClassLoader());
        valid = in.readByte() != 0;
        lastMarkerPos = in.readInt();
        isThreeInARow = in.readByte() != 0;
    }

    public static final Creator<MVCView> CREATOR = new Creator<MVCView>() {
        @Override
        public MVCView createFromParcel(Parcel in) {
            return new MVCView(in);
        }

        @Override
        public MVCView[] newArray(int size) {
            return new MVCView[size];
        }
    };

    public void initComponents(){
        playerTurnTextView = rootView.findViewById(R.id.textview_player_turn);
        playerTurnTextView.setText(rootView.getResources().getText(R.string.Red_player_turn));
        playerTurnVal = BLUE_MARKER;
        isThreeInARow = false;
        GAME_OVER = false;
        initTouchListener();
        initViewComponents();
    }

    private void initViewComponents() {
        frameLayout = new FrameLayout[25];
        for (int i = 1; i < frameLayout.length; i++) {
            int res = rootView.getResources().getIdentifier(FRAME_FIELD_TAG + i, "id", mContext.getPackageName());
            frameLayout[i] = rootView.findViewById(res);
            frameLayout[i].setTag(FRAME_FIELD_TAG + i);
            frameLayout[i].setOnDragListener(dragListener);
        }
        this.blueMarkers = new ImageView[9];
        for (int i = 0; i < blueMarkers.length; i++) {
            int res = mContext.getResources().getIdentifier(BLUE_MARKER_TAG + i, "id", mContext.getPackageName());
            this.blueMarkers[i] = rootView.findViewById(res);
            this.blueMarkers[i].setTag(BLUE_MARKER_TAG + i);
            this.blueMarkers[i].setOnTouchListener(touchListener);
        }
        redMarkers = new ImageView[9];
        for (int i = 0; i < redMarkers.length; i++) {
            int res = mContext.getResources().getIdentifier(RED_MARKER_TAG + i, "id", mContext.getPackageName());
            redMarkers[i] = rootView.findViewById(res);
            redMarkers[i].setTag(RED_MARKER_TAG + i);
            redMarkers[i].setOnTouchListener(touchListener);
        }
    }

    private void initTouchListener(){
        touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if(GAME_OVER){
                        return false;
                    }
                    if (isThreeInARow) {
                        try {
                            FrameLayout parent = (FrameLayout) view.getParent();
                            System.out.println("Get content: " + parent.getContentDescription().toString());
                            if (rules.remove(Integer.parseInt(parent.getContentDescription().toString()), playerTurnVal)) {
                                parent.removeView(view);
                                view.setTag(REMOVE_TAG);
                                System.out.println("Removed Tag: "+ view.getTag());
                                isThreeInARow = false;
                                if(rules.win(player)){
                                    Toast.makeText(mContext, "Winner: " + player, Toast.LENGTH_SHORT).show();
                                    if(player == BLUE_MARKER){
                                        playerTurnTextView.setText(rootView.getResources().getText(R.string.Blue_player_wins));
                                    }else{
                                        playerTurnTextView.setText(rootView.getResources().getText(R.string.Red_player_wins));
                                    }
                                    //Intent winIntent = new Intent (MainActivity.this, GameOverActivity.class);
                                    //startActivity(winIntent);
                                    GAME_OVER = true;
                                }

                                if(!rules.win(player)){
                                    if (playerTurnVal == BLUE_MARKER) {
                                        playerTurnTextView.setText(rootView.getResources().getText(R.string.Blue_player_turn));
                                    } else {
                                        playerTurnTextView.setText(rootView.getResources().getText(R.string.Red_player_turn));
                                    }
                                }
                                return true;
                            }
                        } catch (ClassCastException ex) {
                            Toast.makeText(mContext, "Remove a marker on the field", Toast.LENGTH_LONG).show();
                        }
                    } else if (view.getTag() != PLACE_TAG) {
                        // Construct draggable shadow for view
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                        // Start the drag of the shadow
                        view.startDrag(null, shadowBuilder, view, 0);
                        // Hide the actual view as shadow is being dragged
                        view.setVisibility(View.INVISIBLE);
                        return true;
                    }
                }
                return false;
            }

        };

        //FrameLayout.OnDragListener dragListener = new View.OnDragListener() {

        dragListener = new View.OnDragListener(){
            @Override
            public boolean onDrag(View view, DragEvent event) {
                Drawable enteredZoneBackground = rootView.getResources().getDrawable(R.drawable.circle_field);
                Drawable defaultBackground = rootView.getResources().getDrawable(R.drawable.circle_field);
                // Get the dragged view being dropped over a target view
                final View draggedView = (View) event.getLocalState();
                final ViewGroup dropTargetParent = (ViewGroup) view.getParent();
                final FrameLayout container = (FrameLayout) view;
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // Signals the start of a drag and drop operation.
                        // Code for that event here

                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        // Signals to a View that the drag point has
                        // entered the bounding box of the View.
                        if (view.getTag() != DROP_TAG) {
                            view.setBackground(enteredZoneBackground);
                            view.invalidate();
                        } else if (view.getTag() == DROP_TAG && container.getChildCount() == 0) {
                            view.setTag(null);
                            container.removeAllViews();
                        }

                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        // Signals that the user has moved the drag shadow
                        // outside the bounding box of the View.
                        if (view.getTag() != DROP_TAG) {
                            view.setBackground(defaultBackground);
                            view.invalidate();
                        }

                        break;
                    case DragEvent.ACTION_DROP:
                        // Signals to a View that the user has released the drag shadow,
                        // and the drag point is within the bounding box of the View.
                        // Get View dragged item is being dropped on
                        //if(view.getTag() != "dropped"){

                        int to = Integer.parseInt(view.getContentDescription().toString());
                        //int player = Integer.parseInt(draggedView.getContentDescription().toString());
                        player = Integer.parseInt(draggedView.getContentDescription().toString());
                        if (rules.allMarkersonField()) {
                            ViewGroup fromContainer = (ViewGroup) draggedView.getParent();
                            int from = Integer.parseInt(fromContainer.getContentDescription().toString());
                            valid = rules.legalMove(to, from, player);
                        } else {
                            valid = rules.placeMarker(to, player);
                        }

                        if (valid) {
                            lastMarkerPos = to;
                            View dropTarget = view;
                            System.out.println("SHOW TAG: " + dropTarget.getTag());
                            View v = (View) event.getLocalState();
                            System.out.println("Marker Tag: " + v.getTag());
                            System.out.println("Marker Description: " + v.getContentDescription());
                            ViewGroup owner = (ViewGroup) v.getParent();
                            //System.out.println("Parent: " + owner.get);
                            owner.removeView(v);
                            //FrameLayout container = (FrameLayout) view;
                            container.addView(v,100,100);
                            v.setVisibility(View.VISIBLE);
                            System.out.println("Test: " + v.getParent());
                            // Make desired changes to the drop target below
                            dropTarget.setTag(DROP_TAG);
                            view.setBackground(defaultBackground);
                            draggedView.setTag(PLACE_TAG);
                            System.out.println("Dragged view: " + draggedView.getParent());
                            System.out.println("Red marker Tag: "+ redMarkers[8].getTag());

                            if (playerTurnTextView.getText().toString().equals(rootView.getResources().getText(R.string.Red_player_turn).toString())) {
                                playerTurnTextView.setText(rootView.getResources().getText(R.string.Blue_player_turn));
                                playerTurnVal = RED_MARKER;
                            } else {
                                playerTurnTextView.setText(rootView.getResources().getText(R.string.Red_player_turn));
                                playerTurnVal = BLUE_MARKER;
                            }
                        }

                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        // Signals to a View that the drag and drop operation has concluded.
                        // If event result is set, this means the dragged view was dropped in target
                        if (valid) { // drop succeeded
                            view.invalidate();
                            valid = false;
                            if (rules.allMarkersonField()) {
                                setTagsAsReplace();
                            }
                            isThreeInARow = rules.remove(lastMarkerPos);
                            if (isThreeInARow) {
                                if (playerTurnVal == BLUE_MARKER) {
                                    playerTurnVal = RED_MARKER;
                                    playerTurnTextView.setText(rootView.getResources().getText(R.string.Blue_player_removes));
                                } else {
                                    playerTurnVal = BLUE_MARKER;
                                    playerTurnTextView.setText(rootView.getResources().getText(R.string.Red_player_removes));
                                }
                            }
                        } else { // drop did not occur
                            // restore the view as visible
                            draggedView.post(new Runnable() {
                                @Override
                                public void run() {
                                    draggedView.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    default:
                        break;
                }
                return true;
            }
        };

    }
    private void setTagsAsReplace() {
        for (int i = 0; i < redMarkers.length; i++) {
            if (redMarkers[i] != null) {
                System.out.println("Number red: " + i);
                redMarkers[i].setTag(REPLACE_TAG);
            }
        }
        for (int i = 0; i < blueMarkers.length; i++) {
            if (blueMarkers[i] != null) {
                System.out.println("Number blue: " + i);
                blueMarkers[i].setTag(REPLACE_TAG);
            }
        }
    }

    public View getRootView() {
        return rootView;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(player);
        dest.writeInt(playerTurnVal);
        dest.writeParcelable(rules, flags);
        dest.writeByte((byte) (valid ? 1 : 0));
        dest.writeInt(lastMarkerPos);
        dest.writeByte((byte) (isThreeInARow ? 1 : 0));
    }
}
