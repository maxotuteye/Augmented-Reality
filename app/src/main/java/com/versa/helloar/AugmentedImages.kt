package com.versa.helloar

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.rendering.ModelRenderable

class AugmentedImages : AppCompatActivity(), Scene.OnUpdateListener {

    private var arFragment = CustomArFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_augmented_images)

        arFragment = supportFragmentManager.findFragmentById(R.id.fragment)
                as CustomArFragment
        arFragment.arSceneView.scene.addOnUpdateListener(this)
    }

    fun setupDatabase(config: Config, session: Session) {
        val foxBitmap =
            BitmapFactory.decodeResource(resources, R.drawable.fox)
        val aid = AugmentedImageDatabase(session)
        aid.addImage("fox", foxBitmap)
        config.augmentedImageDatabase = aid
    }

    override fun onUpdate(p0: FrameTime?) {
        val frame = arFragment.arSceneView.arFrame
        val images =
            frame!!.getUpdatedTrackables(
                AugmentedImage::class.java
            )
        for (image in images) {
            if (image.trackingState == TrackingState.TRACKING) {
                if (image.name == "fox") {
                    val anchor = image.createAnchor(image.centerPose)
                    createModel(anchor)
                }
            }
        }
    }

    private fun createModel(anchor: Anchor?) {
        ModelRenderable.builder()
            .setSource(applicationContext, Uri.parse("ArcticFox_Posed.sfb"))
            .build()
            .thenAccept { placeModel(it, anchor) }
    }

    private fun placeModel(it: ModelRenderable?, anchor: Anchor?) {
        val anchorNode = AnchorNode(anchor)
        anchorNode.renderable = it
        arFragment.arSceneView.scene.addChild(anchorNode)
    }
}
