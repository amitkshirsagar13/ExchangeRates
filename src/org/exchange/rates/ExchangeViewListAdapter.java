package org.exchange.rates;

/**
 * <p>
 * <b>Overview:</b>
 * <p>
 *
 *
 * <pre>
 * Creation date: May 18, 2015
 * @author Amit Kshirsagar
 * @email amit.kshirsagar.13@gmail.com
 * @version 1.0
 * @since
 *
 * <p><b>Modification History:</b><p>
 *
 *
 * </pre>
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import static org.exchange.rates.ExchangeContract.Columns.COMPANY;
import static org.exchange.rates.ExchangeContract.Columns.RATE;

public class ExchangeViewListAdapter extends BaseAdapter {
    public ArrayList<HashMap> list;
    Activity activity;

    public ExchangeViewListAdapter(Activity activity, ArrayList<HashMap> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_row, null);
            holder = new ViewHolder();
            holder.imgIcon = (ImageView) convertView.findViewById(R.id.icon);
            holder.txtCompanyName = (TextView) convertView.findViewById(R.id.companyName);
            holder.txtRate = (TextView) convertView.findViewById(R.id.rate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map = list.get(position);
        holder.txtCompanyName.setText(map.get(COMPANY));
        holder.txtRate.setText(map.get(RATE));

        if (map.get(COMPANY).contains("Remitly")) {
            holder.imgIcon.setImageResource(R.drawable.remitly);
        } else if (map.get(COMPANY).contains("Union")) {
            holder.imgIcon.setImageResource(R.drawable.wu);
        } else if (map.get(COMPANY).contains("Xoom")) {
            holder.imgIcon.setImageResource(R.drawable.xoom);
        } else if (map.get(COMPANY).contains("RIA")) {
            holder.imgIcon.setImageResource(R.drawable.ria);
        } else if (map.get(COMPANY).contains("Wise")) {
            holder.imgIcon.setImageResource(R.drawable.transferwise);
        } else if (map.get(COMPANY).contains("Indus")) {
            holder.imgIcon.setImageResource(R.drawable.indus);
        } else if (map.get(COMPANY).contains("Fast")) {
            holder.imgIcon.setImageResource(R.drawable.tf);
        } else if (map.get(COMPANY).contains("State")) {
            holder.imgIcon.setImageResource(R.drawable.sbi);
        } else if (map.get(COMPANY).contains("Remit2India")) {
            holder.imgIcon.setImageResource(R.drawable.r2i);
        } else if (map.get(COMPANY).contains("Axis")) {
            holder.imgIcon.setImageResource(R.drawable.axis);
        } else if (map.get(COMPANY).contains("MoneyDart")) {
            holder.imgIcon.setImageResource(R.drawable.md);
        } else if (map.get(COMPANY).contains("ICICI")) {
            holder.imgIcon.setImageResource(R.drawable.icici);
        } else if (map.get(COMPANY).contains("USForex")) {
            holder.imgIcon.setImageResource(R.drawable.usf);
        } else {
            holder.imgIcon.setImageResource(R.drawable.shinigami);
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView imgIcon;
        TextView txtCompanyName;
        TextView txtRate;
    }

}