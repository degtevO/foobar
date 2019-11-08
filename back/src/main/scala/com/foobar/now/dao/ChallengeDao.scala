package com.foobar.now.dao

import cats.data.NonEmptyList
import com.foobar.now.model.{Challenge, ChallengeStatus}
import com.foobar.now.model.ChallengeStatus.ChallengeStatus
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import doobie._

class ChallengeDao extends SqlPagination {
  def create(challenge: Challenge): ConnectionIO[Challenge] = {
    sql"""insert into challenge (type_id, creator, assigned, status)
         |values (${challenge.typeId}, ${challenge.creator}, ${challenge.assigned}, ${challenge.status})
         |""".stripMargin
      .update
      .withUniqueGeneratedKeys("id", "type_id", "creator", "assigned", "status", "proof")
  }

  def get(id: Long): ConnectionIO[Challenge] = {
    sql"select id, type_id, creator, assigned, status, proof from challenge where id = $id"
      .query[Challenge]
      .unique
  }

  def getAssigned(userId: Long, status: ChallengeStatus, limit: Int, offset: Int): fs2.Stream[ConnectionIO, Challenge] = {
    paginate(limit, offset)(
      sql"select id, type_id, creator, assigned, status, proof from challenge where assigned = $userId and status = $status".query[Challenge]
    ).stream
  }

  def getById(ids: NonEmptyList[Long], limit: Int, offset: Int): fs2.Stream[ConnectionIO, Challenge] = {
    paginate(limit, offset)(
      (sql"select id, type_id, creator, assigned, status, proof from challenge where " ++ Fragments.in(fr"id", ids)).query[Challenge]
    ).stream
  }

  def complete(userId: Long, id: Long, proof: String): doobie.ConnectionIO[Challenge] = {
    val status = ChallengeStatus.Completed
    sql"update challenge set status = $status, proof = $proof where id = $id and assigned = $userId"
      .update
      .withUniqueGeneratedKeys("id", "type_id", "creator", "assigned", "status", "proof")
  }

  def setStatus(id: Long, userId: Long, status: ChallengeStatus): ConnectionIO[Challenge] = {
    sql"update challenge set status = $status where id = $id and assigned = $userId"
      .update
      .withUniqueGeneratedKeys("id", "type_id", "creator", "assigned", "status", "proof")
  }

  def feed(ids: NonEmptyList[Long], limit: Int, offset: Int): fs2.Stream[ConnectionIO, Challenge] = {
    paginate(limit, offset)(
      (sql"select id, type_id, creator, assigned, status, proof from challenge where " ++
        Fragments.or(Fragments.in(fr"creator", ids), Fragments.in(fr"assigned", ids))).query[Challenge]
    ).stream
  }

  def assignedToMe(userId: Long, limit: Int, offset: Int): fs2.Stream[ConnectionIO, Challenge] = {
    paginate(limit, offset)(
      sql"select id, type_id, creator, assigned, status, proof from challenge where assigned = $userId".query[Challenge]
    ).stream
  }

  def createdByMe(userId: Long, limit: Int, offset: Int): fs2.Stream[ConnectionIO, Challenge] = {
    paginate(limit, offset)(
      sql"select id, type_id, creator, assigned, status, proof from challenge where assigned = $userId".query[Challenge]
    ).stream
  }

}

object ChallengeDao {
  def apply(): ChallengeDao = new ChallengeDao()
}