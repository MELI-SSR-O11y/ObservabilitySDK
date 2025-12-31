package com.example.presentation

import com.example.data.SumImpl
import com.example.domain.ISumTest

class SumaTwoTest : ISumTest {
  override fun twoNumbers(a : Int, b : Int) : Int {
    return SumImpl().twoNumbers(a,b)
  }
}