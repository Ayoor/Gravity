package tech.ayodele.gravity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ViewPagerAdapterJava extends PagerAdapter {

    Context context;
    //    arraw of the slider images
    int[] images = {
            R.drawable.gravity,
            R.drawable.personalization,
            R.drawable.community,
            R.drawable.diet,
            R.drawable.resources,
            R.drawable.help,
            R.drawable.community2
    };
    //    arraw of the slider titles
    int[] titles = {
            R.string.Title1,
            R.string.Title2,
            R.string.Title3,
            R.string.Title4,
            R.string.Title5,
            R.string.Title6,
            R.string.Title7
    };

    int[] descriptions = {
            R.string.desc1,
            R.string.desc2,
            R.string.desc3,
            R.string.desc4,
            R.string.desc5,
            R.string.desc6,
            R.string.desc7
    };
    public ViewPagerAdapterJava(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layout =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layout.inflate(R.layout.slider_layout, container, false);

        ImageView sliderImageView = (ImageView) view.findViewById(R.id.sliderImageView);
        TextView sliderTextView = (TextView) view.findViewById(R.id.sliderTitle);
        TextView sliderDescription = (TextView) view.findViewById(R.id.sliderDescription);
        sliderImageView.setImageResource(images[position]);
        sliderTextView.setText(titles[position]);
        sliderDescription.setText(descriptions[position]);
        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
