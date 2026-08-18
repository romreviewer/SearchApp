package com.example.searchapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform