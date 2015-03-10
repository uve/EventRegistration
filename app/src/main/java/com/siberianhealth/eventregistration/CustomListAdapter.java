package com.siberianhealth.eventregistration;

/**
 * Created by uve on 19/02/15.
 */
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.siberianhealth.eventregistration.tickets.Ticket;


public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Ticket> TicketItems;
    //ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Ticket> TicketItems) {
        this.activity = activity;
        this.TicketItems = TicketItems;
    }

    @Override
    public int getCount() {
        return TicketItems.size();
    }

    @Override
    public Ticket getItem(int location) {
        return TicketItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        /*
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
                */

        ImageView myImageView = (ImageView) convertView.findViewById(R.id.thumbnail);
        // supossing to have an image called ic_play inside my drawables.
        myImageView.setImageResource(R.drawable.photo);



        TextView ticket_contract = (TextView) convertView.findViewById(R.id.ticket_contract);
        TextView ticket_name     = (TextView) convertView.findViewById(R.id.ticket_name);
        TextView ticket_place    = (TextView) convertView.findViewById(R.id.ticket_place);


        // getting Ticket data for the row
        final Ticket m = TicketItems.get(position);

        final ImageView CheckboxView = (ImageView) convertView.findViewById(R.id.checkBox);


        ticket_name.setText(m.Name());
        ticket_contract.setText(m.Id());

        ticket_place.setText(m.Place());

        setCheckbox(m, CheckboxView);


        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (m.isPlaced()) {
                    return;
                }

                m.Toggle();
                //ticket_place.setText("clicked");

                setCheckbox(m, CheckboxView);

            }
        });

        return convertView;
    }



    private void setCheckbox(Ticket ticket, ImageView CheckboxView){

        if (ticket.isSelected()){
            CheckboxView.setImageResource(R.drawable.selected);
        }
        else{
            CheckboxView.setImageResource(android.R.drawable.screen_background_light_transparent);
        }
    }

}
