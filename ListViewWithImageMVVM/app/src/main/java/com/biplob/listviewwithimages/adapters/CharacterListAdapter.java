package com.biplob.listviewwithimages.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.biplob.listviewwithimages.R;
import com.biplob.listviewwithimages.models.Character;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CharacterListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Character> characterList;

    public CharacterListAdapter(Context context, ArrayList<Character> characterList) {
        this.context = context;
        this.characterList = characterList;
    }

    @Override
    public int getCount() {
        return characterList.size();
    }

    @Override
    public Character getItem(int position) {
        return characterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateList(ArrayList<Character> characterList) {
        this.characterList = characterList;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        Character row = characterList.get(position);

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (mInflater != null) {
                convertView = mInflater.inflate(R.layout.row_list_item, parent, false);

                holder = new ViewHolder();

                holder.imgProfile = convertView.findViewById(R.id.imgProfile);

                holder.txtTitle = convertView.findViewById(R.id.txtTitle);

                holder.txtDescription = convertView.findViewById(R.id.txtDescription);

                convertView.setTag(holder);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtTitle.setText(row.getName());

        holder.txtDescription.setText(row.getStatus());

        if (row.getImage() != null && !row.getImage().isEmpty()) {
            ImageView image = holder.imgProfile;

            Picasso.get().load(row.getImage())
                    .placeholder(R.drawable.other)
                    .error(R.drawable.other)
                    .into(image);
        } else {
            holder.imgProfile.setImageResource(R.drawable.other);
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView imgProfile;
        TextView txtTitle;
        TextView txtDescription;
    }
}
