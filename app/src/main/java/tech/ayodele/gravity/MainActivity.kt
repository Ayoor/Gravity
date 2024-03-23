package tech.ayodele.gravity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawableContainerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager.widget.ViewPager

class MainActivity : AppCompatActivity() {

    private var mSliderViewPager: ViewPager? = null
    private var mDotLayout: LinearLayout? = null
    private lateinit var skipTextView: TextView
    private lateinit var backTextView: TextView
    private lateinit var nextTextView: TextView
    private lateinit var getStatedBtn: Button
    lateinit var dots: Array<TextView>
    private var viewPagerAdapter: ViewPagerAdapter? = null
    private val testBool= true

    private val PREFS_NAME = "MyPrefsFile"
    private val PREF_ONBOARDING_COMPLETE = "onboarding_complete"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val onboardingCompleted = prefs.getBoolean(PREF_ONBOARDING_COMPLETE, false)

        if (!onboardingCompleted) {
//            Log.i("kk", "$testBool")
            enableEdgeToEdge()
            setContentView(R.layout.activity_main)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            skipTextView = findViewById(R.id.skipET)
            backTextView = findViewById(R.id.backET)
            nextTextView = findViewById(R.id.nextET)
            getStatedBtn = findViewById(R.id.get_started)


            backTextView.setOnClickListener(View.OnClickListener {
                if (getItem() > 0) {
                    mSliderViewPager!!.setCurrentItem(getItem() - 1, true)
                }
            })
            skipTextView.setOnClickListener {
                val editor = prefs.edit()
                editor.putBoolean(PREF_ONBOARDING_COMPLETE, true)
                editor.apply()

                val intent: Intent = Intent(this@MainActivity, Signin::class.java)
                startActivity(intent)
                finish()
            }

            nextTextView.setOnClickListener {
                if (getItem() < 6) {
                    mSliderViewPager!!.setCurrentItem(getItem() + 1, true)
                }
            }

            getStatedBtn.setOnClickListener {



                val editor = prefs.edit()
                editor.putBoolean(PREF_ONBOARDING_COMPLETE, true)
                editor.apply()

                val intent: Intent = Intent(this@MainActivity, Signin::class.java)
                startActivity(intent)
                finish()
            }

            mSliderViewPager = findViewById<ViewPager>(R.id.sliderViewPager2)!!

            mDotLayout = findViewById<LinearLayout>(R.id.indicatorLayout)!!

            viewPagerAdapter = ViewPagerAdapter(this)

            mSliderViewPager!!.setAdapter(viewPagerAdapter)
            setupIndicator(0)
            mSliderViewPager!!.addOnPageChangeListener(viewListener)
        }
        else {
            val editor = prefs.edit()
            editor.putBoolean(PREF_ONBOARDING_COMPLETE, true)
            editor.apply()

            val intent: Intent = Intent(this@MainActivity, Signin::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun getItem(): Int {
        return mSliderViewPager!!.currentItem
    }



    fun setupIndicator(position: Int) {
        mDotLayout?.removeAllViews()
        for (i in 0 until 7) {
            val dot = TextView(this)
            dot.text = Html.fromHtml("&#8226;")
            dot.textSize = 55f
            dot.setTextColor(ContextCompat.getColor(this, R.color.white))
            mDotLayout?.addView(dot)
        }
        (mDotLayout?.getChildAt(position) as? TextView)?.setTextColor(ContextCompat.getColor(this, R.color.black))
    }

    private val viewListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            setupIndicator(position)
            backTextView.visibility = if (position > 0) View.VISIBLE else View.INVISIBLE
            nextTextView.visibility = if (position < 6) View.VISIBLE else View.INVISIBLE
            getStatedBtn.visibility = if (position == 6) View.VISIBLE else View.INVISIBLE
            mDotLayout?.visibility = if (position < 6) View.VISIBLE else View.INVISIBLE
            skipTextView.visibility = if (position < 6) View.VISIBLE else View.INVISIBLE
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }

}