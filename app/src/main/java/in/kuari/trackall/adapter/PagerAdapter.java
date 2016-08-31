package in.kuari.trackall.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;

import in.kuari.trackall.R;
import in.kuari.trackall.fragments.BookMarkFragment;
import in.kuari.trackall.fragments.CourierFragment;
import in.kuari.trackall.fragments.ECommerceFragment;
import in.kuari.trackall.fragments.FlightsFragment;


/**
 * Created by root on 7/31/16.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[] { "Tab1", "Tab2", "Tab3" };
    private Context context;

    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment= BookMarkFragment.newInstance(position);
        switch (position){
            case 0:
                fragment=BookMarkFragment.newInstance(position);
                break;
            case 1:
                fragment= CourierFragment.newInstance(position);
                break;
            case 2:
                fragment= ECommerceFragment.newInstance(position);

                break;
            case 3:
                fragment= FlightsFragment.newInstance(position);

                break;
        }

        return fragment;
    }

   /* @Override
    public CharSequence getPageTitle(int position) {
        Log.d("Positoon",position+"");
        Drawable image = ContextCompat.getDrawable(context, imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString("");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BASELINE);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Generate title based on item position
        return sb;//tabTitles[position];
    }*/
}