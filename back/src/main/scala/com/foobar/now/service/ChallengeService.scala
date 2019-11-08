package com.foobar.now.service

import java.nio.file.Paths
import java.util.UUID

import cats.syntax.apply._
import akka.stream.Materializer
import akka.stream.scaladsl.{FileIO, Source}
import akka.util.ByteString
import cats.data.NonEmptyList
import com.foobar.now.configuration.{HttpConfig, KarmaConfig}
import com.foobar.now.dao.{ChallengeDao, ChallengeTypeDao, UserDao}
import com.foobar.now.model.ChallengeStatus.ChallengeStatus
import com.foobar.now.model.{Challenge, ChallengeStatus, ChallengeType}
import doobie.free.connection
import doobie.util.transactor.Transactor
import monix.eval.Task
import doobie.implicits._

class ChallengeService(config: KarmaConfig,
                       httpConfig: HttpConfig,
                       challengeTypeDao: ChallengeTypeDao,
                       challengeDao: ChallengeDao,
                       userDao: UserDao,
                       xa: Transactor[Task])
                      (implicit materializer: Materializer) {
  def createChallenge(challengeTypeId: Int,
                      creator: Long,
                      assignedTo: Long): Task[Unit] = {
    if (creator == assignedTo) {
      Task.raiseError(new Exception("Users are unable to assign challenge to themselves"))
    } else {
      val challenge = Challenge(0, challengeTypeId, creator, assignedTo, ChallengeStatus.Assigned, None)
      challengeDao.create(challenge).transact(xa).map(_ => ())
    }
  }

  def getRandomChallenge(userId: Long): Task[Challenge] = {
    (for {
      challengeType <- challengeTypeDao.getRandom
      newChallenge = Challenge(0, challengeType.id, 0, userId, ChallengeStatus.Assigned, None)
      challenge <- challengeDao.create(newChallenge)
    } yield challenge).transact(xa)
  }

  def listRandomChallengeTypes: Task[Seq[ChallengeType]] = {
    challengeTypeDao.listRandom.compile.toList.transact(xa)
  }

  def getChallenge(id: Long): Task[Challenge] = {
    challengeDao.get(id).transact(xa)
  }

  def getAssignedChallenges(userId: Long, limit: Option[Int], offset: Option[Int]): Task[List[Challenge]] = {
    challengeDao.getAssigned(userId, ChallengeStatus.Assigned, limit.getOrElse(10), offset.getOrElse(0))
      .compile
      .toList
      .transact(xa)
  }

  def getAcceptedChallenges(userId: Long, limit: Option[Int], offset: Option[Int]): Task[List[Challenge]] = {
    challengeDao.getAssigned(userId, ChallengeStatus.Accepted, limit.getOrElse(10), offset.getOrElse(0))
      .compile
      .toList
      .transact(xa)
  }

  def acceptChallenge(userId: Long, id: Long): Task[Unit] = {
    updateStatus(userId, id, ChallengeStatus.Accepted)
      .transact(xa)
      .map(_ => ())
  }

  def declineChallenge(userId: Long, id: Long): Task[Unit] = {
    (for {
      challenge <- updateStatus(userId, id, ChallengeStatus.Declined)
      _ <- if (challenge.status == ChallengeStatus.Accepted) penalty(challenge.assigned)
           else connection.unit
    } yield ()).transact(xa)
  }

  def completeChallenge(userId: Long, id: Long, proof: Source[ByteString, Any]): Task[Unit] = {
    val fileId = UUID.randomUUID().toString
    Task.deferFuture(
      proof.runWith(FileIO.toPath(Paths.get(httpConfig.uploadFilesDir).resolve(fileId)))
    ) *>
    (for {
      challenge <- challengeDao.complete(userId, id, fileId)
      challengeType <- challengeTypeDao.get(challenge.typeId)
      _ <- userDao.updateKarma(challenge.assigned, challengeType.difficulty.id)
      _ <- userDao.becomeFriend(challenge.creator, challenge.assigned)
      _ <- userDao.becomeFriend(challenge.assigned, challenge.creator)
    } yield ()).transact(xa)
  }

  def feed(userId: Long, limit: Option[Int], offset: Option[Int]): Task[List[Challenge]] = {
    (for {
      ids <- userDao.getAllFriends(userId).compile.toList
      assigned <- challengeDao.feed(NonEmptyList.fromListUnsafe(userId +: ids), limit.getOrElse(10), offset.getOrElse(0)).compile.toList
    } yield assigned).transact(xa)
  }

  def assignedToMe(userId: Long, limit: Option[Int], offset: Option[Int]) = {
    challengeDao.assignedToMe(userId, limit.getOrElse(10), offset.getOrElse(0))
      .compile
      .toList
      .transact(xa)
  }

  def createdByMe(userId: Long, limit: Option[Int], offset: Option[Int]) = {
    challengeDao.assignedToMe(userId, limit.getOrElse(10), offset.getOrElse(0))
      .compile
      .toList
      .transact(xa)
  }

  private def updateStatus(userId: Long, id: Long, status: ChallengeStatus) = {
    challengeDao.setStatus(id, userId, status)
  }

  private def penalty(assignedTo: Long) = {
    for {
      user <- userDao.get(assignedTo)
      newKarmaValue = user.karma - config.declineDecrease
      _ <- userDao.updateKarma(user.id, if (newKarmaValue < 0) 0 else newKarmaValue)
    } yield ()
  }
}
