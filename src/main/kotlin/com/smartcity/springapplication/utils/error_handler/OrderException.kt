package com.smartcity.springapplication.utils.error_handler

import java.lang.RuntimeException

class OrderException(s: String?) : RuntimeException(s)