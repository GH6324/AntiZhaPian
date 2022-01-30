package com.demo.antizha.interfaces

import android.os.Handler
import com.demo.antizha.interfaces.IHandler.HandleWebActListener
import com.demo.antizha.interfaces.IHandler.HandleWebDetailListener
import com.demo.antizha.interfaces.IHandler.HandleWebFlgListener
import com.demo.antizha.interfaces.IHandler
import android.os.Looper
import android.os.Message
import com.demo.antizha.interfaces.IHandler.HandleListener

/* loaded from: classes3.dex */
object IHandler {
    const val MSGWHAT0 = 0
    const val MSGWHAT1 = 1
    const val MSGWHAT10 = 10
    const val MSGWHAT11 = 11
    const val MSGWHAT2 = 2
    const val MSGWHAT3 = 3
    const val MSGWHAT4 = 4
    const val MSGWHAT5 = 5
    const val MSGWHAT6 = 6
    const val MSGWHAT7 = 7
    const val MSGWHAT8 = 8
    const val MSGWHAT9 = 9
    private var listenerWebAct: HandleWebActListener? = null
    private var listenerWebDet: HandleWebDetailListener? = null
    private var listenerWebFlg: HandleWebFlgListener? = null
    var mHandler = Holder(Looper.getMainLooper())
    fun setHandleMsgListener(handleListener: HandleListener?) {
        if (handleListener is HandleWebFlgListener) {
            listenerWebFlg = handleListener
        } else if (handleListener is HandleWebActListener) {
            listenerWebAct = handleListener
        } else if (handleListener is HandleWebDetailListener) {
            listenerWebDet = handleListener
        }
    }

    /* loaded from: classes3.dex */
    interface HandleListener {
        fun handleMsg(message: Message?)

        companion object {
            val mHandler: Handler = IHandler.mHandler
        }
    }

    /* loaded from: classes3.dex */
    interface HandleWebActListener : HandleListener

    /* loaded from: classes3.dex */
    interface HandleWebDetailListener : HandleListener

    /* loaded from: classes3.dex */
    interface HandleWebFlgListener : HandleListener

    /* JADX INFO: Access modifiers changed from: private */ /* loaded from: classes3.dex */
    class Holder(looper: Looper?) : Handler(looper!!) {
        // android.os.Handler
        override fun handleMessage(message: Message) {
            super.handleMessage(message)
            if (listenerWebFlg != null) {
                listenerWebFlg!!.handleMsg(message)
            }
            if (listenerWebAct != null) {
                listenerWebAct!!.handleMsg(message)
            }
            if (listenerWebDet != null) {
                listenerWebDet!!.handleMsg(message)
            }
        }
    }
}