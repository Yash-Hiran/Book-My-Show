package authentication

import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlin.String
import kotlin.Unit
import norm.ParamSetter
import norm.Query
import norm.RowMapper

public data class CheckUserParams(
  public val name: String?,
  public val password: String?
)

public class CheckUserParamSetter : ParamSetter<CheckUserParams> {
  public override fun map(ps: PreparedStatement, params: CheckUserParams): Unit {
    ps.setObject(1, params.name)
    ps.setObject(2, params.password)
  }
}

public data class CheckUserResult(
  public val name: String,
  public val password: String
)

public class CheckUserRowMapper : RowMapper<CheckUserResult> {
  public override fun map(rs: ResultSet): CheckUserResult = CheckUserResult(
  name = rs.getObject("name") as kotlin.String,
    password = rs.getObject("password") as kotlin.String)
}

public class CheckUserQuery : Query<CheckUserParams, CheckUserResult> {
  public override val sql: String = """
      |SELECT name, password FROM users
      |WHERE name = ? AND password = ?
      |""".trimMargin()

  public override val mapper: RowMapper<CheckUserResult> = CheckUserRowMapper()

  public override val paramSetter: ParamSetter<CheckUserParams> = CheckUserParamSetter()
}
