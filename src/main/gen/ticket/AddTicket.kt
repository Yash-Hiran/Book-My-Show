package ticket

import java.sql.PreparedStatement
import kotlin.Int
import kotlin.String
import kotlin.Unit
import norm.Command
import norm.ParamSetter

public data class AddTicketParams(
  public val showId: Int?,
  public val customerId: Int?,
  public val seatNo: Int?
)

public class AddTicketParamSetter : ParamSetter<AddTicketParams> {
  public override fun map(ps: PreparedStatement, params: AddTicketParams): Unit {
    ps.setObject(1, params.showId)
    ps.setObject(2, params.customerId)
    ps.setObject(3, params.seatNo)
  }
}

public class AddTicketCommand : Command<AddTicketParams> {
  public override val sql: String = """
      |INSERT INTO tickets(showId, customerId, seatNo)
      |VALUES (?, ?, ?);
      |""".trimMargin()

  public override val paramSetter: ParamSetter<AddTicketParams> = AddTicketParamSetter()
}
