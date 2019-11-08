package com.foobar.now.dao

import doobie.implicits._
import doobie.util.Read
import doobie.util.query.Query0

trait SqlPagination {
  def limit[A: Read](lim: Int)(q: Query0[A]): Query0[A] =
    (q.toFragment ++ fr" LIMIT $lim").query

  def paginate[A: Read](lim: Int, offset: Int)(q: Query0[A]): Query0[A] =
    (q.toFragment ++ fr" LIMIT $lim OFFSET $offset").query
}

object SqlPagination extends SqlPagination
