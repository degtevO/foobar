package com.foobar.now.model

case class User(
               id: Long,
               login: String,
               name: String,
               email: String,
               location: String,
               avatar: String,
               karma: Int
               )