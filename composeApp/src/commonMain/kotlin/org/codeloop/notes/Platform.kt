package org.codeloop.notes

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform