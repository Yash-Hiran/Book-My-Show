package authentication

import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlin.String
import kotlin.Unit
import norm.ParamSetter
import norm.Query
import norm.RowMapper

public data class CheckUserParams(
  public val username: String?,
  public val password: String?
)

public class CheckUserParamSetter : ParamSetter<CheckUserParams> {
  public override fun map(ps: PreparedStatement, params: CheckUserParams): Unit {
    ps.setObject(1, params.username)
    ps.setObject(2, params.password)
  }
}

public data class CheckUserResult(
  public val username: String
)

public class CheckUserRowMapper : RowMapper<CheckUserResult> {
  public override fun map(rs: ResultSet): CheckUserResult = CheckUserResult(
  username = rs.getObject("username") as kotlin.String)
}

public class CheckUserQuery : Query<CheckUserParams, CheckUserResult> {
  public override val sql: String = """
      |SELECT username FROM users
      |WHERE username = ? AND password = CRYPT(?, password);
      |""".trimMargin()

  public override val mapper: RowMapper<CheckUserResult> = CheckUserRowMapper()

  public override val paramSetter: ParamSetter<CheckUserParams> = CheckUserParamSetter()
}
