package com.kety.smock210.ovnsicorrectqrfull.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.kety.smock210.ovnsicorrectqrfull.Errors;
import com.kety.smock210.ovnsicorrectqrfull.R;

import java.util.ArrayList;

public class TegAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    String[] data = {"one", "two", "three", "four", "five"};

    public TegAdapter(Context context, ArrayList<Errors> products) {
        ctx = context;
        //objects = products;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.adapter, parent, false);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Errors p = getProduct(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.textValue)).setText("");
        ((Spinner) view.findViewById(R.id.spiner)).setAdapter(adapter);
        //ImageButton cbBuy = (ImageButton) view.findViewById(R.id.imagepart);
        // присваиваем чекбоксу обработчик
        //cbBuy.setOnClickListener(this);


        return view;

    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
