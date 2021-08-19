package movie

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import kotlin.Int
import kotlin.String
import kotlin.Unit
import norm.ParamSetter
import norm.Query
import norm.RowMapper

public data class SaveMovieParams(
  public val title: String?,
  public val start_time: Timestamp?,
  public val end_time: Timestamp?
)

public class SaveMovieParamSetter : ParamSetter<SaveMovieParams> {
  public override fun map(ps: PreparedStatement, params: SaveMovieParams): Unit {
    ps.setObject(1, params.title)
    ps.setObject(2, params.start_time)
    ps.setObject(3, params.end_time)
  }
}

public data class SaveMovieResult(
  public val id: Int,
  public val title: String,
  public val startTime: Timestamp,
  public val endTime: Timestamp
)

public class SaveMovieRowMapper : RowMapper<SaveMovieResult> {
  public override fun map(rs: ResultSet): SaveMovieResult = SaveMovieResult(
  id = rs.getObject("id") as kotlin.Int,
    title = rs.getObject("title") as kotlin.String,
    startTime = rs.getObject("start_time") as java.sql.Timestamp,
    endTime = rs.getObject("end_time") as java.sql.Timestamp)
}

public class SaveMovieQuery : Query<SaveMovieParams, SaveMovieResult> {
  public override val sql: String = """
      |INSERT INTO movies(title, start_time, end_time)
      |VALUES (?, ?, ?)
      |returning *;
      |""".trimMargin()

  public override val mapper: RowMapper<SaveMovieResult> = SaveMovieRowMapper()

  public override val paramSetter: ParamSetter<SaveMovieParams> = SaveMovieParamSetter()
}
