package com.example.cameratest2

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Imgproc.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        OpenCVLoader.initDebug()
        var thresh = findViewById<TextView>(R.id.thresh)
        thresh.setText("150")
        var b = findViewById<Button>(R.id.button)
        b.setOnClickListener {
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            startActivityForResult(intent,200)

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 200 && data != null){
            var thresh = findViewById<TextView>(R.id.thresh)

            var imageView = findViewById<ImageView>(R.id.imageView)
            var res = countObjects(data.extras?.get("data") as Bitmap,thresh.text.toString())
            imageView.setImageBitmap(res.first)
            var text = findViewById<TextView>(R.id.textView)
            text.setText("I found  " + res.second.toString() + "  Objects")


                 /*
            var number = makeGray(data.extras?.get("data") as Bitmap)
            var text = findViewById<TextView>(R.id.textView)
            text.setText(number.toString())

                  */
        }
    }
    fun countObjects(bitmap: Bitmap, string: String) : Pair<Bitmap,Int> {
        val mat = Mat()
        val tmp = Mat()
        val thr = Mat()
        val hier = Mat()
        val contours: List<MatOfPoint> = ArrayList()
        val final = bitmap.copy(bitmap.config, true)
        Utils.bitmapToMat(bitmap, mat)
        blur(mat,mat, Size(3.0,3.0));
        cvtColor(mat,tmp, Imgproc.COLOR_RGB2GRAY);
        threshold(tmp,thr,string.toDouble(),255.0, THRESH_BINARY_INV);

        findContours(thr,contours,hier,Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE)

        Utils.matToBitmap(thr, final)
        return  Pair<Bitmap,Int>(final,contours.size);



    }
}