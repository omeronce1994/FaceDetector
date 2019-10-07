package iam.immlkit.facedetector.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView

/**
 * Class to make sure image view is sqaure without setting it's height from outside
 */
class SqaureImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //just use only width params to make an even square
        var temp = heightMeasureSpec
        if(widthMeasureSpec!=0)
            temp=widthMeasureSpec
        super.onMeasure(widthMeasureSpec, temp)
    }
}
