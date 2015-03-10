package com.siberianhealth.eventregistration;

import android.app.FragmentTransaction;
//import android.app.ActionBarActivity;
import android.app.FragmentManager;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.siberianhealth.eventregistration.fragments.FragmentConfirm;
import com.siberianhealth.eventregistration.fragments.FragmentFind;
import com.siberianhealth.eventregistration.fragments.FragmentTickets;
import com.siberianhealth.eventregistration.model.Person;

import java.util.Locale;


public class MainActivity extends Activity implements FragmentFind.OnListenerPersonFound,
                                                      FragmentConfirm.OnListenerPersonConfirmed,
                                                      FragmentTickets.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";

    public FragmentManager fragmentManager;
    public FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        hideNavBar();

        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {

            Resources res = this.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            android.content.res.Configuration conf = res.getConfiguration();

            conf.locale = new Locale("ru");
            res.updateConfiguration(conf, dm);

            PersonFind();

            //Person person = new Person();

            /*
            person.setContract("1005041");
            person.setId(2629);

            SelectTickets(person);
            */

        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // refresh your views here
        super.onConfigurationChanged(newConfig);
    }


    public void OnTicketsAddPerson(){
        PersonFind();
    }


    public void OnTicketsExit(){


        PersonFind();
    }



    private void PersonFind(){

        // Create fragment and give it an argument for the selected article
        FragmentFind newFragment = new FragmentFind();
        Bundle args = new Bundle();
        //args.putInt(ArticleFragment.ARG_POSITION, position);
        newFragment.setArguments(args);

        fragmentTransaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        fragmentTransaction.replace(R.id.container, newFragment);
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();
    }


    public void onPersonFound(Person person){


        Log.d(TAG, "onPersonFound: " + person.Name());

        PersonConfirm(person);
    }


    private void PersonConfirm(Person person){

        // Create fragment and give it an argument for the selected article
        FragmentConfirm newFragment = new FragmentConfirm();
        Bundle args = new Bundle();
        args.putString(FragmentConfirm.PERSON_CONTRACT, person.Contract());
        newFragment.setArguments(args);


        fragmentTransaction = getFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        fragmentTransaction.replace(R.id.container, newFragment);
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();
    }


    public void OnPersonConfirmed(){


        // Create fragment and give it an argument for the selected article
        FragmentTickets newFragment = new FragmentTickets();
        Bundle args = new Bundle();
        //args.putInt(Person.PERSON_ID, person.Id());
        newFragment.setArguments(args);


        fragmentTransaction = getFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        fragmentTransaction.replace(R.id.container, newFragment);
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();
    }


/*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        hideNavBar();
    }




    private void hideNavBar() {
        if (Build.VERSION.SDK_INT >= 19) {
            View v = getWindow().getDecorView();
            v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
