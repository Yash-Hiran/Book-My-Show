package ticket

import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlin.Int
import kotlin.String
import kotlin.Unit
import norm.ParamSetter
import norm.Query
import norm.RowMapper

public data class GetBookedSeatsParams(
  public val showId: Int?
)

public class GetBookedSeatsParamSetter : ParamSetter<GetBookedSeatsParams> {
  public override fun map(ps: PreparedStatement, params: GetBookedSeatsParams): Unit {
    ps.setObject(1, params.showId)
  }
}

public data class GetBookedSeatsResult(
  public val seatno: Int
)

public class GetBookedSeatsRowMapper : RowMapper<GetBookedSeatsResult> {
  public override fun map(rs: ResultSet): GetBookedSeatsResult = GetBookedSeatsResult(
  seatno = rs.getObject("seatno") as kotlin.Int)
}

public class GetBookedSeatsQuery : Query<GetBookedSeatsParams, GetBookedSeatsResult> {
  public override val sql: String = """
      |SELECT seatNo FROM tickets
      |WHERE showId = ?;
      |""".trimMargin()

  public override val mapper: RowMapper<GetBookedSeatsResult> = GetBookedSeatsRowMapper()

  public override val paramSetter: ParamSetter<GetBookedSeatsParams> = GetBookedSeatsParamSetter()
}
