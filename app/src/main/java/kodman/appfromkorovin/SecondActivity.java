package kodman.appfromkorovin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by User on 12/10/2017.
 */

public class SecondActivity extends AppCompatActivity {

    private final static String TAG="SECONDACTIVITY";
    private Context mContext;
    float deltaX;
    float deltaY;
    RecyclerView rv;

    AlertDialog  dialog=null;


    static final int STATUS_DIALOG=1;
    static final int STATUS_SHOW_PRODUCTS=0;
    static short status=SecondActivity.STATUS_SHOW_PRODUCTS;

    float posX=-1,posY;
    String curPrice=null;

    //View currentView=null;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Toast.makeText(this,"restore",Toast.LENGTH_SHORT).show();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(SecondActivity.status==SecondActivity.STATUS_DIALOG)
        {
            Log.d(TAG,"------saved Bundle");
            Toast.makeText(this,"saved Bundle",Toast.LENGTH_SHORT).show();
            outState.putFloat("x",posX);
            outState.putFloat("y",posY);
           // outState.putSerializable("view",currentView);
            outState.putString("price", curPrice);
            outState.putInt("status", status);
        }
    }



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_with_products);
        mContext=getApplicationContext();

        rv= findViewById(R.id.rv);
        rv.setItemAnimator(new DefaultItemAnimator());
        final ProductAdapter adapter=new ProductAdapter(SecondActivity.this,new DBHandler(this).readAllProductsFromDB());
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(SecondActivity.this));

        rv.setItemAnimator(new DefaultItemAnimator());

        rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                                      @Override
                                      public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                                          if(e.getAction()==MotionEvent.ACTION_DOWN)
                                          {
                                              deltaX=e.getX();
                                              deltaY=e.getY();
                                              return false;
                                          }
                                         if( e.getAction()==MotionEvent.ACTION_UP&&
                                                 deltaX==e.getX()&&deltaY==e.getY())

                                         {


                                             posX=e.getX();
                                             posY=e.getY();
                                             createDialog();

                                         }
                                          return false;
                                      }

                                      @Override
                                      public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                                          Toast.makeText(SecondActivity.this,"Change ",Toast.LENGTH_SHORT).show();
                                      }

                                      @Override
                                      public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                                      }
                                  });

        rv.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {


                if(savedInstanceState!=null&&savedInstanceState.containsKey("x"))
                {

                    posX=savedInstanceState.getFloat("x");
                    posY=savedInstanceState.getFloat("y");
                    curPrice=savedInstanceState.getString("price");
                    status=(short)savedInstanceState.getInt("status");
                    Log.d(TAG,"------restore Bundle =x="+posX+" Y="+posY+"  status="+status+" curPrice = "+curPrice);
                 //   Toast.makeText(this,"restore Bundle x= "+posX,Toast.LENGTH_SHORT).show();
                }
                if(posX!=-1&&dialog==null)
                {
                    Log.d(TAG,"------onCreate DIALOG");
                    createDialog();
                }
                else
                {
                    Log.d(TAG,"------CREATE x="+posX+"  Y="+posY);
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {

            }
        });
 }

    private void createDialog()
    {

        Log.d(TAG,"------createDialog x="+posX+"  Y="+posY);
        final View view = rv.findChildViewUnder(posX, posY);

        if (view == null)
        {
            Log.d(TAG,"------createDialog View= null");
            return;
        }

        final String id = ((TextView) view.findViewById(R.id.tvId)).getText().toString();
        if(curPrice==null)
            curPrice = ((TextView) view.findViewById(R.id.tvPrice)).getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
        LayoutInflater inflater = getLayoutInflater();


        View dialogView = inflater.inflate(R.layout.dialog, null);
        final EditText etPrice = dialogView.findViewById(R.id.editTextDialog);

        //Для восстановления при повороте
        etPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                    curPrice=etPrice.getText().toString();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        etPrice.setText(curPrice);


        builder.setMessage(R.string.dialogChangePrice)
                .setView(dialogView)
                .setCancelable(true)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        curPrice=null;
                    }
                })
               .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(SecondActivity.this, "Cancel ", Toast.LENGTH_SHORT).show();
                                SecondActivity.status=SecondActivity.STATUS_SHOW_PRODUCTS;
                                curPrice = null;
                                dialog.dismiss();
                            }
                        })
                .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        curPrice = etPrice.getText().toString();

                        if (new DBHandler(SecondActivity.this).updatePrice(id, curPrice) != -1)
                        {
                            ((TextView)view.findViewById(R.id.tvPrice)).setText(curPrice);
                            SecondActivity.status=SecondActivity.STATUS_SHOW_PRODUCTS;
                            dialog.dismiss();
                        }
                        else
                            Toast.makeText(SecondActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
        dialog = builder.create();
        SecondActivity.status=SecondActivity.STATUS_DIALOG;
        dialog.show();
    }
}
