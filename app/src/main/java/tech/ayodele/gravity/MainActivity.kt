package tech.ayodele.gravity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager.widget.ViewPager

class MainActivity : AppCompatActivity() {
    /* todo
        add GO name and hospital to the signup page (both optional)
    *   email on registration completed
    *   forgot password
    *   user questions -> 1. were you referred by a GP?
    *   dashboard
    *   get user's location
    *   Diet recommendations
    *   feedback ->
    *
    * */
//    variable declarations
    private var mSliderViewPager: ViewPager? = null
    private var mDotLayout: LinearLayout? = null
    private lateinit var skipTextView: TextView
    private lateinit var backTextView: TextView
    private lateinit var nextTextView: TextView
    private lateinit var getStatedBtn: Button
    lateinit var dots: Array<TextView>
    private var viewPagerAdapter: ViewPagerAdapter? = null
    private val testBool = true

    private val PREFS_NAME = "MyPrefsFile"
    private val PREF_ONBOARDING_COMPLETE = "onboarding_complete"

    //on Create method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val onboardingCompleted = prefs.getBoolean(PREF_ONBOARDING_COMPLETE, false)

        if (!onboardingCompleted) { //if first Time use
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

//            backward Scroll of the onBoarding screen
            backTextView.setOnClickListener(View.OnClickListener {

                if (getItem() > 0) {
                    mSliderViewPager!!.setCurrentItem(getItem() - 1, true)
                }
            })
//            skip first timetime onboarding screen
            skipTextView.setOnClickListener {
                val editor = prefs.edit()
                editor.putBoolean(PREF_ONBOARDING_COMPLETE, true)
                editor.apply()

                val intent: Intent = Intent(this@MainActivity, Signin::class.java)
                startActivity(intent)
                finish()
            }
            //forward scroll of the onBoarding screen
            nextTextView.setOnClickListener {
                if (getItem() < 6) {
                    mSliderViewPager!!.setCurrentItem(getItem() + 1, true)
                }
            }
//                finish onboarding and go to sign in
            getStatedBtn.setOnClickListener {


                val editor = prefs.edit()
                editor.putBoolean(PREF_ONBOARDING_COMPLETE, true)
                editor.apply()

                val intent = Intent(this@MainActivity, Signin::class.java)
                startActivity(intent)
                finish()
            }

            mSliderViewPager = findViewById<ViewPager>(R.id.sliderViewPager2)!!

            mDotLayout = findViewById<LinearLayout>(R.id.indicatorLayout)!!

            viewPagerAdapter = ViewPagerAdapter(this)

            mSliderViewPager!!.setAdapter(viewPagerAdapter)
            setupIndicator(0) // dots to show the progress
            mSliderViewPager!!.addOnPageChangeListener(viewListener)
        }
        else {  //onBoarding is complete hence not a new user
            val editor = prefs.edit()
            editor.putBoolean(PREF_ONBOARDING_COMPLETE, true)
            editor.apply()

            val intent: Intent = Intent(this@MainActivity, Signin::class.java)
            startActivity(intent)
            finish()
        }


    }

    // get current slide item
    private fun getItem(): Int {
        return mSliderViewPager!!.currentItem
    }



    private fun setupIndicator(position: Int) {
        val viewpagerAdapter = ViewPagerAdapter(this)
        val dotCount = viewpagerAdapter.titles
        mDotLayout?.removeAllViews()
        for (i in dotCount.indices) {
            val dot = TextView(this)
            dot.text = "•"
            dot.textSize = 55f
            dot.setTextColor(ContextCompat.getColor(this, R.color.white))
            mDotLayout?.addView(dot)
        }
        (mDotLayout?.getChildAt(position) as? TextView)?.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.black
            )
        )
    }

    private val viewListener = object : ViewPager.OnPageChangeListener {
        //default override method
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }
//handles what happens when a page is in view
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