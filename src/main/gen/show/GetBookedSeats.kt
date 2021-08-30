package show

import java.sql.PreparedStatement
import kotlin.String
import kotlin.Unit
import norm.Command
import norm.ParamSetter

public class GetBookedSeatsParams

public class GetBookedSeatsParamSetter : ParamSetter<GetBookedSeatsParams> {
  public override fun map(ps: PreparedStatement, params: GetBookedSeatsParams): Unit {
  }
}

public class GetBookedSeatsCommand : Command<GetBookedSeatsParams> {
  public override val sql: String = ""

  public override val paramSetter: ParamSetter<GetBookedSeatsParams> = GetBookedSeatsParamSetter()
}
