package com.versa.helloar

import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var arF: ArFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arF = arFragment as ArFragment
        arF.setOnTapArPlaneListener { hitResult, _, motionEvent ->
            val anchor = hitResult.createAnchor()
            Toast.makeText(applicationContext, "Trackable: ${hitResult.trackable}",Toast.LENGTH_SHORT).show()
            ModelRenderable.builder()
                .setSource(
                    this,
                    Uri.parse("ArcticFox_Posed.sfb")
                )
                .build()
                .thenAccept { modelRenderable: ModelRenderable? ->
                    addModelToScene(anchor, modelRenderable)
                }
                .exceptionally {
                    val dialog = AlertDialog.Builder(this)
                    dialog.setMessage(it.message)
                        .show()
                    null
                }
        }
    }

    private fun addModelToScene(
        anchor: Anchor?,
        modelRenderable: ModelRenderable?
    ) {
        val anchorNode = AnchorNode(anchor)
        val transformableNode = TransformableNode(arF.transformationSystem)
        transformableNode.setParent(anchorNode)
        transformableNode.renderable = modelRenderable
        arF.arSceneView.scene.addChild(anchorNode)
        transformableNode.select()
    }
}
