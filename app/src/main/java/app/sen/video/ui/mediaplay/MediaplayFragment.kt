package app.sen.video.ui.mediaplay

import android.Manifest
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import app.sen.video.R
import app.sen.video.utils.T
import com.permissionx.guolindev.PermissionX
import kotlinx.android.synthetic.main.fragment_mediaplay.*
import java.io.File

/**
 * A simple [Fragment] subclass.
 * Use the [MediaplayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MediaplayFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_mediaplay, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback{
            override fun surfaceChanged(
                holder: SurfaceHolder?,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
            }

            override fun surfaceCreated(holder: SurfaceHolder) {
                PermissionX.init(activity)
                    .permissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .request { allGranted, grantedList, deniedList ->
                        if (allGranted) {
                            mediaPlay = MediaPlayer.create(requireActivity(),Uri.fromFile(videoFile))
                            mediaPlay.setDisplay(holder)
                            mediaPlay.start()
                        } else {
                            T.normal("请授予相关权限").show()
                        }
                    }
            }
        })
    }


}