package com.demo.antizha.util

import android.text.Editable
import com.demo.antizha.ui.IEditAfterListener
import android.text.TextWatcher


class EditUtil {
    class textWatcher internal constructor(val listener: IEditAfterListener) :
        TextWatcher {
        override fun afterTextChanged(editable: Editable) {
            listener.editLength(editable.toString().length)
        }

        override fun beforeTextChanged(charSequence: CharSequence, i2: Int, i3: Int, i4: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i2: Int, i3: Int, i4: Int) {}
    }

}