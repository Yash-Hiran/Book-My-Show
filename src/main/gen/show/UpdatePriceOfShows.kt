package show

import java.sql.PreparedStatement
import kotlin.Int
import kotlin.String
import kotlin.Unit
import norm.Command
import norm.ParamSetter

public data class UpdatePriceOfShowsParams(
  public val price: Int?,
  public val id: Int?
)

public class UpdatePriceOfShowsParamSetter : ParamSetter<UpdatePriceOfShowsParams> {
  public override fun map(ps: PreparedStatement, params: UpdatePriceOfShowsParams): Unit {
    ps.setObject(1, params.price)
    ps.setObject(2, params.id)
  }
}

public class UpdatePriceOfShowsCommand : Command<UpdatePriceOfShowsParams> {
  public override val sql: String = """
      |UPDATE shows SET price=? WHERE id=?
      |""".trimMargin()

  public override val paramSetter: ParamSetter<UpdatePriceOfShowsParams> =
      UpdatePriceOfShowsParamSetter()
}
