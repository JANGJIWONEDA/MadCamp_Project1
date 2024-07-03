package com.example.project1.diary

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.project1.R
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

class Diary_frag2 : Fragment() {

    private lateinit var gridView: GridView
    private lateinit var myGridAdapter: MyGridAdapter

    private val imageDataList = ArrayList<ImageData>()

    private var isGridViewLatice = false // 그리드뷰 상태를 저장하는 변수 추가

    private var isChecked = false // 버튼의 상태를 나타내는 변수

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diary_frag2, container, false)

        gridView = view.findViewById<GridView>(R.id.gridView) ?: run {
            Log.e("Diary_frag2", "GridView not found in layout")
            return view
        }

        myGridAdapter = MyGridAdapter(requireContext())
        gridView.adapter = myGridAdapter

        // Fragment가 생성될 때 이미지 데이터를 로드
        loadSavedImageData()

        val laticeButton = view.findViewById<Button>(R.id.latice_button)
        laticeButton.setOnClickListener {
            isChecked = !isChecked // 상태 토글
            laticeButton.setBackgroundResource(if (isChecked) R.drawable.list else R.drawable.latice)
            toggleGridViewLatice()
        }

        return view
    }

    private fun toggleGridViewLatice() {
        if (isGridViewLatice) {
            // 원래 상태로 변경
            gridView.numColumns = 1
            myGridAdapter.setImageSize(650, 650)
        } else {
            // Latice 상태로 변경
            gridView.numColumns = 4
            myGridAdapter.setImageSize(250, 250)
        }
        isGridViewLatice = !isGridViewLatice // 상태를 토글
    }

    // JSON 파일에서 이미지 데이터를 로드
    private fun loadSavedImageData() {
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

            // '제주도' 태그가 있는 이미지들을 필터링하여 adapter에 추가
            val name = arguments?.getString("tag") ?: ""
            filterImagesWithTag(name)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun filterImagesWithTag(tagToFilter: String) {
        for (imageData in imageDataList) {
            if (imageData.tag == tagToFilter) {
                myGridAdapter.addImage(imageData)
            }
        }
    }

    inner class MyGridAdapter(private val context: android.content.Context) : BaseAdapter() {
        private var imageWidth = 650
        private var imageHeight = 650

        private val imageDataList = ArrayList<ImageData>()

        override fun getCount(): Int {
            return imageDataList.size
        }

        override fun getItem(position: Int): Any {
            return imageDataList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        fun setImageSize(width: Int, height: Int) {
            imageWidth = width
            imageHeight = height
            notifyDataSetChanged()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val imageView = convertView as? ImageView ?: ImageView(context).apply {
                layoutParams = ViewGroup.LayoutParams(imageWidth, imageHeight)
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

        fun addImage(imageData: ImageData) {
            imageDataList.add(imageData)
            notifyDataSetChanged()
        }

        private fun showImageDialog(imageData: ImageData) {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.image_dialog, null)
            val dialogImageView = dialogView.findViewById<ImageView>(R.id.dialogImageView)
            val textViewDescription = dialogView.findViewById<TextView>(R.id.textViewDescription)
            val textViewTag = dialogView.findViewById<TextView>(R.id.textViewTag)

            dialogImageView.setImageURI(imageData.imageUri) // 이미지 설정
            textViewDescription.text = "가고 싶은 곳: ${imageData.description}"  // 텍스트 설정
            textViewTag.text = "여행지: ${imageData.tag}" // 텍스트 설정

            AlertDialog.Builder(context)
                .setView(dialogView)
                .setPositiveButton("Close") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    data class ImageData(val imageUri: Uri, val description: String, val tag: String)

    companion object {
        fun newInstance(number: Int): Diary_frag2 {
            val fragment = Diary_frag2()
            val bundle = Bundle()
            bundle.putInt("number", number)
            fragment.arguments = bundle
            return fragment
        }
    }
}
