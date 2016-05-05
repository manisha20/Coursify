package salazar.com.coursify;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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


public class FriendsCoursePlanFrag extends Fragment {
    ArrayList<String> friendList = new ArrayList<String>();
    View rootView;

    List<CoursePlan> performanceListForAdapter = new ArrayList<CoursePlan>();
    ListView performanceListView;
    LayoutInflater itemInflator;
    private String friendListString;


    public FriendsCoursePlanFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_friends_course_plan, container, false);
        populateList();//from DB
        String[] myArray = friendList.toArray(new String[friendList.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, myArray);
        final MultiAutoCompleteTextView textView = (MultiAutoCompleteTextView) rootView.findViewById(R.id.multiAutoCompleteTextView2);
        textView.setAdapter(adapter);
        textView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        textView.setThreshold(1);

        Button button = (Button) rootView.findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (textView.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Friends not selected!", Toast.LENGTH_SHORT).show();
                } else {
                    String temp = textView.getText().toString();
                    System.out.println("Before temp " + temp);
                    if (temp != null && temp.length() > 0 && temp.charAt(temp.length() - 1) == ',') {
                        temp = temp.substring(0, temp.length() - 1);
                    }
                    System.out.println("After temp " + temp);
                    friendListString = temp;
                    GetPerformanceAsyncTask tsk = new GetPerformanceAsyncTask();
                    try {
                        performanceListForAdapter = tsk.execute(friendListString).get();
                        performanceListView = (ListView) rootView.findViewById(R.id.reviewListView);
                        ArrayAdapter<CoursePlan> listViewAdapter = new PerformanceListAdapter(getActivity(), R.layout.courseplan_listitem_view, performanceListForAdapter);
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

    private void populateList() {
        String friendListString = SharedPrefLibrary.loadFriendList(getActivity());
        JSONArray friendsListJson = null;
        try {
            friendsListJson = new JSONArray(friendListString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            for (int l = 0; l < friendsListJson.length(); l++) {
                Friend friendObj = new Friend(friendsListJson.getJSONObject(l).getString("name"));
                friendList.add(friendsListJson.getJSONObject(l).getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


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

    public class GetPerformanceAsyncTask extends AsyncTask<String, Void, List<CoursePlan>> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog= ProgressDialog.show(getActivity(), "Retrieving all Reviews","Connecting to DB..", true);
        }


        @Override
        protected List<CoursePlan> doInBackground(String... strings) {

            String frListStr=strings[0];
            List<String> items = Arrays.asList(frListStr.split("\\s*,\\s*"));

            MongoClientURI uri2 = new MongoClientURI("mongodb://manisha:manisha@ds011732.mlab.com:11732/coursify_db");
            MongoClient client2 = new MongoClient(uri2);
            DB db2 = client2.getDB(uri2.getDatabase());
            DBCollection table = db2.getCollection("courseplan");
            BasicDBObject searchQuery = new BasicDBObject();
            for(int i=0;i<items.size();i++){

                searchQuery.put("Name", items.get(i));
                DBCursor dbCursor = table.find(searchQuery);
                System.out.println("dbCursor: " + dbCursor);
                while (dbCursor.hasNext()) {
                    DBObject quizObjS = dbCursor.next();
                    System.out.println("OBJECT: " + quizObjS);

                    CoursePlan student = new CoursePlan();
                    student.setUserName(quizObjS.get("Name").toString());
                    student.setCourseList(quizObjS.get("coursePlan").toString());

                    System.out.println("inside viewStudentPerformance");
                    System.out.println(quizObjS.get("coursePlan").toString());

                    performanceListForAdapter.add(student);
                }
            }
            client2.close();
            return performanceListForAdapter;


        }


        @Override
        protected void onPostExecute( List<CoursePlan> performanceListForAdapter) {

            super.onPostExecute(performanceListForAdapter);
            progressDialog.dismiss();


        }


    }

    private class PerformanceListAdapter extends ArrayAdapter<CoursePlan> {
        private LayoutInflater mInflater;
        List<CoursePlan> mobjects;
        private int mViewResourceId;

        public PerformanceListAdapter(Context ctx, int viewResourceId,List<CoursePlan> objects) {
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
                courseItemView=mInflater.inflate(R.layout.courseplan_listitem_view, parent, false);
            }

            //find course
            CoursePlan currentPerformanceObj=performanceListForAdapter.get(position);
            String userName=currentPerformanceObj.getUserName();
            String courseName=currentPerformanceObj.getCourseList();



            TextView courseID_textView=(TextView)courseItemView.findViewById(R.id.textView2);
            courseID_textView.setText(userName);

            TextView courseID_textView2=(TextView)courseItemView.findViewById(R.id.textView31);
            courseID_textView2.setText(courseName);




            return courseItemView;
        }


    }

}
