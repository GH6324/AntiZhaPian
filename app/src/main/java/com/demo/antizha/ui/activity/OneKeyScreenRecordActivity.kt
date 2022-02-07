package com.demo.antizha.ui.activity

import android.view.View
import android.widget.RadioGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.antizha.R
import com.demo.antizha.databinding.ActivityOneKeyScreenRecordBinding
import qiu.niorgai.StatusBarCompat


class OneKeyScreenRecordActivity : BaseActivity() {
    private lateinit var infoBinding: ActivityOneKeyScreenRecordBinding

    override fun initPage() {
        infoBinding = ActivityOneKeyScreenRecordBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
        StatusBarCompat.translucentStatusBar(this, true, false)
        infoBinding.piTitle.tvTitle.text = "音频录制"
        infoBinding.piTitle.tvTitle.setTypeface(typ_ME)
        infoBinding.recyclerview.setLayoutManager(LinearLayoutManager(this,
            RecyclerView.HORIZONTAL,
            false))
        infoBinding.chronometer.setVisibility(View.INVISIBLE)
        infoBinding.rgMediaType.setOnCheckedChangeListener(OnCheckedChangeListener())
        infoBinding.piTitle.ivBack.setOnClickListener {
            finish()
        }
    }

    fun OnMediaIsVideo() {
        infoBinding.ivAudioRecord.setVisibility(View.GONE)
        infoBinding.ivVideoRecord.setVisibility(View.VISIBLE)
        infoBinding.video.setChecked(true)
        infoBinding.audio.setChecked(false)
    }

    fun OnMediaIsAudio() {
        infoBinding.ivAudioRecord.setVisibility(View.VISIBLE)
        infoBinding.ivVideoRecord.setVisibility(View.GONE)
        infoBinding.audio.setChecked(true)
        infoBinding.video.setChecked(false)
    }

    inner class OnCheckedChangeListener internal constructor() :
        RadioGroup.OnCheckedChangeListener {
        override fun onCheckedChanged(radioGroup: RadioGroup, ResId: Int) {
            if (ResId == R.id.video) {
                OnMediaIsVideo()
            } else if (ResId == R.id.audio) {
                OnMediaIsAudio()
            }
        }
    }

}