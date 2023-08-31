package com.example.namo.ui.bottom.diary.mainDiary

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.namo.R
import com.example.namo.data.entity.home.Event
import com.example.namo.data.remote.diary.*
import com.example.namo.databinding.FragmentDiaryAddBinding
import com.example.namo.ui.bottom.diary.mainDiary.adapter.GalleryListAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat

class DiaryAddFragment : Fragment() {  // 다이어리 추가 화면

    private var _binding: FragmentDiaryAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var galleryAdapter: GalleryListAdapter

    private lateinit var repo: DiaryRepository
    private var imgList = arrayListOf<String>()
    private lateinit var event: Event

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDiaryAddBinding.inflate(inflater, container, false)

        hideBottomNavigation(true)

        galleryAdapter = GalleryListAdapter(requireContext())

        repo = DiaryRepository(requireContext())
        setEvent()
        charCnt()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        if (imgList.isEmpty()) {
            binding.diaryGalleryClickIv.visibility = View.VISIBLE
        }
        onClickListener()
    }


    @SuppressLint("SimpleDateFormat")
    private fun setEvent() {

        event = (arguments?.getSerializable("event") as? Event)!!

        val category = repo.getCategory(event.categoryIdx, event.categoryServerIdx)

        context?.resources?.let {
            binding.itemDiaryCategoryColorIv.background.setTint(category.color)
        }

        binding.apply {

            val formatDate = SimpleDateFormat("yyyy.MM.dd (EE)").format(event.startLong * 1000)
            diaryTodayDayTv.text = SimpleDateFormat("EE").format(event.startLong * 1000)
            diaryTodayNumTv.text = SimpleDateFormat("dd").format(event.startLong * 1000)
            diaryTitleTv.isSelected = true  // marquee
            diaryTitleTv.text = event.title

            if (event.placeName.isEmpty()) diaryInputPlaceTv.text = "장소 없음"
            else diaryInputPlaceTv.text = event.placeName


            diaryInputDateTv.text = formatDate
        }

    }


    private fun onClickListener() {

        binding.apply {

            diaryBackIv.setOnClickListener {
                findNavController().popBackStack()
                hideBottomNavigation(false)
            }

            diaryEditTv.setOnClickListener {
                insertData()
            }

            diaryGalleryClickIv.setOnClickListener {
                getGallery()
            }
            onRecyclerView()
        }


    }

    /** 다이어리 추가 **/
    private fun insertData() {

        val content = binding.diaryContentsEt.text.toString()

        if (content.isEmpty() && imgList.isEmpty()) {
            Snackbar.make(binding.root, "내용이나 이미지를 추가해주세요!", Snackbar.LENGTH_SHORT).show()
            return
        } else {
            repo.addDiary(event.eventId, content, imgList, event.serverIdx)
            view?.findNavController()?.navigate(R.id.homeFragment)
            hideBottomNavigation(false)
        }

    }


    private fun hasImagePermission(): Boolean { // 갤러리 권한 여부
        val writePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val readPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        return writePermission == PackageManager.PERMISSION_GRANTED && readPermission == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("IntentReset")
    private fun getGallery() {

        if (hasImagePermission()) {  // 권한 있으면 갤러리 불러오기

            val intent = Intent(Intent.ACTION_PICK).apply {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            }

            intent.type = "image/*"
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)   //다중 이미지 가져오기

            getImage.launch(intent)

            binding.diaryGalleryClickIv.visibility = View.GONE
            binding.diaryGallerySavedRy.visibility = View.VISIBLE

        } else {  // 없으면 권한 받기
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                ),
                200
            )

            binding.diaryGalleryClickIv.visibility = View.VISIBLE
        }
    }


    private val getImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == RESULT_OK) {

            if (result.data?.clipData != null) { // 사진 여러개 선택한 경우
                val count = result.data?.clipData!!.itemCount
                if (count > 3) {
                    Toast.makeText(requireContext(), "사진은 3장까지 선택 가능합니다.", Toast.LENGTH_SHORT)
                        .show()
                    binding.diaryGalleryClickIv.visibility = View.VISIBLE
                    return@registerForActivityResult
                } else {
                    for (i in 0 until count) {
                        val imageUri = result.data?.clipData!!.getItemAt(i).uri
                        imgList.add(imageUri.toString())
                    }
                }
            } else { // 단일 선택
                result.data?.data?.let {
                    val imageUri: Uri? = result.data!!.data
                    if (imageUri != null) {
                        imgList.add(imageUri.toString())
                    }
                }
            }
        }
        galleryAdapter.addImages(imgList)
    }

    private fun onRecyclerView() {

        val galleryViewRVAdapter = galleryAdapter
        binding.diaryGallerySavedRy.adapter = galleryViewRVAdapter
        binding.diaryGallerySavedRy.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    /** 글자 수 반환 **/
    private fun charCnt() {
        with(binding) {
            diaryContentsEt.addTextChangedListener(object : TextWatcher {
                var maxText = ""
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    maxText = s.toString()
                }

                @SuppressLint("SetTextI18n")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (diaryContentsEt.length() > 200) {
                        Toast.makeText(
                            requireContext(), "최대 200자까지 입력 가능합니다",
                            Toast.LENGTH_SHORT
                        ).show()

                        diaryContentsEt.setText(maxText)
                        diaryContentsEt.setSelection(diaryContentsEt.length())
                        if (s != null) {
                            textNumTv.text = "${s.length} / 200"
                        }
                    } else {
                        textNumTv.text = "${s.toString().length} / 200"
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }

            })
        }
    }

    private fun hideBottomNavigation(bool: Boolean) {
        val bottomNavigationView: BottomNavigationView =
            requireActivity().findViewById(R.id.nav_bar)
        if (bool) {
            bottomNavigationView.visibility = View.GONE
        } else {
            bottomNavigationView.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
        hideBottomNavigation(false)
    }


}