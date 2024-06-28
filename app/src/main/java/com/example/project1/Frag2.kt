package com.example.project1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog

@Suppress("DEPRECATION")
class Frag2 : Fragment() {
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

        return view
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            if (selectedImageUri != null) {
                myGridAdapter.addImage(selectedImageUri)
            }
        }
    }

    inner class MyGridAdapter(private val context: Context) : BaseAdapter() {
        private val imageUris = ArrayList<Uri>()

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
                scaleType = ImageView.ScaleType.FIT_CENTER
                setPadding(5, 5, 5, 5)
            }

            val imageUri = getItem(position) as Uri
            imageView.setImageURI(imageUri)

            imageView.setOnClickListener {
                showImageDialog(imageUri)
            }

            return imageView
        }

        fun addImage(uri: Uri) {
            imageUris.add(uri)
            notifyDataSetChanged()
        }

        private fun showImageDialog(imageUri: Uri) {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.image, null)
            val dialogImageView = dialogView.findViewById<ImageView>(R.id.ImageView)
            dialogImageView.setImageURI(imageUri)

            AlertDialog.Builder(context)
                .setView(dialogView)
                .setPositiveButton("Close") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}