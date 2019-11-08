package com.foobar.now.service

import com.foobar.now.dao.ChallengeTypeDao
import com.foobar.now.model.ChallengeType
import doobie.util.transactor.Transactor
import monix.eval.Task
import doobie.implicits._

class ChallengeTypeService(challengeTypeDao: ChallengeTypeDao, xa: Transactor[Task]) {
  def create(challengeType: ChallengeType): Task[ChallengeType] = {
    challengeTypeDao.create(challengeType).transact(xa)
  }

  def list(limit: Option[Int], offset: Option[Int]): Task[Seq[ChallengeType]] = {
    challengeTypeDao.list(limit.getOrElse(10), offset.getOrElse(0)).compile.toList.transact(xa)
  }

  def get(id: Int): Task[ChallengeType] = {
    challengeTypeDao.get(id).transact(xa)
  }
}
