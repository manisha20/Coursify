package salazar.com.coursify;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFrag2 extends Fragment {
    private RatingBar ratingBar1=null;
    private RatingBar ratingBar2=null;
    private RatingBar ratingBar3=null;
    private RatingBar ratingBar4=null;

    private static String ratingBar1String="9";
    private static String ratingBar2String="9";
    private static String ratingBar3String="9";
    private static String ratingBar4String="9";




    public ReviewFrag2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_review_frag2, container, false);
        ratingBar1 = (RatingBar) rootView.findViewById(R.id.ratingBar1);
        ratingBar2 = (RatingBar) rootView.findViewById(R.id.ratingBar2);
        ratingBar3 = (RatingBar) rootView.findViewById(R.id.ratingBar3);
        ratingBar4 = (RatingBar) rootView.findViewById(R.id.ratingBar4);


        ratingBar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                ratingBar1String=String.valueOf(rating);

            }
        });
        ratingBar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                ratingBar2String=String.valueOf(rating);

            }
        });
        ratingBar3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                ratingBar3String=String.valueOf(rating);

            }
        });
        ratingBar4.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                ratingBar4String=String.valueOf(rating);

            }
        });

        return rootView;
    }
    public static Bundle getSecondFragmentData() {

        Bundle bundle=new Bundle();
        bundle.putString("ratingBar1String", ratingBar1String);
        bundle.putString("ratingBar2String", ratingBar2String);
        bundle.putString("ratingBar3String", ratingBar3String);
        bundle.putString("ratingBar4String", ratingBar4String);
        return bundle;
    }

}
