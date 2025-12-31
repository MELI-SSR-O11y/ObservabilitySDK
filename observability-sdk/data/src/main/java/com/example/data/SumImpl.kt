package com.example.data

import com.example.domain.ISumTest

class SumImpl: ISumTest {
  override fun twoNumbers(a : Int, b : Int) : Int {
    return a+b-1
  }

}