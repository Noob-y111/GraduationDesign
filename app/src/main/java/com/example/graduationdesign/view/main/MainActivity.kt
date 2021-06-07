package com.example.graduationdesign.view.main


import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.viewpager2.widget.ViewPager2
import com.example.graduationdesign.R
import com.example.graduationdesign.base.BaseActivity
import com.example.graduationdesign.databinding.ActivityMainBinding
import com.example.graduationdesign.model.bean.song_list_bean.SongBean
import com.example.graduationdesign.service.MyService
import com.example.graduationdesign.service.POSITION
import com.example.graduationdesign.service.SONG_LIST
import com.example.graduationdesign.tools.ToastUtil
import com.example.graduationdesign.view.current_list.CurrentListFragment
import com.example.graduationdesign.view.main.adapter.MainFooterAdapter
import com.example.graduationdesign.view.play_page.PlayerDialogFragment
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun getToolBarId() = R.id.toolbar

    override fun initView() {
        viewModel = MainActivityViewModel.newInstance(this)
        initContainer()
//        searchBoxWidth()
        bindForegroundServiceAndInitFooter()

        intent.getBundleExtra("user")?.let {
            viewModel.getUser(it)
        }

        setOnClickListener()

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

    private fun setOnClickListener() {
        binding.mainFooter.ivFooterPlayPause.setOnClickListener {
            viewModel.getBinder()?.playOrPause()
        }

        binding.mainFooter.ivCurrentList.setOnClickListener {
            CurrentListFragment(CurrentListFragment.ColorTheme.LIGHT).show(supportFragmentManager, null)
        }
    }

    val adapter = MainFooterAdapter(supportFragmentManager)
    private fun bindForegroundServiceAndInitFooter() {
        binding.mainFooter.pager2Footer.adapter = adapter
        binding.mainFooter.pager2Footer.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
//                viewModel.changeCurrentSong(position, ArrayList<SongBean>(adapter.currentList))
                viewModel.setMainFooterPosition(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                viewModel.mainFooterDragEnable(state)
            }
        })

        Intent(this, MyService::class.java).apply {
            startService(this)
            bindService(this, connection, Service.BIND_AUTO_CREATE)
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MyService.MyBinder
            viewModel.setBinder(binder)
            val mService = service.service

            mService.currentSongListAndPosition.observe(this@MainActivity, {
                val position = (it[POSITION] as Int) + 1
//                        if (binding.mainFooter.pager2Footer.currentItem == position)
//                            return@observe
                val list = it[SONG_LIST]
                if (list is ArrayList<*>) {
                    adapter.submitList(it[SONG_LIST] as ArrayList<SongBean>) {
                        binding.mainFooter.pager2Footer.setCurrentItem(
                            position,
                            false
                        )
                    }
                }
            })

            mService.canFooterShow.observe(this@MainActivity, {
                if (it){
                    binding.mainFooter.clFooter.visibility = View.VISIBLE
                }else{
                    binding.mainFooter.clFooter.visibility = View.GONE
                }
            })

            mService.progressBarDuration.observe(this@MainActivity, {
                binding.mainFooter.musicProgress.max = it
            })

            mService.progressBarPosition.observe(this@MainActivity, {
                binding.mainFooter.musicProgress.progress = it
            })

            mService.progressBarBuffer.observe(this@MainActivity, {
                binding.mainFooter.musicProgress.secondaryProgress = it
            })

            mService.stopOrResumeMediaPlayer.observe(this@MainActivity, {
                if (it){
                    binding.mainFooter.ivFooterPlayPause.setImageResource(R.drawable.footer_play)
                }else{
                    binding.mainFooter.ivFooterPlayPause.setImageResource(R.drawable.footer_pause)
                }
            })
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }

    }

    override fun setToolBarTitle() = ""

    override fun getContentView(): View {
        binding = ActivityMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
        stopService(Intent(this, MyService::class.java))
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

    override fun onBackPressed() {
        super.onBackPressed()

    }

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