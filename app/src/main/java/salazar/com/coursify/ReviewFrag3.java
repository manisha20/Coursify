package salazar.com.coursify;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ListAdapter;
import android.widget.Toast;


import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ReviewFrag3 extends Fragment {
    EditText ed;

    public ReviewFrag3() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_review_frag3, container, false);
        ListView listOfCheckbox=(ListView)rootView.findViewById(R.id.lvMain);
        Context ctx = getActivity();
        Resources res = ctx.getResources();
        String[] options = res.getStringArray(R.array.countrynames);
        final CheckboxAdapter checkboxAdapterObj=new CheckboxAdapter(ctx, R.layout.checkbox_layout, options);
        listOfCheckbox.setAdapter(checkboxAdapterObj);

        ed=(EditText)rootView.findViewById(R.id.editText);
        Button button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean flag=true;
                String courseName=null;
                String comment=null;

                ArrayList<String> str = checkboxAdapterObj.getSelectedString();
                if(str.size()==0){
                    Toast.makeText(getActivity(), "Please select some tags!", Toast.LENGTH_SHORT).show();
                    flag=false;
                }
                System.out.println("CheckBox  " + str.toString());
                Bundle firstFragmentData = ReviewFrag1.getFirstFragmentData();
                if (firstFragmentData.get("courseName").toString().equals("xxx")) {
                    Toast.makeText(getActivity(), "CourseName not selected!", Toast.LENGTH_SHORT).show();
                    flag=false;
                }
                else {
                    courseName = firstFragmentData.get("courseName").toString();

                }
                if (ed.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Please post your comment", Toast.LENGTH_SHORT).show();
                    flag=false;
                }
                else{
                    comment = ed.getText().toString();


                }
                Bundle secondFragmentData = ReviewFrag2.getSecondFragmentData();
                String ratingBar1String = secondFragmentData.get("ratingBar1String").toString();
                String ratingBar2String = secondFragmentData.get("ratingBar2String").toString();
                String ratingBar3String = secondFragmentData.get("ratingBar3String").toString();
                String ratingBar4String = secondFragmentData.get("ratingBar4String").toString();
                if(ratingBar1String.equals("9")||
                        ratingBar1String.equals("9")||
                        ratingBar1String.equals("9")||
                        ratingBar1String.equals("9")){
                    Toast.makeText(getActivity(), "Page View 2 not completed!", Toast.LENGTH_SHORT).show();
                    flag=false;
                }

                String fname=SharedPrefLibrary.loadFirstName(getActivity());
                String lname=SharedPrefLibrary.loadLastName(getActivity());
                if(flag) {
                Review review = new Review();
                review.userName=fname+" "+lname;
                review.courseName=courseName;
                review.ratingBar1String=ratingBar1String;
                review.ratingBar2String=ratingBar2String;
                review.ratingBar3String=ratingBar3String;
                review.ratingBar4String=ratingBar4String;
                review.checkBox=str.toString();
                review.comment=comment;

                SaveToDb tsk = new SaveToDb();
                tsk.execute(review);
                }

            }
        });


        return rootView;
    }
    public class CheckboxAdapter extends ArrayAdapter<String> {
        private LayoutInflater mInflater;
        private String[] mStrings;
        private TypedArray mIcons;
        private int mViewResourceId;
        ArrayList<String> selectedStrings = new ArrayList<String>();

        public CheckboxAdapter(Context ctx,int viewResourceId,String[] strings){
            super(ctx,viewResourceId,strings);

            mInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mStrings = strings;
            mViewResourceId = viewResourceId;
        }

        public int getCount(){
            return mStrings.length;
        }

        public String getItem(int position){
            return mStrings[position];
        }

        public long getItemId(int position){
            return 0;
        }
        ArrayList<String> getSelectedString(){
            return selectedStrings;
        }
        public View getView(int position,View convertView,ViewGroup parent){
            convertView = mInflater.inflate(mViewResourceId, null);

            final CheckBox tv = (CheckBox)convertView.findViewById(R.id.checkBox1);
            tv.setText(mStrings[position]);
            tv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedStrings.add(tv.getText().toString());
                    } else {
                        selectedStrings.remove(tv.getText().toString());
                    }

                }
            });

            return convertView;
        }
    }

    public class SaveToDb extends AsyncTask<Review, Void, Boolean> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog= ProgressDialog.show(getActivity(), "Saving Review","Connecting to the database..", true);
        }
        @Override
        protected Boolean doInBackground(Review... arg0) {
            try
            {
                Review student = arg0[0];
                MongoClientURI uri = new MongoClientURI("mongodb://manisha:manisha@ds011732.mlab.com:11732/coursify_db");
                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());
                DBCollection studentCollection = db.getCollection("review");
                BasicDBObject reviewDoc = new BasicDBObject();
                reviewDoc.put("Name", student.userName);
                reviewDoc.put("courseName", student.courseName);
                reviewDoc.put("ratingBar1String", student.ratingBar1String);
                reviewDoc.put("ratingBar2String", student.ratingBar2String);
                reviewDoc.put("ratingBar3String", student.ratingBar3String);
                reviewDoc.put("ratingBar4String", student.ratingBar4String);
                reviewDoc.put("checkBox", student.checkBox);
                reviewDoc.put("comment", student.comment);
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
            Toast.makeText(getActivity(), "Review Submitted", Toast.LENGTH_SHORT).show();

        }
    }



}
