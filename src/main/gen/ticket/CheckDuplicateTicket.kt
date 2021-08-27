package ticket

import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Unit
import norm.ParamSetter
import norm.Query
import norm.RowMapper

public data class CheckDuplicateTicketParams(
  public val showId: Int?,
  public val seatNo: Int?
)

public class CheckDuplicateTicketParamSetter : ParamSetter<CheckDuplicateTicketParams> {
  public override fun map(ps: PreparedStatement, params: CheckDuplicateTicketParams): Unit {
    ps.setObject(1, params.showId)
    ps.setObject(2, params.seatNo)
  }
}

public data class CheckDuplicateTicketResult(
  public val count: Long?
)

public class CheckDuplicateTicketRowMapper : RowMapper<CheckDuplicateTicketResult> {
  public override fun map(rs: ResultSet): CheckDuplicateTicketResult = CheckDuplicateTicketResult(
  count = rs.getObject("count") as kotlin.Long?)
}

public class CheckDuplicateTicketQuery : Query<CheckDuplicateTicketParams,
    CheckDuplicateTicketResult> {
  public override val sql: String = """
      |SELECT COUNT(*) FROM tickets
      |WHERE showId=? AND seatNo=?;
      |""".trimMargin()

  public override val mapper: RowMapper<CheckDuplicateTicketResult> =
      CheckDuplicateTicketRowMapper()

  public override val paramSetter: ParamSetter<CheckDuplicateTicketParams> =
      CheckDuplicateTicketParamSetter()
}
