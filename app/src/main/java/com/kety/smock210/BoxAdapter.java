package com.kety.smock210.ovnsicorrectqrfull;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by smock210 on 08.11.2016.
 */

public class BoxAdapter extends BaseAdapter implements View.OnClickListener {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Errors> objects;

    BoxAdapter(Context context, ArrayList<Errors> products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

            // кол-во элементов
            @Override
    public int getCount() {
        return objects.size();
        }

            // элемент по позиции
            @Override
    public Object getItem(int position) {
        return objects.get(position);
        }

            // id по позиции
            @Override
    public long getItemId(int position) {
        return position;
        }

            // пункт списка
            @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
        view = lInflater.inflate(R.layout.cellgrid, parent, false);
            }

        Errors p = getProduct(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.textpart)).setText(p.text);
        ((ImageButton) view.findViewById(R.id.imagepart)).setImageResource(R.drawable.ic_info_outline_black_24dp);
                ImageButton cbBuy = (ImageButton) view.findViewById(R.id.imagepart);
                // присваиваем чекбоксу обработчик
                cbBuy.setOnClickListener(this);


        return view;
        }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imagepart:
                View itemView = (View)v.getParent();
                String mess = (String) ((TextView) itemView.findViewById(R.id.textpart)).getText();
                String messOut = "";
                for (int i = 0; i<objects.size();i++){
                    Errors err = objects.get(i);
                    if (err.text.equals(mess)){
                        if (err.fuulText.length()>0) {
                            messOut = err.fuulText;
                        } else {
                            messOut = err.text;
                        }
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Важное сообщение!")
                        .setMessage(Html.fromHtml("<font color=\"#ff0000\">" +messOut + "</font>"))
                        .setCancelable(false)
                        .setNegativeButton("ОК",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            default:
                break;
        }

    }




            // товар по позиции
            Errors getProduct(int position) {
        return ((Errors) getItem(position));
        }

            // содержимое корзины



}
