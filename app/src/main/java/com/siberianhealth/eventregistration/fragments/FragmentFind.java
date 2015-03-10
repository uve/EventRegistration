package com.siberianhealth.eventregistration.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.siberianhealth.eventregistration.Model;
import com.siberianhealth.eventregistration.model.Person;
import com.siberianhealth.eventregistration.R;
import com.siberianhealth.eventregistration.tickets.ListTickets;

import java.util.Locale;


public class FragmentFind extends Fragment {


    private static final String TAG = "FragmentFind";

    private static final Integer CONTRACT_MIN_LENGTH = 6;


    private static Model model = Model.getInstance();

    private OnListenerPersonFound mListener;



    private EditText editText = null;
    private Button buttonNext = null;
    private Button buttonBack = null;

    private LinearLayout langEN = null;
    private LinearLayout langRU = null;
    private ImageView buttonOptions = null;




    public FragmentFind() {
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
    public interface OnListenerPersonFound {
        // TODO: Update argument type and name
        public void onPersonFound(Person person);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnListenerPersonFound) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnListenerPersonFound");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_find, container, false);

        editText = (EditText) rootView.findViewById(R.id.editText);


        buttonBack = (Button) rootView.findViewById(R.id.buttonBack);



        langEN = (LinearLayout) rootView.findViewById(R.id.langEN);
        langRU = (LinearLayout) rootView.findViewById(R.id.langRU);

        buttonOptions = (ImageView) rootView.findViewById(R.id.buttonOptions);



        if (ListTickets.isEmpty()){
            buttonBack.setVisibility(View.GONE);

            langEN.setVisibility(View.VISIBLE);
            langRU.setVisibility(View.VISIBLE);
            buttonOptions.setVisibility(View.VISIBLE);
        }
        else{

            buttonBack.setVisibility(View.VISIBLE);

            langEN.setVisibility(View.INVISIBLE);
            langRU.setVisibility(View.INVISIBLE);
            buttonOptions.setVisibility(View.INVISIBLE);
        }


        buttonBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                return;

            }
        });

        buttonNext = (Button) rootView.findViewById(R.id.buttonNext);
        buttonNext.setEnabled(false);

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                String contract = editText.getText().toString();

                if (contract.length() < CONTRACT_MIN_LENGTH){
                    buttonNext.setEnabled(false);
                }
                else{
                    buttonNext.setEnabled(true);
                }

                /*Log.d(TAG, "onTextChanged: " + contract);*/
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) { }


        });

        editText.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    Log.d(TAG, "enter_key_called");

                    FindContract();

                }
                return false;
            }
        });


        buttonNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                FindContract();

                return;

            }
        });


        buttonOptions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkPassword();

                return;

            }
        });


        final Activity self = this.getActivity();
        final Resources res = self.getResources();
        final DisplayMetrics dm = res.getDisplayMetrics();
        final android.content.res.Configuration conf = res.getConfiguration();

        langEN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Change locale settings in the app.

                conf.locale = new Locale("en");
                res.updateConfiguration(conf, dm);
                self.recreate();

                return;

            }
        });

        langRU.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Change locale settings in the app.

                conf.locale = new Locale("ru");
                res.updateConfiguration(conf, dm);
                self.recreate();

                return;

            }
        });



        return rootView;
    }



    private void checkPassword() {

        /***  ERROR DIALOG ***/
        final AlertDialog.Builder error = new AlertDialog.Builder(this.getActivity());
        error.setTitle(R.string.error);
        error.setMessage(R.string.wrong_password);

        error.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });


        final AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());


        //alert.setTitle("Title");
        alert.setMessage(R.string.password);

        final EditText input = new EditText(this.getActivity());
        alert.setView(input);

        alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Editable value = input.getText();

                if (value.toString().equalsIgnoreCase("masterkey")){
                    setOptions();
                }
                else{
                    error.show();
                }

            }
        });

        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }





    private void setOptions() {

        final Activity self = this.getActivity();

        final AlertDialog.Builder alert = new AlertDialog.Builder(self);

        String ip = model.getServerIP();
        alert.setTitle(R.string.options);
        alert.setMessage(R.string.server_address + ": " + ip);



        final EditText input = new EditText(self);

        input.setText(ip);

        alert.setView(input);

        alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Editable value = input.getText();

                try {
                    model.setServerIP(value.toString());

                    new AlertDialog.Builder(self)
                            .setMessage(R.string.ip_has_changed)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // whatever...
                                }
                            }).create().show();
                }
                catch (Exception e){


                    new AlertDialog.Builder(self)
                            .setTitle(R.string.error)
                            .setMessage(R.string.server_not_found)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    setOptions();
                                }
                            }).create().show();
                }

            }
        });

        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }





    private void FindContract(){

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        String contract = editText.getText().toString();
        editText.setText("");

        Log.d(TAG, "Button click" + contract);


        Person person = model.getPerson(contract);


        if (person == null){

            Log.e(TAG, "Contract not found !!! " + contract);

            ErrorMessage();

            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

            return;
        }

        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);


        mListener.onPersonFound(person);
    }





    public void ErrorMessage(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.error))
                .setMessage("\n" + getString(R.string.person_not_found) + "\n")
                //.setIcon(R.drawable.ic_android_cat)
                .setCancelable(false)
                .setNegativeButton(getString(R.string.next),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = builder.create();
        alert.show();
    }


}
