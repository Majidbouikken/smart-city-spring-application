package com.smartcity.springapplication.utils.error_handler

import java.lang.RuntimeException

class SimpleUserException(s: String?) : RuntimeException(s)