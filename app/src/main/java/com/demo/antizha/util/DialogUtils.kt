package com.demo.antizha.util

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.demo.antizha.R
import com.demo.antizha.ui.BaseDialog
import com.demo.antizha.ui.IClickListener


class DialogUtils {
    companion object {
        @RequiresApi(Build.VERSION_CODES.R)
        fun showNormalDialog(context: Context?,
                             title: String?,
                             subTitle: String?,
                             cancelText: String?,
                             confirmText: String?,
                             iClickListener: IClickListener?): Dialog? {
            return showDialog(context,
                title,
                subTitle,
                cancelText,
                confirmText,
                -1,
                -1,
                iClickListener)
        }

        /* renamed from: a */
        @RequiresApi(Build.VERSION_CODES.R)
        fun showDialog(context: Context?,
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
                    mBaseDialog?.dismiss();
                }

                init {
                    mBaseDialog = baseDialog
                }
            })
            btnConfirm.setOnClickListener(object : View.OnClickListener {
                private var mBaseDialog: BaseDialog? = null
                override fun onClick(view: View?) {
                    iClickListener?.clickOKBtn()
                    mBaseDialog?.dismiss();
                }

                init {
                    mBaseDialog = baseDialog
                }
            })
            return baseDialog
        }
    }
}