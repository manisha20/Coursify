package salazar.com.coursify;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.util.ArrayList;
import java.util.Arrays;


public class ShareCoursePlanFrag extends Fragment {
    String coursePlanString;

    public static ShareCoursePlanFrag newInstance(String param1, String param2) {
        ShareCoursePlanFrag fragment = new ShareCoursePlanFrag();

        return fragment;
    }

    public ShareCoursePlanFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View rootView=inflater.inflate(R.layout.fragment_share_course_plan, container, false);
        Context ctx = getActivity();
        Resources res = ctx.getResources();
        String[] str = res.getStringArray(R.array.courseListNames);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, str);
        final MultiAutoCompleteTextView textView = (MultiAutoCompleteTextView) rootView.findViewById(R.id.multiAutoCompleteTextView);
        textView.setAdapter(adapter);
        textView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        textView.setThreshold(1);

        Button button = (Button) rootView.findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("TextViewData "+textView.getText());
                if (textView.getText().length()==0){
                    Toast.makeText(getActivity(), "Please Select Some Courses!", Toast.LENGTH_SHORT).show();
                }
                else{
                    String temp=textView.getText().toString().trim();
                    System.out.println("before "+temp+" "+temp.charAt(temp.length() - 1));
                    if (temp != null && temp.length() > 0 && temp.charAt(temp.length() - 1) == ',') {
                        temp = temp.substring(0, temp.length() - 1);
                        coursePlanString=temp;
                    }
                    System.out.println("after "+temp);
                    System.out.println("coursePlanString"+coursePlanString);

                    SaveCoursePlanToDb tsk=new SaveCoursePlanToDb();
                    tsk.execute(coursePlanString);
                }

            }
        });





        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public class SaveCoursePlanToDb extends AsyncTask<String, Void, Boolean> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog= ProgressDialog.show(getActivity(), "Saving ","Connecting to the database..Please Wait", true);
        }
        @Override
        protected Boolean doInBackground(String... arg0) {
            try
            {
                String coursePlan = arg0[0];
                MongoClientURI uri = new MongoClientURI("mongodb://manisha:manisha@ds011732.mlab.com:11732/coursify_db");
                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());
                DBCollection studentCollection = db.getCollection("courseplan");
                BasicDBObject reviewDoc = new BasicDBObject();
                reviewDoc.put("Name", SharedPrefLibrary.loadName(getActivity()));
                reviewDoc.put("coursePlan", coursePlan);
                studentCollection.insert(reviewDoc);
                return true;


            }
            catch (Exception e)
            {
                return false;
            }
        }
        @Override
        protected void onPostExecute( Boolean performanceListForAdapter) {
            super.onPostExecute(performanceListForAdapter);
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Submitted", Toast.LENGTH_SHORT).show();

        }
    }



}