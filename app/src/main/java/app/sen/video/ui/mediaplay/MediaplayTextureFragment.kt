package app.sen.video.ui.mediaplay

import android.Manifest
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import app.sen.video.R
import app.sen.video.utils.T
import com.permissionx.guolindev.PermissionX
import kotlinx.android.synthetic.main.fragment_texture_mediaplay.*
import java.io.File

/**
 * A simple [Fragment] subclass.
 * Use the [MediaplayTextureFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MediaplayTextureFragment : Fragment() {

    private lateinit var mediaPlay: MediaPlayer
    val videoFile = File("/sdcard/miui/gallery/cloud/owner/抖音/IMG_6331.MP4")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_texture_mediaplay, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener{
            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture?,
                width: Int,
                height: Int
            ) {
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                return true
            }

            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                val texture: SurfaceTexture = textureView.getSurfaceTexture()
                val surface = Surface(texture)
                PermissionX.init(activity)
                    .permissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .request { allGranted, grantedList, deniedList ->
                        if (allGranted) {
                            mediaPlay = MediaPlayer.create(requireActivity(),Uri.fromFile(videoFile))
                            mediaPlay.setSurface(surface)
                            mediaPlay.start()
                        } else {
                            T.normal("请授予相关权限").show()
                        }
                    }
            }
        }


    }


}