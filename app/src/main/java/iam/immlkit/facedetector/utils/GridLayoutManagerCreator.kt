package iam.immlkit.facedetector.utils

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

/**
 * just more convient way to create layout manager for all 3 list fragments
 */
private val SPANS = 3

fun createGridLayoutManager(context: Context): GridLayoutManager= GridLayoutManager(context, SPANS)