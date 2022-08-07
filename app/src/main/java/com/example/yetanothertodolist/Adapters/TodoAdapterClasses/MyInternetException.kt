package com.example.yetanothertodolist.Adapters.TodoAdapterClasses

import android.widget.Toast
import java.lang.RuntimeException

open class MyInternetException: RuntimeException()

class FourZeroZeroException(override val message: String = ""):MyInternetException()

class FourZeroFourException(override val message: String = ""): MyInternetException()

class FourZeroOneException(override val message: String = ""): MyInternetException()

class FiveZeroZeroException(override val message: String = ""): MyInternetException()