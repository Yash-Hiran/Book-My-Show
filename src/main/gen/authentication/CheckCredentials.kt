package authentication

import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlin.String
import kotlin.Unit
import norm.ParamSetter
import norm.Query
import norm.RowMapper

public data class CheckCredentialsParams(
  public val username: String?,
  public val password: String?
)

public class CheckCredentialsParamSetter : ParamSetter<CheckCredentialsParams> {
  public override fun map(ps: PreparedStatement, params: CheckCredentialsParams): Unit {
    ps.setObject(1, params.username)
    ps.setObject(2, params.password)
  }
}

public data class CheckCredentialsResult(
  public val username: String
)

public class CheckCredentialsRowMapper : RowMapper<CheckCredentialsResult> {
  public override fun map(rs: ResultSet): CheckCredentialsResult = CheckCredentialsResult(
  username = rs.getObject("username") as kotlin.String)
}

public class CheckCredentialsQuery : Query<CheckCredentialsParams, CheckCredentialsResult> {
  public override val sql: String = """
      |SELECT username FROM users
      |WHERE username = ? AND password = CRYPT(?, password);
      |""".trimMargin()

  public override val mapper: RowMapper<CheckCredentialsResult> = CheckCredentialsRowMapper()

  public override val paramSetter: ParamSetter<CheckCredentialsParams> =
      CheckCredentialsParamSetter()
}
