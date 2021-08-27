package ticket

import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlin.Int
import kotlin.String
import kotlin.Unit
import norm.ParamSetter
import norm.Query
import norm.RowMapper

public data class SaveTicketParams(
  public val showId: Int?,
  public val seatNo: Int?,
  public val phoneNo: Int?
)

public class SaveTicketParamSetter : ParamSetter<SaveTicketParams> {
  public override fun map(ps: PreparedStatement, params: SaveTicketParams): Unit {
    ps.setObject(1, params.showId)
    ps.setObject(2, params.seatNo)
    ps.setObject(3, params.phoneNo)
  }
}

public data class SaveTicketResult(
  public val ticketid: Int,
  public val showid: Int,
  public val seatno: Int,
  public val phoneno: Int
)

public class SaveTicketRowMapper : RowMapper<SaveTicketResult> {
  public override fun map(rs: ResultSet): SaveTicketResult = SaveTicketResult(
  ticketid = rs.getObject("ticketid") as kotlin.Int,
    showid = rs.getObject("showid") as kotlin.Int,
    seatno = rs.getObject("seatno") as kotlin.Int,
    phoneno = rs.getObject("phoneno") as kotlin.Int)
}

public class SaveTicketQuery : Query<SaveTicketParams, SaveTicketResult> {
  public override val sql: String = """
      |INSERT INTO tickets(showId, seatNo, phoneNo)
      |VALUES (?, ?, ?)
      |RETURNING *;
      |""".trimMargin()

  public override val mapper: RowMapper<SaveTicketResult> = SaveTicketRowMapper()

  public override val paramSetter: ParamSetter<SaveTicketParams> = SaveTicketParamSetter()
}
