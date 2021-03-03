package com.cuupa.classificator.monitor.sqlite

import org.hibernate.dialect.Dialect
import org.hibernate.dialect.function.SQLFunctionTemplate
import org.hibernate.dialect.function.StandardSQLFunction
import org.hibernate.dialect.function.VarArgsSQLFunction
import org.hibernate.type.StringType
import java.sql.Types


class SqliteDialect : Dialect() {

    init {
        registerTypes()
        registerFunctions()
    }

    private fun registerTypes() {
        registerColumnType(Types.BIT, "integer")
        registerColumnType(Types.TINYINT, "tinyint")
        registerColumnType(Types.SMALLINT, "smallint")
        registerColumnType(Types.INTEGER, "integer")
        registerColumnType(Types.BIGINT, "bigint")
        registerColumnType(Types.FLOAT, "float")
        registerColumnType(Types.REAL, "real")
        registerColumnType(Types.DOUBLE, "double")
        registerColumnType(Types.NUMERIC, "numeric")
        registerColumnType(Types.DECIMAL, "decimal")
        registerColumnType(Types.CHAR, "char")
        registerColumnType(Types.VARCHAR, "varchar")
        registerColumnType(Types.LONGVARCHAR, "longvarchar")
        registerColumnType(Types.DATE, "date")
        registerColumnType(Types.TIME, "time")
        registerColumnType(Types.TIMESTAMP, "timestamp")
        registerColumnType(Types.BINARY, "blob")
        registerColumnType(Types.VARBINARY, "blob")
        registerColumnType(Types.LONGVARBINARY, "blob")
        registerColumnType(Types.BLOB, "blob")
        registerColumnType(Types.CLOB, "clob")
        registerColumnType(Types.BOOLEAN, "integer")

    }

    private fun registerFunctions() {
        registerFunction("concat", VarArgsSQLFunction(StringType.INSTANCE, "", "||", ""))
        registerFunction("mod", SQLFunctionTemplate(StringType.INSTANCE, "?1 % ?2"))
        registerFunction("substr", StandardSQLFunction("substr", StringType.INSTANCE))
        registerFunction("substring", StandardSQLFunction("substr", StringType.INSTANCE))
    }

    override fun hasAlterTable() = false

    override fun dropConstraints() = false

    override fun getDropForeignKeyString() = ""

    override fun getAddForeignKeyConstraintString(
        cn: String?,
        fk: Array<String?>?, t: String?, pk: Array<String?>?, rpk: Boolean
    ) = ""

    override fun getAddPrimaryKeyConstraintString(constraintName: String?) = ""

    fun supportsIdentityColumns() = true

    fun getIdentitySelectString(table: String?, column: String?, type: Int) = "select last_insert_rowid()"

    fun getIdentityColumnString(type: Int) = "integer"
}