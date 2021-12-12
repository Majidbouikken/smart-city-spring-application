package com.smartcity.springapplication.utils.error_handler

import java.lang.RuntimeException

class OfferException(s: String?) : RuntimeException(s)