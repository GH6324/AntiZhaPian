package com.demo.antizha.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.demo.antizha.R
import com.demo.antizha.ui.BaseDialog
import com.demo.antizha.ui.IClickListener
import com.demo.antizha.ui.IOneClickListener
import com.demo.antizha.ui.ProgressDialogBar


class DialogUtils {
    companion object {
        var progressDialogBar: ProgressDialogBar? = null

        fun showNormalDialog(context: Context?,
                             title: String?,
                             subTitle: String?,
                             cancelText: String?,
                             confirmText: String?,
                             iClickListener: IClickListener?): Dialog? {
            return showBtDialog(context,
                title,
                subTitle,
                cancelText,
                confirmText,
                -1,
                -1,
                iClickListener)
        }

        fun showBtDialog(context: Context?,
                         title: String?,
                         subTitle: String?,
                         cancelText: String?,
                         confirmText: String?,
                         cancelColor: Int,
                         confirmColor: Int,
                         iClickListener: IClickListener?): Dialog? {
            if (context == null) {
                return null
            }
            val baseDialog = BaseDialog(context, R.style.base_dialog_style)
            baseDialog.setContentView(R.layout.custom_bt_dialog)
            baseDialog.setGravityLayout(2)
            baseDialog.widthDialogdp = -2.0f
            baseDialog.heightDialogdp = -2.0f
            baseDialog.setCancelable(false)
            baseDialog.setCanceledOnTouchOutside(false)
            baseDialog.initOnCreate()
            baseDialog.show()
            val btnCancel = baseDialog.findViewById(R.id.cancel_btn) as Button
            val btnConfirm = baseDialog.findViewById(R.id.confirm_btn) as Button
            (baseDialog.findViewById(R.id.customdialog_title) as TextView).text = title
            (baseDialog.findViewById(R.id.customdialog_subtitle) as TextView).text = subTitle
            btnCancel.setText(cancelText)
            btnConfirm.setText(confirmText)
            if (cancelColor == -1) {
                btnCancel.setTextColor(-14072090)
            } else {
                btnCancel.setTextColor(cancelColor)
            }
            if (confirmColor == -1) {
                btnConfirm.setTextColor(-14072090)
            } else {
                btnConfirm.setTextColor(confirmColor)
            }
            btnCancel.setOnClickListener(object : View.OnClickListener {
                private var mBaseDialog: BaseDialog? = null
                override fun onClick(view: View?) {
                    iClickListener?.cancelBtn()
                    mBaseDialog?.dismiss()
                }

                init {
                    mBaseDialog = baseDialog
                }
            })
            btnConfirm.setOnClickListener(object : View.OnClickListener {
                private var mBaseDialog: BaseDialog? = null
                override fun onClick(view: View?) {
                    iClickListener?.clickOKBtn()
                    mBaseDialog?.dismiss()
                }

                init {
                    mBaseDialog = baseDialog
                }
            })
            return baseDialog
        }

        fun showBtTitleDialog(context: Context?,
                              title: String?,
                              subTitle: String?,
                              cancelText: String?,
                              confirmText: String?,
                              cancelColor: Int,
                              confirmColor: Int,
                              enableCancel: Boolean,
                              iClickListener: IClickListener?): Dialog? {
            if (context == null) {
                return null
            }
            val baseDialog = BaseDialog(context, R.style.base_dialog_style)
            baseDialog.setContentView(R.layout.custom_bt_title_dialog)
            baseDialog.setGravityLayout(2)
            baseDialog.widthDialogdp = -2.0f
            baseDialog.heightDialogdp = -2.0f
            baseDialog.setCancelable(enableCancel)
            baseDialog.setCanceledOnTouchOutside(enableCancel)
            baseDialog.initOnCreate()
            baseDialog.show()
            val btnCancel = baseDialog.findViewById(R.id.cancel_btn) as Button
            val btnConfirm = baseDialog.findViewById(R.id.confirm_btn) as Button
            (baseDialog.findViewById(R.id.customdialog_title) as TextView).text = title
            (baseDialog.findViewById(R.id.customdialog_subtitle) as TextView).text = subTitle
            btnCancel.setText(cancelText)
            btnConfirm.setText(confirmText)
            if (!TextUtils.isEmpty(subTitle)) {
                (baseDialog.findViewById(R.id.ll_subtit) as LinearLayout).setVisibility(View.VISIBLE)
            }
            if (cancelColor == -1) {
                btnCancel.setTextColor(-14072090)
            } else {
                btnCancel.setTextColor(cancelColor)
            }
            if (confirmColor == -1) {
                btnConfirm.setTextColor(-14072090)
            } else {
                btnConfirm.setTextColor(confirmColor)
            }
            btnCancel.setOnClickListener(object : View.OnClickListener {
                private var mBaseDialog: BaseDialog? = null
                override fun onClick(view: View?) {
                    iClickListener?.cancelBtn()
                    mBaseDialog?.dismiss()
                }

                init {
                    mBaseDialog = baseDialog
                }
            })
            btnConfirm.setOnClickListener(object : View.OnClickListener {
                private var mBaseDialog: BaseDialog? = null
                override fun onClick(view: View?) {
                    iClickListener?.clickOKBtn()
                    mBaseDialog?.dismiss()
                }

                init {
                    mBaseDialog = baseDialog
                }
            })
            return baseDialog
        }

        fun showOneClickDialog(activity: Activity,
                               title: String,
                               subTitle: String,
                               buttonText: String,
                               iOneClickListener: IOneClickListener?): Dialog? {
            if (activity.isFinishing) {
                return null
            }
            val baseDialog = BaseDialog(activity, R.style.base_dialog_style)
            baseDialog.setContentView(R.layout.custom_dialog_one)
            baseDialog.setGravityLayout(2)
            baseDialog.widthDialog = (-2.0).toFloat()
            baseDialog.heightDialog = (-2.0).toFloat()
            baseDialog.setCancelable(false)
            baseDialog.setCanceledOnTouchOutside(false)
            baseDialog.initOnCreate()
            baseDialog.show()
            val subtitle = baseDialog.findViewById<View>(R.id.subtitle) as TextView
            val button = baseDialog.findViewById<View>(R.id.button) as Button
            button.text = buttonText
            (baseDialog.findViewById<View>(R.id.title) as TextView).text = title
            if (!TextUtils.isEmpty(subTitle)) {
                subtitle.visibility = View.VISIBLE
                subtitle.text = subTitle
            }
            button.setOnClickListener(object : View.OnClickListener {
                private var mBaseDialog: BaseDialog? = null
                override fun onClick(view: View?) {
                    iOneClickListener?.clickOKBtn()
                    mBaseDialog?.dismiss()
                }

                init {
                    mBaseDialog = baseDialog
                }
            })
            return baseDialog
        }

        fun showProgressDialog(str: String?, z: Boolean, activity: Activity?) {
            if (activity != null) {
                try {
                    if (!activity.isFinishing) {
                        if (progressDialogBar == null) {
                            progressDialogBar = ProgressDialogBar.create(activity)!!
                        }
                        progressDialogBar!!.setProgress(str)
                        progressDialogBar!!.setCanceledOnTouchOutside(false)
                        progressDialogBar!!.setCancelable(z)
                        progressDialogBar!!.show()
                    }
                } catch (unused: Exception) {
                }
            }
        }

        fun destroyProgressDialog() {
            try {
                if (progressDialogBar != null) {
                    if (progressDialogBar!!.isShowing) {
                        progressDialogBar!!.dismiss()
                    }
                    progressDialogBar = null
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        fun showInterlinkingDialog(activity: Activity?,
                                   title: String?,
                                   subTitle: String?,
                                   cancelText: String?,
                                   confirmText: String?,
                                   iClickListener: IClickListener?): Dialog? {
            return showInterlinkingDialog(activity,
                title,
                subTitle,
                cancelText,
                confirmText,
                -1,
                -1,
                iClickListener)
        }

        fun showInterlinkingDialog(activity: Activity?,
                                   title: String?,
                                   subTitle: String?,
                                   cancelText: String?,
                                   confirmText: String?,
                                   cancelColor: Int,
                                   confirmColor: Int,
                                   iClickListener: IClickListener?): Dialog? {
            return showInterlinkingDialog(activity,
                title,
                subTitle as CharSequence?,
                false,
                cancelText,
                confirmText,
                cancelColor,
                confirmColor,
                iClickListener)
        }

        fun showInterlinkingDialog(activity: Activity?,
                                   title: String?,
                                   subTitle: CharSequence?,
                                   interlinking: Boolean,
                                   cancelText: String?,
                                   confirmText: String?,
                                   cancelColor: Int,
                                   confirmColor: Int,
                                   iClickListener: IClickListener?): Dialog? {
            if (activity == null || activity.isFinishing) {
                return null
            }
            val baseDialog = BaseDialog(activity, R.style.base_dialog_style)
            baseDialog.setContentView(R.layout.custom_bt_dialog)
            baseDialog.setGravityLayout(2)
            baseDialog.widthDialogdp = -2.0f
            baseDialog.heightDialogdp = -2.0f
            baseDialog.setCancelable(false)
            baseDialog.setCanceledOnTouchOutside(false)
            baseDialog.initOnCreate()
            baseDialog.show()
            val tvTitle = baseDialog.findViewById<TextView>(R.id.customdialog_title)
            val tvSubTitle = baseDialog.findViewById<TextView>(R.id.customdialog_subtitle)
            val btCancel = baseDialog.findViewById<Button>(R.id.cancel_btn)
            val btConfirm = baseDialog.findViewById<Button>(R.id.confirm_btn)
            if (interlinking) {
                tvSubTitle.movementMethod = LinkMovementMethod.getInstance()
                tvSubTitle.text = subTitle
            } else {
                tvSubTitle.text = subTitle
            }
            if (TextUtils.isEmpty(subTitle)) {
                tvSubTitle.visibility = View.GONE
            }
            tvTitle.text = title
            btCancel.text = cancelText
            btConfirm.text = confirmText
            if (cancelColor == -1) {
                btCancel.setTextColor(-14072090)
            } else {
                btCancel.setTextColor(cancelColor)
            }
            if (confirmColor == -1) {
                btConfirm.setTextColor(-14072090)
            } else {
                btConfirm.setTextColor(confirmColor)
            }
            btCancel.setOnClickListener { _ ->
                iClickListener?.cancelBtn()
                baseDialog.dismiss()
            }
            btConfirm.setOnClickListener { _ ->
                iClickListener?.clickOKBtn()
                baseDialog.dismiss()
            }
            return baseDialog
        }

        fun showDialogAutoClose(activity: Activity?,
                                finishActivity: Boolean,
                                delayClose: Int,
                                title: String?,
                                resID: Int): Dialog? {
            if (activity == null || activity.isFinishing) {
                return null
            }
            val baseDialog = BaseDialog(activity, R.style.base_dialog_style)
            baseDialog.setContentView(R.layout.custom_iv_h_dialog)
            baseDialog.setGravityLayout(2)
            baseDialog.widthDialog = (-2.0).toFloat()
            baseDialog.heightDialog = (-2.0).toFloat()
            baseDialog.setCancelable(true)
            baseDialog.setCanceledOnTouchOutside(true)
            baseDialog.initOnCreate()
            baseDialog.show()
            baseDialog.findViewById<ImageView>(R.id.iv_img).setBackgroundResource(resID)
            baseDialog.findViewById<TextView>(R.id.tv_title).text = title
            Handler(Looper.getMainLooper()).postDelayed({
                baseDialog.dismiss()
                if (finishActivity)
                    activity.finish()
            }, (1000 * delayClose).toLong())
            return baseDialog
        }

    }
}