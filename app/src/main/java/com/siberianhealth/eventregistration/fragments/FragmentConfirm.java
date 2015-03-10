package com.siberianhealth.eventregistration.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.siberianhealth.eventregistration.Model;
import com.siberianhealth.eventregistration.Model.TicketStatus;
import com.siberianhealth.eventregistration.model.Person;
import com.siberianhealth.eventregistration.R;
import com.siberianhealth.eventregistration.tickets.ListTickets;
import com.siberianhealth.eventregistration.tickets.Ticket;

import java.util.ArrayList;


public class FragmentConfirm extends Fragment {


    private static final String TAG = "FragmentConfirm";

    public static final String PERSON_CONTRACT = "PERSON_CONTRACT";
    public static final String PERSON_ID = "PERSON_ID";

    private static Model model = Model.getInstance();

    private OnListenerPersonConfirmed mListener;

    private String mContract;

    private Button buttonConfirmYes = null;


    public static ListTickets ticketList = new ListTickets();


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment TicketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentConfirm newInstance(String param1) {
        FragmentConfirm fragment = new FragmentConfirm();
        Bundle args = new Bundle();
        args.putString(PERSON_CONTRACT, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentConfirm() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContract = getArguments().getString(PERSON_CONTRACT);
        }
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
    public interface OnListenerPersonConfirmed {
        // TODO: Update argument type and name
        public void OnPersonConfirmed();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnListenerPersonConfirmed) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnListenerPersonConfirmed");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_confirm, container, false);

        final Person person = model.getPerson(mContract);

        TextView name = (TextView) rootView.findViewById(R.id.confirmPersonName);

        name.setText(person.Name());

        final Button buttonConfirmYes = (Button) rootView.findViewById(R.id.buttonNext);
        final Button buttonConfirmNo = (Button) rootView.findViewById(R.id.buttonBack);


        buttonConfirmYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click


                ticketList.addPerson(person);

                mListener.OnPersonConfirmed();

                return;

            }
        });

        buttonConfirmNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                 getFragmentManager().popBackStack();
                return;

            }
        });


        ImageView myImageView = (ImageView) rootView.findViewById(R.id.thumbnail);
        // supossing to have an image called ic_play inside my drawables.
        myImageView.setImageResource(R.drawable.photo);

        return rootView;
    }


    public void ConfirmDialog(final Person person){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.person_found)
                .setMessage("\n" +       getString(R.string.person_confirm) +
                                "\n\n" + getString(R.string.person_contract) + ": " + person.Contract() +
                                "\n" +   getString(R.string.person_name) + ": " + person.Name() +
                                "\n\n")

                //.setIcon(R.drawable.ic_android_cat)
                .setCancelable(false)
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        })
                .setPositiveButton(R.string.confirm_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();

                                CheckTicket(person);
                            }
                        });

        AlertDialog alert = builder.create();
        alert.show();


    }


    public void CheckTicket(final Person person) {

        Log.d(TAG, "CheckTicket for contract: " + person.Contract());

        TicketStatus type = model.CheckTicket(person.Id());

        if (type == TicketStatus.TICKET_EXIST) {

            GetTickets(person);


            Log.d(TAG, "TICKET_EXIST");
        }
        else if (type == TicketStatus.E_NO_TICKETS) {

            Log.d(TAG, "E_NO_TICKETS");
        }
        else if (type == TicketStatus.E_TICKET_ALREADY_LANDED) {

            Log.d(TAG, "E_TICKET_ALREADY_LANDED");
        }
        else if (type == TicketStatus.E_TICKET_ERROR) {

            Log.d(TAG, "E_TICKET_ERROR");
        }
        else {

        }

    }


    public void GetTickets(final Person person) {

        ArrayList<Ticket> tickets = model.GetTickets(person.Id());
/*
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction
                .replace(R.id.container, new TicketFragment())
                .commit();

        getActivity().getFragmentManager().popBackStack();
*/
        Log.d(TAG, tickets.size() + " ticket found");
    }





}
