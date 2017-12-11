package kodman.appfromkorovin;


import java.io.ByteArrayInputStream;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MainActivity extends AppCompatActivity {

    private static final String TAG=MainActivity.class.getSimpleName();
    private static final String AINSOFT_URL="http://ainsoft.pro/test/test.xml";
    private  final String[] URL={"",""};
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pDialog = new ProgressDialog(this);

    }

    private void getData()
    {
        pDialog.setMessage("Downloading json...");

        Log.e(TAG,"---------1");
        pDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest req= new StringRequest(Request.Method.GET,AINSOFT_URL,
            new Response.Listener<String>() {
            @Override
            public void onResponse( String response) {
                Log.e(TAG,"!!!!!!!!!!!!Response: " + response.toString());

        try
        {

            String newStr = URLDecoder.decode(URLEncoder.encode(response, "iso8859-1"),"UTF-8");

            Log.e(TAG,"!!!!!!!!!!!!Response New: " + newStr.toString());
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Document document = documentBuilder.parse(new ByteArrayInputStream(newStr.getBytes()));

            NodeList nodeList = document.getElementsByTagName("product");

            Log.d(TAG," nodeLIst:"+nodeList);
            for(int i=0; i<nodeList.getLength();i++) {
                Node node= nodeList.item(i);
                if(node.getNodeType()==Node.ELEMENT_NODE)
                {
                    Element element=(Element)node;
                    int id=Integer.parseInt(getNode("id",element));
                    String name=getNode("name",element);
                    float price=Float.parseFloat(getNode("price",element));
                    Product p= new Product(id,name,price);
                    writeDataToDB(p);
                    Log.d(TAG,"++++++++++Product : "+p);
                }
            }
        }
        catch (Exception e)
        {
                Log.e(TAG,"EXCEPTION-------------"+e.getMessage());
        }

        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e(TAG,"!!!!!!!!!!!!Error Response: " + error.toString());
                        // TODO Auto-generated method stub
                        pDialog.hide();
                    }
                });

                Log.e(TAG,"---------2  "+req);
                requestQueue.add(req);

    }


    private String getNode(String title,Element element)
    {
        NodeList nodeList=element.getElementsByTagName(title).item(0).getChildNodes();
        Node nodeValue=(Node) nodeList.item(0);
        return nodeValue.getNodeValue();
    }

    public void clickButton(View view)
    {

        switch(view.getId())
        {
            case R.id.btnProducts:
                    Intent intent= new Intent(this,SecondActivity.class);
                    startActivity(intent);
                    break;

            case R.id.btnDownload:
                getData();
                break;
        }

    }

        public void writeDataToDB(Product p)
        {

            new DBHandler(this).insertProductToDB(p.getId(),p.getName(),p.getPrice());
        }
    }
