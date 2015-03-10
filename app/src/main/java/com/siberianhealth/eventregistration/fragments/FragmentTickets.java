package com.siberianhealth.eventregistration.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.siberianhealth.eventregistration.Model;
import com.siberianhealth.eventregistration.R;

import com.siberianhealth.eventregistration.CustomListAdapter;
import com.siberianhealth.eventregistration.model.Person;
import com.siberianhealth.eventregistration.tickets.ListTickets;
import com.siberianhealth.eventregistration.tickets.Ticket;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentTickets.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentTickets#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTickets extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private int mPersonId;

    public static ListTickets ticketList = new ListTickets();
    private CustomListAdapter adapter;

    private OnFragmentInteractionListener mListener;


    private static Model model = Model.getInstance();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentTickets.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTickets newInstance(int param1) {
        FragmentTickets fragment = new FragmentTickets();
        Bundle args = new Bundle();
        args.putInt(Person.PERSON_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentTickets() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mPersonId = getArguments().getInt(Person.PERSON_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ticket, container, false);


        ListView listView = (ListView) rootView.findViewById(R.id.list);
        adapter = new CustomListAdapter(getActivity(), ticketList.ITEMS);
        listView.setAdapter(adapter);


        final Button buttonAddPerson = (Button) rootView.findViewById(R.id.buttonAddPerson);
        final Button buttonExit      = (Button) rootView.findViewById(R.id.buttonExit);
        final Button buttonRegister  = (Button) rootView.findViewById(R.id.buttonRegister);


        ticketList.reloadTickets();


        buttonAddPerson.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //String ticketList.
                mListener.OnTicketsAddPerson();
                return;

            }
        });


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String list = ticketList.getSelected();

                boolean isLand = model.LandTickets(list);

                ticketList.reloadTickets();
                adapter.notifyDataSetChanged();
                //mListener.OnTicketsExit();
                return;

            }
        });


        buttonExit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ticketList.clear();

                mListener.OnTicketsExit();
                return;

            }
        });




        return rootView;
    }
/*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
*/
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        //public void onFragmentInteraction(Uri uri);

        public void OnTicketsAddPerson();
        public void OnTicketsExit();
    }

}
