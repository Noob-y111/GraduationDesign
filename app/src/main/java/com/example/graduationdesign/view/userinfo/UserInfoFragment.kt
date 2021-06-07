package com.example.graduationdesign.view.userinfo

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.graduationdesign.R
import com.example.graduationdesign.base.AppBarStateChangedListener
import com.example.graduationdesign.databinding.UserInfoFragmentBinding
import com.example.graduationdesign.tools.ToastUtil
import com.example.graduationdesign.view.dialog.content.ContentMenuAdapter
import com.example.graduationdesign.view.dialog.content.ContentMenuDialog
import com.example.graduationdesign.view.main.MainActivityViewModel
import com.example.graduationdesign.view.userinfo.adapter.UserInfoAdapter
import com.google.android.material.appbar.AppBarLayout


class UserInfoFragment : Fragment() {

    companion object {
        fun newInstance() = UserInfoFragment()
        private const val PERMISSION_CODE = 100
        private const val RESULT_CODE = 1000
        private const val IMAGE_CODE = 1001
        private const val TAKE_PHOTO = 1002
    }

    enum class Behavior {
        PHOTO_ALBUM, TAKE_PHOTO
    }

    private lateinit var viewModel: UserInfoViewModel
    private lateinit var mainViewModel: MainActivityViewModel
    private lateinit var binding: UserInfoFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UserInfoFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                RESULT_CODE -> {
                    data?.let {
                        it.data?.let { uri ->
                            imageClip(uri)
                        }
                    }
                }

                IMAGE_CODE -> {
                    val bundle = data?.extras
                    bundle?.let {
                        val bitmap = it.getParcelable<Bitmap>("data")
                        viewModel.updateAvatar(mainViewModel.user.value, bitmap)
                    }
                }

                TAKE_PHOTO -> {
                    data?.let {
                        it.data?.let { uri ->
//                        viewModel.getImageByUri(mainViewModel.user.value, uri)
                            imageClip(uri)
                        }
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                newContentMenu()
            } else {
                ToastUtil.show(requireContext(), "权限获取失败")
            }
        }
    }

    private fun newContentMenu() {
        ContentMenuDialog(
            ArrayList<String>().also { list ->
                list.add(resources.getString(R.string.change_head_by_album))
                list.add(resources.getString(R.string.change_head_by_camera))
                list.add(resources.getString(R.string.sava_head))
            },
            null
        ).setContentMenuTitle("你想做？")
            .setOnItemClickListener(object : ContentMenuAdapter.OnItemClickCallback {
                override fun onItemClickListener(
                    view: View,
                    position: Int,
                    dialog: ContentMenuDialog?
                ) {
                    startBehavior(position)
                    dialog?.dismiss()
                }
            }).show(requireActivity().supportFragmentManager, null)
    }

    private fun imageClip(uri: Uri) {
        Intent("com.android.camera.action.CROP").apply {
            setDataAndType(uri, "image/*")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            putExtra("crop", true)
            putExtra("aspectX", 1)
            putExtra("aspectY", 1)
            // outputX outputY 是裁剪图片宽高
            putExtra("outputX", 300)
            putExtra("outputY", 300)
            putExtra("return-data", true)
            startActivityForResult(this, IMAGE_CODE)
        }
    }

    private fun checkOpenPhotoPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.CAMERA
                ),
                PERMISSION_CODE
            )
        } else {
            newContentMenu()
        }
    }

    private fun startBehavior(position: Int) {
        when (position) {
            0 -> gotoPhotoAlbum()
            1 -> takePhoto()
            2 -> {

            }
        }
    }

    private fun takePhoto() {
        startActivityForResult(Intent("android.media.action.IMAGE_CAPTURE").apply {
//            val filePath = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
//                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.path
//            } else {
//                requireContext().filesDir.path
//            }
//            viewModel.returnNewFile(filePath)?.let {
//                val imageUri = FileProvider.getUriForFile(
//                    requireContext(),
//                    "com.example.graduationdesign.fileprovider",
//                    it
//                )
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(this, TAKE_PHOTO)
        }, TAKE_PHOTO)
    }

    private fun gotoPhotoAlbum() {
        startActivityForResult(
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ), RESULT_CODE
        )
    }

    private fun initBehavior() {
        binding.appbar.addOnOffsetChangedListener(object : AppBarStateChangedListener() {
            override fun onStateChanged(appbar: AppBarLayout?, state: State) {}

            override fun onStateChanged(appbar: AppBarLayout?, verticalOffset: Int) {
                appbar?.let {
                    viewModel.changeAlpha(1 - (verticalOffset.toFloat() / it.totalScrollRange))
                }
            }
        })

        binding.userInfoLabel.btnUserEdit.setOnClickListener {
            it.findNavController().navigate(R.id.action_userInfoFragment_to_changeUserFragment)
        }

        binding.userInfoLabel.ivUserHead.setOnClickListener {
//            checkOpenPhotoPermission()
        }

        binding.toolbar.setNavigationOnClickListener {
            it.findNavController().navigateUp()
        }
    }

    private fun userInitial() {
        mainViewModel.user.observe(viewLifecycleOwner, {
            viewModel.getUserDetail(it)
            viewModel.getUserPlaylist(it)
        })

        viewModel.background.observe(viewLifecycleOwner, {
            Glide.with(this)
                .load(it)
                .error(R.drawable.shimmer_bg)
                .placeholder(R.drawable.shimmer_bg)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .into(binding.userInfoBackground)
        })

        viewModel.signature.observe(viewLifecycleOwner, {
            binding.userInfoLabel.tvUserFocusFans.text = it
        })

        viewModel.head.observe(viewLifecycleOwner, {
            Glide.with(this)
                .load(it)
                .error(R.drawable.shimmer_circle_bg)
                .placeholder(R.drawable.shimmer_circle_bg)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.userInfoLabel.ivUserHead)
        })

        viewModel.name.observe(viewLifecycleOwner, {
            binding.userInfoLabel.tvUserInfoName.text = it
            binding.toolbar.title = it
        })

        viewModel.backgroundAlpha.observe(viewLifecycleOwner, {
            binding.userInfoBackground.alpha = it
            binding.userInfoLabel.userInfoLabelRoot.alpha = it
            if (it == 0.4f) {
                binding.userInfoLabel.userInfoLabelRoot.visibility = View.INVISIBLE
            } else {
                binding.userInfoLabel.userInfoLabelRoot.visibility = View.VISIBLE
            }
        })
    }

    private fun initRecycler() {
        val adapter = UserInfoAdapter(R.id.action_userInfoFragment_to_reuseListFragment)
        binding.userInfoRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        mainViewModel.localPlaylist.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = UserInfoViewModel.newInstance(this)
        mainViewModel = MainActivityViewModel.newInstance(requireActivity())
        viewModel.setModel(mainViewModel.getDataModel())
        initBehavior()
        userInitial()
        initRecycler()
    }

}