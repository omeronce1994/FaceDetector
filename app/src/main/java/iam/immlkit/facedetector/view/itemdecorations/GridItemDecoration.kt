package iam.immlkit.facedetector.view.itemdecorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Copied from github gist that is why there is no comments here
 */

class GridItemDecoration(val space: Int, val NUMBER_OF_COLUMNS: Int) : RecyclerView.ItemDecoration() {
    val TAG = "GridItemDecoration"

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        addSpaceToView(outRect, parent?.getChildAdapterPosition(view), parent)
    }

    private fun addSpaceToView(outRect: Rect?, position: Int?, parent: RecyclerView?) {
        if (position == null || parent == null)
            return
        if (position > 0) {
            val grid = parent.layoutManager as GridLayoutManager
            val spanSize = grid.spanSizeLookup.getSpanSize(position)

            if (spanSize == NUMBER_OF_COLUMNS) {
            } else {
                var allSpanSize = 0
                for (i: Int in IntRange(0, position)) {
                    allSpanSize += grid.spanSizeLookup.getSpanSize(i)
                }
                val currentModuloResult = allSpanSize % NUMBER_OF_COLUMNS

//                position%3 == 2 is middle column

                if (currentModuloResult == 0) {
                    outRect?.left = space
                } else if ((position%3 == 2)) {
                    outRect?.left = space
                }

            }
            outRect?.top = space

        }
    }

}