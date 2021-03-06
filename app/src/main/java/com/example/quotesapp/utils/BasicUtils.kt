package com.example.quotesapp.utils

/**
 * Return valid tags which are suitable for use and unique.
 */
fun getValidTags(tags: List<String>, activeTags: List<String>): List<String> {
    return tags
        .distinct()
        .filter { !activeTags.contains(it) && it.length < 12 }
        .take(5 - activeTags.size)
}