package salazar.com.coursify;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class ReviewCourseFrag extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    public static final String TAG = ReviewCourseFrag.class.getSimpleName();

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;


    public static ReviewCourseFrag newInstance() {
        return new ReviewCourseFrag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.review_course_frag, container, false);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity(),getChildFragmentManager());
        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        return v;
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        Context ctxt=null;


        public SectionsPagerAdapter(Context ctxt , FragmentManager fm) {
            super(fm);
            this.ctxt=ctxt;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            if (position == 0){
                 return new ReviewFrag1();
            }
            else if(position == 1){
                return new ReviewFrag2();
            }
            else{
                return new ReviewFrag3();
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Page 1";
                case 1:
                    return "Page 2";
                case 2:
                    return "Page 3";
            }
            return null;
        }
    }


}
