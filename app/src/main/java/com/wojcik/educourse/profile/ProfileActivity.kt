package com.wojcik.educourse.profile

import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.view.*

import com.wojcik.educourse.R
import com.wojcik.educourse.api.RetrofitClient
import com.wojcik.educourse.profile.badges.BadgesFragment
import com.wojcik.educourse.profile.data.UserResponse
import kotlinx.android.synthetic.main.activity_pofile.*
import kotlinx.android.synthetic.main.fragment_pofile.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    var username = ""
    var userId = 0

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pofile)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))


        val b : Bundle = intent.extras!!
        username = b.getString("username")!!

        RetrofitClient.instance.getUserByUsername(username)
            .enqueue(object: Callback<UserResponse> {
                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                }

                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    userId = response.body()!!.id

                }
            })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            var item : Fragment = InfoFragment()
            when (position) {
                0 -> {
                    item = InfoFragment()
                    val args = Bundle()
                    args.putString("username", username)
                    item.arguments = args
                }
                1 -> {
                    item = UpdateFragment()
                    val args = Bundle()
                    args.putString("username", username)
                    item.arguments = args
                }
                2 -> {
                    item = BadgesFragment()
                    val args = Bundle()
                    args.putInt("userId", userId)
                    item.arguments = args
                }
            }
            return item
        }

        override fun getCount(): Int {
            return 3
        }
    }

    class PlaceholderFragment : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(R.layout.fragment_pofile, container, false)
            rootView.section_label.text = getString(R.string.section_format, arguments?.getInt(ARG_SECTION_NUMBER))
            return rootView
        }

        companion object {

            private const val ARG_SECTION_NUMBER = "section_number"

        }
    }

}
