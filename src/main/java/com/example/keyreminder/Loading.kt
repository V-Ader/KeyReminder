package com.example.keyreminder

import android.animation.AnimatorSet
import android.animation.ObjectAnimator.ofFloat
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock.sleep
import android.util.DisplayMetrics
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.thread


class Loading : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        val vimage : ImageView
        vimage = findViewById<ImageView>(R.id.image)

        val moveup= ofFloat(vimage, "y", -200f).setDuration(1000)
        val rotate = ofFloat(vimage, "rotation", 0f, 360f).setDuration(1000)
        val movedown= ofFloat(vimage, "y", 200f).setDuration(1000)
        movedown.interpolator = BounceInterpolator()

        AnimatorSet().apply {
            startDelay = 1000;
            play(moveup)
                .with(rotate)
            play(movedown)
                .after(moveup)
            start()
        }
        thread {
            sleep(4000);

            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent);

            finish();
        }




    }
}