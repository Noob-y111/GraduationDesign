package com.example.graduationdesign.view.main


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.*
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.graduationdesign.R
import com.example.graduationdesign.base.BaseActivity
import com.example.graduationdesign.base.BaseFragment
import com.example.graduationdesign.base.FixFragmentNavigator
import com.example.graduationdesign.databinding.ActivityMainBinding
import com.example.graduationdesign.model.bean.User
import com.example.graduationdesign.tools.ToastUtil
import com.example.graduationdesign.view.club.MusicClubFragment
import com.example.graduationdesign.view.explore.ExploreFragment
import com.example.graduationdesign.view.local.LocalFragment
import com.example.graduationdesign.view.play_page.PlayerDialogFragment
import com.example.graduationdesign.view.songlist.SongListFragment
import com.example.imitationqqmusic.model.tools.ScreenUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import java.util.*

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun getToolBarId() = R.id.toolbar

    override fun initView() {
        viewModel = MainActivityViewModel.newInstance(this)
        initContainer()
//        searchBoxWidth()
        binding.mainFooter.clFooter.setOnClickListener {
            PlayerDialogFragment().show(supportFragmentManager, null)
        }
        intent.getBundleExtra("user")?.let {
            viewModel.getUser(it)
        }


        //observe
        viewModel.toastString.observe(this, {
            ToastUtil.show(this, it)
        })

        viewModel.hideBottomNavigationView.observe(this, {
            if (!it)
                binding.bottomNavigation.visibility = View.VISIBLE
            else
                binding.bottomNavigation.visibility = View.GONE
        })
    }

    override fun setToolBarTitle() = ""

    override fun getContentView(): View {
        binding = ActivityMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(0, 0, 0, "设置")?.setIcon(R.drawable.ic_settings_24)
            ?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return super.onCreateOptionsMenu(menu)
    }

//    private fun initContainer() {
//        val container = supportFragmentManager.findFragmentById(R.id.main_container)
//        val navController =
//            (container as NavHostFragment).navController
//        val navigatorProvider = navController.navigatorProvider
//        val fixFragmentNavigator =
//            FixFragmentNavigator(this, container.childFragmentManager, R.id.main_container)
//        navigatorProvider.addNavigator(fixFragmentNavigator)
//        navController.graph = initNavGraph(navigatorProvider, fixFragmentNavigator)
//
//        val inflater = navController.navInflater
//        val graph = inflater.inflate(R.navigation.main_navigation)
//        navController.graph = graph
//
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            viewModel.shouldHideView(
//                destination.id
//            )
//        }
//
//        binding.bottomNavigation.setOnNavigationItemSelectedListener {
//            navController.navigate(it.itemId)
//            return@setOnNavigationItemSelectedListener true
//        }
//    }

//    private fun initNavGraph(
//        provider: NavigatorProvider,
//        fixFragmentNavigator: FixFragmentNavigator
//    ): NavGraph {
//        val navGraph = NavGraph(NavGraphNavigator(provider))
//
//        val destination1 = fixFragmentNavigator.createDestination().also {
//            it.id = R.id.musicClubFragment
//            MusicClubFragment::class.java.canonicalName?.let { it1 -> it.className = it1 }
//        }
//        navGraph.addDestination(destination1)
//
//        val destination2 = fixFragmentNavigator.createDestination().also {
//            it.id = R.id.exploreFragment
//            ExploreFragment::class.java.canonicalName?.let { it1 -> it.className = it1 }
//        }
//        navGraph.addDestination(destination2)
//
//        val destination3 = fixFragmentNavigator.createDestination().also {
//            it.id = R.id.localFragment
//            LocalFragment::class.java.canonicalName?.let { it1 -> it.className = it1 }
//        }
//        navGraph.addDestination(destination3)
//
//        navGraph.startDestination = destination1.id
//        return navGraph
//    }

    private fun initContainer() {

//        user navigation controller
        val navController =
            (supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment).navController
//        setSupportActionBar(binding.mainToolbar.toolbar)
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            viewModel.shouldHideView(destination.id)
        }
//        val appBarConfiguration = AppBarConfiguration.Builder(binding.bottomNavigation.menu).build()
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

//        viewpager2 滑动监听
//        binding.mainContainer.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                binding.bottomNavigation.menu[position].isChecked = true
//            }
//        })
//        viewModel.setFragmentList(ArrayList<BaseFragment>().also {
//            it.add(MusicClubFragment())
//            it.add(ExploreFragment())
//            it.add(LocalFragment())
//        })
//        binding.mainContainer.adapter = object : FragmentStateAdapter(this) {
//            override fun getItemCount() = viewModel.getFragmentListSize()
//            override fun createFragment(position: Int) = viewModel.getFragmentByPosition(position)
//        }
//        binding.mainContainer.isUserInputEnabled = false
//        binding.mainContainer.offscreenPageLimit = 3
//
//        binding.bottomNavigation.setOnNavigationItemSelectedListener {
//            when (it.itemId) {
//                R.id.musicClubFragment -> {
//                    binding.mainContainer.setCurrentItem(0, false)
//                }
//                R.id.exploreFragment -> {
//                    binding.mainContainer.setCurrentItem(1, false)
//                }
//                R.id.localFragment -> {
//                    binding.mainContainer.setCurrentItem(2, false)
//                }
//            }
//            return@setOnNavigationItemSelectedListener true
//        }
//    }

//    private fun searchBoxWidth(){
//        val width = ScreenUtils.getWidth(this) / 5 * 2
//        binding.mainToolbar.tvSearch.layoutParams.width = width
    }

}