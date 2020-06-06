package com.versa.helloar

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment

class AugmentedView : AppCompatActivity() {

    private lateinit var arF: ArFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_augmented_view)
        Toast.makeText(applicationContext,
            "Augmented Views",
            Toast.LENGTH_SHORT).show()
        arF = supportFragmentManager.findFragmentById(R.id.viewfrag) as ArFragment
        arF.setOnTapArPlaneListener { hitResult, _, _ ->
            createViewRenderable(hitResult.createAnchor())
        }
    }

    private fun createViewRenderable(anchor: Anchor?) {
        ViewRenderable
            .builder()
            .setView(this, R.layout.test)
            .build()
            .thenAccept {
                addToScene(it, anchor)
            }
    }

    private fun addToScene(viewRenderable: ViewRenderable?, anchor: Anchor?) {
        val anchorNode = AnchorNode(anchor)
        anchorNode.renderable = viewRenderable
        arF.arSceneView.scene.addChild(anchorNode)
        val view = viewRenderable!!.view
        val viewPager = view.findViewById<ViewPager>(R.id.viewPager)
        val images = arrayListOf<Int>()
        images.add(R.drawable.ic_launcher_background)
        images.add(R.drawable.ic_launcher_foreground)
        images.add(R.drawable.ic_launcher_background)
        images.add(R.drawable.ic_launcher_foreground)
        images.add(R.drawable.ic_launcher_background)
        images.add(R.drawable.ic_launcher_foreground)

        val adapter = Adapter(images, applicationContext)
        viewPager.adapter = adapter
    }

    class Adapter(private var images: List<Int>, private var context: Context) : PagerAdapter() {

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater.inflate(
                R.layout.item, container, false
            )
            val imageView = view.findViewById<ImageView>(R.id.imageView)
            imageView.setImageResource(images[position])
            container.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as ImageView)
        }

        override fun getCount(): Int {
            return images.size
        }

    }
}
