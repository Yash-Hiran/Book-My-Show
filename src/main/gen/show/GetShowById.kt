package show

import java.sql.Date
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import kotlin.Int
import kotlin.String
import kotlin.Unit
import norm.ParamSetter
import norm.Query
import norm.RowMapper

public data class GetShowByIdParams(
  public val showId: Int?
)

public class GetShowByIdParamSetter : ParamSetter<GetShowByIdParams> {
  public override fun map(ps: PreparedStatement, params: GetShowByIdParams): Unit {
    ps.setObject(1, params.showId)
  }
}

public data class GetShowByIdResult(
  public val id: Int,
  public val movieId: Int,
  public val showDate: Date,
  public val startTime: Timestamp,
  public val endTime: Timestamp
)

public class GetShowByIdRowMapper : RowMapper<GetShowByIdResult> {
  public override fun map(rs: ResultSet): GetShowByIdResult = GetShowByIdResult(
  id = rs.getObject("id") as kotlin.Int,
    movieId = rs.getObject("movie_id") as kotlin.Int,
    showDate = rs.getObject("show_date") as java.sql.Date,
    startTime = rs.getObject("start_time") as java.sql.Timestamp,
    endTime = rs.getObject("end_time") as java.sql.Timestamp)
}

public class GetShowByIdQuery : Query<GetShowByIdParams, GetShowByIdResult> {
  public override val sql: String = """
      |SELECT * FROM shows
      |WHERE id=?;
      |""".trimMargin()

  public override val mapper: RowMapper<GetShowByIdResult> = GetShowByIdRowMapper()

  public override val paramSetter: ParamSetter<GetShowByIdParams> = GetShowByIdParamSetter()
}
