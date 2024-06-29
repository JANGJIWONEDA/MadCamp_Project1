package com.example.project1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import android.Manifest;

@Suppress("DEPRECATION")
class Frag2 : Fragment() {
    private val PERMISSION_REQUEST_CODE = 1001 // 권한 요청 코드 정의

    private val PICK_IMAGE_REQUEST = 1
    private lateinit var gridView: GridView
    private lateinit var myGridAdapter: MyGridAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_frag2, container, false)

        gridView = view.findViewById(R.id.gridView)
        myGridAdapter = MyGridAdapter(requireContext())
        gridView.adapter = myGridAdapter

        val selectImageButton = view.findViewById<Button>(R.id.select_image_button)
        selectImageButton.setOnClickListener {
            openGallery()
        }

        // Fragment가 생성될 때 저장된 이미지 데이터를 로드
        readImageDataFromJsonFile()

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
        val imageUris = ArrayList<Uri>()
        val imageDatas = ArrayList<ImageData>()

        override fun getCount(): Int {
            return imageUris.size
        }

        override fun getItem(position: Int): Any {
            return imageUris[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val imageView = convertView as? ImageView ?: ImageView(context).apply {
                layoutParams = ViewGroup.LayoutParams(200, 200)
                scaleType = ImageView.ScaleType.CENTER_CROP
                setPadding(0, 0, 0, 0)
            }

            val imageUri = getItem(position) as Uri
            imageView.setImageURI(imageUri)

            val description = imageDatas.find { it.imageUri == imageUri }?.description ?: ""
            val tag = imageDatas.find { it.imageUri == imageUri }?.tag ?: ""

            imageView.setOnClickListener {
                showImageDialog(imageUri, description, tag)
            }

            return imageView
        }

        fun addImage(uri: Uri) {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.image_text_input_dialog, null)
            val dialogImageView = dialogView.findViewById<ImageView>(R.id.dialogImageView)
            val editTextDescription = dialogView.findViewById<EditText>(R.id.editTextDescription)
            val editTextTag = dialogView.findViewById<EditText>(R.id.editTextTag)
            dialogImageView.setImageURI(uri)

            val alertDialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .setPositiveButton("Select") { dialog, _ ->
                    val description = editTextDescription.text.toString()
                    val tag = editTextTag.text.toString()
                    imageDatas.add(ImageData(uri, description, tag))
                    imageUris.add(uri)
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

        private fun showImageDialog(imageUri: Uri, description: String, tag: String) {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.image_dialog, null)
            val dialogImageView = dialogView.findViewById<ImageView>(R.id.dialogImageView)
            val textViewDescription = dialogView.findViewById<TextView>(R.id.textViewDescription)
            val textViewTag = dialogView.findViewById<TextView>(R.id.textViewTag)

            dialogImageView.setImageURI(imageUri) // 이미지 설정
            textViewDescription.text = "가고 싶은 곳: $description"  // 텍스트 설정
            textViewTag.text = "여행지: $tag" // 텍스트 설정

            AlertDialog.Builder(context)
                .setView(dialogView)
                .setPositiveButton("Close") { dialog, _ ->
                    dialog.dismiss()
                }
                .setNegativeButton("Delete") { dialog, _ ->
                    val position = imageUris.indexOf(imageUri)
                    if (position != -1) {
                        imageUris.removeAt(position)
                        imageDatas.removeAt(position)
                        notifyDataSetChanged()

                        // 이미지가 삭제될 때마다 JSON 파일에 저장
                        writeImageDataToJsonFile()
                    }
                    dialog.dismiss()
                }
                .show()
        }
    }

    // ImageData 객체를 JSON 형식으로 변환
    private fun ImageData.toJson(): String {
        val jsonObject = JSONObject()
        jsonObject.put("imageUri", imageUri.toString())
        jsonObject.put("description", description)
        jsonObject.put("tag", description)
        return jsonObject.toString()
    }

    // JSON 파일에 ImageData 리스트를 저장
    private fun writeImageDataToJsonFile() {
        val jsonArray = JSONArray()
        for (imageData in myGridAdapter.imageDatas) {
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
                myGridAdapter.imageDatas.add(ImageData(imageUri, description, tag))
                myGridAdapter.imageUris.add(imageUri)
            }

            myGridAdapter.notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
