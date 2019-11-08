package com.foobar.now.dao

import com.foobar.now.model.User
import doobie.free.connection.ConnectionIO
import doobie.implicits._

class UserDao {

  def signIn(loginOrEmail: String, password: String): ConnectionIO[User] = {
    sql"""select id, login, name, email, location, avatar, karma from public.user
         |where (login = $loginOrEmail and password = $password)
         |or (login = $loginOrEmail and password = $password)""".stripMargin
      .query[User]
      .unique
  }

  def get(id: Long): ConnectionIO[User] = {
    sql"select id, login, name, email, location, avatar, karma from public.user where id = $id"
      .query[User]
      .unique
  }

  def list(userId: Long, limit: Int): fs2.Stream[ConnectionIO, User] = {
    sql"""select id, login, name, email, location, avatar, karma from public.user
         |where id <> $userId order by random() limit $limit
       """.stripMargin
      .query[User]
      .stream
  }

  def near(userId: Long, limit: Int): fs2.Stream[ConnectionIO, User] = {
    sql"""select id, login, name, email, location, avatar, karma from public.user
         |where id <> $userId and location = (select location from public.user where id = $userId)
         |order by random() limit $limit
       """.stripMargin
      .query[User]
      .stream
  }

  def friends(userId: Long, limit: Int): fs2.Stream[ConnectionIO, User] = {
    sql"""select u.id, u.login, u.name, u.email, u.location, u.avatar, u.karma from public.user u, friend f
         |where f.user_id = $userId and u.id = f.friend_id
         |order by random() limit $limit
       """.stripMargin
      .query[User]
      .stream
  }

  def getAllFriends(userId: Long): fs2.Stream[doobie.ConnectionIO, Long] = {
    sql"select friend_id from friend where user_id = $userId"
      .query[Long]
      .stream
  }

  def becomeFriend(userId: Long, friendId: Long): ConnectionIO[Int] = {
    sql"insert into friend (user_id, friend_id) values ($userId, $friendId) on conflict do nothing"
      .update
      .run
  }

  def updateKarma(userId: Long, points: Int): ConnectionIO[Int] = {
    sql"update public.user set karma = $points where id = $userId".update.run
  }
}
