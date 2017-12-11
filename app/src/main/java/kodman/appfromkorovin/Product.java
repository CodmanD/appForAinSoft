package kodman.appfromkorovin;

/**
 * Created by User on 12/9/2017.
 */

public class Product {
    private int id=0;
    private String name="";
    private float price=0.0f;


    public Product(){}
    public Product(int id,String name,float price)
    {
        this.id=id;
        this.name=name;
        this.price=price;
    }

    public int getId(){return this.id;}
    public String getName(){return this.name;}
    public float getPrice(){return this.price;}

    public void setId(int id){ this.id=id;}
    public void setName(String name){this.name=name;}
    public void setPrice(float price){this.price=price;}

    @Override
    public String toString(){return "Product : id = "+this.id+", Name = "+this.name+", Price = "+this.price;}
}
