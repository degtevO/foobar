package com.foobar.now.model

import com.foobar.now.model.ChallengeStatus.ChallengeStatus
import com.foobar.now.model.Difficulty.Difficulty
import doobie.enum.JdbcType.VarChar
import doobie.util.Meta.Basic
import doobie.util.Meta
import doobie.enum.JdbcType._
import io.circe.{Decoder, Encoder}

object ChallengeStatus extends Enumeration {
  type ChallengeStatus = Value
  val Assigned = Value("assigned")
  val Accepted = Value("accepted")
  val Declined = Value("declined")
  val Completed = Value("completed")

  implicit val challengeStatusDoobieRead: Meta[ChallengeStatus] =
    Basic.one[ChallengeStatus](
      VarChar,
      List(VarChar, Char, LongVarChar, NChar, NVarChar, LongnVarChar),
      (st, i) => ChallengeStatus.withName(st.getString(i)),
      (st, i, status) => st.setString(i, status.toString),
      (st, i, status) => st.updateString(i, status.toString)
    )

  implicit val encoder: Encoder[ChallengeStatus] = Encoder.enumEncoder(ChallengeStatus)
  implicit val decoder: Decoder[ChallengeStatus] = Decoder.enumDecoder(ChallengeStatus)
}

object Difficulty extends Enumeration {
  type Difficulty = Value

  val Level1 = Value(1)
  val Level2 = Value(2)
  val Level3 = Value(3)

  implicit val encoder: Encoder[Difficulty] = Encoder.enumEncoder(Difficulty)
  implicit val decoder: Decoder[Difficulty] = Decoder.enumDecoder(Difficulty)

  implicit val difficultyDoobieRead: Meta[Difficulty] =
    Basic.one[Difficulty](
      Integer,
      List(Integer, SmallInt),
      (st, i) => Difficulty(st.getInt(i)),
      (st, i, status) => st.setInt(i, status.id),
      (st, i, status) => st.updateInt(i, status.id)
    )
}

case class ChallengeType(id: Int, title: String, description: String, difficulty: Difficulty)

case class Challenge(id: Long,
                     typeId: Int,
                     creator: Long,
                     assigned: Long,
                     status: ChallengeStatus,
                     proof: Option[String]
                    )
