package com.foobar.now.rest.routes

import java.io.File
import java.security.MessageDigest
import java.math.BigInteger

import akka.http.scaladsl.server.directives.FileInfo
import com.foobar.now.configuration.HttpConfig

trait UploadFilesSupport {
  val config: HttpConfig

  def tempDestination(fileInfo: FileInfo): File = {
    val filename = md5HashString(fileInfo.fileName + System.currentTimeMillis().toString)
    val ext = fileInfo.contentType.mediaType.fileExtensions.mkString(".",".","")
    File.createTempFile(fileInfo.fileName, ext, new File(config.uploadFilesDir))
  }

  private def md5HashString(s: String): String = {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(s.getBytes)
    val bigInt = new BigInteger(1,digest)
    val hashedString = bigInt.toString(16)
    hashedString
  }
}
