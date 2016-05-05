package salazar.com.coursify;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AllReviewsFrag extends Fragment {
    List<Review> performanceListForAdapter=new ArrayList<Review>();
    ListView performanceListView;
    String courseName=null;

    public static AllReviewsFrag newInstance(String param1, String param2) {
        AllReviewsFrag fragment = new AllReviewsFrag();
        Bundle args = new Bundle();
        return fragment;
    }

    public AllReviewsFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView=inflater.inflate(R.layout.fragment_all_reviews, container, false);
        Context ctx = getActivity();
        Resources res = ctx.getResources();
        String[] str = res.getStringArray(R.array.courseListNames);
        AutoCompleteTextView t1 = (AutoCompleteTextView)
                rootView.findViewById(R.id.autoCompleteTextView2);

        ArrayAdapter<String> adp=new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line,str);

        t1.setThreshold(1);
        t1.setAdapter(adp);
        t1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                courseName = arg0.getItemAtPosition(arg2).toString();
            }
        });
        Button button = (Button) rootView.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (courseName == null || courseName.length() == 0){
                    Toast.makeText(getActivity(), "Select Course Name!", Toast.LENGTH_SHORT).show();
                }
                else {
                    GetPerformanceAsyncTask tsk = new GetPerformanceAsyncTask();
                    try {
                        performanceListForAdapter = tsk.execute(courseName).get();
                        performanceListView = (ListView) rootView.findViewById(R.id.reviewListView);
                        ArrayAdapter<Review> listViewAdapter = new PerformanceListAdapter(getActivity(), R.layout.review_listitem_view, performanceListForAdapter);
                        performanceListView.setAdapter(listViewAdapter);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
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
    public void onViewCreated (View view, Bundle savedInstanceState) {


    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public class GetPerformanceAsyncTask extends AsyncTask<String, Void, List<Review>> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog= ProgressDialog.show(getActivity(), "Retrieving all Reviews","Connecting to DB..", true);
        }


        @Override
        protected List<Review> doInBackground(String... strings) {

            String courseNameFromUser=strings[0];

            MongoClientURI uri2 = new MongoClientURI("mongodb://manisha:manisha@ds011732.mlab.com:11732/coursify_db");
            MongoClient client2 = new MongoClient(uri2);
            DB db2 = client2.getDB(uri2.getDatabase());
            DBCollection table = db2.getCollection("review");
            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.put("courseName", courseNameFromUser);
//                String details[] = new String[3];
            ArrayList<String> quizIDList = new ArrayList<String>();
//                ArrayList<String> option = new ArrayList<String>();
//                ArrayList<String> correct = new ArrayList<String>();

            DBCursor dbCursor = table.find(searchQuery);
            System.out.println("dbCursor: " + dbCursor);
            while (dbCursor.hasNext()){
                DBObject quizObjS = dbCursor.next();
                System.out.println("OBJECT: " + quizObjS);

                Review student = new Review();
                student.setUserName(quizObjS.get("Name").toString());
                student.courseName=quizObjS.get("courseName").toString();
                student.ratingBar1String=quizObjS.get("ratingBar1String").toString();
                student.ratingBar2String=quizObjS.get("ratingBar2String").toString();
                student.ratingBar3String=quizObjS.get("ratingBar3String").toString();
                student.ratingBar4String=quizObjS.get("ratingBar4String").toString();
                student.checkBox=quizObjS.get("checkBox").toString();
                student.comment=quizObjS.get("comment").toString();


                String quizID = (String) quizObjS.get("Name");
                String studentAnswer = (String) quizObjS.get("ratingBar1String");
                String correctOption = (String) quizObjS.get("ratingBar2String");
                String courseName = (String) quizObjS.get("checkBox");

                System.out.println("inside viewStudentPerformance");
                System.out.println(quizID + studentAnswer + correctOption + courseName);

                performanceListForAdapter.add(student);


            }

            client2.close();
            return performanceListForAdapter;


        }


        @Override
        protected void onPostExecute( List<Review> performanceListForAdapter) {

            super.onPostExecute(performanceListForAdapter);
            progressDialog.dismiss();

        }


    }

    private class PerformanceListAdapter extends ArrayAdapter<Review> {
        private LayoutInflater mInflater;
        List<Review> mobjects;
        private int mViewResourceId;

        public PerformanceListAdapter(Context ctx, int viewResourceId,List<Review> objects) {
            super(ctx,viewResourceId,objects);

            mInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mobjects = objects;
            mViewResourceId = viewResourceId;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View courseItemView=convertView;

            //to make sure we have a view
            if (courseItemView==null){
                courseItemView=mInflater.inflate(R.layout.review_listitem_view, parent, false);
            }

            //find course
            Review currentPerformanceObj=performanceListForAdapter.get(position);
            String userName=currentPerformanceObj.getUserName();
            String courseName=currentPerformanceObj.getCourseName();
            String ratingBar1String=currentPerformanceObj.getRatingBar1String();
            String ratingBar2String=currentPerformanceObj.getRatingBar2String();
            String ratingBar3String=currentPerformanceObj.getRatingBar3String();
            String ratingBar4String=currentPerformanceObj.getRatingBar4String();
            String checkBox=currentPerformanceObj.getCheckBox();
            String comment=currentPerformanceObj.getComment();


            TextView courseID_textView=(TextView)courseItemView.findViewById(R.id.textView17);
            courseID_textView.setText(userName);

            TextView courseID_textView2=(TextView)courseItemView.findViewById(R.id.textView18);
            courseID_textView2.setText(courseName);

            TextView courseName_textView=(TextView)courseItemView.findViewById(R.id.textView19);
            courseName_textView.setText(ratingBar1String);

            TextView courseCurrentRating_textView=(TextView)courseItemView.findViewById(R.id.textView22);
            courseCurrentRating_textView.setText(ratingBar2String);

            TextView courseCurrentRating_textView2=(TextView)courseItemView.findViewById(R.id.textView23);
            courseCurrentRating_textView2.setText(ratingBar3String);

            TextView courseCurrentRating_textView3=(TextView)courseItemView.findViewById(R.id.textView24);
            courseCurrentRating_textView3.setText(ratingBar4String);

            TextView courseCurrentRating_textView4=(TextView)courseItemView.findViewById(R.id.textView27);
            courseCurrentRating_textView4.setText(checkBox);

            TextView courseCurrentRating_textView5=(TextView)courseItemView.findViewById(R.id.textView28);
            courseCurrentRating_textView5.setText(comment);


            return courseItemView;
        }


    }


}
