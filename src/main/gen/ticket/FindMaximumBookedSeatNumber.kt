package ticket

import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlin.Int
import kotlin.String
import kotlin.Unit
import norm.ParamSetter
import norm.Query
import norm.RowMapper

public data class FindMaximumBookedSeatNumberParams(
  public val showId: Int?
)

public class FindMaximumBookedSeatNumberParamSetter : ParamSetter<FindMaximumBookedSeatNumberParams>
    {
  public override fun map(ps: PreparedStatement, params: FindMaximumBookedSeatNumberParams): Unit {
    ps.setObject(1, params.showId)
  }
}

public data class FindMaximumBookedSeatNumberResult(
  public val max: Int?
)

public class FindMaximumBookedSeatNumberRowMapper : RowMapper<FindMaximumBookedSeatNumberResult> {
  public override fun map(rs: ResultSet): FindMaximumBookedSeatNumberResult =
      FindMaximumBookedSeatNumberResult(
  max = rs.getObject("max") as kotlin.Int?)
}

public class FindMaximumBookedSeatNumberQuery : Query<FindMaximumBookedSeatNumberParams,
    FindMaximumBookedSeatNumberResult> {
  public override val sql: String = """
      |SELECT MAX(seatNo) FROM tickets
      |WHERE showId=?;
      |""".trimMargin()

  public override val mapper: RowMapper<FindMaximumBookedSeatNumberResult> =
      FindMaximumBookedSeatNumberRowMapper()

  public override val paramSetter: ParamSetter<FindMaximumBookedSeatNumberParams> =
      FindMaximumBookedSeatNumberParamSetter()
}
