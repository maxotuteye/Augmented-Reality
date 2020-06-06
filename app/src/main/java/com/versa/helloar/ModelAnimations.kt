package com.versa.helloar

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.SkeletonNode
import com.google.ar.sceneform.animation.ModelAnimator
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_model_animations.*

class ModelAnimations : AppCompatActivity() {

    lateinit var arF: ArFragment
    private var modelAnimator: ModelAnimator? = null
    private var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_model_animations)

        Toast.makeText(applicationContext, "Model Animations", Toast.LENGTH_SHORT).show()

        arF = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment
        arF.setOnTapArPlaneListener { hitResult, _, _ ->
            createModel(hitResult.createAnchor())
        }
    }

    private fun createModel(anchor: Anchor?) {
        ModelRenderable
            .builder()
            .setSource(this, Uri.parse("engine.sfb"))
            .build()
            .thenAccept { modelRenderable ->
                val anchorNode = AnchorNode(anchor)
                val skeletonNode = SkeletonNode()
                val transformableNode = TransformableNode(arF.transformationSystem)
                transformableNode.renderable = modelRenderable
                transformableNode.setParent(anchorNode)
                skeletonNode.setParent(transformableNode)
                skeletonNode.renderable = modelRenderable
                arF.arSceneView.scene.addChild(anchorNode)
                transformableNode.select()
                animateButton.setOnClickListener { animateModel(modelRenderable) }
            }
    }

    private fun animateModel(modelRenderable: ModelRenderable?) {
        if (modelAnimator != null && modelAnimator!!.isRunning)
            modelAnimator!!.end()
        if (i == modelRenderable!!.animationDataCount)
            i = 0

        val animationData = modelRenderable.getAnimationData(i)
        modelAnimator = ModelAnimator(animationData, modelRenderable)
        modelAnimator!!.start()
        i++
    }
}
