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

public data class GetShowByShowidParams(
  public val showId: Int?
)

public class GetShowByShowidParamSetter : ParamSetter<GetShowByShowidParams> {
  public override fun map(ps: PreparedStatement, params: GetShowByShowidParams): Unit {
    ps.setObject(1, params.showId)
  }
}

public data class GetShowByShowidResult(
  public val id: Int,
  public val movieId: Int,
  public val showDate: Date,
  public val startTime: Timestamp,
  public val endTime: Timestamp,
  public val capacity: Int,
  public val price: Int
)

public class GetShowByShowidRowMapper : RowMapper<GetShowByShowidResult> {
  public override fun map(rs: ResultSet): GetShowByShowidResult = GetShowByShowidResult(
  id = rs.getObject("id") as kotlin.Int,
    movieId = rs.getObject("movie_id") as kotlin.Int,
    showDate = rs.getObject("show_date") as java.sql.Date,
    startTime = rs.getObject("start_time") as java.sql.Timestamp,
    endTime = rs.getObject("end_time") as java.sql.Timestamp,
    capacity = rs.getObject("capacity") as kotlin.Int,
    price = rs.getObject("price") as kotlin.Int)
}

public class GetShowByShowidQuery : Query<GetShowByShowidParams, GetShowByShowidResult> {
  public override val sql: String = """
      |select * from shows where id=?
      |""".trimMargin()

  public override val mapper: RowMapper<GetShowByShowidResult> = GetShowByShowidRowMapper()

  public override val paramSetter: ParamSetter<GetShowByShowidParams> = GetShowByShowidParamSetter()
}
