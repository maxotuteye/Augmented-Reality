package com.versa.helloar

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.ar.core.AugmentedFace
import com.google.ar.core.Pose
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.Texture
import com.google.ar.sceneform.ux.AugmentedFaceNode
import com.google.ar.sceneform.ux.TransformableNode

class AugmentedFaces : AppCompatActivity() {

    private var modelRenderable: ModelRenderable? = null
    var texture: Texture? = null
    private var isAdded = false
    private var node: Node = Node()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_augmented_faces)

        Toast.makeText(applicationContext, "Augmented Faces", Toast.LENGTH_SHORT).show()

        val facesArFragment =
            supportFragmentManager.findFragmentById(R.id.arFragment) as FacesArFragment

        ModelRenderable
            .builder()
            .setSource(this, Uri.parse("starwarsmask.sfb"))
            .build()
            .thenAccept {
                modelRenderable = it
                modelRenderable!!.isShadowCaster = false
                modelRenderable!!.isShadowReceiver = false
            }

        Texture
            .builder()
            .setSource(this, R.drawable.fox_face_mesh_texture)
            .build()
            .thenAccept {
                this.texture = it
            }
        facesArFragment.arSceneView.cameraStreamRenderPriority = Renderable.RENDER_PRIORITY_FIRST
        facesArFragment.arSceneView.scene.addOnUpdateListener {
            if (modelRenderable == null || texture == null)
                return@addOnUpdateListener
            val frame = facesArFragment.arSceneView.arFrame
            val augmentedFaces: Collection<AugmentedFace> = frame!!
                .getUpdatedTrackables(AugmentedFace::class.java)
            for (face in augmentedFaces) {
                if (isAdded) return@addOnUpdateListener

                val augmentedFaceNode = AugmentedFaceNode(face)
                node.setParent(facesArFragment.arSceneView.scene)
                augmentedFaceNode.setParent(node)
                augmentedFaceNode.localScale = Vector3(0.15f, 0.15f, 0.15f)
                augmentedFaceNode.faceRegionsRenderable = modelRenderable
                augmentedFaceNode.faceMeshTexture = texture
                isAdded = true
            }
        }
    }
}
