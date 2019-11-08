package com.foobar.now.service

import com.foobar.now.dao.UserDao
import com.foobar.now.model.{SignIn, User}
import doobie.util.transactor.Transactor
import monix.eval.Task
import doobie.implicits._

class UserService(userDao: UserDao, xa: Transactor[Task]) {

  def signIn(si: SignIn): Task[User] = {
    userDao.signIn(si.login, si.password).transact(xa)
  }

  def get(id: Long): Task[User] = {
    userDao.get(id).transact(xa)
  }

  def list(userId: Long, limit: Option[Int]): Task[List[User]] = {
    userDao.list(userId, limit.getOrElse(10)).compile.toList.transact(xa)
  }

  def near(userId: Long, limit: Option[Int]): Task[List[User]] = {
    userDao.near(userId, limit.getOrElse(10)).compile.toList.transact(xa)
  }

  def friends(userId: Long, limit: Option[Int]): Task[List[User]] = {
    userDao.friends(userId, limit.getOrElse(10)).compile.toList.transact(xa)
  }
}
