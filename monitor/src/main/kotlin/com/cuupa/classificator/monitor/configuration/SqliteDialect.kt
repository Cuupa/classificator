package com.cuupa.classificator.monitor.configuration

import org.hibernate.dialect.Dialect
import org.hibernate.dialect.function.NoArgSQLFunction
import org.hibernate.dialect.function.SQLFunctionTemplate
import org.hibernate.dialect.function.StandardSQLFunction
import org.hibernate.dialect.function.VarArgsSQLFunction
import org.hibernate.type.StandardBasicTypes
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
        registerFunction("concat", VarArgsSQLFunction(StandardBasicTypes.STRING, "", "||", ""))
        registerFunction("mod", SQLFunctionTemplate(StandardBasicTypes.INTEGER, "?1 % ?2"))
        registerFunction("quote", StandardSQLFunction("quote", StandardBasicTypes.STRING))
        registerFunction("random", NoArgSQLFunction("random", StandardBasicTypes.INTEGER))
        registerFunction("round", StandardSQLFunction("round"))
        registerFunction("substr", StandardSQLFunction("substr", StandardBasicTypes.STRING))
    }

    override fun hasAlterTable() = false

    override fun dropConstraints() = false

    override fun getDropForeignKeyString() = ""

    override fun getAddForeignKeyConstraintString(
        cn: String?,
        fk: Array<String?>?, t: String?, pk: Array<String?>?, rpk: Boolean
    ) = ""

    override fun getAddPrimaryKeyConstraintString(constraintName: String?) = ""

    override fun supportsLimit() = true

    override fun getLimitString(query: String, hasOffset: Boolean) =
        StringBuffer(query.length + 20)
            .append(query)
            .append(if (hasOffset) " limit ? offset ?" else " limit ?")
            .toString()

    override fun supportsCurrentTimestampSelection() = true

    override fun isCurrentTimestampSelectStringCallable() = false

    override fun getCurrentTimestampSelectString() = "select current_timestamp"

    override fun supportsUnionAll() = true

    override fun getAddColumnString() = "add column"

    override fun getForUpdateString() = ""

    override fun supportsOuterJoinForUpdate() = false

    override fun supportsIfExistsBeforeTableName() = true

    override fun supportsCascadeDelete() = false
}