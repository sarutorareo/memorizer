package com.example.jirou.memorizer.adapters

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.BaseAdapter
import android.widget.GridView
import com.example.jirou.memorizer.models.HandAction
import com.example.jirou.memorizer.models.HandActionCorrect
import com.example.jirou.memorizer.views.HandActionCanvasView


class ListAdapterHandAction(private val mContext: Context, private val gridView: GridView,
                            private val mHandActionList : ArrayList<HandAction>) : BaseAdapter() {
    private var cellWidth : Int = 0
    private var cellHeight : Int = 0

    init {
        //描画後にサイズを得る
        val viewTreeObserver : ViewTreeObserver = gridView.viewTreeObserver
        viewTreeObserver.addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener {
            val v = gridView.getChildAt(0)
            if (v != null) {
                this.setCellViewSize( v.measuredWidth, v.measuredHeight)
                Log.e("getView", String.format("OnGlobalLayoutListener: v.measuredState = %d, measuredWidthAndState = %d, measuredHeightAndState = %d, measuredWidth = %d, measuredHeight = %d ",
                        v.measuredState, v.measuredWidthAndState, v.measuredHeightAndState, v.measuredWidth, v.measuredHeight))
            }
        })
    }

    override fun getCount(): Int {
        //グリッド総数
        return mHandActionList.size
    }

    override fun getItem(position: Int): Any {
        //配列の中身
        return mHandActionList[position]
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
        v.text = handAction.getHand()
        v.setActionVal(handAction.getActionVal())
        if (handAction is HandActionCorrect) {
            v.setCompared((handAction as HandActionCorrect).getCompared())
        }

        Log.e("getView", String.format("v: v.width = %d, v.height = %d, v.left = %d, v.right = %d, v.top = %d, v.bottom = %d ",
                v.width, v.height, v.left, v.right, v.top, v.bottom))
        Log.e("getView", String.format("v: v.measuredState = %d, measuredWidthAndState = %d, measuredHeightAndState = %d, measuredWidth = %d, measuredHeight = %d ",
                v.measuredState, v.measuredWidthAndState, v.measuredHeightAndState, v.measuredWidth, v.measuredHeight))

        return v
    }

    fun setCellViewSize(width: Int, height: Int) {
        cellWidth = width
        cellHeight = height
    }

    fun xToCol(x: Float): Int
    {
        return if (cellWidth > 0) Math.floor(x.div(cellWidth).toDouble()).toInt() else -1
    }

    fun yToRow(y: Float): Int
    {
        return if (cellHeight > 0) Math.floor(y.div(cellHeight).toDouble()).toInt() else -1
    }

    fun axisToPosition(rowSize: Int, x: Float, y: Float) : Int
    {
        val col : Int = xToCol(x)
        val row : Int = yToRow(y)
        return row * rowSize + col
    }
}