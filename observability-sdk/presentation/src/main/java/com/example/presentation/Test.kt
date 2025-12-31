package com.example.presentation

import com.example.data.SumImpl
import com.example.domain.ISumTest

class SumaTwoTest : ISumTest {
  override fun twoNumbers(a : Int, b : Int) : Int {
    BuildConfig.LOGS_ENABLED
    BuildConfig.BASE_URL
    BuildConfig.X_API_KEY
    return SumImpl().twoNumbers(a,b)
  }
}