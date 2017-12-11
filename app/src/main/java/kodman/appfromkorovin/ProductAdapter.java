package kodman.appfromkorovin;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by User on 12/10/2017.
 */

public class ProductAdapter  extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    //private List<Image> images;
    Cursor cursor;
    private static Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvId;
        public TextView tvName;
        public TextView tvPrice;

        public MyViewHolder(View view) {
            super(view);
            tvId=(TextView) view.findViewById(R.id.tvId);
            tvName=(TextView)view.findViewById(R.id.tvName);
            tvPrice=(TextView)view.findViewById(R.id.tvPrice);
            Log.d("View Holder","----------------CREATE new OBJECT");
        }
    }


    public ProductAdapter(Context context,Cursor cursor) {
        mContext = context;
        this.cursor = cursor;
        Log.d("ProductAdapter","-----------CREATE");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.tvId.setText(""+cursor.getInt(cursor.getColumnIndex("_id")));
        holder.tvName.setText(cursor.getString(cursor.getColumnIndex("name")));
        holder.tvPrice.setText(""+cursor.getFloat(cursor.getColumnIndex("price")));
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }




    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;

        private ProductAdapter.ClickListener clickListener;

        public RecyclerTouchListener(final Context context, final RecyclerView recyclerView, final ProductAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    Toast.makeText(ProductAdapter.mContext,"DELETE?",Toast.LENGTH_SHORT).show();
                    if (child != null && clickListener != null) {

                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));

                    }
                }
            });


        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null ) {
                clickListener.onClick(child, rv.getChildPosition(child));

           }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}