package ticket

import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlin.Int
import kotlin.String
import kotlin.Unit
import norm.ParamSetter
import norm.Query
import norm.RowMapper

public data class GetTicketByIdParams(
  public val ticketId: Int?
)

public class GetTicketByIdParamSetter : ParamSetter<GetTicketByIdParams> {
  public override fun map(ps: PreparedStatement, params: GetTicketByIdParams): Unit {
    ps.setObject(1, params.ticketId)
  }
}

public data class GetTicketByIdResult(
  public val ticketid: Int,
  public val showid: Int,
  public val seatno: Int,
  public val phoneno: Int
)

public class GetTicketByIdRowMapper : RowMapper<GetTicketByIdResult> {
  public override fun map(rs: ResultSet): GetTicketByIdResult = GetTicketByIdResult(
  ticketid = rs.getObject("ticketid") as kotlin.Int,
    showid = rs.getObject("showid") as kotlin.Int,
    seatno = rs.getObject("seatno") as kotlin.Int,
    phoneno = rs.getObject("phoneno") as kotlin.Int)
}

public class GetTicketByIdQuery : Query<GetTicketByIdParams, GetTicketByIdResult> {
  public override val sql: String = """
      |SELECT * FROM tickets
      |WHERE ticketId=?;
      |""".trimMargin()

  public override val mapper: RowMapper<GetTicketByIdResult> = GetTicketByIdRowMapper()

  public override val paramSetter: ParamSetter<GetTicketByIdParams> = GetTicketByIdParamSetter()
}
