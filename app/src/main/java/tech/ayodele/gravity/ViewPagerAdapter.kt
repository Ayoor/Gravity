package tech.ayodele.gravity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
            //adapter for the onboarding screen viewpager

class ViewPagerAdapter(private var context: Context) : PagerAdapter() {
    //    arraw of the slider images
    private var images = intArrayOf(
        R.drawable.gravity,
        R.drawable.personalization,
        R.drawable.community_icon,
        R.drawable.diet,
        R.drawable.resources,
        R.drawable.helpicon,
        R.drawable.begin
    )

    //    arraw of the slider titles
    private var _titles = intArrayOf(
        R.string.Title1,
        R.string.Title2,
        R.string.Title3,
        R.string.Title4,
        R.string.Title5,
        R.string.Title6,
        R.string.Title7
    )
    val titles = _titles  // exposed variable
    private var descriptions = intArrayOf(
        R.string.desc1,
        R.string.desc2,
        R.string.desc3,
        R.string.desc4,
        R.string.desc5,
        R.string.desc6,
        R.string.desc7
    )


    //    get the count of the items in the slider

    override fun getCount(): Int {
        return _titles.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    //setup slider elements

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layout = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layout.inflate(R.layout.slider_layout, container, false)
        val sliderImageView = view.findViewById<View>(R.id.sliderImageView) as ImageView
        val sliderTextView = view.findViewById<View>(R.id.sliderTitle) as TextView
        val sliderDescription = view.findViewById<View>(R.id.sliderDescription) as TextView
        sliderImageView.setImageResource(images[position])
        sliderTextView.setText(_titles[position])
        sliderDescription.setText(descriptions[position])
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}
