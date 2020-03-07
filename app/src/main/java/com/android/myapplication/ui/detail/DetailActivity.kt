package com.android.myapplication.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.android.myapplication.R
import androidx.databinding.DataBindingUtil.setContentView
import com.android.myapplication.databinding.ActivityDetailBinding
import kotlinx.android.synthetic.main.activity_detail.*
import timber.log.Timber

/**
 * Created by Albert on 2020/3/6.
 * Description:
 */
class DetailActivity : AppCompatActivity() {
    private val args: DetailActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView<ActivityDetailBinding>(this,R.layout.activity_detail)
        val detailId = args.detailId
        Timber.e("detailId", detailId)
        toolbar.setNavigationOnClickListener {
           finish()
        }

    }
}