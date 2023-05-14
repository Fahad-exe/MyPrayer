package com.example.myapplication

import com.example.myapplication.model.Item

interface SalatView {
    fun onDataCompleteFromApi(salat:Item)
    fun onDataErrorFromApi(throwable: Throwable)
}