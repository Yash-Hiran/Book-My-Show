package show

import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlin.Int
import kotlin.String
import kotlin.Unit
import norm.ParamSetter
import norm.Query
import norm.RowMapper

public data class GetShowCapacityParams(
  public val id: Int?
)

public class GetShowCapacityParamSetter : ParamSetter<GetShowCapacityParams> {
  public override fun map(ps: PreparedStatement, params: GetShowCapacityParams): Unit {
    ps.setObject(1, params.id)
  }
}

public data class GetShowCapacityResult(
  public val capacity: Int
)

public class GetShowCapacityRowMapper : RowMapper<GetShowCapacityResult> {
  public override fun map(rs: ResultSet): GetShowCapacityResult = GetShowCapacityResult(
  capacity = rs.getObject("capacity") as kotlin.Int)
}

public class GetShowCapacityQuery : Query<GetShowCapacityParams, GetShowCapacityResult> {
  public override val sql: String = """
      |SELECT capacity FROM shows
      |WHERE id = ?;
      |
      |
      |""".trimMargin()

  public override val mapper: RowMapper<GetShowCapacityResult> = GetShowCapacityRowMapper()

  public override val paramSetter: ParamSetter<GetShowCapacityParams> = GetShowCapacityParamSetter()
}
