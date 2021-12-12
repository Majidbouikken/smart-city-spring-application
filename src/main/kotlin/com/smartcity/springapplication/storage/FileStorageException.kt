package com.smartcity.springapplication.storage

import java.lang.RuntimeException

class FileStorageException(message: String?) : RuntimeException(message)