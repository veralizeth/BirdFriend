package com.example.birdfriend

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.fragment.findNavController
import java.io.OutputStream


class CameraFragment : Fragment() {
    companion object {
        //image pick code
        val IMAGE_PICK_CODE = 1000;

        //Permission code
        val PERMISSION_CODE = 1001;
    }

    var youAndBird: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    var canvas: Canvas = Canvas(youAndBird)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.out_of_camera).setOnClickListener {
            findNavController().navigate(R.id.action_CameraFragment_to_SecondFragment)
        }

        val context = activity?.applicationContext
        view.findViewById<Button>(R.id.upload_button).setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context?.let { it1 ->
                        ContextCompat.checkSelfPermission(
                            it1,
                            Manifest.permission.CAMERA
                        )
                    } == PackageManager.PERMISSION_DENIED) {
                    val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        PERMISSION_CODE
                    )
                } else {
                    //permission already granted
                    pickImageFromGallery();
                }
            }
        }

        val shareButton = view.findViewById<Button>(R.id.share_load_button).setOnClickListener {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "image/jpeg"

            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "title")
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            val uri = requireActivity().contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )!!

            val outstream: OutputStream
            try {
                outstream = requireActivity().contentResolver.openOutputStream(uri)!!
                youAndBird.compress(Bitmap.CompressFormat.JPEG, 100, outstream)
                outstream.close()
            } catch (e: Exception) {
                System.err.println(e.toString())
            }

            share.putExtra(Intent.EXTRA_STREAM, uri)
            share.putExtra(Intent.EXTRA_TEXT, "you and bird")
            startActivity(Intent.createChooser(share, "Send your image!"))
        }

    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(
            intent,
            IMAGE_PICK_CODE
        ) // GIVE AN INTEGER VALUE FOR IMAGE_PICK_CODE LIKE 1000
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val loadimage = requireActivity().findViewById<ImageView>(R.id.load_image)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {

            val imageUri: Uri = data!!.data!!
            val original =
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(
                        requireActivity().contentResolver,
                        imageUri
                    )
                )
            youAndBird = original.copy(Bitmap.Config.ARGB_8888, true)
            Log.d("photo", data.data.toString())
            Log.d("photo", "it worked here!")

            canvas = Canvas(youAndBird)
            // Draw the image bitmap into the cavas
            canvas.drawBitmap(youAndBird, 0.0f, 0.0f, null)
            val bird = context?.getDrawable(R.drawable.fly_p1)
            canvas.drawBitmap(bird!!.toBitmap(), 0f, 0f, null)

            // Attach the canvas to the ImageView
            loadimage?.setImageDrawable(BitmapDrawable(resources, youAndBird))
        }
    }

}