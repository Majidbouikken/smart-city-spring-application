package com.smartcity.springapplication.utils.error_handler

import java.lang.RuntimeException

class FlashDealException(s: String?) : RuntimeException(s)