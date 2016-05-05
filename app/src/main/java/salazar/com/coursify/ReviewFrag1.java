package salazar.com.coursify;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFrag1 extends Fragment {
    static String textFromTV="xxx";

    public ReviewFrag1() {
        // Required empty public constructor
    }

    public static ReviewFrag1 newInstance() {
        ReviewFrag1 fragment = new ReviewFrag1();

        return fragment;
    }
    public static Bundle getFirstFragmentData() {

        Bundle bundle=new Bundle();
        if (textFromTV.equals("xxx")) {
            bundle.putString("courseName","xxx");
        }
        else {
            bundle.putString("courseName", textFromTV);
        }
        return bundle;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_review_frag1, container, false);
        Context ctx = getActivity();
        Resources res = ctx.getResources();
        String[] str = res.getStringArray(R.array.courseListNames);

        AutoCompleteTextView t1 = (AutoCompleteTextView)
                rootView.findViewById(R.id.autoCompleteTextView1);

        ArrayAdapter<String> adp=new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line,str);

        t1.setThreshold(1);
        t1.setAdapter(adp);
        t1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //Log.i("SELECTED TEXT WAS", str[arg2]);
                //Log.i("SELECTED TEXT WAS", arg0.getItemAtPosition(arg2).toString());
                textFromTV=arg0.getItemAtPosition(arg2).toString();
            }
        });
        return rootView;
    }


}
