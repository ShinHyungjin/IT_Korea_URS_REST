package com.koreait.ursrest.adpater;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.koreait.ursrest.R;
import com.koreait.ursrest.activity.MainActivity;
import com.koreait.ursrest.domain.MenuCart;
import com.koreait.ursrest.domain.Menus;
import com.koreait.ursrest.fragment.body.MenusFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class MenusAdapter extends BaseAdapter {
    String TAG = this.getClass().getName();

    public List<Menus> menusList = new ArrayList<Menus>();
    public List<MenuCart> menuCartList = new ArrayList<MenuCart>();

    LayoutInflater layoutInflater;
    MainActivity mainActivity;
    MenusFragment menusFragment;
    DecimalFormat myFormatter;


    public static HashMap<Integer, MenuCart> menuCartMap = new HashMap<Integer, MenuCart>();

    public static HashMap<Integer, MenuCart> getMenuCartMap() {
        return menuCartMap;
    }

    public MenusAdapter(MainActivity mainActivity, MenusFragment menusFragment) {
        this.mainActivity = mainActivity;
        this.menusFragment = menusFragment;
        layoutInflater = mainActivity.getLayoutInflater();
        myFormatter = new DecimalFormat("###,###");
        this.menuCartMap.clear();
    }

    @Override
    public int getCount() {
        return menuCartList.size();
    }

    @Override
    public Object getItem(int position) {
        return menuCartList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return menuCartList.get(position).getMenu_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MenuCart menuCart = menuCartList.get(position);

        View view = null; //이 뷰가 부모 뷰인데, 클릭할때마다 이 부모뷰의 정보를 얻기만 하면 됩니다

        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.item_menus, parent, false);
        } else {
            view = convertView;
        }

        //부모뷰다 액티비티로 뜨네요, 이러면 안되고 뷰를 심었던 부모를 특정해야 함 잠시만요 ..


        ImageView menu_image = view.findViewById(R.id.menu_image);
        TextView menu_id = view.findViewById(R.id.menu_id);
        TextView store_id = view.findViewById(R.id.store_id);
        TextView menu_name = view.findViewById(R.id.menu_name);
        TextView menu_stock = view.findViewById(R.id.menu_stock);
        TextView menu_oneprice = view.findViewById(R.id.menu_oneprice);
        TextView menu_price = view.findViewById(R.id.menu_price);
        TextView menu_quantity = view.findViewById(R.id.menu_quantity);
        ImageButton quantityup = view.findViewById(R.id.quantityup);
        ImageButton quantitydown = view.findViewById(R.id.quantitydown);

        //quantityup.setId(menuCart.getMenu_id());
        //quantitydown.setId(menuCart.getMenu_id());

        System.out.println("menuCart_id: "+ menuCart.getMenu_id() + "\tmenu_id : " + menu_id.getText().toString() +"\tmenu_name : " + menuCart.getMenu_name() + "\tposition : " + position + "\tmenusList_price : " + menuCartList.get(position).getMenu_price() +"\tmenus_price : " +menu_price.getText() + "\tStore_id : " + menuCart.getStore_id());

        Glide.with(mainActivity).load("http://192.168.0.71:8888/resources/data/menu/" + menusList.get(position).getMenu_id()+"."+menusList.get(position).getMenu_image()).into(menu_image);
        
        menu_image.setImageBitmap(menusList.get(position).getBitmap_image());
        menu_id.setText(String.valueOf(menuCart.getMenu_id()));
        store_id.setText(String.valueOf(menuCart.getStore_id()));
        menu_name.setText(menuCart.getMenu_name());
        if (menuCart.getMenu_stock().equals("TRUE")) {
            menu_stock.setText("주문가능");
        } else if (menuCart.getMenu_stock().equals("FALSE")) {
            menu_stock.setText("품절!");
        }

        menu_oneprice.setText(myFormatter.format(menuCart.getMenu_price()));
        menu_price.setText(myFormatter.format(menuCart.getQuantity() * menuCart.getMenu_price()));

        quantityup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //클릭 시점에 ListView의 아이템을 얻어와야 하는ㄷ데 지금 그렇게 하고 있는지요 ?
                //아.. 아닌것같습니다 아이템 얻어오는 시점은 listview에 해당 아이템이 등장할 때 라서 위에 있습니다.
                //그럼 , 수정 하기 기능을 여기서 하더라도, 아이템을 얻어와야 정확합니당 , 이렇게 하면 어떨까요?
                //버튼은 해당 아잉템 안에 존재하는 자식 뷰이니깐, 클릭시 자신의 부모를 접근 하는 방법이요
                //또한가지 방법은 , 클릭시 프라이머리 키를 얻어와서, 리스트뷰를 검색하는 방법이 있어요
                //리스트뷰 어댑터 보여주세용 이 어뎁터입니다
                //get parent view

                //Log.d(TAG, "My  id is "+view.getId());

                int quantitycount = Integer.parseInt(menu_quantity.getText().toString()) + 1;
                menu_quantity.setText(String.valueOf(quantitycount));
                int newprice = menuCart.getMenu_price() * quantitycount;
                menu_price.setText(myFormatter.format(newprice));

                MenuCart menuCart = new MenuCart();
                menuCart.setStore_id(Integer.parseInt(store_id.getText().toString()));
                menuCart.setMenu_id(Integer.parseInt(menu_id.getText().toString()));
                menuCart.setMenu_name(menu_name.getText().toString());
                menuCart.setMenu_stock(menu_stock.getText().toString());
                menuCart.setMenu_price(newprice);
                menuCart.setQuantity(quantitycount);

                //System.out.println("클릭한 MenuCart_id : " + menuCart.getMenu_id() + "\tName : " + menuCart.getMenu_name() + "\tQuantity : " + menuCart.getQuantity() +"\tmenu_price : " + menu_price.getText() +"\tStroe_id : " + menuCart.getStore_id() + "\n");

                menuCartMap.put(menuCart.getMenu_id(), menuCart);

                /*
                Iterator<Integer> it = menuCartMap.keySet().iterator();
                while (it.hasNext()) {
                    int menuCart_id = it.next();
                    MenuCart menuCart2 = menuCartMap.get(menuCart_id);
                    //System.out.println("MenuCart_id : " + menuCart2.getMenu_id() + "\tName : " + menuCart2.getMenu_name() + "\tQuantity : " + menuCart2.getQuantity());
                }
                 */
            }
        });

        quantitydown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu_quantity.getText().toString().equals("0")) {
                    Toast.makeText(mainActivity, "0개 보다 작게 설정할 수 없습니다!", Toast.LENGTH_SHORT).show();
                } else {
                    int quantitycount = Integer.parseInt(menu_quantity.getText().toString()) - 1;
                    menu_quantity.setText(String.valueOf(quantitycount));
                    int newprice = menuCart.getMenu_price() * quantitycount;
                    menu_price.setText(myFormatter.format(newprice));

                    MenuCart menuCart = new MenuCart();
                    menuCart.setMenu_id(Integer.parseInt(menu_id.getText().toString()));
                    menuCart.setMenu_name(menu_name.getText().toString());
                    menuCart.setMenu_stock(menu_stock.getText().toString());
                    menuCart.setMenu_price(newprice);
                    menuCart.setQuantity(quantitycount);

                    //System.out.println("클릭한 MenuCart_id : " + menuCart.getMenu_id() + "\tName : " + menuCart.getMenu_name() + "\tQuantity : " + menuCart.getQuantity() + "\n");

                    if (menuCart.getQuantity() == 0) {
                        menuCartMap.remove(menuCart.getMenu_id());
                    } else {
                        menuCartMap.put(menuCart.getMenu_id(), menuCart);
                    }

                    /*
                    Iterator<Integer> it = menuCartMap.keySet().iterator();
                    while (it.hasNext()) {
                        int menuCart_id = it.next();
                        MenuCart menuCart2 = menuCartMap.get(menuCart_id);
                       // System.out.println("MenuCart_id : " + menuCart2.getMenu_id() + "\tName : " + menuCart2.getMenu_name() + "\tQuantity : " + menuCart2.getQuantity());
                    }
                     */
                }
            }
        });

        return view;
    }
}
