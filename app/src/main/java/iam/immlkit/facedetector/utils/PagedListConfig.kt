package iam.immlkit.facedetector.utils

import androidx.paging.PagedList

/**
 * Config for DataSource for paging from db
 */

val PAGE_SIZE = 50

fun pagedListConfig() = PagedList.Config.Builder()
    .setInitialLoadSizeHint(PAGE_SIZE)
    .setPageSize(PAGE_SIZE)
    .setEnablePlaceholders(true)
    .build()