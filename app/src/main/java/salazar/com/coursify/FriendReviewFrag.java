package salazar.com.coursify;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendReviewFrag extends Fragment {
    ArrayList<String> friendList = new ArrayList<String>();
    View rootView;

    List<Review> performanceListForAdapter=new ArrayList<Review>();
    ListView performanceListView;
    LayoutInflater itemInflator;
    private String  friendListString;
    private ProgressDialog progressDialog;

    public FriendReviewFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemInflator=getLayoutInflater(savedInstanceState);
        rootView=inflater.inflate(R.layout.fragment_friend_review, container, false);



        populateList();//from DB
        String[] myArray = friendList.toArray(new String[friendList.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, myArray);
        final MultiAutoCompleteTextView textView = (MultiAutoCompleteTextView) rootView.findViewById(R.id.multiAutoCompleteTextView2);
        textView.setAdapter(adapter);
        textView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        textView.setThreshold(1);

        final Button button = (Button) rootView.findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (textView.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Friends not selected!", Toast.LENGTH_SHORT).show();
                } else {
                    String temp=textView.getText().toString();
                    System.out.println("Before temp "+temp);
                    if (temp != null && temp.length() > 0 && temp.charAt(temp.length() - 1) == ',') {
                        temp = temp.substring(0, temp.length() - 1);
                    }
                    System.out.println("After temp "+temp);
                    friendListString = temp;

                    progressDialog= new ProgressDialog(getActivity());
                    progressDialog= ProgressDialog.show(getActivity(), "Retrieving all Reviews","Connecting to DB..", true);

                    GetPerformanceAsyncTask tsk = new GetPerformanceAsyncTask(getActivity());
                    try {
                        performanceListForAdapter = tsk.execute(friendListString).get();
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


        //populateListView();
        //registerClickCallback();
        return rootView;
    }

    private void populateList() {
        String friendListString=SharedPrefLibrary.loadFriendList(getActivity());
        JSONArray friendsListJson= null;
        try {
            friendsListJson = new JSONArray(friendListString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            for (int l = 0; l < friendsListJson.length(); l++) {
                Friend friendObj=new Friend(friendsListJson.getJSONObject(l).getString("name"));
                friendList.add(friendsListJson.getJSONObject(l).getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    private void populateListView() {
        ArrayAdapter<String> listViewAdapter=new CourseListAdapter();
        ListView courseListView= (ListView)rootView.findViewById(R.id.reviewListView);
        courseListView.setAdapter(listViewAdapter);
    }

    private void registerClickCallback() {
        ListView courseListView=(ListView)rootView.findViewById(R.id.reviewListView);
        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                String name = friendList.get(position).toString();
                //String name = clickedContact.getName();
                System.out.println("FriendReview "+name+ " selected");
                //Toast.makeText(CotactListViewer.this, name, Toast.LENGTH_LONG).show();

            }
        });
    }

    private class CourseListAdapter extends ArrayAdapter<String>{
        public CourseListAdapter(){
            super(getActivity(), R.layout.contact, friendList);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View contactItemView=convertView;

            if (contactItemView==null){
                /*
                 convertView = mInflater.inflate(R.layout.contact_item, null);
                        holder = new ViewHolder();
                        holder.txtname = (TextView) convertView.findViewById(R.id.lv_contact_item_name);
                        holder.txtphone = (TextView) convertView.findViewById(R.id.lv_contact_item_phone); */

                contactItemView=itemInflator.inflate(R.layout.contact, parent, false);
            }

            //find contact
            String currentContactName= friendList.get(position);
            //fill view
            TextView name_textView=(TextView)contactItemView.findViewById(R.id.nameTv);
            name_textView.setText(currentContactName);
            return contactItemView;


        }

    }

    public class GetPerformanceAsyncTask extends AsyncTask<String, Void, List<Review>> {
        private Activity mActivity;
        private Context mContext;


        // Constructor
        public GetPerformanceAsyncTask(Activity activity)
        {
            this.mActivity= activity;
            mContext= activity;

            // Here we create a new instance of the ProgressDialog with the context   received as parameter
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected List<Review> doInBackground(String... strings) {
            String frListStr=strings[0];
            List<String> items = Arrays.asList(frListStr.split("\\s*,\\s*"));
            System.out.println("Final Check!!!!!"+items.toString());


            MongoClientURI uri2 = new MongoClientURI("mongodb://manisha:manisha@ds011732.mlab.com:11732/coursify_db");
            MongoClient client2 = new MongoClient(uri2);
            DB db2 = client2.getDB(uri2.getDatabase());
            DBCollection table = db2.getCollection("review");
            BasicDBObject searchQuery = new BasicDBObject();
            for(int i=0;i<items.size();i++){

            searchQuery.put("Name", items.get(i));
            DBCursor dbCursor = table.find(searchQuery);
            System.out.println("dbCursor: " + dbCursor);
            while (dbCursor.hasNext()) {
                DBObject quizObjS = dbCursor.next();
                System.out.println("OBJECT: " + quizObjS);

                Review student = new Review();
                student.setUserName(quizObjS.get("Name").toString());
                student.courseName = quizObjS.get("courseName").toString();
                student.ratingBar1String = quizObjS.get("ratingBar1String").toString();
                student.ratingBar2String = quizObjS.get("ratingBar2String").toString();
                student.ratingBar3String = quizObjS.get("ratingBar3String").toString();
                student.ratingBar4String = quizObjS.get("ratingBar4String").toString();
                student.checkBox = quizObjS.get("checkBox").toString();
                student.comment = quizObjS.get("comment").toString();


                String quizID = (String) quizObjS.get("Name");
                String studentAnswer = (String) quizObjS.get("ratingBar1String");
                String correctOption = (String) quizObjS.get("ratingBar2String");
                String courseName = (String) quizObjS.get("checkBox");

                System.out.println("inside viewStudentPerformance");
                System.out.println(quizID + studentAnswer + correctOption + courseName);

                performanceListForAdapter.add(student);
            }
            }
            client2.close();
            return performanceListForAdapter;


        }


        @Override
        protected void onPostExecute( List<Review> performanceListForAdapter) {

            super.onPostExecute(performanceListForAdapter);
            progressDialog.dismiss();
            //getActivity().setContentView(R.layout.fragment_friend_review);



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




