package com.example.project1.photobox

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.project1.R
import com.example.project1.MainActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import com.example.project1.MainMenu
import com.example.project1.diary.database.DiaryDatabase
import com.example.project1.diary.repository.DiaryRepository

class Frag2 : Fragment() {
    private val PERMISSION_REQUEST_CODE = 1001 // 권한 요청 코드 정의

    private val PICK_IMAGE_REQUEST = 1
    private lateinit var gridView: GridView
    private lateinit var myGridAdapter: MyGridAdapter

    private val imageDataList = ArrayList<ImageData>()
    private var filteredImageDataList = ArrayList<ImageData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_frag2, container, false)

        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.animation3)
        view.startAnimation(animation)



        gridView = view.findViewById<GridView>(R.id.gridView)
        myGridAdapter = MyGridAdapter(requireContext())
        gridView.adapter = myGridAdapter

        // Fragment가 생성될 때 이미지 데이터를 로드
        readImageDataFromJsonFile()

        // SearchView 설정
        val searchView = view.findViewById<SearchView>(R.id.search_view)
        val searchText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        val customFont: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.font1)
        searchText.typeface = customFont

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 검색 버튼을 눌렀을 때 처리
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 검색어가 변경될 때마다 호출되는 처리
                filterImages(newText)
                return true
            }
        })

        // Select Image 버튼 설정
        val selectImageButton = view.findViewById<Button>(R.id.select_image_button)
        selectImageButton.setOnClickListener {
            openGallery()
        }

        return view
    }

    private fun openGallery() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE)
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                // Handle permission denied
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            if (selectedImageUri != null) {
                myGridAdapter.addImage(selectedImageUri)
            }
        }
    }

    data class ImageData(val imageUri: Uri, val description: String, val tag: String)

    inner class MyGridAdapter(private val context: Context) : BaseAdapter() {
        var imageDataList = ArrayList<ImageData>()

        fun setImageDataList(imageDataList: List<ImageData>) {
            this.imageDataList = ArrayList(imageDataList)
            notifyDataSetChanged()
        }

        override fun getCount(): Int {
            return imageDataList.size
        }

        override fun getItem(position: Int): Any {
            return imageDataList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val imageView = convertView as? ImageView ?: ImageView(context).apply {
                layoutParams = ViewGroup.LayoutParams(250, 250)
                scaleType = ImageView.ScaleType.CENTER_CROP
                setPadding(0, 0, 0, 0)
            }

            val imageData = getItem(position) as ImageData
            imageView.setImageURI(imageData.imageUri)

            imageView.setOnClickListener {
                showImageDialog(imageData)
            }

            return imageView
        }

        fun addImage(uri: Uri) {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.image_text_input_dialog, null)
            val dialogImageView = dialogView.findViewById<ImageView>(R.id.dialogImageView)
            val editTextDescription = dialogView.findViewById<EditText>(R.id.editTextDescription)
            val autoCompleteImageTag : AutoCompleteTextView = dialogView.findViewById<AutoCompleteTextView>(R.id.editTextTag)

            autoCompleteImageTag.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    autoCompleteImageTag.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    adjustDropDownPosition(autoCompleteImageTag)
                }
            })

            dialogImageView.setImageURI(uri)
            val diaryRepository = DiaryRepository(DiaryDatabase(requireContext()))
            diaryRepository.getAllDiaryTags().observe(viewLifecycleOwner, Observer { tags ->
                tags?.let {
                    autoCompleteImageTag.setAdapter(
                        ArrayAdapter<String>(
                            context,
                            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                            tags
                        )
                    )
                }
            })

            val alertDialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .setPositiveButton("Select") { dialog, _ ->
                    val description = editTextDescription.text.toString()
                    val tag = autoCompleteImageTag.text.toString()
                    imageDataList.add(ImageData(uri, description, tag))
                    notifyDataSetChanged()
                    dialog.dismiss()

                    // 이미지 데이터가 추가될 때마다 JSON 파일에 저장
                    writeImageDataToJsonFile()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        private fun showImageDialog(imageData: ImageData) {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.image_dialog, null)
            val dialogImageView = dialogView.findViewById<ImageView>(R.id.dialogImageView)
            val textViewDescription = dialogView.findViewById<TextView>(R.id.textViewDescription)
            val textViewTag = dialogView.findViewById<TextView>(R.id.textViewTag)

            dialogImageView.setImageURI(imageData.imageUri) // 이미지 설정
            textViewDescription.text = "가고 싶은 곳: ${imageData.description}"  // 텍스트 설정
            textViewTag.text = "여행지: ${imageData.tag}" // 텍스트 설정

            val alertDialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .setPositiveButton("Close") { dialog, _ ->
                    dialog.dismiss()
                }
                .setNegativeButton("Delete") { dialog, _ ->
                    // 이미지 삭제 처리
                    deleteImage(imageData)
                    dialog.dismiss()
                }
                .show()
        }

        private fun deleteImage(imageData: ImageData) {
            imageDataList.remove(imageData)
            notifyDataSetChanged()

            // JSON 파일에 변경된 이미지 데이터를 저장
            writeImageDataToJsonFile()
        }
    }

    // ImageData 객체를 JSON 형식으로 변환
    private fun ImageData.toJson(): String {
        val jsonObject = JSONObject()
        jsonObject.put("imageUri", imageUri.toString())
        jsonObject.put("description", description)
        jsonObject.put("tag", tag)
        return jsonObject.toString()
    }

    // JSON 파일에 ImageData 리스트를 저장
    private fun writeImageDataToJsonFile() {
        val jsonArray = JSONArray()
        for (imageData in myGridAdapter.imageDataList) {
            jsonArray.put(JSONObject(imageData.toJson()))
        }

        try {
            val fileOutputStream = requireContext().openFileOutput("imageData.json", Context.MODE_PRIVATE)
            fileOutputStream.write(jsonArray.toString().toByteArray())
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // JSON 파일에서 ImageData 리스트를 읽어옴
    private fun readImageDataFromJsonFile() {
        try {
            val fileInputStream = requireContext().openFileInput("imageData.json")
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            fileInputStream.close()

            val jsonArray = JSONArray(stringBuilder.toString())
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val imageUri = Uri.parse(jsonObject.getString("imageUri"))
                val description = jsonObject.getString("description")
                val tag = jsonObject.getString("tag")
                imageDataList.add(ImageData(imageUri, description, tag))
            }

            // 필터링 없이 모든 이미지를 일단 추가
            filteredImageDataList.addAll(imageDataList)
            myGridAdapter.setImageDataList(filteredImageDataList)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // SearchView를 통해 이미지 필터링
    private fun filterImages(query: String?) {
        if (query.isNullOrBlank()) {
            // 검색어가 비어있으면 모든 이미지를 보여줌
            myGridAdapter.setImageDataList(imageDataList)
        } else {
            // 검색어가 있으면 해당 검색어를 포함하는 tag를 가진 이미지만 필터링
            filteredImageDataList.clear()
            for (imageData in imageDataList) {
                if (imageData.tag.startsWith(query, ignoreCase = true)) {
                    filteredImageDataList.add(imageData)
                }
            }
            myGridAdapter.setImageDataList(filteredImageDataList)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(requireActivity(), MainMenu::class.java).apply {
                }
                startActivity(intent)
                requireActivity().finish()
            }
        })
    }
    private fun adjustDropDownPosition(autoCompleteTextView: AutoCompleteTextView) {
        val location = IntArray(2)
        autoCompleteTextView.getLocationOnScreen(location)

        val screenHeight = resources.displayMetrics.heightPixels
        val dropDownHeight = autoCompleteTextView.dropDownHeight

        val spaceBelow = screenHeight - location[1] - autoCompleteTextView.height
        val spaceAbove = location[1]

        if (spaceAbove > dropDownHeight) {
            autoCompleteTextView.dropDownVerticalOffset = -autoCompleteTextView.height - dropDownHeight - 30
        }
    }
}
