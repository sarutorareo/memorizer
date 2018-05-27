package com.example.jirou.memorizer.adapters

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.BaseAdapter
import android.widget.GridView
import com.example.jirou.memorizer.models.HandAction
import com.example.jirou.memorizer.models.HandActionCompared
import com.example.jirou.memorizer.models.HandActionList
import com.example.jirou.memorizer.views.HandActionCanvasView


class ListAdapterHandAction(private val mContext: Context, private val gridView: GridView,
                            private val mHandActionList : HandActionList) : BaseAdapter() {
    private var cellWidth : Int = 0
    private var cellHeight : Int = 0

    init {
        //描画後にサイズを得る
        val viewTreeObserver : ViewTreeObserver = gridView.viewTreeObserver
        viewTreeObserver.addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener {
            val v = gridView.getChildAt(0)
            if (v != null) {
                this.setCellViewSize( v.measuredWidth, v.measuredHeight)
            }
        })
    }

    override fun getCount(): Int {
        //グリッド総数
        return mHandActionList.size
    }

    override fun getItem(position: Int): Any {
        assert(position >= 0 && position < mHandActionList.size)
        //配列の中身
        return mHandActionList.get(position) as Any
    }

    override fun getItemId(position: Int): Long {
        //現在の配列の場所
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        Log.e("getView", String.format("position is %d", position) )
        var v: HandActionCanvasView? = convertView as HandActionCanvasView?

        if (v == null) {
            v = HandActionCanvasView(mContext)
        }

        //配列から、アイテムを取得
        val handAction = getItem(position) as HandAction
        //取得したテキストビューにactionを設定
        v.text = handAction.handDispStr
        v.setActionVal(handAction.actionVal)
        if (handAction is HandActionCompared) {
            v.setCompared((handAction as HandActionCompared).getCompared())
        }

        /*
        Log.e("getView", String.format("v: v.width = %d, v.height = %d, v.left = %d, v.right = %d, v.top = %d, v.bottom = %d ",
                v.width, v.height, v.left, v.right, v.top, v.bottom))
        Log.e("getView", String.format("v: v.measuredState = %d, measuredWidthAndState = %d, measuredHeightAndState = %d, measuredWidth = %d, measuredHeight = %d ",
                v.measuredState, v.measuredWidthAndState, v.measuredHeightAndState, v.measuredWidth, v.measuredHeight))
        */
        return v
    }

    private fun setCellViewSize(width: Int, height: Int) {
        cellWidth = width
        cellHeight = height
    }

    private fun mXToCol(x: Float): Int
    {
        return if (cellWidth > 0) Math.floor(x.div(cellWidth).toDouble()).toInt() else -1
    }

    private fun mYToRow(y: Float): Int
    {
        return if (cellHeight > 0) Math.floor(y.div(cellHeight).toDouble()).toInt() else -1
    }

    private fun mAxisToPosition(rowSize: Int, x: Float, y: Float) : Int
    {
        val col : Int = mXToCol(x)
        val row : Int = mYToRow(y)
        return row * rowSize + col
    }

    // Implement On Touch listener
    fun getOnTouchListener(fnGetActionValue : () -> Int) :  (View, MotionEvent) -> Boolean =  { v, event ->
        val currAction : String
        ////////////////////////////////////////////////////////////
        // イベントの状態を調べる
        val action = event.action and MotionEvent.ACTION_MASK
        currAction = when (action) {
            MotionEvent.ACTION_DOWN -> "DOWN"
            MotionEvent.ACTION_MOVE -> "MOVE"
            MotionEvent.ACTION_UP -> "UP"
            MotionEvent.ACTION_CANCEL -> "CANCEL"
            else -> "null"
        }

        val position = (gridView.adapter as ListAdapterHandAction).mAxisToPosition(gridView.numColumns, event.x, event.y)
        /*
        Log.e("onTouch", String.format("action is %s (x=%f, y=%f)", currAction, event.x, event.y) )
        Log.e("onTouch", String.format("(col=%d, row=%d, position=%d) gridView.width = %d, gridView.height = %d, gridView.numColumns = %d",
                (gridView.adapter as ListAdapterHandAction).mXToCol(event.x),
                (gridView.adapter as ListAdapterHandAction).mYToRow(event.y),
                position, gridView.width, gridView.height, gridView.numColumns)
        )
        */

        if (((action == MotionEvent.ACTION_DOWN) || (action == MotionEvent.ACTION_MOVE))
                && (position >= 0) && (position < mHandActionList.size)) {
            //配列から、アイテムを取得
            val handAction = mHandActionList.get(position)
            handAction.setActionVal(fnGetActionValue())
            // getViewで対象のViewを更新
            val targetView : View? = gridView.getChildAt(position)
            if (targetView != null) {
                gridView.adapter.getView(position, targetView, gridView)
            } else {
                // 何もしない
            }
        }

        false
    }

}